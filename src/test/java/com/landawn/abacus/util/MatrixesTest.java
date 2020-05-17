package com.landawn.abacus.util;

import org.junit.jupiter.api.Test;

class MatrixesTest {

    @Test
    void test_zipMatrix() {

        {
            ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            mx.println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx), (i, j) -> (byte) (i + j)).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(Byte.class, N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a)).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zipToInt(N.asList(mx, mx, mx, mx), a -> N.sum(a)).println();
        }

        N.println(StringUtil.repeat('=', 80));
        N.println(StringUtil.repeat('=', 80));

        {
            IntMatrix mx = IntMatrix.range(0, 8).reshape(2, 4);
            mx.println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx), (i, j) -> i + j).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx), (i, j) -> i + j).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx), (i, j) -> i + j).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), (i, j) -> i + j).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zip(byte.class, N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a)).println();

            N.println(StringUtil.repeat('-', 80));
            Matrixes.zipToLong(N.asList(mx, mx, mx, mx), a -> (long) N.sum(a)).println();
        }

    }

}
