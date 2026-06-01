#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/javadoc_usage_check.js <java-file> [java-file...]');
  process.exit(2);
}

let issueCount = 0;

function isBlankJavadocLine(line) {
  return line.trim() === '*';
}

function linePrefix(line) {
  const match = line.match(/^(\s*)\*/);
  return match ? match[1] : null;
}

function report(file, line, type, detail = '') {
  issueCount++;
  console.log(`${file}:${line}: ${type}${detail ? `: ${detail}` : ''}`);
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);

  for (let i = 0; i < lines.length;) {
    if (!isActiveJavadocStart(lines[i])) {
      i++;
      continue;
    }

    const start = i;
    let end = i + 1;
    while (end < lines.length && !lines[end].includes('*/')) {
      end++;
    }

    const block = lines.slice(start, end + 1);
    const usageHeaders = [];

    block.forEach((line, index) => {
      if (line.includes('Usage Examples')) {
        usageHeaders.push(index);
      }
    });

    if (usageHeaders.length > 0) {
      let inPre = false;
      const firstTag = block.findIndex(line => {
        if (line.includes('<pre>{@code')) {
          inPre = true;
          return false;
        }

        if (inPre) {
          if (line.includes('</pre>')) inPre = false;
          return false;
        }

        return /^\s*\* @/.test(line);
      });

      if (firstTag >= 0 && usageHeaders.some(index => index > firstTag)) {
        report(file, start + 1, 'usage_after_tag', `usage lines ${usageHeaders.map(index => start + index + 1).join(', ')}`);
      }

      for (const index of usageHeaders) {
        if (index > 0 && !isBlankJavadocLine(block[index - 1])) {
          report(file, start + index + 1, 'missing_blank_before_usage');
        }

        const prefix = linePrefix(block[index]);

        if (prefix != null) {
          const tagPrefix = block.find(line => /^\s*\* @/.test(line));
          const expectedPrefix = tagPrefix ? linePrefix(tagPrefix) : prefix;

          if (expectedPrefix != null && prefix !== expectedPrefix) {
            report(file, start + index + 1, 'usage_indent_mismatch');
          }
        }
      }

      block.forEach((line, index) => {
        if (line.includes('</pre>') && index + 1 < block.length && !isBlankJavadocLine(block[index + 1])) {
          report(file, start + index + 1, 'missing_blank_after_pre');
        }

        if (/^\s*\*\s+\*\s*<p><b>Usage Examples/.test(line)) {
          report(file, start + index + 1, 'stray_duplicate_star');
        }
      });

      for (let index = 1; index < block.length; index++) {
        if (isBlankJavadocLine(block[index]) && isBlankJavadocLine(block[index - 1])) {
          report(file, start + index + 1, 'double_blank_javadoc_line');
        }
      }
    }

    i = end + 1;
  }
}

if (issueCount > 0) {
  process.exit(1);
}

console.log('No Javadoc Usage Examples formatting issues found.');
