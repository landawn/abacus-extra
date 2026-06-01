#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocLineMask } = require('./javadoc_region');

const requestedNeedle = process.argv[2] || 'returns_this';
const aliases = {
  returns_this: '// returns this',
  returns_response: '// returns response',
  returns_void: '// returns void',
};
const needle = aliases[requestedNeedle] || requestedNeedle;
const roots = process.argv.slice(3);

if (roots.length === 0) {
  console.error('Usage: node scripts/codex/scan_owned_javadoc_comments.js <needle> <path> [path...]');
  process.exit(2);
}

function walk(dir) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const file = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      walk(file);
    } else if (entry.isFile() && entry.name.endsWith('.java')) {
      const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
      const activeJavadoc = activeJavadocLineMask(lines);
      lines.forEach((line, index) => {
        if (activeJavadoc[index] && line.includes(needle)) {
          console.log(`${file.replace(/\\/g, '/')}:${index + 1}:${line.trim()}`);
        }
      });
    }
  }
}

roots.forEach(walk);
