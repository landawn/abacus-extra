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
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteStream;

@Tag("2512")
public class ByteMatrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_validArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
    }

    @Test
    public void test_constructor_nullArray() {
        ByteMatrix m = new ByteMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_emptyArray() {
        ByteMatrix m = new ByteMatrix(new byte[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_singleElement() {
        ByteMatrix m = new ByteMatrix(new byte[][] { { 5 } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(5, m.get(0, 0));
    }

    @Test
    public void test_constructor_nonSquare() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix m = new ByteMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_empty() {
        ByteMatrix empty = ByteMatrix.empty();
        assertTrue(empty.isEmpty());
        assertSame(ByteMatrix.empty(), ByteMatrix.empty());
    }

    @Test
    public void test_of_validArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        ByteMatrix m = ByteMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
    }

    @Test
    public void test_of_nullArray() {
        ByteMatrix m = ByteMatrix.of((byte[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[] { 1, 2, 3 });
        assertEquals(1, m.rowCount());
        assertEquals(3, m.columnCount());
    }

    @Test
    public void test_random() {
        ByteMatrix m = ByteMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
    }

    @Test
    public void test_random_zeroLength() {
        ByteMatrix m = ByteMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void test_repeat() {
        ByteMatrix m = ByteMatrix.repeat(1, 5, (byte) 7);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(7, m.get(0, i));
        }
    }

    @Test
    public void test_range() {
        ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(4, m.get(0, 4));
    }

    @Test
    public void test_range_withBy() {
        ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(8, m.get(0, 4));
    }

    @Test
    public void test_rangeClosed() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 5);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(5, m.get(0, 5));
    }

    @Test
    public void test_rangeClosed_withBy() {
        ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 10, (byte) 2);
        assertEquals(1, m.rowCount());
        assertEquals(6, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(10, m.get(0, 5));
    }

    @Test
    public void test_diagonalLU2RD() {
        ByteMatrix m = ByteMatrix.diagonalLU2RD(new byte[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
    }

    @Test
    public void test_diagonalLU2RD_empty() {
        ByteMatrix m = ByteMatrix.diagonalLU2RD(new byte[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        ByteMatrix m = ByteMatrix.diagonalRU2LD(new byte[] { 1, 2, 3 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
    }

    @Test
    public void test_diagonal_both() {
        ByteMatrix m = ByteMatrix.diagonal(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(0, 2));
    }

    @Test
    public void test_diagonal_differentLengths() {
        assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonal(new byte[] { 1, 2 }, new byte[] { 3, 4, 5 }));
    }

    @Test
    public void test_unbox() {
        Matrix<Byte> boxed = Matrix.of(new Byte[][] { { 1, 2 }, { null, 4 } });
        ByteMatrix primitive = ByteMatrix.unbox(boxed);
        assertEquals(1, primitive.get(0, 0));
        assertEquals(2, primitive.get(0, 1));
        assertEquals(0, primitive.get(1, 0)); // null becomes 0
        assertEquals(4, primitive.get(1, 1));
    }

    // ============ Element Access Tests ============

    @Test
    public void test_componentType() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertEquals(byte.class, m.componentType());
    }

    @Test
    public void test_get_byIndices() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_get_byPoint() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(Point.of(0, 0)));
        assertEquals(4, m.get(Point.of(1, 1)));
    }

    @Test
    public void test_set_byIndices() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.set(0, 1, (byte) 10);
        assertEquals(10, m.get(0, 1));
    }

    @Test
    public void test_set_byPoint() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.set(Point.of(1, 0), (byte) 20);
        assertEquals(20, m.get(1, 0));
    }

    // ============ Neighbor Access Tests ============

    @Test
    public void test_upOf_exists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());
    }

    @Test
    public void test_upOf_notExists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf_exists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());
    }

    @Test
    public void test_downOf_notExists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf_exists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());
    }

    @Test
    public void test_leftOf_notExists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf_exists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());
    }

    @Test
    public void test_rightOf_notExists() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        OptionalByte right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void test_row() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] row = m.row(0);
        assertArrayEquals(new byte[] { 1, 2, 3 }, row);
    }

    @Test
    public void test_row_invalidIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] col = m.column(0);
        assertArrayEquals(new byte[] { 1, 4 }, col);
    }

    @Test
    public void test_column_invalidIndex() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new byte[] { 10, 20 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(0, 1));
    }

    @Test
    public void test_setRow_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1 }));
    }

    @Test
    public void test_setColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.setColumn(0, new byte[] { 10, 20 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 0));
    }

    @Test
    public void test_setColumn_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1 }));
    }

    @Test
    public void test_updateRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, val -> (byte) (val * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
    }

    @Test
    public void test_updateColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, val -> (byte) (val * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(6, m.get(1, 0));
    }

    // ============ Diagonal Access Tests ============

    @Test
    public void test_getLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diag = m.getLU2RD();
        assertArrayEquals(new byte[] { 1, 5, 9 }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setLU2RD(new byte[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new byte[] { 1 }));
    }

    @Test
    public void test_updateLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(val -> (byte) (val * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(10, m.get(1, 1));
        assertEquals(18, m.get(2, 2));
    }

    @Test
    public void test_getRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diag = m.getRU2LD();
        assertArrayEquals(new byte[] { 3, 5, 7 }, diag);
    }

    @Test
    public void test_setRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new byte[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 2));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 0));
    }

    @Test
    public void test_updateRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(val -> (byte) (val * 2));
        assertEquals(6, m.get(0, 2));
        assertEquals(10, m.get(1, 1));
        assertEquals(14, m.get(2, 0));
    }

    // ============ Update Tests ============

    @Test
    public void test_updateAll_unaryOperator() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(val -> (byte) (val * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void test_updateAll_biFunction() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> (byte) (i + j));
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(1, m.get(1, 0));
        assertEquals(2, m.get(1, 1));
    }

    @Test
    public void test_replaceIf_predicate() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf(val -> val < 3, (byte) 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_replaceIf_biPredicate() {
        ByteMatrix m = ByteMatrix.of(new byte[3][3]);
        m.replaceIf((i, j) -> i == j, (byte) 1);
        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(1, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void test_map() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix doubled = m.map(val -> (byte) (val * 2));
        assertEquals(2, doubled.get(0, 0));
        assertEquals(4, doubled.get(0, 1));
        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void test_mapToObj() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> stringMatrix = m.mapToObj(val -> "B" + val, String.class);
        assertEquals("B1", stringMatrix.get(0, 0));
        assertEquals("B2", stringMatrix.get(0, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_singleValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.fill((byte) 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
        assertEquals(0, m.get(1, 1));
    }

    @Test
    public void test_fill_array() {
        ByteMatrix m = ByteMatrix.of(new byte[3][3]);
        m.fill(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
    }

    @Test
    public void test_fill_arrayWithPosition() {
        ByteMatrix m = ByteMatrix.of(new byte[4][4]);
        m.fill(1, 1, new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(1, 1));
        assertEquals(2, m.get(1, 2));
        assertEquals(3, m.get(2, 1));
        assertEquals(4, m.get(2, 2));
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy() {
        ByteMatrix original = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix copy = original.copy();
        assertEquals(original.rowCount(), copy.rowCount());
        assertEquals(original.columnCount(), copy.columnCount());
        assertEquals(1, copy.get(0, 0));
        // Modify copy
        copy.set(0, 0, (byte) 99);
        assertEquals(1, original.get(0, 0)); // Original unchanged
    }

    @Test
    public void test_copy_rowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        ByteMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(3, copy.get(0, 0));
        assertEquals(4, copy.get(0, 1));
    }

    @Test
    public void test_copy_fullRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix copy = m.copy(0, 2, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(2, copy.get(0, 0)); // From (0,1)
        assertEquals(3, copy.get(0, 1)); // From (0,2)
    }

    // ============ Transformation Tests ============

    @Test
    public void test_rotate90() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
    }

    @Test
    public void test_rotate180() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate180();
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void test_rotate270() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
    }

    @Test
    public void test_transpose() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void test_flipH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix flipped = m.flipH();
        assertEquals(2, flipped.get(0, 0));
        assertEquals(1, flipped.get(0, 1));
    }

    @Test
    public void test_flipV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix flipped = m.flipV();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(4, flipped.get(0, 1));
    }

    @Test
    public void test_reverseH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.reverseH();
        assertEquals(2, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
    }

    @Test
    public void test_reverseV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.reverseV();
        assertEquals(3, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void test_reshape() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        ByteMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
    }

    // ============ Repeat Tests ============

    @Test
    public void test_repelem() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(1, 0));
        assertEquals(1, repeated.get(1, 1));
    }

    @Test
    public void test_repmat() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix tiled = m.repmat(2, 2);
        assertEquals(4, tiled.rowCount());
        assertEquals(4, tiled.columnCount());
        assertEquals(1, tiled.get(0, 0));
        assertEquals(2, tiled.get(0, 1));
        assertEquals(1, tiled.get(2, 2));
    }

    // ============ Flatten Tests ============

    @Test
    public void test_flatten() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals(1, flat.get(0));
        assertEquals(2, flat.get(1));
        assertEquals(3, flat.get(2));
        assertEquals(4, flat.get(3));
    }

    @Test
    public void test_flatOp() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.flatOp(arr -> {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (byte) (arr[i] * 2);
            }
        });
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
    }

    // ============ Stack Tests ============

    @Test
    public void test_vstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
        ByteMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(3, stacked.get(1, 0));
    }

    @Test
    public void test_vstack_differentCols() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 4 } });
        ByteMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
    }

    @Test
    public void test_hstack_differentRows() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Tests ============

    @Test
    public void test_add() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = m1.add(m2);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void test_subtract() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix result = m1.subtract(m2);
        assertEquals(4, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(4, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void test_multiply() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 2, 3 }, { 4, 5 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix result = m1.multiply(m2);
        assertEquals(11, result.get(0, 0)); // 2*1 + 3*3 = 11
        assertEquals(16, result.get(0, 1)); // 2*2 + 3*4 = 16
        assertEquals(19, result.get(1, 0)); // 4*1 + 5*3 = 19
        assertEquals(28, result.get(1, 1)); // 4*2 + 5*4 = 28
    }

    // ============ Conversion Tests ============

    @Test
    public void test_boxed() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Byte> boxed = m.boxed();
        assertEquals((byte) 1, boxed.get(0, 0));
        assertEquals((byte) 2, boxed.get(0, 1));
    }

    @Test
    public void test_toIntMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix intMatrix = m.toIntMatrix();
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(2, intMatrix.get(0, 1));
    }

    @Test
    public void test_toLongMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix longMatrix = m.toLongMatrix();
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(2L, longMatrix.get(0, 1));
    }

    @Test
    public void test_toFloatMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(1.0f, floatMatrix.get(0, 0));
        assertEquals(2.0f, floatMatrix.get(0, 1));
    }

    @Test
    public void test_toDoubleMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(1.0, doubleMatrix.get(0, 0));
        assertEquals(2.0, doubleMatrix.get(0, 1));
    }

    // ============ ZipWith Tests ============

    @Test
    public void test_zipWith_twoMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = m1.zipWith(m2, (a, b) -> (byte) (a + b));
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
    }

    @Test
    public void test_zipWith_threeMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 1 }, { 1, 1 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 2, 2 }, { 2, 2 } });
        ByteMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c));
        assertEquals(4, result.get(0, 0));
        assertEquals(5, result.get(0, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamLU2RD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diag = m.streamLU2RD().toArray();
        assertArrayEquals(new byte[] { 1, 5, 9 }, diag);
    }

    @Test
    public void test_streamRU2LD() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        byte[] diag = m.streamRU2LD().toArray();
        assertArrayEquals(new byte[] { 3, 5, 7 }, diag);
    }

    @Test
    public void test_streamH() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        byte[] elements = m.streamH().toArray();
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, elements);
    }

    @Test
    public void test_streamH_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        byte[] row = m.streamH(1).toArray();
        assertArrayEquals(new byte[] { 3, 4 }, row);
    }

    @Test
    public void test_streamH_rowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        byte[] elements = m.streamH(1, 3).toArray();
        assertArrayEquals(new byte[] { 3, 4, 5, 6 }, elements);
    }

    @Test
    public void test_streamV() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        byte[] elements = m.streamV().toArray();
        assertArrayEquals(new byte[] { 1, 3, 2, 4 }, elements);
    }

    @Test
    public void test_streamV_singleColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        byte[] col = m.streamV(0).toArray();
        assertArrayEquals(new byte[] { 1, 3 }, col);
    }

    @Test
    public void test_streamV_columnRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        byte[] elements = m.streamV(1, 3).toArray();
        assertArrayEquals(new byte[] { 2, 5, 3, 6 }, elements);
    }

    @Test
    public void test_streamR() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<byte[]> rows = m.streamR().map(ByteStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new byte[] { 1, 2 }, rows.get(0));
    }

    @Test
    public void test_streamR_rowRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<byte[]> rows = m.streamR(1, 3).map(ByteStream::toArray).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void test_streamC() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        List<byte[]> columnCount = m.streamC().map(ByteStream::toArray).toList();
        assertEquals(2, columnCount.size());
        assertArrayEquals(new byte[] { 1, 3 }, columnCount.get(0));
    }

    @Test
    public void test_streamC_columnRange() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<byte[]> columnCount = m.streamC(0, 2).map(ByteStream::toArray).toList();
        assertEquals(2, columnCount.size());
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend_twoParams() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(0, extended.get(2, 2));
    }

    @Test
    public void test_extend_twoParamsWithDefault() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = m.extend(3, 3, (byte) 9);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(9, extended.get(2, 2));
    }

    @Test
    public void test_extend_fourParams() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1, extended.get(1, 1));
    }

    @Test
    public void test_extend_fourParamsWithDefault() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) 9);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(9, extended.get(0, 0));
        assertEquals(9, extended.get(3, 3));
    }
}
