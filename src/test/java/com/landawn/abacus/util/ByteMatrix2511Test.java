package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteStream;

@Tag("2511")
public class ByteMatrix2511Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        ByteMatrix m = new ByteMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        ByteMatrix m = new ByteMatrix(new byte[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        ByteMatrix m = new ByteMatrix(new byte[][] { { 42 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42, m.get(0, 0));
    }

    @Test
    public void testConstructor_withNonSquareMatrix() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(3, m.cols);
    }

    @Test
    public void testConstructor_withNegativeValues() {
        byte[][] arr = { { -1, -2 }, { -3, -4 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(-1, m.get(0, 0));
        assertEquals(-4, m.get(1, 1));
    }

    @Test
    public void testConstructor_largeMatrix() {
        byte[][] arr = new byte[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                arr[i][j] = (byte) ((i + j) % 128);
            }
        }
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(100, m.rows);
        assertEquals(100, m.cols);
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());
        assertSame(ByteMatrix.empty(), ByteMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
    public void testOf_withSingleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[] { 1, 2, 3 });
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
    }

    @Test
    public void testRandom() {
        ByteMatrix m = ByteMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withZeroLength() {
        ByteMatrix m = ByteMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRandom_withLargeLength() {
        ByteMatrix m = ByteMatrix.random(1000);
        assertEquals(1, m.rows);
        assertEquals(1000, m.cols);
    }

    @Test
    public void testRepeat() {
        ByteMatrix m = ByteMatrix.repeat((byte) 42, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withZero() {
        ByteMatrix m = ByteMatrix.repeat((byte) 0, 3);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertEquals(0, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withNegative() {
        ByteMatrix m = ByteMatrix.repeat((byte) -10, 3);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertEquals(-10, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        ByteMatrix m = ByteMatrix.range((byte) 1, (byte) 5);
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(4, m.get(0, 3));
    }

    @Test
    public void testRange_withStep() {
        ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(0, 3));
        assertEquals(8, m.get(0, 4));
    }

    @Test
    public void testRange_emptyRange() {
        ByteMatrix m = ByteMatrix.range((byte) 5, (byte) 5);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRange_negativeValues() {
        ByteMatrix m = ByteMatrix.range((byte) -5, (byte) 0);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertEquals(-5, m.get(0, 0));
        assertEquals(-1, m.get(0, 4));
    }

    @Test
    public void testRangeClosed() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(0, 3));
    }

    @Test
    public void testRangeClosed_withStep() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 3);
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        assertEquals(0, m.get(0, 0));
        assertEquals(3, m.get(0, 1));
        assertEquals(6, m.get(0, 2));
        assertEquals(9, m.get(0, 3));
    }

    @Test
    public void testRangeClosed_singleElement() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 5, (byte) 5);
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(5, m.get(0, 0));
    }

    @Test
    public void testDiagonalLU2RD() {
        ByteMatrix m = ByteMatrix.diagonalLU2RD(new byte[] { 1, 2, 3 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
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
    public void testDiagonalLU2RD_singleElement() {
        ByteMatrix m = ByteMatrix.diagonalLU2RD(new byte[] { 42 });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42, m.get(0, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        ByteMatrix m = ByteMatrix.diagonalRU2LD(new byte[] { 1, 2, 3 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(2, 2));
    }

    @Test
    public void testDiagonalRU2LD_empty() {
        ByteMatrix m = ByteMatrix.diagonalRU2LD(new byte[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        ByteMatrix m = ByteMatrix.diagonal(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        ByteMatrix m = ByteMatrix.diagonal(new byte[] { 1, 2, 3 }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        ByteMatrix m = ByteMatrix.diagonal(null, new byte[] { 4, 5, 6 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
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
    public void testDiagonal_withBothDiagonalsOverlapping() {
        ByteMatrix m = ByteMatrix.diagonal(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        // Center element should be from main diagonal (set second)
        assertEquals(2, m.get(1, 1));
    }

    @Test
    public void testUnbox() {
        Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(2, unboxed.get(0, 1));
    }

    @Test
    public void testUnbox_withNullElements() {
        Byte[][] boxed = { { 1, null }, { null, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1));
        assertEquals(0, unboxed.get(1, 0));
    }

    @Test
    public void testUnbox_allNulls() {
        Byte[][] boxed = { { null, null }, { null, null } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals(0, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(1, 1));
    }

    // ============ Component Type Test ============

    @Test
    public void testComponentType() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertEquals(byte.class, m.componentType());
    }

    @Test
    public void testComponentType_emptyMatrix() {
        ByteMatrix m = ByteMatrix.empty();
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
    public void testGet_allPositions() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix m = ByteMatrix.of(arr);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(arr[i][j], m.get(i, j));
            }
        }
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

    @Test
    public void testSet_overwriteValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        m.set(0, 0, (byte) 10);
        assertEquals(10, m.get(0, 0));
        m.set(0, 0, (byte) 20);
        assertEquals(20, m.get(0, 0));
    }

    @Test
    public void testSet_negativeValues() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 } });
        m.set(0, 0, (byte) -5);
        assertEquals(-5, m.get(0, 0));
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
    public void testUpOf_multipleRows() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        OptionalByte up = m.upOf(2, 1);
        assertTrue(up.isPresent());
        assertEquals(4, up.get());
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
    public void testDownOf_multipleRows() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        OptionalByte down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());
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
    public void testLeftOf_multipleColumns() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        OptionalByte left = m.leftOf(0, 2);
        assertTrue(left.isPresent());
        assertEquals(2, left.get());
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

    @Test
    public void testRightOf_multipleColumns() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        OptionalByte right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());
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
    public void testRow_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertArrayEquals(new byte[] { 1, 2, 3 }, m.row(0));
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
    public void testColumn_singleColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 }, { 2 }, { 3 } });
        assertArrayEquals(new byte[] { 1, 2, 3 }, m.column(0));
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
    public void testSetRow_invalidIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(-1, new byte[] { 1, 2 }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(1, new byte[] { 1, 2 }));
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
    public void testSetColumn_invalidIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(-1, new byte[] { 1, 2 }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(2, new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 0, 0, 0 } });
        m.updateRow(0, val -> (byte) (val * 2));
        assertArrayEquals(new byte[] { 2, 4, 6 }, m.row(0));
        assertArrayEquals(new byte[] { 0, 0, 0 }, m.row(1));
    }

    @Test
    public void testUpdateRow_multipleRows() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.updateRow(1, val -> (byte) (val + 10));
        assertArrayEquals(new byte[] { 1, 2 }, m.row(0));
        assertArrayEquals(new byte[] { 13, 14 }, m.row(1));
        assertArrayEquals(new byte[] { 5, 6 }, m.row(2));
    }

    @Test
    public void testUpdateColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 0 }, { 2, 0 }, { 3, 0 } });
        m.updateColumn(0, val -> (byte) (val * 2));
        assertArrayEquals(new byte[] { 2, 4, 6 }, m.column(0));
        assertArrayEquals(new byte[] { 0, 0, 0 }, m.column(1));
    }

    @Test
    public void testUpdateColumn_multipleColumns() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.updateColumn(2, val -> (byte) (val + 10));
        assertArrayEquals(new byte[] { 1, 2, 13 }, m.row(0));
        assertArrayEquals(new byte[] { 4, 5, 16 }, m.row(1));
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
    public void testGetLU2RD_singleElement() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 42 } });
        assertArrayEquals(new byte[] { 42 }, m.getLU2RD());
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
    public void testSetLU2RD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[2][3]);
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 0, 0 }, { 0, 2, 0 }, { 0, 0, 3 } });
        m.updateLU2RD(val -> (byte) (val * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(6, m.get(2, 2));
    }

    @Test
    public void testUpdateLU2RD_allValues() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } });
        m.updateLU2RD(val -> (byte) 9);
        assertEquals(9, m.get(0, 0));
        assertEquals(9, m.get(1, 1));
        assertEquals(9, m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 0, 2, 0 }, { 3, 0, 0 } });
        byte[] diag = m.getRU2LD();
        assertArrayEquals(new byte[] { 1, 2, 3 }, diag);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
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
    public void testSetRU2LD_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[3][3]);
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 0, 2, 0 }, { 3, 0, 0 } });
        m.updateRU2LD(val -> (byte) (val * 3));
        assertEquals(3, m.get(0, 2));
        assertEquals(6, m.get(1, 1));
        assertEquals(9, m.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD_rectangular() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 2, 0, 0 } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(val -> (byte) (val + 10)));
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
    public void testUpdateAll_withConstant() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(val -> (byte) 10);
        assertEquals(10, m.get(0, 0));
        assertEquals(10, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(10, m.get(1, 1));
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

    @Test
    public void testReplaceIf_noMatches() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf(val -> val > 10, (byte) 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void testMap() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix mapped = m.map(val -> (byte) (val * 2));
        assertEquals(2, mapped.get(0, 0));
        assertEquals(4, mapped.get(0, 1));
        assertEquals(6, mapped.get(1, 0));
        assertEquals(8, mapped.get(1, 1));
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testMap_identity() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix mapped = m.map(val -> val);
        assertEquals(1, mapped.get(0, 0));
        assertEquals(2, mapped.get(0, 1));
        assertNotSame(m, mapped);
    }

    @Test
    public void testMapToObj() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> mapped = m.mapToObj(val -> String.valueOf(val), String.class);
        assertEquals("1", mapped.get(0, 0));
        assertEquals("2", mapped.get(0, 1));
        assertEquals("3", mapped.get(1, 0));
        assertEquals("4", mapped.get(1, 1));
    }

    @Test
    public void testMapToObj_withComplexType() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> mapped = m.mapToObj(val -> (int) val * 10, Integer.class);
        assertEquals(10, mapped.get(0, 0));
        assertEquals(20, mapped.get(0, 1));
        assertEquals(30, mapped.get(1, 0));
        assertEquals(40, mapped.get(1, 1));
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

    @Test
    public void testFill_withPartialArray() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        m.fill(0, 0, new byte[][] { { 1, 2 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(0, m.get(0, 2));
        assertEquals(0, m.get(1, 0));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix copy = m.copy();
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(1, copy.get(0, 0));
        assertEquals(2, copy.get(0, 1));
        copy.set(0, 0, (byte) 10);
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testCopy_emptyMatrix() {
        ByteMatrix m = ByteMatrix.empty();
        ByteMatrix copy = m.copy();
        assertTrue(copy.isEmpty());
    }

    @Test
    public void testCopy_withRowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ByteMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
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
    public void testCopy_withRowRange_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ByteMatrix copy = m.copy(1, 2);
        assertEquals(1, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(3, copy.get(0, 0));
        assertEquals(4, copy.get(0, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
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

    @Test
    public void testCopy_withFullRange_singleCell() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix copy = m.copy(0, 1, 1, 2);
        assertEquals(1, copy.rows);
        assertEquals(1, copy.cols);
        assertEquals(2, copy.get(0, 0));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_withDefaultValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = m.extend(2, 3);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
        assertEquals(0, extended.get(0, 2));
        assertEquals(0, extended.get(1, 0));
    }

    @Test
    public void testExtend_withCustomDefaultValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = m.extend(2, 3, (byte) 9);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
        assertEquals(9, extended.get(0, 2));
        assertEquals(9, extended.get(1, 0));
    }

    @Test
    public void testExtend_withDirections() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 5 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5, extended.get(1, 1));
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtend_withDirectionsAndValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 5 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) 7);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5, extended.get(1, 1));
        assertEquals(7, extended.get(0, 0));
    }

    @Test
    public void testExtend_invalidSize() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });

        ByteMatrix extended = m.extend(1, 1);
        assertEquals(1, extended.rows);
        assertEquals(1, extended.cols);
    }

    @Test
    public void testExtend_noExtension() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix extended = m.extend(1, 2);
        assertEquals(1, extended.rows);
        assertEquals(2, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
    }

    // ============ Transformation Tests ============

    @Test
    public void testReverseH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertArrayEquals(new byte[] { 3, 2, 1 }, m.row(0));
        assertArrayEquals(new byte[] { 6, 5, 4 }, m.row(1));
    }

    @Test
    public void testReverseH_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        m.reverseH();
        assertArrayEquals(new byte[] { 3, 2, 1 }, m.row(0));
    }

    @Test
    public void testReverseV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertArrayEquals(new byte[] { 5, 3, 1 }, m.column(0));
        assertArrayEquals(new byte[] { 6, 4, 2 }, m.column(1));
    }

    @Test
    public void testReverseV_singleColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 }, { 2 }, { 3 } });
        m.reverseV();
        assertArrayEquals(new byte[] { 3, 2, 1 }, m.column(0));
    }

    @Test
    public void testFlipH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix flipped = m.flipH();
        assertEquals(2, flipped.rows);
        assertEquals(3, flipped.cols);
        assertArrayEquals(new byte[] { 3, 2, 1 }, flipped.row(0));
        assertArrayEquals(new byte[] { 6, 5, 4 }, flipped.row(1));
        assertNotSame(m, flipped);
    }

    @Test
    public void testFlipV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ByteMatrix flipped = m.flipV();
        assertEquals(3, flipped.rows);
        assertEquals(2, flipped.cols);
        assertArrayEquals(new byte[] { 5, 3, 1 }, flipped.column(0));
        assertArrayEquals(new byte[] { 6, 4, 2 }, flipped.column(1));
        assertNotSame(m, flipped);
    }

    @Test
    public void testRotate90() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate90_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix rotated = m.rotate90();
        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
    }

    @Test
    public void testRotate180() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testTranspose_squareMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(3, transposed.get(0, 1));
    }

    @Test
    public void testReshape() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3, 4 } });
        ByteMatrix reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(4, reshaped.get(1, 1));
    }

    @Test
    public void testReshape_toSingleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix reshaped = m.reshape(1, 4);
        assertEquals(1, reshaped.rows);
        assertEquals(4, reshaped.cols);
    }

    @Test
    public void testRepelem() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(1, 0));
        assertEquals(1, repeated.get(1, 1));
        assertEquals(2, repeated.get(0, 2));
    }

    @Test
    public void testRepelem_singleRepeat() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix repeated = m.repelem(1, 1);
        assertEquals(1, repeated.rows);
        assertEquals(2, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
    }

    @Test
    public void testRepmat() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix repeated = m.repmat(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(4, repeated.get(3, 3));
    }

    @Test
    public void testRepmat_singleRepeat() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix repeated = m.repmat(1, 1);
        assertEquals(1, repeated.rows);
        assertEquals(2, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
    }

    @Test
    public void testFlatten() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteList flattened = m.flatten();
        assertEquals(4, flattened.size());
        assertEquals(1, flattened.get(0));
        assertEquals(2, flattened.get(1));
        assertEquals(3, flattened.get(2));
        assertEquals(4, flattened.get(3));
    }

    @Test
    public void testFlatten_emptyMatrix() {
        ByteMatrix m = ByteMatrix.empty();
        ByteList flattened = m.flatten();
        assertEquals(0, flattened.size());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
        ByteMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(3, stacked.get(1, 0));
        assertEquals(4, stacked.get(1, 1));
    }

    @Test
    public void testVstack_incompatibleColumns() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 4 } });
        ByteMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(3, stacked.get(1, 0));
        assertEquals(4, stacked.get(1, 1));
    }

    @Test
    public void testHstack_incompatibleRows() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Boxed Tests ============

    @Test
    public void testBoxed() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Byte> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
        assertEquals(Byte.valueOf((byte) 2), boxed.get(0, 1));
        assertEquals(Byte.valueOf((byte) 3), boxed.get(1, 0));
        assertEquals(Byte.valueOf((byte) 4), boxed.get(1, 1));
    }

    @Test
    public void testBoxed_emptyMatrix() {
        ByteMatrix m = ByteMatrix.empty();
        Matrix<Byte> boxed = m.boxed();
        assertTrue(boxed.isEmpty());
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Byte> diagonal = m.streamLU2RD().boxed().toList();
        assertEquals(3, diagonal.size());
        assertEquals(1, diagonal.get(0).byteValue());
        assertEquals(5, diagonal.get(1).byteValue());
        assertEquals(9, diagonal.get(2).byteValue());
    }

    @Test
    public void testStreamRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Byte> diagonal = m.streamRU2LD().boxed().toList();
        assertEquals(3, diagonal.size());
        assertEquals(3, diagonal.get(0).byteValue());
        assertEquals(5, diagonal.get(1).byteValue());
        assertEquals(7, diagonal.get(2).byteValue());
    }

    @Test
    public void testStreamH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> elements = m.streamH().boxed().toList();
        assertEquals(4, elements.size());
        assertEquals(1, elements.get(0).byteValue());
        assertEquals(2, elements.get(1).byteValue());
    }

    @Test
    public void testStreamH_withRowIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> row = m.streamH(1).boxed().toList();
        assertEquals(2, row.size());
        assertEquals(3, row.get(0).byteValue());
        assertEquals(4, row.get(1).byteValue());
    }

    @Test
    public void testStreamH_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<Byte> elements = m.streamH(1, 3).boxed().toList();
        assertEquals(4, elements.size());
        assertEquals(3, elements.get(0).byteValue());
        assertEquals(4, elements.get(1).byteValue());
    }

    @Test
    public void testStreamV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> elements = m.streamV().boxed().toList();
        assertEquals(4, elements.size());
        assertEquals(1, elements.get(0).byteValue());
        assertEquals(3, elements.get(1).byteValue());
    }

    @Test
    public void testStreamV_withColumnIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Byte> col = m.streamV(1).boxed().toList();
        assertEquals(2, col.size());
        assertEquals(2, col.get(0).byteValue());
        assertEquals(4, col.get(1).byteValue());
    }

    @Test
    public void testStreamV_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Byte> elements = m.streamV(1, 3).boxed().toList();
        assertEquals(4, elements.size());
        assertEquals(2, elements.get(0).byteValue());
        assertEquals(5, elements.get(1).byteValue());
    }

    @Test
    public void testStreamR() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<ByteStream> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).toArray().length);
    }

    @Test
    public void testStreamR_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<ByteStream> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamC() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<ByteStream> cols = m.streamC().toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).toArray().length);
    }

    @Test
    public void testStreamC_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<ByteStream> cols = m.streamC(1, 3).toList();
        assertEquals(2, cols.size());
    }

    // ============ Point Stream Tests ============

    @Test
    public void testPointsH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(0, points.get(0).columnIndex());
    }

    @Test
    public void testPointsV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(0, points.get(0).columnIndex());
    }

    @Test
    public void testPointsLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = m.pointsLU2RD().toList();
        assertEquals(3, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(0, points.get(0).columnIndex());
    }

    @Test
    public void testPointsRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = m.pointsRU2LD().toList();
        assertEquals(3, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(2, points.get(0).columnIndex());
    }

    // ============ Adjacent Points Tests ============

    @Test
    public void testAdjacent4Points() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
    }

    @Test
    public void testAdjacent4Points_corner() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(2, points.size());
    }

    @Test
    public void testAdjacent8Points() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = m.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
    }

    @Test
    public void testAdjacent8Points_corner() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(3, points.size());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger sum = new AtomicInteger(0);
        m.forEach(val -> sum.addAndGet(val));
        assertEquals(10, sum.get());
    }

    @Test
    public void testForEach_withConsumer() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger sum = new AtomicInteger(0);
        m.forEach(val -> sum.addAndGet((int) val));
        assertEquals(10, sum.get());   // 1+2+3+4 = 10
    }

    // ============ Utility Tests ============

    @Test
    public void testIsEmpty() {
        assertTrue(ByteMatrix.empty().isEmpty());
        assertFalse(ByteMatrix.of(new byte[][] { { 1 } }).isEmpty());
    }

    @Test
    public void testIsSameShape() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertTrue(m1.isSameShape(m2));
        assertFalse(m1.isSameShape(m3));
    }

    @Test
    public void testArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        byte[][] result = m.array();
        assertArrayEquals(arr, result);
    }

    @Test
    public void testHashCode_equalMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "not a matrix");
    }

    @Test
    public void testEquals_emptyMatrices() {
        ByteMatrix m1 = ByteMatrix.empty();
        ByteMatrix m2 = ByteMatrix.empty();
        assertEquals(m1, m2);
    }

    @Test
    public void testToString() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("4"));
    }

    @Test
    public void testPrintln() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}
