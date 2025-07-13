package com.landawn.abacus;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.IOUtil;
import com.landawn.abacus.util.Strings;
import com.landawn.abacus.util.stream.Stream;

class CodeHelper {

    @Test
    void test_00() {
        File dir = new File("./src/main/java/com/landawn/abacus/");

        Stream.listFiles(dir, true).filter(f -> f.getName().endsWith("java")).forEach(f -> {
            try {
                String content = IOUtil.readAllToString(f);
                content = Strings.replaceAll(content, "IntBiFunction<Integer, ", "IntBinaryOperator<");
                content = Strings.replaceAll(content, "IntTriFunction<Integer, ", "IntTernaryOperator<");
                content = Strings.replaceAll(content, "CharBiFunction<Character, ", "CharBinaryOperator<");
                content = Strings.replaceAll(content, "ChatTriFunction<Character, ", "CharTernaryOperator<");
                content = content.replaceAll("(\\w+)BiFunction<\\1,\\s*", "$1BinaryOperator<");
                content = content.replaceAll("(\\w+)TriFunction<\\1,\\s*", "$1TernaryOperator<");
                IOUtil.write(content, f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
