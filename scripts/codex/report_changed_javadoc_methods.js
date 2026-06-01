#!/usr/bin/env node

const cp = require('child_process');
const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const file = process.argv[2] || 'src/main/java/com/landawn/abacus/util/CommonUtil.java';
const text = fs.readFileSync(file, 'utf8');
const lines = text.split(/\r?\n/);
const diff = cp.execFileSync('svn', ['diff', '--git', file], { encoding: 'utf8' });

const changedLines = new Set();
let newLine = 0;

for (const line of diff.split(/\r?\n/)) {
  const hunk = line.match(/^@@ -\d+(?:,\d+)? \+(\d+)(?:,(\d+))? @@/);
  if (hunk) {
    newLine = Number(hunk[1]);
    continue;
  }

  if (!newLine) continue;

  if (line.startsWith('+') && !line.startsWith('+++')) {
    changedLines.add(newLine);
    newLine++;
  } else if (line.startsWith('-') && !line.startsWith('---')) {
    continue;
  } else {
    newLine++;
  }
}

function signatureAfter(end) {
  let signature = '';

  for (let i = end + 1; i < Math.min(lines.length, end + 30); i++) {
    const trimmed = lines[i].trim();
    if (!trimmed || trimmed.startsWith('@')) continue;
    signature += ` ${trimmed}`;
    if (trimmed.includes('{') || trimmed.includes(';')) break;
  }

  return signature.replace(/\s+/g, ' ').trim();
}

const methods = new Map();

for (let start = 0; start < lines.length; start++) {
  if (!isActiveJavadocStart(lines[start])) continue;

  let end = start + 1;
  while (end < lines.length && !lines[end].includes('*/')) end++;
  if (end >= lines.length) break;

  let changed = false;
  for (let lineNo = start + 1; lineNo <= end + 1; lineNo++) {
    if (changedLines.has(lineNo)) {
      changed = true;
      break;
    }
  }

  if (changed) {
    const signature = signatureAfter(end);
    const match = signature.match(/\b([A-Za-z_$][\w$]*)\s*\(/);
    const name = match ? match[1] : '(class javadoc)';
    if (!methods.has(name)) methods.set(name, []);
    methods.get(name).push(signature);
  }

  start = end;
}

for (const [name, signatures] of methods) {
  console.log(`${name}: ${signatures.length}`);
  for (const signature of signatures.slice(0, 4)) {
    console.log(`  ${signature}`);
  }
  if (signatures.length > 4) {
    console.log(`  ... ${signatures.length - 4} more overload(s)`);
  }
}

console.log(`CHANGED_JAVADOC_METHOD_COUNT ${methods.size}`);
