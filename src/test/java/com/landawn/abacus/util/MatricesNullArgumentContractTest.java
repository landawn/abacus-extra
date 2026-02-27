package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MatricesNullArgumentContractTest {

    @Test
    public void testZipByteMatrixNullArgumentsThrowIllegalArgumentException() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 5, 6 } });

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip((ByteMatrix) null, m2, (a, b) -> (byte) (a + b)));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (ByteMatrix) null, (a, b) -> (byte) (a + b)));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Throwables.ByteBinaryOperator<RuntimeException>) null));

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip((ByteMatrix) null, m2, m3, (a, b, c) -> (byte) (a + b + c)));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (ByteMatrix) null, m3, (a, b, c) -> (byte) (a + b + c)));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (ByteMatrix) null, (a, b, c) -> (byte) (a + b + c)));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, m3, (Throwables.ByteTernaryOperator<RuntimeException>) null));
    }

    @Test
    public void testZipGenericMatrixNullArgumentsThrowIllegalArgumentException() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "a", "b" } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Double> m3 = Matrix.of(new Double[][] { { 1.0, 2.0 } });

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, (a, b) -> a + b));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, (a, b) -> a + b));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Throwables.BiFunction<String, Integer, String, RuntimeException>) null));

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, (a, b) -> a + b, String.class));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, (a, b) -> a + b, String.class));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(m1, m2, (Throwables.BiFunction<String, Integer, String, RuntimeException>) null, String.class));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (a, b) -> a + b, null));

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, m3, (a, b, c) -> a + b + c.intValue()));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, m3, (a, b, c) -> a + b + c.intValue()));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Matrix<Double>) null, (a, b, c) -> a + b + c.intValue()));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(m1, m2, m3, (Throwables.TriFunction<String, Integer, Double, String, RuntimeException>) null));

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, m3, (a, b, c) -> a + b + c.intValue(), String.class));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, m3, (a, b, c) -> a + b + c.intValue(), String.class));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Matrix<Double>) null, (a, b, c) -> a + b + c.intValue(), String.class));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(m1, m2, m3, (Throwables.TriFunction<String, Integer, Double, String, RuntimeException>) null, String.class));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c.intValue(), null));
    }
}
