#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/inventory_jdoc_examples.js <java-file> [java-file...]');
  process.exit(2);
}

const examplePattern = /(Usage Examples|Common Usage Patterns|Example usage|Examples?:|<b>Example\b|<pre>\{@code)/i;

function findDeclaration(lines, start) {
  const parts = [];

  for (let i = start; i < lines.length; i++) {
    const trimmed = lines[i].trim();
    if (!trimmed || trimmed.startsWith('@')) continue;
    parts.push(trimmed);

    const joined = parts.join(' ');
    if (/[;{]$/.test(trimmed) || joined.includes(')')) {
      return { line: i + 1, text: joined };
    }
  }

  return { line: start + 1, text: '' };
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  let count = 0;

  for (let i = 0; i < lines.length; i++) {
    if (!isActiveJavadocStart(lines[i])) continue;

    const start = i;
    let end = i;
    while (end < lines.length && !lines[end].includes('*/')) end++;

    const block = lines.slice(start, end + 1).join('\n');
    if (examplePattern.test(block)) {
      const declaration = findDeclaration(lines, end + 1);
      count++;
      console.log(`${file}:${start + 1}-${end + 1}:${declaration.line}:${declaration.text}`);
    }

    i = end;
  }

  console.log(`EXAMPLE_JAVADOC_BLOCK_COUNT ${file} ${count}`);
}
