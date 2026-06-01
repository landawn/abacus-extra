#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const apply = process.argv.includes('--apply');
const files = process.argv.slice(2).filter(arg => arg !== '--apply');

if (files.length === 0) {
  console.error('Usage: node scripts/codex/replace_jdoc_type_placeholders.js [--apply] <Java file> [<Java file>...]');
  process.exit(2);
}

let replacementCount = 0;

for (const file of files) {
  const original = fs.readFileSync(file, 'utf8');
  const lines = original.split(/\r?\n/);
  const newline = original.includes('\r\n') ? '\r\n' : '\n';
  const activeJavadoc = activeJavadocLineMask(lines);
  let inPre = false;
  let changed = false;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];

    if (!activeJavadoc[i]) {
      inPre = false;
      continue;
    }

    if (line.includes('<pre>{@code')) {
      inPre = true;
      continue;
    }

    if (inPre && line.includes('}</pre>')) {
      inPre = false;
      continue;
    }

    if (!inPre) {
      continue;
    }

    const updated = line.replace(/\b([A-Z][A-Za-z0-9_.]*)<\.\.\.>/g, '$1<?, ?, ?>');
    if (updated !== line) {
      lines[i] = updated;
      changed = true;
      replacementCount++;
      console.log(`${file}:${i + 1}: ${apply ? 'replaced' : 'would replace'}`);
    }
  }

  if (apply && changed) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
  }
}

console.log(`TYPE_PLACEHOLDER_REPLACEMENT_COUNT ${replacementCount}`);
if (!apply && replacementCount > 0) {
  process.exit(1);
}
