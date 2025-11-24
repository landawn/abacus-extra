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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalChar;
import com.landawn.abacus.util.stream.CharStream;

@Tag("2511")
public class CharMatrix2511Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix m = new CharMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        CharMatrix m = new CharMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        CharMatrix m = new CharMatrix(new char[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        CharMatrix m = new CharMatrix(new char[][] { { 'x' } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals('x', m.get(0, 0));
    }

    @Test
    public void testConstructor_withNonSquareMatrix() {
        char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix m = new CharMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(3, m.cols);
    }

    @Test
    public void testConstructor_withDigits() {
        char[][] arr = { { '1', '2' }, { '3', '4' } };
        CharMatrix m = new CharMatrix(arr);
        assertEquals('1', m.get(0, 0));
        assertEquals('4', m.get(1, 1));
    }

    @Test
    public void testConstructor_largeMatrix() {
        char[][] arr = new char[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                arr[i][j] = (char) ('a' + ((i + j) % 26));
            }
        }
        CharMatrix m = new CharMatrix(arr);
        assertEquals(100, m.rows);
        assertEquals(100, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        CharMatrix empty = CharMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());
        assertSame(CharMatrix.empty(), CharMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix m = CharMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals('a', m.get(0, 0));
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
    public void testOf_withSingleRow() {
        CharMatrix m = CharMatrix.of(new char[] { 'a', 'b', 'c' });
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(0, 2));
    }

    @Test
    public void testRandom() {
        CharMatrix m = CharMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withZeroLength() {
        CharMatrix m = CharMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRandom_withLargeLength() {
        CharMatrix m = CharMatrix.random(1000);
        assertEquals(1, m.rows);
        assertEquals(1000, m.cols);
    }

    @Test
    public void testRepeat() {
        CharMatrix m = CharMatrix.repeat('x', 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals('x', m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withZero() {
        CharMatrix m = CharMatrix.repeat('\u0000', 3);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertEquals('\u0000', m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withZeroLength() {
        CharMatrix m = CharMatrix.repeat('a', 0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRange() {
        CharMatrix m = CharMatrix.range('a', 'e');
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(0, 2));
        assertEquals('d', m.get(0, 3));
    }

    @Test
    public void testRange_withStep() {
        CharMatrix m = CharMatrix.range('a', 'k', 2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('c', m.get(0, 1));
        assertEquals('e', m.get(0, 2));
        assertEquals('g', m.get(0, 3));
        assertEquals('i', m.get(0, 4));
    }

    @Test
    public void testRange_emptyRange() {
        CharMatrix m = CharMatrix.range('e', 'e');
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRange_digits() {
        CharMatrix m = CharMatrix.range('0', '5');
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertEquals('0', m.get(0, 0));
        assertEquals('4', m.get(0, 4));
    }

    @Test
    public void testRangeClosed() {
        CharMatrix m = CharMatrix.rangeClosed('a', 'd');
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(0, 3));
    }

    @Test
    public void testRangeClosed_withStep() {
        CharMatrix m = CharMatrix.rangeClosed('a', 'i', 3);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(0, 1));
        assertEquals('g', m.get(0, 2));
    }

    @Test
    public void testRangeClosed_singleElement() {
        CharMatrix m = CharMatrix.rangeClosed('z', 'z');
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals('z', m.get(0, 0));
    }

    @Test
    public void testDiagonalLU2RD() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[] { 'a', 'b', 'c' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
        assertEquals('\u0000', m.get(0, 1));
        assertEquals('\u0000', m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_empty() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalLU2RD_singleElement() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[] { 'x' });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals('x', m.get(0, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        CharMatrix m = CharMatrix.diagonalRU2LD(new char[] { 'a', 'b', 'c' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 2));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 0));
        assertEquals('\u0000', m.get(0, 0));
        assertEquals('\u0000', m.get(2, 2));
    }

    @Test
    public void testDiagonalRU2LD_empty() {
        CharMatrix m = CharMatrix.diagonalRU2LD(new char[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
        assertEquals('x', m.get(0, 2));
        assertEquals('z', m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'a', 'b', 'c' }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        CharMatrix m = CharMatrix.diagonal(null, new char[] { 'x', 'y', 'z' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('x', m.get(0, 2));
        assertEquals('y', m.get(1, 1));
        assertEquals('z', m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        CharMatrix m = CharMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonal(new char[] { 'a', 'b' }, new char[] { 'x', 'y', 'z' }));
    }

    @Test
    public void testDiagonal_withBothDiagonalsOverlapping() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        // Center element should be from main diagonal (set second)
        assertEquals('b', m.get(1, 1));
    }

    @Test
    public void testUnbox() {
        Character[][] boxed = { { 'a', 'b' }, { 'c', 'd' } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals('a', unboxed.get(0, 0));
        assertEquals('b', unboxed.get(0, 1));
    }

    @Test
    public void testUnbox_withNullElements() {
        Character[][] boxed = { { 'a', null }, { null, 'd' } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals('a', unboxed.get(0, 0));
        assertEquals('\u0000', unboxed.get(0, 1));
        assertEquals('\u0000', unboxed.get(1, 0));
    }

    @Test
    public void testUnbox_allNulls() {
        Character[][] boxed = { { null, null }, { null, null } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals('\u0000', unboxed.get(0, 0));
        assertEquals('\u0000', unboxed.get(1, 1));
    }

    // ============ Component Type Test ============

    @Test
    public void testComponentType() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertEquals(char.class, m.componentType());
    }

    @Test
    public void testComponentType_emptyMatrix() {
        CharMatrix m = CharMatrix.empty();
        assertEquals(char.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet_byIndices() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix m = CharMatrix.of(arr);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
    }

    @Test
    public void testGet_byPoint() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix m = CharMatrix.of(arr);
        assertEquals('a', m.get(Point.of(0, 0)));
        assertEquals('b', m.get(Point.of(0, 1)));
        assertEquals('c', m.get(Point.of(1, 0)));
        assertEquals('d', m.get(Point.of(1, 1)));
    }

    @Test
    public void testGet_allPositions() {
        char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix m = CharMatrix.of(arr);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(arr[i][j], m.get(i, j));
            }
        }
    }

    @Test
    public void testSet_byIndices() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' } });
        m.set(0, 0, 'a');
        m.set(1, 1, 'd');
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(1, 1));
        assertEquals(' ', m.get(0, 1));
        assertEquals(' ', m.get(1, 0));
    }

    @Test
    public void testSet_byPoint() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' } });
        m.set(Point.of(0, 0), 'a');
        m.set(Point.of(1, 1), 'd');
        assertEquals('a', m.get(Point.of(0, 0)));
        assertEquals('d', m.get(Point.of(1, 1)));
        assertEquals(' ', m.get(Point.of(0, 1)));
    }

    @Test
    public void testSet_overwriteValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        m.set(0, 0, 'x');
        assertEquals('x', m.get(0, 0));
        m.set(0, 0, 'y');
        assertEquals('y', m.get(0, 0));
    }

    @Test
    public void testSet_specialCharacters() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' } });
        m.set(0, 0, '\n');
        assertEquals('\n', m.get(0, 0));
    }

    // ============ Directional Access Tests ============

    @Test
    public void testUpOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals('a', up.get());
    }

    @Test
    public void testUpOf_atTopEdge() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void testUpOf_multipleRows() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        OptionalChar up = m.upOf(2, 1);
        assertTrue(up.isPresent());
        assertEquals('d', up.get());
    }

    @Test
    public void testDownOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals('c', down.get());
    }

    @Test
    public void testDownOf_atBottomEdge() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void testDownOf_multipleRows() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        OptionalChar down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals('c', down.get());
    }

    @Test
    public void testLeftOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals('a', left.get());
    }

    @Test
    public void testLeftOf_atLeftEdge() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testLeftOf_multipleColumns() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        OptionalChar left = m.leftOf(0, 2);
        assertTrue(left.isPresent());
        assertEquals('b', left.get());
    }

    @Test
    public void testRightOf() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals('b', right.get());
    }

    @Test
    public void testRightOf_atRightEdge() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    @Test
    public void testRightOf_multipleColumns() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        OptionalChar right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals('b', right.get());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void testRow() {
        char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix m = CharMatrix.of(arr);
        char[] row0 = m.row(0);
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, row0);
        char[] row1 = m.row(1);
        assertArrayEquals(new char[] { 'd', 'e', 'f' }, row1);
    }

    @Test
    public void testRow_invalidIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testRow_singleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.row(0));
    }

    @Test
    public void testColumn() {
        char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix m = CharMatrix.of(arr);
        char[] col0 = m.column(0);
        assertArrayEquals(new char[] { 'a', 'd' }, col0);
        char[] col1 = m.column(1);
        assertArrayEquals(new char[] { 'b', 'e' }, col1);
    }

    @Test
    public void testColumn_invalidIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testColumn_singleColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' }, { 'b' }, { 'c' } });
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.column(0));
    }

    @Test
    public void testSetRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
        m.setRow(0, new char[] { 'a', 'b', 'c' });
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.row(0));
        assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.row(1));
    }

    @Test
    public void testSetRow_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new char[] { 'a', 'b' }));
    }

    @Test
    public void testSetRow_invalidIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(-1, new char[] { 'a', 'b' }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(1, new char[] { 'a', 'b' }));
    }

    @Test
    public void testSetColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' }, { ' ', ' ' } });
        m.setColumn(0, new char[] { 'a', 'b', 'c' });
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.column(0));
        assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.column(1));
    }

    @Test
    public void testSetColumn_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new char[] { 'a' }));
    }

    @Test
    public void testSetColumn_invalidIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(-1, new char[] { 'a', 'b' }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(2, new char[] { 'a', 'b' }));
    }

    @Test
    public void testUpdateRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { ' ', ' ', ' ' } });
        m.updateRow(0, val -> Character.toUpperCase(val));
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, m.row(0));
        assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.row(1));
    }

    @Test
    public void testUpdateRow_multipleRows() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        m.updateRow(1, val -> Character.toUpperCase(val));
        assertArrayEquals(new char[] { 'a', 'b' }, m.row(0));
        assertArrayEquals(new char[] { 'C', 'D' }, m.row(1));
        assertArrayEquals(new char[] { 'e', 'f' }, m.row(2));
    }

    @Test
    public void testUpdateColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', ' ' }, { 'b', ' ' }, { 'c', ' ' } });
        m.updateColumn(0, val -> Character.toUpperCase(val));
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, m.column(0));
        assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.column(1));
    }

    @Test
    public void testUpdateColumn_multipleColumns() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        m.updateColumn(2, val -> Character.toUpperCase(val));
        assertArrayEquals(new char[] { 'a', 'b', 'C' }, m.row(0));
        assertArrayEquals(new char[] { 'd', 'e', 'F' }, m.row(1));
    }

    // ============ Diagonal Access Tests ============

    @Test
    public void testGetLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diag = m.getLU2RD();
        assertArrayEquals(new char[] { 'a', 'e', 'i' }, diag);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_singleElement() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
        assertArrayEquals(new char[] { 'x' }, m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
        m.setLU2RD(new char[] { 'a', 'b', 'c' });
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
        assertEquals(' ', m.get(0, 1));
    }

    @Test
    public void testSetLU2RD_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new char[] { 'a', 'b' }));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[2][3]);
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new char[] { 'a', 'b' }));
    }

    @Test
    public void testUpdateLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', ' ', ' ' }, { ' ', 'b', ' ' }, { ' ', ' ', 'c' } });
        m.updateLU2RD(val -> Character.toUpperCase(val));
        assertEquals('A', m.get(0, 0));
        assertEquals('B', m.get(1, 1));
        assertEquals('C', m.get(2, 2));
    }

    @Test
    public void testUpdateLU2RD_allValues() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', 'x', ' ' }, { 'x', ' ', 'x' }, { ' ', 'x', ' ' } });
        m.updateLU2RD(val -> 'z');
        assertEquals('z', m.get(0, 0));
        assertEquals('z', m.get(1, 1));
        assertEquals('z', m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', 'a' }, { ' ', 'b', ' ' }, { 'c', ' ', ' ' } });
        char[] diag = m.getRU2LD();
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, diag);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
        m.setRU2LD(new char[] { 'a', 'b', 'c' });
        assertEquals('a', m.get(0, 2));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 0));
        assertEquals(' ', m.get(0, 0));
    }

    @Test
    public void testSetRU2LD_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new char[] { 'a', 'b' }));
    }

    @Test
    public void testUpdateRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', 'a' }, { ' ', 'b', ' ' }, { 'c', ' ', ' ' } });
        m.updateRU2LD(val -> Character.toUpperCase(val));
        assertEquals('A', m.get(0, 2));
        assertEquals('B', m.get(1, 1));
        assertEquals('C', m.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD_rectangular() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', 'a' }, { 'b', ' ', ' ' } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(val -> Character.toUpperCase(val)));
    }

    // ============ Update/Replace Tests ============

    @Test
    public void testUpdateAll_unary() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.updateAll(val -> Character.toUpperCase(val));
        assertEquals('A', m.get(0, 0));
        assertEquals('B', m.get(0, 1));
        assertEquals('C', m.get(1, 0));
        assertEquals('D', m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.updateAll((i, j) -> i == j ? 'x' : 'o');
        assertEquals('x', m.get(0, 0));
        assertEquals('o', m.get(0, 1));
        assertEquals('o', m.get(1, 0));
        assertEquals('x', m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withConstant() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.updateAll(val -> '*');
        assertEquals('*', m.get(0, 0));
        assertEquals('*', m.get(0, 1));
        assertEquals('*', m.get(1, 0));
        assertEquals('*', m.get(1, 1));
    }

    @Test
    public void testReplaceIf_predicate() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'B' }, { 'c', 'D' } });
        m.replaceIf(val -> Character.isUpperCase(val), '*');
        assertEquals('a', m.get(0, 0));
        assertEquals('*', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('*', m.get(1, 1));
    }

    @Test
    public void testReplaceIf_biPredicate() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.replaceIf((i, j) -> i == j, 'X');
        assertEquals('X', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('X', m.get(1, 1));
    }

    @Test
    public void testReplaceIf_noMatches() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.replaceIf(val -> Character.isDigit(val), 'X');
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void testMap() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix mapped = m.map(val -> Character.toUpperCase(val));
        assertEquals('A', mapped.get(0, 0));
        assertEquals('B', mapped.get(0, 1));
        assertEquals('C', mapped.get(1, 0));
        assertEquals('D', mapped.get(1, 1));
        assertEquals('a', m.get(0, 0));
    }

    @Test
    public void testMap_identity() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix mapped = m.map(val -> val);
        assertEquals('a', mapped.get(0, 0));
        assertEquals('b', mapped.get(0, 1));
        assertNotSame(m, mapped);
    }

    @Test
    public void testMapToObj() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        Matrix<String> mapped = m.mapToObj(val -> String.valueOf(val), String.class);
        assertEquals("a", mapped.get(0, 0));
        assertEquals("b", mapped.get(0, 1));
        assertEquals("c", mapped.get(1, 0));
        assertEquals("d", mapped.get(1, 1));
    }

    @Test
    public void testMapToObj_withComplexType() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        Matrix<Integer> mapped = m.mapToObj(val -> (int) val, Integer.class);
        assertEquals(97, mapped.get(0, 0));
        assertEquals(98, mapped.get(0, 1));
        assertEquals(99, mapped.get(1, 0));
        assertEquals(100, mapped.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_singleValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' } });
        m.fill('x');
        assertEquals('x', m.get(0, 0));
        assertEquals('x', m.get(0, 1));
        assertEquals('x', m.get(1, 0));
        assertEquals('x', m.get(1, 1));
    }

    @Test
    public void testFill_withArray() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ' }, { ' ', ' ' } });
        m.fill(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(1, 1));
    }

    @Test
    public void testFill_withOffset() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
        m.fill(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals(' ', m.get(0, 0));
        assertEquals('a', m.get(1, 1));
        assertEquals('d', m.get(2, 2));
    }

    @Test
    public void testFill_withOffset_invalidPosition() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        assertThrows(IllegalArgumentException.class, () -> m.fill(3, 3, new char[][] { { 'a', 'b' }, { 'c', 'd' } }));
    }

    @Test
    public void testFill_withPartialArray() {
        CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
        m.fill(0, 0, new char[][] { { 'a', 'b' } });
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals(' ', m.get(0, 2));
        assertEquals(' ', m.get(1, 0));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix copy = m.copy();
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals('a', copy.get(0, 0));
        assertEquals('b', copy.get(0, 1));
        copy.set(0, 0, 'x');
        assertEquals('a', m.get(0, 0));
    }

    @Test
    public void testCopy_emptyMatrix() {
        CharMatrix m = CharMatrix.empty();
        CharMatrix copy = m.copy();
        assertTrue(copy.isEmpty());
    }

    @Test
    public void testCopy_withRowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals('c', copy.get(0, 0));
        assertEquals('d', copy.get(0, 1));
    }

    @Test
    public void testCopy_withRowRange_invalidRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2));
    }

    @Test
    public void testCopy_withRowRange_singleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharMatrix copy = m.copy(1, 2);
        assertEquals(1, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals('c', copy.get(0, 0));
        assertEquals('d', copy.get(0, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        CharMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals('e', copy.get(0, 0));
        assertEquals('f', copy.get(0, 1));
        assertEquals('h', copy.get(1, 0));
        assertEquals('i', copy.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_invalidRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, -1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, 0, 3));
    }

    @Test
    public void testCopy_withFullRange_singleCell() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        CharMatrix copy = m.copy(0, 1, 1, 2);
        assertEquals(1, copy.rows);
        assertEquals(1, copy.cols);
        assertEquals('b', copy.get(0, 0));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_withDefaultValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix extended = m.extend(2, 3);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('a', extended.get(0, 0));
        assertEquals('b', extended.get(0, 1));
        assertEquals('\u0000', extended.get(0, 2));
        assertEquals('\u0000', extended.get(1, 0));
    }

    @Test
    public void testExtend_withCustomDefaultValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix extended = m.extend(2, 3, '*');
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('a', extended.get(0, 0));
        assertEquals('b', extended.get(0, 1));
        assertEquals('*', extended.get(0, 2));
        assertEquals('*', extended.get(1, 0));
    }

    @Test
    public void testExtend_withDirections() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
        CharMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('x', extended.get(1, 1));
        assertEquals('\u0000', extended.get(0, 0));
    }

    @Test
    public void testExtend_withDirectionsAndValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
        CharMatrix extended = m.extend(1, 1, 1, 1, '*');
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('x', extended.get(1, 1));
        assertEquals('*', extended.get(0, 0));
    }

    @Test
    public void testExtend_invalidSize() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });

        CharMatrix extended = m.extend(1, 1);
        assertEquals(1, extended.rows);
        assertEquals(1, extended.cols);
    }

    @Test
    public void testExtend_noExtension() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix extended = m.extend(1, 2);
        assertEquals(1, extended.rows);
        assertEquals(2, extended.cols);
        assertEquals('a', extended.get(0, 0));
        assertEquals('b', extended.get(0, 1));
    }

    // ============ Transformation Tests ============

    @Test
    public void testReverseH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        m.reverseH();
        assertArrayEquals(new char[] { 'c', 'b', 'a' }, m.row(0));
        assertArrayEquals(new char[] { 'f', 'e', 'd' }, m.row(1));
    }

    @Test
    public void testReverseH_singleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        m.reverseH();
        assertArrayEquals(new char[] { 'c', 'b', 'a' }, m.row(0));
    }

    @Test
    public void testReverseV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        m.reverseV();
        assertArrayEquals(new char[] { 'e', 'c', 'a' }, m.column(0));
        assertArrayEquals(new char[] { 'f', 'd', 'b' }, m.column(1));
    }

    @Test
    public void testReverseV_singleColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' }, { 'b' }, { 'c' } });
        m.reverseV();
        assertArrayEquals(new char[] { 'c', 'b', 'a' }, m.column(0));
    }

    @Test
    public void testFlipH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        CharMatrix flipped = m.flipH();
        assertEquals(2, flipped.rows);
        assertEquals(3, flipped.cols);
        assertArrayEquals(new char[] { 'c', 'b', 'a' }, flipped.row(0));
        assertArrayEquals(new char[] { 'f', 'e', 'd' }, flipped.row(1));
        assertNotSame(m, flipped);
    }

    @Test
    public void testFlipV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharMatrix flipped = m.flipV();
        assertEquals(3, flipped.rows);
        assertEquals(2, flipped.cols);
        assertArrayEquals(new char[] { 'e', 'c', 'a' }, flipped.column(0));
        assertArrayEquals(new char[] { 'f', 'd', 'b' }, flipped.column(1));
        assertNotSame(m, flipped);
    }

    @Test
    public void testRotate90() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals('c', rotated.get(0, 0));
        assertEquals('a', rotated.get(0, 1));
        assertEquals('d', rotated.get(1, 0));
        assertEquals('b', rotated.get(1, 1));
    }

    @Test
    public void testRotate90_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        CharMatrix rotated = m.rotate90();
        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
    }

    @Test
    public void testRotate180() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals('d', rotated.get(0, 0));
        assertEquals('c', rotated.get(0, 1));
        assertEquals('b', rotated.get(1, 0));
        assertEquals('a', rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals('b', rotated.get(0, 0));
        assertEquals('d', rotated.get(0, 1));
        assertEquals('a', rotated.get(1, 0));
        assertEquals('c', rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        CharMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals('a', transposed.get(0, 0));
        assertEquals('d', transposed.get(0, 1));
        assertEquals('b', transposed.get(1, 0));
        assertEquals('e', transposed.get(1, 1));
    }

    @Test
    public void testTranspose_squareMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals('a', transposed.get(0, 0));
        assertEquals('c', transposed.get(0, 1));
    }

    @Test
    public void testReshape() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c', 'd' } });
        CharMatrix reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals('a', reshaped.get(0, 0));
        assertEquals('b', reshaped.get(0, 1));
        assertEquals('c', reshaped.get(1, 0));
        assertEquals('d', reshaped.get(1, 1));
    }

    @Test
    public void testReshape_toSingleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix reshaped = m.reshape(1, 4);
        assertEquals(1, reshaped.rows);
        assertEquals(4, reshaped.cols);
    }

    @Test
    public void testRepelem() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals('a', repeated.get(0, 0));
        assertEquals('a', repeated.get(0, 1));
        assertEquals('a', repeated.get(1, 0));
        assertEquals('a', repeated.get(1, 1));
        assertEquals('b', repeated.get(0, 2));
    }

    @Test
    public void testRepelem_singleRepeat() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix repeated = m.repelem(1, 1);
        assertEquals(1, repeated.rows);
        assertEquals(2, repeated.cols);
        assertEquals('a', repeated.get(0, 0));
        assertEquals('b', repeated.get(0, 1));
    }

    @Test
    public void testRepmat() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix repeated = m.repmat(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals('a', repeated.get(0, 0));
        assertEquals('b', repeated.get(0, 1));
        assertEquals('d', repeated.get(3, 3));
    }

    @Test
    public void testRepmat_singleRepeat() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix repeated = m.repmat(1, 1);
        assertEquals(1, repeated.rows);
        assertEquals(2, repeated.cols);
        assertEquals('a', repeated.get(0, 0));
    }

    @Test
    public void testFlatten() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharList flattened = m.flatten();
        assertEquals(4, flattened.size());
        assertEquals('a', flattened.get(0));
        assertEquals('b', flattened.get(1));
        assertEquals('c', flattened.get(2));
        assertEquals('d', flattened.get(3));
    }

    @Test
    public void testFlatten_emptyMatrix() {
        CharMatrix m = CharMatrix.empty();
        CharList flattened = m.flatten();
        assertEquals(0, flattened.size());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' } });
        CharMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals('a', stacked.get(0, 0));
        assertEquals('b', stacked.get(0, 1));
        assertEquals('c', stacked.get(1, 0));
        assertEquals('d', stacked.get(1, 1));
    }

    @Test
    public void testVstack_incompatibleColumns() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c' } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' }, { 'c' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' }, { 'd' } });
        CharMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals('a', stacked.get(0, 0));
        assertEquals('b', stacked.get(0, 1));
        assertEquals('c', stacked.get(1, 0));
        assertEquals('d', stacked.get(1, 1));
    }

    @Test
    public void testHstack_incompatibleRows() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' }, { 'c' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Boxed Tests ============

    @Test
    public void testBoxed() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        Matrix<Character> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(Character.valueOf('a'), boxed.get(0, 0));
        assertEquals(Character.valueOf('b'), boxed.get(0, 1));
        assertEquals(Character.valueOf('c'), boxed.get(1, 0));
        assertEquals(Character.valueOf('d'), boxed.get(1, 1));
    }

    @Test
    public void testBoxed_emptyMatrix() {
        CharMatrix m = CharMatrix.empty();
        Matrix<Character> boxed = m.boxed();
        assertTrue(boxed.isEmpty());
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Character> diagonal = m.streamLU2RD().boxed().toList();
        assertEquals(3, diagonal.size());
        assertEquals('a', diagonal.get(0).charValue());
        assertEquals('e', diagonal.get(1).charValue());
        assertEquals('i', diagonal.get(2).charValue());
    }

    @Test
    public void testStreamRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Character> diagonal = m.streamRU2LD().boxed().toList();
        assertEquals(3, diagonal.size());
        assertEquals('c', diagonal.get(0).charValue());
        assertEquals('e', diagonal.get(1).charValue());
        assertEquals('g', diagonal.get(2).charValue());
    }

    @Test
    public void testStreamH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> elements = m.streamH().boxed().toList();
        assertEquals(4, elements.size());
        assertEquals('a', elements.get(0).charValue());
        assertEquals('b', elements.get(1).charValue());
    }

    @Test
    public void testStreamH_withRowIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> row = m.streamH(1).boxed().toList();
        assertEquals(2, row.size());
        assertEquals('c', row.get(0).charValue());
        assertEquals('d', row.get(1).charValue());
    }

    @Test
    public void testStreamH_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        List<Character> elements = m.streamH(1, 3).boxed().toList();
        assertEquals(4, elements.size());
        assertEquals('c', elements.get(0).charValue());
        assertEquals('d', elements.get(1).charValue());
    }

    @Test
    public void testStreamV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> elements = m.streamV().boxed().toList();
        assertEquals(4, elements.size());
        assertEquals('a', elements.get(0).charValue());
        assertEquals('c', elements.get(1).charValue());
    }

    @Test
    public void testStreamV_withColumnIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> col = m.streamV(1).boxed().toList();
        assertEquals(2, col.size());
        assertEquals('b', col.get(0).charValue());
        assertEquals('d', col.get(1).charValue());
    }

    @Test
    public void testStreamV_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        List<Character> elements = m.streamV(1, 3).boxed().toList();
        assertEquals(4, elements.size());
        assertEquals('b', elements.get(0).charValue());
        assertEquals('e', elements.get(1).charValue());
    }

    @Test
    public void testStreamR() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<CharStream> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).toArray().length);
    }

    @Test
    public void testStreamR_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        List<CharStream> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamC() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<CharStream> cols = m.streamC().toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).toArray().length);
    }

    @Test
    public void testStreamC_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        List<CharStream> cols = m.streamC(1, 3).toList();
        assertEquals(2, cols.size());
    }

    // ============ Point Stream Tests ============

    @Test
    public void testPointsH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(0, points.get(0).columnIndex());
    }

    @Test
    public void testPointsV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(0, points.get(0).columnIndex());
    }

    @Test
    public void testPointsLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Point> points = m.pointsLU2RD().toList();
        assertEquals(3, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(0, points.get(0).columnIndex());
    }

    @Test
    public void testPointsRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Point> points = m.pointsRU2LD().toList();
        assertEquals(3, points.size());
        assertEquals(0, points.get(0).rowIndex());
        assertEquals(2, points.get(0).columnIndex());
    }

    // ============ Adjacent Points Tests ============

    @Test
    public void testAdjacent4Points() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
    }

    @Test
    public void testAdjacent4Points_corner() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(2, points.size());
    }

    @Test
    public void testAdjacent8Points() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Point> points = m.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
    }

    @Test
    public void testAdjacent8Points_corner() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(3, points.size());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        StringBuilder sb = new StringBuilder();
        m.forEach(val -> sb.append(val));
        assertEquals("abcd", sb.toString());
    }

    @Test
    public void testForEach_biConsumer() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        StringBuilder diagonalStr = new StringBuilder();
        m.forEach((i, j, val) -> {
            if (i == j) {
                diagonalStr.append(val.get(i, j));
            }
        });
        assertEquals("ad", diagonalStr.toString());
    }

    // ============ Utility Tests ============

    @Test
    public void testIsEmpty() {
        assertTrue(CharMatrix.empty().isEmpty());
        assertFalse(CharMatrix.of(new char[][] { { 'a' } }).isEmpty());
    }

    @Test
    public void testIsSameShape() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'w', 'x' }, { 'y', 'z' } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        assertTrue(m1.isSameShape(m2));
        assertFalse(m1.isSameShape(m3));
    }

    @Test
    public void testArray() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix m = CharMatrix.of(arr);
        char[][] result = m.array();
        assertArrayEquals(arr, result);
    }

    @Test
    public void testHashCode_equalMatrices() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 'w', 'x' }, { 'y', 'z' } });
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "not a matrix");
    }

    @Test
    public void testEquals_emptyMatrices() {
        CharMatrix m1 = CharMatrix.empty();
        CharMatrix m2 = CharMatrix.empty();
        assertEquals(m1, m2);
    }

    @Test
    public void testToString() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("a"));
        assertTrue(str.contains("d"));
    }

    @Test
    public void testPrintln() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.contains("a"));
    }
}
