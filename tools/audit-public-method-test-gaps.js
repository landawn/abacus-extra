#!/usr/bin/env node
"use strict";

const path = require("node:path");
const { root, javaFiles, testRoot, indexFiles, testMethods, relative } = require("./java-test-tools");
const sources = indexFiles(javaFiles(path.join(root, "src/main/java/com/landawn/abacus/util")));
const tests = testMethods(indexFiles(javaFiles(testRoot)));
const results = sources.filter(unit => unit.classes.length).map(unit => {
    const owner = unit.classes[0].name;
    const candidates = tests.filter(test => test.unit.imports.some(i => i.includes("." + owner))
        || test.body.includes(owner) || path.basename(test.file).startsWith(owner)
        || owner.endsWith("Tuple") && /(?:Numeric|Small|Primitive)Tuple/.test(path.basename(test.file)));
    // Names/argument counts are only a call-site heuristic; same-arity overloads
    // cannot be distinguished without symbol resolution.
    const methods = unit.methods.filter(method => method.modifiers.includes("public") && method.name !== "<init>");
    const unmatched = methods.filter(method => !candidates.some(test => test.calls.some(call =>
        call.name === method.name && (call.arity < 0 || call.arity === method.parameters.length
            || method.parameters.at(-1)?.varargs && call.arity >= method.parameters.length - 1))));
    return {
        typeName: owner, sourceFile: relative(unit.file), publicMethodCount: methods.length,
        candidateTestCount: candidates.length,
        unmatchedMethods: unmatched.map(method => ({ owner: method.owner, name: method.name, line: method.line, parameters: method.parameters }))
    };
});
console.log(JSON.stringify({
    note: "Call-site candidates, not measured coverage. Same-arity overloads and helper calls require review; use JaCoCo and a behavior matrix for coverage.",
    scannedTestMethods: tests.length, classCount: results.length,
    classesWithUnmatchedMethods: results.filter(r => r.unmatchedMethods.length).length,
    totalUnmatchedMethods: results.reduce((sum, r) => sum + r.unmatchedMethods.length, 0),
    results
}, null, 2));
