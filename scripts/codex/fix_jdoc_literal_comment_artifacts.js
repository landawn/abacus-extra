#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocLineMask } = require('./javadoc_region');

const roots = process.argv.slice(2);
if (roots.length === 0) {
  console.error('Usage: node scripts/codex/fix_jdoc_literal_comment_artifacts.js <file-or-dir> [...]');
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

function fixStringArtifacts(text) {
  return text
    .replace(/:\/\/\s+returns\s+/gi, '://')
    .replace(/\/\/\s+returns\s+/gi, '//');
}

function fixLine(line) {
  const idx = commentIndexOutsideLiterals(line);
  if (idx < 0) return fixStringArtifacts(line);
  return fixStringArtifacts(line.slice(0, idx)) + line.slice(idx);
}

const files = [];
for (const root of roots) walk(root, files);

let changedFiles = 0;
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
    if (!inCode) continue;

    const fixed = fixLine(line);
    if (fixed !== line) {
      lines[i] = fixed;
      changed = true;
    }
  }

  if (changed) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
    changedFiles++;
  }
}

console.log(`FIXED_LITERAL_ARTIFACT_FILES ${changedFiles}`);
