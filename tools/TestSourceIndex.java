import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;

/** Parse Java source without resolving project dependencies. Used by the test audit tools. */
class TestSourceIndex {
    private static final Set<String> TEST_ANNOTATIONS = Set.of("Test", "ParameterizedTest", "RepeatedTest", "TestFactory", "TestTemplate");

    public static void main(String[] args) throws IOException {
        final var compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("A JDK (21 or later), not just a JRE, is required.");
        }
        final var diagnostics = new DiagnosticCollector<JavaFileObject>();
        try (final var manager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            final var task = (JavacTask) compiler.getTask(null, manager, diagnostics, List.of("-proc:none", "-encoding", "UTF-8"), null,
                    manager.getJavaFileObjectsFromStrings(List.of(args)));
            final var units = task.parse();
            final var positions = Trees.instance(task).getSourcePositions();
            final List<Object> result = new ArrayList<>();
            for (final var unit : units) {
                final var index = new Index(unit, positions);
                index.scan(unit, "");
                result.add(index.result());
            }
            final var errors = diagnostics.getDiagnostics().stream().filter(d -> d.getKind() == Diagnostic.Kind.ERROR).toList();
            if (!errors.isEmpty()) {
                errors.forEach(System.err::println);
                System.exit(1);
            }
            System.out.println(json(result));
        }
    }

    private static final class Index extends TreeScanner<Void, String> {
        private final CompilationUnitTree unit;
        private final SourcePositions positions;
        private final List<Object> classes = new ArrayList<>();
        private final List<Object> methods = new ArrayList<>();

        Index(CompilationUnitTree unit, SourcePositions positions) {
            this.unit = unit;
            this.positions = positions;
        }

        Map<String, Object> range(Tree tree) {
            final long start = positions.getStartPosition(unit, tree);
            final long end = positions.getEndPosition(unit, tree);
            return object("start", start, "end", end, "line", unit.getLineMap().getLineNumber(start));
        }

        @Override
        public Void visitClass(ClassTree tree, String parent) {
            final String name = tree.getSimpleName().toString();
            if (name.isEmpty()) {
                return null;
            }
            final String owner = parent.isEmpty() ? name : parent + "." + name;
            final var entry = range(tree);
            entry.putAll(object("name", name, "owner", owner, "parent", parent, "kind", tree.getKind().name(),
                    "annotations", annotations(tree.getModifiers().getAnnotations()), "modifiers", tree.getModifiers().getFlags().stream().map(Modifier::toString).toList(),
                    "extends", tree.getExtendsClause() == null ? "" : tree.getExtendsClause().toString()));
            final List<Object> support = new ArrayList<>();
            for (Tree member : tree.getMembers()) {
                if (!(member instanceof MethodTree) && !(member instanceof ClassTree)) {
                    final var item = range(member);
                    item.put("source", member.toString());
                    item.put("kind", member.getKind().name());
                    support.add(item);
                }
            }
            entry.put("support", support);
            classes.add(entry);
            return super.visitClass(tree, owner);
        }

        @Override
        public Void visitMethod(MethodTree tree, String owner) {
            final var entry = range(tree);
            final var annotations = annotations(tree.getModifiers().getAnnotations());
            final boolean test = tree.getModifiers().getAnnotations().stream()
                    .anyMatch(a -> TEST_ANNOTATIONS.contains(simpleName(a.getAnnotationType().toString())));
            final List<Object> calls = new ArrayList<>();
            new TreeScanner<Void, Void>() {
                @Override
                public Void visitMethodInvocation(MethodInvocationTree call, Void unused) {
                    final String select = call.getMethodSelect().toString();
                    calls.add(object("name", simpleName(select), "select", select, "arity", call.getArguments().size(),
                            "selectStart", positions.getStartPosition(unit, call.getMethodSelect()),
                            "selectEnd", positions.getEndPosition(unit, call.getMethodSelect())));
                    return super.visitMethodInvocation(call, unused);
                }

                @Override
                public Void visitMemberReference(MemberReferenceTree reference, Void unused) {
                    calls.add(object("name", reference.getName().toString(), "select", reference.toString(), "arity", -1));
                    return super.visitMemberReference(reference, unused);
                }
            }.scan(tree.getBody(), null);
            entry.putAll(object("name", tree.getName().toString(), "owner", owner, "test", test, "annotations", annotations,
                    "modifiers", tree.getModifiers().getFlags().stream().map(Modifier::toString).toList(),
                    "parameters", tree.getParameters().stream().map(p -> object("name", p.getName().toString(), "type", p.getType().toString(),
                            "varargs", p.toString().endsWith("... " + p.getName()))).toList(),
                    "returnType", tree.getReturnType() == null ? "" : tree.getReturnType().toString(),
                    "body", tree.getBody() == null ? "" : tree.getBody().toString(), "calls", calls));
            if (tree.getBody() != null) {
                entry.put("bodyStart", positions.getStartPosition(unit, tree.getBody()));
                entry.put("bodyEnd", positions.getEndPosition(unit, tree.getBody()));
            }
            methods.add(entry);
            // Local/anonymous classes belong to this method, not to the surrounding test container.
            return null;
        }

        Map<String, Object> result() {
            return object("file", Path.of(unit.getSourceFile().toUri()).toAbsolutePath().normalize().toString().replace('\\', '/'),
                    "packageName", unit.getPackageName() == null ? "" : unit.getPackageName().toString(),
                    "imports", unit.getImports().stream().map(Object::toString).map(String::trim).toList(),
                    "classes", classes, "methods", methods);
        }
    }

    private static List<String> annotations(List<? extends AnnotationTree> annotations) {
        return annotations.stream().map(Object::toString).toList();
    }

    private static String simpleName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    private static Map<String, Object> object(Object... entries) {
        final Map<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            result.put((String) entries[i], entries[i + 1]);
        }
        return result;
    }

    private static String json(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map<?, ?> map) {
            return "{" + String.join(",", map.entrySet().stream().map(e -> json(e.getKey().toString()) + ":" + json(e.getValue())).toList()) + "}";
        }
        if (value instanceof Iterable<?> values) {
            final List<String> items = new ArrayList<>();
            values.forEach(item -> items.add(json(item)));
            return "[" + String.join(",", items) + "]";
        }
        final StringBuilder escaped = new StringBuilder("\"");
        for (char ch : value.toString().toCharArray()) {
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (ch < 32) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        return escaped.append('"').toString();
    }
}
