#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocLineMask, activeJavadocsContain, replaceInActiveJavadocs } = require('./javadoc_region');

const roots = process.argv.slice(2);

if (roots.length === 0) {
  console.error('Usage: node scripts/codex/cleanup_owned_javadoc_examples.js <path> [path...]');
  process.exit(2);
}

const ownedPrefixes = [
  'src/main/java/com/landawn/abacus/http/',
  'src/main/java/com/landawn/abacus/parser/',
  'src/main/java/com/landawn/abacus/poi/',
];

function normalizePath(file) {
  return file.replace(/\\/g, '/');
}

function isOwned(file) {
  const normalized = normalizePath(file);
  return ownedPrefixes.some(prefix => normalized.startsWith(prefix));
}

function walk(input, files) {
  const stat = fs.statSync(input);
  if (stat.isDirectory()) {
    for (const entry of fs.readdirSync(input, { withFileTypes: true })) {
      walk(path.join(input, entry.name), files);
    }
  } else if (stat.isFile() && input.endsWith('.java')) {
    files.push(input);
  }
}

function normalizeInlineComment(line) {
  const match = line.match(/^(\s*\*\s+)(.*?)(\s*)\/\/\s*(.*)$/);
  if (!match) return line;

  const [, prefix, code, spacing, rawComment] = match;
  const before = code.trim();
  const comment = rawComment.trim();

  if (!before || /^(returns|throws)\b/.test(comment)) return line;

  if (/^[-+]?\d/.test(comment)
      || /^(true|false|null)\b/.test(comment)
      || /^["'\[{]/.test(comment)
      || /^e\.g\./.test(comment)
      || /^empty\b/i.test(comment)) {
    return `${prefix}${code}${spacing}// returns ${comment}`;
  }

  if (/\b(?:set|add|remove|clear|header|queryParameter|formParameter|cookie|use|connectTimeout|readTimeout|connectionTimeout)\w*\s*\(/.test(before)
      || /^\.\w+\s*\(/.test(before)) {
    return `${prefix}${code}${spacing}// returns this`;
  }

  if (/\b(?:post|put|patch|delete|get|head|options|execute|async\w*)\s*\(/i.test(before)) {
    return `${prefix}${code}${spacing}// returns response`;
  }

  return `${prefix}${before}`;
}

function hasUsageHeader(lines, index) {
  return /Usage Examples|<b>Examples:|Example usage|<b>Example\b|<p>Usage Examples:/.test(lines[index]);
}

function fixBlankSeparators(lines) {
  const out = [];
  const activeJavadoc = activeJavadocLineMask(lines);

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    if (activeJavadoc[i] && hasUsageHeader(lines, i)) {
      const blank = line.match(/^(\s*)\*\s*</);
      if (blank) {
        const blankLine = `${blank[1]}*`;
        while (out.length && out[out.length - 1] === blankLine && out[out.length - 2] === blankLine) {
          out.pop();
        }
        if (out.length === 0 || out[out.length - 1] !== blankLine) {
          out.push(blankLine);
        }
      }
      out.push(line);
      continue;
    }

    out.push(line);

    if (activeJavadoc[i] && line.includes('</pre>')) {
      const match = line.match(/^(\s*)\*.*<\/pre>/);
      if (!match) continue;
      const blankLine = `${match[1]}*`;
      const next = lines[i + 1];
      if (next !== blankLine) {
        out.push(blankLine);
        while (lines[i + 1] === blankLine) i++;
      }
    }
  }
  return out;
}

function normalizeCodeBlock(lines) {
  let inCode = false;
  const activeJavadoc = activeJavadocLineMask(lines);
  const out = [];

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];

    if (!activeJavadoc[i]) {
      inCode = false;
      out.push(line);
      continue;
    }

    if (line.includes('<pre>{@code')) {
      inCode = true;
      out.push(line);
      continue;
    }

    if (inCode && line.includes('</pre>')) {
      inCode = false;
      out.push(line);
      continue;
    }

    if (inCode) {
      out.push(normalizeInlineComment(line));
    } else {
      out.push(line);
    }
  }

  return out;
}

function replaceKnownPlaceholders(text) {
  let out = text;
  out = replaceInActiveJavadocs(out, /String base64Data = "\.\.\.";\s*\/\/ Base64 encoded Avro data/g, 'String base64Data = parser.serialize(new User("John", 30), new AvroSerConfig().setSchema(User.getSchema()));');
  out = replaceInActiveJavadocs(out, /String base64Data = "rO0ABXNyABF\.\.\.";\s*\/\/ Base64 encoded/g, 'String base64Data = parser.serialize(new User("John", 30));');
  out = replaceInActiveJavadocs(out, /\/\/ \.\.\. use output \.\.\./g, 'output.write(new byte[] {1, 2, 3}); // returns void');
  out = replaceInActiveJavadocs(out, /\/\/ \.\.\. use input \.\.\./g, 'int firstByte = input.read(); // returns first byte or -1');
  out = replaceInActiveJavadocs(out, /PropInfo propInfo = \.\.\./g, 'PropInfo propInfo = beanInfo.getPropInfo("name")');
  out = replaceInActiveJavadocs(out, /PropInfo nameProp = \.\.\./g, 'PropInfo nameProp = beanInfo.getPropInfo("name")');
  out = replaceInActiveJavadocs(out, /PropInfo ageProp = \.\.\./g, 'PropInfo ageProp = beanInfo.getPropInfo("age")');
  out = replaceInActiveJavadocs(out, /PropInfo dateProp = \.\.\./g, 'PropInfo dateProp = beanInfo.getPropInfo("createdAt")');
  out = replaceInActiveJavadocs(out, /PropInfo longDateProp = \.\.\./g, 'PropInfo longDateProp = beanInfo.getPropInfo("createdAtMillis")');
  return out;
}

let changed = 0;

for (const root of roots) {
  const files = [];
  walk(root, files);

  for (const file of files) {
    if (!isOwned(file)) continue;
    let text = fs.readFileSync(file, 'utf8');
    if (!activeJavadocsContain(text, /(Usage Examples|<b>Examples:|Example usage|<b>Example\b|<p>Usage Examples:|<pre>\{@code)/)) continue;

    const original = text;
    text = replaceKnownPlaceholders(text);
    let lines = text.split(/\r?\n/);
    lines = normalizeCodeBlock(lines);
    lines = fixBlankSeparators(lines);
    text = lines.join('\n');

    if (text !== original) {
      fs.writeFileSync(file, text, 'utf8');
      changed++;
      console.log(file);
    }
  }
}

console.log(`CHANGED_FILE_COUNT ${changed}`);
