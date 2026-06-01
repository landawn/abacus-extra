#!/usr/bin/env node
'use strict';

const cp = require('child_process');
const { collectEligibleFiles } = require('./eligible_jdoc_usage_files');

const root = process.argv[2] || 'src/main/java';
const files = collectEligibleFiles(root).map(item => item.file);

if (files.length === 0) {
  console.log('ELIGIBLE_USAGE_FILE_COUNT 0');
  process.exit(0);
}

const applyScripts = [
  ['move_jdoc_usage_before_tags.js', '--apply'],
  ['fix_jdoc_usage_indent.js'],
  ['fix_jdoc_usage_spacing.js'],
  ['fix_jdoc_double_blank_lines.js', '--apply'],
  ['replace_jdoc_type_placeholders.js', '--apply'],
  ['fix_jdoc_literal_comment_artifacts.js', '--apply'],
  ['fix_javadoc_returns_void_comments.js', '--apply'],
  ['fix_javadoc_sample_member_misuse.js', '--apply'],
  ['normalize_jdoc_literal_action_comments.js', '--apply'],
  ['align_jdoc_examples.js', '--apply'],
];

function runNode(args) {
  const result = cp.spawnSync(process.execPath, args, {
    encoding: 'utf8',
    stdio: ['ignore', 'pipe', 'pipe'],
  });

  if (result.stdout) process.stdout.write(result.stdout);
  if (result.stderr) process.stderr.write(result.stderr);

  if (result.status !== 0) {
    process.exit(result.status || 1);
  }
}

console.log(`ELIGIBLE_USAGE_FILE_COUNT ${files.length}`);

for (const [script, ...args] of applyScripts) {
  console.log(`RUN ${script}`);
  runNode([`scripts/codex/${script}`, ...args, ...files]);
}
