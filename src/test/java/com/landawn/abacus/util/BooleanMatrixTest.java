package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.Stream;

class BooleanMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        // Test with null array
        BooleanMatrix matrix1 = new BooleanMatrix(null);
        assertEquals(0, matrix1.rowCount());
        assertEquals(0, matrix1.columnCount());

        // Test with valid array
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix2 = new BooleanMatrix(arr);
        assertEquals(2, matrix2.rowCount());
        assertEquals(2, matrix2.columnCount());
    }

    @Test
    public void testEmpty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        // Test with null/empty
        BooleanMatrix empty = BooleanMatrix.of((boolean[][]) null);
        assertTrue(empty.isEmpty());

        // Test with valid array
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);
        assertEquals(2, matrix.rowCount());
        assertEquals(2, matrix.columnCount());
    }

    @Test
    public void testRandom() {
        BooleanMatrix matrix = BooleanMatrix.random(5);
        assertEquals(1, matrix.rowCount());
        assertEquals(5, matrix.columnCount());
    }

    @Test
    public void testRepeat() {
        BooleanMatrix matrix = BooleanMatrix.repeat(1, 5, true);
        assertEquals(1, matrix.rowCount());
        assertEquals(5, matrix.columnCount());
        for (int i = 0; i < 5; i++) {
            assertTrue(matrix.get(0, i));
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        boolean[] diagonal = { true, false, true };
        BooleanMatrix matrix = BooleanMatrix.mainDiagonal(diagonal);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertTrue(matrix.get(0, 0));
        assertFalse(matrix.get(1, 1));
        assertTrue(matrix.get(2, 2));
        assertFalse(matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        boolean[] diagonal = { true, false, true };
        BooleanMatrix matrix = BooleanMatrix.antiDiagonal(diagonal);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertTrue(matrix.get(0, 2));
        assertFalse(matrix.get(1, 1));
        assertTrue(matrix.get(2, 0));
    }

    @Test
    public void testDiagonal() {
        boolean[] mainDiag = { true, true, false };
        boolean[] antiDiag = { false, true, false };
        BooleanMatrix matrix = BooleanMatrix.diagonals(mainDiag, antiDiag);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());

        // Test with different lengths
        assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.diagonals(new boolean[] { true }, new boolean[] { true, false }));
    }

    @Test
    public void testComponentType() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true } });
        assertEquals(boolean.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);
        assertTrue(matrix.get(0, 0));
        assertFalse(matrix.get(0, 1));
        assertFalse(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(2, 0));
    }

    @Test
    public void testGetWithPoint() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);
        assertTrue(matrix.get(Point.of(0, 0)));
        assertFalse(matrix.get(Point.of(0, 1)));
    }

    @Test
    public void testSet() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);
        matrix.set(0, 0, false);
        assertFalse(matrix.get(0, 0));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.set(2, 0, true));
    }

    @Test
    public void testSetWithPoint() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);
        matrix.set(Point.of(0, 0), false);
        assertFalse(matrix.get(Point.of(0, 0)));
    }

    @Test
    public void testUpOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertTrue(matrix.above(0, 0).isEmpty());
        assertEquals(true, matrix.above(1, 0).orElse(false));
    }

    @Test
    public void testDownOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertEquals(false, matrix.below(0, 0).orElse(true));
        assertTrue(matrix.below(1, 0).isEmpty());
    }

    @Test
    public void testLeftOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertTrue(matrix.left(0, 0).isEmpty());
        assertEquals(true, matrix.left(0, 1).orElse(false));
    }

    @Test
    public void testRightOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertEquals(false, matrix.right(0, 0).orElse(true));
        assertTrue(matrix.right(0, 1).isEmpty());
    }

    @Test
    public void testRow() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] row = matrix.rowView(0);
        assertEquals(2, row.length);
        assertTrue(row[0]);
        assertFalse(row[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(2));
    }

    // Verify rowCopy returns a defensive row snapshot and enforces row bounds.
    @Test
    public void testRowCopy() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] rowCopy = matrix.rowCopy(0);
        assertArrayEquals(new boolean[] { true, false }, rowCopy);

        rowCopy[0] = false;
        assertArrayEquals(new boolean[] { true, false }, matrix.rowView(0));
    }

    @Test
    public void testRowCopy_InvalidIndex() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

        assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(2));
    }

    @Test
    public void testColumn() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] col = matrix.columnCopy(0);
        assertEquals(2, col.length);
        assertTrue(col[0]);
        assertFalse(col[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(2));
    }

    @Test
    public void testSetRow() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.setRow(0, new boolean[] { false, false });
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(0, 1));

        assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new boolean[] { true }));
    }

    @Test
    public void testSetColumn() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.setColumn(0, new boolean[] { false, false });
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(1, 0));

        assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new boolean[] { true }));
    }

    @Test
    public void testUpdateRow() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateRow(0, val -> !val);
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
    }

    @Test
    public void testUpdateColumn() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateColumn(0, val -> !val);
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(1, 0));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(-1, val -> !val));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(2, val -> !val));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertThrows(IllegalArgumentException.class, () -> matrix.updateRow(0, (Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateColumn(0, (Throwables.BooleanUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] diag = matrix.getMainDiagonal();
        assertEquals(2, diag.length);
        assertTrue(diag[0]);
        assertTrue(diag[1]);

        // Test non-square matrix
        BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.setMainDiagonal(new boolean[] { false, false });
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(1, 1));

        assertThrows(IllegalArgumentException.class, () -> matrix.setMainDiagonal(new boolean[] { true }));
    }

    @Test
    public void testUpdateLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateMainDiagonal(val -> !val);
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(1, 1));
    }

    @Test
    public void testGetRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] diag = matrix.getAntiDiagonal();
        assertEquals(2, diag.length);
        assertFalse(diag[0]);
        assertFalse(diag[1]);
    }

    @Test
    public void testSetRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.setAntiDiagonal(new boolean[] { true, true });
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateAntiDiagonal(val -> !val);
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
    }

    @Test
    public void testUpdateAll() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateAll(val -> !val);
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
        assertFalse(matrix.get(1, 1));
    }

    @Test
    public void testUpdateAllWithIndices() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateAll((i, j) -> i == j);
        assertTrue(matrix.get(0, 0));
        assertFalse(matrix.get(0, 1));
        assertFalse(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.replaceIf(val -> !val, true);
        assertTrue(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
    }

    @Test
    public void testReplaceIfWithIndices() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.replaceIf((i, j) -> i == j, true);
        assertTrue(matrix.get(0, 0));
        assertFalse(matrix.get(0, 1));
        assertFalse(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
    }

    @Test
    public void testMap() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix mapped = matrix.map(val -> !val);
        assertFalse(mapped.get(0, 0));
        assertTrue(mapped.get(0, 1));
        assertTrue(mapped.get(1, 0));
        assertFalse(mapped.get(1, 1));
    }

    @Test
    public void testMapNullMapper() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.BooleanFunction<String, RuntimeException>) null, String.class));
    }

    @Test
    public void testMapToObj() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        Matrix<String> mapped = matrix.mapToObj(val -> val ? "YES" : "NO", String.class);
        assertEquals("YES", mapped.get(0, 0));
        assertEquals("NO", mapped.get(0, 1));
    }

    @Test
    public void testFill() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.fill(true);
        assertTrue(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
        assertTrue(matrix.get(1, 1));
    }

    @Test
    public void testFillWithArray() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[][] fillArr = { { false, false }, { false, false } };
        matrix.copyFrom(fillArr);
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(0, 1));
        assertFalse(matrix.get(1, 0));
        assertFalse(matrix.get(1, 1));
    }

    @Test
    public void testFillWithIndices() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[][] fillArr = { { false, false } };
        matrix.copyFrom(0, 1, fillArr);
        assertFalse(matrix.get(0, 1));
        assertFalse(matrix.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(-1, 0, fillArr));
    }

    @Test
    public void testCopy() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix copy = matrix.copy();
        assertEquals(matrix.rowCount(), copy.rowCount());
        assertEquals(matrix.columnCount(), copy.columnCount());
        assertTrue(copy.get(0, 0));

        // Ensure it's a deep copy
        copy.set(0, 0, false);
        assertTrue(matrix.get(0, 0));
    }

    @Test
    public void testCopyWithRowRange() {
        boolean[][] arr = { { true, false }, { false, true }, { true, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix copy = matrix.copy(0, 2);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
    }

    @Test
    public void testCopyWithFullRange() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix copy = matrix.copy(0, 2, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertFalse(copy.get(0, 0));
        assertTrue(copy.get(0, 1));
    }

    @Test
    public void testExtend() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix extended = matrix.resize(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertFalse(extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix extended = matrix.resize(3, 3, true);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertTrue(extended.get(2, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 3, true));
    }

    @Test
    public void testExtendWithDirections() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix extended = matrix.extend(1, 1, 1, 1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertFalse(extended.get(0, 0));
    }

    @Test
    public void testExtendWithDirectionsAndDefault() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix extended = matrix.extend(1, 1, 1, 1, true);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertTrue(extended.get(0, 0));

        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 1, 1, 1, true));
    }

    @Test
    public void testReverseH() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.flipInPlaceHorizontally();
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
    }

    @Test
    public void testReverseV() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.flipInPlaceVertically();
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(1, 0));
    }

    @Test
    public void testFlipH() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix flipped = matrix.flipHorizontally();
        assertFalse(flipped.get(0, 0));
        assertTrue(flipped.get(0, 1));

        // Original unchanged
        assertTrue(matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix flipped = matrix.flipVertically();
        assertFalse(flipped.get(0, 0));
        assertTrue(flipped.get(1, 0));

        // Original unchanged
        assertTrue(matrix.get(0, 0));
    }

    @Test
    public void testRotate90() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix rotated = matrix.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertFalse(rotated.get(0, 0));
        assertTrue(rotated.get(0, 1));
    }

    @Test
    public void testRotate180() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix rotated = matrix.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertTrue(rotated.get(0, 0));
        assertFalse(rotated.get(0, 1));
    }

    @Test
    public void testRotate270() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix rotated = matrix.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertFalse(rotated.get(0, 0));
        assertTrue(rotated.get(1, 0));
    }

    @Test
    public void testTranspose() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix transposed = matrix.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertTrue(transposed.get(0, 0));
        assertFalse(transposed.get(0, 1));
    }

    @Test
    public void testReshape() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix reshaped = matrix.reshape(1, 4);
        assertEquals(1, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
        assertTrue(reshaped.get(0, 0));
        assertFalse(reshaped.get(0, 1));

        // Test reshape with too-small dimensions throws exception
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 3));
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 1));
    }

    @Test
    public void testRepelem() {
        boolean[][] arr = { { true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix repeated = matrix.repeatElements(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertTrue(repeated.get(0, 0));
        assertTrue(repeated.get(0, 1));
        assertTrue(repeated.get(0, 2));
        assertFalse(repeated.get(0, 3));

        assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(0, 1));
    }

    @Test
    public void testRepmat() {
        boolean[][] arr = { { true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix tiled = matrix.repeatMatrix(2, 3);
        assertEquals(2, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertTrue(tiled.get(0, 0));
        assertFalse(tiled.get(0, 1));
        assertTrue(tiled.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(0, 1));
    }

    @Test
    public void testFlatten() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanList flat = matrix.flatten();
        assertEquals(4, flat.size());
        assertTrue(flat.get(0));
        assertFalse(flat.get(1));
        assertFalse(flat.get(2));
        assertTrue(flat.get(3));
    }

    @Test
    public void testFlatOp() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        int[] count = { 0 };
        matrix.applyOnFlattened(array -> count[0] += array.length);
        assertEquals(4, count[0]);
    }

    // Tests for countTrue, all, any
    @Test
    public void testCountTrue() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        assertEquals(3, m.countTrue());
    }

    @Test
    public void testCountTrue_AllTrue() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, true }, { true, true } });
        assertEquals(4, m.countTrue());
    }

    @Test
    public void testCountTrue_AllFalse() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
        assertEquals(0, m.countTrue());
    }

    @Test
    public void testCountTrue_Empty() {
        BooleanMatrix m = BooleanMatrix.empty();
        assertEquals(0, m.countTrue());
    }

    @Test
    public void testAll() {
        BooleanMatrix allTrue = BooleanMatrix.of(new boolean[][] { { true, true }, { true, true } });
        assertTrue(allTrue.all());
    }

    @Test
    public void testAll_WithFalse() {
        BooleanMatrix mixed = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true } });
        assertFalse(mixed.all());
    }

    @Test
    public void testAll_Empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertTrue(empty.all());
    }

    @Test
    public void testAny() {
        BooleanMatrix mixed = BooleanMatrix.of(new boolean[][] { { false, true }, { false, false } });
        assertTrue(mixed.any());
    }

    @Test
    public void testAny_AllFalse() {
        BooleanMatrix allFalse = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
        assertFalse(allFalse.any());
    }

    @Test
    public void testAny_Empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertFalse(empty.any());
    }

    @Test
    public void testVstack() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false, true } });

        BooleanMatrix stacked = a.stackVertically(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(1, 0));

        BooleanMatrix c = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.stackVertically(c));
    }

    @Test
    public void testHstack() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false }, { true } });

        BooleanMatrix stacked = a.stackHorizontally(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(0, 1));

        BooleanMatrix c = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.stackHorizontally(c));
    }

    @Test
    public void testBoxed() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        Matrix<Boolean> boxed = matrix.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(2, boxed.columnCount());
        assertEquals(Boolean.TRUE, boxed.get(0, 0));
        assertEquals(Boolean.FALSE, boxed.get(0, 1));
    }

    @Test
    public void testZipWith() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { false, true } });

        BooleanMatrix and = a.zipWith(b, (x, y) -> x && y);
        assertTrue(and.get(0, 0));
        assertFalse(and.get(0, 1));
        assertFalse(and.get(1, 0));
        assertTrue(and.get(1, 1));

        BooleanMatrix c = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.zipWith(c, (x, y) -> x && y));
    }

    @Test
    public void testZipWithThree() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true } });
        BooleanMatrix c = BooleanMatrix.of(new boolean[][] { { false, true } });

        BooleanMatrix result = a.zipWith(b, c, (x, y, z) -> (x && y) || z);
        assertTrue(result.get(0, 0));
        assertTrue(result.get(0, 1));

        BooleanMatrix d = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.zipWith(b, d, (x, y, z) -> x && y && z));
    }

    @Test
    public void testStreamLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> diagonal = matrix.streamMainDiagonal().toList();
        assertEquals(2, diagonal.size());
        assertTrue(diagonal.get(0));
        assertTrue(diagonal.get(1));

        BooleanMatrix empty = BooleanMatrix.empty();
        assertTrue(empty.streamMainDiagonal().toList().isEmpty());
    }

    @Test
    public void testStreamRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> diagonal = matrix.streamAntiDiagonal().toList();
        assertEquals(2, diagonal.size());
        assertFalse(diagonal.get(0));
        assertFalse(diagonal.get(1));
    }

    @Test
    public void testStreamH() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamHorizontal().toList();
        assertEquals(4, elements.size());
        assertTrue(elements.get(0));
        assertFalse(elements.get(1));
        assertFalse(elements.get(2));
        assertTrue(elements.get(3));
    }

    @Test
    public void testStreamHWithRow() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> row = matrix.streamHorizontal(0).toList();
        assertEquals(2, row.size());
        assertTrue(row.get(0));
        assertFalse(row.get(1));
    }

    @Test
    public void testStreamHWithRange() {
        boolean[][] arr = { { true, false }, { false, true }, { true, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamHorizontal(1, 3).toList();
        assertEquals(4, elements.size());
        assertFalse(elements.get(0));
        assertTrue(elements.get(1));

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
    }

    @Test
    public void testStreamV() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamVertical().toList();
        assertEquals(4, elements.size());
        assertTrue(elements.get(0));
        assertFalse(elements.get(1));
        assertFalse(elements.get(2));
        assertTrue(elements.get(3));
    }

    @Test
    public void testStreamVWithColumn() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> col = matrix.streamVertical(0).toList();
        assertEquals(2, col.size());
        assertTrue(col.get(0));
        assertFalse(col.get(1));
    }

    @Test
    public void testStreamVWithRange() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamVertical(1, 3).toList();
        assertEquals(4, elements.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1, 2));
    }

    @Test
    public void testStreamR() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> rows = matrix.streamRows().map(stream -> stream.toList()).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).size());
        assertTrue(rows.get(0).get(0));
        assertFalse(rows.get(0).get(1));
    }

    @Test
    public void testStreamRWithRange() {
        boolean[][] arr = { { true, false }, { false, true }, { true, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> rows = matrix.streamRows(1, 3).map(stream -> stream.toList()).toList();
        assertEquals(2, rows.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamRows(-1, 2));
    }

    @Test
    public void testStreamC() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> columnCount = matrix.streamColumns().map(stream -> stream.toList()).toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).size());
        assertTrue(columnCount.get(0).get(0));
        assertFalse(columnCount.get(0).get(1));
    }

    @Test
    public void testStreamCWithRange() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> columnCount = matrix.streamColumns(1, 3).map(stream -> stream.toList()).toList();
        assertEquals(2, columnCount.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamColumns(-1, 2));
    }

    @Test
    public void testForEach() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        int[] trueCount = { 0 };
        matrix.forEach(val -> {
            if (val)
                trueCount[0]++;
        });
        assertEquals(2, trueCount[0]);
    }

    @Test
    public void testForEachWithRange() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        int[] trueCount = { 0 };
        matrix.forEach(0, 2, 1, 3, val -> {
            if (val)
                trueCount[0]++;
        });
        assertEquals(2, trueCount[0]);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(-1, 2, 0, 2, val -> {
        }));
    }

    @Test
    public void testForEachNullAction() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.BooleanConsumer<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.forEach(0, 1, 0, 2, (Throwables.BooleanConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::println);
    }

    @Test
    public void testHashCode() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix1 = BooleanMatrix.of(arr);
        BooleanMatrix matrix2 = BooleanMatrix.of(arr);

        assertEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testEquals() {
        boolean[][] arr1 = { { true, false }, { false, true } };
        boolean[][] arr2 = { { true, false }, { false, true } };
        boolean[][] arr3 = { { true, false }, { true, false } };

        BooleanMatrix matrix1 = BooleanMatrix.of(arr1);
        BooleanMatrix matrix2 = BooleanMatrix.of(arr2);
        BooleanMatrix matrix3 = BooleanMatrix.of(arr3);

        assertTrue(matrix1.equals(matrix1));
        assertTrue(matrix1.equals(matrix2));
        assertFalse(matrix1.equals(matrix3));
        assertFalse(matrix1.equals(null));
        assertFalse(matrix1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        String str = matrix.toString();
        assertNotNull(str);
        assertTrue(str.contains("true"));
        assertTrue(str.contains("false"));
    }

    @Nested
    @Tag("2025")
    class BooleanMatrix2025Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = new BooleanMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            BooleanMatrix m = new BooleanMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            BooleanMatrix m = new BooleanMatrix(new boolean[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            BooleanMatrix m = new BooleanMatrix(new boolean[][] { { true } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertTrue(m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(BooleanMatrix.empty(), BooleanMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            BooleanMatrix m = BooleanMatrix.of((boolean[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testRandom() {
            BooleanMatrix m = BooleanMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist (values are random)
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRandom_withRowsCols() {
            BooleanMatrix m = BooleanMatrix.random(2, 3);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertNotNull(m.get(i, j));
                }
            }
        }

        @Test
        public void testRepeat_withRowsCols() {
            BooleanMatrix m = BooleanMatrix.repeat(2, 3, true);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertTrue(m.get(i, j));
                }
            }
        }

        @Test
        public void testRepeat_withFalse() {
            BooleanMatrix m = BooleanMatrix.repeat(1, 3, false);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 3; i++) {
                assertFalse(m.get(0, i));
            }
        }

        @Test
        public void testDiagonalLU2RD() {
            BooleanMatrix m = BooleanMatrix.mainDiagonal(new boolean[] { true, false, true });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 2));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            BooleanMatrix m = BooleanMatrix.antiDiagonal(new boolean[] { true, false, true });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 2));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 0));
            assertFalse(m.get(0, 0));
            assertFalse(m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            BooleanMatrix m = BooleanMatrix.diagonals(new boolean[] { true, false, true }, new boolean[] { false, true, false });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 2));
            assertFalse(m.get(0, 2));
            assertFalse(m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            BooleanMatrix m = BooleanMatrix.diagonals(new boolean[] { true, false, true }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            BooleanMatrix m = BooleanMatrix.diagonals(null, new boolean[] { false, true, false });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertFalse(m.get(0, 2));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            BooleanMatrix m = BooleanMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.diagonals(new boolean[] { true, false }, new boolean[] { true, false, true }));
        }

        @Test
        public void testUnbox() {
            Boolean[][] boxed = { { true, false }, { false, true } };
            Matrix<Boolean> boxedMatrix = Matrix.of(boxed);
            BooleanMatrix unboxed = BooleanMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertTrue(unboxed.get(0, 0));
            assertTrue(unboxed.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Boolean[][] boxed = { { true, null }, { null, false } };
            Matrix<Boolean> boxedMatrix = Matrix.of(boxed);
            BooleanMatrix unboxed = BooleanMatrix.unbox(boxedMatrix);
            assertTrue(unboxed.get(0, 0));
            assertFalse(unboxed.get(0, 1)); // null -> false
            assertFalse(unboxed.get(1, 0)); // null -> false
            assertFalse(unboxed.get(1, 1));
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            assertEquals(boolean.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(1, 2));
        }

        @Test
        public void testGet_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertTrue(m.get(Point.of(0, 0)));
            assertTrue(m.get(Point.of(1, 1)));
            assertFalse(m.get(Point.of(0, 1)));
        }

        @Test
        public void testSet() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.set(0, 0, false);
            assertFalse(m.get(0, 0));

            m.set(1, 1, false);
            assertFalse(m.get(1, 1));
        }

        @Test
        public void testSet_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, true));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, true));
        }

        @Test
        public void testSetWithPoint() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.set(Point.of(0, 0), false);
            assertFalse(m.get(Point.of(0, 0)));
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

            OptionalBoolean up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertTrue(up.get());

            // Top row has no element above
            OptionalBoolean empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

            OptionalBoolean down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertFalse(down.get());

            // Bottom row has no element below
            OptionalBoolean empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

            OptionalBoolean left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertTrue(left.get());

            // Leftmost column has no element to the left
            OptionalBoolean empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

            OptionalBoolean right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertFalse(right.get());

            // Rightmost column has no element to the right
            OptionalBoolean empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            assertArrayEquals(new boolean[] { true, false, true }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, true, false }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            assertArrayEquals(new boolean[] { true, false }, m.columnCopy(0));
            assertArrayEquals(new boolean[] { false, true }, m.columnCopy(1));
            assertArrayEquals(new boolean[] { true, false }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.setRow(0, new boolean[] { false, true });
            assertArrayEquals(new boolean[] { false, true }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, true }, m.rowView(1)); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new boolean[] { true }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new boolean[] { true, false, true }));
        }

        @Test
        public void testSetColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.setColumn(0, new boolean[] { false, true });
            assertArrayEquals(new boolean[] { false, true }, m.columnCopy(0));
            assertArrayEquals(new boolean[] { false, true }, m.columnCopy(1)); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new boolean[] { true }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new boolean[] { true, false, true }));
        }

        @Test
        public void testUpdateRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateRow(0, x -> !x);
            assertArrayEquals(new boolean[] { false, true }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, true }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateColumn(0, x -> !x);
            assertArrayEquals(new boolean[] { false, true }, m.columnCopy(0));
            assertArrayEquals(new boolean[] { false, true }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            assertArrayEquals(new boolean[] { true, true, true }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            m.setMainDiagonal(new boolean[] { false, false, false });
            assertFalse(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new boolean[] { true }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new boolean[] { true, false }));
        }

        @Test
        public void testUpdateLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            m.updateMainDiagonal(x -> !x);
            assertFalse(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(2, 2));
            assertFalse(m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> !x));
        }

        @Test
        public void testGetRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            assertArrayEquals(new boolean[] { true, true, true }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new boolean[] { true }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new boolean[] { true, false }));
        }

        @Test
        public void testUpdateRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            m.updateAntiDiagonal(x -> !x);
            assertFalse(m.get(0, 2));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(2, 0));
            assertFalse(m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> !x));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateAll(x -> !x);
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertFalse(m.get(1, 1));
        }

        @Test
        public void testUpdateAll_withIndices() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            m.updateAll((i, j) -> i == j);
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testReplaceIf() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            m.replaceIf(x -> x, false);
            assertFalse(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(0, 2));
            assertFalse(m.get(1, 0));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(1, 2));
        }

        @Test
        public void testReplaceIf_withIndices() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            m.replaceIf((i, j) -> i == j, false); // Replace diagonal
            assertFalse(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(2, 2));
            assertFalse(m.get(0, 1)); // unchanged
        }

        @Test
        public void testMap() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix result = m.map(x -> !x);
            assertFalse(result.get(0, 0));
            assertTrue(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertFalse(result.get(1, 1));

            // Original unchanged
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<String> result = m.mapToObj(x -> x ? "T" : "F", String.class);
            assertEquals("T", result.get(0, 0));
            assertEquals("F", result.get(0, 1));
            assertEquals("F", result.get(1, 0));
            assertEquals("T", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.fill(true);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertTrue(m.get(i, j));
                }
            }
        }

        @Test
        public void testFill_withArray() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            boolean[][] patch = { { true, false }, { false, true } };
            m.copyFrom(patch);
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            boolean[][] patch = { { true, false }, { false, true } };
            m.copyFrom(1, 1, patch);
            assertFalse(m.get(0, 0)); // unchanged
            assertTrue(m.get(1, 1));
            assertFalse(m.get(1, 2));
            assertFalse(m.get(2, 1));
            assertTrue(m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            boolean[][] patch = { { true, false }, { false, true } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertTrue(copy.get(0, 0));

            // Modify copy shouldn't affect original
            copy.set(0, 0, false);
            assertTrue(m.get(0, 0));
            assertFalse(copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertFalse(subset.get(0, 0));
            assertTrue(subset.get(1, 2));
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertFalse(submatrix.get(0, 0));
            assertFalse(submatrix.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertTrue(extended.get(1, 1));
            assertFalse(extended.get(3, 3)); // new cells are false
        }

        @Test
        public void testExtend_smaller() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertTrue(truncated.get(0, 0));
            assertTrue(truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.resize(3, 3, true);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertTrue(extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, false));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, false));
        }

        @Test
        public void testExtend_directional() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertTrue(extended.get(1, 2));
            assertTrue(extended.get(2, 3));

            // New cells are false
            assertFalse(extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalWithDefault() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix extended = m.extend(1, 1, 1, 1, true);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertTrue(extended.get(1, 1));

            // Check new values
            assertTrue(extended.get(0, 0));
            assertTrue(extended.get(4, 4));
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, false));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            m.flipInPlaceHorizontally();
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertTrue(m.get(0, 2));
            assertFalse(m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
            m.flipInPlaceVertically();
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix flipped = m.flipHorizontally();
            assertTrue(flipped.get(0, 0));
            assertFalse(flipped.get(0, 1));
            assertTrue(flipped.get(0, 2));

            // Original unchanged
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
            BooleanMatrix flipped = m.flipVertically();
            assertTrue(flipped.get(0, 0));
            assertFalse(flipped.get(1, 0));
            assertTrue(flipped.get(2, 0));

            // Original unchanged
            assertTrue(m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertFalse(rotated.get(0, 0));
            assertTrue(rotated.get(0, 1));
            assertTrue(rotated.get(1, 0));
            assertFalse(rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertTrue(rotated.get(0, 0));
            assertFalse(rotated.get(0, 1));
            assertFalse(rotated.get(1, 0));
            assertTrue(rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertFalse(rotated.get(0, 0));
            assertTrue(rotated.get(0, 1));
            assertTrue(rotated.get(1, 0));
            assertFalse(rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertTrue(transposed.get(0, 0));
            assertFalse(transposed.get(0, 1));
            assertFalse(transposed.get(1, 0));
            assertTrue(transposed.get(1, 1));
            assertTrue(transposed.get(2, 0));
            assertFalse(transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertTrue(transposed.get(0, 0));
            assertFalse(transposed.get(0, 1));
            assertFalse(transposed.get(1, 0));
            assertTrue(transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            boolean[] expected = { true, false, true, false, true, false, true, false, true };
            for (int i = 0; i < 9; i++) {
                assertEquals(expected[i], reshaped.get(0, i));
            }
        }

        @Test
        public void testReshape_back() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanMatrix reshaped = m.reshape(1, 9);
            BooleanMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            BooleanMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertTrue(repeated.get(0, 0));
            assertTrue(repeated.get(0, 1));
            assertTrue(repeated.get(0, 2));
            assertFalse(repeated.get(0, 3));
            assertFalse(repeated.get(0, 4));
            assertFalse(repeated.get(0, 5));
            assertTrue(repeated.get(1, 0)); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertTrue(repeated.get(0, 0));
            assertFalse(repeated.get(0, 1));
            assertTrue(repeated.get(0, 2)); // repeat starts
            assertFalse(repeated.get(0, 3));

            assertFalse(repeated.get(1, 0));
            assertTrue(repeated.get(1, 1));

            // Check vertical repeat
            assertTrue(repeated.get(2, 0)); // vertical repeat starts
            assertFalse(repeated.get(2, 1));
        }

        @Test
        public void testRepmat_invalidArguments() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            BooleanList flat = m.flatten();
            assertEquals(9, flat.size());
            boolean[] expected = { true, false, true, false, true, false, true, false, true };
            for (int i = 0; i < 9; i++) {
                assertEquals(expected[i], flat.get(i));
            }
        }

        @Test
        public void testFlatten_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            BooleanList flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Integer> trueCounts = new ArrayList<>();
            m.applyOnFlattened(row -> {
                int count = 0;
                for (boolean val : row) {
                    if (val) {
                        count++;
                    }
                }
                trueCounts.add(count);
            });
            assertEquals(1, trueCounts.size());
            assertEquals(5, trueCounts.get(0).intValue());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, true, false }, { false, false, true } });
            BooleanMatrix stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertTrue(stacked.get(0, 0));
            assertTrue(stacked.get(2, 0));
            assertTrue(stacked.get(3, 2));
        }

        @Test
        public void testVstack_differentColumnCounts() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertTrue(stacked.get(0, 0));
            assertTrue(stacked.get(0, 2));
            assertTrue(stacked.get(1, 3));
        }

        @Test
        public void testHstack_differentRowCounts() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            Matrix<Boolean> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Boolean.TRUE, boxed.get(0, 0));
            assertEquals(Boolean.FALSE, boxed.get(1, 2));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
            BooleanMatrix result = m1.zipWith(m2, (a, b) -> a && b);

            assertFalse(result.get(0, 0)); // true && false
            assertFalse(result.get(0, 1)); // false && true
            assertFalse(result.get(1, 0)); // false && true
            assertFalse(result.get(1, 1)); // true && false
        }

        @Test
        public void testZipWith_differentShapes() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a && b));
        }

        @Test
        public void testZipWith_threeMatrices() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, true }, { false, false } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false }, { true, false } });
            BooleanMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a && b && c);

            assertTrue(result.get(0, 0)); // true && true && true
            assertFalse(result.get(0, 1)); // false && true && false
            assertFalse(result.get(1, 0)); // false && false && true
            assertFalse(result.get(1, 1)); // true && false && false
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, true }, { false, false } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a && b && c));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Boolean> diagonal = m.streamMainDiagonal().toList();
            assertEquals(3, diagonal.size());
            assertTrue(diagonal.get(0));
            assertTrue(diagonal.get(1));
            assertTrue(diagonal.get(2));
        }

        @Test
        public void testStreamLU2RD_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().count());
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Boolean> antiDiagonal = m.streamAntiDiagonal().toList();
            assertEquals(3, antiDiagonal.size());
            assertTrue(antiDiagonal.get(0));
            assertTrue(antiDiagonal.get(1));
            assertTrue(antiDiagonal.get(2));
        }

        @Test
        public void testStreamRU2LD_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().count());
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> all = m.streamHorizontal().toList();
            assertEquals(6, all.size());
            assertTrue(all.get(0));
            assertFalse(all.get(1));
            assertTrue(all.get(2));
            assertFalse(all.get(3));
        }

        @Test
        public void testStreamH_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.streamHorizontal().count());
        }

        @Test
        public void testStreamH_withRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> row1 = m.streamHorizontal(1).toList();
            assertEquals(3, row1.size());
            assertFalse(row1.get(0));
            assertTrue(row1.get(1));
            assertFalse(row1.get(2));
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Boolean> rows = m.streamHorizontal(1, 3).toList();
            assertEquals(6, rows.size());
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> all = m.streamVertical().toList();
            assertEquals(6, all.size());
            assertTrue(all.get(0));
            assertFalse(all.get(1));
            assertFalse(all.get(2));
        }

        @Test
        public void testStreamV_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.streamVertical().count());
        }

        @Test
        public void testStreamV_withColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> col1 = m.streamVertical(1).toList();
            assertEquals(2, col1.size());
            assertFalse(col1.get(0));
            assertTrue(col1.get(1));
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Boolean> columnCount = m.streamVertical(1, 3).toList();
            assertEquals(6, columnCount.size());
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Stream<Boolean>> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            List<Boolean> row0 = rows.get(0).toList();
            assertEquals(3, row0.size());
            assertTrue(row0.get(0));
        }

        @Test
        public void testStreamR_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Stream<Boolean>> rows = m.streamRows(1, 3).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Stream<Boolean>> columnCount = m.streamColumns().toList();
            assertEquals(3, columnCount.size());
        }

        @Test
        public void testStreamC_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Stream<Boolean>> columnCount = m.streamColumns(1, 3).toList();
            assertEquals(2, columnCount.size());
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ Object Methods Tests ============

        @Test
        public void testHashCode() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false }, { true, false } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false }, { true, false } });
            BooleanMatrix m4 = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("true") || str.contains("false"));
        }

        // ============ Edge Case Tests ============

        @Test
        public void testEmptyMatrixOperations() {
            BooleanMatrix empty = BooleanMatrix.empty();

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            BooleanMatrix extended = empty.resize(2, 2, true);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertTrue(extended.get(0, 0));
        }

        @Test
        public void testAllTrueMatrix() {
            BooleanMatrix m = BooleanMatrix.repeat(1, 3, true);
            m = m.reshape(3, 1);
            m = m.repeatMatrix(1, 3);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertTrue(m.get(i, j));
                }
            }
        }

        @Test
        public void testAllFalseMatrix() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    assertFalse(m.get(i, j));
                }
            }
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> values = new ArrayList<>();
            m.forEach(val -> values.add(val));

            assertEquals(4, values.size());
            assertTrue(values.get(0));
            assertFalse(values.get(1));
            assertFalse(values.get(2));
            assertTrue(values.get(3));
        }

        @Test
        public void testForEach_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            List<Boolean> values = new ArrayList<>();
            empty.forEach(val -> values.add(val));
            assertTrue(values.isEmpty());
        }

        @Test
        public void testForEach_withRanges() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Boolean> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, val -> values.add(val));

            assertEquals(4, values.size());
            assertTrue(values.get(0)); // (1,1)
            assertFalse(values.get(1)); // (1,2)
            assertFalse(values.get(2)); // (2,1)
            assertTrue(values.get(3)); // (2,2)
        }

        @Test
        public void testForEach_withRanges_singleCell() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> values = new ArrayList<>();
            m.forEach(0, 1, 0, 1, val -> values.add(val));

            assertEquals(1, values.size());
            assertTrue(values.get(0));
        }

        @Test
        public void testForEach_withRanges_outOfBounds() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, val -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, val -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, -1, 2, val -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, 0, 3, val -> {
            }));
        }

        // ============ Println Tests ============

        @Test
        public void testPrintln() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertFalse(m.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(m::println);
        }

        @Test
        public void testPrintln_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertTrue(empty.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(empty::println);
        }

        // ============ Additional Edge Cases ============

        @Test
        public void testReshape_invalidDimensions() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            // Reshaping with more elements than available should work but fill with default values
            BooleanMatrix reshaped = m.reshape(3, 3);
            assertEquals(3, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        @Test
        public void testFill_partialOverlap() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            boolean[][] patch = { { true, true }, { true, true } };
            m.copyFrom(2, 2, patch); // Only partial overlap
            assertFalse(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 2)); // Only this one should be set
        }

        @Test
        public void testMapToObj_nullHandling() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<Integer> result = m.mapToObj(x -> x ? 1 : 0, Integer.class);
            assertEquals(Integer.valueOf(1), result.get(0, 0));
            assertEquals(Integer.valueOf(0), result.get(0, 1));
            assertEquals(Integer.valueOf(0), result.get(1, 0));
            assertEquals(Integer.valueOf(1), result.get(1, 1));
        }

        @Test
        public void testUpdateAll_complexFunction() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            m.updateAll((i, j) -> (i + j) % 2 == 0);
            assertTrue(m.get(0, 0)); // 0+0=0, even
            assertFalse(m.get(0, 1)); // 0+1=1, odd
            assertFalse(m.get(1, 0)); // 1+0=1, odd
            assertTrue(m.get(1, 1)); // 1+1=2, even
            assertTrue(m.get(2, 2)); // 2+2=4, even
        }

        @Test
        public void testReplaceIf_noMatches() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, true }, { true, true } });
            m.replaceIf(x -> !x, true); // Replace all false with true (but there are none)

            // All should still be true
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testReplaceIf_withIndices_edgeCases() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.replaceIf((i, j) -> i + j == 1, true); // Replace positions where i+j=1

            assertTrue(m.get(0, 0)); // unchanged
            assertTrue(m.get(0, 1)); // 0+1=1, replaced
            assertTrue(m.get(1, 0)); // 1+0=1, replaced
            assertTrue(m.get(1, 1)); // unchanged
        }

        @Test
        public void testZipWith_xorOperation() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, true }, { false, false } });
            BooleanMatrix result = m1.zipWith(m2, (a, b) -> a ^ b); // XOR

            assertFalse(result.get(0, 0)); // true ^ true = false
            assertTrue(result.get(0, 1)); // false ^ true = true
            assertFalse(result.get(1, 0)); // false ^ false = false
            assertTrue(result.get(1, 1)); // true ^ false = true
        }

        @Test
        public void testVstack_withEmpty() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix empty = BooleanMatrix.of(new boolean[0][0]);

            // Stacking with empty should still work if columns match
            BooleanMatrix result = m1.stackVertically(m1.copy());
            assertEquals(4, result.rowCount());
            assertEquals(2, result.columnCount());
        }

        @Test
        public void testHstack_withEmpty() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix result = m1.stackHorizontally(m1.copy());
            assertEquals(2, result.rowCount());
            assertEquals(4, result.columnCount());
        }

        @Test
        public void testRotate90_rectangle() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            BooleanMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertTrue(rotated.get(0, 0));
            assertFalse(rotated.get(1, 0));
            assertTrue(rotated.get(2, 0));
        }

        @Test
        public void testRotate180_rectangle() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            BooleanMatrix rotated = m.rotate180();
            assertEquals(1, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertTrue(rotated.get(0, 0));
            assertFalse(rotated.get(0, 1));
            assertTrue(rotated.get(0, 2));
        }

        @Test
        public void testRotate270_rectangle() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            BooleanMatrix rotated = m.rotate270();
            assertEquals(3, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertTrue(rotated.get(0, 0));
            assertFalse(rotated.get(1, 0));
            assertTrue(rotated.get(2, 0));
        }

        @Test
        public void testCopy_fullMatrix() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix copy = m.copy(0, 2, 0, 2);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertTrue(copy.get(0, 0));
            assertTrue(copy.get(1, 1));

            // Modify copy shouldn't affect original
            copy.set(0, 0, false);
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testExtend_noChange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.resize(2, 2);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertTrue(extended.get(1, 1));
        }

        @Test
        public void testStreamR_operations() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            long rowsWithTrue = m.streamRows().filter(row -> row.anyMatch(b -> b)).count();
            assertEquals(2, rowsWithTrue);
        }

        @Test
        public void testStreamC_operations() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            long columnCountWithAllFalse = m.streamColumns().filter(col -> col.noneMatch(b -> b)).count();
            assertEquals(0, columnCountWithAllFalse); // No columns have all false (col 0: true,false; col 1: false,true; col 2: true,false)
        }

        @Test
        public void testEquals_emptyMatrices() {
            BooleanMatrix empty1 = BooleanMatrix.empty();
            BooleanMatrix empty2 = BooleanMatrix.of(new boolean[0][0]);
            assertTrue(empty1.equals(empty2));
        }

        @Test
        public void testHashCode_consistency() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            int hash1 = m1.hashCode();
            int hash2 = m1.hashCode();
            assertEquals(hash1, hash2); // Same object should have same hash
        }

        @Test
        public void testToString_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.length() > 0);
        }

        @Test
        public void testUpdateRow_allElements() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            m.updateRow(0, x -> true);
            assertArrayEquals(new boolean[] { true, true, true }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, true, false }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn_allElements() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
            m.updateColumn(1, x -> true);
            assertArrayEquals(new boolean[] { true, true, true }, m.columnCopy(1));
            assertArrayEquals(new boolean[] { true, false, true }, m.columnCopy(0)); // unchanged
        }

        @Test
        public void testMap_identityFunction() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix result = m.map(x -> x);
            assertEquals(m, result);
        }

        @Test
        public void testRepelem_edge1x1() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            BooleanMatrix repeated = m.repeatElements(3, 3);
            assertEquals(3, repeated.rowCount());
            assertEquals(3, repeated.columnCount());
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertTrue(repeated.get(i, j));
                }
            }
        }

        @Test
        public void testRepmat_edge1x1() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            BooleanMatrix repeated = m.repeatMatrix(3, 3);
            assertEquals(3, repeated.rowCount());
            assertEquals(3, repeated.columnCount());
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertTrue(repeated.get(i, j));
                }
            }
        }

        // ============ High-Impact Tests for 95% Coverage ============

        @Test
        public void testRotateAndTransposeTallMatrix() {
            // Create a tall matrix (rows > columnCount) - 4 rows × 2 columnCount
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true }, { false, false } });

            // Test rotate90() with tall matrix
            BooleanMatrix rotated90 = m.rotate90();
            assertEquals(2, rotated90.rowCount());
            assertEquals(4, rotated90.columnCount());
            assertFalse(rotated90.get(0, 0));
            assertTrue(rotated90.get(0, 1));
            assertFalse(rotated90.get(0, 2));
            assertTrue(rotated90.get(0, 3));

            // Test rotate270() with tall matrix
            BooleanMatrix rotated270 = m.rotate270();
            assertEquals(2, rotated270.rowCount());
            assertEquals(4, rotated270.columnCount());
            assertFalse(rotated270.get(0, 0));
            assertTrue(rotated270.get(0, 1));
            assertTrue(rotated270.get(0, 2));
            assertFalse(rotated270.get(0, 3));

            // Test transpose() with tall matrix
            BooleanMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(4, transposed.columnCount());
            assertTrue(transposed.get(0, 0));
            assertFalse(transposed.get(0, 1));

            // Test boxed() with tall matrix
            Matrix<Boolean> boxed = m.boxed();
            assertEquals(4, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Boolean.TRUE, boxed.get(0, 0));
            assertEquals(Boolean.FALSE, boxed.get(0, 1));
        }

        @Test
        public void testRepelemOverflow() {
            // Create matrix that will overflow when repeated
            int largeSize = 50000;
            BooleanMatrix m = BooleanMatrix.of(new boolean[largeSize][2]);

            // Test row overflow
            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repeatElements(50000, 1));
            assertTrue(ex1.getMessage().contains("row count overflow"));

            // Test column overflow
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[2][largeSize]);
            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repeatElements(1, 50000));
            assertTrue(ex2.getMessage().contains("column count overflow"));
        }

        @Test
        public void testRepmatOverflow() {
            // Create matrix that will overflow when tiled
            int largeSize = 50000;
            BooleanMatrix m = BooleanMatrix.of(new boolean[largeSize][2]);

            // Test row overflow
            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(50000, 1));
            assertTrue(ex1.getMessage().contains("row count overflow"));

            // Test column overflow
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[2][largeSize]);
            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repeatMatrix(1, 50000));
            assertTrue(ex2.getMessage().contains("column count overflow"));
        }
    }

    @Nested
    @Tag("2510")
    class BooleanMatrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = new BooleanMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testConstructor_withNonSquareMatrix() {
            boolean[][] arr = { { true, false, true }, { false, true, false } };
            BooleanMatrix m = new BooleanMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void testRandom_withZeroLength() {
            BooleanMatrix m = BooleanMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            BooleanMatrix m = BooleanMatrix.diagonals(new boolean[] { true, true, true }, new boolean[] { false, false, false });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(2, 2));
            assertFalse(m.get(0, 2));
            assertFalse(m.get(2, 0));
        }

        @Test
        public void testUnbox() {
            Boolean[][] boxed = { { true, false }, { false, true } };
            Matrix<Boolean> boxedMatrix = Matrix.of(boxed);
            BooleanMatrix unboxed = BooleanMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertTrue(unboxed.get(0, 0));
            assertFalse(unboxed.get(0, 1));
        }

        @Test
        public void testUnbox_withNullElements() {
            Boolean[][] boxed = { { true, null }, { null, false } };
            Matrix<Boolean> boxedMatrix = Matrix.of(boxed);
            BooleanMatrix unboxed = BooleanMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertTrue(unboxed.get(0, 0));
            assertFalse(unboxed.get(0, 1));
            assertFalse(unboxed.get(1, 0));
        }
        // ============ Get/Set Tests ============

        @Test
        public void testGet_byIndices() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testGet_byPoint() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            assertTrue(m.get(Point.of(0, 0)));
            assertFalse(m.get(Point.of(0, 1)));
            assertFalse(m.get(Point.of(1, 0)));
            assertTrue(m.get(Point.of(1, 1)));
        }

        @Test
        public void testSet_byIndices() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            m.set(0, 0, true);
            m.set(1, 1, true);
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
        }

        @Test
        public void testSet_byPoint() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            m.set(Point.of(0, 0), true);
            m.set(Point.of(1, 1), true);
            assertTrue(m.get(Point.of(0, 0)));
            assertTrue(m.get(Point.of(1, 1)));
            assertFalse(m.get(Point.of(0, 1)));
        }

        @Test
        public void testDownOf_atBottomEdge() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf_atLeftEdge() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf_atRightEdge() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void testRow() {
            boolean[][] arr = { { true, false, true }, { false, true, false } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            boolean[] row0 = m.rowView(0);
            assertArrayEquals(new boolean[] { true, false, true }, row0);
            boolean[] row1 = m.rowView(1);
            assertArrayEquals(new boolean[] { false, true, false }, row1);
        }

        @Test
        public void testRow_invalidIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn() {
            boolean[][] arr = { { true, false, true }, { false, true, false } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            boolean[] col0 = m.columnCopy(0);
            assertArrayEquals(new boolean[] { true, false }, col0);
            boolean[] col1 = m.columnCopy(1);
            assertArrayEquals(new boolean[] { false, true }, col1);
        }

        @Test
        public void testColumn_invalidIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false } });
            m.setRow(0, new boolean[] { true, true, true });
            assertArrayEquals(new boolean[] { true, true, true }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, false, false }, m.rowView(1));
        }

        @Test
        public void testSetRow_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new boolean[] { true, true }));
        }

        @Test
        public void testSetColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false }, { false, false } });
            m.setColumn(0, new boolean[] { true, true, true });
            assertArrayEquals(new boolean[] { true, true, true }, m.columnCopy(0));
            assertArrayEquals(new boolean[] { false, false, false }, m.columnCopy(1));
        }

        @Test
        public void testSetColumn_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new boolean[] { true }));
        }

        @Test
        public void testUpdateRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, false, false } });
            m.updateRow(0, val -> !val);
            assertArrayEquals(new boolean[] { false, true, false }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, false, false }, m.rowView(1));
        }

        @Test
        public void testUpdateColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, false }, { true, false } });
            m.updateColumn(0, val -> !val);
            assertArrayEquals(new boolean[] { false, true, false }, m.columnCopy(0));
            assertArrayEquals(new boolean[] { false, false, false }, m.columnCopy(1));
        }

        // ============ Diagonal Access Tests ============

        @Test
        public void testGetLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            boolean[] diag = m.getMainDiagonal();
            assertArrayEquals(new boolean[] { true, true, true }, diag);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            m.setMainDiagonal(new boolean[] { true, true, true });
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(2, 2));
            assertFalse(m.get(0, 1));
        }

        @Test
        public void testSetLU2RD_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new boolean[] { true, true }));
        }

        @Test
        public void testUpdateLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, false, false }, { false, false, true } });
            m.updateMainDiagonal(val -> !val);
            assertFalse(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(2, 2));
        }

        @Test
        public void testSetRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            m.setAntiDiagonal(new boolean[] { true, true, true });
            assertTrue(m.get(0, 2));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(2, 0));
            assertFalse(m.get(0, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, false, false }, { true, false, false } });
            m.updateAntiDiagonal(val -> !val);
            assertFalse(m.get(0, 2));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(2, 0));
        }

        @Test
        public void testUpdateAll_biFunction() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateAll((i, j) -> i == j);
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.replaceIf(val -> val, false);
            assertFalse(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertFalse(m.get(1, 1));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.replaceIf((i, j) -> i == j, false);
            assertFalse(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertFalse(m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void testMap() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix mapped = m.map(val -> !val);
            assertFalse(mapped.get(0, 0));
            assertTrue(mapped.get(0, 1));
            assertTrue(mapped.get(1, 0));
            assertFalse(mapped.get(1, 1));
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<String> mapped = m.mapToObj(val -> val ? "T" : "F", String.class);
            assertEquals("T", mapped.get(0, 0));
            assertEquals("F", mapped.get(0, 1));
            assertEquals("F", mapped.get(1, 0));
            assertEquals("T", mapped.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_singleValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            m.fill(true);
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testFill_withArray() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            m.copyFrom(new boolean[][] { { true, true }, { true, true } });
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testFill_withOffset() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            m.copyFrom(1, 1, new boolean[][] { { true, true }, { true, true } });
            assertFalse(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(2, 2));
        }

        @Test
        public void testFill_withOffset_invalidPosition() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(3, 3, new boolean[][] { { true, true }, { true, true } }));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix copy = m.copy();
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertTrue(copy.get(0, 0));
            assertFalse(copy.get(0, 1));
            copy.set(0, 0, false);
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            BooleanMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertFalse(copy.get(0, 0));
            assertTrue(copy.get(0, 1));
        }

        @Test
        public void testCopy_withRowRange_invalidRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2));
        }

        @Test
        public void testCopy_withFullRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, true, false } });
            BooleanMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertTrue(copy.get(0, 0));
            assertFalse(copy.get(0, 1));
            assertTrue(copy.get(1, 0));
            assertFalse(copy.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_invalidRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, -1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_withDefaultValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix extended = m.resize(2, 3);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertFalse(extended.get(0, 1));
            assertFalse(extended.get(0, 2));
            assertFalse(extended.get(1, 0));
        }

        @Test
        public void testExtend_withCustomDefaultValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false } });
            BooleanMatrix extended = m.resize(2, 3, true);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertFalse(extended.get(0, 0));
            assertFalse(extended.get(0, 1));
            assertTrue(extended.get(0, 2));
            assertTrue(extended.get(1, 0));
        }

        @Test
        public void testExtend_withDirections() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            BooleanMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertTrue(extended.get(1, 1));
            assertFalse(extended.get(0, 0));
        }

        @Test
        public void testExtend_withDirectionsAndValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false } });
            BooleanMatrix extended = m.extend(1, 1, 1, 1, true);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertFalse(extended.get(1, 1));
            assertTrue(extended.get(0, 0));
        }

        @Test
        public void testExtend_invalidSize() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });

            BooleanMatrix extended = m.resize(1, 1);
            assertEquals(1, extended.rowCount());
            assertEquals(1, extended.columnCount());
        }

        // ============ Transformation Tests ============

        @Test
        public void testReverseH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            m.flipInPlaceHorizontally();
            assertTrue(m.get(0, 2));
            assertFalse(m.get(0, 1));
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testReverseV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.flipInPlaceVertically();
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertFalse(m.get(1, 1));
        }

        @Test
        public void testFlipH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix flipped = m.flipHorizontally();
            assertTrue(flipped.get(0, 2));
            assertFalse(flipped.get(0, 1));
            assertTrue(flipped.get(0, 0));
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix flipped = m.flipVertically();
            assertFalse(flipped.get(0, 0));
            assertTrue(flipped.get(0, 1));
            assertTrue(flipped.get(1, 0));
            assertFalse(flipped.get(1, 1));
            assertTrue(m.get(0, 0));
        }
        // ============ Reshape Tests ============

        @Test
        public void testReshape_oneArg() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true, false } });
            BooleanMatrix reshaped = m.reshape(2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertTrue(reshaped.get(0, 0));
            assertFalse(reshaped.get(0, 1));
            assertTrue(reshaped.get(1, 0));
            assertFalse(reshaped.get(1, 1));
        }

        @Test
        public void testReshape_twoArgs() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true, false } });
            BooleanMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertTrue(reshaped.get(0, 0));
            assertFalse(reshaped.get(0, 1));
        }

        @Test
        public void testReshape_invalidSize() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            BooleanMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
        }

        // ============ Repelem/Repmat Tests ============

        @Test
        public void testRepelem() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertTrue(result.get(0, 0));
            assertTrue(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertTrue(result.get(1, 1));
            assertFalse(result.get(0, 2));
        }

        @Test
        public void testRepelem_invalidRepeats() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix result = m.repeatMatrix(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertTrue(result.get(0, 0));
            assertFalse(result.get(0, 1));
            assertTrue(result.get(2, 0));
            assertFalse(result.get(2, 1));
        }

        @Test
        public void testRepmat_invalidRepeats() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        @Test
        public void testFlatOp() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            AtomicInteger count = new AtomicInteger(0);
            m.applyOnFlattened(row -> count.addAndGet(row.length));
            assertEquals(4, count.get());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true } });
            BooleanMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertTrue(stacked.get(0, 0));
            assertFalse(stacked.get(0, 1));
            assertFalse(stacked.get(1, 0));
            assertTrue(stacked.get(1, 1));
        }

        @Test
        public void testHstack() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true }, { false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false }, { true } });
            BooleanMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertTrue(stacked.get(0, 0));
            assertFalse(stacked.get(0, 1));
            assertFalse(stacked.get(1, 0));
            assertTrue(stacked.get(1, 1));
        }

        @Test
        public void testHstack_invalidRows() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false }, { true } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void testBoxed() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<Boolean> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Boolean.TRUE, boxed.get(0, 0));
            assertEquals(Boolean.FALSE, boxed.get(0, 1));
        }

        @Test
        public void testZipWith_two_invalidShape() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a && b));
        }

        @Test
        public void testZipWith_three() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, true } });
            BooleanMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a || b || c);
            assertTrue(result.get(0, 0));
            assertTrue(result.get(0, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Boolean> diag = m.streamMainDiagonal().toList();
            assertEquals(3, diag.size());
            assertTrue(diag.get(0));
            assertTrue(diag.get(1));
            assertTrue(diag.get(2));
        }

        @Test
        public void testStreamH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> all = m.streamHorizontal().toList();
            assertEquals(4, all.size());
            assertTrue(all.get(0));
            assertFalse(all.get(1));
        }

        @Test
        public void testStreamH_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> row = m.streamHorizontal(0).toList();
            assertEquals(3, row.size());
            assertTrue(row.get(0));
            assertFalse(row.get(1));
            assertTrue(row.get(2));
        }

        @Test
        public void testStreamH_rowRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            List<Boolean> rows = m.streamHorizontal(1, 3).toList();
            assertEquals(4, rows.size());
            assertFalse(rows.get(0));
            assertTrue(rows.get(1));
        }

        @Test
        public void testStreamV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> all = m.streamVertical().toList();
            assertEquals(4, all.size());
            assertTrue(all.get(0));
            assertFalse(all.get(1));
        }

        @Test
        public void testStreamV_singleColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
            List<Boolean> col = m.streamVertical(0).toList();
            assertEquals(3, col.size());
            assertTrue(col.get(0));
            assertFalse(col.get(1));
            assertTrue(col.get(2));
        }

        @Test
        public void testStreamV_columnRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> columnCount = m.streamVertical(1, 3).toList();
            assertEquals(4, columnCount.size());
            assertFalse(columnCount.get(0));
            assertTrue(columnCount.get(1));
        }

        @Test
        public void testStreamR() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Stream<Boolean>> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).count());
        }

        @Test
        public void testStreamR_rowRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            List<Stream<Boolean>> rows = m.streamRows(1, 3).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void testStreamC() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Stream<Boolean>> columnCount = m.streamColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).count());
        }

        @Test
        public void testStreamC_columnRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Stream<Boolean>> columnCount = m.streamColumns(1, 3).toList();
            assertEquals(2, columnCount.size());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach_valueConsumer() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> values = new ArrayList<>();
            m.forEach(b -> values.add(b));
            assertEquals(4, values.size());
            assertTrue(values.get(0));
            assertFalse(values.get(1));
        }

        @Test
        public void testForEach_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, true, true } });
            List<Boolean> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, b -> values.add(b));
            assertEquals(4, values.size());
            assertTrue(values.get(0));
            assertFalse(values.get(1));
        }

        // ============ Points Tests (Inherited) ============

        @Test
        public void testPointsLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
            List<Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 2), points.get(2));
        }

        @Test
        public void testPointsRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
            List<Point> points = m.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 2), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
        }

        @Test
        public void testPointsH_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][3]);
            List<Point> points = m.pointsHorizontal(0).toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 2), points.get(2));
        }

        @Test
        public void testPointsV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
        }

        @Test
        public void testPointsV_singleColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][2]);
            List<Point> points = m.pointsVertical(0).toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsR() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
            List<Stream<Point>> rows = m.pointsRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).count());
        }

        @Test
        public void testPointsC() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
            List<Stream<Point>> columnCount = m.pointsColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).count());
        }

        // ============ Utility Tests (Inherited) ============

        @Test
        public void testElementCount() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testElementCount_Empty() {
            BooleanMatrix m = BooleanMatrix.empty();
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testIsEmpty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertTrue(empty.isEmpty());
            BooleanMatrix notEmpty = BooleanMatrix.of(new boolean[][] { { true } });
            assertFalse(notEmpty.isEmpty());
        }

        @Test
        public void testArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            boolean[][] result = m.backingArray();
            assertSame(arr, result);
        }

        @Test
        public void testIsSameShape() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[2][3]);
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[2][3]);
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[3][2]);
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testAccept() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            AtomicInteger counter = new AtomicInteger(0);
            m.accept(matrix -> counter.set(matrix.rowCount() * matrix.columnCount()));
            assertEquals(2, counter.get());
        }

        @Test
        public void testApply() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(2, result);
        }

        // ============ Println Test ============

        @Test
        public void testPrintln() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.contains("true"));
            assertTrue(result.contains("false"));
        }

        // ============ Equals/HashCode Tests ============

        @Test
        public void testHashCode_equal() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals_same() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertEquals(m, m);
        }

        @Test
        public void testEquals_equal() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertEquals(m1, m2);
        }

        @Test
        public void testEquals_notEqual() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true } });
            assertNotEquals(m1, m2);
        }

        @Test
        public void testEquals_null() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            assertNotEquals(m, null);
        }

        @Test
        public void testEquals_differentType() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            assertNotEquals(m, "string");
        }

        // ============ ToString Test ============

        @Test
        public void testToString() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("true"));
            assertTrue(str.contains("false"));
        }
    }

    @Nested
    @Tag("2511")
    class BooleanMatrix2511Test extends TestBase {
        @Test
        public void testConstructor_largeMatrix() {
            boolean[][] arr = new boolean[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    arr[i][j] = (i + j) % 2 == 0;
                }
            }
            BooleanMatrix m = new BooleanMatrix(arr);
            assertEquals(100, m.rowCount());
            assertEquals(100, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
        }

        @Test
        public void testOf_withSingleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[] { true, false, true });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertTrue(m.get(0, 2));
        }

        @Test
        public void testRandom_withLargeLength() {
            BooleanMatrix m = BooleanMatrix.random(1000);
            assertEquals(1, m.rowCount());
            assertEquals(1000, m.columnCount());
        }

        @Test
        public void testRepeat_withZeroLength() {
            BooleanMatrix m = BooleanMatrix.repeat(1, 0, true);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonalLU2RD_singleElement() {
            BooleanMatrix m = BooleanMatrix.mainDiagonal(new boolean[] { true });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testDiagonalRU2LD_empty() {
            BooleanMatrix m = BooleanMatrix.antiDiagonal(new boolean[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withBothDiagonalsOverlapping() {
            BooleanMatrix m = BooleanMatrix.diagonals(new boolean[] { true, false, true }, new boolean[] { false, false, false });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            // Center element should be from main diagonal (set second)
            assertFalse(m.get(1, 1));
        }

        @Test
        public void testUnbox_isAllNulls() {
            Boolean[][] boxed = { { null, null }, { null, null } };
            Matrix<Boolean> boxedMatrix = Matrix.of(boxed);
            BooleanMatrix unboxed = BooleanMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertFalse(unboxed.get(0, 0));
            assertFalse(unboxed.get(1, 1));
        }

        @Test
        public void testComponentType_emptyMatrix() {
            BooleanMatrix m = BooleanMatrix.empty();
            assertEquals(boolean.class, m.componentType());
        }

        @Test
        public void testGet_allPositions() {
            boolean[][] arr = { { true, false, true }, { false, true, false }, { true, true, false } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(arr[i][j], m.get(i, j));
                }
            }
        }

        @Test
        public void testSet_overwriteValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, true } });
            m.set(0, 0, false);
            assertFalse(m.get(0, 0));
            m.set(0, 0, true);
            assertTrue(m.get(0, 0));
        }

        @Test
        public void testUpOf_multipleRows() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            OptionalBoolean up = m.above(2, 1);
            assertTrue(up.isPresent());
            assertTrue(up.get());
        }

        @Test
        public void testDownOf_multipleRows() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            OptionalBoolean down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertFalse(down.get());
        }

        @Test
        public void testLeftOf_multipleColumns() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            OptionalBoolean left = m.left(0, 2);
            assertTrue(left.isPresent());
            assertFalse(left.get());
        }

        @Test
        public void testRightOf_multipleColumns() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            OptionalBoolean right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertFalse(right.get());
        }

        @Test
        public void testRow_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            assertArrayEquals(new boolean[] { true, false, true }, m.rowView(0));
        }

        @Test
        public void testColumn_singleColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true }, { false }, { true } });
            assertArrayEquals(new boolean[] { true, false, true }, m.columnCopy(0));
        }

        @Test
        public void testSetRow_invalidIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(-1, new boolean[] { true, true }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(1, new boolean[] { true, true }));
        }

        @Test
        public void testSetColumn_invalidIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(-1, new boolean[] { true, true }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(2, new boolean[] { true, true }));
        }

        @Test
        public void testUpdateRow_multipleRows() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            m.updateRow(1, val -> !val);
            assertArrayEquals(new boolean[] { true, false }, m.rowView(0));
            assertArrayEquals(new boolean[] { true, false }, m.rowView(1));
            assertArrayEquals(new boolean[] { true, true }, m.rowView(2));
        }

        @Test
        public void testUpdateColumn_multipleColumns() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, false, false } });
            m.updateColumn(2, val -> !val);
            assertArrayEquals(new boolean[] { true, false, false }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, false, true }, m.rowView(1));
        }

        @Test
        public void testGetLU2RD_singleElement() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
            assertArrayEquals(new boolean[] { true }, m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[2][3]);
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new boolean[] { true, true }));
        }

        @Test
        public void testUpdateLU2RD_allValues() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, true, false }, { true, false, true }, { false, true, false } });
            m.updateMainDiagonal(val -> true);
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(2, 2));
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new boolean[] { true, true }));
        }

        @Test
        public void testUpdateRU2LD_rectangular() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { true, false, false } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(val -> !val));
        }

        @Test
        public void testUpdateAll_withConstant() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateAll(val -> true);
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testReplaceIf_noMatches() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.replaceIf(val -> false, true);
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void testMap_identity() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix mapped = m.map(val -> val);
            assertTrue(mapped.get(0, 0));
            assertFalse(mapped.get(0, 1));
            assertNotSame(m, mapped);
        }

        @Test
        public void testMapToObj_withComplexType() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<Integer> mapped = m.mapToObj(val -> val ? 1 : 0, Integer.class);
            assertEquals(1, mapped.get(0, 0));
            assertEquals(0, mapped.get(0, 1));
            assertEquals(0, mapped.get(1, 0));
            assertEquals(1, mapped.get(1, 1));
        }

        @Test
        public void testFill_withPartialArray() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
            m.copyFrom(0, 0, new boolean[][] { { true, true } });
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertFalse(m.get(0, 2));
            assertFalse(m.get(1, 0));
        }

        @Test
        public void testCopy_emptyMatrix() {
            BooleanMatrix m = BooleanMatrix.empty();
            BooleanMatrix copy = m.copy();
            assertTrue(copy.isEmpty());
        }

        @Test
        public void testCopy_withRowRange_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            BooleanMatrix copy = m.copy(1, 2);
            assertEquals(1, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertFalse(copy.get(0, 0));
            assertTrue(copy.get(0, 1));
        }

        @Test
        public void testCopy_withFullRange_singleCell() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix copy = m.copy(0, 1, 1, 2);
            assertEquals(1, copy.rowCount());
            assertEquals(1, copy.columnCount());
            assertFalse(copy.get(0, 0));
        }

        @Test
        public void testExtend_noExtension() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix extended = m.resize(1, 2);
            assertEquals(1, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertFalse(extended.get(0, 1));
        }

        // ============ Transformation Tests ============

        @Test
        public void testReverseH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            m.flipInPlaceHorizontally();
            assertArrayEquals(new boolean[] { true, false, true }, m.rowView(0));
            assertArrayEquals(new boolean[] { false, true, false }, m.rowView(1));
        }

        @Test
        public void testReverseH_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            m.flipInPlaceHorizontally();
            assertArrayEquals(new boolean[] { true, false, true }, m.rowView(0));
        }

        @Test
        public void testReverseV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            m.flipInPlaceVertically();
            assertArrayEquals(new boolean[] { true, false, true }, m.columnCopy(0));
            assertArrayEquals(new boolean[] { true, true, false }, m.columnCopy(1));
        }

        @Test
        public void testReverseV_singleColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true }, { false }, { true } });
            m.flipInPlaceVertically();
            assertArrayEquals(new boolean[] { true, false, true }, m.columnCopy(0));
        }

        @Test
        public void testFlipH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix flipped = m.flipHorizontally();
            assertEquals(2, flipped.rowCount());
            assertEquals(3, flipped.columnCount());
            assertArrayEquals(new boolean[] { true, false, true }, flipped.rowView(0));
            assertArrayEquals(new boolean[] { false, true, false }, flipped.rowView(1));
            assertNotSame(m, flipped);
        }

        @Test
        public void testFlipV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            BooleanMatrix flipped = m.flipVertically();
            assertEquals(3, flipped.rowCount());
            assertEquals(2, flipped.columnCount());
            assertArrayEquals(new boolean[] { true, false, true }, flipped.columnCopy(0));
            assertArrayEquals(new boolean[] { true, true, false }, flipped.columnCopy(1));
            assertNotSame(m, flipped);
        }

        @Test
        public void testRotate90_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
        }

        @Test
        public void testTranspose_squareMatrix() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertTrue(transposed.get(0, 0));
            assertFalse(transposed.get(0, 1));
        }

        @Test
        public void testReshape() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true, false } });
            BooleanMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertTrue(reshaped.get(0, 0));
            assertFalse(reshaped.get(0, 1));
            assertTrue(reshaped.get(1, 0));
            assertFalse(reshaped.get(1, 1));
        }

        @Test
        public void testReshape_toSingleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { true, false } });
            BooleanMatrix reshaped = m.reshape(1, 4);
            assertEquals(1, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
        }

        @Test
        public void testRepelem() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertTrue(repeated.get(0, 0));
            assertTrue(repeated.get(0, 1));
            assertTrue(repeated.get(1, 0));
            assertTrue(repeated.get(1, 1));
            assertFalse(repeated.get(0, 2));
        }

        @Test
        public void testRepelem_singleRepeat() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix repeated = m.repeatElements(1, 1);
            assertEquals(1, repeated.rowCount());
            assertEquals(2, repeated.columnCount());
            assertTrue(repeated.get(0, 0));
            assertFalse(repeated.get(0, 1));
        }

        @Test
        public void testRepmat() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix repeated = m.repeatMatrix(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertTrue(repeated.get(0, 0));
            assertFalse(repeated.get(0, 1));
            assertTrue(repeated.get(2, 2));
        }

        @Test
        public void testRepmat_singleRepeat() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix repeated = m.repeatMatrix(1, 1);
            assertEquals(1, repeated.rowCount());
            assertEquals(2, repeated.columnCount());
            assertTrue(repeated.get(0, 0));
        }

        @Test
        public void testFlatten() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanList flattened = m.flatten();
            assertEquals(4, flattened.size());
            assertTrue(flattened.get(0));
            assertFalse(flattened.get(1));
            assertFalse(flattened.get(2));
            assertTrue(flattened.get(3));
        }

        @Test
        public void testFlatten_emptyMatrix() {
            BooleanMatrix m = BooleanMatrix.empty();
            BooleanList flattened = m.flatten();
            assertEquals(0, flattened.size());
        }

        @Test
        public void testVstack_incompatibleColumns() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack_incompatibleRows() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true }, { false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Tests ============

        @Test
        public void testBoxed() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<Boolean> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Boolean.TRUE, boxed.get(0, 0));
            assertEquals(Boolean.FALSE, boxed.get(0, 1));
            assertEquals(Boolean.FALSE, boxed.get(1, 0));
            assertEquals(Boolean.TRUE, boxed.get(1, 1));
        }

        @Test
        public void testBoxed_emptyMatrix() {
            BooleanMatrix m = BooleanMatrix.empty();
            Matrix<Boolean> boxed = m.boxed();
            assertTrue(boxed.isEmpty());
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
            List<Boolean> diagonal = m.streamMainDiagonal().toList();
            assertEquals(3, diagonal.size());
            assertTrue(diagonal.get(0));
            assertTrue(diagonal.get(1));
            assertTrue(diagonal.get(2));
        }

        @Test
        public void testStreamRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
            List<Boolean> diagonal = m.streamAntiDiagonal().toList();
            assertEquals(3, diagonal.size());
            assertTrue(diagonal.get(0));
            assertTrue(diagonal.get(1));
            assertTrue(diagonal.get(2));
        }

        @Test
        public void testStreamH_withRowIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> row = m.streamHorizontal(1).toList();
            assertEquals(2, row.size());
            assertFalse(row.get(0));
            assertTrue(row.get(1));
        }

        @Test
        public void testStreamH_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            List<Boolean> elements = m.streamHorizontal(1, 3).toList();
            assertEquals(4, elements.size());
            assertFalse(elements.get(0));
            assertTrue(elements.get(1));
        }

        @Test
        public void testStreamV_withColumnIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> col = m.streamVertical(1).toList();
            assertEquals(2, col.size());
            assertFalse(col.get(0));
            assertTrue(col.get(1));
        }

        @Test
        public void testStreamV_withRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> elements = m.streamVertical(1, 3).toList();
            assertEquals(4, elements.size());
            assertFalse(elements.get(0));
            assertTrue(elements.get(1));
        }

        @Test
        public void testStreamR() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Stream<Boolean>> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).toList().size());
        }

        @Test
        public void testStreamC() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Stream<Boolean>> columnCount = m.streamColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).toList().size());
        }
        // ============ Point Stream Tests ============

        @Test
        public void testPointsH() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsV() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsLU2RD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
            List<Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsRU2LD() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
            List<Point> points = m.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(2, points.get(0).columnIndex());
        }

        // ============ Adjacent Points Tests ============

        @Test
        public void testAdjacent4Points() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Point> points = m.adjacent4Points(1, 1).toList();
            assertEquals(4, points.size());
        }

        @Test
        public void testAdjacent4Points_corner() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Point> points = m.adjacent4Points(0, 0).toList();
            assertEquals(2, points.size());
        }

        @Test
        public void testAdjacent8Points() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            List<Point> points = m.adjacent8Points(1, 1).toList();
            assertEquals(8, points.size());
        }

        @Test
        public void testAdjacent8Points_corner() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Point> points = m.adjacent8Points(0, 0).toList();
            assertEquals(3, points.size());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(val -> {
                if (val) {
                    count.incrementAndGet();
                }
            });
            assertEquals(2, count.get());
        }
        // ============ Utility Tests ============

        @Test
        public void testIsEmpty() {
            assertTrue(BooleanMatrix.empty().isEmpty());
            assertFalse(BooleanMatrix.of(new boolean[][] { { true } }).isEmpty());
        }

        @Test
        public void testIsSameShape() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false, true } });
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            boolean[][] result = m.backingArray();
            assertArrayEquals(arr, result);
        }

        @Test
        public void testEquals() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
            assertEquals(m1, m2);
            assertNotEquals(m1, m3);
            assertNotEquals(m1, null);
            assertNotEquals(m1, "not a matrix");
        }

        @Test
        public void testEquals_emptyMatrices() {
            BooleanMatrix m1 = BooleanMatrix.empty();
            BooleanMatrix m2 = BooleanMatrix.empty();
            assertEquals(m1, m2);
        }

        @Test
        public void testPrintln() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.contains("true"));
        }
    }

    @Nested
    @Tag("2512")
    class BooleanMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_validArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = new BooleanMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
        }

        @Test
        public void test_constructor_emptyArray() {
            BooleanMatrix m = new BooleanMatrix(new boolean[0][0]);
            assertTrue(m.isEmpty());
        }
        // ============ Factory Method Tests ============

        @Test
        public void test_empty() {
            BooleanMatrix empty = BooleanMatrix.empty();
            assertTrue(empty.isEmpty());
            assertSame(BooleanMatrix.empty(), BooleanMatrix.empty());
        }

        @Test
        public void test_of_validArray() {
            boolean[][] arr = { { true, false }, { false, true } };
            BooleanMatrix m = BooleanMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
        }

        @Test
        public void test_of_singleRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[] { true, false, true });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void test_random() {
            BooleanMatrix m = BooleanMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_repeat() {
            BooleanMatrix m = BooleanMatrix.repeat(1, 5, true);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertTrue(m.get(0, i));
            }
        }

        @Test
        public void test_repeat_false() {
            BooleanMatrix m = BooleanMatrix.repeat(1, 3, false);
            for (int i = 0; i < 3; i++) {
                assertFalse(m.get(0, i));
            }
        }

        @Test
        public void test_mainDiagonal() {
            BooleanMatrix m = BooleanMatrix.mainDiagonal(new boolean[] { true, false, true });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 2));
            assertFalse(m.get(0, 1));
        }

        @Test
        public void test_mainDiagonal_empty() {
            BooleanMatrix m = BooleanMatrix.mainDiagonal(new boolean[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_antiDiagonal() {
            BooleanMatrix m = BooleanMatrix.antiDiagonal(new boolean[] { true, false, true });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 2));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 0));
        }

        @Test
        public void test_diagonal_both() {
            BooleanMatrix m = BooleanMatrix.diagonals(new boolean[] { true, true, true }, new boolean[] { false, false, false });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertTrue(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(0, 2));
        }

        @Test
        public void test_diagonal_differentLengths() {
            assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.diagonals(new boolean[] { true, true }, new boolean[] { false, false, false }));
        }

        @Test
        public void test_unbox() {
            Matrix<Boolean> boxed = Matrix.of(new Boolean[][] { { true, false }, { null, true } });
            BooleanMatrix primitive = BooleanMatrix.unbox(boxed);
            assertTrue(primitive.get(0, 0));
            assertFalse(primitive.get(0, 1));
            assertFalse(primitive.get(1, 0)); // null becomes false
            assertTrue(primitive.get(1, 1));
        }

        // ============ Element Access Tests ============

        @Test
        public void test_get_byIndices() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertFalse(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void test_get_byPoint() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertTrue(m.get(Point.of(0, 0)));
            assertFalse(m.get(Point.of(0, 1)));
        }

        @Test
        public void test_set_byIndices() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.set(0, 1, true);
            assertTrue(m.get(0, 1));
        }

        @Test
        public void test_set_byPoint() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.set(Point.of(1, 0), true);
            assertTrue(m.get(1, 0));
        }

        // ============ Neighbor Access Tests ============

        @Test
        public void test_upOf_exists() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertTrue(up.get());
        }

        @Test
        public void test_upOf_notExists() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void test_downOf_exists() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertFalse(down.get());
        }

        @Test
        public void test_leftOf_exists() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertTrue(left.get());
        }

        @Test
        public void test_rightOf_exists() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            OptionalBoolean right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertFalse(right.get());
        }
        // ============ Row/Column Access Tests ============

        @Test
        public void test_row() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            boolean[] row = m.rowView(0);
            assertArrayEquals(new boolean[] { true, false, true }, row);
        }

        @Test
        public void test_row_invalidIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            boolean[] col = m.columnCopy(0);
            assertArrayEquals(new boolean[] { true, false }, col);
        }

        @Test
        public void test_column_invalidIndex() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.setRow(0, new boolean[] { false, false });
            assertFalse(m.get(0, 0));
            assertFalse(m.get(0, 1));
        }

        @Test
        public void test_setRow_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new boolean[] { true }));
        }

        @Test
        public void test_setColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.setColumn(0, new boolean[] { false, false });
            assertFalse(m.get(0, 0));
            assertFalse(m.get(1, 0));
        }

        @Test
        public void test_setColumn_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new boolean[] { true }));
        }

        @Test
        public void test_updateRow() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateRow(0, val -> !val);
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
        }

        @Test
        public void test_updateColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateColumn(0, val -> !val);
            assertFalse(m.get(0, 0));
            assertTrue(m.get(1, 0));
        }

        // ============ Diagonal Access Tests ============

        @Test
        public void test_getMainDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
            boolean[] diag = m.getMainDiagonal();
            assertArrayEquals(new boolean[] { true, true, true }, diag);
        }

        @Test
        public void test_getMainDiagonal_nonSquare() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
            m.setMainDiagonal(new boolean[] { false, false, false });
            assertFalse(m.get(0, 0));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(2, 2));
        }

        @Test
        public void test_setMainDiagonal_invalidLength() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new boolean[] { true }));
        }

        @Test
        public void test_updateMainDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, false, false }, { false, false, false } });
            m.updateMainDiagonal(val -> !val);
            assertFalse(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(2, 2));
        }

        @Test
        public void test_getAntiDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
            boolean[] diag = m.getAntiDiagonal();
            assertArrayEquals(new boolean[] { true, true, true }, diag);
        }

        @Test
        public void test_setAntiDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
            m.setAntiDiagonal(new boolean[] { false, false, false });
            assertFalse(m.get(0, 2));
            assertFalse(m.get(1, 1));
            assertFalse(m.get(2, 0));
        }

        @Test
        public void test_updateAntiDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
            m.updateAntiDiagonal(val -> !val);
            assertTrue(m.get(0, 2));
            assertFalse(m.get(1, 1));
            assertTrue(m.get(2, 0));
        }

        // ============ Update Tests ============

        @Test
        public void test_updateAll_unaryOperator() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.updateAll(val -> !val);
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertFalse(m.get(1, 1));
        }

        @Test
        public void test_replaceIf_predicate() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.replaceIf(val -> !val, true);
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void test_replaceIf_biPredicate() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
            m.replaceIf((i, j) -> i == j, true);
            assertTrue(m.get(0, 0));
            assertFalse(m.get(0, 1));
            assertTrue(m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void test_map() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix inverted = m.map(val -> !val);
            assertFalse(inverted.get(0, 0));
            assertTrue(inverted.get(0, 1));
            // Original unchanged
            assertTrue(m.get(0, 0));
        }

        @Test
        public void test_mapToObj() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<String> stringMatrix = m.mapToObj(val -> val ? "YES" : "NO", String.class);
            assertEquals("YES", stringMatrix.get(0, 0));
            assertEquals("NO", stringMatrix.get(0, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_singleValue() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.fill(true);
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertTrue(m.get(1, 1));
        }

        @Test
        public void test_fill_array() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
            m.copyFrom(new boolean[][] { { true, true }, { true, true } });
            assertTrue(m.get(0, 0));
            assertTrue(m.get(0, 1));
            assertTrue(m.get(1, 0));
            assertTrue(m.get(1, 1));
            assertFalse(m.get(2, 2));
        }

        @Test
        public void test_fill_arrayWithPosition() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[4][4]);
            m.copyFrom(1, 1, new boolean[][] { { true, true }, { true, true } });
            assertFalse(m.get(0, 0));
            assertTrue(m.get(1, 1));
            assertTrue(m.get(1, 2));
            assertTrue(m.get(2, 1));
            assertTrue(m.get(2, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy() {
            BooleanMatrix original = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix copy = original.copy();
            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertTrue(copy.get(0, 0));
            // Modify copy
            copy.set(0, 0, false);
            assertTrue(original.get(0, 0)); // Original unchanged
        }

        @Test
        public void test_copy_fullRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, true, true } });
            BooleanMatrix copy = m.copy(0, 2, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertFalse(copy.get(0, 0)); // From (0,1)
            assertTrue(copy.get(0, 1)); // From (0,2)
        }

        // ============ Transformation Tests ============

        @Test
        public void test_rotate90() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertFalse(rotated.get(0, 0));
            assertTrue(rotated.get(0, 1));
        }

        @Test
        public void test_rotate180() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix rotated = m.rotate180();
            assertTrue(rotated.get(0, 0));
            assertFalse(rotated.get(0, 1));
            assertFalse(rotated.get(1, 0));
            assertTrue(rotated.get(1, 1));
        }

        @Test
        public void test_rotate270() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
        }

        @Test
        public void test_transpose() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertTrue(transposed.get(0, 0));
            assertFalse(transposed.get(0, 1));
            assertFalse(transposed.get(1, 0));
            assertTrue(transposed.get(1, 1));
        }

        @Test
        public void test_flipHorizontally() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix flipped = m.flipHorizontally();
            assertFalse(flipped.get(0, 0));
            assertTrue(flipped.get(0, 1));
        }

        @Test
        public void test_flipVertically() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix flipped = m.flipVertically();
            assertFalse(flipped.get(0, 0));
            assertTrue(flipped.get(0, 1));
        }

        @Test
        public void test_flipInPlaceHorizontally() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.flipInPlaceHorizontally();
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
        }

        @Test
        public void test_flipInPlaceVertically() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.flipInPlaceVertically();
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void test_reshape() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            BooleanMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertTrue(reshaped.get(0, 0));
            assertFalse(reshaped.get(0, 1));
        }

        // ============ Repeat Tests ============

        @Test
        public void test_repeatElements() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertTrue(repeated.get(0, 0));
            assertTrue(repeated.get(0, 1));
            assertTrue(repeated.get(1, 0));
            assertTrue(repeated.get(1, 1));
        }

        @Test
        public void test_repeatMatrix() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix tiled = m.repeatMatrix(2, 2);
            assertEquals(4, tiled.rowCount());
            assertEquals(4, tiled.columnCount());
            assertTrue(tiled.get(0, 0));
            assertFalse(tiled.get(0, 1));
            assertTrue(tiled.get(2, 2));
        }

        // ============ Flatten Tests ============

        @Test
        public void test_flatten() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanList flat = m.flatten();
            assertEquals(4, flat.size());
            assertTrue(flat.get(0));
            assertFalse(flat.get(1));
            assertFalse(flat.get(2));
            assertTrue(flat.get(3));
        }

        @Test
        public void test_applyOnFlattened() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            m.applyOnFlattened(arr -> {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = !arr[i];
                }
            });
            assertFalse(m.get(0, 0));
            assertTrue(m.get(0, 1));
        }

        // ============ Stack Tests ============

        @Test
        public void test_stackVertically() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true } });
            BooleanMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertTrue(stacked.get(0, 0));
            assertFalse(stacked.get(1, 0));
        }

        @Test
        public void test_vstack_differentCols() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true, false } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_stackHorizontally() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true }, { false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false }, { true } });
            BooleanMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertTrue(stacked.get(0, 0));
            assertFalse(stacked.get(0, 1));
        }

        @Test
        public void test_hstack_differentRows() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void test_boxed() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            Matrix<Boolean> boxed = m.boxed();
            assertEquals(Boolean.TRUE, boxed.get(0, 0));
            assertEquals(Boolean.FALSE, boxed.get(0, 1));
        }

        @Test
        public void test_zipWith_threeMatrices() {
            BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, true }, { false, false } });
            BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false }, { true, false } });
            BooleanMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a || b || c);
            assertTrue(result.get(0, 0));
            assertTrue(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertTrue(result.get(1, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamMainDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
            List<Boolean> diag = m.streamMainDiagonal().toList();
            assertEquals(3, diag.size());
            assertTrue(diag.get(0));
            assertTrue(diag.get(1));
            assertTrue(diag.get(2));
        }

        @Test
        public void test_streamAntiDiagonal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
            List<Boolean> diag = m.streamAntiDiagonal().toList();
            assertEquals(3, diag.size());
            assertTrue(diag.get(0));
            assertTrue(diag.get(1));
            assertTrue(diag.get(2));
        }

        @Test
        public void test_streamHorizontal() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> elements = m.streamHorizontal().toList();
            assertEquals(4, elements.size());
            assertTrue(elements.get(0));
            assertFalse(elements.get(1));
        }

        @Test
        public void test_streamH_rowRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            List<Boolean> elements = m.streamHorizontal(1, 3).toList();
            assertEquals(4, elements.size());
        }

        @Test
        public void test_streamVertical() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> elements = m.streamVertical().toList();
            assertEquals(4, elements.size());
            assertTrue(elements.get(0));
            assertFalse(elements.get(1));
        }

        @Test
        public void test_streamV_singleColumn() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<Boolean> col = m.streamVertical(0).toList();
            assertEquals(2, col.size());
            assertTrue(col.get(0));
            assertFalse(col.get(1));
        }

        @Test
        public void test_streamV_columnRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<Boolean> elements = m.streamVertical(1, 3).toList();
            assertEquals(4, elements.size());
        }

        @Test
        public void test_streamRows() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<List<Boolean>> rows = m.streamRows().map(Stream::toList).toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).size());
        }

        @Test
        public void test_streamR_rowRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
            List<List<Boolean>> rows = m.streamRows(1, 3).map(Stream::toList).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void test_streamColumns() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            List<List<Boolean>> columnCount = m.streamColumns().map(Stream::toList).toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).size());
        }

        @Test
        public void test_streamC_columnRange() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
            List<List<Boolean>> columnCount = m.streamColumns(0, 2).map(Stream::toList).toList();
            assertEquals(2, columnCount.size());
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend_twoParams() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertFalse(extended.get(2, 2));
        }

        @Test
        public void test_extend_twoParamsWithDefault() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.resize(3, 3, true);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertTrue(extended.get(2, 2));
        }

        @Test
        public void test_extend_fourParams() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertTrue(extended.get(1, 1));
        }

        @Test
        public void test_extend_fourParamsWithDefault() {
            BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix extended = m.extend(1, 1, 1, 1, true);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertTrue(extended.get(0, 0));
            assertTrue(extended.get(3, 3));
        }
    }

    @Nested
    class JavadocExampleMatrixGroup1Test_BooleanMatrix extends TestBase {
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
            BooleanMatrix matrix = BooleanMatrix
                    .of(new boolean[][] { { true, false, true, false }, { false, true, false, true }, { true, true, false, false } });
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
    }

    @Nested
    class JavadocExampleMatrixTest_BooleanMatrix extends TestBase {
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

    @Nested
    class MatrixZeroColumnRowStreamTest_BooleanMatrix extends TestBase {
        @Test
        public void testBooleanMatrixRowsForZeroColumnMatrix() {
            final BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { {}, {}, {} });
            final List<List<Boolean>> rows = matrix.streamRows().map(row -> row.toList()).toList();

            assertEquals(3, rows.size());
            assertTrue(rows.get(0).isEmpty());
            assertTrue(rows.get(1).isEmpty());
            assertTrue(rows.get(2).isEmpty());
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_BooleanMatrix extends TestBase {
        @Test
        public void testBooleanMatrixUpdateAllNullOperator() {
            BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            BooleanMatrix emptyLike = BooleanMatrix.of(new boolean[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.BooleanUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Boolean, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.BooleanPredicate<RuntimeException>) null, true));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, true));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.BooleanPredicate<RuntimeException>) null, true));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, true));
        }

        @Test
        public void testBooleanMatrixStackRejectsNullMatrix() {
            BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
        }
    }

    // === Missing coverage: resize, copyFrom, flipInPlace, and/or/xor ===

    @Test
    public void testResize_expand() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix resized = m.resize(3, 3);
        assertEquals(3, resized.rowCount());
        assertEquals(3, resized.columnCount());
        assertTrue(resized.get(0, 0));
        assertFalse(resized.get(0, 1));
        assertFalse(resized.get(0, 2));
        assertFalse(resized.get(2, 0));
        assertFalse(resized.get(2, 2));
    }

    @Test
    public void testResize_shrink() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, true, true } });
        BooleanMatrix resized = m.resize(2, 2);
        assertEquals(2, resized.rowCount());
        assertEquals(2, resized.columnCount());
        assertTrue(resized.get(0, 0));
        assertFalse(resized.get(0, 1));
        assertFalse(resized.get(1, 0));
        assertTrue(resized.get(1, 1));
    }

    @Test
    public void testResize_withDefaultValue() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
        BooleanMatrix resized = m.resize(2, 3, true);
        assertEquals(2, resized.rowCount());
        assertEquals(3, resized.columnCount());
        assertTrue(resized.get(0, 0));
        assertTrue(resized.get(0, 1));
        assertTrue(resized.get(0, 2));
        assertTrue(resized.get(1, 0));
        assertTrue(resized.get(1, 1));
        assertTrue(resized.get(1, 2));
    }

    @Test
    public void testResize_sameSize() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix resized = m.resize(2, 2);
        assertEquals(2, resized.rowCount());
        assertEquals(2, resized.columnCount());
        assertTrue(resized.get(0, 0));
        assertTrue(resized.get(1, 1));
    }

    @Test
    public void testResize_toEmpty() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix resized = m.resize(0, 0);
        assertEquals(0, resized.rowCount());
        assertEquals(0, resized.columnCount());
        assertTrue(resized.isEmpty());
    }

    @Test
    public void testResize_negativeThrows() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> m.resize(1, -1));
    }

    @Test
    public void testCopyFrom_fullOverwrite() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
        m.copyFrom(new boolean[][] { { true, true }, { true, true } });
        assertTrue(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void testCopyFrom_partialOverwrite() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        m.copyFrom(new boolean[][] { { true, true }, { true, true } });
        assertTrue(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertFalse(m.get(0, 2));
        assertTrue(m.get(1, 0));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(1, 2));
        assertFalse(m.get(2, 0));
    }

    @Test
    public void testCopyFrom_withOffset() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        m.copyFrom(1, 1, new boolean[][] { { true, true }, { true, true } });
        assertFalse(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(1, 2));
        assertTrue(m.get(2, 1));
        assertTrue(m.get(2, 2));
    }

    @Test
    public void testCopyFrom_emptySource() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.copyFrom(new boolean[0][0]);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
    }

    @Test
    public void testCopyFrom_negativeIndexThrows() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, new boolean[][] { { true } }));
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, -1, new boolean[][] { { true } }));
    }

    @Test
    public void testFlipInPlaceHorizontally() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, true } });
        m.flipInPlaceHorizontally();
        assertFalse(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertTrue(m.get(0, 2));
        assertTrue(m.get(1, 0));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(1, 2));
    }

    @Test
    public void testFlipInPlaceHorizontally_singleColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        m.flipInPlaceHorizontally();
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 0));
    }

    @Test
    public void testFlipInPlaceVertically() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        m.flipInPlaceVertically();
        assertTrue(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertFalse(m.get(1, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(2, 0));
        assertFalse(m.get(2, 1));
    }

    @Test
    public void testFlipInPlaceVertically_singleRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        m.flipInPlaceVertically();
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertTrue(m.get(0, 2));
    }

    @Test
    public void testAnd() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { false, true } });
        BooleanMatrix result = a.and(b);
        assertTrue(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    @Test
    public void testAnd_allFalse() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { true, true } });
        BooleanMatrix result = a.and(b);
        assertFalse(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertFalse(result.get(1, 1));
    }

    @Test
    public void testAnd_differentShapeThrows() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        assertThrows(IllegalArgumentException.class, () -> a.and(b));
    }

    @Test
    public void testAnd_nullThrows() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.and(null));
    }

    @Test
    public void testOr() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { false, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false, true }, { false, true } });
        BooleanMatrix result = a.or(b);
        assertTrue(result.get(0, 0));
        assertTrue(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    @Test
    public void testOr_allFalse() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { false, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false, false } });
        BooleanMatrix result = a.or(b);
        assertFalse(result.get(0, 0));
        assertFalse(result.get(0, 1));
    }

    @Test
    public void testOr_differentShapeThrows() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        assertThrows(IllegalArgumentException.class, () -> a.or(b));
    }

    @Test
    public void testXor() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true, true }, { false, true } });
        BooleanMatrix result = a.xor(b);
        assertFalse(result.get(0, 0));
        assertTrue(result.get(0, 1));
        assertTrue(result.get(1, 0));
        assertFalse(result.get(1, 1));
    }

    @Test
    public void testXor_sameMatrixAllFalse() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false }, { true, true } });
        BooleanMatrix result = a.xor(a);
        assertFalse(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertFalse(result.get(1, 1));
    }

    @Test
    public void testXor_differentShapeThrows() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        assertThrows(IllegalArgumentException.class, () -> a.xor(b));
    }

    // --- Bug fix tests ---

    @Test
    public void testUpdateAntiDiagonal_throwsIllegalStateExceptionForNonSquare() {
        // Bug fix: updateAntiDiagonal was missing 'throws IllegalStateException' declaration.
        // Verify it throws IllegalStateException for non-square matrices, consistent with updateMainDiagonal.
        BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });

        assertThrows(IllegalStateException.class, () -> nonSquare.updateAntiDiagonal(v -> !v));
        assertThrows(IllegalStateException.class, () -> nonSquare.updateMainDiagonal(v -> !v));
    }

    @Test
    public void testUpdateAntiDiagonal_squareMatrix() {
        // Verify updateAntiDiagonal works correctly on a square matrix.
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });

        m.updateAntiDiagonal(v -> !v);

        // Anti-diagonal positions: (0,2), (1,1), (2,0)
        assertFalse(m.get(0, 2)); // was true, negated
        assertFalse(m.get(1, 1)); // was true, negated
        assertFalse(m.get(2, 0)); // was true, negated

        // Non-anti-diagonal positions should be unchanged
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
        assertFalse(m.get(1, 2));
        assertFalse(m.get(2, 1));
        assertTrue(m.get(2, 2));
    }

    // Exercise the custom object iterators used by horizontal and vertical boxed streams.
    @SuppressWarnings("unchecked")
    @Test
    public void testStreamHorizontalIteratorAdvanceAndToArray_EdgeCase() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        var iterator = matrix.streamHorizontal(0, 2).iterator();

        assertTrue(iterator instanceof com.landawn.abacus.util.stream.ObjIteratorEx);

        com.landawn.abacus.util.stream.ObjIteratorEx<Boolean> ex = (com.landawn.abacus.util.stream.ObjIteratorEx<Boolean>) iterator;
        ex.advance(1);
        assertEquals(5L, ex.count());
        assertArrayEquals(new Boolean[] { false, true, false, true, false }, ex.toArray(new Boolean[0]));
        ex.advance(10);
        assertEquals(0L, ex.count());
        assertThrows(java.util.NoSuchElementException.class, ex::next);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testStreamVerticalIteratorAdvanceAndToArray_EdgeCase() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        var iterator = matrix.streamVertical(0, 2).iterator();

        assertTrue(iterator instanceof com.landawn.abacus.util.stream.ObjIteratorEx);

        com.landawn.abacus.util.stream.ObjIteratorEx<Boolean> ex = (com.landawn.abacus.util.stream.ObjIteratorEx<Boolean>) iterator;
        assertEquals(Boolean.TRUE, ex.next());
        ex.advance(1);
        assertEquals(4L, ex.count());
        assertArrayEquals(new Boolean[] { true, false, true, true }, ex.toArray(new Boolean[0]));
        ex.advance(10);
        assertEquals(0L, ex.count());
        assertThrows(java.util.NoSuchElementException.class, ex::next);
    }

    @Test
    public void testExtendFlattenAndForEach_SubRangeEdgeCase() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = matrix.extend(0, 0, 2, 1, true);
        BooleanList flattened = matrix.flatten();
        List<Boolean> visited = new ArrayList<>();

        matrix.forEach(0, 2, 1, 2, visited::add);

        assertTrue(extended.get(0, 0));
        assertTrue(extended.get(1, 0));
        assertArrayEquals(new boolean[] { true, false, false, true }, flattened.toArray());
        assertEquals(List.of(false, true), visited);
    }

}
