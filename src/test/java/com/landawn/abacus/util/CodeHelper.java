package com.landawn.abacus.util;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class CodeHelper {

    @Test
    void test_00() throws IOException {
        File file = new File("./src/main/java/com/landawn/abacus/util/Arrays.java");
        String str = IOUtil.readAllToString(file);

        // N.println(str);

        String input = str;
        String output = input.replaceAll("(\\w+)TriFunction<\\1,\\s*", "$1TernaryOperator<");
        System.out.println(output); // Output: ShortBinaryOperator<Short>

        IOUtil.write(output, file);
    }

}
