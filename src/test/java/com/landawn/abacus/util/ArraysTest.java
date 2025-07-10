/*
 * Copyright (C) 2024 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.stream.Stream;

class ArraysTest {

    @Test
    public void test_minSubArrayLen() {
        final String[][] a = { { "a", "b" }, { "c", "d", "d" } };
        final String[][][] b = { { { "a", "b" } }, { { "1", "2" }, { "3", "4" } } };
        final int[][][] c = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);

        assertEquals(2, Arrays.ff.minSubArrayLen(a));
        assertEquals(3, Arrays.ff.maxSubArrayLen(a));
        assertEquals(1, Arrays.ff.minSubArrayLen(b));
        assertEquals(2, Arrays.ff.maxSubArrayLen(b));
        assertEquals(1, Arrays.ff.minSubArrayLen(c));
        assertEquals(2, Arrays.ff.maxSubArrayLen(c));

    }

    @Test
    public void test_zip() {
        {
            final String[][][] a = { { { "a", "b" }, { "c", "d" } }, { { "1", "2" }, { "3", "4" } } };
            final String[][][] b = { { { "a", "b" }, { "c", "d" } }, { { "1", "2" }, { "3", "4" } } };
            N.println(a);
            Arrays.fff.println(a);

            final String[][][] c = Arrays.fff.zip(a, b, (t, u) -> t + u);
            Arrays.fff.println(c);
        }
        {
            final int[][][] a = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);
            final int[][][] b = Arrays.reshape(Array.repeat(0, 9), 3, 2);
            final int[][][] c = Arrays.zip(a, b, -1, -1, (p1, p2) -> p1 + p2);
            Arrays.println(c);
        }

    }

    @Test
    public void test_reshape() {
        {
            final int[] a = Array.rangeClosed(1, 9);
            final int[][] b = Arrays.reshape(a, 2);
            final int[][][] c = Arrays.reshape(a, 2, 2);
            final int[][][] d = Arrays.reshape(a, 3, 2);
            final int[][][] e = Arrays.reshape(a, 2, 3);
            final int[][][] f = Arrays.reshape(a, 3, 3);
            Arrays.println(a);
            N.println(Strings.repeat('-', 80));
            Arrays.println(b);
            N.println(Strings.repeat('-', 80));
            Arrays.println(c);
            N.println(Strings.repeat('-', 80));
            Arrays.println(d);
            N.println(Strings.repeat('-', 80));
            Arrays.println(e);
            N.println(Strings.repeat('-', 80));
            Arrays.println(f);
        }
    }

    @Test
    public void test_001() throws IOException {

        File file = new File("src/main/java/com/landawn/abacus/util/Arrays.java");
        List<String> lines = IOUtil.readAllLines(file);

        Stream.of(lines).filter(line -> line.trim().startsWith("*") && line.contains("N.")).forEach(System.out::println);

        List<String> newLines = Stream.of(lines)
                .map(line -> line.trim().startsWith("*") && line.contains("N.") ? line.replace("N.", "Arrays.") : line)
                .toList();

        IOUtil.writeLines(newLines, file);
    }

}
