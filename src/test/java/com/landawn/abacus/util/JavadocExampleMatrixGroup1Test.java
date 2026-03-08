package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests that verify Javadoc examples in BooleanMatrix, ByteMatrix, CharMatrix, and ShortMatrix.
 */
public class JavadocExampleMatrixGroup1Test {

    // ==================== BooleanMatrix ====================

    @Test
    public void testBooleanMatrix_repeat() {
        BooleanMatrix matrix = BooleanMatrix.repeat(2, 3, true);
        // Result: [[true, true, true], [true, true, true]]
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(matrix.get(i, j));
            }
        }
    }

    @Test
    public void testBooleanMatrix_rowView() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false } });
        boolean[] firstRow = matrix.rowView(0);
        assertArrayEquals(new boolean[] { true, false, false }, firstRow);
    }

    @Test
    public void testBooleanMatrix_columnCopy() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false } });
        boolean[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new boolean[] { true, false }, firstColumn);
    }

    @Test
    public void testBooleanMatrix_getMainDiagonal() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
        boolean[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new boolean[] { true, true, true }, diagonal);
    }

    @Test
    public void testBooleanMatrix_getAntiDiagonal() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
        boolean[] antiDiag = matrix.getAntiDiagonal();
        assertArrayEquals(new boolean[] { true, true, true }, antiDiag);
    }

    @Test
    public void testBooleanMatrix_copy_rows() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        BooleanMatrix subset = matrix.copy(1, 3);
        // Result: [[false, true], [true, true]]
        assertEquals(2, subset.rowCount());
        assertEquals(2, subset.columnCount());
        assertFalse(subset.get(0, 0));
        assertTrue(subset.get(0, 1));
        assertTrue(subset.get(1, 0));
        assertTrue(subset.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_copy_region() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, true, false }, { false, true, false, true }, { true, true, false, false } });
        BooleanMatrix subMatrix = matrix.copy(0, 2, 1, 3);
        // Result: [[false, true], [true, false]]
        assertEquals(2, subMatrix.rowCount());
        assertEquals(2, subMatrix.columnCount());
        assertFalse(subMatrix.get(0, 0));
        assertTrue(subMatrix.get(0, 1));
        assertTrue(subMatrix.get(1, 0));
        assertFalse(subMatrix.get(1, 1));

        // Extract a single column as a matrix
        BooleanMatrix col = matrix.copy(0, 3, 2, 3);
        // Result: [[true], [false], [false]]
        assertEquals(3, col.rowCount());
        assertEquals(1, col.columnCount());
        assertTrue(col.get(0, 0));
        assertFalse(col.get(1, 0));
        assertFalse(col.get(2, 0));
    }

    @Test
    public void testBooleanMatrix_resize_default() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = matrix.resize(3, 3);
        // Result: [[true, false, false], [false, true, false], [false, false, false]]
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertTrue(extended.get(0, 0));
        assertFalse(extended.get(0, 1));
        assertFalse(extended.get(0, 2));
        assertFalse(extended.get(1, 0));
        assertTrue(extended.get(1, 1));
        assertFalse(extended.get(1, 2));
        assertFalse(extended.get(2, 0));
        assertFalse(extended.get(2, 1));
        assertFalse(extended.get(2, 2));
    }

    @Test
    public void testBooleanMatrix_resize_withFill() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = matrix.resize(3, 4, true);
        // Result: [[true, false, true, true], [false, true, true, true], [true, true, true, true]]
        assertEquals(3, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertTrue(extended.get(0, 0));
        assertFalse(extended.get(0, 1));
        assertTrue(extended.get(0, 2));
        assertTrue(extended.get(0, 3));
        assertFalse(extended.get(1, 0));
        assertTrue(extended.get(1, 1));
        assertTrue(extended.get(1, 2));
        assertTrue(extended.get(1, 3));
        assertTrue(extended.get(2, 0));
        assertTrue(extended.get(2, 1));
        assertTrue(extended.get(2, 2));
        assertTrue(extended.get(2, 3));

        // Truncate to smaller size
        BooleanMatrix truncated = matrix.resize(1, 1, false);
        assertEquals(1, truncated.rowCount());
        assertEquals(1, truncated.columnCount());
        assertTrue(truncated.get(0, 0));
    }

    @Test
    public void testBooleanMatrix_extend_default() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, true } });
        BooleanMatrix extended = matrix.extend(1, 1, 1, 1);
        // Result: [[false, false, false, false], [false, true, true, false], [false, false, false, false]]
        assertEquals(3, extended.rowCount());
        assertEquals(4, extended.columnCount());
        for (int j = 0; j < 4; j++) {
            assertFalse(extended.get(0, j));
            assertFalse(extended.get(2, j));
        }
        assertFalse(extended.get(1, 0));
        assertTrue(extended.get(1, 1));
        assertTrue(extended.get(1, 2));
        assertFalse(extended.get(1, 3));
    }

    @Test
    public void testBooleanMatrix_extend_withFill() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, true } });
        BooleanMatrix padded = matrix.extend(1, 1, 2, 2, true);
        // Result: 3x6, all true
        assertEquals(3, padded.rowCount());
        assertEquals(6, padded.columnCount());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                assertTrue(padded.get(i, j));
            }
        }

        // Add border of false values
        BooleanMatrix bordered = matrix.extend(1, 1, 1, 1, false);
        assertEquals(3, bordered.rowCount());
        assertEquals(4, bordered.columnCount());
        for (int j = 0; j < 4; j++) {
            assertFalse(bordered.get(0, j));
            assertFalse(bordered.get(2, j));
        }
        assertFalse(bordered.get(1, 0));
        assertTrue(bordered.get(1, 1));
        assertTrue(bordered.get(1, 2));
        assertFalse(bordered.get(1, 3));
    }

    @Test
    public void testBooleanMatrix_flipInPlaceHorizontally() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, true, false }, { false, true, true } });
        matrix.flipInPlaceHorizontally();
        // matrix is now [[false, true, true], [true, true, false]]
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(0, 2));
        assertTrue(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
        assertFalse(matrix.get(1, 2));
    }

    @Test
    public void testBooleanMatrix_flipInPlaceVertically() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true }, { false, true } });
        matrix.flipInPlaceVertically();
        // matrix is now [[false, true], [true, true], [true, false]]
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
        assertTrue(matrix.get(2, 0));
        assertFalse(matrix.get(2, 1));
    }

    @Test
    public void testBooleanMatrix_reshape() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, true, false } });
        BooleanMatrix reshaped = matrix.reshape(2, 2);
        // Result: [[true, false], [true, false]]
        assertEquals(2, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertTrue(reshaped.get(0, 0));
        assertFalse(reshaped.get(0, 1));
        assertTrue(reshaped.get(1, 0));
        assertFalse(reshaped.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_and() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { false, true } });
        BooleanMatrix result = a.and(b);
        // Result: [[true, false], [false, true]]
        assertTrue(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_or() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { false, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false, true }, { false, true } });
        BooleanMatrix result = a.or(b);
        // Result: [[true, true], [false, true]]
        assertTrue(result.get(0, 0));
        assertTrue(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_xor() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { false, true } });
        BooleanMatrix result = a.xor(b);
        // Result: [[false, true], [true, false]]
        assertFalse(result.get(0, 0));
        assertTrue(result.get(0, 1));
        assertTrue(result.get(1, 0));
        assertFalse(result.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_stackVertically() {
        BooleanMatrix top = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix bottom = BooleanMatrix.of(new boolean[][] { { false, true } });
        BooleanMatrix stacked = top.stackVertically(bottom);
        // Result: [[true, false], [false, true]]
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(0, 1));
        assertFalse(stacked.get(1, 0));
        assertTrue(stacked.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_stackHorizontally() {
        BooleanMatrix left = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        BooleanMatrix right = BooleanMatrix.of(new boolean[][] { { false }, { true } });
        BooleanMatrix stacked = left.stackHorizontally(right);
        // Result: [[true, false], [false, true]]
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(0, 1));
        assertFalse(stacked.get(1, 0));
        assertTrue(stacked.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_zipWith() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { true, true } });

        // Element-wise AND
        BooleanMatrix and = a.zipWith(b, (x, y) -> x && y);
        // Result: [[true, false], [false, true]]
        assertTrue(and.get(0, 0));
        assertFalse(and.get(0, 1));
        assertFalse(and.get(1, 0));
        assertTrue(and.get(1, 1));

        // Element-wise OR
        BooleanMatrix or = a.zipWith(b, (x, y) -> x || y);
        // Result: [[true, true], [true, true]]
        assertTrue(or.get(0, 0));
        assertTrue(or.get(0, 1));
        assertTrue(or.get(1, 0));
        assertTrue(or.get(1, 1));

        // Element-wise XOR
        BooleanMatrix xor = a.zipWith(b, (x, y) -> x ^ y);
        // Result: [[false, true], [true, false]]
        assertFalse(xor.get(0, 0));
        assertTrue(xor.get(0, 1));
        assertTrue(xor.get(1, 0));
        assertFalse(xor.get(1, 1));
    }

    @Test
    public void testBooleanMatrix_applyOnFlattened() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.fill(arr, true));
        // matrix is now [[true, true], [true, true]]
        assertTrue(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
    }

    // ==================== ByteMatrix ====================

    @Test
    public void testByteMatrix_repeat() {
        ByteMatrix matrix = ByteMatrix.repeat(2, 3, (byte) 1);
        // Result: [[1, 1, 1], [1, 1, 1]]
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals((byte) 1, matrix.get(i, j));
            }
        }
    }

    @Test
    public void testByteMatrix_rowView() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] firstRow = matrix.rowView(0);
        assertArrayEquals(new byte[] { 1, 2, 3 }, firstRow);
    }

    @Test
    public void testByteMatrix_columnCopy() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new byte[] { 1, 4 }, firstColumn);
    }

    @Test
    public void testByteMatrix_getMainDiagonal() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testByteMatrix_getAntiDiagonal() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new byte[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testByteMatrix_resize_default() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = matrix.resize(3, 3);
        // Result: [[1, 2, 0], [3, 4, 0], [0, 0, 0]]
        assertEquals((byte) 1, extended.get(0, 0));
        assertEquals((byte) 2, extended.get(0, 1));
        assertEquals((byte) 0, extended.get(0, 2));
        assertEquals((byte) 3, extended.get(1, 0));
        assertEquals((byte) 4, extended.get(1, 1));
        assertEquals((byte) 0, extended.get(1, 2));
        assertEquals((byte) 0, extended.get(2, 0));
        assertEquals((byte) 0, extended.get(2, 1));
        assertEquals((byte) 0, extended.get(2, 2));
    }

    @Test
    public void testByteMatrix_resize_withFill() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = matrix.resize(3, 4, (byte) 9);
        // Result: [[1, 2, 9, 9], [3, 4, 9, 9], [9, 9, 9, 9]]
        assertEquals((byte) 1, extended.get(0, 0));
        assertEquals((byte) 2, extended.get(0, 1));
        assertEquals((byte) 9, extended.get(0, 2));
        assertEquals((byte) 9, extended.get(0, 3));
        assertEquals((byte) 3, extended.get(1, 0));
        assertEquals((byte) 4, extended.get(1, 1));
        assertEquals((byte) 9, extended.get(1, 2));
        assertEquals((byte) 9, extended.get(1, 3));
        for (int j = 0; j < 4; j++) {
            assertEquals((byte) 9, extended.get(2, j));
        }

        // Truncate
        ByteMatrix truncated = matrix.resize(1, 1, (byte) 0);
        assertEquals((byte) 1, truncated.get(0, 0));
    }

    @Test
    public void testByteMatrix_extend_default() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = matrix.extend(1, 1, 1, 1);
        // Result: [[0, 0, 0, 0], [0, 1, 2, 0], [0, 0, 0, 0]]
        assertEquals(3, extended.rowCount());
        assertEquals(4, extended.columnCount());
        for (int j = 0; j < 4; j++) {
            assertEquals((byte) 0, extended.get(0, j));
            assertEquals((byte) 0, extended.get(2, j));
        }
        assertEquals((byte) 0, extended.get(1, 0));
        assertEquals((byte) 1, extended.get(1, 1));
        assertEquals((byte) 2, extended.get(1, 2));
        assertEquals((byte) 0, extended.get(1, 3));
    }

    @Test
    public void testByteMatrix_extend_withFill() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix padded = matrix.extend(1, 1, 2, 2, (byte) 9);
        // Result: [[9, 9, 9, 9, 9, 9], [9, 9, 1, 2, 9, 9], [9, 9, 9, 9, 9, 9]]
        assertEquals(3, padded.rowCount());
        assertEquals(6, padded.columnCount());
        assertEquals((byte) 9, padded.get(0, 0));
        assertEquals((byte) 9, padded.get(1, 0));
        assertEquals((byte) 9, padded.get(1, 1));
        assertEquals((byte) 1, padded.get(1, 2));
        assertEquals((byte) 2, padded.get(1, 3));
        assertEquals((byte) 9, padded.get(1, 4));
        assertEquals((byte) 9, padded.get(1, 5));
    }

    @Test
    public void testByteMatrix_flipInPlaceHorizontally() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        matrix.flipInPlaceHorizontally();
        // matrix is now: [[3, 2, 1], [6, 5, 4]]
        assertArrayEquals(new byte[] { 3, 2, 1 }, matrix.rowView(0));
        assertArrayEquals(new byte[] { 6, 5, 4 }, matrix.rowView(1));
    }

    @Test
    public void testByteMatrix_flipInPlaceVertically() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        matrix.flipInPlaceVertically();
        // matrix is now: [[5, 6], [3, 4], [1, 2]]
        assertArrayEquals(new byte[] { 5, 6 }, matrix.rowView(0));
        assertArrayEquals(new byte[] { 3, 4 }, matrix.rowView(1));
        assertArrayEquals(new byte[] { 1, 2 }, matrix.rowView(2));
    }

    @Test
    public void testByteMatrix_flatten() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteList list = matrix.flatten();
        // Returns ByteList containing [1, 2, 3, 4]
        assertEquals(4, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
    }

    @Test
    public void testByteMatrix_applyOnFlattened() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 5, 3 }, { 4, 1 } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        // matrix is now [[1, 3], [4, 5]]
        assertEquals((byte) 1, matrix.get(0, 0));
        assertEquals((byte) 3, matrix.get(0, 1));
        assertEquals((byte) 4, matrix.get(1, 0));
        assertEquals((byte) 5, matrix.get(1, 1));
    }

    @Test
    public void testByteMatrix_toString() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals("[[1, 2], [3, 4]]", matrix.toString());

        ByteMatrix empty = ByteMatrix.empty();
        assertEquals("[]", empty.toString());
    }

    // ==================== CharMatrix ====================

    @Test
    public void testCharMatrix_repeat() {
        CharMatrix matrix = CharMatrix.repeat(2, 3, 'a');
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals('a', matrix.get(i, j));
            }
        }
    }

    @Test
    public void testCharMatrix_rowView() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        char[] firstRow = matrix.rowView(0);
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, firstRow);
    }

    @Test
    public void testCharMatrix_columnCopy() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        char[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new char[] { 'a', 'd' }, firstColumn);
    }

    @Test
    public void testCharMatrix_getMainDiagonal() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new char[] { 'a', 'e', 'i' }, diagonal);
    }

    @Test
    public void testCharMatrix_getAntiDiagonal() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new char[] { 'c', 'e', 'g' }, diagonal);
    }

    @Test
    public void testCharMatrix_updateRow() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        matrix.updateRow(0, c -> Character.toUpperCase(c));
        // matrix is now [['A', 'B', 'C'], ['d', 'e', 'f']]
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, matrix.rowView(0));
        assertArrayEquals(new char[] { 'd', 'e', 'f' }, matrix.rowView(1));
    }

    @Test
    public void testCharMatrix_updateColumn() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        matrix.updateColumn(1, c -> Character.toLowerCase(c));
        // matrix is now [['A', 'b'], ['C', 'd']]
        assertEquals('A', matrix.get(0, 0));
        assertEquals('b', matrix.get(0, 1));
        assertEquals('C', matrix.get(1, 0));
        assertEquals('d', matrix.get(1, 1));
    }

    @Test
    public void testCharMatrix_updateAll() {
        CharMatrix matrix = CharMatrix.of(new char[3][4]);
        matrix.updateAll((i, j) -> (char) ('a' + i * 4 + j));
        // Result: [['a', 'b', 'c', 'd'], ['e', 'f', 'g', 'h'], ['i', 'j', 'k', 'l']]
        assertEquals('a', matrix.get(0, 0));
        assertEquals('d', matrix.get(0, 3));
        assertEquals('e', matrix.get(1, 0));
        assertEquals('h', matrix.get(1, 3));
        assertEquals('i', matrix.get(2, 0));
        assertEquals('l', matrix.get(2, 3));
    }

    @Test
    public void testCharMatrix_copyFrom() {
        CharMatrix matrix = CharMatrix.of(new char[3][3]);
        matrix.copyFrom(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        // Result: [[0, 0, 0], [0, 'a', 'b'], [0, 'c', 'd']]
        assertEquals('\0', matrix.get(0, 0));
        assertEquals('\0', matrix.get(1, 0));
        assertEquals('a', matrix.get(1, 1));
        assertEquals('b', matrix.get(1, 2));
        assertEquals('c', matrix.get(2, 1));
        assertEquals('d', matrix.get(2, 2));
    }

    @Test
    public void testCharMatrix_copy_rows() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharMatrix copy = matrix.copy(1, 3);
        // Returns [['c', 'd'], ['e', 'f']]
        assertEquals(2, copy.rowCount());
        assertEquals('c', copy.get(0, 0));
        assertEquals('d', copy.get(0, 1));
        assertEquals('e', copy.get(1, 0));
        assertEquals('f', copy.get(1, 1));
    }

    @Test
    public void testCharMatrix_copy_region() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        CharMatrix sub = matrix.copy(0, 2, 1, 3);
        // Result: [['b', 'c'], ['e', 'f']]
        assertEquals(2, sub.rowCount());
        assertEquals(2, sub.columnCount());
        assertEquals('b', sub.get(0, 0));
        assertEquals('c', sub.get(0, 1));
        assertEquals('e', sub.get(1, 0));
        assertEquals('f', sub.get(1, 1));
    }

    @Test
    public void testCharMatrix_resize_withFill() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix extended = matrix.resize(3, 4, 'x');
        // Result: [['a', 'b', 'x', 'x'], ['c', 'd', 'x', 'x'], ['x', 'x', 'x', 'x']]
        assertEquals('a', extended.get(0, 0));
        assertEquals('b', extended.get(0, 1));
        assertEquals('x', extended.get(0, 2));
        assertEquals('x', extended.get(0, 3));
        assertEquals('c', extended.get(1, 0));
        assertEquals('d', extended.get(1, 1));
        assertEquals('x', extended.get(1, 2));
        assertEquals('x', extended.get(2, 0));

        // Truncate
        CharMatrix truncated = matrix.resize(1, 1, '\u0000');
        assertEquals('a', truncated.get(0, 0));
    }

    @Test
    public void testCharMatrix_extend_withFill() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix padded = matrix.extend(1, 1, 2, 2, 'x');
        // Result: 3x6, border x, middle has a,b
        assertEquals(3, padded.rowCount());
        assertEquals(6, padded.columnCount());
        assertEquals('x', padded.get(0, 0));
        assertEquals('x', padded.get(1, 0));
        assertEquals('x', padded.get(1, 1));
        assertEquals('a', padded.get(1, 2));
        assertEquals('b', padded.get(1, 3));
        assertEquals('x', padded.get(1, 4));
        assertEquals('x', padded.get(1, 5));
        assertEquals('x', padded.get(2, 0));
    }

    @Test
    public void testCharMatrix_flipInPlaceHorizontally() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        matrix.flipInPlaceHorizontally();
        // matrix is now [['c', 'b', 'a'], ['f', 'e', 'd']]
        assertArrayEquals(new char[] { 'c', 'b', 'a' }, matrix.rowView(0));
        assertArrayEquals(new char[] { 'f', 'e', 'd' }, matrix.rowView(1));
    }

    @Test
    public void testCharMatrix_flipInPlaceVertically() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        matrix.flipInPlaceVertically();
        // matrix is now [['e', 'f'], ['c', 'd'], ['a', 'b']]
        assertArrayEquals(new char[] { 'e', 'f' }, matrix.rowView(0));
        assertArrayEquals(new char[] { 'c', 'd' }, matrix.rowView(1));
        assertArrayEquals(new char[] { 'a', 'b' }, matrix.rowView(2));
    }

    @Test
    public void testCharMatrix_flipHorizontally() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        CharMatrix flipped = matrix.flipHorizontally();
        assertArrayEquals(new char[] { 'c', 'b', 'a' }, flipped.rowView(0));
        // original unchanged
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, matrix.rowView(0));
    }

    @Test
    public void testCharMatrix_flipVertically() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharMatrix flipped = matrix.flipVertically();
        // Returns [['e', 'f'], ['c', 'd'], ['a', 'b']]
        assertArrayEquals(new char[] { 'e', 'f' }, flipped.rowView(0));
        assertArrayEquals(new char[] { 'c', 'd' }, flipped.rowView(1));
        assertArrayEquals(new char[] { 'a', 'b' }, flipped.rowView(2));
    }

    @Test
    public void testCharMatrix_reshape() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        CharMatrix reshaped1 = matrix.reshape(3, 2);
        // Result: [['a', 'b'], ['c', 'd'], ['e', 'f']]
        assertEquals('a', reshaped1.get(0, 0));
        assertEquals('b', reshaped1.get(0, 1));
        assertEquals('c', reshaped1.get(1, 0));
        assertEquals('d', reshaped1.get(1, 1));
        assertEquals('e', reshaped1.get(2, 0));
        assertEquals('f', reshaped1.get(2, 1));

        CharMatrix reshaped2 = matrix.reshape(2, 4);
        // Result: [['a', 'b', 'c', 'd'], ['e', 'f', '\u0000', '\u0000']]
        assertEquals('a', reshaped2.get(0, 0));
        assertEquals('d', reshaped2.get(0, 3));
        assertEquals('e', reshaped2.get(1, 0));
        assertEquals('f', reshaped2.get(1, 1));
        assertEquals('\u0000', reshaped2.get(1, 2));
        assertEquals('\u0000', reshaped2.get(1, 3));
    }

    @Test
    public void testCharMatrix_repeatElements() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix repeated = matrix.repeatElements(2, 3);
        // Result: [['a', 'a', 'a', 'b', 'b', 'b'], ['a', 'a', 'a', 'b', 'b', 'b']]
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals('a', repeated.get(0, 0));
        assertEquals('a', repeated.get(0, 1));
        assertEquals('a', repeated.get(0, 2));
        assertEquals('b', repeated.get(0, 3));
        assertEquals('b', repeated.get(0, 4));
        assertEquals('b', repeated.get(0, 5));
        assertEquals('a', repeated.get(1, 0));
    }

    @Test
    public void testCharMatrix_repeatMatrix() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix repeated = matrix.repeatMatrix(2, 3);
        // Result: [['a', 'b', 'a', 'b', 'a', 'b'], ['c', 'd', 'c', 'd', 'c', 'd'],
        //          ['a', 'b', 'a', 'b', 'a', 'b'], ['c', 'd', 'c', 'd', 'c', 'd']]
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals('a', repeated.get(0, 0));
        assertEquals('b', repeated.get(0, 1));
        assertEquals('a', repeated.get(0, 2));
        assertEquals('d', repeated.get(1, 1));
        assertEquals('a', repeated.get(2, 0));
        assertEquals('d', repeated.get(3, 5));
    }

    @Test
    public void testCharMatrix_flatten() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharList list = matrix.flatten();
        assertEquals(6, list.size());
        assertEquals('a', list.get(0));
        assertEquals('b', list.get(1));
        assertEquals('c', list.get(2));
        assertEquals('d', list.get(3));
        assertEquals('e', list.get(4));
        assertEquals('f', list.get(5));
    }

    @Test
    public void testCharMatrix_applyOnFlattened() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'd', 'b' }, { 'c', 'a' } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        // matrix is now [['a', 'b'], ['c', 'd']]
        assertEquals('a', matrix.get(0, 0));
        assertEquals('b', matrix.get(0, 1));
        assertEquals('c', matrix.get(1, 0));
        assertEquals('d', matrix.get(1, 1));
    }

    @Test
    public void testCharMatrix_stackVertically() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 'c', 'd' } });
        CharMatrix stacked = a.stackVertically(b);
        // Result: [['a', 'b'], ['c', 'd']]
        assertEquals('a', stacked.get(0, 0));
        assertEquals('b', stacked.get(0, 1));
        assertEquals('c', stacked.get(1, 0));
        assertEquals('d', stacked.get(1, 1));
    }

    @Test
    public void testCharMatrix_stackHorizontally() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'a' }, { 'b' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 'c' }, { 'd' } });
        CharMatrix stacked = a.stackHorizontally(b);
        // Result: [['a', 'c'], ['b', 'd']]
        assertEquals('a', stacked.get(0, 0));
        assertEquals('c', stacked.get(0, 1));
        assertEquals('b', stacked.get(1, 0));
        assertEquals('d', stacked.get(1, 1));
    }

    @Test
    public void testCharMatrix_add() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 1, 2 } });
        CharMatrix sum = a.add(b);
        // Result: [['b', 'd']] (a+1=98, b+2=100)
        assertEquals('b', sum.get(0, 0));
        assertEquals('d', sum.get(0, 1));
    }

    @Test
    public void testCharMatrix_subtract() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'd', 'e' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 1, 2 } });
        CharMatrix diff = a.subtract(b);
        // Result: [['c', 'c']] (d-1=99, e-2=99)
        assertEquals('c', diff.get(0, 0));
        assertEquals('c', diff.get(0, 1));
    }

    @Test
    public void testCharMatrix_toIntMatrix() {
        CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
        IntMatrix intMatrix = charMatrix.toIntMatrix();
        // Result: [[97, 98]]
        assertEquals(97, intMatrix.get(0, 0));
        assertEquals(98, intMatrix.get(0, 1));
    }

    @Test
    public void testCharMatrix_toLongMatrix() {
        CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
        LongMatrix longMatrix = charMatrix.toLongMatrix();
        // Result: [[97L, 98L]]
        assertEquals(97L, longMatrix.get(0, 0));
        assertEquals(98L, longMatrix.get(0, 1));
    }

    @Test
    public void testCharMatrix_toFloatMatrix() {
        CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
        FloatMatrix floatMatrix = charMatrix.toFloatMatrix();
        // Result: [[97.0f, 98.0f]]
        assertEquals(97.0f, floatMatrix.get(0, 0));
        assertEquals(98.0f, floatMatrix.get(0, 1));
    }

    @Test
    public void testCharMatrix_toDoubleMatrix() {
        CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
        DoubleMatrix doubleMatrix = charMatrix.toDoubleMatrix();
        // Result: [[97.0, 98.0]]
        assertEquals(97.0, doubleMatrix.get(0, 0));
        assertEquals(98.0, doubleMatrix.get(0, 1));
    }

    @Test
    public void testCharMatrix_zipWith() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 'A', 'B' } });
        CharMatrix result = a.zipWith(b, (x, y) -> (char) Math.max(x, y));
        // Result: [['a', 'b']] (max of each pair)
        assertEquals('a', result.get(0, 0));
        assertEquals('b', result.get(0, 1));
    }

    @Test
    public void testCharMatrix_zipWith3() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 'c', 'd' } });
        CharMatrix c = CharMatrix.of(new char[][] { { 'e', 'f' } });
        CharMatrix result = a.zipWith(b, c, (x, y, z) -> (char) Math.max(Math.max(x, y), z));
        // Result: [['e', 'f']] (max of each triple)
        assertEquals('e', result.get(0, 0));
        assertEquals('f', result.get(0, 1));
    }

    // ==================== ShortMatrix ====================

    @Test
    public void testShortMatrix_repeat() {
        ShortMatrix matrix = ShortMatrix.repeat(2, 3, (short) 1);
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals((short) 1, matrix.get(i, j));
            }
        }
    }

    @Test
    public void testShortMatrix_rowView() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] firstRow = matrix.rowView(0);
        assertArrayEquals(new short[] { 1, 2, 3 }, firstRow);
    }

    @Test
    public void testShortMatrix_columnCopy() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new short[] { 1, 4 }, firstColumn);
    }

    @Test
    public void testShortMatrix_getMainDiagonal() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testShortMatrix_getAntiDiagonal() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new short[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testShortMatrix_replaceIf() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        matrix.replaceIf(x -> x > 3, (short) 0);
        // Result: [[1, 2, 3], [0, 0, 0]]
        assertEquals((short) 1, matrix.get(0, 0));
        assertEquals((short) 2, matrix.get(0, 1));
        assertEquals((short) 3, matrix.get(0, 2));
        assertEquals((short) 0, matrix.get(1, 0));
        assertEquals((short) 0, matrix.get(1, 1));
        assertEquals((short) 0, matrix.get(1, 2));
    }

    @Test
    public void testShortMatrix_map() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix squared = matrix.map(x -> (short) (x * x));
        // Result: [[1, 4], [9, 16]]
        assertEquals((short) 1, squared.get(0, 0));
        assertEquals((short) 4, squared.get(0, 1));
        assertEquals((short) 9, squared.get(1, 0));
        assertEquals((short) 16, squared.get(1, 1));
    }

    @Test
    public void testShortMatrix_fill() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        matrix.fill((short) 5);
        // Result: [[5, 5], [5, 5]]
        assertEquals((short) 5, matrix.get(0, 0));
        assertEquals((short) 5, matrix.get(0, 1));
        assertEquals((short) 5, matrix.get(1, 0));
        assertEquals((short) 5, matrix.get(1, 1));
    }

    @Test
    public void testShortMatrix_copyFrom() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 } });
        matrix.copyFrom(new short[][] { { 1, 2 }, { 3, 4 } });
        // Result: [[1, 2, 0], [3, 4, 0]]
        assertEquals((short) 1, matrix.get(0, 0));
        assertEquals((short) 2, matrix.get(0, 1));
        assertEquals((short) 0, matrix.get(0, 2));
        assertEquals((short) 3, matrix.get(1, 0));
        assertEquals((short) 4, matrix.get(1, 1));
        assertEquals((short) 0, matrix.get(1, 2));
    }

    @Test
    public void testShortMatrix_copyFrom_offset() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        matrix.copyFrom(1, 1, new short[][] { { 1, 2 }, { 3, 4 } });
        // Result: [[0, 0, 0], [0, 1, 2], [0, 3, 4]]
        assertEquals((short) 0, matrix.get(0, 0));
        assertEquals((short) 0, matrix.get(1, 0));
        assertEquals((short) 1, matrix.get(1, 1));
        assertEquals((short) 2, matrix.get(1, 2));
        assertEquals((short) 3, matrix.get(2, 1));
        assertEquals((short) 4, matrix.get(2, 2));
    }

    @Test
    public void testShortMatrix_copy_rows() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ShortMatrix subset = matrix.copy(1, 3);
        // Returns [[3, 4], [5, 6]]
        assertEquals(2, subset.rowCount());
        assertEquals((short) 3, subset.get(0, 0));
        assertEquals((short) 4, subset.get(0, 1));
        assertEquals((short) 5, subset.get(1, 0));
        assertEquals((short) 6, subset.get(1, 1));
    }

    @Test
    public void testShortMatrix_copy_region() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix region = matrix.copy(0, 2, 1, 3);
        // Returns [[2, 3], [5, 6]]
        assertEquals((short) 2, region.get(0, 0));
        assertEquals((short) 3, region.get(0, 1));
        assertEquals((short) 5, region.get(1, 0));
        assertEquals((short) 6, region.get(1, 1));
    }

    @Test
    public void testShortMatrix_resize_default() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = matrix.resize(3, 3);
        // Result: [[1, 2, 0], [3, 4, 0], [0, 0, 0]]
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) 2, extended.get(0, 1));
        assertEquals((short) 0, extended.get(0, 2));
        assertEquals((short) 3, extended.get(1, 0));
        assertEquals((short) 4, extended.get(1, 1));
        assertEquals((short) 0, extended.get(1, 2));
        assertEquals((short) 0, extended.get(2, 0));
    }

    @Test
    public void testShortMatrix_resize_withFill() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = matrix.resize(3, 4, (short) 9);
        // Result: [[1, 2, 9, 9], [3, 4, 9, 9], [9, 9, 9, 9]]
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) 9, extended.get(0, 2));
        assertEquals((short) 9, extended.get(2, 0));

        ShortMatrix truncated = matrix.resize(1, 1, (short) 0);
        assertEquals((short) 1, truncated.get(0, 0));
    }

    @Test
    public void testShortMatrix_extend_default() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix extended = matrix.extend(1, 1, 1, 1);
        // Result: [[0, 0, 0, 0], [0, 1, 2, 0], [0, 0, 0, 0]]
        assertEquals(3, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals((short) 0, extended.get(0, 0));
        assertEquals((short) 1, extended.get(1, 1));
        assertEquals((short) 2, extended.get(1, 2));
        assertEquals((short) 0, extended.get(1, 3));
    }

    @Test
    public void testShortMatrix_extend_withFill() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix padded = matrix.extend(1, 1, 2, 2, (short) 9);
        // Result: [[9, 9, 9, 9, 9, 9], [9, 9, 1, 2, 9, 9], [9, 9, 9, 9, 9, 9]]
        assertEquals(3, padded.rowCount());
        assertEquals(6, padded.columnCount());
        assertEquals((short) 9, padded.get(0, 0));
        assertEquals((short) 1, padded.get(1, 2));
        assertEquals((short) 2, padded.get(1, 3));
        assertEquals((short) 9, padded.get(1, 4));
    }

    @Test
    public void testShortMatrix_flipInPlaceHorizontally() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        matrix.flipInPlaceHorizontally();
        // matrix is now [[3, 2, 1], [6, 5, 4]]
        assertArrayEquals(new short[] { 3, 2, 1 }, matrix.rowView(0));
        assertArrayEquals(new short[] { 6, 5, 4 }, matrix.rowView(1));
    }

    @Test
    public void testShortMatrix_flipInPlaceVertically() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        matrix.flipInPlaceVertically();
        // matrix is now [[7, 8, 9], [4, 5, 6], [1, 2, 3]]
        assertArrayEquals(new short[] { 7, 8, 9 }, matrix.rowView(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, matrix.rowView(1));
        assertArrayEquals(new short[] { 1, 2, 3 }, matrix.rowView(2));
    }

    @Test
    public void testShortMatrix_flipHorizontally() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix flipped = matrix.flipHorizontally();
        // Result: [[3, 2, 1], [6, 5, 4]]
        assertArrayEquals(new short[] { 3, 2, 1 }, flipped.rowView(0));
        assertArrayEquals(new short[] { 6, 5, 4 }, flipped.rowView(1));
    }

    @Test
    public void testShortMatrix_flipVertically() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix flipped = matrix.flipVertically();
        // Result: [[4, 5, 6], [1, 2, 3]]
        assertArrayEquals(new short[] { 4, 5, 6 }, flipped.rowView(0));
        assertArrayEquals(new short[] { 1, 2, 3 }, flipped.rowView(1));
    }

    @Test
    public void testShortMatrix_rotate90() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = matrix.rotate90();
        // Result: [[3, 1], [4, 2]]
        assertEquals((short) 3, rotated.get(0, 0));
        assertEquals((short) 1, rotated.get(0, 1));
        assertEquals((short) 4, rotated.get(1, 0));
        assertEquals((short) 2, rotated.get(1, 1));
    }

    @Test
    public void testShortMatrix_rotate180() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = matrix.rotate180();
        // Result: [[4, 3], [2, 1]]
        assertEquals((short) 4, rotated.get(0, 0));
        assertEquals((short) 3, rotated.get(0, 1));
        assertEquals((short) 2, rotated.get(1, 0));
        assertEquals((short) 1, rotated.get(1, 1));
    }

    @Test
    public void testShortMatrix_rotate270() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = matrix.rotate270();
        // Result: [[2, 4], [1, 3]]
        assertEquals((short) 2, rotated.get(0, 0));
        assertEquals((short) 4, rotated.get(0, 1));
        assertEquals((short) 1, rotated.get(1, 0));
        assertEquals((short) 3, rotated.get(1, 1));
    }

    @Test
    public void testShortMatrix_repeatElements() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix repeated = matrix.repeatElements(2, 3);
        // Result: [[1, 1, 1, 2, 2, 2], [1, 1, 1, 2, 2, 2], [3, 3, 3, 4, 4, 4], [3, 3, 3, 4, 4, 4]]
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals((short) 1, repeated.get(0, 0));
        assertEquals((short) 1, repeated.get(0, 2));
        assertEquals((short) 2, repeated.get(0, 3));
        assertEquals((short) 2, repeated.get(0, 5));
        assertEquals((short) 1, repeated.get(1, 0));
        assertEquals((short) 3, repeated.get(2, 0));
        assertEquals((short) 4, repeated.get(3, 5));
    }

    @Test
    public void testShortMatrix_repeatMatrix() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix tiled = matrix.repeatMatrix(2, 3);
        // Result: [[1, 2, 1, 2, 1, 2], [3, 4, 3, 4, 3, 4], [1, 2, 1, 2, 1, 2], [3, 4, 3, 4, 3, 4]]
        assertEquals(4, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertEquals((short) 1, tiled.get(0, 0));
        assertEquals((short) 2, tiled.get(0, 1));
        assertEquals((short) 1, tiled.get(0, 2));
        assertEquals((short) 4, tiled.get(1, 1));
        assertEquals((short) 1, tiled.get(2, 0));
        assertEquals((short) 4, tiled.get(3, 5));
    }

    @Test
    public void testShortMatrix_flatten() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortList list = matrix.flatten();
        assertEquals(4, list.size());
        assertEquals((short) 1, list.get(0));
        assertEquals((short) 2, list.get(1));
        assertEquals((short) 3, list.get(2));
        assertEquals((short) 4, list.get(3));
    }

    @Test
    public void testShortMatrix_applyOnFlattened() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 5, 3 }, { 4, 1 } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        // matrix is now [[1, 3], [4, 5]]
        assertEquals((short) 1, matrix.get(0, 0));
        assertEquals((short) 3, matrix.get(0, 1));
        assertEquals((short) 4, matrix.get(1, 0));
        assertEquals((short) 5, matrix.get(1, 1));
    }

    @Test
    public void testShortMatrix_stackVertically() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix stacked = matrix1.stackVertically(matrix2);
        // Result: [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
        assertEquals(3, stacked.rowCount());
        assertArrayEquals(new short[] { 1, 2, 3 }, stacked.rowView(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, stacked.rowView(1));
        assertArrayEquals(new short[] { 7, 8, 9 }, stacked.rowView(2));
    }

    @Test
    public void testShortMatrix_stackHorizontally() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 5 }, { 6 } });
        ShortMatrix stacked = matrix1.stackHorizontally(matrix2);
        // Result: [[1, 2, 5], [3, 4, 6]]
        assertEquals(2, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 2, stacked.get(0, 1));
        assertEquals((short) 5, stacked.get(0, 2));
        assertEquals((short) 3, stacked.get(1, 0));
        assertEquals((short) 4, stacked.get(1, 1));
        assertEquals((short) 6, stacked.get(1, 2));
    }

    @Test
    public void testShortMatrix_add() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix sum = matrix1.add(matrix2);
        // Result: [[6, 8], [10, 12]]
        assertEquals((short) 6, sum.get(0, 0));
        assertEquals((short) 8, sum.get(0, 1));
        assertEquals((short) 10, sum.get(1, 0));
        assertEquals((short) 12, sum.get(1, 1));
    }

    @Test
    public void testShortMatrix_subtract() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix diff = matrix1.subtract(matrix2);
        // Result: [[4, 4], [4, 4]]
        assertEquals((short) 4, diff.get(0, 0));
        assertEquals((short) 4, diff.get(0, 1));
        assertEquals((short) 4, diff.get(1, 0));
        assertEquals((short) 4, diff.get(1, 1));
    }

    @Test
    public void testShortMatrix_multiply() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix product = matrix1.multiply(matrix2);
        // Result: [[19, 22], [43, 50]]
        assertEquals((short) 19, product.get(0, 0));
        assertEquals((short) 22, product.get(0, 1));
        assertEquals((short) 43, product.get(1, 0));
        assertEquals((short) 50, product.get(1, 1));
    }

    @Test
    public void testShortMatrix_toIntMatrix() {
        ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix intMatrix = shortMatrix.toIntMatrix();
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(2, intMatrix.get(0, 1));
        assertEquals(3, intMatrix.get(1, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testShortMatrix_toLongMatrix() {
        ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix longMatrix = shortMatrix.toLongMatrix();
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void testShortMatrix_toFloatMatrix() {
        ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix floatMatrix = shortMatrix.toFloatMatrix();
        assertEquals(1.0f, floatMatrix.get(0, 0));
        assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    @Test
    public void testShortMatrix_toDoubleMatrix() {
        ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix doubleMatrix = shortMatrix.toDoubleMatrix();
        assertEquals(1.0, doubleMatrix.get(0, 0));
        assertEquals(4.0, doubleMatrix.get(1, 1));
    }

    @Test
    public void testShortMatrix_zipWith() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix max = matrix1.zipWith(matrix2, (a, b) -> (short) Math.max(a, b));
        // Result: [[5, 6], [7, 8]]
        assertEquals((short) 5, max.get(0, 0));
        assertEquals((short) 6, max.get(0, 1));
        assertEquals((short) 7, max.get(1, 0));
        assertEquals((short) 8, max.get(1, 1));
    }

    @Test
    public void testShortMatrix_zipWith3() {
        ShortMatrix matrix1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix matrix2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix matrix3 = ShortMatrix.of(new short[][] { { 9, 10 }, { 11, 12 } });
        ShortMatrix average = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> (short) ((a + b + c) / 3));
        // Result: [[5, 6], [7, 8]]
        assertEquals((short) 5, average.get(0, 0));
        assertEquals((short) 6, average.get(0, 1));
        assertEquals((short) 7, average.get(1, 0));
        assertEquals((short) 8, average.get(1, 1));
    }
}
