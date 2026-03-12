#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const repoRoot = process.cwd();
const srcRoot = path.join(repoRoot, 'src', 'main', 'java');
const testRoot = path.join(repoRoot, 'src', 'test', 'java');

function walkJavaFiles(dir) {
    const results = [];
    const entries = fs.readdirSync(dir, { withFileTypes: true });

    for (const entry of entries) {
        const fullPath = path.join(dir, entry.name);

        if (entry.isDirectory()) {
            results.push(...walkJavaFiles(fullPath));
        } else if (entry.isFile() && entry.name.endsWith('.java')) {
            results.push(fullPath);
        }
    }

    return results;
}

function readFile(filePath) {
    return fs.readFileSync(filePath, 'utf8').replace(/\r\n/g, '\n');
}

function stripComments(line, state) {
    let out = '';

    for (let i = 0; i < line.length; i++) {
        const ch = line[i];
        const next = line[i + 1];

        if (state.inBlockComment) {
            if (ch === '*' && next === '/') {
                state.inBlockComment = false;
                i++;
            }

            continue;
        }

        if (state.inString) {
            if (ch === '\\') {
                out += '  ';
                i++;
                continue;
            }

            if (ch === state.inString) {
                state.inString = null;
            }

            out += ' ';
            continue;
        }

        if (ch === '/' && next === '*') {
            state.inBlockComment = true;
            i++;
            continue;
        }

        if (ch === '/' && next === '/') {
            break;
        }

        if (ch === '"' || ch === '\'') {
            state.inString = ch;
            out += ' ';
            continue;
        }

        out += ch;
    }

    return out;
}

function extractTypeInfo(text) {
    const pkgMatch = text.match(/^\s*package\s+([\w.]+)\s*;/m);
    const typeMatch = text.match(/^\s*(?:public\s+)?(?:(?:abstract|final|sealed|non-sealed|static)\s+)*(class|enum|interface)\s+([A-Za-z0-9_]+)/m);

    if (!typeMatch) {
        return null;
    }

    return {
        packageName: pkgMatch ? pkgMatch[1] : '',
        typeKind: typeMatch[1],
        typeName: typeMatch[2]
    };
}

