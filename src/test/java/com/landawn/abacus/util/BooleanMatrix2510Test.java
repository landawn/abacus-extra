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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.Stream;

@Tag("2510")
public class BooleanMatrix2510Test extends TestBase {

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

    @Test
    public void testConstructor_withNonSquareMatrix() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix m = new BooleanMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
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
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withZeroLength() {
        BooleanMatrix m = BooleanMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRepeat() {
        BooleanMatrix m = BooleanMatrix.repeat(true, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertTrue(m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withFalse() {
        BooleanMatrix m = BooleanMatrix.repeat(false, 3);
        assertEquals(1, m.rowCount());
        assertEquals(3, m.columnCount());
        for (int i = 0; i < 3; i++) {
            assertFalse(m.get(0, i));
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        BooleanMatrix m = BooleanMatrix.diagonalLU2RD(new boolean[] { true, false, true });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 2));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_empty() {
        BooleanMatrix m = BooleanMatrix.diagonalLU2RD(new boolean[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalRU2LD() {
        BooleanMatrix m = BooleanMatrix.diagonalRU2LD(new boolean[] { true, false, true });
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
        BooleanMatrix m = BooleanMatrix.diagonal(new boolean[] { true, true, true }, new boolean[] { false, false, false });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertTrue(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(2, 2));
        assertFalse(m.get(0, 2));
        assertFalse(m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        BooleanMatrix m = BooleanMatrix.diagonal(new boolean[] { true, false, true }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        BooleanMatrix m = BooleanMatrix.diagonal(null, new boolean[] { false, true, false });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
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

    // ============ Component Type Test ============

    @Test
    public void testComponentType() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true } });
        assertEquals(boolean.class, m.componentType());
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

    // ============ Directional Access Tests ============

    @Test
    public void testUpOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertTrue(up.get());
    }

    @Test
    public void testUpOf_atTopEdge() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void testDownOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertFalse(down.get());
    }

    @Test
    public void testDownOf_atBottomEdge() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void testLeftOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertTrue(left.get());
    }

    @Test
    public void testLeftOf_atLeftEdge() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testRightOf() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertFalse(right.get());
    }

    @Test
    public void testRightOf_atRightEdge() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void testRow() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix m = BooleanMatrix.of(arr);
        boolean[] row0 = m.row(0);
        assertArrayEquals(new boolean[] { true, false, true }, row0);
        boolean[] row1 = m.row(1);
        assertArrayEquals(new boolean[] { false, true, false }, row1);
    }

    @Test
    public void testRow_invalidIndex() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testColumn() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix m = BooleanMatrix.of(arr);
        boolean[] col0 = m.column(0);
        assertArrayEquals(new boolean[] { true, false }, col0);
        boolean[] col1 = m.column(1);
        assertArrayEquals(new boolean[] { false, true }, col1);
    }

    @Test
    public void testColumn_invalidIndex() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false } });
        m.setRow(0, new boolean[] { true, true, true });
        assertArrayEquals(new boolean[] { true, true, true }, m.row(0));
        assertArrayEquals(new boolean[] { false, false, false }, m.row(1));
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
        assertArrayEquals(new boolean[] { true, true, true }, m.column(0));
        assertArrayEquals(new boolean[] { false, false, false }, m.column(1));
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
        assertArrayEquals(new boolean[] { false, true, false }, m.row(0));
        assertArrayEquals(new boolean[] { false, false, false }, m.row(1));
    }

    @Test
    public void testUpdateColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, false }, { true, false } });
        m.updateColumn(0, val -> !val);
        assertArrayEquals(new boolean[] { false, true, false }, m.column(0));
        assertArrayEquals(new boolean[] { false, false, false }, m.column(1));
    }

    // ============ Diagonal Access Tests ============

    @Test
    public void testGetLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        boolean[] diag = m.getLU2RD();
        assertArrayEquals(new boolean[] { true, true, true }, diag);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        m.setLU2RD(new boolean[] { true, true, true });
        assertTrue(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(2, 2));
        assertFalse(m.get(0, 1));
    }

    @Test
    public void testSetLU2RD_invalidLength() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new boolean[] { true, true }));
    }

    @Test
    public void testUpdateLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, false, false }, { false, false, true } });
        m.updateLU2RD(val -> !val);
        assertFalse(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
        boolean[] diag = m.getRU2LD();
        assertArrayEquals(new boolean[] { true, true, true }, diag);
    }

    @Test
    public void testSetRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        m.setRU2LD(new boolean[] { true, true, true });
        assertTrue(m.get(0, 2));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(2, 0));
        assertFalse(m.get(0, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, false, false }, { true, false, false } });
        m.updateRU2LD(val -> !val);
        assertFalse(m.get(0, 2));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(2, 0));
    }

    // ============ Update/Replace Tests ============

    @Test
    public void testUpdateAll_unary() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.updateAll(val -> !val);
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertFalse(m.get(1, 1));
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
        m.fill(new boolean[][] { { true, true }, { true, true } });
        assertTrue(m.get(0, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void testFill_withOffset() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, false }, { false, false, false }, { false, false, false } });
        m.fill(1, 1, new boolean[][] { { true, true }, { true, true } });
        assertFalse(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(2, 2));
    }

    @Test
    public void testFill_withOffset_invalidPosition() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        assertThrows(IllegalArgumentException.class, () -> m.fill(3, 3, new boolean[][] { { true, true }, { true, true } }));
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
        BooleanMatrix extended = m.extend(2, 3);
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
        BooleanMatrix extended = m.extend(2, 3, true);
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

        BooleanMatrix extended = m.extend(1, 1);
        assertEquals(1, extended.rowCount());
        assertEquals(1, extended.columnCount());
    }

    // ============ Transformation Tests ============

    @Test
    public void testReverseH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        m.reverseH();
        assertTrue(m.get(0, 2));
        assertFalse(m.get(0, 1));
        assertTrue(m.get(0, 0));
    }

    @Test
    public void testReverseV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.reverseV();
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertFalse(m.get(1, 1));
    }

    @Test
    public void testFlipH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        BooleanMatrix flipped = m.flipH();
        assertTrue(flipped.get(0, 2));
        assertFalse(flipped.get(0, 1));
        assertTrue(flipped.get(0, 0));
        assertTrue(m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix flipped = m.flipV();
        assertFalse(flipped.get(0, 0));
        assertTrue(flipped.get(0, 1));
        assertTrue(flipped.get(1, 0));
        assertFalse(flipped.get(1, 1));
        assertTrue(m.get(0, 0));
    }

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
        BooleanMatrix result = m.repelem(2, 2);
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
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix result = m.repmat(2, 2);
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
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanList flat = m.flatten();
        assertEquals(4, flat.size());
        assertTrue(flat.get(0));
        assertFalse(flat.get(1));
        assertFalse(flat.get(2));
        assertTrue(flat.get(3));
    }

    @Test
    public void testFlatOp() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true } });
        BooleanMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(0, 1));
        assertFalse(stacked.get(1, 0));
        assertTrue(stacked.get(1, 1));
    }

    @Test
    public void testVstack_invalidColumns() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false }, { true } });
        BooleanMatrix stacked = m1.hstack(m2);
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
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
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

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_two() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
        BooleanMatrix result = m1.zipWith(m2, (a, b) -> a && b);
        assertFalse(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertFalse(result.get(1, 1));
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
        List<Boolean> diag = m.streamLU2RD().toList();
        assertEquals(3, diag.size());
        assertTrue(diag.get(0));
        assertTrue(diag.get(1));
        assertTrue(diag.get(2));
    }

    @Test
    public void testStreamRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
        List<Boolean> diag = m.streamRU2LD().toList();
        assertEquals(3, diag.size());
        assertTrue(diag.get(0));
        assertTrue(diag.get(1));
        assertTrue(diag.get(2));
    }

    @Test
    public void testStreamH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Boolean> all = m.streamH().toList();
        assertEquals(4, all.size());
        assertTrue(all.get(0));
        assertFalse(all.get(1));
    }

    @Test
    public void testStreamH_singleRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> row = m.streamH(0).toList();
        assertEquals(3, row.size());
        assertTrue(row.get(0));
        assertFalse(row.get(1));
        assertTrue(row.get(2));
    }

    @Test
    public void testStreamH_rowRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        List<Boolean> rows = m.streamH(1, 3).toList();
        assertEquals(4, rows.size());
        assertFalse(rows.get(0));
        assertTrue(rows.get(1));
    }

    @Test
    public void testStreamV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Boolean> all = m.streamV().toList();
        assertEquals(4, all.size());
        assertTrue(all.get(0));
        assertFalse(all.get(1));
    }

    @Test
    public void testStreamV_singleColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
        List<Boolean> col = m.streamV(0).toList();
        assertEquals(3, col.size());
        assertTrue(col.get(0));
        assertFalse(col.get(1));
        assertTrue(col.get(2));
    }

    @Test
    public void testStreamV_columnRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> cols = m.streamV(1, 3).toList();
        assertEquals(4, cols.size());
        assertFalse(cols.get(0));
        assertTrue(cols.get(1));
    }

    @Test
    public void testStreamR() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Stream<Boolean>> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).count());
    }

    @Test
    public void testStreamR_rowRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        List<Stream<Boolean>> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamC() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Stream<Boolean>> cols = m.streamC().toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).count());
    }

    @Test
    public void testStreamC_columnRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Stream<Boolean>> cols = m.streamC(1, 3).toList();
        assertEquals(2, cols.size());
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
        List<Point> points = m.pointsLU2RD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 2), points.get(2));
    }

    @Test
    public void testPointsRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
        List<Point> points = m.pointsRU2LD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 2), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
    }

    @Test
    public void testPointsH_singleRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][3]);
        List<Point> points = m.pointsH(0).toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 2), points.get(2));
    }

    @Test
    public void testPointsV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
    }

    @Test
    public void testPointsV_singleColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][2]);
        List<Point> points = m.pointsV(0).toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsR() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        List<Stream<Point>> rows = m.pointsR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).count());
    }

    @Test
    public void testPointsC() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        List<Stream<Point>> cols = m.pointsC().toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).count());
    }

    // ============ Utility Tests (Inherited) ============

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
        boolean[][] result = m.array();
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
