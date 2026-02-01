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
import com.landawn.abacus.util.u.OptionalShort;
import com.landawn.abacus.util.stream.ShortStream;

@Tag("2511")
public class ShortMatrix2511Test extends TestBase {

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
    public void testConstructor_withNullArray() {
        ShortMatrix m = new ShortMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        ShortMatrix m = new ShortMatrix(new short[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
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

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(ShortMatrix.empty(), ShortMatrix.empty());
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
        assertNotNull(m.row(0));
    }

    @Test
    public void testRandom_zeroLength() {
        ShortMatrix m = ShortMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRepeat() {
        ShortMatrix m = ShortMatrix.repeat((short) 42, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals((short) 42, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_zeroLength() {
        ShortMatrix m = ShortMatrix.repeat((short) 42, 0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRange() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new short[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new short[] { 0, 2, 4, 6, 8 }, m.row(0));
    }

    @Test
    public void testRange_negativeStep() {
        ShortMatrix m = ShortMatrix.range((short) 10, (short) 0, (short) -2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new short[] { 10, 8, 6, 4, 2 }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 4);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new short[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertArrayEquals(new short[] { 0, 2, 4, 6, 8, 10 }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        ShortMatrix m = ShortMatrix.diagonalLU2RD(new short[] { 1, 2, 3 });
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
        ShortMatrix m = ShortMatrix.diagonalRU2LD(new short[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals((short) 1, m.get(0, 2));
        assertEquals((short) 2, m.get(1, 1));
        assertEquals((short) 3, m.get(2, 0));
        assertEquals((short) 0, m.get(0, 0));
    }

    @Test
    public void testDiagonal_bothDiagonals() {
        ShortMatrix m = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
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
        assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonal(new short[] { 1, 2 }, new short[] { 1, 2, 3 }));
    }

    @Test
    public void testDiagonal_emptyBoth() {
        ShortMatrix m = ShortMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
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

    // ============ Get/Set Methods ============

    @Test
    public void testComponentType() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
        assertEquals(short.class, m.componentType());
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
        OptionalShort up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals((short) 1, up.get());

        OptionalShort noUp = m.upOf(0, 0);
        assertFalse(noUp.isPresent());
    }

    @Test
    public void testDownOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals((short) 3, down.get());

        OptionalShort noDown = m.downOf(1, 0);
        assertFalse(noDown.isPresent());
    }

    @Test
    public void testLeftOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals((short) 1, left.get());

        OptionalShort noLeft = m.leftOf(0, 0);
        assertFalse(noLeft.isPresent());
    }

    @Test
    public void testRightOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals((short) 2, right.get());

        OptionalShort noRight = m.rightOf(0, 1);
        assertFalse(noRight.isPresent());
    }

    // ============ Row/Column Operations ============

    @Test
    public void testRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new short[] { 1, 2, 3 }, m.row(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, m.row(1));
    }

    @Test
    public void testRow_invalidIndex() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new short[] { 1, 4 }, m.column(0));
        assertArrayEquals(new short[] { 2, 5 }, m.column(1));
        assertArrayEquals(new short[] { 3, 6 }, m.column(2));
    }

    @Test
    public void testColumn_invalidIndex() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new short[] { 10, 20 });
        assertArrayEquals(new short[] { 10, 20 }, m.row(0));
        assertEquals((short) 3, m.get(1, 0));
    }

    @Test
    public void testSetRow_invalidLength() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2, 3 }));
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
    public void testSetColumn_invalidLength() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, x -> (short) (x * 2));
        assertArrayEquals(new short[] { 2, 4 }, m.row(0));
        assertArrayEquals(new short[] { 3, 4 }, m.row(1));
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

    // ============ Diagonal Operations ============

    @Test
    public void testGetLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new short[] { 1, 5, 9 }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setLU2RD(new short[] { 10, 20, 30 });
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 20, m.get(1, 1));
        assertEquals((short) 30, m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_invalidLength() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new short[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(x -> (short) (x * 10));
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 50, m.get(1, 1));
        assertEquals((short) 90, m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new short[] { 3, 5, 7 }, m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new short[] { 30, 50, 70 });
        assertEquals((short) 30, m.get(0, 2));
        assertEquals((short) 50, m.get(1, 1));
        assertEquals((short) 70, m.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(x -> (short) (x + 100));
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
        m.fill(new short[][] { { 10, 20 }, { 30, 40 } });
        assertEquals((short) 10, m.get(0, 0));
        assertEquals((short) 20, m.get(0, 1));
        assertEquals((short) 30, m.get(1, 0));
        assertEquals((short) 40, m.get(1, 1));
    }

    @Test
    public void testFill_arrayWithOffset() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.fill(1, 1, new short[][] { { 99, 88 } });
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
        ShortMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals((short) 1, extended.get(0, 0));
        assertEquals((short) 0, extended.get(2, 2));
    }

    @Test
    public void testExtend_withDefaultValue() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix extended = m.extend(2, 3, (short) 99);
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
        m.reverseH();
        assertEquals((short) 3, m.get(0, 0));
        assertEquals((short) 2, m.get(0, 1));
        assertEquals((short) 1, m.get(0, 2));
        assertEquals((short) 6, m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals((short) 5, m.get(0, 0));
        assertEquals((short) 6, m.get(0, 1));
        assertEquals((short) 1, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix flipped = m.flipH();
        assertEquals((short) 3, flipped.get(0, 0));
        assertEquals((short) 1, flipped.get(0, 2));
        // Original unchanged
        assertEquals((short) 1, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix flipped = m.flipV();
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
        ShortMatrix result = m.repelem(2, 2);
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
        ShortMatrix result = m.repmat(2, 2);
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
        m.flatOp(row -> {
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
        ShortMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 3, stacked.get(1, 0));
    }

    @Test
    public void testVstack_incompatibleCols() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 }, { 3 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 }, { 4 } });
        ShortMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals((short) 1, stacked.get(0, 0));
        assertEquals((short) 2, stacked.get(0, 1));
    }

    @Test
    public void testHstack_incompatibleRows() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 }, { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
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
    public void testStreamLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testStreamRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] diagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new short[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testStreamH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] all = m.streamH().toArray();
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_singleRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_rowRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        short[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new short[] { 3, 4, 5, 6 }, rows);
    }

    @Test
    public void testStreamV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] all = m.streamV().toArray();
        assertArrayEquals(new short[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_singleColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new short[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_columnRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] columnCount = m.streamV(0, 2).toArray();
        assertArrayEquals(new short[] { 1, 4, 2, 5 }, columnCount);
    }

    // ============ Stream of Streams Methods ============

    @Test
    public void testStreamR() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<short[]> rows = m.streamR().map(ShortStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new short[] { 1, 2 }, rows.get(0));
        assertArrayEquals(new short[] { 3, 4 }, rows.get(1));
    }

    @Test
    public void testStreamC() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<short[]> columnCount = m.streamC().map(ShortStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new short[] { 1, 3 }, columnCount.get(0));
        assertArrayEquals(new short[] { 2, 4 }, columnCount.get(1));
    }

    // ============ Inherited Methods from AbstractMatrix ============

    @Test
    public void testIsEmpty() {
        assertTrue(ShortMatrix.empty().isEmpty());
        assertFalse(ShortMatrix.of(new short[][] { { 1 } }).isEmpty());
    }

    @Test
    public void testHashCode() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1.hashCode(), m2.hashCode());
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
        short[][] arr = m.array();
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
