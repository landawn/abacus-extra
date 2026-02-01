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
import com.landawn.abacus.util.u.OptionalShort;
import com.landawn.abacus.util.stream.ShortStream;

@Tag("2025")
public class ShortMatrix2025Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
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
        assertEquals(42, m.get(0, 0));
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
        assertEquals(1, m.get(0, 0));
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
    public void testRandom() {
        ShortMatrix m = ShortMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withRowsCols() {
        ShortMatrix m = ShortMatrix.random(2, 3);
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
        ShortMatrix m = ShortMatrix.repeat((short) 42, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withRowsCols() {
        ShortMatrix m = ShortMatrix.repeat(2, 3, (short) 42);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(42, m.get(i, j));
            }
        }
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
    public void testRange_withNegativeStep() {
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
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        ShortMatrix m = ShortMatrix.diagonalRU2LD(new short[] { 1, 2, 3 });
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
        ShortMatrix m = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
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
        ShortMatrix m = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        ShortMatrix m = ShortMatrix.diagonal(null, new short[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(4, m.get(0, 2));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        ShortMatrix m = ShortMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonal(new short[] { 1, 2 }, new short[] { 3, 4, 5 }));
    }

    @Test
    public void testUnbox() {
        Short[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Short> boxedMatrix = Matrix.of(boxed);
        ShortMatrix unboxed = ShortMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rowCount());
        assertEquals(2, unboxed.columnCount());
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(4, unboxed.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Short[][] boxed = { { 1, null }, { null, 4 } };
        Matrix<Short> boxedMatrix = Matrix.of(boxed);
        ShortMatrix unboxed = ShortMatrix.unbox(boxedMatrix);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1)); // null -> 0
        assertEquals(0, unboxed.get(1, 0)); // null -> 0
        assertEquals(4, unboxed.get(1, 1));
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
        assertEquals(short.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(1, 2));
    }

    @Test
    public void testGet_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(Point.of(0, 0)));
        assertEquals(4, m.get(Point.of(1, 1)));
        assertEquals(2, m.get(Point.of(0, 1)));
    }

    @Test
    public void testSet() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.set(0, 0, (short) 10);
        assertEquals(10, m.get(0, 0));

        m.set(1, 1, (short) 20);
        assertEquals(20, m.get(1, 1));
    }

    @Test
    public void testSet_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, (short) 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, (short) 0));
    }

    @Test
    public void testSetWithPoint() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.set(Point.of(0, 0), (short) 50);
        assertEquals(50, m.get(Point.of(0, 0)));
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

        OptionalShort up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());

        // Top row has no element above
        OptionalShort empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

        OptionalShort down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());

        // Bottom row has no element below
        OptionalShort empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

        OptionalShort left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());

        // Leftmost column has no element to the left
        OptionalShort empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

        OptionalShort right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());

        // Rightmost column has no element to the right
        OptionalShort empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new short[] { 1, 2, 3 }, m.row(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertArrayEquals(new short[] { 1, 4 }, m.column(0));
        assertArrayEquals(new short[] { 2, 5 }, m.column(1));
        assertArrayEquals(new short[] { 3, 6 }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new short[] { 10, 20 });
        assertArrayEquals(new short[] { 10, 20 }, m.row(0));
        assertArrayEquals(new short[] { 3, 4 }, m.row(1)); // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1 }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2, 3 }));
    }

    @Test
    public void testSetColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.setColumn(0, new short[] { 10, 20 });
        assertArrayEquals(new short[] { 10, 20 }, m.column(0));
        assertArrayEquals(new short[] { 2, 4 }, m.column(1)); // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1 }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2, 3 }));
    }

    @Test
    public void testUpdateRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, x -> (short) (x * 2));
        assertArrayEquals(new short[] { 2, 4 }, m.row(0));
        assertArrayEquals(new short[] { 3, 4 }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, x -> (short) (x + 10));
        assertArrayEquals(new short[] { 11, 13 }, m.column(0));
        assertArrayEquals(new short[] { 2, 4 }, m.column(1)); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new short[] { 1, 5, 9 }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setLU2RD(new short[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new short[] { 1 }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new short[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(x -> (short) (x * 10));
        assertEquals(10, m.get(0, 0));
        assertEquals(50, m.get(1, 1));
        assertEquals(90, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> (short) (x * 2)));
    }

    @Test
    public void testGetRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertArrayEquals(new short[] { 3, 5, 7 }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new short[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 2));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new short[] { 1 }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new short[] { 1, 2 }));
    }

    @Test
    public void testUpdateRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(x -> (short) (x * 10));
        assertEquals(30, m.get(0, 2));
        assertEquals(50, m.get(1, 1));
        assertEquals(70, m.get(2, 0));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> (short) (x * 2)));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(x -> (short) (x * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withIndices() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> (short) (i * 10 + j));
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.replaceIf(x -> x > 3, (short) 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(0, m.get(1, 0)); // was 4
        assertEquals(0, m.get(1, 1)); // was 5
        assertEquals(0, m.get(1, 2)); // was 6
    }

    @Test
    public void testReplaceIf_withIndices() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.replaceIf((i, j) -> i == j, (short) 0); // Replace diagonal
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
        assertEquals(2, m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix result = m.map(x -> (short) (x * 2));
        assertEquals(2, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(6, result.get(1, 0));
        assertEquals(8, result.get(1, 1));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
        assertEquals("val:1", result.get(0, 0));
        assertEquals("val:4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.fill((short) 99);
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
                assertEquals(99, m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        short[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(patch);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(2, 2)); // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        short[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(1, 1, patch);
        assertEquals(0, m.get(0, 0)); // unchanged
        assertEquals(1, m.get(1, 1));
        assertEquals(2, m.get(1, 2));
        assertEquals(3, m.get(2, 1));
        assertEquals(4, m.get(2, 2));
    }

    @Test
    public void testFill_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        short[][] patch = { { 1, 2 }, { 3, 4 } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix copy = m.copy();
        assertEquals(m.rowCount(), copy.rowCount());
        assertEquals(m.columnCount(), copy.columnCount());
        assertEquals(1, copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, (short) 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(99, copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix subset = m.copy(1, 3);
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals(4, subset.get(0, 0));
        assertEquals(9, subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals(2, submatrix.get(0, 0));
        assertEquals(6, submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(0, extended.get(3, 3)); // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
        assertEquals(1, truncated.get(0, 0));
        assertEquals(5, truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = m.extend(3, 3, (short) -1);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(-1, extended.get(2, 2)); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, (short) 0));
    }

    @Test
    public void testExtend_directional() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix extended = m.extend(1, 1, 2, 2);
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix extended = m.extend(1, 1, 1, 1, (short) -1);
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, (short) 0));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void testReverseH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
        assertEquals(6, m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals(5, m.get(0, 0));
        assertEquals(6, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(1, m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ShortMatrix flipped = m.flipV();
        assertEquals(5, flipped.get(0, 0));
        assertEquals(3, flipped.get(1, 0));
        assertEquals(1, flipped.get(2, 0));

        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate270();
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix transposed = m.transpose();
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix transposed = m.transpose();
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i));
        }
    }

    @Test
    public void testReshape_back() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix reshaped = m.reshape(1, 9);
        ShortMatrix reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        ShortMatrix reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix repeated = m.repelem(2, 3);
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortList flat = m.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, flat.get(i));
        }
    }

    @Test
    public void testFlatten_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        ShortList flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Integer> sums = new ArrayList<>();
        m.flatOp(row -> {
            int sum = 0;
            for (short val : row) {
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
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        ShortMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(7, stacked.get(2, 0));
        assertEquals(12, stacked.get(3, 2));
    }

    @Test
    public void testVstack_differentColumnCounts() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(5, stacked.get(0, 2));
        assertEquals(8, stacked.get(1, 3));
    }

    @Test
    public void testHstack_differentRowCounts() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix sum = m1.add(m2);

        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(10, sum.get(1, 0));
        assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix diff = m1.subtract(m2);

        assertEquals(4, diff.get(0, 0));
        assertEquals(4, diff.get(0, 1));
        assertEquals(4, diff.get(1, 0));
        assertEquals(4, diff.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix product = m1.multiply(m2);

        assertEquals(19, product.get(0, 0)); // 1*5 + 2*7
        assertEquals(22, product.get(0, 1)); // 1*6 + 2*8
        assertEquals(43, product.get(1, 0)); // 3*5 + 4*7
        assertEquals(50, product.get(1, 1)); // 3*6 + 4*8
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 } }); // 1x3
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 4 }, { 5 }, { 6 } }); // 3x1
        ShortMatrix product = m1.multiply(m2);

        assertEquals(1, product.rowCount());
        assertEquals(1, product.columnCount());
        assertEquals(32, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix<Short> boxed = m.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(3, boxed.columnCount());
        assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
        assertEquals(Short.valueOf((short) 6), boxed.get(1, 2));
    }

    @Test
    public void testToIntMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix intMatrix = m.toIntMatrix();
        assertEquals(2, intMatrix.rowCount());
        assertEquals(2, intMatrix.columnCount());
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix longMatrix = m.toLongMatrix();
        assertEquals(2, longMatrix.rowCount());
        assertEquals(2, longMatrix.columnCount());
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(2, floatMatrix.rowCount());
        assertEquals(2, floatMatrix.columnCount());
        assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
        assertEquals(4.0f, floatMatrix.get(1, 1), 0.0001f);
    }

    @Test
    public void testToDoubleMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(2, doubleMatrix.rowCount());
        assertEquals(2, doubleMatrix.columnCount());
        assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a * b));

        assertEquals(5, result.get(0, 0)); // 1*5
        assertEquals(12, result.get(0, 1)); // 2*6
        assertEquals(21, result.get(1, 0)); // 3*7
        assertEquals(32, result.get(1, 1)); // 4*8
    }

    @Test
    public void testZipWith_differentShapes() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (short) (a + b)));
    }

    @Test
    public void testZipWith_threeMatrices() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 9, 10 }, { 11, 12 } });
        ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));

        assertEquals(15, result.get(0, 0)); // 1+5+9
        assertEquals(18, result.get(0, 1)); // 2+6+10
        assertEquals(21, result.get(1, 0)); // 3+7+11
        assertEquals(24, result.get(1, 1)); // 4+8+12
    }

    @Test
    public void testZipWith_threeMatrices_differentShapes() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 9, 10, 11 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c)));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new short[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testStreamLU2RD_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new short[] { 3, 5, 7 }, antiDiagonal);
    }

    @Test
    public void testStreamRU2LD_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.streamRU2LD().toArray().length);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        ShortMatrix nonSquare = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] all = m.streamH().toArray();
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.streamH().toArray().length);
    }

    @Test
    public void testStreamH_withRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new short[] { 4, 5, 6, 7, 8, 9 }, rows);
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] all = m.streamV().toArray();
        assertArrayEquals(new short[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.streamV().toArray().length);
    }

    @Test
    public void testStreamV_withColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new short[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] columnCount = m.streamV(1, 3).toArray();
        assertArrayEquals(new short[] { 2, 5, 8, 3, 6, 9 }, columnCount);
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<short[]> rows = m.streamR().map(ShortStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new short[] { 1, 2, 3 }, rows.get(0));
        assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(1));
    }

    @Test
    public void testStreamR_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<short[]> rows = m.streamR(1, 3).map(ShortStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new short[] { 4, 5, 6 }, rows.get(0));
        assertArrayEquals(new short[] { 7, 8, 9 }, rows.get(1));
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<short[]> columnCount = m.streamC().map(ShortStream::toArray).toList();
        assertEquals(3, columnCount.size());
        assertArrayEquals(new short[] { 1, 4 }, columnCount.get(0));
        assertArrayEquals(new short[] { 2, 5 }, columnCount.get(1));
        assertArrayEquals(new short[] { 3, 6 }, columnCount.get(2));
    }

    @Test
    public void testStreamC_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<short[]> columnCount = m.streamC(1, 3).map(ShortStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new short[] { 2, 5, 8 }, columnCount.get(0));
        assertArrayEquals(new short[] { 3, 6, 9 }, columnCount.get(1));
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
    }

    // ============ Object Methods Tests ============

    @Test
    public void testHashCode() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 4, 3 } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 4, 3 } });
        ShortMatrix m4 = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    // ============ Edge Case Tests ============

    @Test
    public void testShortMinMaxValues() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { Short.MAX_VALUE, Short.MIN_VALUE } });
        assertEquals(Short.MAX_VALUE, m.get(0, 0));
        assertEquals(Short.MIN_VALUE, m.get(0, 1));
    }

    @Test
    public void testShortOverflow() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { Short.MAX_VALUE, 1 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, Short.MAX_VALUE } });

        // Addition overflow wraps around
        ShortMatrix sum = m1.add(m2);
        assertTrue(sum.get(0, 0) < 0); // Overflow
    }

    @Test
    public void testNegativeShorts() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { -1, -2 }, { -3, -4 } });

        assertEquals(-1, m.get(0, 0));
        assertEquals(-4, m.get(1, 1));

        ShortMatrix doubled = m.map(x -> (short) (x * 2));
        assertEquals(-2, doubled.get(0, 0));
        assertEquals(-8, doubled.get(1, 1));
    }

    @Test
    public void testEmptyMatrixOperations() {
        ShortMatrix empty = ShortMatrix.empty();

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rowCount);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        ShortMatrix extended = empty.extend(2, 2, (short) 5);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(5, extended.get(0, 0));
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<Short> values = new ArrayList<>();
        m.forEach((short v) -> values.add(v));
        assertEquals(4, values.size());
        assertEquals(Short.valueOf((short) 1), values.get(0));
        assertEquals(Short.valueOf((short) 2), values.get(1));
        assertEquals(Short.valueOf((short) 3), values.get(2));
        assertEquals(Short.valueOf((short) 4), values.get(3));
    }

    @Test
    public void testForEach_withRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Short> values = new ArrayList<>();
        m.forEach(1, 3, 1, 3, (short v) -> values.add(v));
        assertEquals(4, values.size());
        assertEquals(Short.valueOf((short) 5), values.get(0));
        assertEquals(Short.valueOf((short) 6), values.get(1));
        assertEquals(Short.valueOf((short) 8), values.get(2));
        assertEquals(Short.valueOf((short) 9), values.get(3));
    }

    @Test
    public void testForEach_emptyRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<Short> values = new ArrayList<>();
        m.forEach(0, 0, 0, 0, (short v) -> values.add(v));
        assertTrue(values.isEmpty());
    }

    @Test
    public void testForEach_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, x -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, x -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, -1, 2, x -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 2, 0, 3, x -> {
        }));
    }

    // ============ Additional Edge Case Tests ============

    @Test
    public void testRepeat_zeroLength() {
        ShortMatrix m = ShortMatrix.repeat((short) 5, 0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRandom_zeroLength() {
        ShortMatrix m = ShortMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRange_emptyRange() {
        ShortMatrix m = ShortMatrix.range((short) 5, (short) 5);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRangeClosed_singleElement() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 5, (short) 5);
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(5, m.get(0, 0));
    }

    @Test
    public void testExtend_sameSize() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = m.extend(2, 2);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
    }

    @Test
    public void testExtend_emptyMatrix() {
        ShortMatrix empty = ShortMatrix.empty();
        ShortMatrix extended = empty.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionalZeroExtension() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = m.extend(0, 0, 0, 0);
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
    }

    @Test
    public void testRepelem_oneDimension() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix repeated = m.repelem(1, 2);
        assertEquals(1, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(2, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
    }

    @Test
    public void testRepmat_oneDimension() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix repeated = m.repmat(1, 2);
        assertEquals(1, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
    }

    @Test
    public void testCopy_entireMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix copy = m.copy(0, 2);
        assertEquals(2, copy.rowCount());
        assertEquals(3, copy.columnCount());
        assertEquals(1, copy.get(0, 0));
        assertEquals(6, copy.get(1, 2));
    }

    @Test
    public void testCopy_entireMatrixWithColumnRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix copy = m.copy(0, 2, 0, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(3, copy.columnCount());
        assertEquals(1, copy.get(0, 0));
        assertEquals(6, copy.get(1, 2));
    }

    @Test
    public void testFill_entireMatrixWithArray() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.fill(new short[][] { { 9, 8 }, { 7, 6 } });
        assertEquals(9, m.get(0, 0));
        assertEquals(8, m.get(0, 1));
        assertEquals(7, m.get(1, 0));
        assertEquals(6, m.get(1, 1));
    }

    @Test
    public void testFill_withArrayAtOrigin() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.fill(0, 0, new short[][] { { 99, 88 } });
        assertEquals(99, m.get(0, 0));
        assertEquals(88, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(4, m.get(1, 0));
    }

    @Test
    public void testReverseH_singleColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 } });
        m.reverseH();
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 0));
    }

    @Test
    public void testReverseV_singleRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        m.reverseV();
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
    }

    @Test
    public void testRotate90_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix rotated = m.rotate90();
        assertEquals(3, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(6, rotated.get(2, 0));
        assertEquals(3, rotated.get(2, 1));
    }

    @Test
    public void testRotate180_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(3, rotated.columnCount());
        assertEquals(6, rotated.get(0, 0));
        assertEquals(5, rotated.get(0, 1));
        assertEquals(4, rotated.get(0, 2));
        assertEquals(3, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
        assertEquals(1, rotated.get(1, 2));
    }

    @Test
    public void testRotate270_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix rotated = m.rotate270();
        assertEquals(3, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(6, rotated.get(0, 1));
        assertEquals(1, rotated.get(2, 0));
        assertEquals(4, rotated.get(2, 1));
    }

    @Test
    public void testReshape_toSingleRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix reshaped = m.reshape(1, 4);
        assertEquals(1, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
        assertArrayEquals(new short[] { 1, 2, 3, 4 }, reshaped.row(0));
    }

    @Test
    public void testReshape_toSingleColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix reshaped = m.reshape(4, 1);
        assertEquals(4, reshaped.rowCount());
        assertEquals(1, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(1, 0));
        assertEquals(3, reshaped.get(2, 0));
        assertEquals(4, reshaped.get(3, 0));
    }

    @Test
    public void testVstack_sameMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix stacked = m.vstack(m);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(1, stacked.get(1, 0));
    }

    @Test
    public void testHstack_sameMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 } });
        ShortMatrix stacked = m.hstack(m);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(1, stacked.get(0, 1));
    }

    @Test
    public void testAdd_singleElement() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
        ShortMatrix sum = m1.add(m2);
        assertEquals(1, sum.rowCount());
        assertEquals(1, sum.columnCount());
        assertEquals(8, sum.get(0, 0));
    }

    @Test
    public void testSubtract_singleElement() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
        ShortMatrix diff = m1.subtract(m2);
        assertEquals(1, diff.rowCount());
        assertEquals(1, diff.columnCount());
        assertEquals(2, diff.get(0, 0));
    }

    @Test
    public void testMultiply_singleElement() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
        ShortMatrix product = m1.multiply(m2);
        assertEquals(1, product.rowCount());
        assertEquals(1, product.columnCount());
        assertEquals(15, product.get(0, 0));
    }

    @Test
    public void testStreamH_emptyRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        short[] row = m.streamH(0, 0).toArray();
        assertEquals(0, row.length);
    }

    @Test
    public void testStreamV_emptyColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        short[] col = m.streamV(0, 0).toArray();
        assertEquals(0, col.length);
    }

    @Test
    public void testStreamR_emptyRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        long count = m.streamR(1, 1).count();
        assertEquals(0, count);
    }

    @Test
    public void testStreamC_emptyRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        long count = m.streamC(1, 1).count();
        assertEquals(0, count);
    }

    @Test
    public void testMapToObj_withNull() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> result = m.mapToObj(x -> x == 2 ? null : String.valueOf(x), String.class);
        assertEquals("1", result.get(0, 0));
        assertEquals(null, result.get(0, 1));
        assertEquals("3", result.get(1, 0));
    }

    @Test
    public void testUpdateAll_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        empty.updateAll(x -> (short) (x + 1));
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testReplaceIf_noMatches() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf(x -> x > 100, (short) 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_allMatch() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf(x -> x > 0, (short) 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
        assertEquals(0, m.get(1, 1));
    }

    @Test
    public void testZipWith_singleElement() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
        ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
        assertEquals(1, result.rowCount());
        assertEquals(1, result.columnCount());
        assertEquals(5, result.get(0, 0));
    }

    @Test
    public void testZipWith_threeMatrices_singleElement() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 3 } });
        ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
        assertEquals(1, result.rowCount());
        assertEquals(1, result.columnCount());
        assertEquals(6, result.get(0, 0));
    }

    @Test
    public void testFlatOp_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        List<Integer> results = new ArrayList<>();
        empty.flatOp(row -> results.add(row.length));
        assertTrue(results.isEmpty());
    }

    @Test
    public void testDiagonalLU2RD_singleElement() {
        ShortMatrix m = ShortMatrix.diagonalLU2RD(new short[] { 5 });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(5, m.get(0, 0));
    }

    @Test
    public void testDiagonalRU2LD_singleElement() {
        ShortMatrix m = ShortMatrix.diagonalRU2LD(new short[] { 5 });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(5, m.get(0, 0));
    }

    @Test
    public void testBoxed_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        Matrix<Short> boxed = empty.boxed();
        assertEquals(0, boxed.rowCount());
        assertEquals(0, boxed.columnCount());
    }

    @Test
    public void testToIntMatrix_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        IntMatrix intMatrix = empty.toIntMatrix();
        assertEquals(0, intMatrix.rowCount());
        assertEquals(0, intMatrix.columnCount());
    }

    @Test
    public void testToLongMatrix_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        LongMatrix longMatrix = empty.toLongMatrix();
        assertEquals(0, longMatrix.rowCount());
        assertEquals(0, longMatrix.columnCount());
    }

    @Test
    public void testToFloatMatrix_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        FloatMatrix floatMatrix = empty.toFloatMatrix();
        assertEquals(0, floatMatrix.rowCount());
        assertEquals(0, floatMatrix.columnCount());
    }

    @Test
    public void testToDoubleMatrix_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        DoubleMatrix doubleMatrix = empty.toDoubleMatrix();
        assertEquals(0, doubleMatrix.rowCount());
        assertEquals(0, doubleMatrix.columnCount());
    }

    @Test
    public void testEquals_emptyMatrices() {
        ShortMatrix empty1 = ShortMatrix.empty();
        ShortMatrix empty2 = ShortMatrix.of(new short[0][0]);
        assertTrue(empty1.equals(empty2));
    }

    @Test
    public void testHashCode_emptyMatrices() {
        ShortMatrix empty1 = ShortMatrix.empty();
        ShortMatrix empty2 = ShortMatrix.of(new short[0][0]);
        assertEquals(empty1.hashCode(), empty2.hashCode());
    }

    @Test
    public void testToString_empty() {
        ShortMatrix empty = ShortMatrix.empty();
        String str = empty.toString();
        assertNotNull(str);
    }

    @Test
    public void testToString_singleElement() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 42 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("42"));
    }

    @Test
    public void testUpdateRow_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(-1, x -> x));
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateRow(2, x -> x));
    }

    @Test
    public void testUpdateColumn_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(-1, x -> x));
        assertThrows(IndexOutOfBoundsException.class, () -> m.updateColumn(2, x -> x));
    }

    @Test
    public void testTranspose_singleRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        ShortMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(1, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(3, transposed.get(2, 0));
    }

    @Test
    public void testTranspose_singleColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 }, { 2 }, { 3 } });
        ShortMatrix transposed = m.transpose();
        assertEquals(1, transposed.rowCount());
        assertEquals(3, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(2, transposed.get(0, 1));
        assertEquals(3, transposed.get(0, 2));
    }

    @Test
    public void testAdjacentPoints_emptyMatrix() {
        ShortMatrix empty = ShortMatrix.empty();
        // Can't test adjacent points on empty matrix as there are no valid indices
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testUpOf_emptyMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort up = m.upOf(0, 1);
        assertFalse(up.isPresent());
    }

    @Test
    public void testDownOf_bottomRow() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort down = m.downOf(1, 1);
        assertFalse(down.isPresent());
    }

    @Test
    public void testLeftOf_leftmostColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort left = m.leftOf(1, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testRightOf_rightmostColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        OptionalShort right = m.rightOf(1, 1);
        assertFalse(right.isPresent());
    }
}
