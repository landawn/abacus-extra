# Code/Doc/Comment/Log Review State - 2026-06-17

Scope: every `*.java` file under `src/main/java`.

Ledger path git-ignore check: `.gitignore` currently does not ignore `scripts/code_doc_comment_log_review_2026-06-17/`. Keep this ledger tracked for review evidence unless the owner decides otherwise.

Prior ledgers/notes consulted:
- `scripts/`: no prior review ledger found.
- Repository search for `deliberate`, `false`, `ledger`, `review_`, and `code_doc_comment_log_review`: no prior false-positive list found beyond the current prompt text.

Known deliberate design / false-positive list:
- `src/main/java/com/landawn/abacus/util/Arrays.java`: compact nested class name `Arrays.f` is documented as deliberate and mirrors existing functional helper naming. Do not flag naming style as a bug or documentation issue.

## Coverage Map

Each source file is assigned to exactly one read-only reviewing agent.

| File | Lines | Agent | Batch | Status |
| --- | ---: | --- | --- | --- |
| `src/main/java/com/landawn/abacus/util/Arrays.java` | 23656 | Agent A1 | 1 | fixed |
| `src/main/java/com/landawn/abacus/util/BooleanTuple.java` | 3420 | Agent A2 | 1 | clean |
| `src/main/java/com/landawn/abacus/util/PrimitiveTuple.java` | 103 | Agent A2 | 1 | clean |
| `src/main/java/com/landawn/abacus/util/ImmutableIntArray.java` | 743 | Agent A3 | 1 | clean |
| `src/main/java/com/landawn/abacus/util/Points.java` | 2335 | Agent A3 | 1 | clean |
| `src/main/java/com/landawn/abacus/util/ByteTuple.java` | 4759 | Agent B1 | 2 | clean |
| `src/main/java/com/landawn/abacus/util/ShortTuple.java` | 4531 | Agent B1 | 2 | clean |
| `src/main/java/com/landawn/abacus/util/CharTuple.java` | 4871 | Agent B2 | 2 | clean |
| `src/main/java/com/landawn/abacus/util/IntTuple.java` | 4343 | Agent B2 | 2 | clean |
| `src/main/java/com/landawn/abacus/util/DoubleTuple.java` | 4554 | Agent B3 | 2 | clean |
| `src/main/java/com/landawn/abacus/util/FloatTuple.java` | 4753 | Agent B3 | 2 | fixed |
| `src/main/java/com/landawn/abacus/util/LongTuple.java` | 4557 | Agent B3 | 2 | clean |

## Build/Test Baseline

Build baseline:
- `mvn -q -o clean compile` initially failed because sandbox Maven used `C:\Users\CodexSandboxOffline\.m2\repository` and did not have `org.sonatype.central:central-publishing-maven-plugin:0.10.0`.
- Populated isolated local repository `C:\tmp\abacus-extra-m2` with networked Maven, then `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o clean compile` passed.
- Non-escalated offline `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o test` failed in `testCompile` with classpath/access symptoms (`package com.landawn.abacus.util.* does not exist`) even though `target/classes` contained the main classes and `javap` could load them.
- Escalated offline `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o test` passed.

Full-suite baseline:
- Command: `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o test` outside sandbox after clearing `target/surefire-reports`.
- Surefire text summary: `Tests run: 0, Failures: 0, Errors: 0, Skipped: 0` for suite wrapper.
- Surefire XML authoritative suite attributes: `tests=4242 failures=0 errors=0 skipped=0 time=3.508`.

Protocol:
- Clear `target/surefire-reports` before reading test results.
- Build offline with `mvn -q -o clean compile`.
- Record full-suite baseline from the `Tests run: ...` line, not Maven build success.

## Final Verification

Targeted tests:
- Cleared `target/surefire-reports`.
- Command: `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o -Dtest=ArraysTest,FloatTupleTest test` outside sandbox.
- Exit code: 0.
- Surefire text summaries: both wrapper reports showed `Tests run: 0, Failures: 0, Errors: 0, Skipped: 0`.
- Surefire XML: `ArraysTest tests=1648 failures=0 errors=0 skipped=0`; `FloatTupleTest tests=394 failures=0 errors=0 skipped=0`.

Final full suite:
- Cleared `target/surefire-reports`.
- Command: `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o test` outside sandbox.
- Exit code: 0.
- Surefire text summary: `Tests run: 0, Failures: 0, Errors: 0, Skipped: 0` for suite wrapper.
- Surefire XML authoritative suite attributes: `tests=4242 failures=0 errors=0 skipped=0 time=3.29`.
- Baseline vs after: XML count unchanged at `4242`; failures/errors/skips remained zero.

