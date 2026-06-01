#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const childProcess = require('child_process');

const dir = process.argv.slice(2).find(arg => !arg.startsWith('-')) || 'scripts/codex';
const files = fs.readdirSync(dir)
  .filter(name => name.endsWith('.js'))
  .sort()
  .map(name => path.join(dir, name));
const subprocessAllowlist = new Set([
  'audit_codex_scripts.js',
]);

let issueCount = 0;
let warningCount = 0;

function issue(file, message) {
  issueCount++;
  console.log(`${file}: ${message}`);
}

function warn(file, message) {
  warningCount++;
  console.log(`${file}: warning: ${message}`);
}

function firstLine(text, needle) {
  const lines = text.split(/\r?\n/);
  const idx = lines.findIndex(line => line.includes(needle));
  return idx < 0 ? '' : `${idx + 1}: ${lines[idx].trim()}`;
}

for (const file of files) {
  const check = childProcess.spawnSync(process.execPath, ['--check', file], { encoding: 'utf8' });

  if (check.status !== 0) {
    issue(file, `syntax check failed: ${(check.stderr || check.stdout).trim()}`);
    continue;
  }

  const text = fs.readFileSync(file, 'utf8');
  const basename = path.basename(file);
  const writes = /fs\.writeFileSync|writeFileSync\(/.test(text);
  const removes = /fs\.(?:unlinkSync|rmSync|rmdirSync)|unlinkSync\(|rmSync\(|rmdirSync\(/.test(text);
  const shellExec = /childProcess\.(?:exec|execFile|spawn|spawnSync)|execSync\(/.test(text) && !subprocessAllowlist.has(basename);
  const flagSensitiveArg = /process\.argv\[2\]/.test(text) && /process\.argv\.(?:includes|slice)\([^)]*--/.test(text);
  const broadValuesRewrite = basename !== 'audit_codex_scripts.js' && /replaceAll\(['"`]values\.(?:length|size\(\))/.test(text);
  const broadNRewrite = basename !== 'audit_codex_scripts.js' && (/replaceAll\(['"`]\s*\*\s*N\./.test(text) || /replaceAll\(['"`]N\./.test(text));

  if (removes) {
    issue(file, `destructive filesystem call present: ${firstLine(text, 'rmSync') || firstLine(text, 'unlinkSync') || firstLine(text, 'rmdirSync')}`);
  }

  if (shellExec) {
    issue(file, `runs subprocesses; review before use: ${firstLine(text, 'childProcess.') || firstLine(text, 'execSync')}`);
  }

  if (flagSensitiveArg) {
    issue(file, 'uses process.argv[2] while also accepting flags; flags before the file path can be misread');
  }

  if (broadValuesRewrite) {
    issue(file, `broad values.length/values.size rewrite: ${firstLine(text, "replaceAll('values.") || firstLine(text, 'replaceAll("values.')}`);
  }

  if (broadNRewrite) {
    warn(file, `N.* rewrite pattern needs manual review before rerun: ${firstLine(text, "replaceAll('N.") || firstLine(text, 'replaceAll("N.')}`);
  }

  if (writes && !/--apply/.test(text) && !/apply/i.test(path.basename(file))) {
    warn(file, 'writes files without an --apply guard or apply-oriented script name');
  }
}

console.log(`SCRIPT_COUNT ${files.length}`);
console.log(`AUDIT_WARNING_COUNT ${warningCount}`);
console.log(`AUDIT_ISSUE_COUNT ${issueCount}`);

if (issueCount > 0) {
  process.exitCode = 1;
}
