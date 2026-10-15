# Unit-test maintenance plan and results

## Plan

1. Capture a clean full-suite baseline and per-class JaCoCo counters at commit cb03797.
2. Consolidate repeated primitive-tuple membership and equality checks into named parameterized cases. Preserve original input expressions, expected outcomes, and special-value regressions; retain the all-component matrices.
3. Strengthen equality checks for symmetry, transitivity, hash consistency, and different arities. Make failure messages identify the input and component.
4. Move tests into the operation they exercise, align array operation groups, and replace coverage-oriented names and stale implementation-line commentary with behavior descriptions.
5. Extend the layout audit for the new ordering conventions, and document the maintenance rules and old-to-retained case mapping.
6. Run the full suite with fresh coverage and randomized execution order. Require no class-level line or branch coverage loss, passing audit-tool checks, and a clean whitespace diff.

## Results

Completed against baseline commit `cb0379739a085607bc2a544c2d9c84950311f770` on 2026-10-15. Production source and Maven configuration are unchanged.

| Measure | Before | After |
| --- | ---: | ---: |
| Test classes | 22 | 22 |
| Annotated test methods/templates | 5,661 | 5,089 |
| Executed test invocations | 6,345 | 6,515 |
| Failures / errors / skipped | 0 / 0 / 0 | 0 / 0 / 0 |
| Covered production lines | 7,599 / 7,709 (98.57%) | 7,599 / 7,709 (98.57%) |
| Covered production branches | 4,831 / 4,902 (98.55%) | 4,831 / 4,902 (98.55%) |

The consolidation accounts for 608 replaced or removed methods:

| Behavior | Previous methods | Retained templates | Retained cases |
| --- | ---: | ---: | ---: |
| Tuple membership and equality | 573 | 16 | 580 |
| Tuple arity | 22 | 8 | 82 |
| Default constructors and exported arrays | 12 | 8 | 72 |
| Repeated BooleanTuple factory field assertions | 1 | Existing factory template | Existing `3: true, false, true` case |

The 32 consolidated templates retain 734 cases. Four additional floating-point templates contribute 36 invocations, and the eight existing membership matrices now include empty tuples. More invocations with fewer methods reflect broader parameterized behavior checks and less repeated test code.

The [machine-readable mapping](unit-test-maintenance.json) names each previous method and its retained cases or new location. All 608 removals/replacements and 39 moves/renames are accounted for; every target exists. The 16 membership/equality providers were parsed again after editing to verify all 580 input rows, probes, and expected outcomes against the consolidation inventory. Retained methods whose names and owners did not change have unchanged parsed bodies.

### Stronger assertions

- Equality contracts cover every arity, every individual component difference, reflexivity, symmetry, transitivity, equal-value hash consistency, null/unrelated objects, and every other arity in both comparison directions.
- Float and double membership/equality tests exercise NaN with different payloads, positive/negative infinity, and both signed zeros at every position for arities 1 through 9.
- Default-constructor tests verify initial values, separate storage for successive array exports, and isolation after an exported array is mutated.
- High-arity BooleanTuple formatting checks now compare the complete output. Export checks compare every component instead of only array length.

### Organization and maintenance

- Arity groups contain arity checks. Equality, reversing, formatting, and export regressions moved to their corresponding groups; cross-operation checks live in contract groups.
- Three sorting tests moved from flattening to `ArraysMutationTest.MutateViaFlatArrayTests`.
- Array mapping groups now follow `Map`, `MapToObj`, and the same primitive order as conversions. Consolidated tuple groups put contracts and scenarios before local data providers.
- Coverage-oriented method names were replaced with behavior descriptions. Removed 69 stale source-line references and replaced the speculative zip explanation with its actual default-value contract.
- The layout audit now enforces array group order and behavior-oriented names. Two new audit-tool checks cover group ordering, omitted groups, nested fixtures, and unrelated classes.

### Verification

The final clean Maven run passed **6,515 invocations**, with no failures, errors, or skips. Class and method ordering were randomized with seed `20261015`, and the JVM used German locale settings (`de_DE`). The settings were verified in the Surefire XML output.

```powershell
mvn clean test jacoco:report '-Djunit.jupiter.testclass.order.default=org.junit.jupiter.api.ClassOrderer$Random' '-Djunit.jupiter.testmethod.order.default=org.junit.jupiter.api.MethodOrderer$Random' '-Djunit.jupiter.execution.order.random.seed=20261015' '-Duser.language=de' '-Duser.country=DE'
node tools/check-test-layout.js
node --test tools/java-test-tools.test.js
git diff --check
```

All seven audit-tool tests passed. The layout audit reports 22 classes, 5,089 templates, zero exact duplicate groups, and no issues. The whitespace check passed.

Fresh baseline and final JaCoCo reports have identical instruction, line, branch, and method coverage. No coverage regressed in any of the 135 production classes; every instrumented source-line counter is unchanged. Open `target/site/jacoco/index.html` for the generated report.

Coverage remains below 100% because of the previously documented unreachable paths in the sealed tuple base implementations: 110 lines and 71 branches. Their classification is retained in the [coverage analysis](unit-test-coverage.md). This cleanup preserves those measurements without coverage exclusions or artificial access to unreachable implementations.
