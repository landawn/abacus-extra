#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/javadoc_placeholder_report.js <java-file> [java-file...]');
  process.exit(2);
}

let count = 0;

function stripStringAndCharLiterals(s) {
  return s
    .replace(/"(?:\\.|[^"\\])*"/g, '""')
    .replace(/'(?:\\.|[^'\\])*'/g, "''");
}

function hasPlaceholder(line) {
  const code = stripStringAndCharLiterals(line);
  return /\.\.\.\s*;/.test(code)
    || /\(\s*\.\.\.\s*\)/.test(code)
    || /\bcollect\(\s*\.\.\.\s*\)/.test(code)
    || /<\s*\.\.\.\s*>/.test(code)
    || /^\s*\*\s*\.\.\.\s*$/.test(code)
    || code.includes('typical usage')
    || code.includes('edge case');
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  let inJavadoc = false;
  let inPre = false;
  let currentMethod = '';

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];

    if (isActiveJavadocStart(line)) {
      inJavadoc = true;
      inPre = false;
      currentMethod = '';
      continue;
    }

    if (inJavadoc && line.includes('<pre>{@code')) {
      inPre = true;
      continue;
    }

    if (inJavadoc && line.includes('</pre>')) {
      inPre = false;
      continue;
    }

    if (inJavadoc && line.includes('*/')) {
      inJavadoc = false;
      inPre = false;

      let j = i + 1;
      while (j < lines.length && lines[j].trim() === '') j++;
      while (j < lines.length && /^(\s*@|\s*$)/.test(lines[j])) j++;

      const signature = lines[j] || '';
      const match = signature.match(/\b([A-Za-z_$][\w$]*)\s*\(/);
      currentMethod = match ? match[1] : '';
      continue;
    }

    if (!inJavadoc || !inPre) {
      continue;
    }

    if (hasPlaceholder(line)) {
      count++;
      console.log(`${file}:${i + 1}${currentMethod ? `:${currentMethod}` : ''}: ${line.trim()}`);
    }
  }
}

console.log(`PLACEHOLDER_COUNT ${count}`);

if (count > 0) {
  process.exit(1);
}
