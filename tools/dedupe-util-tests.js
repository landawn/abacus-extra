#!/usr/bin/env node
"use strict";

const fs = require("node:fs");
const { javaFiles, testRoot, indexFiles, testMethods, duplicateGroups, location, relative } = require("./java-test-tools");
const args = process.argv.slice(2);
const applyChanges = args.includes("--apply");
const fileMatch = args.find(arg => arg.startsWith("--match="))?.slice(8);
if (applyChanges && !fileMatch) throw new Error("--apply requires an explicit --match=<file> scope. Review the dry-run first.");
const files = javaFiles(testRoot).filter(file => !fileMatch || relative(file).includes(fileMatch));
if (!files.length) throw new Error("No matching Java files");
const index = indexFiles(files);
// Automatic edits stay within one lexical owner. Cross-container candidates
// require manual review, even when their visible fixtures look identical.
const groups = duplicateGroups(testMethods(index)).flatMap(group => {
    const owners = new Map();
    for (const method of group) owners.set(method.owner, [...(owners.get(method.owner) || []), method]);
    return [...owners.values()].filter(methods => methods.length > 1);
});
const changes = new Map();
for (const group of groups) {
    const [keeper, ...removed] = group;
    const fileChanges = changes.get(keeper.file) || [];
    fileChanges.push(...removed);
    changes.set(keeper.file, fileChanges);
}
if (applyChanges) {
    for (const [file, methods] of changes) {
        let source = fs.readFileSync(file, "utf8");
        for (const method of methods.sort((a, b) => b.start - a.start)) {
            // Compiler offsets preserve comments, literals and the file's newline style.
            const lineStart = source.lastIndexOf("\n", method.start - 1) + 1;
            const start = /^[\t ]*$/.test(source.slice(lineStart, method.start)) ? lineStart : method.start;
            const newline = source.slice(method.end).match(/^[\t ]*\r?\n/);
            const end = method.end + (newline ? newline[0].length : 0);
            source = source.slice(0, start) + source.slice(end);
        }
        fs.writeFileSync(file, source);
    }
}
console.log(JSON.stringify({
    applyChanges, fileMatch: fileMatch || null, fileCount: files.length, affectedFiles: changes.size,
    duplicateGroupCount: groups.length, removedMethodCount: groups.reduce((n, group) => n + group.length - 1, 0),
    groups: groups.map(([keeper, ...removed]) => ({ keeper: location(keeper), removed: removed.map(location) }))
}, null, 2));
