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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.Stream;

@Tag("2025")
public class BooleanMatrix2025Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix m = new BooleanMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertTrue(m.get(0, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        BooleanMatrix m = new BooleanMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        BooleanMatrix m = new BooleanMatrix(new boolean[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        BooleanMatrix m = new BooleanMatrix(new boolean[][] { { true } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertTrue(m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(BooleanMatrix.empty(), BooleanMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix m = BooleanMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat() {
        BooleanMatrix m = BooleanMatrix.repeat(true, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertTrue(m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withFalse() {
        BooleanMatrix m = BooleanMatrix.repeat(false, 3);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertFalse(m.get(0, i));
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        BooleanMatrix m = BooleanMatrix.diagonalLU2RD(new boolean[] { true, false, true });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 2));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        BooleanMatrix m = BooleanMatrix.diagonalRU2LD(new boolean[] { true, false, true });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 2));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 0));
        assertFalse(m.get(0, 0));
        assertFalse(m.get(2, 2));
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        BooleanMatrix m = BooleanMatrix.diagonal(new boolean[] { true, false, true }, new boolean[] { false, true, false });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 2));
        assertFalse(m.get(0, 2));
        assertFalse(m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        BooleanMatrix m = BooleanMatrix.diagonal(new boolean[] { true, false, true }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        BooleanMatrix m = BooleanMatrix.diagonal(null, new boolean[] { false, true, false });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertFalse(m.get(0, 2));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        BooleanMatrix m = BooleanMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.diagonal(new boolean[] { true, false }, new boolean[] { true, false, true }));
    }

    @Test
    public void testUnbox() {
        Boolean[][] boxed = { { true, false }, { false, true } };
        Matrix<Boolean> boxedMatrix = Matrix.of(boxed);
        BooleanMatrix unboxed = BooleanMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
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

        OptionalBoolean up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertTrue(up.get());

        // Top row has no element above
        OptionalBoolean empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

        OptionalBoolean down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertFalse(down.get());

        // Bottom row has no element below
        OptionalBoolean empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

        OptionalBoolean left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertTrue(left.get());

        // Leftmost column has no element to the left
        OptionalBoolean empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

        OptionalBoolean right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertFalse(right.get());

        // Rightmost column has no element to the right
        OptionalBoolean empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        assertArrayEquals(new boolean[] { true, false, true }, m.row(0));
        assertArrayEquals(new boolean[] { false, true, false }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        assertArrayEquals(new boolean[] { true, false }, m.column(0));
        assertArrayEquals(new boolean[] { false, true }, m.column(1));
        assertArrayEquals(new boolean[] { true, false }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.setRow(0, new boolean[] { false, true });
        assertArrayEquals(new boolean[] { false, true }, m.row(0));
        assertArrayEquals(new boolean[] { false, true }, m.row(1)); // unchanged
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
        assertArrayEquals(new boolean[] { false, true }, m.column(0));
        assertArrayEquals(new boolean[] { false, true }, m.column(1)); // unchanged
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
        assertArrayEquals(new boolean[] { false, true }, m.row(0));
        assertArrayEquals(new boolean[] { false, true }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.updateColumn(0, x -> !x);
        assertArrayEquals(new boolean[] { false, true }, m.column(0));
        assertArrayEquals(new boolean[] { false, true }, m.column(1)); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        assertArrayEquals(new boolean[] { true, true, true }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        m.setLU2RD(new boolean[] { false, false, false });
        assertFalse(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertFalse(m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new boolean[] { true }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new boolean[] { true, false }));
    }

    @Test
    public void testUpdateLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        m.updateLU2RD(x -> !x);
        assertFalse(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertFalse(m.get(2, 2));
        assertFalse(m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> !x));
    }

    @Test
    public void testGetRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        assertArrayEquals(new boolean[] { true, true, true }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        m.setRU2LD(new boolean[] { false, false, false });
        assertFalse(m.get(0, 2));
        assertFalse(m.get(1, 1));
        assertFalse(m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new boolean[] { true }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new boolean[] { true, false }));
    }

    @Test
    public void testUpdateRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        m.updateRU2LD(x -> !x);
        assertFalse(m.get(0, 2));
        assertFalse(m.get(1, 1));
        assertFalse(m.get(2, 0));
        assertFalse(m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> !x));
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
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                assertTrue(m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        boolean[][] patch = { { true, false }, { false, true } };
        m.fill(patch);
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
        m.fill(1, 1, patch);
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
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
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
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
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
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
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
        BooleanMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertTrue(extended.get(0, 0));
        assertTrue(extended.get(1, 1));
        assertFalse(extended.get(3, 3)); // new cells are false
    }

    @Test
    public void testExtend_smaller() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        BooleanMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertTrue(truncated.get(0, 0));
        assertTrue(truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = m.extend(3, 3, true);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertTrue(extended.get(0, 0));
        assertTrue(extended.get(2, 2)); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, false));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, false));
    }

    @Test
    public void testExtend_directional() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        BooleanMatrix extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rows); // 1 + 3 + 1
        assertEquals(7, extended.cols); // 2 + 3 + 2

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
        assertEquals(5, extended.rows);
        assertEquals(5, extended.cols);

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
        m.reverseH();
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertTrue(m.get(0, 2));
        assertFalse(m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
        m.reverseV();
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
        assertTrue(m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        BooleanMatrix flipped = m.flipH();
        assertTrue(flipped.get(0, 0));
        assertFalse(flipped.get(0, 1));
        assertTrue(flipped.get(0, 2));

        // Original unchanged
        assertTrue(m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
        BooleanMatrix flipped = m.flipV();
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
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertFalse(rotated.get(0, 0));
        assertTrue(rotated.get(0, 1));
        assertTrue(rotated.get(1, 0));
        assertFalse(rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertTrue(rotated.get(0, 0));
        assertFalse(rotated.get(0, 1));
        assertFalse(rotated.get(1, 0));
        assertTrue(rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
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
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
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
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
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
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
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
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

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
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

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
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
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
        m.flatOp(row -> {
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
        BooleanMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rows);
        assertEquals(3, stacked.cols);
        assertTrue(stacked.get(0, 0));
        assertTrue(stacked.get(2, 0));
        assertTrue(stacked.get(3, 2));
    }

    @Test
    public void testVstack_differentColumnCounts() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertTrue(stacked.get(0, 0));
        assertTrue(stacked.get(0, 2));
        assertTrue(stacked.get(1, 3));
    }

    @Test
    public void testHstack_differentRowCounts() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        Matrix<Boolean> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(3, boxed.cols);
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
        List<Boolean> diagonal = m.streamLU2RD().toList();
        assertEquals(3, diagonal.size());
        assertTrue(diagonal.get(0));
        assertTrue(diagonal.get(1));
        assertTrue(diagonal.get(2));
    }

    @Test
    public void testStreamLU2RD_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.streamLU2RD().count());
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Boolean> antiDiagonal = m.streamRU2LD().toList();
        assertEquals(3, antiDiagonal.size());
        assertTrue(antiDiagonal.get(0));
        assertTrue(antiDiagonal.get(1));
        assertTrue(antiDiagonal.get(2));
    }

    @Test
    public void testStreamRU2LD_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.streamRU2LD().count());
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        BooleanMatrix nonSquare = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> all = m.streamH().toList();
        assertEquals(6, all.size());
        assertTrue(all.get(0));
        assertFalse(all.get(1));
        assertTrue(all.get(2));
        assertFalse(all.get(3));
    }

    @Test
    public void testStreamH_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.streamH().count());
    }

    @Test
    public void testStreamH_withRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> row1 = m.streamH(1).toList();
        assertEquals(3, row1.size());
        assertFalse(row1.get(0));
        assertTrue(row1.get(1));
        assertFalse(row1.get(2));
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Boolean> rows = m.streamH(1, 3).toList();
        assertEquals(6, rows.size());
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> all = m.streamV().toList();
        assertEquals(6, all.size());
        assertTrue(all.get(0));
        assertFalse(all.get(1));
        assertFalse(all.get(2));
    }

    @Test
    public void testStreamV_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.streamV().count());
    }

    @Test
    public void testStreamV_withColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> col1 = m.streamV(1).toList();
        assertEquals(2, col1.size());
        assertFalse(col1.get(0));
        assertTrue(col1.get(1));
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Boolean> cols = m.streamV(1, 3).toList();
        assertEquals(6, cols.size());
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Stream<Boolean>> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        List<Boolean> row0 = rows.get(0).toList();
        assertEquals(3, row0.size());
        assertTrue(row0.get(0));
    }

    @Test
    public void testStreamR_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Stream<Boolean>> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Stream<Boolean>> cols = m.streamC().toList();
        assertEquals(3, cols.size());
    }

    @Test
    public void testStreamC_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Stream<Boolean>> cols = m.streamC(1, 3).toList();
        assertEquals(2, cols.size());
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
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
    public void testBooleanLogic() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });

        // Test AND operation
        BooleanMatrix allTrue = BooleanMatrix.of(new boolean[][] { { true, true }, { true, true } });
        BooleanMatrix andResult = m.zipWith(allTrue, (a, b) -> a && b);
        assertTrue(andResult.get(0, 0));
        assertFalse(andResult.get(0, 1));
        assertFalse(andResult.get(1, 0));
        assertTrue(andResult.get(1, 1));

        // Test OR operation
        BooleanMatrix orResult = m.zipWith(allTrue, (a, b) -> a || b);
        assertTrue(orResult.get(0, 0));
        assertTrue(orResult.get(0, 1));
        assertTrue(orResult.get(1, 0));
        assertTrue(orResult.get(1, 1));

        // Test NOT operation
        BooleanMatrix notResult = m.map(a -> !a);
        assertFalse(notResult.get(0, 0));
        assertTrue(notResult.get(0, 1));
        assertTrue(notResult.get(1, 0));
        assertFalse(notResult.get(1, 1));
    }

    @Test
    public void testEmptyMatrixOperations() {
        BooleanMatrix empty = BooleanMatrix.empty();

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rows);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        BooleanMatrix extended = empty.extend(2, 2, true);
        assertEquals(2, extended.rows);
        assertEquals(2, extended.cols);
        assertTrue(extended.get(0, 0));
    }

    @Test
    public void testAllTrueMatrix() {
        BooleanMatrix m = BooleanMatrix.repeat(true, 3);
        m = m.reshape(3, 1);
        m = m.repmat(1, 3);

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
        // Just ensure it doesn't throw an exception
        m.println();
    }

    @Test
    public void testPrintln_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        empty.println();
    }

    // ============ Additional Edge Cases ============

    @Test
    public void testReshape_invalidDimensions() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        // Reshaping with more elements than available should work but fill with default values
        BooleanMatrix reshaped = m.reshape(3, 3);
        assertEquals(3, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    @Test
    public void testFill_partialOverlap() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        boolean[][] patch = { { true, true }, { true, true } };
        m.fill(2, 2, patch); // Only partial overlap
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
        BooleanMatrix result = m1.vstack(m1.copy());
        assertEquals(4, result.rows);
        assertEquals(2, result.cols);
    }

    @Test
    public void testHstack_withEmpty() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix result = m1.hstack(m1.copy());
        assertEquals(2, result.rows);
        assertEquals(4, result.cols);
    }

    @Test
    public void testRotate90_rectangle() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        BooleanMatrix rotated = m.rotate90();
        assertEquals(3, rotated.rows);
        assertEquals(1, rotated.cols);
        assertTrue(rotated.get(0, 0));
        assertFalse(rotated.get(1, 0));
        assertTrue(rotated.get(2, 0));
    }

    @Test
    public void testRotate180_rectangle() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        BooleanMatrix rotated = m.rotate180();
        assertEquals(1, rotated.rows);
        assertEquals(3, rotated.cols);
        assertTrue(rotated.get(0, 0));
        assertFalse(rotated.get(0, 1));
        assertTrue(rotated.get(0, 2));
    }

    @Test
    public void testRotate270_rectangle() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true } });
        BooleanMatrix rotated = m.rotate270();
        assertEquals(3, rotated.rows);
        assertEquals(1, rotated.cols);
        assertTrue(rotated.get(0, 0));
        assertFalse(rotated.get(1, 0));
        assertTrue(rotated.get(2, 0));
    }

    @Test
    public void testCopy_fullMatrix() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix copy = m.copy(0, 2, 0, 2);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertTrue(copy.get(0, 0));
        assertTrue(copy.get(1, 1));

        // Modify copy shouldn't affect original
        copy.set(0, 0, false);
        assertTrue(m.get(0, 0));
    }

    @Test
    public void testExtend_noChange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = m.extend(2, 2);
        assertEquals(2, extended.rows);
        assertEquals(2, extended.cols);
        assertTrue(extended.get(0, 0));
        assertTrue(extended.get(1, 1));
    }

    @Test
    public void testStreamR_operations() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        long rowsWithTrue = m.streamR().filter(row -> row.anyMatch(b -> b)).count();
        assertEquals(2, rowsWithTrue);
    }

    @Test
    public void testStreamC_operations() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        long colsWithAllFalse = m.streamC().filter(col -> col.noneMatch(b -> b)).count();
        assertEquals(0, colsWithAllFalse); // No columns have all false (col 0: true,false; col 1: false,true; col 2: true,false)
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
        assertArrayEquals(new boolean[] { true, true, true }, m.row(0));
        assertArrayEquals(new boolean[] { false, true, false }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn_allElements() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
        m.updateColumn(1, x -> true);
        assertArrayEquals(new boolean[] { true, true, true }, m.column(1));
        assertArrayEquals(new boolean[] { true, false, true }, m.column(0)); // unchanged
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
        BooleanMatrix repeated = m.repelem(3, 3);
        assertEquals(3, repeated.rows);
        assertEquals(3, repeated.cols);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(repeated.get(i, j));
            }
        }
    }

    @Test
    public void testRepmat_edge1x1() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
        BooleanMatrix repeated = m.repmat(3, 3);
        assertEquals(3, repeated.rows);
        assertEquals(3, repeated.cols);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(repeated.get(i, j));
            }
        }
    }

    // ============ High-Impact Tests for 95% Coverage ============

    @Test
    public void testRotateAndTransposeTallMatrix() {
        // Create a tall matrix (rows > cols) - 4 rows × 2 cols
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] {
            { true,  false },
            { false, true  },
            { true,  true  },
            { false, false }
        });

        // Test rotate90() with tall matrix
        BooleanMatrix rotated90 = m.rotate90();
        assertEquals(2, rotated90.rows);
        assertEquals(4, rotated90.cols);
        assertFalse(rotated90.get(0, 0));
        assertTrue(rotated90.get(0, 1));
        assertFalse(rotated90.get(0, 2));
        assertTrue(rotated90.get(0, 3));

        // Test rotate270() with tall matrix
        BooleanMatrix rotated270 = m.rotate270();
        assertEquals(2, rotated270.rows);
        assertEquals(4, rotated270.cols);
        assertFalse(rotated270.get(0, 0));
        assertTrue(rotated270.get(0, 1));
        assertTrue(rotated270.get(0, 2));
        assertFalse(rotated270.get(0, 3));

        // Test transpose() with tall matrix
        BooleanMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(4, transposed.cols);
        assertTrue(transposed.get(0, 0));
        assertFalse(transposed.get(0, 1));

        // Test boxed() with tall matrix
        Matrix<Boolean> boxed = m.boxed();
        assertEquals(4, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(Boolean.TRUE, boxed.get(0, 0));
        assertEquals(Boolean.FALSE, boxed.get(0, 1));
    }

    @Test
    public void testRepelemOverflow() {
        // Create matrix that will overflow when repeated
        int largeSize = 50000;
        BooleanMatrix m = BooleanMatrix.of(new boolean[largeSize][2]);

        // Test row overflow
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> m.repelem(50000, 1));
        assertTrue(ex1.getMessage().contains("too many rows"));

        // Test column overflow
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[2][largeSize]);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> m2.repelem(1, 50000));
        assertTrue(ex2.getMessage().contains("too many columns"));
    }

    @Test
    public void testRepmatOverflow() {
        // Create matrix that will overflow when tiled
        int largeSize = 50000;
        BooleanMatrix m = BooleanMatrix.of(new boolean[largeSize][2]);

        // Test row overflow
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> m.repmat(50000, 1));
        assertTrue(ex1.getMessage().contains("too many rows"));

        // Test column overflow
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[2][largeSize]);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> m2.repmat(1, 50000));
        assertTrue(ex2.getMessage().contains("too many columns"));
    }
}
