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
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteStream;

@Tag("2025")
public class ByteMatrix2025Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
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

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
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
    public void testRandom() {
        ByteMatrix m = ByteMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
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
    public void testRange() {
        ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new byte[] { 0, 2, 4, 6, 8 }, m.row(0));
    }

    @Test
    public void testRange_withNegativeStep() {
        ByteMatrix m = ByteMatrix.range((byte) 10, (byte) 0, (byte) -2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new byte[] { 10, 8, 6, 4, 2 }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 4);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 10, (byte) 2);
        assertEquals(1, m.rows);
        assertEquals(6, m.cols);
        assertArrayEquals(new byte[] { 0, 2, 4, 6, 8, 10 }, m.row(0));
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
        assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonal(new byte[] { 1, 2 }, new byte[] { 3, 4, 5 }));
    }

    @Test
    public void testUnbox() {
        Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(4, unboxed.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Byte[][] boxed = { { 1, null }, { null, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1)); // null -> 0
        assertEquals(0, unboxed.get(1, 0)); // null -> 0
        assertEquals(4, unboxed.get(1, 1));
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertEquals(byte.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(1, 2));
    }

    @Test
    public void testGet_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(Point.of(0, 0)));
        assertEquals(4, m.get(Point.of(1, 1)));
        assertEquals(2, m.get(Point.of(0, 1)));
    }

    @Test
    public void testSet() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.set(0, 0, (byte) 10);
        assertEquals(10, m.get(0, 0));

        m.set(1, 1, (byte) 20);
        assertEquals(20, m.get(1, 1));
    }

    @Test
    public void testSet_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, (byte) 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, (byte) 0));
    }

    @Test
    public void testSetWithPoint() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.set(Point.of(0, 0), (byte) 50);
        assertEquals(50, m.get(Point.of(0, 0)));
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

        OptionalByte up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());

        // Top row has no element above
        OptionalByte empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

        OptionalByte down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());

        // Bottom row has no element below
        OptionalByte empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

        OptionalByte left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());

        // Leftmost column has no element to the left
        OptionalByte empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

        OptionalByte right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());

        // Rightmost column has no element to the right
        OptionalByte empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }


    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new byte[] { 1, 2, 3 }, m.row(0));
        assertArrayEquals(new byte[] { 4, 5, 6 }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new byte[] { 1, 4 }, m.column(0));
        assertArrayEquals(new byte[] { 2, 5 }, m.column(1));
        assertArrayEquals(new byte[] { 3, 6 }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new byte[] { 10, 20 });
        assertArrayEquals(new byte[] { 10, 20 }, m.row(0));
        assertArrayEquals(new byte[] { 3, 4 }, m.row(1)); // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1 }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1, 2, 3 }));
    }

    @Test
    public void testSetColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.setColumn(0, new byte[] { 10, 20 });
        assertArrayEquals(new byte[] { 10, 20 }, m.column(0));
        assertArrayEquals(new byte[] { 2, 4 }, m.column(1)); // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1 }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, x -> (byte) (x * 2));
        assertArrayEquals(new byte[] { 2, 4 }, m.row(0));
        assertArrayEquals(new byte[] { 3, 4 }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, x -> (byte) (x + 10));
        assertArrayEquals(new byte[] { 11, 13 }, m.column(0));
        assertArrayEquals(new byte[] { 2, 4 }, m.column(1)); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new byte[] { 1, 5, 9 }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setLU2RD(new byte[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new byte[] { 1 }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(x -> (byte) (x * 10));
        assertEquals(10, m.get(0, 0));
        assertEquals(50, m.get(1, 1));
        assertEquals(90, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> (byte) (x * 2)));
    }

    @Test
    public void testGetRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new byte[] { 3, 5, 7 }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new byte[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 2));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new byte[] { 1 }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(x -> (byte) (x * 10));
        assertEquals(30, m.get(0, 2));
        assertEquals(50, m.get(1, 1));
        assertEquals(70, m.get(2, 0));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> (byte) (x * 2)));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(x -> (byte) (x * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withIndices() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> (byte) (i * 10 + j));
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.replaceIf(x -> x > 3, (byte) 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(0, m.get(1, 0)); // was 4
        assertEquals(0, m.get(1, 1)); // was 5
        assertEquals(0, m.get(1, 2)); // was 6
    }

    @Test
    public void testReplaceIf_withIndices() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.replaceIf((i, j) -> i == j, (byte) 0); // Replace diagonal
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix result = m.map(x -> (byte) (x * 2));
        assertEquals(2, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(6, result.get(1, 0));
        assertEquals(8, result.get(1, 1));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1", result.get(0, 0));
        assertEquals("val:4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.fill((byte) 99);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                assertEquals(99, m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        byte[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(patch);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(2, 2)); // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        byte[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(1, 1, patch);
        assertEquals(0, m.get(0, 0)); // unchanged
        assertEquals(1, m.get(1, 1));
        assertEquals(2, m.get(1, 2));
        assertEquals(3, m.get(2, 1));
        assertEquals(4, m.get(2, 2));
    }

    @Test
    public void testFill_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        byte[][] patch = { { 1, 2 }, { 3, 4 } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals(1, copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, (byte) 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(99, copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix subset = m.copy(1, 3);
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
        assertEquals(4, subset.get(0, 0));
        assertEquals(9, subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
        assertEquals(2, submatrix.get(0, 0));
        assertEquals(6, submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(0, extended.get(3, 3)); // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertEquals(1, truncated.get(0, 0));
        assertEquals(5, truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = m.extend(3, 3, (byte) -1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(-1, extended.get(2, 2)); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, (byte) 0));
    }

    @Test
    public void testExtend_directional() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rows); // 1 + 3 + 1
        assertEquals(7, extended.cols); // 2 + 3 + 2

        // Original values at offset position
        assertEquals(1, extended.get(1, 2));
        assertEquals(5, extended.get(2, 3));

        // New cells are 0
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionalWithDefault() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) -1);
        assertEquals(5, extended.rows);
        assertEquals(5, extended.cols);

        // Check original values
        assertEquals(1, extended.get(1, 1));

        // Check new values
        assertEquals(-1, extended.get(0, 0));
        assertEquals(-1, extended.get(4, 4));
    }

    @Test
    public void testExtend_directionalWithNegativeValues() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, (byte) 0));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void testReverseH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
        assertEquals(6, m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals(5, m.get(0, 0));
        assertEquals(6, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(1, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ByteMatrix flipped = m.flipV();
        assertEquals(5, flipped.get(0, 0));
        assertEquals(3, flipped.get(1, 0));
        assertEquals(1, flipped.get(2, 0));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    // ============ Rotation Tests ============

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

    // ============ Transpose Tests ============

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
        assertEquals(3, transposed.get(2, 0));
        assertEquals(6, transposed.get(2, 1));
    }

    @Test
    public void testTranspose_square() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(3, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(4, transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i));
        }
    }

    @Test
    public void testReshape_back() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix reshaped = m.reshape(1, 9);
        ByteMatrix reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        ByteMatrix reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

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
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2)); // repeat starts
        assertEquals(2, repeated.get(0, 3));

        assertEquals(3, repeated.get(1, 0));
        assertEquals(4, repeated.get(1, 1));

        // Check vertical repeat
        assertEquals(1, repeated.get(2, 0)); // vertical repeat starts
        assertEquals(2, repeated.get(2, 1));
    }

    @Test
    public void testRepmat_invalidArguments() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteList flat = m.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, flat.get(i));
        }
    }

    @Test
    public void testFlatten_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        ByteList flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Integer> sums = new ArrayList<>();
        m.flatOp(row -> {
            int sum = 0;
            for (byte val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertEquals(45, sums.get(0).intValue());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        ByteMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rows);
        assertEquals(3, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(7, stacked.get(2, 0));
        assertEquals(12, stacked.get(3, 2));
    }

    @Test
    public void testVstack_differentColumnCounts() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(5, stacked.get(0, 2));
        assertEquals(8, stacked.get(1, 3));
    }

    @Test
    public void testHstack_differentRowCounts() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix sum = m1.add(m2);

        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(10, sum.get(1, 0));
        assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix diff = m1.subtract(m2);

        assertEquals(4, diff.get(0, 0));
        assertEquals(4, diff.get(0, 1));
        assertEquals(4, diff.get(1, 0));
        assertEquals(4, diff.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix product = m1.multiply(m2);

        assertEquals(19, product.get(0, 0)); // 1*5 + 2*7
        assertEquals(22, product.get(0, 1)); // 1*6 + 2*8
        assertEquals(43, product.get(1, 0)); // 3*5 + 4*7
        assertEquals(50, product.get(1, 1)); // 3*6 + 4*8
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } }); // 1x3
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 4 }, { 5 }, { 6 } }); // 3x1
        ByteMatrix product = m1.multiply(m2);

        assertEquals(1, product.rows);
        assertEquals(1, product.cols);
        assertEquals(32, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix<Byte> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(3, boxed.cols);
        assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
        assertEquals(Byte.valueOf((byte) 6), boxed.get(1, 2));
    }

    @Test
    public void testToIntMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix intMatrix = m.toIntMatrix();
        assertEquals(2, intMatrix.rows);
        assertEquals(2, intMatrix.cols);
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix longMatrix = m.toLongMatrix();
        assertEquals(2, longMatrix.rows);
        assertEquals(2, longMatrix.cols);
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(2, floatMatrix.rows);
        assertEquals(2, floatMatrix.cols);
        assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
        assertEquals(4.0f, floatMatrix.get(1, 1), 0.0001f);
    }

    @Test
    public void testToDoubleMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(2, doubleMatrix.rows);
        assertEquals(2, doubleMatrix.cols);
        assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = m1.zipWith(m2, (a, b) -> (byte) (a * b));

        assertEquals(5, result.get(0, 0)); // 1*5
        assertEquals(12, result.get(0, 1)); // 2*6
        assertEquals(21, result.get(1, 0)); // 3*7
        assertEquals(32, result.get(1, 1)); // 4*8
    }

    @Test
    public void testZipWith_differentShapes() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (byte) (a + b)));
    }

    @Test
    public void testZipWith_threeMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
        ByteMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c));

        assertEquals(15, result.get(0, 0)); // 1+5+9
        assertEquals(18, result.get(0, 1)); // 2+6+10
        assertEquals(21, result.get(1, 0)); // 3+7+11
        assertEquals(24, result.get(1, 1)); // 4+8+12
    }

    @Test
    public void testZipWith_threeMatrices_differentShapes() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10, 11 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c)));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testStreamLU2RD_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new byte[] { 3, 5, 7 }, antiDiagonal);
    }

    @Test
    public void testStreamRU2LD_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.streamRU2LD().toArray().length);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] all = m.streamH().toArray();
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.streamH().toArray().length);
    }

    @Test
    public void testStreamH_withRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new byte[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new byte[] { 4, 5, 6, 7, 8, 9 }, rows);
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] all = m.streamV().toArray();
        assertArrayEquals(new byte[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.streamV().toArray().length);
    }

    @Test
    public void testStreamV_withColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new byte[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] cols = m.streamV(1, 3).toArray();
        assertArrayEquals(new byte[] { 2, 5, 8, 3, 6, 9 }, cols);
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<byte[]> rows = m.streamR().map(ByteStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new byte[] { 1, 2, 3 }, rows.get(0));
        assertArrayEquals(new byte[] { 4, 5, 6 }, rows.get(1));
    }

    @Test
    public void testStreamR_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<byte[]> rows = m.streamR(1, 3).map(ByteStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new byte[] { 4, 5, 6 }, rows.get(0));
        assertArrayEquals(new byte[] { 7, 8, 9 }, rows.get(1));
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<byte[]> cols = m.streamC().map(ByteStream::toArray).toList();
        assertEquals(3, cols.size());
        assertArrayEquals(new byte[] { 1, 4 }, cols.get(0));
        assertArrayEquals(new byte[] { 2, 5 }, cols.get(1));
        assertArrayEquals(new byte[] { 3, 6 }, cols.get(2));
    }

    @Test
    public void testStreamC_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<byte[]> cols = m.streamC(1, 3).map(ByteStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new byte[] { 2, 5, 8 }, cols.get(0));
        assertArrayEquals(new byte[] { 3, 6, 9 }, cols.get(1));
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
    }

    // ============ Object Methods Tests ============

    @Test
    public void testHashCode() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 4, 3 } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 4, 3 } });
        ByteMatrix m4 = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    // ============ Edge Case Tests ============

    @Test
    public void testByteMinMaxValues() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { Byte.MAX_VALUE, Byte.MIN_VALUE } });
        assertEquals(Byte.MAX_VALUE, m.get(0, 0));
        assertEquals(Byte.MIN_VALUE, m.get(0, 1));
    }

    @Test
    public void testByteOverflow() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { Byte.MAX_VALUE, 1 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, Byte.MAX_VALUE } });

        // Addition overflow wraps around
        ByteMatrix sum = m1.add(m2);
        assertTrue(sum.get(0, 0) < 0); // Overflow
    }

    @Test
    public void testNegativeBytes() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { -1, -2 }, { -3, -4 } });

        assertEquals(-1, m.get(0, 0));
        assertEquals(-4, m.get(1, 1));

        ByteMatrix doubled = m.map(x -> (byte) (x * 2));
        assertEquals(-2, doubled.get(0, 0));
        assertEquals(-8, doubled.get(1, 1));
    }

    @Test
    public void testEmptyMatrixOperations() {
        ByteMatrix empty = ByteMatrix.empty();

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rows);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        ByteMatrix extended = empty.extend(2, 2, (byte) 5);
        assertEquals(2, extended.rows);
        assertEquals(2, extended.cols);
        assertEquals(5, extended.get(0, 0));
    }

    // ============ High-Impact Tests for 95% Coverage ============

    @Test
    public void testRotateTransposeAndConvertTallMatrix() {
        // Create a tall matrix (rows > cols) - 5 rows × 3 cols
        ByteMatrix m = ByteMatrix.of(new byte[][] {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 },
            { 10, 11, 12 },
            { 13, 14, 15 }
        });

        // Test rotate90() with tall matrix
        ByteMatrix rotated90 = m.rotate90();
        assertEquals(3, rotated90.rows);
        assertEquals(5, rotated90.cols);
        assertEquals(13, rotated90.get(0, 0));
        assertEquals(1, rotated90.get(0, 4));

        // Test rotate270() with tall matrix
        ByteMatrix rotated270 = m.rotate270();
        assertEquals(3, rotated270.rows);
        assertEquals(5, rotated270.cols);
        assertEquals(3, rotated270.get(0, 0));

        // Test transpose() with tall matrix
        ByteMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(5, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(15, transposed.get(2, 4));

        // Test boxed() with tall matrix
        Matrix<Byte> boxed = m.boxed();
        assertEquals(5, boxed.rows);
        assertEquals(3, boxed.cols);
        assertEquals(Byte.valueOf((byte)1), boxed.get(0, 0));

        // Test toLongMatrix() with tall matrix
        LongMatrix longMat = m.toLongMatrix();
        assertEquals(5, longMat.rows);
        assertEquals(3, longMat.cols);
        assertEquals(1L, longMat.get(0, 0));
        assertEquals(15L, longMat.get(4, 2));

        // Test toFloatMatrix() with tall matrix
        FloatMatrix floatMat = m.toFloatMatrix();
        assertEquals(5, floatMat.rows);
        assertEquals(3, floatMat.cols);
        assertEquals(1.0f, floatMat.get(0, 0), 0.001f);

        // Test toDoubleMatrix() with tall matrix
        DoubleMatrix doubleMat = m.toDoubleMatrix();
        assertEquals(5, doubleMat.rows);
        assertEquals(3, doubleMat.cols);
        assertEquals(1.0, doubleMat.get(0, 0), 0.001);
    }

    @Test
    public void testRepelemOverflow() {
        int largeSize = 50000;
        ByteMatrix m = ByteMatrix.of(new byte[largeSize][2]);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> m.repelem(50000, 1));
        assertTrue(ex1.getMessage().contains("too many rows"));

        ByteMatrix m2 = ByteMatrix.of(new byte[2][largeSize]);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> m2.repelem(1, 50000));
        assertTrue(ex2.getMessage().contains("too many columns"));
    }

    @Test
    public void testRepmatOverflow() {
        int largeSize = 50000;
        ByteMatrix m = ByteMatrix.of(new byte[largeSize][2]);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> m.repmat(50000, 1));
        assertTrue(ex1.getMessage().contains("too many rows"));

        ByteMatrix m2 = ByteMatrix.of(new byte[2][largeSize]);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> m2.repmat(1, 50000));
        assertTrue(ex2.getMessage().contains("too many columns"));
    }
}
