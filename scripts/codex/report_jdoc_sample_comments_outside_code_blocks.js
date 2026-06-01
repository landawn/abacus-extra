#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/report_jdoc_sample_comments_outside_code_blocks.js <Java file> [<Java file>...]');
  process.exit(2);
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inPre = false;

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

    if (!inPre && /^\s*\*\s+\/\/\s+\S/.test(line)) {
      console.log(`${file}:${i + 1}: ${line.trim()}`);
    }
  }
}
