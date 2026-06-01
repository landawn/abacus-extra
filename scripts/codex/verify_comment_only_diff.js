#!/usr/bin/env node

const { execFileSync } = require('child_process');

const file = process.argv[2];

if (!file) {
  console.error('Usage: node scripts/codex/verify_comment_only_diff.js <path-to-java-file>');
  process.exit(2);
}

let diff = '';
try {
  diff = execFileSync('svn', ['diff', '--git', file], { encoding: 'utf8', stdio: ['ignore', 'pipe', 'pipe'] });
} catch (error) {
  diff = error.stdout || '';
}

const violations = [];

for (const line of diff.split(/\r?\n/)) {
  if (!line || !/^[+-]/.test(line)) continue;
  if (/^(---|\+\+\+|\+\+\+\s|---\s)/.test(line)) continue;

  const content = line.slice(1).trim();

  if (
    content === '' ||
    content.startsWith('*') ||
    content.startsWith('//') ||
    content.startsWith('/*') ||
    content.startsWith('*/')
  ) {
    continue;
  }

  violations.push(line);
}

if (violations.length) {
  console.error(`Non-comment changed lines found in ${file}:`);
  for (const line of violations.slice(0, 50)) {
    console.error(line);
  }
  if (violations.length > 50) {
    console.error(`... ${violations.length - 50} more`);
  }
  process.exit(1);
}

console.log(`Only comment/Javadoc changed lines found in ${file}.`);
