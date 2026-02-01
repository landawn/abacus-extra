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
import com.landawn.abacus.util.u.OptionalFloat;
import com.landawn.abacus.util.stream.FloatStream;

public class FloatMatrixTest extends TestBase {

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
    public void testEmpty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test that empty() returns singleton
        assertSame(FloatMatrix.empty(), FloatMatrix.empty());
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
        FloatMatrix m = FloatMatrix.repeat(42.5f, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42.5f, m.get(0, i), DELTA);
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        FloatMatrix m = FloatMatrix.diagonalLU2RD(new float[] { 1.0f, 2.0f, 3.0f });
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
        FloatMatrix m = FloatMatrix.diagonalRU2LD(new float[] { 1.0f, 2.0f, 3.0f });
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
        FloatMatrix m = FloatMatrix.diagonal(new float[] { 1.0f, 2.0f, 3.0f }, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA);
        assertEquals(3.0f, m.get(2, 2), DELTA);
        assertEquals(4.0f, m.get(0, 2), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA); // Overwritten
        assertEquals(6.0f, m.get(2, 0), DELTA);

        // Test with only main diagonal
        FloatMatrix m2 = FloatMatrix.diagonal(new float[] { 1.0f, 2.0f, 3.0f }, null);
        assertEquals(1.0f, m2.get(0, 0), DELTA);
        assertEquals(2.0f, m2.get(1, 1), DELTA);
        assertEquals(3.0f, m2.get(2, 2), DELTA);

        // Test with only anti-diagonal
        FloatMatrix m3 = FloatMatrix.diagonal(null, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(4.0f, m3.get(0, 2), DELTA);
        assertEquals(5.0f, m3.get(1, 1), DELTA);
        assertEquals(6.0f, m3.get(2, 0), DELTA);

        // Test with empty arrays
        FloatMatrix empty = FloatMatrix.diagonal(null, null);
        assertTrue(empty.isEmpty());

        // Test illegal argument
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonal(new float[] { 1.0f, 2.0f }, new float[] { 3.0f, 4.0f, 5.0f }));
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
        OptionalFloat up = matrix.upOf(1, 1);
        assertTrue(up.isPresent());
        assertEquals(2.0f, up.get(), DELTA);

