#!/usr/bin/env node
"use strict";

const { javaFiles, testRoot, indexFiles, testMethods, hasAssertion, location } = require("./java-test-tools");
const methods = testMethods(indexFiles(javaFiles(testRoot)));
console.log(JSON.stringify({
    scannedMethods: methods.length,
    withoutAssertions: methods.filter(method => !hasAssertion(method)).map(location),
    // These are review candidates: try/catch may be legitimate cleanup or an assertion.
    withTryCatch: methods.filter(method => /\btry\s*\{|\bcatch\s*\(/.test(method.body)).map(location)
}, null, 2));
