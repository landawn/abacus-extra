package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.u.OptionalInt;
import com.landawn.abacus.util.stream.IntStream;

public class IntMatrixTest extends TestBase {

    private IntMatrix matrix;
    private IntMatrix emptyMatrix;

    @BeforeEach
    public void setUp() {
        matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        emptyMatrix = IntMatrix.empty();
    }

    @Test
    public void testConstructor() {
        // Test with valid array
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = new IntMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));

        // Test with null array
        IntMatrix nullMatrix = new IntMatrix(null);
        assertEquals(0, nullMatrix.rowCount());
        assertEquals(0, nullMatrix.columnCount());
    }

    @Test
    public void testEmpty() {
        IntMatrix empty = IntMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test that empty() returns singleton
        assertSame(IntMatrix.empty(), IntMatrix.empty());
    }

    @Test
    public void testOf() {
        // Test with valid array
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());

        // Test with null/empty
        IntMatrix empty1 = IntMatrix.of(null);
        assertTrue(empty1.isEmpty());

        IntMatrix empty2 = IntMatrix.of(new int[0][0]);
        assertTrue(empty2.isEmpty());
    }

    @Test
    public void testCreateFromCharArray() {
        char[][] chars = { { 'A', 'B' }, { 'C', 'D' } };
        IntMatrix m = IntMatrix.from(chars);
        assertEquals(65, m.get(0, 0)); // 'A'
        assertEquals(66, m.get(0, 1)); // 'B'
        assertEquals(67, m.get(1, 0)); // 'C'
        assertEquals(68, m.get(1, 1)); // 'D'

        // Test with null/empty
        IntMatrix empty = IntMatrix.from((char[][]) null);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateFromByteArray() {
        byte[][] bytes = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.from(bytes);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));

        // Test with null/empty
        IntMatrix empty = IntMatrix.from((byte[][]) null);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateFromShortArray() {
        short[][] shorts = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.from(shorts);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));

        // Test with null/empty
        IntMatrix empty = IntMatrix.from((short[][]) null);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testRandom() {
        IntMatrix m = IntMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Values should be random, just check they exist
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat() {
        IntMatrix m = IntMatrix.repeat(1, 5, 42);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        IntMatrix m = IntMatrix.range(0, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, m.get(0, i));
        }
    }

    @Test
    public void testRangeWithStep() {
        IntMatrix m = IntMatrix.range(0, 10, 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(0, 3));
        assertEquals(8, m.get(0, 4));
    }

    @Test
    public void testRangeClosed() {
        IntMatrix m = IntMatrix.rangeClosed(0, 4);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, m.get(0, i));
        }
    }

    @Test
    public void testRangeClosedWithStep() {
        IntMatrix m = IntMatrix.rangeClosed(0, 10, 2);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(0, 3));
        assertEquals(8, m.get(0, 4));
        assertEquals(10, m.get(0, 5));
    }

    @Test
    public void testDiagonalLU2RD() {
        IntMatrix m = IntMatrix.mainDiagonal(new int[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        IntMatrix m = IntMatrix.antiDiagonal(new int[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(2, 2));
    }

    @Test
    public void testDiagonal() {
        // Test with both diagonals
        IntMatrix m = IntMatrix.diagonals(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(4, m.get(0, 2));
        assertEquals(2, m.get(1, 1)); // Overwritten
        assertEquals(6, m.get(2, 0));

        // Test with only main diagonal
        IntMatrix m2 = IntMatrix.diagonals(new int[] { 1, 2, 3 }, null);
        assertEquals(1, m2.get(0, 0));
        assertEquals(2, m2.get(1, 1));
        assertEquals(3, m2.get(2, 2));

        // Test with only anti-diagonal
        IntMatrix m3 = IntMatrix.diagonals(null, new int[] { 4, 5, 6 });
        assertEquals(4, m3.get(0, 2));
        assertEquals(5, m3.get(1, 1));
        assertEquals(6, m3.get(2, 0));

        // Test with empty arrays
        IntMatrix empty = IntMatrix.diagonals(null, null);
        assertTrue(empty.isEmpty());

        // Test illegal argument
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.diagonals(new int[] { 1, 2 }, new int[] { 3, 4, 5 }));
    }

    @Test
    public void testUnbox() {
        Integer[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> boxedMatrix = Matrix.of(boxed);
        IntMatrix unboxed = IntMatrix.unbox(boxedMatrix);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(2, unboxed.get(0, 1));
        assertEquals(3, unboxed.get(1, 0));
        assertEquals(4, unboxed.get(1, 1));
    }

    @Test
    public void testComponentType() {
        assertEquals(int.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        assertEquals(1, matrix.get(0, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(9, matrix.get(2, 2));

        // Test bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(3, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, 3));
    }

    @Test
    public void testGetWithPoint() {
        Sheet.Point p1 = Sheet.Point.of(0, 0);
        assertEquals(1, matrix.get(p1));

        Sheet.Point p2 = Sheet.Point.of(1, 1);
        assertEquals(5, matrix.get(p2));

        Sheet.Point p3 = Sheet.Point.of(2, 2);
        assertEquals(9, matrix.get(p3));
    }

    @Test
    public void testSet() {
        IntMatrix m = matrix.copy();
        m.set(0, 0, 10);
        assertEquals(10, m.get(0, 0));

        m.set(1, 1, 20);
        assertEquals(20, m.get(1, 1));

        // Test bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(3, 0, 0));
    }

    @Test
    public void testSetWithPoint() {
        IntMatrix m = matrix.copy();
        Sheet.Point p = Sheet.Point.of(1, 1);
        m.set(p, 50);
        assertEquals(50, m.get(p));
    }

    @Test
    public void testUpOf() {
        OptionalInt up = matrix.above(1, 1);
        assertTrue(up.isPresent());
        assertEquals(2, up.get());

        // Test top row
        OptionalInt empty = matrix.above(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        OptionalInt down = matrix.below(1, 1);
        assertTrue(down.isPresent());
        assertEquals(8, down.get());

        // Test bottom row
        OptionalInt empty = matrix.below(2, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        OptionalInt left = matrix.left(1, 1);
        assertTrue(left.isPresent());
        assertEquals(4, left.get());

        // Test leftmost column
        OptionalInt empty = matrix.left(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        OptionalInt right = matrix.right(1, 1);
        assertTrue(right.isPresent());
        assertEquals(6, right.get());

        // Test rightmost column
        OptionalInt empty = matrix.right(1, 2);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        int[] row0 = matrix.rowRef(0);
        assertArrayEquals(new int[] { 1, 2, 3 }, row0);

        int[] row1 = matrix.rowRef(1);
        assertArrayEquals(new int[] { 4, 5, 6 }, row1);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.rowRef(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowRef(3));
    }

    @Test
    public void testColumn() {
        int[] col0 = matrix.columnCopy(0);
        assertArrayEquals(new int[] { 1, 4, 7 }, col0);

        int[] col1 = matrix.columnCopy(1);
        assertArrayEquals(new int[] { 2, 5, 8 }, col1);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(3));
    }

    @Test
    public void testSetRow() {
        IntMatrix m = matrix.copy();
        m.setRow(0, new int[] { 10, 20, 30 });
        assertArrayEquals(new int[] { 10, 20, 30 }, m.rowRef(0));

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new int[] { 1, 2 }));
    }

    @Test
    public void testSetColumn() {
        IntMatrix m = matrix.copy();
        m.setColumn(0, new int[] { 10, 20, 30 });
        assertArrayEquals(new int[] { 10, 20, 30 }, m.columnCopy(0));

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new int[] { 1, 2 }));
    }

    @Test
    public void testUpdateRow() {
        IntMatrix m = matrix.copy();
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new int[] { 2, 4, 6 }, m.rowRef(0));
    }

    @Test
    public void testUpdateColumn() {
        IntMatrix m = matrix.copy();
        m.updateColumn(0, x -> x + 10);
        assertArrayEquals(new int[] { 11, 14, 17 }, m.columnCopy(0));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        IntMatrix m = matrix.copy();

        assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(-1, x -> x * 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(3, x -> x + 10));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        IntMatrix m = matrix.copy();

        assertThrows(IllegalArgumentException.class, () -> m.updateRow(0, (Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.updateColumn(0, (Throwables.IntUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        int[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new int[] { 1, 5, 9 }, diagonal);

        // Test non-square matrix
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        IntMatrix m = matrix.copy();
        m.setMainDiagonal(new int[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));

        // Test non-square matrix
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setMainDiagonal(new int[] { 1 }));

        // Test array too short
        assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new int[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        IntMatrix m = matrix.copy();
        m.updateMainDiagonal(x -> x * 10);
        assertEquals(10, m.get(0, 0));
        assertEquals(50, m.get(1, 1));
        assertEquals(90, m.get(2, 2));

        // Test non-square matrix
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateMainDiagonal(x -> x * 2));
    }

    @Test
    public void testGetRU2LD() {
        int[] antiDiagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new int[] { 3, 5, 7 }, antiDiagonal);

        // Test non-square matrix
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getAntiDiagonal());
    }

    @Test
    public void testSetRU2LD() {
        IntMatrix m = matrix.copy();
        m.setAntiDiagonal(new int[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 2));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 0));

        // Test non-square matrix
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setAntiDiagonal(new int[] { 1 }));
    }

    @Test
    public void testUpdateRU2LD() {
        IntMatrix m = matrix.copy();
        m.updateAntiDiagonal(x -> x * 10);
        assertEquals(30, m.get(0, 2));
        assertEquals(50, m.get(1, 1));
        assertEquals(70, m.get(2, 0));

        // Test non-square matrix
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateAntiDiagonal(x -> x * 2));
    }

    @Test
    public void testUpdateAll() {
        IntMatrix m = matrix.copy();
        m.updateAll(x -> x * 2);
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(18, m.get(2, 2));
    }

    @Test
    public void testUpdateAllWithIndices() {
        IntMatrix m = IntMatrix.of(new int[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> i * 10 + j);
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void testUpdateAllNullOperator() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix emptyLike = IntMatrix.of(new int[][] { {}, {} });

        assertThrows(IllegalArgumentException.class, () -> m.updateAll((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.updateAll((Throwables.IntBiFunction<Integer, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.updateAll((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.updateAll((Throwables.IntBiFunction<Integer, RuntimeException>) null));
    }

    @Test
    public void testDiagonalAndReplaceIfNullCallbacks() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix emptyLike = IntMatrix.of(new int[][] { {}, {} });

        assertThrows(IllegalArgumentException.class, () -> m.updateMainDiagonal((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.updateAntiDiagonal((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.replaceIf((Throwables.IntPredicate<RuntimeException>) null, 0));
        assertThrows(IllegalArgumentException.class, () -> m.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0));
    }

    @Test
    public void testReplaceIf() {
        IntMatrix m = matrix.copy();
        m.replaceIf(x -> x > 5, 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(0, m.get(2, 2)); // was 9
    }

    @Test
    public void testReplaceIfWithIndices() {
        IntMatrix m = matrix.copy();
        m.replaceIf((i, j) -> i == j, 0); // Replace diagonal
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        IntMatrix result = matrix.map(x -> x * 2);
        assertEquals(2, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(18, result.get(2, 2));

        // Original should be unchanged
        assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testMapNullMapper() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.IntUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.IntFunction<String, RuntimeException>) null, String.class));
    }

    @Test
    public void testMapToLong() {
        LongMatrix result = matrix.mapToLong(x -> (long) x * 1000000);
        assertEquals(1000000L, result.get(0, 0));
        assertEquals(5000000L, result.get(1, 1));
    }

    @Test
    public void testMapToDouble() {
        DoubleMatrix result = matrix.mapToDouble(x -> x * 0.1);
        assertEquals(0.1, result.get(0, 0), 0.0001);
        assertEquals(0.5, result.get(1, 1), 0.0001);
    }

    @Test
    public void testMapToObj() {
        Matrix<String> result = matrix.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1", result.get(0, 0));
        assertEquals("val:5", result.get(1, 1));
    }

    @Test
    public void testFillWithValue() {
        IntMatrix m = matrix.copy();
        m.fill(99);
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
                assertEquals(99, m.get(i, j));
            }
        }
    }

    @Test
    public void testFillWithArray() {
        IntMatrix m = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        int[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(patch);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(2, 2)); // unchanged
    }

    @Test
    public void testFillWithArrayAtPosition() {
        IntMatrix m = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        int[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(1, 1, patch);
        assertEquals(0, m.get(0, 0)); // unchanged
        assertEquals(1, m.get(1, 1));
        assertEquals(2, m.get(1, 2));
        assertEquals(3, m.get(2, 1));
        assertEquals(4, m.get(2, 2));

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
        // assertThrows(IndexOutOfBoundsException.class, () -> m.fill(3, 0, patch));
        m.fill(3, 0, patch);
    }

    @Test
    public void testCopy() {
        IntMatrix copy = matrix.copy();
        assertEquals(matrix.rowCount(), copy.rowCount());
        assertEquals(matrix.columnCount(), copy.columnCount());
        assertEquals(matrix.get(0, 0), copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, 99);
        assertEquals(1, matrix.get(0, 0));
        assertEquals(99, copy.get(0, 0));
    }

    @Test
    public void testCopyRange() {
        IntMatrix subset = matrix.copy(1, 3);
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals(4, subset.get(0, 0));
        assertEquals(7, subset.get(1, 0));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(2, 1));
    }

    @Test
    public void testCopySubMatrix() {
        IntMatrix submatrix = matrix.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals(2, submatrix.get(0, 0));
        assertEquals(3, submatrix.get(0, 1));
        assertEquals(5, submatrix.get(1, 0));
        assertEquals(6, submatrix.get(1, 1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 2, 0, 4));
    }

    @Test
    public void testExtend() {
        IntMatrix extended = matrix.resize(5, 5);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(0, extended.get(3, 3)); // new cells are 0

        // Test truncation
        IntMatrix truncated = matrix.resize(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
    }

    @Test
    public void testExtendWithDefaultValue() {
        IntMatrix extended = matrix.resize(4, 4, -1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(-1, extended.get(3, 3)); // new cell

        // Test negative dimensions
        assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> matrix.resize(3, -1, 0));
    }

    @Test
    public void testExtendDirectional() {
        IntMatrix extended = matrix.extend(1, 1, 2, 2);
        assertEquals(5, extended.rowCount()); // 1 + 3 + 1
        assertEquals(7, extended.columnCount()); // 2 + 3 + 2

        // Original values should be at offset position
        assertEquals(1, extended.get(1, 2));
        assertEquals(5, extended.get(2, 3));

        // New cells should be 0
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtendDirectionalWithDefaultValue() {
        IntMatrix extended = matrix.extend(1, 1, 1, 1, -1);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());

        // Check original values
        assertEquals(1, extended.get(1, 1));

        // Check new values
        assertEquals(-1, extended.get(0, 0));
        assertEquals(-1, extended.get(4, 4));

        // Test negative values
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 1, 1, 1, 0));
    }

    @Test
    public void testReverseH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
        assertEquals(6, m.get(1, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(4, m.get(1, 2));
    }

    @Test
    public void testReverseV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals(5, m.get(0, 0));
        assertEquals(6, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(1, m.get(2, 0));
        assertEquals(2, m.get(2, 1));
    }

    @Test
    public void testFlipH() {
        IntMatrix flipped = matrix.flippedH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));

        // Original should be unchanged
        assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        IntMatrix flipped = matrix.flippedV();
        assertEquals(7, flipped.get(0, 0));
        assertEquals(8, flipped.get(0, 1));
        assertEquals(9, flipped.get(0, 2));

        // Original should be unchanged
        assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testRotate90() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        IntMatrix reshaped = matrix.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i));
        }

        // Test reshape back
        IntMatrix reshaped2 = reshaped.reshape(3, 3);
        assertEquals(matrix, reshaped2);

        // Test empty matrix
        IntMatrix emptyReshaped = emptyMatrix.reshape(2, 3);
        assertEquals(2, emptyReshaped.rowCount());
        assertEquals(3, emptyReshaped.columnCount());

        // Test reshape with too-small dimensions throws exception
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix repeated = m.repeatElements(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
        assertEquals(2, repeated.get(0, 4));
        assertEquals(2, repeated.get(0, 5));
        assertEquals(1, repeated.get(1, 0)); // second row same as first

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
    }

    @Test
    public void testRepmat() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = m.repeatMatrix(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2)); // repeat starts
        assertEquals(2, repeated.get(0, 3));
        assertEquals(1, repeated.get(0, 4)); // another repeat
        assertEquals(2, repeated.get(0, 5));

        assertEquals(3, repeated.get(1, 0));
        assertEquals(4, repeated.get(1, 1));

        // Check vertical repeat
        assertEquals(1, repeated.get(2, 0)); // vertical repeat starts
        assertEquals(2, repeated.get(2, 1));

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
    }

    @Test
    public void testFlatten() {
        IntList flat = matrix.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, flat.get(i));
        }
    }

    @Test
    public void testFlatOp() {
        List<Integer> sums = new ArrayList<>();
        matrix.applyOnFlattened(row -> {
            int sum = 0;
            for (int val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertEquals(45, sums.get(0).intValue());
    }

    @Test
    public void testVstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        IntMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(7, stacked.get(2, 0));
        assertEquals(12, stacked.get(3, 2));

        // Test different column counts
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m3));
    }

    @Test
    public void testHstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(5, stacked.get(0, 2));
        assertEquals(8, stacked.get(1, 3));

        // Test different row counts
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m3));
    }

    @Test
    public void testAdd() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix sum = m1.add(m2);

        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(10, sum.get(1, 0));
        assertEquals(12, sum.get(1, 1));

        // Test different dimensions
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m3));
    }

    @Test
    public void testSubtract() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix diff = m1.subtract(m2);

        assertEquals(4, diff.get(0, 0));
        assertEquals(4, diff.get(0, 1));
        assertEquals(4, diff.get(1, 0));
        assertEquals(4, diff.get(1, 1));

        // Test different dimensions
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m3));
    }

    @Test
    public void testMultiply() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix product = m1.multiply(m2);

        assertEquals(19, product.get(0, 0)); // 1*5 + 2*7
        assertEquals(22, product.get(0, 1)); // 1*6 + 2*8
        assertEquals(43, product.get(1, 0)); // 3*5 + 4*7
        assertEquals(50, product.get(1, 1)); // 3*6 + 4*8

        // Test incompatible dimensions
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m3));
    }

    @Test
    public void testBoxed() {
        Matrix<Integer> boxed = matrix.boxed();
        assertEquals(3, boxed.rowCount());
        assertEquals(3, boxed.columnCount());
        assertEquals(Integer.valueOf(1), boxed.get(0, 0));
        assertEquals(Integer.valueOf(5), boxed.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        LongMatrix longMatrix = matrix.toLongMatrix();
        assertEquals(3, longMatrix.rowCount());
        assertEquals(3, longMatrix.columnCount());
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(5L, longMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        assertEquals(3, floatMatrix.rowCount());
        assertEquals(3, floatMatrix.columnCount());
        assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
        assertEquals(5.0f, floatMatrix.get(1, 1), 0.0001f);
    }

    @Test
    public void testToDoubleMatrix() {
        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        assertEquals(3, doubleMatrix.rowCount());
        assertEquals(3, doubleMatrix.columnCount());
        assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(5.0, doubleMatrix.get(1, 1), 0.0001);
    }

    @Test
    public void testZipWith() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = m1.zipWith(m2, (a, b) -> a * b);

        assertEquals(5, result.get(0, 0)); // 1*5
        assertEquals(12, result.get(0, 1)); // 2*6
        assertEquals(21, result.get(1, 0)); // 3*7
        assertEquals(32, result.get(1, 1)); // 4*8

        // Test different shapes
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m3, (a, b) -> a + b));
    }

    @Test
    public void testZipWith3Matrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        IntMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15, result.get(0, 0)); // 1+5+9
        assertEquals(18, result.get(0, 1)); // 2+6+10
        assertEquals(21, result.get(1, 0)); // 3+7+11
        assertEquals(24, result.get(1, 1)); // 4+8+12
    }

    @Test
    public void testStreamLU2RD() {
        int[] diagonal = matrix.streamMainDiagonal().toArray();
        assertArrayEquals(new int[] { 1, 5, 9 }, diagonal);

        // Test empty matrix
        assertTrue(emptyMatrix.streamMainDiagonal().toArray().length == 0);

        // Test non-square
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
    }

    @Test
    public void testStreamRU2LD() {
        int[] antiDiagonal = matrix.streamAntiDiagonal().toArray();
        assertArrayEquals(new int[] { 3, 5, 7 }, antiDiagonal);

        // Test empty matrix
        assertTrue(emptyMatrix.streamAntiDiagonal().toArray().length == 0);

        // Test non-square
        IntMatrix nonSquare = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
    }

    @Test
    public void testStreamH() {
        int[] all = matrix.streamHorizontal().toArray();
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, all);

        // Test empty matrix
        assertTrue(emptyMatrix.streamHorizontal().toArray().length == 0);
    }

    @Test
    public void testStreamHRow() {
        int[] row1 = matrix.streamHorizontal(1).toArray();
        assertArrayEquals(new int[] { 4, 5, 6 }, row1);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(3));
    }

    @Test
    public void testStreamHRange() {
        int[] rows = matrix.streamHorizontal(1, 3).toArray();
        assertArrayEquals(new int[] { 4, 5, 6, 7, 8, 9 }, rows);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(2, 1));
    }

    @Test
    public void testStreamV() {
        int[] all = matrix.streamVertical().toArray();
        assertArrayEquals(new int[] { 1, 4, 7, 2, 5, 8, 3, 6, 9 }, all);

        // Test empty matrix
        assertTrue(emptyMatrix.streamVertical().toArray().length == 0);
    }

    @Test
    public void testStreamVColumn() {
        int[] col1 = matrix.streamVertical(1).toArray();
        assertArrayEquals(new int[] { 2, 5, 8 }, col1);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(3));
    }

    @Test
    public void testStreamVRange() {
        int[] columnCount = matrix.streamVertical(1, 3).toArray();
        assertArrayEquals(new int[] { 2, 5, 8, 3, 6, 9 }, columnCount);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(2, 1));
    }

    @Test
    public void testStreamR() {
        List<int[]> rows = matrix.streamR().map(IntStream::toArray).toList();
        assertEquals(3, rows.size());
        assertArrayEquals(new int[] { 1, 2, 3 }, rows.get(0));
        assertArrayEquals(new int[] { 4, 5, 6 }, rows.get(1));
        assertArrayEquals(new int[] { 7, 8, 9 }, rows.get(2));

        // Test empty matrix
        assertTrue(emptyMatrix.streamR().count() == 0);
    }

    @Test
    public void testStreamRWithZeroColumnRows() {
        IntMatrix zeroColumnMatrix = IntMatrix.of(new int[][] { {}, {}, {} });

        assertEquals(3, zeroColumnMatrix.rowCount());
        assertEquals(0, zeroColumnMatrix.columnCount());
        assertTrue(zeroColumnMatrix.isEmpty());

        List<int[]> rows = zeroColumnMatrix.streamR().map(IntStream::toArray).toList();
        assertEquals(3, rows.size());
        assertArrayEquals(new int[0], rows.get(0));
        assertArrayEquals(new int[0], rows.get(1));
        assertArrayEquals(new int[0], rows.get(2));
    }

    @Test
    public void testStreamRRange() {
        List<int[]> rows = matrix.streamR(1, 3).map(IntStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new int[] { 4, 5, 6 }, rows.get(0));
        assertArrayEquals(new int[] { 7, 8, 9 }, rows.get(1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(0, 4));
    }

    @Test
    public void testStreamC() {
        List<int[]> columnCount = matrix.streamC().map(IntStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new int[] { 1, 4, 7 }, columnCount.get(0));
        assertArrayEquals(new int[] { 2, 5, 8 }, columnCount.get(1));
        assertArrayEquals(new int[] { 3, 6, 9 }, columnCount.get(2));

        // Test empty matrix
        assertTrue(emptyMatrix.streamC().count() == 0);
    }

    @Test
    public void testStreamCRange() {
        List<int[]> columnCount = matrix.streamC(1, 3).map(IntStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new int[] { 2, 5, 8 }, columnCount.get(0));
        assertArrayEquals(new int[] { 3, 6, 9 }, columnCount.get(1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(0, 4));
    }

    @Test
    public void testLength() {
        // This is a protected method, test indirectly through row operations
        int[] row = matrix.rowRef(0);
        assertEquals(3, row.length);
    }

    @Test
    public void testForEach() {
        List<Integer> values = new ArrayList<>();
        matrix.forEach(it -> values.add(it));
        assertEquals(9, values.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, values.get(i).intValue());
        }
    }

    @Test
    public void testForEachWithBounds() {
        List<Integer> values = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, it -> values.add(it));
        assertEquals(4, values.size());
        assertEquals(5, values.get(0).intValue());
        assertEquals(6, values.get(1).intValue());
        assertEquals(8, values.get(2).intValue());
        assertEquals(9, values.get(3).intValue());

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(-1, 2, 0, 2, v -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(0, 4, 0, 2, v -> {
        }));
    }

    @Test
    public void testForEachNullAction() {
        assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.IntConsumer<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> matrix.forEach(0, matrix.rowCount(), 0, matrix.columnCount(), (Throwables.IntConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        // Just ensure it doesn't throw
        matrix.println();
        emptyMatrix.println();
    }

    @Test
    public void testHashCode() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2 }, { 4, 3 } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2 }, { 4, 3 } });
        IntMatrix m4 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    @Test
    public void testIteratorNoSuchElement() {
        // Test streamHorizontal iterator
        IntStream stream = matrix.streamHorizontal(0, 1);
        stream.toArray(); // Consume all
        assertThrows(IllegalStateException.class, () -> stream.iterator().next());

        // Test streamMainDiagonal iterator
        IntStream diagStream = matrix.streamMainDiagonal();
        diagStream.toArray(); // Consume all
        assertThrows(IllegalStateException.class, () -> diagStream.iterator().next());
    }

    @Test
    public void testEmptyMatrixOperations() {
        // Test various operations on empty matrix
        assertTrue(emptyMatrix.flatten().isEmpty());
        assertEquals(0, emptyMatrix.copy().rowCount);
        assertEquals(emptyMatrix, emptyMatrix.transpose());
        assertEquals(emptyMatrix, emptyMatrix.rotate90());

        IntMatrix extended = emptyMatrix.resize(2, 2, 5);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(5, extended.get(0, 0));
    }

    @Test
    public void testStatisticalOperations() {
        // Test sum operation on streams
        int totalSum = matrix.streamHorizontal().sum();
        assertEquals(45, totalSum); // 1+2+3+4+5+6+7+8+9 = 45

        // Test sum of specific row
        int row1Sum = matrix.streamHorizontal(1).sum();
        assertEquals(15, row1Sum); // 4+5+6 = 15

        // Test sum of specific column
        int col0Sum = matrix.streamVertical(0).sum();
        assertEquals(12, col0Sum); // 1+4+7 = 12

        // Test min/max on streams
        int min = matrix.streamHorizontal().min().orElse(0);
        assertEquals(1, min);

        int max = matrix.streamHorizontal().max().orElse(0);
        assertEquals(9, max);

        // Test average
        double avg = matrix.streamHorizontal().average().orElse(0.0);
        assertEquals(5.0, avg, 0.0001);

        // Test statistical operations on diagonal
        int diagonalSum = matrix.streamMainDiagonal().sum();
        assertEquals(15, diagonalSum); // 1+5+9 = 15

        int antiDiagonalSum = matrix.streamAntiDiagonal().sum();
        assertEquals(15, antiDiagonalSum); // 3+5+7 = 15
    }

    @Test
    public void testRowColumnStatistics() {
        // Test statistics on individual rows
        List<Integer> rowSums = matrix.streamR().map(row -> row.sum()).toList();
        assertEquals(3, rowSums.size());
        assertEquals(6, rowSums.get(0).intValue()); // 1+2+3
        assertEquals(15, rowSums.get(1).intValue()); // 4+5+6
        assertEquals(24, rowSums.get(2).intValue()); // 7+8+9

        // Test statistics on individual columns
        List<Integer> colSums = matrix.streamC().map(col -> col.sum()).toList();
        assertEquals(3, colSums.size());
        assertEquals(12, colSums.get(0).intValue()); // 1+4+7
        assertEquals(15, colSums.get(1).intValue()); // 2+5+8
        assertEquals(18, colSums.get(2).intValue()); // 3+6+9

        // Test min/max per row
        List<Integer> rowMins = matrix.streamR().map(row -> row.min().orElse(0)).toList();
        assertEquals(1, rowMins.get(0).intValue());
        assertEquals(4, rowMins.get(1).intValue());
        assertEquals(7, rowMins.get(2).intValue());

        List<Integer> rowMaxs = matrix.streamR().map(row -> row.max().orElse(0)).toList();
        assertEquals(3, rowMaxs.get(0).intValue());
        assertEquals(6, rowMaxs.get(1).intValue());
        assertEquals(9, rowMaxs.get(2).intValue());
    }

    @Test
    public void testElementWiseMultiplyWithZipWith() {
        // Test element-wise multiplication using zipWith (since there's no multiply scalar method)
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2, 3 }, { 4, 5 } });

        // Element-wise multiplication
        IntMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(2, elementWiseProduct.get(0, 0)); // 1*2
        assertEquals(6, elementWiseProduct.get(0, 1)); // 2*3
        assertEquals(12, elementWiseProduct.get(1, 0)); // 3*4
        assertEquals(20, elementWiseProduct.get(1, 1)); // 4*5

        // Element-wise division
        IntMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
        assertEquals(2, elementWiseDivision.get(0, 0)); // 2/1
        assertEquals(1, elementWiseDivision.get(0, 1)); // 3/2 (integer division)
        assertEquals(1, elementWiseDivision.get(1, 0)); // 4/3 (integer division)
        assertEquals(1, elementWiseDivision.get(1, 1)); // 5/4 (integer division)
    }

    @Test
    public void testScalarOperationsWithMap() {
        // Test scalar addition using map
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        IntMatrix addScalar = m.map(x -> x + 10);
        assertEquals(11, addScalar.get(0, 0));
        assertEquals(12, addScalar.get(0, 1));
        assertEquals(13, addScalar.get(1, 0));
        assertEquals(14, addScalar.get(1, 1));

        // Test scalar subtraction
        IntMatrix subtractScalar = m.map(x -> x - 1);
        assertEquals(0, subtractScalar.get(0, 0));
        assertEquals(1, subtractScalar.get(0, 1));
        assertEquals(2, subtractScalar.get(1, 0));
        assertEquals(3, subtractScalar.get(1, 1));

        // Test scalar multiplication
        IntMatrix multiplyScalar = m.map(x -> x * 3);
        assertEquals(3, multiplyScalar.get(0, 0));
        assertEquals(6, multiplyScalar.get(0, 1));
        assertEquals(9, multiplyScalar.get(1, 0));
        assertEquals(12, multiplyScalar.get(1, 1));

        // Test scalar division
        IntMatrix m2 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
        IntMatrix divideScalar = m2.map(x -> x / 10);
        assertEquals(1, divideScalar.get(0, 0));
        assertEquals(2, divideScalar.get(0, 1));
        assertEquals(3, divideScalar.get(1, 0));
        assertEquals(4, divideScalar.get(1, 1));
    }

    @Test
    public void testArithmeticEdgeCases() {
        // Test with zero matrix
        IntMatrix zeros = IntMatrix.of(new int[][] { { 0, 0 }, { 0, 0 } });
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        IntMatrix addZero = m.add(zeros);
        assertEquals(m, addZero);

        IntMatrix subtractZero = m.subtract(zeros);
        assertEquals(m, subtractZero);

        // Test multiplication with zero matrix
        IntMatrix multiplyZero = m.multiply(zeros);
        assertEquals(zeros, multiplyZero);

        // Test addition commutativity
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertEquals(m1.add(m2), m2.add(m1));

        // Test subtraction anti-commutativity
        IntMatrix diff1 = m1.subtract(m2);
        IntMatrix diff2 = m2.subtract(m1);
        assertEquals(diff1.get(0, 0), -diff2.get(0, 0));
        assertEquals(diff1.get(1, 1), -diff2.get(1, 1));
    }

    @Test
    public void testLargeMatrixArithmetic() {
        // Test with larger matrices to ensure performance
        int[][] arr1 = new int[10][10];
        int[][] arr2 = new int[10][10];

        // Fill with test data
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arr1[i][j] = i * 10 + j + 1;
                arr2[i][j] = (i * 10 + j + 1) * 2;
            }
        }

        IntMatrix large1 = IntMatrix.of(arr1);
        IntMatrix large2 = IntMatrix.of(arr2);

        // Test addition
        IntMatrix largeSum = large1.add(large2);
        assertEquals(3, largeSum.get(0, 0)); // 1 + 2 = 3
        assertEquals(300, largeSum.get(9, 9)); // 100 + 200 = 300

        // Test that sum of all elements is correct
        long totalSum = largeSum.streamHorizontal().asLongStream().sum();
        assertEquals(15150, totalSum); // (1+2+...+100) + 2*(1+2+...+100) = 3*(1+2+...+100) = 3*5050 = 15150
    }

    @Test
    public void testRejectUnrepresentableZeroRowNonZeroColumnShape() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        // assertThrows(IllegalArgumentException.class, () -> matrix.copy(0, 0));
        // assertThrows(IllegalArgumentException.class, () -> matrix.copy(0, 0, 0, 1));
        // assertThrows(IllegalArgumentException.class, () -> matrix.extend(0, 1));
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(0, 1));
    }
}
