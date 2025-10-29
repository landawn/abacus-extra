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

@Tag("2510")
public class LongMatrix2510Test extends TestBase {

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
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
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
    public void testRange_withNegativeStep() {
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
        assertEquals(0L, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        LongMatrix m = LongMatrix.diagonal(new long[] { 1L, 2L, 3L }, new long[] { 4L, 5L, 6L });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(4L, m.get(0, 2));
        assertEquals(6L, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        LongMatrix m = LongMatrix.diagonal(new long[] { 1L, 2L, 3L }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        LongMatrix m = LongMatrix.diagonal(null, new long[] { 4L, 5L, 6L });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(4L, m.get(0, 2));
        assertEquals(5L, m.get(1, 1));
        assertEquals(6L, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        LongMatrix m = LongMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonal(new long[] { 1L, 2L }, new long[] { 3L, 4L, 5L }));
    }

    @Test
    public void testUnbox() {
        Long[][] boxed = { { 1L, 2L }, { 3L, 4L } };
        Matrix<Long> boxedMatrix = Matrix.of(boxed);
        LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals(1L, unboxed.get(0, 0));
        assertEquals(4L, unboxed.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Long[][] boxed = { { 1L, null }, { null, 4L } };
        Matrix<Long> boxedMatrix = Matrix.of(boxed);
        LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
        assertEquals(1L, unboxed.get(0, 0));
        assertEquals(0L, unboxed.get(0, 1)); // null -> 0
        assertEquals(0L, unboxed.get(1, 0)); // null -> 0
        assertEquals(4L, unboxed.get(1, 1));
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
        assertEquals(long.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        assertEquals(1L, m.get(0, 0));
        assertEquals(5L, m.get(1, 1));
        assertEquals(6L, m.get(1, 2));
    }

    @Test
    public void testGet_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(1L, m.get(Point.of(0, 0)));
        assertEquals(4L, m.get(Point.of(1, 1)));
        assertEquals(2L, m.get(Point.of(0, 1)));
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
    public void testSet_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0L));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0L));
    }

