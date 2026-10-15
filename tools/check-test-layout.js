#!/usr/bin/env node
"use strict";

const fs = require("node:fs");
const path = require("node:path");
const { root, javaFiles, indexFiles, testMethods, location, relative, duplicateGroups, hasAssertion, groupOrderIssues } = require("./java-test-tools");

const index = indexFiles(javaFiles(path.join(root, "src/test/java")));
const methods = testMethods(index);
const issues = [];

for (const unit of index) {
    const tests = unit.methods.filter(method => method.test);
    const file = relative(unit.file);
    if (unit.imports.includes("import static org.junit.jupiter.api.Assertions.*;")) issues.push({ file, rule: "Use explicit static assertion imports" });
    if (tests.length && !unit.file.endsWith("Test.java")) issues.push({ file, rule: "Test files must end in Test.java for Maven discovery" });
    if (fs.readFileSync(unit.file, "utf8").includes("\0")) issues.push({ file, rule: "Use an escaped Java NUL literal instead of a raw NUL byte" });
    for (const cls of unit.classes) {
        if (/\d{4}Tests?$/.test(cls.name) || cls.annotations.some(a => /^@Tag\("(?:\d{4}|base-test)"\)$/.test(a))) {
            issues.push({ file, owner: cls.owner, rule: "Group by API or behavior, not a dated test batch" });
        }
        if (cls.extends === "TestBase") issues.push({ file, owner: cls.owner, rule: "Use explicit fixtures instead of TestBase inheritance" });
        if (cls.annotations.includes("@Nested") && !cls.name.endsWith("Tests")) {
            issues.push({ file, owner: cls.owner, rule: "Nested test groups must end in Tests" });
        }
    }
    issues.push(...groupOrderIssues(unit));
}

for (const method of methods) {
    const at = location(method);
    if (method.modifiers.some(modifier => ["public", "protected", "private", "static"].includes(modifier))) issues.push({ ...at, rule: "JUnit test methods must be package-private instance methods" });
    if (/^test(?:$|_|[A-Z])/.test(method.name)) issues.push({ ...at, rule: "Name the behavior without the redundant test prefix" });
    if (/coverage/i.test(method.name)) issues.push({ ...at, rule: "Name the behavior being protected instead of the coverage goal" });
    if (method.calls.some(call => call.select.startsWith("Assertions."))) issues.push({ ...at, rule: "Use static JUnit assertion imports" });
    if (/assertEquals\((\w+),\s*\1\)/.test(method.body)) issues.push({ ...at, rule: "Call equals directly when testing reflexivity" });
    if (!hasAssertion(method)) issues.push({ ...at, rule: "No assertion or asserting local helper found; review the behavior check" });
}

for (const group of duplicateGroups(methods)) {
    issues.push({ rule: "Review exact duplicate scenarios before adding or removing coverage", methods: group.map(location) });
}
if (!methods.length) issues.push({ rule: "No test methods discovered" });

console.log(JSON.stringify({ passed: issues.length === 0, testClasses: index.filter(u => u.methods.some(m => m.test)).length, testMethods: methods.length, issues }, null, 2));
process.exitCode = issues.length ? 1 : 0;
