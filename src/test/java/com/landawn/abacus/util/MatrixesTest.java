package com.landawn.abacus.util;

import org.junit.jupiter.api.Test;

class MatrixesTest {

    @Test
    void test_toString() {

        {
            ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);

            mx.println();
            mx.boxed().println();

            N.println(Strings.repeat('-', 80));

            N.println(mx);
            N.println(mx.boxed());

            mx.boxed().toDataSetH(N.asList("a", "b", "c", "d")).println();
            mx.boxed().toDataSetV(N.asList("a", "b")).println();
        }
    }

    @Test
    void test_zipMatrix() {

        {
            ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            mx.println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(Byte.class, N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zipToInt(N.asList(mx, mx, mx, mx), N::sum).println();
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            IntMatrix mx = IntMatrix.range(0, 8).reshape(2, 4);
            mx.println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(byte.class, N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zipToLong(N.asList(mx, mx, mx, mx), a -> (long) N.sum(a)).println();
        }

    }

    @Test
    void test_multiply() {
        {
            ByteMatrix mxa = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            mxa.println();

            ByteMatrix mxb = ByteMatrix.range((byte) 0, (byte) 8).reshape(4, 2);
            mxb.println();

            mxa.multiply(mxb).println();
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            IntMatrix mxa = IntMatrix.range(0, 8).reshape(2, 4);
            mxa.println();

            IntMatrix mxb = IntMatrix.range(0, 8).reshape(4, 2);
            mxb.println();

            mxa.multiply(mxb).println();
        }
    }

}
