#!/usr/bin/env node

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const apply = process.argv.includes('--apply');
const file = process.argv.slice(2).find(arg => arg !== '--apply');

if (!file) {
  console.error('Usage: fix_javadoc_sample_member_misuse.js [--apply] <Java file>');
  process.exit(2);
}

const text = fs.readFileSync(file, 'utf8');
const lines = text.split(/\r?\n/);
const newline = text.includes('\r\n') ? '\r\n' : '\n';
const activeJavadoc = activeJavadocLineMask(lines);

const primitiveTypes = 'boolean|byte|char|short|int|long|float|double';
const arrayDecl = new RegExp(`\\b(?:final\\s+)?(?:${primitiveTypes}|[A-Z_$][\\w$]*(?:\\s*<[^;=]+>)?)\\s*\\[\\]\\s+([A-Za-z_$][\\w$]*)\\b`, 'g');

let changed = false;
let replacements = 0;
let inBlock = false;
let blockStart = 0;
let blockLines = [];

function codeOf(line) {
  return line.replace(/^\s*\*\s?/, '');
}

function processBlock(startLine, block) {
  const vars = new Set();

  for (const line of block) {
    const code = codeOf(line);
    arrayDecl.lastIndex = 0;

    let match;
    while ((match = arrayDecl.exec(code)) !== null) {
      vars.add(match[1]);
    }
  }

  if (vars.size === 0) {
    return block;
  }

  return block.map((line, offset) => {
    let next = line;

    for (const varName of vars) {
      const re = new RegExp(`\\b${varName.replace(/[.*+?^${}()|[\\]\\\\]/g, '\\$&')}\\.size\\(\\)`, 'g');

      if (re.test(next)) {
        const before = next;
        next = next.replace(re, `${varName}.length`);

        if (next !== before) {
          replacements += (before.match(re) || []).length;
          console.log(`${file}:${startLine + offset + 1}: ${varName}.size() -> ${varName}.length`);
        }
      }
    }

    if (next !== line) {
      changed = true;
    }

    return next;
  });
}

const output = [];

for (let i = 0; i < lines.length; i++) {
  const line = lines[i];

  if (!activeJavadoc[i]) {
    if (inBlock) {
      output.push(...blockLines);
      inBlock = false;
      blockLines = [];
    }
    output.push(line);
    continue;
  }

  if (!inBlock && line.includes('<pre>{@code')) {
    inBlock = true;
    blockStart = i;
    blockLines = [line];

    if (line.includes('}</pre>')) {
      output.push(...processBlock(blockStart, blockLines));
      inBlock = false;
      blockLines = [];
    }

    continue;
  }

  if (inBlock) {
    blockLines.push(line);

    if (line.includes('}</pre>')) {
      output.push(...processBlock(blockStart, blockLines));
      inBlock = false;
      blockLines = [];
    }

    continue;
  }

  output.push(line);
}

if (apply && changed) {
  fs.writeFileSync(file, output.join(newline), 'utf8');
}

console.log(`REPLACEMENTS ${replacements}`);

if (!apply && replacements > 0) {
  process.exitCode = 1;
}
