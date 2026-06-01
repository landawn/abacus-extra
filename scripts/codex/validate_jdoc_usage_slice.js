#!/usr/bin/env node

const cp = require('child_process');

const listArgs = process.argv.slice(2);

function runNode(args) {
  return cp.spawnSync(process.execPath, args, { encoding: 'utf8' });
}

const inventory = runNode(['scripts/codex/list_jdoc_usage_slice.js', ...listArgs]);

if (inventory.status !== 0) {
  process.stdout.write(inventory.stdout || '');
  process.stderr.write(inventory.stderr || '');
  process.exit(inventory.status || 1);
}

const files = inventory.stdout
  .split(/\r?\n/)
  .map(line => line.trim())
  .filter(Boolean)
  .map(line => line.split(/\t/)[0]);

const checks = [
  ['javadoc_usage_check.js'],
  ['javadoc_placeholder_report.js'],
  ['javadoc_example_comment_report.js'],
  ['javadoc_sample_member_misuse_report.js'],
  ['align_jdoc_examples.js', '--check'],
  ['verify_comment_only_diff.js'],
];

const failures = [];

for (const file of files) {
  for (const check of checks) {
    const args = [`scripts/codex/${check[0]}`, ...check.slice(1), file];
    const result = runNode(args);

    if (result.status !== 0) {
      const output = `${result.stdout || ''}${result.stderr || ''}`.trim();
      failures.push({ file, check: check.join(' '), output });
      break;
    }
  }
}

for (const failure of failures) {
  console.log(`FAIL ${failure.check} ${failure.file}`);
  const lines = failure.output.split(/\r?\n/).slice(0, 30);
  for (const line of lines) console.log(`  ${line}`);
  if (failure.output.split(/\r?\n/).length > lines.length) console.log('  ...');
}

console.log(`VALIDATED_FILE_COUNT ${files.length}`);
console.log(`FAILED_FILE_COUNT ${failures.length}`);

if (failures.length) process.exit(1);
