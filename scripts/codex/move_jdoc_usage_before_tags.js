#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const apply = process.argv.includes('--apply');
const files = process.argv.filter(arg => !arg.startsWith('-')).slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/move_jdoc_usage_before_tags.js [--apply] <java-file> [java-file...]');
  process.exit(2);
}

function isBlank(line) {
  return line.trim() === '*';
}

function firstTagOutsidePre(block) {
  let inPre = false;

  for (let i = 0; i < block.length; i++) {
    const line = block[i];

    if (line.includes('<pre>{@code')) {
      inPre = true;
      continue;
    }

    if (inPre) {
      if (line.includes('</pre>')) inPre = false;
      continue;
    }

    if (/^\s*\* @/.test(line)) return i;
  }

  return -1;
}

function usageRange(block, firstTag) {
  const header = block.findIndex((line, index) => index > firstTag && line.includes('Usage Examples'));
  if (header < 0) return null;

  let start = header;
  if (start > 0 && isBlank(block[start - 1])) start--;

  let end = header;
  while (end < block.length && !block[end].includes('</pre>')) end++;
  if (end >= block.length) return null;
  if (end + 1 < block.length && isBlank(block[end + 1])) end++;

  return { start, end };
}

function fixBlock(block) {
  const firstTag = firstTagOutsidePre(block);
  if (firstTag < 0) return { block, changed: false };

  const range = usageRange(block, firstTag);
  if (!range) return { block, changed: false };

  const moved = block.slice(range.start, range.end + 1);
  const remaining = block.slice(0, range.start).concat(block.slice(range.end + 1));
  const insertionTag = firstTagOutsidePre(remaining);
  const next = remaining.slice(0, insertionTag).concat(moved, remaining.slice(insertionTag));

  return { block: next, changed: true };
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  const out = [];
  let changed = false;

  for (let i = 0; i < lines.length;) {
    if (!isActiveJavadocStart(lines[i])) {
      out.push(lines[i++]);
      continue;
    }

    const start = i;
    let end = i + 1;
    while (end < lines.length && !lines[end].includes('*/')) end++;

    const block = lines.slice(start, end + 1);
    const fixed = fixBlock(block);
    out.push(...fixed.block);
    changed ||= fixed.changed;
    i = end + 1;
  }

  if (changed) {
    if (apply) fs.writeFileSync(file, out.join('\n'), 'utf8');
    console.log(`${file}: ${apply ? 'moved usage block(s)' : 'would move usage block(s)'}`);
  }
}
