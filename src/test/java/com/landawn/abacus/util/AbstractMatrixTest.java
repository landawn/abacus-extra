package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.Stream;

class AbstractMatrixTest extends TestBase {

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
        int[][] array = matrix.backingArray();
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
        IntMatrix repeated = matrix.repeatElements(2, 3);

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
            matrix.repeatElements(0, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.repeatElements(1, 0);
        });
    }

    @Test
    public void testRepmat() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix tiled = matrix.repeatMatrix(2, 3);

        tiled.printAndReturn();

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
            matrix.repeatMatrix(0, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.repeatMatrix(1, -1);
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

        matrix.applyOnFlattened(arrays -> java.util.Arrays.sort(arrays));

        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(1, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
    }

    @Test
    public void testForEachWithIndices() throws Exception {
        IntMatrix matrix = createTestMatrix2x3();
        List<String> positions = new ArrayList<>();

        matrix.forEachIndex((i, j) -> positions.add(i + "," + j));

        Assertions.assertEquals(6, positions.size());
        Assertions.assertTrue(positions.contains("0,0"));
        Assertions.assertTrue(positions.contains("1,2"));
    }

    @Test
    public void testForEachWithIndicesRegion() throws Exception {
        IntMatrix matrix = createTestMatrix();
        List<String> positions = new ArrayList<>();

        matrix.forEachIndex(1, 3, 1, 3, (i, j) -> positions.add(i + "," + j));

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

        matrix.forEachIndex((i, j, m) -> values.add(m.get(i, j)));

        Assertions.assertEquals(6, values.size());
        Assertions.assertEquals(1, values.get(0));
        Assertions.assertEquals(6, values.get(5));
    }

    @Test
    public void testForEachWithMatrixRegion() throws Exception {
        IntMatrix matrix = createTestMatrix();
        List<Integer> values = new ArrayList<>();

        matrix.forEachIndex(0, 2, 0, 2, (i, j, m) -> values.add(m.get(i, j)));

        Assertions.assertEquals(4, values.size());
        Assertions.assertEquals(1, values.get(0));
        Assertions.assertEquals(2, values.get(1));
        Assertions.assertEquals(4, values.get(2));
        Assertions.assertEquals(5, values.get(3));
    }

    @Test
    public void testForEachNullAction() {
        IntMatrix matrix = createTestMatrix();

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEachIndex((Throwables.IntBiConsumer<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matrix.forEachIndex(0, matrix.rowCount(), 0, matrix.columnCount(), (Throwables.IntBiConsumer<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEachIndex((Throwables.BiIntObjConsumer<IntMatrix, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matrix.forEachIndex(0, matrix.rowCount(), 0, matrix.columnCount(), (Throwables.BiIntObjConsumer<IntMatrix, RuntimeException>) null));
    }

    @Test
    public void testAcceptNullAction() {
        IntMatrix matrix = createTestMatrix();
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.accept((Throwables.Consumer<IntMatrix, RuntimeException>) null));
    }

    @Test
    public void testApplyNullAction() {
        IntMatrix matrix = createTestMatrix();
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.apply((Throwables.Function<IntMatrix, String, RuntimeException>) null));
    }

    @Test
    public void testPointsLU2RD() {
        IntMatrix matrix = createTestMatrix();
        List<Sheet.Point> points = matrix.pointsMainDiagonal().collect(Collectors.toList());

        Assertions.assertEquals(3, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(2, 2), points.get(2));
    }

    @Test
    public void testPointsLU2RDNonSquare() {
        IntMatrix matrix = createTestMatrix2x3();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            matrix.pointsMainDiagonal().collect(Collectors.toList());
        });
    }

    @Test
    public void testPointsRU2LD() {
        IntMatrix matrix = createTestMatrix();
        List<Sheet.Point> points = matrix.pointsAntiDiagonal().collect(Collectors.toList());

        Assertions.assertEquals(3, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 2), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsH() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Sheet.Point> points = matrix.pointsHorizontal().collect(Collectors.toList());

        Assertions.assertEquals(4, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(2));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsHRow() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Sheet.Point> points = matrix.pointsHorizontal(1).collect(Collectors.toList());

        Assertions.assertEquals(3, points.size());
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(1, 2), points.get(2));
    }

    @Test
    public void testPointsHRange() {
        IntMatrix matrix = createTestMatrix();
        List<Sheet.Point> points = matrix.pointsHorizontal(1, 3).collect(Collectors.toList());

        Assertions.assertEquals(6, points.size());
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(2, 2), points.get(5));
    }

    @Test
    public void testPointsV() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Sheet.Point> points = matrix.pointsVertical().collect(Collectors.toList());

        Assertions.assertEquals(4, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 0), points.get(1));
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(2));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsVColumn() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Sheet.Point> points = matrix.pointsVertical(1).collect(Collectors.toList());

        Assertions.assertEquals(2, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 1), points.get(1));
    }

    @Test
    public void testPointsVRange() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Sheet.Point> points = matrix.pointsVertical(1, 3).collect(Collectors.toList());

        Assertions.assertEquals(4, points.size());
        Assertions.assertEquals(Sheet.Point.of(0, 1), points.get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 2), points.get(3));
    }

    @Test
    public void testPointsR() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Sheet.Point>> rows = matrix.pointsRows().map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), rows.get(0).get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 2), rows.get(1).get(2));
    }

    @Test
    public void testPointsRRange() {
        IntMatrix matrix = createTestMatrix();
        List<List<Sheet.Point>> rows = matrix.pointsRows(1, 3).map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(1, 0), rows.get(0).get(0));
    }

    @Test
    public void testPointsC() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Sheet.Point>> columnCount = matrix.pointsColumns().map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(0, 0), columnCount.get(0).get(0));
        Assertions.assertEquals(Sheet.Point.of(1, 2), columnCount.get(2).get(1));
    }

    @Test
    public void testPointsCRange() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Sheet.Point>> columnCount = matrix.pointsColumns(1, 3).map(stream -> stream.collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0).size());
        Assertions.assertEquals(Sheet.Point.of(0, 1), columnCount.get(0).get(0));
    }

    @Test
    public void testStreamLU2RD() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> diagonal = matrix.streamMainDiagonal().boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, diagonal.size());
        Assertions.assertEquals(1, diagonal.get(0));
        Assertions.assertEquals(5, diagonal.get(1));
        Assertions.assertEquals(9, diagonal.get(2));
    }

    @Test
    public void testStreamRU2LD() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> diagonal = matrix.streamAntiDiagonal().boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, diagonal.size());
        Assertions.assertEquals(3, diagonal.get(0));
        Assertions.assertEquals(5, diagonal.get(1));
        Assertions.assertEquals(7, diagonal.get(2));
    }

    @Test
    public void testStreamH() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Integer> elements = matrix.streamHorizontal().boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, elements.size());
        Assertions.assertEquals(1, elements.get(0));
        Assertions.assertEquals(6, elements.get(5));
    }

    @Test
    public void testStreamHRow() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> row = matrix.streamHorizontal(1).boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, row.size());
        Assertions.assertEquals(4, row.get(0));
        Assertions.assertEquals(5, row.get(1));
        Assertions.assertEquals(6, row.get(2));
    }

    @Test
    public void testStreamHRange() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> rows = matrix.streamHorizontal(1, 3).boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, rows.size());
        Assertions.assertEquals(4, rows.get(0));
        Assertions.assertEquals(9, rows.get(5));
    }

    @Test
    public void testStreamV() {
        IntMatrix matrix = createTestMatrix2x3();
        List<Integer> elements = matrix.streamVertical().boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, elements.size());
        Assertions.assertEquals(1, elements.get(0));
        Assertions.assertEquals(4, elements.get(1));
        Assertions.assertEquals(2, elements.get(2));
    }

    @Test
    public void testStreamVColumn() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> col = matrix.streamVertical(1).boxed().collect(Collectors.toList());

        Assertions.assertEquals(3, col.size());
        Assertions.assertEquals(2, col.get(0));
        Assertions.assertEquals(5, col.get(1));
        Assertions.assertEquals(8, col.get(2));
    }

    @Test
    public void testStreamVRange() {
        IntMatrix matrix = createTestMatrix();
        List<Integer> columnCount = matrix.streamVertical(1, 3).boxed().collect(Collectors.toList());

        Assertions.assertEquals(6, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0));
        Assertions.assertEquals(5, columnCount.get(1));
    }

    @Test
    public void testStreamR() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Integer>> rows = matrix.streamRows().map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(1, rows.get(0).get(0));
        Assertions.assertEquals(6, rows.get(1).get(2));
    }

    @Test
    public void testStreamRRange() {
        IntMatrix matrix = createTestMatrix();
        List<List<Integer>> rows = matrix.streamRows(1, 3).map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(3, rows.get(0).size());
        Assertions.assertEquals(4, rows.get(0).get(0));
    }

    @Test
    public void testStreamC() {
        IntMatrix matrix = createTestMatrix2x3();
        List<List<Integer>> columnCount = matrix.streamColumns().map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertEquals(2, columnCount.get(0).size());
        Assertions.assertEquals(1, columnCount.get(0).get(0));
        Assertions.assertEquals(6, columnCount.get(2).get(1));
    }

    @Test
    public void testStreamCRange() {
        IntMatrix matrix = createTestMatrix();
        List<List<Integer>> columnCount = matrix.streamColumns(1, 3).map(stream -> stream.boxed().collect(Collectors.toList())).collect(Collectors.toList());

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
        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::printAndReturn);
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

        List<Integer> streamList = empty.streamHorizontal().boxed().collect(Collectors.toList());
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

        List<Integer> diagonal = single.streamMainDiagonal().boxed().collect(Collectors.toList());
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

    @Nested
    /**
     * Comprehensive unit tests for AbstractMatrix.
     * Tests common matrix functionality through concrete subclass implementations.
     */
    @Tag("2025")
    class AbstractMatrix2025Test extends TestBase {
        @Test
        public void testRowsColsCount_squareMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(4, m.elementCount());
        }

        @Test
        public void testRowsColsCount_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(1, m.elementCount());
        }

        @Test
        public void testRowsColsCount_emptyMatrix() {
            IntMatrix m = IntMatrix.empty();
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testComponentType_doubleMatrix() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertEquals(double.class, m.componentType());
        }

        @Test
        public void testComponentType_objectMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
            assertEquals(String.class, m.componentType());
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

        // ============ Copy Tests ============

        @Test
        public void testCopy_intMatrix() {
            IntMatrix original = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix copy = original.copy();

            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(4, copy.get(1, 1));

            // Modify copy should not affect original
            copy.set(0, 0, 99);
            assertEquals(1, original.get(0, 0));
            assertEquals(99, copy.get(0, 0));
        }

        @Test
        public void testCopy_doubleMatrix() {
            DoubleMatrix original = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            DoubleMatrix copy = original.copy();

            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertEquals(1.5, copy.get(0, 0), 0.0001);
            assertEquals(4.5, copy.get(1, 1), 0.0001);
        }

        @Test
        public void testCopy_objectMatrix() {
            Matrix<String> original = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            Matrix<String> copy = original.copy();

            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertEquals("a", copy.get(0, 0));
            assertEquals("d", copy.get(1, 1));
        }

        @Test
        public void testCopy_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            IntMatrix copy = empty.copy();
            assertTrue(copy.isEmpty());
        }

        @Test
        public void testCopy_withRowRange_singleRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            IntMatrix subset = m.copy(1, 2);

            assertEquals(1, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertArrayEquals(new int[] { 4, 5, 6 }, subset.rowView(0));
        }

        @Test
        public void testCopy_withFullRange_entireMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix copy = m.copy(0, 2, 0, 2);

            assertEquals(m, copy);
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2, 0, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3, 0, 2));
        }

        @Test
        public void testRotate180_squareMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix rotated = m.rotate180();

            assertEquals(4, rotated.get(0, 0));
            assertEquals(3, rotated.get(0, 1));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(1, rotated.get(1, 1));
        }

        @Test
        public void testRotate270_squareMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix rotated = m.rotate270();

            assertEquals(2, rotated.get(0, 0));
            assertEquals(4, rotated.get(0, 1));
            assertEquals(1, rotated.get(1, 0));
            assertEquals(3, rotated.get(1, 1));
        }

        @Test
        public void testTranspose_doubleTranspose() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix transposed = m.transpose().transpose();

            assertEquals(m, transposed);
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape_withNegativeDimensions_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.of(new String[][] { { "a" } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> Matrix.of(new String[][] { { "a" } }).reshape(1, -1));

            assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.of(new boolean[][] { { true } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> ByteMatrix.of(new byte[][] { { 1 } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> CharMatrix.of(new char[][] { { 'a' } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> ShortMatrix.of(new short[][] { { 1 } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> IntMatrix.of(new int[][] { { 1 } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.of(new long[][] { { 1L } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.of(new float[][] { { 1F } }).reshape(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.of(new double[][] { { 1D } }).reshape(-1, 1));
        }

        @Test
        public void testReshape_withCols() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 } });
            IntMatrix reshaped = m.reshape(2);

            assertEquals(4, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
        }

        @Test
        public void testReshape_withRowsAndCols() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = m.reshape(3, 2);

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
        public void testReshape_toSingleRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix reshaped = m.reshape(1, 4);

            assertEquals(1, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
            assertArrayEquals(new int[] { 1, 2, 3, 4 }, reshaped.rowView(0));
        }

        @Test
        public void testReshape_toSingleColumn() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix reshaped = m.reshape(4, 1);

            assertEquals(4, reshaped.rowCount());
            assertEquals(1, reshaped.columnCount());
            assertArrayEquals(new int[] { 1, 2, 3, 4 }, reshaped.columnCopy(0));
        }

        @Test
        public void testIsSameShape_differentShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
            assertFalse(m1.isSameShape(m2));
        }

        @Test
        public void testIsSameShape_differentRows() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertFalse(m1.isSameShape(m2));
        }

        @Test
        public void testIsSameShape_differentCols() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertFalse(m1.isSameShape(m2));
        }

        @Test
        public void testIsSameShape_emptyMatrices() {
            IntMatrix m1 = IntMatrix.empty();
            IntMatrix m2 = IntMatrix.empty();
            assertTrue(m1.isSameShape(m2));
        }

        // ============ Repelem and Repmat Tests ============

        @Test
        public void testRepelem() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            IntMatrix repeated = m.repeatElements(2, 3);

            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2));
            assertEquals(2, repeated.get(0, 3));
            assertEquals(2, repeated.get(0, 4));
            assertEquals(2, repeated.get(0, 5));
        }

        @Test
        public void testRepelem_invalidArguments() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(-1, 1));
        }

        @Test
        public void testRepmat() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix repeated = m.repeatMatrix(2, 3);

            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2));
            assertEquals(1, repeated.get(2, 0));
        }

        @Test
        public void testRepmat_invalidArguments() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(-1, 1));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntList flat = m.flatten();

            assertEquals(6, flat.size());
            for (int i = 0; i < 6; i++) {
                assertEquals(i + 1, flat.get(i));
            }
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
        public void testFlatOp() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Integer> values = new ArrayList<>();

            m.applyOnFlattened(arr -> {
                for (int val : arr) {
                    values.add(val);
                }
            });

            assertEquals(6, values.size());
            assertEquals(1, values.get(0).intValue());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach_withIndices() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<String> positions = new ArrayList<>();

            m.forEachIndex((i, j) -> positions.add(i + "," + j));

            assertEquals(4, positions.size());
            assertEquals("0,0", positions.get(0));
            assertEquals("0,1", positions.get(1));
            assertEquals("1,0", positions.get(2));
            assertEquals("1,1", positions.get(3));
        }

        @Test
        public void testForEach_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<String> positions = new ArrayList<>();

            m.forEachIndex(1, 3, 1, 3, (i, j) -> positions.add(i + "," + j));

            assertEquals(4, positions.size());
            assertEquals("1,1", positions.get(0));
            assertEquals("1,2", positions.get(1));
            assertEquals("2,1", positions.get(2));
            assertEquals("2,2", positions.get(3));
        }

        @Test
        public void testForEach_withRange_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEachIndex(-1, 2, 0, 2, (i, j) -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEachIndex(0, 3, 0, 2, (i, j) -> {
            }));
        }

        @Test
        public void testForEach_withMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Integer> values = new ArrayList<>();

            m.forEachIndex((i, j, matrix) -> values.add(matrix.get(i, j)));

            assertEquals(4, values.size());
            assertEquals(1, values.get(0).intValue());
            assertEquals(2, values.get(1).intValue());
            assertEquals(3, values.get(2).intValue());
            assertEquals(4, values.get(3).intValue());
        }

        @Test
        public void testForEach_withMatrixAndRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> values = new ArrayList<>();

            m.forEachIndex(1, 3, 1, 3, (i, j, matrix) -> values.add(matrix.get(i, j)));

            assertEquals(4, values.size());
            assertEquals(5, values.get(0).intValue());
            assertEquals(6, values.get(1).intValue());
            assertEquals(8, values.get(2).intValue());
            assertEquals(9, values.get(3).intValue());
        }

        // ============ Point Stream Tests ============

        @Test
        public void testPointsLU2RD() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.pointsMainDiagonal().toList();

            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 2), points.get(2));
        }

        @Test
        public void testPointsLU2RD_nonSquare() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.pointsMainDiagonal());
        }

        @Test
        public void testPointsRU2LD() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.pointsAntiDiagonal().toList();

            assertEquals(3, points.size());
            assertEquals(Point.of(0, 2), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsRU2LD_nonSquare() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.pointsAntiDiagonal());
        }

        @Test
        public void testPointsH() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsHorizontal().toList();

            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        @Test
        public void testPointsH_withRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsHorizontal(1).toList();

            assertEquals(2, points.size());
            assertEquals(Point.of(1, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
        }

        @Test
        public void testPointsH_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<Point> points = m.pointsHorizontal(1, 3).toList();

            assertEquals(4, points.size());
            assertEquals(Point.of(1, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
            assertEquals(Point.of(2, 1), points.get(3));
        }

        @Test
        public void testPointsH_withRange_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsHorizontal(0, 3));
        }

        @Test
        public void testPointsV() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsVertical().toList();

            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
            assertEquals(Point.of(0, 1), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        @Test
        public void testPointsV_withColumn() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsVertical(1).toList();

            assertEquals(2, points.size());
            assertEquals(Point.of(0, 1), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
        }

        @Test
        public void testPointsV_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Point> points = m.pointsVertical(1, 3).toList();

            assertEquals(4, points.size());
            assertEquals(Point.of(0, 1), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(0, 2), points.get(2));
            assertEquals(Point.of(1, 2), points.get(3));
        }

        @Test
        public void testPointsV_withRange_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsVertical(0, 3));
        }

        @Test
        public void testPointsR() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<List<Point>> rowPoints = m.pointsRows().map(s -> s.toList()).toList();

            assertEquals(2, rowPoints.size());
            assertEquals(2, rowPoints.get(0).size());
            assertEquals(Point.of(0, 0), rowPoints.get(0).get(0));
            assertEquals(Point.of(0, 1), rowPoints.get(0).get(1));
        }

        @Test
        public void testPointsR_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<List<Point>> rowPoints = m.pointsRows(1, 3).map(s -> s.toList()).toList();

            assertEquals(2, rowPoints.size());
            assertEquals(Point.of(1, 0), rowPoints.get(0).get(0));
            assertEquals(Point.of(2, 0), rowPoints.get(1).get(0));
        }

        @Test
        public void testPointsR_withRange_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsRows(0, 3));
        }

        @Test
        public void testPointsC() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<List<Point>> colPoints = m.pointsColumns().map(s -> s.toList()).toList();

            assertEquals(2, colPoints.size());
            assertEquals(2, colPoints.get(0).size());
            assertEquals(Point.of(0, 0), colPoints.get(0).get(0));
            assertEquals(Point.of(1, 0), colPoints.get(0).get(1));
        }

        @Test
        public void testPointsC_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<List<Point>> colPoints = m.pointsColumns(1, 3).map(s -> s.toList()).toList();

            assertEquals(2, colPoints.size());
            assertEquals(Point.of(0, 1), colPoints.get(0).get(0));
            assertEquals(Point.of(0, 2), colPoints.get(1).get(0));
        }

        @Test
        public void testPointsC_withRange_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.pointsColumns(0, 3));
        }

        // ============ Element Stream Tests ============

        @Test
        public void testStreamLU2RD_nonSquare() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.streamAntiDiagonal());
        }

        @Test
        public void testStreamH_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            int[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new int[] { 4, 5, 6, 7, 8, 9 }, rows);
        }

        @Test
        public void testStreamV_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            int[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new int[] { 2, 5, 3, 6 }, columnCount);
        }

        @Test
        public void testStreamR() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<int[]> rows = m.streamRows().map(s -> s.toArray()).toList();

            assertEquals(2, rows.size());
            assertArrayEquals(new int[] { 1, 2, 3 }, rows.get(0));
            assertArrayEquals(new int[] { 4, 5, 6 }, rows.get(1));
        }

        @Test
        public void testStreamR_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<int[]> rows = m.streamRows(1, 3).map(s -> s.toArray()).toList();

            assertEquals(2, rows.size());
            assertArrayEquals(new int[] { 4, 5, 6 }, rows.get(0));
            assertArrayEquals(new int[] { 7, 8, 9 }, rows.get(1));
        }

        @Test
        public void testStreamC() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<int[]> columnCount = m.streamColumns().map(s -> s.toArray()).toList();

            assertEquals(3, columnCount.size());
            assertArrayEquals(new int[] { 1, 4 }, columnCount.get(0));
            assertArrayEquals(new int[] { 2, 5 }, columnCount.get(1));
            assertArrayEquals(new int[] { 3, 6 }, columnCount.get(2));
        }
        // ============ Accept and Apply Tests ============

        @Test
        public void testAccept() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Integer> sums = new ArrayList<>();

            m.accept(matrix -> {
                int sum = 0;
                for (int i = 0; i < matrix.rowCount(); i++) {
                    for (int j = 0; j < matrix.columnCount(); j++) {
                        sum += matrix.get(i, j);
                    }
                }
                sums.add(sum);
            });

            assertEquals(1, sums.size());
            assertEquals(10, sums.get(0).intValue());
        }

        @Test
        public void testApply() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

            Integer sum = m.apply(matrix -> {
                int total = 0;
                for (int i = 0; i < matrix.rowCount(); i++) {
                    for (int j = 0; j < matrix.columnCount(); j++) {
                        total += matrix.get(i, j);
                    }
                }
                return total;
            });

            assertEquals(10, sum.intValue());
        }

        @Test
        public void testApply_returnString() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            String result = m.apply(matrix -> "Matrix is " + matrix.rowCount() + "x" + matrix.columnCount());
            assertEquals("Matrix is 2x2", result);
        }

        // ============ Edge Cases ============

        @Test
        public void testEmptyMatrix_operations() {
            IntMatrix empty = IntMatrix.empty();

            assertTrue(empty.isEmpty());
            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.streamHorizontal().count());
            assertEquals(0, empty.streamVertical().count());
            assertEquals(0, empty.streamRows().count());
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testSingleElement_operations() {
            IntMatrix single = IntMatrix.of(new int[][] { { 42 } });

            assertFalse(single.isEmpty());
            assertEquals(1, single.flatten().size());
            assertEquals(42, single.streamHorizontal().findFirst().orElse(0));
        }

        @Test
        public void testLargeMatrix_count() {
            int[][] large = new int[1000][1000];
            IntMatrix m = IntMatrix.of(large);

            assertEquals(1000, m.rowCount());
            assertEquals(1000, m.columnCount());
            assertEquals(1000000L, m.elementCount());
        }

        // ============ Constructor Validation Tests ============

        @Test
        public void testConstructor_nullArray() {
            IntMatrix m = IntMatrix.of(null);
            assertTrue(m.isEmpty());
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testConstructor_inconsistentRowLengths() {
            int[][] jagged = new int[][] { { 1, 2, 3 }, { 4, 5 }, { 7, 8, 9 } };
            assertThrows(IllegalArgumentException.class, () -> IntMatrix.of(jagged));
        }

        @Test
        public void testConstructor_inconsistentRowLengths_threeRows() {
            int[][] jagged = new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6, 7 } };
            assertThrows(IllegalArgumentException.class, () -> IntMatrix.of(jagged));
        }

        @Test
        public void testConstructor_singleRowArray() {
            int[][] singleRow = new int[][] { { 1, 2, 3 } };
            IntMatrix m = IntMatrix.of(singleRow);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void testConstructor_emptyRowsInArray() {
            int[][] emptyRows = new int[][] { {}, {} };
            IntMatrix m = IntMatrix.of(emptyRows);
            assertEquals(2, m.rowCount());
            assertEquals(0, m.columnCount());
            assertEquals(0, m.elementCount());
        }

        // ============ Reshape Edge Cases ============

        @Test
        public void testReshape_withCols_notEvenlyDivisible() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = m.reshape(4);

            assertEquals(2, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
        }

        @Test
        public void testReshape_withCols_evenlyDivisible() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            IntMatrix reshaped = m.reshape(2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
        }

        @Test
        public void testReshape_largerThanOriginal() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix reshaped = m.reshape(3, 3);

            assertEquals(3, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(0, reshaped.get(2, 2));
        }

        // ============ Additional Stream Tests ============

        @Test
        public void testStreamH_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.streamHorizontal().count();
            assertEquals(0, count);
        }

        @Test
        public void testStreamV_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.streamVertical().count();
            assertEquals(0, count);
        }

        @Test
        public void testStreamR_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.streamRows().count();
            assertEquals(0, count);
        }

        @Test
        public void testStreamC_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.streamColumns().count();
            assertEquals(0, count);
        }

        // ============ ForEach Edge Cases ============

        @Test
        public void testForEach_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            List<String> positions = new ArrayList<>();

            empty.forEachIndex((i, j) -> positions.add(i + "," + j));

            assertTrue(positions.isEmpty());
        }

        @Test
        public void testForEach_emptyRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<String> positions = new ArrayList<>();

            m.forEachIndex(1, 1, 0, 2, (i, j) -> positions.add(i + "," + j));

            assertTrue(positions.isEmpty());
        }

        @Test
        public void testForEach_withMatrix_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            List<Integer> values = new ArrayList<>();

            empty.forEachIndex((i, j, matrix) -> values.add(matrix.get(i, j)));

            assertTrue(values.isEmpty());
        }

        @Test
        public void testForEach_withMatrixAndRange_emptyRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Integer> values = new ArrayList<>();

            m.forEachIndex(0, 0, 1, 1, (i, j, matrix) -> values.add(matrix.get(i, j)));

            assertTrue(values.isEmpty());
        }

        // ============ Point Stream Edge Cases ============

        @Test
        public void testPointsH_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.pointsHorizontal().count();
            assertEquals(0, count);
        }

        @Test
        public void testPointsV_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.pointsVertical().count();
            assertEquals(0, count);
        }

        @Test
        public void testPointsR_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.pointsRows().count();
            assertEquals(0, count);
        }

        @Test
        public void testPointsC_emptyMatrix() {
            IntMatrix empty = IntMatrix.empty();
            long count = empty.pointsColumns().count();
            assertEquals(0, count);
        }

        // ============ Apply and Accept Error Handling ============

        @Test
        public void testAccept_withException() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

            assertThrows(RuntimeException.class, () -> m.accept(matrix -> {
                throw new RuntimeException("Test exception");
            }));
        }

        @Test
        public void testApply_withException() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

            assertThrows(RuntimeException.class, () -> m.apply(matrix -> {
                throw new RuntimeException("Test exception");
            }));
        }

        // ============ Additional Edge Cases ============

        @Test
        public void testCopy_emptyRowRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m.copy(1, 1));
        }

        @Test
        public void testCopy_emptyRegion() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m.copy(1, 1, 0, 2));
        }

        @Test
        public void testIsSameShape_identicalMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertTrue(m.isSameShape(m));
        }

        @Test
        public void testReshape_sameShape() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix reshaped = m.reshape(2, 2);

            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(m, reshaped);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for AbstractMatrix class covering all public methods.
     * Tests use concrete subclass implementations (IntMatrix, DoubleMatrix, Matrix) to test abstract behavior.
     */
    @Tag("2510")
    class AbstractMatrix2510Test extends TestBase {

        // ============ Basic Property Tests ============

        @Test
        public void testRowsColsCount_rectangular() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testRowsColsCount_singleRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4, 5 } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(5, m.elementCount());
        }

        @Test
        public void testRowsColsCount_singleColumn() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 }, { 4 } });
            assertEquals(4, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(4, m.elementCount());
        }

        @Test
        public void testRowsColsCount_largeMatrix() {
            IntMatrix m = IntMatrix.of(new int[100][50]);
            assertEquals(100, m.rowCount());
            assertEquals(50, m.columnCount());
            assertEquals(5000, m.elementCount());
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertEquals(int.class, m.componentType());
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
        // ============ Array Access Tests ============

        @Test
        public void testArray_intMatrix() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            IntMatrix m = IntMatrix.of(arr);
            int[][] returnedArray = m.backingArray();

            assertNotNull(returnedArray);
            assertEquals(2, returnedArray.length);
            assertArrayEquals(new int[] { 1, 2 }, returnedArray[0]);
            assertArrayEquals(new int[] { 3, 4 }, returnedArray[1]);
        }

        @Test
        public void testArray_doubleMatrix() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            DoubleMatrix m = DoubleMatrix.of(arr);
            double[][] returnedArray = m.backingArray();

            assertNotNull(returnedArray);
            assertEquals(2, returnedArray.length);
            assertArrayEquals(new double[] { 1.5, 2.5 }, returnedArray[0]);
            assertArrayEquals(new double[] { 3.5, 4.5 }, returnedArray[1]);
        }

        @Test
        public void testArray_objectMatrix() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = Matrix.of(arr);
            String[][] returnedArray = m.backingArray();

            assertNotNull(returnedArray);
            assertEquals(2, returnedArray.length);
            assertArrayEquals(new String[] { "A", "B" }, returnedArray[0]);
            assertArrayEquals(new String[] { "C", "D" }, returnedArray[1]);
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

        @Test
        public void testCopy_doubleMatrix() {
            DoubleMatrix original = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            DoubleMatrix copy = original.copy();

            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
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

            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertEquals("a", copy.get(0, 0));
            assertEquals("d", copy.get(1, 1));

            // Verify independence
            copy.set(0, 0, "X");
            assertEquals("a", original.get(0, 0));
            assertEquals("X", copy.get(0, 0));
        }

        @Test
        public void testCopy_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            IntMatrix copy = m.copy();
            assertEquals(1, copy.rowCount());
            assertEquals(1, copy.columnCount());
            assertEquals(42, copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange_allRows() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix copy = m.copy(0, 2);

            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(4, copy.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_entireMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix copy = m.copy(0, 2, 0, 2);

            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(4, copy.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            IntMatrix single = m.copy(1, 2, 1, 2);

            assertEquals(1, single.rowCount());
            assertEquals(1, single.columnCount());
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

            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
            assertEquals(6, rotated.get(2, 0));
            assertEquals(3, rotated.get(2, 1));
        }

        @Test
        public void testRotate90_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            IntMatrix rotated = m.rotate90();

            assertEquals(1, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertEquals(42, rotated.get(0, 0));
        }

        @Test
        public void testRotate90_singleRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 } });
            IntMatrix rotated = m.rotate90();

            assertEquals(3, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertEquals(1, rotated.get(0, 0));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(3, rotated.get(2, 0));
        }

        @Test
        public void testRotate180_rectangular() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix rotated = m.rotate180();

            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals(6, rotated.get(0, 0));
            assertEquals(5, rotated.get(0, 1));
            assertEquals(4, rotated.get(0, 2));
            assertEquals(3, rotated.get(1, 0));
            assertEquals(2, rotated.get(1, 1));
            assertEquals(1, rotated.get(1, 2));
        }

        @Test
        public void testRotate180_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            IntMatrix rotated = m.rotate180();

            assertEquals(1, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertEquals(42, rotated.get(0, 0));
        }

        @Test
        public void testRotate270_rectangular() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix rotated = m.rotate270();

            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(6, rotated.get(0, 1));
            assertEquals(1, rotated.get(2, 0));
            assertEquals(4, rotated.get(2, 1));
        }

        @Test
        public void testRotate270_square() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix rotated = m.rotate270();

            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2, rotated.get(0, 0));
            assertEquals(4, rotated.get(0, 1));
            assertEquals(1, rotated.get(1, 0));
            assertEquals(3, rotated.get(1, 1));
        }

        @Test
        public void testRotate270_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            IntMatrix rotated = m.rotate270();

            assertEquals(1, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertEquals(42, rotated.get(0, 0));
        }

        @Test
        public void testTranspose_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            IntMatrix transposed = m.transpose();

            assertEquals(1, transposed.rowCount());
            assertEquals(1, transposed.columnCount());
            assertEquals(42, transposed.get(0, 0));
        }

        @Test
        public void testTranspose_singleRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 } });
            IntMatrix transposed = m.transpose();

            assertEquals(3, transposed.rowCount());
            assertEquals(1, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(3, transposed.get(2, 0));
        }

        @Test
        public void testTranspose_singleColumn() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
            IntMatrix transposed = m.transpose();

            assertEquals(1, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(2, transposed.get(0, 1));
            assertEquals(3, transposed.get(0, 2));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape_singleParam() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = m.reshape(2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(2, 1));
        }

        @Test
        public void testReshape_singleParam_needsPadding() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = m.reshape(4);

            assertEquals(2, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(4, reshaped.get(0, 3));
            assertEquals(5, reshaped.get(1, 0));
            assertEquals(0, reshaped.get(1, 3)); // Padding with default value
        }

        @Test
        public void testReshape_twoParams() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = m.reshape(3, 2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(2, 1));
        }

        @Test
        public void testReshape_twoParams_tooSmallThrows() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });

            // New shape is too small to hold all 6 elements
            assertThrows(IllegalArgumentException.class, () -> m.reshape(1, 3));
            assertThrows(IllegalArgumentException.class, () -> m.reshape(2, 2));
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
        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix repeated = m.repeatElements(2, 2);

            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
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
            IntMatrix repeated = m.repeatElements(3, 2);

            assertEquals(3, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(1, repeated.get(1, 0));
            assertEquals(1, repeated.get(2, 0));
            assertEquals(2, repeated.get(2, 3));
        }

        @Test
        public void testRepelem_invalidArgs() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(-1, 1));
        }

        @Test
        public void testRepmat() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix repeated = m.repeatMatrix(2, 2);

            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2)); // Tiled
            assertEquals(1, repeated.get(2, 0)); // Tiled
            assertEquals(4, repeated.get(3, 3));
        }

        @Test
        public void testRepmat_asymmetric() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            IntMatrix repeated = m.repeatMatrix(3, 2);

            assertEquals(3, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2)); // Second tile
            assertEquals(1, repeated.get(1, 0)); // Second row tile
        }

        @Test
        public void testRepmat_invalidArgs() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(-1, 1));
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
        // ============ FlatOp Tests ============

        @Test
        public void testFlatOp_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 3, 1 }, { 4, 2 } });
            AtomicInteger sum = new AtomicInteger(0);
            m.applyOnFlattened(arr -> {
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
            m.applyOnFlattened(arr -> {
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
            m.applyOnFlattened(arr -> {
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
            m.forEachIndex((i, j) -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        @Test
        public void testForEach_biConsumer_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEachIndex(1, 3, 1, 3, (i, j) -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        @Test
        public void testForEach_biConsumer_outOfBounds() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEachIndex(-1, 2, 0, 2, (i, j) -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEachIndex(0, 3, 0, 2, (i, j) -> {
            }));
        }

        @Test
        public void testForEach_withMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger sum = new AtomicInteger(0);
            m.forEachIndex((i, j, matrix) -> sum.addAndGet(matrix.get(i, j)));
            assertEquals(10, sum.get());
        }

        @Test
        public void testForEach_withMatrix_withRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            AtomicInteger sum = new AtomicInteger(0);
            m.forEachIndex(1, 3, 1, 3, (i, j, matrix) -> sum.addAndGet(matrix.get(i, j)));
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
            Stream<Point> points = m.pointsMainDiagonal();
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
            assertThrows(IllegalStateException.class, () -> m.pointsMainDiagonal().toList());
        }

        @Test
        public void testPointsRU2LD() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Stream<Point> points = m.pointsAntiDiagonal();
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
            assertThrows(IllegalStateException.class, () -> m.pointsAntiDiagonal().toList());
        }

        @Test
        public void testPointsH() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Stream<Point> points = m.pointsHorizontal();
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
            Stream<Point> points = m.pointsHorizontal(1);
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
            Stream<Point> points = m.pointsHorizontal(1, 3);
            List<Point> list = points.toList();

            assertEquals(4, list.size());
            assertEquals(1, list.get(0).rowIndex());
            assertEquals(2, list.get(2).rowIndex());
        }

        @Test
        public void testPointsV() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Stream<Point> points = m.pointsVertical();
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
            Stream<Point> points = m.pointsVertical(1);
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
            Stream<Point> points = m.pointsVertical(1, 3);
            List<Point> list = points.toList();

            assertEquals(4, list.size());
            assertEquals(1, list.get(0).columnIndex());
            assertEquals(2, list.get(2).columnIndex());
        }

        @Test
        public void testPointsR() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Stream<Stream<Point>> rows = m.pointsRows();
            List<List<Point>> list = rows.map(Stream::toList).toList();

            assertEquals(2, list.size());
            assertEquals(2, list.get(0).size());
            assertEquals(0, list.get(0).get(0).rowIndex());
            assertEquals(1, list.get(1).get(0).rowIndex());
        }

        @Test
        public void testPointsR_rowRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            Stream<Stream<Point>> rows = m.pointsRows(1, 3);
            List<List<Point>> list = rows.map(Stream::toList).toList();

            assertEquals(2, list.size());
            assertEquals(1, list.get(0).get(0).rowIndex());
        }

        @Test
        public void testPointsC() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Stream<Stream<Point>> columnCount = m.pointsColumns();
            List<List<Point>> list = columnCount.map(Stream::toList).toList();

            assertEquals(2, list.size());
            assertEquals(2, list.get(0).size());
            assertEquals(0, list.get(0).get(0).columnIndex());
            assertEquals(1, list.get(1).get(0).columnIndex());
        }

        @Test
        public void testPointsC_columnRange() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Stream<Stream<Point>> columnCount = m.pointsColumns(1, 3);
            List<List<Point>> list = columnCount.map(Stream::toList).toList();

            assertEquals(2, list.size());
            assertEquals(1, list.get(0).get(0).columnIndex());
        }

        // ============ Element Stream Tests ============

        @Test
        public void testStreamLU2RD_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            int[] diagonal = m.streamMainDiagonal().toArray();

            assertEquals(3, diagonal.length);
            assertEquals(1, diagonal[0]);
            assertEquals(5, diagonal[1]);
            assertEquals(9, diagonal[2]);
        }

        @Test
        public void testStreamRU2LD_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            int[] diagonal = m.streamAntiDiagonal().toArray();

            assertEquals(3, diagonal.length);
            assertEquals(3, diagonal[0]);
            assertEquals(5, diagonal[1]);
            assertEquals(7, diagonal[2]);
        }

        @Test
        public void testStreamH_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            int[] elements = m.streamHorizontal().toArray();

            assertEquals(4, elements.length);
            assertEquals(1, elements[0]);
            assertEquals(2, elements[1]);
            assertEquals(3, elements[2]);
            assertEquals(4, elements[3]);
        }

        @Test
        public void testStreamH_singleRow_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            int[] row = m.streamHorizontal(1).toArray();

            assertEquals(3, row.length);
            assertEquals(4, row[0]);
            assertEquals(5, row[1]);
            assertEquals(6, row[2]);
        }

        @Test
        public void testStreamH_rowRange_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            int[] elements = m.streamHorizontal(1, 3).toArray();

            assertEquals(4, elements.length);
            assertEquals(3, elements[0]);
            assertEquals(4, elements[1]);
            assertEquals(5, elements[2]);
            assertEquals(6, elements[3]);
        }

        @Test
        public void testStreamV_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            int[] elements = m.streamVertical().toArray();

            assertEquals(4, elements.length);
            assertEquals(1, elements[0]);
            assertEquals(3, elements[1]);
            assertEquals(2, elements[2]);
            assertEquals(4, elements[3]);
        }

        @Test
        public void testStreamV_singleColumn_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            int[] col = m.streamVertical(1).toArray();

            assertEquals(2, col.length);
            assertEquals(2, col[0]);
            assertEquals(5, col[1]);
        }

        @Test
        public void testStreamV_columnRange_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            int[] elements = m.streamVertical(1, 3).toArray();

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
            List<int[]> rows = m.streamRows().map(s -> s.toArray()).toList();

            assertEquals(2, rows.size());
            assertArrayEquals(new int[] { 1, 2 }, rows.get(0));
            assertArrayEquals(new int[] { 3, 4 }, rows.get(1));
        }

        @Test
        public void testStreamR_rowRange_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<int[]> rows = m.streamRows(1, 3).map(s -> s.toArray()).toList();

            assertEquals(2, rows.size());
            assertArrayEquals(new int[] { 3, 4 }, rows.get(0));
            assertArrayEquals(new int[] { 5, 6 }, rows.get(1));
        }

        @Test
        public void testStreamC_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<int[]> columnCount = m.streamColumns().map(s -> s.toArray()).toList();

            assertEquals(2, columnCount.size());
            assertArrayEquals(new int[] { 1, 3 }, columnCount.get(0));
            assertArrayEquals(new int[] { 2, 4 }, columnCount.get(1));
        }

        @Test
        public void testStreamC_columnRange_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<int[]> columnCount = m.streamColumns(1, 3).map(s -> s.toArray()).toList();

            assertEquals(2, columnCount.size());
            assertArrayEquals(new int[] { 2, 5 }, columnCount.get(0));
            assertArrayEquals(new int[] { 3, 6 }, columnCount.get(1));
        }

        // ============ Accept and Apply Tests ============

        @Test
        public void testAccept() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger sum = new AtomicInteger(0);
            m.accept(matrix -> {
                for (int i = 0; i < matrix.rowCount(); i++) {
                    for (int j = 0; j < matrix.columnCount(); j++) {
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
                for (int i = 0; i < matrix.rowCount(); i++) {
                    for (int j = 0; j < matrix.columnCount(); j++) {
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
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1, result.get(0, 0));
            assertEquals(3, result.get(0, 1));
        }

        // ============ Println Tests ============

        @Test
        public void testPrintln_intMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            String output = m.printAndReturn();
            assertNotNull(output);
            assertTrue(output.contains("1"));
            assertTrue(output.contains("4"));
        }

        @Test
        public void testPrintln_doubleMatrix() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            String output = m.printAndReturn();
            assertNotNull(output);
            assertTrue(output.contains("1.5") || output.contains("1"));
        }

        @Test
        public void testPrintln_objectMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String output = m.printAndReturn();
            assertNotNull(output);
            assertTrue(output.contains("A"));
            assertTrue(output.contains("D"));
        }

        @Test
        public void testPrintln_emptyMatrix() {
            IntMatrix m = IntMatrix.empty();
            String output = m.printAndReturn();
            assertNotNull(output);
        }
    }

    @Nested
    @Tag("2512")
    class AbstractMatrix2512Test extends TestBase {

        // ============ Dimensional Methods Tests ============

        @Test
        public void test_isEmpty_emptyMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[0][0]);
            assertTrue(matrix.isEmpty());
        }

        @Test
        public void test_isEmpty_nonEmptyMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertFalse(matrix.isEmpty());
        }

        @Test
        public void test_isEmpty_zeroRows() {
            IntMatrix matrix = IntMatrix.of(new int[0][5]);
            assertTrue(matrix.isEmpty());
        }

        @Test
        public void test_isEmpty_zeroCols() {
            IntMatrix matrix = IntMatrix.of(new int[][] {});
            assertTrue(matrix.isEmpty());
        }

        // ============ Shape Methods Tests ============

        @Test
        public void test_isSameShape_sameShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            assertTrue(m1.isSameShape(m2));
        }

        @Test
        public void test_isSameShape_differentRows() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 } });
            assertFalse(m1.isSameShape(m2));
        }

        @Test
        public void test_isSameShape_differentCols() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });
            assertFalse(m1.isSameShape(m2));
        }

        // ============ forEach Tests ============

        @Test
        public void test_forEach_simpleIteration() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger sum = new AtomicInteger(0);
            matrix.forEachIndex((i, j) -> sum.addAndGet(matrix.get(i, j)));
            assertEquals(10, sum.get());
        }

        @Test
        public void test_forEach_withMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger sum = new AtomicInteger(0);
            matrix.forEachIndex((i, j, m) -> sum.addAndGet(m.get(i, j)));
            assertEquals(10, sum.get());
        }

        @Test
        public void test_forEach_withRegion() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            AtomicInteger sum = new AtomicInteger(0);
            matrix.forEachIndex(0, 2, 0, 2, (i, j) -> sum.addAndGet(matrix.get(i, j)));
            assertEquals(12, sum.get()); // 1+2+4+5
        }

        @Test
        public void test_forEach_withRegionAndMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            AtomicInteger sum = new AtomicInteger(0);
            matrix.forEachIndex(1, 3, 1, 3, (i, j, m) -> sum.addAndGet(m.get(i, j)));
            assertEquals(28, sum.get()); // 5+6+8+9
        }

        @Test
        public void test_forEach_emptyMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[0][0]);
            AtomicInteger count = new AtomicInteger(0);
            matrix.forEachIndex((i, j) -> count.incrementAndGet());
            assertEquals(0, count.get());
        }

        // ============ Point Stream Tests ============

        @Test
        public void test_adjacent4Points_centerPosition() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = matrix.adjacent4Points(1, 1).toList();
            assertEquals(4, points.size());
            assertTrue(points.contains(Point.of(0, 1))); // up
            assertTrue(points.contains(Point.of(1, 2))); // right
            assertTrue(points.contains(Point.of(2, 1))); // down
            assertTrue(points.contains(Point.of(1, 0))); // left
        }

        @Test
        public void test_adjacent4Points_topLeftCorner() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.adjacent4Points(0, 0).toList();
            assertEquals(2, points.size());
            assertTrue(points.contains(Point.of(0, 1))); // right
            assertTrue(points.contains(Point.of(1, 0))); // down
        }

        @Test
        public void test_adjacent4Points_bottomRightCorner() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.adjacent4Points(1, 1).toList();
            assertEquals(2, points.size());
            assertTrue(points.contains(Point.of(0, 1))); // up
            assertTrue(points.contains(Point.of(1, 0))); // left
        }

        @Test
        public void test_adjacent8Points_centerPosition() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = matrix.adjacent8Points(1, 1).toList();
            assertEquals(8, points.size());
        }

        @Test
        public void test_adjacent8Points_topLeftCorner() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.adjacent8Points(0, 0).toList();
            assertEquals(3, points.size());
            assertTrue(points.contains(Point.of(0, 1))); // right
            assertTrue(points.contains(Point.of(1, 1))); // rightDown
            assertTrue(points.contains(Point.of(1, 0))); // down
        }

        @Test
        public void test_adjacent8Points_bottomRightCorner() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.adjacent8Points(1, 1).toList();
            assertEquals(3, points.size());
            assertTrue(points.contains(Point.of(0, 0))); // leftUp
            assertTrue(points.contains(Point.of(0, 1))); // up
            assertTrue(points.contains(Point.of(1, 0))); // left
        }

        // ============ Diagonal Point Stream Tests ============

        @Test
        public void test_pointsMainDiagonal_squareMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = matrix.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 2), points.get(2));
        }

        @Test
        public void test_pointsMainDiagonal_nonSquareMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> matrix.pointsMainDiagonal().toList());
        }

        @Test
        public void test_pointsAntiDiagonal_squareMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = matrix.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 2), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void test_pointsAntiDiagonal_nonSquareMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> matrix.pointsAntiDiagonal().toList());
        }

        // ============ Horizontal Point Stream Tests ============

        @Test
        public void test_pointsH_allPoints() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        @Test
        public void test_pointsH_singleRow() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.pointsHorizontal(1).toList();
            assertEquals(2, points.size());
            assertEquals(Point.of(1, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
        }

        @Test
        public void test_pointsH_rowRange() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<Point> points = matrix.pointsHorizontal(1, 3).toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(1, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
            assertEquals(Point.of(2, 1), points.get(3));
        }

        // ============ Vertical Point Stream Tests ============

        @Test
        public void test_pointsV_allPoints() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
            assertEquals(Point.of(0, 1), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        @Test
        public void test_pointsV_singleColumn() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = matrix.pointsVertical(0).toList();
            assertEquals(2, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
        }

        @Test
        public void test_pointsV_columnRange() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Point> points = matrix.pointsVertical(1, 3).toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 1), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(0, 2), points.get(2));
            assertEquals(Point.of(1, 2), points.get(3));
        }

        // ============ Row/Column Point Stream Tests ============

        @Test
        public void test_pointsR_allRows() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<List<Point>> rows = matrix.pointsRows().map(Stream::toList).toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).size());
            assertEquals(Point.of(0, 0), rows.get(0).get(0));
            assertEquals(Point.of(0, 1), rows.get(0).get(1));
        }

        @Test
        public void test_pointsR_rowRange() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<List<Point>> rows = matrix.pointsRows(1, 3).map(Stream::toList).toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).size());
        }

        @Test
        public void test_pointsC_allColumns() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            List<List<Point>> columnCount = matrix.pointsColumns().map(Stream::toList).toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).size());
            assertEquals(Point.of(0, 0), columnCount.get(0).get(0));
            assertEquals(Point.of(1, 0), columnCount.get(0).get(1));
        }

        @Test
        public void test_pointsC_columnRange() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<List<Point>> columnCount = matrix.pointsColumns(0, 2).map(Stream::toList).toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).size());
        }

        // ============ Accept and Apply Tests ============

        @Test
        public void test_accept() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger rows = new AtomicInteger(0);
            matrix.accept(m -> rows.set(m.rowCount()));
            assertEquals(2, rows.get());
        }

        @Test
        public void test_apply() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            int totalElements = matrix.apply(m -> m.rowCount() * m.columnCount());
            assertEquals(4, totalElements);
        }

        @Test
        public void test_apply_returnString() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            String result = matrix.apply(m -> "Matrix " + m.rowCount() + "x" + m.columnCount());
            assertEquals("Matrix 2x2", result);
        }

        // ============ Reshape Tests ============

        @Test
        public void test_reshape_singleParameter() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = matrix.reshape(2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
        }

        @Test
        public void test_reshape_singleParameter_withPadding() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = matrix.reshape(4);
            assertEquals(2, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
            assertEquals(5, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(1, 1));
            assertEquals(0, reshaped.get(1, 2)); // Padded
            assertEquals(0, reshaped.get(1, 3)); // Padded
        }

        // ============ Component Type Test ============

        @Test
        public void test_componentType_intMatrix() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(int.class, matrix.componentType());
        }

        @Test
        public void test_componentType_doubleMatrix() {
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(double.class, matrix.componentType());
        }

        @Test
        public void test_componentType_booleanMatrix() {
            BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            assertEquals(boolean.class, matrix.componentType());
        }

        // ============ Array Method Test ============

        @Test
        public void test_array_returnsInternalArray() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            IntMatrix matrix = IntMatrix.of(arr);
            int[][] returned = matrix.backingArray();
            assertEquals(arr, returned); // Same reference
            returned[0][0] = 999;
            assertEquals(999, matrix.get(0, 0)); // Modification affects matrix
        }

        // ============ Println Test ============

        @Test
        public void test_println_returnsString() {
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            String result = matrix.printAndReturn();
            assertNotNull(result);
            assertTrue(result.contains("1"));
            assertTrue(result.contains("2"));
        }
    }

    @Nested
    class JavadocExampleMatrixTest_AbstractMatrix extends TestBase {
        // ==================== AbstractMatrix streamRows/streamColumns examples ====================

        @Test
        public void testAbstractMatrixStreamRRowSums() {
            // AbstractMatrix.java: IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
            // Row sums: 6 and 15
            IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Integer> rowSums = new ArrayList<>();
            matrix.streamRows().forEach(rowStream -> {
                int sum = rowStream.sum();
                rowSums.add(sum);
            });
            assertEquals(2, rowSums.size());
            assertEquals(6, rowSums.get(0));
            assertEquals(15, rowSums.get(1));
        }

        @Test
        public void testAbstractMatrixStreamCColumnAverages() {
            // AbstractMatrix.java: DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
            // Column averages: 2.0 and 3.0
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            List<Double> colAvgs = new ArrayList<>();
            matrix.streamColumns().forEach(colStream -> {
                double avg = colStream.average().orElse(0);
                colAvgs.add(avg);
            });
            assertEquals(2, colAvgs.size());
            assertEquals(2.0, colAvgs.get(0), 0.001);
            assertEquals(3.0, colAvgs.get(1), 0.001);
        }
    }

    @Nested
    class JavadocExampleUtilsTest_AbstractMatrix extends TestBase {
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
    }

    // Placed here because these assertions cover the shared matrix-shape contract across Matrix and primitive matrix implementations.
    @Nested
    @Tag("2512")
    class MatrixRepresentableShapeValidationTest extends TestBase {

        @Test
        public void testExtendRejectsZeroRowNonZeroColumnShape() {
            Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            BooleanMatrix booleanMatrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
            ByteMatrix byteMatrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
            ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix intMatrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix longMatrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { { 1F, 2F }, { 3F, 4F } });
            DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] { { 1D, 2D }, { 3D, 4D } });

            assertThrows(IllegalArgumentException.class, () -> matrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> booleanMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> byteMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> charMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> shortMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> intMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> longMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> floatMatrix.resize(0, 1));
            assertThrows(IllegalArgumentException.class, () -> doubleMatrix.resize(0, 1));
        }

        @Test
        public void testDirectionalExtendRejectsZeroRowNonZeroColumnShapeOnEmptyMatrix() {
            Matrix<Integer> matrix = Matrix.of(new Integer[0][0]);
            BooleanMatrix booleanMatrix = BooleanMatrix.empty();
            ByteMatrix byteMatrix = ByteMatrix.empty();
            CharMatrix charMatrix = CharMatrix.empty();
            ShortMatrix shortMatrix = ShortMatrix.empty();
            IntMatrix intMatrix = IntMatrix.empty();
            LongMatrix longMatrix = LongMatrix.empty();
            FloatMatrix floatMatrix = FloatMatrix.empty();
            DoubleMatrix doubleMatrix = DoubleMatrix.empty();

            assertThrows(IllegalArgumentException.class, () -> matrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> booleanMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> byteMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> charMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> shortMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> intMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> longMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> floatMatrix.extend(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> doubleMatrix.extend(0, 0, 0, 1));
        }

        @Test
        public void testIndexErrorMessagesInterpolateValues() {
            Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

            IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(5, value -> value));

            assertFalse(ex.getMessage().contains("{}"));
            assertTrue(ex.getMessage().contains("5"));
            assertTrue(ex.getMessage().contains("2"));
        }

        @Test
        public void testRectangularValidationMessageInterpolateValues() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Matrix.of(new Integer[][] { { 1, 2 }, { 3 } }));

            assertFalse(ex.getMessage().contains("{}"));
            assertTrue(ex.getMessage().contains("row 0"));
            assertTrue(ex.getMessage().contains("row 1"));
        }

        @Test
        public void testRowFromRepeatAndDiagonalIsTypeSafe() {
            Matrix<String> repeated = Matrix.repeat(1, 2, "a");
            String[] repeatRow = repeated.rowView(0);
            assertTrue(repeatRow.getClass().getComponentType() == String.class);
            repeatRow[0] = "x";
            assertTrue("x".equals(repeated.get(0, 0)));

            Matrix<String> diagonal = Matrix.mainDiagonal(new String[] { "p", "q" });
            String[] diagonalRow = diagonal.rowView(0);
            assertTrue(diagonalRow.getClass().getComponentType() == String.class);
            assertTrue("p".equals(diagonalRow[0]));
        }

        @Test
        public void testRepeatStillSupportsWiderGenericAssignments() {
            Matrix<Number> matrix = Matrix.repeat(1, 1, 1);
            matrix.set(0, 0, 2.5d);
            Number[] row = matrix.rowView(0);

            assertTrue(Math.abs(matrix.get(0, 0).doubleValue() - 2.5d) < 1e-9d);
            assertTrue(Math.abs(row[0].doubleValue() - 2.5d) < 1e-9d);
        }

        @Test
        public void testFailedSetDoesNotCorruptElementTypeForLaterAllocations() {
            Serializable[][] backing = new String[][] { { "a" } };
            Matrix<Serializable> matrix = new Matrix<>(backing);

            assertThrows(IllegalArgumentException.class, () -> matrix.set(0, 0, 1));

            Matrix<Serializable> transposed = matrix.transpose();
            assertEquals("a", transposed.get(0, 0));
        }

        @Test
        public void testSetWideningSupportsCovariantRows() {
            Number[][] backing = new Number[][] { new Integer[] { 1, 2 } };
            Matrix<Number> matrix = Matrix.of(backing);

            matrix.set(0, 1, 2L);

            assertEquals(2L, matrix.get(0, 1));
            assertEquals(Number.class, matrix.rowView(0).getClass().getComponentType());
        }

        @Test
        public void testRotateAndTransposeOnNxZeroMatricesReturnEmptyShape() {
            Matrix<String> objectMatrix = Matrix.of(new String[][] { {}, {} });
            assertEquals(0, objectMatrix.rotate90().rowCount());
            assertEquals(0, objectMatrix.rotate90().columnCount());
            assertEquals(0, objectMatrix.rotate270().rowCount());
            assertEquals(0, objectMatrix.rotate270().columnCount());
            assertEquals(0, objectMatrix.transpose().rowCount());
            assertEquals(0, objectMatrix.transpose().columnCount());

            BooleanMatrix booleanMatrix = BooleanMatrix.of(new boolean[][] { {}, {} });
            ByteMatrix byteMatrix = ByteMatrix.of(new byte[][] { {}, {} });
            CharMatrix charMatrix = CharMatrix.of(new char[][] { {}, {} });
            ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { {}, {} });
            IntMatrix intMatrix = IntMatrix.of(new int[][] { {}, {} });
            LongMatrix longMatrix = LongMatrix.of(new long[][] { {}, {} });
            FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { {}, {} });
            DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] { {}, {} });

            assertEquals(0, booleanMatrix.transpose().rowCount());
            assertEquals(0, booleanMatrix.rotate90().rowCount());
            assertEquals(0, booleanMatrix.rotate270().rowCount());
            assertEquals(0, byteMatrix.transpose().rowCount());
            assertEquals(0, byteMatrix.rotate90().rowCount());
            assertEquals(0, byteMatrix.rotate270().rowCount());
            assertEquals(0, charMatrix.transpose().rowCount());
            assertEquals(0, charMatrix.rotate90().rowCount());
            assertEquals(0, charMatrix.rotate270().rowCount());
            assertEquals(0, shortMatrix.transpose().rowCount());
            assertEquals(0, shortMatrix.rotate90().rowCount());
            assertEquals(0, shortMatrix.rotate270().rowCount());
            assertEquals(0, intMatrix.transpose().rowCount());
            assertEquals(0, intMatrix.rotate90().rowCount());
            assertEquals(0, intMatrix.rotate270().rowCount());
            assertEquals(0, longMatrix.transpose().rowCount());
            assertEquals(0, longMatrix.rotate90().rowCount());
            assertEquals(0, longMatrix.rotate270().rowCount());
            assertEquals(0, floatMatrix.transpose().rowCount());
            assertEquals(0, floatMatrix.rotate90().rowCount());
            assertEquals(0, floatMatrix.rotate270().rowCount());
            assertEquals(0, doubleMatrix.transpose().rowCount());
            assertEquals(0, doubleMatrix.rotate90().rowCount());
            assertEquals(0, doubleMatrix.rotate270().rowCount());
        }

        @Test
        public void testCopyRejectsUnrepresentableZeroRowNonZeroColumnShape() {
            Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });
            BooleanMatrix booleanMatrix = BooleanMatrix.of(new boolean[][] { { true, false } });
            ByteMatrix byteMatrix = ByteMatrix.of(new byte[][] { { 1, 2 } });
            CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 } });
            IntMatrix intMatrix = IntMatrix.of(new int[][] { { 1, 2 } });
            LongMatrix longMatrix = LongMatrix.of(new long[][] { { 1L, 2L } });
            FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { { 1F, 2F } });
            DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] { { 1D, 2D } });

            assertThrows(IllegalArgumentException.class, () -> matrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> booleanMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> byteMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> charMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> shortMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> intMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> longMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> floatMatrix.copy(0, 0));
            assertThrows(IllegalArgumentException.class, () -> doubleMatrix.copy(0, 0));

            assertThrows(IllegalArgumentException.class, () -> matrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> booleanMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> byteMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> charMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> shortMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> intMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> longMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> floatMatrix.copy(0, 0, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> doubleMatrix.copy(0, 0, 0, 1));
        }

        @Test
        public void testRandomAndRepeatRejectUnrepresentableShape() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.repeat(0, 1, "x"));
            assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.repeat(0, 1, true));
            assertThrows(IllegalArgumentException.class, () -> ByteMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> ByteMatrix.repeat(0, 1, (byte) 1));
            assertThrows(IllegalArgumentException.class, () -> CharMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> CharMatrix.repeat(0, 1, 'a'));
            assertThrows(IllegalArgumentException.class, () -> ShortMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> ShortMatrix.repeat(0, 1, (short) 1));
            assertThrows(IllegalArgumentException.class, () -> IntMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> IntMatrix.repeat(0, 1, 1));
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.repeat(0, 1, 1L));
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> FloatMatrix.repeat(0, 1, 1F));
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.random(0, 1));
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.repeat(0, 1, 1D));
        }

        @Test
        public void testColumnAndDiagonalReadsAreTypeSafeForObjectBackedMatrices() {
            Matrix<String> repeated = Matrix.repeat(2, 2, "a");
            String[] col = repeated.columnCopy(0);
            assertEquals(String.class, col.getClass().getComponentType());
            assertEquals("a", col[0]);

            Matrix<String> diagonal = Matrix.mainDiagonal(new String[] { "x", "y" });
            String[] main = diagonal.getMainDiagonal();
            String[] anti = diagonal.getAntiDiagonal();
            assertEquals(String.class, main.getClass().getComponentType());
            assertEquals(String.class, anti.getClass().getComponentType());
            assertEquals("x", main[0]);
            assertEquals(null, anti[0]);
        }

        @Test
        public void testMapValidatesTargetElementType() {
            Matrix<String> matrix = Matrix.repeat(1, 1, "x");

            assertThrows(IllegalArgumentException.class, () -> matrix.map(v -> v, null));
        }
    }

    // === Missing coverage: elementCount, backingArray identity, reshape edge cases ===

    @Nested
    class ElementCountAndBackingArrayTests extends TestBase {

        @Test
        public void testElementCount_empty() {
            IntMatrix m = IntMatrix.of(new int[0][0]);
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testElementCount_singleElement() {
            IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
            assertEquals(1, m.elementCount());
        }

        @Test
        public void testElementCount_singleRow() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4, 5 } });
            assertEquals(5, m.elementCount());
        }

        @Test
        public void testElementCount_singleColumn() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
            assertEquals(3, m.elementCount());
        }

        @Test
        public void testBackingArray_isSameReference() {
            int[][] data = { { 1, 2 }, { 3, 4 } };
            IntMatrix m = IntMatrix.of(data);
            assertTrue(data == m.backingArray());
        }

        @Test
        public void testBackingArray_mutationAffectsMatrix() {
            int[][] data = { { 1, 2 }, { 3, 4 } };
            IntMatrix m = IntMatrix.of(data);
            data[0][0] = 99;
            assertEquals(99, m.get(0, 0));
        }

        @Test
        public void testReshape_singleColumnCount_zeroThrows() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.reshape(0));
        }

        @Test
        public void testReshape_singleColumnCount_negativeThrows() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.reshape(-1));
        }

        @Test
        public void testReshape_sameColumnCountSameMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix reshaped = m.reshape(3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(6, reshaped.get(1, 2));
        }

        @Test
        public void testReshape_twoArgs_tooSmallThrows() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m.reshape(1, 2));
        }

        @Test
        public void testIsSameShape_nullThrows() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.isSameShape(null));
        }

        @Test
        public void testPointsMainDiagonal_emptyMatrix() {
            IntMatrix m = IntMatrix.of(new int[0][0]);
            assertEquals(0, m.pointsMainDiagonal().count());
        }

        @Test
        public void testPointsAntiDiagonal_emptyMatrix() {
            IntMatrix m = IntMatrix.of(new int[0][0]);
            assertEquals(0, m.pointsAntiDiagonal().count());
        }
    }

}
