#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const apply = process.argv.includes('--apply');
const files = process.argv.slice(2).filter(arg => arg !== '--apply');

if (files.length === 0) {
  console.error('Usage: node scripts/codex/normalize_jdoc_trailing_comment_style.js [--apply] <java-file> [...]');
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

function isStandaloneSampleComment(line, commentIndex) {
  return /^\s*\*\s*$/.test(line.slice(0, commentIndex));
}

function normalizeComment(comment, code) {
  const text = comment.replace(/^\/\/\s*/, '').trim();
  const lowered = text.charAt(0).toLowerCase() + text.slice(1);

  if (/^Returns:?\s*/.test(text)) return `// returns ${text.replace(/^Returns:?\s*/, '')}`.trimEnd();
  if (/^Return:?\s*/.test(text)) return `// returns ${text.replace(/^Return:?\s*/, '')}`.trimEnd();
  if (/^Throws:?\s*/.test(text)) return `// throws ${text.replace(/^Throws:?\s*/, '')}`.trimEnd();
  if (/^Throw:?\s*/.test(text)) return `// throws ${text.replace(/^Throw:?\s*/, '')}`.trimEnd();
  if (/^IllegalStateException:?\s*/.test(text)) return `// throws IllegalStateException; ${text.replace(/^IllegalStateException:?\s*/, '')}`.trimEnd();
  if (/^Contains CSV\b/.test(text) && /\bcsv\b/i.test(code)) {
    return `// csv contains${text.replace(/^Contains CSV/, ' CSV')}`;
  }
  if (/^Terminal operation\s*-\s*closes stream\b/.test(text)) return '// closes stream as terminal operation';

  const replacements = [
    [/^Remove\b/, 'removes'],
    [/^Keep\b/, 'keeps'],
    [/^Transform\b/, 'transforms'],
    [/^Collect\b/, 'collects'],
    [/^Use\b/, 'uses'],
    [/^Filter\b/, 'filters'],
    [/^Switch\b/, 'switches'],
    [/^Maintain\b/, 'maintains'],
    [/^Stop\b/, 'stops'],
    [/^Only process\b/, 'processes only'],
    [/^Sum\b/, 'sums'],
    [/^Logs\b/, 'logs'],
    [/^Log\b/, 'logs'],
    [/^Buffer\b/, 'buffers'],
    [/^Waits\b/, 'waits'],
    [/^Get\b/, 'gets'],
    [/^Reuse\b/, 'reuses'],
    [/^Elements are\b/, 'elements are'],
    [/^Inserts\b/, 'inserts'],
    [/^File contains\b/, 'file contains'],
    [/^Writer contains\b/, 'writer contains'],
    [/^Output stream contains\b/, 'output contains'],
    [/^Stream with\b/, 'stream contains'],
    [/^Empty stream\b/, 'stream is empty'],
    [/^New stream\b/, 'creates a new stream'],
    [/^Auto-closed\b/, 'closes automatically'],
    [/^Terminal operation\s*-\s*/, 'closes stream as terminal operation; '],
    [/^User must close the reader\b/, 'closes reader manually'],
  ];

  for (const [pattern, replacement] of replacements) {
    if (pattern.test(text)) {
      return `// ${text.replace(pattern, replacement)}`;
    }
  }

  if (/^[A-Z][a-z]/.test(text) && /\b(?:contains|is|are|has|becomes)\b/.test(lowered)) {
    return `// ${lowered}`;
  }

  return comment;
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
    if (idx < 0 || isStandaloneSampleComment(line, idx)) {
      continue;
    }

    const before = line.slice(0, idx);
    const comment = line.slice(idx);
    const normalized = normalizeComment(comment, before.replace(/^\s*\*\s?/, ''));

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

console.log(`NORMALIZED_JDOC_TRAILING_COMMENT_STYLE ${changedCount}`);
if (!apply && changedCount > 0) {
  process.exit(1);
}
