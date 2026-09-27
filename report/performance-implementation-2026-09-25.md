# Implemented performance and memory improvements

Implemented the contract-preserving findings from the [review](performance-review-2026-09-25.md) in `Arrays.java` and all eight primitive tuple families.

- Added 72 direct `toArray()` overrides for nonempty tuple arities. Each exports fields into a fresh array without initializing the retained cache. All eight `toList()` methods wrap that detached array.
- Added eight singleton `forEach()` overrides with the existing null validation and direct primitive callbacks.
- Replaced boxed priority-queue selection in larger tuple `lowerMedian()` calls with sorting a detached primitive array. Existing arity 0–3 behavior, encounter order, cache identity, NaNs, and signed zeros are preserved.
- Changed DoubleTuple's exceptional sum path to reject non-finite inputs before constructing any exact BigDecimal totals. Compensated ordinary summation and the exact finite-overflow fallback remain intact.
- Added an exact-sum fast path to LongTuple2.median. A signed-overflow guard retains the original BigInteger-backed path whenever the sum does not fit in a long.
- Changed all nine `minRowLength` overloads to stop at the first null/empty row, and all 18 `flatten` overloads to skip the second structural traversal when the total count is zero. Existing result allocation, runtime types, and callback behavior are preserved.

Empty zip-array sharing was not implemented because some methods promise fresh arrays. Varargs rewrites, cache synchronization, pooling, interning, and other unproven candidates remain deferred.

## Validation

Main sources compile cleanly using Java 25 `javac --release 21` and `abacus-common:8.0.1`. Direct JUnit 6.1.1 execution passed **6,282 tests**, with no failures, skips, or aborts. This includes 31 new regression cases across three test classes, with internal checks spanning all tuple arities and primitive families, 4,800 randomized numeric lower medians, floating-point special values, 256 boundary and 10,000 random exact long-pair medians, non-finite double sum positions, callback exceptions, and detached array/list ownership.

The existing public functional-interface validation test now expects 386 methods rather than 378, covering the eight added singleton traversal overrides. All new test classes are included by the repository's `base-test` tag policy. An independent reviewer also checked the numeric changes and ownership behavior. `git diff --check` passes.

The normal offline Maven lifecycle remains unavailable because the publishing extension `central-publishing-maven-plugin:0.11.0` is not cached. Compilation and tests used the local dependencies directly. The source-rewriting `ArraysTest.test_001` ran against a disposable copy, not the production file.

## Before/after allocation diagnostic

The same `ProductionOptimizationProbe` ran against preserved baseline classes and the compiled production changes in separate JVM processes, using HotSpot 25+37, `-Xms256m -Xmx256m`, and 200,000 warmup plus 200,000 measured invocations per operation. It measures thread-allocated bytes and forces exported arrays/lists and newly constructed tuples to escape where indicated.

| Operation | Baseline B/op | Implemented B/op |
| --- | ---: | ---: |
| IntTuple9.lowerMedian | 184 | 56 |
| DoubleTuple9.lowerMedian | 288 | 88 |
| Cold IntTuple9.toArray, including tuple construction | 168 | 112 |
| Cold IntTuple9.toList, including tuple construction | 192 | 136 |
| Warm IntTuple9.toArray | 56 | 56 |
| Cold IntTuple1.forEach, including tuple construction | 48 | 24 |
| LongTuple2.median, safe-sum inputs | 0 | 0 |
| DoubleTuple4.sum with a finite prefix followed by NaN | ~3,342 | 48 |

These are scenario-specific allocation diagnostics, not throughput benchmarks or universal object-size guarantees. JIT optimization eliminated LongTuple2's allocations in both paths in this driver; no measured allocation improvement is claimed for that row. The optimized NaN-sum path still allocated its varargs array in this driver. Values differ from the earlier review's prototype probe because compilation and input profiles affect allocation elimination.

The runner, diagnostic source, preserved baseline classes, compiler/test logs, and exact diagnostic outputs are under `target/performance-review/`. They are temporary build artifacts. The original review's source line references describe commit `accc394e673e24574c1e64a97e9b837f69e73826`; implementation insertions have shifted current line numbers.
