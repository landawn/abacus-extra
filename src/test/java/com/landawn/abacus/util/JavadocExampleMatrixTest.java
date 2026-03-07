package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests that verify Javadoc examples in Matrix class files.
 */
public class JavadocExampleMatrixTest {

    // ==================== IntMatrix Javadoc Examples ====================

    @Test
    public void testIntMatrixEmptyRowCount() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.empty();
        // matrix.rowCount() returns 0
        IntMatrix matrix = IntMatrix.empty();
        assertEquals(0, matrix.rowCount());
    }

    @Test
    public void testIntMatrixEmptyColumnCount() {
        // IntMatrix.java: matrix.columnCount() returns 0
        IntMatrix matrix = IntMatrix.empty();
        assertEquals(0, matrix.columnCount());
    }

    @Test
    public void testIntMatrixOfGet() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
        // matrix.get(0, 1) returns 2
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(2, matrix.get(0, 1));
    }

    @Test
    public void testIntMatrixFromChar() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.from(new char[][] {{'A', 'B'}, {'C', 'D'}});
        // matrix.get(0, 0) returns 65
        IntMatrix matrix = IntMatrix.from(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertEquals(65, matrix.get(0, 0));
    }

    @Test
    public void testIntMatrixFromByte() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.from(new byte[][] {{1, 2}, {3, 4}});
        // matrix.get(1, 0) returns 3
        IntMatrix matrix = IntMatrix.from(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(3, matrix.get(1, 0));
    }

    @Test
    public void testIntMatrixFromShort() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.from(new short[][] {{1, 2}, {3, 4}});
        // matrix.get(1, 1) returns 4
        IntMatrix matrix = IntMatrix.from(new short[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(4, matrix.get(1, 1));
    }

    @Test
    public void testIntMatrixRange() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.range(0, 5);   // Creates [[0, 1, 2, 3, 4]]
        IntMatrix matrix = IntMatrix.range(0, 5);
        assertEquals("[[0, 1, 2, 3, 4]]", matrix.toString());
    }

    @Test
    public void testIntMatrixRangeEmpty() {
        // IntMatrix.java: IntMatrix empty = IntMatrix.range(5, 0);    // Creates an empty matrix
        // range(5,0) wraps Array.range(5,0) which returns int[0], so result is 1 row with 0 columns
        IntMatrix empty = IntMatrix.range(5, 0);
        assertEquals(1, empty.rowCount());
        assertEquals(0, empty.columnCount());
    }

    @Test
    public void testIntMatrixRangeWithStep() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.range(0, 10, 2);   // Creates [[0, 2, 4, 6, 8]]
        IntMatrix matrix = IntMatrix.range(0, 10, 2);
        assertEquals("[[0, 2, 4, 6, 8]]", matrix.toString());
    }

    @Test
    public void testIntMatrixRangeDescending() {
        // IntMatrix.java: IntMatrix desc = IntMatrix.range(10, 0, -2);    // Creates [[10, 8, 6, 4, 2]]
        IntMatrix desc = IntMatrix.range(10, 0, -2);
        assertEquals("[[10, 8, 6, 4, 2]]", desc.toString());
    }

    @Test
    public void testIntMatrixRepeat() {
        // IntMatrix.java: IntMatrix matrix = IntMatrix.repeat(2, 3, 1);
        // Result: [[1, 1, 1], [1, 1, 1]]
        IntMatrix matrix = IntMatrix.repeat(2, 3, 1);
        assertEquals("[[1, 1, 1], [1, 1, 1]]", matrix.toString());
    }

    // ==================== ByteMatrix Javadoc Examples ====================

    @Test
    public void testByteMatrixEmptyRowCount() {
        ByteMatrix matrix = ByteMatrix.empty();
        assertEquals(0, matrix.rowCount());
    }

    @Test
    public void testByteMatrixOfGet() {
        // ByteMatrix.java: ByteMatrix matrix = ByteMatrix.of(new byte[][] {{1, 2, 3}, {4, 5, 6}});
        // matrix.get(1, 2) returns 6
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals((byte) 6, matrix.get(1, 2));
    }

    @Test
    public void testByteMatrixRange() {
        // ByteMatrix.java: ByteMatrix range = ByteMatrix.range((byte)1, (byte)5);
        // Creates matrix: [[1, 2, 3, 4]]
        ByteMatrix range = ByteMatrix.range((byte) 1, (byte) 5);
        assertEquals("[[1, 2, 3, 4]]", range.toString());
    }

    @Test
    public void testByteMatrixRangeWithStep() {
        // ByteMatrix.java: ByteMatrix range = ByteMatrix.range((byte)0, (byte)10, (byte)2);    // Creates [[0, 2, 4, 6, 8]]
        ByteMatrix range = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        assertEquals("[[0, 2, 4, 6, 8]]", range.toString());
    }

    @Test
    public void testByteMatrixRangeDescending() {
        // ByteMatrix.java: ByteMatrix desc = ByteMatrix.range((byte)10, (byte)0, (byte)-2);    // Creates [[10, 8, 6, 4, 2]]
        ByteMatrix desc = ByteMatrix.range((byte) 10, (byte) 0, (byte) -2);
        assertEquals("[[10, 8, 6, 4, 2]]", desc.toString());
    }

    @Test
    public void testByteMatrixRangeClosed() {
        // ByteMatrix.java: ByteMatrix range = ByteMatrix.rangeClosed((byte)1, (byte)4);
        // Creates matrix: [[1, 2, 3, 4]]
        ByteMatrix range = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
        assertEquals("[[1, 2, 3, 4]]", range.toString());
    }

    @Test
    public void testByteMatrixRangeClosedWithStep() {
        // ByteMatrix.java: ByteMatrix range = ByteMatrix.rangeClosed((byte)0, (byte)8, (byte)2);     // Creates [[0, 2, 4, 6, 8]]
        ByteMatrix range = ByteMatrix.rangeClosed((byte) 0, (byte) 8, (byte) 2);
        assertEquals("[[0, 2, 4, 6, 8]]", range.toString());
    }

    @Test
    public void testByteMatrixRangeClosedPartial() {
        // ByteMatrix.java: ByteMatrix partial = ByteMatrix.rangeClosed((byte)0, (byte)9, (byte)2);   // Creates [[0, 2, 4, 6, 8]] (9 not reachable)
        ByteMatrix partial = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 2);
        assertEquals("[[0, 2, 4, 6, 8]]", partial.toString());
    }

    @Test
    public void testByteMatrixRangeClosedDescending() {
        // ByteMatrix.java: ByteMatrix desc = ByteMatrix.rangeClosed((byte)10, (byte)0, (byte)-2);    // Creates [[10, 8, 6, 4, 2, 0]]
        ByteMatrix desc = ByteMatrix.rangeClosed((byte) 10, (byte) 0, (byte) -2);
        assertEquals("[[10, 8, 6, 4, 2, 0]]", desc.toString());
    }

    @Test
    public void testByteMatrixZipWith2() {
        // ByteMatrix.java: ByteMatrix result = matrix1.zipWith(matrix2, (a, b) -> (byte)(a * b));
        // result is: [[5, 12], [21, 32]]
        ByteMatrix matrix1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix matrix2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = matrix1.zipWith(matrix2, (a, b) -> (byte) (a * b));
        assertEquals("[[5, 12], [21, 32]]", result.toString());
    }

    @Test
    public void testByteMatrixZipWith3() {
        // ByteMatrix.java: result is: [[15, 18], [21, 24]]
        ByteMatrix matrix1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix matrix2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix matrix3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
        ByteMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> (byte) (a + b + c));
        assertEquals("[[15, 18], [21, 24]]", result.toString());
    }

    @Test
    public void testByteMatrixToString() {
        // ByteMatrix.java: System.out.println(matrix.toString());   // Output: [[1, 2], [3, 4]]
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals("[[1, 2], [3, 4]]", matrix.toString());
    }

    @Test
    public void testByteMatrixEmptyToString() {
        // ByteMatrix.java: System.out.println(empty.toString());   // Output: []
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals("[]", empty.toString());
    }

    @Test
    public void testByteMatrixMainDiagonal() {
        // ByteMatrix.java: ByteMatrix matrix = ByteMatrix.mainDiagonal(new byte[] {1, 2, 3});
        // Creates 3x3 matrix with diagonal [1, 2, 3] and zeros elsewhere
        ByteMatrix matrix = ByteMatrix.mainDiagonal(new byte[] { 1, 2, 3 });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals((byte) 1, matrix.get(0, 0));
        assertEquals((byte) 2, matrix.get(1, 1));
        assertEquals((byte) 3, matrix.get(2, 2));
        assertEquals((byte) 0, matrix.get(0, 1));
        assertEquals((byte) 0, matrix.get(1, 0));
    }

    @Test
    public void testByteMatrixAntiDiagonal() {
        // ByteMatrix.java: ByteMatrix matrix = ByteMatrix.antiDiagonal(new byte[] {1, 2, 3});
        // Creates 3x3 matrix with anti-diagonal [1, 2, 3] and zeros elsewhere
        ByteMatrix matrix = ByteMatrix.antiDiagonal(new byte[] { 1, 2, 3 });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals((byte) 1, matrix.get(0, 2));
        assertEquals((byte) 2, matrix.get(1, 1));
        assertEquals((byte) 3, matrix.get(2, 0));
        assertEquals((byte) 0, matrix.get(0, 0));
    }

    @Test
    public void testByteMatrixForEach() {
        // ByteMatrix.java: matrix.forEach(value -> System.out.print(value + " "));
        // Output: 1 2 3 4
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> values = new ArrayList<>();
        matrix.forEach(value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals((byte) 1, values.get(0));
        assertEquals((byte) 2, values.get(1));
        assertEquals((byte) 3, values.get(2));
        assertEquals((byte) 4, values.get(3));
    }

    @Test
    public void testByteMatrixForEachWithRange() {
        // ByteMatrix.java: matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
        // Output: 5 6 8 9  (processes elements in rows 1-2, columns 1-2)
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Byte> values = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals((byte) 5, values.get(0));
        assertEquals((byte) 6, values.get(1));
        assertEquals((byte) 8, values.get(2));
        assertEquals((byte) 9, values.get(3));
    }

    // ==================== ShortMatrix Javadoc Examples ====================

    @Test
    public void testShortMatrixEmptyRowCount() {
        ShortMatrix matrix = ShortMatrix.empty();
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    @Test
    public void testShortMatrixRange() {
        // ShortMatrix.java: ShortMatrix matrix = ShortMatrix.range((short) 0, (short) 5);   // Creates [[0, 1, 2, 3, 4]]
        ShortMatrix matrix = ShortMatrix.range((short) 0, (short) 5);
        assertEquals("[[0, 1, 2, 3, 4]]", matrix.toString());
    }

    @Test
    public void testShortMatrixForEach() {
        // ShortMatrix.java: matrix.forEach(value -> System.out.print(value + " "));
        // Output: 1 2 3 4
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<Short> values = new ArrayList<>();
        matrix.forEach(value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals((short) 1, values.get(0));
        assertEquals((short) 2, values.get(1));
        assertEquals((short) 3, values.get(2));
        assertEquals((short) 4, values.get(3));
    }

    @Test
    public void testShortMatrixForEachWithRange() {
        // ShortMatrix.java: matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
        // Output: 5 6 8 9  (processes elements in rows 1-2, columns 1-2)
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Short> values = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals((short) 5, values.get(0));
        assertEquals((short) 6, values.get(1));
        assertEquals((short) 8, values.get(2));
        assertEquals((short) 9, values.get(3));
    }

    // ==================== CharMatrix Javadoc Examples ====================

    @Test
    public void testCharMatrixEmptyRowCount() {
        // CharMatrix.java: CharMatrix matrix = CharMatrix.empty();
        // matrix.rowCount() returns 0
        // matrix.columnCount() returns 0
        CharMatrix matrix = CharMatrix.empty();
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    // ==================== IntMatrix additional examples ====================

    @Test
    public void testIntMatrixToString() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals("[[1, 2, 3], [4, 5, 6]]", matrix.toString());
    }

    // ==================== LongMatrix Javadoc Examples ====================

    @Test
    public void testLongMatrixEmptyRowCount() {
        LongMatrix matrix = LongMatrix.empty();
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    @Test
    public void testLongMatrixForEach() {
        // LongMatrix.java: matrix.forEach(value -> System.out.print(value + " "));
        // Output: 1 2 3 4
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        List<Long> values = new ArrayList<>();
        matrix.forEach(value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals(1L, values.get(0));
        assertEquals(2L, values.get(1));
        assertEquals(3L, values.get(2));
        assertEquals(4L, values.get(3));
    }

    @Test
    public void testLongMatrixForEachWithRange() {
        // LongMatrix.java: matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
        // Output: 5 6 8 9 (elements from rows 1-2, columns 1-2)
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Long> values = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals(5L, values.get(0));
        assertEquals(6L, values.get(1));
        assertEquals(8L, values.get(2));
        assertEquals(9L, values.get(3));
    }

    // ==================== FloatMatrix Javadoc Examples ====================

    @Test
    public void testFloatMatrixEmptyRowCount() {
        FloatMatrix matrix = FloatMatrix.empty();
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    @Test
    public void testFloatMatrixFromInt() {
        // FloatMatrix.java: FloatMatrix matrix = FloatMatrix.from(new int[][] {{1, 2}, {3, 4}});
        // Creates a matrix with values {{1.0f, 2.0f}, {3.0f, 4.0f}}
        // assert matrix.get(1, 0) == 3.0f;
        FloatMatrix matrix = FloatMatrix.from(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(3.0f, matrix.get(1, 0));
    }

    @Test
    public void testFloatMatrixZipWith2() {
        // FloatMatrix.java: FloatMatrix result = matrix1.zipWith(matrix2, (a, b) -> a * b);
        // result is [[3.0f, 8.0f]]
        FloatMatrix matrix1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix matrix2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
        FloatMatrix result = matrix1.zipWith(matrix2, (a, b) -> a * b);
        assertEquals(3.0f, result.get(0, 0), 0.001f);
        assertEquals(8.0f, result.get(0, 1), 0.001f);
    }

    @Test
    public void testFloatMatrixZipWith3() {
        // FloatMatrix.java: FloatMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> a + b + c);
        // result is [[6.0f]]
        FloatMatrix matrix1 = FloatMatrix.of(new float[][] { { 1.0f } });
        FloatMatrix matrix2 = FloatMatrix.of(new float[][] { { 2.0f } });
        FloatMatrix matrix3 = FloatMatrix.of(new float[][] { { 3.0f } });
        FloatMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> a + b + c);
        assertEquals(6.0f, result.get(0, 0), 0.001f);
    }

    @Test
    public void testFloatMatrixMainDiagonal() {
        // FloatMatrix.java: FloatMatrix matrix = FloatMatrix.mainDiagonal(new float[] {1.0f, 2.0f, 3.0f});
        // Creates 3x3 matrix:
        // [[1.0, 0.0, 0.0],
        //  [0.0, 2.0, 0.0],
        //  [0.0, 0.0, 3.0]]
        FloatMatrix matrix = FloatMatrix.mainDiagonal(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0f, matrix.get(0, 0));
        assertEquals(2.0f, matrix.get(1, 1));
        assertEquals(3.0f, matrix.get(2, 2));
        assertEquals(0.0f, matrix.get(0, 1));
    }

    @Test
    public void testFloatMatrixAntiDiagonal() {
        // FloatMatrix.java: FloatMatrix matrix = FloatMatrix.antiDiagonal(new float[] {1.0f, 2.0f, 3.0f});
        // Creates 3x3 matrix:
        // [[0.0, 0.0, 1.0],
        //  [0.0, 2.0, 0.0],
        //  [3.0, 0.0, 0.0]]
        FloatMatrix matrix = FloatMatrix.antiDiagonal(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(1.0f, matrix.get(0, 2));
        assertEquals(2.0f, matrix.get(1, 1));
        assertEquals(3.0f, matrix.get(2, 0));
        assertEquals(0.0f, matrix.get(0, 0));
    }

    @Test
    public void testFloatMatrixFill() {
        // FloatMatrix.java: FloatMatrix identity = FloatMatrix.of(new float[3][3]);
        // identity.fill(1.0f);
        // Creates a matrix filled with 1.0f: [[1.0f, 1.0f, 1.0f], [1.0f, 1.0f, 1.0f], [1.0f, 1.0f, 1.0f]]
        FloatMatrix identity = FloatMatrix.of(new float[3][3]);
        identity.fill(1.0f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(1.0f, identity.get(i, j));
            }
        }
    }

    // ==================== DoubleMatrix Javadoc Examples ====================

    @Test
    public void testDoubleMatrixEmptyRowCount() {
        DoubleMatrix matrix = DoubleMatrix.empty();
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    @Test
    public void testDoubleMatrixFlipH() {
        // DoubleMatrix.java: DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}});
        // DoubleMatrix flipped = matrix.flippedH();   // returns [[3.0, 2.0, 1.0]], original unchanged
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        DoubleMatrix flipped = matrix.flippedH();
        assertEquals(3.0, flipped.get(0, 0));
        assertEquals(2.0, flipped.get(0, 1));
        assertEquals(1.0, flipped.get(0, 2));
        // Verify original unchanged
        assertEquals(1.0, matrix.get(0, 0));
    }

    @Test
    public void testDoubleMatrixFlipV() {
        // DoubleMatrix.java: DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0}, {2.0}, {3.0}});
        // DoubleMatrix flipped = matrix.flippedV();   // returns [[3.0], [2.0], [1.0]], original unchanged
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0 }, { 2.0 }, { 3.0 } });
        DoubleMatrix flipped = matrix.flippedV();
        assertEquals(3.0, flipped.get(0, 0));
        assertEquals(2.0, flipped.get(1, 0));
        assertEquals(1.0, flipped.get(2, 0));
        // Verify original unchanged
        assertEquals(1.0, matrix.get(0, 0));
    }

    // ==================== BooleanMatrix Javadoc Examples ====================

    @Test
    public void testBooleanMatrixEmptyRowCount() {
        BooleanMatrix matrix = BooleanMatrix.empty();
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    @Test
    public void testBooleanMatrixMainDiagonal() {
        // BooleanMatrix.java: BooleanMatrix matrix = BooleanMatrix.mainDiagonal(new boolean[] {true, false, true});
        BooleanMatrix matrix = BooleanMatrix.mainDiagonal(new boolean[] { true, false, true });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertTrue(matrix.get(0, 0));
        assertEquals(false, matrix.get(1, 1));
        assertTrue(matrix.get(2, 2));
        assertEquals(false, matrix.get(0, 1));
    }

    // ==================== IntMatrix range/rangeClosed examples ====================

    @Test
    public void testIntMatrixRangeClosed() {
        // Similar to range but inclusive end
        IntMatrix matrix = IntMatrix.rangeClosed(1, 5);
        assertEquals("[[1, 2, 3, 4, 5]]", matrix.toString());
    }

    @Test
    public void testIntMatrixRangeClosedWithStep() {
        IntMatrix matrix = IntMatrix.rangeClosed(0, 10, 2);
        assertEquals("[[0, 2, 4, 6, 8, 10]]", matrix.toString());
    }

    // ==================== ShortMatrix range examples ====================

    @Test
    public void testShortMatrixRangeWithStep() {
        // ShortMatrix.java: ShortMatrix matrix = ShortMatrix.range((short) 0, (short) 10, (short) 2);   // Creates [[0, 2, 4, 6, 8]]
        ShortMatrix matrix = ShortMatrix.range((short) 0, (short) 10, (short) 2);
        assertEquals("[[0, 2, 4, 6, 8]]", matrix.toString());
    }

    @Test
    public void testShortMatrixRangeDescending() {
        // ShortMatrix.java: ShortMatrix desc = ShortMatrix.range((short) 10, (short) 0, (short) -2);    // Creates [[10, 8, 6, 4, 2]]
        ShortMatrix desc = ShortMatrix.range((short) 10, (short) 0, (short) -2);
        assertEquals("[[10, 8, 6, 4, 2]]", desc.toString());
    }

    @Test
    public void testShortMatrixRangeClosed() {
        // ShortMatrix.java: ShortMatrix matrix = ShortMatrix.rangeClosed((short) 1, (short) 4);   // Creates [[1, 2, 3, 4]]
        ShortMatrix matrix = ShortMatrix.rangeClosed((short) 1, (short) 4);
        assertEquals("[[1, 2, 3, 4]]", matrix.toString());
    }

    // ==================== LongMatrix range examples ====================

    @Test
    public void testLongMatrixRange() {
        // LongMatrix range examples similar to IntMatrix
        LongMatrix matrix = LongMatrix.range(0, 5);
        assertEquals("[[0, 1, 2, 3, 4]]", matrix.toString());
    }

    @Test
    public void testLongMatrixRangeWithStep() {
        LongMatrix matrix = LongMatrix.range(0, 10, 2);
        assertEquals("[[0, 2, 4, 6, 8]]", matrix.toString());
    }

    // ==================== IntMatrix diagonal examples ====================

    @Test
    public void testIntMatrixMainDiagonal() {
        // IntMatrix mainDiagonal
        IntMatrix matrix = IntMatrix.mainDiagonal(new int[] { 1, 2, 3 });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(1, 1));
        assertEquals(3, matrix.get(2, 2));
        assertEquals(0, matrix.get(0, 1));
        assertEquals(0, matrix.get(1, 0));
    }

    @Test
    public void testIntMatrixAntiDiagonal() {
        IntMatrix matrix = IntMatrix.antiDiagonal(new int[] { 1, 2, 3 });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1, matrix.get(0, 2));
        assertEquals(2, matrix.get(1, 1));
        assertEquals(3, matrix.get(2, 0));
        assertEquals(0, matrix.get(0, 0));
    }

    // ==================== DoubleMatrix diagonal examples ====================

    @Test
    public void testDoubleMatrixMainDiagonal() {
        DoubleMatrix matrix = DoubleMatrix.mainDiagonal(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(1, 1));
        assertEquals(3.0, matrix.get(2, 2));
        assertEquals(0.0, matrix.get(0, 1));
    }

    // ==================== AbstractMatrix streamR/streamC examples ====================

    @Test
    public void testAbstractMatrixStreamRRowSums() {
        // AbstractMatrix.java: IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
        // Row sums: 6 and 15
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Integer> rowSums = new ArrayList<>();
        matrix.streamR().forEach(rowStream -> {
            int sum = rowStream.sum();
            rowSums.add(sum);
        });
        assertEquals(2, rowSums.size());
        assertEquals(6, rowSums.get(0));
        assertEquals(15, rowSums.get(1));
    }

    @Test
    public void testAbstractMatrixStreamCColumnAverages() {
        // AbstractMatrix.java: DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        // Column averages: 2.0 and 3.0
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        List<Double> colAvgs = new ArrayList<>();
        matrix.streamC().forEach(colStream -> {
            double avg = colStream.average().orElse(0);
            colAvgs.add(avg);
        });
        assertEquals(2, colAvgs.size());
        assertEquals(2.0, colAvgs.get(0), 0.001);
        assertEquals(3.0, colAvgs.get(1), 0.001);
    }

    // ==================== IntMatrix forEach examples ====================

    @Test
    public void testIntMatrixForEach() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> values = new ArrayList<>();
        matrix.forEach(value -> values.add(value));
        assertEquals(4, values.size());
        assertEquals(1, values.get(0));
        assertEquals(2, values.get(1));
        assertEquals(3, values.get(2));
        assertEquals(4, values.get(3));
    }

    // ==================== DoubleMatrix toString example ====================

    @Test
    public void testDoubleMatrixToString() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        assertEquals("[[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]", matrix.toString());
    }

    // ==================== FloatMatrix toString example ====================

    @Test
    public void testFloatMatrixToString() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        assertEquals("[[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]", matrix.toString());
    }

    // ==================== LongMatrix toString example ====================

    @Test
    public void testLongMatrixToString() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals("[[1, 2, 3], [4, 5, 6]]", matrix.toString());
    }

    // ==================== Matrix (generic) Javadoc Examples ====================

    @Test
    public void testMatrixOfGet() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(Integer.valueOf(1), matrix.get(0, 0));
        assertEquals(Integer.valueOf(6), matrix.get(1, 2));
    }

    @Test
    public void testMatrixEmptyRowCount() {
        Matrix<String> matrix = new Matrix<>(new String[0][0]);
        assertEquals(0, matrix.rowCount());
        assertEquals(0, matrix.columnCount());
    }

    // ==================== CharMatrix range examples ====================

    @Test
    public void testCharMatrixOfGet() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        assertEquals('a', matrix.get(0, 0));
        assertEquals('f', matrix.get(1, 2));
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
    }

    // ==================== IntMatrix fill example ====================

    @Test
    public void testIntMatrixFill() {
        IntMatrix matrix = IntMatrix.of(new int[2][3]);
        matrix.fill(7);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(7, matrix.get(i, j));
            }
        }
    }

    // ==================== BooleanMatrix of/get ====================

    @Test
    public void testBooleanMatrixOfGet() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertTrue(matrix.get(0, 0));
        assertEquals(false, matrix.get(0, 1));
        assertEquals(false, matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
    }
}
