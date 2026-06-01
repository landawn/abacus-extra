#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const args = process.argv.slice(2);
const apply = args[0] === '--apply';
const files = apply ? args.slice(1) : args;

if (files.length === 0) {
  console.error('Usage: node scripts/codex/fix_assignment_style_public_jdoc_examples.js [--apply] <java-file> [...]');
  process.exit(2);
}

function nextDeclaration(lines, endIndex) {
  for (let i = endIndex + 1; i < lines.length; i++) {
    const trimmed = lines[i].trim();
    if (!trimmed || trimmed.startsWith('@')) continue;
    return trimmed;
  }

  return '';
}

function isPublicMethodDeclaration(decl) {
  return /^public\b/.test(decl) && !/\b(class|interface|enum|@interface)\b/.test(decl) && decl.includes('(');
}

function resultComment(result) {
  let rhs = result.trim();
  rhs = rhs.replace(/\s+$/, '');

  let m = rhs.match(/^throws\s+(?:an?\s+)?(.+)$/i);
  if (m) return `throws ${m[1].trim()}`;

  m = rhs.match(/^(?:an?\s+)?([A-Za-z_$][\w$]*(?:Exception|Error)\b.*)$/);
  if (m) return `throws ${m[1].trim()}`;

  return `returns ${rhs}`;
}

function convertLine(line) {
  const match = line.match(/^(\s*\*\s+)([A-Z][\w$]*(?:\.[A-Za-z_$][\w$]*)+\s*\(.*\))\s*=\s*(.+?)\s*$/);
  if (!match) return null;

  const [, prefix, call, result] = match;
  if (call.includes('//') || result.includes('//')) return null;

  return `${prefix}${call};   // ${resultComment(result)}`;
}

let total = 0;

for (const file of files) {
  const text = fs.readFileSync(file, 'utf8');
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const lines = text.split(/\r?\n/);
  const changes = [];

  for (let i = 0; i < lines.length; i++) {
    if (!isActiveJavadocStart(lines[i])) continue;

    const start = i;
    let end = -1;
    for (let j = i + 1; j < lines.length; j++) {
      if (lines[j].includes('*/')) {
        end = j;
        break;
      }
    }

    if (end < 0) break;

    const decl = nextDeclaration(lines, end);
    if (!isPublicMethodDeclaration(decl)) {
      i = end;
      continue;
    }

    let inCode = false;
    for (let j = start; j <= end; j++) {
      if (!inCode && lines[j].includes('<pre>{@code')) {
        inCode = true;
        continue;
      }
      if (inCode && lines[j].includes('</pre>')) {
        inCode = false;
        continue;
      }
      if (!inCode) continue;

      const converted = convertLine(lines[j]);
      if (converted && converted !== lines[j]) {
        changes.push({ line: j, before: lines[j], after: converted });
      }
    }

    i = end;
  }

  for (const change of changes) {
    console.log(`${file}:${change.line + 1}: ${change.before.trim()} -> ${change.after.trim()}`);
    if (apply) lines[change.line] = change.after;
  }

  total += changes.length;
  if (apply && changes.length) fs.writeFileSync(file, lines.join(newline), 'utf8');
}

console.log(`ASSIGNMENT_STYLE_PUBLIC_EXAMPLE_COUNT ${total}`);
if (!apply && total > 0) process.exit(1);