    @Test
    public void testSetWithPoint() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.set(Point.of(0, 0), 50L);
        assertEquals(50L, m.get(Point.of(0, 0)));
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        OptionalLong up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1L, up.get());

        // Top row has no element above
        OptionalLong empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        OptionalLong down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3L, down.get());

        // Bottom row has no element below
        OptionalLong empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        OptionalLong left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1L, left.get());

        // Leftmost column has no element to the left
        OptionalLong empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        OptionalLong right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2L, right.get());

        // Rightmost column has no element to the right
        OptionalLong empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }


    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        assertArrayEquals(new long[] { 1L, 2L, 3L }, m.row(0));
        assertArrayEquals(new long[] { 4L, 5L, 6L }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        assertArrayEquals(new long[] { 1L, 4L }, m.column(0));
        assertArrayEquals(new long[] { 2L, 5L }, m.column(1));
        assertArrayEquals(new long[] { 3L, 6L }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setRow(0, new long[] { 10L, 20L });
        assertArrayEquals(new long[] { 10L, 20L }, m.row(0));
        assertArrayEquals(new long[] { 3L, 4L }, m.row(1)); // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L, 2L, 3L }));
    }

    @Test
    public void testSetColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setColumn(0, new long[] { 10L, 20L });
        assertArrayEquals(new long[] { 10L, 20L }, m.column(0));
        assertArrayEquals(new long[] { 2L, 4L }, m.column(1)); // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L, 2L, 3L }));
    }

    @Test
    public void testUpdateRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateRow(0, x -> x * 2L);
        assertArrayEquals(new long[] { 2L, 4L }, m.row(0));
        assertArrayEquals(new long[] { 3L, 4L }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateColumn(0, x -> x + 10L);
        assertArrayEquals(new long[] { 11L, 13L }, m.column(0));
        assertArrayEquals(new long[] { 2L, 4L }, m.column(1)); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        assertArrayEquals(new long[] { 1L, 5L, 9L }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
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
    public void testSetLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new long[] { 1L }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new long[] { 1L, 2L }));
    }

    @Test
    public void testUpdateLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.updateLU2RD(x -> x * 10L);
        assertEquals(10L, m.get(0, 0));
        assertEquals(50L, m.get(1, 1));
        assertEquals(90L, m.get(2, 2));
        assertEquals(2L, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> x * 2L));
    }

    @Test
    public void testGetRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        assertArrayEquals(new long[] { 3L, 5L, 7L }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.setRU2LD(new long[] { 10L, 20L, 30L });
        assertEquals(10L, m.get(0, 2));
        assertEquals(20L, m.get(1, 1));
        assertEquals(30L, m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new long[] { 1L }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new long[] { 1L, 2L }));
    }

    @Test
    public void testUpdateRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.updateRU2LD(x -> x * 10L);
        assertEquals(30L, m.get(0, 2));
        assertEquals(50L, m.get(1, 1));
        assertEquals(70L, m.get(2, 0));
        assertEquals(2L, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> x * 2L));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateAll(x -> x * 2L);
        assertEquals(2L, m.get(0, 0));
        assertEquals(4L, m.get(0, 1));
        assertEquals(6L, m.get(1, 0));
        assertEquals(8L, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withIndices() {
        LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L }, { 0L, 0L } });
        m.updateAll((i, j) -> (long) (i * 10 + j));
        assertEquals(0L, m.get(0, 0));
        assertEquals(1L, m.get(0, 1));
        assertEquals(10L, m.get(1, 0));
        assertEquals(11L, m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        m.replaceIf(x -> x > 3L, 0L);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(3L, m.get(0, 2));
        assertEquals(0L, m.get(1, 0)); // was 4
        assertEquals(0L, m.get(1, 1)); // was 5
        assertEquals(0L, m.get(1, 2)); // was 6
    }

    @Test
    public void testReplaceIf_withIndices() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.replaceIf((i, j) -> i == j, 0L); // Replace diagonal
        assertEquals(0L, m.get(0, 0));
        assertEquals(0L, m.get(1, 1));
        assertEquals(0L, m.get(2, 2));
        assertEquals(2L, m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m.map(x -> x * 2L);
        assertEquals(2L, result.get(0, 0));
        assertEquals(4L, result.get(0, 1));
        assertEquals(6L, result.get(1, 0));
        assertEquals(8L, result.get(1, 1));

        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1", result.get(0, 0));
        assertEquals("val:4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.fill(99L);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                assertEquals(99L, m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } });
        long[][] patch = { { 1L, 2L }, { 3L, 4L } };
        m.fill(patch);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(3L, m.get(1, 0));
        assertEquals(4L, m.get(1, 1));
        assertEquals(0L, m.get(2, 2)); // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } });
        long[][] patch = { { 1L, 2L }, { 3L, 4L } };
        m.fill(1, 1, patch);
        assertEquals(0L, m.get(0, 0)); // unchanged
        assertEquals(1L, m.get(1, 1));
        assertEquals(2L, m.get(1, 2));
        assertEquals(3L, m.get(2, 1));
        assertEquals(4L, m.get(2, 2));
    }

    @Test
    public void testFill_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long[][] patch = { { 1L, 2L }, { 3L, 4L } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals(1L, copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, 99L);
        assertEquals(1L, m.get(0, 0));
        assertEquals(99L, copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix subset = m.copy(1, 3);
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
        assertEquals(4L, subset.get(0, 0));
        assertEquals(9L, subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
        assertEquals(2L, submatrix.get(0, 0));
        assertEquals(6L, submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(4L, extended.get(1, 1));
        assertEquals(0L, extended.get(3, 3)); // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertEquals(1L, truncated.get(0, 0));
        assertEquals(5L, truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix extended = m.extend(3, 3, -1L);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(-1L, extended.get(2, 2)); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, 0L));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, 0L));
    }

    @Test
    public void testExtend_directional() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rows); // 1 + 3 + 1
        assertEquals(7, extended.cols); // 2 + 3 + 2

        // Original values at offset position
        assertEquals(1L, extended.get(1, 2));
        assertEquals(5L, extended.get(2, 3));

        // New cells are 0
        assertEquals(0L, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionalWithDefault() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix extended = m.extend(1, 1, 1, 1, -1L);
        assertEquals(5, extended.rows);
        assertEquals(5, extended.cols);

        // Check original values
        assertEquals(1L, extended.get(1, 1));

        // Check new values
        assertEquals(-1L, extended.get(0, 0));
        assertEquals(-1L, extended.get(4, 4));
    }

    @Test
    public void testExtend_directionalWithNegativeValues() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0L));
    }

    // ============ Reverse/Flip Tests ============

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
        assertEquals(3L, m.get(1, 0));
        assertEquals(1L, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix flipped = m.flipH();
        assertEquals(3L, flipped.get(0, 0));
        assertEquals(2L, flipped.get(0, 1));
        assertEquals(1L, flipped.get(0, 2));

        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix flipped = m.flipV();
        assertEquals(5L, flipped.get(0, 0));
        assertEquals(3L, flipped.get(1, 0));
        assertEquals(1L, flipped.get(2, 0));

        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3L, rotated.get(0, 0));
        assertEquals(1L, rotated.get(0, 1));
        assertEquals(4L, rotated.get(1, 0));
        assertEquals(2L, rotated.get(1, 1));
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
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2L, rotated.get(0, 0));
        assertEquals(4L, rotated.get(0, 1));
        assertEquals(1L, rotated.get(1, 0));
        assertEquals(3L, rotated.get(1, 1));
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(4L, transposed.get(0, 1));
        assertEquals(2L, transposed.get(1, 0));
        assertEquals(5L, transposed.get(1, 1));
        assertEquals(3L, transposed.get(2, 0));
        assertEquals(6L, transposed.get(2, 1));
    }

    @Test
    public void testTranspose_square() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(3L, transposed.get(0, 1));
        assertEquals(2L, transposed.get(1, 0));
        assertEquals(4L, transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i));
        }
    }

    @Test
    public void testReshape_back() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix reshaped = m.reshape(1, 9);
        LongMatrix reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        LongMatrix empty = LongMatrix.empty();
        LongMatrix reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1L, repeated.get(0, 0));
        assertEquals(1L, repeated.get(0, 1));
        assertEquals(1L, repeated.get(0, 2));
        assertEquals(2L, repeated.get(0, 3));
        assertEquals(2L, repeated.get(0, 4));
        assertEquals(2L, repeated.get(0, 5));
        assertEquals(1L, repeated.get(1, 0)); // second row same as first
    }

    @Test
    public void testRepelem_invalidArguments() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1L, repeated.get(0, 0));
        assertEquals(2L, repeated.get(0, 1));
        assertEquals(1L, repeated.get(0, 2)); // repeat starts
        assertEquals(2L, repeated.get(0, 3));

        assertEquals(3L, repeated.get(1, 0));
        assertEquals(4L, repeated.get(1, 1));
        assertEquals(3L, repeated.get(1, 2)); // repeat
        assertEquals(4L, repeated.get(1, 3));

        // Second vertical repeat
        assertEquals(1L, repeated.get(2, 0));
        assertEquals(2L, repeated.get(2, 1));
    }

    @Test
    public void testRepmat_invalidArguments() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongList flat = m.flatten();
        assertEquals(6, flat.size());
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, flat.array());
    }

    @Test
    public void testFlatten_empty() {
        LongMatrix m = LongMatrix.empty();
        LongList flat = m.flatten();
        assertEquals(0, flat.size());
    }

    // ============ FlatOp Tests ============

    @Test
    public void testFlatOp() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        final int[] count = { 0 };
        m.flatOp(row -> count[0] += row.length);
        assertEquals(4, count[0]);
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix stacked = m1.vstack(m2);
        assertEquals(4, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1L, stacked.get(0, 0));
        assertEquals(4L, stacked.get(1, 1));
        assertEquals(5L, stacked.get(2, 0));
        assertEquals(8L, stacked.get(3, 1));
    }

    @Test
    public void testVstack_differentColumnCount() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L, 5L } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals(1L, stacked.get(0, 0));
        assertEquals(2L, stacked.get(0, 1));
        assertEquals(5L, stacked.get(0, 2));
        assertEquals(6L, stacked.get(0, 3));
    }

    @Test
    public void testHstack_differentRowCount() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = m1.add(m2);
        assertEquals(6L, result.get(0, 0));
        assertEquals(8L, result.get(0, 1));
        assertEquals(10L, result.get(1, 0));
        assertEquals(12L, result.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m1.subtract(m2);
        assertEquals(4L, result.get(0, 0));
        assertEquals(4L, result.get(0, 1));
        assertEquals(4L, result.get(1, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 2L, 3L }, { 4L, 5L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m1.multiply(m2);
        assertEquals(11L, result.get(0, 0));
        assertEquals(16L, result.get(0, 1));
        assertEquals(19L, result.get(1, 0));
        assertEquals(28L, result.get(1, 1));
    }


    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Matrix<Long> boxed = m.boxed();
        assertEquals(Long.valueOf(1L), boxed.get(0, 0));
        assertEquals(Long.valueOf(4L), boxed.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        FloatMatrix result = m.toFloatMatrix();
        assertEquals(1.0f, result.get(0, 0), 0.001f);
        assertEquals(4.0f, result.get(1, 1), 0.001f);
    }

    @Test
    public void testToDoubleMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(1.0, result.get(0, 0), 0.001);
        assertEquals(4.0, result.get(1, 1), 0.001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_twoMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(6L, result.get(0, 0));
        assertEquals(8L, result.get(0, 1));
        assertEquals(10L, result.get(1, 0));
        assertEquals(12L, result.get(1, 1));
    }

    @Test
    public void testZipWith_twoMatrices_differentDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
    }

    @Test
    public void testZipWith_threeMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });
        LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(15L, result.get(0, 0)); // 1 + 5 + 9
        assertEquals(18L, result.get(0, 1)); // 2 + 6 + 10
        assertEquals(21L, result.get(1, 0)); // 3 + 7 + 11
        assertEquals(24L, result.get(1, 1)); // 4 + 8 + 12
    }

    @Test
    public void testZipWith_threeMatrices_differentDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.streamLU2RD().toArray());
    }

    @Test
    public void testStreamRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.streamRU2LD().toArray());
    }

    @Test
    public void testStreamH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] all = m.streamH().toArray();
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);
    }

    @Test
    public void testStreamH_withRowIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
    }

    @Test
    public void testStreamH_withRowRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new long[] { 4L, 5L, 6L, 7L, 8L, 9L }, rows);
    }

    @Test
    public void testStreamH_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 2));
    }

    @Test
    public void testStreamV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] all = m.streamV().toArray();
        assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
    }

    @Test
    public void testStreamV_withColumnIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new long[] { 2L, 5L }, col1);
    }

    @Test
    public void testStreamV_withColumnRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] cols = m.streamV(1, 3).toArray();
        assertArrayEquals(new long[] { 2L, 5L, 3L, 6L }, cols);
    }

    @Test
    public void testStreamV_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
    }

    // ============ Stream of Streams Tests ============

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

    // ============ Points Stream Tests ============

    @Test
    public void testPointsH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
        assertEquals(Point.of(0, 1), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    // ============ Sum Tests ============
    // Note: sumByLong(), sumByDouble() methods don't exist in LongMatrix

    // ============ Average Tests ============
    // Note: averageByLong(), averageByDouble() methods don't exist in LongMatrix

    // ============ Empty Matrix Tests ============

    @Test
    public void testIsEmpty_true() {
        LongMatrix empty = LongMatrix.empty();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testIsEmpty_false() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
        assertFalse(m.isEmpty());
    }

    // ============ Equals and HashCode Tests ============

    @Test
    public void testEquals() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    @Test
    public void testHashCode_equal() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        assertEquals(m1.hashCode(), m2.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
