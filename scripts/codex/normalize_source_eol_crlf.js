#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const roots = process.argv.slice(2);

if (roots.length === 0) {
  console.error('Usage: node scripts/codex/normalize_source_eol_crlf.js <path> [path...]');
  process.exit(2);
}

function walk(input, files) {
  const stat = fs.statSync(input);
  if (stat.isDirectory()) {
    for (const entry of fs.readdirSync(input, { withFileTypes: true })) {
      walk(path.join(input, entry.name), files);
    }
  } else if (stat.isFile() && input.endsWith('.java')) {
    files.push(input);
  }
}

let changed = 0;

for (const root of roots) {
  const files = [];
  walk(root, files);

  for (const file of files) {
    const original = fs.readFileSync(file, 'utf8');
    const normalized = original.replace(/\r?\n/g, '\r\n');
    if (normalized !== original) {
      fs.writeFileSync(file, normalized, 'utf8');
      changed++;
      console.log(file);
    }
  }
}

console.log(`CRLF_FILE_COUNT ${changed}`);
