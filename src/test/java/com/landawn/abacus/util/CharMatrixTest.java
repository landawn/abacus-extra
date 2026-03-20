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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalChar;
import com.landawn.abacus.util.stream.CharStream;
import com.landawn.abacus.util.stream.Stream;

class CharMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = new CharMatrix(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());

        CharMatrix nullMatrix = new CharMatrix(null);
        Assertions.assertEquals(0, nullMatrix.rowCount());
        Assertions.assertEquals(0, nullMatrix.columnCount());
    }

    @Test
    public void testEmpty() {
        CharMatrix empty = CharMatrix.empty();
        Assertions.assertEquals(0, empty.rowCount());
        Assertions.assertEquals(0, empty.columnCount());
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());

        CharMatrix emptyMatrix = CharMatrix.of();
        Assertions.assertTrue(emptyMatrix.isEmpty());

        CharMatrix nullMatrix = CharMatrix.of((char[][]) null);
        Assertions.assertTrue(nullMatrix.isEmpty());
    }

    @Test
    public void testRandom() {
        CharMatrix matrix = CharMatrix.random(5);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
    }

    @Test
    public void testRepeat() {
        CharMatrix matrix = CharMatrix.repeat(1, 4, 'x');
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        for (int i = 0; i < 4; i++) {
            Assertions.assertEquals('x', matrix.get(0, i));
        }
    }

    @Test
    public void testRange() {
        CharMatrix matrix = CharMatrix.range('a', 'e');
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(0, 2));
        Assertions.assertEquals('d', matrix.get(0, 3));
    }

    @Test
    public void testRangeWithStep() {
        CharMatrix matrix = CharMatrix.range('a', 'g', 2);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('e', matrix.get(0, 2));
    }

    @Test
    public void testRangeClosed() {
        CharMatrix matrix = CharMatrix.rangeClosed('a', 'd');
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('d', matrix.get(0, 3));
    }

    @Test
    public void testRangeClosedWithStep() {
        CharMatrix matrix = CharMatrix.rangeClosed('a', 'g', 2);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('g', matrix.get(0, 3));
    }

    @Test
    public void testDiagonalLU2RD() {
        char[] diagonal = { 'a', 'b', 'c' };
        CharMatrix matrix = CharMatrix.mainDiagonal(diagonal);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('c', matrix.get(2, 2));
        Assertions.assertEquals((char) 0, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        char[] diagonal = { 'a', 'b', 'c' };
        CharMatrix matrix = CharMatrix.antiDiagonal(diagonal);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 2));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('c', matrix.get(2, 0));
        Assertions.assertEquals((char) 0, matrix.get(0, 0));
    }

    @Test
    public void testDiagonal() {
        char[] main = { 'a', 'b', 'c' };
        char[] anti = { 'x', 'y', 'z' };
        CharMatrix matrix = CharMatrix.diagonals(main, anti);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('c', matrix.get(2, 2));
        Assertions.assertEquals('x', matrix.get(0, 2));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('z', matrix.get(2, 0));

        CharMatrix emptyMatrix = CharMatrix.diagonals(null, null);
        Assertions.assertTrue(emptyMatrix.isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonals(new char[] { 'a' }, new char[] { 'x', 'y' }));
    }

    @Test
    public void testUnbox() {
        Character[][] boxed = { { 'a', 'b' }, { 'c', 'd' } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        Assertions.assertEquals(2, unboxed.rowCount());
        Assertions.assertEquals(2, unboxed.columnCount());
        Assertions.assertEquals('a', unboxed.get(0, 0));
        Assertions.assertEquals('d', unboxed.get(1, 1));
    }

    @Test
    public void testComponentType() {
        CharMatrix matrix = CharMatrix.empty();
        Assertions.assertEquals(char.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(1, 0));
        Assertions.assertEquals('d', matrix.get(1, 1));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(2, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        Assertions.assertEquals('a', matrix.get(Point.of(0, 0)));
        Assertions.assertEquals('d', matrix.get(Point.of(1, 1)));
    }

    @Test
    public void testSet() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        matrix.set(0, 0, 'x');
        Assertions.assertEquals('x', matrix.get(0, 0));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.set(2, 0, 'y'));
    }

    @Test
    public void testSetWithPoint() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        matrix.set(Point.of(1, 1), 'x');
        Assertions.assertEquals('x', matrix.get(1, 1));
    }

    @Test
    public void testUpOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar up = matrix.above(1, 0);
        Assertions.assertTrue(up.isPresent());
        Assertions.assertEquals('a', up.get());

        OptionalChar empty = matrix.above(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar down = matrix.below(0, 0);
        Assertions.assertTrue(down.isPresent());
        Assertions.assertEquals('c', down.get());

        OptionalChar empty = matrix.below(1, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar left = matrix.left(0, 1);
        Assertions.assertTrue(left.isPresent());
        Assertions.assertEquals('a', left.get());

        OptionalChar empty = matrix.left(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar right = matrix.right(0, 0);
        Assertions.assertTrue(right.isPresent());
        Assertions.assertEquals('b', right.get());

        OptionalChar empty = matrix.right(0, 1);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] row0 = matrix.rowView(0);
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, row0);

        char[] row1 = matrix.rowView(1);
        Assertions.assertArrayEquals(new char[] { 'd', 'e', 'f' }, row1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowView(2));
    }

    // Verify rowCopy returns a defensive row snapshot and enforces row bounds.
    @Test
    public void testRowCopy() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] rowCopy = matrix.rowCopy(0);
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, rowCopy);

        rowCopy[0] = 'z';
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, matrix.rowView(0));
    }

    @Test
    public void testRowCopy_InvalidIndex() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(2));
    }

    @Test
    public void testColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] col0 = matrix.columnCopy(0);
        Assertions.assertArrayEquals(new char[] { 'a', 'd' }, col0);

        char[] col1 = matrix.columnCopy(1);
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, col1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(3));
    }

    @Test
    public void testSetRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setRow(0, new char[] { 'x', 'y', 'z' });
        Assertions.assertArrayEquals(new char[] { 'x', 'y', 'z' }, matrix.rowView(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new char[] { 'x', 'y' }));
    }

    @Test
    public void testSetColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setColumn(0, new char[] { 'x', 'y' });
        Assertions.assertArrayEquals(new char[] { 'x', 'y' }, matrix.columnCopy(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new char[] { 'x' }));
    }

    @Test
    public void testUpdateRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateRow(0, c -> Character.toUpperCase(c));
        Assertions.assertArrayEquals(new char[] { 'A', 'B', 'C' }, matrix.rowView(0));
    }

    @Test
    public void testUpdateColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateColumn(1, c -> Character.toUpperCase(c));
        Assertions.assertArrayEquals(new char[] { 'B', 'E' }, matrix.columnCopy(1));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(-1, c -> Character.toUpperCase(c)));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(3, c -> Character.toUpperCase(c)));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateRow(0, (Throwables.CharUnaryOperator<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateColumn(0, (Throwables.CharUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.getMainDiagonal();
        Assertions.assertArrayEquals(new char[] { 'a', 'e', 'i' }, diagonal);

        CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'a', 'b' } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setMainDiagonal(new char[] { 'x', 'y', 'z' });
        Assertions.assertEquals('x', matrix.get(0, 0));
        Assertions.assertEquals('y', matrix.get(1, 1));
        Assertions.assertEquals('z', matrix.get(2, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setMainDiagonal(new char[] { 'x', 'y' }));
    }

    @Test
    public void testUpdateLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateMainDiagonal(c -> Character.toUpperCase(c));
        Assertions.assertEquals('A', matrix.get(0, 0));
        Assertions.assertEquals('E', matrix.get(1, 1));
        Assertions.assertEquals('I', matrix.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.getAntiDiagonal();
        Assertions.assertArrayEquals(new char[] { 'c', 'e', 'g' }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setAntiDiagonal(new char[] { 'x', 'y', 'z' });
        Assertions.assertEquals('x', matrix.get(0, 2));
        Assertions.assertEquals('y', matrix.get(1, 1));
        Assertions.assertEquals('z', matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateAntiDiagonal(c -> Character.toUpperCase(c));
        Assertions.assertEquals('C', matrix.get(0, 2));
        Assertions.assertEquals('E', matrix.get(1, 1));
        Assertions.assertEquals('G', matrix.get(2, 0));
    }

    @Test
    public void testUpdateAll() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateAll(c -> Character.toUpperCase(c));
        Assertions.assertEquals('A', matrix.get(0, 0));
        Assertions.assertEquals('B', matrix.get(0, 1));
        Assertions.assertEquals('C', matrix.get(1, 0));
        Assertions.assertEquals('D', matrix.get(1, 1));
    }

    @Test
    public void testUpdateAllWithIndices() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateAll((i, j) -> (char) ('0' + i * 2 + j));
        Assertions.assertEquals('0', matrix.get(0, 0));
        Assertions.assertEquals('1', matrix.get(0, 1));
        Assertions.assertEquals('2', matrix.get(1, 0));
        Assertions.assertEquals('3', matrix.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.replaceIf(c -> c < 'c', 'x');
        Assertions.assertEquals('x', matrix.get(0, 0));
        Assertions.assertEquals('x', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(1, 0));
        Assertions.assertEquals('d', matrix.get(1, 1));
    }

    @Test
    public void testReplaceIfWithIndices() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.replaceIf((i, j) -> i == j, 'x');
        Assertions.assertEquals('x', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(1, 0));
        Assertions.assertEquals('x', matrix.get(1, 1));
    }

    @Test
    public void testMap() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix upper = matrix.map(c -> Character.toUpperCase(c));
        Assertions.assertEquals('A', upper.get(0, 0));
        Assertions.assertEquals('B', upper.get(0, 1));
        Assertions.assertEquals('C', upper.get(1, 0));
        Assertions.assertEquals('D', upper.get(1, 1));
    }

    @Test
    public void testMapNullMapper() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.CharUnaryOperator<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.CharFunction<String, RuntimeException>) null, String.class));
    }

    @Test
    public void testMapToObj() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        Matrix<String> stringMatrix = matrix.mapToObj(c -> String.valueOf(c), String.class);
        Assertions.assertEquals("a", stringMatrix.get(0, 0));
        Assertions.assertEquals("d", stringMatrix.get(1, 1));
    }

    @Test
    public void testFill() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.fill('x');
        Assertions.assertEquals('x', matrix.get(0, 0));
        Assertions.assertEquals('x', matrix.get(0, 1));
        Assertions.assertEquals('x', matrix.get(1, 0));
        Assertions.assertEquals('x', matrix.get(1, 1));
    }

    @Test
    public void testFillWithArray() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[][] b = { { 'x', 'y' }, { 'z', 'w' } };
        matrix.copyFrom(b);
        Assertions.assertEquals('x', matrix.get(0, 0));
        Assertions.assertEquals('y', matrix.get(0, 1));
        Assertions.assertEquals('z', matrix.get(1, 0));
        Assertions.assertEquals('w', matrix.get(1, 1));
        Assertions.assertEquals('i', matrix.get(2, 2)); // unchanged
    }

    @Test
    public void testFillWithIndices() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[][] b = { { 'x', 'y' } };
        matrix.copyFrom(1, 1, b);
        Assertions.assertEquals('a', matrix.get(0, 0)); // unchanged
        Assertions.assertEquals('x', matrix.get(1, 1));
        Assertions.assertEquals('y', matrix.get(1, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(-1, 0, b));
        assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(0, -1, b));
    }

    @Test
    public void testCopy() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        CharMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rowCount(), copy.rowCount());
        Assertions.assertEquals(matrix.columnCount(), copy.columnCount());
        Assertions.assertEquals('a', copy.get(0, 0));
        Assertions.assertEquals('d', copy.get(1, 1));

        copy.set(0, 0, 'x');
        Assertions.assertEquals('a', matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testCopyWithRowRange() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);
        CharMatrix copy = matrix.copy(0, 2);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals('a', copy.get(0, 0));
        Assertions.assertEquals('d', copy.get(1, 1));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 4));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(2, 1));
    }

    @Test
    public void testCopyWithFullRange() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);
        CharMatrix copy = matrix.copy(0, 2, 1, 3);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals('b', copy.get(0, 0));
        Assertions.assertEquals('c', copy.get(0, 1));
        Assertions.assertEquals('e', copy.get(1, 0));
        Assertions.assertEquals('f', copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.resize(3, 3);
        Assertions.assertEquals(3, extended.rowCount());
        Assertions.assertEquals(3, extended.columnCount());
        Assertions.assertEquals('a', extended.get(0, 0));
        Assertions.assertEquals('d', extended.get(1, 1));
        Assertions.assertEquals((char) 0, extended.get(2, 2));

        CharMatrix truncated = matrix.resize(1, 1);
        Assertions.assertEquals(1, truncated.rowCount());
        Assertions.assertEquals(1, truncated.columnCount());
        Assertions.assertEquals('a', truncated.get(0, 0));
    }

    @Test
    public void testExtendWithDefaultValue() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.resize(3, 3, 'x');
        Assertions.assertEquals('x', extended.get(2, 2));
        Assertions.assertEquals('x', extended.get(0, 2));
        Assertions.assertEquals('x', extended.get(2, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 2, 'x'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.resize(2, -1, 'x'));
    }

    @Test
    public void testExtendInAllDirections() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.extend(1, 1, 1, 1);
        Assertions.assertEquals(4, extended.rowCount());
        Assertions.assertEquals(4, extended.columnCount());
        Assertions.assertEquals('a', extended.get(1, 1));
        Assertions.assertEquals('d', extended.get(2, 2));
        Assertions.assertEquals((char) 0, extended.get(0, 0));
    }

    @Test
    public void testExtendInAllDirectionsWithDefaultValue() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.extend(1, 1, 1, 1, 'x');
        Assertions.assertEquals('x', extended.get(0, 0));
        Assertions.assertEquals('x', extended.get(0, 3));
        Assertions.assertEquals('x', extended.get(3, 0));
        Assertions.assertEquals('x', extended.get(3, 3));
        Assertions.assertEquals('a', extended.get(1, 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 0, 0, 0, 'x'));
    }

    @Test
    public void testReverseH() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.flipInPlaceHorizontally();
        Assertions.assertEquals('c', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(0, 1));
        Assertions.assertEquals('a', matrix.get(0, 2));
        Assertions.assertEquals('f', matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.flipInPlaceVertically();
        Assertions.assertEquals('e', matrix.get(0, 0));
        Assertions.assertEquals('f', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(1, 0));
        Assertions.assertEquals('a', matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix flipped = matrix.flipHorizontally();
        Assertions.assertEquals('c', flipped.get(0, 0));
        Assertions.assertEquals('a', flipped.get(0, 2));
        Assertions.assertEquals('a', matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testFlipV() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix flipped = matrix.flipVertically();
        Assertions.assertEquals('e', flipped.get(0, 0));
        Assertions.assertEquals('a', flipped.get(2, 0));
        Assertions.assertEquals('a', matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testRotate90() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals('c', rotated.get(0, 0));
        Assertions.assertEquals('a', rotated.get(0, 1));
        Assertions.assertEquals('d', rotated.get(1, 0));
        Assertions.assertEquals('b', rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix rotated = matrix.rotate180();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals('d', rotated.get(0, 0));
        Assertions.assertEquals('c', rotated.get(0, 1));
        Assertions.assertEquals('b', rotated.get(1, 0));
        Assertions.assertEquals('a', rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix rotated = matrix.rotate270();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals('b', rotated.get(0, 0));
        Assertions.assertEquals('d', rotated.get(0, 1));
        Assertions.assertEquals('a', rotated.get(1, 0));
        Assertions.assertEquals('c', rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix transposed = matrix.transpose();
        Assertions.assertEquals(3, transposed.rowCount());
        Assertions.assertEquals(2, transposed.columnCount());
        Assertions.assertEquals('a', transposed.get(0, 0));
        Assertions.assertEquals('d', transposed.get(0, 1));
        Assertions.assertEquals('b', transposed.get(1, 0));
        Assertions.assertEquals('e', transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix reshaped = matrix.reshape(3, 2);
        Assertions.assertEquals(3, reshaped.rowCount());
        Assertions.assertEquals(2, reshaped.columnCount());
        Assertions.assertEquals('a', reshaped.get(0, 0));
        Assertions.assertEquals('b', reshaped.get(0, 1));
        Assertions.assertEquals('c', reshaped.get(1, 0));
        Assertions.assertEquals('d', reshaped.get(1, 1));

        CharMatrix empty = CharMatrix.empty().reshape(2, 3);
        Assertions.assertEquals(2, empty.rowCount());
        Assertions.assertEquals(3, empty.columnCount());

        // Test reshape with too-small dimensions throws exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix repeated = matrix.repeatElements(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals('a', repeated.get(0, 0));
        Assertions.assertEquals('a', repeated.get(0, 2));
        Assertions.assertEquals('a', repeated.get(1, 0));
        Assertions.assertEquals('b', repeated.get(0, 3));
        Assertions.assertEquals('d', repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(1, 0));
    }

    @Test
    public void testRepmat() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix repeated = matrix.repeatMatrix(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals('a', repeated.get(0, 0));
        Assertions.assertEquals('a', repeated.get(0, 2));
        Assertions.assertEquals('a', repeated.get(0, 4));
        Assertions.assertEquals('a', repeated.get(2, 0));
        Assertions.assertEquals('d', repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(1, 0));
    }

    @Test
    public void testFlatten() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharList flat = matrix.flatten();
        Assertions.assertEquals(6, flat.size());
        Assertions.assertEquals('a', flat.get(0));
        Assertions.assertEquals('b', flat.get(1));
        Assertions.assertEquals('c', flat.get(2));
        Assertions.assertEquals('d', flat.get(3));
        Assertions.assertEquals('e', flat.get(4));
        Assertions.assertEquals('f', flat.get(5));
    }

    @Test
    public void testFlatOp() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<Character> collected = new ArrayList<>();
        matrix.applyOnFlattened(row -> {
            for (char c : row) {
                collected.add(c);
            }
        });

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains('a'));
        Assertions.assertTrue(collected.contains('d'));
    }

    @Test
    public void testVstack() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[][] b = { { 'e', 'f' }, { 'g', 'h' } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix stacked = matrixA.stackVertically(matrixB);
        Assertions.assertEquals(4, stacked.rowCount());
        Assertions.assertEquals(2, stacked.columnCount());
        Assertions.assertEquals('a', stacked.get(0, 0));
        Assertions.assertEquals('e', stacked.get(2, 0));

        CharMatrix differentCols = CharMatrix.of(new char[][] { { 'x', 'y', 'z' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.stackVertically(differentCols));
    }

    @Test
    public void testHstack() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[][] b = { { 'e', 'f' }, { 'g', 'h' } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix stacked = matrixA.stackHorizontally(matrixB);
        Assertions.assertEquals(2, stacked.rowCount());
        Assertions.assertEquals(4, stacked.columnCount());
        Assertions.assertEquals('a', stacked.get(0, 0));
        Assertions.assertEquals('e', stacked.get(0, 2));

        CharMatrix differentRows = CharMatrix.of(new char[][] { { 'x' }, { 'y' }, { 'z' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.stackHorizontally(differentRows));
    }

    @Test
    public void testAdd() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[][] b = { { 1, 2 }, { 3, 4 } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix sum = matrixA.add(matrixB);
        Assertions.assertEquals('b', sum.get(0, 0)); // 'a' + 1
        Assertions.assertEquals('d', sum.get(0, 1)); // 'b' + 2
        Assertions.assertEquals('f', sum.get(1, 0)); // 'c' + 3
        Assertions.assertEquals('h', sum.get(1, 1)); // 'd' + 4

        CharMatrix differentShape = CharMatrix.of(new char[][] { { 'x' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.add(differentShape));
    }

    @Test
    public void testSubtract() {
        char[][] a = { { 'd', 'e' }, { 'f', 'g' } };
        char[][] b = { { 1, 2 }, { 3, 4 } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix diff = matrixA.subtract(matrixB);
        Assertions.assertEquals('c', diff.get(0, 0)); // 'd' - 1
        Assertions.assertEquals('c', diff.get(0, 1)); // 'e' - 2
        Assertions.assertEquals('c', diff.get(1, 0)); // 'f' - 3
        Assertions.assertEquals('c', diff.get(1, 1)); // 'g' - 4

        CharMatrix differentShape = CharMatrix.of(new char[][] { { 'x' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.subtract(differentShape));
    }

    @Test
    public void testMultiply() {
        char[][] a = { { 2, 3 }, { 4, 5 } };
        char[][] b = { { 1, 2 }, { 3, 4 } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix product = matrixA.multiply(matrixB);
        Assertions.assertEquals(2, product.rowCount());
        Assertions.assertEquals(2, product.columnCount());
        // Results will be char values from multiplication

        CharMatrix incompatible = CharMatrix.of(new char[][] { { 'x', 'y', 'z' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.multiply(incompatible));
    }

    @Test
    public void testBoxed() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        Matrix<Character> boxed = matrix.boxed();
        Assertions.assertEquals(2, boxed.rowCount());
        Assertions.assertEquals(2, boxed.columnCount());
        Assertions.assertEquals(Character.valueOf('a'), boxed.get(0, 0));
        Assertions.assertEquals(Character.valueOf('d'), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        IntMatrix intMatrix = matrix.toIntMatrix();
        Assertions.assertEquals(2, intMatrix.rowCount());
        Assertions.assertEquals(2, intMatrix.columnCount());
        Assertions.assertEquals(97, intMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100, intMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testToLongMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        LongMatrix longMatrix = matrix.toLongMatrix();
        Assertions.assertEquals(2, longMatrix.rowCount());
        Assertions.assertEquals(2, longMatrix.columnCount());
        Assertions.assertEquals(97L, longMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100L, longMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testToFloatMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        Assertions.assertEquals(2, floatMatrix.rowCount());
        Assertions.assertEquals(2, floatMatrix.columnCount());
        Assertions.assertEquals(97.0f, floatMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100.0f, floatMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testToDoubleMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        Assertions.assertEquals(2, doubleMatrix.rowCount());
        Assertions.assertEquals(2, doubleMatrix.columnCount());
        Assertions.assertEquals(97.0, doubleMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100.0, doubleMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testZipWith() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[][] b = { { 'A', 'B' }, { 'C', 'D' } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix result = matrixA.zipWith(matrixB, (x, y) -> (char) Math.max(x, y));
        Assertions.assertEquals('a', result.get(0, 0));
        Assertions.assertEquals('b', result.get(0, 1));
        Assertions.assertEquals('c', result.get(1, 0));
        Assertions.assertEquals('d', result.get(1, 1));

        CharMatrix differentShape = CharMatrix.of(new char[][] { { 'x' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.zipWith(differentShape, (x, y) -> x));
    }

    @Test
    public void testZipWith3() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[][] b = { { 'e', 'f' }, { 'g', 'h' } };
        char[][] c = { { 'i', 'j' }, { 'k', 'l' } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);
        CharMatrix matrixC = CharMatrix.of(c);

        CharMatrix result = matrixA.zipWith(matrixB, matrixC, (x, y, z) -> (char) Math.max(Math.max(x, y), z));
        Assertions.assertEquals('i', result.get(0, 0));
        Assertions.assertEquals('j', result.get(0, 1));
        Assertions.assertEquals('k', result.get(1, 0));
        Assertions.assertEquals('l', result.get(1, 1));
    }

    @Test
    public void testStreamLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.streamMainDiagonal().toArray();
        Assertions.assertArrayEquals(new char[] { 'a', 'e', 'i' }, diagonal);

        CharMatrix empty = CharMatrix.empty();
        Assertions.assertTrue(empty.streamMainDiagonal().toList().isEmpty());

        CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'a', 'b' } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
    }

    @Test
    public void testStreamRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.streamAntiDiagonal().toArray();
        Assertions.assertArrayEquals(new char[] { 'c', 'e', 'g' }, diagonal);
    }

    @Test
    public void testStreamH() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] all = matrix.streamHorizontal().toArray();
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, all);

        CharMatrix empty = CharMatrix.empty();
        Assertions.assertTrue(empty.streamHorizontal().toList().isEmpty());
    }

    @Test
    public void testStreamHRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] row1 = matrix.streamHorizontal(1).toArray();
        Assertions.assertArrayEquals(new char[] { 'd', 'e', 'f' }, row1);
    }

    @Test
    public void testStreamHRange() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] range = matrix.streamHorizontal(1, 3).toArray();
        Assertions.assertArrayEquals(new char[] { 'c', 'd', 'e', 'f' }, range);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(0, 4));
    }

    @Test
    public void testStreamV() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] all = matrix.streamVertical().toArray();
        Assertions.assertArrayEquals(new char[] { 'a', 'd', 'b', 'e', 'c', 'f' }, all);
    }

    @Test
    public void testStreamVColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] col1 = matrix.streamVertical(1).toArray();
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, col1);
    }

    @Test
    public void testStreamVRange() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] range = matrix.streamVertical(1, 3).toArray();
        Assertions.assertArrayEquals(new char[] { 'b', 'e', 'c', 'f' }, range);
    }

    @Test
    public void testStreamR() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> rows = matrix.streamRows().toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new char[] { 'a', 'b' }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'c', 'd' }, rows.get(1).toArray());
    }

    @Test
    public void testStreamRRange() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> rows = matrix.streamRows(1, 3).toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new char[] { 'c', 'd' }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'e', 'f' }, rows.get(1).toArray());
    }

    @Test
    public void testStreamC() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> columnCount = matrix.streamColumns().toList();
        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertArrayEquals(new char[] { 'a', 'd' }, columnCount.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, columnCount.get(1).toArray());
        Assertions.assertArrayEquals(new char[] { 'c', 'f' }, columnCount.get(2).toArray());
    }

    @Test
    public void testStreamCRange() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> columnCount = matrix.streamColumns(1, 3).toList();
        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, columnCount.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'c', 'f' }, columnCount.get(1).toArray());
    }

    @Test
    public void testLength() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        // length is protected, cannot test directly from here
        // It's tested indirectly through other operations
        Assertions.assertEquals(2, matrix.columnCount());
    }

    @Test
    public void testForEach() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<Character> collected = new ArrayList<>();
        matrix.forEach(c -> collected.add(c));

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains('a'));
        Assertions.assertTrue(collected.contains('b'));
        Assertions.assertTrue(collected.contains('c'));
        Assertions.assertTrue(collected.contains('d'));
    }

    @Test
    public void testForEachWithRange() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<Character> collected = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, c -> collected.add(c));

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains('e'));
        Assertions.assertTrue(collected.contains('f'));
        Assertions.assertTrue(collected.contains('h'));
        Assertions.assertTrue(collected.contains('i'));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(0, 4, 0, 3, c -> {
        }));
    }

    @Test
    public void testForEachNullAction() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.CharConsumer<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach(0, 1, 0, 2, (Throwables.CharConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::printAndReturn);
    }

    @Test
    public void testHashCode() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix1 = CharMatrix.of(a);
        CharMatrix matrix2 = CharMatrix.of(a.clone());

        // Same content should have same hash code
        Assertions.assertEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testEquals() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix1 = CharMatrix.of(a);
        CharMatrix matrix2 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix matrix3 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'e' } });
        CharMatrix matrix4 = CharMatrix.of(new char[][] { { 'a', 'b' } });

        Assertions.assertEquals(matrix1, matrix1); // same instance
        Assertions.assertEquals(matrix1, matrix2); // same content
        Assertions.assertNotEquals(matrix1, matrix3); // different content
        Assertions.assertNotEquals(matrix1, matrix4); // different dimensions
        Assertions.assertNotEquals(matrix1, null);
        Assertions.assertNotEquals(matrix1, "not a matrix");
    }

    @Test
    public void testToString() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        String str = matrix.toString();
        Assertions.assertNotNull(str);
        Assertions.assertTrue(str.contains("a"));
        Assertions.assertTrue(str.contains("d"));
    }

    @Nested
    @Tag("2025")
    class CharMatrix2025Test extends TestBase {

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
            CharMatrix m = CharMatrix.repeat(1, 5, 'Z');
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
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E' }, m.rowView(0));
        }

        @Test
        public void testRange_withStep() {
            CharMatrix m = CharMatrix.range('A', 'K', 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new char[] { 'A', 'C', 'E', 'G', 'I' }, m.rowView(0));
        }

        @Test
        public void testRange_withNegativeStep() {
            CharMatrix m = CharMatrix.range('J', 'A', -2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new char[] { 'J', 'H', 'F', 'D', 'B' }, m.rowView(0));
        }

        @Test
        public void testRangeClosed() {
            CharMatrix m = CharMatrix.rangeClosed('A', 'E');
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E' }, m.rowView(0));
        }

        @Test
        public void testRangeClosed_withStep() {
            CharMatrix m = CharMatrix.rangeClosed('A', 'K', 2);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new char[] { 'A', 'C', 'E', 'G', 'I', 'K' }, m.rowView(0));
        }

        @Test
        public void testDiagonalLU2RD() {
            CharMatrix m = CharMatrix.mainDiagonal(new char[] { 'A', 'B', 'C' });
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
            CharMatrix m = CharMatrix.antiDiagonal(new char[] { 'X', 'Y', 'Z' });
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
            CharMatrix m = CharMatrix.diagonals(new char[] { 'A', 'B', 'C' }, new char[] { 'X', 'Y', 'Z' });
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
            CharMatrix m = CharMatrix.diagonals(new char[] { 'P', 'Q', 'R' }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('P', m.get(0, 0));
            assertEquals('Q', m.get(1, 1));
            assertEquals('R', m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            CharMatrix m = CharMatrix.diagonals(null, new char[] { 'X', 'Y', 'Z' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('X', m.get(0, 2));
            assertEquals('Y', m.get(1, 1));
            assertEquals('Z', m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            CharMatrix m = CharMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonals(new char[] { 'A', 'B' }, new char[] { 'X', 'Y', 'Z' }));
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
            assertEquals(0, unboxed.get(0, 1)); // null -> 0
            assertEquals(0, unboxed.get(1, 0)); // null -> 0
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

            OptionalChar up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals('A', up.get());

            // Top row has no element above
            OptionalChar empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

            OptionalChar down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals('C', down.get());

            // Bottom row has no element below
            OptionalChar empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

            OptionalChar left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals('A', left.get());

            // Leftmost column has no element to the left
            OptionalChar empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });

            OptionalChar right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals('B', right.get());

            // Rightmost column has no element to the right
            OptionalChar empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, m.rowView(0));
            assertArrayEquals(new char[] { 'D', 'E', 'F' }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            assertArrayEquals(new char[] { 'A', 'D' }, m.columnCopy(0));
            assertArrayEquals(new char[] { 'B', 'E' }, m.columnCopy(1));
            assertArrayEquals(new char[] { 'C', 'F' }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            m.setRow(0, new char[] { 'X', 'Y' });
            assertArrayEquals(new char[] { 'X', 'Y' }, m.rowView(0));
            assertArrayEquals(new char[] { 'C', 'D' }, m.rowView(1)); // unchanged
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
            assertArrayEquals(new char[] { 'X', 'Y' }, m.columnCopy(0));
            assertArrayEquals(new char[] { 'B', 'D' }, m.columnCopy(1)); // unchanged
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
            assertArrayEquals(new char[] { 'B', 'C' }, m.rowView(0));
            assertArrayEquals(new char[] { 'C', 'D' }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            m.updateColumn(0, x -> (char) (x + 1));
            assertArrayEquals(new char[] { 'B', 'D' }, m.columnCopy(0));
            assertArrayEquals(new char[] { 'B', 'D' }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            assertArrayEquals(new char[] { 'A', 'E', 'I' }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            m.setMainDiagonal(new char[] { 'X', 'Y', 'Z' });
            assertEquals('X', m.get(0, 0));
            assertEquals('Y', m.get(1, 1));
            assertEquals('Z', m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new char[] { 'X' }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new char[] { 'X', 'Y' }));
        }

        @Test
        public void testUpdateLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            m.updateMainDiagonal(x -> (char) (x + 1));
            assertEquals('B', m.get(0, 0));
            assertEquals('F', m.get(1, 1));
            assertEquals('J', m.get(2, 2));
            assertEquals('B', m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> (char) (x + 1)));
        }

        @Test
        public void testGetRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            assertArrayEquals(new char[] { 'C', 'E', 'G' }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            m.setAntiDiagonal(new char[] { 'X', 'Y', 'Z' });
            assertEquals('X', m.get(0, 2));
            assertEquals('Y', m.get(1, 1));
            assertEquals('Z', m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new char[] { 'X' }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new char[] { 'X', 'Y' }));
        }

        @Test
        public void testUpdateRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            m.updateAntiDiagonal(x -> (char) (x + 1));
            assertEquals('D', m.get(0, 2));
            assertEquals('F', m.get(1, 1));
            assertEquals('H', m.get(2, 0));
            assertEquals('B', m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> (char) (x + 1)));
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
            assertEquals('?', m.get(1, 0)); // was D
            assertEquals('?', m.get(1, 1)); // was E
            assertEquals('?', m.get(1, 2)); // was F
        }

        @Test
        public void testReplaceIf_withIndices() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            m.replaceIf((i, j) -> i == j, 'X'); // Replace diagonal
            assertEquals('X', m.get(0, 0));
            assertEquals('X', m.get(1, 1));
            assertEquals('X', m.get(2, 2));
            assertEquals('B', m.get(0, 1)); // unchanged
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
            m.copyFrom(patch);
            assertEquals('X', m.get(0, 0));
            assertEquals('Y', m.get(0, 1));
            assertEquals('Z', m.get(1, 0));
            assertEquals('W', m.get(1, 1));
            assertEquals('A', m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'A', 'A' }, { 'A', 'A', 'A' }, { 'A', 'A', 'A' } });
            char[][] patch = { { 'X', 'Y' }, { 'Z', 'W' } };
            m.copyFrom(1, 1, patch);
            assertEquals('A', m.get(0, 0)); // unchanged
            assertEquals('X', m.get(1, 1));
            assertEquals('Y', m.get(1, 2));
            assertEquals('Z', m.get(2, 1));
            assertEquals('W', m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            char[][] patch = { { 'X', 'Y' }, { 'Z', 'W' } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
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
            CharMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals('A', extended.get(0, 0));
            assertEquals('D', extended.get(1, 1));
            assertEquals(0, extended.get(3, 3)); // new cells are 0
        }

        @Test
        public void testExtend_smaller() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            CharMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals('A', truncated.get(0, 0));
            assertEquals('E', truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix extended = m.resize(3, 3, '?');
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('A', extended.get(0, 0));
            assertEquals('?', extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, '?'));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, '?'));
        }

        @Test
        public void testExtend_directional() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            CharMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

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
            m.flipInPlaceHorizontally();
            assertEquals('C', m.get(0, 0));
            assertEquals('B', m.get(0, 1));
            assertEquals('A', m.get(0, 2));
            assertEquals('F', m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } });
            m.flipInPlaceVertically();
            assertEquals('E', m.get(0, 0));
            assertEquals('F', m.get(0, 1));
            assertEquals('C', m.get(1, 0));
            assertEquals('A', m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            CharMatrix flipped = m.flipHorizontally();
            assertEquals('C', flipped.get(0, 0));
            assertEquals('B', flipped.get(0, 1));
            assertEquals('A', flipped.get(0, 2));

            // Original unchanged
            assertEquals('A', m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } });
            CharMatrix flipped = m.flipVertically();
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
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' }, reshaped.rowView(0));
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
            CharMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals('A', repeated.get(0, 0));
            assertEquals('A', repeated.get(0, 1));
            assertEquals('A', repeated.get(0, 2));
            assertEquals('B', repeated.get(0, 3));
            assertEquals('B', repeated.get(0, 4));
            assertEquals('B', repeated.get(0, 5));
            assertEquals('A', repeated.get(1, 0)); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals('A', repeated.get(0, 0));
            assertEquals('B', repeated.get(0, 1));
            assertEquals('A', repeated.get(0, 2)); // repeat starts
            assertEquals('B', repeated.get(0, 3));

            assertEquals('C', repeated.get(1, 0));
            assertEquals('D', repeated.get(1, 1));

            // Check vertical repeat
            assertEquals('A', repeated.get(2, 0)); // vertical repeat starts
            assertEquals('B', repeated.get(2, 1));
        }

        @Test
        public void testRepmat_invalidArguments() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
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
            m.applyOnFlattened(row -> {
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
            CharMatrix stacked = m1.stackVertically(m2);

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
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'E', 'F' }, { 'G', 'H' } });
            CharMatrix stacked = m1.stackHorizontally(m2);

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
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
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

            assertEquals(19, product.get(0, 0)); // 1*5 + 2*7
            assertEquals(22, product.get(0, 1)); // 1*6 + 2*8
            assertEquals(43, product.get(1, 0)); // 3*5 + 4*7
            assertEquals(50, product.get(1, 1)); // 3*6 + 4*8
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        @Test
        public void testMultiply_rectangularMatrices() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 1, 2, 3 } }); // 1x3
            CharMatrix m2 = CharMatrix.of(new char[][] { { 4 }, { 5 }, { 6 } }); // 3x1
            CharMatrix product = m1.multiply(m2);

            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(32, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
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
            assertEquals(65, intMatrix.get(0, 0)); // 'A' = 65
            assertEquals(68, intMatrix.get(1, 1)); // 'D' = 68
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

            assertEquals(5, result.get(0, 0)); // 1*5
            assertEquals(12, result.get(0, 1)); // 2*6
            assertEquals(21, result.get(1, 0)); // 3*7
            assertEquals(32, result.get(1, 1)); // 4*8
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

            assertEquals(15, result.get(0, 0)); // 1+5+9
            assertEquals(18, result.get(0, 1)); // 2+6+10
            assertEquals(21, result.get(1, 0)); // 3+7+11
            assertEquals(24, result.get(1, 1)); // 4+8+12
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
            char[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new char[] { 'A', 'E', 'I' }, diagonal);
        }

        @Test
        public void testStreamLU2RD_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().toArray().length);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            char[] antiDiagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new char[] { 'C', 'E', 'G' }, antiDiagonal);
        }

        @Test
        public void testStreamRU2LD_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().toArray().length);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'A', 'B' } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            char[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F' }, all);
        }

        @Test
        public void testStreamH_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertEquals(0, empty.streamHorizontal().toArray().length);
        }

        @Test
        public void testStreamH_withRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            char[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new char[] { 'D', 'E', 'F' }, row1);
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            char[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new char[] { 'D', 'E', 'F', 'G', 'H', 'I' }, rows);
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            char[] all = m.streamVertical().toArray();
            assertArrayEquals(new char[] { 'A', 'D', 'B', 'E', 'C', 'F' }, all);
        }

        @Test
        public void testStreamV_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertEquals(0, empty.streamVertical().toArray().length);
        }

        @Test
        public void testStreamV_withColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            char[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new char[] { 'B', 'E' }, col1);
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            char[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new char[] { 'B', 'E', 'H', 'C', 'F', 'I' }, columnCount);
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            List<char[]> rows = m.streamRows().map(CharStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, rows.get(0));
            assertArrayEquals(new char[] { 'D', 'E', 'F' }, rows.get(1));
        }

        @Test
        public void testStreamR_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            List<char[]> rows = m.streamRows(1, 3).map(CharStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new char[] { 'D', 'E', 'F' }, rows.get(0));
            assertArrayEquals(new char[] { 'G', 'H', 'I' }, rows.get(1));
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });
            List<char[]> columnCount = m.streamColumns().map(CharStream::toArray).toList();
            assertEquals(3, columnCount.size());
            assertArrayEquals(new char[] { 'A', 'D' }, columnCount.get(0));
            assertArrayEquals(new char[] { 'B', 'E' }, columnCount.get(1));
            assertArrayEquals(new char[] { 'C', 'F' }, columnCount.get(2));
        }

        @Test
        public void testStreamC_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' }, { 'G', 'H', 'I' } });
            List<char[]> columnCount = m.streamColumns(1, 3).map(CharStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new char[] { 'B', 'E', 'H' }, columnCount.get(0));
            assertArrayEquals(new char[] { 'C', 'F', 'I' }, columnCount.get(1));
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ Object Methods Tests ============

        @Test
        public void testHashCode() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix m3 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'D', 'C' } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            CharMatrix m3 = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'D', 'C' } });
            CharMatrix m4 = CharMatrix.of(new char[][] { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
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
            assertEquals('A', m.get(0, 0)); // Unicode for 'A'
            assertEquals('B', m.get(0, 1)); // Unicode for 'B'
            assertEquals('\u4E2D', m.get(1, 0)); // Chinese character
            assertEquals('\u6587', m.get(1, 1)); // Chinese character
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

            CharMatrix extended = empty.resize(2, 2, 'X');
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals('X', extended.get(0, 0));
        }

        // ============ High-Impact Tests for 95% Coverage ============

        @Test
        public void testRotateTransposeAndConvertTallMatrix() {
            // Create a tall matrix (rows > columnCount) - 4 rows × 2 columnCount
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

            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repeatElements(50000, 1));
            assertTrue(ex1.getMessage().contains("row count overflow"));

            CharMatrix m2 = CharMatrix.of(new char[2][largeSize]);
            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repeatElements(1, 50000));
            assertTrue(ex2.getMessage().contains("column count overflow"));
        }

        @Test
        public void testRepmatOverflow() {
            int largeSize = 50000;
            CharMatrix m = CharMatrix.of(new char[largeSize][2]);

            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(50000, 1));
            assertTrue(ex1.getMessage().contains("row count overflow"));

            CharMatrix m2 = CharMatrix.of(new char[2][largeSize]);
            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repeatMatrix(1, 50000));
            assertTrue(ex2.getMessage().contains("column count overflow"));
        }
    }

    @Nested
    @Tag("2510")
    class CharMatrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            CharMatrix m = new CharMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('c', m.get(1, 0));
            assertEquals('d', m.get(1, 1));
        }

        @Test
        public void testConstructor_withSingleElement() {
            CharMatrix m = new CharMatrix(new char[][] { { 'x' } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals('x', m.get(0, 0));
        }

        @Test
        public void testConstructor_withNonSquareMatrix() {
            char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
            CharMatrix m = new CharMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void testConstructor_withNumbers() {
            char[][] arr = { { '1', '2' }, { '3', '4' } };
            CharMatrix m = new CharMatrix(arr);
            assertEquals('1', m.get(0, 0));
            assertEquals('4', m.get(1, 1));
        }

        @Test
        public void testOf_withValidArray() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            CharMatrix m = CharMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals('a', m.get(0, 0));
        }

        @Test
        public void testRandom_withZeroLength() {
            CharMatrix m = CharMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRepeat_withZero() {
            CharMatrix m = CharMatrix.repeat(1, 3, '\0');
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 3; i++) {
                assertEquals('\0', m.get(0, i));
            }
        }

        @Test
        public void testRange() {
            CharMatrix m = CharMatrix.range('a', 'e');
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('c', m.get(0, 2));
            assertEquals('d', m.get(0, 3));
        }

        @Test
        public void testRange_withStep() {
            CharMatrix m = CharMatrix.range('a', 'j', 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('c', m.get(0, 1));
            assertEquals('e', m.get(0, 2));
            assertEquals('g', m.get(0, 3));
            assertEquals('i', m.get(0, 4));
        }

        @Test
        public void testRange_emptyRange() {
            CharMatrix m = CharMatrix.range('a', 'a');
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRangeClosed() {
            CharMatrix m = CharMatrix.rangeClosed('a', 'd');
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('d', m.get(0, 3));
        }

        @Test
        public void testRangeClosed_withStep() {
            CharMatrix m = CharMatrix.rangeClosed('a', 'j', 3);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('d', m.get(0, 1));
            assertEquals('g', m.get(0, 2));
            assertEquals('j', m.get(0, 3));
        }

        @Test
        public void testRangeClosed_singleElement() {
            CharMatrix m = CharMatrix.rangeClosed('x', 'x');
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals('x', m.get(0, 0));
        }

        @Test
        public void testDiagonalLU2RD() {
            CharMatrix m = CharMatrix.mainDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
            assertEquals('\0', m.get(0, 1));
            assertEquals('\0', m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            CharMatrix m = CharMatrix.antiDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 2));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 0));
            assertEquals('\0', m.get(0, 0));
            assertEquals('\0', m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            CharMatrix m = CharMatrix.diagonals(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
            assertEquals('x', m.get(0, 2));
            assertEquals('z', m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            CharMatrix m = CharMatrix.diagonals(new char[] { 'a', 'b', 'c' }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            CharMatrix m = CharMatrix.diagonals(null, new char[] { 'x', 'y', 'z' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('x', m.get(0, 2));
            assertEquals('y', m.get(1, 1));
            assertEquals('z', m.get(2, 0));
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonals(new char[] { 'a', 'b' }, new char[] { 'x', 'y', 'z' }));
        }

        @Test
        public void testUnbox() {
            Character[][] boxed = { { 'a', 'b' }, { 'c', 'd' } };
            Matrix<Character> boxedMatrix = Matrix.of(boxed);
            CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals('a', unboxed.get(0, 0));
            assertEquals('b', unboxed.get(0, 1));
        }

        @Test
        public void testUnbox_withNullElements() {
            Character[][] boxed = { { 'a', null }, { null, 'd' } };
            Matrix<Character> boxedMatrix = Matrix.of(boxed);
            CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
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

        @Test
        public void testDownOf_atBottomEdge() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf_atLeftEdge() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf_atRightEdge() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void testRow() {
            char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
            CharMatrix m = CharMatrix.of(arr);
            char[] row0 = m.rowView(0);
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, row0);
            char[] row1 = m.rowView(1);
            assertArrayEquals(new char[] { 'd', 'e', 'f' }, row1);
        }

        @Test
        public void testRow_invalidIndex() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn() {
            char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
            CharMatrix m = CharMatrix.of(arr);
            char[] col0 = m.columnCopy(0);
            assertArrayEquals(new char[] { 'a', 'd' }, col0);
            char[] col1 = m.columnCopy(1);
            assertArrayEquals(new char[] { 'b', 'e' }, col1);
        }

        @Test
        public void testColumn_invalidIndex() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
            m.setRow(0, new char[] { 'a', 'b', 'c' });
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.rowView(0));
            assertArrayEquals(new char[] { '\0', '\0', '\0' }, m.rowView(1));
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
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.columnCopy(0));
            assertArrayEquals(new char[] { '\0', '\0', '\0' }, m.columnCopy(1));
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
            assertArrayEquals(new char[] { 'b', 'c', 'd' }, m.rowView(0));
            assertArrayEquals(new char[] { 'd', 'e', 'f' }, m.rowView(1));
        }

        @Test
        public void testUpdateColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            m.updateColumn(0, val -> (char) (val + 2));
            assertArrayEquals(new char[] { 'c', 'e', 'g' }, m.columnCopy(0));
            assertArrayEquals(new char[] { 'b', 'd', 'f' }, m.columnCopy(1));
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
            m.setMainDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
            assertEquals('\0', m.get(0, 1));
        }

        @Test
        public void testSetLU2RD_invalidLength() {
            CharMatrix m = CharMatrix.of(new char[3][3]);
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new char[] { 'a', 'b' }));
        }

        @Test
        public void testUpdateLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', '\0', '\0' }, { '\0', 'b', '\0' }, { '\0', '\0', 'c' } });
            m.updateMainDiagonal(val -> (char) (val + 1));
            assertEquals('b', m.get(0, 0));
            assertEquals('c', m.get(1, 1));
            assertEquals('d', m.get(2, 2));
        }

        @Test
        public void testSetRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
            m.setAntiDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals('a', m.get(0, 2));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 0));
            assertEquals('\0', m.get(0, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', 'a' }, { '\0', 'b', '\0' }, { 'c', '\0', '\0' } });
            m.updateAntiDiagonal(val -> (char) (val + 1));
            assertEquals('b', m.get(0, 2));
            assertEquals('c', m.get(1, 1));
            assertEquals('d', m.get(2, 0));
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
            m.copyFrom(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('a', m.get(0, 0));
            assertEquals('d', m.get(1, 1));
        }

        @Test
        public void testFill_withOffset() {
            CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
            m.copyFrom(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('\0', m.get(0, 0));
            assertEquals('a', m.get(1, 1));
            assertEquals('d', m.get(2, 2));
        }

        @Test
        public void testFill_withOffset_invalidPosition() {
            CharMatrix m = CharMatrix.of(new char[2][2]);
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(3, 3, new char[][] { { 'a', 'b' }, { 'c', 'd' } }));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix copy = m.copy();
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals('a', copy.get(0, 0));
            assertEquals('b', copy.get(0, 1));
            copy.set(0, 0, 'x');
            assertEquals('a', m.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            CharMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
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
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
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
            CharMatrix extended = m.resize(2, 3);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('a', extended.get(0, 0));
            assertEquals('b', extended.get(0, 1));
            assertEquals('\0', extended.get(0, 2));
            assertEquals('\0', extended.get(1, 0));
        }

        @Test
        public void testExtend_withCustomDefaultValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix extended = m.resize(2, 3, 'x');
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('a', extended.get(0, 0));
            assertEquals('b', extended.get(0, 1));
            assertEquals('x', extended.get(0, 2));
            assertEquals('x', extended.get(1, 0));
        }

        @Test
        public void testExtend_withDirections() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
            CharMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('x', extended.get(1, 1));
            assertEquals('\0', extended.get(0, 0));
        }

        @Test
        public void testExtend_withDirectionsAndValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
            CharMatrix extended = m.extend(1, 1, 1, 1, 'o');
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('x', extended.get(1, 1));
            assertEquals('o', extended.get(0, 0));
        }
        // ============ Transformation Tests ============

        @Test
        public void testReverseH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            m.flipInPlaceHorizontally();
            assertEquals('c', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('a', m.get(0, 2));
        }

        @Test
        public void testReverseV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.flipInPlaceVertically();
            assertEquals('c', m.get(0, 0));
            assertEquals('d', m.get(0, 1));
            assertEquals('a', m.get(1, 0));
            assertEquals('b', m.get(1, 1));
        }

        @Test
        public void testFlipH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix flipped = m.flipHorizontally();
            assertEquals('c', flipped.get(0, 0));
            assertEquals('b', flipped.get(0, 1));
            assertEquals('a', flipped.get(0, 2));
            assertEquals('a', m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix flipped = m.flipVertically();
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
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals('c', rotated.get(0, 0));
            assertEquals('a', rotated.get(0, 1));
            assertEquals('d', rotated.get(1, 0));
            assertEquals('b', rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals('d', rotated.get(0, 0));
            assertEquals('c', rotated.get(0, 1));
            assertEquals('b', rotated.get(1, 0));
            assertEquals('a', rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals('b', rotated.get(0, 0));
            assertEquals('d', rotated.get(0, 1));
            assertEquals('a', rotated.get(1, 0));
            assertEquals('c', rotated.get(1, 1));
        }

        @Test
        public void testTranspose_square() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
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
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals('a', reshaped.get(0, 0));
            assertEquals('b', reshaped.get(0, 1));
            assertEquals('c', reshaped.get(1, 0));
            assertEquals('d', reshaped.get(1, 1));
        }

        @Test
        public void testReshape_twoArgs() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c', 'd' } });
            CharMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals('a', reshaped.get(0, 0));
            assertEquals('b', reshaped.get(0, 1));
        }

        @Test
        public void testReshape_invalidSize() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
            CharMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
        }

        // ============ Repelem/Repmat Tests ============

        @Test
        public void testRepelem() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals('a', result.get(0, 0));
            assertEquals('a', result.get(0, 1));
            assertEquals('a', result.get(1, 0));
            assertEquals('a', result.get(1, 1));
            assertEquals('b', result.get(0, 2));
        }

        @Test
        public void testRepelem_invalidRepeats() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix result = m.repeatMatrix(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals('a', result.get(0, 0));
            assertEquals('b', result.get(0, 1));
            assertEquals('a', result.get(2, 0));
            assertEquals('b', result.get(2, 1));
        }

        @Test
        public void testRepmat_invalidRepeats() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        @Test
        public void testFlatOp() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            AtomicInteger count = new AtomicInteger(0);
            m.applyOnFlattened(row -> count.addAndGet(row.length));
            assertEquals(4, count.get());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' } });
            CharMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals('a', stacked.get(0, 0));
            assertEquals('b', stacked.get(0, 1));
            assertEquals('c', stacked.get(1, 0));
            assertEquals('d', stacked.get(1, 1));
        }

        @Test
        public void testHstack() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' }, { 'c' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' }, { 'd' } });
            CharMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals('a', stacked.get(0, 0));
            assertEquals('b', stacked.get(0, 1));
            assertEquals('c', stacked.get(1, 0));
            assertEquals('d', stacked.get(1, 1));
        }

        @Test
        public void testHstack_invalidRows() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' }, { 'c' } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void testBoxed() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            Matrix<Character> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Character.valueOf('a'), boxed.get(0, 0));
            assertEquals(Character.valueOf('b'), boxed.get(0, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_two() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { '1', '2' }, { '3', '4' } });
            CharMatrix result = m1.zipWith(m2, (a, b) -> (char) (a + b - a));
            assertEquals(('1'), result.get(0, 0));
            assertEquals(('2'), result.get(0, 1));
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
            List<Character> diag = m.streamMainDiagonal().boxed().toList();
            assertEquals(3, diag.size());
            assertEquals('a', diag.get(0));
            assertEquals('e', diag.get(1));
            assertEquals('i', diag.get(2));
        }

        @Test
        public void testStreamRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            List<Character> diag = m.streamAntiDiagonal().boxed().toList();
            assertEquals(3, diag.size());
            assertEquals('c', diag.get(0));
            assertEquals('e', diag.get(1));
            assertEquals('g', diag.get(2));
        }

        @Test
        public void testStreamH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Character> all = m.streamHorizontal().boxed().toList();
            assertEquals(4, all.size());
            assertEquals('a', all.get(0));
            assertEquals('b', all.get(1));
        }

        @Test
        public void testStreamH_singleRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            List<Character> row = m.streamHorizontal(0).boxed().toList();
            assertEquals(3, row.size());
            assertEquals('a', row.get(0));
            assertEquals('b', row.get(1));
            assertEquals('c', row.get(2));
        }

        @Test
        public void testStreamH_rowRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            List<Character> rows = m.streamHorizontal(1, 3).boxed().toList();
            assertEquals(4, rows.size());
            assertEquals('c', rows.get(0));
            assertEquals('d', rows.get(1));
        }

        @Test
        public void testStreamV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Character> all = m.streamVertical().boxed().toList();
            assertEquals(4, all.size());
            assertEquals('a', all.get(0));
            assertEquals('c', all.get(1));
        }

        @Test
        public void testStreamV_singleColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            List<Character> col = m.streamVertical(0).boxed().toList();
            assertEquals(3, col.size());
            assertEquals('a', col.get(0));
            assertEquals('c', col.get(1));
            assertEquals('e', col.get(2));
        }

        @Test
        public void testStreamV_columnRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            List<Character> columnCount = m.streamVertical(1, 3).boxed().toList();
            assertEquals(4, columnCount.size());
            assertEquals('b', columnCount.get(0));
            assertEquals('e', columnCount.get(1));
        }

        @Test
        public void testStreamR() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<CharStream> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).count());
        }

        @Test
        public void testStreamR_rowRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            List<CharStream> rows = m.streamRows(1, 3).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void testStreamC() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<CharStream> columnCount = m.streamColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).count());
        }

        @Test
        public void testStreamC_columnRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            List<CharStream> columnCount = m.streamColumns(1, 3).toList();
            assertEquals(2, columnCount.size());
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
            List<Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 2), points.get(2));
        }

        @Test
        public void testPointsRU2LD() {
            CharMatrix m = CharMatrix.of(new char[3][3]);
            List<Point> points = m.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 2), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsH() {
            CharMatrix m = CharMatrix.of(new char[2][2]);
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
        }

        @Test
        public void testPointsH_singleRow() {
            CharMatrix m = CharMatrix.of(new char[2][3]);
            List<Point> points = m.pointsHorizontal(0).toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 2), points.get(2));
        }

        @Test
        public void testPointsV() {
            CharMatrix m = CharMatrix.of(new char[2][2]);
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
        }

        @Test
        public void testPointsV_singleColumn() {
            CharMatrix m = CharMatrix.of(new char[3][2]);
            List<Point> points = m.pointsVertical(0).toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsR() {
            CharMatrix m = CharMatrix.of(new char[2][2]);
            List<Stream<Point>> rows = m.pointsRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).count());
        }

        @Test
        public void testPointsC() {
            CharMatrix m = CharMatrix.of(new char[2][2]);
            List<Stream<Point>> columnCount = m.pointsColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).count());
        }

        // ============ Utility Tests (Inherited) ============

        @Test
        public void testElementCount() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testElementCount_Empty() {
            CharMatrix m = CharMatrix.empty();
            assertEquals(0, m.elementCount());
        }

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
            char[][] result = m.backingArray();
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
            m.accept(matrix -> counter.set(matrix.rowCount() * matrix.columnCount()));
            assertEquals(2, counter.get());
        }

        @Test
        public void testApply() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(2, result);
        }

        // ============ Println Test ============

        @Test
        public void testPrintln() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            String result = m.printAndReturn();
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

    @Nested
    @Tag("2511")
    class CharMatrix2511Test extends TestBase {
        @Test
        public void testConstructor_largeMatrix() {
            char[][] arr = new char[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    arr[i][j] = (char) ('a' + ((i + j) % 26));
                }
            }
            CharMatrix m = new CharMatrix(arr);
            assertEquals(100, m.rowCount());
            assertEquals(100, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
        }

        @Test
        public void testOf_withSingleRow() {
            CharMatrix m = CharMatrix.of(new char[] { 'a', 'b', 'c' });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('c', m.get(0, 2));
        }

        @Test
        public void testRandom_withLargeLength() {
            CharMatrix m = CharMatrix.random(1000);
            assertEquals(1, m.rowCount());
            assertEquals(1000, m.columnCount());
        }

        @Test
        public void testRepeat_withZero() {
            CharMatrix m = CharMatrix.repeat(1, 3, '\u0000');
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 3; i++) {
                assertEquals('\u0000', m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withZeroLength() {
            CharMatrix m = CharMatrix.repeat(1, 0, 'a');
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRange_withStep() {
            CharMatrix m = CharMatrix.range('a', 'k', 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('c', m.get(0, 1));
            assertEquals('e', m.get(0, 2));
            assertEquals('g', m.get(0, 3));
            assertEquals('i', m.get(0, 4));
        }

        @Test
        public void testRange_emptyRange() {
            CharMatrix m = CharMatrix.range('e', 'e');
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRange_digits() {
            CharMatrix m = CharMatrix.range('0', '5');
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('0', m.get(0, 0));
            assertEquals('4', m.get(0, 4));
        }

        @Test
        public void testRangeClosed_withStep() {
            CharMatrix m = CharMatrix.rangeClosed('a', 'i', 3);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('d', m.get(0, 1));
            assertEquals('g', m.get(0, 2));
        }

        @Test
        public void testRangeClosed_singleElement() {
            CharMatrix m = CharMatrix.rangeClosed('z', 'z');
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals('z', m.get(0, 0));
        }

        @Test
        public void testDiagonalLU2RD() {
            CharMatrix m = CharMatrix.mainDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
            assertEquals('\u0000', m.get(0, 1));
            assertEquals('\u0000', m.get(1, 0));
        }

        @Test
        public void testDiagonalLU2RD_singleElement() {
            CharMatrix m = CharMatrix.mainDiagonal(new char[] { 'x' });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals('x', m.get(0, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            CharMatrix m = CharMatrix.antiDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 2));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 0));
            assertEquals('\u0000', m.get(0, 0));
            assertEquals('\u0000', m.get(2, 2));
        }

        @Test
        public void testDiagonalRU2LD_empty() {
            CharMatrix m = CharMatrix.antiDiagonal(new char[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withBothDiagonalsOverlapping() {
            CharMatrix m = CharMatrix.diagonals(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            // Center element should be from main diagonal (set second)
            assertEquals('b', m.get(1, 1));
        }

        @Test
        public void testUnbox_withNullElements() {
            Character[][] boxed = { { 'a', null }, { null, 'd' } };
            Matrix<Character> boxedMatrix = Matrix.of(boxed);
            CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals('a', unboxed.get(0, 0));
            assertEquals('\u0000', unboxed.get(0, 1));
            assertEquals('\u0000', unboxed.get(1, 0));
        }

        @Test
        public void testUnbox_isAllNulls() {
            Character[][] boxed = { { null, null }, { null, null } };
            Matrix<Character> boxedMatrix = Matrix.of(boxed);
            CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals('\u0000', unboxed.get(0, 0));
            assertEquals('\u0000', unboxed.get(1, 1));
        }

        @Test
        public void testComponentType_emptyMatrix() {
            CharMatrix m = CharMatrix.empty();
            assertEquals(char.class, m.componentType());
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

        @Test
        public void testUpOf_multipleRows() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            OptionalChar up = m.above(2, 1);
            assertTrue(up.isPresent());
            assertEquals('d', up.get());
        }

        @Test
        public void testDownOf_multipleRows() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            OptionalChar down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals('c', down.get());
        }

        @Test
        public void testLeftOf_multipleColumns() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
            OptionalChar left = m.left(0, 2);
            assertTrue(left.isPresent());
            assertEquals('b', left.get());
        }

        @Test
        public void testRightOf_multipleColumns() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
            OptionalChar right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals('b', right.get());
        }

        @Test
        public void testRow_singleRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.rowView(0));
        }

        @Test
        public void testColumn_singleColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a' }, { 'b' }, { 'c' } });
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.columnCopy(0));
        }

        @Test
        public void testSetRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
            m.setRow(0, new char[] { 'a', 'b', 'c' });
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.rowView(0));
            assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.rowView(1));
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
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, m.columnCopy(0));
            assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.columnCopy(1));
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
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, m.rowView(0));
            assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.rowView(1));
        }

        @Test
        public void testUpdateRow_multipleRows() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            m.updateRow(1, val -> Character.toUpperCase(val));
            assertArrayEquals(new char[] { 'a', 'b' }, m.rowView(0));
            assertArrayEquals(new char[] { 'C', 'D' }, m.rowView(1));
            assertArrayEquals(new char[] { 'e', 'f' }, m.rowView(2));
        }

        @Test
        public void testUpdateColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', ' ' }, { 'b', ' ' }, { 'c', ' ' } });
            m.updateColumn(0, val -> Character.toUpperCase(val));
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, m.columnCopy(0));
            assertArrayEquals(new char[] { ' ', ' ', ' ' }, m.columnCopy(1));
        }

        @Test
        public void testUpdateColumn_multipleColumns() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            m.updateColumn(2, val -> Character.toUpperCase(val));
            assertArrayEquals(new char[] { 'a', 'b', 'C' }, m.rowView(0));
            assertArrayEquals(new char[] { 'd', 'e', 'F' }, m.rowView(1));
        }

        @Test
        public void testGetLU2RD_singleElement() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
            assertArrayEquals(new char[] { 'x' }, m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
            m.setMainDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
            assertEquals(' ', m.get(0, 1));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[2][3]);
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new char[] { 'a', 'b' }));
        }

        @Test
        public void testUpdateLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', ' ', ' ' }, { ' ', 'b', ' ' }, { ' ', ' ', 'c' } });
            m.updateMainDiagonal(val -> Character.toUpperCase(val));
            assertEquals('A', m.get(0, 0));
            assertEquals('B', m.get(1, 1));
            assertEquals('C', m.get(2, 2));
        }

        @Test
        public void testUpdateLU2RD_allValues() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', 'x', ' ' }, { 'x', ' ', 'x' }, { ' ', 'x', ' ' } });
            m.updateMainDiagonal(val -> 'z');
            assertEquals('z', m.get(0, 0));
            assertEquals('z', m.get(1, 1));
            assertEquals('z', m.get(2, 2));
        }

        @Test
        public void testGetRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', 'a' }, { ' ', 'b', ' ' }, { 'c', ' ', ' ' } });
            char[] diag = m.getAntiDiagonal();
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, diag);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
            m.setAntiDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals('a', m.get(0, 2));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 0));
            assertEquals(' ', m.get(0, 0));
        }

        @Test
        public void testSetRU2LD_invalidLength() {
            CharMatrix m = CharMatrix.of(new char[3][3]);
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new char[] { 'a', 'b' }));
        }

        @Test
        public void testUpdateRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', 'a' }, { ' ', 'b', ' ' }, { 'c', ' ', ' ' } });
            m.updateAntiDiagonal(val -> Character.toUpperCase(val));
            assertEquals('A', m.get(0, 2));
            assertEquals('B', m.get(1, 1));
            assertEquals('C', m.get(2, 0));
        }

        @Test
        public void testUpdateRU2LD_rectangular() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', 'a' }, { 'b', ' ', ' ' } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(val -> Character.toUpperCase(val)));
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
            m.copyFrom(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('a', m.get(0, 0));
            assertEquals('d', m.get(1, 1));
        }

        @Test
        public void testFill_withOffset() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
            m.copyFrom(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals(' ', m.get(0, 0));
            assertEquals('a', m.get(1, 1));
            assertEquals('d', m.get(2, 2));
        }

        @Test
        public void testFill_withPartialArray() {
            CharMatrix m = CharMatrix.of(new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } });
            m.copyFrom(0, 0, new char[][] { { 'a', 'b' } });
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals(' ', m.get(0, 2));
            assertEquals(' ', m.get(1, 0));
        }

        @Test
        public void testCopy_emptyMatrix() {
            CharMatrix m = CharMatrix.empty();
            CharMatrix copy = m.copy();
            assertTrue(copy.isEmpty());
        }

        @Test
        public void testCopy_withRowRange_singleRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            CharMatrix copy = m.copy(1, 2);
            assertEquals(1, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals('c', copy.get(0, 0));
            assertEquals('d', copy.get(0, 1));
        }

        @Test
        public void testCopy_withFullRange_singleCell() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix copy = m.copy(0, 1, 1, 2);
            assertEquals(1, copy.rowCount());
            assertEquals(1, copy.columnCount());
            assertEquals('b', copy.get(0, 0));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_withDefaultValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix extended = m.resize(2, 3);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('a', extended.get(0, 0));
            assertEquals('b', extended.get(0, 1));
            assertEquals('\u0000', extended.get(0, 2));
            assertEquals('\u0000', extended.get(1, 0));
        }

        @Test
        public void testExtend_withCustomDefaultValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix extended = m.resize(2, 3, '*');
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('a', extended.get(0, 0));
            assertEquals('b', extended.get(0, 1));
            assertEquals('*', extended.get(0, 2));
            assertEquals('*', extended.get(1, 0));
        }

        @Test
        public void testExtend_withDirections() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
            CharMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('x', extended.get(1, 1));
            assertEquals('\u0000', extended.get(0, 0));
        }

        @Test
        public void testExtend_withDirectionsAndValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
            CharMatrix extended = m.extend(1, 1, 1, 1, '*');
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('x', extended.get(1, 1));
            assertEquals('*', extended.get(0, 0));
        }

        @Test
        public void testExtend_invalidSize() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });

            CharMatrix extended = m.resize(1, 1);
            assertEquals(1, extended.rowCount());
            assertEquals(1, extended.columnCount());
        }

        @Test
        public void testExtend_noExtension() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix extended = m.resize(1, 2);
            assertEquals(1, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals('a', extended.get(0, 0));
            assertEquals('b', extended.get(0, 1));
        }

        // ============ Transformation Tests ============

        @Test
        public void testReverseH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            m.flipInPlaceHorizontally();
            assertArrayEquals(new char[] { 'c', 'b', 'a' }, m.rowView(0));
            assertArrayEquals(new char[] { 'f', 'e', 'd' }, m.rowView(1));
        }

        @Test
        public void testReverseH_singleRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
            m.flipInPlaceHorizontally();
            assertArrayEquals(new char[] { 'c', 'b', 'a' }, m.rowView(0));
        }

        @Test
        public void testReverseV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            m.flipInPlaceVertically();
            assertArrayEquals(new char[] { 'e', 'c', 'a' }, m.columnCopy(0));
            assertArrayEquals(new char[] { 'f', 'd', 'b' }, m.columnCopy(1));
        }

        @Test
        public void testReverseV_singleColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a' }, { 'b' }, { 'c' } });
            m.flipInPlaceVertically();
            assertArrayEquals(new char[] { 'c', 'b', 'a' }, m.columnCopy(0));
        }

        @Test
        public void testFlipH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix flipped = m.flipHorizontally();
            assertEquals(2, flipped.rowCount());
            assertEquals(3, flipped.columnCount());
            assertArrayEquals(new char[] { 'c', 'b', 'a' }, flipped.rowView(0));
            assertArrayEquals(new char[] { 'f', 'e', 'd' }, flipped.rowView(1));
            assertNotSame(m, flipped);
        }

        @Test
        public void testFlipV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            CharMatrix flipped = m.flipVertically();
            assertEquals(3, flipped.rowCount());
            assertEquals(2, flipped.columnCount());
            assertArrayEquals(new char[] { 'e', 'c', 'a' }, flipped.columnCopy(0));
            assertArrayEquals(new char[] { 'f', 'd', 'b' }, flipped.columnCopy(1));
            assertNotSame(m, flipped);
        }

        @Test
        public void testRotate90_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
        }

        @Test
        public void testTranspose_squareMatrix() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals('a', transposed.get(0, 0));
            assertEquals('c', transposed.get(0, 1));
        }

        @Test
        public void testReshape() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c', 'd' } });
            CharMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals('a', reshaped.get(0, 0));
            assertEquals('b', reshaped.get(0, 1));
            assertEquals('c', reshaped.get(1, 0));
            assertEquals('d', reshaped.get(1, 1));
        }

        @Test
        public void testReshape_toSingleRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix reshaped = m.reshape(1, 4);
            assertEquals(1, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
        }

        @Test
        public void testRepelem() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals('a', repeated.get(0, 0));
            assertEquals('a', repeated.get(0, 1));
            assertEquals('a', repeated.get(1, 0));
            assertEquals('a', repeated.get(1, 1));
            assertEquals('b', repeated.get(0, 2));
        }

        @Test
        public void testRepelem_singleRepeat() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix repeated = m.repeatElements(1, 1);
            assertEquals(1, repeated.rowCount());
            assertEquals(2, repeated.columnCount());
            assertEquals('a', repeated.get(0, 0));
            assertEquals('b', repeated.get(0, 1));
        }

        @Test
        public void testRepmat() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix repeated = m.repeatMatrix(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals('a', repeated.get(0, 0));
            assertEquals('b', repeated.get(0, 1));
            assertEquals('d', repeated.get(3, 3));
        }

        @Test
        public void testRepmat_singleRepeat() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix repeated = m.repeatMatrix(1, 1);
            assertEquals(1, repeated.rowCount());
            assertEquals(2, repeated.columnCount());
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

        @Test
        public void testVstack_incompatibleColumns() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'c' } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack_incompatibleRows() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' }, { 'c' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Tests ============

        @Test
        public void testBoxed() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            Matrix<Character> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
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
            List<Character> diagonal = m.streamMainDiagonal().boxed().toList();
            assertEquals(3, diagonal.size());
            assertEquals('a', diagonal.get(0).charValue());
            assertEquals('e', diagonal.get(1).charValue());
            assertEquals('i', diagonal.get(2).charValue());
        }

        @Test
        public void testStreamRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            List<Character> diagonal = m.streamAntiDiagonal().boxed().toList();
            assertEquals(3, diagonal.size());
            assertEquals('c', diagonal.get(0).charValue());
            assertEquals('e', diagonal.get(1).charValue());
            assertEquals('g', diagonal.get(2).charValue());
        }

        @Test
        public void testStreamH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Character> elements = m.streamHorizontal().boxed().toList();
            assertEquals(4, elements.size());
            assertEquals('a', elements.get(0).charValue());
            assertEquals('b', elements.get(1).charValue());
        }

        @Test
        public void testStreamH_withRowIndex() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Character> row = m.streamHorizontal(1).boxed().toList();
            assertEquals(2, row.size());
            assertEquals('c', row.get(0).charValue());
            assertEquals('d', row.get(1).charValue());
        }

        @Test
        public void testStreamH_withRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            List<Character> elements = m.streamHorizontal(1, 3).boxed().toList();
            assertEquals(4, elements.size());
            assertEquals('c', elements.get(0).charValue());
            assertEquals('d', elements.get(1).charValue());
        }

        @Test
        public void testStreamV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Character> elements = m.streamVertical().boxed().toList();
            assertEquals(4, elements.size());
            assertEquals('a', elements.get(0).charValue());
            assertEquals('c', elements.get(1).charValue());
        }

        @Test
        public void testStreamV_withColumnIndex() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Character> col = m.streamVertical(1).boxed().toList();
            assertEquals(2, col.size());
            assertEquals('b', col.get(0).charValue());
            assertEquals('d', col.get(1).charValue());
        }

        @Test
        public void testStreamV_withRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            List<Character> elements = m.streamVertical(1, 3).boxed().toList();
            assertEquals(4, elements.size());
            assertEquals('b', elements.get(0).charValue());
            assertEquals('e', elements.get(1).charValue());
        }

        @Test
        public void testStreamR() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<CharStream> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).toArray().length);
        }

        @Test
        public void testStreamC() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<CharStream> columnCount = m.streamColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).toArray().length);
        }
        // ============ Point Stream Tests ============

        @Test
        public void testPointsH() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsV() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsLU2RD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            List<Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsRU2LD() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            List<Point> points = m.pointsAntiDiagonal().toList();
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
            m.forEachIndex((i, j, val) -> {
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
            char[][] result = m.backingArray();
            assertArrayEquals(arr, result);
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
        public void testPrintln() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            String result = m.printAndReturn();
            assertNotNull(result);
            assertTrue(result.contains("a"));
        }
    }

    @Nested
    @Tag("2512")
    class CharMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_validArray() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            CharMatrix m = new CharMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
        }

        @Test
        public void test_constructor_emptyArray() {
            CharMatrix m = new CharMatrix(new char[0][0]);
            assertTrue(m.isEmpty());
        }
        // ============ Factory Method Tests ============

        @Test
        public void test_empty() {
            CharMatrix empty = CharMatrix.empty();
            assertTrue(empty.isEmpty());
            assertSame(CharMatrix.empty(), CharMatrix.empty());
        }

        @Test
        public void test_of_validArray() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            CharMatrix m = CharMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
        }

        @Test
        public void test_of_singleRow() {
            CharMatrix m = CharMatrix.of(new char[] { 'a', 'b', 'c' });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void test_random() {
            CharMatrix m = CharMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_repeat() {
            CharMatrix m = CharMatrix.repeat(1, 5, 'x');
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals('x', m.get(0, i));
            }
        }

        @Test
        public void test_range() {
            CharMatrix m = CharMatrix.range('a', 'f');
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('e', m.get(0, 4));
        }

        @Test
        public void test_range_withBy() {
            CharMatrix m = CharMatrix.range('a', 'k', 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('i', m.get(0, 4));
        }

        @Test
        public void test_rangeClosed() {
            CharMatrix m = CharMatrix.rangeClosed('a', 'e');
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('e', m.get(0, 4));
        }

        @Test
        public void test_rangeClosed_withBy() {
            CharMatrix m = CharMatrix.rangeClosed('a', 'i', 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('i', m.get(0, 4));
        }

        @Test
        public void test_mainDiagonal() {
            CharMatrix m = CharMatrix.mainDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 2));
            assertEquals('\0', m.get(0, 1));
        }

        @Test
        public void test_mainDiagonal_empty() {
            CharMatrix m = CharMatrix.mainDiagonal(new char[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_antiDiagonal() {
            CharMatrix m = CharMatrix.antiDiagonal(new char[] { 'a', 'b', 'c' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 2));
            assertEquals('b', m.get(1, 1));
            assertEquals('c', m.get(2, 0));
        }

        @Test
        public void test_diagonal_both() {
            CharMatrix m = CharMatrix.diagonals(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals('a', m.get(0, 0));
            assertEquals('x', m.get(0, 2));
        }

        @Test
        public void test_unbox() {
            Matrix<Character> boxed = Matrix.of(new Character[][] { { 'a', 'b' }, { null, 'd' } });
            CharMatrix primitive = CharMatrix.unbox(boxed);
            assertEquals('a', primitive.get(0, 0));
            assertEquals('b', primitive.get(0, 1));
            assertEquals('\0', primitive.get(1, 0)); // null becomes '\0'
            assertEquals('d', primitive.get(1, 1));
        }

        // ============ Element Access Tests ============

        @Test
        public void test_componentType() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertEquals(char.class, m.componentType());
        }

        @Test
        public void test_get_byIndices() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('c', m.get(1, 0));
            assertEquals('d', m.get(1, 1));
        }

        @Test
        public void test_get_byPoint() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('a', m.get(Point.of(0, 0)));
            assertEquals('d', m.get(Point.of(1, 1)));
        }

        @Test
        public void test_set_byIndices() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.set(0, 1, 'x');
            assertEquals('x', m.get(0, 1));
        }

        @Test
        public void test_set_byPoint() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.set(Point.of(1, 0), 'y');
            assertEquals('y', m.get(1, 0));
        }

        // ============ Neighbor Access Tests ============

        @Test
        public void test_upOf_exists() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals('a', up.get());
        }

        @Test
        public void test_upOf_notExists() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void test_downOf_exists() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals('c', down.get());
        }

        @Test
        public void test_leftOf_exists() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals('a', left.get());
        }

        @Test
        public void test_rightOf_exists() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            OptionalChar right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals('b', right.get());
        }
        // ============ Row/Column Access Tests ============

        @Test
        public void test_row() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            char[] row = m.rowView(0);
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, row);
        }

        @Test
        public void test_row_invalidIndex() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            char[] col = m.columnCopy(0);
            assertArrayEquals(new char[] { 'a', 'd' }, col);
        }

        @Test
        public void test_column_invalidIndex() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.setRow(0, new char[] { 'x', 'y' });
            assertEquals('x', m.get(0, 0));
            assertEquals('y', m.get(0, 1));
        }

        @Test
        public void test_setRow_invalidLength() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new char[] { 'x' }));
        }

        @Test
        public void test_setColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.setColumn(0, new char[] { 'x', 'y' });
            assertEquals('x', m.get(0, 0));
            assertEquals('y', m.get(1, 0));
        }

        @Test
        public void test_setColumn_invalidLength() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new char[] { 'x' }));
        }

        @Test
        public void test_updateRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.updateRow(0, val -> (char) (val + 1));
            assertEquals('b', m.get(0, 0));
            assertEquals('c', m.get(0, 1));
        }

        @Test
        public void test_updateColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.updateColumn(0, val -> (char) (val + 1));
            assertEquals('b', m.get(0, 0));
            assertEquals('d', m.get(1, 0));
        }

        // ============ Diagonal Access Tests ============

        @Test
        public void test_getMainDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            char[] diag = m.getMainDiagonal();
            assertArrayEquals(new char[] { 'a', 'e', 'i' }, diag);
        }

        @Test
        public void test_getMainDiagonal_nonSquare() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            m.setMainDiagonal(new char[] { 'x', 'y', 'z' });
            assertEquals('x', m.get(0, 0));
            assertEquals('y', m.get(1, 1));
            assertEquals('z', m.get(2, 2));
        }

        @Test
        public void test_setMainDiagonal_invalidLength() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new char[] { 'x' }));
        }

        @Test
        public void test_updateMainDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            m.updateMainDiagonal(val -> (char) (val + 1));
            assertEquals('b', m.get(0, 0));
            assertEquals('f', m.get(1, 1));
            assertEquals('j', m.get(2, 2));
        }

        @Test
        public void test_getAntiDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            char[] diag = m.getAntiDiagonal();
            assertArrayEquals(new char[] { 'c', 'e', 'g' }, diag);
        }

        @Test
        public void test_setAntiDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            m.setAntiDiagonal(new char[] { 'x', 'y', 'z' });
            assertEquals('x', m.get(0, 2));
            assertEquals('y', m.get(1, 1));
            assertEquals('z', m.get(2, 0));
        }

        @Test
        public void test_updateAntiDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            m.updateAntiDiagonal(val -> (char) (val + 1));
            assertEquals('d', m.get(0, 2));
            assertEquals('f', m.get(1, 1));
            assertEquals('h', m.get(2, 0));
        }

        // ============ Update Tests ============

        @Test
        public void test_updateAll_unaryOperator() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.updateAll(val -> (char) (val + 1));
            assertEquals('b', m.get(0, 0));
            assertEquals('c', m.get(0, 1));
            assertEquals('d', m.get(1, 0));
            assertEquals('e', m.get(1, 1));
        }

        @Test
        public void test_updateAll_biFunction() {
            CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
            m.updateAll((i, j) -> (char) ('a' + i + j));
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('b', m.get(1, 0));
            assertEquals('c', m.get(1, 1));
        }

        @Test
        public void test_replaceIf_predicate() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.replaceIf(val -> val < 'c', 'x');
            assertEquals('x', m.get(0, 0));
            assertEquals('x', m.get(0, 1));
            assertEquals('c', m.get(1, 0));
            assertEquals('d', m.get(1, 1));
        }

        @Test
        public void test_replaceIf_biPredicate() {
            CharMatrix m = CharMatrix.of(new char[3][3]);
            m.replaceIf((i, j) -> i == j, 'x');
            assertEquals('x', m.get(0, 0));
            assertEquals('\0', m.get(0, 1));
            assertEquals('x', m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void test_map() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix incremented = m.map(val -> (char) (val + 1));
            assertEquals('b', incremented.get(0, 0));
            assertEquals('c', incremented.get(0, 1));
            // Original unchanged
            assertEquals('a', m.get(0, 0));
        }

        @Test
        public void test_mapToObj() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            Matrix<String> stringMatrix = m.mapToObj(val -> "C" + val, String.class);
            assertEquals("Ca", stringMatrix.get(0, 0));
            assertEquals("Cb", stringMatrix.get(0, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_singleValue() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.fill('x');
            assertEquals('x', m.get(0, 0));
            assertEquals('x', m.get(0, 1));
            assertEquals('x', m.get(1, 0));
            assertEquals('x', m.get(1, 1));
        }

        @Test
        public void test_fill_array() {
            CharMatrix m = CharMatrix.of(new char[3][3]);
            m.copyFrom(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('a', m.get(0, 0));
            assertEquals('b', m.get(0, 1));
            assertEquals('c', m.get(1, 0));
            assertEquals('d', m.get(1, 1));
            assertEquals('\0', m.get(2, 2));
        }

        @Test
        public void test_fill_arrayWithPosition() {
            CharMatrix m = CharMatrix.of(new char[4][4]);
            m.copyFrom(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            assertEquals('\0', m.get(0, 0));
            assertEquals('a', m.get(1, 1));
            assertEquals('b', m.get(1, 2));
            assertEquals('c', m.get(2, 1));
            assertEquals('d', m.get(2, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy() {
            CharMatrix original = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix copy = original.copy();
            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertEquals('a', copy.get(0, 0));
            // Modify copy
            copy.set(0, 0, 'z');
            assertEquals('a', original.get(0, 0)); // Original unchanged
        }

        @Test
        public void test_copy_fullRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            CharMatrix copy = m.copy(0, 2, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals('b', copy.get(0, 0)); // From (0,1)
            assertEquals('c', copy.get(0, 1)); // From (0,2)
        }

        // ============ Transformation Tests ============

        @Test
        public void test_rotate90() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals('c', rotated.get(0, 0));
            assertEquals('a', rotated.get(0, 1));
        }

        @Test
        public void test_rotate180() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix rotated = m.rotate180();
            assertEquals('d', rotated.get(0, 0));
            assertEquals('c', rotated.get(0, 1));
            assertEquals('b', rotated.get(1, 0));
            assertEquals('a', rotated.get(1, 1));
        }

        @Test
        public void test_rotate270() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
        }

        @Test
        public void test_transpose() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals('a', transposed.get(0, 0));
            assertEquals('d', transposed.get(0, 1));
            assertEquals('b', transposed.get(1, 0));
            assertEquals('e', transposed.get(1, 1));
        }

        @Test
        public void test_flipHorizontally() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix flipped = m.flipHorizontally();
            assertEquals('b', flipped.get(0, 0));
            assertEquals('a', flipped.get(0, 1));
        }

        @Test
        public void test_flipVertically() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix flipped = m.flipVertically();
            assertEquals('c', flipped.get(0, 0));
            assertEquals('d', flipped.get(0, 1));
        }

        @Test
        public void test_flipInPlaceHorizontally() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.flipInPlaceHorizontally();
            assertEquals('b', m.get(0, 0));
            assertEquals('a', m.get(0, 1));
        }

        @Test
        public void test_flipInPlaceVertically() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.flipInPlaceVertically();
            assertEquals('c', m.get(0, 0));
            assertEquals('d', m.get(0, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void test_reshape() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals('a', reshaped.get(0, 0));
            assertEquals('b', reshaped.get(0, 1));
        }

        // ============ Repeat Tests ============

        @Test
        public void test_repeatElements() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals('a', repeated.get(0, 0));
            assertEquals('a', repeated.get(0, 1));
            assertEquals('a', repeated.get(1, 0));
            assertEquals('a', repeated.get(1, 1));
        }

        @Test
        public void test_repeatMatrix() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix tiled = m.repeatMatrix(2, 2);
            assertEquals(4, tiled.rowCount());
            assertEquals(4, tiled.columnCount());
            assertEquals('a', tiled.get(0, 0));
            assertEquals('b', tiled.get(0, 1));
            assertEquals('a', tiled.get(2, 2));
        }

        // ============ Flatten Tests ============

        @Test
        public void test_flatten() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals('a', flat.get(0));
            assertEquals('b', flat.get(1));
            assertEquals('c', flat.get(2));
            assertEquals('d', flat.get(3));
        }

        @Test
        public void test_applyOnFlattened() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            m.applyOnFlattened(arr -> {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (char) (arr[i] + 1);
                }
            });
            assertEquals('b', m.get(0, 0));
            assertEquals('c', m.get(0, 1));
        }

        // ============ Stack Tests ============

        @Test
        public void test_stackVertically() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' } });
            CharMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals('a', stacked.get(0, 0));
            assertEquals('c', stacked.get(1, 0));
        }

        @Test
        public void test_vstack_differentCols() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd', 'e' } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_stackHorizontally() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' }, { 'c' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' }, { 'd' } });
            CharMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals('a', stacked.get(0, 0));
            assertEquals('b', stacked.get(0, 1));
        }

        @Test
        public void test_hstack_differentRows() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' }, { 'e', 'f' } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Tests ============

        @Test
        public void test_add() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { '\1', '\1' }, { '\1', '\1' } });
            CharMatrix result = m1.add(m2);
            assertEquals('b', result.get(0, 0));
            assertEquals('c', result.get(0, 1));
            assertEquals('d', result.get(1, 0));
            assertEquals('e', result.get(1, 1));
        }

        @Test
        public void test_subtract() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'b', 'c' }, { 'd', 'e' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { '\1', '\1' }, { '\1', '\1' } });
            CharMatrix result = m1.subtract(m2);
            assertEquals('a', result.get(0, 0));
            assertEquals('b', result.get(0, 1));
            assertEquals('c', result.get(1, 0));
            assertEquals('d', result.get(1, 1));
        }

        @Test
        public void test_multiply() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { '\2', '\3' }, { '\4', '\5' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { '\2', '\2' }, { '\2', '\2' } });
            CharMatrix result = m1.multiply(m2);
            assertEquals('\12', result.get(0, 0)); // 2*2 + 3*2 = 10
            assertEquals('\12', result.get(0, 1)); // 2*2 + 3*2 = 10
            assertEquals('\22', result.get(1, 0)); // 4*2 + 5*2 = 18
            assertEquals('\22', result.get(1, 1)); // 4*2 + 5*2 = 18
        }

        // ============ Conversion Tests ============

        @Test
        public void test_boxed() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            Matrix<Character> boxed = m.boxed();
            assertEquals('a', boxed.get(0, 0));
            assertEquals('b', boxed.get(0, 1));
        }

        @Test
        public void test_toIntMatrix() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            IntMatrix intMatrix = m.toIntMatrix();
            assertEquals('a', intMatrix.get(0, 0));
            assertEquals('b', intMatrix.get(0, 1));
        }

        @Test
        public void test_toLongMatrix() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            LongMatrix longMatrix = m.toLongMatrix();
            assertEquals('a', longMatrix.get(0, 0));
            assertEquals('b', longMatrix.get(0, 1));
        }

        @Test
        public void test_toFloatMatrix() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals('a', floatMatrix.get(0, 0));
            assertEquals('b', floatMatrix.get(0, 1));
        }

        @Test
        public void test_toDoubleMatrix() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals('a', doubleMatrix.get(0, 0));
            assertEquals('b', doubleMatrix.get(0, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void test_zipWith_twoMatrices() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { '\1', '\1' }, { '\1', '\1' } });
            CharMatrix result = m1.zipWith(m2, (a, b) -> (char) (a + b));
            assertEquals('b', result.get(0, 0));
            assertEquals('c', result.get(0, 1));
        }

        @Test
        public void test_zipWith_threeMatrices() {
            CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix m2 = CharMatrix.of(new char[][] { { '\1', '\1' }, { '\1', '\1' } });
            CharMatrix m3 = CharMatrix.of(new char[][] { { '\1', '\1' }, { '\1', '\1' } });
            CharMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (char) (a + b + c));
            assertEquals('c', result.get(0, 0));
            assertEquals('d', result.get(0, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamMainDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            char[] diag = m.streamMainDiagonal().toArray();
            assertArrayEquals(new char[] { 'a', 'e', 'i' }, diag);
        }

        @Test
        public void test_streamAntiDiagonal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            char[] diag = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new char[] { 'c', 'e', 'g' }, diag);
        }

        @Test
        public void test_streamHorizontal() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            char[] elements = m.streamHorizontal().toArray();
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, elements);
        }

        @Test
        public void test_streamH_singleRow() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            char[] row = m.streamHorizontal(1).toArray();
            assertArrayEquals(new char[] { 'c', 'd' }, row);
        }

        @Test
        public void test_streamH_rowRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            char[] elements = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new char[] { 'c', 'd', 'e', 'f' }, elements);
        }

        @Test
        public void test_streamVertical() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            char[] elements = m.streamVertical().toArray();
            assertArrayEquals(new char[] { 'a', 'c', 'b', 'd' }, elements);
        }

        @Test
        public void test_streamV_singleColumn() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            char[] col = m.streamVertical(0).toArray();
            assertArrayEquals(new char[] { 'a', 'c' }, col);
        }

        @Test
        public void test_streamV_columnRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            char[] elements = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new char[] { 'b', 'e', 'c', 'f' }, elements);
        }

        @Test
        public void test_streamRows() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<char[]> rows = m.streamRows().map(CharStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new char[] { 'a', 'b' }, rows.get(0));
        }

        @Test
        public void test_streamR_rowRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            List<char[]> rows = m.streamRows(1, 3).map(CharStream::toArray).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void test_streamColumns() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            List<char[]> columnCount = m.streamColumns().map(CharStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new char[] { 'a', 'c' }, columnCount.get(0));
        }

        @Test
        public void test_streamC_columnRange() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            List<char[]> columnCount = m.streamColumns(0, 2).map(CharStream::toArray).toList();
            assertEquals(2, columnCount.size());
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend_twoParams() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('a', extended.get(0, 0));
            assertEquals('\0', extended.get(2, 2));
        }

        @Test
        public void test_extend_twoParamsWithDefault() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix extended = m.resize(3, 3, 'x');
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals('x', extended.get(2, 2));
        }

        @Test
        public void test_extend_fourParams() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals('a', extended.get(1, 1));
        }

        @Test
        public void test_extend_fourParamsWithDefault() {
            CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix extended = m.extend(1, 1, 1, 1, 'x');
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals('x', extended.get(0, 0));
            assertEquals('x', extended.get(3, 3));
        }
    }

    @Nested
    class JavadocExampleMatrixGroup1Test_CharMatrix extends TestBase {
        // ==================== CharMatrix ====================

        @Test
        public void testCharMatrix_repeat() {
            CharMatrix matrix = CharMatrix.repeat(2, 3, 'a');
            assertEquals(2, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals('a', matrix.get(i, j));
                }
            }
        }

        @Test
        public void testCharMatrix_rowView() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            char[] firstRow = matrix.rowView(0);
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, firstRow);
        }

        @Test
        public void testCharMatrix_columnCopy() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            char[] firstColumn = matrix.columnCopy(0);
            assertArrayEquals(new char[] { 'a', 'd' }, firstColumn);
        }

        @Test
        public void testCharMatrix_getMainDiagonal() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            char[] diagonal = matrix.getMainDiagonal();
            assertArrayEquals(new char[] { 'a', 'e', 'i' }, diagonal);
        }

        @Test
        public void testCharMatrix_getAntiDiagonal() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            char[] diagonal = matrix.getAntiDiagonal();
            assertArrayEquals(new char[] { 'c', 'e', 'g' }, diagonal);
        }

        @Test
        public void testCharMatrix_updateRow() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            matrix.updateRow(0, c -> Character.toUpperCase(c));
            // matrix is now [['A', 'B', 'C'], ['d', 'e', 'f']]
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, matrix.rowView(0));
            assertArrayEquals(new char[] { 'd', 'e', 'f' }, matrix.rowView(1));
        }

        @Test
        public void testCharMatrix_updateColumn() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'A', 'B' }, { 'C', 'D' } });
            matrix.updateColumn(1, c -> Character.toLowerCase(c));
            // matrix is now [['A', 'b'], ['C', 'd']]
            assertEquals('A', matrix.get(0, 0));
            assertEquals('b', matrix.get(0, 1));
            assertEquals('C', matrix.get(1, 0));
            assertEquals('d', matrix.get(1, 1));
        }

        @Test
        public void testCharMatrix_updateAll() {
            CharMatrix matrix = CharMatrix.of(new char[3][4]);
            matrix.updateAll((i, j) -> (char) ('a' + i * 4 + j));
            // Result: [['a', 'b', 'c', 'd'], ['e', 'f', 'g', 'h'], ['i', 'j', 'k', 'l']]
            assertEquals('a', matrix.get(0, 0));
            assertEquals('d', matrix.get(0, 3));
            assertEquals('e', matrix.get(1, 0));
            assertEquals('h', matrix.get(1, 3));
            assertEquals('i', matrix.get(2, 0));
            assertEquals('l', matrix.get(2, 3));
        }

        @Test
        public void testCharMatrix_copyFrom() {
            CharMatrix matrix = CharMatrix.of(new char[3][3]);
            matrix.copyFrom(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            // Result: [[0, 0, 0], [0, 'a', 'b'], [0, 'c', 'd']]
            assertEquals('\0', matrix.get(0, 0));
            assertEquals('\0', matrix.get(1, 0));
            assertEquals('a', matrix.get(1, 1));
            assertEquals('b', matrix.get(1, 2));
            assertEquals('c', matrix.get(2, 1));
            assertEquals('d', matrix.get(2, 2));
        }

        @Test
        public void testCharMatrix_copy_rows() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            CharMatrix copy = matrix.copy(1, 3);
            // Returns [['c', 'd'], ['e', 'f']]
            assertEquals(2, copy.rowCount());
            assertEquals('c', copy.get(0, 0));
            assertEquals('d', copy.get(0, 1));
            assertEquals('e', copy.get(1, 0));
            assertEquals('f', copy.get(1, 1));
        }

        @Test
        public void testCharMatrix_copy_region() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
            CharMatrix sub = matrix.copy(0, 2, 1, 3);
            // Result: [['b', 'c'], ['e', 'f']]
            assertEquals(2, sub.rowCount());
            assertEquals(2, sub.columnCount());
            assertEquals('b', sub.get(0, 0));
            assertEquals('c', sub.get(0, 1));
            assertEquals('e', sub.get(1, 0));
            assertEquals('f', sub.get(1, 1));
        }

        @Test
        public void testCharMatrix_resize_withFill() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix extended = matrix.resize(3, 4, 'x');
            // Result: [['a', 'b', 'x', 'x'], ['c', 'd', 'x', 'x'], ['x', 'x', 'x', 'x']]
            assertEquals('a', extended.get(0, 0));
            assertEquals('b', extended.get(0, 1));
            assertEquals('x', extended.get(0, 2));
            assertEquals('x', extended.get(0, 3));
            assertEquals('c', extended.get(1, 0));
            assertEquals('d', extended.get(1, 1));
            assertEquals('x', extended.get(1, 2));
            assertEquals('x', extended.get(2, 0));

            // Truncate
            CharMatrix truncated = matrix.resize(1, 1, '\u0000');
            assertEquals('a', truncated.get(0, 0));
        }

        @Test
        public void testCharMatrix_extend_withFill() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix padded = matrix.extend(1, 1, 2, 2, 'x');
            // Result: 3x6, border x, middle has a,b
            assertEquals(3, padded.rowCount());
            assertEquals(6, padded.columnCount());
            assertEquals('x', padded.get(0, 0));
            assertEquals('x', padded.get(1, 0));
            assertEquals('x', padded.get(1, 1));
            assertEquals('a', padded.get(1, 2));
            assertEquals('b', padded.get(1, 3));
            assertEquals('x', padded.get(1, 4));
            assertEquals('x', padded.get(1, 5));
            assertEquals('x', padded.get(2, 0));
        }

        @Test
        public void testCharMatrix_flipInPlaceHorizontally() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            matrix.flipInPlaceHorizontally();
            // matrix is now [['c', 'b', 'a'], ['f', 'e', 'd']]
            assertArrayEquals(new char[] { 'c', 'b', 'a' }, matrix.rowView(0));
            assertArrayEquals(new char[] { 'f', 'e', 'd' }, matrix.rowView(1));
        }

        @Test
        public void testCharMatrix_flipInPlaceVertically() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            matrix.flipInPlaceVertically();
            // matrix is now [['e', 'f'], ['c', 'd'], ['a', 'b']]
            assertArrayEquals(new char[] { 'e', 'f' }, matrix.rowView(0));
            assertArrayEquals(new char[] { 'c', 'd' }, matrix.rowView(1));
            assertArrayEquals(new char[] { 'a', 'b' }, matrix.rowView(2));
        }

        @Test
        public void testCharMatrix_flipHorizontally() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
            CharMatrix flipped = matrix.flipHorizontally();
            assertArrayEquals(new char[] { 'c', 'b', 'a' }, flipped.rowView(0));
            // original unchanged
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, matrix.rowView(0));
        }

        @Test
        public void testCharMatrix_flipVertically() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            CharMatrix flipped = matrix.flipVertically();
            // Returns [['e', 'f'], ['c', 'd'], ['a', 'b']]
            assertArrayEquals(new char[] { 'e', 'f' }, flipped.rowView(0));
            assertArrayEquals(new char[] { 'c', 'd' }, flipped.rowView(1));
            assertArrayEquals(new char[] { 'a', 'b' }, flipped.rowView(2));
        }

        @Test
        public void testCharMatrix_reshape() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            CharMatrix reshaped1 = matrix.reshape(3, 2);
            // Result: [['a', 'b'], ['c', 'd'], ['e', 'f']]
            assertEquals('a', reshaped1.get(0, 0));
            assertEquals('b', reshaped1.get(0, 1));
            assertEquals('c', reshaped1.get(1, 0));
            assertEquals('d', reshaped1.get(1, 1));
            assertEquals('e', reshaped1.get(2, 0));
            assertEquals('f', reshaped1.get(2, 1));

            CharMatrix reshaped2 = matrix.reshape(2, 4);
            // Result: [['a', 'b', 'c', 'd'], ['e', 'f', '\u0000', '\u0000']]
            assertEquals('a', reshaped2.get(0, 0));
            assertEquals('d', reshaped2.get(0, 3));
            assertEquals('e', reshaped2.get(1, 0));
            assertEquals('f', reshaped2.get(1, 1));
            assertEquals('\u0000', reshaped2.get(1, 2));
            assertEquals('\u0000', reshaped2.get(1, 3));
        }

        @Test
        public void testCharMatrix_repeatElements() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix repeated = matrix.repeatElements(2, 3);
            // Result: [['a', 'a', 'a', 'b', 'b', 'b'], ['a', 'a', 'a', 'b', 'b', 'b']]
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertEquals('a', repeated.get(0, 0));
            assertEquals('a', repeated.get(0, 1));
            assertEquals('a', repeated.get(0, 2));
            assertEquals('b', repeated.get(0, 3));
            assertEquals('b', repeated.get(0, 4));
            assertEquals('b', repeated.get(0, 5));
            assertEquals('a', repeated.get(1, 0));
        }

        @Test
        public void testCharMatrix_repeatMatrix() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix repeated = matrix.repeatMatrix(2, 3);
            // Result: [['a', 'b', 'a', 'b', 'a', 'b'], ['c', 'd', 'c', 'd', 'c', 'd'],
            //          ['a', 'b', 'a', 'b', 'a', 'b'], ['c', 'd', 'c', 'd', 'c', 'd']]
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertEquals('a', repeated.get(0, 0));
            assertEquals('b', repeated.get(0, 1));
            assertEquals('a', repeated.get(0, 2));
            assertEquals('d', repeated.get(1, 1));
            assertEquals('a', repeated.get(2, 0));
            assertEquals('d', repeated.get(3, 5));
        }

        @Test
        public void testCharMatrix_flatten() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
            CharList list = matrix.flatten();
            assertEquals(6, list.size());
            assertEquals('a', list.get(0));
            assertEquals('b', list.get(1));
            assertEquals('c', list.get(2));
            assertEquals('d', list.get(3));
            assertEquals('e', list.get(4));
            assertEquals('f', list.get(5));
        }

        @Test
        public void testCharMatrix_applyOnFlattened() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'd', 'b' }, { 'c', 'a' } });
            matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
            // matrix is now [['a', 'b'], ['c', 'd']]
            assertEquals('a', matrix.get(0, 0));
            assertEquals('b', matrix.get(0, 1));
            assertEquals('c', matrix.get(1, 0));
            assertEquals('d', matrix.get(1, 1));
        }

        @Test
        public void testCharMatrix_stackVertically() {
            CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix b = CharMatrix.of(new char[][] { { 'c', 'd' } });
            CharMatrix stacked = a.stackVertically(b);
            // Result: [['a', 'b'], ['c', 'd']]
            assertEquals('a', stacked.get(0, 0));
            assertEquals('b', stacked.get(0, 1));
            assertEquals('c', stacked.get(1, 0));
            assertEquals('d', stacked.get(1, 1));
        }

        @Test
        public void testCharMatrix_stackHorizontally() {
            CharMatrix a = CharMatrix.of(new char[][] { { 'a' }, { 'b' } });
            CharMatrix b = CharMatrix.of(new char[][] { { 'c' }, { 'd' } });
            CharMatrix stacked = a.stackHorizontally(b);
            // Result: [['a', 'c'], ['b', 'd']]
            assertEquals('a', stacked.get(0, 0));
            assertEquals('c', stacked.get(0, 1));
            assertEquals('b', stacked.get(1, 0));
            assertEquals('d', stacked.get(1, 1));
        }

        @Test
        public void testCharMatrix_add() {
            CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix b = CharMatrix.of(new char[][] { { 1, 2 } });
            CharMatrix sum = a.add(b);
            // Result: [['b', 'd']] (a+1=98, b+2=100)
            assertEquals('b', sum.get(0, 0));
            assertEquals('d', sum.get(0, 1));
        }

        @Test
        public void testCharMatrix_subtract() {
            CharMatrix a = CharMatrix.of(new char[][] { { 'd', 'e' } });
            CharMatrix b = CharMatrix.of(new char[][] { { 1, 2 } });
            CharMatrix diff = a.subtract(b);
            // Result: [['c', 'c']] (d-1=99, e-2=99)
            assertEquals('c', diff.get(0, 0));
            assertEquals('c', diff.get(0, 1));
        }

        @Test
        public void testCharMatrix_toIntMatrix() {
            CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            IntMatrix intMatrix = charMatrix.toIntMatrix();
            // Result: [[97, 98]]
            assertEquals(97, intMatrix.get(0, 0));
            assertEquals(98, intMatrix.get(0, 1));
        }

        @Test
        public void testCharMatrix_toLongMatrix() {
            CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            LongMatrix longMatrix = charMatrix.toLongMatrix();
            // Result: [[97L, 98L]]
            assertEquals(97L, longMatrix.get(0, 0));
            assertEquals(98L, longMatrix.get(0, 1));
        }

        @Test
        public void testCharMatrix_zipWith() {
            CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix b = CharMatrix.of(new char[][] { { 'A', 'B' } });
            CharMatrix result = a.zipWith(b, (x, y) -> (char) Math.max(x, y));
            // Result: [['a', 'b']] (max of each pair)
            assertEquals('a', result.get(0, 0));
            assertEquals('b', result.get(0, 1));
        }

        @Test
        public void testCharMatrix_zipWith3() {
            CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b' } });
            CharMatrix b = CharMatrix.of(new char[][] { { 'c', 'd' } });
            CharMatrix c = CharMatrix.of(new char[][] { { 'e', 'f' } });
            CharMatrix result = a.zipWith(b, c, (x, y, z) -> (char) Math.max(Math.max(x, y), z));
            // Result: [['e', 'f']] (max of each triple)
            assertEquals('e', result.get(0, 0));
            assertEquals('f', result.get(0, 1));
        }
    }

    @Nested
    class JavadocExampleMatrixTest_CharMatrix extends TestBase {
        // ==================== CharMatrix Javadoc Examples ====================

        @Test
        public void testCharMatrixEmptyRowCount() {
            // CharMatrix.java: CharMatrix matrix = CharMatrix.empty();
            // matrix.rowCount() returns 0
            // matrix.columnCount() returns 0
            CharMatrix matrix = CharMatrix.empty();
            assertEquals(0, matrix.rowCount());
            assertEquals(0, matrix.columnCount());
        }

        // ==================== CharMatrix range examples ====================

        @Test
        public void testCharMatrixOfGet() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
            assertEquals('a', matrix.get(0, 0));
            assertEquals('f', matrix.get(1, 2));
            assertEquals(2, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
        }
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_CharMatrix extends TestBase {
        @Test
        public void testCharMatrixRowsForZeroColumnMatrix() {
            final CharMatrix matrix = CharMatrix.of(new char[][] { {}, {}, {} });
            final List<char[]> rows = matrix.streamRows().map(CharStream::toArray).toList();

            assertEquals(3, rows.size());
            assertArrayEquals(new char[0], rows.get(0));
            assertArrayEquals(new char[0], rows.get(1));
            assertArrayEquals(new char[0], rows.get(2));
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_CharMatrix extends TestBase {
        @Test
        public void testCharMatrixUpdateAllNullOperator() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            CharMatrix emptyLike = CharMatrix.of(new char[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.CharUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Character, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.CharPredicate<RuntimeException>) null, 'x'));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 'x'));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.CharPredicate<RuntimeException>) null, 'x'));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 'x'));
        }

        @Test
        public void testCharMatrixStackAndMultiplyRejectNullMatrix() {
            CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.add(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.subtract(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.multiply(null));
        }
    }

    // === Missing coverage: resize, copyFrom, flipInPlace ===

    @Test
    public void testResize_expand() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix resized = m.resize(3, 3);
        assertEquals(3, resized.rowCount());
        assertEquals(3, resized.columnCount());
        assertEquals('a', resized.get(0, 0));
        assertEquals('b', resized.get(0, 1));
        assertEquals('\0', resized.get(0, 2));
        assertEquals('c', resized.get(1, 0));
        assertEquals('\0', resized.get(2, 0));
        assertEquals('\0', resized.get(2, 2));
    }

    @Test
    public void testResize_shrink() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        CharMatrix resized = m.resize(2, 2);
        assertEquals(2, resized.rowCount());
        assertEquals(2, resized.columnCount());
        assertEquals('a', resized.get(0, 0));
        assertEquals('b', resized.get(0, 1));
        assertEquals('d', resized.get(1, 0));
        assertEquals('e', resized.get(1, 1));
    }

    @Test
    public void testResize_withDefaultValue() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'x' } });
        CharMatrix resized = m.resize(2, 3, 'z');
        assertEquals(2, resized.rowCount());
        assertEquals(3, resized.columnCount());
        assertEquals('x', resized.get(0, 0));
        assertEquals('z', resized.get(0, 1));
        assertEquals('z', resized.get(0, 2));
        assertEquals('z', resized.get(1, 0));
        assertEquals('z', resized.get(1, 1));
        assertEquals('z', resized.get(1, 2));
    }

    @Test
    public void testResize_toEmpty() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix resized = m.resize(0, 0);
        assertEquals(0, resized.rowCount());
        assertTrue(resized.isEmpty());
    }

    @Test
    public void testResize_negativeThrows() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> m.resize(1, -1));
    }

    @Test
    public void testCopyFrom_fullOverwrite() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0' }, { '\0', '\0' } });
        m.copyFrom(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
    }

    @Test
    public void testCopyFrom_partialOverwrite() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
        m.copyFrom(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('\0', m.get(0, 2));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
        assertEquals('\0', m.get(2, 0));
    }

    @Test
    public void testCopyFrom_withOffset() {
        CharMatrix m = CharMatrix.of(new char[][] { { '\0', '\0', '\0' }, { '\0', '\0', '\0' }, { '\0', '\0', '\0' } });
        m.copyFrom(1, 1, new char[][] { { 'x', 'y' }, { 'z', 'w' } });
        assertEquals('\0', m.get(0, 0));
        assertEquals('\0', m.get(1, 0));
        assertEquals('x', m.get(1, 1));
        assertEquals('y', m.get(1, 2));
        assertEquals('z', m.get(2, 1));
        assertEquals('w', m.get(2, 2));
    }

    @Test
    public void testCopyFrom_emptySource() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.copyFrom(new char[0][0]);
        assertEquals('a', m.get(0, 0));
        assertEquals('d', m.get(1, 1));
    }

    @Test
    public void testCopyFrom_negativeIndexThrows() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' } });
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, new char[][] { { 'b' } }));
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, -1, new char[][] { { 'b' } }));
    }

    @Test
    public void testFlipInPlaceHorizontally() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        m.flipInPlaceHorizontally();
        assertEquals('c', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('a', m.get(0, 2));
        assertEquals('f', m.get(1, 0));
        assertEquals('e', m.get(1, 1));
        assertEquals('d', m.get(1, 2));
    }

    @Test
    public void testFlipInPlaceHorizontally_singleColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a' }, { 'b' } });
        m.flipInPlaceHorizontally();
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 0));
    }

    @Test
    public void testFlipInPlaceVertically() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        m.flipInPlaceVertically();
        assertEquals('e', m.get(0, 0));
        assertEquals('f', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
        assertEquals('a', m.get(2, 0));
        assertEquals('b', m.get(2, 1));
    }

    @Test
    public void testFlipInPlaceVertically_singleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        m.flipInPlaceVertically();
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(0, 2));
    }

    // --- Bug fix tests ---

    @Test
    public void testMultiply_noIntermediateCharCast() {
        // Bug fix: CharMatrix.multiply() had an unnecessary (char) cast on each intermediate
        // product, inconsistent with ByteMatrix and ShortMatrix. The fix removes it.
        // Verify that matrix multiplication still produces correct results.
        CharMatrix a = CharMatrix.of(new char[][] { { 2, 3 }, { 4, 5 } });
        CharMatrix b = CharMatrix.of(new char[][] { { 1, 2 }, { 3, 4 } });

        CharMatrix product = a.multiply(b);

        // product[0][0] = 2*1 + 3*3 = 11
        // product[0][1] = 2*2 + 3*4 = 16
        // product[1][0] = 4*1 + 5*3 = 19
        // product[1][1] = 4*2 + 5*4 = 28
        assertEquals(11, product.get(0, 0));
        assertEquals(16, product.get(0, 1));
        assertEquals(19, product.get(1, 0));
        assertEquals(28, product.get(1, 1));
    }

    @Test
    public void testMultiply_largeValuesOverflow() {
        // Test with larger char values to verify behavior with values that overflow char range
        // during intermediate multiplication. The result should be the same whether or not
        // intermediate values are truncated, due to modular arithmetic.
        CharMatrix a = CharMatrix.of(new char[][] { { 300, 300 } });
        CharMatrix b = CharMatrix.of(new char[][] { { 300 }, { 300 } });

        CharMatrix product = a.multiply(b);

        // 300*300 + 300*300 = 180000
        // (char) 180000 = 180000 % 65536 = 49928 -- but wait, let's compute step by step
        // Due to char truncation on +=:
        // step k=0: result[0][0] = (char)(0 + 300*300) = (char)(90000) = (char)(90000 % 65536) = 24464
        // step k=1: result[0][0] = (char)(24464 + 300*300) = (char)(24464 + 90000) = (char)(114464) = (char)(114464 % 65536) = 48928
        // Without intermediate cast, same result due to modular arithmetic
        assertEquals((char) (300 * 300 + 300 * 300), product.get(0, 0));
    }

    @Test
    public void testMultiply_dimensionMismatchThrows() {
        CharMatrix a = CharMatrix.of(new char[][] { { 'a', 'b', 'c' } });
        CharMatrix b = CharMatrix.of(new char[][] { { 'x', 'y' } });

        assertThrows(IllegalArgumentException.class, () -> a.multiply(b));
    }

}
