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
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.u.OptionalFloat;
import com.landawn.abacus.util.stream.FloatStream;

@Tag("2025")
public class FloatMatrix2025Test extends TestBase {

    private static final float DELTA = 0.0001f;

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = new FloatMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(4.0f, m.get(1, 1), DELTA);
    }

    @Test
    public void testConstructor_withNullArray() {
        FloatMatrix m = new FloatMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        FloatMatrix m = new FloatMatrix(new float[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        FloatMatrix m = new FloatMatrix(new float[][] { { 42.5f } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42.5f, m.get(0, 0), DELTA);
    }

    @Test
    public void testConstructor_withFloatSpecialValues() {
        float[][] arr = { { Float.NaN, Float.POSITIVE_INFINITY }, { Float.NEGATIVE_INFINITY, -0.0f } };
        FloatMatrix m = new FloatMatrix(arr);
        assertTrue(Float.isNaN(m.get(0, 0)));
        assertEquals(Float.POSITIVE_INFINITY, m.get(0, 1), DELTA);
        assertEquals(Float.NEGATIVE_INFINITY, m.get(1, 0), DELTA);
        assertEquals(-0.0f, m.get(1, 1), DELTA);
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(FloatMatrix.empty(), FloatMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = FloatMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0f, m.get(0, 0), DELTA);
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
    public void testOf_withFloatPrecision() {
        float[][] arr = { { 0.1f, 0.2f }, { 0.3f, 0.4f } };
        FloatMatrix m = FloatMatrix.of(arr);
        assertEquals(0.1f, m.get(0, 0), DELTA);
        assertEquals(0.2f, m.get(0, 1), DELTA);
        assertEquals(0.3f, m.get(1, 0), DELTA);
        assertEquals(0.4f, m.get(1, 1), DELTA);
    }

    @Test
    public void testCreateFromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        FloatMatrix m = FloatMatrix.create(ints);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(4.0f, m.get(1, 1), DELTA);
    }

    @Test
    public void testCreateFromIntArray_withNull() {
        FloatMatrix m = FloatMatrix.create((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withEmpty() {
        FloatMatrix m = FloatMatrix.create(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withJaggedArray() {
        int[][] jagged = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.create(jagged));
    }

    @Test
    public void testCreateFromIntArray_withNullRow() {
        int[][] nullRow = { { 1, 2 }, null };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.create(nullRow));
    }

    @Test
    public void testCreateFromIntArray_withNullFirstRow() {
        int[][] nullFirstRow = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.create(nullFirstRow));
    }

    @Test
    public void testCreateFromIntArray_largeValues() {
        int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 1000000, -1000000 } };
        FloatMatrix m = FloatMatrix.create(ints);
        assertEquals(Integer.MAX_VALUE, m.get(0, 0), 1.0f);
        assertEquals(Integer.MIN_VALUE, m.get(0, 1), 1.0f);
        assertEquals(1000000.0f, m.get(1, 0), DELTA);
        assertEquals(-1000000.0f, m.get(1, 1), DELTA);
    }

    @Test
    public void testRandom() {
        FloatMatrix m = FloatMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        // Just verify elements exist and are in valid range
        for (int i = 0; i < 5; i++) {
            float val = m.get(0, i);
            assertTrue(val >= 0.0f && val < 1.0f);
        }
    }

    @Test
    public void testRepeat() {
        FloatMatrix m = FloatMatrix.repeat(3.14f, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14f, m.get(0, i), DELTA);
        }
    }

    @Test
    public void testRepeat_withSpecialValues() {
        FloatMatrix m = FloatMatrix.repeat(Float.NaN, 3);
        for (int i = 0; i < 3; i++) {
            assertTrue(Float.isNaN(m.get(0, i)));
        }

        FloatMatrix m2 = FloatMatrix.repeat(Float.POSITIVE_INFINITY, 2);
        for (int i = 0; i < 2; i++) {
            assertEquals(Float.POSITIVE_INFINITY, m2.get(0, i), DELTA);
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        FloatMatrix m = FloatMatrix.diagonalLU2RD(new float[] { 1.5f, 2.5f, 3.5f });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.5f, m.get(0, 0), DELTA);
        assertEquals(2.5f, m.get(1, 1), DELTA);
        assertEquals(3.5f, m.get(2, 2), DELTA);
        assertEquals(0.0f, m.get(0, 1), DELTA);
        assertEquals(0.0f, m.get(1, 0), DELTA);
    }

    @Test
    public void testDiagonalRU2LD() {
        FloatMatrix m = FloatMatrix.diagonalRU2LD(new float[] { 1.5f, 2.5f, 3.5f });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.5f, m.get(0, 2), DELTA);
        assertEquals(2.5f, m.get(1, 1), DELTA);
        assertEquals(3.5f, m.get(2, 0), DELTA);
        assertEquals(0.0f, m.get(0, 0), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA);
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        FloatMatrix m = FloatMatrix.diagonal(new float[] { 1.0f, 2.0f, 3.0f }, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA);
        assertEquals(3.0f, m.get(2, 2), DELTA);
        assertEquals(4.0f, m.get(0, 2), DELTA);
        assertEquals(6.0f, m.get(2, 0), DELTA);
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        FloatMatrix m = FloatMatrix.diagonal(new float[] { 1.0f, 2.0f, 3.0f }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(1, 1), DELTA);
        assertEquals(3.0f, m.get(2, 2), DELTA);
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        FloatMatrix m = FloatMatrix.diagonal(null, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(4.0f, m.get(0, 2), DELTA);
        assertEquals(5.0f, m.get(1, 1), DELTA);
        assertEquals(6.0f, m.get(2, 0), DELTA);
    }

    @Test
    public void testDiagonal_withBothNull() {
        FloatMatrix m = FloatMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonal(new float[] { 1.0f, 2.0f }, new float[] { 3.0f, 4.0f, 5.0f }));
    }

    @Test
    public void testUnbox() {
        Float[][] boxed = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        Matrix<Float> boxedMatrix = Matrix.of(boxed);
        FloatMatrix unboxed = FloatMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
        assertEquals(1.0f, unboxed.get(0, 0), DELTA);
        assertEquals(4.0f, unboxed.get(1, 1), DELTA);
    }

    @Test
    public void testUnbox_withNullValues() {
        Float[][] boxed = { { 1.0f, null }, { null, 4.0f } };
        Matrix<Float> boxedMatrix = Matrix.of(boxed);
        FloatMatrix unboxed = FloatMatrix.unbox(boxedMatrix);
        assertEquals(1.0f, unboxed.get(0, 0), DELTA);
        assertEquals(0.0f, unboxed.get(0, 1), DELTA); // null -> 0.0f
        assertEquals(0.0f, unboxed.get(1, 0), DELTA); // null -> 0.0f
        assertEquals(4.0f, unboxed.get(1, 1), DELTA);
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f } });
        assertEquals(float.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(5.0f, m.get(1, 1), DELTA);
        assertEquals(6.0f, m.get(1, 2), DELTA);
    }

    @Test
    public void testGet_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(1.0f, m.get(Point.of(0, 0)), DELTA);
        assertEquals(4.0f, m.get(Point.of(1, 1)), DELTA);
        assertEquals(2.0f, m.get(Point.of(0, 1)), DELTA);
    }

    @Test
    public void testSet() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(0, 0, 10.5f);
        assertEquals(10.5f, m.get(0, 0), DELTA);

        m.set(1, 1, 20.7f);
        assertEquals(20.7f, m.get(1, 1), DELTA);
    }

    @Test
    public void testSet_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0.0f));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0.0f));
    }

    @Test
    public void testSetWithPoint() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(Point.of(0, 0), 50.5f);
        assertEquals(50.5f, m.get(Point.of(0, 0)), DELTA);
    }

    @Test
    public void testSet_withSpecialValues() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(0, 0, Float.NaN);
        assertTrue(Float.isNaN(m.get(0, 0)));

        m.set(0, 1, Float.POSITIVE_INFINITY);
        assertEquals(Float.POSITIVE_INFINITY, m.get(0, 1), DELTA);

        m.set(1, 0, -0.0f);
        assertEquals(-0.0f, m.get(1, 0), DELTA);
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        OptionalFloat up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1.0f, up.get(), DELTA);

        // Top row has no element above
        OptionalFloat empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        OptionalFloat down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3.0f, down.get(), DELTA);

        // Bottom row has no element below
        OptionalFloat empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        OptionalFloat left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1.0f, left.get(), DELTA);

        // Leftmost column has no element to the left
        OptionalFloat empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        OptionalFloat right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2.0f, right.get(), DELTA);

        // Rightmost column has no element to the right
        OptionalFloat empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testAdjacent4Points_centerElement() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
        assertNotNull(points.get(0)); // up
        assertNotNull(points.get(1)); // right
        assertNotNull(points.get(2)); // down
        assertNotNull(points.get(3)); // left
    }

    @Test
    public void testAdjacent4Points_cornerElement() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(4, points.size());
        assertEquals(null, points.get(0)); // up (out of bounds)
        assertNotNull(points.get(1)); // right
        assertNotNull(points.get(2)); // down
        assertEquals(null, points.get(3)); // left (out of bounds)
    }

    @Test
    public void testAdjacent8Points_centerElement() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        List<Point> points = m.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
        // All should be non-null for center element
        for (Point p : points) {
            assertNotNull(p);
        }
    }

    @Test
    public void testAdjacent8Points_cornerElement() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(8, points.size());
        assertEquals(null, points.get(0)); // left-up
        assertEquals(null, points.get(1)); // up
        assertEquals(null, points.get(2)); // right-up
        assertNotNull(points.get(3)); // right
        assertNotNull(points.get(4)); // right-down
        assertNotNull(points.get(5)); // down
        assertEquals(null, points.get(6)); // left-down
        assertEquals(null, points.get(7)); // left
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, m.row(0), DELTA);
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, m.row(1), DELTA);
    }

    @Test
    public void testRow_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        assertArrayEquals(new float[] { 1.0f, 4.0f }, m.column(0), DELTA);
        assertArrayEquals(new float[] { 2.0f, 5.0f }, m.column(1), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f }, m.column(2), DELTA);
    }

    @Test
    public void testColumn_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setRow(0, new float[] { 10.5f, 20.5f });
        assertArrayEquals(new float[] { 10.5f, 20.5f }, m.row(0), DELTA);
        assertArrayEquals(new float[] { 3.0f, 4.0f }, m.row(1), DELTA); // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f, 2.0f, 3.0f }));
    }

    @Test
    public void testSetColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setColumn(0, new float[] { 10.5f, 20.5f });
        assertArrayEquals(new float[] { 10.5f, 20.5f }, m.column(0), DELTA);
        assertArrayEquals(new float[] { 2.0f, 4.0f }, m.column(1), DELTA); // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f, 2.0f, 3.0f }));
    }

    @Test
    public void testUpdateRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateRow(0, x -> x * 2.0f);
        assertArrayEquals(new float[] { 2.0f, 4.0f }, m.row(0), DELTA);
        assertArrayEquals(new float[] { 3.0f, 4.0f }, m.row(1), DELTA); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateColumn(0, x -> x + 10.0f);
        assertArrayEquals(new float[] { 11.0f, 13.0f }, m.column(0), DELTA);
        assertArrayEquals(new float[] { 2.0f, 4.0f }, m.column(1), DELTA); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, m.getLU2RD(), DELTA);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.setLU2RD(new float[] { 10.5f, 20.5f, 30.5f });
        assertEquals(10.5f, m.get(0, 0), DELTA);
        assertEquals(20.5f, m.get(1, 1), DELTA);
        assertEquals(30.5f, m.get(2, 2), DELTA);
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new float[] { 1.0f }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testUpdateLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.updateLU2RD(x -> x * 10.0f);
        assertEquals(10.0f, m.get(0, 0), DELTA);
        assertEquals(50.0f, m.get(1, 1), DELTA);
        assertEquals(90.0f, m.get(2, 2), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> x * 2.0f));
    }

    @Test
    public void testGetRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, m.getRU2LD(), DELTA);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.setRU2LD(new float[] { 10.5f, 20.5f, 30.5f });
        assertEquals(10.5f, m.get(0, 2), DELTA);
        assertEquals(20.5f, m.get(1, 1), DELTA);
        assertEquals(30.5f, m.get(2, 0), DELTA);
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new float[] { 1.0f }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void testUpdateRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.updateRU2LD(x -> x * 10.0f);
        assertEquals(30.0f, m.get(0, 2), DELTA);
        assertEquals(50.0f, m.get(1, 1), DELTA);
        assertEquals(70.0f, m.get(2, 0), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> x * 2.0f));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateAll(x -> x * 2.0f);
        assertEquals(2.0f, m.get(0, 0), DELTA);
        assertEquals(4.0f, m.get(0, 1), DELTA);
        assertEquals(6.0f, m.get(1, 0), DELTA);
        assertEquals(8.0f, m.get(1, 1), DELTA);
    }

    @Test
    public void testUpdateAll_withIndices() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f }, { 0.0f, 0.0f } });
        m.updateAll((i, j) -> (float) (i * 10 + j));
        assertEquals(0.0f, m.get(0, 0), DELTA);
        assertEquals(1.0f, m.get(0, 1), DELTA);
        assertEquals(10.0f, m.get(1, 0), DELTA);
        assertEquals(11.0f, m.get(1, 1), DELTA);
    }

    @Test
    public void testReplaceIf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        m.replaceIf(x -> x > 3.0f, 0.0f);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA);
        assertEquals(3.0f, m.get(0, 2), DELTA);
        assertEquals(0.0f, m.get(1, 0), DELTA); // was 4.0f
        assertEquals(0.0f, m.get(1, 1), DELTA); // was 5.0f
        assertEquals(0.0f, m.get(1, 2), DELTA); // was 6.0f
    }

    @Test
    public void testReplaceIf_withIndices() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.replaceIf((i, j) -> i == j, 0.0f); // Replace diagonal
        assertEquals(0.0f, m.get(0, 0), DELTA);
        assertEquals(0.0f, m.get(1, 1), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA); // unchanged
    }

    @Test
    public void testMap() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m.map(x -> x * 2.0f);
        assertEquals(2.0f, result.get(0, 0), DELTA);
        assertEquals(4.0f, result.get(0, 1), DELTA);
        assertEquals(6.0f, result.get(1, 0), DELTA);
        assertEquals(8.0f, result.get(1, 1), DELTA);

        // Original unchanged
        assertEquals(1.0f, m.get(0, 0), DELTA);
    }

    @Test
    public void testMapToObj() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.5f, 2.5f }, { 3.5f, 4.5f } });
        Matrix<String> result = m.mapToObj(x -> String.format("%.1f", x), String.class);
        assertEquals("1.5", result.get(0, 0));
        assertEquals("4.5", result.get(1, 1));
    }

    @Test
    public void testMapToDouble() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(1.0, result.get(0, 0), 0.0001);
        assertEquals(4.0, result.get(1, 1), 0.0001);
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.fill(99.5f);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                assertEquals(99.5f, m.get(i, j), DELTA);
            }
        }
    }

    @Test
    public void testFill_withArray() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
        float[][] patch = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        m.fill(patch);
        assertEquals(1.5f, m.get(0, 0), DELTA);
        assertEquals(2.5f, m.get(0, 1), DELTA);
        assertEquals(3.5f, m.get(1, 0), DELTA);
        assertEquals(4.5f, m.get(1, 1), DELTA);
        assertEquals(0.0f, m.get(2, 2), DELTA); // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
        float[][] patch = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        m.fill(1, 1, patch);
        assertEquals(0.0f, m.get(0, 0), DELTA); // unchanged
        assertEquals(1.5f, m.get(1, 1), DELTA);
        assertEquals(2.5f, m.get(1, 2), DELTA);
        assertEquals(3.5f, m.get(2, 1), DELTA);
        assertEquals(4.5f, m.get(2, 2), DELTA);
    }

    @Test
    public void testFill_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        float[][] patch = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals(1.0f, copy.get(0, 0), DELTA);

        // Modify copy shouldn't affect original
        copy.set(0, 0, 99.5f);
        assertEquals(1.0f, m.get(0, 0), DELTA);
        assertEquals(99.5f, copy.get(0, 0), DELTA);
    }

    @Test
    public void testCopy_withRowRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix subset = m.copy(1, 3);
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
        assertEquals(4.0f, subset.get(0, 0), DELTA);
        assertEquals(9.0f, subset.get(1, 2), DELTA);
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
        assertEquals(2.0f, submatrix.get(0, 0), DELTA);
        assertEquals(6.0f, submatrix.get(1, 1), DELTA);
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1.0f, extended.get(0, 0), DELTA);
        assertEquals(4.0f, extended.get(1, 1), DELTA);
        assertEquals(0.0f, extended.get(3, 3), DELTA); // new cells are 0.0f
    }

    @Test
    public void testExtend_smaller() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertEquals(1.0f, truncated.get(0, 0), DELTA);
        assertEquals(5.0f, truncated.get(1, 1), DELTA);
    }

    @Test
    public void testExtend_withDefaultValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix extended = m.extend(3, 3, -1.5f);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1.0f, extended.get(0, 0), DELTA);
        assertEquals(-1.5f, extended.get(2, 2), DELTA); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, 0.0f));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, 0.0f));
    }

    @Test
    public void testExtend_directional() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rows); // 1 + 3 + 1
        assertEquals(7, extended.cols); // 2 + 3 + 2

        // Original values at offset position
        assertEquals(1.0f, extended.get(1, 2), DELTA);
        assertEquals(5.0f, extended.get(2, 3), DELTA);

        // New cells are 0.0f
        assertEquals(0.0f, extended.get(0, 0), DELTA);
    }

    @Test
    public void testExtend_directionalWithDefault() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix extended = m.extend(1, 1, 1, 1, -1.5f);
        assertEquals(5, extended.rows);
        assertEquals(5, extended.cols);

        // Check original values
        assertEquals(1.0f, extended.get(1, 1), DELTA);

        // Check new values
        assertEquals(-1.5f, extended.get(0, 0), DELTA);
        assertEquals(-1.5f, extended.get(4, 4), DELTA);
    }

    @Test
    public void testExtend_directionalWithNegativeValues() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0.0f));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void testReverseH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        m.reverseH();
        assertEquals(3.0f, m.get(0, 0), DELTA);
        assertEquals(2.0f, m.get(0, 1), DELTA);
        assertEquals(1.0f, m.get(0, 2), DELTA);
        assertEquals(6.0f, m.get(1, 0), DELTA);
    }

    @Test
    public void testReverseV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        m.reverseV();
        assertEquals(5.0f, m.get(0, 0), DELTA);
        assertEquals(6.0f, m.get(0, 1), DELTA);
        assertEquals(3.0f, m.get(1, 0), DELTA);
        assertEquals(1.0f, m.get(2, 0), DELTA);
    }

    @Test
    public void testFlipH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix flipped = m.flipH();
        assertEquals(3.0f, flipped.get(0, 0), DELTA);
        assertEquals(2.0f, flipped.get(0, 1), DELTA);
        assertEquals(1.0f, flipped.get(0, 2), DELTA);

        // Original unchanged
        assertEquals(1.0f, m.get(0, 0), DELTA);
    }

    @Test
    public void testFlipV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        FloatMatrix flipped = m.flipV();
        assertEquals(5.0f, flipped.get(0, 0), DELTA);
        assertEquals(3.0f, flipped.get(1, 0), DELTA);
        assertEquals(1.0f, flipped.get(2, 0), DELTA);

        // Original unchanged
        assertEquals(1.0f, m.get(0, 0), DELTA);
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3.0f, rotated.get(0, 0), DELTA);
        assertEquals(1.0f, rotated.get(0, 1), DELTA);
        assertEquals(4.0f, rotated.get(1, 0), DELTA);
        assertEquals(2.0f, rotated.get(1, 1), DELTA);
    }

    @Test
    public void testRotate180() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4.0f, rotated.get(0, 0), DELTA);
        assertEquals(3.0f, rotated.get(0, 1), DELTA);
        assertEquals(2.0f, rotated.get(1, 0), DELTA);
        assertEquals(1.0f, rotated.get(1, 1), DELTA);
    }

    @Test
    public void testRotate270() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2.0f, rotated.get(0, 0), DELTA);
        assertEquals(4.0f, rotated.get(0, 1), DELTA);
        assertEquals(1.0f, rotated.get(1, 0), DELTA);
        assertEquals(3.0f, rotated.get(1, 1), DELTA);
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1.0f, transposed.get(0, 0), DELTA);
        assertEquals(4.0f, transposed.get(0, 1), DELTA);
        assertEquals(2.0f, transposed.get(1, 0), DELTA);
        assertEquals(5.0f, transposed.get(1, 1), DELTA);
        assertEquals(3.0f, transposed.get(2, 0), DELTA);
        assertEquals(6.0f, transposed.get(2, 1), DELTA);
    }

    @Test
    public void testTranspose_square() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1.0f, transposed.get(0, 0), DELTA);
        assertEquals(3.0f, transposed.get(0, 1), DELTA);
        assertEquals(2.0f, transposed.get(1, 0), DELTA);
        assertEquals(4.0f, transposed.get(1, 1), DELTA);
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, reshaped.get(0, i), DELTA);
        }
    }

    @Test
    public void testReshape_back() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix reshaped = m.reshape(1, 9);
        FloatMatrix reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        FloatMatrix reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1.0f, repeated.get(0, 0), DELTA);
        assertEquals(1.0f, repeated.get(0, 1), DELTA);
        assertEquals(1.0f, repeated.get(0, 2), DELTA);
        assertEquals(2.0f, repeated.get(0, 3), DELTA);
        assertEquals(2.0f, repeated.get(0, 4), DELTA);
        assertEquals(2.0f, repeated.get(0, 5), DELTA);
        assertEquals(1.0f, repeated.get(1, 0), DELTA); // second row same as first
    }

    @Test
    public void testRepelem_invalidArguments() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals(1.0f, repeated.get(0, 0), DELTA);
        assertEquals(2.0f, repeated.get(0, 1), DELTA);
        assertEquals(1.0f, repeated.get(0, 2), DELTA); // repeat starts
        assertEquals(2.0f, repeated.get(0, 3), DELTA);

        assertEquals(3.0f, repeated.get(1, 0), DELTA);
        assertEquals(4.0f, repeated.get(1, 1), DELTA);

        // Check vertical repeat
        assertEquals(1.0f, repeated.get(2, 0), DELTA); // vertical repeat starts
        assertEquals(2.0f, repeated.get(2, 1), DELTA);
    }

    @Test
    public void testRepmat_invalidArguments() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatList flat = m.flatten();
        assertEquals(9, flat.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, flat.get(i), DELTA);
        }
    }

    @Test
    public void testFlatten_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        FloatList flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        List<Float> sums = new ArrayList<>();
        m.flatOp(row -> {
            float sum = 0.0f;
            for (float val : row) {
                sum += val;
            }
            sums.add(sum);
        });
        assertEquals(1, sums.size());
        assertEquals(45.0f, sums.get(0), DELTA);
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 7.0f, 8.0f, 9.0f }, { 10.0f, 11.0f, 12.0f } });
        FloatMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rows);
        assertEquals(3, stacked.cols);
        assertEquals(1.0f, stacked.get(0, 0), DELTA);
        assertEquals(7.0f, stacked.get(2, 0), DELTA);
        assertEquals(12.0f, stacked.get(3, 2), DELTA);
    }

    @Test
    public void testVstack_differentColumnCounts() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals(1.0f, stacked.get(0, 0), DELTA);
        assertEquals(5.0f, stacked.get(0, 2), DELTA);
        assertEquals(8.0f, stacked.get(1, 3), DELTA);
    }

    @Test
    public void testHstack_differentRowCounts() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void testAdd() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix sum = m1.add(m2);

        assertEquals(6.0f, sum.get(0, 0), DELTA);
        assertEquals(8.0f, sum.get(0, 1), DELTA);
        assertEquals(10.0f, sum.get(1, 0), DELTA);
        assertEquals(12.0f, sum.get(1, 1), DELTA);
    }

    @Test
    public void testAdd_differentDimensions() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testSubtract() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix diff = m1.subtract(m2);

        assertEquals(4.0f, diff.get(0, 0), DELTA);
        assertEquals(4.0f, diff.get(0, 1), DELTA);
        assertEquals(4.0f, diff.get(1, 0), DELTA);
        assertEquals(4.0f, diff.get(1, 1), DELTA);
    }

    @Test
    public void testSubtract_differentDimensions() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMultiply() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix product = m1.multiply(m2);

        assertEquals(19.0f, product.get(0, 0), DELTA); // 1*5 + 2*7
        assertEquals(22.0f, product.get(0, 1), DELTA); // 1*6 + 2*8
        assertEquals(43.0f, product.get(1, 0), DELTA); // 3*5 + 4*7
        assertEquals(50.0f, product.get(1, 1), DELTA); // 3*6 + 4*8
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } }); // 1x3
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 4.0f }, { 5.0f }, { 6.0f } }); // 3x1
        FloatMatrix product = m1.multiply(m2);

        assertEquals(1, product.rows);
        assertEquals(1, product.cols);
        assertEquals(32.0f, product.get(0, 0), DELTA); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        Matrix<Float> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(3, boxed.cols);
        assertEquals(Float.valueOf(1.0f), boxed.get(0, 0));
        assertEquals(Float.valueOf(6.0f), boxed.get(1, 2));
    }

    @Test
    public void testToDoubleMatrix() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        DoubleMatrix doubleMatrix = m.toDoubleMatrix();
        assertEquals(2, doubleMatrix.rows);
        assertEquals(2, doubleMatrix.cols);
        assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
        assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);

        assertEquals(5.0f, result.get(0, 0), DELTA); // 1*5
        assertEquals(12.0f, result.get(0, 1), DELTA); // 2*6
        assertEquals(21.0f, result.get(1, 0), DELTA); // 3*7
        assertEquals(32.0f, result.get(1, 1), DELTA); // 4*8
    }

    @Test
    public void testZipWith_differentShapes() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
    }

    @Test
    public void testZipWith_threeMatrices() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 9.0f, 10.0f }, { 11.0f, 12.0f } });
        FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15.0f, result.get(0, 0), DELTA); // 1+5+9
        assertEquals(18.0f, result.get(0, 1), DELTA); // 2+6+10
        assertEquals(21.0f, result.get(1, 0), DELTA); // 3+7+11
        assertEquals(24.0f, result.get(1, 1), DELTA); // 4+8+12
    }

    @Test
    public void testZipWith_threeMatrices_differentShapes() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 9.0f, 10.0f, 11.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal, DELTA);
    }

    @Test
    public void testStreamLU2RD_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, antiDiagonal, DELTA);
    }

    @Test
    public void testStreamRU2LD_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.streamRU2LD().toArray().length);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        FloatMatrix nonSquare = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        float[] all = m.streamH().toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }, all, DELTA);
    }

    @Test
    public void testStreamH_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.streamH().toArray().length);
    }

    @Test
    public void testStreamH_withRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        float[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, row1, DELTA);
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, rows, DELTA);
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        float[] all = m.streamV().toArray();
        assertArrayEquals(new float[] { 1.0f, 4.0f, 2.0f, 5.0f, 3.0f, 6.0f }, all, DELTA);
    }

    @Test
    public void testStreamV_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.streamV().toArray().length);
    }

    @Test
    public void testStreamV_withColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        float[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f }, col1, DELTA);
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] cols = m.streamV(1, 3).toArray();
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f }, cols, DELTA);
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        List<float[]> rows = m.streamR().map(FloatStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, rows.get(0), DELTA);
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(1), DELTA);
    }

    @Test
    public void testStreamR_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        List<float[]> rows = m.streamR(1, 3).map(FloatStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new float[] { 4.0f, 5.0f, 6.0f }, rows.get(0), DELTA);
        assertArrayEquals(new float[] { 7.0f, 8.0f, 9.0f }, rows.get(1), DELTA);
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        List<float[]> cols = m.streamC().map(FloatStream::toArray).toList();
        assertEquals(3, cols.size());
        assertArrayEquals(new float[] { 1.0f, 4.0f }, cols.get(0), DELTA);
        assertArrayEquals(new float[] { 2.0f, 5.0f }, cols.get(1), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f }, cols.get(2), DELTA);
    }

    @Test
    public void testStreamC_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        List<float[]> cols = m.streamC(1, 3).map(FloatStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new float[] { 2.0f, 5.0f, 8.0f }, cols.get(0), DELTA);
        assertArrayEquals(new float[] { 3.0f, 6.0f, 9.0f }, cols.get(1), DELTA);
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        List<Float> values = new ArrayList<>();
        m.forEach(v -> values.add(v));
        assertEquals(9, values.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, values.get(i), DELTA);
        }
    }

    // ============ Object Methods Tests ============

    @Test
    public void testPrintln() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        // Just ensure it doesn't throw
        m.println();

        FloatMatrix empty = FloatMatrix.empty();
        empty.println();
    }

    @Test
    public void testHashCode() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 4.0f, 3.0f } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 4.0f, 3.0f } });
        FloatMatrix m4 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0") || str.contains("1"));
        assertTrue(str.contains("2.0") || str.contains("2"));
        assertTrue(str.contains("3.0") || str.contains("3"));
        assertTrue(str.contains("4.0") || str.contains("4"));
    }

    // ============ Statistical Operations Tests ============

    @Test
    public void testStatisticalOperations() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });

        // Test sum
        double totalSum = m.streamH().sum();
        assertEquals(45.0, totalSum, DELTA); // 1+2+3+4+5+6+7+8+9 = 45

        // Test sum of specific row
        double row1Sum = m.streamH(1).sum();
        assertEquals(15.0, row1Sum, DELTA); // 4+5+6 = 15

        // Test sum of specific column
        double col0Sum = m.streamV(0).sum();
        assertEquals(12.0, col0Sum, DELTA); // 1+4+7 = 12

        // Test min/max
        OptionalFloat min = m.streamH().min();
        assertTrue(min.isPresent());
        assertEquals(1.0f, min.get(), DELTA);

        OptionalFloat max = m.streamH().max();
        assertTrue(max.isPresent());
        assertEquals(9.0f, max.get(), DELTA);

        // Test average
        OptionalDouble avg = m.streamH().average();
        assertTrue(avg.isPresent());
        assertEquals(5.0, avg.get(), DELTA);

        // Test diagonal operations
        double diagonalSum = m.streamLU2RD().sum();
        assertEquals(15.0, diagonalSum, DELTA); // 1+5+9 = 15

        double antiDiagonalSum = m.streamRU2LD().sum();
        assertEquals(15.0, antiDiagonalSum, DELTA); // 3+5+7 = 15
    }

    @Test
    public void testRowColumnStatistics() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });

        // Test statistics on individual rows
        List<Double> rowSums = m.streamR().map(row -> row.sum()).toList();
        assertEquals(3, rowSums.size());
        assertEquals(6.0, rowSums.get(0), DELTA); // 1+2+3
        assertEquals(15.0, rowSums.get(1), DELTA); // 4+5+6
        assertEquals(24.0, rowSums.get(2), DELTA); // 7+8+9

        // Test statistics on individual columns
        List<Double> colSums = m.streamC().map(col -> col.sum()).toList();
        assertEquals(3, colSums.size());
        assertEquals(12.0, colSums.get(0), DELTA); // 1+4+7
        assertEquals(15.0, colSums.get(1), DELTA); // 2+5+8
        assertEquals(18.0, colSums.get(2), DELTA); // 3+6+9
    }

    // ============ Edge Case Tests ============

    @Test
    public void testEmptyMatrixOperations() {
        FloatMatrix empty = FloatMatrix.empty();

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rows);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        FloatMatrix extended = empty.extend(2, 2, 5.5f);
        assertEquals(2, extended.rows);
        assertEquals(2, extended.cols);
        assertEquals(5.5f, extended.get(0, 0), DELTA);
    }

    @Test
    public void testArithmeticEdgeCases() {
        // Test with zero matrix
        FloatMatrix zeros = FloatMatrix.of(new float[][] { { 0.0f, 0.0f }, { 0.0f, 0.0f } });
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        FloatMatrix addZero = m.add(zeros);
        assertEquals(m, addZero);

        FloatMatrix subtractZero = m.subtract(zeros);
        assertEquals(m, subtractZero);

        // Test multiplication with zero matrix
        FloatMatrix multiplyZero = m.multiply(zeros);
        assertEquals(zeros, multiplyZero);

        // Test addition commutativity
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        assertEquals(m1.add(m2), m2.add(m1));

        // Test subtraction anti-commutativity
        FloatMatrix diff1 = m1.subtract(m2);
        FloatMatrix diff2 = m2.subtract(m1);
        assertEquals(diff1.get(0, 0), -diff2.get(0, 0), DELTA);
        assertEquals(diff1.get(1, 1), -diff2.get(1, 1), DELTA);
    }

    @Test
    public void testScalarOperationsWithMap() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });

        // Scalar addition
        FloatMatrix addScalar = m.map(x -> x + 10.0f);
        assertEquals(11.0f, addScalar.get(0, 0), DELTA);
        assertEquals(14.0f, addScalar.get(1, 1), DELTA);

        // Scalar multiplication
        FloatMatrix multiplyScalar = m.map(x -> x * 3.0f);
        assertEquals(3.0f, multiplyScalar.get(0, 0), DELTA);
        assertEquals(12.0f, multiplyScalar.get(1, 1), DELTA);

        // Scalar division
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 10.0f, 20.0f }, { 30.0f, 40.0f } });
        FloatMatrix divideScalar = m2.map(x -> x / 10.0f);
        assertEquals(1.0f, divideScalar.get(0, 0), DELTA);
        assertEquals(4.0f, divideScalar.get(1, 1), DELTA);
    }

    @Test
    public void testElementWiseMultiplyWithZipWith() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f, 3.0f }, { 4.0f, 5.0f } });

        // Element-wise multiplication
        FloatMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(2.0f, elementWiseProduct.get(0, 0), DELTA); // 1*2
        assertEquals(6.0f, elementWiseProduct.get(0, 1), DELTA); // 2*3
        assertEquals(12.0f, elementWiseProduct.get(1, 0), DELTA); // 3*4
        assertEquals(20.0f, elementWiseProduct.get(1, 1), DELTA); // 4*5

        // Element-wise division
        FloatMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
        assertEquals(2.0f, elementWiseDivision.get(0, 0), DELTA); // 2/1
        assertEquals(1.5f, elementWiseDivision.get(0, 1), DELTA); // 3/2
        assertEquals(1.333333f, elementWiseDivision.get(1, 0), DELTA); // 4/3
        assertEquals(1.25f, elementWiseDivision.get(1, 1), DELTA); // 5/4
    }

    // ============ Float Special Value Tests ============

    @Test
    public void testNaNOperations() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { Float.NaN, 2.0f }, { 3.0f, 4.0f } });

        assertTrue(Float.isNaN(m.get(0, 0)));
        assertFalse(Float.isNaN(m.get(0, 1)));

        // Test that NaN propagates in arithmetic
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 1.0f }, { 1.0f, 1.0f } });
        FloatMatrix sum = m.add(m2);
        assertTrue(Float.isNaN(sum.get(0, 0)));
        assertEquals(3.0f, sum.get(0, 1), DELTA);
    }

    @Test
    public void testInfinityOperations() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { Float.POSITIVE_INFINITY, 2.0f }, { Float.NEGATIVE_INFINITY, 4.0f } });

        assertEquals(Float.POSITIVE_INFINITY, m.get(0, 0), DELTA);
        assertEquals(Float.NEGATIVE_INFINITY, m.get(1, 0), DELTA);

        // Test arithmetic with infinity
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 1.0f }, { 1.0f, 1.0f } });
        FloatMatrix sum = m.add(m2);
        assertEquals(Float.POSITIVE_INFINITY, sum.get(0, 0), DELTA);
        assertEquals(Float.NEGATIVE_INFINITY, sum.get(1, 0), DELTA);
    }

    @Test
    public void testNegativeZero() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { -0.0f, 0.0f }, { 1.0f, -1.0f } });

        // Test that -0.0f and 0.0f are treated differently in bit representation
        // but equal in value comparison
        assertEquals(-0.0f, m.get(0, 0), DELTA);
        assertEquals(0.0f, m.get(0, 1), DELTA);

        // They should be equal in value
        assertTrue(m.get(0, 0) == 0.0f);
        assertTrue(m.get(0, 1) == 0.0f);
    }

    @Test
    public void testFloatPrecisionLimits() {
        // Test float precision limits
        FloatMatrix m = FloatMatrix.of(new float[][] { { Float.MAX_VALUE, Float.MIN_VALUE }, { Float.MIN_NORMAL, 1e-45f } });

        assertEquals(Float.MAX_VALUE, m.get(0, 0), DELTA);
        assertEquals(Float.MIN_VALUE, m.get(0, 1), DELTA);
        assertEquals(Float.MIN_NORMAL, m.get(1, 0), DELTA);
        assertEquals(1e-45f, m.get(1, 1), DELTA);

        // Test overflow
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { Float.MAX_VALUE } });
        FloatMatrix overflow = m2.map(x -> x * 2.0f);
        assertEquals(Float.POSITIVE_INFINITY, overflow.get(0, 0), DELTA);
    }

    @Test
    public void testFloatPrecisionInArithmetic() {
        // Test precision loss in float arithmetic
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 0.1f, 0.2f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 0.3f, 0.4f } });

        FloatMatrix sum = m1.add(m2);

        // Float arithmetic may have precision issues
        assertEquals(0.4f, sum.get(0, 0), 0.001f); // More lenient delta for precision
        assertEquals(0.6f, sum.get(0, 1), 0.001f);
    }

    @Test
    public void testVerySmallValues() {
        // Test with very small values near zero
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1e-20f, 1e-30f }, { 1e-38f, 1e-40f } });

        assertEquals(1e-20f, m.get(0, 0), 1e-21f);
        assertEquals(1e-30f, m.get(0, 1), 1e-31f);

        // Values near the limit of float precision
        assertTrue(m.get(1, 0) > 0);
        assertTrue(m.get(1, 1) >= 0); // May underflow to 0
    }

    @Test
    public void testIntToFloatConversion() {
        // Test precision when converting large integers to float
        int[][] largeInts = { { 16777216, 16777217 }, { -16777216, -16777217 } };
        FloatMatrix m = FloatMatrix.create(largeInts);

        // 16777216 can be exactly represented in float
        assertEquals(16777216.0f, m.get(0, 0), DELTA);

        // 16777217 may lose precision in float representation
        float converted = m.get(0, 1);
        assertTrue(Math.abs(converted - 16777217.0f) < 2.0f);
    }
}
