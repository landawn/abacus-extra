#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocLineMask } = require('./javadoc_region');

const args = process.argv.slice(2);
const apply = args.includes('--apply');
const quiet = args.includes('--quiet') || args.includes('-q');
const targets = args.filter(arg => !arg.startsWith('-'));

if (targets.length === 0) {
  console.error('Usage: node scripts/codex/align_jdoc_examples.js [--check|--apply] PATH [PATH ...]');
  process.exit(2);
}

function charWidth(ch) {
  return ch.codePointAt(0) > 0xffff ? 2 : 1;
}

function displayWidth(text) {
  let width = 0;
  for (const ch of text) {
    width += charWidth(ch);
  }
  return width;
}

function scanTokens(rest) {
  const tokens = [];
  let inString = null;
  let depth = 0;

  for (let i = 0; i < rest.length; i++) {
    const ch = rest[i];

    if (inString) {
      if (ch === '\\') {
        i++;
        continue;
      }
      if (ch === inString) inString = null;
      continue;
    }

    if (ch === '"' || ch === "'") {
      inString = ch;
    } else if (ch === '(' || ch === '[' || ch === '{') {
      depth++;
    } else if (ch === ')' || ch === ']' || ch === '}') {
      depth--;
    } else if (ch === '/' && rest[i + 1] === '/') {
      tokens.push(['//', i]);
      break;
    } else if (ch === '=' && depth === 0 && rest[i - 1] === ' ' && rest[i + 1] === ' ') {
      tokens.push(['=', i]);
    } else if (ch === ';' && depth === 0) {
      tokens.push([';', i]);
    }
  }

  return tokens;
}

function classify(rest) {
  let hasSemicolon = false;
  let eqIndex = null;

  for (const [kind, index] of scanTokens(rest)) {
    if (kind === '//') {
      return index > 0 && rest.slice(0, index).trim() ? ['//', index] : null;
    }

    if (kind === ';') {
      hasSemicolon = true;
    } else if (kind === '=' && eqIndex === null) {
      eqIndex = index;
    }
  }

  if (eqIndex !== null && !hasSemicolon && rest.slice(0, eqIndex).trim()) {
    return ['=', eqIndex];
  }

  return null;
}

function splitLine(line) {
  const eolMatch = line.match(/(\r?\n)$/);
  const eol = eolMatch ? eolMatch[1] : '';
  const content = eol ? line.slice(0, -eol.length) : line;
  const match = content.match(/^(\s*\*[ \t]?)(.*)$/);

  if (!match) return null;

  const prefix = match[1];
  const rest = match[2];
  const classified = classify(rest);

  if (!classified) return null;

  const [kind, index] = classified;
  const code = rest.slice(0, index).replace(/[ \t]+$/, '');

  if (!code) return null;

  const gap = rest.slice(0, index).length - code.length;
  return { prefix, code, gap, tail: rest.slice(index), kind, eol };
}

function realign(lines) {
  let inCode = false;
  const activeJavadoc = activeJavadocLineMask(lines);
  const parsed = Array(lines.length).fill(null);

  for (let i = 0; i < lines.length; i++) {
    const content = lines[i].replace(/\r?\n$/, '');

    if (!activeJavadoc[i]) {
      inCode = false;
      continue;
    }

    if (!inCode) {
      const codeIndex = content.indexOf('{@code');
      if (codeIndex >= 0 && !content.slice(codeIndex + '{@code'.length).includes('}')) {
        inCode = true;
      }
      continue;
    }

    if (content.includes('</pre>') || /^\s*\*\s*\}\s*$/.test(content)) {
      inCode = false;
      continue;
    }

    parsed[i] = splitLine(lines[i]);
  }

  const changed = [];
  const nextLines = lines.slice();

  for (let i = 0; i < lines.length;) {
    if (!parsed[i]) {
      i++;
      continue;
    }

    const kind = parsed[i].kind;
    let j = i;
    while (j < lines.length && parsed[j] && parsed[j].kind === kind) j++;

    const group = [];
    for (let k = i; k < j; k++) group.push(k);

    const baseGap = Math.max(1, Math.min(...group.map(k => parsed[k].gap)));
    const target = Math.max(...group.map(k => displayWidth(parsed[k].code))) + baseGap;

    for (const k of group) {
      const item = parsed[k];
      const pad = Math.max(1, target - displayWidth(item.code));
      const rebuilt = item.prefix + item.code + ' '.repeat(pad) + item.tail + item.eol;
      if (rebuilt !== lines[k]) {
        nextLines[k] = rebuilt;
        changed.push(k);
      }
    }

    i = j;
  }

  return { nextLines, changed };
}

function javaFiles(target) {
  const stat = fs.statSync(target);
  if (!stat.isDirectory()) return [target];

  const result = [];
  for (const name of fs.readdirSync(target)) {
    const child = path.join(target, name);
    const childStat = fs.statSync(child);
    if (childStat.isDirectory()) {
      result.push(...javaFiles(child));
    } else if (name.endsWith('.java')) {
      result.push(child);
    }
  }
  return result;
}

let totalFiles = 0;
let totalChanged = 0;
let filesChanged = 0;

for (const target of targets) {
  for (const file of javaFiles(target)) {
    if (!fs.existsSync(file) || !fs.statSync(file).isFile()) {
      console.error(`skip (not a file): ${file}`);
      continue;
    }

    totalFiles++;
    const text = fs.readFileSync(file, 'utf8');
    const lines = text.match(/[^\n]*(?:\n|$)/g).filter((line, index, arr) => index < arr.length - 1 || line.length > 0);
    const { nextLines, changed } = realign(lines);

    if (changed.length && apply) {
      fs.writeFileSync(file, nextLines.join(''), 'utf8');
    }

    if (changed.length) {
      filesChanged++;
      totalChanged += changed.length;
      if (!quiet) {
        console.log(`${file}: ${apply ? 're-aligned' : 'would re-align'} ${changed.length} line(s)`);
      }
    }
  }
}

console.log(`\n${totalFiles} file(s) scanned; ${apply ? 're-aligned' : 'would re-align'} ${totalChanged} line(s) across ${filesChanged} file(s).`);

if (!apply && totalChanged) {
  process.exit(1);
}
