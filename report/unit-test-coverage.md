# Unit test coverage improvements

Verified on 2026-10-15 against baseline commit `bc50cbe05906aeeba39487f2a72a3e4573bbaa5b`, using Maven 3.9.16, JDK 25, and the project's Java 21 compilation target.

| Measure | Clean baseline | Final clean build |
| --- | ---: | ---: |
| Line coverage | 7,379 / 7,709 = **95.72%** | 7,599 / 7,709 = **98.57%** |
| Branch coverage | 4,255 / 4,902 = **86.80%** | 4,831 / 4,902 = **98.55%** |
| Method coverage | 1,937 / 1,969 = **98.37%** | 1,941 / 1,969 = **98.58%** |
| Executable classes covered | 135 / 135 | 135 / 135 |
| Test methods/templates | 5,631 | 5,661 |
| Executed test cases | 5,941 | **6,345** |
| Failures / errors / skipped | 0 / 0 / 0 | **0 / 0 / 0** |

Thirty added methods/templates contribute 404 executed cases, covering another 220 lines and 576 branches. The final run used randomized class and method order with seed `20261015` and German locale `de_DE`; Surefire reports confirm those settings. All measured counters match the preceding normal-order run.

## Coverage scope correction

The earlier cleanup report included stale `ApiDocGenerator` classes in `target/classes`. Their source is under `tools/code`, outside Maven's production source tree. A full `mvn clean test jacoco:report` removed those artifacts and established the baseline above. The stale tool contributed 873 uncovered lines and 649 uncovered branches to the earlier report.

Both columns above measure all production classes compiled from `src/main/java`. The implementation changes are confined to tests and fixtures; no coverage exclusions were added.

## Added contracts

- Tuple equality varies each component independently, checks symmetry and reflexivity, rejects null and unrelated types, and verifies equal hash codes. A separately constructed package-level empty tuple prevents singleton identity from masking equality behavior.
- Tuple containment checks every component position and a missing value for every supported nonempty arity.
- Array mapping checks null/empty result types, suppressed callbacks, and rejection of primitive target types.
- Array mutation checks skipped callbacks on absent inputs and value updates through ragged shapes while preserving null and empty branches.
- Shape and structure tests cover empty reshapes, flattened arrays without elements, null element counts, and maximum row lengths.
- Printing checks complete structure, indentation, nulls, and empty branches across primitive and object arrays.
- The zip matrix covers eight primitive types, dimensions 1–3, binary/ternary operators, and both truncation and default padding. Every parameterized case checks all combinations of null, empty, singleton, and ragged fixtures. An independent shape oracle verifies callback arguments, traversal order, result shape, operator results, and preservation of inputs. Two object-array cases cover a shorter right-hand grid or cube.

These tests live in the existing API groups. Shared reflection fixtures select public overloads; assertions use explicit values and a separate shape oracle.

## What reached 100%

`Arrays.java`, including `Arrays.f`, `Arrays.ff`, and `Arrays.fff`, now has **4,318 / 4,318 lines and 2,590 / 2,590 branches covered**.

`ImmutableIntArray`, `PrimitiveTuple`, and every concrete tuple subtype have complete measured line and branch coverage. `Points.java` has all 80 executable lines covered; JaCoCo reports no instrumented branches for it.

## Why overall coverage is below 100%

Every remaining gap belongs to one of the eight sealed tuple base classes:

| Base class | Uncovered lines | Uncovered branches |
| --- | ---: | ---: |
| `BooleanTuple` | 1 | 1 |
| `ByteTuple` | 14 | 9 |
| `CharTuple` | 13 | 8 |
| `ShortTuple` | 14 | 9 |
| `IntTuple` | 14 | 9 |
| `LongTuple` | 14 | 9 |
| `FloatTuple` | 20 | 13 |
| `DoubleTuple` | 20 | 13 |
| **Total** | **110** | **71** |

The missed paths have three causes:

1. All ten permitted subtypes override `min`, `max`, `sum`, and `average` in each numeric/character family. None calls `super` for these operations. These 28 base implementations are bypassed by normal Java dispatch.
2. The empty subtype overrides `median` and `lowerMedian`. Valid instances that use the base implementations are nonempty, so their empty-input exception paths cannot run.
3. Every nonempty subtype overrides `forEach`. Only the empty subtype inherits the base loop, so that loop's body cannot run for a valid instance.

Literal 100% would require a production refactor to remove or reorganize these dormant implementations and guards. Exercising them by bypassing dispatch or corrupting tuple state would not establish a valid API contract. [unit-test-coverage.json](unit-test-coverage.json) records the counters, source lines, subtype overrides, and explanation for each of the 49 methods containing a missed path.

## Reproduction

```text
mvn clean test jacoco:report
node tools/check-test-layout.js
node tools/find-duplicate-tests.js --summary-by-file
```

To reproduce the final randomized verification:

```text
mvn '-Djunit.jupiter.testmethod.order.default=org.junit.jupiter.api.MethodOrderer$Random' '-Djunit.jupiter.testclass.order.default=org.junit.jupiter.api.ClassOrderer$Random' '-Djunit.jupiter.execution.order.random.seed=20261015' '-Duser.language=de' '-Duser.country=DE' clean test jacoco:report
```

The HTML report is `target/site/jacoco/index.html`. Local baseline/final XML reports and Maven logs are preserved under ignored `target/coverage-verification`; another clean build removes these temporary files. The layout check passes for 22 test classes and 5,661 methods, with no exact duplicate candidates.
