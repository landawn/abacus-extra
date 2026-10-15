"use strict";

const test = require("node:test");
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");
const { root, indexFiles, testMethods, duplicateGroups, hasAssertion } = require("./java-test-tools");

fs.mkdirSync(path.join(root, "target"), { recursive: true });
const directory = fs.mkdtempSync(path.join(root, "target/test-audit-fixture-"));
const file = path.join(directory, "ExampleTest.java");
fs.writeFileSync(file, `
class ExampleTest {
    @org.junit.jupiter.api.Test
    void commentsAndLiterals() {
        // An apostrophe shouldn't consume a closing brace: }
        String value = "http://example.test/{@Test}";
        char brace = '}';
        String block = """
            @Test void fakeTest() { }
            """;
        assertEquals("a b", value);
    }
    @Test void indirect() { check(); }
    private void check() { assertEquals(1, 1); }
    @Test void noAssertion() { loop(); }
    private void loop() { loop(); }
    @Test void sameOne() { assertEquals(1, 1); }
    @Test void sameTwo() { assertEquals(1, 1); }
    void arrayArgument(@Note("...") int[] values) { }
    void variableArguments(int... /* spread */ values) { }
    @Test void spaced() { assertEquals("a b", "a b"); }
    @Test void compact() { assertEquals("ab", "ab"); }
    @ParameterizedTest @ValueSource(ints = { 1 })
    void firstData(int n) { assertEquals(n, n); }
    @ParameterizedTest @ValueSource(ints = { 2 })
    void secondData(int n) { assertEquals(n, n); }
    @Nested class FirstFixture {
        int expected = 1;
        @BeforeEach void setup() { expected = 2; }
        @Test void fixture() { assertEquals(expected, actual()); }
    }
    @Nested class SecondFixture {
        int expected = 2;
        @Test void fixture() { assertEquals(expected, actual()); }
    }
}
`, "utf8");

let index;
try {
    index = indexFiles([file]);
} finally {
    if (path.dirname(directory) !== path.join(root, "target")) throw new Error("Unexpected fixture directory");
    fs.rmSync(directory, { recursive: true });
}
const methods = testMethods(index);

test("compiler parsing includes nested/qualified/parameterized tests and ignores comment/string lookalikes", () => {
    assert.equal(methods.length, 11);
    assert.equal(methods.filter(m => m.name === "fixture").length, 2);
    assert.ok(methods.find(m => m.name === "commentsAndLiterals").body.includes("http://example.test/{@Test}"));
    assert.ok(!methods.some(m => m.name === "fakeTest"));
});

test("duplicates preserve literal contents, data sources, and enclosing fixture semantics", () => {
    const groups = duplicateGroups(methods);
    assert.equal(groups.length, 1);
    assert.deepEqual(groups[0].map(m => m.name), ["sameOne", "sameTwo"]);
});

test("assertion audit follows local helpers and terminates on recursive helpers", () => {
    assert.equal(hasAssertion(methods.find(m => m.name === "indirect")), true);
    assert.equal(hasAssertion(methods.find(m => m.name === "noAssertion")), false);
});

test("syntax errors fail explicitly instead of silently returning a partial inventory", () => {
    const brokenDirectory = fs.mkdtempSync(path.join(root, "target/test-audit-fixture-"));
    const brokenFile = path.join(brokenDirectory, "Broken.java");
    try {
        fs.writeFileSync(brokenFile, "class Broken { @Test void broken( { } }");
        assert.throws(() => indexFiles([brokenFile]));
    } finally {
        if (path.dirname(brokenDirectory) !== path.join(root, "target")) throw new Error("Unexpected fixture directory");
        fs.rmSync(brokenDirectory, { recursive: true });
    }
});

test("array parameters and variable arguments stay distinct in call-site audits", () => {
    const helpers = index[0].methods;
    assert.equal(helpers.find(m => m.name === "arrayArgument").parameters[0].varargs, false);
    assert.equal(helpers.find(m => m.name === "variableArguments").parameters[0].varargs, true);
    const call = methods.find(m => m.name === "sameOne").calls[0];
    assert.ok(call.selectEnd > call.selectStart);
});
