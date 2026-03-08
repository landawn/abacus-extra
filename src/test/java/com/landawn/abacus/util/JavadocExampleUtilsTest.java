package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * Tests verifying Javadoc examples from ImmutableIntArray.java, AbstractMatrix.java,
 * Matrices.java, and ParallelMode.java.
 */
public class JavadocExampleUtilsTest {

    // ==================== ImmutableIntArray Javadoc Examples ====================

    @Test
    public void testImmutableIntArray_unsafeWrap_get() {
        // From unsafeWrap Javadoc
        int[] data = new int[] { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
        assertEquals(20, array.get(1)); // prints: 20

        // CAUTION: Modifying the original array affects the ImmutableIntArray
        data[1] = 99;
        assertEquals(99, array.get(1)); // prints: 99 (not recommended!)
    }

    @Test
    public void testImmutableIntArray_copyOf() {
        // From copyOf Javadoc
        int[] data = { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);
        assertEquals(10, array.get(0)); // prints: 10

        // Modifying the original array does NOT affect the ImmutableIntArray
        data[0] = 99;
        assertEquals(10, array.get(0)); // still prints: 10
    }

    @Test
    public void testImmutableIntArray_isEmpty() {
        // From isEmpty Javadoc
        ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
        assertTrue(empty.isEmpty()); // returns true

        ImmutableIntArray nonEmpty = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        assertFalse(nonEmpty.isEmpty()); // returns false

        ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
        assertTrue(fromNull.isEmpty()); // returns true (null becomes empty array)
    }

    @Test
    public void testImmutableIntArray_length() {
        // From length Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        assertEquals(3, array.length()); // returns 3
    }

    @Test
    public void testImmutableIntArray_contains() {
        // From contains Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
        assertTrue(array.contains(30)); // returns true
        assertFalse(array.contains(99)); // returns false

        ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
        assertFalse(empty.contains(1)); // returns false
    }

    @Test
    public void testImmutableIntArray_get() {
        // From get Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 5, 10, 15, 20 });
        assertEquals(10, array.get(1)); // returns 10
        assertEquals(5, array.get(0)); // returns 5
        assertEquals(20, array.get(array.length() - 1)); // returns 20
    }

    @Test
    public void testImmutableIntArray_forEach_sum() {
        // From forEach Javadoc: Accumulate sum
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
        int[] sum = { 0 };
        array.forEach(value -> sum[0] += value);
        assertEquals(15, sum[0]); // prints: 15
    }

    @Test
    public void testImmutableIntArray_stream_sum() {
        // From stream Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

        int sum = array.stream().sum(); // returns 15
        assertEquals(15, sum);
    }

    @Test
    public void testImmutableIntArray_stream_max() {
        // From stream Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

        int max = array.stream().max().orElse(0); // returns 5
        assertEquals(5, max);
    }

    @Test
    public void testImmutableIntArray_stream_filter() {
        // From stream Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

        int[] evens = array.stream().filter(x -> x % 2 == 0).toArray(); // returns {2, 4}
        assertArrayEquals(new int[] { 2, 4 }, evens);
    }

    @Test
    public void testImmutableIntArray_stream_mapAndSum() {
        // From stream Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

        int sumOfSquares = array.stream().map(x -> x * x).sum(); // returns 55 (1 + 4 + 9 + 16 + 25)
        assertEquals(55, sumOfSquares);
    }

    @Test
    public void testImmutableIntArray_subArray() {
        // From subArray Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

        int[] subArray = array.subArray(1, 4);
        assertArrayEquals(new int[] { 20, 30, 40 }, subArray); // contains {20, 30, 40}

        int[] first3 = array.subArray(0, 3);
        assertArrayEquals(new int[] { 10, 20, 30 }, first3); // contains {10, 20, 30}

        int[] empty = array.subArray(2, 2);
        assertEquals(0, empty.length); // empty array with length 0
    }

    @Test
    public void testImmutableIntArray_hashCode_equals() {
        // From hashCode Javadoc
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });

