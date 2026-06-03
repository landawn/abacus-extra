# scripts/

Project-agnostic tooling for auditing and tidying the Javadoc **"Usage Examples"**
blocks (`<pre>{@code ... }</pre>`) of public methods in public classes anywhere
under `src/main/java/`. Nothing is tied to a package or class name, so the same
tools work on any project's source tree.

These support a **comments-only** workflow: audit which documented public methods
need examples, write/fix examples (verified against real behavior with a throwaway
test), normalize the block structure, align the columns, and confirm only comments
changed. The tools **never invent behavior-comment text** — that is verified with a
test, never guessed.

## `jdoc.py` — the toolkit

One CLI over the `jdoc_tools/` package. **Reports never write; fixers are dry-run
unless `--apply`.** Run `python scripts/jdoc.py -h` for the full name list.

```bash
# Step A — what needs work
python scripts/jdoc.py audit                                   # per-file: documented methods vs. missing examples
python scripts/jdoc.py report missing-examples <file|dir>      # documented public methods with no examples block
python scripts/jdoc.py report methods          <file|dir>      # all documented public methods + example status

# audit existing examples (read-only; exit 1 on findings)
python scripts/jdoc.py report usage-check               <file|dir>   # Section-2 block structure
python scripts/jdoc.py report missing-behavior-comments <file|dir>   # call lines lacking // returns|throws
python scripts/jdoc.py validate src/main/java                        # all structural checks across the tree

# normalize structure / align (Step E) — writes only with --apply
python scripts/jdoc.py fix usage-spacing --apply <file|dir>
python scripts/jdoc.py fix align         --apply <file|dir>
python scripts/jdoc.py cleanup           --apply src/main/java

# guard — confirm a file's git diff changed only comments
python scripts/jdoc.py verify-comment-only <file>
```

All tree commands default their root to `src/main/java` and accept repeatable
`--exclude-package <pkg>` if you ever need to skip one.

### Layout
| File | Role |
|------|------|
| `jdoc.py` | CLI dispatcher (single entry point) |
| `jdoc_tools/region.py` | shared Javadoc/code-block + light Java-structure primitives |
| `jdoc_tools/reports.py` | read-only checks (audits) |
| `jdoc_tools/fixes.py` | in-place structural fixers (dry-run by default) |
| `jdoc_tools/pipeline.py` | file discovery + `audit`/`validate`/`cleanup` + git helpers |

### Reports (read-only)
`methods`, `missing-examples`, `usage-check`, `missing-behavior-comments`,
`placeholders`, `sample-member-misuse`, `suspicious-strings`, `scan`,
`changed-methods`.

### Fixers (structure / mechanical only — never guess comment text)
`usage-indent`, `usage-spacing`, `double-blank`, `move-comments-into-pre`,
`type-placeholders`, `literal-artifacts`, `sample-member-misuse`,
`assignment-style`, plus the two delegated to existing scripts: `align`,
`move-usage-before-tags`.

## Other scripts (kept, generic)
- `align_jdoc_examples.py` — display-width-aware column alignment of `{@code}` blocks (Step E; reused by `cleanup`/`validate`).
- `fix_javadoc_format4.py` — move the Usage Examples block before the `@tags` and collapse double blanks (reused by `cleanup`).
- `verify_jdoc_test.py` + `JdocTestRunner.java` — Maven-free runner for a single throwaway `…JavadocVerifyTest` (a fast alternative for Step C: `mvn -o test -Dtest=…`).
- `check_corruption.py` — scan sources for decompiler artifacts.

## Notes
- `svn` was replaced with **`git`** (`verify-comment-only`, `changed-methods`).
- The earlier content-guessing fixers (which rewrote comment *text* using one
  library's vocabulary) and the `add_missing_examples.py` example *generator*
  were removed: they conflict with the rule "Never guess expected values; verify
  with tests." Auditing + structural normalization is automated; the example
  *content* is written and test-verified by the agent.
