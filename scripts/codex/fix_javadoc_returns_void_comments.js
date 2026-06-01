#!/usr/bin/env node

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const apply = process.argv.includes('--apply');
const files = process.argv.slice(2).filter(arg => arg !== '--apply');

if (files.length === 0) {
  console.error('Usage: node scripts/codex/fix_javadoc_returns_void_comments.js [--apply] <java-file> [java-file...]');
  process.exit(2);
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

    if (!inCode || !/\/\/\s*returns void\s*$/.test(line)) {
      continue;
    }

    const commentIndex = line.indexOf('//');
    if (/^\s*\*\s*$/.test(line.slice(0, commentIndex))) {
      continue;
    }

    if (line.includes('proxy.run();')) {
      lines[i] = line.replace(/\/\/\s*returns void\s*$/, '// invokes the handler');
    } else {
      lines[i] = line.replace(/\/\/\s*returns void\s*$/, '// no exception thrown');
    }

    changed = true;
    changedCount++;
    console.log(`${file}:${i + 1}: ${apply ? 'fixed' : 'would fix'}`);
  }

  if (changed && apply) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
  }
}

console.log(`RETURNS_VOID_COMMENT_FIX_COUNT ${changedCount}`);

if (!apply && changedCount > 0) {
  process.exitCode = 1;
}
