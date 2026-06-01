#!/usr/bin/env node
'use strict';

const fs = require('fs');

const [file, startArg, endArg] = process.argv.slice(2);

if (!file || !startArg || !endArg) {
  console.error('Usage: node scripts/codex/print_range.js <file> <start-line> <end-line>');
  process.exit(2);
}

const start = Number(startArg);
const end = Number(endArg);
const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);

for (let i = Math.max(1, start); i <= Math.min(lines.length, end); i++) {
  console.log(`${String(i).padStart(6, ' ')}: ${lines[i - 1]}`);
}