Diff/format checks:
- `git diff --check -- src/main/java/com/landawn/abacus/util/Arrays.java src/main/java/com/landawn/abacus/util/FloatTuple.java scripts/code_doc_comment_log_review_2026-06-17/state.md` passed with no whitespace errors.
- Verified CRLF line endings in `Arrays.java` and `FloatTuple.java` after edits.

Code fixes/tests:
- No behavior/code fixes were made.
- No regression tests were added because no correctness bugs were confirmed.
- All edits are Javadoc/comment-only.

## Findings

### A1-JDOC-001 - fixed

- Workstream: Javadoc/comments
- Severity: low
- Confidence: confirmed
- File/line: `src/main/java/com/landawn/abacus/util/Arrays.java:2466` representative; repeated callback docs across primitive/object mapping, update, replace, and zip methods.
- Trace/repro: docs said callbacks such as `mapper`, `operator`, `predicate`, and `zipFunction` "must not be null" unconditionally, while representative methods return before callback dereference for null/empty input. Example: `Arrays.updateAll((boolean[]) null, null)` and `Arrays.updateAll(new boolean[0], null)` return before `operator.applyAsBoolean`, but `Arrays.updateAll(new boolean[] { true }, null)` dereferences `operator` while processing the element.
- Fix: documentation-only. Updated 237 callback `@param` lines so `mapper`/`operator`/`predicate` are required when elements are processed and `zipFunction` is required when result elements are produced. Left strict `action`, `targetElementType`, and array-parameter docs unchanged where the implementation validates eagerly or needs the value for allocation/type inference.
- Verification: verified representative method bodies and searched for remaining strict callback `@param` lines; none remain for `mapper`, `operator`, `predicate`, or `zipFunction`. Verified `Arrays.java` CRLF line endings after scripted edit. Ran `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o clean compile` successfully.

### B3-JDOC-001 - fixed

- Workstream: Javadoc/comments
- Severity: low
- Confidence: confirmed
- File/line: `src/main/java/com/landawn/abacus/util/FloatTuple.java:2681`, `:3062`, `:3448`, `:3838`, `:4244`, `:4656`.
- Trace/repro: concrete `FloatTuple4` through `FloatTuple9` hash examples refer to "NaN bits" or "same NaN bit pattern", but `hashCode()` uses `Float.floatToIntBits(...)`, which canonicalizes NaN payloads. Base `FloatTuple` hash Javadoc correctly says NaN hashes to a single canonical value.
- Fix: documentation-only; replaced raw/same NaN bit wording with canonical-NaN wording on six hash example comments.
- Verification: inspected `FloatTuple` hash implementations and confirmed use of `Float.floatToIntBits`. Searched `DoubleTuple` for analogous raw NaN-bit wording; none found. Verified `FloatTuple.java` CRLF line endings after edit. Ran `mvn -q -Dmaven.repo.local=C:\tmp\abacus-extra-m2 -o clean compile` successfully.

## Batch Notes

### Batch 1

Status: complete. A1 fixed comment-only finding; A2 and A3 complete clean.

Agents:
- A1: `Arrays.java` - fixed one Javadoc/comment finding on callback null contract.
- A2: `BooleanTuple.java`, `PrimitiveTuple.java` - clean; no findings.
- A3: `ImmutableIntArray.java`, `Points.java` - clean; no findings.

### Batch 2

Status: complete. B1 and B2 complete clean; B3 fixed one comment-only FloatTuple finding.

Agents:
- B1: `ByteTuple.java`, `ShortTuple.java` - clean; no findings.
- B2: `CharTuple.java`, `IntTuple.java` - clean; no findings.
- B3: `DoubleTuple.java`, `FloatTuple.java`, `LongTuple.java` - fixed one Javadoc/comment finding in `FloatTuple.java`.

## False Positives Added

- `src/main/java/com/landawn/abacus/util/Arrays.java`: numeric `toBoolean` conversions treating negative values as `false` are documented and tested behavior.
- `src/main/java/com/landawn/abacus/util/Arrays.java`: object zip type inference rejecting fully null/untyped inputs unless an explicit target element type or usable default is provided is covered by tests and appears intentional.
- `src/main/java/com/landawn/abacus/util/CharTuple.java`: `forEach(null)` documenting `IllegalArgumentException` is not a doc bug; `N.checkArgNotNull` confirms that exception type.
- `src/main/java/com/landawn/abacus/util/IntTuple.java`: `sum()` overflow documentation is not stale; the helper path throws when the total does not fit in `int`.
- `src/main/java/com/landawn/abacus/util/IntTuple.java` and analogous tuple classes: arity-specific `accept`/`map`/`filter` methods lacking explicit null checks are not contract mismatches; direct invocation of a null functional argument yields `NullPointerException` as documented.
