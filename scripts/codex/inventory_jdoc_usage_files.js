#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocsContain, countInActiveJavadocs } = require('./javadoc_region');

const root = process.argv[2] || 'src/main/java';
const includeStreams = new Set(process.argv.slice(3));
const result = [];

function walk(dir) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      walk(full);
    } else if (entry.isFile() && entry.name.endsWith('.java')) {
      inspect(full);
    }
  }
}

function hasPublicClass(text) {
  return /\bpublic\s+(?:final\s+|abstract\s+|sealed\s+|non-sealed\s+)*class\b/.test(text)
    || /\bpublic\s+record\b/.test(text);
}

function packageName(text) {
  const match = text.match(/^\s*package\s+([A-Za-z0-9_.]+)\s*;/m);
  return match ? match[1] : '';
}

function inspect(file) {
  const normalized = file.replace(/\\/g, '/');
  const text = fs.readFileSync(file, 'utf8');
  const pkg = packageName(text);

  if (!hasPublicClass(text)) return;
  if (/\bpublic\s+@interface\b/.test(text)) return;
  if (pkg === 'com.landawn.abacus.type' || pkg.startsWith('com.landawn.abacus.type.')) return;
  if (pkg === 'com.landawn.abacus.util.function' || pkg.startsWith('com.landawn.abacus.util.function.')) return;
  if ((pkg === 'com.landawn.abacus.util.stream' || pkg.startsWith('com.landawn.abacus.util.stream.'))
      && !includeStreams.has(path.basename(file))) {
    return;
  }

  if (!activeJavadocsContain(text, /(Usage Examples|<b>Examples:|Example usage|<b>Example\b)/)) return;

  const count = countInActiveJavadocs(text, /<pre>\{@code/g);
  result.push({ file: normalized, count });
}

walk(root);

for (const item of result.sort((a, b) => a.file.localeCompare(b.file))) {
  console.log(`${item.file}\t${item.count}`);
}

console.error(`USAGE_FILE_COUNT ${result.length}`);
