package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@Tag("2512")
public class CharMatrix2512Test extends TestBase {

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
    public void test_constructor_nullArray() {
        CharMatrix m = new CharMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_emptyArray() {
        CharMatrix m = new CharMatrix(new char[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_singleElement() {
        CharMatrix m = new CharMatrix(new char[][] { { 'x' } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals('x', m.get(0, 0));
    }

    @Test
    public void test_constructor_nonSquare() {
        char[][] arr = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
        CharMatrix m = new CharMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
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
    public void test_of_nullArray() {
        CharMatrix m = CharMatrix.of((char[][]) null);
        assertTrue(m.isEmpty());
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
    public void test_random_zeroLength() {
        CharMatrix m = CharMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void test_repeat() {
        CharMatrix m = CharMatrix.repeat('x', 5);
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
    public void test_diagonalLU2RD() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[] { 'a', 'b', 'c' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 2));
        assertEquals('\0', m.get(0, 1));
    }

    @Test
    public void test_diagonalLU2RD_empty() {
        CharMatrix m = CharMatrix.diagonalLU2RD(new char[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        CharMatrix m = CharMatrix.diagonalRU2LD(new char[] { 'a', 'b', 'c' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('a', m.get(0, 2));
        assertEquals('b', m.get(1, 1));
        assertEquals('c', m.get(2, 0));
    }

    @Test
    public void test_diagonal_both() {
        CharMatrix m = CharMatrix.diagonal(new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'y', 'z' });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals('a', m.get(0, 0));
        assertEquals('x', m.get(0, 2));
    }

    @Test
    public void test_diagonal_differentLengths() {
        assertThrows(IllegalArgumentException.class, () -> CharMatrix.diagonal(new char[] { 'a', 'b' }, new char[] { 'x', 'y', 'z' }));
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
        OptionalChar up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals('a', up.get());
    }

    @Test
    public void test_upOf_notExists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf_exists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals('c', down.get());
    }

    @Test
    public void test_downOf_notExists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf_exists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals('a', left.get());
    }

    @Test
    public void test_leftOf_notExists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf_exists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals('b', right.get());
    }

    @Test
    public void test_rightOf_notExists() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        OptionalChar right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void test_row() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        char[] row = m.row(0);
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, row);
    }

    @Test
    public void test_row_invalidIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        char[] col = m.column(0);
        assertArrayEquals(new char[] { 'a', 'd' }, col);
    }

    @Test
    public void test_column_invalidIndex() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
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
    public void test_getLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diag = m.getLU2RD();
        assertArrayEquals(new char[] { 'a', 'e', 'i' }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        m.setLU2RD(new char[] { 'x', 'y', 'z' });
        assertEquals('x', m.get(0, 0));
        assertEquals('y', m.get(1, 1));
        assertEquals('z', m.get(2, 2));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new char[] { 'x' }));
    }

    @Test
    public void test_updateLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        m.updateLU2RD(val -> (char) (val + 1));
        assertEquals('b', m.get(0, 0));
        assertEquals('f', m.get(1, 1));
        assertEquals('j', m.get(2, 2));
    }

    @Test
    public void test_getRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diag = m.getRU2LD();
        assertArrayEquals(new char[] { 'c', 'e', 'g' }, diag);
    }

    @Test
    public void test_setRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        m.setRU2LD(new char[] { 'x', 'y', 'z' });
        assertEquals('x', m.get(0, 2));
        assertEquals('y', m.get(1, 1));
        assertEquals('z', m.get(2, 0));
    }

    @Test
    public void test_updateRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        m.updateRU2LD(val -> (char) (val + 1));
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
        m.fill(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        assertEquals('a', m.get(0, 0));
        assertEquals('b', m.get(0, 1));
        assertEquals('c', m.get(1, 0));
        assertEquals('d', m.get(1, 1));
        assertEquals('\0', m.get(2, 2));
    }

    @Test
    public void test_fill_arrayWithPosition() {
        CharMatrix m = CharMatrix.of(new char[4][4]);
        m.fill(1, 1, new char[][] { { 'a', 'b' }, { 'c', 'd' } });
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
    public void test_copy_rowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        CharMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals('c', copy.get(0, 0));
        assertEquals('d', copy.get(0, 1));
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
    public void test_flipH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix flipped = m.flipH();
        assertEquals('b', flipped.get(0, 0));
        assertEquals('a', flipped.get(0, 1));
    }

