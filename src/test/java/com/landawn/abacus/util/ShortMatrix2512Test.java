package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalShort;
import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.util.stream.Stream;

@Tag("2512")
public class ShortMatrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_withValidArray() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_constructor_withNullArray() {
        ShortMatrix m = new ShortMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withEmptyArray() {
        ShortMatrix m = new ShortMatrix(new short[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void test_constructor_withSingleElement() {
        ShortMatrix m = new ShortMatrix(new short[][] { { 42 } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_empty_returnsEmptyMatrix() {
        ShortMatrix empty = ShortMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
        assertSame(ShortMatrix.empty(), ShortMatrix.empty());
    }

    @Test
    public void test_of_withValidArray() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = ShortMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(6, m.get(1, 2));
    }

    @Test
    public void test_of_withNullArray() {
        ShortMatrix m = ShortMatrix.of((short[][]) null);
        assertSame(ShortMatrix.empty(), m);
    }

    @Test
    public void test_of_withEmptyArray() {
        ShortMatrix m = ShortMatrix.of(new short[0][0]);
        assertSame(ShortMatrix.empty(), m);
    }

    @Test
    public void test_random_createsRandomMatrix() {
        ShortMatrix m = ShortMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
    }

    @Test
    public void test_repeat_createsRepeatedValues() {
        ShortMatrix m = ShortMatrix.repeat((short) 7, 4);
        assertEquals(1, m.rowCount());
        assertEquals(4, m.columnCount());
        for (int i = 0; i < 4; i++) {
            assertEquals(7, m.get(0, i));
        }
    }

    @Test
    public void test_range_withTwoParameters() {
        ShortMatrix m = ShortMatrix.range((short) 1, (short) 5);
        assertEquals(1, m.rowCount());
        assertEquals(4, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(0, 3));
    }

    @Test
    public void test_range_withThreeParameters() {
        ShortMatrix m = ShortMatrix.range((short) 0, (short) 10, (short) 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals(0, m.get(0, 0));
        assertEquals(8, m.get(0, 4));
    }

    @Test
    public void test_rangeClosed_withTwoParameters() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 1, (short) 4);
        assertEquals(1, m.rowCount());
        assertEquals(4, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(0, 3));
    }

    @Test
    public void test_rangeClosed_withThreeParameters() {
        ShortMatrix m = ShortMatrix.rangeClosed((short) 1, (short) 9, (short) 2);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(9, m.get(0, 4));
    }

    @Test
    public void test_diagonalLU2RD_createsMainDiagonal() {
        short[] diag = { 1, 2, 3 };
        ShortMatrix m = ShortMatrix.diagonalLU2RD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
    }

    @Test
    public void test_diagonalRU2LD_createsAntiDiagonal() {
        short[] diag = { 1, 2, 3 };
        ShortMatrix m = ShortMatrix.diagonalRU2LD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
    }

    @Test
    public void test_diagonal_withBothDiagonals() {
        short[] lu = { 1, 2, 3 };
        short[] ru = { 4, 5, 6 };
        ShortMatrix m = ShortMatrix.diagonal(lu, ru);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void test_diagonal_withDifferentLengths_throwsException() {
        short[] lu = { 1, 2 };
        short[] ru = { 3, 4, 5 };
        assertThrows(IllegalArgumentException.class, () -> ShortMatrix.diagonal(lu, ru));
    }

    @Test
    public void test_diagonal_withBothNull_returnsEmpty() {
        ShortMatrix m = ShortMatrix.diagonal(null, null);
        assertSame(ShortMatrix.empty(), m);
    }

    @Test
    public void test_unbox_convertsBoxedMatrix() {
        Short[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Short> boxedMatrix = Matrix.of(boxed);
        ShortMatrix m = ShortMatrix.unbox(boxedMatrix);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    // ============ Component Type Test ============

    @Test
    public void test_componentType_returnsShortClass() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1 } });
        assertEquals(short.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void test_get_withIndices() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        assertEquals(1, m.get(0, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(1, 2));
    }

    @Test
    public void test_get_withPoint() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        Point p = Point.of(1, 0);
        assertEquals(3, m.get(p));
    }

    @Test
    public void test_set_withIndices() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.set(0, 1, (short) 99);
        assertEquals(99, m.get(0, 1));
    }

    @Test
    public void test_set_withPoint() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        Point p = Point.of(1, 1);
        m.set(p, (short) 88);
        assertEquals(88, m.get(p));
    }

    // ============ Directional Navigation Tests ============

    @Test
    public void test_upOf_withElementAbove() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.upOf(1, 0);
        assertTrue(result.isPresent());
        assertEquals(1, result.getAsShort());
    }

    @Test
    public void test_upOf_atTopRow_returnsEmpty() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.upOf(0, 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_downOf_withElementBelow() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.downOf(0, 1);
        assertTrue(result.isPresent());
        assertEquals(4, result.getAsShort());
    }

    @Test
    public void test_downOf_atBottomRow_returnsEmpty() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.downOf(1, 1);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_leftOf_withElementToLeft() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.leftOf(0, 1);
        assertTrue(result.isPresent());
        assertEquals(1, result.getAsShort());
    }

