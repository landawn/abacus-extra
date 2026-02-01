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

@Tag("2511")
public class IntMatrix2511Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = new IntMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        IntMatrix m = new IntMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        IntMatrix m = new IntMatrix(new int[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        IntMatrix m = new IntMatrix(new int[][] { { 42 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42, m.get(0, 0));
    }

    @Test
    public void testConstructor_withLargeMatrix() {
        int[][] arr = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arr[i][j] = i * 10 + j;
            }
        }
        IntMatrix m = new IntMatrix(arr);
        assertEquals(10, m.rows);
        assertEquals(10, m.cols);
        assertEquals(0, m.get(0, 0));
        assertEquals(99, m.get(9, 9));
        assertEquals(55, m.get(5, 5));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        IntMatrix empty = IntMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(IntMatrix.empty(), IntMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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

    @Test
    public void testOf_withSingleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4, 5 } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    @Test
    public void testOf_withSingleColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 }, { 4 } });
        assertEquals(4, m.rows);
        assertEquals(1, m.cols);
    }

    // ============ Create Method Tests ============

    @Test
    public void testCreate_fromCharArray() {
        char[][] chars = { { 'A', 'B' }, { 'C', 'D' } };
        IntMatrix m = IntMatrix.from(chars);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(65, m.get(0, 0));   // ASCII 'A'
        assertEquals(68, m.get(1, 1));   // ASCII 'D'
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
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertNotNull(m.row(0));
    }

    @Test
    public void testRandom_zeroLength() {
        IntMatrix m = IntMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRepeat() {
        IntMatrix m = IntMatrix.repeat(42, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_zeroLength() {
        IntMatrix m = IntMatrix.repeat(42, 0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRange() {
        IntMatrix m = IntMatrix.range(0, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        IntMatrix m = IntMatrix.range(0, 10, 2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 0, 2, 4, 6, 8 }, m.row(0));
    }

    @Test
    public void testRange_negativeStep() {
        IntMatrix m = IntMatrix.range(10, 0, -2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 10, 8, 6, 4, 2 }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        IntMatrix m = IntMatrix.rangeClosed(0, 4);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        IntMatrix m = IntMatrix.rangeClosed(0, 10, 2);
        assertEquals(1, m.rows);
        assertEquals(6, m.cols);
        assertArrayEquals(new int[] { 0, 2, 4, 6, 8, 10 }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        IntMatrix m = IntMatrix.diagonalLU2RD(new int[] { 1, 2, 3 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        IntMatrix m = IntMatrix.diagonalRU2LD(new int[] { 1, 2, 3 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertEquals(0, m.get(0, 0));
    }

    @Test
    public void testDiagonal_bothDiagonals() {
        IntMatrix m = IntMatrix.diagonal(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(4, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void testDiagonal_differentLengths() {
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.diagonal(new int[] { 1, 2 }, new int[] { 1, 2, 3 }));
    }

    @Test
    public void testDiagonal_emptyBoth() {
        IntMatrix m = IntMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testUnbox() {
        Matrix<Integer> boxed = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m = IntMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    // ============ Get/Set Methods ============

    @Test
    public void testComponentType() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertEquals(int.class, m.componentType());
    }

    @Test
    public void testGet() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testGet_withPoint() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Point p = Point.of(1, 0);
        assertEquals(3, m.get(p));
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
    public void testSet_withPoint() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Point p = Point.of(1, 0);
        m.set(p, 99);
        assertEquals(99, m.get(1, 0));
    }

    // ============ Directional Methods (up, down, left, right) ============

    @Test
    public void testUpOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());

        OptionalInt noUp = m.upOf(0, 0);
        assertFalse(noUp.isPresent());
    }

    @Test
    public void testDownOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());

        OptionalInt noDown = m.downOf(1, 0);
        assertFalse(noDown.isPresent());
    }

    @Test
    public void testLeftOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());

        OptionalInt noLeft = m.leftOf(0, 0);
        assertFalse(noLeft.isPresent());
    }

    @Test
    public void testRightOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());

        OptionalInt noRight = m.rightOf(0, 1);
        assertFalse(noRight.isPresent());
    }

    // ============ Row/Column Operations ============

    @Test
    public void testRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new int[] { 1, 2, 3 }, m.row(0));
        assertArrayEquals(new int[] { 4, 5, 6 }, m.row(1));
    }

    @Test
    public void testRow_invalidIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new int[] { 1, 4 }, m.column(0));
        assertArrayEquals(new int[] { 2, 5 }, m.column(1));
        assertArrayEquals(new int[] { 3, 6 }, m.column(2));
    }

    @Test
    public void testColumn_invalidIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new int[] { 10, 20 });
        assertArrayEquals(new int[] { 10, 20 }, m.row(0));
        assertEquals(3, m.get(1, 0));
    }

    @Test
    public void testSetRow_invalidLength() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new int[] { 1, 2, 3 }));
    }

    @Test
    public void testSetColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setColumn(0, new int[] { 10, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(30, m.get(1, 0));
        assertEquals(2, m.get(0, 1));
    }

    @Test
    public void testSetColumn_invalidLength() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new int[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new int[] { 2, 4 }, m.row(0));
        assertArrayEquals(new int[] { 3, 4 }, m.row(1));
    }

    @Test
    public void testUpdateColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, x -> x * 10);
        assertEquals(10, m.get(0, 0));
        assertEquals(30, m.get(1, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(4, m.get(1, 1));
    }

    // ============ Diagonal Operations ============

    @Test
    public void testGetLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new int[] { 1, 5, 9 }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
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
    public void testSetLU2RD_invalidLength() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new int[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(x -> x * 10);
        assertEquals(10, m.get(0, 0));
        assertEquals(50, m.get(1, 1));
        assertEquals(90, m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new int[] { 3, 5, 7 }, m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new int[] { 30, 50, 70 });
        assertEquals(30, m.get(0, 2));
        assertEquals(50, m.get(1, 1));
        assertEquals(70, m.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(x -> x + 100);
        assertEquals(103, m.get(0, 2));
        assertEquals(105, m.get(1, 1));
        assertEquals(107, m.get(2, 0));
    }

    // ============ Update Methods ============

    @Test
    public void testUpdateAll_unary() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(x -> x * 2);
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll((i, j) -> i * 10 + j);
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_predicate() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.replaceIf(x -> x % 2 == 0, 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(99, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(99, m.get(1, 0));
    }

    @Test
    public void testReplaceIf_biPredicate() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf((i, j) -> i == j, 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(0, m.get(1, 1));
    }

    // ============ Map Methods ============

    @Test
    public void testMap() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.map(x -> x * 10);
        assertEquals(10, result.get(0, 0));
        assertEquals(20, result.get(0, 1));
        assertEquals(30, result.get(1, 0));
        assertEquals(40, result.get(1, 1));
        // Original should be unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testMapToLong() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.mapToLong(x -> x * 1000L);
        assertEquals(1000L, result.get(0, 0));
        assertEquals(4000L, result.get(1, 1));
    }

    @Test
    public void testMapToDouble() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.mapToDouble(x -> x * 0.5);
        assertEquals(0.5, result.get(0, 0), 0.0001);
        assertEquals(2.0, result.get(1, 1), 0.0001);
    }

    @Test
    public void testMapToObj() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> result = m.mapToObj(x -> "Value:" + x, String.class);
        assertEquals("Value:1", result.get(0, 0));
        assertEquals("Value:4", result.get(1, 1));
    }

    // ============ Fill Methods ============

    @Test
    public void testFill_value() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.fill(99);
        assertEquals(99, m.get(0, 0));
        assertEquals(99, m.get(0, 1));
        assertEquals(99, m.get(1, 0));
        assertEquals(99, m.get(1, 1));
    }

    @Test
    public void testFill_array() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.fill(new int[][] { { 10, 20 }, { 30, 40 } });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(0, 1));
        assertEquals(30, m.get(1, 0));
        assertEquals(40, m.get(1, 1));
    }

    @Test
    public void testFill_arrayWithOffset() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.fill(1, 1, new int[][] { { 99, 88 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(99, m.get(1, 1));
        assertEquals(88, m.get(1, 2));
    }

    // ============ Copy Methods ============

    @Test
    public void testCopy() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = m.copy();
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(1, copy.get(0, 0));

        // Verify it's a deep copy
        copy.set(0, 0, 99);
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testCopy_rowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(3, copy.get(0, 0));
        assertEquals(6, copy.get(1, 1));
    }

    @Test
    public void testCopy_fullRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix copy = m.copy(1, 2, 1, 3);
        assertEquals(1, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(5, copy.get(0, 0));
        assertEquals(6, copy.get(0, 1));
    }

    // ============ Extend Methods ============

    @Test
    public void testExtend_newSize() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(0, extended.get(2, 2));
    }

    @Test
    public void testExtend_withDefaultValue() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix extended = m.extend(2, 3, 99);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(99, extended.get(0, 2));
        assertEquals(99, extended.get(1, 0));
    }

    @Test
    public void testExtend_directions() {
        IntMatrix m = IntMatrix.of(new int[][] { { 5 } });
        IntMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5, extended.get(1, 1));
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionsWithDefault() {
        IntMatrix m = IntMatrix.of(new int[][] { { 5 } });
        IntMatrix extended = m.extend(1, 1, 1, 1, 9);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5, extended.get(1, 1));
        assertEquals(9, extended.get(0, 0));
        assertEquals(9, extended.get(2, 2));
    }

    // ============ Reverse and Flip Methods ============

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
        assertEquals(1, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(1, flipped.get(0, 2));
        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix flipped = m.flipV();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(1, flipped.get(1, 0));
        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    // ============ Rotation Methods ============

    @Test
    public void testRotate90() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate90();
        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(6, rotated.get(2, 0));
    }

    @Test
    public void testRotate180() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate270();
        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3, rotated.get(0, 0));
        assertEquals(6, rotated.get(0, 1));
    }

    // ============ Transpose and Reshape Methods ============

    @Test
    public void testTranspose() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(6, transposed.get(2, 1));
    }

    @Test
    public void testReshape() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testRepelem() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(4, result.get(3, 3));
    }

    @Test
    public void testRepmat() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix result = m.repmat(2, 2);
        assertEquals(2, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(1, result.get(1, 0));
    }

    // ============ Flatten and FlatOp Methods ============

    @Test
    public void testFlatten() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntList flattened = m.flatten();
        assertEquals(4, flattened.size());
        assertEquals(1, flattened.get(0));
        assertEquals(2, flattened.get(1));
        assertEquals(3, flattened.get(2));
        assertEquals(4, flattened.get(3));
    }

    @Test
    public void testFlatOp() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        final int[] sum = { 0 };
        m.flatOp(row -> {
            for (int val : row) {
                sum[0] += val;
            }
        });
        assertEquals(10, sum[0]);
    }

    // ============ Stack Methods ============

    @Test
    public void testVstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 } });
        IntMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(3, stacked.get(1, 0));
    }

    @Test
    public void testVstack_incompatibleCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 }, { 3 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2 }, { 4 } });
        IntMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
    }

    @Test
    public void testHstack_incompatibleRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2 }, { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Methods ============

    @Test
    public void testAdd() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
        IntMatrix result = m1.add(m2);
        assertEquals(11, result.get(0, 0));
        assertEquals(22, result.get(0, 1));
        assertEquals(33, result.get(1, 0));
        assertEquals(44, result.get(1, 1));
    }

    @Test
    public void testSubtract() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m1.subtract(m2);
        assertEquals(9, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(27, result.get(1, 0));
        assertEquals(36, result.get(1, 1));
    }

    @Test
    public void testMultiply() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
        IntMatrix result = m1.multiply(m2);
        assertEquals(70, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(150, result.get(1, 0));
        assertEquals(220, result.get(1, 1));
    }

    // ============ Conversion Methods ============

    @Test
    public void testBoxed() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(Integer.valueOf(1), boxed.get(0, 0));
        assertEquals(Integer.valueOf(4), boxed.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.mapToLong(i -> (long) i);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(4L, result.get(1, 1));
    }

    // IntMatrix doesn't have mapToFloat method - removed test
    // @Test
    // public void testToFloatMatrix() {
    //     IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
    //     FloatMatrix result = m.mapToFloat(i -> (float) i);
    //     assertEquals(2, result.rows);
    //     assertEquals(2, result.cols);
    //     assertEquals(1.0f, result.get(0, 0), 0.0001f);
    //     assertEquals(4.0f, result.get(1, 1), 0.0001f);
    // }

    @Test
    public void testToDoubleMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.mapToDouble(i -> (double) i);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1.0, result.get(0, 0), 0.0001);
        assertEquals(4.0, result.get(1, 1), 0.0001);
    }

    // ============ ZipWith Methods ============

    @Test
    public void testZipWith_twoMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
        IntMatrix result = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(11, result.get(0, 0));
        assertEquals(22, result.get(0, 1));
        assertEquals(33, result.get(1, 0));
        assertEquals(44, result.get(1, 1));
    }

    @Test
    public void testZipWith_threeMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 10, 20 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 100, 200 } });
        IntMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(111, result.get(0, 0));
        assertEquals(222, result.get(0, 1));
    }

    // ============ Stream Methods ============

    @Test
    public void testStreamLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new int[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testStreamRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new int[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testStreamH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] all = m.streamH().toArray();
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new int[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_rowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        int[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new int[] { 3, 4, 5, 6 }, rows);
    }

    @Test
    public void testStreamV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] all = m.streamV().toArray();
        assertArrayEquals(new int[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_singleColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new int[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_columnRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] cols = m.streamV(0, 2).toArray();
        assertArrayEquals(new int[] { 1, 4, 2, 5 }, cols);
    }

    // ============ Stream of Streams Methods ============

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
        List<int[]> cols = m.streamC().map(IntStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new int[] { 1, 3 }, cols.get(0));
        assertArrayEquals(new int[] { 2, 4 }, cols.get(1));
    }

    // ============ Inherited Methods from AbstractMatrix ============

    @Test
    public void testIsEmpty() {
        assertTrue(IntMatrix.empty().isEmpty());
        assertFalse(IntMatrix.of(new int[][] { { 1 } }).isEmpty());
    }

    @Test
    public void testHashCode() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 5 } });

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "not a matrix");
    }

    @Test
    public void testToString() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("4"));
    }

    @Test
    public void testToArray() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[][] arr = m.array();
        assertEquals(2, arr.length);
        assertEquals(2, arr[0].length);
        assertArrayEquals(new int[] { 1, 2 }, arr[0]);
        assertArrayEquals(new int[] { 3, 4 }, arr[1]);
    }

    // ============ Edge Cases ============

    @Test
    public void testLargeDimensions() {
        int[][] data = new int[100][50];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                data[i][j] = i + j;
            }
        }
        IntMatrix m = IntMatrix.of(data);
        assertEquals(100, m.rows);
        assertEquals(50, m.cols);
        assertEquals(0, m.get(0, 0));
        assertEquals(148, m.get(99, 49));
    }

    @Test
    public void testSingleRowOperations() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4, 5 } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);

        IntMatrix transposed = m.transpose();
        assertEquals(5, transposed.rows);
        assertEquals(1, transposed.cols);
    }

    @Test
    public void testSingleColumnOperations() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
        assertEquals(3, m.rows);
        assertEquals(1, m.cols);

        IntMatrix transposed = m.transpose();
        assertEquals(1, transposed.rows);
        assertEquals(3, transposed.cols);
    }

    @Test
    public void testChainedOperations() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.map(x -> x * 2).transpose().map(x -> x + 1);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(3, result.get(0, 0));   // (1*2)+1
        assertEquals(7, result.get(0, 1));   // (3*2)+1
        assertEquals(5, result.get(1, 0));   // (2*2)+1
        assertEquals(9, result.get(1, 1));   // (4*2)+1
    }
}
