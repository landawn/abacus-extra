#!/usr/bin/env node

const fs = require('fs');
const { activeJavadocLineMask, isActiveJavadocStart } = require('./javadoc_region');

const files = process.argv.slice(2);
if (files.length === 0) {
  console.error('Usage: node scripts/codex/fix_jdoc_usage_spacing.js <java-file> [...]');
  process.exit(2);
}

function isBlankJavadoc(line) {
  return /^\s*\*\s*$/.test(line);
}

function blankFor(line) {
  const match = line.match(/^(\s*)\*/);
  return `${match ? match[1] : '     '}*`;
}

for (const file of files) {
  const text = fs.readFileSync(file, 'utf8');
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const lines = text.split(/\r?\n/);
  let changed = false;

  let inJavadoc = false;
  for (let i = 0; i < lines.length; i++) {
    if (!inJavadoc && isActiveJavadocStart(lines[i])) {
      inJavadoc = true;
      continue;
    }
    if (inJavadoc && lines[i].includes('*/')) {
      inJavadoc = false;
      continue;
    }
    if (!inJavadoc) continue;

    if (i > 0 && isBlankJavadoc(lines[i]) && isBlankJavadoc(lines[i - 1])) {
      lines.splice(i, 1);
      i--;
      changed = true;
    }
  }

  for (let i = 0; i < lines.length; i++) {
    const activeJavadoc = activeJavadocLineMask(lines);
    if (!activeJavadoc[i]) continue;
    if (!/<p><b>.*(?:Usage Examples|Examples?|Example usage).*<\/b><\/p>/.test(lines[i])) continue;

    const blank = blankFor(lines[i]);

    while (i > 0 && isBlankJavadoc(lines[i - 1]) && i > 1 && isBlankJavadoc(lines[i - 2])) {
      lines.splice(i - 1, 1);
      i--;
      changed = true;
    }

    if (i > 0 && !isBlankJavadoc(lines[i - 1])) {
      lines.splice(i, 0, blank);
      i++;
      changed = true;
    }

    let close = i + 1;
    while (close < lines.length && !lines[close].includes('</pre>')) close++;
    if (close >= lines.length) continue;

    while (close + 2 < lines.length && isBlankJavadoc(lines[close + 1]) && isBlankJavadoc(lines[close + 2])) {
      lines.splice(close + 2, 1);
      changed = true;
    }

    if (close + 1 < lines.length && !isBlankJavadoc(lines[close + 1])) {
      lines.splice(close + 1, 0, blank);
      changed = true;
    }
  }

  if (changed) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
  }
}
