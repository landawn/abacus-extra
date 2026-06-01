#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

function usage() {
  console.error('Usage: node scripts/codex/fix_jdoc_double_blank_lines.js --apply <Java file> [<Java file>...]');
}

const args = process.argv.slice(2);
const applyIndex = args.indexOf('--apply');
const apply = applyIndex !== -1;
if (apply) {
  args.splice(applyIndex, 1);
}

if (!apply || args.length === 0) {
  usage();
  process.exit(2);
}

let fixCount = 0;

for (const file of args) {
  const original = fs.readFileSync(file, 'utf8');
  const lines = original.split(/\r?\n/);
  const newline = original.includes('\r\n') ? '\r\n' : '\n';
  const out = [];
  let inJavadoc = false;
  let previousBlankJavadocLine = false;

  for (const line of lines) {
    const startsJavadoc = isActiveJavadocStart(line);
    if (startsJavadoc) {
      inJavadoc = true;
      previousBlankJavadocLine = false;
    }

    const blankJavadocLine = inJavadoc && /^\s*\*\s*$/.test(line);

    if (blankJavadocLine && previousBlankJavadocLine) {
      fixCount++;
      continue;
    }

    out.push(line);
    previousBlankJavadocLine = blankJavadocLine;

    if (inJavadoc && /\*\/\s*$/.test(line)) {
      inJavadoc = false;
      previousBlankJavadocLine = false;
    }
  }

  const updated = out.join(newline);
  if (updated !== original) {
    fs.writeFileSync(file, updated, 'utf8');
  }
}

console.log(`DOUBLE_BLANK_JAVADOC_FIX_COUNT ${fixCount}`);
