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
import com.landawn.abacus.util.u.OptionalChar;
import com.landawn.abacus.util.stream.CharStream;

@Tag("2025")
public class CharMatrix2025Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        char[][] arr = { { 'A', 'B' }, { 'C', 'D' } };
        CharMatrix m = new CharMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals('A', m.get(0, 0));
        assertEquals('D', m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        CharMatrix m = new CharMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        CharMatrix m = new CharMatrix(new char[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        CharMatrix m = new CharMatrix(new char[][] { { 'X' } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals('X', m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(CharMatrix.empty(), CharMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        char[][] arr = { { 'A', 'B' }, { 'C', 'D' } };
        CharMatrix m = CharMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals('A', m.get(0, 0));
    }

    @Test
    public void testOf_withNullArray() {
        CharMatrix m = CharMatrix.of((char[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withEmptyArray() {
        CharMatrix m = CharMatrix.of(new char[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testRandom() {
        CharMatrix m = CharMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        // Just verify elements exist (values are random)
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withRowsCols() {
        CharMatrix m = CharMatrix.random(2, 3);
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
        CharMatrix m = CharMatrix.repeat('Z', 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals('Z', m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withRowsCols() {
        CharMatrix m = CharMatrix.repeat(2, 3, 'Z');
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals('Z', m.get(i, j));
            }
        }
    }

    @Test
    public void testRange() {
        CharMatrix m = CharMatrix.range('A', 'F');
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E' }, m.row(0));
    }

    @Test
    public void testRange_withStep() {
        CharMatrix m = CharMatrix.range('A', 'K', 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new char[] { 'A', 'C', 'E', 'G', 'I' }, m.row(0));
    }

    @Test
    public void testRange_withNegativeStep() {
        CharMatrix m = CharMatrix.range('J', 'A', -2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new char[] { 'J', 'H', 'F', 'D', 'B' }, m.row(0));
    }

    @Test
    public void testRangeClosed() {
        CharMatrix m = CharMatrix.rangeClosed('A', 'E');
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E' }, m.row(0));
    }

    @Test
    public void testRangeClosed_withStep() {
        CharMatrix m = CharMatrix.rangeClosed('A', 'K', 2);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertArrayEquals(new char[] { 'A', 'C', 'E', 'G', 'I', 'K' }, m.row(0));
    }

    @Test
    public void testDiagonalLU2RD() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[] { 'A', 'B', 'C' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('A', m.get(0, 0));
        assertEquals('B', m.get(1, 1));
        assertEquals('C', m.get(2, 2));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        CharMatrix m = CharMatrix.diagonalRU2LD(new char[] { 'X', 'Y', 'Z' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('X', m.get(0, 2));
        assertEquals('Y', m.get(1, 1));
        assertEquals('Z', m.get(2, 0));
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'A', 'B', 'C' }, new char[] { 'X', 'Y', 'Z' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('A', m.get(0, 0));
        assertEquals('B', m.get(1, 1));
        assertEquals('C', m.get(2, 2));
        assertEquals('X', m.get(0, 2));
        assertEquals('Z', m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'P', 'Q', 'R' }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('P', m.get(0, 0));
        assertEquals('Q', m.get(1, 1));
        assertEquals('R', m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        CharMatrix m = CharMatrix.diagonal(null, new char[] { 'X', 'Y', 'Z' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('X', m.get(0, 2));
        assertEquals('Y', m.get(1, 1));
        assertEquals('Z', m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        CharMatrix m = CharMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonal(new char[] { 'A', 'B' }, new char[] { 'X', 'Y', 'Z' }));
    }

    @Test
    public void testUnbox() {
        Character[][] boxed = { { 'A', 'B' }, { 'C', 'D' } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rowCount());
        assertEquals(2, unboxed.columnCount());
        assertEquals('A', unboxed.get(0, 0));
        assertEquals('D', unboxed.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Character[][] boxed = { { 'A', null }, { null, 'D' } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        assertEquals('A', unboxed.get(0, 0));
        assertEquals(0, unboxed.get(0, 1));   // null -> 0
        assertEquals(0, unboxed.get(1, 0));   // null -> 0
        assertEquals('D', unboxed.get(1, 1));
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A' } });
        assertEquals(char.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        assertEquals('A', m.get(0, 0));
        assertEquals('E', m.get(1, 1));
        assertEquals('F', m.get(1, 2));
    }

    @Test
    public void testGet_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertEquals('A', m.get(Point.of(0, 0)));
        assertEquals('D', m.get(Point.of(1, 1)));
        assertEquals('B', m.get(Point.of(0, 1)));
    }

    @Test
    public void testSet() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.set(0, 0, 'X');
        assertEquals('X', m.get(0, 0));

        m.set(1, 1, 'Y');
        assertEquals('Y', m.get(1, 1));
    }

    @Test
    public void testSet_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 'X'));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 'X'));
    }

    @Test
    public void testSetWithPoint() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.set(Point.of(0, 0), 'Z');
        assertEquals('Z', m.get(Point.of(0, 0)));
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

        OptionalChar up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals('A', up.get());

        // Top row has no element above
        OptionalChar empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

        OptionalChar down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals('C', down.get());

        // Bottom row has no element below
        OptionalChar empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

        OptionalChar left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals('A', left.get());

        // Leftmost column has no element to the left
        OptionalChar empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

        OptionalChar right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals('B', right.get());

        // Rightmost column has no element to the right
        OptionalChar empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, m.row(0));
        assertArrayEquals(new char[] { 'D', 'E', 'F' }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        assertArrayEquals(new char[] { 'A', 'D' }, m.column(0));
        assertArrayEquals(new char[] { 'B', 'E' }, m.column(1));
        assertArrayEquals(new char[] { 'C', 'F' }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.setRow(0, new char[] { 'X', 'Y' });
        assertArrayEquals(new char[] { 'X', 'Y' }, m.row(0));
        assertArrayEquals(new char[] { 'C', 'D' }, m.row(1));   // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new char[] { 'A' }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new char[] { 'A', 'B', 'C' }));
    }

    @Test
    public void testSetColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.setColumn(0, new char[] { 'X', 'Y' });
        assertArrayEquals(new char[] { 'X', 'Y' }, m.column(0));
        assertArrayEquals(new char[] { 'B', 'D' }, m.column(1));   // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new char[] { 'A' }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new char[] { 'A', 'B', 'C' }));
    }

    @Test
    public void testUpdateRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.updateRow(0, x -> (char) (x + 1));
        assertArrayEquals(new char[] { 'B', 'C' }, m.row(0));
        assertArrayEquals(new char[] { 'C', 'D' }, m.row(1));   // unchanged
    }

    @Test
    public void testUpdateColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.updateColumn(0, x -> (char) (x + 1));
        assertArrayEquals(new char[] { 'B', 'D' }, m.column(0));
        assertArrayEquals(new char[] { 'B', 'D' }, m.column(1));   // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        assertArrayEquals(new char[] { 'A', 'E', 'I' }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        m.setLU2RD(new char[] { 'X', 'Y', 'Z' });
        assertEquals('X', m.get(0, 0));
        assertEquals('Y', m.get(1, 1));
        assertEquals('Z', m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new char[] { 'X' }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new char[] { 'X', 'Y' }));
    }

    @Test
    public void testUpdateLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        m.updateLU2RD(x -> (char) (x + 1));
        assertEquals('B', m.get(0, 0));
        assertEquals('F', m.get(1, 1));
        assertEquals('J', m.get(2, 2));
        assertEquals('B', m.get(0, 1));   // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> (char) (x + 1)));
    }

    @Test
    public void testGetRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        assertArrayEquals(new char[] { 'C', 'E', 'G' }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        m.setRU2LD(new char[] { 'X', 'Y', 'Z' });
        assertEquals('X', m.get(0, 2));
        assertEquals('Y', m.get(1, 1));
        assertEquals('Z', m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new char[] { 'X' }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new char[] { 'X', 'Y' }));
    }

    @Test
    public void testUpdateRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        m.updateRU2LD(x -> (char) (x + 1));
        assertEquals('D', m.get(0, 2));
        assertEquals('F', m.get(1, 1));
        assertEquals('H', m.get(2, 0));
        assertEquals('B', m.get(0, 1));   // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> (char) (x + 1)));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.updateAll(x -> (char) (x + 1));
        assertEquals('B', m.get(0, 0));
        assertEquals('C', m.get(0, 1));
        assertEquals('D', m.get(1, 0));
        assertEquals('E', m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withIndices() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'A' }, { 'A', 'A' } });
        m.updateAll((i, j) -> (char) ('A' + i * 10 + j));
        assertEquals('A', m.get(0, 0));
        assertEquals('B', m.get(0, 1));
        assertEquals('K', m.get(1, 0));
        assertEquals('L', m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        m.replaceIf(x -> x > 'C', '?');
        assertEquals('A', m.get(0, 0));
        assertEquals('B', m.get(0, 1));
        assertEquals('C', m.get(0, 2));
        assertEquals('?', m.get(1, 0));   // was D
        assertEquals('?', m.get(1, 1));   // was E
        assertEquals('?', m.get(1, 2));   // was F
    }

    @Test
    public void testReplaceIf_withIndices() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        m.replaceIf((i, j) -> i == j, 'X');   // Replace diagonal
        assertEquals('X', m.get(0, 0));
        assertEquals('X', m.get(1, 1));
        assertEquals('X', m.get(2, 2));
        assertEquals('B', m.get(0, 1));   // unchanged
    }

    @Test
    public void testMap() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix result = m.map(x -> (char) (x + 1));
        assertEquals('B', result.get(0, 0));
        assertEquals('C', result.get(0, 1));
        assertEquals('D', result.get(1, 0));
        assertEquals('E', result.get(1, 1));

        // Original unchanged
        assertEquals('A', m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        Matrix<String> result = m.mapToObj(x -> "char:" + x, String.class);
        assertEquals("char:A", result.get(0, 0));
        assertEquals("char:D", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        m.fill('Z');
        for (int i = 0; i < m.rowCount(); i++) {
            for (int j = 0; j < m.columnCount(); j++) {
                assertEquals('Z', m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'A', 'A' }, { 'A', 'A', 'A' }, { 'A', 'A', 'A' } });
        char[][] patch = { { 'X', 'Y' }, { 'Z', 'W' } };
        m.fill(patch);
        assertEquals('X', m.get(0, 0));
        assertEquals('Y', m.get(0, 1));
        assertEquals('Z', m.get(1, 0));
        assertEquals('W', m.get(1, 1));
        assertEquals('A', m.get(2, 2));   // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'A', 'A' }, { 'A', 'A', 'A' }, { 'A', 'A', 'A' } });
        char[][] patch = { { 'X', 'Y' }, { 'Z', 'W' } };
        m.fill(1, 1, patch);
        assertEquals('A', m.get(0, 0));   // unchanged
        assertEquals('X', m.get(1, 1));
        assertEquals('Y', m.get(1, 2));
        assertEquals('Z', m.get(2, 1));
        assertEquals('W', m.get(2, 2));
    }

    @Test
    public void testFill_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        char[][] patch = { { 'X', 'Y' }, { 'Z', 'W' } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix copy = m.copy();
        assertEquals(m.rowCount(), copy.rowCount());
        assertEquals(m.columnCount(), copy.columnCount());
        assertEquals('A', copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, 'Z');
        assertEquals('A', m.get(0, 0));
        assertEquals('Z', copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix subset = m.copy(1, 3);
        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals('D', subset.get(0, 0));
        assertEquals('I', subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals('B', submatrix.get(0, 0));
        assertEquals('F', submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals('A', extended.get(0, 0));
        assertEquals('D', extended.get(1, 1));
        assertEquals(0, extended.get(3, 3));   // new cells are 0
    }

    @Test
    public void testExtend_smaller() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
        assertEquals('A', truncated.get(0, 0));
        assertEquals('E', truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix extended = m.extend(3, 3, '?');
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals('A', extended.get(0, 0));
        assertEquals('?', extended.get(2, 2));   // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, '?'));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, '?'));
    }

    @Test
    public void testExtend_directional() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rowCount());   // 1 + 3 + 1
        assertEquals(7, extended.columnCount());   // 2 + 3 + 2

        // Original values at offset position
        assertEquals('A', extended.get(1, 2));
        assertEquals('E', extended.get(2, 3));

        // New cells are 0
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionalWithDefault() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix extended = m.extend(1, 1, 1, 1, '?');
        assertEquals(5, extended.rowCount());
        assertEquals(5, extended.columnCount());

        // Check original values
        assertEquals('A', extended.get(1, 1));

        // Check new values
        assertEquals('?', extended.get(0, 0));
        assertEquals('?', extended.get(4, 4));
    }

    @Test
    public void testExtend_directionalWithNegativeValues() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, '?'));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void testReverseH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        m.reverseH();
        assertEquals('C', m.get(0, 0));
        assertEquals('B', m.get(0, 1));
        assertEquals('A', m.get(0, 2));
        assertEquals('F', m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } });
        m.reverseV();
        assertEquals('E', m.get(0, 0));
        assertEquals('F', m.get(0, 1));
        assertEquals('C', m.get(1, 0));
        assertEquals('A', m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        CharMatrix flipped = m.flipH();
        assertEquals('C', flipped.get(0, 0));
        assertEquals('B', flipped.get(0, 1));
        assertEquals('A', flipped.get(0, 2));

        // Original unchanged
        assertEquals('A', m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } });
        CharMatrix flipped = m.flipV();
        assertEquals('E', flipped.get(0, 0));
        assertEquals('C', flipped.get(1, 0));
        assertEquals('A', flipped.get(2, 0));

        // Original unchanged
        assertEquals('A', m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals('C', rotated.get(0, 0));
        assertEquals('A', rotated.get(0, 1));
        assertEquals('D', rotated.get(1, 0));
        assertEquals('B', rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals('D', rotated.get(0, 0));
        assertEquals('C', rotated.get(0, 1));
        assertEquals('B', rotated.get(1, 0));
        assertEquals('A', rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals('B', rotated.get(0, 0));
        assertEquals('D', rotated.get(0, 1));
        assertEquals('A', rotated.get(1, 0));
        assertEquals('C', rotated.get(1, 1));
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        CharMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals('A', transposed.get(0, 0));
        assertEquals('D', transposed.get(0, 1));
        assertEquals('B', transposed.get(1, 0));
        assertEquals('E', transposed.get(1, 1));
        assertEquals('C', transposed.get(2, 0));
        assertEquals('F', transposed.get(2, 1));
    }

    @Test
    public void testTranspose_square() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix transposed = m.transpose();
        assertEquals(2, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals('A', transposed.get(0, 0));
        assertEquals('C', transposed.get(0, 1));
        assertEquals('B', transposed.get(1, 0));
        assertEquals('D', transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rowCount());
        assertEquals(9, reshaped.columnCount());
        assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' }, reshaped.row(0));
    }

    @Test
    public void testReshape_back() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharMatrix reshaped = m.reshape(1, 9);
        CharMatrix reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        CharMatrix empty = CharMatrix.empty();
        CharMatrix reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        CharMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals('A', repeated.get(0, 0));
        assertEquals('A', repeated.get(0, 1));
        assertEquals('A', repeated.get(0, 2));
        assertEquals('B', repeated.get(0, 3));
        assertEquals('B', repeated.get(0, 4));
        assertEquals('B', repeated.get(0, 5));
        assertEquals('A', repeated.get(1, 0));   // second row same as first
    }

    @Test
    public void testRepelem_invalidArguments() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());

        // Check pattern
        assertEquals('A', repeated.get(0, 0));
        assertEquals('B', repeated.get(0, 1));
        assertEquals('A', repeated.get(0, 2));   // repeat starts
        assertEquals('B', repeated.get(0, 3));

        assertEquals('C', repeated.get(1, 0));
        assertEquals('D', repeated.get(1, 1));

        // Check vertical repeat
        assertEquals('A', repeated.get(2, 0));   // vertical repeat starts
        assertEquals('B', repeated.get(2, 1));
    }

    @Test
    public void testRepmat_invalidArguments() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        CharList flat = m.flatten();
        assertEquals(9, flat.size());
        char[] expected = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
        for (int i = 0; i < 9; i++) {
            assertEquals(expected[i], flat.get(i));
        }
    }

    @Test
    public void testFlatten_empty() {
        CharMatrix empty = CharMatrix.empty();
        CharList flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        List<Integer> sums = new ArrayList<>();
        m.flatOp(row -> {
            int sum = 0;
            for (char val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertTrue(sums.get(0) > 0);
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'G', 'H', 'I' }, { 'J', 'K', 'L' } });
        CharMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals('A', stacked.get(0, 0));
        assertEquals('G', stacked.get(2, 0));
        assertEquals('L', stacked.get(3, 2));
    }

    @Test
    public void testVstack_differentColumnCounts() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'A', 'B', 'C' } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'E', 'F' }, { 'G', 'H' } });
        CharMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals('A', stacked.get(0, 0));
        assertEquals('E', stacked.get(0, 2));
        assertEquals('H', stacked.get(1, 3));
    }

    @Test
    public void testHstack_differentRowCounts() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'E', 'F' } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 5, 6 }, { 7, 8 } });
        CharMatrix sum = m1.add(m2);

        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(10, sum.get(1, 0));
        assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void testAdd_differentDimensions() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'E', 'F' }, { 'G', 'H' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix diff = m1.subtract(m2);

        assertEquals('E' - 1, diff.get(0, 0));
        assertEquals('F' - 2, diff.get(0, 1));
        assertEquals('G' - 3, diff.get(1, 0));
        assertEquals('H' - 4, diff.get(1, 1));
    }

    @Test
    public void testSubtract_differentDimensions() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 5, 6 }, { 7, 8 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 5, 6 }, { 7, 8 } });
        CharMatrix product = m1.multiply(m2);

        assertEquals(19, product.get(0, 0));   // 1*5 + 2*7
        assertEquals(22, product.get(0, 1));   // 1*6 + 2*8
        assertEquals(43, product.get(1, 0));   // 3*5 + 4*7
        assertEquals(50, product.get(1, 1));   // 3*6 + 4*8
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2, 3 } });   // 1x3
        CharMatrix m2 = CharMatrix.of(new char[][] { { 4 }, { 5 }, { 6 } });   // 3x1
        CharMatrix product = m1.multiply(m2);

        assertEquals(1, product.rowCount());
        assertEquals(1, product.columnCount());
        assertEquals(32, product.get(0, 0));   // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        Matrix<Character> boxed = m.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(3, boxed.columnCount());
        assertEquals(Character.valueOf('A'), boxed.get(0, 0));
        assertEquals(Character.valueOf('F'), boxed.get(1, 2));
    }

    @Test
    public void testToIntMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        IntMatrix intMatrix = m.toIntMatrix();
        assertEquals(2, intMatrix.rowCount());
        assertEquals(2, intMatrix.columnCount());
        assertEquals(65, intMatrix.get(0, 0));   // 'A' = 65
        assertEquals(68, intMatrix.get(1, 1));   // 'D' = 68
    }

    @Test
    public void testToLongMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        LongMatrix longMatrix = m.toLongMatrix();
        assertEquals(2, longMatrix.rowCount());
        assertEquals(2, longMatrix.columnCount());
        assertEquals(65L, longMatrix.get(0, 0));
        assertEquals(68L, longMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(2, floatMatrix.rowCount());
        assertEquals(2, floatMatrix.columnCount());
        assertEquals(65.0f, floatMatrix.get(0, 0), 0.0001f);
        assertEquals(68.0f, floatMatrix.get(1, 1), 0.0001f);
    }

    @Test
    public void testToDoubleMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(2, doubleMatrix.rowCount());
        assertEquals(2, doubleMatrix.columnCount());
        assertEquals(65.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(68.0, doubleMatrix.get(1, 1), 0.0001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 5, 6 }, { 7, 8 } });
        CharMatrix result = m1.zipWith(m2, (a, b) -> (char) (a * b));

        assertEquals(5, result.get(0, 0));   // 1*5
        assertEquals(12, result.get(0, 1));   // 2*6
        assertEquals(21, result.get(1, 0));   // 3*7
        assertEquals(32, result.get(1, 1));   // 4*8
    }

    @Test
    public void testZipWith_differentShapes() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (char) (a + b)));
    }

    @Test
    public void testZipWith_threeMatrices() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 5, 6 }, { 7, 8 } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 9, 10 }, { 11, 12 } });
        CharMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (char) (a + b + c));

        assertEquals(15, result.get(0, 0));   // 1+5+9
        assertEquals(18, result.get(0, 1));   // 2+6+10
        assertEquals(21, result.get(1, 0));   // 3+7+11
        assertEquals(24, result.get(1, 1));   // 4+8+12
    }

    @Test
    public void testZipWith_threeMatrices_differentShapes() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 5, 6 }, { 7, 8 } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 9, 10, 11 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> (char) (a + b + c)));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        char[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new char[] { 'A', 'E', 'I' }, diagonal);
    }

    @Test
    public void testStreamLU2RD_empty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        char[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new char[] { 'C', 'E', 'G' }, antiDiagonal);
    }

    @Test
    public void testStreamRU2LD_empty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.streamRU2LD().toArray().length);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        char[] all = m.streamH().toArray();
        assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F' }, all);
    }

    @Test
    public void testStreamH_empty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.streamH().toArray().length);
    }

    @Test
    public void testStreamH_withRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        char[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new char[] { 'D', 'E', 'F' }, row1);
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        char[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new char[] { 'D', 'E', 'F', 'G', 'H', 'I' }, rows);
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        char[] all = m.streamV().toArray();
        assertArrayEquals(new char[] { 'A', 'D', 'B', 'E', 'C', 'F' }, all);
    }

    @Test
    public void testStreamV_empty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.streamV().toArray().length);
    }

    @Test
    public void testStreamV_withColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        char[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new char[] { 'B', 'E' }, col1);
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        char[] cols = m.streamV(1, 3).toArray();
        assertArrayEquals(new char[] { 'B', 'E', 'H', 'C', 'F', 'I' }, cols);
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        List<char[]> rows = m.streamR().map(CharStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, rows.get(0));
        assertArrayEquals(new char[] { 'D', 'E', 'F' }, rows.get(1));
    }

    @Test
    public void testStreamR_empty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        List<char[]> rows = m.streamR(1, 3).map(CharStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new char[] { 'D', 'E', 'F' }, rows.get(0));
        assertArrayEquals(new char[] { 'G', 'H', 'I' }, rows.get(1));
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
        List<char[]> cols = m.streamC().map(CharStream::toArray).toList();
        assertEquals(3, cols.size());
        assertArrayEquals(new char[] { 'A', 'D' }, cols.get(0));
        assertArrayEquals(new char[] { 'B', 'E' }, cols.get(1));
        assertArrayEquals(new char[] { 'C', 'F' }, cols.get(2));
    }

    @Test
    public void testStreamC_empty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
        List<char[]> cols = m.streamC(1, 3).map(CharStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new char[] { 'B', 'E', 'H' }, cols.get(0));
        assertArrayEquals(new char[] { 'C', 'F', 'I' }, cols.get(1));
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
    }

    // ============ Object Methods Tests ============

    @Test
    public void testHashCode() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'D', 'C' } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode());   // Usually different
    }

    @Test
    public void testEquals() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'D', 'C' } });
        CharMatrix m4 = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });

        assertTrue(m1.equals(m1));   // Same object
        assertTrue(m1.equals(m2));   // Same values
        assertFalse(m1.equals(m3));   // Different values
        assertFalse(m1.equals(m4));   // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("A"));
        assertTrue(str.contains("B"));
        assertTrue(str.contains("C"));
        assertTrue(str.contains("D"));
    }

    // ============ Edge Case Tests ============

    @Test
    public void testUnicodeCharacters() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\u0041', '\u0042' }, { '\u4E2D', '\u6587' } });
        assertEquals('A', m.get(0, 0));   // Unicode for 'A'
        assertEquals('B', m.get(0, 1));   // Unicode for 'B'
        assertEquals('\u4E2D', m.get(1, 0));   // Chinese character
        assertEquals('\u6587', m.get(1, 1));   // Chinese character
    }

    @Test
    public void testSpecialCharacters() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\n', '\t' }, { ' ', '!' } });
        assertEquals('\n', m.get(0, 0));
        assertEquals('\t', m.get(0, 1));
        assertEquals(' ', m.get(1, 0));
        assertEquals('!', m.get(1, 1));
    }

    @Test
    public void testCharMinMaxValues() {
        CharMatrix m = CharMatrix.of(new char[][] { { Character.MAX_VALUE, Character.MIN_VALUE } });
        assertEquals(Character.MAX_VALUE, m.get(0, 0));
        assertEquals(Character.MIN_VALUE, m.get(0, 1));
    }

    @Test
    public void testEmptyMatrixOperations() {
        CharMatrix empty = CharMatrix.empty();

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rowCount);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        CharMatrix extended = empty.extend(2, 2, 'X');
        assertEquals(2, extended.rowCount());
        assertEquals(2, extended.columnCount());
        assertEquals('X', extended.get(0, 0));
    }

    // ============ High-Impact Tests for 95% Coverage ============

    @Test
    public void testRotateTransposeAndConvertTallMatrix() {
        // Create a tall matrix (rows > cols) - 4 rows × 2 cols
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' }, { 'g', 'h' } });

        // Test rotate90() with tall matrix
        CharMatrix rotated90 = m.rotate90();
        assertEquals(2, rotated90.rowCount());
        assertEquals(4, rotated90.columnCount());
        assertEquals('g', rotated90.get(0, 0));

        // Test rotate270() with tall matrix
        CharMatrix rotated270 = m.rotate270();
        assertEquals(2, rotated270.rowCount());
        assertEquals(4, rotated270.columnCount());
        assertEquals('b', rotated270.get(0, 0));

        // Test transpose() with tall matrix
        CharMatrix transposed = m.transpose();
        assertEquals(2, transposed.rowCount());
        assertEquals(4, transposed.columnCount());
        assertEquals('a', transposed.get(0, 0));
        assertEquals('h', transposed.get(1, 3));

        // Test boxed() with tall matrix
        Matrix<Character> boxed = m.boxed();
        assertEquals(4, boxed.rowCount());
        assertEquals(2, boxed.columnCount());
        assertEquals(Character.valueOf('a'), boxed.get(0, 0));

        // Test toLongMatrix() with tall matrix
        LongMatrix longMat = m.toLongMatrix();
        assertEquals(4, longMat.rowCount());
        assertEquals(2, longMat.columnCount());
        assertEquals('a', longMat.get(0, 0));

        // Test toFloatMatrix() with tall matrix
        FloatMatrix floatMat = m.toFloatMatrix();
        assertEquals(4, floatMat.rowCount());
        assertEquals(2, floatMat.columnCount());

        // Test toDoubleMatrix() with tall matrix
        DoubleMatrix doubleMat = m.toDoubleMatrix();
        assertEquals(4, doubleMat.rowCount());
        assertEquals(2, doubleMat.columnCount());
    }

    @Test
    public void testRepelemOverflow() {
        int largeSize = 50000;
        CharMatrix m = CharMatrix.of(new char[largeSize][2]);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repelem(50000, 1));
        assertTrue(ex1.getMessage().contains("row count overflow"));

        CharMatrix m2 = CharMatrix.of(new char[2][largeSize]);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repelem(1, 50000));
        assertTrue(ex2.getMessage().contains("column count overflow"));
    }

    @Test
    public void testRepmatOverflow() {
        int largeSize = 50000;
        CharMatrix m = CharMatrix.of(new char[largeSize][2]);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repmat(50000, 1));
        assertTrue(ex1.getMessage().contains("row count overflow"));

        CharMatrix m2 = CharMatrix.of(new char[2][largeSize]);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repmat(1, 50000));
        assertTrue(ex2.getMessage().contains("column count overflow"));
    }
}
