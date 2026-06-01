#!/usr/bin/env node

const cp = require('child_process');
const fs = require('fs');
const path = require('path');

const roots = process.argv.slice(2);

if (roots.length === 0) {
  console.error('Usage: node scripts/codex/revert_eol_only_owned_files.js <path> [path...]');
  process.exit(2);
}

function walk(input, files) {
  const stat = fs.statSync(input);
  if (stat.isDirectory()) {
    for (const entry of fs.readdirSync(input, { withFileTypes: true })) {
      walk(path.join(input, entry.name), files);
    }
  } else if (stat.isFile() && input.endsWith('.java')) {
    files.push(input);
  }
}

function run(args) {
  return cp.spawnSync('svn', args, { encoding: 'utf8' });
}

const files = [];
roots.forEach(root => walk(root, files));

let reverted = 0;
for (const file of files) {
  const status = run(['status', file]);
  if (!/^M/.test(status.stdout || '')) continue;

  const diff = run(['diff', '-x', '--ignore-eol-style', file]);
  if ((diff.stdout || '').trim() === '') {
    const result = run(['revert', file]);
    if (result.status !== 0) {
      process.stderr.write(result.stderr || result.stdout || '');
      process.exit(result.status || 1);
    }
    reverted++;
    console.log(file);
  }
}

console.log(`REVERTED_EOL_ONLY_FILE_COUNT ${reverted}`);
