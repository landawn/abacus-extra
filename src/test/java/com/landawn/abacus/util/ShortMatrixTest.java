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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalShort;
import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.util.stream.Stream;

class ShortMatrixTest extends TestBase {

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
    public void testDiagonalRU2LD() {
        ShortMatrix m = ShortMatrix.antiDiagonal(new short[] { 1, 2, 3 });
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
        ShortMatrix m = ShortMatrix.diagonals(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals((short) 1, m.get(0, 0));
        assertEquals((short) 2, m.get(1, 1));
        assertEquals((short) 3, m.get(2, 2));
        assertEquals((short) 4, m.get(0, 2));
        assertEquals((short) 2, m.get(1, 1)); // Overwritten
        assertEquals((short) 6, m.get(2, 0));

        // Test with only main diagonal
        ShortMatrix m2 = ShortMatrix.diagonals(new short[] { 1, 2, 3 }, null);
        assertEquals((short) 1, m2.get(0, 0));
        assertEquals((short) 2, m2.get(1, 1));
        assertEquals((short) 3, m2.get(2, 2));

        // Test with only anti-diagonal
        ShortMatrix m3 = ShortMatrix.diagonals(null, new short[] { 4, 5, 6 });
        assertEquals((short) 4, m3.get(0, 2));
        assertEquals((short) 5, m3.get(1, 1));
        assertEquals((short) 6, m3.get(2, 0));

        // Test with empty arrays
        ShortMatrix empty = ShortMatrix.diagonals(null, null);
        assertTrue(empty.isEmpty());

        // Test illegal argument
        assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonals(new short[] { 1, 2 }, new short[] { 3, 4, 5 }));
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
        OptionalShort up = matrix.above(1, 1);
        assertTrue(up.isPresent());
        assertEquals((short) 2, up.get());

        // Test top row
        OptionalShort empty = matrix.above(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        OptionalShort down = matrix.below(1, 1);
        assertTrue(down.isPresent());
        assertEquals((short) 8, down.get());

        // Test bottom row
        OptionalShort empty = matrix.below(2, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        OptionalShort left = matrix.left(1, 1);
        assertTrue(left.isPresent());
        assertEquals((short) 4, left.get());

        // Test leftmost column
        OptionalShort empty = matrix.left(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        OptionalShort right = matrix.right(1, 1);
        assertTrue(right.isPresent());
        assertEquals((short) 6, right.get());

        // Test rightmost column
        OptionalShort empty = matrix.right(1, 2);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        short[] row0 = matrix.rowView(0);
        assertArrayEquals(new short[] { 1, 2, 3 }, row0);

        short[] row1 = matrix.rowView(1);
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(3));
    }

    // Verify rowCopy returns a defensive row snapshot and enforces row bounds.
    @Test
    public void testRowCopy() {
        short[] rowCopy = matrix.rowCopy(0);
        assertArrayEquals(new short[] { 1, 2, 3 }, rowCopy);

        rowCopy[0] = 99;
        assertArrayEquals(new short[] { 1, 2, 3 }, matrix.rowView(0));
    }

    @Test
    public void testRowCopy_InvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(3));
    }

