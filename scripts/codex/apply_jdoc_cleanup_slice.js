#!/usr/bin/env node

const cp = require('child_process');
const path = require('path');

const root = process.argv[2] || 'src/main/java';
const prefix = (process.argv[3] || '').replace(/\\/g, '/');
const start = process.argv[4] || '';
const end = process.argv[5] || '\uffff';
const exclude = new Set((process.argv[6] || '').split(',').filter(Boolean));

function run(args) {
  const result = cp.spawnSync(process.execPath, args, { encoding: 'utf8', stdio: 'inherit' });
  if (result.status !== 0) process.exit(result.status || 1);
}

const inventory = cp.spawnSync(process.execPath, ['scripts/codex/inventory_jdoc_usage_files.js', root], { encoding: 'utf8' });
if (inventory.status !== 0) {
  process.stdout.write(inventory.stdout || '');
  process.stderr.write(inventory.stderr || '');
  process.exit(inventory.status || 1);
}

const files = [];
for (const line of inventory.stdout.split(/\r?\n/)) {
  if (!line.trim()) continue;
  const file = line.split(/\t/)[0];
  if (prefix && !file.startsWith(prefix)) continue;
  const base = path.basename(file);
  if (base < start || base > end) continue;
  if (exclude.has(base) || exclude.has(file)) continue;
  files.push(file);
}

if (files.length === 0) {
  console.log('No files selected.');
  process.exit(0);
}

for (const script of ['fix_jdoc_usage_indent.js', 'fix_jdoc_usage_spacing.js']) {
  for (let i = 0; i < files.length; i += 25) {
    run([`scripts/codex/${script}`, ...files.slice(i, i + 25)]);
  }
}

for (let i = 0; i < files.length; i += 25) {
  run(['scripts/codex/normalize_jdoc_example_comments.js', '--apply', ...files.slice(i, i + 25)]);
}

for (const file of files) {
  run(['scripts/codex/align_jdoc_examples.js', '--apply', file]);
}

console.log(`CLEANED_FILE_COUNT ${files.length}`);
