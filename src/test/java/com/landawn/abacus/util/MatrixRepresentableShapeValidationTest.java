package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@Tag("2512")
public class MatrixRepresentableShapeValidationTest extends TestBase {

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
        String[] repeatRow = repeated.row(0);
        assertTrue(repeatRow.getClass().getComponentType() == String.class);
        repeatRow[0] = "x";
        assertTrue("x".equals(repeated.get(0, 0)));

        Matrix<String> diagonal = Matrix.mainDiagonal(new String[] { "p", "q" });
        String[] diagonalRow = diagonal.row(0);
        assertTrue(diagonalRow.getClass().getComponentType() == String.class);
        assertTrue("p".equals(diagonalRow[0]));
    }

    @Test
    public void testRepeatStillSupportsWiderGenericAssignments() {
        Matrix<Number> matrix = Matrix.repeat(1, 1, 1);
        matrix.set(0, 0, 2.5d);
        Number[] row = matrix.row(0);

        assertTrue(Math.abs(matrix.get(0, 0).doubleValue() - 2.5d) < 1e-9d);
        assertTrue(Math.abs(row[0].doubleValue() - 2.5d) < 1e-9d);
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
