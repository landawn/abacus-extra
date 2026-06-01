#!/usr/bin/env python3
"""
Batch-adds "Usage Examples" blocks to Dataset.java methods that are missing them.
Run: python scripts/add_missing_examples.py
"""
import re, sys

FILE = "src/main/java/com/landawn/abacus/util/Dataset.java"

# -- helper: find all method Javadoc blocks that are missing Usage Examples --
def find_missing_matches(content):
    """Returns list of (insertion_offset, insertion_text, method_sig_snippet) for each missing block."""
    pattern = re.compile(r'(/\*\*[\s\S]*?\*/)\s*\n(\s*)((?:<\w+[\s\S]*?>)?\s*\w[\w\s<>,]+\s+\w+\s*\([^)]*\)[^{;]*(?:\{|\;))', re.MULTILINE)
    results = []
    for m in pattern.finditer(content):
        javadoc = m.group(1)
        indent = m.group(2)
        method_decl = m.group(3).strip()
        if 'Usage Examples' not in javadoc:
            # Find insertion point: after the description text, before @param/@return/@throws/@see
            # Strategy: insert after the last <br /> or paragraph before the tags
            # Actually, we look for the last closing </p> or <br /> before @param/@return/@throws/@see
            # Or if no </p>, insert before @param
            tags_match = re.search(r'(?:@param|@return|@throws|@see|@since|@deprecated)', javadoc)
            if tags_match:
                insert_offset = m.start() + tags_match.start()
            else:
                # insert before the last newline before */
                insert_offset = m.end() - 3  # before ' */'

            # Generate examples based on method signature
            examples = generate_examples(method_decl, indent)
            if examples:
                results.append((insert_offset, examples, method_decl))
    return results

