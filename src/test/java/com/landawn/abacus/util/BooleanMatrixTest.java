package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;

public class BooleanMatrixTest extends TestBase {

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
        BooleanMatrix matrix = BooleanMatrix.diagonalLU2RD(diagonal);
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
        BooleanMatrix matrix = BooleanMatrix.diagonalRU2LD(diagonal);
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
        BooleanMatrix matrix = BooleanMatrix.diagonal(mainDiag, antiDiag);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());

        // Test with different lengths
        assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.diagonal(new boolean[] { true }, new boolean[] { true, false }));
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

        assertTrue(matrix.upOf(0, 0).isEmpty());
        assertEquals(true, matrix.upOf(1, 0).orElse(false));
    }

    @Test
    public void testDownOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertEquals(false, matrix.downOf(0, 0).orElse(true));
        assertTrue(matrix.downOf(1, 0).isEmpty());
    }

    @Test
    public void testLeftOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertTrue(matrix.leftOf(0, 0).isEmpty());
        assertEquals(true, matrix.leftOf(0, 1).orElse(false));
    }

    @Test
    public void testRightOf() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        assertEquals(false, matrix.rightOf(0, 0).orElse(true));
        assertTrue(matrix.rightOf(0, 1).isEmpty());
    }

    @Test
    public void testRow() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] row = matrix.row(0);
        assertEquals(2, row.length);
        assertTrue(row[0]);
        assertFalse(row[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.row(2));
    }

    @Test
    public void testColumn() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] col = matrix.column(0);
        assertEquals(2, col.length);
        assertTrue(col[0]);
        assertFalse(col[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.column(2));
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
    public void testGetLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] diag = matrix.getLU2RD();
        assertEquals(2, diag.length);
        assertTrue(diag[0]);
        assertTrue(diag[1]);

        // Test non-square matrix
        BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.setLU2RD(new boolean[] { false, false });
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(1, 1));

        assertThrows(IllegalArgumentException.class, () -> matrix.setLU2RD(new boolean[] { true }));
    }

    @Test
    public void testUpdateLU2RD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateLU2RD(val -> !val);
        assertFalse(matrix.get(0, 0));
        assertFalse(matrix.get(1, 1));
    }

    @Test
    public void testGetRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        boolean[] diag = matrix.getRU2LD();
        assertEquals(2, diag.length);
        assertFalse(diag[0]);
        assertFalse(diag[1]);
    }

    @Test
    public void testSetRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.setRU2LD(new boolean[] { true, true });
        assertTrue(matrix.get(0, 1));
        assertTrue(matrix.get(1, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.updateRU2LD(val -> !val);
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
        matrix.fill(fillArr);
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
        matrix.fill(0, 1, fillArr);
        assertFalse(matrix.get(0, 1));
        assertFalse(matrix.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.fill(-1, 0, fillArr));
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

        BooleanMatrix extended = matrix.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertFalse(extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix extended = matrix.extend(3, 3, true);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertTrue(extended.get(2, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 3, true));
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

        matrix.reverseH();
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(0, 1));
    }

    @Test
    public void testReverseV() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        matrix.reverseV();
        assertFalse(matrix.get(0, 0));
        assertTrue(matrix.get(1, 0));
    }

    @Test
    public void testFlipH() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix flipped = matrix.flipH();
        assertFalse(flipped.get(0, 0));
        assertTrue(flipped.get(0, 1));

        // Original unchanged
        assertTrue(matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix flipped = matrix.flipV();
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
    }

    @Test
    public void testRepelem() {
        boolean[][] arr = { { true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix repeated = matrix.repelem(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertTrue(repeated.get(0, 0));
        assertTrue(repeated.get(0, 1));
        assertTrue(repeated.get(0, 2));
        assertFalse(repeated.get(0, 3));

        assertThrows(IllegalArgumentException.class, () -> matrix.repelem(0, 1));
    }

    @Test
    public void testRepmat() {
        boolean[][] arr = { { true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        BooleanMatrix tiled = matrix.repmat(2, 3);
        assertEquals(2, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertTrue(tiled.get(0, 0));
        assertFalse(tiled.get(0, 1));
        assertTrue(tiled.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.repmat(0, 1));
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
        matrix.flatOp(array -> count[0] += array.length);
        assertEquals(4, count[0]);
    }

    @Test
    public void testVstack() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false, true } });

        BooleanMatrix stacked = a.vstack(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(1, 0));

        BooleanMatrix c = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.vstack(c));
    }

    @Test
    public void testHstack() {
        BooleanMatrix a = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        BooleanMatrix b = BooleanMatrix.of(new boolean[][] { { false }, { true } });

        BooleanMatrix stacked = a.hstack(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(0, 1));

        BooleanMatrix c = BooleanMatrix.of(new boolean[][] { { true } });
        assertThrows(IllegalArgumentException.class, () -> a.hstack(c));
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

        List<Boolean> diagonal = matrix.streamLU2RD().toList();
        assertEquals(2, diagonal.size());
        assertTrue(diagonal.get(0));
        assertTrue(diagonal.get(1));

        BooleanMatrix empty = BooleanMatrix.empty();
        assertTrue(empty.streamLU2RD().toList().isEmpty());
    }

    @Test
    public void testStreamRU2LD() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> diagonal = matrix.streamRU2LD().toList();
        assertEquals(2, diagonal.size());
        assertFalse(diagonal.get(0));
        assertFalse(diagonal.get(1));
    }

    @Test
    public void testStreamH() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamH().toList();
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

        List<Boolean> row = matrix.streamH(0).toList();
        assertEquals(2, row.size());
        assertTrue(row.get(0));
        assertFalse(row.get(1));
    }

    @Test
    public void testStreamHWithRange() {
        boolean[][] arr = { { true, false }, { false, true }, { true, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamH(1, 3).toList();
        assertEquals(4, elements.size());
        assertFalse(elements.get(0));
        assertTrue(elements.get(1));

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
    }

    @Test
    public void testStreamV() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamV().toList();
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

        List<Boolean> col = matrix.streamV(0).toList();
        assertEquals(2, col.size());
        assertTrue(col.get(0));
        assertFalse(col.get(1));
    }

    @Test
    public void testStreamVWithRange() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<Boolean> elements = matrix.streamV(1, 3).toList();
        assertEquals(4, elements.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(-1, 2));
    }

    @Test
    public void testStreamR() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> rows = matrix.streamR().map(stream -> stream.toList()).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).size());
        assertTrue(rows.get(0).get(0));
        assertFalse(rows.get(0).get(1));
    }

    @Test
    public void testStreamRWithRange() {
        boolean[][] arr = { { true, false }, { false, true }, { true, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> rows = matrix.streamR(1, 3).map(stream -> stream.toList()).toList();
        assertEquals(2, rows.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(-1, 2));
    }

    @Test
    public void testStreamC() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> columnCount = matrix.streamC().map(stream -> stream.toList()).toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).size());
        assertTrue(columnCount.get(0).get(0));
        assertFalse(columnCount.get(0).get(1));
    }

    @Test
    public void testStreamCWithRange() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix matrix = BooleanMatrix.of(arr);

        List<List<Boolean>> columnCount = matrix.streamC(1, 3).map(stream -> stream.toList()).toList();
        assertEquals(2, columnCount.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(-1, 2));
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

        // Just ensure it doesn't throw
        matrix.println();
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
}
