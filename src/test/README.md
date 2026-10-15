# Unit tests

Run `mvn test` with JDK 21 or later. Maven discovers `*Test.java` classes directly, excludes `slow-test`, and fails on test failures or an empty suite. `AbacusExtraTestSuite` is an optional IDE entry point for the same utility tests. It is not the Maven discovery mechanism.

For a focused run, use `mvn -Dtest=IntTupleTest test`. Quote the argument when selecting several classes in PowerShell: `mvn '-Dtest=IntTupleTest,ArraysShapeTest' test`.

For a fresh coverage report, use `mvn clean test jacoco:report`. Open `target/site/jacoco/index.html`; machine-readable results are in `target/site/jacoco/jacoco.xml`. A repeated run without cleaning can append to `target/jacoco.exec`, so do not use accumulated results to prove that removed tests preserve coverage. The JaCoCo agent uses its own `jacocoArgLine` property alongside the existing Surefire JVM options.

## Ownership

All utility tests live in `java/com/landawn/abacus/util` relative to this directory.

| Unit or behavior | Test class |
| --- | --- |
| Array element counts and row lengths | `ArraysStructureTest` |
| Array mapping and primitive conversions | `ArraysMappingTest`, `ArraysConversionTest` |
| Array shape, flattening, and reshaping | `ArraysShapeTest` |
| Array mutation and callbacks | `ArraysMutationTest` |
| Array zip overloads | `ArraysZipTest` |
| Array printing and formatting | `ArraysPrintlnTest` |
| Contracts spanning array operations | `ArraysContractTest` |
| Immutable int arrays, including regressions | `ImmutableIntArrayTest` |
| Each primitive tuple implementation | `BooleanTupleTest` through `DoubleTupleTest` |
| Shared primitive tuple behavior | `PrimitiveTupleTest` |
| Contracts spanning numeric or small tuple families | `NumericTupleContractTest`, `SmallTupleContractTest` |
| Points | `PointsTest`, grouped by dimension and point type |
| Public functional-interface null validation | `FunctionalInterfaceNullValidationTest` |

Keep regressions with the unit or contract they protect. Do not create another dated, review, or performance file for ordinary functional assertions. Cache isolation and defensive-copy checks belong in the normal unit suite. Reserve `slow-test` for tests with a concrete reason to exclude them from the default run.

`ArrayTestSupport` contains only shared array fixtures and deterministic input generation. Keep helpers local unless multiple test classes use them. `CodeHelper.main` is a manual source-generation utility, not a test; running it rewrites production source files.

## Layout and names

Use package-private JUnit Jupiter test classes and methods, explicit static assertion imports, and `@Nested` groups ending in `Tests`. Name the operation, scenario, and expected behavior when that makes the failure easier to identify. Omit the redundant `test` prefix and dated tags.

Tuple groups use this shared source order, omitting groups that do not apply:

1. `Of`, `From`, `Arity`, `Elements`.
2. `Min`, `Max`, `Sum`, `Average`, `Median`, `LowerMedian`.
3. `Reversed`, `Contains`, `ToArray`, `ToList`, `ForEach`, `Stream`.
4. `Accept`, `Map`, `Filter`, `ToOptional`.
5. `HashCode`, `Equals`, `ToString`, `TypeHierarchy`, `Contract`.

Array groups follow their operation families. Point groups follow dimension and coordinate/payload type. Keep related scenarios adjacent, with arities in ascending order where applicable. Source order is for navigation; tests must pass independently of execution order. Use descriptive fixture groups, such as `RoundingTests`, when a regression needs its own helper scope.

## Assertions and consolidation

- Parameterize scenarios that share the same behavior and oracle. Preserve distinct inputs, types, arities, floating-point edge cases, invalid arguments, aliasing, and callback contracts. Factory cases check all fields, arity, and exported order where applicable.
- Prefer exact expected values or arrays. A non-null result or substring alone does not establish formatting, element order, or completeness. Multidimensional printing uses `\n` and documented indentation.
- For reflexivity, use `assertTrue(value.equals(value))`; `assertEquals(value, value)` can succeed through JUnit's identity shortcut.
- Use fixed random seeds and explicit locales for expected formatted values. Clean up streams, executors, and other resources in the test that owns them.
- Keep type-specific overload coverage even when test text looks similar. Remove a scenario only after reviewing its input, fixture, operation, and assertions against a named retained test or parameterized case. Record removals when doing a broad consolidation.
- Compare a fresh full-suite coverage report before and after removals. Coverage is a guard against losing exercised paths; it does not establish assertion quality by itself.

Run `node tools/check-test-layout.js` and `node --test tools/java-test-tools.test.js` from the repository root. The layout check enforces discovery names, tuple group order, package-private methods, assertion style, and duplicate review. See [the audit tools](../../tools/README.md) for deeper review commands and their limitations.
