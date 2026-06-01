#!/usr/bin/env node

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/javadoc_example_comment_report.js <java-file> [java-file...]');
  process.exit(2);
}

const issues = [];

function commentIndexOutsideString(code) {
  let quote = null;

  for (let i = 0; i < code.length; i++) {
    const ch = code[i];

    if (quote) {
      if (ch === '\\') {
        i++;
      } else if (ch === quote) {
        quote = null;
      }

      continue;
    }

    if (ch === '"' || ch === "'") {
      quote = ch;
    } else if (ch === '/' && code[i + 1] === '/') {
      return i;
    }
  }

  return -1;
}

function isAcceptableComment(comment) {
  const text = comment.replace(/^\/\/\s*/, '').trim();

  if (/^(returns|throws)\b/.test(text)) {
    return !/^returns\s+void\s*$/i.test(text);
  }

  if (/^(true|false|null)\b/.test(text)) return true;
  if (/^(Optional(?:Boolean|Char|Byte|Short|Int|Long|Float|Double)?|Nullable)\b/.test(text)) return true;
  if (/^(-?\d+(?:\.\d+)?(?:[a-zA-Z])?|\[[^\]]*\]|\{[^}]*\}|"[^"]*"|'[^']*')/.test(text)) return true;

  if (/^[a-z_$][\w$]*(?:\[[^\]]+\])?\s+(?:is|are|contains|has|becomes)\b/.test(text)) return true;
  if (/^(converts|adds|removes|fills|sorts|reverses|rotates|shuffles|copies|pads|prints|keeps|leaves|uses|treats|searches|starts|ends|invokes|transforms|collects|processes|sums|filters|switches|maintains|stops|logs|buffers|inserts|gets|reuses|waits|creates|closes|maps|contains)\b/.test(text)) return true;
  if (/^[a-z_$][\w$]*(?:\[[^\]]+\])?\s+(?:is|are|contains|has|holds|becomes)\b/.test(text)) return true;
  if (/^(no change|no exception thrown|same instance|empty result|unchanged|case-sensitive|case-insensitive)\b/.test(text)) return true;

  return false;
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inCode = false;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    if (!activeJavadoc[i]) {
      inCode = false;
      continue;
    }
    if (!inCode && line.includes('<pre>{@code')) {
      inCode = true;
      continue;
    }
    if (inCode && line.includes('</pre>')) {
      inCode = false;
      continue;
    }
    if (!inCode) continue;

    const match = line.match(/^\s*\*\s+(.*)$/);
    if (!match) continue;

    const code = match[1];
    const commentIndex = commentIndexOutsideString(code);
    if (commentIndex < 0) continue;

    const before = code.slice(0, commentIndex).trim();
    const comment = code.slice(commentIndex).trim();

    if (!before || before.startsWith('//')) continue;
    if (isAcceptableComment(comment)) continue;
    issues.push(`${file}:${i + 1}: * ${code}`);
  }
}

for (const issue of issues) {
  console.log(issue);
}

console.log(`BAD_EXAMPLE_COMMENT_COUNT ${issues.length}`);
if (issues.length) process.exit(1);
