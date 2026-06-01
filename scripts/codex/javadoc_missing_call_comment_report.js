#!/usr/bin/env node

const fs = require('fs');
const { isActiveJavadocStart } = require('./javadoc_region');

const files = process.argv.slice(2);

if (files.length === 0) {
  console.error('Usage: node scripts/codex/javadoc_missing_call_comment_report.js <java-file> [java-file...]');
  process.exit(2);
}

let count = 0;

function isLikelyCall(code) {
  if (!code.includes('(') || !code.includes(')')) return false;
  if (/^\s*(if|for|while|switch|try|catch|return|throw|new\s|class\s|interface\s|enum\s)\b/.test(code)) return false;
  if (/^\s*[@{}]/.test(code)) return false;
  if (/=/.test(code)) return false;
  if (/^\s*(?:final\s+)?(?:var|void|int|long|boolean|double|float|char|byte|short|String|Object|Class|List|Set|Map|Collection|Iterable|Iterator|Stream|File|Path|Hash|Hasher|HashFunction|EventBus|Logger|Pool|Poolable|ActivityPrint|Traverser|MappedByteBuffer|ExecutorService|[A-Z][\w$<>?, ]+)\s+\w+\b/.test(code)) return false;
  return /^\s*(?:N|CommonUtil)\.[A-Za-z_$][\w$]*\s*\([^;]*\)\s*;?$/.test(code);
}

function hasFollowUpComment(lines, index) {
  const next = lines[index + 1] ? lines[index + 1].trim() : '';
  return /^\*\s*\/\/\s*(?:returns|throws|[a-z_$][\w$]*(?:\[[^\]]+\])?\s+(?:is|are|contains|has|becomes)|no exception thrown|no change|same instance|empty result|unchanged)\b/.test(next);
}

function nextDeclaration(lines, end) {
  for (let i = end + 1; i < lines.length; i++) {
    const line = lines[i].trim();
    if (!line) continue;
    return line;
  }

  return '';
}

function isCallableDeclaration(line) {
  return /^(?:@\w+(?:\([^)]*\))?\s*)*(?:public|protected)\s+/.test(line)
    && line.includes('(')
    && !/\b(class|interface|enum|record)\b/.test(line);
}

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8').split(/\r?\n/);

  for (let start = 0; start < lines.length; start++) {
    if (!isActiveJavadocStart(lines[start])) continue;

    let end = start + 1;
    while (end < lines.length && !lines[end].includes('*/')) end++;

    if (!isCallableDeclaration(nextDeclaration(lines, end))) {
      start = end;
      continue;
    }

    let inPre = false;

    for (let index = start; index <= end; index++) {
      const line = lines[index];

      if (line.includes('<pre>{@code')) {
        inPre = true;
        continue;
      }

      if (!inPre) continue;

      if (line.includes('</pre>')) {
        inPre = false;
        continue;
      }

      const match = line.match(/^\s*\*\s?(.*)$/);
      if (!match) continue;

      const code = match[1].trim();
      if (!code || code.startsWith('//') || code.startsWith('*')) continue;
      if (/\/\//.test(code)) continue;
      if (hasFollowUpComment(lines, index)) continue;
      if (isLikelyCall(code)) {
        count++;
        console.log(`${file}:${index + 1}: ${line}`);
      }
    }

    start = end;
  }
}

console.log(`MISSING_CALL_COMMENT_COUNT ${count}`);
process.exit(count === 0 ? 0 : 1);
