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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalChar;
import com.landawn.abacus.util.stream.CharStream;
import com.landawn.abacus.util.stream.Stream;

@Tag("2510")
public class CharMatrix2510Test extends TestBase {

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
    public void testConstructor_withNumbers() {
        char[][] arr = { { '1', '2' }, { '3', '4' } };
        CharMatrix m = new CharMatrix(arr);
        assertEquals('1', m.get(0, 0));
        assertEquals('4', m.get(1, 1));
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
        CharMatrix m = CharMatrix.repeat('\0', 3);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertEquals('\0', m.get(0, i));
        }
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
        CharMatrix m = CharMatrix.range('a', 'j', 2);
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
        CharMatrix m = CharMatrix.range('a', 'a');
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
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
        CharMatrix m = CharMatrix.rangeClosed('a', 'j', 3);
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(0, 1));
        assertEquals('g', m.get(0, 2));
        assertEquals('j', m.get(0, 3));
    }

    @Test
    public void testRangeClosed_singleElement() {
        CharMatrix m = CharMatrix.rangeClosed('x', 'x');
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals('x', m.get(0, 0));
    }

    @Test
    public void testDiagonalLU2RD() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[] { 'a', 'b', 'c' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
        assertEquals('\0', m.get(0, 1));
        assertEquals('\0', m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_empty() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalRU2LD() {
        CharMatrix m = CharMatrix.diagonalRU2LD(new char[] { 'a', 'b', 'c' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 2));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 0));
        assertEquals('\0', m.get(0, 0));
        assertEquals('\0', m.get(2, 2));
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals('a', m.get(0, 0));
        assertEquals('y', m.get(1, 1));
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
        assertEquals('\0', unboxed.get(0, 1));
        assertEquals('\0', unboxed.get(1, 0));
    }

    // ============ Component Type Test ============

    @Test
    public void testComponentType() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
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
    public void testSet_byIndices() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
        m.set(0, 0, 'a');
        m.set(1, 1, 'd');
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(1, 1));
        assertEquals('\0', m.get(0, 1));
        assertEquals('\0', m.get(1, 0));
    }

    @Test
    public void testSet_byPoint() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
        m.set(Point.of(0, 0), 'a');
        m.set(Point.of(1, 1), 'd');
        assertEquals('a', m.get(Point.of(0, 0)));
        assertEquals('d', m.get(Point.of(1, 1)));
        assertEquals('\0', m.get(Point.of(0, 1)));
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

    // ============ Adjacent Points Tests ============

    @Test
    public void testAdjacent4Points_center() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 2)));
        assertTrue(points.contains(Point.of(2, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAdjacent4Points_corner() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(2, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAdjacent8Points_center() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        List<Point> points = m.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
        assertTrue(points.contains(Point.of(0, 0)));
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(0, 2)));
        assertTrue(points.contains(Point.of(1, 0)));
        assertTrue(points.contains(Point.of(1, 2)));
        assertTrue(points.contains(Point.of(2, 0)));
        assertTrue(points.contains(Point.of(2, 1)));
        assertTrue(points.contains(Point.of(2, 2)));
    }

    @Test
    public void testAdjacent8Points_corner() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(3, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
        assertTrue(points.contains(Point.of(1, 1)));
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
    public void testSetRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
        m.setRow(0, new char[] { 'a', 'b', 'c' });
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.row(0));
        assertArrayEquals(new char[] { '\0', '\0', '\0' }, m.row(1));
    }

    @Test
    public void testSetRow_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new char[] { 'a', 'b' }));
    }

    @Test
    public void testSetColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' }, { '\0', '\0' } });
        m.setColumn(0, new char[] { 'a', 'b', 'c' });
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.column(0));
        assertArrayEquals(new char[] { '\0', '\0', '\0' }, m.column(1));
    }

    @Test
    public void testSetColumn_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new char[] { 'a' }));
    }

    @Test
    public void testUpdateRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        m.updateRow(0, val -> (char) (val + 1));
        assertArrayEquals(new char[] { 'b', 'c', 'd' }, m.row(0));
        assertArrayEquals(new char[] { 'd', 'e', 'f' }, m.row(1));
    }

    @Test
    public void testUpdateColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        m.updateColumn(0, val -> (char) (val + 2));
        assertArrayEquals(new char[] { 'c', 'e', 'g' }, m.column(0));
        assertArrayEquals(new char[] { 'b', 'd', 'f' }, m.column(1));
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
        char[] diag = m.getLU2RD();
        assertArrayEquals(new char[] { 'a', 'e' }, diag);
    }

    @Test
    public void testSetLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
        m.setLU2RD(new char[] { 'a', 'b', 'c' });
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
        assertEquals('\0', m.get(0, 1));
    }

    @Test
    public void testSetLU2RD_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new char[] { 'a', 'b' }));
    }

    @Test
    public void testUpdateLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', '\0', '\0' }, { '\0', 'b', '\0' }, { '\0', '\0', 'c' } });
        m.updateLU2RD(val -> (char) (val + 1));
        assertEquals('b', m.get(0, 0));
        assertEquals('c', m.get(1, 1));
        assertEquals('d', m.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diag = m.getRU2LD();
        assertArrayEquals(new char[] { 'c', 'e', 'g' }, diag);
    }

    @Test
    public void testSetRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
        m.setRU2LD(new char[] { 'a', 'b', 'c' });
        assertEquals('a', m.get(0, 2));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 0));
        assertEquals('\0', m.get(0, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', 'a' }, { '\0', 'b', '\0' }, { 'c', '\0', '\0' } });
        m.updateRU2LD(val -> (char) (val + 1));
        assertEquals('b', m.get(0, 2));
        assertEquals('c', m.get(1, 1));
        assertEquals('d', m.get(2, 0));
    }

    // ============ Update/Replace Tests ============

    @Test
    public void testUpdateAll_unary() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.updateAll(val -> (char) (val + 1));
        assertEquals('b', m.get(0, 0));
        assertEquals('c', m.get(0, 1));
        assertEquals('d', m.get(1, 0));
        assertEquals('e', m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.updateAll((i, j) -> (char) ('0' + i + j));
        assertEquals('0', m.get(0, 0));
        assertEquals('1', m.get(0, 1));
        assertEquals('1', m.get(1, 0));
        assertEquals('2', m.get(1, 1));
    }

    @Test
    public void testReplaceIf_predicate() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.replaceIf(val -> val > 'b', 'x');
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('x', m.get(1, 0));
        assertEquals('x', m.get(1, 1));
    }

    @Test
    public void testReplaceIf_biPredicate() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.replaceIf((i, j) -> i == j, 'x');
        assertEquals('x', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('x', m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void testMap() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix mapped = m.map(val -> (char) (val + 1));
        assertEquals('b', mapped.get(0, 0));
        assertEquals('c', mapped.get(0, 1));
        assertEquals('d', mapped.get(1, 0));
        assertEquals('e', mapped.get(1, 1));
        assertEquals('a', m.get(0, 0));
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

    // ============ Fill Tests ============

    @Test
    public void testFill_singleValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
        m.fill('x');
        assertEquals('x', m.get(0, 0));
        assertEquals('x', m.get(0, 1));
        assertEquals('x', m.get(1, 0));
        assertEquals('x', m.get(1, 1));
    }

    @Test
    public void testFill_withArray() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
        m.fill(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(1, 1));
    }

    @Test
    public void testFill_withOffset() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
        m.fill(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals('\0', m.get(0, 0));
        assertEquals('a', m.get(1, 1));
        assertEquals('d', m.get(2, 2));
    }

    @Test
    public void testFill_withOffset_invalidPosition() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        assertThrows(IllegalArgumentException.class, () -> m.fill(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } }));
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

    // ============ Extend Tests ============

    @Test
    public void testExtend_withDefaultValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix extended = m.extend(2, 3);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('a', extended.get(0, 0));
        assertEquals('b', extended.get(0, 1));
        assertEquals('\0', extended.get(0, 2));
        assertEquals('\0', extended.get(1, 0));
    }

    @Test
    public void testExtend_withCustomDefaultValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix extended = m.extend(2, 3, 'x');
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('a', extended.get(0, 0));
        assertEquals('b', extended.get(0, 1));
        assertEquals('x', extended.get(0, 2));
        assertEquals('x', extended.get(1, 0));
    }

    @Test
    public void testExtend_withDirections() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
        CharMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('x', extended.get(1, 1));
        assertEquals('\0', extended.get(0, 0));
    }

    @Test
    public void testExtend_withDirectionsAndValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
        CharMatrix extended = m.extend(1, 1, 1, 1, 'o');
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals('x', extended.get(1, 1));
        assertEquals('o', extended.get(0, 0));
    }

    @Test
    public void testExtend_invalidSize() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(1, 1));
    }

    // ============ Transformation Tests ============

    @Test
    public void testReverseH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        m.reverseH();
        assertEquals('c', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('a', m.get(0, 2));
    }

    @Test
    public void testReverseV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.reverseV();
        assertEquals('c', m.get(0, 0));
        assertEquals('d', m.get(0, 1));
        assertEquals('a', m.get(1, 0));
        assertEquals('b', m.get(1, 1));
    }

    @Test
    public void testFlipH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        CharMatrix flipped = m.flipH();
        assertEquals('c', flipped.get(0, 0));
        assertEquals('b', flipped.get(0, 1));
        assertEquals('a', flipped.get(0, 2));
        assertEquals('a', m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix flipped = m.flipV();
        assertEquals('c', flipped.get(0, 0));
        assertEquals('d', flipped.get(0, 1));
        assertEquals('a', flipped.get(1, 0));
        assertEquals('b', flipped.get(1, 1));
        assertEquals('a', m.get(0, 0));
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
    public void testTranspose_square() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals('a', transposed.get(0, 0));
        assertEquals('c', transposed.get(0, 1));
        assertEquals('b', transposed.get(1, 0));
        assertEquals('d', transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape_oneArg() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c', 'd' } });
        CharMatrix reshaped = m.reshape(2);
        assertEquals(2, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals('a', reshaped.get(0, 0));
        assertEquals('b', reshaped.get(0, 1));
        assertEquals('c', reshaped.get(1, 0));
        assertEquals('d', reshaped.get(1, 1));
    }

    @Test
    public void testReshape_twoArgs() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c', 'd' } });
        CharMatrix reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals('a', reshaped.get(0, 0));
        assertEquals('b', reshaped.get(0, 1));
    }

    @Test
    public void testReshape_invalidSize() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        assertThrows(IllegalArgumentException.class, () -> m.reshape(2, 2));
    }

    // ============ Repelem/Repmat Tests ============

    @Test
    public void testRepelem() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
        assertEquals('a', result.get(0, 0));
        assertEquals('a', result.get(0, 1));
        assertEquals('a', result.get(1, 0));
        assertEquals('a', result.get(1, 1));
        assertEquals('b', result.get(0, 2));
    }

    @Test
    public void testRepelem_invalidRepeats() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix result = m.repmat(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
        assertEquals('a', result.get(0, 0));
        assertEquals('b', result.get(0, 1));
        assertEquals('a', result.get(2, 0));
        assertEquals('b', result.get(2, 1));
    }

    @Test
    public void testRepmat_invalidRepeats() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals('a', flat.get(0));
        assertEquals('b', flat.get(1));
        assertEquals('c', flat.get(2));
        assertEquals('d', flat.get(3));
    }

    @Test
    public void testFlatOp() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
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
    public void testVstack_invalidColumns() {
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
    public void testHstack_invalidRows() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' }, { 'c' } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Boxed Test ============

    @Test
    public void testBoxed() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        Matrix<Character> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(Character.valueOf('a'), boxed.get(0, 0));
        assertEquals(Character.valueOf('b'), boxed.get(0, 1));
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_two() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { '1', '2' }, { '3', '4' } });
        CharMatrix result = m1.zipWith(m2, (a, b) -> (char) (a + b - 'a'));
        assertEquals((char) ('1'), result.get(0, 0));
        assertEquals((char) ('2'), result.get(0, 1));
    }

    @Test
    public void testZipWith_two_invalidShape() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c' } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a));
    }

    @Test
    public void testZipWith_three() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' } });
        CharMatrix m3 = CharMatrix.of(new char[][] { { 'e', 'f' } });
        CharMatrix result = m1.zipWith(m2, m3, (a, b, c) -> c);
        assertEquals('e', result.get(0, 0));
        assertEquals('f', result.get(0, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Character> diag = m.streamLU2RD().boxed().toList();
        assertEquals(3, diag.size());
        assertEquals('a', diag.get(0));
        assertEquals('e', diag.get(1));
        assertEquals('i', diag.get(2));
    }

    @Test
    public void testStreamRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Character> diag = m.streamRU2LD().boxed().toList();
        assertEquals(3, diag.size());
        assertEquals('c', diag.get(0));
        assertEquals('e', diag.get(1));
        assertEquals('g', diag.get(2));
    }

    @Test
    public void testStreamH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> all = m.streamH().boxed().toList();
        assertEquals(4, all.size());
        assertEquals('a', all.get(0));
        assertEquals('b', all.get(1));
    }

    @Test
    public void testStreamH_singleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        List<Character> row = m.streamH(0).boxed().toList();
        assertEquals(3, row.size());
        assertEquals('a', row.get(0));
        assertEquals('b', row.get(1));
        assertEquals('c', row.get(2));
    }

    @Test
    public void testStreamH_rowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        List<Character> rows = m.streamH(1, 3).boxed().toList();
        assertEquals(4, rows.size());
        assertEquals('c', rows.get(0));
        assertEquals('d', rows.get(1));
    }

    @Test
    public void testStreamV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> all = m.streamV().boxed().toList();
        assertEquals(4, all.size());
        assertEquals('a', all.get(0));
        assertEquals('c', all.get(1));
    }

    @Test
    public void testStreamV_singleColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        List<Character> col = m.streamV(0).boxed().toList();
        assertEquals(3, col.size());
        assertEquals('a', col.get(0));
        assertEquals('c', col.get(1));
        assertEquals('e', col.get(2));
    }

    @Test
    public void testStreamV_columnRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        List<Character> cols = m.streamV(1, 3).boxed().toList();
        assertEquals(4, cols.size());
        assertEquals('b', cols.get(0));
        assertEquals('e', cols.get(1));
    }

    @Test
    public void testStreamR() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<CharStream> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).count());
    }

    @Test
    public void testStreamR_rowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        List<CharStream> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamC() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<CharStream> cols = m.streamC().toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).count());
    }

    @Test
    public void testStreamC_columnRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        List<CharStream> cols = m.streamC(1, 3).toList();
        assertEquals(2, cols.size());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach_valueConsumer() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<Character> values = new ArrayList<>();
        m.forEach(c -> values.add(c));
        assertEquals(4, values.size());
        assertEquals('a', values.get(0));
        assertEquals('b', values.get(1));
    }

    @Test
    public void testForEach_withRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        List<Character> values = new ArrayList<>();
        m.forEach(1, 3, 1, 3, c -> values.add(c));
        assertEquals(4, values.size());
        assertEquals('e', values.get(0));
        assertEquals('f', values.get(1));
    }

    // ============ Points Tests (Inherited) ============

    @Test
    public void testPointsLU2RD() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        List<Point> points = m.pointsLU2RD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 2), points.get(2));
    }

    @Test
    public void testPointsRU2LD() {
        CharMatrix m = CharMatrix.of(new char[3][3]);
        List<Point> points = m.pointsRU2LD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 2), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsH() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        List<Point> points = m.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
    }

    @Test
    public void testPointsH_singleRow() {
        CharMatrix m = CharMatrix.of(new char[2][3]);
        List<Point> points = m.pointsH(0).toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 2), points.get(2));
    }

    @Test
    public void testPointsV() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        List<Point> points = m.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
    }

    @Test
    public void testPointsV_singleColumn() {
        CharMatrix m = CharMatrix.of(new char[3][2]);
        List<Point> points = m.pointsV(0).toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsR() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        List<Stream<Point>> rows = m.pointsR().toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).count());
    }

    @Test
    public void testPointsC() {
        CharMatrix m = CharMatrix.of(new char[2][2]);
        List<Stream<Point>> cols = m.pointsC().toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).count());
    }

    // ============ Utility Tests (Inherited) ============

    @Test
    public void testIsEmpty() {
        CharMatrix empty = CharMatrix.empty();
        assertTrue(empty.isEmpty());
        CharMatrix notEmpty = CharMatrix.of(new char[][] { { 'a' } });
        assertFalse(notEmpty.isEmpty());
    }

    @Test
    public void testArray() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix m = CharMatrix.of(arr);
        char[][] result = m.array();
        assertSame(arr, result);
    }

    @Test
    public void testIsSameShape() {
        CharMatrix m1 = CharMatrix.of(new char[2][3]);
        CharMatrix m2 = CharMatrix.of(new char[2][3]);
        CharMatrix m3 = CharMatrix.of(new char[3][2]);
        assertTrue(m1.isSameShape(m2));
        assertFalse(m1.isSameShape(m3));
    }

    @Test
    public void testAccept() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        AtomicInteger counter = new AtomicInteger(0);
        m.accept(matrix -> counter.set(matrix.rows * matrix.cols));
        assertEquals(2, counter.get());
    }

    @Test
    public void testApply() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        int result = m.apply(matrix -> matrix.rows * matrix.cols);
        assertEquals(2, result);
    }

    // ============ Println Test ============

    @Test
    public void testPrintln() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.contains("a"));
        assertTrue(result.contains("d"));
    }

    // ============ Equals/HashCode Tests ============

    @Test
    public void testHashCode_equal() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals_same() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertEquals(m, m);
    }

    @Test
    public void testEquals_equal() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals(m1, m2);
    }

    @Test
    public void testEquals_notEqual() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' } });
        assertNotEquals(m1, m2);
    }

    @Test
    public void testEquals_null() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertNotEquals(m, null);
    }

    @Test
    public void testEquals_differentType() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertNotEquals(m, "string");
    }

    // ============ ToString Test ============

    @Test
    public void testToString() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("a"));
        assertTrue(str.contains("d"));
    }
}