    @Test
    public void test_leftOf_atLeftColumn_returnsEmpty() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.leftOf(1, 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_rightOf_withElementToRight() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.rightOf(1, 0);
        assertTrue(result.isPresent());
        assertEquals(4, result.getAsShort());
    }

    @Test
    public void test_rightOf_atRightColumn_returnsEmpty() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        OptionalShort result = m.rightOf(0, 1);
        assertFalse(result.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void test_row_returnsCorrectRow() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] row = m.row(1);
        assertArrayEquals(new short[] { 4, 5, 6 }, row);
    }

    @Test
    public void test_row_outOfBounds_throwsException() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column_returnsCorrectColumn() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] col = m.column(1);
        assertArrayEquals(new short[] { 2, 5 }, col);
    }

    @Test
    public void test_column_outOfBounds_throwsException() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow_updatesRow() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.setRow(0, new short[] { 9, 8 });
        assertEquals(9, m.get(0, 0));
        assertEquals(8, m.get(0, 1));
    }

    @Test
    public void test_setRow_wrongLength_throwsException() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new short[] { 1, 2, 3 }));
    }

    @Test
    public void test_setColumn_updatesColumn() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.setColumn(1, new short[] { 7, 8 });
        assertEquals(7, m.get(0, 1));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void test_setColumn_wrongLength_throwsException() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new short[] { 1, 2, 3 }));
    }

    @Test
    public void test_updateRow_appliesFunction() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.updateRow(0, x -> (short) (x * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
    }

    @Test
    public void test_updateColumn_appliesFunction() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.updateColumn(0, x -> (short) (x + 10));
        assertEquals(11, m.get(0, 0));
        assertEquals(13, m.get(1, 0));
        assertEquals(2, m.get(0, 1));
    }

    // ============ Diagonal Tests ============

    @Test
    public void test_getLU2RD_returnsMainDiagonal() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] diag = m.getLU2RD();
        assertArrayEquals(new short[] { 1, 5, 9 }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare_throwsException() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD_setsMainDiagonal() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.setLU2RD(new short[] { 11, 22, 33 });
        assertEquals(11, m.get(0, 0));
        assertEquals(22, m.get(1, 1));
        assertEquals(33, m.get(2, 2));
    }

    @Test
    public void test_setLU2RD_wrongLength_throwsException() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new short[] { 1, 2, 3 }));
    }

    @Test
    public void test_updateLU2RD_appliesFunction() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.updateLU2RD(x -> (short) (x * 10));
        assertEquals(10, m.get(0, 0));
        assertEquals(50, m.get(1, 1));
        assertEquals(90, m.get(2, 2));
    }

    @Test
    public void test_getRU2LD_returnsAntiDiagonal() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] diag = m.getRU2LD();
        assertArrayEquals(new short[] { 3, 5, 7 }, diag);
    }

    @Test
    public void test_setRU2LD_setsAntiDiagonal() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.setRU2LD(new short[] { 11, 22, 33 });
        assertEquals(11, m.get(0, 2));
        assertEquals(22, m.get(1, 1));
        assertEquals(33, m.get(2, 0));
    }

    @Test
    public void test_updateRU2LD_appliesFunction() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.updateRU2LD(x -> (short) (x + 100));
        assertEquals(103, m.get(0, 2));
        assertEquals(105, m.get(1, 1));
        assertEquals(107, m.get(2, 0));
    }

    // ============ Update/Replace Tests ============

    @Test
    public void test_updateAll_withUnaryOperator() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.updateAll(x -> (short) (x * 2));
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void test_updateAll_withBiFunction() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.updateAll((i, j) -> (short) (i * 10 + j));
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void test_replaceIf_withPredicate() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.replaceIf(x -> x > 3, (short) 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(99, m.get(1, 0));
        assertEquals(99, m.get(1, 2));
    }

    @Test
    public void test_replaceIf_withBiPredicate() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.replaceIf((i, j) -> i == j, (short) 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(0, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void test_map_createsNewMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix result = m.map(x -> (short) (x * 3));
        assertEquals(3, result.get(0, 0));
        assertEquals(6, result.get(0, 1));
        assertEquals(9, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
        assertEquals(1, m.get(0, 0));   // original unchanged
    }

    @Test
    public void test_mapToObj_createsObjectMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        Matrix<String> result = m.mapToObj(x -> "val" + x, String.class);
        assertEquals("val1", result.get(0, 0));
        assertEquals("val4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_withValue() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.fill((short) 7);
        assertEquals(7, m.get(0, 0));
        assertEquals(7, m.get(0, 1));
        assertEquals(7, m.get(1, 0));
        assertEquals(7, m.get(1, 1));
    }

    @Test
    public void test_fill_withArray() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[][] b = { { 9, 8 }, { 7, 6 } };
        m.fill(b);
        assertEquals(9, m.get(0, 0));
        assertEquals(6, m.get(1, 1));
    }

    @Test
    public void test_fill_withArrayAndOffset() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[][] b = { { 99, 98 } };
        m.fill(1, 1, b);
        assertEquals(1, m.get(0, 0));
        assertEquals(99, m.get(1, 1));
        assertEquals(98, m.get(1, 2));
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy_createsIndependentCopy() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix copy = m.copy();
        assertEquals(m.rowCount(), copy.rowCount());
        assertEquals(m.columnCount(), copy.columnCount());
        assertEquals(1, copy.get(0, 0));

        copy.set(0, 0, (short) 99);
        assertEquals(1, m.get(0, 0));   // original unchanged
    }

    @Test
    public void test_copy_withRowRange() {
        short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(3, copy.get(0, 0));
        assertEquals(6, copy.get(1, 1));
    }

    @Test
    public void test_copy_withFullRange() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix copy = m.copy(0, 2, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(2, copy.get(0, 0));
        assertEquals(6, copy.get(1, 1));
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend_increasesSize() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(0, extended.get(2, 2));
    }

    @Test
    public void test_extend_withDefaultValue() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix extended = m.extend(3, 3, (short) 99);
        assertEquals(99, extended.get(2, 2));
    }

    @Test
    public void test_extend_withDirections() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1, extended.get(1, 1));
    }

    @Test
    public void test_extend_withDirectionsAndDefault() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix extended = m.extend(1, 1, 1, 1, (short) 88);
        assertEquals(88, extended.get(0, 0));
        assertEquals(1, extended.get(1, 1));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void test_reverseH_reversesHorizontally() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(1, m.get(0, 2));
        assertEquals(6, m.get(1, 0));
    }

    @Test
    public void test_reverseV_reversesVertically() {
        short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.reverseV();
        assertEquals(5, m.get(0, 0));
        assertEquals(6, m.get(0, 1));
        assertEquals(1, m.get(2, 0));
    }

    @Test
    public void test_flipH_createsNewHorizontallyFlipped() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(1, m.get(0, 0));   // original unchanged
    }

    @Test
    public void test_flipV_createsNewVerticallyFlipped() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix flipped = m.flipV();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(1, m.get(0, 0));   // original unchanged
    }

    // ============ Rotation Tests ============

    @Test
    public void test_rotate90_rotatesClockwise() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.println();
        ShortMatrix rotated = m.rotate90();
        rotated.println();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void test_rotate180_rotates180Degrees() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix rotated = m.rotate180();
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void test_rotate270_rotatesCounterClockwise() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        m.println();
        ShortMatrix rotated = m.rotate270();
        rotated.println();
        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    // ============ Transpose Test ============

    @Test
    public void test_transpose_swapsRowsAndColumns() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(6, transposed.get(2, 1));
    }

    // ============ Reshape Test ============

    @Test
    public void test_reshape_changesShape() {
        short[][] arr = { { 1, 2, 3, 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix reshaped = m.reshape(2, 3);
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(6, reshaped.get(1, 2));
    }

    @Test
    public void test_reshape_incompatibleSize_fillsWithZeros() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        ShortMatrix reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(0, reshaped.get(1, 1));   // filled with zero
    }

    // ============ Repelem Test ============

    @Test
    public void test_repelem_repeatsElements() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(1, 1));
        assertEquals(4, repeated.get(2, 2));
    }

    // ============ Repmat Test ============

    @Test
    public void test_repmat_repeatsMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortMatrix repeated = m.repmat(2, 2);
        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(2, 2));
        assertEquals(4, repeated.get(3, 3));
    }

    // ============ Flatten Tests ============

    @Test
    public void test_flatten_returnsAllElements() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        ShortList flattened = m.flatten();
        assertEquals(6, flattened.size());
        assertEquals(1, flattened.get(0));
        assertEquals(6, flattened.get(5));
    }

    @Test
    public void test_flatOp_appliesOperationToEachRow() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        final int[] sum = { 0 };
        m.flatOp(row -> {
            for (short val : row) {
                sum[0] += val;
            }
        });
        assertEquals(10, sum[0]);
    }

    // ============ Stack Tests ============

    @Test
    public void test_vstack_stacksVertically() {
        short[][] arr1 = { { 1, 2 } };
        short[][] arr2 = { { 3, 4 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix stacked = m1.vstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(3, stacked.get(1, 0));
    }

    @Test
    public void test_vstack_incompatibleColumns_throwsException() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3, 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack_stacksHorizontally() {
        short[][] arr1 = { { 1 }, { 2 } };
        short[][] arr2 = { { 3 }, { 4 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(3, stacked.get(0, 1));
    }

    @Test
    public void test_hstack_incompatibleRows_throwsException() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 2 }, { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Tests ============

    @Test
    public void test_add_addsMatrices() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 5, 6 }, { 7, 8 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix sum = m1.add(m2);
        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void test_add_incompatibleSize_throwsException() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void test_subtract_subtractsMatrices() {
        short[][] arr1 = { { 5, 6 }, { 7, 8 } };
        short[][] arr2 = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix diff = m1.subtract(m2);
        assertEquals(4, diff.get(0, 0));
        assertEquals(4, diff.get(0, 1));
        assertEquals(4, diff.get(1, 1));
    }

    @Test
    public void test_multiply_multipliesMatrices() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 2, 0 }, { 1, 2 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix product = m1.multiply(m2);
        assertEquals(4, product.get(0, 0));
        assertEquals(4, product.get(0, 1));
        assertEquals(10, product.get(1, 0));
        assertEquals(8, product.get(1, 1));
    }

    @Test
    public void test_multiply_incompatibleSize_throwsException() {
        ShortMatrix m1 = ShortMatrix.of(new short[][] { { 1, 2, 3 } });
        ShortMatrix m2 = ShortMatrix.of(new short[][] { { 4, 5 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    // ============ Conversion Tests ============

    @Test
    public void test_boxed_convertsToBoxedMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        Matrix<Short> boxed = m.boxed();
        assertEquals(Short.valueOf((short) 1), boxed.get(0, 0));
        assertEquals(Short.valueOf((short) 4), boxed.get(1, 1));
    }

    @Test
    public void test_toIntMatrix_convertsToIntMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        IntMatrix intMatrix = m.toIntMatrix();
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void test_toLongMatrix_convertsToLongMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        LongMatrix longMatrix = m.toLongMatrix();
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void test_toFloatMatrix_convertsToFloatMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        FloatMatrix floatMatrix = m.toFloatMatrix();
        assertEquals(1.0f, floatMatrix.get(0, 0));
        assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    @Test
    public void test_toDoubleMatrix_convertsToDoubleMatrix() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(1.0, doubleMatrix.get(0, 0));
        assertEquals(4.0, doubleMatrix.get(1, 1));
    }

    // ============ Zip Tests ============

    @Test
    public void test_zipWith_withTwoMatrices() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 5, 6 }, { 7, 8 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix result = m1.zipWith(m2, (a, b) -> (short) (a + b));
        assertEquals(6, result.get(0, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void test_zipWith_withThreeMatrices() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 5, 6 }, { 7, 8 } };
        short[][] arr3 = { { 9, 10 }, { 11, 12 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        ShortMatrix m3 = new ShortMatrix(arr3);
        ShortMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (short) (a + b + c));
        assertEquals(15, result.get(0, 0));
        assertEquals(24, result.get(1, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamLU2RD_streamsDiagonal() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamLU2RD().toArray();
        assertArrayEquals(new short[] { 1, 5, 9 }, result);
    }

    @Test
    public void test_streamRU2LD_streamsAntiDiagonal() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamRU2LD().toArray();
        assertArrayEquals(new short[] { 3, 5, 7 }, result);
    }

    @Test
    public void test_streamH_streamsAllElements() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamH().toArray();
        assertArrayEquals(new short[] { 1, 2, 3, 4 }, result);
    }

    @Test
    public void test_streamH_withRowIndex() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamH(1).toArray();
        assertArrayEquals(new short[] { 4, 5, 6 }, result);
    }

    @Test
    public void test_streamH_withRowRange() {
        short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamH(1, 3).toArray();
        assertArrayEquals(new short[] { 3, 4, 5, 6 }, result);
    }

    @Test
    public void test_streamV_streamsAllElementsVertically() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamV().toArray();
        assertArrayEquals(new short[] { 1, 3, 2, 4 }, result);
    }

    @Test
    public void test_streamV_withColumnIndex() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamV(1).toArray();
        assertArrayEquals(new short[] { 2, 5 }, result);
    }

    @Test
    public void test_streamV_withColumnRange() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        short[] result = m.streamV(1, 3).toArray();
        assertArrayEquals(new short[] { 2, 5, 3, 6 }, result);
    }

    @Test
    public void test_streamR_streamsRows() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        Stream<ShortStream> streams = m.streamR();
        assertEquals(2, streams.count());
    }

    @Test
    public void test_streamR_withRange() {
        short[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        Stream<ShortStream> streams = m.streamR(1, 3);
        assertEquals(2, streams.count());
    }

    @Test
    public void test_streamC_streamsColumns() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        Stream<ShortStream> streams = m.streamC();
        assertEquals(2, streams.count());
    }

    @Test
    public void test_streamC_withRange() {
        short[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        ShortMatrix m = new ShortMatrix(arr);
        Stream<ShortStream> streams = m.streamC(1, 3);
        assertEquals(2, streams.count());
    }

    // ============ ForEach Test ============

    @Test
    public void test_forEach_iteratesAllElements() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        final int[] sum = { 0 };
        m.forEach(val -> sum[0] += val);
        assertEquals(10, sum[0]);
    }

    // ============ Utility Tests ============

    @Test
    public void test_println_returnsString() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void test_hashCode_consistentWithEquals() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void test_equals_sameContent() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        assertEquals(m1, m2);
    }

    @Test
    public void test_equals_differentContent() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 1, 2 }, { 3, 5 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_equals_differentDimensions() {
        short[][] arr1 = { { 1, 2 }, { 3, 4 } };
        short[][] arr2 = { { 1, 2, 3 } };
        ShortMatrix m1 = new ShortMatrix(arr1);
        ShortMatrix m2 = new ShortMatrix(arr2);
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_toString_returnsStringRepresentation() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        ShortMatrix m = new ShortMatrix(arr);
        String result = m.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("4"));
    }
}
