#!/usr/bin/env node

const fs = require("fs");
const path = require("path");
const { execFileSync } = require("child_process");

const repoRoot = process.cwd();
const sourceRoot = path.join(repoRoot, "src", "main", "java", "com", "landawn", "abacus", "util");
const testRoot = path.join(repoRoot, "src", "test", "java", "com", "landawn", "abacus", "util");
const packageName = "com.landawn.abacus.util";
const canonicalImports = new Set(["import org.junit.jupiter.api.Nested;", "import com.landawn.abacus.TestBase;"]);
const testAnnotations = new Set(["Test", "ParameterizedTest", "RepeatedTest", "TestFactory", "TestTemplate"]);
const headTrackedPaths = new Set(gitPaths());

function gitPaths() {
  try {
    const output = execFileSync("git", ["ls-tree", "-r", "--name-only", "HEAD", "--", "src/test/java/com/landawn/abacus/util"], {
      cwd: repoRoot,
      encoding: "utf8",
      stdio: ["ignore", "pipe", "ignore"],
    });
    return output
      .split(/\r?\n/)
      .map((line) => line.trim())
      .filter(Boolean)
      .filter((line) => line.endsWith(".java"));
  } catch {
    return [];
  }
}

function walk(dir, files = []) {
  if (!fs.existsSync(dir)) {
    return files;
  }

  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      walk(fullPath, files);
    } else if (entry.name.endsWith(".java")) {
      files.push(path.relative(repoRoot, fullPath).replace(/\\/g, "/"));
    }
  }

  return files;
}

function readTrackedFile(relativePath) {
  const absolutePath = path.join(repoRoot, relativePath);
  if (!headTrackedPaths.has(relativePath)) {
    return fs.existsSync(absolutePath) ? fs.readFileSync(absolutePath, "utf8") : "";
  }

  return execFileSync("git", ["show", `HEAD:${relativePath}`], {
    cwd: repoRoot,
    encoding: "utf8",
    stdio: ["ignore", "pipe", "ignore"],
  });
}

function listSourceClasses() {
  return fs
    .readdirSync(sourceRoot)
    .filter((name) => name.endsWith(".java"))
    .map((name) => name.slice(0, -5))
    .sort((a, b) => b.length - a.length);
}

function findMatchingBrace(text, startIndex) {
  let depth = 0;
  let inString = false;
  let inChar = false;
  let inLineComment = false;
  let inBlockComment = false;
  let escaped = false;

  for (let i = startIndex; i < text.length; i++) {
    const ch = text[i];
    const next = text[i + 1];

    if (inLineComment) {
      if (ch === "\n") {
        inLineComment = false;
      }
      continue;
    }

    if (inBlockComment) {
      if (ch === "*" && next === "/") {
        inBlockComment = false;
        i++;
      }
      continue;
    }

    if (escaped) {
      escaped = false;
      continue;
    }

    if (ch === "\\") {
      escaped = true;
      continue;
    }

    if (!inChar && ch === "\"") {
      inString = !inString;
      continue;
    }

    if (!inString && ch === "'") {
      inChar = !inChar;
      continue;
    }

    if (inString || inChar) {
      continue;
    }

    if (ch === "/" && next === "/") {
      inLineComment = true;
      i++;
      continue;
    }

    if (ch === "/" && next === "*") {
      inBlockComment = true;
      i++;
      continue;
    }

    if (ch === "{") {
      depth++;
    } else if (ch === "}") {
      depth--;
      if (depth === 0) {
        return i;
      }
    }
  }

  return -1;
}

