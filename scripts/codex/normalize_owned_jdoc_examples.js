#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { activeJavadocLineMask, activeJavadocsContain, replaceInActiveJavadocs } = require('./javadoc_region');

const roots = process.argv.slice(2);

if (roots.length === 0) {
  console.error('Usage: node scripts/codex/normalize_owned_jdoc_examples.js <path> [path...]');
  process.exit(2);
}

function walk(p, out) {
  const stat = fs.statSync(p);

  if (stat.isDirectory()) {
    for (const entry of fs.readdirSync(p)) {
      walk(path.join(p, entry), out);
    }
  } else if (p.endsWith('.java')) {
    out.push(p);
  }
}

function isBlankJavadocLine(line) {
  return line.trim() === '*';
}

function javadocBlankLike(line) {
  const match = line.match(/^(\s*)\*/);
  return match ? `${match[1]} *` : '     *';
}

function normalizeText(text) {
  let out = text;

  out = out.replace(/(\r?\n)/g, '\n');

  const replacements = [
    ['String id = eventBus.identifier();   // Returns "myBus"', 'String id = eventBus.identifier();   // returns "myBus"'],
    ['Thread.currentThread().interrupt();   // Restore interrupted status', 'Thread.currentThread().interrupt();   // returns void; restores interrupted status'],
    ['buffer.putInt(0, 42);   // Write to the beginning of the file', 'buffer.putInt(0, 42);   // returns MappedByteBuffer; writes 42 at offset 0'],
    ['.limit(1000)  // Limit traversal', '.limit(1000)'],
    ['byte[] largeData = new byte[1024 * 1024];  // 1MB', 'byte[] largeData = new byte[1024 * 1024];'],
    ['System.out.println(sha256.bits());   // prints: 256', 'sha256.bits();   // returns 256'],
    ['hasher.put(buffer);   // Buffer position is now at limit', 'hasher.put(buffer);   // returns this hasher; buffer position is advanced to limit'],
    ['HashFunction murmur = Hashing.murmur3_32(42);   // seed = 42', 'HashFunction murmur = Hashing.murmur3_32(42);   // returns 32-bit Murmur3 hash function with seed 42'],
    ['byte[] hashBytes = sha256Hash.asBytes();   // 32 bytes', 'byte[] hashBytes = sha256Hash.asBytes();   // returns 32-byte array'],
    ['byte[] key = new byte[64];  // 512-bit key', 'byte[] key = new byte[64];'],
    ['Hashing.sha512(),  // 512 bits', 'Hashing.sha512(),'],
    ['Hashing.sha512()   // 512 bits', 'Hashing.sha512()'],
    ['super(3600000, 300000);   // 1 hour lifetime, 5 minute max idle', 'super(3600000, 300000);   // returns void; initializes 1 hour lifetime and 5 minute max idle'],
    ['super(3600000, 300000);   // 1 hour lifetime, 5 minutes max idle', 'super(3600000, 300000);   // returns void; initializes 1 hour lifetime and 5 minutes max idle'],
    ['activity.setLiveTime(7200000);   // set to 2 hours', 'activity.setLiveTime(7200000);   // returns void; sets live time to 2 hours'],
    ['activity.setMaxIdleTime(600000);   // set to 10 minutes', 'activity.setMaxIdleTime(600000);   // returns void; sets max idle time to 10 minutes'],
    ['pool.add(resource);   // return to pool', 'pool.add(resource);   // returns true if the resource is accepted by the pool'],
    ['pool.add(obj);   // return to pool', 'pool.add(obj);   // returns true if the object is accepted by the pool'],
    ['pool.add(borrowed);   // return to pool', 'pool.add(borrowed);   // returns true if the object is accepted by the pool'],
    ['pool.put("database1", borrowed);   // return to pool', 'pool.put("database1", borrowed);   // returns true if the object is accepted by the pool'],
    ['key.length() * 2 + data.getDataSize();   // * 2 for UTF-16 encoding (2 bytes per char)', 'key.length() * 2 + data.getDataSize();   // returns approximate memory size in bytes'],
    ['1024 * 1024 * 500, // 500MB max', '1024 * 1024 * 500,'],
    ['1024 * 1024 * 100, // 100MB max', '1024 * 1024 * 100,'],
    ['pool.evict();   // free up space', 'pool.evict();   // returns void; frees expired or idle entries'],
    ['pool.evict();   // free up some space', 'pool.evict();   // returns void; frees expired or idle entries'],
    ['pool.clear();   // empty the pool but keep it open', 'pool.clear();   // returns void; removes all entries while keeping the pool open'],
    ['pool.put(newObj);   // still usable', 'pool.put(newObj);   // returns true if the object is accepted by the pool'],
    ['pool.close();   // ensure cleanup', 'pool.close();   // returns void; releases pool resources'],
    ['private long memoryUsage = 1024;  // example size in bytes', 'private long memoryUsage = 1024;'],
    ['this.activityPrint = new ActivityPrint(3600000, 600000);   // 1hr live, 10min idle', 'this.activityPrint = new ActivityPrint(3600000, 600000);   // returns ActivityPrint with 1 hour lifetime and 10 minute max idle'],
    ['600000,  // 10 minute lifetime', '600000,'],
    ['60000    // 1 minute max idle', '60000'],
    ['pool.add(retrieved);   // return to pool', 'pool.add(retrieved);   // returns true if the object is accepted by the pool'],
    ['return user;  // Automatically serialized to JSON by this method', 'return user;  // returns user serialized to JSON by this method'],
  ];

  for (const [from, to] of replacements) {
    out = replaceInActiveJavadocs(out, from, to);
  }

  const lines = out.split('\n');
  const activeJavadoc = activeJavadocLineMask(lines);
  const fixed = [];

  for (let i = 0; i < lines.length; i++) {
    fixed.push(lines[i]);

    if (activeJavadoc[i] && lines[i].includes('</pre>') && i + 1 < lines.length && !isBlankJavadocLine(lines[i + 1])) {
      fixed.push(javadocBlankLike(lines[i]));
    }
  }

  return fixed.join('\n');
}

const files = [];
for (const root of roots) {
  if (fs.existsSync(root)) {
    walk(root, files);
  }
}

let changed = 0;

for (const file of files) {
  const normalized = file.replace(/\\/g, '/');

  if (normalized.endsWith('/CommonUtil.java')) continue;

  const original = fs.readFileSync(file, 'utf8');
  if (/\bpublic\s+@interface\b/.test(original)) continue;
  if (!activeJavadocsContain(original, /(Usage Examples|<b>Examples:|Example usage|<b>Example\b)/)) continue;

  const eol = original.includes('\r\n') ? '\r\n' : '\n';
  const updated = normalizeText(original).replace(/\n/g, eol);

  if (updated !== original) {
    fs.writeFileSync(file, updated);
    changed++;
    console.log(`updated ${normalized}`);
  }
}

console.log(`UPDATED_FILES ${changed}`);
