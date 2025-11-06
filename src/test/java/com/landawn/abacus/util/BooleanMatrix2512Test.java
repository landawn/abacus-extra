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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.Stream;

@Tag("2512")
public class BooleanMatrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_validArray() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix m = new BooleanMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
    }

    @Test
    public void test_constructor_nullArray() {
        BooleanMatrix m = new BooleanMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_emptyArray() {
        BooleanMatrix m = new BooleanMatrix(new boolean[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_singleElement() {
        BooleanMatrix m = new BooleanMatrix(new boolean[][] { { true } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertTrue(m.get(0, 0));
    }

    @Test
    public void test_constructor_nonSquare() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        BooleanMatrix m = new BooleanMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(3, m.cols);
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_empty() {
        BooleanMatrix empty = BooleanMatrix.empty();
        assertTrue(empty.isEmpty());
        assertSame(BooleanMatrix.empty(), BooleanMatrix.empty());
    }

    @Test
    public void test_of_validArray() {
        boolean[][] arr = { { true, false }, { false, true } };
        BooleanMatrix m = BooleanMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
    }

    @Test
    public void test_of_nullArray() {
        BooleanMatrix m = BooleanMatrix.of((boolean[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_singleRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[] { true, false, true });
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
    }

    @Test
    public void test_random() {
        BooleanMatrix m = BooleanMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    @Test
    public void test_random_zeroLength() {
        BooleanMatrix m = BooleanMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void test_repeat() {
        BooleanMatrix m = BooleanMatrix.repeat(true, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertTrue(m.get(0, i));
        }
    }

    @Test
    public void test_repeat_false() {
        BooleanMatrix m = BooleanMatrix.repeat(false, 3);
        for (int i = 0; i < 3; i++) {
            assertFalse(m.get(0, i));
        }
    }

    @Test
    public void test_diagonalLU2RD() {
        BooleanMatrix m = BooleanMatrix.diagonalLU2RD(new boolean[] { true, false, true });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 2));
        assertFalse(m.get(0, 1));
    }

    @Test
    public void test_diagonalLU2RD_empty() {
        BooleanMatrix m = BooleanMatrix.diagonalLU2RD(new boolean[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        BooleanMatrix m = BooleanMatrix.diagonalRU2LD(new boolean[] { true, false, true });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 2));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 0));
    }

    @Test
    public void test_diagonal_both() {
        BooleanMatrix m = BooleanMatrix.diagonal(new boolean[] { true, true, true }, new boolean[] { false, false, false });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertTrue(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(0, 2));
    }

    @Test
    public void test_diagonal_differentLengths() {
        assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.diagonal(new boolean[] { true, true }, new boolean[] { false, false, false }));
    }

    @Test
    public void test_unbox() {
        Matrix<Boolean> boxed = Matrix.of(new Boolean[][] { { true, false }, { null, true } });
        BooleanMatrix primitive = BooleanMatrix.unbox(boxed);
        assertTrue(primitive.get(0, 0));
        assertFalse(primitive.get(0, 1));
        assertFalse(primitive.get(1, 0)); // null becomes false
        assertTrue(primitive.get(1, 1));
    }

    // ============ Element Access Tests ============

    @Test
    public void test_componentType() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertEquals(boolean.class, m.componentType());
    }

    @Test
    public void test_get_byIndices() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void test_get_byPoint() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertTrue(m.get(Point.of(0, 0)));
        assertFalse(m.get(Point.of(0, 1)));
    }

    @Test
    public void test_set_byIndices() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.set(0, 1, true);
        assertTrue(m.get(0, 1));
    }

    @Test
    public void test_set_byPoint() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.set(Point.of(1, 0), true);
        assertTrue(m.get(1, 0));
    }

    // ============ Neighbor Access Tests ============

    @Test
    public void test_upOf_exists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertTrue(up.get());
    }

    @Test
    public void test_upOf_notExists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf_exists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertFalse(down.get());
    }

    @Test
    public void test_downOf_notExists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf_exists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertTrue(left.get());
    }

    @Test
    public void test_leftOf_notExists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf_exists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertFalse(right.get());
    }

    @Test
    public void test_rightOf_notExists() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        OptionalBoolean right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void test_row() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        boolean[] row = m.row(0);
        assertArrayEquals(new boolean[] { true, false, true }, row);
    }

    @Test
    public void test_row_invalidIndex() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        boolean[] col = m.column(0);
        assertArrayEquals(new boolean[] { true, false }, col);
    }

    @Test
    public void test_column_invalidIndex() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.setRow(0, new boolean[] { false, false });
        assertFalse(m.get(0, 0));
        assertFalse(m.get(0, 1));
    }

    @Test
    public void test_setRow_invalidLength() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new boolean[] { true }));
    }

    @Test
    public void test_setColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.setColumn(0, new boolean[] { false, false });
        assertFalse(m.get(0, 0));
        assertFalse(m.get(1, 0));
    }

    @Test
    public void test_setColumn_invalidLength() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new boolean[] { true }));
    }

    @Test
    public void test_updateRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.updateRow(0, val -> !val);
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
    }

    @Test
    public void test_updateColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.updateColumn(0, val -> !val);
        assertFalse(m.get(0, 0));
        assertTrue(m.get(1, 0));
    }

    // ============ Diagonal Access Tests ============

    @Test
    public void test_getLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
        boolean[] diag = m.getLU2RD();
        assertArrayEquals(new boolean[] { true, true, true }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, false } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
        m.setLU2RD(new boolean[] { false, false, false });
        assertFalse(m.get(0, 0));
        assertFalse(m.get(1, 1));
        assertFalse(m.get(2, 2));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new boolean[] { true }));
    }

    @Test
    public void test_updateLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, false, false }, { false, false, false } });
        m.updateLU2RD(val -> !val);
        assertFalse(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(2, 2));
    }

    @Test
    public void test_getRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
        boolean[] diag = m.getRU2LD();
        assertArrayEquals(new boolean[] { true, true, true }, diag);
    }

    @Test
    public void test_setRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        m.setRU2LD(new boolean[] { false, false, false });
        assertFalse(m.get(0, 2));
        assertFalse(m.get(1, 1));
        assertFalse(m.get(2, 0));
    }

    @Test
    public void test_updateRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
        m.updateRU2LD(val -> !val);
        assertTrue(m.get(0, 2));
        assertFalse(m.get(1, 1));
        assertTrue(m.get(2, 0));
    }

    // ============ Update Tests ============

    @Test
    public void test_updateAll_unaryOperator() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.updateAll(val -> !val);
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertFalse(m.get(1, 1));
    }

    @Test
    public void test_updateAll_biFunction() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false }, { false, false } });
        m.updateAll((i, j) -> i == j);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertFalse(m.get(1, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void test_replaceIf_predicate() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.replaceIf(val -> val == false, true);
        assertTrue(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void test_replaceIf_biPredicate() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
        m.replaceIf((i, j) -> i == j, true);
        assertTrue(m.get(0, 0));
        assertFalse(m.get(0, 1));
        assertTrue(m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void test_map() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix inverted = m.map(val -> !val);
        assertFalse(inverted.get(0, 0));
        assertTrue(inverted.get(0, 1));
        // Original unchanged
        assertTrue(m.get(0, 0));
    }

    @Test
    public void test_mapToObj() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        Matrix<String> stringMatrix = m.mapToObj(val -> val ? "YES" : "NO", String.class);
        assertEquals("YES", stringMatrix.get(0, 0));
        assertEquals("NO", stringMatrix.get(0, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_singleValue() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.fill(true);
        assertTrue(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertTrue(m.get(1, 1));
    }

    @Test
    public void test_fill_array() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
        m.fill(new boolean[][] { { true, true }, { true, true } });
        assertTrue(m.get(0, 0));
        assertTrue(m.get(0, 1));
        assertTrue(m.get(1, 0));
        assertTrue(m.get(1, 1));
        assertFalse(m.get(2, 2));
    }

    @Test
    public void test_fill_arrayWithPosition() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[4][4]);
        m.fill(1, 1, new boolean[][] { { true, true }, { true, true } });
        assertFalse(m.get(0, 0));
        assertTrue(m.get(1, 1));
        assertTrue(m.get(1, 2));
        assertTrue(m.get(2, 1));
        assertTrue(m.get(2, 2));
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy() {
        BooleanMatrix original = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix copy = original.copy();
        assertEquals(original.rows, copy.rows);
        assertEquals(original.cols, copy.cols);
        assertTrue(copy.get(0, 0));
        // Modify copy
        copy.set(0, 0, false);
        assertTrue(original.get(0, 0)); // Original unchanged
    }

    @Test
    public void test_copy_rowRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        BooleanMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertFalse(copy.get(0, 0));
        assertTrue(copy.get(0, 1));
    }

    @Test
    public void test_copy_fullRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, true, true } });
        BooleanMatrix copy = m.copy(0, 2, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertFalse(copy.get(0, 0)); // From (0,1)
        assertTrue(copy.get(0, 1)); // From (0,2)
    }

    // ============ Transformation Tests ============

    @Test
    public void test_rotate90() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertFalse(rotated.get(0, 0));
        assertTrue(rotated.get(0, 1));
    }

    @Test
    public void test_rotate180() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix rotated = m.rotate180();
        assertTrue(rotated.get(0, 0));
        assertFalse(rotated.get(0, 1));
        assertFalse(rotated.get(1, 0));
        assertTrue(rotated.get(1, 1));
    }

    @Test
    public void test_rotate270() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
    }

    @Test
    public void test_transpose() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        BooleanMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertTrue(transposed.get(0, 0));
        assertFalse(transposed.get(0, 1));
        assertFalse(transposed.get(1, 0));
        assertTrue(transposed.get(1, 1));
    }

    @Test
    public void test_flipH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix flipped = m.flipH();
        assertFalse(flipped.get(0, 0));
        assertTrue(flipped.get(0, 1));
    }

    @Test
    public void test_flipV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix flipped = m.flipV();
        assertFalse(flipped.get(0, 0));
        assertTrue(flipped.get(0, 1));
    }

    @Test
    public void test_reverseH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.reverseH();
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
    }

    @Test
    public void test_reverseV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.reverseV();
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void test_reshape() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        BooleanMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertTrue(reshaped.get(0, 0));
        assertFalse(reshaped.get(0, 1));
    }

    // ============ Repeat Tests ============

    @Test
    public void test_repelem() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertTrue(repeated.get(0, 0));
        assertTrue(repeated.get(0, 1));
        assertTrue(repeated.get(1, 0));
        assertTrue(repeated.get(1, 1));
    }

    @Test
    public void test_repmat() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix tiled = m.repmat(2, 2);
        assertEquals(4, tiled.rows);
        assertEquals(4, tiled.cols);
        assertTrue(tiled.get(0, 0));
        assertFalse(tiled.get(0, 1));
        assertTrue(tiled.get(2, 2));
    }

    // ============ Flatten Tests ============

    @Test
    public void test_flatten() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanList flat = m.flatten();
        assertEquals(4, flat.size());
        assertTrue(flat.get(0));
        assertFalse(flat.get(1));
        assertFalse(flat.get(2));
        assertTrue(flat.get(3));
    }

    @Test
    public void test_flatOp() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        m.flatOp(arr -> {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = !arr[i];
            }
        });
        assertFalse(m.get(0, 0));
        assertTrue(m.get(0, 1));
    }

    // ============ Stack Tests ============

    @Test
    public void test_vstack() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true } });
        BooleanMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(1, 0));
    }

    @Test
    public void test_vstack_differentCols() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true, false } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true }, { false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false }, { true } });
        BooleanMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertTrue(stacked.get(0, 0));
        assertFalse(stacked.get(0, 1));
    }

    @Test
    public void test_hstack_differentRows() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Boxed Test ============

    @Test
    public void test_boxed() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        Matrix<Boolean> boxed = m.boxed();
        assertEquals(Boolean.TRUE, boxed.get(0, 0));
        assertEquals(Boolean.FALSE, boxed.get(0, 1));
    }

    // ============ ZipWith Tests ============

    @Test
    public void test_zipWith_twoMatrices() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { false, true }, { true, false } });
        BooleanMatrix result = m1.zipWith(m2, (a, b) -> a && b);
        assertFalse(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertFalse(result.get(1, 1));
    }

    @Test
    public void test_zipWith_threeMatrices() {
        BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] { { true, true }, { false, false } });
        BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] { { true, false }, { true, false } });
        BooleanMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a || b || c);
        assertTrue(result.get(0, 0));
        assertTrue(result.get(0, 1));
        assertTrue(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamLU2RD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } });
        List<Boolean> diag = m.streamLU2RD().toList();
        assertEquals(3, diag.size());
        assertTrue(diag.get(0));
        assertTrue(diag.get(1));
        assertTrue(diag.get(2));
    }

    @Test
    public void test_streamRU2LD() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } });
        List<Boolean> diag = m.streamRU2LD().toList();
        assertEquals(3, diag.size());
        assertTrue(diag.get(0));
        assertTrue(diag.get(1));
        assertTrue(diag.get(2));
    }

    @Test
    public void test_streamH() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Boolean> elements = m.streamH().toList();
        assertEquals(4, elements.size());
        assertTrue(elements.get(0));
        assertFalse(elements.get(1));
    }

    @Test
    public void test_streamH_singleRow() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Boolean> row = m.streamH(1).toList();
        assertEquals(2, row.size());
        assertFalse(row.get(0));
        assertTrue(row.get(1));
    }

    @Test
    public void test_streamH_rowRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        List<Boolean> elements = m.streamH(1, 3).toList();
        assertEquals(4, elements.size());
    }

    @Test
    public void test_streamV() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Boolean> elements = m.streamV().toList();
        assertEquals(4, elements.size());
        assertTrue(elements.get(0));
        assertFalse(elements.get(1));
    }

    @Test
    public void test_streamV_singleColumn() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Boolean> col = m.streamV(0).toList();
        assertEquals(2, col.size());
        assertTrue(col.get(0));
        assertFalse(col.get(1));
    }

    @Test
    public void test_streamV_columnRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<Boolean> elements = m.streamV(1, 3).toList();
        assertEquals(4, elements.size());
    }

    @Test
    public void test_streamR() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<List<Boolean>> rows = m.streamR().map(Stream::toList).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).size());
    }

    @Test
    public void test_streamR_rowRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true }, { true, true } });
        List<List<Boolean>> rows = m.streamR(1, 3).map(Stream::toList).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void test_streamC() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<List<Boolean>> cols = m.streamC().map(Stream::toList).toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).size());
    }

    @Test
    public void test_streamC_columnRange() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false } });
        List<List<Boolean>> cols = m.streamC(0, 2).map(Stream::toList).toList();
        assertEquals(2, cols.size());
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend_twoParams() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertTrue(extended.get(0, 0));
        assertFalse(extended.get(2, 2));
    }

    @Test
    public void test_extend_twoParamsWithDefault() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = m.extend(3, 3, true);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertTrue(extended.get(2, 2));
    }

    @Test
    public void test_extend_fourParams() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertTrue(extended.get(1, 1));
    }

    @Test
    public void test_extend_fourParamsWithDefault() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix extended = m.extend(1, 1, 1, 1, true);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertTrue(extended.get(0, 0));
        assertTrue(extended.get(3, 3));
    }
}