function extractPublicMethods(text, typeName, typeKind) {
    const lines = text.split('\n');
    const methods = [];
    const state = { inBlockComment: false, inString: null };
    let topLevelDepth = 0;
    let inTopLevelType = false;
    let pending = '';
    let pendingStartLine = 0;

    for (let index = 0; index < lines.length; index++) {
        const rawLine = lines[index];
        const line = stripComments(rawLine, state);
        const trimmed = line.trim();

        if (!inTopLevelType) {
            if (new RegExp(`\\b(class|enum|interface)\\s+${typeName}\\b`).test(line)) {
                inTopLevelType = true;
            }
        }

        if (inTopLevelType && topLevelDepth === 1) {
            if (!pending && trimmed.includes('public') && trimmed.includes('(')) {
                pending = trimmed;
                pendingStartLine = index + 1;
            } else if (pending) {
                pending += ` ${trimmed}`;
            }

            if (pending && (trimmed.includes('{') || trimmed.endsWith(';'))) {
                const signature = pending.replace(/@\w+(?:\s*\([^)]*\))?/g, ' ').replace(/\s+/g, ' ').trim();

                if (/\bpublic\b/.test(signature)
                    && !/\b(class|interface|enum|record)\b/.test(signature)
                    && !/\bnew\s+[A-Za-z0-9_]+\s*\(/.test(signature)) {
                    const match = signature.match(/([A-Za-z_][A-Za-z0-9_]*)\s*\(/g);

                    if (match && match.length > 0) {
                        const methodName = match[match.length - 1].replace(/\s*\($/, '');
                        const isConstructor = methodName === typeName;

                        if (!isConstructor) {
                            methods.push({
                                name: methodName,
                                signature,
                                line: pendingStartLine
                            });
                        }
                    }
                }

                pending = '';
                pendingStartLine = 0;
            }
        }

        for (const ch of line) {
            if (ch === '{') {
                topLevelDepth++;
            } else if (ch === '}') {
                topLevelDepth--;
            }
        }
    }

    if (typeKind === 'enum') {
        methods.unshift(
            { name: 'values', signature: 'public static values()', line: 0, synthetic: true },
            { name: 'valueOf', signature: 'public static valueOf(String)', line: 0, synthetic: true },
            { name: 'name', signature: 'public final name()', line: 0, synthetic: true },
            { name: 'ordinal', signature: 'public final ordinal()', line: 0, synthetic: true },
            { name: 'toString', signature: 'public String toString()', line: 0, synthetic: true },
            { name: 'equals', signature: 'public boolean equals(Object)', line: 0, synthetic: true },
            { name: 'hashCode', signature: 'public int hashCode()', line: 0, synthetic: true },
            { name: 'compareTo', signature: 'public int compareTo(Enum)', line: 0, synthetic: true }
        );
    }

    return methods;
}

function extractTestMethods(text) {
    const lines = text.split('\n');
    const tests = [];

    for (let i = 0; i < lines.length; i++) {
        if (!/@Test\b/.test(lines[i])) {
            continue;
        }

        for (let j = i + 1; j < Math.min(i + 6, lines.length); j++) {
            const match = lines[j].match(/\bpublic\s+void\s+([A-Za-z0-9_]+)\s*\(/);

            if (match) {
                let bodyStart = -1;

                for (let k = j; k < Math.min(j + 8, lines.length); k++) {
                    if (lines[k].includes('{')) {
                        bodyStart = k;
                        break;
                    }
                }

                let braceDepth = 0;
                const bodyLines = [];

                if (bodyStart >= 0) {
                    for (let k = bodyStart; k < lines.length; k++) {
                        const line = lines[k];
                        bodyLines.push(line);

                        for (const ch of line) {
                            if (ch === '{') {
                                braceDepth++;
                            } else if (ch === '}') {
                                braceDepth--;
                            }
                        }

                        if (braceDepth === 0) {
                            break;
                        }
                    }
                }

                tests.push({
                    name: match[1],
                    line: j + 1,
                    body: bodyLines.join('\n')
                });
                break;
            }
        }
    }

    return tests;
}

function normalizeName(name) {
    return name.toLowerCase().replace(/[^a-z0-9]/g, '');
}

function buildAliases(methodName) {
    const aliases = new Set([methodName]);

    if (methodName.startsWith('get') && methodName.length > 3) {
        aliases.add(methodName.slice(3));
    }

    if (methodName.startsWith('set') && methodName.length > 3) {
        aliases.add(methodName.slice(3));
    }

    if (methodName.startsWith('is') && methodName.length > 2) {
        aliases.add(methodName.slice(2));
    }

    if (methodName.startsWith('unsafe') && methodName.length > 6) {
        aliases.add(methodName.slice(6));
    }

    if (methodName === 'copyOf') {
        aliases.add('copy');
    }

    if (methodName === 'forEachIndexed') {
        aliases.add('forEachWithIndex');
    }

    return Array.from(aliases).map(normalizeName);
}

function bodyReferencesMethod(methodName, test) {
    const escapedName = methodName.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    const directCall = new RegExp(`(?:\\.|\\b)${escapedName}\\s*\\(`);
    return directCall.test(test.body);
}

function isCovered(methodName, tests) {
    const aliases = buildAliases(methodName);

    return tests.some(test => {
        if (bodyReferencesMethod(methodName, test)) {
            return true;
        }

        const normalized = normalizeName(test.name.replace(/^test/, ''));
        return aliases.some(alias => normalized.includes(alias));
    });
}

function relative(p) {
    return path.relative(repoRoot, p).replace(/\\/g, '/');
}

function main() {
    const sourceFiles = walkJavaFiles(srcRoot)
        .filter(file => relative(file).startsWith('src/main/java/com/landawn/abacus/util/'));

    const results = [];

    for (const sourceFile of sourceFiles) {
        const sourceText = readFile(sourceFile);
        const info = extractTypeInfo(sourceText);

        if (!info) {
            continue;
        }

        const testFile = path.join(testRoot, ...info.packageName.split('.'), `${info.typeName}Test.java`);
        const testExists = fs.existsSync(testFile);
        const methods = extractPublicMethods(sourceText, info.typeName, info.typeKind);
        const tests = testExists ? extractTestMethods(readFile(testFile)) : [];
        const uncovered = methods.filter(method => !isCovered(method.name, tests));

        results.push({
            typeName: info.typeName,
            sourceFile: relative(sourceFile),
            testFile: testExists ? relative(testFile) : null,
            publicMethodCount: methods.length,
            testMethodCount: tests.length,
            uncovered
        });
    }

    const summary = {
        classCount: results.length,
        classesWithGaps: results.filter(it => it.uncovered.length > 0 || !it.testFile).length,
        totalUncoveredMethods: results.reduce((sum, it) => sum + it.uncovered.length, 0),
        results: results
            .filter(it => it.uncovered.length > 0 || !it.testFile)
            .map(it => ({
                typeName: it.typeName,
                sourceFile: it.sourceFile,
                testFile: it.testFile,
                publicMethodCount: it.publicMethodCount,
                testMethodCount: it.testMethodCount,
                uncoveredMethods: it.uncovered.map(method => ({
                    name: method.name,
                    line: method.line,
                    synthetic: Boolean(method.synthetic),
                    signature: method.signature
                }))
            }))
    };

    console.log(JSON.stringify(summary, null, 2));
}

main();
