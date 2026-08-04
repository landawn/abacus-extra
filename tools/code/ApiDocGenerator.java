
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.source.doctree.DeprecatedTree;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.ParamTree;
import com.sun.source.doctree.ReturnTree;
import com.sun.source.doctree.SeeTree;
import com.sun.source.doctree.ThrowsTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.DocTrees;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

public final class ApiDocGenerator {

    private ApiDocGenerator() {
    }

    private static final class LibraryInfo {
        String name = "unknown";
        String version = "unknown";
        String javaTarget = "unknown";
        String gitSha = "unknown";
        String generatedAt = LocalDate.now().toString();
    }

    private static final class DeprecatedInfo {
        String message;
        String since;
        boolean forRemoval;
    }

    private static final class TypeParamInfo {
        String name;
        List<String> bounds = new ArrayList<>();
    }

    private static final class ParamInfo {
        String name;
        String type;
        String javadoc;
        String nullability = "unspecified";
    }

    private static final class ThrowInfo {
        String type;
        String condition;
    }

    private static final class DocThrowInfo {
        String type;
        String condition;
    }

    private static final class ConstructorInfo {
        String signature;
        String javadocSummary;
        String since;
        DeprecatedInfo deprecated;
        List<ParamInfo> params = new ArrayList<>();
        List<ThrowInfo> throwsList = new ArrayList<>();
    }

    private static final class MethodInfo {
        String name;
        String kind;
        List<String> modifiers = new ArrayList<>();
        String signature;
        String returnType;
        List<TypeParamInfo> typeParams = new ArrayList<>();
        List<ParamInfo> params = new ArrayList<>();
        List<ThrowInfo> throwsList = new ArrayList<>();
        String since;
        DeprecatedInfo deprecated;
        String javadocSummary;
        String returns;
        List<String> contract = new ArrayList<>();
        String performance;
        List<String> examples = new ArrayList<>();
        List<String> seeAlso = new ArrayList<>();
        int order;
    }

    private static final class FieldInfo {
        String name;
        List<String> modifiers = new ArrayList<>();
        String type;
        String value;
        String javadocSummary;
    }

    private static final class TypeInfo {
        String fqn;
        String name;
        String kind;
        List<String> modifiers = new ArrayList<>();
        String since;
        DeprecatedInfo deprecated;
        String javadocSummary;
        String threadSafety = "unspecified";
        String nullability = "unspecified";
        List<TypeParamInfo> typeParams = new ArrayList<>();
        List<ConstructorInfo> constructors = new ArrayList<>();
        List<MethodInfo> methods = new ArrayList<>();
        List<FieldInfo> fields = new ArrayList<>();
        int order;
    }

    private static final class PackageInfoData {
        String name;
        String summary;
        List<TypeInfo> types = new ArrayList<>();
    }

    private static final class UnitData {
        CompilationUnitTree unit;
        Path path;
        String source;
        String packageName;
        Map<String, String> explicitImports = new LinkedHashMap<>();
        List<String> wildcardImports = new ArrayList<>();
    }

    private static final class DocInfo {
        String summary;
        String fullBody;
        String since;
        boolean deprecated;
        String deprecatedMessage;
        String returns;
        Map<String, String> paramDocs = new LinkedHashMap<>();
        List<DocThrowInfo> throwsList = new ArrayList<>();
        List<String> seeAlso = new ArrayList<>();
        List<String> contract = new ArrayList<>();
        String performance;
        List<String> examples = new ArrayList<>();
    }

    private static final Pattern PRE_BLOCK_PATTERN = Pattern.compile("(?is)<pre\\b[^>]*>(.*?)</pre>");
    private static int methodOrder;

