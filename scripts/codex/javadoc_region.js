'use strict';

function isActiveJavadocStart(line) {
  return /^\s*\/\*\*/.test(line);
}

function isJavadocEnd(line) {
  return line.includes('*/');
}

function activeJavadocLineMask(lines) {
  const mask = new Array(lines.length).fill(false);
  let inJavadoc = false;

  for (let i = 0; i < lines.length; i++) {
    if (!inJavadoc && isActiveJavadocStart(lines[i])) {
      inJavadoc = true;
    }

    if (inJavadoc) {
      mask[i] = true;
    }

    if (inJavadoc && isJavadocEnd(lines[i])) {
      inJavadoc = false;
    }
  }

  return mask;
}

function replaceInActiveJavadocs(text, search, replacement) {
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const normalized = text.replace(/\r\n/g, '\n');
  const lines = normalized.split('\n');
  const mask = activeJavadocLineMask(lines);
  const activeRanges = [];
  let offset = 0;

  for (let i = 0; i < lines.length; i++) {
    const nextOffset = offset + lines[i].length;
    if (mask[i]) {
      activeRanges.push([offset, nextOffset]);
    }

    offset = nextOffset + 1;
  }

  function startsInActiveJavadoc(index) {
    return activeRanges.some(([start, end]) => index >= start && index < end);
  }

  let updated;

  if (typeof search === 'string') {
    let cursor = 0;
    let index = normalized.indexOf(search, cursor);
    const parts = [];

    while (index >= 0) {
      if (startsInActiveJavadoc(index)) {
        parts.push(normalized.slice(cursor, index), replacement);
        cursor = index + search.length;
      } else {
        parts.push(normalized.slice(cursor, index + search.length));
        cursor = index + search.length;
      }

      index = normalized.indexOf(search, cursor);
    }

    parts.push(normalized.slice(cursor));
    updated = parts.join('');
  } else {
    const flags = search.global ? search.flags : `${search.flags}g`;
    const globalSearch = new RegExp(search.source, flags);
    const singleSearch = new RegExp(search.source, flags.replace(/g/g, ''));
    let cursor = 0;
    const parts = [];
    let match;

    while ((match = globalSearch.exec(normalized)) !== null) {
      if (match[0].length === 0) {
        globalSearch.lastIndex++;
        continue;
      }

      if (startsInActiveJavadoc(match.index)) {
        parts.push(normalized.slice(cursor, match.index), match[0].replace(singleSearch, replacement));
        cursor = match.index + match[0].length;
      }
    }

    parts.push(normalized.slice(cursor));
    updated = parts.join('');
  }

  return updated.replace(/\n/g, newline);
}

function activeJavadocsContain(text, pattern) {
  const lines = text.split(/\r?\n/);

  for (let i = 0; i < lines.length;) {
    if (!isActiveJavadocStart(lines[i])) {
      i++;
      continue;
    }

    const start = i;
    let end = i;
    while (end < lines.length && !isJavadocEnd(lines[end])) {
      end++;
    }

    pattern.lastIndex = 0;
    if (pattern.test(lines.slice(start, Math.min(end + 1, lines.length)).join('\n'))) {
      return true;
    }

    i = Math.min(end + 1, lines.length);
  }

  return false;
}

function countInActiveJavadocs(text, pattern) {
  const lines = text.split(/\r?\n/);
  let count = 0;

  for (let i = 0; i < lines.length;) {
    if (!isActiveJavadocStart(lines[i])) {
      i++;
      continue;
    }

    const start = i;
    let end = i;
    while (end < lines.length && !isJavadocEnd(lines[end])) {
      end++;
    }

    const block = lines.slice(start, Math.min(end + 1, lines.length)).join('\n');
    pattern.lastIndex = 0;
    count += (block.match(pattern) || []).length;
    i = Math.min(end + 1, lines.length);
  }

  return count;
}

module.exports = {
  activeJavadocLineMask,
  activeJavadocsContain,
  countInActiveJavadocs,
  isActiveJavadocStart,
  isJavadocEnd,
  replaceInActiveJavadocs,
};
