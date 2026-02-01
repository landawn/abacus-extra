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
import com.landawn.abacus.util.u.OptionalInt;
import com.landawn.abacus.util.stream.IntStream;

@Tag("2510")
public class IntMatrix2510Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = new IntMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        IntMatrix m = new IntMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        IntMatrix m = new IntMatrix(new int[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        IntMatrix m = new IntMatrix(new int[][] { { 42 } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        IntMatrix empty = IntMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(IntMatrix.empty(), IntMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testOf_withNullArray() {
        IntMatrix m = IntMatrix.of((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withEmptyArray() {
        IntMatrix m = IntMatrix.of(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    // ============ Create Method Tests ============

    @Test
    public void testCreate_fromCharArray() {
        char[][] chars = { { 'A', 'B' }, { 'C', 'D' } };
        IntMatrix m = IntMatrix.from(chars);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(65, m.get(0, 0)); // ASCII 'A'
        assertEquals(68, m.get(1, 1)); // ASCII 'D'
    }

    @Test
    public void testCreate_fromCharArray_empty() {
        IntMatrix m = IntMatrix.from(new char[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromCharArray_null() {
        IntMatrix m = IntMatrix.from((char[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromCharArray_nullFirstRow() {
        char[][] chars = { null, { 'A', 'B' } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.from(chars));
    }

    @Test
    public void testCreate_fromCharArray_differentRowLengths() {
        char[][] chars = { { 'A', 'B' }, { 'C' } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.from(chars));
    }

    @Test
    public void testCreate_fromByteArray() {
        byte[][] bytes = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.from(bytes);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testCreate_fromByteArray_empty() {
        IntMatrix m = IntMatrix.from(new byte[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromByteArray_null() {
        IntMatrix m = IntMatrix.from((byte[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromByteArray_nullFirstRow() {
        byte[][] bytes = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.from(bytes));
    }

    @Test
    public void testCreate_fromByteArray_differentRowLengths() {
        byte[][] bytes = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.from(bytes));
    }

    @Test
    public void testCreate_fromShortArray() {
        short[][] shorts = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.from(shorts);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testCreate_fromShortArray_empty() {
        IntMatrix m = IntMatrix.from(new short[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromShortArray_null() {
        IntMatrix m = IntMatrix.from((short[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreate_fromShortArray_nullFirstRow() {
        short[][] shorts = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.from(shorts));
    }

    @Test
    public void testCreate_fromShortArray_differentRowLengths() {
        short[][] shorts = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.from(shorts));
    }

    @Test
    public void testRandom() {
        IntMatrix m = IntMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat() {
        IntMatrix m = IntMatrix.repeat(42, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        IntMatrix m = IntMatrix.range(0, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        IntMatrix m = IntMatrix.range(0, 10, 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new int[] { 0, 2, 4, 6, 8 }, m.row(0));
    }

    @Test
    public void testRange_withNegativeStep() {
        IntMatrix m = IntMatrix.range(10, 0, -2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new int[] { 10, 8, 6, 4, 2 }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        IntMatrix m = IntMatrix.rangeClosed(0, 4);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        IntMatrix m = IntMatrix.rangeClosed(0, 10, 2);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertArrayEquals(new int[] { 0, 2, 4, 6, 8, 10 }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        IntMatrix m = IntMatrix.diagonalLU2RD(new int[] { 1, 2, 3 });
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
        IntMatrix m = IntMatrix.diagonalRU2LD(new int[] { 1, 2, 3 });
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
        IntMatrix m = IntMatrix.diagonal(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
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
        IntMatrix m = IntMatrix.diagonal(new int[] { 1, 2, 3 }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        IntMatrix m = IntMatrix.diagonal(null, new int[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(4, m.get(0, 2));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        IntMatrix m = IntMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.diagonal(new int[] { 1, 2 }, new int[] { 3, 4, 5 }));
    }

    @Test
    public void testUnbox() {
        Integer[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> boxedMatrix = Matrix.of(boxed);
        IntMatrix unboxed = IntMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rowCount());
        assertEquals(2, unboxed.columnCount());
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(4, unboxed.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Integer[][] boxed = { { 1, null }, { null, 4 } };
        Matrix<Integer> boxedMatrix = Matrix.of(boxed);
        IntMatrix unboxed = IntMatrix.unbox(boxedMatrix);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1)); // null -> 0
        assertEquals(0, unboxed.get(1, 0)); // null -> 0
        assertEquals(4, unboxed.get(1, 1));
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertEquals(int.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(1, 2));
    }

    @Test
    public void testGet_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(Point.of(0, 0)));
        assertEquals(4, m.get(Point.of(1, 1)));
        assertEquals(2, m.get(Point.of(0, 1)));
    }

    @Test
    public void testSet() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.set(0, 0, 10);
        assertEquals(10, m.get(0, 0));

        m.set(1, 1, 20);
        assertEquals(20, m.get(1, 1));
    }

    @Test
    public void testSet_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0));
    }

    @Test
    public void testSetWithPoint() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.set(Point.of(0, 0), 50);
        assertEquals(50, m.get(Point.of(0, 0)));
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        OptionalInt up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());

        // Top row has no element above
        OptionalInt empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        OptionalInt down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());

        // Bottom row has no element below
        OptionalInt empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        OptionalInt left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());

        // Leftmost column has no element to the left
        OptionalInt empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        OptionalInt right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());

        // Rightmost column has no element to the right
        OptionalInt empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new int[] { 1, 2, 3 }, m.row(0));
        assertArrayEquals(new int[] { 4, 5, 6 }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new int[] { 1, 4 }, m.column(0));
        assertArrayEquals(new int[] { 2, 5 }, m.column(1));
        assertArrayEquals(new int[] { 3, 6 }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new int[] { 10, 20 });
        assertArrayEquals(new int[] { 10, 20 }, m.row(0));
        assertArrayEquals(new int[] { 3, 4 }, m.row(1)); // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new int[] { 1 }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new int[] { 1, 2, 3 }));
    }

    @Test
    public void testSetColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setColumn(0, new int[] { 10, 20 });
        assertArrayEquals(new int[] { 10, 20 }, m.column(0));
        assertArrayEquals(new int[] { 2, 4 }, m.column(1)); // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new int[] { 1 }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new int[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new int[] { 2, 4 }, m.row(0));
        assertArrayEquals(new int[] { 3, 4 }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, x -> x + 10);
        assertArrayEquals(new int[] { 11, 13 }, m.column(0));
        assertArrayEquals(new int[] { 2, 4 }, m.column(1)); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new int[] { 1, 5, 9 }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setLU2RD(new int[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new int[] { 1 }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new int[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(x -> x * 10);
        assertEquals(10, m.get(0, 0));
        assertEquals(50, m.get(1, 1));
        assertEquals(90, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> x * 2));
    }

    @Test
    public void testGetRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new int[] { 3, 5, 7 }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new int[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 2));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new int[] { 1 }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new int[] { 1, 2 }));
    }

    @Test
    public void testUpdateRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(x -> x * 10);
        assertEquals(30, m.get(0, 2));
        assertEquals(50, m.get(1, 1));
        assertEquals(70, m.get(2, 0));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> x * 2));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(x -> x * 2);
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withIndices() {
        IntMatrix m = IntMatrix.of(new int[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> i * 10 + j);
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.replaceIf(x -> x > 3, 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(0, m.get(1, 0)); // was 4
        assertEquals(0, m.get(1, 1)); // was 5
        assertEquals(0, m.get(1, 2)); // was 6
    }

    @Test
    public void testReplaceIf_withIndices() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.replaceIf((i, j) -> i == j, 0); // Replace diagonal
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.map(x -> x * 2);
        assertEquals(2, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(6, result.get(1, 0));
        assertEquals(8, result.get(1, 1));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1", result.get(0, 0));
        assertEquals("val:4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.fill(99);
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
                assertEquals(99, m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        IntMatrix m = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        int[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(patch);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(2, 2)); // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        IntMatrix m = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        int[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(1, 1, patch);
        assertEquals(0, m.get(0, 0)); // unchanged
        assertEquals(1, m.get(1, 1));
        assertEquals(2, m.get(1, 2));
        assertEquals(3, m.get(2, 1));
        assertEquals(4, m.get(2, 2));
    }

    @Test
    public void testFill_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[][] patch = { { 1, 2 }, { 3, 4 } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = m.copy();
        assertEquals(m.rowCount(), copy.rowCount());
        assertEquals(m.columnCount(), copy.columnCount());
        assertEquals(1, copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(99, copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix subset = m.copy(1, 3);
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals(4, subset.get(0, 0));
        assertEquals(9, subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals(2, submatrix.get(0, 0));
        assertEquals(6, submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(0, extended.get(3, 3)); // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
        assertEquals(1, truncated.get(0, 0));
        assertEquals(5, truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = m.extend(3, 3, -1);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(-1, extended.get(2, 2)); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, 0));
    }

    @Test
    public void testExtend_directional() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix extended = m.extend(1, 1, 2, 2);
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
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix extended = m.extend(1, 1, 1, 1, -1);
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
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void testReverseH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
        assertEquals(6, m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals(5, m.get(0, 0));
        assertEquals(6, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(1, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix flipped = m.flipV();
        assertEquals(5, flipped.get(0, 0));
        assertEquals(3, flipped.get(1, 0));
        assertEquals(1, flipped.get(2, 0));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate270();
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
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose();
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
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix transposed = m.transpose();
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
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i));
        }
    }

    @Test
    public void testReshape_back() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix reshaped = m.reshape(1, 9);
        IntMatrix reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        IntMatrix empty = IntMatrix.empty();
        IntMatrix reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
        assertEquals(2, repeated.get(0, 4));
        assertEquals(2, repeated.get(0, 5));
        assertEquals(1, repeated.get(1, 0)); // second row same as first
    }

    @Test
    public void testRepelem_invalidArguments() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2)); // repeat starts
        assertEquals(2, repeated.get(0, 3));

        assertEquals(3, repeated.get(1, 0));
        assertEquals(4, repeated.get(1, 1));
        assertEquals(3, repeated.get(1, 2)); // repeat
        assertEquals(4, repeated.get(1, 3));

        // Second vertical repeat
        assertEquals(1, repeated.get(2, 0));
        assertEquals(2, repeated.get(2, 1));
    }

    @Test
    public void testRepmat_invalidArguments() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntList flat = m.flatten();
        assertEquals(6, flat.size());
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, flat.array());
    }

    @Test
    public void testFlatten_empty() {
        IntMatrix m = IntMatrix.empty();
        IntList flat = m.flatten();
        assertEquals(0, flat.size());
    }

    // ============ FlatOp Tests ============

    @Test
    public void testFlatOp() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        final int[] count = { 0 };
        m.flatOp(row -> count[0] += row.length);
        assertEquals(4, count[0]);
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix stacked = m1.vstack(m2);
        assertEquals(4, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(4, stacked.get(1, 1));
        assertEquals(5, stacked.get(2, 0));
        assertEquals(8, stacked.get(3, 1));
    }

    @Test
    public void testVstack_differentColumnCount() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(5, stacked.get(0, 2));
        assertEquals(6, stacked.get(0, 3));
    }

    @Test
    public void testHstack_differentRowCount() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = m1.add(m2);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m1.subtract(m2);
        assertEquals(4, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(4, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 2, 3 }, { 4, 5 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m1.multiply(m2);
        assertEquals(11, result.get(0, 0));
        assertEquals(16, result.get(0, 1));
        assertEquals(19, result.get(1, 0));
        assertEquals(28, result.get(1, 1));
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> boxed = m.boxed();
        assertEquals(Integer.valueOf(1), boxed.get(0, 0));
        assertEquals(Integer.valueOf(4), boxed.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.toLongMatrix();
        assertEquals(1L, result.get(0, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix result = m.toFloatMatrix();
        assertEquals(1.0f, result.get(0, 0), 0.001f);
        assertEquals(4.0f, result.get(1, 1), 0.001f);
    }

    @Test
    public void testToDoubleMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(1.0, result.get(0, 0), 0.001);
        assertEquals(4.0, result.get(1, 1), 0.001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_twoMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testZipWith_twoMatrices_differentDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
    }

    @Test
    public void testZipWith_threeMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        IntMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(15, result.get(0, 0)); // 1 + 5 + 9
        assertEquals(18, result.get(0, 1)); // 2 + 6 + 10
        assertEquals(21, result.get(1, 0)); // 3 + 7 + 11
        assertEquals(24, result.get(1, 1)); // 4 + 8 + 12
    }

    @Test
    public void testZipWith_threeMatrices_differentDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new int[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.streamLU2RD().toArray());
    }

    @Test
    public void testStreamRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new int[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.streamRU2LD().toArray());
    }

    @Test
    public void testStreamH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] all = m.streamH().toArray();
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_withRowIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new int[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_withRowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new int[] { 4, 5, 6, 7, 8, 9 }, rows);
    }

    @Test
    public void testStreamH_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 2));
    }

    @Test
    public void testStreamV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] all = m.streamV().toArray();
        assertArrayEquals(new int[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_withColumnIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new int[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_withColumnRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] columnCount = m.streamV(1, 3).toArray();
        assertArrayEquals(new int[] { 2, 5, 3, 6 }, columnCount);
    }

    @Test
    public void testStreamV_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
    }

    // ============ Stream of Streams Tests ============

    @Test
    public void testStreamR() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<int[]> rows = m.streamR().map(IntStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new int[] { 1, 2 }, rows.get(0));
        assertArrayEquals(new int[] { 3, 4 }, rows.get(1));
    }

    @Test
    public void testStreamC() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<int[]> columnCount = m.streamC().map(IntStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new int[] { 1, 3 }, columnCount.get(0));
        assertArrayEquals(new int[] { 2, 4 }, columnCount.get(1));
    }

    // ============ Points Stream Tests ============

    @Test
    public void testPointsH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
        assertEquals(Point.of(0, 1), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    // ============ Sum Tests ============
    // Note: sumByInt(), sumByLong(), sumByDouble() methods don't exist in IntMatrix

    // ============ Average Tests ============
    // Note: averageByInt(), averageByLong(), averageByDouble() methods don't exist in IntMatrix

    // ============ Empty Matrix Tests ============

    @Test
    public void testIsEmpty_true() {
        IntMatrix empty = IntMatrix.empty();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testIsEmpty_false() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertFalse(m.isEmpty());
    }

    // ============ Equals and HashCode Tests ============

    @Test
    public void testEquals() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 5 } });

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    @Test
    public void testHashCode_equal() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        assertEquals(m1.hashCode(), m2.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
