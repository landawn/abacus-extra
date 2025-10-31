package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalLong;
import com.landawn.abacus.util.stream.LongStream;

@Tag("2511")
public class LongMatrix2511Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix m = new LongMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        LongMatrix m = new LongMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        LongMatrix m = new LongMatrix(new long[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        LongMatrix m = new LongMatrix(new long[][] { { 42L } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42L, m.get(0, 0));
    }

    @Test
    public void testConstructor_withLargeMatrix() {
        long[][] arr = new long[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arr[i][j] = i * 10L + j;
            }
        }
        LongMatrix m = new LongMatrix(arr);
        assertEquals(10, m.rows);
        assertEquals(10, m.cols);
        assertEquals(0L, m.get(0, 0));
        assertEquals(99L, m.get(9, 9));
        assertEquals(55L, m.get(5, 5));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(LongMatrix.empty(), LongMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix m = LongMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void testOf_withNullArray() {
        LongMatrix m = LongMatrix.of((long[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withEmptyArray() {
        LongMatrix m = LongMatrix.of(new long[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withSingleRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L, 4L, 5L } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    @Test
    public void testOf_withSingleColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L }, { 2L }, { 3L }, { 4L } });
        assertEquals(4, m.rows);
        assertEquals(1, m.cols);
    }

    // ============ Create Method Tests ============

    @Test
    public void testCreate_fromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        LongMatrix m = LongMatrix.create(ints);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void testCreate_fromIntArray_empty() {
        LongMatrix m = LongMatrix.create(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromIntArray_null() {
        LongMatrix m = LongMatrix.create((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromIntArray_nullFirstRow() {
        int[][] ints = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.create(ints));
    }

    @Test
    public void testCreate_fromIntArray_differentRowLengths() {
        int[][] ints = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.create(ints));
    }

    @Test
    public void testRandom() {
        LongMatrix m = LongMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertNotNull(m.row(0));
    }

    @Test
    public void testRandom_zeroLength() {
        LongMatrix m = LongMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRepeat() {
        LongMatrix m = LongMatrix.repeat(42L, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(42L, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_zeroLength() {
        LongMatrix m = LongMatrix.repeat(42L, 0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRange() {
        LongMatrix m = LongMatrix.range(0L, 5L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        LongMatrix m = LongMatrix.range(0L, 10L, 2L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.row(0));
    }

    @Test
    public void testRange_negativeStep() {
        LongMatrix m = LongMatrix.range(10L, 0L, -2L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 10L, 8L, 6L, 4L, 2L }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
        assertEquals(1, m.rows);
        assertEquals(6, m.cols);
        assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        LongMatrix m = LongMatrix.diagonalLU2RD(new long[] { 1L, 2L, 3L });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(0L, m.get(0, 1));
        assertEquals(0L, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        LongMatrix m = LongMatrix.diagonalRU2LD(new long[] { 1L, 2L, 3L });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 2));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 0));
        assertEquals(0L, m.get(0, 0));
    }

    @Test
    public void testDiagonal_bothDiagonals() {
        LongMatrix m = LongMatrix.diagonal(new long[] { 1L, 2L, 3L }, new long[] { 4L, 5L, 6L });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(4L, m.get(0, 2));
        assertEquals(2L, m.get(1, 1));
        assertEquals(6L, m.get(2, 0));
    }

    @Test
    public void testDiagonal_differentLengths() {
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonal(new long[] { 1L, 2L }, new long[] { 1L, 2L, 3L }));
    }

    @Test
    public void testDiagonal_emptyBoth() {
        LongMatrix m = LongMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testUnbox() {
        Matrix<Long> boxed = Matrix.of(new Long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m = LongMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    // ============ Get/Set Methods ============

    @Test
    public void testComponentType() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
        assertEquals(long.class, m.componentType());
    }

    @Test
    public void testGet() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(3L, m.get(1, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void testGet_withPoint() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Point p = Point.of(1, 0);
        assertEquals(3L, m.get(p));
    }

    @Test
    public void testSet() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.set(0, 0, 10L);
        assertEquals(10L, m.get(0, 0));
        m.set(1, 1, 20L);
        assertEquals(20L, m.get(1, 1));
    }

    @Test
    public void testSet_withPoint() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Point p = Point.of(1, 0);
        m.set(p, 99L);
        assertEquals(99L, m.get(1, 0));
    }

    // ============ Directional Methods (up, down, left, right) ============

    @Test
    public void testUpOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1L, up.get());

        OptionalLong noUp = m.upOf(0, 0);
        assertFalse(noUp.isPresent());
    }

    @Test
    public void testDownOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3L, down.get());

        OptionalLong noDown = m.downOf(1, 0);
        assertFalse(noDown.isPresent());
    }

    @Test
    public void testLeftOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1L, left.get());

        OptionalLong noLeft = m.leftOf(0, 0);
        assertFalse(noLeft.isPresent());
    }

    @Test
    public void testRightOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2L, right.get());

        OptionalLong noRight = m.rightOf(0, 1);
        assertFalse(noRight.isPresent());
    }

    // ============ Row/Column Operations ============

    @Test
    public void testRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        assertArrayEquals(new long[] { 1L, 2L, 3L }, m.row(0));
        assertArrayEquals(new long[] { 4L, 5L, 6L }, m.row(1));
    }

    @Test
    public void testRow_invalidIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        assertArrayEquals(new long[] { 1L, 4L }, m.column(0));
        assertArrayEquals(new long[] { 2L, 5L }, m.column(1));
        assertArrayEquals(new long[] { 3L, 6L }, m.column(2));
    }

    @Test
    public void testColumn_invalidIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setRow(0, new long[] { 10L, 20L });
        assertArrayEquals(new long[] { 10L, 20L }, m.row(0));
        assertEquals(3L, m.get(1, 0));
    }

    @Test
    public void testSetRow_invalidLength() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L, 2L, 3L }));
    }

    @Test
    public void testSetColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setColumn(0, new long[] { 10L, 30L });
        assertEquals(10L, m.get(0, 0));
        assertEquals(30L, m.get(1, 0));
        assertEquals(2L, m.get(0, 1));
    }

    @Test
    public void testSetColumn_invalidLength() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L, 2L, 3L }));
    }

    @Test
    public void testUpdateRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new long[] { 2L, 4L }, m.row(0));
        assertArrayEquals(new long[] { 3L, 4L }, m.row(1));
    }

    @Test
    public void testUpdateColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateColumn(0, x -> x * 10);
        assertEquals(10L, m.get(0, 0));
        assertEquals(30L, m.get(1, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(4L, m.get(1, 1));
    }

    // ============ Diagonal Operations ============

    @Test
    public void testGetLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        assertArrayEquals(new long[] { 1L, 5L, 9L }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.setLU2RD(new long[] { 10L, 20L, 30L });
        assertEquals(10L, m.get(0, 0));
        assertEquals(20L, m.get(1, 1));
        assertEquals(30L, m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_invalidLength() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new long[] { 1L, 2L, 3L }));
    }

    @Test
    public void testUpdateLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.updateLU2RD(x -> x * 10);
        assertEquals(10L, m.get(0, 0));
        assertEquals(50L, m.get(1, 1));
        assertEquals(90L, m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        assertArrayEquals(new long[] { 3L, 5L, 7L }, m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.setRU2LD(new long[] { 30L, 50L, 70L });
        assertEquals(30L, m.get(0, 2));
        assertEquals(50L, m.get(1, 1));
        assertEquals(70L, m.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.updateRU2LD(x -> x + 100L);
        assertEquals(103L, m.get(0, 2));
        assertEquals(105L, m.get(1, 1));
        assertEquals(107L, m.get(2, 0));
    }

    // ============ Update Methods ============

    @Test
    public void testUpdateAll_unary() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateAll(x -> x * 2);
        assertEquals(2L, m.get(0, 0));
        assertEquals(4L, m.get(0, 1));
        assertEquals(6L, m.get(1, 0));
        assertEquals(8L, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateAll((i, j) -> i * 10L + j);
        assertEquals(0L, m.get(0, 0));
        assertEquals(1L, m.get(0, 1));
        assertEquals(10L, m.get(1, 0));
        assertEquals(11L, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_predicate() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        m.replaceIf(x -> x % 2 == 0, 99L);
        assertEquals(1L, m.get(0, 0));
        assertEquals(99L, m.get(0, 1));
        assertEquals(3L, m.get(0, 2));
        assertEquals(99L, m.get(1, 0));
    }

    @Test
    public void testReplaceIf_biPredicate() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.replaceIf((i, j) -> i == j, 0L);
        assertEquals(0L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(3L, m.get(1, 0));
        assertEquals(0L, m.get(1, 1));
    }

    // ============ Map Methods ============

    @Test
    public void testMap() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m.map(x -> x * 10);
        assertEquals(10L, result.get(0, 0));
        assertEquals(20L, result.get(0, 1));
        assertEquals(30L, result.get(1, 0));
        assertEquals(40L, result.get(1, 1));
        // Original should be unchanged
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void testMapToDouble() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix result = m.mapToDouble(x -> x * 0.5);
        assertEquals(0.5, result.get(0, 0), 0.0001);
        assertEquals(2.0, result.get(1, 1), 0.0001);
    }

    @Test
    public void testMapToObj() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Matrix<String> result = m.mapToObj(x -> "Value:" + x, String.class);
        assertEquals("Value:1", result.get(0, 0));
        assertEquals("Value:4", result.get(1, 1));
    }

    // ============ Fill Methods ============

    @Test
    public void testFill_value() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.fill(99L);
        assertEquals(99L, m.get(0, 0));
        assertEquals(99L, m.get(0, 1));
        assertEquals(99L, m.get(1, 0));
        assertEquals(99L, m.get(1, 1));
    }

    @Test
    public void testFill_array() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.fill(new long[][] { { 10L, 20L }, { 30L, 40L } });
        assertEquals(10L, m.get(0, 0));
        assertEquals(20L, m.get(0, 1));
        assertEquals(30L, m.get(1, 0));
        assertEquals(40L, m.get(1, 1));
    }

    @Test
    public void testFill_arrayWithOffset() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.fill(1, 1, new long[][] { { 99L, 88L } });
        assertEquals(1L, m.get(0, 0));
        assertEquals(99L, m.get(1, 1));
        assertEquals(88L, m.get(1, 2));
    }

    // ============ Copy Methods ============

    @Test
    public void testCopy() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix copy = m.copy();
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(1L, copy.get(0, 0));

        // Verify it's a deep copy
        copy.set(0, 0, 99L);
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void testCopy_rowRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(3L, copy.get(0, 0));
        assertEquals(6L, copy.get(1, 1));
    }

    @Test
    public void testCopy_fullRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix copy = m.copy(1, 2, 1, 3);
        assertEquals(1, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(5L, copy.get(0, 0));
        assertEquals(6L, copy.get(0, 1));
    }

    // ============ Extend Methods ============

    @Test
    public void testExtend_newSize() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(0L, extended.get(2, 2));
    }

    @Test
    public void testExtend_withDefaultValue() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix extended = m.extend(2, 3, 99L);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(99L, extended.get(0, 2));
        assertEquals(99L, extended.get(1, 0));
    }

    @Test
    public void testExtend_directions() {
        LongMatrix m = LongMatrix.of(new long[][] { { 5L } });
        LongMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5L, extended.get(1, 1));
        assertEquals(0L, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionsWithDefault() {
        LongMatrix m = LongMatrix.of(new long[][] { { 5L } });
        LongMatrix extended = m.extend(1, 1, 1, 1, 9L);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5L, extended.get(1, 1));
        assertEquals(9L, extended.get(0, 0));
        assertEquals(9L, extended.get(2, 2));
    }

    // ============ Reverse and Flip Methods ============

    @Test
    public void testReverseH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        m.reverseH();
        assertEquals(3L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(1L, m.get(0, 2));
        assertEquals(6L, m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        m.reverseV();
        assertEquals(5L, m.get(0, 0));
        assertEquals(6L, m.get(0, 1));
        assertEquals(1L, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix flipped = m.flipH();
        assertEquals(3L, flipped.get(0, 0));
        assertEquals(1L, flipped.get(0, 2));
        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix flipped = m.flipV();
        assertEquals(3L, flipped.get(0, 0));
        assertEquals(1L, flipped.get(1, 0));
        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    // ============ Rotation Methods ============

    @Test
    public void testRotate90() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix rotated = m.rotate90();
        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4L, rotated.get(0, 0));
        assertEquals(1L, rotated.get(0, 1));
        assertEquals(6L, rotated.get(2, 0));
    }

    @Test
    public void testRotate180() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4L, rotated.get(0, 0));
        assertEquals(3L, rotated.get(0, 1));
        assertEquals(2L, rotated.get(1, 0));
        assertEquals(1L, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix rotated = m.rotate270();
        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3L, rotated.get(0, 0));
        assertEquals(6L, rotated.get(0, 1));
    }

    // ============ Transpose and Reshape Methods ============

    @Test
    public void testTranspose() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(4L, transposed.get(0, 1));
        assertEquals(6L, transposed.get(2, 1));
    }

    @Test
    public void testReshape() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1L, reshaped.get(0, 0));
        assertEquals(2L, reshaped.get(0, 1));
        assertEquals(6L, reshaped.get(2, 1));
    }

    @Test
    public void testRepelem() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(1L, result.get(0, 1));
        assertEquals(1L, result.get(1, 0));
        assertEquals(4L, result.get(3, 3));
    }

    @Test
    public void testRepmat() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix result = m.repmat(2, 2);
        assertEquals(2, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(2L, result.get(0, 1));
        assertEquals(1L, result.get(0, 2));
        assertEquals(1L, result.get(1, 0));
    }

    // ============ Flatten and FlatOp Methods ============

    @Test
    public void testFlatten() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongList flattened = m.flatten();
        assertEquals(4, flattened.size());
        assertEquals(1L, flattened.get(0));
        assertEquals(2L, flattened.get(1));
        assertEquals(3L, flattened.get(2));
        assertEquals(4L, flattened.get(3));
    }

    @Test
    public void testFlatOp() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        final long[] sum = { 0L };
        m.flatOp(row -> {
            for (long val : row) {
                sum[0] += val;
            }
        });
        assertEquals(10L, sum[0]);
    }

    // ============ Stack Methods ============

    @Test
    public void testVstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
        LongMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1L, stacked.get(0, 0));
        assertEquals(3L, stacked.get(1, 0));
    }

    @Test
    public void testVstack_incompatibleCols() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L, 5L } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L }, { 3L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 2L }, { 4L } });
        LongMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1L, stacked.get(0, 0));
        assertEquals(2L, stacked.get(0, 1));
    }

    @Test
    public void testHstack_incompatibleRows() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 2L }, { 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Methods ============

    @Test
    public void testAdd() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix result = m1.add(m2);
        assertEquals(11L, result.get(0, 0));
        assertEquals(22L, result.get(0, 1));
        assertEquals(33L, result.get(1, 0));
        assertEquals(44L, result.get(1, 1));
    }

    @Test
    public void testSubtract() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m1.subtract(m2);
        assertEquals(9L, result.get(0, 0));
        assertEquals(18L, result.get(0, 1));
        assertEquals(27L, result.get(1, 0));
        assertEquals(36L, result.get(1, 1));
    }

    @Test
    public void testMultiply() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix result = m1.multiply(m2);
        assertEquals(70, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(150, result.get(1, 0));
        assertEquals(220, result.get(1, 1));
    }

    // ============ Conversion Methods ============

    @Test
    public void testBoxed() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Matrix<Long> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(Long.valueOf(1L), boxed.get(0, 0));
        assertEquals(Long.valueOf(4L), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        IntMatrix result = m.mapToInt(l -> (int) l);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(4, result.get(1, 1));
    }

    // LongMatrix doesn't have mapToFloat method - removed test
    // @Test
    // public void testToFloatMatrix() {
    //     LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
    //     FloatMatrix result = m.mapToFloat(l -> (float) l);
    //     assertEquals(2, result.rows);
    //     assertEquals(2, result.cols);
    //     assertEquals(1.0f, result.get(0, 0), 0.0001f);
    //     assertEquals(4.0f, result.get(1, 1), 0.0001f);
    // }

    @Test
    public void testToDoubleMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix result = m.mapToDouble(l -> (double) l);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1.0, result.get(0, 0), 0.0001);
        assertEquals(4.0, result.get(1, 1), 0.0001);
    }

    // ============ ZipWith Methods ============

    @Test
    public void testZipWith_twoMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix result = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(11L, result.get(0, 0));
        assertEquals(22L, result.get(0, 1));
        assertEquals(33L, result.get(1, 0));
        assertEquals(44L, result.get(1, 1));
    }

    @Test
    public void testZipWith_threeMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 100L, 200L } });
        LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(111L, result.get(0, 0));
        assertEquals(222L, result.get(0, 1));
    }

    // ============ Stream Methods ============

    @Test
    public void testStreamLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);
    }

    @Test
    public void testStreamRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testStreamH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] all = m.streamH().toArray();
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);
    }

    @Test
    public void testStreamH_singleRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
    }

    @Test
    public void testStreamH_rowRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        long[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new long[] { 3L, 4L, 5L, 6L }, rows);
    }

    @Test
    public void testStreamV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] all = m.streamV().toArray();
        assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
    }

    @Test
    public void testStreamV_singleColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new long[] { 2L, 5L }, col1);
    }

    @Test
    public void testStreamV_columnRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] cols = m.streamV(0, 2).toArray();
        assertArrayEquals(new long[] { 1L, 4L, 2L, 5L }, cols);
    }

    // ============ Stream of Streams Methods ============

    @Test
    public void testStreamR() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        List<long[]> rows = m.streamR().map(LongStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new long[] { 1L, 2L }, rows.get(0));
        assertArrayEquals(new long[] { 3L, 4L }, rows.get(1));
    }

    @Test
    public void testStreamC() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        List<long[]> cols = m.streamC().map(LongStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new long[] { 1L, 3L }, cols.get(0));
        assertArrayEquals(new long[] { 2L, 4L }, cols.get(1));
    }

    // ============ Inherited Methods from AbstractMatrix ============

    @Test
    public void testIsEmpty() {
        assertTrue(LongMatrix.empty().isEmpty());
        assertFalse(LongMatrix.of(new long[][] { { 1L } }).isEmpty());
    }

    @Test
    public void testHashCode() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "not a matrix");
    }

    @Test
    public void testToString() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("4"));
    }

    @Test
    public void testToArray() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long[][] arr = m.array();
        assertEquals(2, arr.length);
        assertEquals(2, arr[0].length);
        assertArrayEquals(new long[] { 1L, 2L }, arr[0]);
        assertArrayEquals(new long[] { 3L, 4L }, arr[1]);
    }

    // ============ Edge Cases ============

    @Test
    public void testLargeDimensions() {
        long[][] data = new long[100][50];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                data[i][j] = i + j;
            }
        }
        LongMatrix m = LongMatrix.of(data);
        assertEquals(100, m.rows);
        assertEquals(50, m.cols);
        assertEquals(0L, m.get(0, 0));
        assertEquals(148L, m.get(99, 49));
    }

    @Test
    public void testSingleRowOperations() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L, 4L, 5L } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);

        LongMatrix transposed = m.transpose();
        assertEquals(5, transposed.rows);
        assertEquals(1, transposed.cols);
    }

    @Test
    public void testSingleColumnOperations() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L }, { 2L }, { 3L } });
        assertEquals(3, m.rows);
        assertEquals(1, m.cols);

        LongMatrix transposed = m.transpose();
        assertEquals(1, transposed.rows);
        assertEquals(3, transposed.cols);
    }

    @Test
    public void testChainedOperations() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m.map(x -> x * 2).transpose().map(x -> x + 1);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(3L, result.get(0, 0)); // (1*2)+1
        assertEquals(7L, result.get(0, 1)); // (3*2)+1
        assertEquals(5L, result.get(1, 0)); // (2*2)+1
        assertEquals(9L, result.get(1, 1)); // (4*2)+1
    }
}
