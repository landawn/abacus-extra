#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const all = process.argv.includes('--all');
const files = process.argv.slice(2).filter(arg => arg !== '--all');

if (files.length === 0) {
  console.error('Usage: node scripts/codex/report_jdoc_standalone_sample_comments.js [--all] <Java file> [<Java file>...]');
  process.exit(2);
}

function looksLikeResultComment(text) {
  const lower = text.trim().toLowerCase();
  const prefixes = [
    'return',
    'throw',
    'print',
    'result',
    'represent',
    'useful',
    'using',
    'set ',
    'can be used',
    'default ',
    'high precision',
    'parse with',
    'convert to',
    'note:',
    'a java',
    'the default',
    'formatted with',
    'numeric =',
    'numberformatexception',
    'nullpointerexception',
    'illegalargumentexception',
    'arithmeticexception',
  ];

  return prefixes.some(prefix => lower.startsWith(prefix));
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inPre = false;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];

    if (!activeJavadoc[i]) {
      inPre = false;
      continue;
    }

    if (line.includes('<pre>{@code')) {
      inPre = true;
      continue;
    }

    if (inPre && line.includes('}</pre>')) {
      inPre = false;
      continue;
    }

    if (!inPre) {
      continue;
    }

    const match = line.match(/^\s*\*\s+\/\/\s+(.+)$/);

    if (!match) {
      continue;
    }

    const text = match[1].trim();
    const kind = looksLikeResultComment(text) ? 'NON_HEADING' : 'HEADING';
    if (all || kind !== 'HEADING') {
      console.log(`${kind}\t${file}:${i + 1}\t${text}`);
    }
  }
}
