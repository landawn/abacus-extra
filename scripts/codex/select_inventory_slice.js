#!/usr/bin/env node

const cp = require('child_process');
const path = require('path');

const root = process.argv[2] || 'src/main/java';
const prefix = (process.argv[3] || '').replace(/\\/g, '/');
const start = process.argv[4] || '';
const end = process.argv[5] || '\uffff';
const exclude = new Set((process.argv[6] || '').split(',').filter(Boolean));

const inventory = cp.spawnSync(process.execPath, ['scripts/codex/inventory_jdoc_usage_files.js', root], { encoding: 'utf8' });
if (inventory.status !== 0) {
  process.stdout.write(inventory.stdout || '');
  process.stderr.write(inventory.stderr || '');
  process.exit(inventory.status || 1);
}

for (const line of inventory.stdout.split(/\r?\n/)) {
  if (!line.trim()) continue;
  const file = line.split(/\t/)[0];
  if (prefix && !file.startsWith(prefix)) continue;
  const base = path.basename(file);
  if (base < start || base > end) continue;
  if (exclude.has(base) || exclude.has(file)) continue;
  console.log(file);
}
