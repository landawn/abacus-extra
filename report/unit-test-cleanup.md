# Unit test cleanup results

Baseline: commit `984de56b096a6d7faef8db0848574d3a9df15c14`. Verification completed on 2026-10-15 with Maven 3.9.16 and JDK 25, compiling the project for Java 21.

| Measure | Before | After |
| --- | ---: | ---: |
| Utility test classes | 26 | 22 |
| Annotated test methods/templates | 6,308 | 5,631 |
| Full-suite executions | 6,340 | 5,941 |
| Failures / errors / skipped | 0 / 0 / 0 | 0 / 0 / 0 |
| Covered lines | 7,379 / 8,582 | 7,379 / 8,582 |
| Covered branches | 4,255 / 5,551 | 4,255 / 5,551 |
| Covered methods | 1,937 / 2,055 | 1,937 / 2,055 |

The final coverage run started with the compiled test classes, Surefire reports, and JaCoCo execution data removed. Every per-class JaCoCo instruction, branch, line, complexity, method, and class counter matches the baseline. The suite executes 399 fewer cases while retaining the same exercised production paths.

## Consolidation

The former monolithic array test and four supplementary array suites are organized into eight classes: structure, mapping, conversion, shape, mutation, zip, printing, and cross-operation contracts. Dated nested classes were replaced with API groups. Primitive tuple groups use a shared order, and point tests are grouped by dimension and point type.

The immutable-array regression suite was merged into its owning class. Five numeric tuple suites became `NumericTupleContractTest`; three small tuple suites became `SmallTupleContractTest`. Functional cache/isolation checks remain in the default unit suite.

| Consolidated scenarios | Original methods | Replacement templates | Executed cases |
| --- | ---: | ---: | ---: |
| Primitive tuple factories | 463 | 8 | 147 |
| Point factories | 196 | 39 | 176 |
| Immutable-array null/empty factories | 8 | 2 | 4 |

Another 59 redundant methods were removed after reviewing their retained counterpart: 49 equivalent or weaker scenarios and 10 exact duplicates exposed by assertion standardization. Distinct inputs, callback behaviors, primitive types, floating-point cases, and regression contracts were retained.

All 6,308 original methods were accounted for: 5,582 retained methods and 726 replaced or removed methods. The 49 new parameterized templates bring the final total to 5,631. [unit-test-consolidation.json](unit-test-consolidation.json) names the retained method or parameterized case and rationale for each of the 726 replacements/removals. Original identifiers can be inspected at the baseline commit.

## Improvements

- Factory parameterizations verify all public components and, for tuples, arity and exported order.
- Printing assertions now verify complete output, including line separators, indentation, nulls, empty arrays, and element order. Seventy-two existing tests were strengthened; seven then became exact duplicates, leaving 65 strengthened printing scenarios.
- Fifty-five self-equality assertions now invoke `equals` directly, avoiding JUnit's identity shortcut. Twenty-six formatting calls specify `Locale.ROOT`.
- Test methods are package-private with behavior-oriented names and explicit static assertion imports. Unused imports and the unused `TestBase` were removed; shared array fixtures live in `ArrayTestSupport`.
- Maven discovers test classes directly, fails on test failures or an empty suite, and preserves JaCoCo agent options. The suite class remains available for IDE use. `CodeHelper` is an explicit manual source-maintenance entry point.
- Compiler-backed audit tools replace the fragile text scanners. Deduplication defaults to a dry-run and requires an explicit file scope for edits. The old merger that rebuilt files from `HEAD` was removed.

## Verification

- Original full suite: **6,340 passed**.
- Final fresh full suite and JaCoCo report: **5,941 passed**, no failures, errors, or skipped tests.
- Full suite with randomized class/method order, seed `20261015`, and German locale (`de_DE`): **5,941 passed**. Surefire reports confirm those settings were active.
- Audit-tool regression checks: **5 passed**.
- Layout check: **22 classes / 5,631 methods**, no violations.
- Exact duplicate audit: **0 candidate groups**. Assertion audit: **0 methods without a detected assertion or asserting local helper**.
- `git diff --check` passed. Production source files have no changes.

The failure gate was also exercised during development: incorrect new printing expectations caused Maven to exit with status 1. Correcting the expectations to the documented formatting restored a passing build.

Full-suite verification used `mvn test jacoco:report` after clearing generated test/coverage state. To reproduce from a checkout, use `mvn clean test jacoco:report`. To repeat the order/locale check:

```text
mvn '-Djunit.jupiter.testmethod.order.default=org.junit.jupiter.api.MethodOrderer$Random' '-Djunit.jupiter.testclass.order.default=org.junit.jupiter.api.ClassOrderer$Random' '-Djunit.jupiter.execution.order.random.seed=20261015' '-Duser.language=de' '-Duser.country=DE' test
```

Local run logs and baseline/final coverage snapshots are under ignored `target/test-cleanup`; the generated HTML report is `target/site/jacoco/index.html`. Coverage preservation does not prove complete behavioral coverage, and semantic duplicates can remain beyond the exact-duplicate audit. See [test conventions](../src/test/README.md) and [audit tool limitations](../tools/README.md).