        // Test top row
        OptionalFloat empty = matrix.upOf(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        OptionalFloat down = matrix.downOf(1, 1);
        assertTrue(down.isPresent());
        assertEquals(8.0f, down.get(), DELTA);

        // Test bottom row
        OptionalFloat empty = matrix.downOf(2, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        OptionalFloat left = matrix.leftOf(1, 1);
        assertTrue(left.isPresent());
        assertEquals(4.0f, left.get(), DELTA);

        // Test leftmost column
        OptionalFloat empty = matrix.leftOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        OptionalFloat right = matrix.rightOf(1, 1);
        assertTrue(right.isPresent());
        assertEquals(6.0f, right.get(), DELTA);

        // Test rightmost column
        OptionalFloat empty = matrix.rightOf(1, 2);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        float[] row0 = matrix.row(0);
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, row0, DELTA);

        float[] row1 = matrix.row(1);
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, row1, DELTA);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.row(3));
    }

    @Test
    public void testColumn() {
        float[] col0 = matrix.column(0);
        assertArrayEquals(new float[] { 1.0f, 4.0f, 7.0f }, col0, DELTA);

        float[] col1 = matrix.column(1);
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, col1, DELTA);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.column(3));
    }

    @Test
    public void testSetRow() {
        FloatMatrix m = matrix.copy();
        m.setRow(0, new float[] { 10.0f, 20.0f, 30.0f });
        assertArrayEquals(new float[] { 10.0f, 20.0f, 30.0f }, m.row(0), DELTA);

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testSetColumn() {
        FloatMatrix m = matrix.copy();
        m.setColumn(0, new float[] { 10.0f, 20.0f, 30.0f });
        assertArrayEquals(new float[] { 10.0f, 20.0f, 30.0f }, m.column(0), DELTA);

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testUpdateRow() {
        FloatMatrix m = matrix.copy();
        m.updateRow(0, x -> x * 2.0f);
        assertArrayEquals(new float[] { 2.0f, 4.0f, 6.0f }, m.row(0), DELTA);
    }

    @Test
    public void testUpdateColumn() {
        FloatMatrix m = matrix.copy();
        m.updateColumn(0, x -> x + 10.0f);
        assertArrayEquals(new float[] { 11.0f, 14.0f, 17.0f }, m.column(0), DELTA);
    }

    @Test
    public void testGetLU2RD() {
        float[] diagonal = matrix.getLU2RD();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal, DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        FloatMatrix m = matrix.copy();
        m.setLU2RD(new float[] { 10.0f, 20.0f, 30.0f });
        assertEquals(10.0f, m.get(0, 0), DELTA);
        assertEquals(20.0f, m.get(1, 1), DELTA);
        assertEquals(30.0f, m.get(2, 2), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setLU2RD(new float[] { 1.0f }));

        // Test array too short
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testUpdateLU2RD() {
        FloatMatrix m = matrix.copy();
        m.updateLU2RD(x -> x * 10.0f);
        assertEquals(10.0f, m.get(0, 0), DELTA);
        assertEquals(50.0f, m.get(1, 1), DELTA);
        assertEquals(90.0f, m.get(2, 2), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateLU2RD(x -> x * 2.0f));
    }

    @Test
    public void testGetRU2LD() {
        float[] antiDiagonal = matrix.getRU2LD();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, antiDiagonal, DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        FloatMatrix m = matrix.copy();
        m.setRU2LD(new float[] { 10.0f, 20.0f, 30.0f });
        assertEquals(10.0f, m.get(0, 2), DELTA);
        assertEquals(20.0f, m.get(1, 1), DELTA);
        assertEquals(30.0f, m.get(2, 0), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setRU2LD(new float[] { 1.0f }));
    }

    @Test
    public void testUpdateRU2LD() {
        FloatMatrix m = matrix.copy();
        m.updateRU2LD(x -> x * 10.0f);
        assertEquals(30.0f, m.get(0, 2), DELTA);
        assertEquals(50.0f, m.get(1, 1), DELTA);
        assertEquals(70.0f, m.get(2, 0), DELTA);

        // Test non-square matrix
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateRU2LD(x -> x * 2.0f));
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
        m.fill(patch);
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
        m.fill(1, 1, patch);
        assertEquals(0.0f, m.get(0, 0), DELTA); // unchanged
        assertEquals(1.0f, m.get(1, 1), DELTA);
        assertEquals(2.0f, m.get(1, 2), DELTA);
        assertEquals(3.0f, m.get(2, 1), DELTA);
        assertEquals(4.0f, m.get(2, 2), DELTA);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
        // assertThrows(IndexOutOfBoundsException.class, () -> m.fill(3, 0, patch));
        m.fill(3, 0, patch);
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
        FloatMatrix extended = matrix.extend(5, 5);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0), DELTA);
        assertEquals(0.0f, extended.get(3, 3), DELTA); // new cells are 0

        // Test truncation
        FloatMatrix truncated = matrix.extend(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
    }

    @Test
    public void testExtendWithDefaultValue() {
        FloatMatrix extended = matrix.extend(4, 4, -1.0f);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0), DELTA);
        assertEquals(-1.0f, extended.get(3, 3), DELTA); // new cell

        // Test negative dimensions
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 3, 0.0f));
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(3, -1, 0.0f));
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
        m.reverseH();
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
        m.reverseV();
        assertEquals(5.0f, m.get(0, 0), DELTA);
        assertEquals(6.0f, m.get(0, 1), DELTA);
        assertEquals(3.0f, m.get(1, 0), DELTA);
        assertEquals(4.0f, m.get(1, 1), DELTA);
        assertEquals(1.0f, m.get(2, 0), DELTA);
        assertEquals(2.0f, m.get(2, 1), DELTA);
    }

    @Test
    public void testFlipH() {
        FloatMatrix flipped = matrix.flipH();
        assertEquals(3.0f, flipped.get(0, 0), DELTA);
        assertEquals(2.0f, flipped.get(0, 1), DELTA);
        assertEquals(1.0f, flipped.get(0, 2), DELTA);

        // Original should be unchanged
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
    }

    @Test
    public void testFlipV() {
        FloatMatrix flipped = matrix.flipV();
        assertEquals(7.0f, flipped.get(0, 0), DELTA);
        assertEquals(8.0f, flipped.get(0, 1), DELTA);
        assertEquals(9.0f, flipped.get(0, 2), DELTA);

        // Original should be unchanged
        assertEquals(1.0f, matrix.get(0, 0), DELTA);
    }

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
    }

    @Test
    public void testRepelem() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix repeated = m.repelem(2, 3);
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
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix repeated = m.repmat(2, 3);
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
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
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
        matrix.flatOp(row -> {
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
        FloatMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals(1.0f, stacked.get(0, 0), DELTA);
        assertEquals(7.0f, stacked.get(2, 0), DELTA);
        assertEquals(12.0f, stacked.get(3, 2), DELTA);

        // Test different column counts
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m3));
    }

    @Test
    public void testHstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1.0f, stacked.get(0, 0), DELTA);
        assertEquals(5.0f, stacked.get(0, 2), DELTA);
        assertEquals(8.0f, stacked.get(1, 3), DELTA);

        // Test different row counts
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m3));
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
    public void testZipWith3Matrices() {
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
    public void testStreamLU2RD() {
        float[] diagonal = matrix.streamLU2RD().toArray();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamLU2RD().toArray().length == 0);

        // Test non-square
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        float[] antiDiagonal = matrix.streamRU2LD().toArray();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, antiDiagonal, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamRU2LD().toArray().length == 0);

        // Test non-square
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        float[] all = matrix.streamH().toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, all, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamH().toArray().length == 0);
    }

    @Test
    public void testStreamHRow() {
        float[] row1 = matrix.streamH(1).toArray();
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, row1, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(3));
    }

    @Test
    public void testStreamHRange() {
        float[] rows = matrix.streamH(1, 3).toArray();
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, rows, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        float[] all = matrix.streamV().toArray();
        assertArrayEquals(new float[] { 1.0f, 4.0f, 7.0f, 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f }, all, DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamV().toArray().length == 0);
    }

    @Test
    public void testStreamVColumn() {
        float[] col1 = matrix.streamV(1).toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, col1, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(3));
    }

    @Test
    public void testStreamVRange() {
        float[] columnCount = matrix.streamV(1, 3).toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f }, columnCount, DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        List<float[]> rows = matrix.streamR().map(FloatStream::toArray).toList();
        assertEquals(3, rows.size());
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, rows.get(0), DELTA);
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(1), DELTA);
        assertArrayEquals(new float[] { 7.0f, 8.0f, 9.0f }, rows.get(2), DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamR().count() == 0);
    }

    @Test
    public void testStreamRRange() {
        List<float[]> rows = matrix.streamR(1, 3).map(FloatStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(0), DELTA);
        assertArrayEquals(new float[] { 7.0f, 8.0f, 9.0f }, rows.get(1), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(0, 4));
    }

    @Test
    public void testStreamC() {
        List<float[]> columnCount = matrix.streamC().map(FloatStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new float[] { 1.0f, 4.0f, 7.0f }, columnCount.get(0), DELTA);
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, columnCount.get(1), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f, 9.0f }, columnCount.get(2), DELTA);

        // Test empty matrix
        assertTrue(emptyMatrix.streamC().count() == 0);
    }

    @Test
    public void testStreamCRange() {
        List<float[]> columnCount = matrix.streamC(1, 3).map(FloatStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, columnCount.get(0), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f, 9.0f }, columnCount.get(1), DELTA);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(0, 4));
    }

    @Test
    public void testLength() {
        // This is a protected method, test indirectly through row operations
        float[] row = matrix.row(0);
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
    public void testPrintln() {
        // Just ensure it doesn't throw
        matrix.println();
        emptyMatrix.println();
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
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
        assertTrue(str.contains("4.0"));
    }

    @Test
    public void testIteratorNoSuchElement() {
        // Test streamH iterator
        FloatStream stream = matrix.streamH(0, 1);
        stream.toArray(); // Consume all
        assertThrows(IllegalStateException.class, () -> stream.iterator().next());

        // Test streamLU2RD iterator
        FloatStream diagStream = matrix.streamLU2RD();
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

        FloatMatrix extended = emptyMatrix.extend(2, 2, 5.0f);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(5.0f, extended.get(0, 0), DELTA);
    }
}
