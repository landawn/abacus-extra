package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.stream.CharStream;
import com.landawn.abacus.util.stream.DoubleStream;
import com.landawn.abacus.util.stream.FloatStream;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.stream.ShortStream;

public class MatrixZeroColumnRowStreamTest extends TestBase {

    @Test
    public void testIntMatrixRowsForZeroColumnMatrix() {
        final IntMatrix matrix = IntMatrix.of(new int[][] { {}, {}, {} });
        final List<int[]> rows = matrix.streamR().map(IntStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new int[0], rows.get(0));
        assertArrayEquals(new int[0], rows.get(1));
        assertArrayEquals(new int[0], rows.get(2));
    }

    @Test
    public void testLongMatrixRowsForZeroColumnMatrix() {
        final LongMatrix matrix = LongMatrix.of(new long[][] { {}, {}, {} });
        final List<long[]> rows = matrix.streamR().map(LongStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new long[0], rows.get(0));
        assertArrayEquals(new long[0], rows.get(1));
        assertArrayEquals(new long[0], rows.get(2));
    }

    @Test
    public void testDoubleMatrixRowsForZeroColumnMatrix() {
        final DoubleMatrix matrix = DoubleMatrix.of(new double[][] { {}, {}, {} });
        final List<double[]> rows = matrix.streamR().map(DoubleStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new double[0], rows.get(0));
        assertArrayEquals(new double[0], rows.get(1));
        assertArrayEquals(new double[0], rows.get(2));
    }

    @Test
    public void testFloatMatrixRowsForZeroColumnMatrix() {
        final FloatMatrix matrix = FloatMatrix.of(new float[][] { {}, {}, {} });
        final List<float[]> rows = matrix.streamR().map(FloatStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new float[0], rows.get(0));
        assertArrayEquals(new float[0], rows.get(1));
        assertArrayEquals(new float[0], rows.get(2));
    }

    @Test
    public void testShortMatrixRowsForZeroColumnMatrix() {
        final ShortMatrix matrix = ShortMatrix.of(new short[][] { {}, {}, {} });
        final List<short[]> rows = matrix.streamR().map(ShortStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new short[0], rows.get(0));
        assertArrayEquals(new short[0], rows.get(1));
        assertArrayEquals(new short[0], rows.get(2));
    }

    @Test
    public void testByteMatrixRowsForZeroColumnMatrix() {
        final ByteMatrix matrix = ByteMatrix.of(new byte[][] { {}, {}, {} });
        final List<byte[]> rows = matrix.streamR().map(ByteStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new byte[0], rows.get(0));
        assertArrayEquals(new byte[0], rows.get(1));
        assertArrayEquals(new byte[0], rows.get(2));
    }

    @Test
    public void testCharMatrixRowsForZeroColumnMatrix() {
        final CharMatrix matrix = CharMatrix.of(new char[][] { {}, {}, {} });
        final List<char[]> rows = matrix.streamR().map(CharStream::toArray).toList();

        assertEquals(3, rows.size());
        assertArrayEquals(new char[0], rows.get(0));
        assertArrayEquals(new char[0], rows.get(1));
        assertArrayEquals(new char[0], rows.get(2));
    }

    @Test
    public void testBooleanMatrixRowsForZeroColumnMatrix() {
        final BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { {}, {}, {} });
        final List<List<Boolean>> rows = matrix.streamR().map(row -> row.toList()).toList();

        assertEquals(3, rows.size());
        assertTrue(rows.get(0).isEmpty());
        assertTrue(rows.get(1).isEmpty());
        assertTrue(rows.get(2).isEmpty());
    }

    @Test
    public void testGenericMatrixRowsForZeroColumnMatrix() {
        final Matrix<Integer> matrix = Matrix.of(new Integer[][] { {}, {}, {} });
        final List<List<Integer>> rows = matrix.streamR().map(row -> row.toList()).toList();

        assertEquals(3, rows.size());
        assertTrue(rows.get(0).isEmpty());
        assertTrue(rows.get(1).isEmpty());
        assertTrue(rows.get(2).isEmpty());
    }
}
