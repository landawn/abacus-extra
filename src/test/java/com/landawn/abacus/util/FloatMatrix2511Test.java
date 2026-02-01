package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalFloat;
import com.landawn.abacus.util.stream.FloatStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * Comprehensive unit tests for FloatMatrix class covering all public methods.
 */
@Tag("2511")
public class FloatMatrix2511Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = new FloatMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(4.0f, m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        FloatMatrix m = new FloatMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        FloatMatrix m = new FloatMatrix(new float[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        FloatMatrix m = new FloatMatrix(new float[][] { { 42.5f } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42.5f, m.get(0, 0));
    }

    @Test
    public void testConstructor_withLargerMatrix() {
        float[][] arr = { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } };
        FloatMatrix m = new FloatMatrix(arr);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(5.0f, m.get(1, 1));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
        assertSame(FloatMatrix.empty(), FloatMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = FloatMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
    }

    @Test
    public void testOf_withNullArray() {
        FloatMatrix m = FloatMatrix.of((float[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withEmptyArray() {
        FloatMatrix m = FloatMatrix.of(new float[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withEmptyRows() {
        FloatMatrix m = FloatMatrix.of(new float[3][0]);
        assertEquals(3, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testCreateFromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        FloatMatrix m = FloatMatrix.from(ints);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(4.0f, m.get(1, 1));
    }

    @Test
    public void testCreateFromIntArray_withNull() {
        FloatMatrix m = FloatMatrix.from((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withEmpty() {
        FloatMatrix m = FloatMatrix.from(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withJaggedArray() {
        int[][] jagged = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(jagged));
    }

    @Test
    public void testCreateFromIntArray_withNullRow() {
        int[][] nullRow = { { 1, 2 }, null };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(nullRow));
    }

    @Test
    public void testCreateFromIntArray_withNullFirstRow() {
        int[][] nullFirstRow = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(nullFirstRow));
    }

    @Test
    public void testRandom() {
        FloatMatrix m = FloatMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withZeroLength() {
        FloatMatrix m = FloatMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRandom_withLargeLength() {
        FloatMatrix m = FloatMatrix.random(100);
        assertEquals(1, m.rowCount());
        assertEquals(100, m.columnCount());
    }

    @Test
    public void testRepeat() {
        FloatMatrix m = FloatMatrix.repeat(3.14f, 5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14f, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withZeroLength() {
        FloatMatrix m = FloatMatrix.repeat(1.0f, 0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testRepeat_withNegativeValue() {
        FloatMatrix m = FloatMatrix.repeat(-5.5f, 3);
        assertEquals(1, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(-5.5f, m.get(0, 0));
    }

    @Test
    public void testDiagonalLU2RD() {
        FloatMatrix m = FloatMatrix.diagonalLU2RD(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(2.0f, m.get(1, 1));
        assertEquals(3.0f, m.get(2, 2));
        assertEquals(0.0f, m.get(0, 1));
        assertEquals(0.0f, m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_withNull() {
        FloatMatrix m = FloatMatrix.diagonalLU2RD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalLU2RD_withEmptyArray() {
        FloatMatrix m = FloatMatrix.diagonalLU2RD(new float[0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalRU2LD() {
        FloatMatrix m = FloatMatrix.diagonalRU2LD(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 2));
        assertEquals(2.0f, m.get(1, 1));
        assertEquals(3.0f, m.get(2, 0));
        assertEquals(0.0f, m.get(0, 0));
        assertEquals(0.0f, m.get(2, 2));
    }

    @Test
    public void testDiagonalRU2LD_withNull() {
        FloatMatrix m = FloatMatrix.diagonalRU2LD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        FloatMatrix m = FloatMatrix.diagonal(new float[] { 1.0f, 4.0f }, new float[] { 2.0f, 3.0f });
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(2.0f, m.get(0, 1));
        assertEquals(3.0f, m.get(1, 0));
        assertEquals(4.0f, m.get(1, 1));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        FloatMatrix m = FloatMatrix.diagonal(new float[] { 1.0f, 2.0f, 3.0f }, null);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(2.0f, m.get(1, 1));
        assertEquals(3.0f, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        FloatMatrix m = FloatMatrix.diagonal(null, new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 2));
        assertEquals(2.0f, m.get(1, 1));
        assertEquals(3.0f, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothEmpty() {
        FloatMatrix m = FloatMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonal(new float[] { 1.0f, 2.0f }, new float[] { 1.0f, 2.0f, 3.0f }));
    }

    @Test
    public void testDiagonal_singleElement() {
        FloatMatrix m = FloatMatrix.diagonal(new float[] { 5.0f }, new float[] { 7.0f });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(5.0f, m.get(0, 0)); // Main diagonal takes precedence
    }

    @Test
    public void testUnbox() {
        Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m = FloatMatrix.unbox(boxed);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(4.0f, m.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, null }, { null, 4.0f } });
        FloatMatrix m = FloatMatrix.unbox(boxed);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(0.0f, m.get(0, 1));
        assertEquals(0.0f, m.get(1, 0));
        assertEquals(4.0f, m.get(1, 1));
    }

    // ============ Element Access Tests ============

    @Test
    public void testComponentType() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f } });
        assertEquals(float.class, m.componentType());
    }

    @Test
    public void testGet() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(2.0f, m.get(0, 1));
        assertEquals(3.0f, m.get(1, 0));
        assertEquals(4.0f, m.get(1, 1));
    }

    @Test
    public void testGet_withPoint() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(2.0f, m.get(Point.of(0, 1)));
        assertEquals(3.0f, m.get(Point.of(1, 0)));
    }

    @Test
    public void testSet() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(0, 1, 9.0f);
        assertEquals(9.0f, m.get(0, 1));
    }

    @Test
    public void testSet_withPoint() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(Point.of(0, 1), 9.0f);
        assertEquals(9.0f, m.get(0, 1));
    }

    @Test
    public void testSet_negativeValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(0, 0, -99.9f);
        assertEquals(-99.9f, m.get(0, 0));
    }

    // ============ Neighbor Tests ============

    @Test
    public void testUpOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1.0f, up.get());
    }

    @Test
    public void testUpOf_firstRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void testDownOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3.0f, down.get());
    }

    @Test
    public void testDownOf_lastRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void testLeftOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1.0f, left.get());
    }

    @Test
    public void testLeftOf_firstColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testRightOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2.0f, right.get());
    }

    @Test
    public void testRightOf_lastColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void testRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        float[] row = m.row(0);
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, row);
    }

    @Test
    public void testRow_invalidIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        float[] col = m.column(0);
        assertArrayEquals(new float[] { 1.0f, 4.0f }, col);
    }

    @Test
    public void testColumn_invalidIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setRow(0, new float[] { 7.0f, 8.0f });
        assertEquals(7.0f, m.get(0, 0));
        assertEquals(8.0f, m.get(0, 1));
    }

    @Test
    public void testSetRow_wrongLength() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 7.0f }));
    }

    @Test
    public void testSetColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setColumn(0, new float[] { 7.0f, 8.0f });
        assertEquals(7.0f, m.get(0, 0));
        assertEquals(8.0f, m.get(1, 0));
    }

    @Test
    public void testSetColumn_wrongLength() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 7.0f }));
    }

    @Test
    public void testUpdateRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateRow(0, x -> x * 2);
        assertEquals(2.0f, m.get(0, 0));
        assertEquals(4.0f, m.get(0, 1));
    }

    @Test
    public void testUpdateColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateColumn(0, x -> x + 10);
        assertEquals(11.0f, m.get(0, 0));
        assertEquals(13.0f, m.get(1, 0));
    }

    // ============ Diagonal Tests ============

    @Test
    public void testGetLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diagonal = m.getLU2RD();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setLU2RD(new float[] { 9.0f, 8.0f });
        assertEquals(9.0f, m.get(0, 0));
        assertEquals(8.0f, m.get(1, 1));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new float[] { 9.0f, 8.0f }));
    }

    @Test
    public void testSetLU2RD_tooShort() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new float[] { 9.0f }));
    }

    @Test
    public void testUpdateLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateLU2RD(x -> x * 2);
        assertEquals(2.0f, m.get(0, 0));
        assertEquals(8.0f, m.get(1, 1));
    }

    @Test
    public void testGetRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diagonal = m.getRU2LD();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, diagonal);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setRU2LD(new float[] { 9.0f, 8.0f });
        assertEquals(9.0f, m.get(0, 1));
        assertEquals(8.0f, m.get(1, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateRU2LD(x -> x + 10);
        assertEquals(12.0f, m.get(0, 1));
        assertEquals(13.0f, m.get(1, 0));
    }

    // ============ Update/Transform Tests ============

    @Test
    public void testUpdateAll_unaryOperator() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateAll(x -> x * 2);
        assertEquals(2.0f, m.get(0, 0));
        assertEquals(4.0f, m.get(0, 1));
        assertEquals(6.0f, m.get(1, 0));
        assertEquals(8.0f, m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateAll((i, j) -> (float) (i + j));
        assertEquals(0.0f, m.get(0, 0));
        assertEquals(1.0f, m.get(0, 1));
        assertEquals(1.0f, m.get(1, 0));
        assertEquals(2.0f, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_predicate() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.replaceIf(x -> x > 2, 0.0f);
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(2.0f, m.get(0, 1));
        assertEquals(0.0f, m.get(1, 0));
        assertEquals(0.0f, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_biPredicate() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.replaceIf((i, j) -> i == j, 0.0f);
        assertEquals(0.0f, m.get(0, 0));
        assertEquals(2.0f, m.get(0, 1));
        assertEquals(3.0f, m.get(1, 0));
        assertEquals(0.0f, m.get(1, 1));
    }

    @Test
    public void testMap() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m.map(x -> x * 2);
        assertEquals(2.0f, result.get(0, 0));
        assertEquals(4.0f, result.get(0, 1));
        assertEquals(6.0f, result.get(1, 0));
        assertEquals(8.0f, result.get(1, 1));
        assertEquals(1.0f, m.get(0, 0));
    }

    @Test
    public void testMapToObj() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
        assertEquals("1.0", result.get(0, 0));
        assertEquals("4.0", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_singleValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.fill(9.0f);
        assertEquals(9.0f, m.get(0, 0));
        assertEquals(9.0f, m.get(0, 1));
        assertEquals(9.0f, m.get(1, 0));
        assertEquals(9.0f, m.get(1, 1));
    }

    @Test
    public void testFill_array() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.fill(new float[][] { { 7.0f, 8.0f }, { 9.0f, 10.0f } });
        assertEquals(7.0f, m.get(0, 0));
        assertEquals(8.0f, m.get(0, 1));
        assertEquals(9.0f, m.get(1, 0));
        assertEquals(10.0f, m.get(1, 1));
    }

    @Test
    public void testFill_withPosition() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.fill(1, 1, new float[][] { { 11.0f, 12.0f }, { 13.0f, 14.0f } });
        assertEquals(1.0f, m.get(0, 0));
        assertEquals(11.0f, m.get(1, 1));
        assertEquals(12.0f, m.get(1, 2));
        assertEquals(13.0f, m.get(2, 1));
        assertEquals(14.0f, m.get(2, 2));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix copy = m.copy();
        assertEquals(1.0f, copy.get(0, 0));
        assertEquals(4.0f, copy.get(1, 1));
        copy.set(0, 0, 99.0f);
        assertEquals(1.0f, m.get(0, 0));
    }

    @Test
    public void testCopy_rows() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        FloatMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(3.0f, copy.get(0, 0));
        assertEquals(6.0f, copy.get(1, 1));
    }

    @Test
    public void testCopy_subMatrix() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(5.0f, copy.get(0, 0));
        assertEquals(9.0f, copy.get(1, 1));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_simple() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0));
        assertEquals(4.0f, extended.get(1, 1));
        assertEquals(0.0f, extended.get(2, 2));
    }

    @Test
    public void testExtend_withDefaultValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix extended = m.extend(3, 3, 9.0f);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(9.0f, extended.get(2, 2));
    }

    @Test
    public void testExtend_directions() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 5.0f } });
        FloatMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(5.0f, extended.get(1, 1));
        assertEquals(0.0f, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionsWithDefault() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 5.0f } });
        FloatMatrix extended = m.extend(1, 1, 1, 1, 9.0f);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(5.0f, extended.get(1, 1));
        assertEquals(9.0f, extended.get(0, 0));
    }

    // ============ Flip/Reverse Tests ============

    @Test
    public void testReverseH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.reverseH();
        assertEquals(2.0f, m.get(0, 0));
        assertEquals(1.0f, m.get(0, 1));
        assertEquals(4.0f, m.get(1, 0));
        assertEquals(3.0f, m.get(1, 1));
    }

    @Test
    public void testReverseV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.reverseV();
        assertEquals(3.0f, m.get(0, 0));
        assertEquals(4.0f, m.get(0, 1));
        assertEquals(1.0f, m.get(1, 0));
        assertEquals(2.0f, m.get(1, 1));
    }

    @Test
    public void testFlipH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix flipped = m.flipH();
        assertEquals(2.0f, flipped.get(0, 0));
        assertEquals(1.0f, flipped.get(0, 1));
        assertEquals(1.0f, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix flipped = m.flipV();
        assertEquals(3.0f, flipped.get(0, 0));
        assertEquals(4.0f, flipped.get(0, 1));
        assertEquals(1.0f, m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3.0f, rotated.get(0, 0));
        assertEquals(1.0f, rotated.get(0, 1));
        assertEquals(4.0f, rotated.get(1, 0));
        assertEquals(2.0f, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate180();
        assertEquals(4.0f, rotated.get(0, 0));
        assertEquals(3.0f, rotated.get(0, 1));
        assertEquals(2.0f, rotated.get(1, 0));
        assertEquals(1.0f, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate270();
        assertEquals(2.0f, rotated.get(0, 0));
        assertEquals(4.0f, rotated.get(0, 1));
        assertEquals(1.0f, rotated.get(1, 0));
        assertEquals(3.0f, rotated.get(1, 1));
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1.0f, transposed.get(0, 0));
        assertEquals(4.0f, transposed.get(0, 1));
        assertEquals(3.0f, transposed.get(2, 0));
        assertEquals(6.0f, transposed.get(2, 1));
    }

    @Test
    public void testTranspose_square() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(1.0f, transposed.get(0, 0));
        assertEquals(3.0f, transposed.get(0, 1));
        assertEquals(2.0f, transposed.get(1, 0));
        assertEquals(4.0f, transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f } });
        FloatMatrix reshaped = m.reshape(2, 3);
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
        assertEquals(1.0f, reshaped.get(0, 0));
        assertEquals(4.0f, reshaped.get(1, 0));
    }

    // ============ Repelem Tests ============

    @Test
    public void testRepelem() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1.0f, result.get(0, 0));
        assertEquals(1.0f, result.get(0, 1));
        assertEquals(1.0f, result.get(1, 0));
        assertEquals(1.0f, result.get(1, 1));
        assertEquals(4.0f, result.get(2, 2));
        assertEquals(4.0f, result.get(3, 3));
    }

    // ============ Repmat Tests ============

    @Test
    public void testRepmat() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m.repmat(2, 2);
        assertEquals(4, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1.0f, result.get(0, 0));
        assertEquals(2.0f, result.get(0, 1));
        assertEquals(1.0f, result.get(0, 2));
        assertEquals(2.0f, result.get(0, 3));
        assertEquals(1.0f, result.get(2, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals(1.0f, flat.get(0));
        assertEquals(2.0f, flat.get(1));
        assertEquals(3.0f, flat.get(2));
        assertEquals(4.0f, flat.get(3));
    }

    @Test
    public void testFlatOp() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        final float[] sum = { 0.0f };
        m.flatOp(row -> {
            for (float val : row) {
                sum[0] += val;
            }
        });
        assertEquals(10.0f, sum[0]);
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = m1.vstack(m2);
        assertEquals(4, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1.0f, stacked.get(0, 0));
        assertEquals(5.0f, stacked.get(2, 0));
    }

    @Test
    public void testVstack_incompatibleColumns() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertEquals(1.0f, stacked.get(0, 0));
        assertEquals(5.0f, stacked.get(0, 2));
    }

    @Test
    public void testHstack_incompatibleRows() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Tests ============

    @Test
    public void testAdd() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix result = m1.add(m2);
        assertEquals(6.0f, result.get(0, 0));
        assertEquals(8.0f, result.get(0, 1));
        assertEquals(10.0f, result.get(1, 0));
        assertEquals(12.0f, result.get(1, 1));
    }

    @Test
    public void testAdd_incompatibleSize() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m1.subtract(m2);
        assertEquals(4.0f, result.get(0, 0));
        assertEquals(4.0f, result.get(0, 1));
        assertEquals(4.0f, result.get(1, 0));
        assertEquals(4.0f, result.get(1, 1));
    }

    @Test
    public void testMultiply() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f, 0.0f }, { 1.0f, 2.0f } });
        FloatMatrix result = m1.multiply(m2);
        assertEquals(4.0f, result.get(0, 0));
        assertEquals(4.0f, result.get(0, 1));
        assertEquals(10.0f, result.get(1, 0));
        assertEquals(8.0f, result.get(1, 1));
    }

    @Test
    public void testMultiply_incompatibleSize() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Matrix<Float> boxed = m.boxed();
        assertEquals(Float.valueOf(1.0f), boxed.get(0, 0));
        assertEquals(Float.valueOf(4.0f), boxed.get(1, 1));
    }

    @Test
    public void testToDoubleMatrix() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        DoubleMatrix dm = m.toDoubleMatrix();
        assertEquals(2, dm.rowCount());
        assertEquals(2, dm.columnCount());
        assertEquals(1.0, dm.get(0, 0));
        assertEquals(4.0, dm.get(1, 1));
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith_binary() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix result = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(6.0f, result.get(0, 0));
        assertEquals(12.0f, result.get(1, 1));
    }

    @Test
    public void testZipWith_ternary() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 1.0f }, { 1.0f, 1.0f } });
        FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(7.0f, result.get(0, 0));
        assertEquals(13.0f, result.get(1, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatStream stream = m.streamLU2RD();
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, result);
    }

    @Test
    public void testStreamRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatStream stream = m.streamRU2LD();
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, result);
    }

    @Test
    public void testStreamH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatStream stream = m.streamH();
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, result);
    }

    @Test
    public void testStreamH_withRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatStream stream = m.streamH(1);
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 3.0f, 4.0f }, result);
    }

    @Test
    public void testStreamH_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        FloatStream stream = m.streamH(1, 3);
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f, 6.0f }, result);
    }

    @Test
    public void testStreamV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatStream stream = m.streamV();
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 1.0f, 3.0f, 2.0f, 4.0f }, result);
    }

    @Test
    public void testStreamV_withColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatStream stream = m.streamV(0);
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 1.0f, 3.0f }, result);
    }

    @Test
    public void testStreamV_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatStream stream = m.streamV(1, 3);
        float[] result = stream.toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f, 3.0f, 6.0f }, result);
    }

    @Test
    public void testStreamR() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<FloatStream> stream = m.streamR();
        assertEquals(2, stream.count());
    }

    @Test
    public void testStreamR_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        Stream<FloatStream> stream = m.streamR(1, 3);
        assertEquals(2, stream.count());
    }

    @Test
    public void testStreamC() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<FloatStream> stream = m.streamC();
        assertEquals(2, stream.count());
    }

    @Test
    public void testStreamC_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        Stream<FloatStream> stream = m.streamC(1, 3);
        assertEquals(2, stream.count());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        final float[] sum = { 0.0f };
        m.forEach(val -> sum[0] += val);
        assertEquals(10.0f, sum[0]);
    }

    @Test
    public void testForEach_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        final float[] sum = { 0.0f };
        m.forEach(1, 3, 1, 3, val -> sum[0] += val);
        assertEquals(28.0f, sum[0]);
    }

    // ============ Inherited Methods Tests ============

    @Test
    public void testIsEmpty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertTrue(empty.isEmpty());
        FloatMatrix notEmpty = FloatMatrix.of(new float[][] { { 1.0f } });
        assertFalse(notEmpty.isEmpty());
    }

    @Test
    public void testIsSameShape() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertTrue(m1.isSameShape(m2));
        assertFalse(m1.isSameShape(m3));
    }

    @Test
    public void testArray() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        float[][] array = m.array();
        assertArrayEquals(new float[] { 1.0f, 2.0f }, array[0]);
        assertArrayEquals(new float[] { 3.0f, 4.0f }, array[1]);
    }

    @Test
    public void testReshape_singleParam() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f } });
        FloatMatrix reshaped = m.reshape(3);
        assertEquals(2, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
    }

    @Test
    public void testPointsLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Point> points = m.pointsLU2RD();
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Point> points = m.pointsRU2LD();
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Point> points = m.pointsH();
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsH_withRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Point> points = m.pointsH(1);
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsH_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        Stream<Point> points = m.pointsH(1, 3);
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Point> points = m.pointsV();
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsV_withColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Point> points = m.pointsV(1);
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsV_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        Stream<Point> points = m.pointsV(1, 3);
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsR() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Stream<Point>> points = m.pointsR();
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsC() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Stream<Stream<Point>> points = m.pointsC();
        assertEquals(2, points.count());
    }

    @Test
    public void testForEach_biConsumer() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        final int[] count = { 0 };
        m.forEach((i, j) -> count[0]++);
        assertEquals(4, count[0]);
    }

    @Test
    public void testForEach_biObjConsumer() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        final int[] count = { 0 };
        m.forEach((i, j, matrix) -> count[0]++);
        assertEquals(4, count[0]);
    }

    @Test
    public void testAccept() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        final boolean[] called = { false };
        m.accept(matrix -> called[0] = true);
        assertTrue(called[0]);
    }

    @Test
    public void testApply() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
        assertEquals(4, result);
    }

    // ============ Equality Tests ============

    @Test
    public void testHashCode() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 5.0f } });
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertTrue(m1.equals(m1));
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("4.0"));
    }

    @Test
    public void testPrintln() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        String result = m.println();
        assertNotNull(result);
    }

    @Test
    public void testPrintln_empty() {
        FloatMatrix m = FloatMatrix.empty();
        String result = m.println();
        assertNotNull(result);
    }
}
