#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const repoRoot = process.cwd();
const testRoot = path.join(repoRoot, "src", "test", "java");
const args = new Set(process.argv.slice(2));
const matchArg = process.argv.slice(2).find((arg) => arg.startsWith("--match="));
const matchText = matchArg ? matchArg.slice("--match=".length) : "";
const uniqueFileArg = process.argv.slice(2).find((arg) => arg.startsWith("--unique-file="));
const uniqueFileText = uniqueFileArg ? uniqueFileArg.slice("--unique-file=".length) : "";
const defaultInclude = /Test\.java$/;
const defaultExclude = /[\\/]com[\\/]landawn[\\/]abacus[\\/][^\\/]+\.java$/;
const annotationNames = new Set(["Test", "ParameterizedTest", "RepeatedTest", "TestFactory", "TestTemplate"]);

function walk(dir, files = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      walk(fullPath, files);
    } else if (defaultInclude.test(entry.name) && !defaultExclude.test(fullPath)) {
      files.push(fullPath);
    }
  }
  return files;
}

function stripBlockComments(text) {
  return text.replace(/\/\*[\s\S]*?\*\//g, "");
}

function stripLineComments(text) {
  let out = "";
  let inString = false;
  let inChar = false;
  let escaped = false;

  for (let i = 0; i < text.length; i++) {
    const ch = text[i];
    const next = text[i + 1];

    if (escaped) {
      out += ch;
      escaped = false;
      continue;
    }

    if (ch === "\\") {
      out += ch;
      escaped = true;
      continue;
    }

    if (!inChar && ch === "\"") {
      inString = !inString;
      out += ch;
      continue;
    }

    if (!inString && ch === "'") {
      inChar = !inChar;
      out += ch;
      continue;
    }

    if (!inString && !inChar && ch === "/" && next === "/") {
      while (i < text.length && text[i] !== "\n") {
        i++;
      }
      out += "\n";
      continue;
    }

    out += ch;
  }

  return out;
}

function stripComments(text) {
  return stripLineComments(stripBlockComments(text));
}

function compressWhitespace(text) {
  return text.replace(/\s+/g, " ").trim();
}

function normalizeBody(body) {
  return compressWhitespace(stripComments(body))
    .replace(/\(\s+/g, "(")
    .replace(/\s+\)/g, ")")
    .replace(/\s*([{}[\]();,:<>+\-*/%!=&|?])\s*/g, "$1");
}

function findMatchingBrace(text, startIndex) {
  let depth = 0;
  let inString = false;
  let inChar = false;
  let escaped = false;

  for (let i = startIndex; i < text.length; i++) {
    const ch = text[i];

    if (escaped) {
      escaped = false;
      continue;
    }

    if (ch === "\\") {
      escaped = true;
      continue;
    }

    if (!inChar && ch === "\"") {
      inString = !inString;
      continue;
    }

    if (!inString && ch === "'") {
      inChar = !inChar;
      continue;
    }

    if (inString || inChar) {
      continue;
    }

    if (ch === "{") {
      depth++;
    } else if (ch === "}") {
      depth--;
      if (depth === 0) {
        return i;
      }
    }
  }

  return -1;
}

function getLineNumber(text, index) {
  let line = 1;
  for (let i = 0; i < index; i++) {
    if (text[i] === "\n") {
      line++;
    }
  }
  return line;
}

function parseMethods(filePath) {
  const raw = fs.readFileSync(filePath, "utf8");
  const text = stripBlockComments(raw);
  const methods = [];
  let idx = 0;

  while (idx < text.length) {
    const atIndex = text.indexOf("@", idx);
    if (atIndex === -1) {
      break;
    }

    const nameMatch = /^@([A-Za-z_][A-Za-z0-9_]*)/.exec(text.slice(atIndex));
    if (!nameMatch) {
      idx = atIndex + 1;
      continue;
    }

    const annotationName = nameMatch[1];
    if (!annotationNames.has(annotationName)) {
      idx = atIndex + 1;
      continue;
    }

    let searchIndex = atIndex + nameMatch[0].length;
    while (searchIndex < text.length && /\s/.test(text[searchIndex])) {
      searchIndex++;
    }

    if (text[searchIndex] === "(") {
      let depth = 1;
      searchIndex++;
      while (searchIndex < text.length && depth > 0) {
        if (text[searchIndex] === "(") {
          depth++;
        } else if (text[searchIndex] === ")") {
          depth--;
        }
        searchIndex++;
      }
    }

    let blockStart = searchIndex;
    while (blockStart < text.length && /\s/.test(text[blockStart])) {
      blockStart++;
    }

    while (text[blockStart] === "@") {
      const extra = /^@([A-Za-z_][A-Za-z0-9_]*)/.exec(text.slice(blockStart));
      if (!extra) {
        break;
      }
      blockStart += extra[0].length;
      while (blockStart < text.length && /\s/.test(text[blockStart])) {
        blockStart++;
      }
      if (text[blockStart] === "(") {
        let depth = 1;
        blockStart++;
        while (blockStart < text.length && depth > 0) {
          if (text[blockStart] === "(") {
            depth++;
          } else if (text[blockStart] === ")") {
            depth--;
          }
          blockStart++;
        }
      }
      while (blockStart < text.length && /\s/.test(text[blockStart])) {
        blockStart++;
      }
    }

    const methodMatch = /^(?:public|protected|private)?\s*(?:static\s+)?(?:final\s+)?(?:synchronized\s+)?(?:<[^>]+>\s+)?[\w.$<>\[\], ?]+\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(/.exec(
      text.slice(blockStart),
    );

    if (!methodMatch) {
      idx = atIndex + 1;
      continue;
    }

    const methodName = methodMatch[1];
    const signatureStart = blockStart;
    const parenIndex = text.indexOf("(", signatureStart + methodMatch[0].indexOf(methodName) + methodName.length);
    const braceIndex = text.indexOf("{", parenIndex);
    if (braceIndex === -1) {
      idx = atIndex + 1;
      continue;
    }
    const closeBraceIndex = findMatchingBrace(text, braceIndex);
    if (closeBraceIndex === -1) {
      idx = atIndex + 1;
      continue;
    }

    const methodSource = text.slice(signatureStart, closeBraceIndex + 1);
    const bodySource = text.slice(braceIndex + 1, closeBraceIndex);
    methods.push({
      filePath,
      methodName,
      annotationName,
      startLine: getLineNumber(text, signatureStart),
      source: methodSource,
      normalized: normalizeBody(bodySource),
      body: compressWhitespace(stripComments(bodySource)),
    });
    idx = closeBraceIndex + 1;
  }

  return methods;
}

function groupByNormalized(methods) {
  const groups = new Map();

  for (const method of methods) {
    if (!method.normalized) {
      continue;
    }
    const key = `${method.annotationName}:${method.normalized}`;
    const bucket = groups.get(key) || [];
    bucket.push(method);
    groups.set(key, bucket);
  }

  return [...groups.values()]
    .filter((bucket) => bucket.length > 1)
    .sort((a, b) => {
      const fileCompare = a[0].filePath.localeCompare(b[0].filePath);
      if (fileCompare !== 0) {
        return fileCompare;
      }
      return a[0].startLine - b[0].startLine;
    });
}

function summarize(groups) {
  return groups.map((group) => ({
    normalizedLength: group[0].normalized.length,
    methods: group.map((method) => ({
      file: path.relative(repoRoot, method.filePath).replace(/\\/g, "/"),
      line: method.startLine,
      method: method.methodName,
      annotation: method.annotationName,
    })),
  }));
}

function filterGroups(groups) {
  if (!matchText) {
    return groups;
  }

  return groups.filter((group) =>
    group.some((method) => {
      const file = path.relative(repoRoot, method.filePath).replace(/\\/g, "/");
      return file.includes(matchText) || method.methodName.includes(matchText);
    }),
  );
}

const files = walk(testRoot);
const methods = files.flatMap(parseMethods);
const duplicateGroups = filterGroups(groupByNormalized(methods));
const duplicateKeys = new Set(duplicateGroups.flatMap((group) => group.map((method) => `${method.filePath}:${method.startLine}`)));

const report = {
  scannedFiles: files.length,
  scannedMethods: methods.length,
  duplicateGroupCount: duplicateGroups.length,
  duplicateMethodCount: duplicateGroups.reduce((sum, group) => sum + group.length, 0),
  groups: summarize(duplicateGroups),
};

if (args.has("--summary-by-file")) {
  const summaryByFile = new Map();

  for (const method of methods) {
    const key = path.relative(repoRoot, method.filePath).replace(/\\/g, "/");
    const entry = summaryByFile.get(key) || { file: key, totalMethods: 0, duplicateMethods: 0, duplicateGroups: 0 };
    entry.totalMethods += 1;
    summaryByFile.set(key, entry);
  }

  for (const group of duplicateGroups) {
    for (const method of group) {
      const key = path.relative(repoRoot, method.filePath).replace(/\\/g, "/");
      const entry = summaryByFile.get(key) || { file: key, totalMethods: 0, duplicateMethods: 0, duplicateGroups: 0 };
      entry.duplicateMethods += 1;
      entry.duplicateGroups += 1;
      summaryByFile.set(key, entry);
    }
  }

  const summary = [...summaryByFile.values()].map((entry) => ({
    ...entry,
    duplicateRatio: entry.totalMethods === 0 ? 0 : Number((entry.duplicateMethods / entry.totalMethods).toFixed(3)),
  })).sort((a, b) => {
    if (b.duplicateMethods !== a.duplicateMethods) {
      return b.duplicateMethods - a.duplicateMethods;
    }
    return a.file.localeCompare(b.file);
  });

  console.log(JSON.stringify({
    scannedFiles: files.length,
    scannedMethods: methods.length,
    duplicateGroupCount: duplicateGroups.length,
    files: summary,
  }, null, 2));
} else if (uniqueFileText) {
  const selectedMethods = methods
    .filter((method) => {
      const file = path.relative(repoRoot, method.filePath).replace(/\\/g, "/");
      return file.includes(uniqueFileText);
    })
    .map((method) => ({
      file: path.relative(repoRoot, method.filePath).replace(/\\/g, "/"),
      line: method.startLine,
      method: method.methodName,
      annotation: method.annotationName,
      isDuplicate: duplicateKeys.has(`${method.filePath}:${method.startLine}`),
    }));

  console.log(JSON.stringify({
    fileMatch: uniqueFileText,
    methods: selectedMethods.filter((method) => !method.isDuplicate),
    duplicateMethods: selectedMethods.filter((method) => method.isDuplicate),
  }, null, 2));
} else {
  console.log(JSON.stringify(report, null, 2));
}
