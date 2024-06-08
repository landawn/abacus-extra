package com.landawn.abacus.util;

import org.junit.jupiter.api.Test;

class ArraysTest {

    @Test
    void test_zip() {
        String[][][] a = { { { "a", "b" }, { "c", "d" } }, { { "1", "2" }, { "3", "4" } } };
        String[][][] b = { { { "a", "b" }, { "c", "d" } }, { { "1", "2" }, { "3", "4" } } };
        N.println(a);
        Arrays.fff.println(a);

        String[][][] c = Arrays.fff.zip(a, b, (t, u) -> t + u);
        Arrays.fff.println(c);

    }

}
