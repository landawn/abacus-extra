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
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.stream.Stream;

@Tag("2510")
public class ByteMatrix2510Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        ByteMatrix m = new ByteMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        ByteMatrix m = new ByteMatrix(new byte[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        ByteMatrix m = new ByteMatrix(new byte[][] { { 42 } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42, m.get(0, 0));
    }

    @Test
    public void testConstructor_withNonSquareMatrix() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
    }

    @Test
    public void testConstructor_withNegativeValues() {
        byte[][] arr = { { -1, -2 }, { -3, -4 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(-1, m.get(0, 0));
        assertEquals(-4, m.get(1, 1));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
        assertSame(ByteMatrix.empty(), ByteMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testOf_withNullArray() {
        ByteMatrix m = ByteMatrix.of((byte[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withEmptyArray() {
        ByteMatrix m = ByteMatrix.of(new byte[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testRandom() {
        ByteMatrix m = ByteMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withZeroLength() {
        ByteMatrix m = ByteMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRepeat() {
        ByteMatrix m = ByteMatrix.repeat((byte) 42, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withZero() {
        ByteMatrix m = ByteMatrix.repeat((byte) 0, 3);
        assertEquals(1, m.rowCount());
        assertEquals(3, m.columnCount());
        for (int i = 0; i < 3; i++) {
            assertEquals(0, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        ByteMatrix m = ByteMatrix.range((byte) 1, (byte) 5);
        assertEquals(1, m.rowCount());
        assertEquals(4, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(4, m.get(0, 3));
    }

    @Test
    public void testRange_withStep() {
        ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(0, 3));
        assertEquals(8, m.get(0, 4));
    }

    @Test
    public void testRange_emptyRange() {
        ByteMatrix m = ByteMatrix.range((byte) 5, (byte) 5);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRangeClosed() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
        assertEquals(1, m.rowCount());
        assertEquals(4, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(0, 3));
    }

    @Test
    public void testRangeClosed_withStep() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 3);
        assertEquals(1, m.rowCount());
        assertEquals(4, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(3, m.get(0, 1));
        assertEquals(6, m.get(0, 2));
        assertEquals(9, m.get(0, 3));
    }

    @Test
    public void testRangeClosed_singleElement() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 5, (byte) 5);
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(5, m.get(0, 0));
    }

    @Test
    public void testDiagonalLU2RD() {
        ByteMatrix m = ByteMatrix.diagonalLU2RD(new byte[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_empty() {
        ByteMatrix m = ByteMatrix.diagonalLU2RD(new byte[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalRU2LD() {
        ByteMatrix m = ByteMatrix.diagonalRU2LD(new byte[] { 1, 2, 3 });
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
        ByteMatrix m = ByteMatrix.diagonal(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
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
        ByteMatrix m = ByteMatrix.diagonal(new byte[] { 1, 2, 3 }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        ByteMatrix m = ByteMatrix.diagonal(null, new byte[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(4, m.get(0, 2));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        ByteMatrix m = ByteMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonal(new byte[] { 1, 2 }, new byte[] { 1, 2, 3 }));
    }

    @Test
    public void testUnbox() {
        Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rowCount());
        assertEquals(2, unboxed.columnCount());
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(2, unboxed.get(0, 1));
    }

    @Test
    public void testUnbox_withNullElements() {
        Byte[][] boxed = { { 1, null }, { null, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rowCount());
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1));
        assertEquals(0, unboxed.get(1, 0));
    }

    // ============ Component Type Test ============

    @Test
    public void testComponentType() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertEquals(byte.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet_byIndices() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testGet_byPoint() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        assertEquals(1, m.get(Point.of(0, 0)));
        assertEquals(2, m.get(Point.of(0, 1)));
        assertEquals(3, m.get(Point.of(1, 0)));
        assertEquals(4, m.get(Point.of(1, 1)));
    }

    @Test
    public void testSet_byIndices() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.set(0, 0, (byte) 1);
        m.set(1, 1, (byte) 4);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void testSet_byPoint() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.set(Point.of(0, 0), (byte) 1);
        m.set(Point.of(1, 1), (byte) 4);
        assertEquals(1, m.get(Point.of(0, 0)));
        assertEquals(4, m.get(Point.of(1, 1)));
        assertEquals(0, m.get(Point.of(0, 1)));
    }

    // ============ Directional Access Tests ============

    @Test
    public void testUpOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());
    }

    @Test
    public void testUpOf_atTopEdge() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void testDownOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());
    }

    @Test
    public void testDownOf_atBottomEdge() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void testLeftOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());
    }

    @Test
    public void testLeftOf_atLeftEdge() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testRightOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());
    }

    @Test
    public void testRightOf_atRightEdge() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void testRow() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix m = ByteMatrix.of(arr);
        byte[] row0 = m.row(0);
        assertArrayEquals(new byte[] { 1, 2, 3 }, row0);
        byte[] row1 = m.row(1);
        assertArrayEquals(new byte[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testRow_invalidIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testColumn() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix m = ByteMatrix.of(arr);
        byte[] col0 = m.column(0);
        assertArrayEquals(new byte[] { 1, 4 }, col0);
        byte[] col1 = m.column(1);
        assertArrayEquals(new byte[] { 2, 5 }, col1);
    }

    @Test
    public void testColumn_invalidIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 } });
        m.setRow(0, new byte[] { 1, 2, 3 });
        assertArrayEquals(new byte[] { 1, 2, 3 }, m.row(0));
        assertArrayEquals(new byte[] { 0, 0, 0 }, m.row(1));
    }

    @Test
    public void testSetRow_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1, 2 }));
    }

    @Test
    public void testSetColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 }, { 0, 0 } });
        m.setColumn(0, new byte[] { 1, 2, 3 });
        assertArrayEquals(new byte[] { 1, 2, 3 }, m.column(0));
        assertArrayEquals(new byte[] { 0, 0, 0 }, m.column(1));
    }

    @Test
    public void testSetColumn_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1 }));
    }

    @Test
    public void testUpdateRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.updateRow(0, val -> (byte) (val * 2));
        assertArrayEquals(new byte[] { 2, 4, 6 }, m.row(0));
        assertArrayEquals(new byte[] { 4, 5, 6 }, m.row(1));
    }

    @Test
    public void testUpdateColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.updateColumn(0, val -> (byte) (val + 10));
        assertArrayEquals(new byte[] { 11, 13, 15 }, m.column(0));
        assertArrayEquals(new byte[] { 2, 4, 6 }, m.column(1));
    }

    // ============ Diagonal Access Tests ============

    @Test
    public void testGetLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diag = m.getLU2RD();
        assertArrayEquals(new byte[] { 1, 5, 9 }, diag);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        m.setLU2RD(new byte[] { 1, 2, 3 });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
    }

    @Test
    public void testSetLU2RD_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[3][3]);
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 0, 0 }, { 0, 2, 0 }, { 0, 0, 3 } });
        m.updateLU2RD(val -> (byte) (val * 10));
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diag = m.getRU2LD();
        assertArrayEquals(new byte[] { 3, 5, 7 }, diag);
    }

    @Test
    public void testSetRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        m.setRU2LD(new byte[] { 1, 2, 3 });
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertEquals(0, m.get(0, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 0, 2, 0 }, { 3, 0, 0 } });
        m.updateRU2LD(val -> (byte) (val * 5));
        assertEquals(5, m.get(0, 2));
        assertEquals(10, m.get(1, 1));
        assertEquals(15, m.get(2, 0));
    }

    // ============ Update/Replace Tests ============

    @Test
    public void testUpdateAll_unary() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(val -> (byte) (val * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll((i, j) -> (byte) (i + j));
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(1, m.get(1, 0));
        assertEquals(2, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_predicate() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf(val -> val > 2, (byte) 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
        assertEquals(0, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_biPredicate() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf((i, j) -> i == j, (byte) 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(0, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void testMap() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix mapped = m.map(val -> (byte) (val * 10));
        assertEquals(10, mapped.get(0, 0));
        assertEquals(20, mapped.get(0, 1));
        assertEquals(30, mapped.get(1, 0));
        assertEquals(40, mapped.get(1, 1));
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> mapped = m.mapToObj(val -> "V" + val, String.class);
        assertEquals("V1", mapped.get(0, 0));
        assertEquals("V2", mapped.get(0, 1));
        assertEquals("V3", mapped.get(1, 0));
        assertEquals("V4", mapped.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_singleValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.fill((byte) 5);
        assertEquals(5, m.get(0, 0));
        assertEquals(5, m.get(0, 1));
        assertEquals(5, m.get(1, 0));
        assertEquals(5, m.get(1, 1));
    }

    @Test
    public void testFill_withArray() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.fill(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testFill_withOffset() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        m.fill(1, 1, new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(1, 1));
        assertEquals(4, m.get(2, 2));
    }

    @Test
    public void testFill_withOffset_invalidPosition() {
        ByteMatrix m = ByteMatrix.of(new byte[2][2]);
        assertThrows(IllegalArgumentException.class, () -> m.fill(3, 3, new byte[][] { { 1, 2 }, { 3, 4 } }));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix copy = m.copy();
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(1, copy.get(0, 0));
        assertEquals(2, copy.get(0, 1));
        copy.set(0, 0, (byte) 99);
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ByteMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(3, copy.get(0, 0));
        assertEquals(4, copy.get(0, 1));
    }

    @Test
    public void testCopy_withRowRange_invalidRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2));
    }

    @Test
    public void testCopy_withFullRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(5, copy.get(0, 0));
        assertEquals(6, copy.get(0, 1));
        assertEquals(8, copy.get(1, 0));
        assertEquals(9, copy.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_invalidRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, -1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_withDefaultValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = m.extend(2, 3);
        assertEquals(2, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
        assertEquals(0, extended.get(0, 2));
        assertEquals(0, extended.get(1, 0));
    }

    @Test
    public void testExtend_withCustomDefaultValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = m.extend(2, 3, (byte) 9);
        assertEquals(2, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
        assertEquals(9, extended.get(0, 2));
        assertEquals(9, extended.get(1, 0));
    }

    @Test
    public void testExtend_withDirections() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 5 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(5, extended.get(1, 1));
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtend_withDirectionsAndValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 5 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) 7);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(5, extended.get(1, 1));
        assertEquals(7, extended.get(0, 0));
    }

    @Test
    public void testExtend_invalidSize() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = m.extend(1, 1);
        assertEquals(1, extended.rowCount());
        assertEquals(1, extended.columnCount());
    }

    // ============ Transformation Tests ============

    @Test
    public void testReverseH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
    }

    @Test
    public void testReverseV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.reverseV();
        assertEquals(3, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(1, m.get(1, 0));
        assertEquals(2, m.get(1, 1));
    }

    @Test
    public void testFlipH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix flipped = m.flipV();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(4, flipped.get(0, 1));
        assertEquals(1, flipped.get(1, 0));
        assertEquals(2, flipped.get(1, 1));
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testRotate90() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testTranspose_square() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix transposed = m.transpose();
        assertEquals(2, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(3, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(4, transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape_oneArg() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3, 4 } });
        ByteMatrix reshaped = m.reshape(2);
        assertEquals(2, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(4, reshaped.get(1, 1));
    }

    @Test
    public void testReshape_twoArgs() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3, 4 } });
        ByteMatrix reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
    }

    @Test
    public void testReshape_invalidSize() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        ByteMatrix reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
    }

    // ============ Repelem/Repmat Tests ============

    @Test
    public void testRepelem() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(0, 2));
    }

    @Test
    public void testRepelem_invalidRepeats() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix result = m.repmat(2, 2);
        assertEquals(4, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(1, result.get(2, 0));
        assertEquals(2, result.get(2, 1));
    }

    @Test
    public void testRepmat_invalidRepeats() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals(1, flat.get(0));
        assertEquals(2, flat.get(1));
        assertEquals(3, flat.get(2));
        assertEquals(4, flat.get(3));
    }

    @Test
    public void testFlatOp() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
        ByteMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(3, stacked.get(1, 0));
        assertEquals(4, stacked.get(1, 1));
    }

    @Test
    public void testVstack_invalidColumns() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 4 } });
        ByteMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(3, stacked.get(1, 0));
        assertEquals(4, stacked.get(1, 1));
    }

    @Test
    public void testHstack_invalidRows() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Boxed Test ============

    @Test
    public void testBoxed() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Byte> boxed = m.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(2, boxed.columnCount());
        assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
        assertEquals(Byte.valueOf((byte) 2), boxed.get(0, 1));
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_two() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = m1.zipWith(m2, (a, b) -> (byte) (a + b));
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testZipWith_two_invalidShape() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (byte) (a + b)));
    }

    @Test
    public void testZipWith_three() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 5, 6 } });
        ByteMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c));
        assertEquals(9, result.get(0, 0));
        assertEquals(12, result.get(0, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Byte> diag = m.streamLU2RD().boxed().toList();
        assertEquals(3, diag.size());
        assertEquals((byte) 1, diag.get(0));
        assertEquals((byte) 5, diag.get(1));
        assertEquals((byte) 9, diag.get(2));
    }

    @Test
    public void testStreamRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Byte> diag = m.streamRU2LD().boxed().toList();
        assertEquals(3, diag.size());
        assertEquals((byte) 3, diag.get(0));
        assertEquals((byte) 5, diag.get(1));
        assertEquals((byte) 7, diag.get(2));
    }

    @Test
    public void testStreamH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> all = m.streamH().boxed().toList();
        assertEquals(4, all.size());
        assertEquals((byte) 1, all.get(0));
        assertEquals((byte) 2, all.get(1));
    }

    @Test
    public void testStreamH_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Byte> row = m.streamH(0).boxed().toList();
        assertEquals(3, row.size());
        assertEquals((byte) 1, row.get(0));
        assertEquals((byte) 2, row.get(1));
        assertEquals((byte) 3, row.get(2));
    }

    @Test
    public void testStreamH_rowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<Byte> rows = m.streamH(1, 3).boxed().toList();
        assertEquals(4, rows.size());
        assertEquals((byte) 3, rows.get(0));
        assertEquals((byte) 4, rows.get(1));
    }

    @Test
    public void testStreamV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> all = m.streamV().boxed().toList();
        assertEquals(4, all.size());
        assertEquals((byte) 1, all.get(0));
        assertEquals((byte) 3, all.get(1));
    }

    @Test
    public void testStreamV_singleColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<Byte> col = m.streamV(0).boxed().toList();
        assertEquals(3, col.size());
        assertEquals((byte) 1, col.get(0));
        assertEquals((byte) 3, col.get(1));
        assertEquals((byte) 5, col.get(2));
    }

    @Test
    public void testStreamV_columnRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Byte> columnCount = m.streamV(1, 3).boxed().toList();
        assertEquals(4, columnCount.size());
        assertEquals((byte) 2, columnCount.get(0));
        assertEquals((byte) 5, columnCount.get(1));
    }

    @Test
    public void testStreamR() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<ByteStream> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).count());
    }

    @Test
    public void testStreamR_rowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<ByteStream> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamC() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<ByteStream> columnCount = m.streamC().toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).count());
    }

    @Test
    public void testStreamC_columnRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<ByteStream> columnCount = m.streamC(1, 3).toList();
        assertEquals(2, columnCount.size());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach_valueConsumer() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> values = new ArrayList<>();
        m.forEach(b -> values.add(b));
        assertEquals(4, values.size());
        assertEquals((byte) 1, values.get(0));
        assertEquals((byte) 2, values.get(1));
    }

    @Test
    public void testForEach_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Byte> values = new ArrayList<>();
        m.forEach(1, 3, 1, 3, b -> values.add(b));
        assertEquals(4, values.size());
        assertEquals((byte) 5, values.get(0));
        assertEquals((byte) 6, values.get(1));
    }

    // ============ Points Tests (Inherited) ============

    @Test
    public void testPointsLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[3][3]);
        List<Point> points = m.pointsLU2RD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 2), points.get(2));
    }

    @Test
    public void testPointsRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[3][3]);
        List<Point> points = m.pointsRU2LD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 2), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsH() {
        ByteMatrix m = ByteMatrix.of(new byte[2][2]);
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
    }

    @Test
    public void testPointsH_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[2][3]);
        List<Point> points = m.pointsH(0).toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 2), points.get(2));
    }

    @Test
    public void testPointsV() {
        ByteMatrix m = ByteMatrix.of(new byte[2][2]);
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
    }

    @Test
    public void testPointsV_singleColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[3][2]);
        List<Point> points = m.pointsV(0).toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsR() {
        ByteMatrix m = ByteMatrix.of(new byte[2][2]);
        List<Stream<Point>> rows = m.pointsR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).count());
    }

    @Test
    public void testPointsC() {
        ByteMatrix m = ByteMatrix.of(new byte[2][2]);
        List<Stream<Point>> columnCount = m.pointsC().toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).count());
    }

    // ============ Utility Tests (Inherited) ============

    @Test
    public void testIsEmpty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertTrue(empty.isEmpty());
        ByteMatrix notEmpty = ByteMatrix.of(new byte[][] { { 1 } });
        assertFalse(notEmpty.isEmpty());
    }

    @Test
    public void testArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        byte[][] result = m.array();
        assertSame(arr, result);
    }

    @Test
    public void testIsSameShape() {
        ByteMatrix m1 = ByteMatrix.of(new byte[2][3]);
        ByteMatrix m2 = ByteMatrix.of(new byte[2][3]);
        ByteMatrix m3 = ByteMatrix.of(new byte[3][2]);
        assertTrue(m1.isSameShape(m2));
        assertFalse(m1.isSameShape(m3));
    }

    @Test
    public void testAccept() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        AtomicInteger counter = new AtomicInteger(0);
        m.accept(matrix -> counter.set(matrix.rowCount() * matrix.columnCount()));
        assertEquals(2, counter.get());
    }

    @Test
    public void testApply() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
        assertEquals(2, result);
    }

    // ============ Println Test ============

    @Test
    public void testPrintln() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("4"));
    }

    // ============ Equals/HashCode Tests ============

    @Test
    public void testHashCode_equal() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals_same() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertEquals(m, m);
    }

    @Test
    public void testEquals_equal() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1, m2);
    }

    @Test
    public void testEquals_notEqual() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
        assertNotEquals(m1, m2);
    }

    @Test
    public void testEquals_null() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertNotEquals(m, null);
    }

    @Test
    public void testEquals_differentType() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertNotEquals(m, "string");
    }

    // ============ ToString Test ============

    @Test
    public void testToString() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("4"));
    }
}
