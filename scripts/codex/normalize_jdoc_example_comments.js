#!/usr/bin/env node

const fs = require('fs');
const { activeJavadocLineMask } = require('./javadoc_region');

const args = process.argv.slice(2);
const apply = args.includes('--apply');
const files = args.filter(arg => arg !== '--apply');
if (files.length === 0) {
  console.error('Usage: node scripts/codex/normalize_jdoc_example_comments.js [--apply] <java-file> [...]');
  process.exit(2);
}

function normalizeComment(comment) {
  let c = comment;
  c = c.replace(/^\/\/\s*returns\s+throws\b/i, '// throws');
  c = c.replace(/^\/\/\s*-->\s*/i, '// returns ');
  c = c.replace(/^\/\/\s*Returns\b/, '// returns');
  c = c.replace(/^\/\/\s*Return\b/, '// returns');
  c = c.replace(/^\/\/\s*Throws\b/, '// throws');
  c = c.replace(/^\/\/\s*Throw\b/, '// throws');
  c = c.replace(/^\/\/\s*Prints?:\s*/i, '// returns void; prints ');
  c = c.replace(/^\/\/\s*Creates?\s+/i, '// returns ');
  c = c.replace(/^\/\/\s*Same\s+/i, '// returns same ');
  c = c.replace(/^\/\/\s*([\[{].*)$/, '// returns $1');
  c = c.replace(/^\/\/\s*(true|false|null)\b/i, (_, value) => `// returns ${value.toLowerCase()}`);
  c = c.replace(/^\/\/\s*(\[[^\]]*\]|\{[^}]*\}|\"[^\"]*\"|'.*'|-?\d[\w.]*)\b/, '// returns $1');
  return c;
}

function shouldDropComment(code, comment) {
  const trimmed = code.trim();
  const c = comment.trim();

  if (/^\/\/\s*(\d+\s*(bytes?|bits?|MB|KB)|seed\s*=|Limit\b|Hash by\b|Compare by\b|Custom\b|hasNext logic|output logic|method may throw|Recursive\b|Skip\b|Non-recursive\b|Fallback\b|Recovery\b|May throw\b|Only called\b|Default\b|row\b|col\b|column\b|group by\b|aggregate\b|result column\b|transform function\b|Duplicate\b|initial state\b|continue while\b|generate next\b|supplier\b|next\b|hasNext\b|Bottom\b|Top\b|different\b|another\b|Automatic\b|closed\b|weather may be null\b|Just verify\b|equal ranges\b|Changed\b|Renamed\b|New field\b|Maps to\b)/i.test(c)) {
    return true;
  }

  if (/[,({[\]}]\s*$/.test(trimmed) || /^\s*(return|if|})\b/.test(trimmed) || /^\s*[\w.]+\s*->/.test(trimmed) || /^\s*[\w()[\]."' -]+,?\s*$/.test(trimmed)) {
    return true;
  }

  if (/^\w[\w<>\[\], ?]*\s+\w+\s*=/.test(trimmed) && !/[A-Z][A-Za-z0-9_]*\s*\./.test(trimmed) && !/\.\w+\s*\(/.test(trimmed)) {
    return true;
  }

  if (/^[),;]*$/.test(trimmed)) {
    return true;
  }

  return false;
}

function commentIndexOutsideLiterals(line) {
  let inString = false;
  let inChar = false;
  let escaped = false;

  for (let i = 0; i < line.length - 1; i++) {
    const ch = line[i];
    if (escaped) {
      escaped = false;
      continue;
    }
    if (ch === '\\') {
      escaped = true;
      continue;
    }
    if (!inChar && ch === '"') {
      inString = !inString;
      continue;
    }
    if (!inString && ch === "'") {
      inChar = !inChar;
      continue;
    }
    if (!inString && !inChar && ch === '/' && line[i + 1] === '/') {
      return i;
    }
  }

  return -1;
}

function isStandaloneSampleCommentLine(line, commentIndex) {
  return /^\s*\*\s*$/.test(line.slice(0, commentIndex));
}

let changedCount = 0;

for (const file of files) {
  const text = fs.readFileSync(file, 'utf8');
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const lines = text.split(/\r?\n/);
  const activeJavadoc = activeJavadocLineMask(lines);
  let inCode = false;
  let changed = false;

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

    const idx = commentIndexOutsideLiterals(line);
    if (idx < 0) continue;
    if (isStandaloneSampleCommentLine(line, idx)) continue;

    const before = line.slice(0, idx);
    const comment = line.slice(idx);
    const code = before.replace(/^\s*\*\s?/, '');
    const normalized = normalizeComment(comment);

    if (/^\/\/\s+(returns|throws)\b/.test(normalized.trim())) {
      if (normalized !== comment) {
        lines[i] = before + normalized;
        changed = true;
        changedCount++;
      }
      continue;
    }

    if (shouldDropComment(code, normalized)) {
      lines[i] = before.replace(/\s+$/, '');
      changed = true;
      changedCount++;
      continue;
    }

    if (code.includes('=') || /\.\w+\s*\(/.test(code)) {
      lines[i] = before + normalized.replace(/^\/\/\s*/, '// returns ');
      changed = true;
      changedCount++;
    }
  }

  if (apply && changed) {
    fs.writeFileSync(file, lines.join(newline), 'utf8');
  }
}

console.log(`NORMALIZED_JDOC_EXAMPLE_COMMENTS ${changedCount}`);
if (!apply && changedCount > 0) {
  process.exit(1);
}