function stripComments(text) {
  return text
    .replace(/\/\*[\s\S]*?\*\//g, "")
    .replace(/\/\/[^\n\r]*/g, "");
}

function normalizeWhitespace(text) {
  return stripComments(text).replace(/\s+/g, " ").trim();
}

function parseJavaFile(relativePath, raw) {
  const packageMatch = raw.match(/^\s*package\s+[^;]+;/m);
  const importMatches = raw.match(/^\s*import\s+[^;]+;/gm) || [];
  const packageEnd = packageMatch ? raw.indexOf(packageMatch[0]) + packageMatch[0].length : 0;
  const lastImport = importMatches.length === 0 ? packageEnd : raw.lastIndexOf(importMatches[importMatches.length - 1]) + importMatches[importMatches.length - 1].length;
  const preludeEnd = Math.max(packageEnd, lastImport);
  const classMatch = /^\s*(?:public\s+|protected\s+|private\s+)?(?:abstract\s+|final\s+)?class\s+([A-Za-z_][A-Za-z0-9_]*)/m.exec(raw.slice(preludeEnd));
  const classIndex = classMatch ? preludeEnd + classMatch.index + classMatch[0].indexOf("class") : -1;

  if (classIndex === -1) {
    throw new Error(`No class declaration found in ${relativePath}`);
  }

  const bodyStart = raw.indexOf("{", classIndex);
  const bodyEnd = findMatchingBrace(raw, bodyStart);
  const classChunk = raw.slice(preludeEnd, bodyEnd + 1).trim();
  const classDecl = raw.slice(classIndex, bodyStart);
  const classNameMatch = classDecl.match(/\bclass\s+([A-Za-z_][A-Za-z0-9_]*)/);
  const className = classNameMatch ? classNameMatch[1] : null;
  const header = raw.slice(0, classIndex);
  const banner = packageMatch ? raw.slice(0, raw.indexOf(packageMatch[0])) : "";
  const body = raw.slice(bodyStart + 1, bodyEnd);
  const classAnnotations = raw.slice(preludeEnd, classIndex).trim();

  return {
    relativePath,
    raw,
    packageDecl: packageMatch ? packageMatch[0].trim() : `package ${packageName};`,
    imports: importMatches.map((line) => line.trim()),
    banner,
    classChunk,
    classDecl,
    className,
    body,
    bodyStart,
    bodyEnd,
    classAnnotations,
    header,
  };
}

function removePublicFromClassChunk(chunk) {
  return chunk.replace(/\bpublic\s+class\b/, "class");
}

function extractTestMethods(file) {
  const text = file.raw;
  const methods = [];
  let index = file.bodyStart + 1;

  while (index < file.bodyEnd) {
    const atIndex = text.indexOf("@", index);
    if (atIndex === -1 || atIndex >= file.bodyEnd) {
      break;
    }

    const annotationMatch = /^@([A-Za-z_][A-Za-z0-9_]*)/.exec(text.slice(atIndex));
    if (!annotationMatch || !testAnnotations.has(annotationMatch[1])) {
      index = atIndex + 1;
      continue;
    }

    let blockStart = atIndex;
    let lineStart = text.lastIndexOf("\n", blockStart - 1);
    if (lineStart === -1) {
      lineStart = 0;
    }

    while (lineStart > file.bodyStart) {
      const prevLineStart = text.lastIndexOf("\n", lineStart - 2);
      const start = prevLineStart === -1 ? 0 : prevLineStart + 1;
      const line = text.slice(start, lineStart).trim();
      if (line.startsWith("//") || line.startsWith("/*") || line.startsWith("*") || line === "") {
        lineStart = start;
      } else {
        break;
      }
    }

    blockStart = lineStart;

    const methodMatch = /(?:public|protected|private)?\s*(?:static\s+)?(?:final\s+)?(?:synchronized\s+)?(?:<[^>]+>\s+)?[\w.$<>\[\], ?]+\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(/.exec(
      text.slice(atIndex),
    );
    if (!methodMatch) {
      index = atIndex + 1;
      continue;
    }

    const methodName = methodMatch[1];
    const signatureIndex = text.indexOf(methodMatch[0], atIndex);
    const braceIndex = text.indexOf("{", signatureIndex);
    const closeBraceIndex = findMatchingBrace(text, braceIndex);

    methods.push({
      name: methodName,
      source: text.slice(blockStart, closeBraceIndex + 1).trim(),
      normalized: normalizeWhitespace(text.slice(signatureIndex, closeBraceIndex + 1)),
    });

    index = closeBraceIndex + 1;
  }

  return methods;
}

function ownerFromMethodName(methodName, sourceClasses) {
  const body = methodName.replace(/^test_?/, "");
  for (const source of sourceClasses) {
    if (body.includes(source)) {
      return source;
    }
  }

  return null;
}

function resolveOwner(file, sourceClasses) {
  const fileName = path.basename(file.relativePath);
  const className = file.className || fileName.replace(/\.java$/, "");

  if (fileName === "MatrixesTest.java" || className === "MatricesTest") {
    return { type: "whole", owner: "Matrices", base: true };
  }

  if (fileName === "MatricesNullArgumentContractTest.java") {
    return { type: "whole", owner: "Matrices" };
  }

  if (fileName === "MatrixParityFuzzTest.java") {
    return { type: "whole", owner: "Matrix", comment: "Placed here because this parity test treats Matrix as the generic reference surface across matrix implementations." };
  }

  if (fileName === "MatrixRepresentableShapeValidationTest.java") {
    return { type: "whole", owner: "AbstractMatrix", comment: "Placed here because these assertions cover the shared matrix-shape contract across Matrix and primitive matrix implementations." };
  }

  if (fileName === "MatrixZeroColumnRowStreamTest.java" || fileName === "PrimitiveMatrixUpdateAllNullValidationTest.java"
      || fileName === "JavadocExampleMatrixTest.java" || fileName === "JavadocExampleMatrixGroup1Test.java"
      || fileName === "JavadocExampleMatrixGroup2Test.java" || fileName === "JavadocExampleTupleTest.java"
      || fileName === "JavadocExampleUtilsTest.java" || fileName === "JavadocExampleOtherTest.java") {
    return { type: "split" };
  }

  if (fileName === "JavadocExampleArraysTest.java") {
    return { type: "whole", owner: "Arrays" };
  }

  for (const source of sourceClasses) {
    const canonicalFile = `${source}Test.java`;
    if (fileName === canonicalFile || className === `${source}Test`) {
      return { type: "whole", owner: source, base: true };
    }
  }

  for (const source of sourceClasses) {
    const pattern = new RegExp(`^${source}(?:[0-9]+)?Test$`);
    if (pattern.test(className) || pattern.test(fileName.replace(/\.java$/, ""))) {
      return { type: "whole", owner: source };
    }
  }

  return { type: "skip" };
}

function splitOwnerForMethod(fileName, methodName, sourceClasses) {
  if (fileName === "MatrixZeroColumnRowStreamTest.java") {
    if (methodName.includes("GenericMatrix")) {
      return "Matrix";
    }
    return ownerFromMethodName(methodName, sourceClasses);
  }

  if (fileName === "PrimitiveMatrixUpdateAllNullValidationTest.java") {
    if (methodName.includes("ObjectMatrix")) {
      return "Matrix";
    }
    return ownerFromMethodName(methodName, sourceClasses);
  }

  if (fileName === "JavadocExampleUtilsTest.java") {
    if (methodName.includes("ImmutableIntArray")) {
      return "ImmutableIntArray";
    }
    if (methodName.includes("AbstractMatrix")) {
      return "AbstractMatrix";
    }
    return "Matrices";
  }

  if (fileName === "JavadocExampleOtherTest.java") {
    if (methodName.includes("ImmutableIntArray")) {
      return "ImmutableIntArray";
    }
    if (methodName.includes("ParallelMode")) {
      return "ParallelMode";
    }
    return "Points";
  }

  return ownerFromMethodName(methodName, sourceClasses);
}

function insertBeforeFinalBrace(raw, chunk) {
  const end = raw.lastIndexOf("}");
  const before = raw.slice(0, end).trimEnd();
  return `${before}\n\n${chunk}\n}\n`;
}

function normalizeLineEndings(text) {
  return text.replace(/\r\n/g, "\n");
}

const sourceClasses = listSourceClasses();
const tracked = new Set([...headTrackedPaths]);
const utilFiles = [...tracked].filter((relativePath) => relativePath.startsWith("src/test/java/com/landawn/abacus/util/"));
const parsedFiles = utilFiles.map((relativePath) => parseJavaFile(relativePath, normalizeLineEndings(readTrackedFile(relativePath))));

const ownerBuckets = new Map();
const nestedNames = new Map();
const allImportsByOwner = new Map();
const filesToDelete = new Set();
const baseFiles = new Map();

function ensureOwner(owner) {
  if (!ownerBuckets.has(owner)) {
    ownerBuckets.set(owner, []);
  }
  if (!allImportsByOwner.has(owner)) {
    allImportsByOwner.set(owner, new Set([...canonicalImports]));
  }
}

function reserveNestedName(owner, desiredName) {
  const key = `${owner}:${desiredName}`;
  const count = nestedNames.get(key) || 0;
  nestedNames.set(key, count + 1);
  return count === 0 ? desiredName : `${desiredName}_${count + 1}`;
}

for (const file of parsedFiles) {
  const resolution = resolveOwner(file, sourceClasses);
  const fileName = path.basename(file.relativePath);

  if (resolution.type === "skip") {
    continue;
  }

  if (resolution.type === "whole") {
    const owner = resolution.owner;
    ensureOwner(owner);
    for (const imp of file.imports) {
      allImportsByOwner.get(owner).add(imp);
    }

    const canonicalPath = `src/test/java/com/landawn/abacus/util/${owner}Test.java`;
    const currentBase = baseFiles.get(owner);
    if (resolution.base && (!currentBase || currentBase.relativePath !== canonicalPath)) {
      baseFiles.set(owner, { ...file, canonicalPath });
      if (file.relativePath !== canonicalPath) {
        filesToDelete.add(file.relativePath);
      }
      continue;
    }

    const nestedChunk = `${resolution.comment ? `    // ${resolution.comment}\n` : ""}    @Nested\n${removePublicFromClassChunk(file.classChunk)
      .split("\n")
      .map((line) => `    ${line}`)
      .join("\n")}`;
    ownerBuckets.get(owner).push(nestedChunk);
    filesToDelete.add(file.relativePath);
    continue;
  }

  if (resolution.type === "split") {
    const methods = extractTestMethods(file);
    const grouped = new Map();

    for (const method of methods) {
      const owner = splitOwnerForMethod(fileName, method.name, sourceClasses);
      if (!owner) {
        throw new Error(`Unable to resolve owner for ${fileName}#${method.name}`);
      }
      ensureOwner(owner);
      if (!grouped.has(owner)) {
        grouped.set(owner, []);
      }
      grouped.get(owner).push(method.source);
      for (const imp of file.imports) {
        allImportsByOwner.get(owner).add(imp);
      }
    }

    for (const [owner, memberSources] of grouped.entries()) {
      const nestedName = reserveNestedName(owner, `${path.basename(file.relativePath, ".java")}_${owner}`);
      const comment =
        fileName === "MatrixParityFuzzTest.java"
          ? "Placed here because this parity test spans Matrix and IntMatrix behavior."
          : fileName === "MatrixRepresentableShapeValidationTest.java"
            ? "Placed here because these assertions cover shared matrix behavior across multiple implementations."
            : "";
      const annotations = file.classAnnotations
        .split(/\r?\n/)
        .map((line) => line.trim())
        .filter((line) => line.startsWith("@"));
      const annotationBlock = annotations.length === 0 ? "" : `${annotations.map((line) => `    ${line}`).join("\n")}\n`;
      const body = memberSources.map((source) => source.split("\n").map((line) => `        ${line}`).join("\n")).join("\n\n");
      const chunk = `${comment ? `    // ${comment}\n` : ""}    @Nested\n${annotationBlock}    class ${nestedName} extends TestBase {\n${body}\n    }`;
      ownerBuckets.get(owner).push(chunk);
    }

    filesToDelete.add(file.relativePath);
  }
}

