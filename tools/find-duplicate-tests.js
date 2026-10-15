#!/usr/bin/env node
"use strict";

const { javaFiles, testRoot, indexFiles, testMethods, duplicateGroups, location, relative } = require("./java-test-tools");
const args = process.argv.slice(2);
const option = name => args.find(arg => arg.startsWith("--" + name + "="))?.slice(name.length + 3);
const scope = option("scope") || "all";
if (!["all", "same-file", "cross-file"].includes(scope)) throw new Error("Invalid --scope");
const index = indexFiles(javaFiles(testRoot));
const methods = testMethods(index);
const groups = duplicateGroups(methods, { crossFile: true }).filter(group => {
    const crossFile = new Set(group.map(m => m.file)).size > 1;
    if (scope === "same-file" && crossFile || scope === "cross-file" && !crossFile) return false;
    if (option("match") && !group.some(m => relative(m.file).includes(option("match")) || m.name.includes(option("match")))) return false;
    if (args.includes("--exclude-type-variants") && crossFile) {
        const families = new Set(group.map(m => relative(m.file).replace(/(Boolean|Byte|Char|Short|Int|Long|Float|Double)TupleTest/, "PrimitiveFamilyTest")));
        if (families.size === 1) return false;
    }
    return true;
});
const duplicateIds = new Set(groups.flat().map(m => m.file + ":" + m.start));
const report = {
    scannedFiles: index.length, scannedMethods: methods.length, scope,
    duplicateGroupCount: groups.length,
    duplicateMethodCount: groups.reduce((sum, g) => sum + g.length, 0),
    groups: groups.map(group => ({ scope: new Set(group.map(m => m.file)).size > 1 ? "cross-file" : "same-file", methods: group.map(location) }))
};
if (args.includes("--summary-by-file")) {
    delete report.groups;
    report.files = index.map(file => ({
        file: relative(file.file), totalMethods: file.methods.filter(m => m.test).length,
        duplicateMethods: methods.filter(m => m.file === file.file && duplicateIds.has(m.file + ":" + m.start)).length
    }));
}
if (option("unique-file")) {
    report.methods = methods.filter(m => relative(m.file).includes(option("unique-file")) && !duplicateIds.has(m.file + ":" + m.start)).map(location);
}
console.log(JSON.stringify(report, null, 2));
