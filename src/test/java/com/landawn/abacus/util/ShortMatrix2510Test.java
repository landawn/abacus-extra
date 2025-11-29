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

@Tag("2510")
public class ShortMatrix2510Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        ShortMatrix m = new ShortMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        ShortMatrix m = new ShortMatrix(new short[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        ShortMatrix m = new ShortMatrix(new short[][] { { 42 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(ShortMatrix.empty(), ShortMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = ShortMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat() {
        ShortMatrix m = ShortMatrix.repeat((short) 42, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void testRange() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new short[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new short[] { 0, 2, 4, 6, 8 }, m.row(0));
    }

    @Test
    public void testRange_withNegativeStep() {
        ShortMatrix m = ShortMatrix.range((short) 10, (short) 0, (short) -2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new short[] { 10, 8, 6, 4, 2 }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 4);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new short[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rows);
        assertEquals(6, m.cols);
        assertArrayEquals(new short[] { 0, 2, 4, 6, 8, 10 }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        ShortMatrix m = ShortMatrix.diagonalLU2RD(new short[] { 1, 2, 3 });
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
        ShortMatrix m = ShortMatrix.diagonalRU2LD(new short[] { 1, 2, 3 });
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
        ShortMatrix m = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
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
        ShortMatrix m = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        ShortMatrix m = ShortMatrix.diagonal(null, new short[] { 4, 5, 6 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
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
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(4, unboxed.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Short[][] boxed = { { 1, null }, { null, 4 } };
        Matrix<Short> boxedMatrix = Matrix.of(boxed);
        ShortMatrix unboxed = ShortMatrix.unbox(boxedMatrix);
        assertEquals(1, unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1));   // null -> 0
        assertEquals(0, unboxed.get(1, 0));   // null -> 0
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
        assertArrayEquals(new short[] { 3, 4 }, m.row(1));   // unchanged
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
        assertArrayEquals(new short[] { 2, 4 }, m.column(1));   // unchanged
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
        assertArrayEquals(new short[] { 3, 4 }, m.row(1));   // unchanged
    }

    @Test
    public void testUpdateColumn() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, x -> (short) (x + 10));
        assertArrayEquals(new short[] { 11, 13 }, m.column(0));
        assertArrayEquals(new short[] { 2, 4 }, m.column(1));   // unchanged
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
        assertEquals(2, m.get(0, 1));   // unchanged
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
        assertEquals(2, m.get(0, 1));   // unchanged
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
        assertEquals(0, m.get(1, 0));   // was 4
        assertEquals(0, m.get(1, 1));   // was 5
        assertEquals(0, m.get(1, 2));   // was 6
    }

    @Test
    public void testReplaceIf_withIndices() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.replaceIf((i, j) -> i == j, (short) 0);   // Replace diagonal
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
        assertEquals(2, m.get(0, 1));   // unchanged
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
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
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
        assertEquals(0, m.get(2, 2));   // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        short[][] patch = { { 1, 2 }, { 3, 4 } };
        m.fill(1, 1, patch);
        assertEquals(0, m.get(0, 0));   // unchanged
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
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
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
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
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
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
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
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(0, extended.get(3, 3));   // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertEquals(1, truncated.get(0, 0));
        assertEquals(5, truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix extended = m.extend(3, 3, (short) -1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(-1, extended.get(2, 2));   // new cell
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
        assertEquals(5, extended.rows);   // 1 + 3 + 1
        assertEquals(7, extended.cols);   // 2 + 3 + 2

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
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix rotated = m.rotate270();
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortMatrix transposed = m.transpose();
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix transposed = m.transpose();
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ShortMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
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
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
        assertEquals(2, repeated.get(0, 4));
        assertEquals(2, repeated.get(0, 5));
        assertEquals(1, repeated.get(1, 0));   // second row same as first
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
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));   // repeat starts
        assertEquals(2, repeated.get(0, 3));

        assertEquals(3, repeated.get(1, 0));
        assertEquals(4, repeated.get(1, 1));
        assertEquals(3, repeated.get(1, 2));   // repeat
        assertEquals(4, repeated.get(1, 3));

        // Second vertical repeat
        assertEquals(1, repeated.get(2, 0));
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
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ShortList flat = m.flatten();
        assertEquals(6, flat.size());
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, flat.array());
    }

    @Test
    public void testFlatten_empty() {
        ShortMatrix m = ShortMatrix.empty();
        ShortList flat = m.flatten();
        assertEquals(0, flat.size());
    }

    // ============ FlatOp Tests ============

    @Test
    public void testFlatOp() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        final int[] count = { 0 };
        m.flatOp(row -> count[0] += row.length);
        assertEquals(4, count[0]);
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix stacked = m1.vstack(m2);
        assertEquals(4, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(4, stacked.get(1, 1));
        assertEquals(5, stacked.get(2, 0));
        assertEquals(8, stacked.get(3, 1));
    }

    @Test
    public void testVstack_differentColumnCount() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(5, stacked.get(0, 2));
        assertEquals(6, stacked.get(0, 3));
    }

    @Test
    public void testHstack_differentRowCount() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix result = m1.add(m2);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix result = m1.subtract(m2);
        assertEquals(4, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(4, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 2, 3 }, { 4, 5 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix result = m1.multiply(m2);
        assertEquals(11, result.get(0, 0));
        assertEquals(16, result.get(0, 1));
        assertEquals(19, result.get(1, 0));
        assertEquals(28, result.get(1, 1));
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Short> boxed = m.boxed();
        assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
        assertEquals(Short.valueOf((short) 4), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.toIntMatrix();
        assertEquals(1, result.get(0, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.toLongMatrix();
        assertEquals(1L, result.get(0, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix result = m.toFloatMatrix();
        assertEquals(1.0f, result.get(0, 0), 0.001f);
        assertEquals(4.0f, result.get(1, 1), 0.001f);
    }

    @Test
    public void testToDoubleMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(1.0, result.get(0, 0), 0.001);
        assertEquals(4.0, result.get(1, 1), 0.001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_twoMatrices() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testZipWith_twoMatrices_differentDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (short) (a + b)));
    }

    @Test
    public void testZipWith_threeMatrices() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 9, 10 }, { 11, 12 } });
        ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
        assertEquals(15, result.get(0, 0));   // 1 + 5 + 9
        assertEquals(18, result.get(0, 1));   // 2 + 6 + 10
        assertEquals(21, result.get(1, 0));   // 3 + 7 + 11
        assertEquals(24, result.get(1, 1));   // 4 + 8 + 12
    }

    @Test
    public void testZipWith_threeMatrices_differentDimensions() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 5, 6 }, { 7, 8 } });
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
    public void testStreamLU2RD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.streamLU2RD().toArray());
    }

    @Test
    public void testStreamRU2LD() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] diagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new short[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.streamRU2LD().toArray());
    }

    @Test
    public void testStreamH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] all = m.streamH().toArray();
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_withRowIndex() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new short[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_withRowRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        short[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new short[] { 4, 5, 6, 7, 8, 9 }, rows);
    }

    @Test
    public void testStreamH_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 2));
    }

    @Test
    public void testStreamV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] all = m.streamV().toArray();
        assertArrayEquals(new short[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_withColumnIndex() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new short[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_withColumnRange() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        short[] cols = m.streamV(1, 3).toArray();
        assertArrayEquals(new short[] { 2, 5, 3, 6 }, cols);
    }

    @Test
    public void testStreamV_outOfBounds() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
    }

    // ============ Stream of Streams Tests ============

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
        List<short[]> cols = m.streamC().map(ShortStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new short[] { 1, 3 }, cols.get(0));
        assertArrayEquals(new short[] { 2, 4 }, cols.get(1));
    }

    // ============ Points Stream Tests ============

    @Test
    public void testPointsH() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsV() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
        assertEquals(Point.of(0, 1), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    // ============ Sum Tests ============
    // Note: sumByInt(), sumByLong(), sumByDouble() methods don't exist in ShortMatrix

    // ============ Average Tests ============
    // Note: averageByInt(), averageByLong(), averageByDouble() methods don't exist in ShortMatrix

    // ============ Empty Matrix Tests ============

    @Test
    public void testIsEmpty_true() {
        ShortMatrix empty = ShortMatrix.empty();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testIsEmpty_false() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
        assertFalse(m.isEmpty());
    }

    // ============ Equals and HashCode Tests ============

    @Test
    public void testEquals() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m3 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 5 } });

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    @Test
    public void testHashCode_equal() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });

        assertEquals(m1.hashCode(), m2.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
