#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const repoRoot = process.cwd();
const testRoot = path.join(repoRoot, "src", "test", "java");
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

function stripComments(text) {
  return text
    .replace(/\/\*[\s\S]*?\*\//g, "")
    .replace(/\/\/[^\n\r]*/g, "");
}

function extractTests(relativePath, raw) {
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

    const methodMatch = /(?:public|protected|private)?\s*(?:static\s+)?(?:final\s+)?(?:synchronized\s+)?(?:<[^>]+>\s+)?[\w.$<>\[\], ?]+\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(/.exec(
      raw.slice(atIndex),
    );

    if (!methodMatch) {
      index = atIndex + 1;
      continue;
    }

    const methodName = methodMatch[1];
    const signatureIndex = raw.indexOf(methodMatch[0], atIndex);
    const braceIndex = raw.indexOf("{", signatureIndex);
    const closeBraceIndex = findMatchingBrace(raw, braceIndex);

    const line = raw.slice(0, signatureIndex).split("\n").length;
    methods.push({
      relativePath,
      methodName,
      line,
      body: raw.slice(braceIndex + 1, closeBraceIndex),
    });

    index = closeBraceIndex + 1;
  }

  return methods;
}

function hasAssertion(body) {
  const cleaned = stripComments(body);
  return /\bassert[A-Z][A-Za-z0-9_]*\s*\(/.test(cleaned) || /\bAssertions\.[A-Za-z0-9_]+\s*\(/.test(cleaned);
}

function hasTryCatch(body) {
  return /\btry\s*\{[\s\S]*?\}\s*catch\s*\(/.test(stripComments(body));
}

const report = {
  withoutAssertions: [],
  withTryCatch: [],
};

for (const filePath of walk(testRoot)) {
  const relativePath = path.relative(repoRoot, filePath).replace(/\\/g, "/");
  const raw = fs.readFileSync(filePath, "utf8").replace(/\r\n/g, "\n");

  for (const test of extractTests(relativePath, raw)) {
    if (!hasAssertion(test.body)) {
      report.withoutAssertions.push(test);
    }
    if (hasTryCatch(test.body)) {
      report.withTryCatch.push(test);
    }
  }
}

console.log(JSON.stringify(report, null, 2));