        assertEquals(array1.hashCode(), array2.hashCode()); // equal arrays have same hash
    }

    @Test
    public void testImmutableIntArray_equals() {
        // From equals Javadoc
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });
        ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

        assertTrue(array1.equals(array2)); // true
        assertFalse(array1.equals(array3)); // false
        assertFalse(array1.equals(new int[] { 1, 2, 3 })); // false (different type)
    }

    @Test
    public void testImmutableIntArray_toString() {
        // From toString Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        assertEquals("[1, 2, 3]", array.toString()); // prints: [1, 2, 3]

        ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
        assertEquals("[]", empty.toString()); // prints: []

        ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] { 42 });
        assertEquals("[42]", single.toString()); // prints: [42]
    }

    // ==================== AbstractMatrix (via IntMatrix) Javadoc Examples ====================

    @Test
    public void testAbstractMatrix_componentType() {
        // From componentType Javadoc
        IntMatrix intMatrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(int.class, intMatrix.componentType()); // Returns int.class
    }

    @Test
    public void testAbstractMatrix_rowView() {
        // From rowView Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] row0 = matrix.rowView(0); // Returns [1, 2, 3] (direct reference)
        assertArrayEquals(new int[] { 1, 2, 3 }, row0);
        row0[0] = 99; // Also changes matrix element at (0, 0) to 99
        assertEquals(99, matrix.get(0, 0));
    }

    @Test
    public void testAbstractMatrix_rowCopy() {
        // From rowCopy Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] rowCopy = matrix.rowCopy(0); // Returns [1, 2, 3] (independent copy)
        assertArrayEquals(new int[] { 1, 2, 3 }, rowCopy);
        rowCopy[0] = 99; // Does NOT affect the original matrix
        assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testAbstractMatrix_columnCopy() {
        // From columnCopy Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] colCopy = matrix.columnCopy(1); // Returns [2, 5] (independent copy)
        assertArrayEquals(new int[] { 2, 5 }, colCopy);
        colCopy[0] = 99; // Does NOT affect the original matrix
        assertEquals(2, matrix.get(0, 1));
    }

    @Test
    public void testAbstractMatrix_rowCount() {
        // From rowCount Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(2, matrix.rowCount()); // Returns 2
    }

    @Test
    public void testAbstractMatrix_columnCount() {
        // From columnCount Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(3, matrix.columnCount()); // Returns 3
    }

    @Test
    public void testAbstractMatrix_elementCount() {
        // From elementCount Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(6, matrix.elementCount()); // Returns 6
    }

    @Test
    public void testAbstractMatrix_isEmpty() {
        // From isEmpty Javadoc
        IntMatrix empty = IntMatrix.of(new int[0][0]);
        assertTrue(empty.isEmpty()); // Returns true (0x0)

        IntMatrix notEmpty = IntMatrix.of(new int[][] { { 1 } });
        assertFalse(notEmpty.isEmpty()); // Returns false (1x1)
    }

    @Test
    public void testAbstractMatrix_backingArray() {
        // From backingArray Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[][] array = matrix.backingArray();
        array[0][0] = 10; // This WILL modify the matrix!
        assertEquals(10, matrix.get(0, 0));
    }

    @Test
    public void testAbstractMatrix_copy() {
        // From copy Javadoc
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = original.copy();
        copy.set(0, 0, 10); // Original matrix remains unchanged
        assertEquals(1, original.get(0, 0));
        assertEquals(10, copy.get(0, 0));
    }

    @Test
    public void testAbstractMatrix_copyRowRange() {
        // From copy(fromRowIndex, toRowIndex) Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix subMatrix = matrix.copy(0, 2); // Contains rows 0 and 1
        assertEquals(2, subMatrix.rowCount());
        assertEquals(2, subMatrix.columnCount());
        assertEquals(1, subMatrix.get(0, 0));
        assertEquals(4, subMatrix.get(1, 1));

        IntMatrix lastRow = matrix.copy(2, 3); // Contains only row 2
        assertEquals(1, lastRow.rowCount());
        assertEquals(5, lastRow.get(0, 0));
        assertEquals(6, lastRow.get(0, 1));
    }

    @Test
    public void testAbstractMatrix_copyRegion() {
        // From copy(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex) Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix subMatrix = matrix.copy(0, 2, 1, 3);
        // subMatrix: {{2, 3}, {5, 6}} (rows 0-1, columns 1-2)
        assertEquals(2, subMatrix.rowCount());
        assertEquals(2, subMatrix.columnCount());
        assertEquals(2, subMatrix.get(0, 0));
        assertEquals(3, subMatrix.get(0, 1));
        assertEquals(5, subMatrix.get(1, 0));
        assertEquals(6, subMatrix.get(1, 1));

        IntMatrix centerElement = matrix.copy(1, 2, 1, 2);
        // centerElement: {{5}} (just the center element)
        assertEquals(1, centerElement.rowCount());
        assertEquals(1, centerElement.columnCount());
        assertEquals(5, centerElement.get(0, 0));
    }

    @Test
    public void testAbstractMatrix_rotate90() {
        // From rotate90 Javadoc
        // Original:    Rotated 90 degrees clockwise:
        // 1 2 3        7 4 1
        // 4 5 6   =>   8 5 2
        // 7 8 9        9 6 3
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix rotated = original.rotate90();
        assertEquals(7, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(0, 2));
        assertEquals(8, rotated.get(1, 0));
        assertEquals(5, rotated.get(1, 1));
        assertEquals(2, rotated.get(1, 2));
        assertEquals(9, rotated.get(2, 0));
        assertEquals(6, rotated.get(2, 1));
        assertEquals(3, rotated.get(2, 2));
    }

    @Test
    public void testAbstractMatrix_rotate180() {
        // From rotate180 Javadoc
        // Original:    Rotated 180 degrees:
        // 1 2 3        9 8 7
        // 4 5 6   =>   6 5 4
        // 7 8 9        3 2 1
        // Note: the Javadoc uses a 2x3 matrix for the code but shows 3x3 in diagram.
        // Let's test the 3x3 diagram.
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix rotated = original.rotate180();
        assertEquals(9, rotated.get(0, 0));
        assertEquals(8, rotated.get(0, 1));
        assertEquals(7, rotated.get(0, 2));
        assertEquals(6, rotated.get(1, 0));
        assertEquals(5, rotated.get(1, 1));
        assertEquals(4, rotated.get(1, 2));
        assertEquals(3, rotated.get(2, 0));
        assertEquals(2, rotated.get(2, 1));
        assertEquals(1, rotated.get(2, 2));
    }

    @Test
    public void testAbstractMatrix_rotate270() {
        // From rotate270 Javadoc
        // Original:    Rotated 270 degrees clockwise:
        // 1 2 3        3 6 9
        // 4 5 6   =>   2 5 8
        // 7 8 9        1 4 7
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix rotated = original.rotate270();
        assertEquals(3, rotated.get(0, 0));
        assertEquals(6, rotated.get(0, 1));
        assertEquals(9, rotated.get(0, 2));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(5, rotated.get(1, 1));
        assertEquals(8, rotated.get(1, 2));
        assertEquals(1, rotated.get(2, 0));
        assertEquals(4, rotated.get(2, 1));
        assertEquals(7, rotated.get(2, 2));
    }

    @Test
    public void testAbstractMatrix_transpose() {
        // From transpose Javadoc
        // 2x3 becomes 3x2
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = original.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
        assertEquals(3, transposed.get(2, 0));
        assertEquals(6, transposed.get(2, 1));
    }

    @Test
    public void testAbstractMatrix_reshape_singleArg() {
        // From reshape(int) Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = matrix.reshape(2); // Becomes [[1, 2], [3, 4], [5, 6]]
        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(4, reshaped.get(1, 1));
        assertEquals(5, reshaped.get(2, 0));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testAbstractMatrix_reshape_padding() {
        // From reshape(int) Javadoc: padding
        IntMatrix matrix2 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped2 = matrix2.reshape(4); // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
        assertEquals(2, reshaped2.rowCount());
        assertEquals(4, reshaped2.columnCount());
        assertEquals(1, reshaped2.get(0, 0));
        assertEquals(2, reshaped2.get(0, 1));
        assertEquals(3, reshaped2.get(0, 2));
        assertEquals(4, reshaped2.get(0, 3));
        assertEquals(5, reshaped2.get(1, 0));
        assertEquals(6, reshaped2.get(1, 1));
        assertEquals(0, reshaped2.get(1, 2));
        assertEquals(0, reshaped2.get(1, 3));
    }

    @Test
    public void testAbstractMatrix_reshape_twoArgs() {
        // From reshape(int, int) Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1, 2], [3, 4], [5, 6]]
        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(4, reshaped.get(1, 1));
        assertEquals(5, reshaped.get(2, 0));
        assertEquals(6, reshaped.get(2, 1));

        IntMatrix extended = matrix.reshape(2, 4); // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
        assertEquals(2, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(4, extended.get(0, 3));
        assertEquals(0, extended.get(1, 2));
    }

    @Test
    public void testAbstractMatrix_isSameShape() {
        // From isSameShape Javadoc
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertTrue(m1.isSameShape(m2)); // Returns true (both are 2x2)

        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(m1.isSameShape(m3)); // Returns false (2x2 vs 2x3)
    }

    @Test
    public void testAbstractMatrix_repeatElements() {
        // From repeatElements Javadoc
        // Original:    repeatElements(2, 2):
        // 1 2          1 1 2 2
        // 3 4     =>   1 1 2 2
        //              3 3 4 4
        //              3 3 4 4
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = matrix.repeatElements(2, 2);
        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(2, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
        assertEquals(1, repeated.get(1, 0));
        assertEquals(1, repeated.get(1, 1));
        assertEquals(2, repeated.get(1, 2));
        assertEquals(2, repeated.get(1, 3));
        assertEquals(3, repeated.get(2, 0));
        assertEquals(3, repeated.get(2, 1));
        assertEquals(4, repeated.get(2, 2));
        assertEquals(4, repeated.get(2, 3));
        assertEquals(3, repeated.get(3, 0));
        assertEquals(3, repeated.get(3, 1));
        assertEquals(4, repeated.get(3, 2));
        assertEquals(4, repeated.get(3, 3));
    }

    @Test
    public void testAbstractMatrix_repeatMatrix() {
        // From repeatMatrix Javadoc
        // Original:    repeatMatrix(2, 2):
        // 1 2          1 2 1 2
        // 3 4     =>   3 4 3 4
        //              1 2 1 2
        //              3 4 3 4
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix tiled = matrix.repeatMatrix(2, 2);
        assertEquals(4, tiled.rowCount());
        assertEquals(4, tiled.columnCount());
        assertEquals(1, tiled.get(0, 0));
        assertEquals(2, tiled.get(0, 1));
        assertEquals(1, tiled.get(0, 2));
        assertEquals(2, tiled.get(0, 3));
        assertEquals(3, tiled.get(1, 0));
        assertEquals(4, tiled.get(1, 1));
        assertEquals(3, tiled.get(1, 2));
        assertEquals(4, tiled.get(1, 3));
        assertEquals(1, tiled.get(2, 0));
        assertEquals(2, tiled.get(2, 1));
        assertEquals(1, tiled.get(2, 2));
        assertEquals(2, tiled.get(2, 3));
        assertEquals(3, tiled.get(3, 0));
        assertEquals(4, tiled.get(3, 1));
        assertEquals(3, tiled.get(3, 2));
        assertEquals(4, tiled.get(3, 3));
    }

    @Test
    public void testAbstractMatrix_flatten() {
        // From flatten Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntList flat = matrix.flatten(); // Returns [1, 2, 3, 4]
        assertEquals("[1, 2, 3, 4]", flat.toString());

        IntMatrix matrix2 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntList flat2 = matrix2.flatten(); // Returns [1, 2, 3, 4, 5, 6]
        assertEquals("[1, 2, 3, 4, 5, 6]", flat2.toString());
    }

    @Test
    public void testAbstractMatrix_applyOnFlattened() {
        // From applyOnFlattened Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 3, 1, 4 }, { 1, 5, 9 } });
        matrix.applyOnFlattened(a -> java.util.Arrays.sort(a)); // Sorts all elements
        // Matrix becomes [[1, 1, 3], [4, 5, 9]] (elements sorted in row-major order)
        assertEquals(1, matrix.get(0, 0));
        assertEquals(1, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(9, matrix.get(1, 2));
    }

    @Test
    public void testAbstractMatrix_adjacent4Points_corner() {
        // From adjacent4Points Javadoc
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Stream<Point> adjacent = matrix.adjacent4Points(0, 0);
        // Returns stream of Point(0, 1) and Point(1, 0) - only right and down exist
        List<Point> points = adjacent.toList();
        assertEquals(2, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAbstractMatrix_adjacent4Points_center() {
        // From adjacent4Points Javadoc
        IntMatrix larger = IntMatrix.of(new int[3][3]);
        Stream<Point> centerAdj = larger.adjacent4Points(1, 1);
        // Returns all 4 adjacent points: (0,1), (1,2), (2,1), (1,0)
        List<Point> points = centerAdj.toList();
        assertEquals(4, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 2)));
        assertTrue(points.contains(Point.of(2, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAbstractMatrix_adjacent8Points_corner() {
        // From adjacent8Points Javadoc
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        Stream<Point> corner = matrix.adjacent8Points(0, 0);
        // Returns 3 points: (0,1), (1,1), (1,0)
        List<Point> points = corner.toList();
        assertEquals(3, points.size());
        assertTrue(points.contains(Point.of(0, 1)));
        assertTrue(points.contains(Point.of(1, 1)));
        assertTrue(points.contains(Point.of(1, 0)));
    }

    @Test
    public void testAbstractMatrix_adjacent8Points_center() {
        // From adjacent8Points Javadoc
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false, true }, { false, true, false }, { true, false, true } });
        Stream<Point> adjacent = matrix.adjacent8Points(1, 1);
        // Returns stream of all 8 surrounding points for the center position
        List<Point> points = adjacent.toList();
        assertEquals(8, points.size());
    }

    // ==================== Matrices Javadoc Examples ====================

    @Test
    public void testMatrices_isSameShape_2() {
        // From Matrices.isSameShape(a, b) Javadoc
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }); // 2x2
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }); // 2x2
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }); // 2x3

        assertTrue(Matrices.isSameShape(m1, m2)); // true
        assertFalse(Matrices.isSameShape(m1, m3)); // false
    }

    @Test
    public void testMatrices_isSameShape_3() {
        // From Matrices.isSameShape(a, b, c) Javadoc
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        assertTrue(Matrices.isSameShape(m1, m2, m3)); // true
    }

    @Test
    public void testMatrices_isParallelizable_count() {
        // From isParallelizable(matrix, count) Javadoc
        // Default mode is AUTO, count 5000 < 8192 so should return false
        Matrices.setParallelMode(ParallelMode.AUTO);
        IntMatrix matrix = IntMatrix.of(new int[100][100]);
        boolean shouldParallelize = Matrices.isParallelizable(matrix, 5000);
        // Returns true only if settings allow and count >= 8192
        // Since 5000 < 8192, in AUTO mode this returns false
        assertFalse(shouldParallelize);
    }

    @Test
    public void testMatrices_getSetParallelMode() {
        // From getParallelMode Javadoc
        ParallelMode original = Matrices.getParallelMode();
        try {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());

            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());

            Matrices.setParallelMode(ParallelMode.AUTO);
            assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
        } finally {
            Matrices.setParallelMode(original);
        }
    }

    @Test
    public void testMatrices_runWithParallelMode() {
        // From runWithParallelMode Javadoc
        ParallelMode original = Matrices.getParallelMode();
        try {
            Matrices.setParallelMode(ParallelMode.AUTO);

            Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
            });

            // After execution, the original setting is restored
            assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
        } finally {
            Matrices.setParallelMode(original);
        }
    }
}
