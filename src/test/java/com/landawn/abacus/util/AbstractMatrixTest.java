package com.landawn.abacus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class AbstractMatrixTest extends TestBase {

    // Since AbstractMatrix is abstract, we'll test it through IntMatrix which is a concrete implementation
    private IntMatrix createTestMatrix() {
        return IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
    }

    private IntMatrix createTestMatrix2x3() {
        return IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
    }

    private IntMatrix createTestMatrix3x2() {
        return IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
    }

    @Test
    public void testConstructor() {
        IntMatrix matrix = createTestMatrix();
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(9, matrix.elementCount());
    }

    @Test
    public void testConstructorWithInconsistentRows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4, 5 } });
        });
    }

    @Test
    public void testComponentType() {
        IntMatrix matrix = createTestMatrix();
        Assertions.assertEquals(int.class, matrix.componentType());
    }

    @Test
    public void testArray() {
        IntMatrix matrix = createTestMatrix();
        int[][] array = matrix.array();
        Assertions.assertEquals(3, array.length);
        Assertions.assertEquals(3, array[0].length);
        Assertions.assertEquals(1, array[0][0]);
    }

    @Test
    public void testIsEmpty() {
        IntMatrix matrix = createTestMatrix();
        Assertions.assertFalse(matrix.isEmpty());

        IntMatrix emptyMatrix = IntMatrix.of(new int[0][0]);
        Assertions.assertTrue(emptyMatrix.isEmpty());
    }

    @Test
    public void testCopy() {
        IntMatrix matrix = createTestMatrix();
        IntMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rowCount(), copy.rowCount());
        Assertions.assertEquals(matrix.columnCount(), copy.columnCount());
        Assertions.assertEquals(matrix.get(0, 0), copy.get(0, 0));
        Assertions.assertEquals(matrix.get(2, 2), copy.get(2, 2));

        // Verify they are independent
        copy.set(0, 0, 100);
        Assertions.assertNotEquals(matrix.get(0, 0), copy.get(0, 0));
    }

    @Test
    public void testCopyRowRange() {
        IntMatrix matrix = createTestMatrix();
        IntMatrix copy = matrix.copy(0, 2);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(3, copy.columnCount());
        Assertions.assertEquals(1, copy.get(0, 0));
        Assertions.assertEquals(6, copy.get(1, 2));
    }

    @Test
    public void testCopyRowRangeInvalidIndices() {
        IntMatrix matrix = createTestMatrix();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.copy(-1, 2);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.copy(0, 4);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.copy(2, 1);
        });
    }

    @Test
    public void testCopyRegion() {
        IntMatrix matrix = createTestMatrix();
        IntMatrix copy = matrix.copy(1, 3, 1, 3);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals(5, copy.get(0, 0));
        Assertions.assertEquals(6, copy.get(0, 1));
        Assertions.assertEquals(8, copy.get(1, 0));
        Assertions.assertEquals(9, copy.get(1, 1));
    }

    @Test
    public void testCopyRegionInvalidIndices() {
        IntMatrix matrix = createTestMatrix();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.copy(0, 2, -1, 2);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.copy(0, 2, 0, 4);
        });
    }

    @Test
    public void testRotate90() {
        IntMatrix matrix = createTestMatrix2x3();
        IntMatrix rotated = matrix.rotate90();

        Assertions.assertEquals(3, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals(4, rotated.get(0, 0));
        Assertions.assertEquals(1, rotated.get(0, 1));
        Assertions.assertEquals(5, rotated.get(1, 0));
        Assertions.assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        IntMatrix matrix = createTestMatrix2x3();
        IntMatrix rotated = matrix.rotate180();

        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(3, rotated.columnCount());
        Assertions.assertEquals(6, rotated.get(0, 0));
        Assertions.assertEquals(5, rotated.get(0, 1));
        Assertions.assertEquals(4, rotated.get(0, 2));
        Assertions.assertEquals(3, rotated.get(1, 0));
    }

    @Test
    public void testRotate270() {
        IntMatrix matrix = createTestMatrix2x3();
        IntMatrix rotated = matrix.rotate270();

        Assertions.assertEquals(3, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals(3, rotated.get(0, 0));
        Assertions.assertEquals(6, rotated.get(0, 1));
        Assertions.assertEquals(2, rotated.get(1, 0));
        Assertions.assertEquals(5, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        IntMatrix matrix = createTestMatrix2x3();
        IntMatrix transposed = matrix.transpose();

        Assertions.assertEquals(3, transposed.rowCount());
        Assertions.assertEquals(2, transposed.columnCount());
        Assertions.assertEquals(1, transposed.get(0, 0));
        Assertions.assertEquals(4, transposed.get(0, 1));
        Assertions.assertEquals(2, transposed.get(1, 0));
        Assertions.assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testReshapeWithCols() {
        IntMatrix matrix = createTestMatrix2x3();
        IntMatrix reshaped = matrix.reshape(2);

        Assertions.assertEquals(3, reshaped.rowCount());
        Assertions.assertEquals(2, reshaped.columnCount());
        Assertions.assertEquals(1, reshaped.get(0, 0));
        Assertions.assertEquals(2, reshaped.get(0, 1));
        Assertions.assertEquals(3, reshaped.get(1, 0));
    }

    @Test
    public void testReshapeWithRowsAndCols() {
        IntMatrix matrix = createTestMatrix2x3();
        IntMatrix reshaped = matrix.reshape(3, 2);

        Assertions.assertEquals(3, reshaped.rowCount());
        Assertions.assertEquals(2, reshaped.columnCount());
        Assertions.assertEquals(1, reshaped.get(0, 0));
        Assertions.assertEquals(2, reshaped.get(0, 1));
        Assertions.assertEquals(3, reshaped.get(1, 0));
        Assertions.assertEquals(4, reshaped.get(1, 1));
    }

    @Test
    public void testIsSameShape() {
        IntMatrix matrix1 = createTestMatrix();
        IntMatrix matrix2 = IntMatrix.of(new int[][] { { 10, 20, 30 }, { 40, 50, 60 }, { 70, 80, 90 } });
        IntMatrix matrix3 = createTestMatrix2x3();

        Assertions.assertTrue(matrix1.isSameShape(matrix2));
        Assertions.assertFalse(matrix1.isSameShape(matrix3));
    }

    @Test
    public void testRepelem() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = matrix.repelem(2, 3);

        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals(1, repeated.get(0, 0));
        Assertions.assertEquals(1, repeated.get(0, 2));
        Assertions.assertEquals(1, repeated.get(1, 0));
        Assertions.assertEquals(2, repeated.get(0, 3));
    }

    @Test
    public void testRepelemInvalidArgs() {
        IntMatrix matrix = createTestMatrix();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.repelem(0, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.repelem(1, 0);
        });
    }

    @Test
    public void testRepmat() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix tiled = matrix.repmat(2, 3);

        tiled.println();

        Assertions.assertEquals(4, tiled.rowCount());
        Assertions.assertEquals(6, tiled.columnCount());
        Assertions.assertEquals(1, tiled.get(0, 0));
        Assertions.assertEquals(2, tiled.get(0, 1));
        Assertions.assertEquals(1, tiled.get(0, 2));
        Assertions.assertEquals(3, tiled.get(1, 0));
    }

    @Test
    public void testRepmatInvalidArgs() {
        IntMatrix matrix = createTestMatrix();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.repmat(0, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.repmat(1, -1);
        });
    }

    @Test
    public void testFlatten() {
        IntMatrix matrix = createTestMatrix2x3();
        IntList flat = matrix.flatten();

        Assertions.assertEquals(6, flat.size());
        Assertions.assertEquals(1, flat.get(0));
        Assertions.assertEquals(2, flat.get(1));
        Assertions.assertEquals(3, flat.get(2));
        Assertions.assertEquals(4, flat.get(3));
        Assertions.assertEquals(5, flat.get(4));
        Assertions.assertEquals(6, flat.get(5));
    }

    @Test
    public void testFlatOp() throws Exception {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 3, 1, 4 }, { 1, 5, 9 } });

        matrix.flatOp(arrays -> java.util.Arrays.sort(arrays));

        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(1, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
    }

    @Test
    public void testForEachWithIndices() throws Exception {
        IntMatrix matrix = createTestMatrix2x3();
        List<String> positions = new ArrayList<>();

        matrix.forEach((i, j) -> positions.add(i + "," + j));

        Assertions.assertEquals(6, positions.size());
        Assertions.assertTrue(positions.contains("0,0"));
        Assertions.assertTrue(positions.contains("1,2"));
    }

    @Test
    public void testForEachWithIndicesRegion() throws Exception {
        IntMatrix matrix = createTestMatrix();
        List<String> positions = new ArrayList<>();

        matrix.forEach(1, 3, 1, 3, (i, j) -> positions.add(i + "," + j));

        Assertions.assertEquals(4, positions.size());
        Assertions.assertTrue(positions.contains("1,1"));
        Assertions.assertTrue(positions.contains("1,2"));
        Assertions.assertTrue(positions.contains("2,1"));
        Assertions.assertTrue(positions.contains("2,2"));
    }

    @Test
    public void testForEachWithMatrix() throws Exception {
        IntMatrix matrix = createTestMatrix2x3();
        List<Integer> values = new ArrayList<>();

        matrix.forEach((i, j, m) -> values.add(m.get(i, j)));

        Assertions.assertEquals(6, values.size());
        Assertions.assertEquals(1, values.get(0));
        Assertions.assertEquals(6, values.get(5));
    }

    @Test
    public void testForEachWithMatrixRegion() throws Exception {
        IntMatrix matrix = createTestMatrix();
        List<Integer> values = new ArrayList<>();

        matrix.forEach(0, 2, 0, 2, (i, j, m) -> values.add(m.get(i, j)));

        Assertions.assertEquals(4, values.size());
        Assertions.assertEquals(1, values.get(0));
        Assertions.assertEquals(2, values.get(1));
        Assertions.assertEquals(4, values.get(2));
        Assertions.assertEquals(5, values.get(3));
    }

    @Test
    public void testPointsLU2RD() {
        IntMatrix matrix = createTestMatrix();
        List<Sheet.Point> points = matrix.pointsLU2RD().collect(Collectors.toList());

        Assertions.assertEquals(3, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(2, 2), points.get(2));
    }

    @Test
    public void testPointsLU2RDNonSquare() {
        IntMatrix matrix = createTestMatrix2x3();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            matrix.pointsLU2RD().collect(Collectors.toList());
        });
    }

    @Test
    public void testPointsRU2LD() {
        IntMatrix matrix = createTestMatrix();
        List<Sheet.Point> points = matrix.pointsRU2LD().collect(Collectors.toList());

        Assertions.assertEquals(3, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 2), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsH() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Sheet.Point> points = matrix.pointsH().collect(Collectors.toList());

        Assertions.assertEquals(4, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(2));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsHRow() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Sheet.Point> points = matrix.pointsH(1).collect(Collectors.toList());

        Assertions.assertEquals(3, points.size());
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(1, 2), points.get(2));
    }

    @Test
    public void testPointsHRange() {
        IntMatrix matrix = createTestMatrix();
        List<Sheet.Point> points = matrix.pointsH(1, 3).collect(Collectors.toList());

        Assertions.assertEquals(6, points.size());
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(2, 2), points.get(5));
    }

    @Test
    public void testPointsV() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Sheet.Point> points = matrix.pointsV().collect(Collectors.toList());

        Assertions.assertEquals(4, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(2));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsVColumn() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Sheet.Point> points = matrix.pointsV(1).collect(Collectors.toList());

        Assertions.assertEquals(2, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
    }

    @Test
    public void testPointsVRange() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Sheet.Point> points = matrix.pointsV(1, 3).collect(Collectors.toList());

        Assertions.assertEquals(4, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 2), points.get(3));
    }

    @Test
    public void testPointsR() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Sheet.Point>> rows = matrix.pointsR().map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), rows.get(0).get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 2), rows.get(1).get(2));
    }

    @Test
    public void testPointsRRange() {
        IntMatrix matrix = createTestMatrix();
        List<List<Sheet.Point>> rows = matrix.pointsR(1, 3).map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(1, 0), rows.get(0).get(0));
    }

    @Test
    public void testPointsC() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Sheet.Point>> columnCount = matrix.pointsC().map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), columnCount.get(0).get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 2), columnCount.get(2).get(1));
    }

    @Test
    public void testPointsCRange() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Sheet.Point>> columnCount = matrix.pointsC(1, 3).map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(0, 1), columnCount.get(0).get(0));
    }

    @Test
    public void testStreamLU2RD() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> diagonal = matrix.streamLU2RD().boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, diagonal.size());
        Assertions.assertEquals(1, diagonal.get(0));
        Assertions.assertEquals(5, diagonal.get(1));
        Assertions.assertEquals(9, diagonal.get(2));
    }

    @Test
    public void testStreamRU2LD() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> diagonal = matrix.streamRU2LD().boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, diagonal.size());
        Assertions.assertEquals(3, diagonal.get(0));
        Assertions.assertEquals(5, diagonal.get(1));
        Assertions.assertEquals(7, diagonal.get(2));
    }

    @Test
    public void testStreamH() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Integer> elements = matrix.streamH().boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, elements.size());
        Assertions.assertEquals(1, elements.get(0));
        Assertions.assertEquals(6, elements.get(5));
    }

    @Test
    public void testStreamHRow() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> row = matrix.streamH(1).boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, row.size());
        Assertions.assertEquals(4, row.get(0));
        Assertions.assertEquals(5, row.get(1));
        Assertions.assertEquals(6, row.get(2));
    }

    @Test
    public void testStreamHRange() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> rows = matrix.streamH(1, 3).boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, rows.size());
        Assertions.assertEquals(4, rows.get(0));
        Assertions.assertEquals(9, rows.get(5));
    }

    @Test
    public void testStreamV() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Integer> elements = matrix.streamV().boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, elements.size());
        Assertions.assertEquals(1, elements.get(0));
        Assertions.assertEquals(4, elements.get(1));
        Assertions.assertEquals(2, elements.get(2));
    }

    @Test
    public void testStreamVColumn() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> col = matrix.streamV(1).boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, col.size());
        Assertions.assertEquals(2, col.get(0));
        Assertions.assertEquals(5, col.get(1));
        Assertions.assertEquals(8, col.get(2));
    }

    @Test
    public void testStreamVRange() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> columnCount = matrix.streamV(1, 3).boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0));
        Assertions.assertEquals(5, columnCount.get(1));
    }

    @Test
    public void testStreamR() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Integer>> rows = matrix.streamR().map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(1, rows.get(0).get(0));
        Assertions.assertEquals(6, rows.get(1).get(2));
    }

    @Test
    public void testStreamRRange() {
        IntMatrix matrix = createTestMatrix();
        List<List<Integer>> rows = matrix.streamR(1, 3).map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(4, rows.get(0).get(0));
    }

    @Test
    public void testStreamC() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Integer>> columnCount = matrix.streamC().map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0).size());
        Assertions.assertEquals(1, columnCount.get(0).get(0));
        Assertions.assertEquals(6, columnCount.get(2).get(1));
    }

    @Test
    public void testStreamCRange() {
        IntMatrix matrix = createTestMatrix();
        List<List<Integer>> columnCount = matrix.streamC(1, 3).map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertEquals(3, columnCount.get(0).size());
        Assertions.assertEquals(2, columnCount.get(0).get(0));
    }

    @Test
    public void testAccept() throws Exception {
        IntMatrix matrix = createTestMatrix();
        List<Integer> values = new ArrayList<>();

        matrix.accept(m -> {
            values.add(m.get(0, 0));
            values.add(m.get(2, 2));
        });

        Assertions.assertEquals(2, values.size());
        Assertions.assertEquals(1, values.get(0));
        Assertions.assertEquals(9, values.get(1));
    }

    @Test
    public void testApply() throws Exception {
        IntMatrix matrix = createTestMatrix();

        String result = matrix.apply(m -> "Matrix " + m.rowCount() + "x" + m.columnCount());
        Assertions.assertEquals("Matrix 3x3", result);

        int sum = matrix.apply(m -> {
            int s = 0;
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    s += m.get(i, j);
                }
            }
            return s;
        });
        Assertions.assertEquals(45, sum);
    }

    @Test
    public void testCheckSameShape() {
        IntMatrix matrix1 = createTestMatrix();
        IntMatrix matrix2 = IntMatrix.of(new int[][] { { 10, 20, 30 }, { 40, 50, 60 }, { 70, 80, 90 } });
        IntMatrix matrix3 = createTestMatrix2x3();

        // This should not throw
        matrix1.checkSameShape(matrix2);

        // This should throw
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix1.checkSameShape(matrix3);
        });
    }

    @Test
    public void testCheckIfRowAndColumnSizeAreSame() {
        IntMatrix squareMatrix = createTestMatrix();
        IntMatrix nonSquareMatrix = createTestMatrix2x3();

        // This should not throw
        squareMatrix.checkIfRowAndColumnSizeAreSame();

        // This should throw
        Assertions.assertThrows(IllegalStateException.class, () -> {
            nonSquareMatrix.checkIfRowAndColumnSizeAreSame();
        });
    }

    @Test
    public void testPrintln() {
        IntMatrix matrix = createTestMatrix();
        // Just verify it doesn't throw an exception
        matrix.println();
    }

    @Test
    public void testEmptyMatrix() {
        IntMatrix empty = IntMatrix.of(new int[0][0]);

        Assertions.assertEquals(0, empty.rowCount());
        Assertions.assertEquals(0, empty.columnCount());
        Assertions.assertEquals(0, empty.elementCount());
        Assertions.assertTrue(empty.isEmpty());

        IntList flatList = empty.flatten();
        Assertions.assertTrue(flatList.isEmpty());

        List<Integer> streamList = empty.streamH().boxed().collect(Collectors.toList());
        Assertions.assertTrue(streamList.isEmpty());
    }

    @Test
    public void testSingleElementMatrix() {
        IntMatrix single = IntMatrix.of(new int[][] { { 42 } });

        Assertions.assertEquals(1, single.rowCount());
        Assertions.assertEquals(1, single.columnCount());
        Assertions.assertEquals(1, single.elementCount());
        Assertions.assertFalse(single.isEmpty());
        Assertions.assertEquals(42, single.get(0, 0));

        List<Integer> diagonal = single.streamLU2RD().boxed().collect(Collectors.toList());
        Assertions.assertEquals(1, diagonal.size());
        Assertions.assertEquals(42, diagonal.get(0));
    }

    @Test
    public void testLargeMatrix() {
        int size = 100;
        int[][] data = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = i * size + j;
            }
        }

        IntMatrix matrix = IntMatrix.of(data);
        Assertions.assertEquals(size, matrix.rowCount());
        Assertions.assertEquals(size, matrix.columnCount());
        Assertions.assertEquals(size * size, matrix.elementCount());
        Assertions.assertEquals(5050, matrix.get(50, 50));

        // Test operations on large matrix
        IntMatrix transposed = matrix.transpose();
        Assertions.assertEquals(matrix.get(10, 20), transposed.get(20, 10));

        IntMatrix copy = matrix.copy(0, 10, 0, 10);
        Assertions.assertEquals(10, copy.rowCount());
        Assertions.assertEquals(10, copy.columnCount());
    }
}
