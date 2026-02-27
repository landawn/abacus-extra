package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;

public class MatrixParityFuzzTest extends TestBase {

    @Test
    public void testIntAndGenericMatrixParity() {
        final Random random = new Random(123456789L);

        for (int n = 0; n < 400; n++) {
            final int rows = random.nextInt(5);
            final int cols = rows == 0 ? 0 : random.nextInt(5);
            final int[][] source = new int[rows][cols];
            final Integer[][] boxedSource = new Integer[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    final int v = random.nextInt(101) - 50;
                    source[i][j] = v;
                    boxedSource[i][j] = v;
                }
            }

            final IntMatrix intMatrix = IntMatrix.of(source);
            final Matrix<Integer> genericMatrix = Matrix.of(boxedSource);

            assertEquals(intMatrix.rowCount(), genericMatrix.rowCount());
            assertEquals(intMatrix.columnCount(), genericMatrix.columnCount());
            assertEquals(intMatrix.elementCount(), genericMatrix.elementCount());
            assertEquals(intMatrix.isEmpty(), genericMatrix.isEmpty());

            assertArrayEquals(intMatrix.streamH().toArray(), toIntArray(genericMatrix.streamH().toList()));
            assertArrayEquals(intMatrix.streamV().toArray(), toIntArray(genericMatrix.streamV().toList()));

            final List<int[]> intRows = intMatrix.streamR().map(IntStream::toArray).toList();
            final List<int[]> genericRows = genericMatrix.streamR().map(row -> toIntArray(row.toList())).toList();
            assertEquals(intRows.size(), genericRows.size());
            for (int i = 0; i < intRows.size(); i++) {
                assertArrayEquals(intRows.get(i), genericRows.get(i));
            }

            final List<int[]> intCols = intMatrix.streamC().map(IntStream::toArray).toList();
            final List<int[]> genericCols = genericMatrix.streamC().map(col -> toIntArray(col.toList())).toList();
            assertEquals(intCols.size(), genericCols.size());
            for (int i = 0; i < intCols.size(); i++) {
                assertArrayEquals(intCols.get(i), genericCols.get(i));
            }

            assertTransformParity(intMatrix::transpose, genericMatrix::transpose);
            assertTransformParity(intMatrix::rotate90, genericMatrix::rotate90);
            assertMatrixEquals(intMatrix.rotate180(), genericMatrix.rotate180());
            assertTransformParity(intMatrix::rotate270, genericMatrix::rotate270);
            assertMatrixEquals(intMatrix.flipH(), genericMatrix.flipH());
            assertMatrixEquals(intMatrix.flipV(), genericMatrix.flipV());

            final int rowRepeats = random.nextInt(3) + 1;
            final int colRepeats = random.nextInt(3) + 1;
            assertMatrixEquals(intMatrix.repeatElements(rowRepeats, colRepeats), genericMatrix.repeatElements(rowRepeats, colRepeats));
            assertMatrixEquals(intMatrix.repeatMatrix(rowRepeats, colRepeats), genericMatrix.repeatMatrix(rowRepeats, colRepeats));

            final int elements = (int) intMatrix.elementCount();
            int newRows = 0;
            int newCols = 0;

            if (elements == 0) {
                newRows = random.nextInt(5);
                newCols = newRows == 0 ? 0 : random.nextInt(5);
            } else {
                int divisor;
                do {
                    divisor = random.nextInt(elements) + 1;
                } while (elements % divisor != 0);

                newRows = divisor;
                newCols = elements / divisor;
            }

            final IntMatrix reshapedInt = intMatrix.reshape(newRows, newCols);
            final Matrix<Integer> reshapedGeneric = genericMatrix.reshape(newRows, newCols);

            if (elements == 0) {
                assertEquals(reshapedInt.rowCount(), reshapedGeneric.rowCount());
                assertEquals(reshapedInt.columnCount(), reshapedGeneric.columnCount());
            } else {
                assertMatrixEquals(reshapedInt, reshapedGeneric);
            }
        }
    }

    private static int[] toIntArray(final List<Integer> list) {
        final int[] a = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i);
        }

        return a;
    }

    private static void assertTransformParity(final java.util.function.Supplier<IntMatrix> intTransform,
            final java.util.function.Supplier<Matrix<Integer>> genericTransform) {
        try {
            final IntMatrix intResult = intTransform.get();
            final Matrix<Integer> genericResult = genericTransform.get();
            assertMatrixEquals(intResult, genericResult);
        } catch (final IllegalArgumentException e) {
            try {
                genericTransform.get();
            } catch (final IllegalArgumentException expected) {
                return;
            }

            throw e;
        }
    }

    private static void assertMatrixEquals(final IntMatrix intMatrix, final Matrix<Integer> genericMatrix) {
        assertEquals(intMatrix.rowCount(), genericMatrix.rowCount());
        assertEquals(intMatrix.columnCount(), genericMatrix.columnCount());

        for (int i = 0; i < intMatrix.rowCount(); i++) {
            for (int j = 0; j < intMatrix.columnCount(); j++) {
                assertEquals(intMatrix.get(i, j), genericMatrix.get(i, j).intValue());
            }
        }
    }
}
