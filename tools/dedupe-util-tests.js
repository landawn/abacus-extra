#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const repoRoot = process.cwd();
const testRoot = path.join(repoRoot, "src", "test", "java", "com", "landawn", "abacus", "util");
const applyChanges = process.argv.includes("--apply");
const summaryOnly = process.argv.includes("--summary-only");
const fileArg = process.argv.slice(2).find((arg) => arg.startsWith("--match="));
const fileMatch = fileArg ? fileArg.slice("--match=".length) : "";
const testAnnotations = new Set(["Test", "ParameterizedTest", "RepeatedTest", "TestFactory", "TestTemplate"]);

function walk(dir, files = []) {
  if (!fs.existsSync(dir)) {
    return files;
  }

  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      walk(fullPath, files);
    } else if (entry.name.endsWith(".java")) {
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

function normalizeText(text) {
  return stripComments(text)
    .replace(/\s+/g, " ")
    .trim()
    .replace(/\(\s+/g, "(")
    .replace(/\s+\)/g, ")")
    .replace(/\s*([{}[\]();,:<>+\-*/%!=&|?])\s*/g, "$1");
}

function countLine(text, index) {
  let line = 1;

  for (let i = 0; i < index; i++) {
    if (text[i] === "\n") {
      line++;
    }
  }

  return line;
}

function findMatchingBrace(text, startIndex) {
  let depth = 0;
  let inString = false;
  let inChar = false;
  let inLineComment = false;
  let inBlockComment = false;
  let escaped = false;

  for (let i = startIndex; i < text.length; i++) {
    const ch = text[i];
    const next = text[i + 1];

    if (inLineComment) {
      if (ch === "\n") {
        inLineComment = false;
      }
      continue;
    }

    if (inBlockComment) {
      if (ch === "*" && next === "/") {
        inBlockComment = false;
        i++;
      }
      continue;
    }

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

    if (ch === "/" && next === "/") {
      inLineComment = true;
      i++;
      continue;
    }

    if (ch === "/" && next === "*") {
      inBlockComment = true;
      i++;
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

function leadingBlockStart(text, index, minIndent = 0) {
  let lineStart = text.lastIndexOf("\n", index - 1);
  lineStart = lineStart === -1 ? 0 : lineStart + 1;

  while (lineStart > 0) {
    const previousBreak = text.lastIndexOf("\n", lineStart - 2);
    const candidateStart = previousBreak === -1 ? 0 : previousBreak + 1;
    const line = text.slice(candidateStart, lineStart).replace(/\r/g, "");
    const trimmed = line.trim();
    const indent = line.match(/^[ \t]*/)[0].length;

    if (trimmed === "" || trimmed.startsWith("//") || trimmed.startsWith("/*") || trimmed.startsWith("*") || (trimmed.startsWith("@") && indent >= minIndent)) {
      lineStart = candidateStart;
      continue;
    }

    break;
  }

  return lineStart;
}

function parseNestedClasses(text) {
  const classes = [];
  const classPattern = /^[ \t]*(?:public|protected|private)?\s*(?:static\s+)?(?:final\s+)?class\s+([A-Za-z_][A-Za-z0-9_]*)[^{]*\{/gm;
  let match;

  while ((match = classPattern.exec(text)) !== null) {
    const nameIndex = match.index;
    const lineStart = text.lastIndexOf("\n", nameIndex - 1);
    const declarationStart = lineStart === -1 ? 0 : lineStart + 1;
    const indent = text.slice(declarationStart, nameIndex).match(/^[ \t]*/)[0].length;
    const braceIndex = match.index + match[0].lastIndexOf("{");

    if (braceIndex === -1) {
      continue;
    }

    const closeBraceIndex = findMatchingBrace(text, braceIndex);
    if (closeBraceIndex === -1) {
      continue;
    }

    const depth = classes.filter((entry) => entry.braceIndex < braceIndex && entry.closeBraceIndex > closeBraceIndex).length;
    classes.push({
      name: match[1],
      indent,
      declarationStart,
      start: leadingBlockStart(text, declarationStart, indent),
      braceIndex,
      closeBraceIndex,
      depth,
    });
  }

  return classes;
}

function methodNameScore(methodName) {
  const simplified = methodName
    .replace(/^test_?/i, "")
    .replace(/[A-Z]/g, " $&")
    .replace(/_/g, " ")
    .trim();

  return simplified.length + simplified.split(/\s+/).filter(Boolean).length * 10;
}

function tokenizeName(methodName) {
  return methodName
    .replace(/^test_?/i, "")
    .replace(/([a-z0-9])([A-Z])/g, "$1 $2")
    .replace(/_/g, " ")
    .split(/\s+/)
    .map((token) => token.trim().toLowerCase())
    .map((token) => {
      if (token === "cols" || token === "col") {
        return "column";
      }
      if (token === "rows") {
        return "row";
      }
      if (token.endsWith("ies") && token.length > 4) {
        return `${token.slice(0, -3)}y`;
      }
      if (token.endsWith("s") && token.length > 3) {
        return token.slice(0, -1);
      }
      return token;
    })
    .filter(Boolean);
}

function identifierOverlapScore(methodName, bodySource) {
  const nameTokens = new Set(tokenizeName(methodName));
  if (nameTokens.size === 0) {
    return 0;
  }

  const identifiers = new Set(
    stripComments(bodySource)
      .replace(/([a-z0-9])([A-Z])/g, "$1 $2")
      .match(/\b[A-Za-z_][A-Za-z0-9_]*\b/g) || [],
  );
  let score = 0;

  for (const identifier of identifiers) {
    const normalized = identifier.toLowerCase();
    if (nameTokens.has(normalized)) {
      score += 3;
      continue;
    }

    for (const token of tokenizeName(normalized)) {
      if (nameTokens.has(token)) {
        score += 1;
      }
    }
  }

  return score;
}

function chooseKeeper(methods) {
  return methods
    .slice()
    .sort((a, b) => {
      if (b.overlapScore !== a.overlapScore) {
        return b.overlapScore - a.overlapScore;
      }
      if (b.annotationScore !== a.annotationScore) {
        return b.annotationScore - a.annotationScore;
      }
      if (b.source.length !== a.source.length) {
        return b.source.length - a.source.length;
      }
      if (b.nameScore !== a.nameScore) {
        return b.nameScore - a.nameScore;
      }
      if (a.depth !== b.depth) {
        return a.depth - b.depth;
      }
      return a.start - b.start;
    })[0];
}

function parseMethods(filePath) {
  const raw = fs.readFileSync(filePath, "utf8").replace(/\r\n/g, "\n");
  const classes = parseNestedClasses(raw);
  const methods = [];
  let index = 0;

  while (index < raw.length) {
    const atIndex = raw.indexOf("@", index);
    if (atIndex === -1) {
      break;
    }

    const annotationMatch = /^@([A-Za-z_][A-Za-z0-9_]*)/.exec(raw.slice(atIndex));
    if (!annotationMatch || !testAnnotations.has(annotationMatch[1])) {
      index = atIndex + 1;
      continue;
    }

    let annotationEnd = atIndex;

    while (annotationEnd < raw.length) {
      const currentAnnotation = /^@([A-Za-z_][A-Za-z0-9_]*)/.exec(raw.slice(annotationEnd));
      if (!currentAnnotation) {
        break;
      }

      annotationEnd += currentAnnotation[0].length;

      while (annotationEnd < raw.length && /\s/.test(raw[annotationEnd])) {
        annotationEnd++;
      }

      if (raw[annotationEnd] === "(") {
        let depth = 1;
        annotationEnd++;

        while (annotationEnd < raw.length && depth > 0) {
          if (raw[annotationEnd] === "(") {
            depth++;
          } else if (raw[annotationEnd] === ")") {
            depth--;
          }
          annotationEnd++;
        }
      }

      const newlineMatch = /^[ \t]*(?:\r?\n)/.exec(raw.slice(annotationEnd));
      if (!newlineMatch) {
        break;
      }
      annotationEnd += newlineMatch[0].length;

      while (raw[annotationEnd] === " " || raw[annotationEnd] === "\t") {
        annotationEnd++;
      }
    }

    const methodMatch = /(?:public|protected|private)?\s*(?:static\s+)?(?:final\s+)?(?:synchronized\s+)?(?:<[^>]+>\s+)?[\w.$<>\[\], ?]+\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(/.exec(
      raw.slice(atIndex),
    );

    if (!methodMatch) {
      index = atIndex + 1;
      continue;
    }

    const signatureIndex = raw.indexOf(methodMatch[0], atIndex);
    const braceIndex = raw.indexOf("{", signatureIndex);
    const closeBraceIndex = findMatchingBrace(raw, braceIndex);

    if (braceIndex === -1 || closeBraceIndex === -1) {
      index = atIndex + 1;
      continue;
    }

    const methodIndent = raw.slice(raw.lastIndexOf("\n", signatureIndex - 1) + 1, signatureIndex).match(/^[ \t]*/)[0].length;
    const start = leadingBlockStart(raw, atIndex, methodIndent);
    let end = closeBraceIndex + 1;
    while (raw[end] === "\n") {
      end++;
    }

    const bodySource = raw.slice(braceIndex + 1, closeBraceIndex);
    const annotationSource = raw.slice(atIndex, signatureIndex);
    const enclosingClass = classes
      .filter((entry) => entry.braceIndex < braceIndex && entry.closeBraceIndex > closeBraceIndex)
      .sort((a, b) => b.depth - a.depth)[0];

    methods.push({
      filePath,
      methodName: methodMatch[1],
      start,
      end,
      source: raw.slice(start, end),
      line: countLine(raw, signatureIndex),
      annotationKey: normalizeText(annotationSource),
      annotationScore: stripComments(annotationSource)
        .split(/\r?\n/)
        .filter((line) => line.trim().startsWith("@")).length,
      normalizedBody: normalizeText(bodySource),
      depth: enclosingClass ? enclosingClass.depth : 0,
      className: enclosingClass ? enclosingClass.name : path.basename(filePath, ".java"),
      nameScore: methodNameScore(methodMatch[1]),
      overlapScore: identifierOverlapScore(methodMatch[1], bodySource),
    });

    index = closeBraceIndex + 1;
  }

  return { raw, classes, methods };
}

function removeRanges(text, ranges) {
  let output = text;
  for (const range of ranges.sort((a, b) => b.start - a.start)) {
    output = output.slice(0, range.start) + output.slice(range.end);
  }

  return output;
}

function removeEmptyNestedClasses(text) {
  let output = text;
  let changed = false;

  while (true) {
    const classes = parseNestedClasses(output)
      .filter((entry) => entry.depth > 0)
      .sort((a, b) => b.start - a.start);
    let removedInPass = false;

    for (const entry of classes) {
      const body = output.slice(entry.braceIndex + 1, entry.closeBraceIndex);
      if (stripComments(body).trim() !== "") {
        continue;
      }

      let end = entry.closeBraceIndex + 1;
      while (output[end] === "\n") {
        end++;
      }

      output = output.slice(0, entry.start) + output.slice(end);
      removedInPass = true;
      changed = true;
    }

    if (!removedInPass) {
      break;
    }
  }

  return { text: output, changed };
}

function collapseBlankLines(text) {
  return text
    .replace(/[ \t]+\n/g, "\n")
    .replace(/\n{3,}/g, "\n\n")
    .trimEnd() + "\n";
}

function dedupeFile(filePath) {
  const { raw, methods } = parseMethods(filePath);
  const groups = new Map();

  for (const method of methods) {
    if (!method.normalizedBody) {
      continue;
    }

    const key = `${method.annotationKey}::${method.normalizedBody}`;
    const bucket = groups.get(key) || [];
    bucket.push(method);
    groups.set(key, bucket);
  }

  const removals = [];
  const duplicates = [];

  for (const methodsInGroup of groups.values()) {
    if (methodsInGroup.length <= 1) {
      continue;
    }

    const keeper = chooseKeeper(methodsInGroup);
    const removed = methodsInGroup
      .filter((entry) => entry !== keeper)
      .sort((a, b) => a.start - b.start);

    if (removed.length === 0) {
      continue;
    }

    duplicates.push({
      keeper: {
        className: keeper.className,
        methodName: keeper.methodName,
        line: keeper.line,
      },
      removed: removed.map((entry) => ({
        className: entry.className,
        methodName: entry.methodName,
        line: entry.line,
      })),
    });
    removals.push(...removed.map((entry) => ({ start: entry.start, end: entry.end })));
  }

  if (removals.length === 0) {
    return {
      file: path.relative(repoRoot, filePath).replace(/\\/g, "/"),
      duplicateGroupCount: 0,
      removedMethodCount: 0,
      changed: false,
      duplicates: [],
    };
  }

  let updated = removeRanges(raw, removals);
  const classCleanup = removeEmptyNestedClasses(updated);
  updated = collapseBlankLines(classCleanup.text);

  if (applyChanges) {
    fs.writeFileSync(filePath, updated);
  }

  return {
    file: path.relative(repoRoot, filePath).replace(/\\/g, "/"),
    duplicateGroupCount: duplicates.length,
    removedMethodCount: removals.length,
    removedEmptyNestedClasses: classCleanup.changed,
    changed: updated !== raw,
    duplicates,
  };
}

const report = walk(testRoot)
  .sort()
  .filter((filePath) => !fileMatch || path.relative(repoRoot, filePath).replace(/\\/g, "/").includes(fileMatch))
  .map(dedupeFile)
  .filter((entry) => entry.duplicateGroupCount > 0);

const summary = {
  applyChanges,
  fileMatch: fileMatch || null,
  fileCount: walk(testRoot).filter((filePath) => !fileMatch || path.relative(repoRoot, filePath).replace(/\\/g, "/").includes(fileMatch)).length,
  affectedFiles: report.length,
  duplicateGroupCount: report.reduce((sum, entry) => sum + entry.duplicateGroupCount, 0),
  removedMethodCount: report.reduce((sum, entry) => sum + entry.removedMethodCount, 0),
  files: summaryOnly
    ? report.map((entry) => ({
        file: entry.file,
        duplicateGroupCount: entry.duplicateGroupCount,
        removedMethodCount: entry.removedMethodCount,
        removedEmptyNestedClasses: entry.removedEmptyNestedClasses,
      }))
    : report,
};

console.log(JSON.stringify(summary, null, 2));
