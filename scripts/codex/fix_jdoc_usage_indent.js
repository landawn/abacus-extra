#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const files = process.argv.slice(2);
if (files.length === 0) {
  console.error('Usage: node scripts/codex/fix_jdoc_usage_indent.js <java-file> [...]');
  process.exit(2);
}

const tagRe = /^(\s*)\*\s*@(param|return|throws|see|since|deprecated)\b/;
const starRe = /^(\s*)\*(.*)$/;
const usageRe = /<p><b>.*(?:Usage Examples|Example).*<\/b><\/p>/;

for (const file of files) {
  const text = fs.readFileSync(file, 'utf8');
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const lines = text.split(/\r?\n/);
  let changed = false;

  for (let start = 0; start < lines.length; start++) {
    if (!isActiveJavadocStart(lines[start])) continue;

    let end = start + 1;
    while (end < lines.length && !lines[end].includes('*/')) end++;
    if (end >= lines.length) break;

    let expected = null;
    for (let i = start; i <= end; i++) {
      const match = lines[i].match(tagRe);
      if (match) {
        expected = match[1];
        break;
      }
    }
    if (expected == null) {
      for (let i = start + 1; i <= end; i++) {
        const match = lines[i].match(starRe);
        if (match && match[2].trim()) {
          expected = match[1];
          break;
        }
      }
    }
    if (expected == null) {
      start = end;
      continue;
    }

    for (let i = start + 1; i <= end; i++) {
      if (!usageRe.test(lines[i])) continue;

      let blockStart = i;
      if (blockStart > start && /^\s*\*\s*$/.test(lines[blockStart - 1])) blockStart--;

      let blockEnd = i;
      while (blockEnd <= end && !lines[blockEnd].includes('</pre>')) blockEnd++;
      if (blockEnd <= end && blockEnd + 1 <= end && (/^\s*\*\s*$/.test(lines[blockEnd + 1]) || /^\s*$/.test(lines[blockEnd + 1]))) blockEnd++;

      for (let j = blockStart; j <= blockEnd; j++) {
        if (/^\s*$/.test(lines[j])) {
          lines[j] = `${expected}*`;
          changed = true;
          continue;
        }
        const match = lines[j].match(starRe);
        if (match && match[1] !== expected) {
          lines[j] = expected + '*' + match[2];
          changed = true;
        }
      }
    }

    start = end;
  }

  if (changed) fs.writeFileSync(file, lines.join(newline), 'utf8');
}
