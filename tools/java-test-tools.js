"use strict";

const fs = require("node:fs");
const path = require("node:path");
const { execFileSync } = require("node:child_process");

const root = path.resolve(__dirname, "..");
const testRoot = path.join(root, "src/test/java/com/landawn/abacus/util");

function javaFiles(directory) {
    return fs.readdirSync(directory, { withFileTypes: true }).flatMap(entry => {
        const file = path.join(directory, entry.name);
        return entry.isDirectory() ? javaFiles(file) : entry.name.endsWith(".java") ? [file] : [];
    }).sort();
}

function relative(file) {
    return path.relative(root, file).replace(/\\/g, "/");
}

function indexFiles(files) {
    if (!files.length) return [];
    const source = path.join(__dirname, "TestSourceIndex.java");
    const output = path.join(root, "target/test-audit-index");
    const compiled = path.join(output, "TestSourceIndex.class");
    const options = { cwd: root, encoding: "utf8", maxBuffer: 128 * 1024 * 1024, windowsHide: true, stdio: ["ignore", "pipe", "pipe"] };
    fs.mkdirSync(output, { recursive: true });
    if (!fs.existsSync(compiled) || fs.statSync(compiled).mtimeMs < fs.statSync(source).mtimeMs) {
        execFileSync("javac", ["-encoding", "UTF-8", "--release", "21", "-d", output, source], options);
    }
    return JSON.parse(execFileSync("java", ["-cp", output, "TestSourceIndex", ...files], options));
}

function testMethods(index) {
    return index.flatMap(file => file.methods.filter(method => method.test).map(method => ({ ...method, file: file.file, unit: file })));
}

function location(method) {
    return { file: relative(method.file), owner: method.owner, method: method.name, line: method.line };
}

// A body is only interchangeable when parameter sources, enclosing fixtures and
// helper implementations match. Type-specific tests are deliberately kept separate.
function contextKey(method) {
    const unit = method.unit;
    const classes = unit.classes.filter(c => method.owner === c.owner || method.owner.startsWith(c.owner + "."));
    return JSON.stringify({
        imports: unit.imports.slice().sort(),
        classes: classes.map(c => ({
            annotations: c.annotations.filter(a => !/^@Nested\b/.test(a)),
            extends: c.extends,
            support: c.support.map(s => s.source),
            helpers: unit.methods.filter(m => m.owner === c.owner && !m.test).map(m => ({ name: m.name, parameters: m.parameters, body: m.body, annotations: m.annotations }))
        })).filter(c => c.support.length || c.helpers.length || c.annotations.length || c.extends)
    });
}

function duplicateGroups(methods, { crossFile = false } = {}) {
    const groups = new Map();
    for (const method of methods) {
        const key = JSON.stringify([crossFile ? "" : method.file, method.annotations, method.parameters, method.body, contextKey(method)]);
        const bucket = groups.get(key) || [];
        bucket.push(method);
        groups.set(key, bucket);
    }
    return [...groups.values()].filter(group => group.length > 1);
}

function hasAssertion(method, seen = new Set()) {
    const key = `${method.file}:${method.start}`;
    if (seen.has(key)) return false;
    seen.add(key);
    if (method.calls.some(call => /^assert[A-Z]/.test(call.name) || call.name === "fail")) return true;
    return method.calls.some(call => {
        // Only resolve unqualified/this calls in the lexical enclosing classes.
        // An unrelated object's similarly named method is not an assertion helper.
        if (call.select !== call.name && call.select !== `this.${call.name}`) return false;
        return method.unit.methods.filter(helper => !helper.test && helper.name === call.name
            && (method.owner === helper.owner || method.owner.startsWith(helper.owner + "."))
            && helper.parameters.length === call.arity)
            .some(helper => hasAssertion({ ...helper, file: method.file, unit: method.unit }, seen));
    });
}

module.exports = { root, testRoot, javaFiles, relative, indexFiles, testMethods, location, contextKey, duplicateGroups, hasAssertion };
