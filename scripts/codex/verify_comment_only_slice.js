#!/usr/bin/env node

const cp = require('child_process');

function runNode(args) {
  return cp.spawnSync(process.execPath, args, { encoding: 'utf8' });
}

const listed = runNode(['scripts/codex/list_jdoc_usage_slice.js']);

if (listed.status !== 0) {
  process.stdout.write(listed.stdout || '');
  process.stderr.write(listed.stderr || '');
  process.exit(listed.status || 1);
}

const files = listed.stdout
  .split(/\r?\n/)
  .map(line => line.trim())
  .filter(Boolean)
  .map(line => line.split(/\t/)[0]);

let failed = false;

for (const file of files) {
  const result = runNode(['scripts/codex/verify_comment_only_diff.js', file]);

  if (result.status !== 0) {
    failed = true;
    process.stdout.write(result.stdout || '');
    process.stderr.write(result.stderr || '');
  }
}

console.log(`COMMENT_ONLY_VERIFIED_FILE_COUNT ${files.length}`);

if (failed) process.exit(1);
