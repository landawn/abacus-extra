#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocsContain, countInActiveJavadocs } = require('./javadoc_region');

const root = process.argv[2] || 'src/main/java/com/landawn/abacus/util';
const start = (process.argv[3] || 'AddrUtil.java').toLowerCase();
const end = (process.argv[4] || 'Charsets.java').toLowerCase();
const excludes = new Set((process.argv[5] || 'CommonUtil.java').split(',').map(s => s.trim()).filter(Boolean));

const usagePattern = /(Usage Examples|<b>Examples:|Example usage|<b>Example\b)/;
const publicClassPattern = /\bpublic\s+(?:final\s+|abstract\s+|sealed\s+|non-sealed\s+)*class\b|\bpublic\s+record\b/;

for (const name of fs.readdirSync(root).filter(name => name.endsWith('.java')).sort((a, b) => a.toLowerCase().localeCompare(b.toLowerCase()))) {
  const lower = name.toLowerCase();

  if (lower < start || lower > end || excludes.has(name)) continue;

  const file = path.join(root, name).replace(/\\/g, '/');
  const text = fs.readFileSync(file, 'utf8');

  if (publicClassPattern.test(text) && !/\bpublic\s+@interface\b/.test(text) && activeJavadocsContain(text, usagePattern)) {
    const count = countInActiveJavadocs(text, /<pre>\{@code/g);
    console.log(`${file}\t${count}`);
  }
}
