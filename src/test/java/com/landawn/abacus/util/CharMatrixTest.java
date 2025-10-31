package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalChar;
import com.landawn.abacus.util.stream.CharStream;

public class CharMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = new CharMatrix(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);

        CharMatrix nullMatrix = new CharMatrix(null);
        Assertions.assertEquals(0, nullMatrix.rows);
        Assertions.assertEquals(0, nullMatrix.cols);
    }

    @Test
    public void testEmpty() {
        CharMatrix empty = CharMatrix.empty();
        Assertions.assertEquals(0, empty.rows);
        Assertions.assertEquals(0, empty.cols);
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);

        CharMatrix emptyMatrix = CharMatrix.of();
        Assertions.assertTrue(emptyMatrix.isEmpty());

        CharMatrix nullMatrix = CharMatrix.of((char[][]) null);
        Assertions.assertTrue(nullMatrix.isEmpty());
    }

    @Test
    public void testRandom() {
        CharMatrix matrix = CharMatrix.random(5);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
    }

    @Test
    public void testRepeat() {
        CharMatrix matrix = CharMatrix.repeat('x', 4);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        for (int i = 0; i < 4; i++) {
            Assertions.assertEquals('x', matrix.get(0, i));
        }
    }

    @Test
    public void testRange() {
        CharMatrix matrix = CharMatrix.range('a', 'e');
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(0, 2));
        Assertions.assertEquals('d', matrix.get(0, 3));
    }

    @Test
    public void testRangeWithStep() {
        CharMatrix matrix = CharMatrix.range('a', 'g', 2);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('e', matrix.get(0, 2));
    }

    @Test
    public void testRangeClosed() {
        CharMatrix matrix = CharMatrix.rangeClosed('a', 'd');
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('d', matrix.get(0, 3));
    }

    @Test
    public void testRangeClosedWithStep() {
        CharMatrix matrix = CharMatrix.rangeClosed('a', 'g', 2);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('g', matrix.get(0, 3));
    }

    @Test
    public void testDiagonalLU2RD() {
        char[] diagonal = { 'a', 'b', 'c' };
        CharMatrix matrix = CharMatrix.diagonalLU2RD(diagonal);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('c', matrix.get(2, 2));
        Assertions.assertEquals((char) 0, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        char[] diagonal = { 'a', 'b', 'c' };
        CharMatrix matrix = CharMatrix.diagonalRU2LD(diagonal);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 2));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('c', matrix.get(2, 0));
        Assertions.assertEquals((char) 0, matrix.get(0, 0));
    }

    @Test
    public void testDiagonal() {
        char[] main = { 'a', 'b', 'c' };
        char[] anti = { 'x', 'y', 'z' };
        CharMatrix matrix = CharMatrix.diagonal(main, anti);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals('a', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('c', matrix.get(2, 2));
        Assertions.assertEquals('x', matrix.get(0, 2));
        Assertions.assertEquals('b', matrix.get(1, 1));
        Assertions.assertEquals('z', matrix.get(2, 0));

        CharMatrix emptyMatrix = CharMatrix.diagonal(null, null);
        Assertions.assertTrue(emptyMatrix.isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonal(new char[] { 'a' }, new char[] { 'x', 'y' }));
    }

    @Test
    public void testUnbox() {
        Character[][] boxed = { { 'a', 'b' }, { 'c', 'd' } };
        Matrix<Character> boxedMatrix = Matrix.of(boxed);
        CharMatrix unboxed = CharMatrix.unbox(boxedMatrix);
        Assertions.assertEquals(2, unboxed.rows);
        Assertions.assertEquals(2, unboxed.cols);
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

        OptionalChar up = matrix.upOf(1, 0);
        Assertions.assertTrue(up.isPresent());
        Assertions.assertEquals('a', up.get());

        OptionalChar empty = matrix.upOf(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar down = matrix.downOf(0, 0);
        Assertions.assertTrue(down.isPresent());
        Assertions.assertEquals('c', down.get());

        OptionalChar empty = matrix.downOf(1, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar left = matrix.leftOf(0, 1);
        Assertions.assertTrue(left.isPresent());
        Assertions.assertEquals('a', left.get());

        OptionalChar empty = matrix.leftOf(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        OptionalChar right = matrix.rightOf(0, 0);
        Assertions.assertTrue(right.isPresent());
        Assertions.assertEquals('b', right.get());

        OptionalChar empty = matrix.rightOf(0, 1);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] row0 = matrix.row(0);
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, row0);

        char[] row1 = matrix.row(1);
        Assertions.assertArrayEquals(new char[] { 'd', 'e', 'f' }, row1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.row(2));
    }

    @Test
    public void testColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] col0 = matrix.column(0);
        Assertions.assertArrayEquals(new char[] { 'a', 'd' }, col0);

        char[] col1 = matrix.column(1);
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, col1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.column(3));
    }

    @Test
    public void testSetRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setRow(0, new char[] { 'x', 'y', 'z' });
        Assertions.assertArrayEquals(new char[] { 'x', 'y', 'z' }, matrix.row(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new char[] { 'x', 'y' }));
    }

    @Test
    public void testSetColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setColumn(0, new char[] { 'x', 'y' });
        Assertions.assertArrayEquals(new char[] { 'x', 'y' }, matrix.column(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new char[] { 'x' }));
    }

    @Test
    public void testUpdateRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateRow(0, c -> Character.toUpperCase(c));
        Assertions.assertArrayEquals(new char[] { 'A', 'B', 'C' }, matrix.row(0));
    }

    @Test
    public void testUpdateColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateColumn(1, c -> Character.toUpperCase(c));
        Assertions.assertArrayEquals(new char[] { 'B', 'E' }, matrix.column(1));
    }

    @Test
    public void testGetLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.getLU2RD();
        Assertions.assertArrayEquals(new char[] { 'a', 'e', 'i' }, diagonal);

        CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'a', 'b' } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setLU2RD(new char[] { 'x', 'y', 'z' });
        Assertions.assertEquals('x', matrix.get(0, 0));
        Assertions.assertEquals('y', matrix.get(1, 1));
        Assertions.assertEquals('z', matrix.get(2, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setLU2RD(new char[] { 'x', 'y' }));
    }

    @Test
    public void testUpdateLU2RD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateLU2RD(c -> Character.toUpperCase(c));
        Assertions.assertEquals('A', matrix.get(0, 0));
        Assertions.assertEquals('E', matrix.get(1, 1));
        Assertions.assertEquals('I', matrix.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.getRU2LD();
        Assertions.assertArrayEquals(new char[] { 'c', 'e', 'g' }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.setRU2LD(new char[] { 'x', 'y', 'z' });
        Assertions.assertEquals('x', matrix.get(0, 2));
        Assertions.assertEquals('y', matrix.get(1, 1));
        Assertions.assertEquals('z', matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.updateRU2LD(c -> Character.toUpperCase(c));
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
        matrix.fill(b);
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
        matrix.fill(1, 1, b);
        Assertions.assertEquals('a', matrix.get(0, 0)); // unchanged
        Assertions.assertEquals('x', matrix.get(1, 1));
        Assertions.assertEquals('y', matrix.get(1, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.fill(-1, 0, b));
        assertThrows(IllegalArgumentException.class, () -> matrix.fill(0, -1, b));
    }

    @Test
    public void testCopy() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);
        CharMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rows, copy.rows);
        Assertions.assertEquals(matrix.cols, copy.cols);
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

        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
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

        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
        Assertions.assertEquals('b', copy.get(0, 0));
        Assertions.assertEquals('c', copy.get(0, 1));
        Assertions.assertEquals('e', copy.get(1, 0));
        Assertions.assertEquals('f', copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.extend(3, 3);
        Assertions.assertEquals(3, extended.rows);
        Assertions.assertEquals(3, extended.cols);
        Assertions.assertEquals('a', extended.get(0, 0));
        Assertions.assertEquals('d', extended.get(1, 1));
        Assertions.assertEquals((char) 0, extended.get(2, 2));

        CharMatrix truncated = matrix.extend(1, 1);
        Assertions.assertEquals(1, truncated.rows);
        Assertions.assertEquals(1, truncated.cols);
        Assertions.assertEquals('a', truncated.get(0, 0));
    }

    @Test
    public void testExtendWithDefaultValue() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.extend(3, 3, 'x');
        Assertions.assertEquals('x', extended.get(2, 2));
        Assertions.assertEquals('x', extended.get(0, 2));
        Assertions.assertEquals('x', extended.get(2, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 2, 'x'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(2, -1, 'x'));
    }

    @Test
    public void testExtendInAllDirections() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix extended = matrix.extend(1, 1, 1, 1);
        Assertions.assertEquals(4, extended.rows);
        Assertions.assertEquals(4, extended.cols);
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

        matrix.reverseH();
        Assertions.assertEquals('c', matrix.get(0, 0));
        Assertions.assertEquals('b', matrix.get(0, 1));
        Assertions.assertEquals('a', matrix.get(0, 2));
        Assertions.assertEquals('f', matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        matrix.reverseV();
        Assertions.assertEquals('e', matrix.get(0, 0));
        Assertions.assertEquals('f', matrix.get(0, 1));
        Assertions.assertEquals('c', matrix.get(1, 0));
        Assertions.assertEquals('a', matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix flipped = matrix.flipH();
        Assertions.assertEquals('c', flipped.get(0, 0));
        Assertions.assertEquals('a', flipped.get(0, 2));
        Assertions.assertEquals('a', matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testFlipV() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix flipped = matrix.flipV();
        Assertions.assertEquals('e', flipped.get(0, 0));
        Assertions.assertEquals('a', flipped.get(2, 0));
        Assertions.assertEquals('a', matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testRotate90() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
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
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
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
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
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
        Assertions.assertEquals(3, transposed.rows);
        Assertions.assertEquals(2, transposed.cols);
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
        Assertions.assertEquals(3, reshaped.rows);
        Assertions.assertEquals(2, reshaped.cols);
        Assertions.assertEquals('a', reshaped.get(0, 0));
        Assertions.assertEquals('b', reshaped.get(0, 1));
        Assertions.assertEquals('c', reshaped.get(1, 0));
        Assertions.assertEquals('d', reshaped.get(1, 1));

        CharMatrix empty = CharMatrix.empty().reshape(2, 3);
        Assertions.assertEquals(2, empty.rows);
        Assertions.assertEquals(3, empty.cols);
    }

    @Test
    public void testRepelem() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix repeated = matrix.repelem(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals('a', repeated.get(0, 0));
        Assertions.assertEquals('a', repeated.get(0, 2));
        Assertions.assertEquals('a', repeated.get(1, 0));
        Assertions.assertEquals('b', repeated.get(0, 3));
        Assertions.assertEquals('d', repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repelem(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        CharMatrix repeated = matrix.repmat(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals('a', repeated.get(0, 0));
        Assertions.assertEquals('a', repeated.get(0, 2));
        Assertions.assertEquals('a', repeated.get(0, 4));
        Assertions.assertEquals('a', repeated.get(2, 0));
        Assertions.assertEquals('d', repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repmat(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repmat(1, 0));
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
        matrix.flatOp(row -> {
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

        CharMatrix stacked = matrixA.vstack(matrixB);
        Assertions.assertEquals(4, stacked.rows);
        Assertions.assertEquals(2, stacked.cols);
        Assertions.assertEquals('a', stacked.get(0, 0));
        Assertions.assertEquals('e', stacked.get(2, 0));

        CharMatrix differentCols = CharMatrix.of(new char[][] { { 'x', 'y', 'z' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.vstack(differentCols));
    }

    @Test
    public void testHstack() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[][] b = { { 'e', 'f' }, { 'g', 'h' } };
        CharMatrix matrixA = CharMatrix.of(a);
        CharMatrix matrixB = CharMatrix.of(b);

        CharMatrix stacked = matrixA.hstack(matrixB);
        Assertions.assertEquals(2, stacked.rows);
        Assertions.assertEquals(4, stacked.cols);
        Assertions.assertEquals('a', stacked.get(0, 0));
        Assertions.assertEquals('e', stacked.get(0, 2));

        CharMatrix differentRows = CharMatrix.of(new char[][] { { 'x' }, { 'y' }, { 'z' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.hstack(differentRows));
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
        Assertions.assertEquals(2, product.rows);
        Assertions.assertEquals(2, product.cols);
        // Results will be char values from multiplication

        CharMatrix incompatible = CharMatrix.of(new char[][] { { 'x', 'y', 'z' } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.multiply(incompatible));
    }

    @Test
    public void testBoxed() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        Matrix<Character> boxed = matrix.boxed();
        Assertions.assertEquals(2, boxed.rows);
        Assertions.assertEquals(2, boxed.cols);
        Assertions.assertEquals(Character.valueOf('a'), boxed.get(0, 0));
        Assertions.assertEquals(Character.valueOf('d'), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        IntMatrix intMatrix = matrix.toIntMatrix();
        Assertions.assertEquals(2, intMatrix.rows);
        Assertions.assertEquals(2, intMatrix.cols);
        Assertions.assertEquals(97, intMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100, intMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testToLongMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        LongMatrix longMatrix = matrix.toLongMatrix();
        Assertions.assertEquals(2, longMatrix.rows);
        Assertions.assertEquals(2, longMatrix.cols);
        Assertions.assertEquals(97L, longMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100L, longMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testToFloatMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        Assertions.assertEquals(2, floatMatrix.rows);
        Assertions.assertEquals(2, floatMatrix.cols);
        Assertions.assertEquals(97.0f, floatMatrix.get(0, 0)); // 'a'
        Assertions.assertEquals(100.0f, floatMatrix.get(1, 1)); // 'd'
    }

    @Test
    public void testToDoubleMatrix() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        Assertions.assertEquals(2, doubleMatrix.rows);
        Assertions.assertEquals(2, doubleMatrix.cols);
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

        char[] diagonal = matrix.streamLU2RD().toArray();
        Assertions.assertArrayEquals(new char[] { 'a', 'e', 'i' }, diagonal);

        CharMatrix empty = CharMatrix.empty();
        Assertions.assertTrue(empty.streamLU2RD().toList().isEmpty());

        CharMatrix nonSquare = CharMatrix.of(new char[][] { { 'a', 'b' } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] diagonal = matrix.streamRU2LD().toArray();
        Assertions.assertArrayEquals(new char[] { 'c', 'e', 'g' }, diagonal);
    }

    @Test
    public void testStreamH() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] all = matrix.streamH().toArray();
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, all);

        CharMatrix empty = CharMatrix.empty();
        Assertions.assertTrue(empty.streamH().toList().isEmpty());
    }

    @Test
    public void testStreamHRow() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] row1 = matrix.streamH(1).toArray();
        Assertions.assertArrayEquals(new char[] { 'd', 'e', 'f' }, row1);
    }

    @Test
    public void testStreamHRange() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] range = matrix.streamH(1, 3).toArray();
        Assertions.assertArrayEquals(new char[] { 'c', 'd', 'e', 'f' }, range);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(0, 4));
    }

    @Test
    public void testStreamV() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] all = matrix.streamV().toArray();
        Assertions.assertArrayEquals(new char[] { 'a', 'd', 'b', 'e', 'c', 'f' }, all);
    }

    @Test
    public void testStreamVColumn() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] col1 = matrix.streamV(1).toArray();
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, col1);
    }

    @Test
    public void testStreamVRange() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        char[] range = matrix.streamV(1, 3).toArray();
        Assertions.assertArrayEquals(new char[] { 'b', 'e', 'c', 'f' }, range);
    }

    @Test
    public void testStreamR() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> rows = matrix.streamR().toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new char[] { 'a', 'b' }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'c', 'd' }, rows.get(1).toArray());
    }

    @Test
    public void testStreamRRange() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> rows = matrix.streamR(1, 3).toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new char[] { 'c', 'd' }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'e', 'f' }, rows.get(1).toArray());
    }

    @Test
    public void testStreamC() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> cols = matrix.streamC().toList();
        Assertions.assertEquals(3, cols.size());
        Assertions.assertArrayEquals(new char[] { 'a', 'd' }, cols.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, cols.get(1).toArray());
        Assertions.assertArrayEquals(new char[] { 'c', 'f' }, cols.get(2).toArray());
    }

    @Test
    public void testStreamCRange() {
        char[][] a = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix matrix = CharMatrix.of(a);

        List<CharStream> cols = matrix.streamC(1, 3).toList();
        Assertions.assertEquals(2, cols.size());
        Assertions.assertArrayEquals(new char[] { 'b', 'e' }, cols.get(0).toArray());
        Assertions.assertArrayEquals(new char[] { 'c', 'f' }, cols.get(1).toArray());
    }

    @Test
    public void testLength() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        // length is protected, cannot test directly from here
        // It's tested indirectly through other operations
        Assertions.assertEquals(2, matrix.cols);
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
    public void testPrintln() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        CharMatrix matrix = CharMatrix.of(a);

        // Just verify it doesn't throw
        matrix.println();
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
}