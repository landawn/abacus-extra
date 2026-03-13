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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.u.OptionalFloat;
import com.landawn.abacus.util.stream.FloatStream;
import com.landawn.abacus.util.stream.Stream;

class FloatMatrixTest extends TestBase {

    private static final float DELTA = 0.0001f;
    private FloatMatrix matrix;
    private FloatMatrix emptyMatrix;

    @BeforeEach
    public void setUp() {
        matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        emptyMatrix = FloatMatrix.empty();
    }

    @Test
    public void testConstructor() {
        // Test with valid array
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = new FloatMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), DELTA);

        // Test with null array
        FloatMatrix nullMatrix = new FloatMatrix(null);
        assertEquals(0, nullMatrix.rowCount());
        assertEquals(0, nullMatrix.columnCount());
    }

    @Test
    public void testOf() {
        // Test with valid array
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = FloatMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());

        // Test with null/empty
        FloatMatrix empty1 = FloatMatrix.of(null);
        assertTrue(empty1.isEmpty());

        FloatMatrix empty2 = FloatMatrix.of(new float[0][0]);
        assertTrue(empty2.isEmpty());
    }

    @Test
    public void testCreateFromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        FloatMatrix m = FloatMatrix.from(ints);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA);
        assertEquals(3.0f, m.get(1, 0), DELTA);
        assertEquals(4.0f, m.get(1, 1), DELTA);

        // Test with null/empty
        FloatMatrix empty = FloatMatrix.from((int[][]) null);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testRandom() {
        FloatMatrix m = FloatMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Values should be random, just check they exist
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat() {
        FloatMatrix m = FloatMatrix.repeat(1, 5, 42.5f);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42.5f, m.get(0, i), DELTA);
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        FloatMatrix m = FloatMatrix.mainDiagonal(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA);
        assertEquals(3.0f, m.get(2, 2), DELTA);
        assertEquals(0.0f, m.get(0, 1), DELTA);
        assertEquals(0.0f, m.get(1, 0), DELTA);
    }

    @Test
    public void testDiagonalRU2LD() {
        FloatMatrix m = FloatMatrix.antiDiagonal(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 2), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA);
        assertEquals(3.0f, m.get(2, 0), DELTA);
        assertEquals(0.0f, m.get(0, 0), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA);
    }

    @Test
    public void testDiagonal() {
        // Test with both diagonals
        FloatMatrix m = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA);
        assertEquals(3.0f, m.get(2, 2), DELTA);
        assertEquals(4.0f, m.get(0, 2), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA); // Overwritten
        assertEquals(6.0f, m.get(2, 0), DELTA);

        // Test with only main diagonal
        FloatMatrix m2 = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, null);
        assertEquals(1.0f, m2.get(0, 0), DELTA);
        assertEquals(2.0f, m2.get(1, 1), DELTA);
        assertEquals(3.0f, m2.get(2, 2), DELTA);

        // Test with only anti-diagonal
        FloatMatrix m3 = FloatMatrix.diagonals(null, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(4.0f, m3.get(0, 2), DELTA);
        assertEquals(5.0f, m3.get(1, 1), DELTA);
        assertEquals(6.0f, m3.get(2, 0), DELTA);

        // Test with empty arrays
        FloatMatrix empty = FloatMatrix.diagonals(null, null);
        assertTrue(empty.isEmpty());

        // Test illegal argument
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonals(new float[] { 1.0f, 2.0f }, new float[] { 3.0f, 4.0f, 5.0f }));
    }

    @Test
    public void testUnbox() {
        Float[][] boxed = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        Matrix<Float> boxedMatrix = Matrix.of(boxed);
        FloatMatrix unboxed = FloatMatrix.unbox(boxedMatrix);
        assertEquals(1.0f, unboxed.get(0, 0), DELTA);
        assertEquals(2.0f, unboxed.get(0, 1), DELTA);
        assertEquals(3.0f, unboxed.get(1, 0), DELTA);
        assertEquals(4.0f, unboxed.get(1, 1), DELTA);
    }

    @Test
    public void testComponentType() {
        assertEquals(float.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
        assertEquals(5.0f, matrix.get(1, 1), DELTA);
        assertEquals(9.0f, matrix.get(2, 2), DELTA);

        // Test bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(3, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, 3));
    }

    @Test
    public void testGetWithPoint() {
        Sheet.Point p1 = Sheet.Point.of(0, 0);
        assertEquals(1.0f, matrix.get(p1), DELTA);

        Sheet.Point p2 = Sheet.Point.of(1, 1);
        assertEquals(5.0f, matrix.get(p2), DELTA);

        Sheet.Point p3 = Sheet.Point.of(2, 2);
        assertEquals(9.0f, matrix.get(p3), DELTA);
    }

    @Test
    public void testSet() {
        FloatMatrix m = matrix.copy();
        m.set(0, 0, 10.0f);
        assertEquals(10.0f, m.get(0, 0), DELTA);

        m.set(1, 1, 20.0f);
        assertEquals(20.0f, m.get(1, 1), DELTA);

        // Test bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0.0f));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(3, 0, 0.0f));
    }

    @Test
    public void testSetWithPoint() {
        FloatMatrix m = matrix.copy();
        Sheet.Point p = Sheet.Point.of(1, 1);
        m.set(p, 50.0f);
        assertEquals(50.0f, m.get(p), DELTA);
    }

    @Test
    public void testUpOf() {
        OptionalFloat up = matrix.above(1, 1);
        assertTrue(up.isPresent());
        assertEquals(2.0f, up.get(), DELTA);

        // Test top row
        OptionalFloat empty = matrix.above(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        OptionalFloat down = matrix.below(1, 1);
        assertTrue(down.isPresent());
        assertEquals(8.0f, down.get(), DELTA);

        // Test bottom row
        OptionalFloat empty = matrix.below(2, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        OptionalFloat left = matrix.left(1, 1);
        assertTrue(left.isPresent());
        assertEquals(4.0f, left.get(), DELTA);

        // Test leftmost column
        OptionalFloat empty = matrix.left(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        OptionalFloat right = matrix.right(1, 1);
        assertTrue(right.isPresent());
        assertEquals(6.0f, right.get(), DELTA);

        // Test rightmost column
        OptionalFloat empty = matrix.right(1, 2);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        float[] row0 = matrix.rowView(0);
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, row0, DELTA);

        float[] row1 = matrix.rowView(1);
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, row1, DELTA);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(3));
    }

    // Verify rowCopy returns a defensive row snapshot and enforces row bounds.
    @Test
    public void testRowCopy() {
        float[] rowCopy = matrix.rowCopy(0);
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, rowCopy, DELTA);

        rowCopy[0] = 99.0f;
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, matrix.rowView(0), DELTA);
    }

    @Test
    public void testRowCopy_InvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(3));
    }

    @Test
    public void testColumn() {
        float[] col0 = matrix.columnCopy(0);
        assertArrayEquals(new float[] { 1.0f, 4.0f, 7.0f }, col0, DELTA);

        float[] col1 = matrix.columnCopy(1);
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, col1, DELTA);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(3));
    }

    @Test
    public void testSetRow() {
        FloatMatrix m = matrix.copy();
        m.setRow(0, new float[] { 10.0f, 20.0f, 30.0f });
        assertArrayEquals(new float[] { 10.0f, 20.0f, 30.0f }, m.rowView(0), DELTA);

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testSetColumn() {
        FloatMatrix m = matrix.copy();
        m.setColumn(0, new float[] { 10.0f, 20.0f, 30.0f });
        assertArrayEquals(new float[] { 10.0f, 20.0f, 30.0f }, m.columnCopy(0), DELTA);

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testUpdateRow() {
        FloatMatrix m = matrix.copy();
        m.updateRow(0, x -> x * 2.0f);
        assertArrayEquals(new float[] { 2.0f, 4.0f, 6.0f }, m.rowView(0), DELTA);
    }

    @Test
    public void testUpdateColumn() {
        FloatMatrix m = matrix.copy();
        m.updateColumn(0, x -> x + 10.0f);
        assertArrayEquals(new float[] { 11.0f, 14.0f, 17.0f }, m.columnCopy(0), DELTA);
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        FloatMatrix m = matrix.copy();

        assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(-1, x -> x * 2.0f));
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(3, x -> x + 10.0f));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        FloatMatrix m = matrix.copy();

        assertThrows(IllegalArgumentException.class, () -> m.updateRow(0, (Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.updateColumn(0, (Throwables.FloatUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        float[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal, DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        FloatMatrix m = matrix.copy();
        m.setMainDiagonal(new float[] { 10.0f, 20.0f, 30.0f });
        assertEquals(10.0f, m.get(0, 0), DELTA);
        assertEquals(20.0f, m.get(1, 1), DELTA);
        assertEquals(30.0f, m.get(2, 2), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setMainDiagonal(new float[] { 1.0f }));

        // Test array too short
        assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testUpdateLU2RD() {
        FloatMatrix m = matrix.copy();
        m.updateMainDiagonal(x -> x * 10.0f);
        assertEquals(10.0f, m.get(0, 0), DELTA);
        assertEquals(50.0f, m.get(1, 1), DELTA);
        assertEquals(90.0f, m.get(2, 2), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateMainDiagonal(x -> x * 2.0f));
    }

    @Test
    public void testGetRU2LD() {
        float[] antiDiagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, antiDiagonal, DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getAntiDiagonal());
    }

    @Test
    public void testSetRU2LD() {
        FloatMatrix m = matrix.copy();
        m.setAntiDiagonal(new float[] { 10.0f, 20.0f, 30.0f });
        assertEquals(10.0f, m.get(0, 2), DELTA);
        assertEquals(20.0f, m.get(1, 1), DELTA);
        assertEquals(30.0f, m.get(2, 0), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setAntiDiagonal(new float[] { 1.0f }));
    }

    @Test
    public void testUpdateRU2LD() {
        FloatMatrix m = matrix.copy();
        m.updateAntiDiagonal(x -> x * 10.0f);
        assertEquals(30.0f, m.get(0, 2), DELTA);
        assertEquals(50.0f, m.get(1, 1), DELTA);
        assertEquals(70.0f, m.get(2, 0), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateAntiDiagonal(x -> x * 2.0f));
    }

    @Test
    public void testUpdateAll() {
        FloatMatrix m = matrix.copy();
        m.updateAll(x -> x * 2.0f);
        assertEquals(2.0f, m.get(0, 0), DELTA);
        assertEquals(4.0f, m.get(0, 1), DELTA);
        assertEquals(18.0f, m.get(2, 2), DELTA);
    }

    @Test
    public void testUpdateAllWithIndices() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f }, { 0.0f, 0.0f } });
        m.updateAll((i, j) -> i * 10.0f + j);
        assertEquals(0.0f, m.get(0, 0), DELTA);
        assertEquals(1.0f, m.get(0, 1), DELTA);
        assertEquals(10.0f, m.get(1, 0), DELTA);
        assertEquals(11.0f, m.get(1, 1), DELTA);
    }

    @Test
    public void testReplaceIf() {
        FloatMatrix m = matrix.copy();
        m.replaceIf(x -> x > 5.0f, 0.0f);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(5.0f, m.get(1, 1), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA); // was 9.0f
    }

    @Test
    public void testReplaceIfWithIndices() {
        FloatMatrix m = matrix.copy();
        m.replaceIf((i, j) -> i == j, 0.0f); // Replace diagonal
        assertEquals(0.0f, m.get(0, 0), DELTA);
        assertEquals(0.0f, m.get(1, 1), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
    }

    @Test
    public void testMap() {
        FloatMatrix result = matrix.map(x -> x * 2.0f);
        assertEquals(2.0f, result.get(0, 0), DELTA);
        assertEquals(4.0f, result.get(0, 1), DELTA);
        assertEquals(18.0f, result.get(2, 2), DELTA);

        // Original should be unchanged
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
    }

    @Test
    public void testMapNullMapper() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.FloatFunction<String, RuntimeException>) null, String.class));
    }

    @Test
    public void testMapToObj() {
        Matrix<String> result = matrix.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1.0", result.get(0, 0));
        assertEquals("val:5.0", result.get(1, 1));
    }

    @Test
    public void testFillWithValue() {
        FloatMatrix m = matrix.copy();
        m.fill(99.5f);
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
                assertEquals(99.5f, m.get(i, j), DELTA);
            }
        }
    }

    @Test
    public void testFillWithArray() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
        float[][] patch = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        m.copyFrom(patch);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA);
        assertEquals(3.0f, m.get(1, 0), DELTA);
        assertEquals(4.0f, m.get(1, 1), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA); // unchanged
    }

    @Test
    public void testFillWithArrayAtPosition() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
        float[][] patch = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        m.copyFrom(1, 1, patch);
        assertEquals(0.0f, m.get(0, 0), DELTA); // unchanged
        assertEquals(1.0f, m.get(1, 1), DELTA);
        assertEquals(2.0f, m.get(1, 2), DELTA);
        assertEquals(3.0f, m.get(2, 1), DELTA);
        assertEquals(4.0f, m.get(2, 2), DELTA);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        // assertThrows(IndexOutOfBoundsException.class, () -> m.copyFrom(3, 0, patch));
        m.copyFrom(3, 0, patch);
    }

    @Test
    public void testCopy() {
        FloatMatrix copy = matrix.copy();
        assertEquals(matrix.rowCount(), copy.rowCount());
        assertEquals(matrix.columnCount(), copy.columnCount());
        assertEquals(matrix.get(0, 0), copy.get(0, 0), DELTA);

        // Modify copy shouldn't affect original
        copy.set(0, 0, 99.0f);
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
        assertEquals(99.0f, copy.get(0, 0), DELTA);
    }

    @Test
    public void testCopyRange() {
        FloatMatrix subset = matrix.copy(1, 3);
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals(4.0f, subset.get(0, 0), DELTA);
        assertEquals(7.0f, subset.get(1, 0), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(2, 1));
    }

    @Test
    public void testCopySubMatrix() {
        FloatMatrix submatrix = matrix.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals(2.0f, submatrix.get(0, 0), DELTA);
        assertEquals(3.0f, submatrix.get(0, 1), DELTA);
        assertEquals(5.0f, submatrix.get(1, 0), DELTA);
        assertEquals(6.0f, submatrix.get(1, 1), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 2, 0, 4));
    }

    @Test
    public void testExtend() {
        FloatMatrix extended = matrix.resize(5, 5);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0), DELTA);
        assertEquals(0.0f, extended.get(3, 3), DELTA); // new cells are 0

        // Test truncation
        FloatMatrix truncated = matrix.resize(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
    }

    @Test
    public void testExtendWithDefaultValue() {
        FloatMatrix extended = matrix.resize(4, 4, -1.0f);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0), DELTA);
        assertEquals(-1.0f, extended.get(3, 3), DELTA); // new cell

        // Test negative dimensions
        assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 3, 0.0f));
        assertThrows(IllegalArgumentException.class, () -> matrix.resize(3, -1, 0.0f));
    }

    @Test
    public void testExtendDirectional() {
        FloatMatrix extended = matrix.extend(1, 1, 2, 2);
        assertEquals(5, extended.rowCount()); // 1 + 3 + 1
        assertEquals(7, extended.columnCount()); // 2 + 3 + 2

        // Original values should be at offset position
        assertEquals(1.0f, extended.get(1, 2), DELTA);
        assertEquals(5.0f, extended.get(2, 3), DELTA);

        // New cells should be 0
        assertEquals(0.0f, extended.get(0, 0), DELTA);
    }

    @Test
    public void testExtendDirectionalWithDefaultValue() {
        FloatMatrix extended = matrix.extend(1, 1, 1, 1, -1.0f);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());

        // Check original values
        assertEquals(1.0f, extended.get(1, 1), DELTA);

        // Check new values
        assertEquals(-1.0f, extended.get(0, 0), DELTA);
        assertEquals(-1.0f, extended.get(4, 4), DELTA);

        // Test negative values
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 1, 1, 1, 0.0f));
    }

    @Test
    public void testReverseH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        m.flipInPlaceHorizontally();
        assertEquals(3.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA);
        assertEquals(1.0f, m.get(0, 2), DELTA);
        assertEquals(6.0f, m.get(1, 0), DELTA);
        assertEquals(5.0f, m.get(1, 1), DELTA);
        assertEquals(4.0f, m.get(1, 2), DELTA);
    }

    @Test
    public void testReverseV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        m.flipInPlaceVertically();
        assertEquals(5.0f, m.get(0, 0), DELTA);
        assertEquals(6.0f, m.get(0, 1), DELTA);
        assertEquals(3.0f, m.get(1, 0), DELTA);
        assertEquals(4.0f, m.get(1, 1), DELTA);
        assertEquals(1.0f, m.get(2, 0), DELTA);
        assertEquals(2.0f, m.get(2, 1), DELTA);
    }

    @Test
    public void testFlipH() {
        FloatMatrix flipped = matrix.flipHorizontally();
        assertEquals(3.0f, flipped.get(0, 0), DELTA);
        assertEquals(2.0f, flipped.get(0, 1), DELTA);
        assertEquals(1.0f, flipped.get(0, 2), DELTA);

        // Original should be unchanged
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
    }

    @Test
    public void testFlipV() {
        FloatMatrix flipped = matrix.flipVertically();
        assertEquals(7.0f, flipped.get(0, 0), DELTA);
        assertEquals(8.0f, flipped.get(0, 1), DELTA);
        assertEquals(9.0f, flipped.get(0, 2), DELTA);

        // Original should be unchanged
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
    }

    @Test
    public void testTranspose() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1.0f, transposed.get(0, 0), DELTA);
        assertEquals(4.0f, transposed.get(0, 1), DELTA);
        assertEquals(2.0f, transposed.get(1, 0), DELTA);
        assertEquals(5.0f, transposed.get(1, 1), DELTA);
    }

    @Test
    public void testReshape() {
        FloatMatrix reshaped = matrix.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i), DELTA);
        }

        // Test reshape back
        FloatMatrix reshaped2 = reshaped.reshape(3, 3);
        assertEquals(matrix, reshaped2);

        // Test empty matrix
        FloatMatrix emptyReshaped = emptyMatrix.reshape(2, 3);
        assertEquals(2, emptyReshaped.rowCount());
        assertEquals(3, emptyReshaped.columnCount());

        // Test reshape with too-small dimensions throws exception
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix repeated = m.repeatElements(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1.0f, repeated.get(0, 0), DELTA);
        assertEquals(1.0f, repeated.get(0, 1), DELTA);
        assertEquals(1.0f, repeated.get(0, 2), DELTA);
        assertEquals(2.0f, repeated.get(0, 3), DELTA);
        assertEquals(2.0f, repeated.get(0, 4), DELTA);
        assertEquals(2.0f, repeated.get(0, 5), DELTA);
        assertEquals(1.0f, repeated.get(1, 0), DELTA); // second row same as first

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
    }

    @Test
    public void testRepmat() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix repeated = m.repeatMatrix(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1.0f, repeated.get(0, 0), DELTA);
        assertEquals(2.0f, repeated.get(0, 1), DELTA);
        assertEquals(1.0f, repeated.get(0, 2), DELTA); // repeat starts
        assertEquals(2.0f, repeated.get(0, 3), DELTA);
        assertEquals(1.0f, repeated.get(0, 4), DELTA); // another repeat
        assertEquals(2.0f, repeated.get(0, 5), DELTA);

        assertEquals(3.0f, repeated.get(1, 0), DELTA);
        assertEquals(4.0f, repeated.get(1, 1), DELTA);

        // Check vertical repeat
        assertEquals(1.0f, repeated.get(2, 0), DELTA); // vertical repeat starts
        assertEquals(2.0f, repeated.get(2, 1), DELTA);

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
    }

    @Test
    public void testFlatten() {
        FloatList flat = matrix.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, flat.get(i), DELTA);
        }
    }

    @Test
    public void testFlatOp() {
        List<Float> sums = new ArrayList<>();
        matrix.applyOnFlattened(row -> {
            float sum = 0.0f;
            for (float val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertEquals(45.0f, sums.get(0), DELTA);
    }

    @Test
    public void testVstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 7.0f, 8.0f, 9.0f }, { 10.0f, 11.0f, 12.0f } });
        FloatMatrix stacked = m1.stackVertically(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals(1.0f, stacked.get(0, 0), DELTA);
        assertEquals(7.0f, stacked.get(2, 0), DELTA);
        assertEquals(12.0f, stacked.get(3, 2), DELTA);

        // Test different column counts
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m3));
    }

    @Test
    public void testHstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = m1.stackHorizontally(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1.0f, stacked.get(0, 0), DELTA);
        assertEquals(5.0f, stacked.get(0, 2), DELTA);
        assertEquals(8.0f, stacked.get(1, 3), DELTA);

        // Test different row counts
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m3));
    }

    @Test
    public void testAdd() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix sum = m1.add(m2);

        assertEquals(6.0f, sum.get(0, 0), DELTA);
        assertEquals(8.0f, sum.get(0, 1), DELTA);
        assertEquals(10.0f, sum.get(1, 0), DELTA);
        assertEquals(12.0f, sum.get(1, 1), DELTA);

        // Test different dimensions
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m3));
    }

    @Test
    public void testSubtract() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix diff = m1.subtract(m2);

        assertEquals(4.0f, diff.get(0, 0), DELTA);
        assertEquals(4.0f, diff.get(0, 1), DELTA);
        assertEquals(4.0f, diff.get(1, 0), DELTA);
        assertEquals(4.0f, diff.get(1, 1), DELTA);

        // Test different dimensions
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m3));
    }

    @Test
    public void testMultiply() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix product = m1.multiply(m2);

        assertEquals(19.0f, product.get(0, 0), DELTA); // 1*5 + 2*7
        assertEquals(22.0f, product.get(0, 1), DELTA); // 1*6 + 2*8
        assertEquals(43.0f, product.get(1, 0), DELTA); // 3*5 + 4*7
        assertEquals(50.0f, product.get(1, 1), DELTA); // 3*6 + 4*8

        // Test incompatible dimensions
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m3));
    }

    @Test
    public void testBoxed() {
        Matrix<Float> boxed = matrix.boxed();
        assertEquals(3, boxed.rowCount());
        assertEquals(3, boxed.columnCount());
        assertEquals(Float.valueOf(1.0f), boxed.get(0, 0));
        assertEquals(Float.valueOf(5.0f), boxed.get(1, 1));
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
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);

        assertEquals(5.0f, result.get(0, 0), DELTA); // 1*5
        assertEquals(12.0f, result.get(0, 1), DELTA); // 2*6
        assertEquals(21.0f, result.get(1, 0), DELTA); // 3*7
        assertEquals(32.0f, result.get(1, 1), DELTA); // 4*8

        // Test different shapes
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m3, (a, b) -> a + b));
    }

    @Test
    public void testStreamLU2RD() {
        float[] diagonal = matrix.streamMainDiagonal().toArray();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamMainDiagonal().toArray().length == 0);

        // Test non-square
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
    }

    @Test
    public void testStreamRU2LD() {
        float[] antiDiagonal = matrix.streamAntiDiagonal().toArray();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, antiDiagonal, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamAntiDiagonal().toArray().length == 0);

        // Test non-square
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
    }

    @Test
    public void testStreamH() {
        float[] all = matrix.streamHorizontal().toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, all, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamHorizontal().toArray().length == 0);
    }

    @Test
    public void testStreamHRow() {
        float[] row1 = matrix.streamHorizontal(1).toArray();
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, row1, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(3));
    }

    @Test
    public void testStreamHRange() {
        float[] rows = matrix.streamHorizontal(1, 3).toArray();
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, rows, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(2, 1));
    }

    @Test
    public void testStreamV() {
        float[] all = matrix.streamVertical().toArray();
        assertArrayEquals(new float[] { 1.0f, 4.0f, 7.0f, 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f }, all, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamVertical().toArray().length == 0);
    }

    @Test
    public void testStreamVColumn() {
        float[] col1 = matrix.streamVertical(1).toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, col1, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(3));
    }

    @Test
    public void testStreamVRange() {
        float[] columnCount = matrix.streamVertical(1, 3).toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f }, columnCount, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(2, 1));
    }

    @Test
    public void testStreamR() {
        List<float[]> rows = matrix.streamRows().map(FloatStream::toArray).toList();
        assertEquals(3, rows.size());
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, rows.get(0), DELTA);
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(1), DELTA);
        assertArrayEquals(new float[] { 7.0f, 8.0f, 9.0f }, rows.get(2), DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamRows().count() == 0);
    }

    @Test
    public void testStreamRRange() {
        List<float[]> rows = matrix.streamRows(1, 3).map(FloatStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(0), DELTA);
        assertArrayEquals(new float[] { 7.0f, 8.0f, 9.0f }, rows.get(1), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamRows(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamRows(0, 4));
    }

    @Test
    public void testStreamC() {
        List<float[]> columnCount = matrix.streamColumns().map(FloatStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new float[] { 1.0f, 4.0f, 7.0f }, columnCount.get(0), DELTA);
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, columnCount.get(1), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f, 9.0f }, columnCount.get(2), DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamColumns().count() == 0);
    }

    @Test
    public void testStreamCRange() {
        List<float[]> columnCount = matrix.streamColumns(1, 3).map(FloatStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, columnCount.get(0), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f, 9.0f }, columnCount.get(1), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamColumns(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamColumns(0, 4));
    }

    @Test
    public void testLength() {
        // This is a protected method, test indirectly through row operations
        float[] row = matrix.rowView(0);
        assertEquals(3, row.length);
    }

    @Test
    public void testForEach() {
        List<Float> values = new ArrayList<>();
        matrix.forEach(it -> values.add(it));
        assertEquals(9, values.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, values.get(i), DELTA);
        }
    }

    @Test
    public void testForEachWithBounds() {
        List<Float> values = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, it -> values.add(it));
        assertEquals(4, values.size());
        assertEquals(5.0f, values.get(0), DELTA);
        assertEquals(6.0f, values.get(1), DELTA);
        assertEquals(8.0f, values.get(2), DELTA);
        assertEquals(9.0f, values.get(3), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(-1, 2, 0, 2, v -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(0, 4, 0, 2, v -> {
        }));
    }

    @Test
    public void testForEachNullAction() {
        assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.FloatConsumer<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> matrix.forEach(0, matrix.rowCount(), 0, matrix.columnCount(), (Throwables.FloatConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        assertFalse(matrix.isEmpty());
        assertTrue(emptyMatrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            matrix.println();
            emptyMatrix.println();
        });
    }

    @Test
    public void testToString() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        String str = m.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
        assertTrue(str.contains("4.0"));
    }

    @Test
    public void testIteratorNoSuchElement() {
        // Test streamHorizontal iterator
        FloatStream stream = matrix.streamHorizontal(0, 1);
        stream.toArray(); // Consume all
        assertThrows(IllegalStateException.class, () -> stream.iterator().next());

        // Test streamMainDiagonal iterator
        FloatStream diagStream = matrix.streamMainDiagonal();
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

        FloatMatrix extended = emptyMatrix.resize(2, 2, 5.0f);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(5.0f, extended.get(0, 0), DELTA);
    }

    @Nested
    @Tag("2025")
    class FloatMatrix2025Test extends TestBase {

        private static final float DELTA = 0.0001f;

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            FloatMatrix m = new FloatMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(4.0f, m.get(1, 1), DELTA);
        }

        @Test
        public void testConstructor_withSingleElement() {
            FloatMatrix m = new FloatMatrix(new float[][] { { 42.5f } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.5f, m.get(0, 0), DELTA);
        }

        @Test
        public void testConstructor_withFloatSpecialValues() {
            float[][] arr = { { Float.NaN, Float.POSITIVE_INFINITY }, { Float.NEGATIVE_INFINITY, -0.0f } };
            FloatMatrix m = new FloatMatrix(arr);
            assertTrue(Float.isNaN(m.get(0, 0)));
            assertEquals(Float.POSITIVE_INFINITY, m.get(0, 1), DELTA);
            assertEquals(Float.NEGATIVE_INFINITY, m.get(1, 0), DELTA);
            assertEquals(-0.0f, m.get(1, 1), DELTA);
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(FloatMatrix.empty(), FloatMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            FloatMatrix m = FloatMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), DELTA);
        }

        @Test
        public void testOf_withFloatPrecision() {
            float[][] arr = { { 0.1f, 0.2f }, { 0.3f, 0.4f } };
            FloatMatrix m = FloatMatrix.of(arr);
            assertEquals(0.1f, m.get(0, 0), DELTA);
            assertEquals(0.2f, m.get(0, 1), DELTA);
            assertEquals(0.3f, m.get(1, 0), DELTA);
            assertEquals(0.4f, m.get(1, 1), DELTA);
        }

        @Test
        public void testCreateFromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            FloatMatrix m = FloatMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(4.0f, m.get(1, 1), DELTA);
        }

        @Test
        public void testCreateFromIntArray_withNull() {
            FloatMatrix m = FloatMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withEmpty() {
            FloatMatrix m = FloatMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withJaggedArray() {
            int[][] jagged = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(jagged));
        }

        @Test
        public void testCreateFromIntArray_withNullRow() {
            int[][] nullRow = { { 1, 2 }, null };
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromIntArray_withNullFirstRow() {
            int[][] nullFirstRow = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(nullFirstRow));
        }

        @Test
        public void testCreateFromIntArray_largeValues() {
            int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 1000000, -1000000 } };
            FloatMatrix m = FloatMatrix.from(ints);
            assertEquals(Integer.MAX_VALUE, m.get(0, 0), 1.0f);
            assertEquals(Integer.MIN_VALUE, m.get(0, 1), 1.0f);
            assertEquals(1000000.0f, m.get(1, 0), DELTA);
            assertEquals(-1000000.0f, m.get(1, 1), DELTA);
        }

        @Test
        public void testRandom() {
            FloatMatrix m = FloatMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist and are in valid range
            for (int i = 0; i < 5; i++) {
                float val = m.get(0, i);
                assertTrue(val >= 0.0f && val < 1.0f);
            }
        }

        @Test
        public void testRandom_withRowsCols() {
            FloatMatrix m = FloatMatrix.random(2, 3);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    float val = m.get(i, j);
                    assertTrue(val >= 0.0f && val < 1.0f);
                }
            }
        }

        @Test
        public void testRepeat() {
            FloatMatrix m = FloatMatrix.repeat(1, 5, 3.14f);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14f, m.get(0, i), DELTA);
            }
        }

        @Test
        public void testRepeat_withRowsCols() {
            FloatMatrix m = FloatMatrix.repeat(2, 3, 3.14f);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(3.14f, m.get(i, j), DELTA);
                }
            }
        }

        @Test
        public void testRepeat_withSpecialValues() {
            FloatMatrix m = FloatMatrix.repeat(1, 3, Float.NaN);
            for (int i = 0; i < 3; i++) {
                assertTrue(Float.isNaN(m.get(0, i)));
            }

            FloatMatrix m2 = FloatMatrix.repeat(1, 2, Float.POSITIVE_INFINITY);
            for (int i = 0; i < 2; i++) {
                assertEquals(Float.POSITIVE_INFINITY, m2.get(0, i), DELTA);
            }
        }

        @Test
        public void testDiagonalLU2RD() {
            FloatMatrix m = FloatMatrix.mainDiagonal(new float[] { 1.5f, 2.5f, 3.5f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.5f, m.get(0, 0), DELTA);
            assertEquals(2.5f, m.get(1, 1), DELTA);
            assertEquals(3.5f, m.get(2, 2), DELTA);
            assertEquals(0.0f, m.get(0, 1), DELTA);
            assertEquals(0.0f, m.get(1, 0), DELTA);
        }

        @Test
        public void testDiagonalRU2LD() {
            FloatMatrix m = FloatMatrix.antiDiagonal(new float[] { 1.5f, 2.5f, 3.5f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.5f, m.get(0, 2), DELTA);
            assertEquals(2.5f, m.get(1, 1), DELTA);
            assertEquals(3.5f, m.get(2, 0), DELTA);
            assertEquals(0.0f, m.get(0, 0), DELTA);
            assertEquals(0.0f, m.get(2, 2), DELTA);
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            FloatMatrix m = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, new float[] { 4.0f, 5.0f, 6.0f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(2.0f, m.get(1, 1), DELTA);
            assertEquals(3.0f, m.get(2, 2), DELTA);
            assertEquals(4.0f, m.get(0, 2), DELTA);
            assertEquals(6.0f, m.get(2, 0), DELTA);
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            FloatMatrix m = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(2.0f, m.get(1, 1), DELTA);
            assertEquals(3.0f, m.get(2, 2), DELTA);
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            FloatMatrix m = FloatMatrix.diagonals(null, new float[] { 4.0f, 5.0f, 6.0f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(4.0f, m.get(0, 2), DELTA);
            assertEquals(5.0f, m.get(1, 1), DELTA);
            assertEquals(6.0f, m.get(2, 0), DELTA);
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonals(new float[] { 1.0f, 2.0f }, new float[] { 3.0f, 4.0f, 5.0f }));
        }

        @Test
        public void testUnbox() {
            Float[][] boxed = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            Matrix<Float> boxedMatrix = Matrix.of(boxed);
            FloatMatrix unboxed = FloatMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1.0f, unboxed.get(0, 0), DELTA);
            assertEquals(4.0f, unboxed.get(1, 1), DELTA);
        }

        @Test
        public void testUnbox_withNullValues() {
            Float[][] boxed = { { 1.0f, null }, { null, 4.0f } };
            Matrix<Float> boxedMatrix = Matrix.of(boxed);
            FloatMatrix unboxed = FloatMatrix.unbox(boxedMatrix);
            assertEquals(1.0f, unboxed.get(0, 0), DELTA);
            assertEquals(0.0f, unboxed.get(0, 1), DELTA); // null -> 0.0f
            assertEquals(0.0f, unboxed.get(1, 0), DELTA); // null -> 0.0f
            assertEquals(4.0f, unboxed.get(1, 1), DELTA);
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f } });
            assertEquals(float.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(5.0f, m.get(1, 1), DELTA);
            assertEquals(6.0f, m.get(1, 2), DELTA);
        }

        @Test
        public void testGet_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(1.0f, m.get(Point.of(0, 0)), DELTA);
            assertEquals(4.0f, m.get(Point.of(1, 1)), DELTA);
            assertEquals(2.0f, m.get(Point.of(0, 1)), DELTA);
        }

        @Test
        public void testSet() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(0, 0, 10.5f);
            assertEquals(10.5f, m.get(0, 0), DELTA);

            m.set(1, 1, 20.7f);
            assertEquals(20.7f, m.get(1, 1), DELTA);
        }

        @Test
        public void testSet_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0.0f));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0.0f));
        }

        @Test
        public void testSetWithPoint() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(Point.of(0, 0), 50.5f);
            assertEquals(50.5f, m.get(Point.of(0, 0)), DELTA);
        }

        @Test
        public void testSet_withSpecialValues() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(0, 0, Float.NaN);
            assertTrue(Float.isNaN(m.get(0, 0)));

            m.set(0, 1, Float.POSITIVE_INFINITY);
            assertEquals(Float.POSITIVE_INFINITY, m.get(0, 1), DELTA);

            m.set(1, 0, -0.0f);
            assertEquals(-0.0f, m.get(1, 0), DELTA);
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

            OptionalFloat up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.0f, up.get(), DELTA);

            // Top row has no element above
            OptionalFloat empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

            OptionalFloat down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.0f, down.get(), DELTA);

            // Bottom row has no element below
            OptionalFloat empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

            OptionalFloat left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.0f, left.get(), DELTA);

            // Leftmost column has no element to the left
            OptionalFloat empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

            OptionalFloat right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.0f, right.get(), DELTA);

            // Rightmost column has no element to the right
            OptionalFloat empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, m.rowView(0), DELTA);
            assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, m.rowView(1), DELTA);
        }

        @Test
        public void testRow_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            assertArrayEquals(new float[] { 1.0f, 4.0f }, m.columnCopy(0), DELTA);
            assertArrayEquals(new float[] { 2.0f, 5.0f }, m.columnCopy(1), DELTA);
            assertArrayEquals(new float[] { 3.0f, 6.0f }, m.columnCopy(2), DELTA);
        }

        @Test
        public void testColumn_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setRow(0, new float[] { 10.5f, 20.5f });
            assertArrayEquals(new float[] { 10.5f, 20.5f }, m.rowView(0), DELTA);
            assertArrayEquals(new float[] { 3.0f, 4.0f }, m.rowView(1), DELTA); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f, 2.0f, 3.0f }));
        }

        @Test
        public void testSetColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setColumn(0, new float[] { 10.5f, 20.5f });
            assertArrayEquals(new float[] { 10.5f, 20.5f }, m.columnCopy(0), DELTA);
            assertArrayEquals(new float[] { 2.0f, 4.0f }, m.columnCopy(1), DELTA); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f, 2.0f, 3.0f }));
        }

        @Test
        public void testUpdateRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateRow(0, x -> x * 2.0f);
            assertArrayEquals(new float[] { 2.0f, 4.0f }, m.rowView(0), DELTA);
            assertArrayEquals(new float[] { 3.0f, 4.0f }, m.rowView(1), DELTA); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateColumn(0, x -> x + 10.0f);
            assertArrayEquals(new float[] { 11.0f, 13.0f }, m.columnCopy(0), DELTA);
            assertArrayEquals(new float[] { 2.0f, 4.0f }, m.columnCopy(1), DELTA); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, m.getMainDiagonal(), DELTA);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.setMainDiagonal(new float[] { 10.5f, 20.5f, 30.5f });
            assertEquals(10.5f, m.get(0, 0), DELTA);
            assertEquals(20.5f, m.get(1, 1), DELTA);
            assertEquals(30.5f, m.get(2, 2), DELTA);
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new float[] { 1.0f }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new float[] { 1.0f, 2.0f }));
        }

        @Test
        public void testUpdateLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.updateMainDiagonal(x -> x * 10.0f);
            assertEquals(10.0f, m.get(0, 0), DELTA);
            assertEquals(50.0f, m.get(1, 1), DELTA);
            assertEquals(90.0f, m.get(2, 2), DELTA);
            assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x * 2.0f));
        }

        @Test
        public void testGetRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, m.getAntiDiagonal(), DELTA);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.setAntiDiagonal(new float[] { 10.5f, 20.5f, 30.5f });
            assertEquals(10.5f, m.get(0, 2), DELTA);
            assertEquals(20.5f, m.get(1, 1), DELTA);
            assertEquals(30.5f, m.get(2, 0), DELTA);
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new float[] { 1.0f }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new float[] { 1.0f, 2.0f }));
        }

        @Test
        public void testUpdateRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.updateAntiDiagonal(x -> x * 10.0f);
            assertEquals(30.0f, m.get(0, 2), DELTA);
            assertEquals(50.0f, m.get(1, 1), DELTA);
            assertEquals(70.0f, m.get(2, 0), DELTA);
            assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x * 2.0f));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAll(x -> x * 2.0f);
            assertEquals(2.0f, m.get(0, 0), DELTA);
            assertEquals(4.0f, m.get(0, 1), DELTA);
            assertEquals(6.0f, m.get(1, 0), DELTA);
            assertEquals(8.0f, m.get(1, 1), DELTA);
        }

        @Test
        public void testUpdateAll_withIndices() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f }, { 0.0f, 0.0f } });
            m.updateAll((i, j) -> (float) (i * 10 + j));
            assertEquals(0.0f, m.get(0, 0), DELTA);
            assertEquals(1.0f, m.get(0, 1), DELTA);
            assertEquals(10.0f, m.get(1, 0), DELTA);
            assertEquals(11.0f, m.get(1, 1), DELTA);
        }

        @Test
        public void testReplaceIf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            m.replaceIf(x -> x > 3.0f, 0.0f);
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(2.0f, m.get(0, 1), DELTA);
            assertEquals(3.0f, m.get(0, 2), DELTA);
            assertEquals(0.0f, m.get(1, 0), DELTA); // was 4.0f
            assertEquals(0.0f, m.get(1, 1), DELTA); // was 5.0f
            assertEquals(0.0f, m.get(1, 2), DELTA); // was 6.0f
        }

        @Test
        public void testReplaceIf_withIndices() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.replaceIf((i, j) -> i == j, 0.0f); // Replace diagonal
            assertEquals(0.0f, m.get(0, 0), DELTA);
            assertEquals(0.0f, m.get(1, 1), DELTA);
            assertEquals(0.0f, m.get(2, 2), DELTA);
            assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
        }

        @Test
        public void testMap() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.map(x -> x * 2.0f);
            assertEquals(2.0f, result.get(0, 0), DELTA);
            assertEquals(4.0f, result.get(0, 1), DELTA);
            assertEquals(6.0f, result.get(1, 0), DELTA);
            assertEquals(8.0f, result.get(1, 1), DELTA);

            // Original unchanged
            assertEquals(1.0f, m.get(0, 0), DELTA);
        }

        @Test
        public void testMapToObj() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.5f, 2.5f }, { 3.5f, 4.5f } });
            Matrix<String> result = m.mapToObj(x -> String.format("%.1f", x), String.class);
            assertEquals("1.5", result.get(0, 0));
            assertEquals("4.5", result.get(1, 1));
        }

        @Test
        public void testMapToDouble() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            DoubleMatrix result = m.toDoubleMatrix();
            assertEquals(1.0, result.get(0, 0), 0.0001);
            assertEquals(4.0, result.get(1, 1), 0.0001);
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.fill(99.5f);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals(99.5f, m.get(i, j), DELTA);
                }
            }
        }

        @Test
        public void testFill_withArray() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
            float[][] patch = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            m.copyFrom(patch);
            assertEquals(1.5f, m.get(0, 0), DELTA);
            assertEquals(2.5f, m.get(0, 1), DELTA);
            assertEquals(3.5f, m.get(1, 0), DELTA);
            assertEquals(4.5f, m.get(1, 1), DELTA);
            assertEquals(0.0f, m.get(2, 2), DELTA); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
            float[][] patch = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            m.copyFrom(1, 1, patch);
            assertEquals(0.0f, m.get(0, 0), DELTA); // unchanged
            assertEquals(1.5f, m.get(1, 1), DELTA);
            assertEquals(2.5f, m.get(1, 2), DELTA);
            assertEquals(3.5f, m.get(2, 1), DELTA);
            assertEquals(4.5f, m.get(2, 2), DELTA);
        }

        @Test
        public void testFill_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            float[][] patch = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1.0f, copy.get(0, 0), DELTA);

            // Modify copy shouldn't affect original
            copy.set(0, 0, 99.5f);
            assertEquals(1.0f, m.get(0, 0), DELTA);
            assertEquals(99.5f, copy.get(0, 0), DELTA);
        }

        @Test
        public void testCopy_withRowRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals(4.0f, subset.get(0, 0), DELTA);
            assertEquals(9.0f, subset.get(1, 2), DELTA);
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(2.0f, submatrix.get(0, 0), DELTA);
            assertEquals(6.0f, submatrix.get(1, 1), DELTA);
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1.0f, extended.get(0, 0), DELTA);
            assertEquals(4.0f, extended.get(1, 1), DELTA);
            assertEquals(0.0f, extended.get(3, 3), DELTA); // new cells are 0.0f
        }

        @Test
        public void testExtend_smaller() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1.0f, truncated.get(0, 0), DELTA);
            assertEquals(5.0f, truncated.get(1, 1), DELTA);
        }

        @Test
        public void testExtend_withDefaultValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix extended = m.resize(3, 3, -1.5f);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0f, extended.get(0, 0), DELTA);
            assertEquals(-1.5f, extended.get(2, 2), DELTA); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, 0.0f));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, 0.0f));
        }

        @Test
        public void testExtend_directional() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertEquals(1.0f, extended.get(1, 2), DELTA);
            assertEquals(5.0f, extended.get(2, 3), DELTA);

            // New cells are 0.0f
            assertEquals(0.0f, extended.get(0, 0), DELTA);
        }

        @Test
        public void testExtend_directionalWithDefault() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix extended = m.extend(1, 1, 1, 1, -1.5f);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals(1.0f, extended.get(1, 1), DELTA);

            // Check new values
            assertEquals(-1.5f, extended.get(0, 0), DELTA);
            assertEquals(-1.5f, extended.get(4, 4), DELTA);
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0.0f));
        }

        @Test
        public void testExtend_directional_allZeros() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.extend(0, 0, 0, 0, 5.5f);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0f, result.get(0, 0), DELTA);
            assertEquals(4.0f, result.get(1, 1), DELTA);
            assertFalse(result == m); // Should be a copy
        }

        @Test
        public void testExtend_mixedSmallerAndLarger() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix extended = m.resize(2, 1, 9.9f);
            assertEquals(2, extended.rowCount());
            assertEquals(1, extended.columnCount());
            assertEquals(1.0f, extended.get(0, 0), DELTA);
            assertEquals(4.0f, extended.get(1, 0), DELTA);
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            m.flipInPlaceHorizontally();
            assertEquals(3.0f, m.get(0, 0), DELTA);
            assertEquals(2.0f, m.get(0, 1), DELTA);
            assertEquals(1.0f, m.get(0, 2), DELTA);
            assertEquals(6.0f, m.get(1, 0), DELTA);
        }

        @Test
        public void testReverseV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            m.flipInPlaceVertically();
            assertEquals(5.0f, m.get(0, 0), DELTA);
            assertEquals(6.0f, m.get(0, 1), DELTA);
            assertEquals(3.0f, m.get(1, 0), DELTA);
            assertEquals(1.0f, m.get(2, 0), DELTA);
        }

        @Test
        public void testFlipH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix flipped = m.flipHorizontally();
            assertEquals(3.0f, flipped.get(0, 0), DELTA);
            assertEquals(2.0f, flipped.get(0, 1), DELTA);
            assertEquals(1.0f, flipped.get(0, 2), DELTA);

            // Original unchanged
            assertEquals(1.0f, m.get(0, 0), DELTA);
        }

        @Test
        public void testFlipV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix flipped = m.flipVertically();
            assertEquals(5.0f, flipped.get(0, 0), DELTA);
            assertEquals(3.0f, flipped.get(1, 0), DELTA);
            assertEquals(1.0f, flipped.get(2, 0), DELTA);

            // Original unchanged
            assertEquals(1.0f, m.get(0, 0), DELTA);
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0f, rotated.get(0, 0), DELTA);
            assertEquals(1.0f, rotated.get(0, 1), DELTA);
            assertEquals(4.0f, rotated.get(1, 0), DELTA);
            assertEquals(2.0f, rotated.get(1, 1), DELTA);
        }

        @Test
        public void testRotate180() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4.0f, rotated.get(0, 0), DELTA);
            assertEquals(3.0f, rotated.get(0, 1), DELTA);
            assertEquals(2.0f, rotated.get(1, 0), DELTA);
            assertEquals(1.0f, rotated.get(1, 1), DELTA);
        }

        @Test
        public void testRotate270() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2.0f, rotated.get(0, 0), DELTA);
            assertEquals(4.0f, rotated.get(0, 1), DELTA);
            assertEquals(1.0f, rotated.get(1, 0), DELTA);
            assertEquals(3.0f, rotated.get(1, 1), DELTA);
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0f, transposed.get(0, 0), DELTA);
            assertEquals(4.0f, transposed.get(0, 1), DELTA);
            assertEquals(2.0f, transposed.get(1, 0), DELTA);
            assertEquals(5.0f, transposed.get(1, 1), DELTA);
            assertEquals(3.0f, transposed.get(2, 0), DELTA);
            assertEquals(6.0f, transposed.get(2, 1), DELTA);
        }

        @Test
        public void testTranspose_square() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0f, transposed.get(0, 0), DELTA);
            assertEquals(3.0f, transposed.get(0, 1), DELTA);
            assertEquals(2.0f, transposed.get(1, 0), DELTA);
            assertEquals(4.0f, transposed.get(1, 1), DELTA);
        }

        @Test
        public void testTranspose_moreRowsThanCols() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
            assertEquals(1.0f, transposed.get(0, 0), DELTA);
            assertEquals(3.0f, transposed.get(0, 1), DELTA);
            assertEquals(5.0f, transposed.get(0, 2), DELTA);
            assertEquals(2.0f, transposed.get(1, 0), DELTA);
            assertEquals(4.0f, transposed.get(1, 1), DELTA);
            assertEquals(6.0f, transposed.get(1, 2), DELTA);
        }

        @Test
        public void testRotate90_moreColsThanRows() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4.0f, rotated.get(0, 0), DELTA);
            assertEquals(1.0f, rotated.get(0, 1), DELTA);
        }

        @Test
        public void testRotate90_moreRowsThanCols() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals(5.0f, rotated.get(0, 0), DELTA);
            assertEquals(3.0f, rotated.get(0, 1), DELTA);
            assertEquals(1.0f, rotated.get(0, 2), DELTA);
        }

        @Test
        public void testRotate270_moreColsThanRows() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix rotated = m.rotate270();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0f, rotated.get(0, 0), DELTA);
            assertEquals(6.0f, rotated.get(0, 1), DELTA);
        }

        @Test
        public void testRotate270_moreRowsThanCols() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals(2.0f, rotated.get(0, 0), DELTA);
            assertEquals(4.0f, rotated.get(0, 1), DELTA);
            assertEquals(6.0f, rotated.get(0, 2), DELTA);
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, reshaped.get(0, i), DELTA);
            }
        }

        @Test
        public void testReshape_back() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix reshaped = m.reshape(1, 9);
            FloatMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            FloatMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        @Test
        public void testReshape_singleRowMatrix() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f } });
            FloatMatrix reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1.0f, reshaped.get(0, 0), DELTA);
            assertEquals(2.0f, reshaped.get(0, 1), DELTA);
            assertEquals(3.0f, reshaped.get(0, 2), DELTA);
            assertEquals(4.0f, reshaped.get(1, 0), DELTA);
            assertEquals(5.0f, reshaped.get(1, 1), DELTA);
            assertEquals(6.0f, reshaped.get(1, 2), DELTA);
        }

        @Test
        public void testReshape_toZeroDimensions_throwsForNonEmpty() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            // New shape 0x0 is too small to hold all 4 elements
            assertThrows(IllegalArgumentException.class, () -> m.reshape(0, 0));

            // Empty matrix can be reshaped to 0x0
            FloatMatrix empty = FloatMatrix.empty();
            FloatMatrix reshaped = empty.reshape(0, 0);
            assertEquals(0, reshaped.rowCount());
            assertEquals(0, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1.0f, repeated.get(0, 0), DELTA);
            assertEquals(1.0f, repeated.get(0, 1), DELTA);
            assertEquals(1.0f, repeated.get(0, 2), DELTA);
            assertEquals(2.0f, repeated.get(0, 3), DELTA);
            assertEquals(2.0f, repeated.get(0, 4), DELTA);
            assertEquals(2.0f, repeated.get(0, 5), DELTA);
            assertEquals(1.0f, repeated.get(1, 0), DELTA); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1.0f, repeated.get(0, 0), DELTA);
            assertEquals(2.0f, repeated.get(0, 1), DELTA);
            assertEquals(1.0f, repeated.get(0, 2), DELTA); // repeat starts
            assertEquals(2.0f, repeated.get(0, 3), DELTA);

            assertEquals(3.0f, repeated.get(1, 0), DELTA);
            assertEquals(4.0f, repeated.get(1, 1), DELTA);

            // Check vertical repeat
            assertEquals(1.0f, repeated.get(2, 0), DELTA); // vertical repeat starts
            assertEquals(2.0f, repeated.get(2, 1), DELTA);
        }

        @Test
        public void testRepmat_invalidArguments() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatList flat = m.flatten();
            assertEquals(9, flat.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, flat.get(i), DELTA);
            }
        }

        @Test
        public void testFlatten_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            FloatList flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            List<Float> sums = new ArrayList<>();
            m.applyOnFlattened(row -> {
                float sum = 0.0f;
                for (float val : row) {
                    sum += val;
                }
                sums.add(sum);
            });
            assertEquals(1, sums.size());
            assertEquals(45.0f, sums.get(0), DELTA);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 7.0f, 8.0f, 9.0f }, { 10.0f, 11.0f, 12.0f } });
            FloatMatrix stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals(1.0f, stacked.get(0, 0), DELTA);
            assertEquals(7.0f, stacked.get(2, 0), DELTA);
            assertEquals(12.0f, stacked.get(3, 2), DELTA);
        }

        @Test
        public void testVstack_differentColumnCounts() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1.0f, stacked.get(0, 0), DELTA);
            assertEquals(5.0f, stacked.get(0, 2), DELTA);
            assertEquals(8.0f, stacked.get(1, 3), DELTA);
        }

        @Test
        public void testHstack_differentRowCounts() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix sum = m1.add(m2);

            assertEquals(6.0f, sum.get(0, 0), DELTA);
            assertEquals(8.0f, sum.get(0, 1), DELTA);
            assertEquals(10.0f, sum.get(1, 0), DELTA);
            assertEquals(12.0f, sum.get(1, 1), DELTA);
        }

        @Test
        public void testAdd_differentDimensions() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix diff = m1.subtract(m2);

            assertEquals(4.0f, diff.get(0, 0), DELTA);
            assertEquals(4.0f, diff.get(0, 1), DELTA);
            assertEquals(4.0f, diff.get(1, 0), DELTA);
            assertEquals(4.0f, diff.get(1, 1), DELTA);
        }

        @Test
        public void testSubtract_differentDimensions() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix product = m1.multiply(m2);

            assertEquals(19.0f, product.get(0, 0), DELTA); // 1*5 + 2*7
            assertEquals(22.0f, product.get(0, 1), DELTA); // 1*6 + 2*8
            assertEquals(43.0f, product.get(1, 0), DELTA); // 3*5 + 4*7
            assertEquals(50.0f, product.get(1, 1), DELTA); // 3*6 + 4*8
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        @Test
        public void testMultiply_rectangularMatrices() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } }); // 1x3
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 4.0f }, { 5.0f }, { 6.0f } }); // 3x1
            FloatMatrix product = m1.multiply(m2);

            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(32.0f, product.get(0, 0), DELTA); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            Matrix<Float> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Float.valueOf(1.0f), boxed.get(0, 0));
            assertEquals(Float.valueOf(6.0f), boxed.get(1, 2));
        }

        @Test
        public void testBoxed_moreRowsThanCols() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            Matrix<Float> boxed = m.boxed();
            assertEquals(4, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Float.valueOf(1.0f), boxed.get(0, 0));
            assertEquals(Float.valueOf(8.0f), boxed.get(3, 1));
        }

        @Test
        public void testBoxed_withSpecialValues() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { Float.NaN, Float.POSITIVE_INFINITY }, { Float.NEGATIVE_INFINITY, -0.0f } });
            Matrix<Float> boxed = m.boxed();
            assertTrue(Float.isNaN(boxed.get(0, 0)));
            assertEquals(Float.POSITIVE_INFINITY, boxed.get(0, 1));
            assertEquals(Float.NEGATIVE_INFINITY, boxed.get(1, 0));
            assertEquals(-0.0f, boxed.get(1, 1));
        }

        @Test
        public void testToDoubleMatrix() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(2, doubleMatrix.rowCount());
            assertEquals(2, doubleMatrix.columnCount());
            assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
            assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);

            assertEquals(5.0f, result.get(0, 0), DELTA); // 1*5
            assertEquals(12.0f, result.get(0, 1), DELTA); // 2*6
            assertEquals(21.0f, result.get(1, 0), DELTA); // 3*7
            assertEquals(32.0f, result.get(1, 1), DELTA); // 4*8
        }

        @Test
        public void testZipWith_differentShapes() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
        }

        @Test
        public void testZipWith_threeMatrices() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 9.0f, 10.0f }, { 11.0f, 12.0f } });
            FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15.0f, result.get(0, 0), DELTA); // 1+5+9
            assertEquals(18.0f, result.get(0, 1), DELTA); // 2+6+10
            assertEquals(21.0f, result.get(1, 0), DELTA); // 3+7+11
            assertEquals(24.0f, result.get(1, 1), DELTA); // 4+8+12
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 9.0f, 10.0f, 11.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal, DELTA);
        }

        @Test
        public void testStreamLU2RD_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().toArray().length);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] antiDiagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, antiDiagonal, DELTA);
        }

        @Test
        public void testStreamRU2LD_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().toArray().length);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            float[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }, all, DELTA);
        }

        @Test
        public void testStreamH_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.streamHorizontal().toArray().length);
        }

        @Test
        public void testStreamH_withRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            float[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, row1, DELTA);
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, rows, DELTA);
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            float[] all = m.streamVertical().toArray();
            assertArrayEquals(new float[] { 1.0f, 4.0f, 2.0f, 5.0f, 3.0f, 6.0f }, all, DELTA);
        }

        @Test
        public void testStreamV_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.streamVertical().toArray().length);
        }

        @Test
        public void testStreamV_withColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            float[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new float[] { 2.0f, 5.0f }, col1, DELTA);
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f }, columnCount, DELTA);
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            List<float[]> rows = m.streamRows().map(FloatStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, rows.get(0), DELTA);
            assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(1), DELTA);
        }

        @Test
        public void testStreamR_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            List<float[]> rows = m.streamRows(1, 3).map(FloatStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(0), DELTA);
            assertArrayEquals(new float[] { 7.0f, 8.0f, 9.0f }, rows.get(1), DELTA);
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            List<float[]> columnCount = m.streamColumns().map(FloatStream::toArray).toList();
            assertEquals(3, columnCount.size());
            assertArrayEquals(new float[] { 1.0f, 4.0f }, columnCount.get(0), DELTA);
            assertArrayEquals(new float[] { 2.0f, 5.0f }, columnCount.get(1), DELTA);
            assertArrayEquals(new float[] { 3.0f, 6.0f }, columnCount.get(2), DELTA);
        }

        @Test
        public void testStreamC_empty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            List<float[]> columnCount = m.streamColumns(1, 3).map(FloatStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, columnCount.get(0), DELTA);
            assertArrayEquals(new float[] { 3.0f, 6.0f, 9.0f }, columnCount.get(1), DELTA);
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            List<Float> values = new ArrayList<>();
            m.forEach(v -> values.add(v));
            assertEquals(9, values.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, values.get(i), DELTA);
            }
        }

        @Test
        public void testForEach_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            List<Float> values = new ArrayList<>();
            m.forEach(0, 2, 1, 3, v -> values.add(v));
            assertEquals(4, values.size());
            assertEquals(2.0f, values.get(0), DELTA);
            assertEquals(3.0f, values.get(1), DELTA);
            assertEquals(5.0f, values.get(2), DELTA);
            assertEquals(6.0f, values.get(3), DELTA);
        }

        @Test
        public void testForEach_withRange_outOfBounds() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, v -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, v -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, -1, 2, v -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, 0, 3, v -> {
            }));
        }

        @Test
        public void testForEach_withSpecialValues() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { Float.NaN, Float.POSITIVE_INFINITY }, { Float.NEGATIVE_INFINITY, -0.0f } });
            List<Float> values = new ArrayList<>();
            m.forEach(v -> values.add(v));
            assertEquals(4, values.size());
            assertTrue(Float.isNaN(values.get(0)));
            assertEquals(Float.POSITIVE_INFINITY, values.get(1), DELTA);
            assertEquals(Float.NEGATIVE_INFINITY, values.get(2), DELTA);
            assertEquals(-0.0f, values.get(3), DELTA);
        }

        @Test
        public void testForEach_largeMatrix_parallelable() {
            // Create a large matrix that should trigger parallel processing
            int size = 1000;
            float[][] data = new float[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    data[i][j] = i + j;
                }
            }
            FloatMatrix large = FloatMatrix.of(data);
            final float[] sum = { 0.0f };
            large.forEach(1, 10, 1, 10, v -> sum[0] += v);
            assertTrue(sum[0] > 0);
        }

        // ============ Object Methods Tests ============

        @Test
        public void testPrintln() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertFalse(m.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(m::println);

            FloatMatrix empty = FloatMatrix.empty();
            assertTrue(empty.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(empty::println);
        }

        @Test
        public void testHashCode() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 4.0f, 3.0f } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 4.0f, 3.0f } });
            FloatMatrix m4 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0") || str.contains("1"));
            assertTrue(str.contains("2.0") || str.contains("2"));
            assertTrue(str.contains("3.0") || str.contains("3"));
            assertTrue(str.contains("4.0") || str.contains("4"));
        }

        // ============ Statistical Operations Tests ============

        @Test
        public void testStatisticalOperations() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });

            // Test sum
            double totalSum = m.streamHorizontal().sum();
            assertEquals(45.0, totalSum, DELTA); // 1+2+3+4+5+6+7+8+9 = 45

            // Test sum of specific row
            double row1Sum = m.streamHorizontal(1).sum();
            assertEquals(15.0, row1Sum, DELTA); // 4+5+6 = 15

            // Test sum of specific column
            double col0Sum = m.streamVertical(0).sum();
            assertEquals(12.0, col0Sum, DELTA); // 1+4+7 = 12

            // Test min/max
            OptionalFloat min = m.streamHorizontal().min();
            assertTrue(min.isPresent());
            assertEquals(1.0f, min.get(), DELTA);

            OptionalFloat max = m.streamHorizontal().max();
            assertTrue(max.isPresent());
            assertEquals(9.0f, max.get(), DELTA);

            // Test average
            OptionalDouble avg = m.streamHorizontal().average();
            assertTrue(avg.isPresent());
            assertEquals(5.0, avg.get(), DELTA);

            // Test diagonal operations
            double diagonalSum = m.streamMainDiagonal().sum();
            assertEquals(15.0, diagonalSum, DELTA); // 1+5+9 = 15

            double antiDiagonalSum = m.streamAntiDiagonal().sum();
            assertEquals(15.0, antiDiagonalSum, DELTA); // 3+5+7 = 15
        }

        @Test
        public void testRowColumnStatistics() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });

            // Test statistics on individual rows
            List<Double> rowSums = m.streamRows().map(row -> row.sum()).toList();
            assertEquals(3, rowSums.size());
            assertEquals(6.0, rowSums.get(0), DELTA); // 1+2+3
            assertEquals(15.0, rowSums.get(1), DELTA); // 4+5+6
            assertEquals(24.0, rowSums.get(2), DELTA); // 7+8+9

            // Test statistics on individual columns
            List<Double> colSums = m.streamColumns().map(col -> col.sum()).toList();
            assertEquals(3, colSums.size());
            assertEquals(12.0, colSums.get(0), DELTA); // 1+4+7
            assertEquals(15.0, colSums.get(1), DELTA); // 2+5+8
            assertEquals(18.0, colSums.get(2), DELTA); // 3+6+9
        }

        // ============ Edge Case Tests ============

        @Test
        public void testEmptyMatrixOperations() {
            FloatMatrix empty = FloatMatrix.empty();

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            FloatMatrix extended = empty.resize(2, 2, 5.5f);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(5.5f, extended.get(0, 0), DELTA);
        }

        @Test
        public void testArithmeticEdgeCases() {
            // Test with zero matrix
            FloatMatrix zeros = FloatMatrix.of(new float[][] { { 0.0f, 0.0f }, { 0.0f, 0.0f } });
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

            FloatMatrix addZero = m.add(zeros);
            assertEquals(m, addZero);

            FloatMatrix subtractZero = m.subtract(zeros);
            assertEquals(m, subtractZero);

            // Test multiplication with zero matrix
            FloatMatrix multiplyZero = m.multiply(zeros);
            assertEquals(zeros, multiplyZero);

            // Test addition commutativity
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            assertEquals(m1.add(m2), m2.add(m1));

            // Test subtraction anti-commutativity
            FloatMatrix diff1 = m1.subtract(m2);
            FloatMatrix diff2 = m2.subtract(m1);
            assertEquals(diff1.get(0, 0), -diff2.get(0, 0), DELTA);
            assertEquals(diff1.get(1, 1), -diff2.get(1, 1), DELTA);
        }

        @Test
        public void testScalarOperationsWithMap() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

            // Scalar addition
            FloatMatrix addScalar = m.map(x -> x + 10.0f);
            assertEquals(11.0f, addScalar.get(0, 0), DELTA);
            assertEquals(14.0f, addScalar.get(1, 1), DELTA);

            // Scalar multiplication
            FloatMatrix multiplyScalar = m.map(x -> x * 3.0f);
            assertEquals(3.0f, multiplyScalar.get(0, 0), DELTA);
            assertEquals(12.0f, multiplyScalar.get(1, 1), DELTA);

            // Scalar division
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 10.0f, 20.0f }, { 30.0f, 40.0f } });
            FloatMatrix divideScalar = m2.map(x -> x / 10.0f);
            assertEquals(1.0f, divideScalar.get(0, 0), DELTA);
            assertEquals(4.0f, divideScalar.get(1, 1), DELTA);
        }

        @Test
        public void testElementWiseMultiplyWithZipWith() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f, 3.0f }, { 4.0f, 5.0f } });

            // Element-wise multiplication
            FloatMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(2.0f, elementWiseProduct.get(0, 0), DELTA); // 1*2
            assertEquals(6.0f, elementWiseProduct.get(0, 1), DELTA); // 2*3
            assertEquals(12.0f, elementWiseProduct.get(1, 0), DELTA); // 3*4
            assertEquals(20.0f, elementWiseProduct.get(1, 1), DELTA); // 4*5

            // Element-wise division
            FloatMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
            assertEquals(2.0f, elementWiseDivision.get(0, 0), DELTA); // 2/1
            assertEquals(1.5f, elementWiseDivision.get(0, 1), DELTA); // 3/2
            assertEquals(1.333333f, elementWiseDivision.get(1, 0), DELTA); // 4/3
            assertEquals(1.25f, elementWiseDivision.get(1, 1), DELTA); // 5/4
        }

        // ============ Float Special Value Tests ============

        @Test
        public void testNaNOperations() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { Float.NaN, 2.0f }, { 3.0f, 4.0f } });

            assertTrue(Float.isNaN(m.get(0, 0)));
            assertFalse(Float.isNaN(m.get(0, 1)));

            // Test that NaN propagates in arithmetic
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 1.0f }, { 1.0f, 1.0f } });
            FloatMatrix sum = m.add(m2);
            assertTrue(Float.isNaN(sum.get(0, 0)));
            assertEquals(3.0f, sum.get(0, 1), DELTA);
        }

        @Test
        public void testInfinityOperations() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { Float.POSITIVE_INFINITY, 2.0f }, { Float.NEGATIVE_INFINITY, 4.0f } });

            assertEquals(Float.POSITIVE_INFINITY, m.get(0, 0), DELTA);
            assertEquals(Float.NEGATIVE_INFINITY, m.get(1, 0), DELTA);

            // Test arithmetic with infinity
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 1.0f }, { 1.0f, 1.0f } });
            FloatMatrix sum = m.add(m2);
            assertEquals(Float.POSITIVE_INFINITY, sum.get(0, 0), DELTA);
            assertEquals(Float.NEGATIVE_INFINITY, sum.get(1, 0), DELTA);
        }

        @Test
        public void testNegativeZero() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { -0.0f, 0.0f }, { 1.0f, -1.0f } });

            // Test that -0.0f and 0.0f are treated differently in bit representation
            // but equal in value comparison
            assertEquals(-0.0f, m.get(0, 0), DELTA);
            assertEquals(0.0f, m.get(0, 1), DELTA);

            // They should be equal in value
            assertTrue(m.get(0, 0) == 0.0f);
            assertTrue(m.get(0, 1) == 0.0f);
        }

        @Test
        public void testFloatPrecisionLimits() {
            // Test float precision limits
            FloatMatrix m = FloatMatrix.of(new float[][] { { Float.MAX_VALUE, Float.MIN_VALUE }, { Float.MIN_NORMAL, 1e-45f } });

            assertEquals(Float.MAX_VALUE, m.get(0, 0), DELTA);
            assertEquals(Float.MIN_VALUE, m.get(0, 1), DELTA);
            assertEquals(Float.MIN_NORMAL, m.get(1, 0), DELTA);
            assertEquals(1e-45f, m.get(1, 1), DELTA);

            // Test overflow
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { Float.MAX_VALUE } });
            FloatMatrix overflow = m2.map(x -> x * 2.0f);
            assertEquals(Float.POSITIVE_INFINITY, overflow.get(0, 0), DELTA);
        }

        @Test
        public void testFloatPrecisionInArithmetic() {
            // Test precision loss in float arithmetic
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 0.1f, 0.2f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 0.3f, 0.4f } });

            FloatMatrix sum = m1.add(m2);

            // Float arithmetic may have precision issues
            assertEquals(0.4f, sum.get(0, 0), 0.001f); // More lenient delta for precision
            assertEquals(0.6f, sum.get(0, 1), 0.001f);
        }

        @Test
        public void testVerySmallValues() {
            // Test with very small values near zero
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1e-20f, 1e-30f }, { 1e-38f, 1e-40f } });

            assertEquals(1e-20f, m.get(0, 0), 1e-21f);
            assertEquals(1e-30f, m.get(0, 1), 1e-31f);

            // Values near the limit of float precision
            assertTrue(m.get(1, 0) > 0);
            assertTrue(m.get(1, 1) >= 0); // May underflow to 0
        }

        @Test
        public void testIntToFloatConversion() {
            // Test precision when converting large integers to float
            int[][] largeInts = { { 16777216, 16777217 }, { -16777216, -16777217 } };
            FloatMatrix m = FloatMatrix.from(largeInts);

            // 16777216 can be exactly represented in float
            assertEquals(16777216.0f, m.get(0, 0), DELTA);

            // 16777217 may lose precision in float representation
            float converted = m.get(0, 1);
            assertTrue(Math.abs(converted - 16777217.0f) < 2.0f);
        }
    }

    @Nested
    @Tag("2510")
    class FloatMatrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            FloatMatrix m = new FloatMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(4.0f, m.get(1, 1));
        }

        @Test
        public void testConstructor_withSingleElement() {
            FloatMatrix m = new FloatMatrix(new float[][] { { 42.5f } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.5f, m.get(0, 0));
        }

        @Test
        public void testOf_withValidArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            FloatMatrix m = FloatMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
        }

        @Test
        public void testCreateFromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            FloatMatrix m = FloatMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(4.0f, m.get(1, 1));
        }

        @Test
        public void testRandom_withZeroLength() {
            FloatMatrix m = FloatMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRepeat() {
            FloatMatrix m = FloatMatrix.repeat(1, 5, 3.14f);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14f, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withZeroLength() {
            FloatMatrix m = FloatMatrix.repeat(1, 0, 1.0f);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonalLU2RD() {
            FloatMatrix m = FloatMatrix.mainDiagonal(new float[] { 1.0f, 2.0f, 3.0f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(2.0f, m.get(1, 1));
            assertEquals(3.0f, m.get(2, 2));
            assertEquals(0.0f, m.get(0, 1));
            assertEquals(0.0f, m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            FloatMatrix m = FloatMatrix.antiDiagonal(new float[] { 1.0f, 2.0f, 3.0f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 2));
            assertEquals(2.0f, m.get(1, 1));
            assertEquals(3.0f, m.get(2, 0));
            assertEquals(0.0f, m.get(0, 0));
            assertEquals(0.0f, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            FloatMatrix m = FloatMatrix.diagonals(new float[] { 1.0f, 4.0f }, new float[] { 2.0f, 3.0f });
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(2.0f, m.get(0, 1));
            assertEquals(3.0f, m.get(1, 0));
            assertEquals(4.0f, m.get(1, 1));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            FloatMatrix m = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(2.0f, m.get(1, 1));
            assertEquals(3.0f, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            FloatMatrix m = FloatMatrix.diagonals(null, new float[] { 1.0f, 2.0f, 3.0f });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 2));
            assertEquals(2.0f, m.get(1, 1));
            assertEquals(3.0f, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothEmpty() {
            FloatMatrix m = FloatMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonals(new float[] { 1.0f, 2.0f }, new float[] { 1.0f, 2.0f, 3.0f }));
        }

        @Test
        public void testUnbox() {
            Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m = FloatMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(4.0f, m.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, null }, { null, 4.0f } });
            FloatMatrix m = FloatMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(0.0f, m.get(0, 1));
            assertEquals(0.0f, m.get(1, 0));
            assertEquals(4.0f, m.get(1, 1));
        }

        @Test
        public void testGet() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(2.0f, m.get(0, 1));
            assertEquals(3.0f, m.get(1, 0));
            assertEquals(4.0f, m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(2.0f, m.get(Point.of(0, 1)));
            assertEquals(3.0f, m.get(Point.of(1, 0)));
        }

        @Test
        public void testSet() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(0, 1, 9.0f);
            assertEquals(9.0f, m.get(0, 1));
        }

        @Test
        public void testSet_withPoint() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(Point.of(0, 1), 9.0f);
            assertEquals(9.0f, m.get(0, 1));
        }

        // ============ Neighbor Tests ============

        @Test
        public void testUpOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.0f, up.get());
        }

        @Test
        public void testDownOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.0f, down.get());
        }

        @Test
        public void testLeftOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.0f, left.get());
        }

        @Test
        public void testRightOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.0f, right.get());
        }
        // ============ Row/Column Access Tests ============

        @Test
        public void testRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            float[] row = m.rowView(0);
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, row);
        }

        @Test
        public void testColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            float[] col = m.columnCopy(0);
            assertArrayEquals(new float[] { 1.0f, 4.0f }, col);
        }

        @Test
        public void testSetRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setRow(0, new float[] { 7.0f, 8.0f });
            assertEquals(7.0f, m.get(0, 0));
            assertEquals(8.0f, m.get(0, 1));
        }

        @Test
        public void testSetRow_wrongLength() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 7.0f }));
        }

        @Test
        public void testSetColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setColumn(0, new float[] { 7.0f, 8.0f });
            assertEquals(7.0f, m.get(0, 0));
            assertEquals(8.0f, m.get(1, 0));
        }

        @Test
        public void testSetColumn_wrongLength() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 7.0f }));
        }

        @Test
        public void testUpdateRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateRow(0, x -> x * 2);
            assertEquals(2.0f, m.get(0, 0));
            assertEquals(4.0f, m.get(0, 1));
        }

        @Test
        public void testUpdateColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateColumn(0, x -> x + 10);
            assertEquals(11.0f, m.get(0, 0));
            assertEquals(13.0f, m.get(1, 0));
        }

        // ============ Diagonal Tests ============

        @Test
        public void testGetLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diagonal = m.getMainDiagonal();
            assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal);
        }

        @Test
        public void testSetLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setMainDiagonal(new float[] { 9.0f, 8.0f });
            assertEquals(9.0f, m.get(0, 0));
            assertEquals(8.0f, m.get(1, 1));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new float[] { 9.0f, 8.0f }));
        }

        @Test
        public void testUpdateLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateMainDiagonal(x -> x * 2);
            assertEquals(2.0f, m.get(0, 0));
            assertEquals(8.0f, m.get(1, 1));
        }

        @Test
        public void testGetRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diagonal = m.getAntiDiagonal();
            assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, diagonal);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setAntiDiagonal(new float[] { 9.0f, 8.0f });
            assertEquals(9.0f, m.get(0, 1));
            assertEquals(8.0f, m.get(1, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAntiDiagonal(x -> x + 10);
            assertEquals(12.0f, m.get(0, 1));
            assertEquals(13.0f, m.get(1, 0));
        }

        // ============ Update/Transform Tests ============

        @Test
        public void testUpdateAll_unaryOperator() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAll(x -> x * 2);
            assertEquals(2.0f, m.get(0, 0));
            assertEquals(4.0f, m.get(0, 1));
            assertEquals(6.0f, m.get(1, 0));
            assertEquals(8.0f, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAll((i, j) -> (float) (i + j));
            assertEquals(0.0f, m.get(0, 0));
            assertEquals(1.0f, m.get(0, 1));
            assertEquals(1.0f, m.get(1, 0));
            assertEquals(2.0f, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.replaceIf(x -> x > 2, 0.0f);
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(2.0f, m.get(0, 1));
            assertEquals(0.0f, m.get(1, 0));
            assertEquals(0.0f, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.replaceIf((i, j) -> i == j, 0.0f);
            assertEquals(0.0f, m.get(0, 0));
            assertEquals(2.0f, m.get(0, 1));
            assertEquals(3.0f, m.get(1, 0));
            assertEquals(0.0f, m.get(1, 1));
        }

        @Test
        public void testMap() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.map(x -> x * 2);
            assertEquals(2.0f, result.get(0, 0));
            assertEquals(4.0f, result.get(0, 1));
            assertEquals(6.0f, result.get(1, 0));
            assertEquals(8.0f, result.get(1, 1));
            assertEquals(1.0f, m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
            assertEquals("1.0", result.get(0, 0));
            assertEquals("4.0", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_singleValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.fill(9.0f);
            assertEquals(9.0f, m.get(0, 0));
            assertEquals(9.0f, m.get(0, 1));
            assertEquals(9.0f, m.get(1, 0));
            assertEquals(9.0f, m.get(1, 1));
        }

        @Test
        public void testFill_array() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.copyFrom(new float[][] { { 7.0f, 8.0f }, { 9.0f, 10.0f } });
            assertEquals(7.0f, m.get(0, 0));
            assertEquals(8.0f, m.get(0, 1));
            assertEquals(9.0f, m.get(1, 0));
            assertEquals(10.0f, m.get(1, 1));
        }

        @Test
        public void testFill_withPosition() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.copyFrom(1, 1, new float[][] { { 11.0f, 12.0f }, { 13.0f, 14.0f } });
            assertEquals(1.0f, m.get(0, 0));
            assertEquals(11.0f, m.get(1, 1));
            assertEquals(12.0f, m.get(1, 2));
            assertEquals(13.0f, m.get(2, 1));
            assertEquals(14.0f, m.get(2, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix copy = m.copy();
            assertEquals(1.0f, copy.get(0, 0));
            assertEquals(4.0f, copy.get(1, 1));
            copy.set(0, 0, 99.0f);
            assertEquals(1.0f, m.get(0, 0));
        }

        @Test
        public void testCopy_rows() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3.0f, copy.get(0, 0));
            assertEquals(6.0f, copy.get(1, 1));
        }

        @Test
        public void testCopy_subMatrix() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5.0f, copy.get(0, 0));
            assertEquals(9.0f, copy.get(1, 1));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_simple() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0f, extended.get(0, 0));
            assertEquals(4.0f, extended.get(1, 1));
            assertEquals(0.0f, extended.get(2, 2));
        }

        @Test
        public void testExtend_withDefaultValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix extended = m.resize(3, 3, 9.0f);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(9.0f, extended.get(2, 2));
        }

        @Test
        public void testExtend_directions() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 5.0f } });
            FloatMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5.0f, extended.get(1, 1));
            assertEquals(0.0f, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionsWithDefault() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 5.0f } });
            FloatMatrix extended = m.extend(1, 1, 1, 1, 9.0f);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5.0f, extended.get(1, 1));
            assertEquals(9.0f, extended.get(0, 0));
        }

        // ============ Flip/Reverse Tests ============

        @Test
        public void testReverseH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.flipInPlaceHorizontally();
            assertEquals(2.0f, m.get(0, 0));
            assertEquals(1.0f, m.get(0, 1));
            assertEquals(4.0f, m.get(1, 0));
            assertEquals(3.0f, m.get(1, 1));
        }

        @Test
        public void testReverseV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.flipInPlaceVertically();
            assertEquals(3.0f, m.get(0, 0));
            assertEquals(4.0f, m.get(0, 1));
            assertEquals(1.0f, m.get(1, 0));
            assertEquals(2.0f, m.get(1, 1));
        }

        @Test
        public void testFlipH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix flipped = m.flipHorizontally();
            assertEquals(2.0f, flipped.get(0, 0));
            assertEquals(1.0f, flipped.get(0, 1));
            assertEquals(1.0f, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix flipped = m.flipVertically();
            assertEquals(3.0f, flipped.get(0, 0));
            assertEquals(4.0f, flipped.get(0, 1));
            assertEquals(1.0f, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0f, rotated.get(0, 0));
            assertEquals(1.0f, rotated.get(0, 1));
            assertEquals(4.0f, rotated.get(1, 0));
            assertEquals(2.0f, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate180();
            assertEquals(4.0f, rotated.get(0, 0));
            assertEquals(3.0f, rotated.get(0, 1));
            assertEquals(2.0f, rotated.get(1, 0));
            assertEquals(1.0f, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate270();
            assertEquals(2.0f, rotated.get(0, 0));
            assertEquals(4.0f, rotated.get(0, 1));
            assertEquals(1.0f, rotated.get(1, 0));
            assertEquals(3.0f, rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0f, transposed.get(0, 0));
            assertEquals(4.0f, transposed.get(0, 1));
            assertEquals(3.0f, transposed.get(2, 0));
            assertEquals(6.0f, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(1.0f, transposed.get(0, 0));
            assertEquals(3.0f, transposed.get(0, 1));
            assertEquals(2.0f, transposed.get(1, 0));
            assertEquals(4.0f, transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f } });
            FloatMatrix reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1.0f, reshaped.get(0, 0));
            assertEquals(4.0f, reshaped.get(1, 0));
        }

        // ============ Repelem Tests ============

        @Test
        public void testRepelem() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0f, result.get(0, 0));
            assertEquals(1.0f, result.get(0, 1));
            assertEquals(1.0f, result.get(1, 0));
            assertEquals(1.0f, result.get(1, 1));
            assertEquals(4.0f, result.get(2, 2));
            assertEquals(4.0f, result.get(3, 3));
        }

        // ============ Repmat Tests ============

        @Test
        public void testRepmat() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.repeatMatrix(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0f, result.get(0, 0));
            assertEquals(2.0f, result.get(0, 1));
            assertEquals(1.0f, result.get(0, 2));
            assertEquals(2.0f, result.get(0, 3));
            assertEquals(1.0f, result.get(2, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1.0f, flat.get(0));
            assertEquals(2.0f, flat.get(1));
            assertEquals(3.0f, flat.get(2));
            assertEquals(4.0f, flat.get(3));
        }

        @Test
        public void testFlatOp() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            final float[] sum = { 0.0f };
            m.applyOnFlattened(row -> {
                for (float val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10.0f, sum[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix stacked = m1.stackVertically(m2);
            assertEquals(4, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1.0f, stacked.get(0, 0));
            assertEquals(5.0f, stacked.get(2, 0));
        }

        @Test
        public void testVstack_incompatibleColumns() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1.0f, stacked.get(0, 0));
            assertEquals(5.0f, stacked.get(0, 2));
        }

        @Test
        public void testHstack_incompatibleRows() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Tests ============

        @Test
        public void testAdd() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix result = m1.add(m2);
            assertEquals(6.0f, result.get(0, 0));
            assertEquals(8.0f, result.get(0, 1));
            assertEquals(10.0f, result.get(1, 0));
            assertEquals(12.0f, result.get(1, 1));
        }

        @Test
        public void testAdd_incompatibleSize() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m1.subtract(m2);
            assertEquals(4.0f, result.get(0, 0));
            assertEquals(4.0f, result.get(0, 1));
            assertEquals(4.0f, result.get(1, 0));
            assertEquals(4.0f, result.get(1, 1));
        }

        @Test
        public void testMultiply() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f, 0.0f }, { 1.0f, 2.0f } });
            FloatMatrix result = m1.multiply(m2);
            assertEquals(4.0f, result.get(0, 0));
            assertEquals(4.0f, result.get(0, 1));
            assertEquals(10.0f, result.get(1, 0));
            assertEquals(8.0f, result.get(1, 1));
        }

        @Test
        public void testMultiply_incompatibleSize() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Matrix<Float> boxed = m.boxed();
            assertEquals(Float.valueOf(1.0f), boxed.get(0, 0));
            assertEquals(Float.valueOf(4.0f), boxed.get(1, 1));
        }

        @Test
        public void testToDoubleMatrix() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            DoubleMatrix dm = m.toDoubleMatrix();
            assertEquals(2, dm.rowCount());
            assertEquals(2, dm.columnCount());
            assertEquals(1.0, dm.get(0, 0));
            assertEquals(4.0, dm.get(1, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_binary() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix result = m1.zipWith(m2, (a, b) -> a + b);
            assertEquals(6.0f, result.get(0, 0));
            assertEquals(12.0f, result.get(1, 1));
        }

        @Test
        public void testZipWith_ternary() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 1.0f }, { 1.0f, 1.0f } });
            FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(7.0f, result.get(0, 0));
            assertEquals(13.0f, result.get(1, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatStream stream = m.streamMainDiagonal();
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, result);
        }

        @Test
        public void testStreamRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatStream stream = m.streamAntiDiagonal();
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, result);
        }

        @Test
        public void testStreamH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatStream stream = m.streamHorizontal();
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, result);
        }

        @Test
        public void testStreamH_withRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatStream stream = m.streamHorizontal(1);
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 3.0f, 4.0f }, result);
        }

        @Test
        public void testStreamH_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatStream stream = m.streamHorizontal(1, 3);
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f, 6.0f }, result);
        }

        @Test
        public void testStreamV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatStream stream = m.streamVertical();
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 1.0f, 3.0f, 2.0f, 4.0f }, result);
        }

        @Test
        public void testStreamV_withColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatStream stream = m.streamVertical(0);
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 1.0f, 3.0f }, result);
        }

        @Test
        public void testStreamV_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatStream stream = m.streamVertical(1, 3);
            float[] result = stream.toArray();
            assertArrayEquals(new float[] { 2.0f, 5.0f, 3.0f, 6.0f }, result);
        }

        @Test
        public void testStreamR() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<FloatStream> stream = m.streamRows();
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamR_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            Stream<FloatStream> stream = m.streamRows(1, 3);
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamC() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<FloatStream> stream = m.streamColumns();
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamC_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            Stream<FloatStream> stream = m.streamColumns(1, 3);
            assertEquals(2, stream.count());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            final float[] sum = { 0.0f };
            m.forEach(val -> sum[0] += val);
            assertEquals(10.0f, sum[0]);
        }

        @Test
        public void testForEach_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            final float[] sum = { 0.0f };
            m.forEach(1, 3, 1, 3, val -> sum[0] += val);
            assertEquals(28.0f, sum[0]);
        }

        // ============ Inherited Methods Tests ============

        @Test
        public void testElementCount() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testElementCount_Empty() {
            FloatMatrix m = FloatMatrix.empty();
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testIsSameShape() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testAdjacent4Points() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.adjacent4Points(1, 1).toList();
            assertEquals(4, points.size());
        }

        @Test
        public void testAdjacent4Points_Corner() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.adjacent4Points(0, 0).toList();
            assertEquals(2, points.size());
        }

        @Test
        public void testAdjacent8Points() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.adjacent8Points(1, 1).toList();
            assertEquals(8, points.size());
        }

        @Test
        public void testAdjacent8Points_Corner() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.adjacent8Points(0, 0).toList();
            assertEquals(3, points.size());
        }

        @Test
        public void testArray() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            float[][] array = m.backingArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f }, array[0]);
            assertArrayEquals(new float[] { 3.0f, 4.0f }, array[1]);
        }

        @Test
        public void testReshape_singleParam() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f } });
            FloatMatrix reshaped = m.reshape(3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        @Test
        public void testPointsLU2RD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Point> points = m.pointsMainDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsRU2LD() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Point> points = m.pointsAntiDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Point> points = m.pointsHorizontal();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsH_withRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Point> points = m.pointsHorizontal(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            Stream<Point> points = m.pointsHorizontal(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Point> points = m.pointsVertical();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV_withColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Point> points = m.pointsVertical(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsV_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            Stream<Point> points = m.pointsVertical(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsR() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Stream<Point>> points = m.pointsRows();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsC() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Stream<Stream<Point>> points = m.pointsColumns();
            assertEquals(2, points.count());
        }

        @Test
        public void testForEach_biConsumer() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            final int[] count = { 0 };
            m.forEachIndex((i, j) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testForEach_biObjConsumer() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            final int[] count = { 0 };
            m.forEachIndex((i, j, matrix) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testAccept() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            final boolean[] called = { false };
            m.accept(matrix -> called[0] = true);
            assertTrue(called[0]);
        }

        @Test
        public void testApply() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(4, result);
        }

        // ============ Equality Tests ============

        @Test
        public void testHashCode() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 5.0f } });
            assertTrue(m1.equals(m2));
            assertFalse(m1.equals(m3));
            assertTrue(m1.equals(m1));
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("4.0"));
        }

        @Test
        public void testPrintln() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            String result = m.println();
            assertNotNull(result);
        }

        @Test
        public void testPrintln_empty() {
            FloatMatrix m = FloatMatrix.empty();
            String result = m.println();
            assertNotNull(result);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for FloatMatrix class covering all public methods.
     */
    @Tag("2511")
    class FloatMatrix2511Test extends TestBase {
        @Test
        public void testConstructor_withLargerMatrix() {
            float[][] arr = { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } };
            FloatMatrix m = new FloatMatrix(arr);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(5.0f, m.get(1, 1));
        }

        @Test
        public void testOf_withEmptyRows() {
            FloatMatrix m = FloatMatrix.of(new float[3][0]);
            assertEquals(3, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRandom_withLargeLength() {
            FloatMatrix m = FloatMatrix.random(100);
            assertEquals(1, m.rowCount());
            assertEquals(100, m.columnCount());
        }

        @Test
        public void testRepeat_withNegativeValue() {
            FloatMatrix m = FloatMatrix.repeat(1, 3, -5.5f);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(-5.5f, m.get(0, 0));
        }

        @Test
        public void testDiagonalLU2RD_withEmptyArray() {
            FloatMatrix m = FloatMatrix.mainDiagonal(new float[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_singleElement() {
            FloatMatrix m = FloatMatrix.diagonals(new float[] { 5.0f }, new float[] { 7.0f });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5.0f, m.get(0, 0)); // Main diagonal takes precedence
        }

        @Test
        public void testSet_negativeValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(0, 0, -99.9f);
            assertEquals(-99.9f, m.get(0, 0));
        }

        @Test
        public void testIsEmpty() {
            FloatMatrix empty = FloatMatrix.empty();
            assertTrue(empty.isEmpty());
            FloatMatrix notEmpty = FloatMatrix.of(new float[][] { { 1.0f } });
            assertFalse(notEmpty.isEmpty());
        }
    }

    @Nested
    @Tag("2512")
    class FloatMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_withValidArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            FloatMatrix m = new FloatMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(4.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_constructor_withNullArray() {
            FloatMatrix m = new FloatMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withEmptyArray() {
            FloatMatrix m = new FloatMatrix(new float[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withSingleElement() {
            FloatMatrix m = new FloatMatrix(new float[][] { { 42.5f } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.5f, m.get(0, 0), 0.0f);
        }

        @Test
        public void test_of_withValidArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            FloatMatrix m = FloatMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
        }

        @Test
        public void test_of_withNullArray() {
            FloatMatrix m = FloatMatrix.of((float[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_of_withEmptyArray() {
            FloatMatrix m = FloatMatrix.of(new float[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_of_withSingleRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        // ============ Create Method Tests ============

        @Test
        public void test_create_fromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            FloatMatrix m = FloatMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(4.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_create_fromIntArray_nullFirstRow() {
            int[][] ints = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(ints));
        }

        @Test
        public void test_create_fromIntArray_differentRowLengths() {
            int[][] ints = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(ints));
        }

        // ============ Random and Repeat Tests ============

        @Test
        public void test_random() {
            FloatMatrix m = FloatMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_repeat() {
            FloatMatrix m = FloatMatrix.repeat(1, 5, 3.14f);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14f, m.get(0, i), 0.0f);
            }
        }

        @Test
        public void test_repeat_zeroLength() {
            FloatMatrix m = FloatMatrix.repeat(1, 0, 3.14f);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        // ============ Diagonal Tests ============

        @Test
        public void test_mainDiagonal() {
            float[] diag = { 1.0f, 2.0f, 3.0f };
            FloatMatrix m = FloatMatrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(2.0f, m.get(1, 1), 0.0f);
            assertEquals(3.0f, m.get(2, 2), 0.0f);
            assertEquals(0.0f, m.get(0, 1), 0.0f);
            assertEquals(0.0f, m.get(1, 0), 0.0f);
        }

        @Test
        public void test_mainDiagonal_null() {
            FloatMatrix m = FloatMatrix.mainDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_antiDiagonal() {
            float[] diag = { 1.0f, 2.0f, 3.0f };
            FloatMatrix m = FloatMatrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 2), 0.0f);
            assertEquals(2.0f, m.get(1, 1), 0.0f);
            assertEquals(3.0f, m.get(2, 0), 0.0f);
            assertEquals(0.0f, m.get(0, 0), 0.0f);
        }

        @Test
        public void test_antiDiagonal_null() {
            FloatMatrix m = FloatMatrix.antiDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_diagonal_both() {
            float[] lu2rd = { 1.0f, 2.0f, 3.0f };
            float[] ru2ld = { 4.0f, 5.0f, 6.0f };
            FloatMatrix m = FloatMatrix.diagonals(lu2rd, ru2ld);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(2.0f, m.get(1, 1), 0.0f);
            assertEquals(3.0f, m.get(2, 2), 0.0f);
            assertEquals(4.0f, m.get(0, 2), 0.0f);
            assertEquals(6.0f, m.get(2, 0), 0.0f);
        }

        @Test
        public void test_diagonal_differentLengths() {
            float[] lu2rd = { 1.0f, 2.0f };
            float[] ru2ld = { 4.0f, 5.0f, 6.0f };
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonals(lu2rd, ru2ld));
        }
        // ============ Unbox Test ============

        @Test
        public void test_unbox() {
            Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m = FloatMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(4.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_unbox_withNulls() {
            Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, null }, { null, 4.0f } });
            FloatMatrix m = FloatMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(0.0f, m.get(0, 1), 0.0f);
            assertEquals(0.0f, m.get(1, 0), 0.0f);
            assertEquals(4.0f, m.get(1, 1), 0.0f);
        }

        // ============ Component Type Test ============

        // ============ Get and Set Tests ============

        @Test
        public void test_get_byIndices() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(2.0f, m.get(0, 1), 0.0f);
            assertEquals(3.0f, m.get(1, 0), 0.0f);
            assertEquals(4.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_get_byPoint() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Point p = Point.of(0, 1);
            assertEquals(2.0f, m.get(p), 0.0f);
        }

        @Test
        public void test_set_byIndices() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.set(0, 1, 9.0f);
            assertEquals(9.0f, m.get(0, 1), 0.0f);
        }

        @Test
        public void test_set_byPoint() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Point p = Point.of(1, 1);
            m.set(p, 9.0f);
            assertEquals(9.0f, m.get(p), 0.0f);
        }

        // ============ Directional Access Tests ============

        @Test
        public void test_upOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.0f, up.get(), 0.0f);
        }

        @Test
        public void test_upOf_firstRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void test_downOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.0f, down.get(), 0.0f);
        }

        @Test
        public void test_downOf_lastRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void test_leftOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.0f, left.get(), 0.0f);
        }

        @Test
        public void test_leftOf_firstColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void test_rightOf() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.0f, right.get(), 0.0f);
        }

        @Test
        public void test_rightOf_lastColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            OptionalFloat right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row and Column Access Tests ============

        @Test
        public void test_row() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            float[] row = m.rowView(0);
            assertArrayEquals(new float[] { 1.0f, 2.0f }, row, 0.0f);
        }

        @Test
        public void test_row_invalidIndex() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            float[] col = m.columnCopy(0);
            assertArrayEquals(new float[] { 1.0f, 3.0f }, col, 0.0f);
        }

        @Test
        public void test_column_invalidIndex() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setRow(0, new float[] { 9.0f, 8.0f });
            assertArrayEquals(new float[] { 9.0f, 8.0f }, m.rowView(0), 0.0f);
        }

        @Test
        public void test_setRow_invalidRowIndex() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(5, new float[] { 1.0f, 2.0f }));
        }

        @Test
        public void test_setRow_invalidLength() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f }));
        }

        @Test
        public void test_setColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setColumn(0, new float[] { 9.0f, 8.0f });
            assertArrayEquals(new float[] { 9.0f, 8.0f }, m.columnCopy(0), 0.0f);
        }

        @Test
        public void test_setColumn_invalidColumnIndex() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(5, new float[] { 1.0f }));
        }

        @Test
        public void test_setColumn_invalidLength() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f }));
        }

        // ============ Update Row and Column Tests ============

        @Test
        public void test_updateRow() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateRow(0, x -> x * 2);
            assertArrayEquals(new float[] { 2.0f, 4.0f }, m.rowView(0), 0.0f);
            assertArrayEquals(new float[] { 3.0f, 4.0f }, m.rowView(1), 0.0f);
        }

        @Test
        public void test_updateColumn() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateColumn(0, x -> x * 2);
            assertArrayEquals(new float[] { 2.0f, 6.0f }, m.columnCopy(0), 0.0f);
            assertArrayEquals(new float[] { 2.0f, 4.0f }, m.columnCopy(1), 0.0f);
        }

        // ============ Diagonal Get/Set Tests ============

        @Test
        public void test_getMainDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diag = m.getMainDiagonal();
            assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diag, 0.0f);
        }

        @Test
        public void test_getMainDiagonal_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setMainDiagonal(new float[] { 9.0f, 8.0f });
            assertArrayEquals(new float[] { 9.0f, 8.0f }, m.getMainDiagonal(), 0.0f);
        }

        @Test
        public void test_setMainDiagonal_nonSquare() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new float[] { 9.0f }));
        }

        @Test
        public void test_setMainDiagonal_invalidLength() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new float[] { 9.0f }));
        }

        @Test
        public void test_updateMainDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateMainDiagonal(x -> x * 2);
            assertArrayEquals(new float[] { 2.0f, 8.0f }, m.getMainDiagonal(), 0.0f);
        }

        @Test
        public void test_getAntiDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diag = m.getAntiDiagonal();
            assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, diag, 0.0f);
        }

        @Test
        public void test_setAntiDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.setAntiDiagonal(new float[] { 9.0f, 8.0f });
            assertArrayEquals(new float[] { 9.0f, 8.0f }, m.getAntiDiagonal(), 0.0f);
        }

        @Test
        public void test_updateAntiDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAntiDiagonal(x -> x * 2);
            float[] diag = m.getAntiDiagonal();
            assertEquals(4.0f, diag[0], 0.0f);
            assertEquals(6.0f, diag[1], 0.0f);
        }

        // ============ Update All Tests ============

        @Test
        public void test_updateAll_unaryOperator() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAll(x -> x * 2);
            assertEquals(2.0f, m.get(0, 0), 0.0f);
            assertEquals(4.0f, m.get(0, 1), 0.0f);
            assertEquals(6.0f, m.get(1, 0), 0.0f);
            assertEquals(8.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_updateAll_biFunction() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.updateAll((i, j) -> (i + 1) * 10.0f + (j + 1));
            assertEquals(11.0f, m.get(0, 0), 0.0f);
            assertEquals(12.0f, m.get(0, 1), 0.0f);
            assertEquals(21.0f, m.get(1, 0), 0.0f);
            assertEquals(22.0f, m.get(1, 1), 0.0f);
        }

        // ============ Replace If Tests ============

        @Test
        public void test_replaceIf_predicate() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.replaceIf(x -> x > 2, 99.0f);
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(2.0f, m.get(0, 1), 0.0f);
            assertEquals(99.0f, m.get(1, 0), 0.0f);
            assertEquals(99.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_replaceIf_biPredicate() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.replaceIf((i, j) -> i == j, 99.0f);
            assertEquals(99.0f, m.get(0, 0), 0.0f);
            assertEquals(2.0f, m.get(0, 1), 0.0f);
            assertEquals(3.0f, m.get(1, 0), 0.0f);
            assertEquals(99.0f, m.get(1, 1), 0.0f);
        }

        // ============ Map Tests ============

        @Test
        public void test_map() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.map(x -> x * 2);
            assertEquals(2.0f, result.get(0, 0), 0.0f);
            assertEquals(4.0f, result.get(0, 1), 0.0f);
            assertEquals(6.0f, result.get(1, 0), 0.0f);
            assertEquals(8.0f, result.get(1, 1), 0.0f);
            // Original should be unchanged
            assertEquals(1.0f, m.get(0, 0), 0.0f);
        }

        @Test
        public void test_mapToObj() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
            assertEquals("1.0", result.get(0, 0));
            assertEquals("2.0", result.get(0, 1));
            assertEquals("3.0", result.get(1, 0));
            assertEquals("4.0", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_value() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.fill(9.0f);
            assertEquals(9.0f, m.get(0, 0), 0.0f);
            assertEquals(9.0f, m.get(0, 1), 0.0f);
            assertEquals(9.0f, m.get(1, 0), 0.0f);
            assertEquals(9.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_fill_array() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            m.copyFrom(new float[][] { { 9.0f, 8.0f }, { 7.0f, 6.0f } });
            assertEquals(9.0f, m.get(0, 0), 0.0f);
            assertEquals(8.0f, m.get(0, 1), 0.0f);
            assertEquals(7.0f, m.get(1, 0), 0.0f);
            assertEquals(6.0f, m.get(1, 1), 0.0f);
        }

        @Test
        public void test_fill_withOffset() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            m.copyFrom(1, 1, new float[][] { { 99.0f } });
            assertEquals(1.0f, m.get(0, 0), 0.0f);
            assertEquals(99.0f, m.get(1, 1), 0.0f);
            assertEquals(9.0f, m.get(2, 2), 0.0f);
        }

        @Test
        public void test_fill_withOffset_oversizedArray() {
            // According to javadoc, fill() copies what fits - does not throw when source array is larger
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            m.copyFrom(0, 0, new float[][] { { 9.0f, 8.0f, 7.0f } }); // Third element should be ignored
            assertEquals(9.0f, m.get(0, 0), 0.0f);
            assertEquals(8.0f, m.get(0, 1), 0.0f); // Only copies what fits
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1.0f, copy.get(0, 0), 0.0f);
            copy.set(0, 0, 99.0f);
            assertEquals(1.0f, m.get(0, 0), 0.0f); // Original unchanged
        }

        @Test
        public void test_copy_withRowRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3.0f, copy.get(0, 0), 0.0f);
            assertEquals(6.0f, copy.get(1, 1), 0.0f);
        }

        @Test
        public void test_copy_withFullRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            FloatMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5.0f, copy.get(0, 0), 0.0f);
            assertEquals(9.0f, copy.get(1, 1), 0.0f);
        }

        @Test
        public void test_copy_invalidRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 5));
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0f, extended.get(0, 0), 0.0f);
            assertEquals(0.0f, extended.get(2, 2), 0.0f);
        }

        @Test
        public void test_extend_withDefaultValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix extended = m.resize(2, 3, 99.0f);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0f, extended.get(0, 0), 0.0f);
            assertEquals(99.0f, extended.get(1, 1), 0.0f);
            assertEquals(99.0f, extended.get(0, 2), 0.0f);
        }

        @Test
        public void test_extend_smaller() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix result = m.resize(1, 2);
            assertEquals(1, result.rowCount());
            assertEquals(2, result.columnCount());
        }

        @Test
        public void test_extend_directional() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1.0f, extended.get(1, 1), 0.0f);
            assertEquals(0.0f, extended.get(0, 0), 0.0f);
        }

        @Test
        public void test_extend_directional_withDefaultValue() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix extended = m.extend(1, 1, 1, 1, 99.0f);
            assertEquals(3, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1.0f, extended.get(1, 1), 0.0f);
            assertEquals(99.0f, extended.get(0, 0), 0.0f);
        }

        // ============ Reverse and Flip Tests ============

        @Test
        public void test_flipInPlaceHorizontally() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            m.flipInPlaceHorizontally();
            assertEquals(3.0f, m.get(0, 0), 0.0f);
            assertEquals(2.0f, m.get(0, 1), 0.0f);
            assertEquals(1.0f, m.get(0, 2), 0.0f);
        }

        @Test
        public void test_flipInPlaceVertically() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            m.flipInPlaceVertically();
            assertEquals(5.0f, m.get(0, 0), 0.0f);
            assertEquals(3.0f, m.get(1, 0), 0.0f);
            assertEquals(1.0f, m.get(2, 0), 0.0f);
        }

        @Test
        public void test_flipHorizontally() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix flipped = m.flipHorizontally();
            assertEquals(3.0f, flipped.get(0, 0), 0.0f);
            assertEquals(2.0f, flipped.get(0, 1), 0.0f);
            assertEquals(1.0f, flipped.get(0, 2), 0.0f);
            // Original unchanged
            assertEquals(1.0f, m.get(0, 0), 0.0f);
        }

        @Test
        public void test_flipVertically() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            FloatMatrix flipped = m.flipVertically();
            assertEquals(5.0f, flipped.get(0, 0), 0.0f);
            assertEquals(3.0f, flipped.get(1, 0), 0.0f);
            assertEquals(1.0f, flipped.get(2, 0), 0.0f);
            // Original unchanged
            assertEquals(1.0f, m.get(0, 0), 0.0f);
        }

        // ============ Rotate Tests ============

        @Test
        public void test_rotate90() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0f, rotated.get(0, 0), 0.0f);
            assertEquals(1.0f, rotated.get(0, 1), 0.0f);
            assertEquals(4.0f, rotated.get(1, 0), 0.0f);
            assertEquals(2.0f, rotated.get(1, 1), 0.0f);
        }

        @Test
        public void test_rotate180() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate180();
            assertEquals(4.0f, rotated.get(0, 0), 0.0f);
            assertEquals(3.0f, rotated.get(0, 1), 0.0f);
            assertEquals(2.0f, rotated.get(1, 0), 0.0f);
            assertEquals(1.0f, rotated.get(1, 1), 0.0f);
        }

        @Test
        public void test_rotate270() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2.0f, rotated.get(0, 0), 0.0f);
            assertEquals(4.0f, rotated.get(0, 1), 0.0f);
            assertEquals(1.0f, rotated.get(1, 0), 0.0f);
            assertEquals(3.0f, rotated.get(1, 1), 0.0f);
        }

        // ============ Transpose Test ============

        @Test
        public void test_transpose() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0f, transposed.get(0, 0), 0.0f);
            assertEquals(4.0f, transposed.get(0, 1), 0.0f);
            assertEquals(2.0f, transposed.get(1, 0), 0.0f);
            assertEquals(5.0f, transposed.get(1, 1), 0.0f);
        }

        @Test
        public void test_transpose_square() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0f, transposed.get(0, 0), 0.0f);
            assertEquals(3.0f, transposed.get(0, 1), 0.0f);
        }

        // ============ Reshape Test ============

        @Test
        public void test_reshape() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            FloatMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1.0f, reshaped.get(0, 0), 0.0f);
            assertEquals(2.0f, reshaped.get(0, 1), 0.0f);
            assertEquals(3.0f, reshaped.get(1, 0), 0.0f);
            assertEquals(4.0f, reshaped.get(1, 1), 0.0f);
        }

        @Test
        public void test_reshape_differentSize() {
            // According to javadoc, reshape allows different sizes - excess elements truncated, missing filled with zeros
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix reshaped = m.reshape(3, 3); // 4 elements reshaped to 9 positions
            assertEquals(3, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            // Original elements
            assertEquals(1.0f, reshaped.get(0, 0), 0.0f);
            assertEquals(2.0f, reshaped.get(0, 1), 0.0f);
            assertEquals(3.0f, reshaped.get(0, 2), 0.0f);
            assertEquals(4.0f, reshaped.get(1, 0), 0.0f);
            // Remaining positions filled with zeros
            assertEquals(0.0f, reshaped.get(1, 1), 0.0f);
            assertEquals(0.0f, reshaped.get(1, 2), 0.0f);
            assertEquals(0.0f, reshaped.get(2, 0), 0.0f);
            assertEquals(0.0f, reshaped.get(2, 1), 0.0f);
            assertEquals(0.0f, reshaped.get(2, 2), 0.0f);
        }

        // ============ Repelem Test ============

        @Test
        public void test_repeatElements() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0f, result.get(0, 0), 0.0f);
            assertEquals(1.0f, result.get(0, 1), 0.0f);
            assertEquals(1.0f, result.get(1, 0), 0.0f);
            assertEquals(1.0f, result.get(1, 1), 0.0f);
            assertEquals(2.0f, result.get(0, 2), 0.0f);
        }

        @Test
        public void test_repeatElements_invalidRepeats() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
        }

        // ============ Repmat Test ============

        @Test
        public void test_repeatMatrix() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix result = m.repeatMatrix(2, 2);
            assertEquals(2, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0f, result.get(0, 0), 0.0f);
            assertEquals(2.0f, result.get(0, 1), 0.0f);
            assertEquals(1.0f, result.get(0, 2), 0.0f);
            assertEquals(2.0f, result.get(0, 3), 0.0f);
        }

        @Test
        public void test_repeatMatrix_invalidRepeats() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
        }

        // ============ Flatten Test ============

        @Test
        public void test_flatten() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1.0f, flat.get(0), 0.0f);
            assertEquals(2.0f, flat.get(1), 0.0f);
            assertEquals(3.0f, flat.get(2), 0.0f);
            assertEquals(4.0f, flat.get(3), 0.0f);
        }

        // ============ FlatOp Test ============

        @Test
        public void test_applyOnFlattened() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            AtomicInteger count = new AtomicInteger(0);
            m.applyOnFlattened(row -> count.addAndGet(row.length));
            assertEquals(4, count.get());
        }

        // ============ Vstack and Hstack Tests ============

        @Test
        public void test_stackVertically() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
            FloatMatrix result = m1.stackVertically(m2);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0f, result.get(0, 0), 0.0f);
            assertEquals(3.0f, result.get(1, 0), 0.0f);
        }

        @Test
        public void test_vstack_incompatibleCols() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_stackHorizontally() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f }, { 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f }, { 4.0f } });
            FloatMatrix result = m1.stackHorizontally(m2);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0f, result.get(0, 0), 0.0f);
            assertEquals(3.0f, result.get(0, 1), 0.0f);
        }

        @Test
        public void test_hstack_incompatibleRows() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f }, { 4.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void test_add() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix result = m1.add(m2);
            assertEquals(6.0f, result.get(0, 0), 0.0f);
            assertEquals(8.0f, result.get(0, 1), 0.0f);
            assertEquals(10.0f, result.get(1, 0), 0.0f);
            assertEquals(12.0f, result.get(1, 1), 0.0f);
        }

        @Test
        public void test_add_incompatibleDimensions() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void test_subtract() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix result = m1.subtract(m2);
            assertEquals(4.0f, result.get(0, 0), 0.0f);
            assertEquals(4.0f, result.get(0, 1), 0.0f);
            assertEquals(4.0f, result.get(1, 0), 0.0f);
            assertEquals(4.0f, result.get(1, 1), 0.0f);
        }

        @Test
        public void test_subtract_incompatibleDimensions() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void test_multiply() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f, 0.0f }, { 1.0f, 2.0f } });
            FloatMatrix result = m1.multiply(m2);
            assertEquals(4.0f, result.get(0, 0), 0.0f);
            assertEquals(4.0f, result.get(0, 1), 0.0f);
            assertEquals(10.0f, result.get(1, 0), 0.0f);
            assertEquals(8.0f, result.get(1, 1), 0.0f);
        }

        @Test
        public void test_multiply_incompatibleDimensions() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void test_boxed() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            Matrix<Float> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(1.0f, boxed.get(0, 0), 0.0f);
            assertEquals(4.0f, boxed.get(1, 1), 0.0f);
        }

        // ============ ToDoubleMatrix Test ============

        @Test
        public void test_toDoubleMatrix() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            DoubleMatrix result = m.toDoubleMatrix();
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.001);
            assertEquals(4.0, result.get(1, 1), 0.001);
        }

        // ============ ZipWith Tests ============

        @Test
        public void test_zipWith_binary() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(5.0f, result.get(0, 0), 0.0f);
            assertEquals(12.0f, result.get(0, 1), 0.0f);
            assertEquals(21.0f, result.get(1, 0), 0.0f);
            assertEquals(32.0f, result.get(1, 1), 0.0f);
        }

        @Test
        public void test_zipWith_ternary() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f } });
            FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(9.0f, result.get(0, 0), 0.0f);
            assertEquals(12.0f, result.get(0, 1), 0.0f);
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamHorizontal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            double sum = m.streamHorizontal().sum();
            assertEquals(10.0, sum, 0.0);
        }

        @Test
        public void test_streamH_byRowIndex() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            double sum = m.streamHorizontal(0).sum();
            assertEquals(3.0, sum, 0.0);
        }

        @Test
        public void test_streamH_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            double sum = m.streamHorizontal(1, 3).sum();
            assertEquals(18.0, sum, 0.0);
        }

        @Test
        public void test_streamVertical() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            double sum = m.streamVertical().sum();
            assertEquals(10.0, sum, 0.0);
        }

        @Test
        public void test_streamV_byColumnIndex() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            double sum = m.streamVertical(0).sum();
            assertEquals(4.0, sum, 0.0);
        }

        @Test
        public void test_streamV_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            double sum = m.streamVertical(1, 3).sum();
            assertEquals(16.0, sum, 0.0);
        }

        @Test
        public void test_streamMainDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            double sum = m.streamMainDiagonal().sum();
            assertEquals(5.0, sum, 0.0);
        }

        @Test
        public void test_streamAntiDiagonal() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            double sum = m.streamAntiDiagonal().sum();
            assertEquals(5.0, sum, 0.0);
        }

        @Test
        public void test_streamRows() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            long count = m.streamRows().count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamR_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
            long count = m.streamRows(1, 3).count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamColumns() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            long count = m.streamColumns().count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamC_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
            long count = m.streamColumns(1, 3).count();
            assertEquals(2, count);
        }

        // ============ ForEach Tests ============

        @Test
        public void test_forEach() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(x -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        @Test
        public void test_forEach_withRange() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(1, 3, 1, 3, x -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        // ============ Utility Tests ============

        @Test
        public void test_println() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.length() > 0);
        }

        @Test
        public void test_equals_same() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(m1, m2);
        }

        @Test
        public void test_equals_different() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 5.0f } });
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_equals_null() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            assertNotEquals(m1, null);
        }

        @Test
        public void test_toString() {
            FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.length() > 0);
        }
    }

    @Nested
    class JavadocExampleMatrixGroup1Test_FloatMatrix extends TestBase {
        @Test
        public void testCharMatrix_toFloatMatrix() {
            CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            FloatMatrix floatMatrix = charMatrix.toFloatMatrix();
            // Result: [[97.0f, 98.0f]]
            assertEquals(97.0f, floatMatrix.get(0, 0));
            assertEquals(98.0f, floatMatrix.get(0, 1));
        }

        @Test
        public void testShortMatrix_toFloatMatrix() {
            ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix floatMatrix = shortMatrix.toFloatMatrix();
            assertEquals(1.0f, floatMatrix.get(0, 0));
            assertEquals(4.0f, floatMatrix.get(1, 1));
        }
    }

    @Nested
    class JavadocExampleMatrixGroup2Test_FloatMatrix extends TestBase {
        // ==================== FloatMatrix ====================

        @Test
        public void testFloatMatrix_repeat() {
            FloatMatrix matrix = FloatMatrix.repeat(2, 3, 1.0f);
            assertEquals(2, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            assertEquals(1.0f, matrix.get(0, 0));
        }

        @Test
        public void testFloatMatrix_diagonals() {
            FloatMatrix matrix = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, new float[] { 4.0f, 5.0f, 6.0f });
            assertEquals(1.0f, matrix.get(0, 0));
            assertEquals(0.0f, matrix.get(0, 1));
            assertEquals(4.0f, matrix.get(0, 2));
            assertEquals(0.0f, matrix.get(1, 0));
            assertEquals(2.0f, matrix.get(1, 1));
            assertEquals(0.0f, matrix.get(1, 2));
            assertEquals(6.0f, matrix.get(2, 0));
            assertEquals(0.0f, matrix.get(2, 1));
            assertEquals(3.0f, matrix.get(2, 2));
        }

        @Test
        public void testFloatMatrix_get() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            assertEquals(2.0f, matrix.get(0, 1));
        }

        @Test
        public void testFloatMatrix_above() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            u.OptionalFloat value = matrix.above(1, 0);
            assertEquals(1.0f, value.get());
            u.OptionalFloat empty = matrix.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testFloatMatrix_below() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            u.OptionalFloat value = matrix.below(0, 0);
            assertEquals(3.0f, value.get());
            u.OptionalFloat empty = matrix.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testFloatMatrix_getMainDiagonal() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diagonal = matrix.getMainDiagonal();
            assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal);
        }

        @Test
        public void testFloatMatrix_getAntiDiagonal() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
            float[] diagonal = matrix.getAntiDiagonal();
            assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, diagonal);
        }

        @Test
        public void testFloatMatrix_updateAll() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            matrix.updateAll(x -> x * 2);
            assertEquals(2.0f, matrix.get(0, 0));
            assertEquals(4.0f, matrix.get(0, 1));
            assertEquals(6.0f, matrix.get(1, 0));
            assertEquals(8.0f, matrix.get(1, 1));
        }

        @Test
        public void testFloatMatrix_replaceIf() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { -1.0f, 2.0f, -3.0f }, { 4.0f, -5.0f, 6.0f } });
            matrix.replaceIf(x -> x < 0, 0.0f);
            assertEquals(0.0f, matrix.get(0, 0));
            assertEquals(2.0f, matrix.get(0, 1));
            assertEquals(0.0f, matrix.get(0, 2));
            assertEquals(4.0f, matrix.get(1, 0));
            assertEquals(0.0f, matrix.get(1, 1));
            assertEquals(6.0f, matrix.get(1, 2));
        }

        @Test
        public void testFloatMatrix_flatten() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatList list = matrix.flatten();
            assertEquals(FloatList.of(1.0f, 2.0f, 3.0f, 4.0f), list);
        }

        @Test
        public void testFloatMatrix_applyOnFlattened() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 5.0f, 3.0f }, { 4.0f, 1.0f } });
            matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
            assertEquals(1.0f, matrix.get(0, 0));
            assertEquals(3.0f, matrix.get(0, 1));
            assertEquals(4.0f, matrix.get(1, 0));
            assertEquals(5.0f, matrix.get(1, 1));
        }

        @Test
        public void testFloatMatrix_add() {
            FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix sum = a.add(b);
            assertEquals(6.0f, sum.get(0, 0));
            assertEquals(8.0f, sum.get(0, 1));
            assertEquals(10.0f, sum.get(1, 0));
            assertEquals(12.0f, sum.get(1, 1));
        }

        @Test
        public void testFloatMatrix_subtract() {
            FloatMatrix a = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix b = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix diff = a.subtract(b);
            assertEquals(4.0f, diff.get(0, 0));
            assertEquals(4.0f, diff.get(0, 1));
            assertEquals(4.0f, diff.get(1, 0));
            assertEquals(4.0f, diff.get(1, 1));
        }

        @Test
        public void testFloatMatrix_multiply() {
            FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix product = a.multiply(b);
            assertEquals(19.0f, product.get(0, 0));
            assertEquals(22.0f, product.get(0, 1));
            assertEquals(43.0f, product.get(1, 0));
            assertEquals(50.0f, product.get(1, 1));
        }

        @Test
        public void testFloatMatrix_stackVertically() {
            FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix stacked = a.stackVertically(b);
            assertEquals(4, stacked.rowCount());
            assertEquals(1.0f, stacked.get(0, 0));
            assertEquals(5.0f, stacked.get(2, 0));
            assertEquals(8.0f, stacked.get(3, 1));
        }

        @Test
        public void testFloatMatrix_stackHorizontally() {
            FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
            FloatMatrix stacked = a.stackHorizontally(b);
            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertArrayEquals(new float[] { 1.0f, 2.0f, 5.0f, 6.0f }, stacked.rowView(0));
            assertArrayEquals(new float[] { 3.0f, 4.0f, 7.0f, 8.0f }, stacked.rowView(1));
        }

        @Test
        public void testFloatMatrix_repeatElements() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix repeated = matrix.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertArrayEquals(new float[] { 1.0f, 1.0f, 1.0f, 2.0f, 2.0f, 2.0f }, repeated.rowView(0));
        }

        @Test
        public void testFloatMatrix_repeatMatrix() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
            FloatMatrix repeated = matrix.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertArrayEquals(new float[] { 1.0f, 2.0f, 1.0f, 2.0f, 1.0f, 2.0f }, repeated.rowView(0));
        }

        @Test
        public void testFloatMatrix_zipWith() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
            FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(3.0f, result.get(0, 0));
            assertEquals(8.0f, result.get(0, 1));
        }

        @Test
        public void testFloatMatrix_zipWith3() {
            FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f } });
            FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f } });
            FloatMatrix m3 = FloatMatrix.of(new float[][] { { 3.0f } });
            FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(6.0f, result.get(0, 0));
        }

        @Test
        public void testFloatMatrix_toIntMatrix() {
            FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { { 1.9f, 2.1f }, { 3.5f, 4.0f } });
            IntMatrix intMatrix = floatMatrix.toIntMatrix();
            assertEquals(1, intMatrix.get(0, 0));
            assertEquals(2, intMatrix.get(0, 1));
            assertEquals(3, intMatrix.get(1, 0));
            assertEquals(4, intMatrix.get(1, 1));
        }

        @Test
        public void testFloatMatrix_toLongMatrix() {
            FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { { 1.9f, 2.1f }, { 3.5f, 4.0f } });
            LongMatrix longMatrix = floatMatrix.toLongMatrix();
            assertEquals(1L, longMatrix.get(0, 0));
            assertEquals(2L, longMatrix.get(0, 1));
            assertEquals(3L, longMatrix.get(1, 0));
            assertEquals(4L, longMatrix.get(1, 1));
        }
    }

    @Nested
    class JavadocExampleMatrixTest_FloatMatrix extends TestBase {
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

        // ==================== FloatMatrix toString example ====================

        @Test
        public void testFloatMatrixToString() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
            assertEquals("[[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]", matrix.toString());
        }
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_FloatMatrix extends TestBase {
        @Test
        public void testFloatMatrixRowsForZeroColumnMatrix() {
            final FloatMatrix matrix = FloatMatrix.of(new float[][] { {}, {}, {} });
            final List<float[]> rows = matrix.streamRows().map(FloatStream::toArray).toList();

            assertEquals(3, rows.size());
            assertArrayEquals(new float[0], rows.get(0));
            assertArrayEquals(new float[0], rows.get(1));
            assertArrayEquals(new float[0], rows.get(2));
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_FloatMatrix extends TestBase {
        @Test
        public void testFloatMatrixUpdateAllNullOperator() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1f, 2f }, { 3f, 4f } });
            FloatMatrix emptyLike = FloatMatrix.of(new float[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.FloatUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Float, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.FloatPredicate<RuntimeException>) null, 0f));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0f));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.FloatPredicate<RuntimeException>) null, 0f));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0f));
        }

        @Test
        public void testFloatMatrixBinaryOpsRejectNullMatrix() {
            FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1F } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.add(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.subtract(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.multiply(null));
        }
    }

}
