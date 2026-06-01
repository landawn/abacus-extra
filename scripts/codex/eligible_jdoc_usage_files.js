#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');
const { activeJavadocsContain, countInActiveJavadocs } = require('./javadoc_region');

function stripComments(text) {
  let out = '';
  let inBlock = false;
  let inLine = false;
  let inString = false;
  let inChar = false;
  let escaped = false;

  for (let i = 0; i < text.length; i++) {
    const ch = text[i];
    const next = text[i + 1];

    if (inLine) {
      if (ch === '\n' || ch === '\r') {
        inLine = false;
        out += ch;
      } else {
        out += ' ';
      }
      continue;
    }

    if (inBlock) {
      if (ch === '*' && next === '/') {
        out += '  ';
        i++;
        inBlock = false;
      } else {
        out += ch === '\n' || ch === '\r' ? ch : ' ';
      }
      continue;
    }

    if (escaped) {
      escaped = false;
      out += ' ';
      continue;
    }

    if ((inString || inChar) && ch === '\\') {
      escaped = true;
      out += ' ';
      continue;
    }

    if (!inString && !inChar && ch === '/' && next === '/') {
      out += '  ';
      i++;
      inLine = true;
      continue;
    }

    if (!inString && !inChar && ch === '/' && next === '*') {
      out += '  ';
      i++;
      inBlock = true;
      continue;
    }

    if (!inChar && ch === '"') {
      inString = !inString;
      out += ' ';
      continue;
    }

    if (!inString && ch === "'") {
      inChar = !inChar;
      out += ' ';
      continue;
    }

    out += inString || inChar ? ' ' : ch;
  }

  return out;
}

function packageName(text) {
  const match = text.match(/^\s*package\s+([A-Za-z0-9_.]+)\s*;/m);
  return match ? match[1] : '';
}

function hasPublicTopLevelType(text) {
  const code = stripComments(text);
  return /^\s*public\s+(?:abstract\s+|final\s+|sealed\s+|non-sealed\s+|strictfp\s+)*\b(?:class|interface|enum|record)\b/m.test(code);
}

function inspect(file, options) {
  const text = fs.readFileSync(file, 'utf8');
  const pkg = packageName(text);

  if (pkg === options.excludePackage) return null;
  if (!hasPublicTopLevelType(text)) return null;
  if (/^\s*public\s+@interface\b/m.test(stripComments(text))) return null;
  if (!activeJavadocsContain(text, /(?:Usage Examples|<b>Examples?:|Example usage|<b>Example\b|Typical usage pattern)/i)) return null;

  return {
    file: file.replace(/\\/g, '/'),
    packageName: pkg,
    count: countInActiveJavadocs(text, /<pre>\{@code/g),
  };
}

function walk(dir, options, result) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);

    if (entry.isDirectory()) {
      walk(full, options, result);
    } else if (entry.isFile() && entry.name.endsWith('.java')) {
      const item = inspect(full, options);
      if (item) result.push(item);
    }
  }
}

function collectEligibleFiles(root = 'src/main/java', options = {}) {
  const merged = {
    excludePackage: 'com.landawn.abacus.util',
    ...options,
  };
  const result = [];
  walk(root, merged, result);
  return result.sort((a, b) => a.file.localeCompare(b.file));
}

if (require.main === module) {
  const root = process.argv[2] || 'src/main/java';
  const files = collectEligibleFiles(root);

  for (const item of files) {
    console.log(`${item.file}\t${item.count}\t${item.packageName}`);
  }

  console.error(`ELIGIBLE_USAGE_FILE_COUNT ${files.length}`);
}

module.exports = {
  collectEligibleFiles,
  packageName,
  stripComments,
};
