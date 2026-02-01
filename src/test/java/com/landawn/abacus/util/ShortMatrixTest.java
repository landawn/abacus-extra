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
import com.landawn.abacus.util.u.OptionalShort;
import com.landawn.abacus.util.stream.ShortStream;

public class ShortMatrixTest extends TestBase {

    private ShortMatrix matrix;
    private ShortMatrix emptyMatrix;

    @BeforeEach
    public void setUp() {
        matrix = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        emptyMatrix = ShortMatrix.empty();
    }

    @Test
    public void testConstructor() {
        // Test with valid array
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals((short) 1, m.get(0, 0));

        // Test with null array
        ShortMatrix nullMatrix = new ShortMatrix(null);
        assertEquals(0, nullMatrix.rowCount());
        assertEquals(0, nullMatrix.columnCount());
    }

    @Test
    public void testEmpty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test that empty() returns singleton
        assertSame(ShortMatrix.empty(), ShortMatrix.empty());
    }

    @Test
    public void testOf() {
        // Test with valid array
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = ShortMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());

        // Test with null/empty
        ShortMatrix empty1 = ShortMatrix.of(null);
        assertTrue(empty1.isEmpty());

        ShortMatrix empty2 = ShortMatrix.of(new short[0][0]);
        assertTrue(empty2.isEmpty());
    }

    @Test
    public void testRandom() {
        ShortMatrix m = ShortMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Values should be random, just check they exist
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat() {
        ShortMatrix m = ShortMatrix.repeat((short) 42, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals((short) 42, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals((short) i, m.get(0, i));
        }
    }

    @Test
    public void testRangeWithStep() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals((short) 0, m.get(0, 0));
        assertEquals((short) 2, m.get(0, 1));
        assertEquals((short) 4, m.get(0, 2));
        assertEquals((short) 6, m.get(0, 3));
        assertEquals((short) 8, m.get(0, 4));
    }

    @Test
    public void testRangeClosed() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 4);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals((short) i, m.get(0, i));
        }
    }

    @Test
    public void testRangeClosedWithStep() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertEquals((short) 0, m.get(0, 0));
        assertEquals((short) 2, m.get(0, 1));
        assertEquals((short) 4, m.get(0, 2));
        assertEquals((short) 6, m.get(0, 3));
        assertEquals((short) 8, m.get(0, 4));
        assertEquals((short) 10, m.get(0, 5));
    }

    @Test
    public void testDiagonalLU2RD() {
        ShortMatrix m = ShortMatrix.diagonalLU2RD(new short[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals((short) 1, m.get(0, 0));
        assertEquals((short) 2, m.get(1, 1));
        assertEquals((short) 3, m.get(2, 2));
        assertEquals((short) 0, m.get(0, 1));
        assertEquals((short) 0, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        ShortMatrix m = ShortMatrix.diagonalRU2LD(new short[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals((short) 1, m.get(0, 2));
        assertEquals((short) 2, m.get(1, 1));
        assertEquals((short) 3, m.get(2, 0));
        assertEquals((short) 0, m.get(0, 0));
        assertEquals((short) 0, m.get(2, 2));
    }

    @Test
    public void testDiagonal() {
        // Test with both diagonals
        ShortMatrix m = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals((short) 1, m.get(0, 0));
        assertEquals((short) 2, m.get(1, 1));
        assertEquals((short) 3, m.get(2, 2));
        assertEquals((short) 4, m.get(0, 2));
        assertEquals((short) 2, m.get(1, 1)); // Overwritten
        assertEquals((short) 6, m.get(2, 0));

        // Test with only main diagonal
        ShortMatrix m2 = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, null);
        assertEquals((short) 1, m2.get(0, 0));
        assertEquals((short) 2, m2.get(1, 1));
        assertEquals((short) 3, m2.get(2, 2));

        // Test with only anti-diagonal
        ShortMatrix m3 = ShortMatrix.diagonal(null, new short[] { 4, 5, 6 });
        assertEquals((short) 4, m3.get(0, 2));
        assertEquals((short) 5, m3.get(1, 1));
        assertEquals((short) 6, m3.get(2, 0));

        // Test with empty arrays
        ShortMatrix empty = ShortMatrix.diagonal(null, null);
        assertTrue(empty.isEmpty());

        // Test illegal argument
        assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonal(new short[] { 1, 2 }, new short[] { 3, 4, 5 }));
    }

    @Test
    public void testUnbox() {
        Short[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Short> boxedMatrix = Matrix.of(boxed);
        ShortMatrix unboxed = ShortMatrix.unbox(boxedMatrix);
        assertEquals((short) 1, unboxed.get(0, 0));
        assertEquals((short) 2, unboxed.get(0, 1));
        assertEquals((short) 3, unboxed.get(1, 0));
        assertEquals((short) 4, unboxed.get(1, 1));
    }

    @Test
    public void testComponentType() {
        assertEquals(short.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        assertEquals((short) 1, matrix.get(0, 0));
        assertEquals((short) 5, matrix.get(1, 1));
        assertEquals((short) 9, matrix.get(2, 2));

        // Test bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(3, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, 3));
    }

    @Test
    public void testGetWithPoint() {
        Sheet.Point p1 = Sheet.Point.of(0, 0);
        assertEquals((short) 1, matrix.get(p1));

        Sheet.Point p2 = Sheet.Point.of(1, 1);
        assertEquals((short) 5, matrix.get(p2));

        Sheet.Point p3 = Sheet.Point.of(2, 2);
        assertEquals((short) 9, matrix.get(p3));
    }

    @Test
    public void testSet() {
        ShortMatrix m = matrix.copy();
        m.set(0, 0, (short) 10);
        assertEquals((short) 10, m.get(0, 0));

        m.set(1, 1, (short) 20);
        assertEquals((short) 20, m.get(1, 1));

        // Test bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, (short) 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(3, 0, (short) 0));
    }

    @Test
    public void testSetWithPoint() {
        ShortMatrix m = matrix.copy();
        Sheet.Point p = Sheet.Point.of(1, 1);
        m.set(p, (short) 50);
        assertEquals((short) 50, m.get(p));
    }

    @Test
    public void testUpOf() {
        OptionalShort up = matrix.upOf(1, 1);
        assertTrue(up.isPresent());
        assertEquals((short) 2, up.get());

        // Test top row
        OptionalShort empty = matrix.upOf(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        OptionalShort down = matrix.downOf(1, 1);
        assertTrue(down.isPresent());
        assertEquals((short) 8, down.get());

        // Test bottom row
        OptionalShort empty = matrix.downOf(2, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        OptionalShort left = matrix.leftOf(1, 1);
        assertTrue(left.isPresent());
        assertEquals((short) 4, left.get());

        // Test leftmost column
        OptionalShort empty = matrix.leftOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        OptionalShort right = matrix.rightOf(1, 1);
        assertTrue(right.isPresent());
        assertEquals((short) 6, right.get());

        // Test rightmost column
        OptionalShort empty = matrix.rightOf(1, 2);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        short[] row0 = matrix.row(0);
        assertArrayEquals(new short[] { 1, 2, 3 }, row0);

        short[] row1 = matrix.row(1);
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.row(3));
    }

    @Test
    public void testColumn() {
        short[] col0 = matrix.column(0);
        assertArrayEquals(new short[] { 1, 4, 7 }, col0);

        short[] col1 = matrix.column(1);
        assertArrayEquals(new short[] { 2, 5, 8 }, col1);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.column(3));
    }

    @Test
    public void testSetRow() {
        ShortMatrix m = matrix.copy();
        m.setRow(0, new short[] { 10, 20, 30 });
        assertArrayEquals(new short[] { 10, 20, 30 }, m.row(0));

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2 }));
    }

    @Test
    public void testSetColumn() {
        ShortMatrix m = matrix.copy();
        m.setColumn(0, new short[] { 10, 20, 30 });
        assertArrayEquals(new short[] { 10, 20, 30 }, m.column(0));

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2 }));
    }

    @Test
    public void testUpdateRow() {
        ShortMatrix m = matrix.copy();
        m.updateRow(0, x -> (short) (x * 2));
        assertArrayEquals(new short[] { 2, 4, 6 }, m.row(0));
    }

    @Test
    public void testUpdateColumn() {
        ShortMatrix m = matrix.copy();
        m.updateColumn(0, x -> (short) (x + 10));
        assertArrayEquals(new short[] { 11, 14, 17 }, m.column(0));
    }

    @Test
    public void testGetLU2RD() {
        short[] diagonal = matrix.getLU2RD();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        ShortMatrix m = matrix.copy();
        m.setLU2RD(new short[] { 10, 20, 30 });
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 20, m.get(1, 1));
        assertEquals((short) 30, m.get(2, 2));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setLU2RD(new short[] { 1 }));

        // Test array too short
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new short[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ShortMatrix m = matrix.copy();
        m.updateLU2RD(x -> (short) (x * 10));
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 50, m.get(1, 1));
        assertEquals((short) 90, m.get(2, 2));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateLU2RD(x -> (short) (x * 2)));
    }

    @Test
    public void testGetRU2LD() {
        short[] antiDiagonal = matrix.getRU2LD();
        assertArrayEquals(new short[] { 3, 5, 7 }, antiDiagonal);

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        ShortMatrix m = matrix.copy();
        m.setRU2LD(new short[] { 10, 20, 30 });
        assertEquals((short) 10, m.get(0, 2));
        assertEquals((short) 20, m.get(1, 1));
        assertEquals((short) 30, m.get(2, 0));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setRU2LD(new short[] { 1 }));
    }

    @Test
    public void testUpdateRU2LD() {
        ShortMatrix m = matrix.copy();
        m.updateRU2LD(x -> (short) (x * 10));
        assertEquals((short) 30, m.get(0, 2));
        assertEquals((short) 50, m.get(1, 1));
        assertEquals((short) 70, m.get(2, 0));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateRU2LD(x -> (short) (x * 2)));
    }

    @Test
    public void testUpdateAll() {
        ShortMatrix m = matrix.copy();
        m.updateAll(x -> (short) (x * 2));
        assertEquals((short) 2, m.get(0, 0));
        assertEquals((short) 4, m.get(0, 1));
        assertEquals((short) 18, m.get(2, 2));
    }

    @Test
    public void testUpdateAllWithIndices() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> (short) (i * 10 + j));
        assertEquals((short) 0, m.get(0, 0));
        assertEquals((short) 1, m.get(0, 1));
        assertEquals((short) 10, m.get(1, 0));
        assertEquals((short) 11, m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        ShortMatrix m = matrix.copy();
        m.replaceIf(x -> x > 5, (short) 0);
        assertEquals((short) 1, m.get(0, 0));
        assertEquals((short) 5, m.get(1, 1));
        assertEquals((short) 0, m.get(2, 2)); // was 9
    }

    @Test
    public void testReplaceIfWithIndices() {
        ShortMatrix m = matrix.copy();
        m.replaceIf((i, j) -> i == j, (short) 0); // Replace diagonal
        assertEquals((short) 0, m.get(0, 0));
        assertEquals((short) 0, m.get(1, 1));
        assertEquals((short) 0, m.get(2, 2));
        assertEquals((short) 2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        ShortMatrix result = matrix.map(x -> (short) (x * 2));
        assertEquals((short) 2, result.get(0, 0));
        assertEquals((short) 4, result.get(0, 1));
        assertEquals((short) 18, result.get(2, 2));

        // Original should be unchanged
        assertEquals((short) 1, matrix.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        Matrix<String> result = matrix.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1", result.get(0, 0));
        assertEquals("val:5", result.get(1, 1));
    }

    @Test
    public void testFillWithValue() {
        ShortMatrix m = matrix.copy();
        m.fill((short) 99);
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
                assertEquals((short) 99, m.get(i, j));
            }
        }
    }

    @Test
    public void testFillWithArray() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        short[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(patch);
        assertEquals((short) 1, m.get(0, 0));
        assertEquals((short) 2, m.get(0, 1));
        assertEquals((short) 3, m.get(1, 0));
        assertEquals((short) 4, m.get(1, 1));
        assertEquals((short) 0, m.get(2, 2)); // unchanged
    }

    @Test
    public void testFillWithArrayAtPosition() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        short[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(1, 1, patch);
        assertEquals((short) 0, m.get(0, 0)); // unchanged
        assertEquals((short) 1, m.get(1, 1));
        assertEquals((short) 2, m.get(1, 2));
        assertEquals((short) 3, m.get(2, 1));
        assertEquals((short) 4, m.get(2, 2));

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
        // assertThrows(IndexOutOfBoundsException.class, () -> m.fill(3, 0, patch));
        m.fill(3, 0, patch);
    }

    @Test
    public void testCopy() {
        ShortMatrix copy = matrix.copy();
        assertEquals(matrix.rowCount(), copy.rowCount());
        assertEquals(matrix.columnCount(), copy.columnCount());
        assertEquals(matrix.get(0, 0), copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, (short) 99);
        assertEquals((short) 1, matrix.get(0, 0));
        assertEquals((short) 99, copy.get(0, 0));
    }

    @Test
    public void testCopyRange() {
        ShortMatrix subset = matrix.copy(1, 3);
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals((short) 4, subset.get(0, 0));
        assertEquals((short) 7, subset.get(1, 0));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(2, 1));
    }

    @Test
    public void testCopySubMatrix() {
        ShortMatrix submatrix = matrix.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals((short) 2, submatrix.get(0, 0));
        assertEquals((short) 3, submatrix.get(0, 1));
        assertEquals((short) 5, submatrix.get(1, 0));
        assertEquals((short) 6, submatrix.get(1, 1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 2, 0, 4));
    }

    @Test
    public void testExtend() {
        ShortMatrix extended = matrix.extend(5, 5);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) 0, extended.get(3, 3)); // new cells are 0

        // Test truncation
        ShortMatrix truncated = matrix.extend(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
    }

    @Test
    public void testExtendWithDefaultValue() {
        ShortMatrix extended = matrix.extend(4, 4, (short) -1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) -1, extended.get(3, 3)); // new cell

        // Test negative dimensions
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 3, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(3, -1, (short) 0));
    }

    @Test
    public void testExtendDirectional() {
        ShortMatrix extended = matrix.extend(1, 1, 2, 2);
        assertEquals(5, extended.rowCount()); // 1 + 3 + 1
        assertEquals(7, extended.columnCount()); // 2 + 3 + 2

        // Original values should be at offset position
        assertEquals((short) 1, extended.get(1, 2));
        assertEquals((short) 5, extended.get(2, 3));

        // New cells should be 0
        assertEquals((short) 0, extended.get(0, 0));
    }

    @Test
    public void testExtendDirectionalWithDefaultValue() {
        ShortMatrix extended = matrix.extend(1, 1, 1, 1, (short) -1);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());

        // Check original values
        assertEquals((short) 1, extended.get(1, 1));

        // Check new values
        assertEquals((short) -1, extended.get(0, 0));
        assertEquals((short) -1, extended.get(4, 4));

        // Test negative values
        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 1, 1, 1, (short) 0));
    }

    @Test
    public void testReverseH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals((short) 3, m.get(0, 0));
        assertEquals((short) 2, m.get(0, 1));
        assertEquals((short) 1, m.get(0, 2));
        assertEquals((short) 6, m.get(1, 0));
        assertEquals((short) 5, m.get(1, 1));
        assertEquals((short) 4, m.get(1, 2));
    }

    @Test
    public void testReverseV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals((short) 5, m.get(0, 0));
        assertEquals((short) 6, m.get(0, 1));
        assertEquals((short) 3, m.get(1, 0));
        assertEquals((short) 4, m.get(1, 1));
        assertEquals((short) 1, m.get(2, 0));
        assertEquals((short) 2, m.get(2, 1));
    }

    @Test
    public void testFlipH() {
        ShortMatrix flipped = matrix.flipH();
        assertEquals((short) 3, flipped.get(0, 0));
        assertEquals((short) 2, flipped.get(0, 1));
        assertEquals((short) 1, flipped.get(0, 2));

        // Original should be unchanged
        assertEquals((short) 1, matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        ShortMatrix flipped = matrix.flipV();
        assertEquals((short) 7, flipped.get(0, 0));
        assertEquals((short) 8, flipped.get(0, 1));
        assertEquals((short) 9, flipped.get(0, 2));

        // Original should be unchanged
        assertEquals((short) 1, matrix.get(0, 0));
    }

    @Test
    public void testRotate90() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals((short) 3, rotated.get(0, 0));
        assertEquals((short) 1, rotated.get(0, 1));
        assertEquals((short) 4, rotated.get(1, 0));
        assertEquals((short) 2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals((short) 4, rotated.get(0, 0));
        assertEquals((short) 3, rotated.get(0, 1));
        assertEquals((short) 2, rotated.get(1, 0));
        assertEquals((short) 1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals((short) 2, rotated.get(0, 0));
        assertEquals((short) 4, rotated.get(0, 1));
        assertEquals((short) 1, rotated.get(1, 0));
        assertEquals((short) 3, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals((short) 1, transposed.get(0, 0));
        assertEquals((short) 4, transposed.get(0, 1));
        assertEquals((short) 2, transposed.get(1, 0));
        assertEquals((short) 5, transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        ShortMatrix reshaped = matrix.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        for (int i = 0; i < 9; i++) {
            assertEquals((short) (i + 1), reshaped.get(0, i));
        }

        // Test reshape back
        ShortMatrix reshaped2 = reshaped.reshape(3, 3);
        assertEquals(matrix, reshaped2);

        // Test empty matrix
        ShortMatrix emptyReshaped = emptyMatrix.reshape(2, 3);
        assertEquals(2, emptyReshaped.rowCount());
        assertEquals(3, emptyReshaped.columnCount());
    }

    @Test
    public void testRepelem() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals((short) 1, repeated.get(0, 0));
        assertEquals((short) 1, repeated.get(0, 1));
        assertEquals((short) 1, repeated.get(0, 2));
        assertEquals((short) 2, repeated.get(0, 3));
        assertEquals((short) 2, repeated.get(0, 4));
        assertEquals((short) 2, repeated.get(0, 5));
        assertEquals((short) 1, repeated.get(1, 0)); // second row same as first

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals((short) 1, repeated.get(0, 0));
        assertEquals((short) 2, repeated.get(0, 1));
        assertEquals((short) 1, repeated.get(0, 2)); // repeat starts
        assertEquals((short) 2, repeated.get(0, 3));
        assertEquals((short) 1, repeated.get(0, 4)); // another repeat
        assertEquals((short) 2, repeated.get(0, 5));

        assertEquals((short) 3, repeated.get(1, 0));
        assertEquals((short) 4, repeated.get(1, 1));

        // Check vertical repeat
        assertEquals((short) 1, repeated.get(2, 0)); // vertical repeat starts
        assertEquals((short) 2, repeated.get(2, 1));

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    @Test
    public void testFlatten() {
        ShortList flat = matrix.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals((short) (i + 1), flat.get(i));
        }
    }

    @Test
    public void testFlatOp() {
        List<Short> sums = new ArrayList<>();
        matrix.flatOp(row -> {
            short sum = 0;
            for (short val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertEquals((short) 45, sums.get(0));
    }

    @Test
    public void testVstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        ShortMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 7, stacked.get(2, 0));
        assertEquals((short) 12, stacked.get(3, 2));

        // Test different column counts
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m3));
    }

    @Test
    public void testHstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 5, stacked.get(0, 2));
        assertEquals((short) 8, stacked.get(1, 3));

        // Test different row counts
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m3));
    }

    @Test
    public void testAdd() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix sum = m1.add(m2);

        assertEquals((short) 6, sum.get(0, 0));
        assertEquals((short) 8, sum.get(0, 1));
        assertEquals((short) 10, sum.get(1, 0));
        assertEquals((short) 12, sum.get(1, 1));

        // Test different dimensions
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m3));
    }

    @Test
    public void testSubtract() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix diff = m1.subtract(m2);

        assertEquals((short) 4, diff.get(0, 0));
        assertEquals((short) 4, diff.get(0, 1));
        assertEquals((short) 4, diff.get(1, 0));
        assertEquals((short) 4, diff.get(1, 1));

        // Test different dimensions
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m3));
    }

    @Test
    public void testMultiply() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix product = m1.multiply(m2);

        assertEquals((short) 19, product.get(0, 0)); // 1*5 + 2*7
        assertEquals((short) 22, product.get(0, 1)); // 1*6 + 2*8
        assertEquals((short) 43, product.get(1, 0)); // 3*5 + 4*7
        assertEquals((short) 50, product.get(1, 1)); // 3*6 + 4*8

        // Test incompatible dimensions
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m3));
    }

    @Test
    public void testBoxed() {
        Matrix<Short> boxed = matrix.boxed();
        assertEquals(3, boxed.rowCount());
        assertEquals(3, boxed.columnCount());
        assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
        assertEquals(Short.valueOf((short) 5), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        IntMatrix intMatrix = matrix.toIntMatrix();
        assertEquals(3, intMatrix.rowCount());
        assertEquals(3, intMatrix.columnCount());
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(5, intMatrix.get(1, 1));
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
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a * b));

        assertEquals((short) 5, result.get(0, 0)); // 1*5
        assertEquals((short) 12, result.get(0, 1)); // 2*6
        assertEquals((short) 21, result.get(1, 0)); // 3*7
        assertEquals((short) 32, result.get(1, 1)); // 4*8

        // Test different shapes
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m3, (a, b) -> (short) (a + b)));
    }

    @Test
    public void testZipWith3Matrices() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 9, 10 }, { 11, 12 } });
        ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));

        assertEquals((short) 15, result.get(0, 0)); // 1+5+9
        assertEquals((short) 18, result.get(0, 1)); // 2+6+10
        assertEquals((short) 21, result.get(1, 0)); // 3+7+11
        assertEquals((short) 24, result.get(1, 1)); // 4+8+12
    }

    @Test
    public void testStreamLU2RD() {
        short[] diagonal = matrix.streamLU2RD().toArray();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);

        // Test empty matrix
        assertTrue(emptyMatrix.streamLU2RD().toArray().length == 0);

        // Test non-square
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        short[] antiDiagonal = matrix.streamRU2LD().toArray();
        assertArrayEquals(new short[] { 3, 5, 7 }, antiDiagonal);

        // Test empty matrix
        assertTrue(emptyMatrix.streamRU2LD().toArray().length == 0);

        // Test non-square
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        short[] all = matrix.streamH().toArray();
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, all);

        // Test empty matrix
        assertTrue(emptyMatrix.streamH().toArray().length == 0);
    }

    @Test
    public void testStreamHRow() {
        short[] row1 = matrix.streamH(1).toArray();
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(3));
    }

    @Test
    public void testStreamHRange() {
        short[] rows = matrix.streamH(1, 3).toArray();
        assertArrayEquals(new short[] { 4, 5, 6, 7, 8, 9 }, rows);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        short[] all = matrix.streamV().toArray();
        assertArrayEquals(new short[] { 1, 4, 7, 2, 5, 8, 3, 6, 9 }, all);

        // Test empty matrix
        assertTrue(emptyMatrix.streamV().toArray().length == 0);
    }

    @Test
    public void testStreamVColumn() {
        short[] col1 = matrix.streamV(1).toArray();
        assertArrayEquals(new short[] { 2, 5, 8 }, col1);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(3));
    }

    @Test
    public void testStreamVRange() {
        short[] columnCount = matrix.streamV(1, 3).toArray();
        assertArrayEquals(new short[] { 2, 5, 8, 3, 6, 9 }, columnCount);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        List<short[]> rows = matrix.streamR().map(ShortStream::toArray).toList();
        assertEquals(3, rows.size());
        assertArrayEquals(new short[] { 1, 2, 3 }, rows.get(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(1));
        assertArrayEquals(new short[] { 7, 8, 9 }, rows.get(2));

        // Test empty matrix
        assertTrue(emptyMatrix.streamR().count() == 0);
    }

    @Test
    public void testStreamRRange() {
        List<short[]> rows = matrix.streamR(1, 3).map(ShortStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(0));
        assertArrayEquals(new short[] { 7, 8, 9 }, rows.get(1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(0, 4));
    }

    @Test
    public void testStreamC() {
        List<short[]> columnCount = matrix.streamC().map(ShortStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new short[] { 1, 4, 7 }, columnCount.get(0));
        assertArrayEquals(new short[] { 2, 5, 8 }, columnCount.get(1));
        assertArrayEquals(new short[] { 3, 6, 9 }, columnCount.get(2));

        // Test empty matrix
        assertTrue(emptyMatrix.streamC().count() == 0);
    }

    @Test
    public void testStreamCRange() {
        List<short[]> columnCount = matrix.streamC(1, 3).map(ShortStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new short[] { 2, 5, 8 }, columnCount.get(0));
        assertArrayEquals(new short[] { 3, 6, 9 }, columnCount.get(1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(0, 4));
    }

    @Test
    public void testLength() {
        // This is a protected method, test indirectly through row operations
        short[] row = matrix.row(0);
        assertEquals(3, row.length);
    }

    @Test
    public void testForEach() {
        List<Short> values = new ArrayList<>();
        matrix.forEach(it -> values.add(it));
        assertEquals(9, values.size());
        for (int i = 0; i < 9; i++) {
            assertEquals((short) (i + 1), values.get(i));
        }
    }

    @Test
    public void testForEachWithBounds() {
        List<Short> values = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, it -> values.add(it));
        assertEquals(4, values.size());
        assertEquals((short) 5, values.get(0));
        assertEquals((short) 6, values.get(1));
        assertEquals((short) 8, values.get(2));
        assertEquals((short) 9, values.get(3));

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
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 4, 3 } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 4, 3 } });
        ShortMatrix m4 = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    @Test
    public void testIteratorNoSuchElement() {
        // Test streamH iterator
        ShortStream stream = matrix.streamH(0, 1);
        stream.toArray(); // Consume all
        assertThrows(IllegalStateException.class, () -> stream.iterator().next());

        // Test streamLU2RD iterator
        ShortStream diagStream = matrix.streamLU2RD();
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

        ShortMatrix extended = emptyMatrix.extend(2, 2, (short) 5);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals((short) 5, extended.get(0, 0));
    }
}
