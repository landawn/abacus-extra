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
import com.landawn.abacus.util.u.OptionalLong;
import com.landawn.abacus.util.stream.LongStream;

@Tag("2025")
public class LongMatrix2025Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix m = new LongMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        LongMatrix m = new LongMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        LongMatrix m = new LongMatrix(new long[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        LongMatrix m = new LongMatrix(new long[][] { { 42L } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42L, m.get(0, 0));
    }

    @Test
    public void testConstructor_withLargeValues() {
        long[][] arr = { { Long.MAX_VALUE, Long.MIN_VALUE }, { 1000000000000L, -1000000000000L } };
        LongMatrix m = new LongMatrix(arr);
        assertEquals(Long.MAX_VALUE, m.get(0, 0));
        assertEquals(Long.MIN_VALUE, m.get(0, 1));
        assertEquals(1000000000000L, m.get(1, 0));
        assertEquals(-1000000000000L, m.get(1, 1));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(LongMatrix.empty(), LongMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix m = LongMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
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
    public void testCreateFromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        LongMatrix m = LongMatrix.from(ints);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void testCreateFromIntArray_withNull() {
        LongMatrix m = LongMatrix.from((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withEmpty() {
        LongMatrix m = LongMatrix.from(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withJaggedArray() {
        int[][] jagged = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(jagged));
    }

    @Test
    public void testCreateFromIntArray_withNullRow() {
        int[][] nullRow = { { 1, 2 }, null };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(nullRow));
    }

    @Test
    public void testCreateFromIntArray_withNullFirstRow() {
        int[][] nullFirstRow = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(nullFirstRow));
    }

    @Test
    public void testCreateFromIntArray_withLargeInts() {
        int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 1000000, -1000000 } };
        LongMatrix m = LongMatrix.from(ints);
        assertEquals(Integer.MAX_VALUE, m.get(0, 0));
        assertEquals(Integer.MIN_VALUE, m.get(0, 1));
        assertEquals(1000000L, m.get(1, 0));
        assertEquals(-1000000L, m.get(1, 1));
    }

    @Test
    public void testRandom() {
        LongMatrix m = LongMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withRowsCols() {
        LongMatrix m = LongMatrix.random(2, 3);
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
        LongMatrix m = LongMatrix.repeat(1, 5, 42L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42L, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withRowsCols() {
        LongMatrix m = LongMatrix.repeat(2, 3, 42L);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(42L, m.get(i, j));
            }
        }
    }

    @Test
    public void testRepeat_withLargeValue() {
        LongMatrix m = LongMatrix.repeat(1, 3, Long.MAX_VALUE);
        assertEquals(1, m.rowCount());
        assertEquals(3, m.columnCount());
        for (int i = 0; i < 3; i++) {
            assertEquals(Long.MAX_VALUE, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        LongMatrix m = LongMatrix.range(0L, 5L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        LongMatrix m = LongMatrix.range(0L, 10L, 2L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.row(0));
    }

    @Test
    public void testRange_withNegativeStep() {
        LongMatrix m = LongMatrix.range(10L, 0L, -2L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new long[] { 10L, 8L, 6L, 4L, 2L }, m.row(0));
    }

    @Test
    public void testRange_withLargeValues() {
        LongMatrix m = LongMatrix.range(1000000000000L, 1000000000005L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new long[] { 1000000000000L, 1000000000001L, 1000000000002L, 1000000000003L, 1000000000004L }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        LongMatrix m = LongMatrix.diagonalLU2RD(new long[] { 1L, 2L, 3L });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(0L, m.get(0, 1));
        assertEquals(0L, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        LongMatrix m = LongMatrix.diagonalRU2LD(new long[] { 1L, 2L, 3L });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1L, m.get(0, 2));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 0));
        assertEquals(0L, m.get(0, 0));
        assertEquals(0L, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        LongMatrix m = LongMatrix.diagonal(new long[] { 1L, 2L, 3L }, new long[] { 4L, 5L, 6L });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(4L, m.get(0, 2));
        assertEquals(6L, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        LongMatrix m = LongMatrix.diagonal(new long[] { 1L, 2L, 3L }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        LongMatrix m = LongMatrix.diagonal(null, new long[] { 4L, 5L, 6L });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
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
        assertEquals(2, unboxed.rowCount());
        assertEquals(2, unboxed.columnCount());
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

    @Test
    public void testSet_withLargeValues() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.set(0, 0, Long.MAX_VALUE);
        m.set(1, 1, Long.MIN_VALUE);
        assertEquals(Long.MAX_VALUE, m.get(0, 0));
        assertEquals(Long.MIN_VALUE, m.get(1, 1));
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
    public void testMapToInt() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        IntMatrix result = m.mapToInt(x -> (int) (x * 10));
        assertEquals(10, result.get(0, 0));
        assertEquals(40, result.get(1, 1));
    }

    @Test
    public void testMapToDouble() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix result = m.mapToDouble(x -> x * 0.1);
        assertEquals(0.1, result.get(0, 0), 0.0001);
        assertEquals(0.4, result.get(1, 1), 0.0001);
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
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
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
        assertEquals(m.rowCount(), copy.rowCount());
        assertEquals(m.columnCount(), copy.columnCount());
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
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
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
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
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
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1L, extended.get(0, 0));
        assertEquals(4L, extended.get(1, 1));
        assertEquals(0L, extended.get(3, 3)); // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
        assertEquals(1L, truncated.get(0, 0));
        assertEquals(5L, truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix extended = m.extend(3, 3, -1L);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
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
        assertEquals(5, extended.rowCount()); // 1 + 3 + 1
        assertEquals(7, extended.columnCount()); // 2 + 3 + 2

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
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());

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
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3L, rotated.get(0, 0));
        assertEquals(1L, rotated.get(0, 1));
        assertEquals(4L, rotated.get(1, 0));
        assertEquals(2L, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4L, rotated.get(0, 0));
        assertEquals(3L, rotated.get(0, 1));
        assertEquals(2L, rotated.get(1, 0));
        assertEquals(1L, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(2L, rotated.get(0, 0));
        assertEquals(4L, rotated.get(0, 1));
        assertEquals(1L, rotated.get(1, 0));
        assertEquals(3L, rotated.get(1, 1));
    }

    @Test
    public void testRotate90_verticalMatrix() {
        // Test the rows > columnCount branch (3 rows, 2 columnCount)
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(3, rotated.columnCount());
        assertEquals(5L, rotated.get(0, 0));
        assertEquals(3L, rotated.get(0, 1));
        assertEquals(1L, rotated.get(0, 2));
        assertEquals(6L, rotated.get(1, 0));
        assertEquals(4L, rotated.get(1, 1));
        assertEquals(2L, rotated.get(1, 2));
    }

    @Test
    public void testRotate270_verticalMatrix() {
        // Test the rows > columnCount branch (3 rows, 2 columnCount)
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(3, rotated.columnCount());
        assertEquals(2L, rotated.get(0, 0));
        assertEquals(4L, rotated.get(0, 1));
        assertEquals(6L, rotated.get(0, 2));
        assertEquals(1L, rotated.get(1, 0));
        assertEquals(3L, rotated.get(1, 1));
        assertEquals(5L, rotated.get(1, 2));
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
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
        assertEquals(2, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(3L, transposed.get(0, 1));
        assertEquals(2L, transposed.get(1, 0));
    }

    @Test
    public void testTranspose_verticalMatrix() {
        // Test the rows > columnCount branch (3 rows, 2 columnCount)
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix transposed = m.transpose();
        assertEquals(2, transposed.rowCount());
        assertEquals(3, transposed.columnCount());
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(3L, transposed.get(0, 1));
        assertEquals(5L, transposed.get(0, 2));
        assertEquals(2L, transposed.get(1, 0));
        assertEquals(4L, transposed.get(1, 1));
        assertEquals(6L, transposed.get(1, 2));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1L, reshaped.get(0, i));
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
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

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
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1L, repeated.get(0, 0));
        assertEquals(2L, repeated.get(0, 1));
        assertEquals(1L, repeated.get(0, 2)); // repeat starts
        assertEquals(2L, repeated.get(0, 3));

        assertEquals(3L, repeated.get(1, 0));
        assertEquals(4L, repeated.get(1, 1));

        // Check vertical repeat
        assertEquals(1L, repeated.get(2, 0)); // vertical repeat starts
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
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongList flat = m.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1L, flat.get(i));
        }
    }

    @Test
    public void testFlatten_empty() {
        LongMatrix empty = LongMatrix.empty();
        LongList flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        List<Long> sums = new ArrayList<>();
        m.flatOp(row -> {
            long sum = 0;
            for (long val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertEquals(45L, sums.get(0).longValue());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 7L, 8L, 9L }, { 10L, 11L, 12L } });
        LongMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals(1L, stacked.get(0, 0));
        assertEquals(7L, stacked.get(2, 0));
        assertEquals(12L, stacked.get(3, 2));
    }

    @Test
    public void testVstack_differentColumnCounts() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1L, stacked.get(0, 0));
        assertEquals(5L, stacked.get(0, 2));
        assertEquals(8L, stacked.get(1, 3));
    }

    @Test
    public void testHstack_differentRowCounts() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix sum = m1.add(m2);

        assertEquals(6L, sum.get(0, 0));
        assertEquals(8L, sum.get(0, 1));
        assertEquals(10L, sum.get(1, 0));
        assertEquals(12L, sum.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testAdd_withLargeValues() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1000000000000L, 2000000000000L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3000000000000L, 4000000000000L } });
        LongMatrix sum = m1.add(m2);
        assertEquals(4000000000000L, sum.get(0, 0));
        assertEquals(6000000000000L, sum.get(0, 1));
    }

    @Test
    public void testSubtract() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix diff = m1.subtract(m2);

        assertEquals(4L, diff.get(0, 0));
        assertEquals(4L, diff.get(0, 1));
        assertEquals(4L, diff.get(1, 0));
        assertEquals(4L, diff.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix product = m1.multiply(m2);

        assertEquals(19L, product.get(0, 0)); // 1*5 + 2*7
        assertEquals(22L, product.get(0, 1)); // 1*6 + 2*8
        assertEquals(43L, product.get(1, 0)); // 3*5 + 4*7
        assertEquals(50L, product.get(1, 1)); // 3*6 + 4*8
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } }); // 1x3
        LongMatrix m2 = LongMatrix.of(new long[][] { { 4L }, { 5L }, { 6L } }); // 3x1
        LongMatrix product = m1.multiply(m2);

        assertEquals(1, product.rowCount());
        assertEquals(1, product.columnCount());
        assertEquals(32L, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
    }

    @Test
    public void testMultiply_withLargeValues() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1000000L, 2000000L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3000000L }, { 4000000L } });
        LongMatrix product = m1.multiply(m2);
        assertEquals(11000000000000L, product.get(0, 0)); // 1000000*3000000 + 2000000*4000000
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        Matrix<Long> boxed = m.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(3, boxed.columnCount());
        assertEquals(Long.valueOf(1L), boxed.get(0, 0));
        assertEquals(Long.valueOf(6L), boxed.get(1, 2));
    }

    @Test
    public void testToFloatMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(2, floatMatrix.rowCount());
        assertEquals(2, floatMatrix.columnCount());
        assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
        assertEquals(4.0f, floatMatrix.get(1, 1), 0.0001f);
    }

    @Test
    public void testToDoubleMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(2, doubleMatrix.rowCount());
        assertEquals(2, doubleMatrix.columnCount());
        assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
    }

    @Test
    public void testBoxed_verticalMatrix() {
        // Test the rows > columnCount branch (3 rows, 2 columnCount)
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        Matrix<Long> boxed = m.boxed();
        assertEquals(3, boxed.rowCount());
        assertEquals(2, boxed.columnCount());
        assertEquals(Long.valueOf(1L), boxed.get(0, 0));
        assertEquals(Long.valueOf(4L), boxed.get(1, 1));
        assertEquals(Long.valueOf(6L), boxed.get(2, 1));
    }

    @Test
    public void testBoxed_extremeValues() {
        LongMatrix m = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE }, { 0L, -1L } });
        Matrix<Long> boxed = m.boxed();
        assertEquals(Long.valueOf(Long.MAX_VALUE), boxed.get(0, 0));
        assertEquals(Long.valueOf(Long.MIN_VALUE), boxed.get(0, 1));
        assertEquals(Long.valueOf(0L), boxed.get(1, 0));
        assertEquals(Long.valueOf(-1L), boxed.get(1, 1));
    }

    @Test
    public void testToFloatMatrix_verticalMatrix() {
        // Test the rows > columnCount branch (4 rows, 2 columnCount)
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L }, { 7L, 8L } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(4, floatMatrix.rowCount());
        assertEquals(2, floatMatrix.columnCount());
        assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
        assertEquals(8.0f, floatMatrix.get(3, 1), 0.0001f);
    }

    @Test
    public void testToFloatMatrix_extremeValues() {
        // Test conversion with extreme long values - may lose precision
        LongMatrix m = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE }, { 1000000000000L, -1000000000000L } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        // Verify conversion happened (exact values may lose precision)
        assertNotNull(floatMatrix);
        assertEquals(2, floatMatrix.rowCount());
        assertEquals(2, floatMatrix.columnCount());
    }

    @Test
    public void testToDoubleMatrix_verticalMatrix() {
        // Test the rows > columnCount branch via create() (3 rows, 2 columnCount)
        LongMatrix m = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L }, { 50L, 60L } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(3, doubleMatrix.rowCount());
        assertEquals(2, doubleMatrix.columnCount());
        assertEquals(10.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(60.0, doubleMatrix.get(2, 1), 0.0001);
    }

    @Test
    public void testToDoubleMatrix_extremeValues() {
        // Test with extreme long values - check precision
        LongMatrix m = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE }, { 9007199254740992L, -9007199254740992L } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        // Double has 53 bits of precision, so very large longs may lose precision
        assertNotNull(doubleMatrix);
        assertEquals(2, doubleMatrix.rowCount());
        assertEquals(2, doubleMatrix.columnCount());
        // Verify the values are present (may not be exact due to precision)
        assertTrue(doubleMatrix.get(0, 0) > 0);
        assertTrue(doubleMatrix.get(0, 1) < 0);
    }

    @Test
    public void testUnbox_verticalMatrix() {
        // Test unbox with rows > columnCount (3 rows, 2 columnCount)
        Matrix<Long> boxedMatrix = Matrix.of(new Long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix m = LongMatrix.unbox(boxedMatrix);
        assertEquals(3, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1L, m.get(0, 0));
        assertEquals(6L, m.get(2, 1));
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = m1.zipWith(m2, (a, b) -> a * b);

        assertEquals(5L, result.get(0, 0)); // 1*5
        assertEquals(12L, result.get(0, 1)); // 2*6
        assertEquals(21L, result.get(1, 0)); // 3*7
        assertEquals(32L, result.get(1, 1)); // 4*8
    }

    @Test
    public void testZipWith_differentShapes() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
    }

    @Test
    public void testZipWith_threeMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });
        LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15L, result.get(0, 0)); // 1+5+9
        assertEquals(18L, result.get(0, 1)); // 2+6+10
        assertEquals(21L, result.get(1, 0)); // 3+7+11
        assertEquals(24L, result.get(1, 1)); // 4+8+12
    }

    @Test
    public void testZipWith_threeMatrices_differentShapes() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L, 11L } });
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
    public void testStreamLU2RD_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new long[] { 3L, 5L, 7L }, antiDiagonal);
    }

    @Test
    public void testStreamRU2LD_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.streamRU2LD().toArray().length);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] all = m.streamH().toArray();
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);
    }

    @Test
    public void testStreamH_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.streamH().toArray().length);
    }

    @Test
    public void testStreamH_withRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new long[] { 4L, 5L, 6L, 7L, 8L, 9L }, rows);
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] all = m.streamV().toArray();
        assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
    }

    @Test
    public void testStreamV_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.streamV().toArray().length);
    }

    @Test
    public void testStreamV_withColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new long[] { 2L, 5L }, col1);
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] columnCount = m.streamV(1, 3).toArray();
        assertArrayEquals(new long[] { 2L, 5L, 8L, 3L, 6L, 9L }, columnCount);
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        List<long[]> rows = m.streamR().map(LongStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new long[] { 1L, 2L, 3L }, rows.get(0));
        assertArrayEquals(new long[] { 4L, 5L, 6L }, rows.get(1));
    }

    @Test
    public void testStreamR_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        List<long[]> rows = m.streamR(1, 3).map(LongStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new long[] { 4L, 5L, 6L }, rows.get(0));
        assertArrayEquals(new long[] { 7L, 8L, 9L }, rows.get(1));
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        List<long[]> columnCount = m.streamC().map(LongStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new long[] { 1L, 4L }, columnCount.get(0));
        assertArrayEquals(new long[] { 2L, 5L }, columnCount.get(1));
        assertArrayEquals(new long[] { 3L, 6L }, columnCount.get(2));
    }

    @Test
    public void testStreamC_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        List<long[]> columnCount = m.streamC(1, 3).map(LongStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new long[] { 2L, 5L, 8L }, columnCount.get(0));
        assertArrayEquals(new long[] { 3L, 6L, 9L }, columnCount.get(1));
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        List<Long> values = new ArrayList<>();
        m.forEach(v -> values.add(v));
        assertEquals(9, values.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1L, values.get(i).longValue());
        }
    }

    @Test
    public void testForEach_withBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        List<Long> values = new ArrayList<>();
        m.forEach(1, 3, 1, 3, v -> values.add(v));
        assertEquals(4, values.size());
        assertEquals(5L, values.get(0).longValue());
        assertEquals(6L, values.get(1).longValue());
        assertEquals(8L, values.get(2).longValue());
        assertEquals(9L, values.get(3).longValue());
    }

    @Test
    public void testForEach_withBounds_outOfBounds() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, v -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, v -> {
        }));
    }

    // ============ Object Methods Tests ============

    @Test
    public void testPrintln() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        // Just ensure it doesn't throw
        m.println();

        LongMatrix empty = LongMatrix.empty();
        empty.println();
    }

    @Test
    public void testHashCode() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 4L, 3L } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 4L, 3L } });
        LongMatrix m4 = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    // ============ Statistical Operations Tests ============

    @Test
    public void testStatisticalOperations() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });

        // Test sum
        long totalSum = m.streamH().sum();
        assertEquals(45L, totalSum); // 1+2+3+4+5+6+7+8+9 = 45

        // Test sum of specific row
        long row1Sum = m.streamH(1).sum();
        assertEquals(15L, row1Sum); // 4+5+6 = 15

        // Test sum of specific column
        long col0Sum = m.streamV(0).sum();
        assertEquals(12L, col0Sum); // 1+4+7 = 12

        // Test min/max
        long min = m.streamH().min().orElse(0L);
        assertEquals(1L, min);

        long max = m.streamH().max().orElse(0L);
        assertEquals(9L, max);

        // Test average
        double avg = m.streamH().average().orElse(0.0);
        assertEquals(5.0, avg, 0.0001);

        // Test diagonal operations
        long diagonalSum = m.streamLU2RD().sum();
        assertEquals(15L, diagonalSum); // 1+5+9 = 15

        long antiDiagonalSum = m.streamRU2LD().sum();
        assertEquals(15L, antiDiagonalSum); // 3+5+7 = 15
    }

    @Test
    public void testRowColumnStatistics() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });

        // Test statistics on individual rows
        List<Long> rowSums = m.streamR().map(row -> row.sum()).toList();
        assertEquals(3, rowSums.size());
        assertEquals(6L, rowSums.get(0).longValue()); // 1+2+3
        assertEquals(15L, rowSums.get(1).longValue()); // 4+5+6
        assertEquals(24L, rowSums.get(2).longValue()); // 7+8+9

        // Test statistics on individual columns
        List<Long> colSums = m.streamC().map(col -> col.sum()).toList();
        assertEquals(3, colSums.size());
        assertEquals(12L, colSums.get(0).longValue()); // 1+4+7
        assertEquals(15L, colSums.get(1).longValue()); // 2+5+8
        assertEquals(18L, colSums.get(2).longValue()); // 3+6+9
    }

    // ============ Edge Case Tests ============

    @Test
    public void testEmptyMatrixOperations() {
        LongMatrix empty = LongMatrix.empty();

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rowCount);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        LongMatrix extended = empty.extend(2, 2, 5L);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(5L, extended.get(0, 0));
    }

    @Test
    public void testArithmeticEdgeCases() {
        // Test with zero matrix
        LongMatrix zeros = LongMatrix.of(new long[][] { { 0L, 0L }, { 0L, 0L } });
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        LongMatrix addZero = m.add(zeros);
        assertEquals(m, addZero);

        LongMatrix subtractZero = m.subtract(zeros);
        assertEquals(m, subtractZero);

        // Test multiplication with zero matrix
        LongMatrix multiplyZero = m.multiply(zeros);
        assertEquals(zeros, multiplyZero);

        // Test addition commutativity
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        assertEquals(m1.add(m2), m2.add(m1));

        // Test subtraction anti-commutativity
        LongMatrix diff1 = m1.subtract(m2);
        LongMatrix diff2 = m2.subtract(m1);
        assertEquals(diff1.get(0, 0), -diff2.get(0, 0));
        assertEquals(diff1.get(1, 1), -diff2.get(1, 1));
    }

    @Test
    public void testLargeMatrixArithmetic() {
        // Test with larger matrices
        long[][] arr1 = new long[10][10];
        long[][] arr2 = new long[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arr1[i][j] = i * 10 + j + 1;
                arr2[i][j] = (i * 10 + j + 1) * 2;
            }
        }

        LongMatrix large1 = LongMatrix.of(arr1);
        LongMatrix large2 = LongMatrix.of(arr2);

        // Test addition
        LongMatrix largeSum = large1.add(large2);
        assertEquals(3L, largeSum.get(0, 0)); // 1 + 2 = 3
        assertEquals(300L, largeSum.get(9, 9)); // 100 + 200 = 300

        // Test sum of all elements
        long totalSum = largeSum.streamH().sum();
        assertEquals(15150L, totalSum); // 3*(1+2+...+100) = 3*5050 = 15150
    }

    @Test
    public void testScalarOperationsWithMap() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

        // Scalar addition
        LongMatrix addScalar = m.map(x -> x + 10L);
        assertEquals(11L, addScalar.get(0, 0));
        assertEquals(14L, addScalar.get(1, 1));

        // Scalar multiplication
        LongMatrix multiplyScalar = m.map(x -> x * 3L);
        assertEquals(3L, multiplyScalar.get(0, 0));
        assertEquals(12L, multiplyScalar.get(1, 1));

        // Scalar division
        LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix divideScalar = m2.map(x -> x / 10L);
        assertEquals(1L, divideScalar.get(0, 0));
        assertEquals(4L, divideScalar.get(1, 1));
    }

    @Test
    public void testElementWiseMultiplyWithZipWith() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 3L }, { 4L, 5L } });

        // Element-wise multiplication
        LongMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(2L, elementWiseProduct.get(0, 0)); // 1*2
        assertEquals(6L, elementWiseProduct.get(0, 1)); // 2*3
        assertEquals(12L, elementWiseProduct.get(1, 0)); // 3*4
        assertEquals(20L, elementWiseProduct.get(1, 1)); // 4*5

        // Element-wise division
        LongMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
        assertEquals(2L, elementWiseDivision.get(0, 0)); // 2/1
        assertEquals(1L, elementWiseDivision.get(0, 1)); // 3/2 (integer division)
        assertEquals(1L, elementWiseDivision.get(1, 0)); // 4/3 (integer division)
        assertEquals(1L, elementWiseDivision.get(1, 1)); // 5/4 (integer division)
    }

    // ============ Long Overflow Tests ============

    @Test
    public void testLongOverflow() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { Long.MAX_VALUE, 1L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, Long.MAX_VALUE } });

        // Addition overflow
        LongMatrix sum = m1.add(m2);
        // Overflow wraps around to negative
        assertTrue(sum.get(0, 0) < 0);

        // Test max and min values
        LongMatrix extremes = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE } });
        assertEquals(Long.MAX_VALUE, extremes.get(0, 0));
        assertEquals(Long.MIN_VALUE, extremes.get(0, 1));
    }

    @Test
    public void testNegativeValues() {
        LongMatrix m = LongMatrix.of(new long[][] { { -1L, -2L }, { -3L, -4L } });

        assertEquals(-1L, m.get(0, 0));
        assertEquals(-4L, m.get(1, 1));

        // Test operations with negative values
        LongMatrix doubled = m.map(x -> x * 2L);
        assertEquals(-2L, doubled.get(0, 0));
        assertEquals(-8L, doubled.get(1, 1));
    }

    @Test
    public void testVeryLargeValues() {
        long largeValue = 1_000_000_000_000L; // 1 trillion
        LongMatrix m = LongMatrix.of(new long[][] { { largeValue, largeValue * 2 }, { largeValue * 3, largeValue * 4 } });

        assertEquals(largeValue, m.get(0, 0));
        assertEquals(largeValue * 2, m.get(0, 1));
        assertEquals(largeValue * 3, m.get(1, 0));
        assertEquals(largeValue * 4, m.get(1, 1));

        // Test operations with large values
        LongMatrix doubled = m.map(x -> x * 2L);
        assertEquals(largeValue * 2, doubled.get(0, 0));
        assertEquals(largeValue * 8, doubled.get(1, 1));
    }

    @Test
    public void testConversionFromIntWithMaxMin() {
        int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 0, -1 } };
        LongMatrix m = LongMatrix.from(ints);

        // Verify proper conversion
        assertEquals(Integer.MAX_VALUE, m.get(0, 0));
        assertEquals(Integer.MIN_VALUE, m.get(0, 1));
        assertEquals(0L, m.get(1, 0));
        assertEquals(-1L, m.get(1, 1));

        // Verify that long values are different from int values (no truncation)
        assertTrue(m.get(0, 0) < Long.MAX_VALUE);
        assertTrue(m.get(0, 1) > Long.MIN_VALUE);
    }

    @Test
    public void testLargeValueArithmetic() {
        // Test addition with large values
        long billion = 1_000_000_000L;
        LongMatrix m1 = LongMatrix.of(new long[][] { { billion * 100, billion * 200 } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { billion * 300, billion * 400 } });

        LongMatrix sum = m1.add(m2);
        assertEquals(billion * 400, sum.get(0, 0));
        assertEquals(billion * 600, sum.get(0, 1));

        // Test subtraction with large values
        LongMatrix diff = m2.subtract(m1);
        assertEquals(billion * 200, diff.get(0, 0));
        assertEquals(billion * 200, diff.get(0, 1));
    }

    @Test
    public void testRangeWithLargeStep() {
        LongMatrix m = LongMatrix.range(0L, 10000000000L, 2000000000L);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new long[] { 0L, 2000000000L, 4000000000L, 6000000000L, 8000000000L }, m.row(0));
    }
}
