#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/report_jdoc_comments_outside_pre.js <Java file> [<Java file>...]');
  process.exit(2);
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);

  for (let i = 0; i < lines.length; i++) {
    if (!activeJavadoc[i]) {
      continue;
    }

    if (!lines[i].includes('Usage Examples:</b></p>')) {
      continue;
    }

    for (let j = i + 1; j < Math.min(lines.length, i + 8) && activeJavadoc[j]; j++) {
      if (lines[j].includes('<pre>{@code')) {
        break;
      }

      if (/^\s*\*\s+\/\/\s+/.test(lines[j])) {
        console.log(`${file}:${j + 1}: ${lines[j].trim()}`);
      }
    }
  }
}