def generate_examples(sig, indent):
    """Generate usage examples block for the given method signature."""
    sig_lower = sig.lower()
    SP = "     * "
    examples = None

    # --- getRow overloads (missing) ---
    if sig.startswith('<T> T getRow(int rowIndex, Collection<String> columnNames, Class<? extends T> rowType)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "Map<String, Object> row = ds.getRow(0, Arrays.asList(\"id\", \"name\"), Map.class);\n"
            + SP + "// row contains: {\"id\": 1, \"name\": \"Alice\"}\n"
            + SP + "\n"
            + SP + "ds.getRow(0, Arrays.asList(\"bad_col\"), Map.class);\n"
            + SP + "// throws IllegalArgumentException\n"
            + SP + "ds.getRow(5, Arrays.asList(\"id\"), Map.class);\n"
            + SP + "// throws IndexOutOfBoundsException\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> T getRow(int rowIndex, IntFunction<? extends T> rowSupplier)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "List<Object> row = ds.getRow(0, ArrayList::new);\n"
            + SP + "// row contains: [1, \"Alice\"]\n"
            + SP + "\n"
            + SP + "ds.getRow(-1, ArrayList::new);\n"
            + SP + "// throws IndexOutOfBoundsException\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> T getRow(int rowIndex, Collection<String> columnNames, IntFunction<? extends T> rowSupplier)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "ArrayList row = ds.getRow(0, Arrays.asList(\"name\", \"age\"), ArrayList::new);\n"
            + SP + "// row contains: [\"Alice\", 25]\n"
            + SP + "\n"
            + SP + "ds.getRow(0, (Collection<String>) null, ArrayList::new);\n"
            + SP + "// throws IllegalArgumentException\n"
            + SP + "}</pre>\n"
        )

    # --- firstRow overloads (missing) ---
    elif sig.startswith('<T> Optional<T> firstRow(Class<? extends T> rowType)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "Optional<Object[]> first = ds.firstRow(Object[].class);\n"
            + SP + "assertTrue(first.isPresent());\n"
            + SP + "// first.get() returns [1, \"Alice\"]\n"
            + SP + "\n"
            + SP + "Dataset empty = Dataset.empty();\n"
            + SP + "Optional<Map> emptyFirst = empty.firstRow(Map.class);\n"
            + SP + "assertFalse(emptyFirst.isPresent());\n"
            + SP + "// returns an empty Optional\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> firstRow(Collection<String> columnNames, Class<? extends T> rowType)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "Optional<Map> first = ds.firstRow(Arrays.asList(\"id\", \"name\"), Map.class);\n"
            + SP + "assertTrue(first.isPresent());\n"
            + SP + "// first.get() returns Map with {\"id\": 1, \"name\": \"Alice\"}\n"
            + SP + "\n"
            + SP + "Optional<Object[]> empty = Dataset.empty().firstRow(Arrays.asList(\"id\"), Object[].class);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> firstRow(IntFunction<? extends T> rowSupplier)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "Optional<ArrayList> first = ds.firstRow(ArrayList::new);\n"
            + SP + "assertTrue(first.isPresent());\n"
            + SP + "// first.get() returns [1, \"Alice\"]\n"
            + SP + "\n"
            + SP + "Optional<ArrayList> empty = Dataset.empty().firstRow(ArrayList::new);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> firstRow(Collection<String> columnNames, IntFunction<? extends T> rowSupplier)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "Optional<ArrayList> first = ds.firstRow(Arrays.asList(\"name\", \"age\"), ArrayList::new);\n"
            + SP + "assertTrue(first.isPresent());\n"
            + SP + "// first.get() returns [\"Alice\", 25]\n"
            + SP + "\n"
            + SP + "Optional<ArrayList> empty = Dataset.empty().firstRow(Arrays.asList(\"id\"), ArrayList::new);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )

    # --- lastRow overloads (6 methods, first 4 already have some form, but missing in list includes all) ---
    elif sig == 'Optional<Object[]> lastRow();':
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "Optional<Object[]> last = ds.lastRow();\n"
            + SP + "assertTrue(last.isPresent());\n"
            + SP + "// last.get() returns [2, \"Bob\"]\n"
            + SP + "\n"
            + SP + "Optional<Object[]> empty = Dataset.empty().lastRow();\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> lastRow(Class<? extends T> rowType)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "Optional<Object[]> last = ds.lastRow(Object[].class);\n"
            + SP + "assertTrue(last.isPresent());\n"
            + SP + "// last.get() returns [2, \"Bob\"]\n"
            + SP + "\n"
            + SP + "Optional<Map> empty = Dataset.empty().lastRow(Map.class);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> lastRow(Collection<String> columnNames, Class<? extends T> rowType)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "Optional<Map> last = ds.lastRow(Arrays.asList(\"id\", \"name\"), Map.class);\n"
            + SP + "assertTrue(last.isPresent());\n"
            + SP + "// last.get() returns {\"id\": 2, \"name\": \"Bob\"}\n"
            + SP + "\n"
            + SP + "Optional<Map> empty = Dataset.empty().lastRow(Arrays.asList(\"id\"), Map.class);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> lastRow(IntFunction<? extends T> rowSupplier)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "Optional<ArrayList> last = ds.lastRow(ArrayList::new);\n"
            + SP + "assertTrue(last.isPresent());\n"
            + SP + "// last.get() returns [2, \"Bob\"]\n"
            + SP + "\n"
            + SP + "Optional<ArrayList> empty = Dataset.empty().lastRow(ArrayList::new);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Optional<T> lastRow(Collection<String> columnNames, IntFunction<? extends T> rowSupplier)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "Optional<ArrayList> last = ds.lastRow(Arrays.asList(\"name\", \"age\"), ArrayList::new);\n"
            + SP + "assertTrue(last.isPresent());\n"
            + SP + "// last.get() returns [\"Bob\", 30]\n"
            + SP + "\n"
            + SP + "Optional<ArrayList> empty = Dataset.empty().lastRow(Arrays.asList(\"id\"), ArrayList::new);\n"
            + SP + "assertFalse(empty.isPresent());\n"
            + SP + "}</pre>\n"
        )

    # --- forEach overloads (missing) ---
    elif sig.startswith('<E extends Exception> void forEach(Throwables.Consumer<? super DisposableObjArray, E> action)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "ds.forEach(row -> System.out.println(\"Row: \" + row.get(0) + \", \" + row.get(1)));\n"
            + SP + "// prints: Row: 1, Alice\n"
            + SP + "//         Row: 2, Bob\n"
            + SP + "\n"
            + SP + "List<String> names = new ArrayList<>();\n"
            + SP + "ds.forEach(row -> names.add((String) row.get(1)));\n"
            + SP + "// names contains [\"Alice\", \"Bob\"]\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(Collection<String> columnNames, Throwables.Consumer<? super DisposableObjArray, E> action)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "ds.forEach(Arrays.asList(\"name\", \"age\"), row -> {\n"
            + SP + "    System.out.println(row.get(0) + \" is \" + row.get(1) + \" years old\");\n"
            + SP + "});\n"
            + SP + "// Only retrieves columns \"name\" and \"age\" for each row\n"
            + SP + "\n"
            + SP + "ds.forEach((Collection<String>) null, row -> {});\n"
            + SP + "// throws IllegalArgumentException\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(int fromRowIndex, int toRowIndex, Throwables.Consumer'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}, {3, \"Charlie\"}});\n"
            + SP + "ds.forEach(0, 2, row -> System.out.println(\"Row: \" + row.get(0)));\n"
            + SP + "// Only processes rows 0 and 1 (inclusive, exclusive)\n"
            + SP + "\n"
            + SP + "ds.forEach(-1, 1, row -> {});\n"
            + SP + "// throws IndexOutOfBoundsException\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(int fromRowIndex, int toRowIndex, Collection<String> columnNames,'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}, {3, \"Charlie\", 35}});\n"
            + SP + "ds.forEach(0, 2, Arrays.asList(\"name\", \"age\"), row -> {\n"
            + SP + "    System.out.println(row.get(0) + \": \" + row.get(1));\n"
            + SP + "});\n"
            + SP + "// Only processes first 2 rows with selected columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(Tuple2<String, String> columnNames, Throwables.BiConsumer'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "ds.forEach(Tuple.of(\"id\", \"name\"), (id, name) ->\n"
            + SP + "    System.out.println(id + \": \" + name));\n"
            + SP + "// prints: 1: Alice\n"
            + SP + "//         2: Bob\n"
            + SP + "\n"
            + SP + "ds.forEach(Tuple.of(\"bad\", \"name\"), (a, b) -> {});\n"
            + SP + "// throws IllegalArgumentException\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(int fromRowIndex, int toRowIndex, Tuple2<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}, {3, \"Charlie\"}});\n"
            + SP + "ds.forEach(0, 2, Tuple.of(\"id\", \"name\"), (id, name) ->\n"
            + SP + "    System.out.println(id + \": \" + name));\n"
            + SP + "// Only processes first 2 rows\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(Tuple3<String, String, String> columnNames, Throwables.TriConsumer'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
            + SP + "ds.forEach(Tuple.of(\"id\", \"name\", \"age\"), (id, name, age) ->\n"
            + SP + "    System.out.println(id + \": \" + name + \", \" + age));\n"
            + SP + "// prints: 1: Alice, 25\n"
            + SP + "//         2: Bob, 30\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<E extends Exception> void forEach(int fromRowIndex, int toRowIndex, Tuple3<String, String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}, {3, \"Charlie\", 35}});\n"
            + SP + "ds.forEach(0, 2, Tuple.of(\"id\", \"name\", \"age\"), (id, name, age) ->\n"
            + SP + "    System.out.println(id + \": \" + name));\n"
            + SP + "// Only processes first 2 rows\n"
            + SP + "}</pre>\n"
        )

    # --- toList filter/converter overloads (4 missing) ---
    elif sig.startswith('<T> List<T> toList(Predicate<? super String> columnNameFilter, Function<? super String, String> columnNameConverter, Class<? extends T> rowType)'):
        if 'IntFunction' not in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
                + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
                + SP + "List<Map> maps = ds.toList(\n"
                + SP + "    col -> !\"id\".equals(col),\n"
                + SP + "    col -> col.toUpperCase(),\n"
                + SP + "    Map.class);\n"
                + SP + "// Result: [{\"NAME\": \"Alice\", \"AGE\": 25}, {\"NAME\": \"Bob\", \"AGE\": 30}]\n"
                + SP + "// Filters out \"id\" column, converts \"name\" -> \"NAME\", \"age\" -> \"AGE\"\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\"),\n"
                + SP + "    new Object[][] {{1, \"Alice\", 25}, {2, \"Bob\", 30}});\n"
                + SP + "List<ArrayList> lists = ds.toList(\n"
                + SP + "    col -> !\"id\".equals(col),\n"
                + SP + "    col -> col.toUpperCase(),\n"
                + SP + "    ArrayList::new);\n"
                + SP + "// Each row is [\"Alice\", 25] in a new ArrayList\n"
                + SP + "}</pre>\n"
            )
    elif sig.startswith('<T> List<T> toList(int fromRowIndex, int toRowIndex, Predicate'):
        if 'IntFunction' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\", \"score\"),\n"
                + SP + "    new Object[][] {{1, \"Alice\", 25, 95.5}, {2, \"Bob\", 30, 87.3}, {3, \"Charlie\", 35, 92.1}});\n"
                + SP + "List<ArrayList> list = ds.toList(0, 2,\n"
                + SP + "    col -> col.startsWith(\"n\"),\n"
                + SP + "    col -> col.toUpperCase(),\n"
                + SP + "    ArrayList::new);\n"
                + SP + "// Each row contains only \"NAME\" value in a new ArrayList\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"name\", \"age\", \"score\"),\n"
                + SP + "    new Object[][] {{1, \"Alice\", 25, 95.5}, {2, \"Bob\", 30, 87.3}, {3, \"Charlie\", 35, 92.1}});\n"
                + SP + "List<Map> map = ds.toList(0, 2,\n"
                + SP + "    col -> col.startsWith(\"n\"),\n"
                + SP + "    col -> col.toUpperCase(),\n"
                + SP + "    Map.class);\n"
                + SP + "// Returns first 2 rows with only the \"name\" column mapped to \"NAME\"\n"
                + SP + "}</pre>\n"
            )

    # --- toEntities overloads (missing) ---
    elif sig.startswith('<T> List<T> toEntities(int fromRowIndex, int toRowIndex, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {100, \"Bob\", 1, \"iPhone\"},\n"
            + SP + "        {200, \"Alice\", 3, \"Android\"}\n"
            + SP + "    });\n"
            + SP + "List<Account> accounts = ds.toEntities(0, 2,\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Maps \"d.*\" columns to the \"devices\" field in Account\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> List<T> toEntities(Collection<String> columnNames, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {100, \"Bob\", 1, \"iPhone\"}\n"
            + SP + "    });\n"
            + SP + "List<Account> accounts = ds.toEntities(\n"
            + SP + "    N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Only maps specified columns with column prefix mapping\n"
            + SP + "}</pre>\n"
        )

    # --- toMergedEntities overloads (missing) ---
    elif sig.startswith('<T> List<T> toMergedEntities(Collection<String> selectPropNames, Class<? extends T> beanClass)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"devices.id\", \"devices.model\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {100, \"Bob\", 1, \"iPhone\"},\n"
            + SP + "        {100, \"Bob\", 2, \"MacBook\"},\n"
            + SP + "        {200, \"Alice\", 3, \"Android\"}\n"
            + SP + "    });\n"
            + SP + "List<Account> accounts = ds.toMergedEntities(\n"
            + SP + "    N.asList(\"id\", \"name\"), Account.class);\n"
            + SP + "// Bob's two rows merge into one, Alice's remains one row\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> List<T> toMergedEntities(String idPropName, Collection<String> selectPropNames,'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"devices.id\", \"devices.model\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {100, \"Bob\", 1, \"iPhone\"},\n"
            + SP + "        {100, \"Bob\", 2, \"MacBook\"},\n"
            + SP + "        {200, \"Alice\", 3, \"Android\"}\n"
            + SP + "    });\n"
            + SP + "List<Account> accounts = ds.toMergedEntities(\"id\",\n"
            + SP + "    N.asList(\"id\", \"name\"), Account.class);\n"
            + SP + "// Merges rows with same \"id\" value, includes selected columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> List<T> toMergedEntities(String idPropName, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {100, \"Bob\", 1, \"iPhone\"},\n"
            + SP + "        {100, \"Bob\", 2, \"MacBook\"},\n"
            + SP + "        {200, \"Alice\", 3, \"Android\"}\n"
            + SP + "    });\n"
            + SP + "List<Account> accounts = ds.toMergedEntities(\"id\",\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Merges by \"id\", maps \"d.*\" columns to \"devices\" field\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> List<T> toMergedEntities(Collection<String> idPropNames, Collection<String> selectPropNames, Class'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"dept\", \"year\", \"budget\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {\"ENG\", 2024, 10000},\n"
            + SP + "        {\"ENG\", 2024, 20000},\n"
            + SP + "        {\"HR\", 2024, 5000}\n"
            + SP + "    });\n"
            + SP + "// Merges rows with same composite key (dept, year)\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> List<T> toMergedEntities(Collection<String> idPropNames, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"dept\", \"year\", \"d.id\", \"d.budget\"),\n"
            + SP + "    new Object[][] {\n"
            + SP + "        {\"ENG\", 2024, 1, 10000},\n"
            + SP + "        {\"ENG\", 2024, 2, 20000},\n"
            + SP + "        {\"HR\", 2024, 3, 5000}\n"
            + SP + "    });\n"
            + SP + "List<Account> accounts = ds.toMergedEntities(\n"
            + SP + "    N.asList(\"dept\", \"year\"),\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Merges by composite key (dept, year), with column prefix mapping\n"
            + SP + "}</pre>\n"
        )

    # --- topBy overloads (missing) ---
    elif sig.startswith('Dataset topBy(Collection<String> columnNames, int n) throws IllegalArgumentException;'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"name\", \"score\"),\n"
            + SP + "    new Object[][] {{\"A\", 95}, {\"B\", 80}, {\"C\", 88}});\n"
            + SP + "Dataset top2 = ds.topBy(N.asList(\"score\"), 2);\n"
            + SP + "// top2 contains rows with top 2 scores: {\"A\", 95}, {\"C\", 88}\n"
            + SP + "\n"
            + SP + "ds.topBy(N.asList(\"score\"), 0);\n"
            + SP + "// returns an empty Dataset\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset topBy(Collection<String> columnNames, int n, Comparator'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"name\", \"score\"),\n"
            + SP + "    new Object[][] {{\"A\", 95}, {\"B\", 80}, {\"C\", 88}});\n"
            + SP + "// Custom comparator: top 2 by ascending score\n"
            + SP + "Dataset top2 = ds.topBy(N.asList(\"score\"), 2,\n"
            + SP + "    Comparator.comparing(row -> (Integer) row[0]));\n"
            + SP + "// Returns rows with lowest 2 scores: {\"B\", 80}, {\"C\", 88}\n"
            + SP + "}</pre>\n"
        )

    # --- filter overloads (missing - many) ---
    elif sig.startswith('Dataset filter(int fromRowIndex, int toRowIndex, Predicate<? super DisposableObjArray> filter)'):
        if ', int max)' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"age\", \"name\"),\n"
                + SP + "    new Object[][] {{25, \"Alice\"}, {30, \"Bob\"}, {35, \"Charlie\"}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3,\n"
                + SP + "    row -> (Integer) row.get(0) > 28, 1);\n"
                + SP + "// At most 1 row from range [0,3) where age > 28\n"
                + SP + "}</pre>\n"
            )
        else:
            pass  # handled below
    elif sig.startswith('Dataset filter(Tuple2<String, String> columnNames, BiPredicate'):
        if ', int max)' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"price\", \"qty\"),\n"
                + SP + "    new Object[][] {{10.0, 5}, {20.0, 3}, {5.0, 10}});\n"
                + SP + "Dataset filtered = ds.filter(\n"
                + SP + "    Tuple.of(\"price\", \"qty\"), (p, q) -> (Double) p * (Integer) q > 50, 2);\n"
                + SP + "// At most 2 rows where price * qty > 50\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"price\", \"qty\"),\n"
                + SP + "    new Object[][] {{10.0, 5}, {20.0, 3}, {5.0, 10}});\n"
                + SP + "Dataset filtered = ds.filter(\n"
                + SP + "    Tuple.of(\"price\", \"qty\"), (p, q) -> (Double) p * (Integer) q > 50);\n"
                + SP + "// Returns rows where price * qty > 50\n"
                + SP + "}</pre>\n"
            )
    elif sig.startswith('Dataset filter(Tuple3<String, String, String> columnNames, TriPredicate'):
        if ', int max)' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"x\", \"y\", \"z\"),\n"
                + SP + "    new Object[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});\n"
                + SP + "Dataset filtered = ds.filter(Tuple.of(\"x\", \"y\", \"z\"),\n"
                + SP + "    (x, y, z) -> (Integer) x + (Integer) y > (Integer) z, 2);\n"
                + SP + "// At most 2 rows where x + y > z\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"x\", \"y\", \"z\"),\n"
                + SP + "    new Object[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});\n"
                + SP + "Dataset filtered = ds.filter(Tuple.of(\"x\", \"y\", \"z\"),\n"
                + SP + "    (x, y, z) -> (Integer) x + (Integer) y > (Integer) z);\n"
                + SP + "// Returns rows where x + y > z\n"
                + SP + "}</pre>\n"
            )
    elif sig.startswith('Dataset filter(String columnName, Predicate<?> filter)'):
        if ', int max)' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"age\"),\n"
                + SP + "    new Object[][] {{25}, {30}, {35}});\n"
                + SP + "Dataset filtered = ds.filter(\"age\", age -> (Integer) age > 28, 2);\n"
                + SP + "// At most 2 rows where age > 28\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"age\"),\n"
                + SP + "    new Object[][] {{25}, {30}, {35}});\n"
                + SP + "Dataset filtered = ds.filter(\"age\", age -> (Integer) age > 28);\n"
                + SP + "// Returns rows where age > 28\n"
                + SP + "\n"
                + SP + "ds.filter(\"nonexistent\", v -> false);\n"
                + SP + "// throws IllegalArgumentException\n"
                + SP + "}</pre>\n"
            )
    elif sig.startswith('Dataset filter(Collection<String> columnNames, Predicate'):
        if ', int max)' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    new Object[][] {{1, 10}, {5, 3}, {3, 8}});\n"
                + SP + "Dataset filtered = ds.filter(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    row -> (Integer) row.get(0) + (Integer) row.get(1) > 10, 2);\n"
                + SP + "// At most 2 rows where a + b > 10\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    new Object[][] {{1, 10}, {5, 3}, {3, 8}});\n"
                + SP + "Dataset filtered = ds.filter(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    row -> (Integer) row.get(0) + (Integer) row.get(1) > 10);\n"
                + SP + "// Returns rows where a + b > 10\n"
                + SP + "}</pre>\n"
            )

    # --- mapColumn / mapColumns / flatMapColumn / flatMapColumns (missing) ---
    elif sig.startswith('Dataset mapColumn(String fromColumnName, String newColumnName, Collection<String> copyingColumnNames, Function<?, ?> mapper)'):
        pass  # handled by generic below
    elif sig.startswith('Dataset mapColumns(Tuple2<String, String> fromColumnNames, String newColumnName, Collection<String> copyingColumnNames, BiFunction'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"firstName\", \"lastName\"),\n"
            + SP + "    new Object[][] {{\"John\", \"Doe\"}, {\"Jane\", \"Smith\"}});\n"
            + SP + "Dataset result = ds.mapColumns(\n"
            + SP + "    Tuple.of(\"firstName\", \"lastName\"), \"fullName\",\n"
            + SP + "    N.asList(\"firstName\", \"lastName\"),\n"
            + SP + "    (first, last) -> first + \" \" + last);\n"
            + SP + "// Adds \"fullName\" column while keeping existing columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset mapColumns(Tuple3<String, String, String> fromColumnNames'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"x\", \"y\", \"z\"),\n"
            + SP + "    new Object[][] {{1, 2, 3}, {4, 5, 6}});\n"
            + SP + "Dataset result = ds.mapColumns(\n"
            + SP + "    Tuple.of(\"x\", \"y\", \"z\"), \"sum\",\n"
            + SP + "    N.asList(\"x\", \"y\", \"z\"),\n"
            + SP + "    (x, y, z) -> (Integer)x + (Integer)y + (Integer)z);\n"
            + SP + "// Adds \"sum\" column with x+y+z while keeping existing columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset mapColumns(Collection<String> fromColumnNames, String newColumnName, Collection<String> copyingColumnNames,'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"x\", \"y\", \"z\"),\n"
            + SP + "    new Object[][] {{1, 2, 3}, {4, 5, 6}});\n"
            + SP + "Dataset result = ds.mapColumns(\n"
            + SP + "    N.asList(\"x\", \"y\", \"z\"), \"sum\",\n"
            + SP + "    N.asList(\"x\", \"y\", \"z\"),\n"
            + SP + "    row -> (Integer)row.get(0) + (Integer)row.get(1) + (Integer)row.get(2));\n"
            + SP + "// Adds \"sum\" column while keeping existing columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset flatMapColumn(String fromColumnName, String newColumnName, String copyingColumnName, Function'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"tags\"),\n"
            + SP + "    new Object[][] {{1, Arrays.asList(\"java\", \"sql\")}, {2, Arrays.asList(\"c++\", \"go\")}});\n"
            + SP + "// Flat-maps the \"tags\" list column, duplicating \"id\" per tag\n"
            + SP + "Dataset result = ds.flatMapColumn(\"tags\", \"tag\", \"id\",\n"
            + SP + "    tagList -> (List<String>) tagList);\n"
            + SP + "// Result: {1, \"java\"}, {1, \"sql\"}, {2, \"c++\"}, {2, \"go\"}\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset flatMapColumn(String fromColumnName, String newColumnName, Collection<String> copyingColumnNames, Function'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"id\", \"tags\"),\n"
            + SP + "    new Object[][] {{1, Arrays.asList(\"java\", \"sql\")}, {2, Arrays.asList(\"c++\", \"go\")}});\n"
            + SP + "Dataset result = ds.flatMapColumn(\"tags\", \"tag\",\n"
            + SP + "    N.asList(\"id\"),\n"
            + SP + "    tagList -> (List<String>) tagList);\n"
            + SP + "// Result keeps both \"id\" and \"tag\" columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset flatMapColumns(Tuple2<String, String> fromColumnNames'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"keys\", \"values\"),\n"
            + SP + "    new Object[][] {{\n"
            + SP + "        Arrays.asList(\"a\", \"b\"), Arrays.asList(1, 2)}});\n"
            + SP + "Dataset result = ds.flatMapColumns(\n"
            + SP + "    Tuple.of(\"keys\", \"values\"), \"pair\",\n"
            + SP + "    N.asList(\"keys\", \"values\"),\n"
            + SP + "    (keys, values) -> Tuple.of(keys, values));\n"
            + SP + "// Flat-maps two list columns, keeping both source columns\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset flatMapColumns(Tuple3<String, String, String> fromColumnNames'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\", \"c\"),\n"
            + SP + "    new Object[][] {{\n"
            + SP + "        Arrays.asList(1, 2), Arrays.asList(10, 20), Arrays.asList(100, 200)}});\n"
            + SP + "Dataset result = ds.flatMapColumns(\n"
            + SP + "    Tuple.of(\"a\", \"b\", \"c\"), \"sum\",\n"
            + SP + "    N.asList(\"a\"),\n"
            + SP + "    (a, b, c) -> (Integer)a + (Integer)b + (Integer)c);\n"
            + SP + "// Flat-maps three list columns, keeping column \"a\"\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset flatMapColumns(Collection<String> fromColumnNames'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"keys\", \"values\"),\n"
            + SP + "    new Object[][] {{\n"
            + SP + "        Arrays.asList(\"a\", \"b\"), Arrays.asList(1, 2)}});\n"
            + SP + "Dataset result = ds.flatMapColumns(\n"
            + SP + "    N.asList(\"keys\", \"values\"), \"pair\",\n"
            + SP + "    N.asList(\"keys\", \"values\"),\n"
            + SP + "    row -> Tuple.of(row.get(0), row.get(1)));\n"
            + SP + "// Flat-maps two list columns simultaneously\n"
            + SP + "}</pre>\n"
        )

    # --- innerJoin overloads (missing) ---
    elif sig.startswith('Dataset innerJoin(Dataset right, String columnName, String joinColumnNameOnRight)'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset left = Dataset.rows(Arrays.asList(\"id\", \"name\"),\n"
            + SP + "    new Object[][] {{1, \"Alice\"}, {2, \"Bob\"}});\n"
            + SP + "Dataset right = Dataset.rows(Arrays.asList(\"userId\", \"score\"),\n"
            + SP + "    new Object[][] {{1, 95}, {2, 80}});\n"
            + SP + "Dataset joined = left.innerJoin(right, \"id\", \"userId\");\n"
            + SP + "// Result: {1, \"Alice\", 1, 95}, {2, \"Bob\", 2, 80}\n"
            + SP + "\n"
            + SP + "left.innerJoin(right, \"bad\", \"userId\");\n"
            + SP + "// throws IllegalArgumentException\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('Dataset innerJoin(Dataset right, Map<String, String> onColumnNames)'):
        if ', String newColumnName' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset left = Dataset.rows(Arrays.asList(\"id\", \"dept\"),\n"
                + SP + "    new Object[][] {{1, \"IT\"}, {2, \"HR\"}});\n"
                + SP + "Dataset right = Dataset.rows(Arrays.asList(\"userId\", \"dept\"),\n"
                + SP + "    new Object[][] {{1, \"IT\"}, {2, \"HR\"}});\n"
                + SP + "Dataset joined = left.innerJoin(right,\n"
                + SP + "    Map.of(\"id\", \"userId\", \"dept\", \"dept\"),\n"
                + SP + "    \"joined_col\", Map.class);\n"
                + SP + "// Joins on multiple columns and wraps match in new column\n"
                + SP + "}</pre>\n"
            )
        else:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset left = Dataset.rows(Arrays.asList(\"id\", \"dept\"),\n"
                + SP + "    new Object[][] {{1, \"IT\"}, {2, \"HR\"}});\n"
                + SP + "Dataset right = Dataset.rows(Arrays.asList(\"userId\", \"dept\"),\n"
                + SP + "    new Object[][] {{1, \"IT\"}, {2, \"HR\"}});\n"
                + SP + "Dataset joined = left.innerJoin(right,\n"
                + SP + "    Map.of(\"id\", \"userId\", \"dept\", \"dept\"));\n"
                + SP + "// Joins on both id and dept columns\n"
                + SP + "}</pre>\n"
            )

    # --- stream overloads (missing) ---
    elif sig.startswith('<T> Stream<T> stream(Map<String, String> prefixAndFieldNameMap,'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {{100, \"Bob\", 1, \"iPhone\"}, {200, \"Alice\", 3, \"Android\"}});\n"
            + SP + "Stream<Account> stream = ds.stream(\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Maps \"d.*\" columns to Account.devices field\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Stream<T> stream(int fromRowIndex, int toRowIndex, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {{100, \"Bob\", 1, \"iPhone\"}, {200, \"Alice\", 3, \"Android\"}});\n"
            + SP + "Stream<Account> stream = ds.stream(0, 2,\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Streams first 2 rows as Account entities with column prefix mapping\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Stream<T> stream(Collection<String> columnNames, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {{100, \"Bob\", 1, \"iPhone\"}, {200, \"Alice\", 3, \"Android\"}});\n"
            + SP + "Stream<Account> stream = ds.stream(\n"
            + SP + "    N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Streams selected columns as Account entities with prefix mapping\n"
            + SP + "}</pre>\n"
        )
    elif sig.startswith('<T> Stream<T> stream(int fromRowIndex, int toRowIndex, Collection<String> columnNames, Map<String, String>'):
        examples = (
            SP + "<p><b>Usage Examples:</b></p>\n"
            + SP + "<pre>{@code\n"
            + SP + "Dataset ds = Dataset.rows(N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    new Object[][] {{100, \"Bob\", 1, \"iPhone\"}, {200, \"Alice\", 3, \"Android\"}});\n"
            + SP + "Stream<Account> stream = ds.stream(0, 2,\n"
            + SP + "    N.asList(\"id\", \"name\", \"d.id\", \"d.model\"),\n"
            + SP + "    Map.of(\"d\", \"devices\"), Account.class);\n"
            + SP + "// Streams first 2 rows, selected columns, with prefix mapping\n"
            + SP + "}</pre>\n"
        )

    # --- Generic fallback for remaining filter/map overloads (the ones with fromRowIndex, toRowIndex) ---
    if examples is None:
        # Try to match by more generic patterns
        # filter(int fromRowIndex, int toRowIndex, ... )
        if 'filter(int fromRowIndex, int toRowIndex, Predicate' in sig and 'max' not in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"age\", \"name\"),\n"
                + SP + "    new Object[][] {{25, \"Alice\"}, {30, \"Bob\"}, {35, \"Charlie\"}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3,\n"
                + SP + "    row -> (Integer) row.get(0) > 28);\n"
                + SP + "// Filters rows [0,3) where age > 28\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, Tuple2' in sig and 'max' not in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    new Object[][] {{1, 10}, {5, 3}, {3, 8}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3,\n"
                + SP + "    Tuple.of(\"a\", \"b\"), (a, b) -> (Integer) a > (Integer) b);\n"
                + SP + "// Filters rows [0,3) where a > b\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, Tuple2' in sig and 'max' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    new Object[][] {{1, 10}, {5, 3}, {3, 8}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3,\n"
                + SP + "    Tuple.of(\"a\", \"b\"), (a, b) -> (Integer) a > (Integer) b, 1);\n"
                + SP + "// At most 1 row from [0,3) where a > b\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, Tuple3' in sig and 'max' not in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"x\", \"y\", \"z\"),\n"
                + SP + "    new Object[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3,\n"
                + SP + "    Tuple.of(\"x\", \"y\", \"z\"), (x, y, z) -> (Integer)x + (Integer)y > (Integer)z);\n"
                + SP + "// Filters rows [0,3) where x + y > z\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, Tuple3' in sig and 'max' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"x\", \"y\", \"z\"),\n"
                + SP + "    new Object[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3,\n"
                + SP + "    Tuple.of(\"x\", \"y\", \"z\"), (x, y, z) -> (Integer)x + (Integer)y > (Integer)z, 1);\n"
                + SP + "// At most 1 row from [0,3) where x + y > z\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, String columnName, Predicate<?> filter)' in sig and 'max' not in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"age\"),\n"
                + SP + "    new Object[][] {{25}, {30}, {35}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3, \"age\", age -> (Integer) age > 28);\n"
                + SP + "// Filters rows [0,3) where age > 28\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, String columnName, Predicate<?> filter, int max)' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"age\"),\n"
                + SP + "    new Object[][] {{25}, {30}, {35}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3, \"age\", age -> (Integer) age > 28, 1);\n"
                + SP + "// At most 1 row from [0,3) where age > 28\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, Collection<String> columnNames, Predicate' in sig and 'max' not in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    new Object[][] {{1, 10}, {5, 3}, {3, 8}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3, Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    row -> (Integer) row.get(0) + (Integer) row.get(1) > 10);\n"
                + SP + "// Filters rows [0,3) where a + b > 10\n"
                + SP + "}</pre>\n"
            )
        elif 'filter(int fromRowIndex, int toRowIndex, Collection<String> columnNames, Predicate' in sig and 'max' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    new Object[][] {{1, 10}, {5, 3}, {3, 8}});\n"
                + SP + "Dataset filtered = ds.filter(0, 3, Arrays.asList(\"a\", \"b\"),\n"
                + SP + "    row -> (Integer) row.get(0) + (Integer) row.get(1) > 10, 1);\n"
                + SP + "// At most 1 row from [0,3) where a + b > 10\n"
                + SP + "}</pre>\n"
            )
        elif 'mapColumn' in sig and 'Collection<String> copyingColumnNames' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"name\", \"age\"),\n"
                + SP + "    new Object[][] {{\"Alice\", 25}, {\"Bob\", 30}});\n"
                + SP + "Dataset result = ds.mapColumn(\"name\", \"upperName\",\n"
                + SP + "    N.asList(\"name\", \"age\"),\n"
                + SP + "    n -> ((String) n).toUpperCase());\n"
                + SP + "// Adds \"upperName\" column while keeping existing columns\n"
                + SP + "}</pre>\n"
            )
        elif 'mapColumn' in sig and 'String copyingColumnName' in sig:
            examples = (
                SP + "<p><b>Usage Examples:</b></p>\n"
                + SP + "<pre>{@code\n"
                + SP + "Dataset ds = Dataset.rows(Arrays.asList(\"name\", \"age\"),\n"
                + SP + "    new Object[][] {{\"Alice\", 25}, {\"Bob\", 30}});\n"
                + SP + "Dataset result = ds.mapColumn(\"name\", \"upperName\", \"age\",\n"
                + SP + "    n -> ((String) n).toUpperCase());\n"
                + SP + "// Adds \"upperName\" column while copying \"age\"\n"
                + SP + "}</pre>\n"
            )

    return examples

# -- main --
def main():
    with open(FILE, 'r', encoding='utf-8') as f:
        content = f.read()

    matches = find_missing_matches(content)
    print(f"Found {len(matches)} methods missing Usage Examples")
    
    if not matches:
        print("No changes needed.")
        return

    # Apply changes in reverse order to preserve offsets
    matches.sort(key=lambda x: x[0], reverse=True)
    
    new_content = content
    for offset, examples, sig in matches:
        new_content = new_content[:offset] + examples + new_content[offset:]
        print(f"  Added examples for: {sig[:80]}...")

    with open(FILE, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print(f"\nDone. Updated {FILE}")

if __name__ == '__main__':
    main()
