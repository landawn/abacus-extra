#!/usr/bin/env node
'use strict';

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

function usage() {
  console.error('Usage: node scripts/codex/move_jdoc_sample_comments_into_pre.js [--apply] <Java file> [<Java file>...]');
}

const args = process.argv.slice(2);
const apply = args[0] === '--apply';
const files = apply ? args.slice(1) : args;

if (files.length === 0) {
  usage();
  process.exit(2);
}

function isSampleComment(line) {
  return /^\s*\*\s+\/\/\s+\S/.test(line);
}

function blankFor(line) {
  const match = line.match(/^(\s*)\*/);
  return `${match ? match[1] : '     '}*`;
}

let total = 0;

for (const file of files) {
  const original = fs.readFileSync(file, 'utf8');
  const newline = original.includes('\r\n') ? '\r\n' : '\n';
  const lines = original.split(/\r?\n/);
  let changed = false;

  for (let i = 0; i < lines.length; i++) {
    const activeJavadoc = activeJavadocLineMask(lines);
    if (!activeJavadoc[i]) {
      continue;
    }

    if (!lines[i].includes('Usage Examples')) {
      continue;
    }

    const commentsBeforePre = [];
    let preIndex = -1;

    for (let j = i + 1; j < Math.min(lines.length, i + 20) && activeJavadoc[j]; j++) {
      if (lines[j].includes('<pre>{@code')) {
        preIndex = j;
        break;
      }

      if (isSampleComment(lines[j])) {
        commentsBeforePre.push({ index: j, line: lines[j] });
      }
    }

    if (preIndex < 0) {
      continue;
    }

    for (let j = i - 1; j >= 0 && activeJavadoc[j] && isSampleComment(lines[j]); j--) {
      commentsBeforePre.unshift({ index: j, line: lines[j] });
    }

    let endPreIndex = preIndex + 1;
    while (endPreIndex < lines.length && activeJavadoc[endPreIndex] && !lines[endPreIndex].includes('}</pre>')) {
      endPreIndex++;
    }

    if (endPreIndex >= lines.length || !activeJavadoc[endPreIndex]) {
      continue;
    }

    const commentsAfterPre = [];

    for (let j = endPreIndex + 1; j < lines.length && activeJavadoc[j] && isSampleComment(lines[j]); j++) {
      commentsAfterPre.push({ index: j, line: lines[j] });
    }

    const comments = [...commentsBeforePre, ...commentsAfterPre];

    if (comments.length === 0) {
      continue;
    }

    for (const comment of commentsBeforePre) {
      console.log(`${file}:${comment.index + 1}: move before pre ${comment.line.trim()}`);
    }

    for (const comment of commentsAfterPre) {
      console.log(`${file}:${comment.index + 1}: move before close ${comment.line.trim()}`);
    }

    total += comments.length;

    if (apply) {
      for (let c = comments.length - 1; c >= 0; c--) {
        lines.splice(comments[c].index, 1);
      }

      const removedBeforePre = comments.filter(comment => comment.index < preIndex).length;
      const adjustedPreIndex = preIndex - removedBeforePre;
      const removedBeforeEndPre = comments.filter(comment => comment.index < endPreIndex).length;
      let adjustedEndPreIndex = endPreIndex - removedBeforeEndPre;
      let insertIndex = adjustedPreIndex + 1;

      if (/^\s*\*\s*$/.test(lines[insertIndex] || '')) {
        lines.splice(insertIndex, 1);
        adjustedEndPreIndex--;
      }

      if (commentsBeforePre.length > 0) {
        lines.splice(insertIndex, 0, ...commentsBeforePre.map(comment => comment.line));
        adjustedEndPreIndex += commentsBeforePre.length;
      }

      if (commentsAfterPre.length > 0) {
        const closeInsert = adjustedEndPreIndex;
        if (closeInsert > 0 && !/^\s*\*\s*$/.test(lines[closeInsert - 1])) {
          lines.splice(closeInsert, 0, blankFor(lines[closeInsert]));
          adjustedEndPreIndex++;
        }

        lines.splice(adjustedEndPreIndex, 0, ...commentsAfterPre.map(comment => comment.line));
      }

      changed = true;
      i = insertIndex + commentsBeforePre.length;
    }
  }

  if (apply && changed) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
  }
}

console.log(`MOVED_JDOC_SAMPLE_COMMENT_COUNT ${total}`);
if (!apply && total > 0) {
  process.exit(1);
}