    @Test
    public void test_flipV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix flipped = m.flipV();
        assertEquals('c', flipped.get(0, 0));
        assertEquals('d', flipped.get(0, 1));
    }

    @Test
    public void test_reverseH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.reverseH();
        assertEquals('b', m.get(0, 0));
        assertEquals('a', m.get(0, 1));
    }

    @Test
    public void test_reverseV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.reverseV();
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
    public void test_repelem() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals('a', repeated.get(0, 0));
        assertEquals('a', repeated.get(0, 1));
        assertEquals('a', repeated.get(1, 0));
        assertEquals('a', repeated.get(1, 1));
    }

    @Test
    public void test_repmat() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix tiled = m.repmat(2, 2);
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
    public void test_flatOp() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        m.flatOp(arr -> {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (char) (arr[i] + 1);
            }
        });
        assertEquals('b', m.get(0, 0));
        assertEquals('c', m.get(0, 1));
    }

    // ============ Stack Tests ============

    @Test
    public void test_vstack() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' } });
        CharMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals('a', stacked.get(0, 0));
        assertEquals('c', stacked.get(1, 0));
    }

    @Test
    public void test_vstack_differentCols() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd', 'e' } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a' }, { 'c' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'b' }, { 'd' } });
        CharMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals('a', stacked.get(0, 0));
        assertEquals('b', stacked.get(0, 1));
    }

    @Test
    public void test_hstack_differentRows() {
        CharMatrix m1 = CharMatrix.of(new char[][] { { 'a', 'b' } });
        CharMatrix m2 = CharMatrix.of(new char[][] { { 'c', 'd' }, { 'e', 'f' } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
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
        assertEquals((int) 'a', intMatrix.get(0, 0));
        assertEquals((int) 'b', intMatrix.get(0, 1));
    }

    @Test
    public void test_toLongMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        LongMatrix longMatrix = m.toLongMatrix();
        assertEquals((long) 'a', longMatrix.get(0, 0));
        assertEquals((long) 'b', longMatrix.get(0, 1));
    }

    @Test
    public void test_toFloatMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals((float) 'a', floatMatrix.get(0, 0));
        assertEquals((float) 'b', floatMatrix.get(0, 1));
    }

    @Test
    public void test_toDoubleMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals((double) 'a', doubleMatrix.get(0, 0));
        assertEquals((double) 'b', doubleMatrix.get(0, 1));
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
    public void test_streamLU2RD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diag = m.streamLU2RD().toArray();
        assertArrayEquals(new char[] { 'a', 'e', 'i' }, diag);
    }

    @Test
    public void test_streamRU2LD() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } });
        char[] diag = m.streamRU2LD().toArray();
        assertArrayEquals(new char[] { 'c', 'e', 'g' }, diag);
    }

    @Test
    public void test_streamH() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        char[] elements = m.streamH().toArray();
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, elements);
    }

    @Test
    public void test_streamH_singleRow() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        char[] row = m.streamH(1).toArray();
        assertArrayEquals(new char[] { 'c', 'd' }, row);
    }

    @Test
    public void test_streamH_rowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        char[] elements = m.streamH(1, 3).toArray();
        assertArrayEquals(new char[] { 'c', 'd', 'e', 'f' }, elements);
    }

    @Test
    public void test_streamV() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        char[] elements = m.streamV().toArray();
        assertArrayEquals(new char[] { 'a', 'c', 'b', 'd' }, elements);
    }

    @Test
    public void test_streamV_singleColumn() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        char[] col = m.streamV(0).toArray();
        assertArrayEquals(new char[] { 'a', 'c' }, col);
    }

    @Test
    public void test_streamV_columnRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        char[] elements = m.streamV(1, 3).toArray();
        assertArrayEquals(new char[] { 'b', 'e', 'c', 'f' }, elements);
    }

    @Test
    public void test_streamR() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<char[]> rows = m.streamR().map(CharStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new char[] { 'a', 'b' }, rows.get(0));
    }

    @Test
    public void test_streamR_rowRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' }, { 'e', 'f' } });
        List<char[]> rows = m.streamR(1, 3).map(CharStream::toArray).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void test_streamC() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        List<char[]> columnCount = m.streamC().map(CharStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new char[] { 'a', 'c' }, columnCount.get(0));
    }

    @Test
    public void test_streamC_columnRange() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } });
        List<char[]> columnCount = m.streamC(0, 2).map(CharStream::toArray).toList();
        assertEquals(2, columnCount.size());
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend_twoParams() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals('a', extended.get(0, 0));
        assertEquals('\0', extended.get(2, 2));
    }

    @Test
    public void test_extend_twoParamsWithDefault() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix extended = m.extend(3, 3, 'x');
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
