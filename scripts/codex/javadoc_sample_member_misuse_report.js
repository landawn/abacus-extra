#!/usr/bin/env node

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: javadoc_sample_member_misuse_report.js <Java file> [Java file...]');
  process.exit(2);
}

const primitiveTypes = 'boolean|byte|char|short|int|long|float|double';
const arrayDecl = new RegExp(`\\b(?:final\\s+)?(?:${primitiveTypes}|[A-Z_$][\\w$]*(?:\\s*<[^;=]+>)?)\\s*\\[\\]\\s+([A-Za-z_$][\\w$]*)\\b`, 'g');
const collectionDecl = /\b(?:final\s+)?(?:List|ArrayList|LinkedList|Collection|Set|HashSet|LinkedHashSet|SortedSet|NavigableSet|Map|HashMap|LinkedHashMap|SortedMap|NavigableMap|Deque|ArrayDeque|Queue|PrimitiveList|BooleanList|CharList|ByteList|ShortList|IntList|LongList|FloatList|DoubleList)\b(?:\s*<[^;=]+>)?\s+([A-Za-z_$][\w$]*)\b/g;

const issues = [];

function codeOf(line) {
  return line.replace(/^\s*\*\s?/, '');
}

function escapeRegExp(value) {
  return value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function scanBlock(file, startLine, block) {
  const arrayVars = new Set();
  const collectionVars = new Set();

  for (const line of block) {
    const code = codeOf(line);
    arrayDecl.lastIndex = 0;
    collectionDecl.lastIndex = 0;

    let match;
    while ((match = arrayDecl.exec(code)) !== null) {
      arrayVars.add(match[1]);
    }

    while ((match = collectionDecl.exec(code)) !== null) {
      collectionVars.add(match[1]);
    }
  }

  for (let i = 0; i < block.length; i++) {
    const code = codeOf(block[i]);

    for (const varName of arrayVars) {
      if (new RegExp(`\\b${escapeRegExp(varName)}\\.size\\(\\)`).test(code)) {
        issues.push(`${file}:${startLine + i + 1}: array variable "${varName}" uses .size(): ${code.trim()}`);
      }
    }

    for (const varName of collectionVars) {
      if (new RegExp(`\\b${escapeRegExp(varName)}\\.length\\b`).test(code)) {
        issues.push(`${file}:${startLine + i + 1}: collection variable "${varName}" uses .length: ${code.trim()}`);
      }
    }
  }
}

for (const file of files) {
  const text = fs.readFileSync(file, 'utf8');
  const lines = text.split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inBlock = false;
  let blockStart = 0;
  let blockLines = [];

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];

    if (!activeJavadoc[i]) {
      inBlock = false;
      blockLines = [];
      continue;
    }

    if (!inBlock && line.includes('<pre>{@code')) {
      inBlock = true;
      blockStart = i;
      blockLines = [line];

      if (line.includes('}</pre>')) {
        scanBlock(file, blockStart, blockLines);
        inBlock = false;
        blockLines = [];
      }

      continue;
    }

    if (inBlock) {
      blockLines.push(line);

      if (line.includes('}</pre>')) {
        scanBlock(file, blockStart, blockLines);
        inBlock = false;
        blockLines = [];
      }
    }
  }
}

for (const issue of issues) {
  console.log(issue);
}

console.log(`MISUSE_COUNT ${issues.length}`);

if (issues.length > 0) {
  process.exitCode = 1;
}
