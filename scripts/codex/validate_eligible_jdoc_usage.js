#!/usr/bin/env node
'use strict';

const cp = require('child_process');
const { collectEligibleFiles } = require('./eligible_jdoc_usage_files');

const root = process.argv[2] || 'src/main/java';
const files = collectEligibleFiles(root).map(item => item.file);

const checks = [
  ['javadoc_usage_check.js'],
  ['javadoc_placeholder_report.js'],
  ['javadoc_example_comment_report.js'],
  ['javadoc_missing_call_comment_report.js'],
  ['javadoc_sample_member_misuse_report.js'],
  ['align_jdoc_examples.js', '--check'],
  ['verify_comment_only_diff.js'],
];

function runNode(args) {
  return cp.spawnSync(process.execPath, args, {
    encoding: 'utf8',
    stdio: ['ignore', 'pipe', 'pipe'],
  });
}

const failures = [];

for (const file of files) {
  for (const [script, ...args] of checks) {
    const result = runNode([`scripts/codex/${script}`, ...args, file]);

    if (result.status !== 0) {
      failures.push({
        file,
        check: script,
        output: `${result.stdout || ''}${result.stderr || ''}`.trim(),
      });
      break;
    }
  }
}

for (const failure of failures) {
  console.log(`FAIL ${failure.check} ${failure.file}`);
  const lines = failure.output.split(/\r?\n/).slice(0, 20);
  for (const line of lines) console.log(`  ${line}`);
  if (failure.output.split(/\r?\n/).length > lines.length) console.log('  ...');
}

console.log(`VALIDATED_FILE_COUNT ${files.length}`);
console.log(`FAILED_FILE_COUNT ${failures.length}`);

if (failures.length) {
  process.exit(1);
}