    public static void main(final String[] args) throws Exception {
        final Path sourceRoot = args.length > 0 ? Path.of(args[0]) : Path.of("src/main/java");
        final Path markdownOut = args.length > 1 ? Path.of(args[1]) : Path.of("./docs/ai/API.md");
        final Path jsonOut = args.length > 2 ? Path.of(args[2]) : Path.of("./docs/ai/api-index.json");
        final Path pomPath = args.length > 3 ? Path.of(args[3]) : Path.of("pom.xml");

        final LibraryInfo library = readLibraryInfo(pomPath);
        final List<Path> javaFiles = listJavaFiles(sourceRoot);
        if (javaFiles.isEmpty()) {
            throw new IllegalStateException("No Java source files under " + sourceRoot);
        }

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("No system Java compiler");
        }

        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8);
        final Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjectsFromPaths(javaFiles);
        final JavacTask task = (JavacTask) compiler.getTask(new StringWriter(), fileManager, diagnostics, List.of("-proc:none", "-Xlint:none"), null, files);

        final List<CompilationUnitTree> parsedUnits = new ArrayList<>();
        for (final CompilationUnitTree unit : task.parse()) {
            parsedUnits.add(unit);
        }
        fileManager.close();

        final DocTrees docTrees = DocTrees.instance(task);
        final Trees trees = Trees.instance(task);
        final SourcePositions sourcePositions = trees.getSourcePositions();

        final Map<String, PackageInfoData> packageMap = new LinkedHashMap<>();
        final Map<String, Map<String, String>> typesByPackage = new LinkedHashMap<>();
        final Map<String, String> allTypes = new LinkedHashMap<>();
        final List<UnitData> units = new ArrayList<>();

        for (final CompilationUnitTree unit : parsedUnits) {
            final UnitData ud = new UnitData();
            ud.unit = unit;
            ud.path = Path.of(unit.getSourceFile().toUri());
            ud.source = readFile(ud.path);
            ud.packageName = unit.getPackageName() == null ? "" : unit.getPackageName().toString();
            packageMap.computeIfAbsent(ud.packageName, k -> {
                final PackageInfoData p = new PackageInfoData();
                p.name = k;
                return p;
            });

            for (final ImportTree it : unit.getImports()) {
                if (it.isStatic()) {
                    continue;
                }
                final String q = it.getQualifiedIdentifier().toString();
                if (q.endsWith(".*")) {
                    ud.wildcardImports.add(q.substring(0, q.length() - 2));
                } else {
                    final int idx = q.lastIndexOf('.');
                    if (idx > 0) {
                        ud.explicitImports.put(q.substring(idx + 1), q);
                    }
                }
            }

            if ("package-info.java".equals(ud.path.getFileName().toString()) && unit.getPackage() != null) {
                final DocInfo d = readDoc(docTrees, TreePath.getPath(unit, unit.getPackage()));
                if (d != null && !isBlank(d.summary)) {
                    packageMap.get(ud.packageName).summary = d.summary;
                }
            }

            for (final Tree t : unit.getTypeDecls()) {
                if (t instanceof ClassTree ct) {
                    final String simple = ct.getSimpleName().toString();
                    if (!simple.isEmpty()) {
                        final String fqn = ud.packageName.isEmpty() ? simple : ud.packageName + "." + simple;
                        typesByPackage.computeIfAbsent(ud.packageName, k -> new LinkedHashMap<>()).put(simple, fqn);
                        allTypes.put(fqn, fqn);
                    }
                }
            }

            units.add(ud);
        }

        int typeOrder = 0;
        methodOrder = 0;
        for (final UnitData ud : units) {
            for (final Tree t : ud.unit.getTypeDecls()) {
                if (t instanceof ClassTree ct) {
                    typeOrder = collectType(TreePath.getPath(ud.unit, ct), ud, null, null, packageMap, typesByPackage, allTypes, docTrees, sourcePositions,
                            typeOrder);
                }
            }
        }

        final List<PackageInfoData> packages = new ArrayList<>(packageMap.values());
        packages.sort(Comparator.comparing(p -> p.name));
        for (final PackageInfoData p : packages) {
            p.types.sort(Comparator.comparingInt(t -> t.order));
        }

        createParentDirectories(markdownOut);
        createParentDirectories(jsonOut);
        Files.writeString(markdownOut, toMarkdown(library, packages), StandardCharsets.UTF_8);
        Files.writeString(jsonOut, toJson(library, packages), StandardCharsets.UTF_8);

        final long errors = diagnostics.getDiagnostics().stream().filter(d -> d.getKind() == Diagnostic.Kind.ERROR).count();
        System.out.println("Generated " + markdownOut + " and " + jsonOut + " from " + javaFiles.size() + " files.");
        System.out.println("Diagnostics: " + diagnostics.getDiagnostics().size() + " (" + errors + " errors)");
    }

    private static int collectType(final TreePath typePath, final UnitData unitData, final TypeInfo parentType, final String enclosingFqn,
            final Map<String, PackageInfoData> packageMap, final Map<String, Map<String, String>> typesByPackage, final Map<String, String> allTypes,
            final DocTrees docTrees, final SourcePositions sourcePositions, final int startOrder) {
        int nextOrder = startOrder;
        final ClassTree classTree = (ClassTree) typePath.getLeaf();
        if (!isPublicType(classTree, parentType)) {
            return nextOrder;
        }

        final String simpleName = classTree.getSimpleName().toString();
        final String fqn;
        if (enclosingFqn == null) {
            fqn = unitData.packageName.isEmpty() ? simpleName : unitData.packageName + "." + simpleName;
        } else {
            fqn = enclosingFqn + "." + simpleName;
        }
        allTypes.putIfAbsent(fqn, fqn);

        final TypeInfo type = new TypeInfo();
        type.order = nextOrder++;
        type.fqn = fqn;
        type.name = simpleName;
        type.kind = mapTypeKind(classTree.getKind());
        type.modifiers = sortedModifiers(classTree.getModifiers(), parentType);
        if (classTree.getKind() == Tree.Kind.RECORD) {
            if (parentType != null && !type.modifiers.contains("static")) {
                type.modifiers.add("static");
            }
            if (!type.modifiers.contains("final")) {
                type.modifiers.add("final");
            }
        } else if (parentType != null && ("interface".equals(parentType.kind) || "annotation".equals(parentType.kind))
                && !type.modifiers.contains("static")) {
            type.modifiers.add("static");
        }
        type.typeParams = readTypeParams(classTree.getTypeParameters());
        type.nullability = inferNullability(classTree.getModifiers().getAnnotations(), "");

        final DocInfo typeDoc = readDoc(docTrees, typePath);
        if (typeDoc != null) {
            type.javadocSummary = typeDoc.summary;
            type.since = typeDoc.since;
            type.threadSafety = inferThreadSafety(typeDoc.fullBody);
        }
        type.deprecated = readDeprecated(classTree.getModifiers(), typeDoc);

        final List<VariableTree> recordComponents = readRecordComponents(classTree);
        boolean hasCanonicalRecordConstructor = false;
        for (final Tree member : classTree.getMembers()) {
            if (member instanceof VariableTree varTree) {
                if (!isPublicField(varTree, classTree)) {
                    continue;
                }
                final FieldInfo field = new FieldInfo();
                field.name = varTree.getName().toString();
                field.type = normalize(varTree.getType() == null ? "" : varTree.getType().toString());
                field.modifiers = sortedModifiers(varTree.getModifiers(), null);
                if (varTree.getInitializer() != null) {
                    field.value = normalize(varTree.getInitializer().toString());
                }
                final DocInfo fieldDoc = readDoc(docTrees, new TreePath(typePath, varTree));
                if (fieldDoc != null) {
                    field.javadocSummary = fieldDoc.summary;
                }
                type.fields.add(field);
            } else if (member instanceof MethodTree methodTree) {
                if (isConstructor(methodTree)) {
                    final boolean compactRecordConstructor = classTree.getKind() == Tree.Kind.RECORD
                            && isCompactRecordConstructor(unitData, sourcePositions, classTree, methodTree);
                    if (classTree.getKind() == Tree.Kind.RECORD
                            && (compactRecordConstructor || isCanonicalRecordConstructor(methodTree, recordComponents))) {
                        hasCanonicalRecordConstructor = true;
                    }
                    if (!isPublicConstructor(methodTree, classTree)) {
                        continue;
                    }
                    final DocInfo ctorDoc = readDoc(docTrees, new TreePath(typePath, methodTree));
                    final ConstructorInfo ctor = new ConstructorInfo();
                    ctor.signature = compactRecordConstructor ? buildRecordConstructorSignature(simpleName, recordComponents)
                            : readMethodSignature(unitData, sourcePositions, classTree, methodTree);
                    ctor.javadocSummary = ctorDoc == null ? null : ctorDoc.summary;
                    ctor.since = ctorDoc == null ? null : ctorDoc.since;
                    ctor.deprecated = readDeprecated(methodTree.getModifiers(), ctorDoc);
                    if (compactRecordConstructor) {
                        final DocInfo paramDoc = ctorDoc == null ? typeDoc : ctorDoc;
                        for (final VariableTree component : recordComponents) {
                            ctor.params.add(readParam(component, paramDoc));
                        }
                    } else {
                        ctor.params = readParams(methodTree, ctorDoc == null && classTree.getKind() == Tree.Kind.RECORD ? typeDoc : ctorDoc);
                    }
                    ctor.throwsList = readThrows(methodTree, ctorDoc, unitData, typesByPackage, allTypes, type);
                    type.constructors.add(ctor);
                } else {
                    if (!isPublicMethod(methodTree, classTree)) {
                        continue;
                    }
                    final DocInfo methodDoc = readDoc(docTrees, new TreePath(typePath, methodTree));
                    final MethodInfo method = new MethodInfo();
                    method.order = methodOrder++;
                    method.name = methodTree.getName().toString();
                    method.kind = methodTree.getModifiers().getFlags().contains(Modifier.STATIC) ? "static" : "instance";
                    method.modifiers = sortedModifiers(methodTree.getModifiers(), type);
                    method.signature = readMethodSignature(unitData, sourcePositions, classTree, methodTree);
                    method.returnType = normalize(methodTree.getReturnType() == null ? "void" : methodTree.getReturnType().toString());
                    method.typeParams = readTypeParams(methodTree.getTypeParameters());
                    method.params = readParams(methodTree, methodDoc);
                    method.throwsList = readThrows(methodTree, methodDoc, unitData, typesByPackage, allTypes, type);
                    if (methodDoc != null) {
                        method.javadocSummary = methodDoc.summary;
                        method.returns = methodDoc.returns;
                        method.since = methodDoc.since;
                        method.contract = methodDoc.contract;
                        method.performance = methodDoc.performance;
                        method.examples = methodDoc.examples;
                        method.seeAlso = methodDoc.seeAlso;
                    }
                    method.deprecated = readDeprecated(methodTree.getModifiers(), methodDoc);
                    type.methods.add(method);
                }
            } else if (member instanceof ClassTree nested) {
                nextOrder = collectType(new TreePath(typePath, nested), unitData, type, fqn, packageMap, typesByPackage, allTypes, docTrees, sourcePositions,
                        nextOrder);
            }
        }

        if (classTree.getKind() == Tree.Kind.RECORD && !hasCanonicalRecordConstructor) {
            final ConstructorInfo ctor = new ConstructorInfo();
            ctor.signature = buildRecordConstructorSignature(simpleName, recordComponents);
            ctor.since = typeDoc == null ? null : typeDoc.since;
            for (final VariableTree component : recordComponents) {
                ctor.params.add(readParam(component, typeDoc));
            }
            type.constructors.add(0, ctor);
        }

        packageMap.get(unitData.packageName).types.add(type);
        return nextOrder;
    }

    private static boolean isPublicType(final ClassTree classTree, final TypeInfo parentType) {
        final Set<Modifier> flags = classTree.getModifiers().getFlags();
        if (flags.contains(Modifier.PUBLIC)) {
            return true;
        }
        if (parentType == null) {
            return false;
        }
        if ("interface".equals(parentType.kind) || "annotation".equals(parentType.kind)) {
            return !flags.contains(Modifier.PRIVATE);
        }
        return false;
    }

    private static boolean isConstructor(final MethodTree methodTree) {
        return methodTree.getReturnType() == null;
    }

    private static List<VariableTree> readRecordComponents(final ClassTree classTree) {
        if (classTree.getKind() != Tree.Kind.RECORD) {
            return List.of();
        }
        return classTree.getMembers()
                .stream()
                .filter(VariableTree.class::isInstance)
                .map(VariableTree.class::cast)
                .filter(component -> !component.getModifiers().getFlags().contains(Modifier.STATIC))
                .collect(Collectors.toList());
    }

    private static boolean isCanonicalRecordConstructor(final MethodTree constructor, final List<VariableTree> components) {
        if (constructor.getParameters().size() != components.size()) {
            return false;
        }
        for (int i = 0; i < components.size(); i++) {
            final String constructorType = normalize(constructor.getParameters().get(i).getType().toString());
            final String componentType = normalize(components.get(i).getType().toString());
            if (!Objects.equals(constructorType, componentType)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCompactRecordConstructor(final UnitData unitData, final SourcePositions sourcePositions, final ClassTree owner,
            final MethodTree constructor) {
        final BlockTree body = constructor.getBody();
        if (body == null) {
            return false;
        }
        final long start = sourcePositions.getStartPosition(unitData.unit, constructor);
        final long bodyStart = sourcePositions.getStartPosition(unitData.unit, body);
        if (start < 0 || bodyStart <= start || bodyStart > unitData.source.length()) {
            return false;
        }
        final String header = unitData.source.substring((int) start, (int) bodyStart).trim();
        return header.matches("(?s).*\\b" + Pattern.quote(owner.getSimpleName().toString()) + "\\s*$");
    }

    private static String buildRecordConstructorSignature(final String recordName, final List<VariableTree> components) {
        return "public " + recordName + "("
                + components.stream().map(component -> normalize(component.getType() + " " + component.getName())).collect(Collectors.joining(", ")) + ")";
    }

    private static boolean isPublicConstructor(final MethodTree methodTree, final ClassTree owner) {
        final Set<Modifier> flags = methodTree.getModifiers().getFlags();
        if (flags.contains(Modifier.PUBLIC)) {
            return true;
        }
        return owner.getKind() == Tree.Kind.RECORD && !flags.contains(Modifier.PRIVATE) && !flags.contains(Modifier.PROTECTED);
    }

    private static boolean isPublicMethod(final MethodTree methodTree, final ClassTree owner) {
        final Set<Modifier> flags = methodTree.getModifiers().getFlags();
        if (flags.contains(Modifier.PUBLIC)) {
            return true;
        }
        if (flags.contains(Modifier.PRIVATE)) {
            return false;
        }
        return owner.getKind() == Tree.Kind.INTERFACE || owner.getKind() == Tree.Kind.ANNOTATION_TYPE;
    }

    private static boolean isPublicField(final VariableTree variableTree, final ClassTree owner) {
        final Set<Modifier> flags = variableTree.getModifiers().getFlags();
        if (flags.contains(Modifier.PUBLIC)) {
            return true;
        }
        if (flags.contains(Modifier.PRIVATE)) {
            return false;
        }
        return owner.getKind() == Tree.Kind.INTERFACE || owner.getKind() == Tree.Kind.ANNOTATION_TYPE;
    }

    private static List<String> sortedModifiers(final ModifiersTree modifiers, final TypeInfo ownerType) {
        final LinkedHashSet<String> out = new LinkedHashSet<>();
        final List<Modifier> order = List.of(Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.ABSTRACT, Modifier.STATIC, Modifier.FINAL,
                Modifier.SEALED, Modifier.NON_SEALED, Modifier.TRANSIENT, Modifier.VOLATILE, Modifier.SYNCHRONIZED, Modifier.NATIVE, Modifier.STRICTFP,
                Modifier.DEFAULT);
        if (ownerType != null && ("interface".equals(ownerType.kind) || "annotation".equals(ownerType.kind))) {
            out.add("public");
        }
        for (final Modifier m : order) {
            if (modifiers.getFlags().contains(m)) {
                out.add(m.toString().toLowerCase(Locale.ROOT));
            }
        }
        return new ArrayList<>(out);
    }

    private static List<TypeParamInfo> readTypeParams(final List<? extends TypeParameterTree> typeParameters) {
        final List<TypeParamInfo> out = new ArrayList<>();
        for (final TypeParameterTree tp : typeParameters) {
            final TypeParamInfo info = new TypeParamInfo();
            info.name = tp.getName().toString();
            if (tp.getBounds() == null || tp.getBounds().isEmpty()) {
                info.bounds.add("java.lang.Object");
            } else {
                for (final Tree bound : tp.getBounds()) {
                    info.bounds.add(normalize(bound.toString()));
                }
            }
            out.add(info);
        }
        return out;
    }

    private static String readMethodSignature(final UnitData unitData, final SourcePositions sourcePositions, final ClassTree owner,
            final MethodTree methodTree) {
        final long start = sourcePositions.getStartPosition(unitData.unit, methodTree);
        long end = sourcePositions.getEndPosition(unitData.unit, methodTree);
        if (start < 0 || start >= unitData.source.length() || end < 0) {
            return buildMethodSignature(owner, methodTree);
        }
        final BlockTree body = methodTree.getBody();
        if (body != null) {
            final long bodyStart = sourcePositions.getStartPosition(unitData.unit, body);
            if (bodyStart > start) {
                end = bodyStart;
            }
        }
        if (end < 0 || end > unitData.source.length()) {
            return buildMethodSignature(owner, methodTree);
        }
        String value = unitData.source.substring((int) start, (int) end).trim();
        if (value.endsWith("{")) {
            value = value.substring(0, value.length() - 1).trim();
        }
        if (value.endsWith(";")) {
            value = value.substring(0, value.length() - 1).trim();
        }
        return normalize(value);
    }

    private static String buildMethodSignature(final ClassTree owner, final MethodTree methodTree) {
        final StringBuilder sb = new StringBuilder();
        final List<String> modifiers = sortedModifiers(methodTree.getModifiers(), null);
        if (!modifiers.isEmpty()) {
            sb.append(String.join(" ", modifiers)).append(' ');
        }
        if (!methodTree.getTypeParameters().isEmpty()) {
            sb.append('<').append(methodTree.getTypeParameters().stream().map(Objects::toString).collect(Collectors.joining(", "))).append("> ");
        }
        if (isConstructor(methodTree)) {
            sb.append(owner.getSimpleName());
        } else {
            sb.append(methodTree.getReturnType()).append(' ').append(methodTree.getName());
        }
        sb.append('(').append(methodTree.getParameters().stream().map(Objects::toString).collect(Collectors.joining(", "))).append(')');
        if (!methodTree.getThrows().isEmpty()) {
            sb.append(" throws ").append(methodTree.getThrows().stream().map(Objects::toString).collect(Collectors.joining(", ")));
        }
        if (methodTree.getDefaultValue() != null) {
            sb.append(" default ").append(methodTree.getDefaultValue());
        }
        return normalize(sb.toString());
    }

    private static DocInfo readDoc(final DocTrees docTrees, final TreePath path) {
        if (path == null) {
            return null;
        }
        final DocCommentTree comment = docTrees.getDocCommentTree(path);
        if (comment == null) {
            return null;
        }

        final DocInfo doc = new DocInfo();
        doc.summary = normalizeDocText(comment.getFirstSentence());
        final String rawBody = rawDocText(comment.getFullBody());
        doc.fullBody = normalize(rawBody);
        doc.examples = extractExamples(rawBody);
        final String narrativeBody = stripPreformatted(rawBody);
        final String body = normalize(narrativeBody == null ? null : narrativeBody.replaceAll("<[^>]+>", " "));
        if (!isBlank(body)) {
            for (final String sentence : body.split("(?<=[.!?])\\s+")) {
                final String s = sentence.trim();
                if (s.isEmpty()) {
                    continue;
                }
                final String lower = s.toLowerCase(Locale.ROOT);
                if (lower.contains(" o(") || lower.startsWith("o(") || lower.contains("complexity")) {
                    doc.performance = s;
                } else if (lower.contains("must") || lower.contains("if ") || lower.contains("when ") || lower.contains("should")) {
                    doc.contract.add(s);
                }
            }
        }

        for (final DocTree tag : comment.getBlockTags()) {
            switch (tag.getKind()) {
                case SINCE -> doc.since = normalize(tag.toString().replaceFirst("^@since\\s*", ""));
                case PARAM -> {
                    final ParamTree p = (ParamTree) tag;
                    doc.paramDocs.put(p.getName().toString(), normalizeDocText(p.getDescription()));
                }
                case RETURN -> {
                    final ReturnTree r = (ReturnTree) tag;
                    doc.returns = normalizeDocText(r.getDescription());
                }
                case THROWS, EXCEPTION -> {
                    final ThrowsTree t = (ThrowsTree) tag;
                    final DocThrowInfo info = new DocThrowInfo();
                    info.type = normalize(t.getExceptionName().toString());
                    info.condition = normalizeDocText(t.getDescription());
                    doc.throwsList.add(info);
                }
                case SEE -> {
                    final SeeTree s = (SeeTree) tag;
                    final String ref = normalizeSeparatedDocText(s.getReference());
                    if (!isBlank(ref)) {
                        doc.seeAlso.add(ref);
                    }
                }
                case DEPRECATED -> {
                    final DeprecatedTree d = (DeprecatedTree) tag;
                    doc.deprecated = true;
                    doc.deprecatedMessage = normalizeDocText(d.getBody());
                }
                default -> {
                }
            }
        }
        return doc;
    }

    private static String normalizeDocText(final List<? extends DocTree> trees) {
        return normalize(rawDocText(trees));
    }

    private static String normalizeSeparatedDocText(final List<? extends DocTree> trees) {
        return trees == null || trees.isEmpty() ? null : normalize(trees.stream().map(Objects::toString).collect(Collectors.joining(" ")));
    }

    private static String rawDocText(final List<? extends DocTree> trees) {
        return trees == null || trees.isEmpty() ? null : trees.stream().map(Objects::toString).collect(Collectors.joining());
    }

    private static List<String> extractExamples(final String rawBody) {
        final List<String> examples = new ArrayList<>();
        if (isBlank(rawBody)) {
            return examples;
        }

        final Matcher matcher = PRE_BLOCK_PATTERN.matcher(rawBody);
        while (matcher.find()) {
            String example = matcher.group(1).trim();
            if (example.startsWith("{@code")) {
                example = example.substring("{@code".length()).stripLeading();
                if (example.endsWith("}")) {
                    example = example.substring(0, example.length() - 1);
                }
            } else {
                example = example.replaceFirst("(?is)^<code\\b[^>]*>", "").replaceFirst("(?is)</code>\\s*$", "");
            }
            example = stripCommonIndent(example);
            if (!isBlank(example)) {
                examples.add(example);
            }
        }
        return examples;
    }

    private static String stripPreformatted(final String text) {
        return text == null ? null : PRE_BLOCK_PATTERN.matcher(text).replaceAll(" ");
    }

    private static String stripCommonIndent(final String text) {
        final String[] lines = text.replace("\r\n", "\n").replace('\r', '\n').strip().split("\n", -1);
        int indent = Integer.MAX_VALUE;
        for (final String line : lines) {
            if (!line.isBlank()) {
                int current = 0;
                while (current < line.length() && Character.isWhitespace(line.charAt(current))) {
                    current++;
                }
                indent = Math.min(indent, current);
            }
        }
        if (indent == Integer.MAX_VALUE) {
            return null;
        }
        final int commonIndent = indent;
        return java.util.Arrays.stream(lines).map(line -> line.isBlank() ? "" : line.substring(Math.min(commonIndent, line.length())))
                .collect(Collectors.joining("\n"));
    }

    private static List<ParamInfo> readParams(final MethodTree methodTree, final DocInfo doc) {
        final List<ParamInfo> out = new ArrayList<>();
        for (final VariableTree param : methodTree.getParameters()) {
            out.add(readParam(param, doc));
        }
        return out;
    }

    private static ParamInfo readParam(final VariableTree param, final DocInfo doc) {
        final ParamInfo p = new ParamInfo();
        p.name = param.getName().toString();
        final String annotations = param.getModifiers().getAnnotations().stream().map(a -> normalize(a.toString())).collect(Collectors.joining(" "));
        final String type = normalize(param.getType() == null ? "" : param.getType().toString());
        p.type = annotations.isEmpty() ? type : annotations + " " + type;
        p.javadoc = doc == null ? null : doc.paramDocs.get(p.name);
        p.nullability = inferNullability(param.getModifiers().getAnnotations(), p.type);
        return p;
    }

    private static List<ThrowInfo> readThrows(final MethodTree methodTree, final DocInfo doc, final UnitData unitData,
            final Map<String, Map<String, String>> typesByPackage, final Map<String, String> allTypes, final TypeInfo ownerType) {
        final List<ThrowInfo> out = new ArrayList<>();
        final Set<String> ownerTypeParams = ownerType.typeParams.stream().map(tp -> tp.name).collect(Collectors.toSet());
        final Set<String> methodTypeParams = methodTree.getTypeParameters().stream().map(tp -> tp.getName().toString()).collect(Collectors.toSet());
        for (final ExpressionTree thrownType : methodTree.getThrows()) {
            final String declared = normalize(thrownType.toString());
            final ThrowInfo t = new ThrowInfo();
            t.type = resolveExceptionType(declared, unitData, typesByPackage, allTypes, ownerTypeParams, methodTypeParams);
            if (doc != null) {
                for (final DocThrowInfo documented : doc.throwsList) {
                    if (sameExceptionType(declared, documented.type)) {
                        t.condition = documented.condition;
                        break;
                    }
                }
            }
            out.add(t);
        }

        if (doc != null) {
            for (final DocThrowInfo documented : doc.throwsList) {
                boolean alreadyPresent = false;
                for (final ThrowInfo existing : out) {
                    if (sameExceptionType(existing.type, documented.type)) {
                        if (isBlank(existing.condition)) {
                            existing.condition = documented.condition;
                        }
                        alreadyPresent = true;
                        break;
                    }
                }
                if (!alreadyPresent) {
                    final ThrowInfo t = new ThrowInfo();
                    t.type = resolveExceptionType(documented.type, unitData, typesByPackage, allTypes, ownerTypeParams, methodTypeParams);
                    t.condition = documented.condition;
                    out.add(t);
                }
            }
        }
        return out;
    }

    private static boolean sameExceptionType(final String left, final String right) {
        return Objects.equals(left, right) || Objects.equals(simpleName(left), simpleName(right));
    }

    private static String resolveExceptionType(final String declared, final UnitData unitData, final Map<String, Map<String, String>> typesByPackage,
            final Map<String, String> allTypes, final Set<String> ownerTypeParams, final Set<String> methodTypeParams) {
        if (isBlank(declared) || declared.contains(".") || ownerTypeParams.contains(declared) || methodTypeParams.contains(declared)) {
            return declared;
        }
        final String explicit = unitData.explicitImports.get(declared);
        if (explicit != null) {
            return explicit;
        }
        final Map<String, String> inPackage = typesByPackage.get(unitData.packageName);
        if (inPackage != null && inPackage.containsKey(declared)) {
            return inPackage.get(declared);
        }
        for (final String wildcardImport : unitData.wildcardImports) {
            final String candidate = wildcardImport + "." + declared;
            if (allTypes.containsKey(candidate) || "java.lang".equals(wildcardImport)) {
                return candidate;
            }
        }
        if (declared.endsWith("Exception") || declared.endsWith("Error") || "Throwable".equals(declared)) {
            return "java.lang." + declared;
        }
        if (!unitData.packageName.isEmpty()) {
            final String candidate = unitData.packageName + "." + declared;
            if (allTypes.containsKey(candidate)) {
                return candidate;
            }
        }
        return declared;
    }

    private static DeprecatedInfo readDeprecated(final ModifiersTree modifiers, final DocInfo doc) {
        AnnotationTree deprecatedAnn = null;
        for (final AnnotationTree ann : modifiers.getAnnotations()) {
            final String annName = ann.getAnnotationType().toString();
            if ("Deprecated".equals(annName) || "java.lang.Deprecated".equals(annName)) {
                deprecatedAnn = ann;
                break;
            }
        }

        final boolean deprecatedTag = doc != null && doc.deprecated;
        if (deprecatedAnn == null && !deprecatedTag) {
            return null;
        }

        final DeprecatedInfo d = new DeprecatedInfo();
        if (deprecatedTag) {
            d.message = doc.deprecatedMessage;
        }
        if (deprecatedAnn != null) {
            for (final ExpressionTree arg : deprecatedAnn.getArguments()) {
                if (arg instanceof AssignmentTree assignmentTree) {
                    final String key = assignmentTree.getVariable().toString();
                    final String value = stripQuotes(normalize(assignmentTree.getExpression().toString()));
                    if ("since".equals(key)) {
                        d.since = value;
                    } else if ("forRemoval".equals(key)) {
                        d.forRemoval = Boolean.parseBoolean(value);
                    }
                }
            }
        }
        return d;
    }

    private static String inferNullability(final List<? extends AnnotationTree> annotations, final String typeText) {
        final String anns = annotations.stream().map(a -> a.getAnnotationType().toString().toLowerCase(Locale.ROOT)).collect(Collectors.joining(" "));
        final String txt = (anns + " " + String.valueOf(typeText).toLowerCase(Locale.ROOT)).trim();
        if (txt.contains("nullable") || txt.contains("checkfornull")) {
            return "nullable";
        }
        if (txt.contains("nonnull") || txt.contains("notnull") || txt.contains("non_null")) {
            return "non-null";
        }
        return "unspecified";
    }

    private static String inferThreadSafety(final String javadoc) {
        if (isBlank(javadoc)) {
            return "unspecified";
        }
        final String text = javadoc.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
        if (text.contains("not thread-safe") || text.contains("not thread safe")) {
            return "not-thread-safe";
        }
        if (text.contains("thread-safe only") || text.contains("thread safe only") || text.contains("thread-safety depends")
                || text.contains("thread safety depends") || text.contains("conditionally thread-safe") || text.contains("conditionally thread safe")) {
            return "conditional";
        }
        if (text.contains("thread-safe") || text.contains("thread safe")) {
            return "thread-safe";
        }
        return "unspecified";
    }

    private static LibraryInfo readLibraryInfo(final Path pomPath) {
        final LibraryInfo out = new LibraryInfo();
        if (!Files.exists(pomPath)) {
            return out;
        }
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            final Document doc = factory.newDocumentBuilder().parse(Files.newInputStream(pomPath));
            doc.getDocumentElement().normalize();

            final String name = readProjectElement(doc, "name");
            final String artifactId = readProjectElement(doc, "artifactId");
            final String version = readProjectElement(doc, "version");
            final String javaTarget = readProjectElement(doc, "maven.compiler.target");
            final String javaRelease = readProjectElement(doc, "maven.compiler.release");
            if (!isBlank(name)) {
                out.name = name.trim();
            } else if (!isBlank(artifactId)) {
                out.name = artifactId.trim();
            }
            if (!isBlank(version)) {
                out.version = version.trim();
            }
            if (!isBlank(javaTarget)) {
                out.javaTarget = javaTarget.trim();
            } else if (!isBlank(javaRelease)) {
                out.javaTarget = javaRelease.trim();
            }
        } catch (final Exception ignored) {
        }
        return out;
    }

    private static String readProjectElement(final Document doc, final String key) {
        final NodeList children = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (key.equals(child.getNodeName())) {
                return child.getTextContent();
            }
            if ("properties".equals(child.getNodeName())) {
                final NodeList props = child.getChildNodes();
                for (int j = 0; j < props.getLength(); j++) {
                    final Node prop = props.item(j);
                    if (prop.getNodeType() == Node.ELEMENT_NODE && key.equals(prop.getNodeName())) {
                        return prop.getTextContent();
                    }
                }
            }
        }
        return null;
    }

    private static List<Path> listJavaFiles(final Path root) throws IOException {
        final List<Path> files = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        files.sort(Comparator.comparing(Path::toString));
        return files;
    }

    private static void createParentDirectories(final Path file) throws IOException {
        final Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private static String toMarkdown(final LibraryInfo library, final List<PackageInfoData> packages) {
        final StringBuilder sb = new StringBuilder(1 << 20);
        sb.append("# ").append(md(library.name)).append(" API Index (v").append(md(library.version)).append(")\n");
        sb.append("- Build: ").append(md(library.gitSha)).append('\n');
        sb.append("- Java: ").append(md(library.javaTarget)).append('\n');
        sb.append("- Generated: ").append(md(library.generatedAt)).append("\n\n");
        sb.append("## Packages\n");
        for (final PackageInfoData p : packages) {
            if (isBlank(p.summary)) {
                sb.append("- ").append(md(p.name)).append('\n');
            } else {
                sb.append("- ").append(md(p.name)).append(" — ").append(md(p.summary)).append('\n');
            }
        }
        sb.append('\n');

        for (final PackageInfoData p : packages) {
            sb.append("## ").append(md(p.name)).append('\n');
            for (final TypeInfo t : p.types) {
                sb.append("### ").append(capitalize(md(t.kind))).append(' ').append(md(t.name)).append(" (").append(md(t.fqn)).append(")\n");
                sb.append(isBlank(t.javadocSummary) ? "unspecified" : md(t.javadocSummary)).append("\n\n");
                if (!isBlank(t.since)) {
                    sb.append("**Since:** ").append(md(t.since)).append('\n');
                }
                sb.append("**Thread-safety:** ").append(md(t.threadSafety)).append('\n');
                sb.append("**Nullability:** ").append(md(t.nullability)).append("\n\n");

                sb.append("#### Public Constructors\n");
                if (t.constructors.isEmpty()) {
                    sb.append("- (none)\n\n");
                } else {
                    for (final ConstructorInfo c : t.constructors) {
                        sb.append("- `").append(c.signature).append('`');
                        if (!isBlank(c.javadocSummary)) {
                            sb.append(" — ").append(md(c.javadocSummary));
                        }
                        sb.append('\n');
                    }
                    sb.append('\n');
                }

                sb.append("#### Public Static Methods\n");
                appendMethodGroupsMarkdown(sb, t.methods, "static");
                sb.append('\n');

                sb.append("#### Public Instance Methods\n");
                appendMethodGroupsMarkdown(sb, t.methods, "instance");
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    private static void appendMethodGroupsMarkdown(final StringBuilder sb, final List<MethodInfo> methods, final String kind) {
        final Map<String, List<MethodInfo>> groups = new LinkedHashMap<>();
        methods.stream()
                .filter(m -> kind.equals(m.kind))
                .sorted(Comparator.comparingInt(m -> m.order))
                .forEach(m -> groups.computeIfAbsent(m.name, k -> new ArrayList<>()).add(m));
        if (groups.isEmpty()) {
            sb.append("- (none)\n");
            return;
        }
        for (final Map.Entry<String, List<MethodInfo>> e : groups.entrySet()) {
            final MethodInfo first = e.getValue().get(0);
            sb.append("##### ").append(md(e.getKey())).append("(...) -> ").append(md(first.returnType)).append('\n');
            for (final MethodInfo m : e.getValue()) {
                sb.append("- **Signature:** `").append(m.signature).append("`\n");
                if (!isBlank(m.javadocSummary)) {
                    sb.append("- **Summary:** ").append(md(m.javadocSummary)).append('\n');
                }
                if (!m.contract.isEmpty()) {
                    sb.append("- **Contract:**\n");
                    for (final String c : m.contract) {
                        sb.append("  - ").append(md(c)).append('\n');
                    }
                }
                sb.append("- **Parameters:**\n");
                if (m.params.isEmpty()) {
                    sb.append("  - (none)\n");
                } else {
                    for (final ParamInfo p : m.params) {
                        sb.append("  - `").append(p.name).append("` (`").append(p.type).append("`)");
                        if (!isBlank(p.javadoc)) {
                            sb.append(" — ").append(md(p.javadoc));
                        }
                        sb.append('\n');
                    }
                }
                if (!"void".equals(m.returnType)) {
                    sb.append("- **Returns:** ").append(isBlank(m.returns) ? "unspecified" : md(m.returns)).append('\n');
                }
                if (!m.throwsList.isEmpty()) {
                    sb.append("- **Throws:**\n");
                    for (final ThrowInfo t : m.throwsList) {
                        sb.append("  - `").append(t.type).append('`');
                        if (!isBlank(t.condition)) {
                            sb.append(" — ").append(md(t.condition));
                        }
                        sb.append('\n');
                    }
                }
                if (!isBlank(m.performance)) {
                    sb.append("- **Performance:** ").append(md(m.performance)).append('\n');
                }
                if (!m.examples.isEmpty()) {
                    sb.append("- **Examples:**\n");
                    for (final String example : m.examples) {
                        sb.append("  ```java\n");
                        for (final String line : example.split("\\n", -1)) {
                            sb.append("  ").append(line).append('\n');
                        }
                        sb.append("  ```\n");
                    }
                }
                if (!m.seeAlso.isEmpty()) {
                    sb.append("- **See also:** ").append(md(String.join(", ", m.seeAlso))).append('\n');
                }
            }
        }
    }

    private static String toJson(final LibraryInfo library, final List<PackageInfoData> packages) {
        final Map<String, Object> root = new LinkedHashMap<>();
        final Map<String, Object> lib = new LinkedHashMap<>();
        lib.put("name", library.name);
        lib.put("version", library.version);
        lib.put("generated_at", library.generatedAt);
        lib.put("java_target", library.javaTarget);
        final Map<String, Object> source = new LinkedHashMap<>();
        source.put("git_sha", library.gitSha);
        lib.put("source", source);
        root.put("library", lib);

        final List<Object> packageArr = new ArrayList<>();
        for (final PackageInfoData p : packages) {
            final Map<String, Object> pkg = new LinkedHashMap<>();
            pkg.put("name", p.name);
            if (!isBlank(p.summary)) {
                pkg.put("summary", p.summary);
            }
            final List<Object> typeArr = new ArrayList<>();
            for (final TypeInfo t : p.types) {
                final Map<String, Object> type = new LinkedHashMap<>();
                type.put("fqn", t.fqn);
                type.put("name", t.name);
                type.put("kind", t.kind);
                type.put("modifiers", t.modifiers);
                if (!isBlank(t.since)) {
                    type.put("since", t.since);
                }
                if (t.deprecated != null) {
                    type.put("deprecated", deprecatedJson(t.deprecated));
                }
                if (!isBlank(t.javadocSummary)) {
                    type.put("javadoc_summary", t.javadocSummary);
                }
                type.put("thread_safety", t.threadSafety);
                type.put("nullability", t.nullability);
                type.put("type_params", t.typeParams.stream().map(ApiDocGenerator::typeParamJson).collect(Collectors.toList()));
                type.put("constructors", t.constructors.stream().map(ApiDocGenerator::constructorJson).collect(Collectors.toList()));
                type.put("methods", t.methods.stream().map(ApiDocGenerator::methodJson).collect(Collectors.toList()));
                type.put("fields", t.fields.stream().map(ApiDocGenerator::fieldJson).collect(Collectors.toList()));
                typeArr.add(type);
            }
            pkg.put("types", typeArr);
            packageArr.add(pkg);
        }
        root.put("packages", packageArr);
        return JsonWriter.write(root);
    }

    private static Map<String, Object> typeParamJson(final TypeParamInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", value.name);
        out.put("bounds", value.bounds);
        return out;
    }

    private static Map<String, Object> constructorJson(final ConstructorInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("signature", value.signature);
        if (!isBlank(value.javadocSummary)) {
            out.put("javadoc_summary", value.javadocSummary);
        }
        if (!isBlank(value.since)) {
            out.put("since", value.since);
        }
        if (value.deprecated != null) {
            out.put("deprecated", deprecatedJson(value.deprecated));
        }
        out.put("params", value.params.stream().map(ApiDocGenerator::paramJson).collect(Collectors.toList()));
        return out;
    }

    private static Map<String, Object> methodJson(final MethodInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", value.name);
        out.put("kind", value.kind);
        out.put("modifiers", value.modifiers);
        out.put("signature", value.signature);
        out.put("return_type", value.returnType);
        out.put("type_params", value.typeParams.stream().map(ApiDocGenerator::typeParamJson).collect(Collectors.toList()));
        out.put("params", value.params.stream().map(ApiDocGenerator::paramJson).collect(Collectors.toList()));
        out.put("throws", value.throwsList.stream().map(ApiDocGenerator::throwJson).collect(Collectors.toList()));
        if (!isBlank(value.since)) {
            out.put("since", value.since);
        }
        if (value.deprecated != null) {
            out.put("deprecated", deprecatedJson(value.deprecated));
        }
        if (!isBlank(value.javadocSummary)) {
            out.put("javadoc_summary", value.javadocSummary);
        }
        out.put("contract", value.contract == null ? List.of() : value.contract);
        if (!isBlank(value.performance)) {
            out.put("performance", value.performance);
        }
        out.put("examples", value.examples == null ? List.of() : value.examples);
        out.put("see_also", value.seeAlso == null ? List.of() : value.seeAlso);
        return out;
    }

    private static Map<String, Object> paramJson(final ParamInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", value.name);
        out.put("type", value.type);
        if (!isBlank(value.javadoc)) {
            out.put("javadoc", value.javadoc);
        }
        out.put("nullability", value.nullability);
        return out;
    }

    private static Map<String, Object> throwJson(final ThrowInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("type", value.type);
        if (!isBlank(value.condition)) {
            out.put("condition", value.condition);
        }
        return out;
    }

    private static Map<String, Object> fieldJson(final FieldInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", value.name);
        out.put("modifiers", value.modifiers);
        out.put("type", value.type);
        if (!isBlank(value.value)) {
            out.put("value", value.value);
        }
        if (!isBlank(value.javadocSummary)) {
            out.put("javadoc_summary", value.javadocSummary);
        }
        return out;
    }

    private static Map<String, Object> deprecatedJson(final DeprecatedInfo value) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("is_deprecated", true);
        if (!isBlank(value.message)) {
            out.put("message", value.message);
        }
        if (!isBlank(value.since)) {
            out.put("since", value.since);
        }
        out.put("for_removal", value.forRemoval);
        return out;
    }

    private static String mapTypeKind(final Tree.Kind kind) {
        return switch (kind) {
            case CLASS -> "class";
            case INTERFACE -> "interface";
            case ENUM -> "enum";
            case RECORD -> "record";
            case ANNOTATION_TYPE -> "annotation";
            default -> "class";
        };
    }

    private static String capitalize(final String value) {
        if (isBlank(value)) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private static String simpleName(final String value) {
        if (value == null) {
            return null;
        }
        final int idx = value.lastIndexOf('.');
        return idx < 0 ? value : value.substring(idx + 1);
    }

    private static String stripQuotes(final String value) {
        if (value == null) {
            return null;
        }
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String readFile(final Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            return "";
        }
    }

    private static String normalize(final String value) {
        if (value == null) {
            return null;
        }
        final String v = value.replaceAll("\\s+", " ").trim();
        return v.isEmpty() ? null : v;
    }

    private static boolean isBlank(final String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String md(final String text) {
        if (text == null) {
            return "";
        }
        final String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        final StringBuilder out = new StringBuilder(normalized.length() + 16);
        for (int i = 0; i < normalized.length(); i++) {
            final char c = normalized.charAt(i);
            if (c == '[' || c == ']' || c == '\\') {
                out.append('\\');
            }
            out.append(c);
        }
        return out.toString();
    }

    private static final class JsonWriter {
        private JsonWriter() {
        }

        static String write(final Object value) {
            final StringBuilder sb = new StringBuilder(1 << 20);
            writeValue(value, sb, 0);
            sb.append('\n');
            return sb.toString();
        }

        @SuppressWarnings("unchecked")
        private static void writeValue(final Object value, final StringBuilder sb, final int indent) {
            if (value == null) {
                sb.append("null");
            } else if (value instanceof String s) {
                sb.append('"').append(escape(s)).append('"');
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value instanceof Map<?, ?> map) {
                final List<Map.Entry<String, Object>> entries = ((Map<String, Object>) map).entrySet().stream().toList();
                sb.append("{\n");
                for (int i = 0; i < entries.size(); i++) {
                    final Map.Entry<String, Object> entry = entries.get(i);
                    indent(sb, indent + 2);
                    sb.append('"').append(escape(entry.getKey())).append("\": ");
                    writeValue(entry.getValue(), sb, indent + 2);
                    if (i < entries.size() - 1) {
                        sb.append(',');
                    }
                    sb.append('\n');
                }
                indent(sb, indent);
                sb.append('}');
            } else if (value instanceof List<?> list) {
                sb.append("[");
                if (!list.isEmpty()) {
                    sb.append('\n');
                    for (int i = 0; i < list.size(); i++) {
                        indent(sb, indent + 2);
                        writeValue(list.get(i), sb, indent + 2);
                        if (i < list.size() - 1) {
                            sb.append(',');
                        }
                        sb.append('\n');
                    }
                    indent(sb, indent);
                }
                sb.append(']');
            } else {
                sb.append('"').append(escape(value.toString())).append('"');
            }
        }

        private static void indent(final StringBuilder sb, final int spaces) {
            sb.append(" ".repeat(Math.max(0, spaces)));
        }

        private static String escape(final String s) {
            final StringBuilder out = new StringBuilder(s.length() + 16);
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt(i);
                switch (c) {
                    case '\\' -> out.append("\\\\");
                    case '"' -> out.append("\\\"");
                    case '\n' -> out.append("\\n");
                    case '\r' -> out.append("\\r");
                    case '\t' -> out.append("\\t");
                    case '\b' -> out.append("\\b");
                    case '\f' -> out.append("\\f");
                    default -> {
                        if (c < 0x20) {
                            out.append(String.format("\\u%04x", (int) c));
                        } else {
                            out.append(c);
                        }
                    }
                }
            }
            return out.toString();
        }
    }
}
