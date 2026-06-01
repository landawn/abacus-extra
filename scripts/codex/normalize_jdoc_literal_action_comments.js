#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const args = process.argv.slice(2);
const apply = args[0] === '--apply';
const files = apply ? args.slice(1) : args;

if (files.length === 0) {
  console.error('Usage: node scripts/codex/normalize_jdoc_literal_action_comments.js [--apply] <java-file> [...]');
  process.exit(2);
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

    if (!inString && !inChar && ch === '/' && line[i + 1] === '/') {
      return i;
    }
  }

  return -1;
}

function isStandaloneSampleCommentLine(line, commentIndex) {
  return /^\s*\*\s*$/.test(line.slice(0, commentIndex));
}

function normalizeComment(comment) {
  const text = comment.replace(/^\/\/\s*/, '');

  if (/^[\[{]|^["']/.test(text.trimStart())) {
    return comment;
  }

  return comment
    .replace(/^\/\/\s*Adds\b/, '// adds')
    .replace(/^\/\/\s*Removes\b/, '// removes')
    .replace(/^\/\/\s*Fills\b/, '// fills')
    .replace(/^\/\/\s*Sorts\b/, '// sorts')
    .replace(/^\/\/\s*Reverses\b/, '// reverses')
    .replace(/^\/\/\s*Rotates\b/, '// rotates')
    .replace(/^\/\/\s*Shuffles\b/, '// shuffles')
    .replace(/^\/\/\s*Copies\b/, '// copies')
    .replace(/^\/\/\s*Pads\b/, '// pads')
    .replace(/^\/\/\s*Prints\b/, '// prints')
    .replace(/^\/\/\s*Keeps\b/, '// keeps')
    .replace(/^\/\/\s*Leaves\b/, '// leaves')
    .replace(/^\/\/\s*Uses\b/, '// uses')
    .replace(/^\/\/\s*Treats\b/, '// treats')
    .replace(/^\/\/\s*Searches\b/, '// searches')
    .replace(/^\/\/\s*Starts\b/, '// starts')
    .replace(/^\/\/\s*Ends\b/, '// ends')
    .replace(/^\/\/\s*Invokes\b/, '// invokes')
    .replace(/^\/\/\s*Converts\b/, '// converts');
}

let changedCount = 0;

for (const file of files) {
  const text = fs.readFileSync(file, 'utf8');
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const lines = text.split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inCode = false;
  let changed = false;

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

    if (!inCode) {
      continue;
    }

    const idx = commentIndexOutsideLiterals(line);
    if (idx < 0) {
      continue;
    }
    if (isStandaloneSampleCommentLine(line, idx)) {
      continue;
    }

    const before = line.slice(0, idx);
    const comment = line.slice(idx);
    const normalized = normalizeComment(comment);

    if (normalized !== comment) {
      lines[i] = before + normalized;
      changed = true;
      changedCount++;
    }
  }

  if (apply && changed) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
  }
}

console.log(`NORMALIZED_JDOC_LITERAL_ACTION_COMMENTS ${changedCount}`);
if (!apply && changedCount > 0) {
  process.exit(1);
}
