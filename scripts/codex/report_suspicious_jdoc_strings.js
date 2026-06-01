#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocLineMask } = require('./javadoc_region');

const roots = process.argv.slice(2);
if (roots.length === 0) {
  console.error('Usage: node scripts/codex/report_suspicious_jdoc_strings.js <file-or-dir> [...]');
  process.exit(2);
}

function walk(target, files) {
  if (!fs.existsSync(target)) return;
  const stat = fs.statSync(target);
  if (stat.isDirectory()) {
    for (const entry of fs.readdirSync(target)) {
      if (entry === '.git' || entry === 'target') continue;
      walk(path.join(target, entry), files);
    }
  } else if (target.endsWith('.java')) {
    files.push(target);
  }
}

function hasSuspiciousString(line) {
  let inString = false;
  let escaped = false;
  let current = '';

  for (let i = 0; i < line.length; i++) {
    const ch = line[i];

    if (!inString) {
      if (ch === '"') {
        inString = true;
        escaped = false;
        current = '"';
      }
      continue;
    }

    current += ch;

    if (escaped) {
      escaped = false;
      continue;
    }
    if (ch === '\\') {
      escaped = true;
      continue;
    }
    if (ch === '"') {
      if (/\/\/\s*(returns|throws)\b/i.test(current)) return true;
      inString = false;
      current = '';
    }
  }

  return false;
}

function commentIndexOutsideLiterals(line) {
  let inString = false;
  let inChar = false;
  let escaped = false;

  for (let i = 0; i < line.length - 1; i++) {
    const ch = line[i];
    if (escaped) {
      escaped = false;
      continue;
    }
    if (ch === '\\') {
      escaped = true;
      continue;
    }
    if (!inChar && ch === '"') {
      inString = !inString;
      continue;
    }
    if (!inString && ch === "'") {
      inChar = !inChar;
      continue;
    }
    if (!inString && !inChar && ch === '/' && line[i + 1] === '/') return i;
  }

  return -1;
}

const files = [];
for (const root of roots) walk(root, files);

let count = 0;
for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inCode = false;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    if (!activeJavadoc[i]) {
      inCode = false;
      continue;
    }
    if (!inCode && line.includes('<pre>{@code')) {
      inCode = true;
      continue;
    }
    if (inCode && line.includes('</pre>')) {
      inCode = false;
      continue;
    }
    const idx = commentIndexOutsideLiterals(line);
    const code = idx < 0 ? line : line.slice(0, idx);
    if (inCode && hasSuspiciousString(code)) {
      console.log(`${file}:${i + 1}:${line}`);
      count++;
    }
  }
}

if (count > 0) process.exit(1);
