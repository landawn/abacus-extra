package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.Stream;

/**
 * Comprehensive unit tests for AbstractMatrix class covering all public methods.
 * Tests use concrete subclass implementations (IntMatrix, DoubleMatrix, Matrix) to test abstract behavior.
 */
@Tag("2510")
public class AbstractMatrix2510Test extends TestBase {

    // ============ Basic Property Tests ============

    @Test
    public void testRowsColsCount_rectangular() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(2, m.rows);
        assertEquals(3, m.cols);
        assertEquals(6, m.count);
    }

    @Test
    public void testRowsColsCount_square() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(4, m.count);
    }

    @Test
    public void testRowsColsCount_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(1, m.count);
    }

    @Test
    public void testRowsColsCount_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4, 5 } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertEquals(5, m.count);
    }

    @Test
    public void testRowsColsCount_singleColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 }, { 4 } });
        assertEquals(4, m.rows);
        assertEquals(1, m.cols);
        assertEquals(4, m.count);
    }

    @Test
    public void testRowsColsCount_empty() {
        IntMatrix m = IntMatrix.empty();
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertEquals(0, m.count);
    }

    @Test
    public void testRowsColsCount_largeMatrix() {
        IntMatrix m = IntMatrix.of(new int[100][50]);
        assertEquals(100, m.rows);
        assertEquals(50, m.cols);
        assertEquals(5000, m.count);
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertEquals(int.class, m.componentType());
    }

    @Test
    public void testComponentType_doubleMatrix() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertEquals(double.class, m.componentType());
    }

    @Test
    public void testComponentType_longMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertEquals(long.class, m.componentType());
    }

    @Test
    public void testComponentType_floatMatrix() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertEquals(float.class, m.componentType());
    }

    @Test
    public void testComponentType_byteMatrix() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
        assertEquals(byte.class, m.componentType());
    }

    @Test
    public void testComponentType_shortMatrix() {
        ShortMatrix m = ShortMatrix.of(new short[][] { { 1, 2 } });
        assertEquals(short.class, m.componentType());
    }

    @Test
    public void testComponentType_charMatrix() {
        CharMatrix m = CharMatrix.of(new char[][] { { 'A', 'B' } });
        assertEquals(char.class, m.componentType());
    }

    @Test
    public void testComponentType_booleanMatrix() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false } });
        assertEquals(boolean.class, m.componentType());
    }

    @Test
    public void testComponentType_objectMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
        assertEquals(String.class, m.componentType());
    }

    // ============ Array Access Tests ============

    @Test
    public void testArray_intMatrix() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.of(arr);
        int[][] returnedArray = m.array();

        assertNotNull(returnedArray);
        assertEquals(2, returnedArray.length);
        assertArrayEquals(new int[] { 1, 2 }, returnedArray[0]);
        assertArrayEquals(new int[] { 3, 4 }, returnedArray[1]);
    }

    @Test
    public void testArray_doubleMatrix() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        DoubleMatrix m = DoubleMatrix.of(arr);
        double[][] returnedArray = m.array();

        assertNotNull(returnedArray);
        assertEquals(2, returnedArray.length);
        assertArrayEquals(new double[] { 1.5, 2.5 }, returnedArray[0]);
        assertArrayEquals(new double[] { 3.5, 4.5 }, returnedArray[1]);
    }

    @Test
    public void testArray_objectMatrix() {
        String[][] arr = { { "A", "B" }, { "C", "D" } };
        Matrix<String> m = Matrix.of(arr);
        String[][] returnedArray = m.array();

        assertNotNull(returnedArray);
        assertEquals(2, returnedArray.length);
        assertArrayEquals(new String[] { "A", "B" }, returnedArray[0]);
        assertArrayEquals(new String[] { "C", "D" }, returnedArray[1]);
    }

    // ============ isEmpty Tests ============

    @Test
    public void testIsEmpty_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testIsEmpty_nonEmptyMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertFalse(m.isEmpty());
    }

    @Test
    public void testIsEmpty_zeroRows() {
        IntMatrix m = IntMatrix.of(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testIsEmpty_zeroColumns() {
        IntMatrix m = IntMatrix.of(new int[5][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testIsEmpty_largeMatrix() {
        IntMatrix m = IntMatrix.of(new int[100][100]);
        assertFalse(m.isEmpty());
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy_intMatrix() {
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = original.copy();

        assertEquals(original.rows, copy.rows);
        assertEquals(original.cols, copy.cols);
        assertEquals(1, copy.get(0, 0));
        assertEquals(4, copy.get(1, 1));

        // Verify independence
        copy.set(0, 0, 99);
        assertEquals(1, original.get(0, 0));
        assertEquals(99, copy.get(0, 0));
    }

    @Test
    public void testCopy_doubleMatrix() {
        DoubleMatrix original = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        DoubleMatrix copy = original.copy();

        assertEquals(original.rows, copy.rows);
        assertEquals(original.cols, copy.cols);
        assertEquals(1.5, copy.get(0, 0), 0.0001);
        assertEquals(4.5, copy.get(1, 1), 0.0001);

        // Verify independence
        copy.set(0, 0, 99.5);
        assertEquals(1.5, original.get(0, 0), 0.0001);
        assertEquals(99.5, copy.get(0, 0), 0.0001);
    }

    @Test
    public void testCopy_objectMatrix() {
        Matrix<String> original = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        Matrix<String> copy = original.copy();

        assertEquals(original.rows, copy.rows);
        assertEquals(original.cols, copy.cols);
        assertEquals("a", copy.get(0, 0));
        assertEquals("d", copy.get(1, 1));

        // Verify independence
        copy.set(0, 0, "X");
        assertEquals("a", original.get(0, 0));
        assertEquals("X", copy.get(0, 0));
    }

    @Test
    public void testCopy_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        IntMatrix copy = empty.copy();
        assertTrue(copy.isEmpty());
    }

    @Test
    public void testCopy_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntMatrix copy = m.copy();
        assertEquals(1, copy.rows);
        assertEquals(1, copy.cols);
        assertEquals(42, copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix subset = m.copy(1, 3);

        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
        assertEquals(4, subset.get(0, 0));
        assertEquals(9, subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix subset = m.copy(1, 2);

        assertEquals(1, subset.rows);
        assertEquals(3, subset.cols);
        assertArrayEquals(new int[] { 4, 5, 6 }, subset.row(0));
    }

    @Test
    public void testCopy_withRowRange_allRows() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = m.copy(0, 2);

        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(1, copy.get(0, 0));
        assertEquals(4, copy.get(1, 1));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix submatrix = m.copy(0, 2, 1, 3);

        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
        assertEquals(2, submatrix.get(0, 0));
        assertEquals(6, submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_entireMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = m.copy(0, 2, 0, 2);

        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(1, copy.get(0, 0));
        assertEquals(4, copy.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix single = m.copy(1, 2, 1, 2);

        assertEquals(1, single.rows);
        assertEquals(1, single.cols);
        assertEquals(5, single.get(0, 0));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2, 0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3, 0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1, 0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 2, 1));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90_rectangular() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate90();

        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(6, rotated.get(2, 0));
        assertEquals(3, rotated.get(2, 1));
    }

    @Test
    public void testRotate90_square() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate90();

        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate90_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntMatrix rotated = m.rotate90();

        assertEquals(1, rotated.rows);
        assertEquals(1, rotated.cols);
        assertEquals(42, rotated.get(0, 0));
    }

    @Test
    public void testRotate90_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        IntMatrix rotated = m.rotate90();

        assertEquals(3, rotated.rows);
        assertEquals(1, rotated.cols);
        assertEquals(1, rotated.get(0, 0));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(3, rotated.get(2, 0));
    }

    @Test
    public void testRotate180_rectangular() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate180();

        assertEquals(2, rotated.rows);
        assertEquals(3, rotated.cols);
        assertEquals(6, rotated.get(0, 0));
        assertEquals(5, rotated.get(0, 1));
        assertEquals(4, rotated.get(0, 2));
        assertEquals(3, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
        assertEquals(1, rotated.get(1, 2));
    }

    @Test
    public void testRotate180_square() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate180();

        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate180_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntMatrix rotated = m.rotate180();

        assertEquals(1, rotated.rows);
        assertEquals(1, rotated.cols);
        assertEquals(42, rotated.get(0, 0));
    }

    @Test
    public void testRotate270_rectangular() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate270();

        assertEquals(3, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3, rotated.get(0, 0));
        assertEquals(6, rotated.get(0, 1));
        assertEquals(1, rotated.get(2, 0));
        assertEquals(4, rotated.get(2, 1));
    }

    @Test
    public void testRotate270_square() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate270();

        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    @Test
    public void testRotate270_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntMatrix rotated = m.rotate270();

        assertEquals(1, rotated.rows);
        assertEquals(1, rotated.cols);
        assertEquals(42, rotated.get(0, 0));
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose_rectangular() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose();

        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
        assertEquals(3, transposed.get(2, 0));
        assertEquals(6, transposed.get(2, 1));
    }

    @Test
    public void testTranspose_square() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix transposed = m.transpose();

        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(3, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(4, transposed.get(1, 1));
    }

    @Test
    public void testTranspose_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntMatrix transposed = m.transpose();

        assertEquals(1, transposed.rows);
        assertEquals(1, transposed.cols);
        assertEquals(42, transposed.get(0, 0));
    }

    @Test
    public void testTranspose_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        IntMatrix transposed = m.transpose();

        assertEquals(3, transposed.rows);
        assertEquals(1, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(3, transposed.get(2, 0));
    }

    @Test
    public void testTranspose_singleColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
        IntMatrix transposed = m.transpose();

        assertEquals(1, transposed.rows);
        assertEquals(3, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(2, transposed.get(0, 1));
        assertEquals(3, transposed.get(0, 2));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape_singleParam() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(2);

        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testReshape_singleParam_needsPadding() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(4);

        assertEquals(2, reshaped.rows);
        assertEquals(4, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(4, reshaped.get(0, 3));
        assertEquals(5, reshaped.get(1, 0));
        assertEquals(0, reshaped.get(1, 3)); // Padding with default value
    }

    @Test
    public void testReshape_twoParams() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(3, 2);

        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testReshape_twoParams_truncate() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(1, 3);

        assertEquals(1, reshaped.rows);
        assertEquals(3, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(0, 2));
    }

    // ============ isSameShape Tests ============

    @Test
    public void testIsSameShape_same() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertTrue(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_differentRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertFalse(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_differentCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertFalse(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_bothEmpty() {
        IntMatrix m1 = IntMatrix.empty();
        IntMatrix m2 = IntMatrix.empty();
        assertTrue(m1.isSameShape(m2));
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = m.repelem(2, 2);

        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(1, 0));
        assertEquals(1, repeated.get(1, 1));
        assertEquals(4, repeated.get(2, 2));
        assertEquals(4, repeated.get(3, 3));
    }

    @Test
    public void testRepelem_asymmetric() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix repeated = m.repelem(3, 2);

        assertEquals(3, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(1, 0));
        assertEquals(1, repeated.get(2, 0));
        assertEquals(2, repeated.get(2, 3));
    }

    @Test
    public void testRepelem_invalidArgs() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(-1, 1));
    }

    @Test
    public void testRepmat() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = m.repmat(2, 2);

        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2)); // Tiled
        assertEquals(1, repeated.get(2, 0)); // Tiled
        assertEquals(4, repeated.get(3, 3));
    }

    @Test
    public void testRepmat_asymmetric() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix repeated = m.repmat(3, 2);

        assertEquals(3, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2)); // Second tile
        assertEquals(1, repeated.get(1, 0)); // Second row tile
    }

    @Test
    public void testRepmat_invalidArgs() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(-1, 1));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntList flat = m.flatten();

        assertEquals(4, flat.size());
        assertEquals(1, flat.get(0));
        assertEquals(2, flat.get(1));
        assertEquals(3, flat.get(2));
        assertEquals(4, flat.get(3));
    }

    @Test
    public void testFlatten_doubleMatrix() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        DoubleList flat = m.flatten();

        assertEquals(4, flat.size());
        assertEquals(1.5, flat.get(0), 0.001);
        assertEquals(2.5, flat.get(1), 0.001);
        assertEquals(3.5, flat.get(2), 0.001);
        assertEquals(4.5, flat.get(3), 0.001);
    }

    @Test
    public void testFlatten_objectMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<String> flat = m.flatten();

        assertEquals(4, flat.size());
        assertEquals("A", flat.get(0));
        assertEquals("B", flat.get(1));
        assertEquals("C", flat.get(2));
        assertEquals("D", flat.get(3));
    }

    @Test
    public void testFlatten_emptyMatrix() {
        IntMatrix m = IntMatrix.empty();
        IntList flat = m.flatten();
        assertEquals(0, flat.size());
    }

    @Test
    public void testFlatten_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntList flat = m.flatten();

        assertEquals(1, flat.size());
        assertEquals(42, flat.get(0));
    }

    // ============ FlatOp Tests ============

    @Test
    public void testFlatOp_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 3, 1 }, { 4, 2 } });
        AtomicInteger sum = new AtomicInteger(0);
        m.flatOp(arr -> {
            for (int val : arr) {
                sum.addAndGet(val);
            }
        });
        assertEquals(10, sum.get());
    }

    @Test
    public void testFlatOp_doubleMatrix() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        final double[] sum = { 0.0 };
        m.flatOp(arr -> {
            for (double val : arr) {
                sum[0] += val;
            }
        });
        assertEquals(12.0, sum[0], 0.001);
    }

    @Test
    public void testFlatOp_objectMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        StringBuilder sb = new StringBuilder();
        m.flatOp(arr -> {
            for (String val : arr) {
                sb.append(val);
            }
        });
        assertEquals("ABCD", sb.toString());
    }

    // ============ forEach Tests ============

    @Test
    public void testForEach_biConsumer() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach((i, j) -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    @Test
    public void testForEach_biConsumer_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(1, 3, 1, 3, (i, j) -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    @Test
    public void testForEach_biConsumer_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, (i, j) -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, (i, j) -> {
        }));
    }

    @Test
    public void testForEach_withMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger sum = new AtomicInteger(0);
        m.forEach((i, j, matrix) -> sum.addAndGet(matrix.get(i, j)));
        assertEquals(10, sum.get());
    }

    @Test
    public void testForEach_withMatrix_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        AtomicInteger sum = new AtomicInteger(0);
        m.forEach(1, 3, 1, 3, (i, j, matrix) -> sum.addAndGet(matrix.get(i, j)));
        assertEquals(28, sum.get()); // 5 + 6 + 8 + 9
    }

    // ============ Adjacent Points Tests ============

    @Test
    public void testAdjacent4Points_center() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 2)));
        assertTrue(points.contains(Point.of(2, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAdjacent4Points_corner() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(2, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAdjacent8Points_center() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[3][3]);
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
        BooleanMatrix m = BooleanMatrix.of(new boolean[2][2]);
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(3, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
        assertTrue(points.contains(Point.of(1, 1)));
    }

    @Test
    public void testAdjacent4Points_centerElement() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
        assertNotNull(points.get(0)); // up
        assertNotNull(points.get(1)); // right
        assertNotNull(points.get(2)); // down
        assertNotNull(points.get(3)); // left
    }

    @Test
    public void testAdjacent4Points_cornerElement() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(2, points.size());
        assertNotNull(points.get(0)); // right
        assertNotNull(points.get(1)); // down
    }

    @Test
    public void testAdjacent8Points_centerElement() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        List<Point> points = m.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
        // All should be non-null for center element
        for (Point p : points) {
            assertNotNull(p);
        }
    }

    @Test
    public void testAdjacent8Points_cornerElement() {
        BooleanMatrix m = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(3, points.size());
        assertNotNull(points.get(0)); // right
        assertNotNull(points.get(1)); // right-down
        assertNotNull(points.get(2)); // down
    }

    // ============ Point Stream Tests ============

    @Test
    public void testPointsLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Stream<Point> points = m.pointsLU2RD();
        List<Point> list = points.toList();

        assertEquals(3, list.size());
        assertEquals(0, list.get(0).rowIndex());
        assertEquals(0, list.get(0).columnIndex());
        assertEquals(1, list.get(1).rowIndex());
        assertEquals(1, list.get(1).columnIndex());
        assertEquals(2, list.get(2).rowIndex());
        assertEquals(2, list.get(2).columnIndex());
    }

    @Test
    public void testPointsLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.pointsLU2RD().toList());
    }

    @Test
    public void testPointsRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Stream<Point> points = m.pointsRU2LD();
        List<Point> list = points.toList();

        assertEquals(3, list.size());
        assertEquals(0, list.get(0).rowIndex());
        assertEquals(2, list.get(0).columnIndex());
        assertEquals(1, list.get(1).rowIndex());
        assertEquals(1, list.get(1).columnIndex());
        assertEquals(2, list.get(2).rowIndex());
        assertEquals(0, list.get(2).columnIndex());
    }

    @Test
    public void testPointsRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.pointsRU2LD().toList());
    }

    @Test
    public void testPointsH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Stream<Point> points = m.pointsH();
        List<Point> list = points.toList();

        assertEquals(4, list.size());
        assertEquals(0, list.get(0).rowIndex());
        assertEquals(0, list.get(0).columnIndex());
        assertEquals(0, list.get(1).rowIndex());
        assertEquals(1, list.get(1).columnIndex());
        assertEquals(1, list.get(2).rowIndex());
        assertEquals(0, list.get(2).columnIndex());
    }

    @Test
    public void testPointsH_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Stream<Point> points = m.pointsH(1);
        List<Point> list = points.toList();

        assertEquals(3, list.size());
        assertEquals(1, list.get(0).rowIndex());
        assertEquals(0, list.get(0).columnIndex());
        assertEquals(1, list.get(2).rowIndex());
        assertEquals(2, list.get(2).columnIndex());
    }

    @Test
    public void testPointsH_rowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        Stream<Point> points = m.pointsH(1, 3);
        List<Point> list = points.toList();

        assertEquals(4, list.size());
        assertEquals(1, list.get(0).rowIndex());
        assertEquals(2, list.get(2).rowIndex());
    }

    @Test
    public void testPointsV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Stream<Point> points = m.pointsV();
        List<Point> list = points.toList();

        assertEquals(4, list.size());
        assertEquals(0, list.get(0).rowIndex());
        assertEquals(0, list.get(0).columnIndex());
        assertEquals(1, list.get(1).rowIndex());
        assertEquals(0, list.get(1).columnIndex());
        assertEquals(0, list.get(2).rowIndex());
        assertEquals(1, list.get(2).columnIndex());
    }

    @Test
    public void testPointsV_singleColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Stream<Point> points = m.pointsV(1);
        List<Point> list = points.toList();

        assertEquals(2, list.size());
        assertEquals(0, list.get(0).rowIndex());
        assertEquals(1, list.get(0).columnIndex());
        assertEquals(1, list.get(1).rowIndex());
        assertEquals(1, list.get(1).columnIndex());
    }

    @Test
    public void testPointsV_columnRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Stream<Point> points = m.pointsV(1, 3);
        List<Point> list = points.toList();

        assertEquals(4, list.size());
        assertEquals(1, list.get(0).columnIndex());
        assertEquals(2, list.get(2).columnIndex());
    }

    @Test
    public void testPointsR() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Stream<Stream<Point>> rows = m.pointsR();
        List<List<Point>> list = rows.map(Stream::toList).toList();

        assertEquals(2, list.size());
        assertEquals(2, list.get(0).size());
        assertEquals(0, list.get(0).get(0).rowIndex());
        assertEquals(1, list.get(1).get(0).rowIndex());
    }

    @Test
    public void testPointsR_rowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        Stream<Stream<Point>> rows = m.pointsR(1, 3);
        List<List<Point>> list = rows.map(Stream::toList).toList();

        assertEquals(2, list.size());
        assertEquals(1, list.get(0).get(0).rowIndex());
    }

    @Test
    public void testPointsC() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Stream<Stream<Point>> cols = m.pointsC();
        List<List<Point>> list = cols.map(Stream::toList).toList();

        assertEquals(2, list.size());
        assertEquals(2, list.get(0).size());
        assertEquals(0, list.get(0).get(0).columnIndex());
        assertEquals(1, list.get(1).get(0).columnIndex());
    }

    @Test
    public void testPointsC_columnRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Stream<Stream<Point>> cols = m.pointsC(1, 3);
        List<List<Point>> list = cols.map(Stream::toList).toList();

        assertEquals(2, list.size());
        assertEquals(1, list.get(0).get(0).columnIndex());
    }

    // ============ Element Stream Tests ============

    @Test
    public void testStreamLU2RD_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamLU2RD().toArray();

        assertEquals(3, diagonal.length);
        assertEquals(1, diagonal[0]);
        assertEquals(5, diagonal[1]);
        assertEquals(9, diagonal[2]);
    }

    @Test
    public void testStreamRU2LD_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamRU2LD().toArray();

        assertEquals(3, diagonal.length);
        assertEquals(3, diagonal[0]);
        assertEquals(5, diagonal[1]);
        assertEquals(7, diagonal[2]);
    }

    @Test
    public void testStreamH_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[] elements = m.streamH().toArray();

        assertEquals(4, elements.length);
        assertEquals(1, elements[0]);
        assertEquals(2, elements[1]);
        assertEquals(3, elements[2]);
        assertEquals(4, elements[3]);
    }

    @Test
    public void testStreamH_singleRow_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] row = m.streamH(1).toArray();

        assertEquals(3, row.length);
        assertEquals(4, row[0]);
        assertEquals(5, row[1]);
        assertEquals(6, row[2]);
    }

    @Test
    public void testStreamH_rowRange_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        int[] elements = m.streamH(1, 3).toArray();

        assertEquals(4, elements.length);
        assertEquals(3, elements[0]);
        assertEquals(4, elements[1]);
        assertEquals(5, elements[2]);
        assertEquals(6, elements[3]);
    }

    @Test
    public void testStreamV_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[] elements = m.streamV().toArray();

        assertEquals(4, elements.length);
        assertEquals(1, elements[0]);
        assertEquals(3, elements[1]);
        assertEquals(2, elements[2]);
        assertEquals(4, elements[3]);
    }

    @Test
    public void testStreamV_singleColumn_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] col = m.streamV(1).toArray();

        assertEquals(2, col.length);
        assertEquals(2, col[0]);
        assertEquals(5, col[1]);
    }

    @Test
    public void testStreamV_columnRange_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] elements = m.streamV(1, 3).toArray();

        assertEquals(4, elements.length);
        assertEquals(2, elements[0]);
        assertEquals(5, elements[1]);
        assertEquals(3, elements[2]);
        assertEquals(6, elements[3]);
    }

    // ============ Row/Column Stream Tests ============

    @Test
    public void testStreamR_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<int[]> rows = m.streamR().map(s -> s.toArray()).toList();

        assertEquals(2, rows.size());
        assertArrayEquals(new int[] { 1, 2 }, rows.get(0));
        assertArrayEquals(new int[] { 3, 4 }, rows.get(1));
    }

    @Test
    public void testStreamR_rowRange_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<int[]> rows = m.streamR(1, 3).map(s -> s.toArray()).toList();

        assertEquals(2, rows.size());
        assertArrayEquals(new int[] { 3, 4 }, rows.get(0));
        assertArrayEquals(new int[] { 5, 6 }, rows.get(1));
    }

    @Test
    public void testStreamC_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<int[]> cols = m.streamC().map(s -> s.toArray()).toList();

        assertEquals(2, cols.size());
        assertArrayEquals(new int[] { 1, 3 }, cols.get(0));
        assertArrayEquals(new int[] { 2, 4 }, cols.get(1));
    }

    @Test
    public void testStreamC_columnRange_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<int[]> cols = m.streamC(1, 3).map(s -> s.toArray()).toList();

        assertEquals(2, cols.size());
        assertArrayEquals(new int[] { 2, 5 }, cols.get(0));
        assertArrayEquals(new int[] { 3, 6 }, cols.get(1));
    }

    // ============ Accept and Apply Tests ============

    @Test
    public void testAccept() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger sum = new AtomicInteger(0);
        m.accept(matrix -> {
            for (int i = 0; i < matrix.rows; i++) {
                for (int j = 0; j < matrix.cols; j++) {
                    sum.addAndGet(matrix.get(i, j));
                }
            }
        });
        assertEquals(10, sum.get());
    }

    @Test
    public void testApply() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int sum = m.apply(matrix -> {
            int total = 0;
            for (int i = 0; i < matrix.rows; i++) {
                for (int j = 0; j < matrix.cols; j++) {
                    total += matrix.get(i, j);
                }
            }
            return total;
        });
        assertEquals(10, sum);
    }

    @Test
    public void testApply_returnsMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.apply(matrix -> matrix.transpose());
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(3, result.get(0, 1));
    }

    // ============ Println Tests ============

    @Test
    public void testPrintln_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String output = m.println();
        assertNotNull(output);
        assertTrue(output.contains("1"));
        assertTrue(output.contains("4"));
    }

    @Test
    public void testPrintln_doubleMatrix() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        String output = m.println();
        assertNotNull(output);
        assertTrue(output.contains("1.5") || output.contains("1"));
    }

    @Test
    public void testPrintln_objectMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String output = m.println();
        assertNotNull(output);
        assertTrue(output.contains("A"));
        assertTrue(output.contains("D"));
    }

    @Test
    public void testPrintln_emptyMatrix() {
        IntMatrix m = IntMatrix.empty();
        String output = m.println();
        assertNotNull(output);
    }
}
