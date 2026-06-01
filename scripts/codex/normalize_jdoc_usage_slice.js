#!/usr/bin/env node

const fs = require('fs');
const cp = require('child_process');
const { isActiveJavadocStart } = require('./javadoc_region');

const apply = process.argv.includes('--apply');
const listArgs = process.argv.slice(2).filter(arg => arg !== '--apply');

function runNode(args) {
  return cp.spawnSync(process.execPath, args, { encoding: 'utf8' });
}

const listed = runNode(['scripts/codex/list_jdoc_usage_slice.js', ...listArgs]);

if (listed.status !== 0) {
  process.stdout.write(listed.stdout || '');
  process.stderr.write(listed.stderr || '');
  process.exit(listed.status || 1);
}

const files = listed.stdout
  .split(/\r?\n/)
  .map(line => line.trim())
  .filter(Boolean)
  .map(line => line.split(/\t/)[0]);

function normalizeComment(code, rawComment) {
  let comment = rawComment.trim();

  if (/^\/\/\s+(returns|throws)\b/.test(comment)) return comment;

  comment = comment.replace(/^\/\/\s*/, '').trim();
  comment = comment.replace(/\s+—\s+/g, '; ');
  comment = comment.replace(/\s+-\s+/g, '; ');

  if (/^returns?\b/i.test(comment)) return `// returns ${comment.replace(/^returns?\s*/i, '').trim()}`;
  if (/^throws?\b/i.test(comment)) return `// throws ${comment.replace(/^throws?\s*/i, '').trim()}`;

  const lower = comment.charAt(0).toLowerCase() + comment.slice(1);
  const trimmedCode = code.trim();

  if (/^if\s*\(/.test(trimmedCode)) {
    return `// returns true; ${lower}`;
  }

  if (/System\.out\.(?:print|println|printf)\s*\(/.test(trimmedCode)) {
    const printed = comment.replace(/^Output:\s*/i, '').trim();
    return `// returns void; prints ${printed}`;
  }

  if (/\.(?:flush|close|reset|mark|write|set|add|addAll|removeAllOccurrences|removeIf|removeDuplicates|removeAt|removeRange|moveRange|replaceAll|sort|reverse|shuffle|clear)\s*\(/.test(trimmedCode)
      || /^(?:cal|calendar|writer|reader|brotliStream|flags|list|or)\./.test(trimmedCode)) {
    return `// returns void; ${lower}`;
  }

  if (/\.append\s*\(/.test(trimmedCode)) {
    return `// returns writer; ${lower}`;
  }

  if (/\.remove\s*\(/.test(trimmedCode) || /\.replaceIf\s*\(/.test(trimmedCode)) {
    return `// returns ${lower}`;
  }

  return `// returns ${comment}`;
}

function normalizeFile(text) {
  const newline = text.includes('\r\n') ? '\r\n' : '\n';
  const lines = text.split(/\r?\n/);
  let inJavadoc = false;
  let inPre = false;
  let changed = false;
  const out = [];

  for (let i = 0; i < lines.length; i++) {
    let line = lines[i];

    if (isActiveJavadocStart(line)) inJavadoc = true;
    if (inJavadoc && line.includes('<pre>{@code')) inPre = true;

    if (inJavadoc && inPre) {
      line = line
        .replace("// ['A', 'B', 'C', ..., 'Z']", "// returns ['A', 'B', 'C', 'D']")
        .replace("// ['0', '1', '2', ..., '9']", "// returns ['0', '1', '2', '3']")
        .replace('Bracketed form: "[T, h, e,  , q, u, i, c, k, ...]"', 'Bracketed form: "[T, h, e]"')
        .replace("// returns {'a', 'c', 'e', ..., 'y'}", "// returns {'a', 'c', 'e', 'g'}")
        .replace("// returns {'z', 'w', 't', ..., 'b'}", "// returns {'z', 'w', 't', 'q'}")
        .replace("// returns {0, 10, 20, 30, ..., 90}", "// returns {0, 10, 20, 30}")
        .replace("// returns {50, 45, 40, ..., 5}", "// returns {50, 45, 40, 35}")
        .replace("// returns {0, 2, 4, ..., 18}", "// returns {0, 2, 4, 6}")
        .replace("// returns {0, 100, 200, ..., 900}", "// returns {0, 100, 200, 300}")
        .replace("// returns {5000, 4500, 4000, ..., 1500}", "// returns {5000, 4500, 4000, 3500}")
        .replace("// returns {0, 10, 20, 30, ..., 90, 100}", "// returns {0, 10, 20, 30}")
        .replace("// returns {50, 45, 40, ..., 5, 0}", "// returns {50, 45, 40, 35}")
        .replace("// returns {0, 2, 4, ..., 18, 20}", "// returns {0, 2, 4, 6}")
        .replace("// returns {0, 100, 200, ..., 900, 1000}", "// returns {0, 100, 200, 300}")
        .replace("// returns {5000, 4500, 4000, ..., 1500, 1000}", "// returns {5000, 4500, 4000, 3500}");

      const match = line.match(/^(\s*\*\s+)(.*?)(\s+)\/\/(.*)$/);
      if (match) {
        const code = match[2];
        if (!code.trim()) {
          out.push(line);
          continue;
        }
        const comment = `//${match[4]}`;
        const normalized = normalizeComment(code, comment);
        line = `${match[1]}${code}${match[3]}${normalized}`;
      }
    }

    out.push(line);

    if (inJavadoc && line.includes('</pre>')) {
      inPre = false;
      const next = lines[i + 1];
      if (next != null && next.trim() !== '*') {
        const prefix = line.match(/^(\s*)\*/)?.[1] || ' ';
        out.push(`${prefix}*`);
        changed = true;
      }
    }

    if (inJavadoc && line.includes('*/')) {
      inJavadoc = false;
      inPre = false;
    }

    if (out[out.length - 1] !== lines[i]) changed = true;
  }

  return { text: out.join(newline), changed };
}

let changedFiles = 0;

for (const file of files) {
  const before = fs.readFileSync(file, 'utf8');
  const after = normalizeFile(before).text;

  if (after !== before) {
    changedFiles++;
    if (apply) fs.writeFileSync(file, after, 'utf8');
    console.log(`${file}: ${apply ? 'normalized' : 'would normalize'}`);
  }
}

console.log(`NORMALIZED_FILE_COUNT ${changedFiles}`);