    @Test
    public void testColumn() {
        short[] col0 = matrix.columnCopy(0);
        assertArrayEquals(new short[] { 1, 4, 7 }, col0);

        short[] col1 = matrix.columnCopy(1);
        assertArrayEquals(new short[] { 2, 5, 8 }, col1);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(3));
    }

    @Test
    public void testSetRow() {
        ShortMatrix m = matrix.copy();
        m.setRow(0, new short[] { 10, 20, 30 });
        assertArrayEquals(new short[] { 10, 20, 30 }, m.rowView(0));

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2 }));
    }

    @Test
    public void testSetColumn() {
        ShortMatrix m = matrix.copy();
        m.setColumn(0, new short[] { 10, 20, 30 });
        assertArrayEquals(new short[] { 10, 20, 30 }, m.columnCopy(0));

        // Test wrong size
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2 }));
    }

    @Test
    public void testUpdateRow() {
        ShortMatrix m = matrix.copy();
        m.updateRow(0, x -> (short) (x * 2));
        assertArrayEquals(new short[] { 2, 4, 6 }, m.rowView(0));
    }

    @Test
    public void testUpdateColumn() {
        ShortMatrix m = matrix.copy();
        m.updateColumn(0, x -> (short) (x + 10));
        assertArrayEquals(new short[] { 11, 14, 17 }, m.columnCopy(0));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        ShortMatrix m = matrix.copy();

        assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(-1, x -> (short) (x * 2)));
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(3, x -> (short) (x + 10)));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        ShortMatrix m = matrix.copy();

        assertThrows(IllegalArgumentException.class, () -> m.updateRow(0, (Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> m.updateColumn(0, (Throwables.ShortUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        short[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        ShortMatrix m = matrix.copy();
        m.setMainDiagonal(new short[] { 10, 20, 30 });
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 20, m.get(1, 1));
        assertEquals((short) 30, m.get(2, 2));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setMainDiagonal(new short[] { 1 }));

        // Test array too short
        assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new short[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ShortMatrix m = matrix.copy();
        m.updateMainDiagonal(x -> (short) (x * 10));
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 50, m.get(1, 1));
        assertEquals((short) 90, m.get(2, 2));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateMainDiagonal(x -> (short) (x * 2)));
    }

    @Test
    public void testGetRU2LD() {
        short[] antiDiagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new short[] { 3, 5, 7 }, antiDiagonal);

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getAntiDiagonal());
    }

    @Test
    public void testSetRU2LD() {
        ShortMatrix m = matrix.copy();
        m.setAntiDiagonal(new short[] { 10, 20, 30 });
        assertEquals((short) 10, m.get(0, 2));
        assertEquals((short) 20, m.get(1, 1));
        assertEquals((short) 30, m.get(2, 0));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.setAntiDiagonal(new short[] { 1 }));
    }

    @Test
    public void testUpdateRU2LD() {
        ShortMatrix m = matrix.copy();
        m.updateAntiDiagonal(x -> (short) (x * 10));
        assertEquals((short) 30, m.get(0, 2));
        assertEquals((short) 50, m.get(1, 1));
        assertEquals((short) 70, m.get(2, 0));

        // Test non-square matrix
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.updateAntiDiagonal(x -> (short) (x * 2)));
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
    public void testMapNullMapper() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

        assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.ShortFunction<String, RuntimeException>) null, String.class));
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
        m.copyFrom(patch);
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
        m.copyFrom(1, 1, patch);
        assertEquals((short) 0, m.get(0, 0)); // unchanged
        assertEquals((short) 1, m.get(1, 1));
        assertEquals((short) 2, m.get(1, 2));
        assertEquals((short) 3, m.get(2, 1));
        assertEquals((short) 4, m.get(2, 2));

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        // assertThrows(IndexOutOfBoundsException.class, () -> m.copyFrom(3, 0, patch));
        m.copyFrom(3, 0, patch);
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
        ShortMatrix extended = matrix.resize(5, 5);
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) 0, extended.get(3, 3)); // new cells are 0

        // Test truncation
        ShortMatrix truncated = matrix.resize(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
    }

    @Test
    public void testExtendWithDefaultValue() {
        ShortMatrix extended = matrix.resize(4, 4, (short) -1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) -1, extended.get(3, 3)); // new cell

        // Test negative dimensions
        assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 3, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> matrix.resize(3, -1, (short) 0));
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
        m.flipInPlaceHorizontally();
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
        m.flipInPlaceVertically();
        assertEquals((short) 5, m.get(0, 0));
        assertEquals((short) 6, m.get(0, 1));
        assertEquals((short) 3, m.get(1, 0));
        assertEquals((short) 4, m.get(1, 1));
        assertEquals((short) 1, m.get(2, 0));
        assertEquals((short) 2, m.get(2, 1));
    }

    @Test
    public void testFlipH() {
        ShortMatrix flipped = matrix.flipHorizontally();
        assertEquals((short) 3, flipped.get(0, 0));
        assertEquals((short) 2, flipped.get(0, 1));
        assertEquals((short) 1, flipped.get(0, 2));

        // Original should be unchanged
        assertEquals((short) 1, matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        ShortMatrix flipped = matrix.flipVertically();
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

        // Test reshape with too-small dimensions throws exception
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix repeated = m.repeatElements(2, 3);
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
        assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
    }

    @Test
    public void testRepmat() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix repeated = m.repeatMatrix(2, 3);
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
        assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
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
        matrix.applyOnFlattened(row -> {
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
        ShortMatrix stacked = m1.stackVertically(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 7, stacked.get(2, 0));
        assertEquals((short) 12, stacked.get(3, 2));

        // Test different column counts
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m3));
    }

    @Test
    public void testHstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix stacked = m1.stackHorizontally(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 5, stacked.get(0, 2));
        assertEquals((short) 8, stacked.get(1, 3));

        // Test different row counts
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m3));
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
        short[] diagonal = matrix.streamMainDiagonal().toArray();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);

        // Test empty matrix
        assertTrue(emptyMatrix.streamMainDiagonal().toArray().length == 0);

        // Test non-square
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
    }

    @Test
    public void testStreamRU2LD() {
        short[] antiDiagonal = matrix.streamAntiDiagonal().toArray();
        assertArrayEquals(new short[] { 3, 5, 7 }, antiDiagonal);

        // Test empty matrix
        assertTrue(emptyMatrix.streamAntiDiagonal().toArray().length == 0);

        // Test non-square
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
    }

    @Test
    public void testStreamH() {
        short[] all = matrix.streamHorizontal().toArray();
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, all);

        // Test empty matrix
        assertTrue(emptyMatrix.streamHorizontal().toArray().length == 0);
    }

    @Test
    public void testStreamHRow() {
        short[] row1 = matrix.streamHorizontal(1).toArray();
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(3));
    }

    @Test
    public void testStreamHRange() {
        short[] rows = matrix.streamHorizontal(1, 3).toArray();
        assertArrayEquals(new short[] { 4, 5, 6, 7, 8, 9 }, rows);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(2, 1));
    }

    @Test
    public void testStreamV() {
        short[] all = matrix.streamVertical().toArray();
        assertArrayEquals(new short[] { 1, 4, 7, 2, 5, 8, 3, 6, 9 }, all);

        // Test empty matrix
        assertTrue(emptyMatrix.streamVertical().toArray().length == 0);
    }

    @Test
    public void testStreamVColumn() {
        short[] col1 = matrix.streamVertical(1).toArray();
        assertArrayEquals(new short[] { 2, 5, 8 }, col1);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(3));
    }

    @Test
    public void testStreamVRange() {
        short[] columnCount = matrix.streamVertical(1, 3).toArray();
        assertArrayEquals(new short[] { 2, 5, 8, 3, 6, 9 }, columnCount);

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(2, 1));
    }

    @Test
    public void testStreamR() {
        List<short[]> rows = matrix.streamRows().map(ShortStream::toArray).toList();
        assertEquals(3, rows.size());
        assertArrayEquals(new short[] { 1, 2, 3 }, rows.get(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(1));
        assertArrayEquals(new short[] { 7, 8, 9 }, rows.get(2));

        // Test empty matrix
        assertTrue(emptyMatrix.streamRows().count() == 0);
    }

    @Test
    public void testStreamRRange() {
        List<short[]> rows = matrix.streamRows(1, 3).map(ShortStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(0));
        assertArrayEquals(new short[] { 7, 8, 9 }, rows.get(1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamRows(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamRows(0, 4));
    }

    @Test
    public void testStreamC() {
        List<short[]> columnCount = matrix.streamColumns().map(ShortStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new short[] { 1, 4, 7 }, columnCount.get(0));
        assertArrayEquals(new short[] { 2, 5, 8 }, columnCount.get(1));
        assertArrayEquals(new short[] { 3, 6, 9 }, columnCount.get(2));

        // Test empty matrix
        assertTrue(emptyMatrix.streamColumns().count() == 0);
    }

    @Test
    public void testStreamCRange() {
        List<short[]> columnCount = matrix.streamColumns(1, 3).map(ShortStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new short[] { 2, 5, 8 }, columnCount.get(0));
        assertArrayEquals(new short[] { 3, 6, 9 }, columnCount.get(1));

        // Test bounds
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamColumns(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamColumns(0, 4));
    }

    @Test
    public void testLength() {
        // This is a protected method, test indirectly through row operations
        short[] row = matrix.rowView(0);
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
    public void testForEachNullAction() {
        assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.ShortConsumer<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> matrix.forEach(0, matrix.rowCount(), 0, matrix.columnCount(), (Throwables.ShortConsumer<RuntimeException>) null));
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
        // Test streamHorizontal iterator
        ShortStream stream = matrix.streamHorizontal(0, 1);
        stream.toArray(); // Consume all
        assertThrows(IllegalStateException.class, () -> stream.iterator().next());

        // Test streamMainDiagonal iterator
        ShortStream diagStream = matrix.streamMainDiagonal();
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

        ShortMatrix extended = emptyMatrix.resize(2, 2, (short) 5);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals((short) 5, extended.get(0, 0));
    }

    @Nested
    class JavadocExampleMatrixGroup1Test_ShortMatrix extends TestBase {
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

    @Nested
    class JavadocExampleMatrixTest_ShortMatrix extends TestBase {
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
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_ShortMatrix extends TestBase {
        @Test
        public void testShortMatrixRowsForZeroColumnMatrix() {
            final ShortMatrix matrix = ShortMatrix.of(new short[][] { {}, {}, {} });
            final List<short[]> rows = matrix.streamRows().map(ShortStream::toArray).toList();

            assertEquals(3, rows.size());
            assertArrayEquals(new short[0], rows.get(0));
            assertArrayEquals(new short[0], rows.get(1));
            assertArrayEquals(new short[0], rows.get(2));
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_ShortMatrix extends TestBase {
        @Test
        public void testShortMatrixUpdateAllNullOperator() {
            ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix emptyLike = ShortMatrix.of(new short[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.ShortUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Short, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.ShortPredicate<RuntimeException>) null, (short) 0));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (short) 0));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.ShortPredicate<RuntimeException>) null, (short) 0));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (short) 0));
        }

        @Test
        public void testShortMatrixBinaryOpsRejectNullMatrix() {
            ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.add(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.subtract(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.multiply(null));
        }
    }

    @Nested
    @Tag("2025")
    class ShortMatrix2025Test extends TestBase {
        @Test
        public void testConstructor_withEmptyArray() {
            ShortMatrix m = new ShortMatrix(new short[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withValidArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = ShortMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            ShortMatrix m = ShortMatrix.of((short[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            ShortMatrix m = ShortMatrix.of(new short[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testRandom() {
            ShortMatrix m = ShortMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist (values are random)
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRandom_withRowsCols() {
            ShortMatrix m = ShortMatrix.random(2, 3);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertNotNull(m.get(i, j));
                }
            }
        }

        @Test
        public void testRepeat() {
            ShortMatrix m = ShortMatrix.repeat(1, 5, (short) 42);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(42, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withRowsCols() {
            ShortMatrix m = ShortMatrix.repeat(2, 3, (short) 42);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(42, m.get(i, j));
                }
            }
        }

        @Test
        public void testRange() {
            ShortMatrix m = ShortMatrix.range((short) 0, (short) 5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new short[] { 0, 1, 2, 3, 4 }, m.rowView(0));
        }

        @Test
        public void testRange_withStep() {
            ShortMatrix m = ShortMatrix.range((short) 0, (short) 10, (short) 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new short[] { 0, 2, 4, 6, 8 }, m.rowView(0));
        }

        @Test
        public void testRange_withNegativeStep() {
            ShortMatrix m = ShortMatrix.range((short) 10, (short) 0, (short) -2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new short[] { 10, 8, 6, 4, 2 }, m.rowView(0));
        }

        @Test
        public void testRangeClosed() {
            ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 4);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new short[] { 0, 1, 2, 3, 4 }, m.rowView(0));
        }

        @Test
        public void testRangeClosed_withStep() {
            ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 10, (short) 2);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new short[] { 0, 2, 4, 6, 8, 10 }, m.rowView(0));
        }

        @Test
        public void testDiagonalLU2RD() {
            ShortMatrix m = ShortMatrix.mainDiagonal(new short[] { 1, 2, 3 });
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
            ShortMatrix m = ShortMatrix.antiDiagonal(new short[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            ShortMatrix m = ShortMatrix.diagonals(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(4, m.get(0, 2));
            assertEquals(6, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            ShortMatrix m = ShortMatrix.diagonals(new short[] { 1, 2, 3 }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            ShortMatrix m = ShortMatrix.diagonals(null, new short[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(4, m.get(0, 2));
            assertEquals(5, m.get(1, 1));
            assertEquals(6, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            ShortMatrix m = ShortMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonals(new short[] { 1, 2 }, new short[] { 3, 4, 5 }));
        }

        @Test
        public void testUnbox() {
            Short[][] boxed = { { 1, 2 }, { 3, 4 } };
            Matrix<Short> boxedMatrix = Matrix.of(boxed);
            ShortMatrix unboxed = ShortMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1, unboxed.get(0, 0));
            assertEquals(4, unboxed.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Short[][] boxed = { { 1, null }, { null, 4 } };
            Matrix<Short> boxedMatrix = Matrix.of(boxed);
            ShortMatrix unboxed = ShortMatrix.unbox(boxedMatrix);
            assertEquals(1, unboxed.get(0, 0));
            assertEquals(0, unboxed.get(0, 1)); // null -> 0
            assertEquals(0, unboxed.get(1, 0)); // null -> 0
            assertEquals(4, unboxed.get(1, 1));
        }
        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals(1, m.get(0, 0));
            assertEquals(5, m.get(1, 1));
            assertEquals(6, m.get(1, 2));
        }

        @Test
        public void testGet_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(1, m.get(Point.of(0, 0)));
            assertEquals(4, m.get(Point.of(1, 1)));
            assertEquals(2, m.get(Point.of(0, 1)));
        }

        @Test
        public void testSet() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.set(0, 0, (short) 10);
            assertEquals(10, m.get(0, 0));

            m.set(1, 1, (short) 20);
            assertEquals(20, m.get(1, 1));
        }

        @Test
        public void testSet_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, (short) 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, (short) 0));
        }

        @Test
        public void testSetWithPoint() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.set(Point.of(0, 0), (short) 50);
            assertEquals(50, m.get(Point.of(0, 0)));
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

            OptionalShort up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1, up.get());

            // Top row has no element above
            OptionalShort empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

            OptionalShort down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3, down.get());

            // Bottom row has no element below
            OptionalShort empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

            OptionalShort left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1, left.get());

            // Leftmost column has no element to the left
            OptionalShort empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

            OptionalShort right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2, right.get());

            // Rightmost column has no element to the right
            OptionalShort empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertArrayEquals(new short[] { 1, 2, 3 }, m.rowView(0));
            assertArrayEquals(new short[] { 4, 5, 6 }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertArrayEquals(new short[] { 1, 4 }, m.columnCopy(0));
            assertArrayEquals(new short[] { 2, 5 }, m.columnCopy(1));
            assertArrayEquals(new short[] { 3, 6 }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.setRow(0, new short[] { 10, 20 });
            assertArrayEquals(new short[] { 10, 20 }, m.rowView(0));
            assertArrayEquals(new short[] { 3, 4 }, m.rowView(1)); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1 }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2, 3 }));
        }

        @Test
        public void testSetColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.setColumn(0, new short[] { 10, 20 });
            assertArrayEquals(new short[] { 10, 20 }, m.columnCopy(0));
            assertArrayEquals(new short[] { 2, 4 }, m.columnCopy(1)); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1 }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2, 3 }));
        }

        @Test
        public void testUpdateRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.updateRow(0, x -> (short) (x * 2));
            assertArrayEquals(new short[] { 2, 4 }, m.rowView(0));
            assertArrayEquals(new short[] { 3, 4 }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.updateColumn(0, x -> (short) (x + 10));
            assertArrayEquals(new short[] { 11, 13 }, m.columnCopy(0));
            assertArrayEquals(new short[] { 2, 4 }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertArrayEquals(new short[] { 1, 5, 9 }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setMainDiagonal(new short[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new short[] { 1 }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new short[] { 1, 2 }));
        }

        @Test
        public void testUpdateLU2RD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateMainDiagonal(x -> (short) (x * 10));
            assertEquals(10, m.get(0, 0));
            assertEquals(50, m.get(1, 1));
            assertEquals(90, m.get(2, 2));
            assertEquals(2, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> (short) (x * 2)));
        }

        @Test
        public void testGetRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertArrayEquals(new short[] { 3, 5, 7 }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setAntiDiagonal(new short[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 2));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new short[] { 1 }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new short[] { 1, 2 }));
        }

        @Test
        public void testUpdateRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateAntiDiagonal(x -> (short) (x * 10));
            assertEquals(30, m.get(0, 2));
            assertEquals(50, m.get(1, 1));
            assertEquals(70, m.get(2, 0));
            assertEquals(2, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> (short) (x * 2)));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll(x -> (short) (x * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(6, m.get(1, 0));
            assertEquals(8, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_withIndices() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0 }, { 0, 0 } });
            m.updateAll((i, j) -> (short) (i * 10 + j));
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(10, m.get(1, 0));
            assertEquals(11, m.get(1, 1));
        }

        @Test
        public void testReplaceIf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.replaceIf(x -> x > 3, (short) 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
            assertEquals(0, m.get(1, 0)); // was 4
            assertEquals(0, m.get(1, 1)); // was 5
            assertEquals(0, m.get(1, 2)); // was 6
        }

        @Test
        public void testReplaceIf_withIndices() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.replaceIf((i, j) -> i == j, (short) 0); // Replace diagonal
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(1, 1));
            assertEquals(0, m.get(2, 2));
            assertEquals(2, m.get(0, 1)); // unchanged
        }

        @Test
        public void testMap() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m.map(x -> (short) (x * 2));
            assertEquals(2, result.get(0, 0));
            assertEquals(4, result.get(0, 1));
            assertEquals(6, result.get(1, 0));
            assertEquals(8, result.get(1, 1));

            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
            assertEquals("val:1", result.get(0, 0));
            assertEquals("val:4", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.fill((short) 99);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals(99, m.get(i, j));
                }
            }
        }

        @Test
        public void testFill_withArray() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            short[][] patch = { { 1, 2 }, { 3, 4 } };
            m.copyFrom(patch);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
            assertEquals(0, m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            short[][] patch = { { 1, 2 }, { 3, 4 } };
            m.copyFrom(1, 1, patch);
            assertEquals(0, m.get(0, 0)); // unchanged
            assertEquals(1, m.get(1, 1));
            assertEquals(2, m.get(1, 2));
            assertEquals(3, m.get(2, 1));
            assertEquals(4, m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            short[][] patch = { { 1, 2 }, { 3, 4 } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1, copy.get(0, 0));

            // Modify copy shouldn't affect original
            copy.set(0, 0, (short) 99);
            assertEquals(1, m.get(0, 0));
            assertEquals(99, copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals(4, subset.get(0, 0));
            assertEquals(9, subset.get(1, 2));
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(2, submatrix.get(0, 0));
            assertEquals(6, submatrix.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
            assertEquals(0, extended.get(3, 3)); // new cells are 0
        }

        @Test
        public void testExtend_smaller() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1, truncated.get(0, 0));
            assertEquals(5, truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix extended = m.resize(3, 3, (short) -1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(-1, extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, (short) 0));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, (short) 0));
        }

        @Test
        public void testExtend_directional() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertEquals(1, extended.get(1, 2));
            assertEquals(5, extended.get(2, 3));

            // New cells are 0
            assertEquals(0, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalWithDefault() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix extended = m.extend(1, 1, 1, 1, (short) -1);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals(1, extended.get(1, 1));

            // Check new values
            assertEquals(-1, extended.get(0, 0));
            assertEquals(-1, extended.get(4, 4));
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, (short) 0));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.flipInPlaceHorizontally();
            assertEquals(3, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(1, m.get(0, 2));
            assertEquals(6, m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            m.flipInPlaceVertically();
            assertEquals(5, m.get(0, 0));
            assertEquals(6, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(1, m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix flipped = m.flipHorizontally();
            assertEquals(3, flipped.get(0, 0));
            assertEquals(2, flipped.get(0, 1));
            assertEquals(1, flipped.get(0, 2));

            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            ShortMatrix flipped = m.flipVertically();
            assertEquals(5, flipped.get(0, 0));
            assertEquals(3, flipped.get(1, 0));
            assertEquals(1, flipped.get(2, 0));

            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
            assertEquals(4, rotated.get(1, 0));
            assertEquals(2, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4, rotated.get(0, 0));
            assertEquals(3, rotated.get(0, 1));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(1, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2, rotated.get(0, 0));
            assertEquals(4, rotated.get(0, 1));
            assertEquals(1, rotated.get(1, 0));
            assertEquals(3, rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(4, transposed.get(0, 1));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(5, transposed.get(1, 1));
            assertEquals(3, transposed.get(2, 0));
            assertEquals(6, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(3, transposed.get(0, 1));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(4, transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, reshaped.get(0, i));
            }
        }

        @Test
        public void testReshape_back() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix reshaped = m.reshape(1, 9);
            ShortMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            ShortMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem_invalidArguments() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat_invalidArguments() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortList flat = m.flatten();
            assertEquals(9, flat.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, flat.get(i));
            }
        }

        @Test
        public void testFlatten_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            ShortList flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> sums = new ArrayList<>();
            m.applyOnFlattened(row -> {
                int sum = 0;
                for (short val : row) {
                    sum += val;
                }
                sums.add(sum);
            });
            assertEquals(1, sums.size());
            assertEquals(45, sums.get(0).intValue());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 7, 8, 9 }, { 10, 11, 12 } });
            ShortMatrix stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(7, stacked.get(2, 0));
            assertEquals(12, stacked.get(3, 2));
        }

        @Test
        public void testVstack_differentColumnCounts() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(5, stacked.get(0, 2));
            assertEquals(8, stacked.get(1, 3));
        }

        @Test
        public void testHstack_differentRowCounts() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix sum = m1.add(m2);

            assertEquals(6, sum.get(0, 0));
            assertEquals(8, sum.get(0, 1));
            assertEquals(10, sum.get(1, 0));
            assertEquals(12, sum.get(1, 1));
        }

        @Test
        public void testAdd_differentDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix diff = m1.subtract(m2);

            assertEquals(4, diff.get(0, 0));
            assertEquals(4, diff.get(0, 1));
            assertEquals(4, diff.get(1, 0));
            assertEquals(4, diff.get(1, 1));
        }

        @Test
        public void testSubtract_differentDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix product = m1.multiply(m2);

            assertEquals(19, product.get(0, 0)); // 1*5 + 2*7
            assertEquals(22, product.get(0, 1)); // 1*6 + 2*8
            assertEquals(43, product.get(1, 0)); // 3*5 + 4*7
            assertEquals(50, product.get(1, 1)); // 3*6 + 4*8
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        @Test
        public void testMultiply_rectangularMatrices() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 } }); // 1x3
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 4 }, { 5 }, { 6 } }); // 3x1
            ShortMatrix product = m1.multiply(m2);

            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(32, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Short> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
            assertEquals(Short.valueOf((short) 6), boxed.get(1, 2));
        }

        @Test
        public void testToIntMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix intMatrix = m.toIntMatrix();
            assertEquals(2, intMatrix.rowCount());
            assertEquals(2, intMatrix.columnCount());
            assertEquals(1, intMatrix.get(0, 0));
            assertEquals(4, intMatrix.get(1, 1));
        }

        @Test
        public void testToLongMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix longMatrix = m.toLongMatrix();
            assertEquals(2, longMatrix.rowCount());
            assertEquals(2, longMatrix.columnCount());
            assertEquals(1L, longMatrix.get(0, 0));
            assertEquals(4L, longMatrix.get(1, 1));
        }

        @Test
        public void testToFloatMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals(2, floatMatrix.rowCount());
            assertEquals(2, floatMatrix.columnCount());
            assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
            assertEquals(4.0f, floatMatrix.get(1, 1), 0.0001f);
        }

        @Test
        public void testToDoubleMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(2, doubleMatrix.rowCount());
            assertEquals(2, doubleMatrix.columnCount());
            assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
            assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a * b));

            assertEquals(5, result.get(0, 0)); // 1*5
            assertEquals(12, result.get(0, 1)); // 2*6
            assertEquals(21, result.get(1, 0)); // 3*7
            assertEquals(32, result.get(1, 1)); // 4*8
        }

        @Test
        public void testZipWith_differentShapes() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (short) (a + b)));
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 9, 10, 11 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c)));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            short[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);
        }

        @Test
        public void testStreamLU2RD_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().toArray().length);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            short[] antiDiagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new short[] { 3, 5, 7 }, antiDiagonal);
        }

        @Test
        public void testStreamRU2LD_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().toArray().length);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            short[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, all);
        }

        @Test
        public void testStreamH_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.streamHorizontal().toArray().length);
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            short[] all = m.streamVertical().toArray();
            assertArrayEquals(new short[] { 1, 4, 2, 5, 3, 6 }, all);
        }

        @Test
        public void testStreamV_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.streamVertical().toArray().length);
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            short[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new short[] { 2, 5, 8, 3, 6, 9 }, columnCount);
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<short[]> rows = m.streamRows().map(ShortStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new short[] { 1, 2, 3 }, rows.get(0));
            assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(1));
        }

        @Test
        public void testStreamR_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<short[]> rows = m.streamRows(1, 3).map(ShortStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(0));
            assertArrayEquals(new short[] { 7, 8, 9 }, rows.get(1));
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<short[]> columnCount = m.streamColumns().map(ShortStream::toArray).toList();
            assertEquals(3, columnCount.size());
            assertArrayEquals(new short[] { 1, 4 }, columnCount.get(0));
            assertArrayEquals(new short[] { 2, 5 }, columnCount.get(1));
            assertArrayEquals(new short[] { 3, 6 }, columnCount.get(2));
        }

        @Test
        public void testStreamC_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<short[]> columnCount = m.streamColumns(1, 3).map(ShortStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new short[] { 2, 5, 8 }, columnCount.get(0));
            assertArrayEquals(new short[] { 3, 6, 9 }, columnCount.get(1));
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ Object Methods Tests ============

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
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
            assertTrue(str.contains("4"));
        }

        // ============ Edge Case Tests ============

        @Test
        public void testShortMinMaxValues() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { Short.MAX_VALUE, Short.MIN_VALUE } });
            assertEquals(Short.MAX_VALUE, m.get(0, 0));
            assertEquals(Short.MIN_VALUE, m.get(0, 1));
        }

        @Test
        public void testShortOverflow() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { Short.MAX_VALUE, 1 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, Short.MAX_VALUE } });

            // Addition overflow wraps around
            ShortMatrix sum = m1.add(m2);
            assertTrue(sum.get(0, 0) < 0); // Overflow
        }

        @Test
        public void testNegativeShorts() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { -1, -2 }, { -3, -4 } });

            assertEquals(-1, m.get(0, 0));
            assertEquals(-4, m.get(1, 1));

            ShortMatrix doubled = m.map(x -> (short) (x * 2));
            assertEquals(-2, doubled.get(0, 0));
            assertEquals(-8, doubled.get(1, 1));
        }

        @Test
        public void testEmptyMatrixOperations() {
            ShortMatrix empty = ShortMatrix.empty();

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            ShortMatrix extended = empty.resize(2, 2, (short) 5);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(5, extended.get(0, 0));
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Short> values = new ArrayList<>();
            m.forEach((short v) -> values.add(v));
            assertEquals(4, values.size());
            assertEquals(Short.valueOf((short) 1), values.get(0));
            assertEquals(Short.valueOf((short) 2), values.get(1));
            assertEquals(Short.valueOf((short) 3), values.get(2));
            assertEquals(Short.valueOf((short) 4), values.get(3));
        }

        @Test
        public void testForEach_withRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Short> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, (short v) -> values.add(v));
            assertEquals(4, values.size());
            assertEquals(Short.valueOf((short) 5), values.get(0));
            assertEquals(Short.valueOf((short) 6), values.get(1));
            assertEquals(Short.valueOf((short) 8), values.get(2));
            assertEquals(Short.valueOf((short) 9), values.get(3));
        }

        @Test
        public void testForEach_emptyRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Short> values = new ArrayList<>();
            m.forEach(0, 0, 0, 0, (short v) -> values.add(v));
            assertTrue(values.isEmpty());
        }

        @Test
        public void testForEach_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, x -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, x -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, -1, 2, x -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, 0, 3, x -> {
            }));
        }

        // ============ Additional Edge Case Tests ============

        @Test
        public void testRepeat_zeroLength() {
            ShortMatrix m = ShortMatrix.repeat(1, 0, (short) 5);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRandom_zeroLength() {
            ShortMatrix m = ShortMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRange_emptyRange() {
            ShortMatrix m = ShortMatrix.range((short) 5, (short) 5);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRangeClosed_singleElement() {
            ShortMatrix m = ShortMatrix.rangeClosed((short) 5, (short) 5);
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5, m.get(0, 0));
        }

        @Test
        public void testExtend_sameSize() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix extended = m.resize(2, 2);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
        }

        @Test
        public void testExtend_emptyMatrix() {
            ShortMatrix empty = ShortMatrix.empty();
            ShortMatrix extended = empty.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(0, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalZeroExtension() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix extended = m.extend(0, 0, 0, 0);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
        }

        @Test
        public void testRepelem_oneDimension() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix repeated = m.repeatElements(1, 2);
            assertEquals(1, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(2, repeated.get(0, 2));
            assertEquals(2, repeated.get(0, 3));
        }

        @Test
        public void testRepmat_oneDimension() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix repeated = m.repeatMatrix(1, 2);
            assertEquals(1, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2));
            assertEquals(2, repeated.get(0, 3));
        }

        @Test
        public void testCopy_entireMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix copy = m.copy(0, 2);
            assertEquals(2, copy.rowCount());
            assertEquals(3, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(6, copy.get(1, 2));
        }

        @Test
        public void testCopy_entireMatrixWithColumnRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix copy = m.copy(0, 2, 0, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(3, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(6, copy.get(1, 2));
        }

        @Test
        public void testFill_entireMatrixWithArray() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.copyFrom(new short[][] { { 9, 8 }, { 7, 6 } });
            assertEquals(9, m.get(0, 0));
            assertEquals(8, m.get(0, 1));
            assertEquals(7, m.get(1, 0));
            assertEquals(6, m.get(1, 1));
        }

        @Test
        public void testFill_withArrayAtOrigin() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.copyFrom(0, 0, new short[][] { { 99, 88 } });
            assertEquals(99, m.get(0, 0));
            assertEquals(88, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
            assertEquals(4, m.get(1, 0));
        }

        @Test
        public void testReverseH_singleColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 } });
            m.flipInPlaceHorizontally();
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 0));
        }

        @Test
        public void testReverseV_singleRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            m.flipInPlaceVertically();
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
        }

        @Test
        public void testRotate90_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
            assertEquals(6, rotated.get(2, 0));
            assertEquals(3, rotated.get(2, 1));
        }

        @Test
        public void testRotate180_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals(6, rotated.get(0, 0));
            assertEquals(5, rotated.get(0, 1));
            assertEquals(4, rotated.get(0, 2));
            assertEquals(3, rotated.get(1, 0));
            assertEquals(2, rotated.get(1, 1));
            assertEquals(1, rotated.get(1, 2));
        }

        @Test
        public void testRotate270_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix rotated = m.rotate270();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(6, rotated.get(0, 1));
            assertEquals(1, rotated.get(2, 0));
            assertEquals(4, rotated.get(2, 1));
        }

        @Test
        public void testReshape_toSingleRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix reshaped = m.reshape(1, 4);
            assertEquals(1, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
            assertArrayEquals(new short[] { 1, 2, 3, 4 }, reshaped.rowView(0));
        }

        @Test
        public void testReshape_toSingleColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix reshaped = m.reshape(4, 1);
            assertEquals(4, reshaped.rowCount());
            assertEquals(1, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(1, 0));
            assertEquals(3, reshaped.get(2, 0));
            assertEquals(4, reshaped.get(3, 0));
        }

        @Test
        public void testVstack_sameMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix stacked = m.stackVertically(m);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(1, stacked.get(1, 0));
        }

        @Test
        public void testHstack_sameMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 } });
            ShortMatrix stacked = m.stackHorizontally(m);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(1, stacked.get(0, 1));
        }

        @Test
        public void testAdd_singleElement() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
            ShortMatrix sum = m1.add(m2);
            assertEquals(1, sum.rowCount());
            assertEquals(1, sum.columnCount());
            assertEquals(8, sum.get(0, 0));
        }

        @Test
        public void testSubtract_singleElement() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
            ShortMatrix diff = m1.subtract(m2);
            assertEquals(1, diff.rowCount());
            assertEquals(1, diff.columnCount());
            assertEquals(2, diff.get(0, 0));
        }

        @Test
        public void testMultiply_singleElement() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
            ShortMatrix product = m1.multiply(m2);
            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(15, product.get(0, 0));
        }

        @Test
        public void testStreamH_emptyRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            short[] row = m.streamHorizontal(0, 0).toArray();
            assertEquals(0, row.length);
        }

        @Test
        public void testStreamV_emptyColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            short[] col = m.streamVertical(0, 0).toArray();
            assertEquals(0, col.length);
        }

        @Test
        public void testStreamR_emptyRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            long count = m.streamRows(1, 1).count();
            assertEquals(0, count);
        }

        @Test
        public void testStreamC_emptyRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            long count = m.streamColumns(1, 1).count();
            assertEquals(0, count);
        }

        @Test
        public void testMapToObj_withNull() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> result = m.mapToObj(x -> x == 2 ? null : String.valueOf(x), String.class);
            assertEquals("1", result.get(0, 0));
            assertEquals(null, result.get(0, 1));
            assertEquals("3", result.get(1, 0));
        }

        @Test
        public void testUpdateAll_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            empty.updateAll(x -> (short) (x + 1));
            assertTrue(empty.isEmpty());
        }

        @Test
        public void testReplaceIf_noMatches() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf(x -> x > 100, (short) 99);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_allMatch() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf(x -> x > 0, (short) 0);
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(0, m.get(1, 0));
            assertEquals(0, m.get(1, 1));
        }

        @Test
        public void testZipWith_singleElement() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
            ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
            assertEquals(1, result.rowCount());
            assertEquals(1, result.columnCount());
            assertEquals(5, result.get(0, 0));
        }

        @Test
        public void testZipWith_threeMatrices_singleElement() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 3 } });
            ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
            assertEquals(1, result.rowCount());
            assertEquals(1, result.columnCount());
            assertEquals(6, result.get(0, 0));
        }

        @Test
        public void testFlatOp_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            List<Integer> results = new ArrayList<>();
            empty.applyOnFlattened(row -> results.add(row.length));
            assertTrue(results.isEmpty());
        }

        @Test
        public void testDiagonalLU2RD_singleElement() {
            ShortMatrix m = ShortMatrix.mainDiagonal(new short[] { 5 });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5, m.get(0, 0));
        }

        @Test
        public void testDiagonalRU2LD_singleElement() {
            ShortMatrix m = ShortMatrix.antiDiagonal(new short[] { 5 });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5, m.get(0, 0));
        }

        @Test
        public void testBoxed_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            Matrix<Short> boxed = empty.boxed();
            assertEquals(0, boxed.rowCount());
            assertEquals(0, boxed.columnCount());
        }

        @Test
        public void testToIntMatrix_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            IntMatrix intMatrix = empty.toIntMatrix();
            assertEquals(0, intMatrix.rowCount());
            assertEquals(0, intMatrix.columnCount());
        }

        @Test
        public void testToLongMatrix_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            LongMatrix longMatrix = empty.toLongMatrix();
            assertEquals(0, longMatrix.rowCount());
            assertEquals(0, longMatrix.columnCount());
        }

        @Test
        public void testToFloatMatrix_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            FloatMatrix floatMatrix = empty.toFloatMatrix();
            assertEquals(0, floatMatrix.rowCount());
            assertEquals(0, floatMatrix.columnCount());
        }

        @Test
        public void testToDoubleMatrix_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            DoubleMatrix doubleMatrix = empty.toDoubleMatrix();
            assertEquals(0, doubleMatrix.rowCount());
            assertEquals(0, doubleMatrix.columnCount());
        }

        @Test
        public void testEquals_emptyMatrices() {
            ShortMatrix empty1 = ShortMatrix.empty();
            ShortMatrix empty2 = ShortMatrix.of(new short[0][0]);
            assertTrue(empty1.equals(empty2));
        }

        @Test
        public void testHashCode_emptyMatrices() {
            ShortMatrix empty1 = ShortMatrix.empty();
            ShortMatrix empty2 = ShortMatrix.of(new short[0][0]);
            assertEquals(empty1.hashCode(), empty2.hashCode());
        }

        @Test
        public void testToString_empty() {
            ShortMatrix empty = ShortMatrix.empty();
            String str = empty.toString();
            assertNotNull(str);
        }

        @Test
        public void testToString_singleElement() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 42 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("42"));
        }

        @Test
        public void testUpdateRow_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(-1, x -> x));
            assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(2, x -> x));
        }

        @Test
        public void testUpdateColumn_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(-1, x -> x));
            assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(2, x -> x));
        }

        @Test
        public void testTranspose_singleRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            ShortMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(1, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(3, transposed.get(2, 0));
        }

        @Test
        public void testTranspose_singleColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 }, { 3 } });
            ShortMatrix transposed = m.transpose();
            assertEquals(1, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(2, transposed.get(0, 1));
            assertEquals(3, transposed.get(0, 2));
        }

        @Test
        public void testUpOf_emptyMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort up = m.above(0, 1);
            assertFalse(up.isPresent());
        }

        @Test
        public void testDownOf_bottomRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort down = m.below(1, 1);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf_leftmostColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort left = m.left(1, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf_rightmostColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort right = m.right(1, 1);
            assertFalse(right.isPresent());
        }
    }

    @Nested
    @Tag("2510")
    class ShortMatrix2510Test extends TestBase {

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortList flat = m.flatten();
            assertEquals(6, flat.size());
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, flat.toArray());
        }

        @Test
        public void testFlatten_empty() {
            ShortMatrix m = ShortMatrix.empty();
            ShortList flat = m.flatten();
            assertEquals(0, flat.size());
        }

        // ============ FlatOp Tests ============

        @Test
        public void testFlatOp() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            final int[] count = { 0 };
            m.applyOnFlattened(row -> count[0] += row.length);
            assertEquals(4, count[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix stacked = m1.stackVertically(m2);
            assertEquals(4, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(4, stacked.get(1, 1));
            assertEquals(5, stacked.get(2, 0));
            assertEquals(8, stacked.get(3, 1));
        }

        @Test
        public void testHstack() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(2, stacked.get(0, 1));
            assertEquals(5, stacked.get(0, 2));
            assertEquals(6, stacked.get(0, 3));
        }

        @Test
        public void testHstack_differentRowCount() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix result = m1.add(m2);
            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void testAdd_differentDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m1.subtract(m2);
            assertEquals(4, result.get(0, 0));
            assertEquals(4, result.get(0, 1));
            assertEquals(4, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testSubtract_differentDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 2, 3 }, { 4, 5 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m1.multiply(m2);
            assertEquals(11, result.get(0, 0));
            assertEquals(16, result.get(0, 1));
            assertEquals(19, result.get(1, 0));
            assertEquals(28, result.get(1, 1));
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Short> boxed = m.boxed();
            assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
            assertEquals(Short.valueOf((short) 4), boxed.get(1, 1));
        }

        @Test
        public void testToIntMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix result = m.toIntMatrix();
            assertEquals(1, result.get(0, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testToLongMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix result = m.toLongMatrix();
            assertEquals(1L, result.get(0, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void testToFloatMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix result = m.toFloatMatrix();
            assertEquals(1.0f, result.get(0, 0), 0.001f);
            assertEquals(4.0f, result.get(1, 1), 0.001f);
        }

        @Test
        public void testToDoubleMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix result = m.toDoubleMatrix();
            assertEquals(1.0, result.get(0, 0), 0.001);
            assertEquals(4.0, result.get(1, 1), 0.001);
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_twoMatrices() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void testZipWith_twoMatrices_differentDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (short) (a + b)));
        }

        @Test
        public void testZipWith_threeMatrices() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 9, 10 }, { 11, 12 } });
            ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
            assertEquals(15, result.get(0, 0)); // 1 + 5 + 9
            assertEquals(18, result.get(0, 1)); // 2 + 6 + 10
            assertEquals(21, result.get(1, 0)); // 3 + 7 + 11
            assertEquals(24, result.get(1, 1)); // 4 + 8 + 12
        }

        @Test
        public void testZipWith_threeMatrices_differentDimensions() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c)));
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.streamMainDiagonal().toArray());
        }

        @Test
        public void testStreamRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            short[] diagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new short[] { 3, 5, 7 }, diagonal);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.streamAntiDiagonal().toArray());
        }

        @Test
        public void testStreamH_withRowIndex() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            short[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new short[] { 4, 5, 6 }, row1);
        }

        @Test
        public void testStreamH_withRowRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            short[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new short[] { 4, 5, 6, 7, 8, 9 }, rows);
        }

        @Test
        public void testStreamH_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 2));
        }

        @Test
        public void testStreamV_withColumnIndex() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            short[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new short[] { 2, 5 }, col1);
        }

        @Test
        public void testStreamV_withColumnRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            short[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new short[] { 2, 5, 3, 6 }, columnCount);
        }

        @Test
        public void testStreamV_outOfBounds() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
        }

        // ============ Stream of Streams Tests ============

        @Test
        public void testStreamR() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<short[]> rows = m.streamRows().map(ShortStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new short[] { 1, 2 }, rows.get(0));
            assertArrayEquals(new short[] { 3, 4 }, rows.get(1));
        }

        @Test
        public void testStreamC() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<short[]> columnCount = m.streamColumns().map(ShortStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new short[] { 1, 3 }, columnCount.get(0));
            assertArrayEquals(new short[] { 2, 4 }, columnCount.get(1));
        }

        // ============ Points Stream Tests ============

        @Test
        public void testPointsH() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        @Test
        public void testPointsV() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
            assertEquals(Point.of(0, 1), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        // ============ Sum Tests ============
        // Note: sumByInt(), sumByLong(), sumByDouble() methods don't exist in ShortMatrix

        // ============ Average Tests ============
        // Note: averageByInt(), averageByLong(), averageByDouble() methods don't exist in ShortMatrix

        // ============ Empty Matrix Tests ============

        @Test
        public void testElementCount() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testElementCount_Empty() {
            ShortMatrix m = ShortMatrix.empty();
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testIsEmpty_true() {
            ShortMatrix empty = ShortMatrix.empty();
            assertTrue(empty.isEmpty());
        }

        @Test
        public void testIsEmpty_false() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
            assertFalse(m.isEmpty());
        }

        // ============ Equals and HashCode Tests ============

        @Test
        public void testEquals() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 5 } });

            assertEquals(m1, m2);
            assertNotEquals(m1, m3);
        }

        @Test
        public void testHashCode_equal() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

            assertEquals(m1.hashCode(), m2.hashCode());
        }

        // ============ ToString Tests ============

        @Test
        public void testToString() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.length() > 0);
        }
    }

    @Nested
    @Tag("2511")
    class ShortMatrix2511Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals((short) 1, m.get(0, 0));
            assertEquals((short) 4, m.get(1, 1));
        }

        @Test
        public void testConstructor_withSingleElement() {
            ShortMatrix m = new ShortMatrix(new short[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals((short) 42, m.get(0, 0));
        }

        @Test
        public void testConstructor_withLargeMatrix() {
            short[][] arr = new short[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    arr[i][j] = (short) (i * 10 + j);
                }
            }
            ShortMatrix m = new ShortMatrix(arr);
            assertEquals(10, m.rowCount());
            assertEquals(10, m.columnCount());
            assertEquals((short) 0, m.get(0, 0));
            assertEquals((short) 99, m.get(9, 9));
            assertEquals((short) 55, m.get(5, 5));
        }

        @Test
        public void testOf_withValidArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = ShortMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals((short) 1, m.get(0, 0));
        }

        @Test
        public void testOf_withSingleRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3, 4, 5 } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void testOf_withSingleColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 }, { 3 }, { 4 } });
            assertEquals(4, m.rowCount());
            assertEquals(1, m.columnCount());
        }

        // ============ Create Method Tests ============

        @Test
        public void testRandom() {
            ShortMatrix m = ShortMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertNotNull(m.rowView(0));
        }

        @Test
        public void testRepeat() {
            ShortMatrix m = ShortMatrix.repeat(1, 5, (short) 42);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals((short) 42, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_zeroLength() {
            ShortMatrix m = ShortMatrix.repeat(1, 0, (short) 42);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonalLU2RD() {
            ShortMatrix m = ShortMatrix.mainDiagonal(new short[] { 1, 2, 3 });
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
            ShortMatrix m = ShortMatrix.antiDiagonal(new short[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals((short) 1, m.get(0, 2));
            assertEquals((short) 2, m.get(1, 1));
            assertEquals((short) 3, m.get(2, 0));
            assertEquals((short) 0, m.get(0, 0));
        }

        @Test
        public void testDiagonal_bothDiagonals() {
            ShortMatrix m = ShortMatrix.diagonals(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals((short) 1, m.get(0, 0));
            assertEquals((short) 2, m.get(1, 1));
            assertEquals((short) 3, m.get(2, 2));
            assertEquals((short) 4, m.get(0, 2));
            assertEquals((short) 2, m.get(1, 1));
            assertEquals((short) 6, m.get(2, 0));
        }

        @Test
        public void testDiagonal_differentLengths() {
            assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonals(new short[] { 1, 2 }, new short[] { 1, 2, 3 }));
        }

        @Test
        public void testUnbox() {
            Matrix<Short> boxed = Matrix.of(new Short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m = ShortMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals((short) 1, m.get(0, 0));
            assertEquals((short) 4, m.get(1, 1));
        }

        @Test
        public void testGet() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertEquals((short) 1, m.get(0, 0));
            assertEquals((short) 2, m.get(0, 1));
            assertEquals((short) 3, m.get(1, 0));
            assertEquals((short) 4, m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Point p = Point.of(1, 0);
            assertEquals((short) 3, m.get(p));
        }

        @Test
        public void testSet() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.set(0, 0, (short) 10);
            assertEquals((short) 10, m.get(0, 0));
            m.set(1, 1, (short) 20);
            assertEquals((short) 20, m.get(1, 1));
        }

        @Test
        public void testSet_withPoint() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Point p = Point.of(1, 0);
            m.set(p, (short) 99);
            assertEquals((short) 99, m.get(1, 0));
        }

        // ============ Directional Methods (up, down, left, right) ============

        @Test
        public void testUpOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals((short) 1, up.get());

            OptionalShort noUp = m.above(0, 0);
            assertFalse(noUp.isPresent());
        }

        @Test
        public void testDownOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals((short) 3, down.get());

            OptionalShort noDown = m.below(1, 0);
            assertFalse(noDown.isPresent());
        }

        @Test
        public void testLeftOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals((short) 1, left.get());

            OptionalShort noLeft = m.left(0, 0);
            assertFalse(noLeft.isPresent());
        }

        @Test
        public void testRightOf() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            OptionalShort right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals((short) 2, right.get());

            OptionalShort noRight = m.right(0, 1);
            assertFalse(noRight.isPresent());
        }

        @Test
        public void testRow_invalidIndex() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn_invalidIndex() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.setRow(0, new short[] { 10, 20 });
            assertArrayEquals(new short[] { 10, 20 }, m.rowView(0));
            assertEquals((short) 3, m.get(1, 0));
        }

        @Test
        public void testSetColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.setColumn(0, new short[] { 10, 30 });
            assertEquals((short) 10, m.get(0, 0));
            assertEquals((short) 30, m.get(1, 0));
            assertEquals((short) 2, m.get(0, 1));
        }

        @Test
        public void testUpdateColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.updateColumn(0, x -> (short) (x * 10));
            assertEquals((short) 10, m.get(0, 0));
            assertEquals((short) 30, m.get(1, 0));
            assertEquals((short) 2, m.get(0, 1));
            assertEquals((short) 4, m.get(1, 1));
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setMainDiagonal(new short[] { 10, 20, 30 });
            assertEquals((short) 10, m.get(0, 0));
            assertEquals((short) 20, m.get(1, 1));
            assertEquals((short) 30, m.get(2, 2));
        }

        @Test
        public void testUpdateLU2RD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateMainDiagonal(x -> (short) (x * 10));
            assertEquals((short) 10, m.get(0, 0));
            assertEquals((short) 50, m.get(1, 1));
            assertEquals((short) 90, m.get(2, 2));
        }

        @Test
        public void testSetRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setAntiDiagonal(new short[] { 30, 50, 70 });
            assertEquals((short) 30, m.get(0, 2));
            assertEquals((short) 50, m.get(1, 1));
            assertEquals((short) 70, m.get(2, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateAntiDiagonal(x -> (short) (x + 100));
            assertEquals((short) 103, m.get(0, 2));
            assertEquals((short) 105, m.get(1, 1));
            assertEquals((short) 107, m.get(2, 0));
        }

        // ============ Update Methods ============

        @Test
        public void testUpdateAll_unary() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll(x -> (short) (x * 2));
            assertEquals((short) 2, m.get(0, 0));
            assertEquals((short) 4, m.get(0, 1));
            assertEquals((short) 6, m.get(1, 0));
            assertEquals((short) 8, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll((i, j) -> (short) (i * 10 + j));
            assertEquals((short) 0, m.get(0, 0));
            assertEquals((short) 1, m.get(0, 1));
            assertEquals((short) 10, m.get(1, 0));
            assertEquals((short) 11, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.replaceIf(x -> x % 2 == 0, (short) 99);
            assertEquals((short) 1, m.get(0, 0));
            assertEquals((short) 99, m.get(0, 1));
            assertEquals((short) 3, m.get(0, 2));
            assertEquals((short) 99, m.get(1, 0));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf((i, j) -> i == j, (short) 0);
            assertEquals((short) 0, m.get(0, 0));
            assertEquals((short) 2, m.get(0, 1));
            assertEquals((short) 3, m.get(1, 0));
            assertEquals((short) 0, m.get(1, 1));
        }

        // ============ Map Methods ============

        @Test
        public void testMap() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m.map(x -> (short) (x * 10));
            assertEquals((short) 10, result.get(0, 0));
            assertEquals((short) 20, result.get(0, 1));
            assertEquals((short) 30, result.get(1, 0));
            assertEquals((short) 40, result.get(1, 1));
            // Original should be unchanged
            assertEquals((short) 1, m.get(0, 0));
        }

        // ShortMatrix doesn't have mapToInt method - removed test
        // @Test
        // public void testMapToInt() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     IntMatrix result = m.mapToInt(x -> x * 1000);
        //     assertEquals(1000, result.get(0, 0));
        //     assertEquals(4000, result.get(1, 1));
        // }

        // ShortMatrix doesn't have mapToLong method - removed test
        // @Test
        // public void testMapToLong() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     LongMatrix result = m.mapToLong(x -> x * 1000L);
        //     assertEquals(1000L, result.get(0, 0));
        //     assertEquals(4000L, result.get(1, 1));
        // }

        // ShortMatrix doesn't have mapToDouble method - removed test
        // @Test
        // public void testMapToDouble() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     DoubleMatrix result = m.mapToDouble(x -> x * 0.5);
        //     assertEquals(0.5, result.get(0, 0), 0.0001);
        //     assertEquals(2.0, result.get(1, 1), 0.0001);
        // }

        @Test
        public void testMapToObj() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> result = m.mapToObj(x -> "Value:" + x, String.class);
            assertEquals("Value:1", result.get(0, 0));
            assertEquals("Value:4", result.get(1, 1));
        }

        // ============ Fill Methods ============

        @Test
        public void testFill_value() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.fill((short) 99);
            assertEquals((short) 99, m.get(0, 0));
            assertEquals((short) 99, m.get(0, 1));
            assertEquals((short) 99, m.get(1, 0));
            assertEquals((short) 99, m.get(1, 1));
        }

        @Test
        public void testFill_array() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            m.copyFrom(new short[][] { { 10, 20 }, { 30, 40 } });
            assertEquals((short) 10, m.get(0, 0));
            assertEquals((short) 20, m.get(0, 1));
            assertEquals((short) 30, m.get(1, 0));
            assertEquals((short) 40, m.get(1, 1));
        }

        @Test
        public void testFill_arrayWithOffset() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.copyFrom(1, 1, new short[][] { { 99, 88 } });
            assertEquals((short) 1, m.get(0, 0));
            assertEquals((short) 99, m.get(1, 1));
            assertEquals((short) 88, m.get(1, 2));
        }

        // ============ Copy Methods ============

        @Test
        public void testCopy() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix copy = m.copy();
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals((short) 1, copy.get(0, 0));

            // Verify it's a deep copy
            copy.set(0, 0, (short) 99);
            assertEquals((short) 1, m.get(0, 0));
        }

        @Test
        public void testCopy_rowRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            ShortMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals((short) 3, copy.get(0, 0));
            assertEquals((short) 6, copy.get(1, 1));
        }

        @Test
        public void testCopy_fullRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ShortMatrix copy = m.copy(1, 2, 1, 3);
            assertEquals(1, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals((short) 5, copy.get(0, 0));
            assertEquals((short) 6, copy.get(0, 1));
        }

        // ============ Extend Methods ============

        @Test
        public void testExtend_newSize() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals((short) 1, extended.get(0, 0));
            assertEquals((short) 0, extended.get(2, 2));
        }

        @Test
        public void testExtend_withDefaultValue() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix extended = m.resize(2, 3, (short) 99);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals((short) 1, extended.get(0, 0));
            assertEquals((short) 99, extended.get(0, 2));
            assertEquals((short) 99, extended.get(1, 0));
        }

        @Test
        public void testExtend_directions() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 5 } });
            ShortMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals((short) 5, extended.get(1, 1));
            assertEquals((short) 0, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionsWithDefault() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 5 } });
            ShortMatrix extended = m.extend(1, 1, 1, 1, (short) 9);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals((short) 5, extended.get(1, 1));
            assertEquals((short) 9, extended.get(0, 0));
            assertEquals((short) 9, extended.get(2, 2));
        }

        // ============ Reverse and Flip Methods ============

        @Test
        public void testReverseH() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.flipInPlaceHorizontally();
            assertEquals((short) 3, m.get(0, 0));
            assertEquals((short) 2, m.get(0, 1));
            assertEquals((short) 1, m.get(0, 2));
            assertEquals((short) 6, m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            m.flipInPlaceVertically();
            assertEquals((short) 5, m.get(0, 0));
            assertEquals((short) 6, m.get(0, 1));
            assertEquals((short) 1, m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix flipped = m.flipHorizontally();
            assertEquals((short) 3, flipped.get(0, 0));
            assertEquals((short) 1, flipped.get(0, 2));
            // Original unchanged
            assertEquals((short) 1, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix flipped = m.flipVertically();
            assertEquals((short) 3, flipped.get(0, 0));
            assertEquals((short) 1, flipped.get(1, 0));
            // Original unchanged
            assertEquals((short) 1, m.get(0, 0));
        }

        // ============ Rotation Methods ============

        @Test
        public void testRotate90() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals((short) 4, rotated.get(0, 0));
            assertEquals((short) 1, rotated.get(0, 1));
            assertEquals((short) 6, rotated.get(2, 0));
        }

        @Test
        public void testRotate270() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix rotated = m.rotate270();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals((short) 3, rotated.get(0, 0));
            assertEquals((short) 6, rotated.get(0, 1));
        }

        // ============ Transpose and Reshape Methods ============

        @Test
        public void testTranspose() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals((short) 1, transposed.get(0, 0));
            assertEquals((short) 4, transposed.get(0, 1));
            assertEquals((short) 6, transposed.get(2, 1));
        }

        @Test
        public void testReshape() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ShortMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals((short) 1, reshaped.get(0, 0));
            assertEquals((short) 2, reshaped.get(0, 1));
            assertEquals((short) 6, reshaped.get(2, 1));
        }

        @Test
        public void testRepelem() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals((short) 1, result.get(0, 0));
            assertEquals((short) 1, result.get(0, 1));
            assertEquals((short) 1, result.get(1, 0));
            assertEquals((short) 4, result.get(3, 3));
        }

        @Test
        public void testRepmat() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix result = m.repeatMatrix(2, 2);
            assertEquals(2, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals((short) 1, result.get(0, 0));
            assertEquals((short) 2, result.get(0, 1));
            assertEquals((short) 1, result.get(0, 2));
            assertEquals((short) 1, result.get(1, 0));
        }

        // ============ Flatten and FlatOp Methods ============

        @Test
        public void testFlatten() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortList flattened = m.flatten();
            assertEquals(4, flattened.size());
            assertEquals((short) 1, flattened.get(0));
            assertEquals((short) 2, flattened.get(1));
            assertEquals((short) 3, flattened.get(2));
            assertEquals((short) 4, flattened.get(3));
        }

        @Test
        public void testFlatOp() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            final int[] sum = { 0 };
            m.applyOnFlattened(row -> {
                for (short val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10, sum[0]);
        }

        // ============ Stack Methods ============

        @Test
        public void testVstack() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 } });
            ShortMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals((short) 1, stacked.get(0, 0));
            assertEquals((short) 3, stacked.get(1, 0));
        }

        @Test
        public void testHstack() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 }, { 3 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 }, { 4 } });
            ShortMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals((short) 1, stacked.get(0, 0));
            assertEquals((short) 2, stacked.get(0, 1));
        }

        // ============ Arithmetic Methods ============

        @Test
        public void testAdd() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 10, 20 }, { 30, 40 } });
            ShortMatrix result = m1.add(m2);
            assertEquals((short) 11, result.get(0, 0));
            assertEquals((short) 22, result.get(0, 1));
            assertEquals((short) 33, result.get(1, 0));
            assertEquals((short) 44, result.get(1, 1));
        }

        @Test
        public void testSubtract() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 10, 20 }, { 30, 40 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m1.subtract(m2);
            assertEquals((short) 9, result.get(0, 0));
            assertEquals((short) 18, result.get(0, 1));
            assertEquals((short) 27, result.get(1, 0));
            assertEquals((short) 36, result.get(1, 1));
        }

        @Test
        public void testMultiply() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 10, 20 }, { 30, 40 } });
            ShortMatrix result = m1.multiply(m2);
            assertEquals((short) 70, result.get(0, 0));
            assertEquals((short) 100, result.get(0, 1));
            assertEquals((short) 150, result.get(1, 0));
            assertEquals((short) 220, result.get(1, 1));
        }

        // ============ Conversion Methods ============

        @Test
        public void testBoxed() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Short> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
            assertEquals(Short.valueOf((short) 4), boxed.get(1, 1));
        }

        // ShortMatrix doesn't have mapToInt method - removed test
        // @Test
        // public void testToIntMatrix() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     IntMatrix result = m.mapToInt(s -> (int) s);
        //     assertEquals(2, result.rowCount());
        //     assertEquals(2, result.columnCount());
        //     assertEquals(1, result.get(0, 0));
        //     assertEquals(4, result.get(1, 1));
        // }

        // ShortMatrix doesn't have mapToLong method - removed test
        // @Test
        // public void testToLongMatrix() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     LongMatrix result = m.mapToLong(s -> (long) s);
        //     assertEquals(2, result.rowCount());
        //     assertEquals(2, result.columnCount());
        //     assertEquals(1L, result.get(0, 0));
        //     assertEquals(4L, result.get(1, 1));
        // }

        // ShortMatrix doesn't have mapToFloat method - removed test
        // @Test
        // public void testToFloatMatrix() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     FloatMatrix result = m.mapToFloat(s -> (float) s);
        //     assertEquals(2, result.rowCount());
        //     assertEquals(2, result.columnCount());
        //     assertEquals(1.0f, result.get(0, 0), 0.0001f);
        //     assertEquals(4.0f, result.get(1, 1), 0.0001f);
        // }

        // ShortMatrix doesn't have mapToDouble method - removed test
        // @Test
        // public void testToDoubleMatrix() {
        //     ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        //     DoubleMatrix result = m.mapToDouble(s -> (double) s);
        //     assertEquals(2, result.rowCount());
        //     assertEquals(2, result.columnCount());
        //     assertEquals(1.0, result.get(0, 0), 0.0001);
        //     assertEquals(4.0, result.get(1, 1), 0.0001);
        // }

        // ============ ZipWith Methods ============

        @Test
        public void testZipWith_twoMatrices() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 10, 20 }, { 30, 40 } });
            ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
            assertEquals((short) 11, result.get(0, 0));
            assertEquals((short) 22, result.get(0, 1));
            assertEquals((short) 33, result.get(1, 0));
            assertEquals((short) 44, result.get(1, 1));
        }

        @Test
        public void testZipWith_threeMatrices() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 10, 20 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 100, 200 } });
            ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
            assertEquals((short) 111, result.get(0, 0));
            assertEquals((short) 222, result.get(0, 1));
        }

        // ============ Stream Methods ============

        @Test
        public void testStreamH_rowRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            short[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new short[] { 3, 4, 5, 6 }, rows);
        }

        @Test
        public void testStreamV_columnRange() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            short[] columnCount = m.streamVertical(0, 2).toArray();
            assertArrayEquals(new short[] { 1, 4, 2, 5 }, columnCount);
        }

        // ============ Stream of Streams Methods ============

        // ============ Inherited Methods from AbstractMatrix ============

        @Test
        public void testIsEmpty() {
            assertTrue(ShortMatrix.empty().isEmpty());
            assertFalse(ShortMatrix.of(new short[][] { { 1 } }).isEmpty());
        }

        @Test
        public void testEquals() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 5 } });

            assertEquals(m1, m2);
            assertNotEquals(m1, m3);
            assertNotEquals(m1, null);
            assertNotEquals(m1, "not a matrix");
        }

        @Test
        public void testToString() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }

        @Test
        public void testToArray() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            short[][] arr = m.backingArray();
            assertEquals(2, arr.length);
            assertEquals(2, arr[0].length);
            assertArrayEquals(new short[] { 1, 2 }, arr[0]);
            assertArrayEquals(new short[] { 3, 4 }, arr[1]);
        }

        // ============ Edge Cases ============

        @Test
        public void testLargeDimensions() {
            short[][] data = new short[100][50];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 50; j++) {
                    data[i][j] = (short) (i + j);
                }
            }
            ShortMatrix m = ShortMatrix.of(data);
            assertEquals(100, m.rowCount());
            assertEquals(50, m.columnCount());
            assertEquals((short) 0, m.get(0, 0));
            assertEquals((short) 148, m.get(99, 49));
        }

        @Test
        public void testSingleRowOperations() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3, 4, 5 } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());

            ShortMatrix transposed = m.transpose();
            assertEquals(5, transposed.rowCount());
            assertEquals(1, transposed.columnCount());
        }

        @Test
        public void testSingleColumnOperations() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 }, { 3 } });
            assertEquals(3, m.rowCount());
            assertEquals(1, m.columnCount());

            ShortMatrix transposed = m.transpose();
            assertEquals(1, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
        }

        @Test
        public void testChainedOperations() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m.map(x -> (short) (x * 2)).transpose().map(x -> (short) (x + 1));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals((short) 3, result.get(0, 0)); // (1*2)+1
            assertEquals((short) 7, result.get(0, 1)); // (3*2)+1
            assertEquals((short) 5, result.get(1, 0)); // (2*2)+1
            assertEquals((short) 9, result.get(1, 1)); // (4*2)+1
        }
    }

    @Nested
    @Tag("2512")
    class ShortMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_withValidArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void test_constructor_withNullArray() {
            ShortMatrix m = new ShortMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withEmptyArray() {
            ShortMatrix m = new ShortMatrix(new short[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void test_constructor_withSingleElement() {
            ShortMatrix m = new ShortMatrix(new short[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42, m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void test_empty_returnsEmptyMatrix() {
            ShortMatrix empty = ShortMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());
            assertSame(ShortMatrix.empty(), ShortMatrix.empty());
        }

        @Test
        public void test_of_withValidArray() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = ShortMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(6, m.get(1, 2));
        }

        @Test
        public void test_of_withNullArray() {
            ShortMatrix m = ShortMatrix.of((short[][]) null);
            assertSame(ShortMatrix.empty(), m);
        }

        @Test
        public void test_of_withEmptyArray() {
            ShortMatrix m = ShortMatrix.of(new short[0][0]);
            assertSame(ShortMatrix.empty(), m);
        }

        @Test
        public void test_random_createsRandomMatrix() {
            ShortMatrix m = ShortMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_repeat_createsRepeatedValues() {
            ShortMatrix m = ShortMatrix.repeat(1, 4, (short) 7);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            for (int i = 0; i < 4; i++) {
                assertEquals(7, m.get(0, i));
            }
        }

        @Test
        public void test_range_withTwoParameters() {
            ShortMatrix m = ShortMatrix.range((short) 1, (short) 5);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(0, 3));
        }

        @Test
        public void test_range_withThreeParameters() {
            ShortMatrix m = ShortMatrix.range((short) 0, (short) 10, (short) 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(8, m.get(0, 4));
        }

        @Test
        public void test_rangeClosed_withTwoParameters() {
            ShortMatrix m = ShortMatrix.rangeClosed((short) 1, (short) 4);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(0, 3));
        }

        @Test
        public void test_rangeClosed_withThreeParameters() {
            ShortMatrix m = ShortMatrix.rangeClosed((short) 1, (short) 9, (short) 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(9, m.get(0, 4));
        }

        @Test
        public void test_mainDiagonal_createsMainDiagonal() {
            short[] diag = { 1, 2, 3 };
            ShortMatrix m = ShortMatrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(0, m.get(0, 1));
        }

        @Test
        public void test_antiDiagonal_createsAntiDiagonal() {
            short[] diag = { 1, 2, 3 };
            ShortMatrix m = ShortMatrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
        }

        @Test
        public void test_diagonal_withBothDiagonals() {
            short[] lu = { 1, 2, 3 };
            short[] ru = { 4, 5, 6 };
            ShortMatrix m = ShortMatrix.diagonals(lu, ru);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(4, m.get(0, 2));
            assertEquals(6, m.get(2, 0));
        }

        @Test
        public void test_diagonal_withDifferentLengths_throwsException() {
            short[] lu = { 1, 2 };
            short[] ru = { 3, 4, 5 };
            assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonals(lu, ru));
        }

        @Test
        public void test_diagonal_withBothNull_returnsEmpty() {
            ShortMatrix m = ShortMatrix.diagonals(null, null);
            assertSame(ShortMatrix.empty(), m);
        }

        @Test
        public void test_unbox_convertsBoxedMatrix() {
            Short[][] boxed = { { 1, 2 }, { 3, 4 } };
            Matrix<Short> boxedMatrix = Matrix.of(boxed);
            ShortMatrix m = ShortMatrix.unbox(boxedMatrix);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(1, 1));
        }

        // ============ Component Type Test ============

        @Test
        public void test_componentType_returnsShortClass() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
            assertEquals(short.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void test_get_withIndices() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            assertEquals(1, m.get(0, 0));
            assertEquals(5, m.get(1, 1));
            assertEquals(6, m.get(1, 2));
        }

        @Test
        public void test_get_withPoint() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            Point p = Point.of(1, 0);
            assertEquals(3, m.get(p));
        }

        @Test
        public void test_set_withIndices() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.set(0, 1, (short) 99);
            assertEquals(99, m.get(0, 1));
        }

        @Test
        public void test_set_withPoint() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            Point p = Point.of(1, 1);
            m.set(p, (short) 88);
            assertEquals(88, m.get(p));
        }

        // ============ Directional Navigation Tests ============

        @Test
        public void test_upOf_withElementAbove() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.above(1, 0);
            assertTrue(result.isPresent());
            assertEquals(1, result.getAsShort());
        }

        @Test
        public void test_upOf_atTopRow_returnsEmpty() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.above(0, 0);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_downOf_withElementBelow() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.below(0, 1);
            assertTrue(result.isPresent());
            assertEquals(4, result.getAsShort());
        }

        @Test
        public void test_downOf_atBottomRow_returnsEmpty() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.below(1, 1);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_leftOf_withElementToLeft() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.left(0, 1);
            assertTrue(result.isPresent());
            assertEquals(1, result.getAsShort());
        }

        @Test
        public void test_leftOf_atLeftColumn_returnsEmpty() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.left(1, 0);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_rightOf_withElementToRight() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.right(1, 0);
            assertTrue(result.isPresent());
            assertEquals(4, result.getAsShort());
        }

        @Test
        public void test_rightOf_atRightColumn_returnsEmpty() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            OptionalShort result = m.right(0, 1);
            assertFalse(result.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void test_row_returnsCorrectRow() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] row = m.rowView(1);
            assertArrayEquals(new short[] { 4, 5, 6 }, row);
        }

        @Test
        public void test_row_outOfBounds_throwsException() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column_returnsCorrectColumn() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] col = m.columnCopy(1);
            assertArrayEquals(new short[] { 2, 5 }, col);
        }

        @Test
        public void test_column_outOfBounds_throwsException() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow_updatesRow() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.setRow(0, new short[] { 9, 8 });
            assertEquals(9, m.get(0, 0));
            assertEquals(8, m.get(0, 1));
        }

        @Test
        public void test_setRow_wrongLength_throwsException() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2, 3 }));
        }

        @Test
        public void test_setColumn_updatesColumn() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.setColumn(1, new short[] { 7, 8 });
            assertEquals(7, m.get(0, 1));
            assertEquals(8, m.get(1, 1));
        }

        @Test
        public void test_setColumn_wrongLength_throwsException() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2, 3 }));
        }

        @Test
        public void test_updateRow_appliesFunction() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.updateRow(0, x -> (short) (x * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
        }

        @Test
        public void test_updateColumn_appliesFunction() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.updateColumn(0, x -> (short) (x + 10));
            assertEquals(11, m.get(0, 0));
            assertEquals(13, m.get(1, 0));
            assertEquals(2, m.get(0, 1));
        }

        // ============ Diagonal Tests ============

        @Test
        public void test_getMainDiagonal_returnsMainDiagonal() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] diag = m.getMainDiagonal();
            assertArrayEquals(new short[] { 1, 5, 9 }, diag);
        }

        @Test
        public void test_getMainDiagonal_nonSquare_throwsException() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal_setsMainDiagonal() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.setMainDiagonal(new short[] { 11, 22, 33 });
            assertEquals(11, m.get(0, 0));
            assertEquals(22, m.get(1, 1));
            assertEquals(33, m.get(2, 2));
        }

        @Test
        public void test_setMainDiagonal_wrongLength_throwsException() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new short[] { 1, 2, 3 }));
        }

        @Test
        public void test_updateMainDiagonal_appliesFunction() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.updateMainDiagonal(x -> (short) (x * 10));
            assertEquals(10, m.get(0, 0));
            assertEquals(50, m.get(1, 1));
            assertEquals(90, m.get(2, 2));
        }

        @Test
        public void test_getAntiDiagonal_returnsAntiDiagonal() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] diag = m.getAntiDiagonal();
            assertArrayEquals(new short[] { 3, 5, 7 }, diag);
        }

        @Test
        public void test_setAntiDiagonal_setsAntiDiagonal() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.setAntiDiagonal(new short[] { 11, 22, 33 });
            assertEquals(11, m.get(0, 2));
            assertEquals(22, m.get(1, 1));
            assertEquals(33, m.get(2, 0));
        }

        @Test
        public void test_updateAntiDiagonal_appliesFunction() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.updateAntiDiagonal(x -> (short) (x + 100));
            assertEquals(103, m.get(0, 2));
            assertEquals(105, m.get(1, 1));
            assertEquals(107, m.get(2, 0));
        }

        // ============ Update/Replace Tests ============

        @Test
        public void test_updateAll_withUnaryOperator() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.updateAll(x -> (short) (x * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(6, m.get(1, 0));
            assertEquals(8, m.get(1, 1));
        }

        @Test
        public void test_updateAll_withBiFunction() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.updateAll((i, j) -> (short) (i * 10 + j));
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(10, m.get(1, 0));
            assertEquals(11, m.get(1, 1));
        }

        @Test
        public void test_replaceIf_withPredicate() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.replaceIf(x -> x > 3, (short) 99);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(99, m.get(1, 0));
            assertEquals(99, m.get(1, 2));
        }

        @Test
        public void test_replaceIf_withBiPredicate() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.replaceIf((i, j) -> i == j, (short) 0);
            assertEquals(0, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(0, m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void test_map_createsNewMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix result = m.map(x -> (short) (x * 3));
            assertEquals(3, result.get(0, 0));
            assertEquals(6, result.get(0, 1));
            assertEquals(9, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
            assertEquals(1, m.get(0, 0)); // original unchanged
        }

        @Test
        public void test_mapToObj_createsObjectMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            Matrix<String> result = m.mapToObj(x -> "val" + x, String.class);
            assertEquals("val1", result.get(0, 0));
            assertEquals("val4", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_withValue() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.fill((short) 7);
            assertEquals(7, m.get(0, 0));
            assertEquals(7, m.get(0, 1));
            assertEquals(7, m.get(1, 0));
            assertEquals(7, m.get(1, 1));
        }

        @Test
        public void test_fill_withArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[][] b = { { 9, 8 }, { 7, 6 } };
            m.copyFrom(b);
            assertEquals(9, m.get(0, 0));
            assertEquals(6, m.get(1, 1));
        }

        @Test
        public void test_fill_withArrayAndOffset() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[][] b = { { 99, 98 } };
            m.copyFrom(1, 1, b);
            assertEquals(1, m.get(0, 0));
            assertEquals(99, m.get(1, 1));
            assertEquals(98, m.get(1, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy_createsIndependentCopy() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1, copy.get(0, 0));

            copy.set(0, 0, (short) 99);
            assertEquals(1, m.get(0, 0)); // original unchanged
        }

        @Test
        public void test_copy_withRowRange() {
            short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3, copy.get(0, 0));
            assertEquals(6, copy.get(1, 1));
        }

        @Test
        public void test_copy_withFullRange() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix copy = m.copy(0, 2, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(2, copy.get(0, 0));
            assertEquals(6, copy.get(1, 1));
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend_increasesSize() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(0, extended.get(2, 2));
        }

        @Test
        public void test_extend_withDefaultValue() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix extended = m.resize(3, 3, (short) 99);
            assertEquals(99, extended.get(2, 2));
        }

        @Test
        public void test_extend_withDirections() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1, extended.get(1, 1));
        }

        @Test
        public void test_extend_withDirectionsAndDefault() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix extended = m.extend(1, 1, 1, 1, (short) 88);
            assertEquals(88, extended.get(0, 0));
            assertEquals(1, extended.get(1, 1));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void test_reverseH_reversesHorizontally() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.flipInPlaceHorizontally();
            assertEquals(3, m.get(0, 0));
            assertEquals(1, m.get(0, 2));
            assertEquals(6, m.get(1, 0));
        }

        @Test
        public void test_reverseV_reversesVertically() {
            short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.flipInPlaceVertically();
            assertEquals(5, m.get(0, 0));
            assertEquals(6, m.get(0, 1));
            assertEquals(1, m.get(2, 0));
        }

        @Test
        public void test_flipH_createsNewHorizontallyFlipped() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix flipped = m.flipHorizontally();
            assertEquals(3, flipped.get(0, 0));
            assertEquals(1, m.get(0, 0)); // original unchanged
        }

        @Test
        public void test_flipV_createsNewVerticallyFlipped() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix flipped = m.flipVertically();
            assertEquals(3, flipped.get(0, 0));
            assertEquals(1, m.get(0, 0)); // original unchanged
        }

        // ============ Rotation Tests ============

        @Test
        public void test_rotate90_rotatesClockwise() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.printAndReturn();
            ShortMatrix rotated = m.rotate90();
            rotated.printAndReturn();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
            assertEquals(4, rotated.get(1, 0));
            assertEquals(2, rotated.get(1, 1));
        }

        @Test
        public void test_rotate180_rotates180Degrees() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix rotated = m.rotate180();
            assertEquals(4, rotated.get(0, 0));
            assertEquals(3, rotated.get(0, 1));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(1, rotated.get(1, 1));
        }

        @Test
        public void test_rotate270_rotatesCounterClockwise() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            m.printAndReturn();
            ShortMatrix rotated = m.rotate270();
            rotated.printAndReturn();
            assertEquals(2, rotated.get(0, 0));
            assertEquals(4, rotated.get(0, 1));
            assertEquals(1, rotated.get(1, 0));
            assertEquals(3, rotated.get(1, 1));
        }

        // ============ Transpose Test ============

        @Test
        public void test_transpose_swapsRowsAndColumns() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(4, transposed.get(0, 1));
            assertEquals(6, transposed.get(2, 1));
        }

        // ============ Reshape Test ============

        @Test
        public void test_reshape_changesShape() {
            short[][] arr = { { 1, 2, 3, 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(6, reshaped.get(1, 2));
        }

        @Test
        public void test_reshape_incompatibleSize_fillsWithZeros() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            ShortMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(0, reshaped.get(1, 1)); // filled with zero
        }

        // ============ Repelem Test ============

        @Test
        public void test_repeatElements_repeatsElements() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(1, 1));
            assertEquals(4, repeated.get(2, 2));
        }

        // ============ Repmat Test ============

        @Test
        public void test_repeatMatrix_repeatsMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortMatrix repeated = m.repeatMatrix(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(2, 2));
            assertEquals(4, repeated.get(3, 3));
        }

        // ============ Flatten Tests ============

        @Test
        public void test_flatten_returnsAllElements() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            ShortList flattened = m.flatten();
            assertEquals(6, flattened.size());
            assertEquals(1, flattened.get(0));
            assertEquals(6, flattened.get(5));
        }

        @Test
        public void test_applyOnFlattened_appliesOperationToEachRow() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            final int[] sum = { 0 };
            m.applyOnFlattened(row -> {
                for (short val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10, sum[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void test_vstack_stacksVertically() {
            short[][] arr1 = { { 1, 2 } };
            short[][] arr2 = { { 3, 4 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(3, stacked.get(1, 0));
        }

        @Test
        public void test_vstack_incompatibleColumns_throwsException() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4, 5 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_hstack_stacksHorizontally() {
            short[][] arr1 = { { 1 }, { 2 } };
            short[][] arr2 = { { 3 }, { 4 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(3, stacked.get(0, 1));
        }

        @Test
        public void test_hstack_incompatibleRows_throwsException() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 }, { 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Tests ============

        @Test
        public void test_add_addsMatrices() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 5, 6 }, { 7, 8 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix sum = m1.add(m2);
            assertEquals(6, sum.get(0, 0));
            assertEquals(8, sum.get(0, 1));
            assertEquals(12, sum.get(1, 1));
        }

        @Test
        public void test_add_incompatibleSize_throwsException() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void test_subtract_subtractsMatrices() {
            short[][] arr1 = { { 5, 6 }, { 7, 8 } };
            short[][] arr2 = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix diff = m1.subtract(m2);
            assertEquals(4, diff.get(0, 0));
            assertEquals(4, diff.get(0, 1));
            assertEquals(4, diff.get(1, 1));
        }

        @Test
        public void test_multiply_multipliesMatrices() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 2, 0 }, { 1, 2 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix product = m1.multiply(m2);
            assertEquals(4, product.get(0, 0));
            assertEquals(4, product.get(0, 1));
            assertEquals(10, product.get(1, 0));
            assertEquals(8, product.get(1, 1));
        }

        @Test
        public void test_multiply_incompatibleSize_throwsException() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 4, 5 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Conversion Tests ============

        @Test
        public void test_boxed_convertsToBoxedMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            Matrix<Short> boxed = m.boxed();
            assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
            assertEquals(Short.valueOf((short) 4), boxed.get(1, 1));
        }

        @Test
        public void test_toIntMatrix_convertsToIntMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            IntMatrix intMatrix = m.toIntMatrix();
            assertEquals(1, intMatrix.get(0, 0));
            assertEquals(4, intMatrix.get(1, 1));
        }

        @Test
        public void test_toLongMatrix_convertsToLongMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            LongMatrix longMatrix = m.toLongMatrix();
            assertEquals(1L, longMatrix.get(0, 0));
            assertEquals(4L, longMatrix.get(1, 1));
        }

        @Test
        public void test_toFloatMatrix_convertsToFloatMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals(1.0f, floatMatrix.get(0, 0));
            assertEquals(4.0f, floatMatrix.get(1, 1));
        }

        @Test
        public void test_toDoubleMatrix_convertsToDoubleMatrix() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(1.0, doubleMatrix.get(0, 0));
            assertEquals(4.0, doubleMatrix.get(1, 1));
        }

        // ============ Zip Tests ============

        @Test
        public void test_zipWith_withTwoMatrices() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 5, 6 }, { 7, 8 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
            assertEquals(6, result.get(0, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void test_zipWith_withThreeMatrices() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 5, 6 }, { 7, 8 } };
            short[][] arr3 = { { 9, 10 }, { 11, 12 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            ShortMatrix m3 = new ShortMatrix(arr3);
            ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
            assertEquals(15, result.get(0, 0));
            assertEquals(24, result.get(1, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamMainDiagonal_streamsDiagonal() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamMainDiagonal().toArray();
            assertArrayEquals(new short[] { 1, 5, 9 }, result);
        }

        @Test
        public void test_streamAntiDiagonal_streamsAntiDiagonal() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new short[] { 3, 5, 7 }, result);
        }

        @Test
        public void test_streamH_streamsAllElements() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamHorizontal().toArray();
            assertArrayEquals(new short[] { 1, 2, 3, 4 }, result);
        }

        @Test
        public void test_streamH_withRowIndex() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamHorizontal(1).toArray();
            assertArrayEquals(new short[] { 4, 5, 6 }, result);
        }

        @Test
        public void test_streamH_withRowRange() {
            short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new short[] { 3, 4, 5, 6 }, result);
        }

        @Test
        public void test_streamV_streamsAllElementsVertically() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamVertical().toArray();
            assertArrayEquals(new short[] { 1, 3, 2, 4 }, result);
        }

        @Test
        public void test_streamV_withColumnIndex() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamVertical(1).toArray();
            assertArrayEquals(new short[] { 2, 5 }, result);
        }

        @Test
        public void test_streamV_withColumnRange() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            short[] result = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new short[] { 2, 5, 3, 6 }, result);
        }

        @Test
        public void test_streamR_streamsRows() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            Stream<ShortStream> streams = m.streamRows();
            assertEquals(2, streams.count());
        }

        @Test
        public void test_streamR_withRange() {
            short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            Stream<ShortStream> streams = m.streamRows(1, 3);
            assertEquals(2, streams.count());
        }

        @Test
        public void test_streamC_streamsColumns() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            Stream<ShortStream> streams = m.streamColumns();
            assertEquals(2, streams.count());
        }

        @Test
        public void test_streamC_withRange() {
            short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ShortMatrix m = new ShortMatrix(arr);
            Stream<ShortStream> streams = m.streamColumns(1, 3);
            assertEquals(2, streams.count());
        }

        // ============ ForEach Test ============

        @Test
        public void test_forEach_iteratesAllElements() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            final int[] sum = { 0 };
            m.forEach(val -> sum[0] += val);
            assertEquals(10, sum[0]);
        }

        // ============ Utility Tests ============

        @Test
        public void test_println_returnsString() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            String result = m.printAndReturn();
            assertNotNull(result);
            assertTrue(result.length() > 0);
        }

        @Test
        public void test_hashCode_consistentWithEquals() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void test_equals_sameContent() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            assertEquals(m1, m2);
        }

        @Test
        public void test_equals_differentContent() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 1, 2 }, { 3, 5 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_equals_differentDimensions() {
            short[][] arr1 = { { 1, 2 }, { 3, 4 } };
            short[][] arr2 = { { 1, 2, 3 } };
            ShortMatrix m1 = new ShortMatrix(arr1);
            ShortMatrix m2 = new ShortMatrix(arr2);
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_toString_returnsStringRepresentation() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            ShortMatrix m = new ShortMatrix(arr);
            String result = m.toString();
            assertNotNull(result);
            assertTrue(result.contains("1"));
            assertTrue(result.contains("4"));
        }
    }

    // ============================================================
    // Tests for AbstractMatrix-inherited methods missing coverage
    // ============================================================

    @Nested
    class ShortMatrixAbstractMethodsTest extends TestBase {

        @Test
        public void testIsSameShape() {
            ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
            ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });

            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testIsSameShape_NullThrows() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.isSameShape(null));
        }

        @Test
        public void testAdjacent4Points() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

            List<Sheet.Point> center = m.adjacent4Points(1, 1).toList();
            assertEquals(4, center.size());

            List<Sheet.Point> corner = m.adjacent4Points(0, 0).toList();
            assertEquals(2, corner.size());

            List<Sheet.Point> edge = m.adjacent4Points(0, 1).toList();
            assertEquals(3, edge.size());
        }

        @Test
        public void testAdjacent8Points() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

            List<Sheet.Point> center = m.adjacent8Points(1, 1).toList();
            assertEquals(8, center.size());

            List<Sheet.Point> corner = m.adjacent8Points(0, 0).toList();
            assertEquals(3, corner.size());
        }

        @Test
        public void testPointsMainDiagonal() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Sheet.Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsMainDiagonal_NonSquareThrows() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.pointsMainDiagonal());
        }

        @Test
        public void testPointsAntiDiagonal() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Sheet.Point> points = m.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(2, points.get(0).columnIndex());
        }

        @Test
        public void testPointsHorizontal() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Sheet.Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
        }

        @Test
        public void testPointsHorizontal_SingleRow() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Sheet.Point> points = m.pointsHorizontal(0).toList();
            assertEquals(2, points.size());
        }

        @Test
        public void testPointsVertical() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Sheet.Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
        }

        @Test
        public void testPointsVertical_SingleColumn() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<Sheet.Point> points = m.pointsVertical(1).toList();
            assertEquals(2, points.size());
        }

        @Test
        public void testPointsRows() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<List<Sheet.Point>> rows = m.pointsRows().map(s -> s.toList()).toList();
            assertEquals(3, rows.size());
            assertEquals(2, rows.get(0).size());
        }

        @Test
        public void testPointsColumns() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<List<Sheet.Point>> cols = m.pointsColumns().map(s -> s.toList()).toList();
            assertEquals(3, cols.size());
            assertEquals(2, cols.get(0).size());
        }

        @Test
        public void testAccept() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            int[] sum = { 0 };
            m.accept(matrix -> {
                ShortList flat = matrix.flatten();
                for (int i = 0; i < flat.size(); i++)
                    sum[0] += flat.get(i);
            });
            assertEquals(10, sum[0]);
        }

        @Test
        public void testAccept_NullThrows() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.accept(null));
        }

        @Test
        public void testApply() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            long count = m.apply(matrix -> matrix.elementCount());
            assertEquals(4, count);
        }

        @Test
        public void testApply_NullThrows() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.apply(null));
        }

        @Test
        public void testForEachIndex() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            List<String> indices = new ArrayList<>();
            m.forEachIndex((r, c) -> indices.add(r + "," + c));
            assertEquals(4, indices.size());
            assertTrue(indices.contains("0,0"));
            assertTrue(indices.contains("1,1"));
        }

        @Test
        public void testToIntMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix intM = m.toIntMatrix();
            assertEquals(1, intM.get(0, 0));
            assertEquals(4, intM.get(1, 1));
        }

        @Test
        public void testToLongMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix longM = m.toLongMatrix();
            assertEquals(1L, longM.get(0, 0));
            assertEquals(4L, longM.get(1, 1));
        }

        @Test
        public void testToFloatMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix floatM = m.toFloatMatrix();
            assertEquals(1.0f, floatM.get(0, 0), 0.001f);
            assertEquals(4.0f, floatM.get(1, 1), 0.001f);
        }

        @Test
        public void testToDoubleMatrix() {
            ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix doubleM = m.toDoubleMatrix();
            assertEquals(1.0, doubleM.get(0, 0), 0.001);
            assertEquals(4.0, doubleM.get(1, 1), 0.001);
        }
    }

}
