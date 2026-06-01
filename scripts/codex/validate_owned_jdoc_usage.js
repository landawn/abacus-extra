#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');
const { activeJavadocsContain } = require('./javadoc_region');

const roots = process.argv.slice(2);

if (roots.length === 0) {
  console.error('Usage: node scripts/codex/validate_owned_jdoc_usage.js <path> [path...]');
  process.exit(2);
}

const validators = [
  ['javadoc_usage_check.js'],
  ['javadoc_placeholder_report.js'],
  ['javadoc_example_comment_report.js'],
  ['javadoc_sample_member_misuse_report.js'],
  ['align_jdoc_examples.js', '--check'],
  ['verify_comment_only_diff.js'],
];

function walk(p, out) {
  const stat = fs.statSync(p);

  if (stat.isDirectory()) {
    for (const entry of fs.readdirSync(p)) {
      walk(path.join(p, entry), out);
    }
  } else if (p.endsWith('.java')) {
    const text = fs.readFileSync(p, 'utf8');

    if (/\bpublic\s+@interface\b/.test(text)) {
      return;
    }

    if (activeJavadocsContain(text, /(Usage Examples|<b>Examples:|Example usage|<b>Example\b)/)) {
      out.push(p);
    }
  }
}

const files = [];
for (const root of roots) {
  if (fs.existsSync(root)) {
    walk(root, files);
  }
}

let failures = 0;

for (const file of files.sort()) {
  for (const [script, ...args] of validators) {
    const result = spawnSync(process.execPath, [path.join('scripts', 'codex', script), ...args, file], {
      cwd: process.cwd(),
      encoding: 'utf8',
    });

    if (result.status !== 0) {
      failures += 1;
      process.stdout.write(`${file}: ${script} failed\n`);
      if (result.stdout) process.stdout.write(result.stdout);
      if (result.stderr) process.stdout.write(result.stderr);
    }
  }
}

console.log(`VALIDATED_FILES ${files.length}`);
console.log(`VALIDATION_FAILURES ${failures}`);
process.exit(failures === 0 ? 0 : 1);