for (const source of sourceClasses) {
  ensureOwner(source);
  if (!baseFiles.has(source)) {
    const canonicalPath = `src/test/java/com/landawn/abacus/util/${source}Test.java`;
    baseFiles.set(source, {
      relativePath: canonicalPath,
      canonicalPath,
      banner: "",
      packageDecl: `package ${packageName};`,
      imports: [],
      classDecl: `public class ${source}Test extends TestBase `,
      body: "",
      raw: `package ${packageName};\n\npublic class ${source}Test extends TestBase {\n}\n`,
    });
  }
}

for (const source of sourceClasses) {
  const base = baseFiles.get(source);
  const canonicalPath = `src/test/java/com/landawn/abacus/util/${source}Test.java`;
  const imports = [...allImportsByOwner.get(source)]
    .filter((line) => !line.includes("com.landawn.abacus.util.") || line.includes(`${source}`) || !line.endsWith(".*;"))
    .sort((a, b) => {
      const aStatic = a.startsWith("import static ");
      const bStatic = b.startsWith("import static ");
      if (aStatic !== bStatic) {
        return aStatic ? -1 : 1;
      }
      return a.localeCompare(b);
    });
  const banner = base.banner || "";
  const packageDecl = base.packageDecl || `package ${packageName};`;
  const nestedChunks = ownerBuckets.get(source) || [];
  let output;

  if (base.className === `${source}Test`) {
    const baseRaw = normalizeLineEndings(base.raw);
    const parsedBase = parseJavaFile(base.relativePath, baseRaw);
    const prePackage = parsedBase.banner || banner;
    const importBlock = imports.length === 0 ? "" : `${imports.join("\n")}\n\n`;
    const prelude = `${prePackage}${packageDecl}\n\n${importBlock}`;
    const classHeader = baseRaw.slice(baseRaw.indexOf(parsedBase.classDecl), parsedBase.bodyStart + 1);
    const body = parsedBase.body.trimEnd();
    const appended = nestedChunks.length === 0 ? body : `${body}${body ? "\n\n" : ""}${nestedChunks.join("\n\n")}`;
    output = `${prelude}${classHeader}${appended ? `\n${appended}\n` : "\n"}\n}\n`;
  } else {
    const prelude = `${banner}${packageDecl}\n\n${imports.join("\n")}\n\n`;
    const classHeader = `public class ${source}Test extends TestBase {`;
    const appended = nestedChunks.join("\n\n");
    output = `${prelude}${classHeader}${appended ? `\n\n${appended}\n` : "\n"}\n}\n`;
  }

  fs.writeFileSync(path.join(repoRoot, canonicalPath), output.replace(/\n{3,}/g, "\n\n"));
}

for (const relativePath of filesToDelete) {
  const absolutePath = path.join(repoRoot, relativePath);
  if (fs.existsSync(absolutePath) && !sourceClasses.some((source) => relativePath === `src/test/java/com/landawn/abacus/util/${source}Test.java`)) {
    fs.unlinkSync(absolutePath);
  }
}

if (fs.existsSync(path.join(repoRoot, "src/test/java/com/landawn/abacus/util/MatrixesTest.java"))) {
  fs.unlinkSync(path.join(repoRoot, "src/test/java/com/landawn/abacus/util/MatrixesTest.java"));
}

console.log(`Consolidated ${utilFiles.length} util test files into ${sourceClasses.length} canonical test classes.`);
