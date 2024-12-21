/*
 * Copyright (C) 2024 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.landawn.abacus.util;

import org.junit.jupiter.api.Test;

class MatrixesTest {

    @Test
    public void test_toString() {

        {
            final ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);

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
    public void test_zipMatrix() {

        {
            final ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
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
            Matrixes.zip(N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a), Byte.class).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zipToInt(N.asList(mx, mx, mx, mx), N::sum).println();
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            final IntMatrix mx = IntMatrix.range(0, 8).reshape(2, 4);
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
            Matrixes.zip(N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a), byte.class).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zipToLong(N.asList(mx, mx, mx, mx), a -> (long) N.sum(a)).println();
        }

    }

    @Test
    public void test_multiply() {
        {
            final ByteMatrix mxa = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            mxa.println();

            final ByteMatrix mxb = ByteMatrix.range((byte) 0, (byte) 8).reshape(4, 2);
            mxb.println();

            mxa.multiply(mxb).println();
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            final IntMatrix mxa = IntMatrix.range(0, 8).reshape(2, 4);
            mxa.println();

            final IntMatrix mxb = IntMatrix.range(0, 8).reshape(4, 2);
            mxb.println();

            mxa.multiply(mxb).println();
        }
    }

    @Test
    public void test_multiply_perf() {
        final int rows = 2000;
        final int cols = 3000;

        final int[][] a = new int[rows][cols];
        final int[][] b = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                a[i][j] = 1;
                b[j][i] = 2;
            }
        }

        final IntMatrix mxa = IntMatrix.of(a);
        final IntMatrix mxb = IntMatrix.of(b);

        //    final IntMatrix mxc = mxa.multiply(mxb);
        //
        //    assertEquals(rows, mxc.rows);
        //    assertEquals(rows, mxc.cols);
        // mxc.println();

        Profiler.run(1, 1, 1, "seq-multiply(" + rows + ", " + cols + ")", () -> {
            Matrixes.setParallelEnabled(ParallelEnabled.NO);
            mxa.multiply(mxb);
        }).printResult();

        Profiler.run(1, 1, 1, "parallel-multiply(" + rows + ", " + cols + ")", () -> {
            Matrixes.setParallelEnabled(ParallelEnabled.YES);
            mxa.multiply(mxb);
        }).printResult();

    }

}
