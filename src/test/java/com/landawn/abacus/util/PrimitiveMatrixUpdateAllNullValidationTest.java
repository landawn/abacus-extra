package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class PrimitiveMatrixUpdateAllNullValidationTest extends TestBase {

    @Test
    public void testByteMatrixUpdateAllNullOperator() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix emptyLike = ByteMatrix.of(new byte[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.ByteUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Byte, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.BytePredicate<RuntimeException>) null, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (byte) 0));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.BytePredicate<RuntimeException>) null, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (byte) 0));
    }

    @Test
    public void testShortMatrixUpdateAllNullOperator() {
        ShortMatrix matrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix emptyLike = ShortMatrix.of(new short[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Short, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.ShortPredicate<RuntimeException>) null, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (short) 0));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.ShortUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.ShortPredicate<RuntimeException>) null, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (short) 0));
    }

    @Test
    public void testLongMatrixUpdateAllNullOperator() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix emptyLike = LongMatrix.of(new long[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.LongUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Long, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.LongPredicate<RuntimeException>) null, 0L));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0L));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.LongPredicate<RuntimeException>) null, 0L));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0L));
    }

    @Test
    public void testFloatMatrixUpdateAllNullOperator() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1f, 2f }, { 3f, 4f } });
        FloatMatrix emptyLike = FloatMatrix.of(new float[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Float, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.FloatPredicate<RuntimeException>) null, 0f));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0f));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.FloatUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.FloatPredicate<RuntimeException>) null, 0f));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0f));
    }

    @Test
    public void testDoubleMatrixUpdateAllNullOperator() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1d, 2d }, { 3d, 4d } });
        DoubleMatrix emptyLike = DoubleMatrix.of(new double[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Double, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.DoublePredicate<RuntimeException>) null, 0d));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0d));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.DoublePredicate<RuntimeException>) null, 0d));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0d));
    }

    @Test
    public void testCharMatrixUpdateAllNullOperator() {
        CharMatrix matrix = CharMatrix.of(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
        CharMatrix emptyLike = CharMatrix.of(new char[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.CharUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Character, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.CharPredicate<RuntimeException>) null, 'x'));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 'x'));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.CharUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.CharPredicate<RuntimeException>) null, 'x'));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 'x'));
    }

    @Test
    public void testBooleanMatrixUpdateAllNullOperator() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        BooleanMatrix emptyLike = BooleanMatrix.of(new boolean[][] { {}, {} });
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Boolean, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.BooleanPredicate<RuntimeException>) null, true));
        assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, true));

        assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.BooleanUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.BooleanPredicate<RuntimeException>) null, true));
        assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, true));
    }
}
