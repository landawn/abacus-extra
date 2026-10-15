# Test maintenance tools

Run these commands from the repository root with Node.js and JDK 21 or later on `PATH`. No npm installation is needed. `TestSourceIndex.java` uses the JDK compiler parser; it parses source without resolving project dependencies. Compiled helper classes are cached under `target/test-audit-index`.

| Command | Purpose |
| --- | --- |
| `node tools/check-test-layout.js` | Check discovery, tuple/array operation order, behavior names, assertion conventions, and exact duplicate candidates; nonzero exit on violations. |
| `node --test tools/java-test-tools.test.js` | Check parsing, literal preservation, fixture separation, helper assertions, syntax errors, varargs metadata, and operation-group ordering. |
| `node tools/find-duplicate-tests.js` | Report exact parsed-body duplicate candidates with fixture context. |
| `node tools/find-duplicate-tests.js --scope=same-file --summary-by-file` | Summarize candidates within files. |
| `node tools/find-duplicate-tests.js --scope=cross-file --exclude-type-variants` | Review cross-file candidates while retaining primitive-family variants. |
| `node tools/find-nonasserting-tests.js` | Find tests without assertions or asserting local helpers; also list try/catch blocks for review. |
| `node tools/audit-public-method-test-gaps.js` | Find public methods without a candidate test call of matching name/arity. This is not a coverage report. |
| `node tools/dedupe-util-tests.js --match=IntTupleTest.java` | Preview exact duplicate removals within the specified file and lexical owner. |

All commands except an explicit dedupe apply are read-only with respect to source files. They write only generated compiler classes or temporary parser fixtures under `target`. Invalid Java syntax fails explicitly instead of producing a partial inventory. Reports count annotated methods/templates; Maven reports executed invocations, including parameterized cases.

## Applying duplicate removals

Review the dry-run's retained method and removed methods first. Apply only the reviewed scope:

```text
node tools/dedupe-util-tests.js --apply --match=IntTupleTest.java
mvn -Dtest=IntTupleTest test
```

`--apply` requires a nonempty `--match`. Automatic edits are limited to one file and lexical owner per duplicate group. The comparison preserves literals, parameter sources, imports, annotations, enclosing fields, inheritance, and helper bodies. It removes method ranges using compiler offsets, without rebuilding files from Git history. Inspect the diff and rerun the affected tests after applying; the tool does not determine whether contextual effects such as extensions or test-name-sensitive fixtures make two scenarios interchangeable.

The former `consolidate-test-classes.js` was removed because it reconstructed files from `HEAD` and could discard working-tree changes. Use deliberate source edits to reorganize test classes.

## Limits

Duplicate detection is conservative, not semantic equivalence analysis. Similar code for different primitive types, different data, or different callbacks can test different contracts. Local variable renames can hide duplicates; review those manually.

Assertion discovery follows unqualified and `this` helper calls through enclosing classes, with cycle protection. It does not resolve overload types, inherited helpers, external helpers, custom assertion libraries, or JUnit extensions. Its findings require review.

The public-method audit includes explicitly public methods in nested production types and distinguishes varargs from ordinary array parameters. Calls are matched by name and argument count without symbol resolution. Helper calls, implicit record accessors, generic dispatch, and same-arity overloads can produce false positives or negatives. Use fresh JaCoCo reports and a behavior matrix to assess coverage.

For test ownership and conventions, see [src/test/README.md](../src/test/README.md). The original cleanup is recorded in [report/unit-test-cleanup.md](../report/unit-test-cleanup.md). The latest plan, consolidation mapping, and measurements are in [report/unit-test-maintenance.md](../report/unit-test-maintenance.md).
