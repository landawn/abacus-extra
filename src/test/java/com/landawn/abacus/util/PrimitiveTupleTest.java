package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.IntTuple.IntTuple1;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.u.Optional;

class PrimitiveTupleTest {

    @Nested
    class ArityTests {

        @Test
        void arity_alwaysPositive() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple2 tuple2 = IntTuple.of(5, 10);
            IntTuple3 tuple3 = IntTuple.of(5, 10, 15);

            assertTrue(tuple1.arity() > 0);
            assertTrue(tuple2.arity() > 0);
            assertTrue(tuple3.arity() > 0);
        }

        @Test
        void arity_consistentAcrossCalls() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int arity1 = tuple.arity();
            int arity2 = tuple.arity();
            int arity3 = tuple.arity();

            assertEquals(arity1, arity2);
            assertEquals(arity2, arity3);
        }

        @Test
        void arity_doubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_doubleTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_doubleTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(3, tuple.arity());
        }

        @Test
        void arity_floatTuple2() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_floatTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(3, tuple.arity());
        }

        @Test
        void arity_intTuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_intTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(3, tuple.arity());
        }

        @Test
        void arity_longTuple() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_longTuple2() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_longTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(3, tuple.arity());
        }

        @Test
        void arity_returnsElementCount() {
            // IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            // int size = tuple2.arity();   // returns 2
            IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertEquals(2, tuple2.arity());

            // IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            // int size3 = tuple3.arity();   // returns 3
            IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            assertEquals(3, tuple3.arity());
        }

        @Test
        void arity_returnsElementCount_values_1_0F() {
            FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            assertEquals(1, tuple1.arity());

            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(2, tuple2.arity());

            FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(3, tuple3.arity());
        }

        @Test
        void arity_tuple1() {
            IntTuple1 tuple = IntTuple.of(10);
            assertEquals(1, tuple.arity());
        }

        @Test
        void arity_tuple2() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertEquals(2, tuple.arity());
        }

        @Test
        void arity_tuple3() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(3, tuple.arity());
        }

        @Test
        void arityThroughDoubleTuple() {
            DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(10.5);
            DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(10.5, 20.7);

            assertEquals(1, tuple1.arity());
            assertEquals(2, tuple2.arity());
        }

        @Test
        void arityThroughIntTuple() {
            IntTuple.IntTuple1 tuple1 = IntTuple.of(10);
            IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 30);
            IntTuple.IntTuple4 tuple4 = IntTuple.of(10, 20, 30, 40);

            assertEquals(1, tuple1.arity());
            assertEquals(2, tuple2.arity());
            assertEquals(3, tuple3.arity());
            assertEquals(4, tuple4.arity());
        }

        @Test
        void arityThroughLongTuple() {
            LongTuple.LongTuple1 tuple1 = LongTuple.of(10L);
            LongTuple.LongTuple2 tuple2 = LongTuple.of(10L, 20L);

            assertEquals(1, tuple1.arity());
            assertEquals(2, tuple2.arity());
        }

        @Test
        void differentArityOperations() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple2 tuple2 = FloatTuple.of(5.0f, 10.0f);
            FloatTuple3 tuple3 = FloatTuple.of(5.0f, 10.0f, 15.0f);

            assertEquals(1, tuple1.arity());
            assertEquals(2, tuple2.arity());
            assertEquals(3, tuple3.arity());

            Float sum1 = tuple1.map(t -> t._1);
            Float sum2 = tuple2.map(t -> t._1 + t._2);
            Float sum3 = tuple3.map(t -> t._1 + t._2 + t._3);

            assertEquals(5.0f, sum1, 0.001f);
            assertEquals(15.0f, sum2, 0.001f);
            assertEquals(30.0f, sum3, 0.001f);
        }

        @Test
        void integration_arityConsistency() {
            IntTuple1 tuple1 = IntTuple.of(1);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            IntTuple3 tuple3 = IntTuple.of(1, 2, 3);

            assertEquals(1, tuple1.arity());
            assertEquals(2, tuple2.arity());
            assertEquals(3, tuple3.arity());

            // Arity should be consistent even after operations
            tuple1.filter(t -> true);
            tuple2.map(t -> t._1 + t._2);
            tuple3.toOptional();

            assertEquals(1, tuple1.arity());
            assertEquals(2, tuple2.arity());
            assertEquals(3, tuple3.arity());
        }
    }

    @Nested
    class LowerMedianTests {

        @Test
        void lowerMedianAndStatisticalMedian() throws ReflectiveOperationException {
            assertEquals((byte) 2, ByteTuple.of((byte) 4, (byte) 1, (byte) 3, (byte) 2).lowerMedian());
            assertEquals(2.5d, ByteTuple.of((byte) 4, (byte) 1, (byte) 3, (byte) 2).median());

            assertEquals('B', CharTuple.of('D', 'A', 'C', 'B').lowerMedian());

            assertEquals((short) 2, ShortTuple.of((short) 4, (short) 1, (short) 3, (short) 2).lowerMedian());
            assertEquals(2.5d, ShortTuple.of((short) 4, (short) 1, (short) 3, (short) 2).median());

            final IntTuple.IntTuple4 ints = IntTuple.of(4, 1, 3, 2);
            assertEquals(2, ints.lowerMedian());
            assertEquals(2.5d, ints.median());
            assertEquals(-0.5d, IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE).median());

            assertEquals(2L, LongTuple.of(4L, 1L, 3L, 2L).lowerMedian());
            assertEquals(2.5d, LongTuple.of(4L, 1L, 3L, 2L).median());
            assertEquals(-0.5d, LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE).median());

            assertEquals(2f, FloatTuple.of(4f, 1f, 3f, 2f).lowerMedian());
            assertEquals(2.5d, FloatTuple.of(4f, 1f, 3f, 2f).median());

            final DoubleTuple.DoubleTuple4 doubles = DoubleTuple.of(4d, 1d, 3d, 2d);
            assertEquals(2d, doubles.lowerMedian());
            assertEquals(2.5d, doubles.median());
            assertEquals(4d, doubles._1);
            assertEquals(Double.MAX_VALUE, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).median());
            assertTrue(Double.isNaN(DoubleTuple.of(1d, Double.NaN).median()));

            assertEquals("Cannot compute lowerMedian() for an empty tuple",
                    assertThrows(NoSuchElementException.class, () -> DoubleTuple.from((double[]) null).lowerMedian()).getMessage());
            assertEquals("Cannot compute median() for an empty tuple",
                    assertThrows(NoSuchElementException.class, () -> DoubleTuple.from((double[]) null).median()).getMessage());

            for (final Class<?> tupleType : List.of(ByteTuple.class, CharTuple.class, ShortTuple.class, IntTuple.class, LongTuple.class, FloatTuple.class,
                    DoubleTuple.class)) {
                if (!tupleType.equals(CharTuple.class)) {
                    assertEquals(double.class, tupleType.getMethod("median").getReturnType());
                    assertFalse(tupleType.getMethod("median").isAnnotationPresent(Deprecated.class));
                }
            }
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_accessesAllFields() {
            IntTuple3 tuple = IntTuple.of(100, 200, 300);
            int[] values = new int[3];

            tuple.accept(t -> {
                values[0] = t._1;
                values[1] = t._2;
                values[2] = t._3;
            });

            assertEquals(100, values[0]);
            assertEquals(200, values[1]);
            assertEquals(300, values[2]);
        }

        @Test
        void accept_doubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(3.14, 2.71);
            double[] result = new double[1];

            tuple.accept(t -> result[0] = t._1 + t._2);

            assertEquals(5.85, result[0], 0.001);
        }

        @Test
        void accept_doubleTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            List<Double> values = new ArrayList<>();

            tuple.accept(t -> {
                values.add(t._1);
                values.add(t._2);
            });

            assertEquals(2, values.size());
            assertEquals(1.5, values.get(0).doubleValue(), 0.0001);
            assertEquals(2.5, values.get(1).doubleValue(), 0.0001);
        }

        @Test
        void accept_doubleTuple2_values_1_0_2_0() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            final List<DoubleTuple2> results = new ArrayList<>();
            tuple.accept(t -> results.add(t));
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void accept_doubleTuple3() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            final List<DoubleTuple3> results = new ArrayList<>();
            tuple.accept(t -> results.add(t));
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void accept_executesAction() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            AtomicInteger sum = new AtomicInteger(0);

            tuple.accept(t -> sum.set(t._1 + t._2));

            assertEquals(30, sum.get());
        }

        @Test
        void accept_floatTuple2() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            final List<FloatTuple2> results = new ArrayList<>();
            tuple.accept(t -> results.add(t));
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void accept_floatTuple3() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final List<FloatTuple3> results = new ArrayList<>();
            tuple.accept(t -> results.add(t));
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void accept_intTuple2() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> values = new ArrayList<>();

            tuple.accept(t -> {
                values.add(t._1);
                values.add(t._2);
            });

            assertEquals(2, values.size());
            assertEquals(10, values.get(0).intValue());
            assertEquals(20, values.get(1).intValue());
        }

        @Test
        void accept_intTuple2_values_10_20() throws Exception {
            IntTuple2 tuple = IntTuple.of(10, 20);
            final List<IntTuple2> results = new ArrayList<>();
            tuple.accept(t -> results.add(t));
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void accept_intTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> sums = new ArrayList<>();

            tuple.accept(t -> sums.add(t._1 + t._2 + t._3));

            assertEquals(1, sums.size());
            assertEquals(6, sums.get(0).intValue());
        }

        @Test
        void accept_intTuple3_values_1_2_3() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            final List<IntTuple3> results = new ArrayList<>();
            tuple.accept(t -> results.add(t));
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void accept_longTuple() {
            LongTuple2 tuple = LongTuple.of(1000L, 2000L);
            long[] result = new long[1];

            tuple.accept(t -> result[0] = t._1 * t._2);

            assertEquals(2000000L, result[0]);
        }

        @Test
        void accept_longTuple2() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            List<Long> values = new ArrayList<>();

            tuple.accept(t -> {
                values.add(t._1);
                values.add(t._2);
            });

            assertEquals(2, values.size());
            assertEquals(100L, values.get(0).longValue());
            assertEquals(200L, values.get(1).longValue());
        }

        @Test
        void accept_multipleOperations() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> results = new ArrayList<>();

            tuple.accept(t -> {
                results.add(t._1 * 2);
                results.add(t._2 * 2);
                results.add(t._3 * 2);
            });

            assertEquals(3, results.size());
            assertEquals(2, results.get(0).intValue());
            assertEquals(4, results.get(1).intValue());
            assertEquals(6, results.get(2).intValue());
        }

        @Test
        void accept_returnsVoid() {
            IntTuple2 tuple = IntTuple.of(1, 2);

            int[] sum = new int[1];
            tuple.accept(t -> sum[0] = t._1 + t._2);

            assertEquals(3, sum[0]);
        }

        @Test
        void accept_withException() {
            IntTuple2 tuple = IntTuple.of(10, 0);

            assertThrows(ArithmeticException.class, () -> {
                tuple.accept(t -> {
                    @SuppressWarnings("unused")
                    int result = t._1 / t._2; // Division by zero
                });
            });
        }

        @Test
        void accept_withMultipleCalls() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            AtomicInteger callCount = new AtomicInteger(0);

            tuple.accept(t -> callCount.incrementAndGet());
            tuple.accept(t -> callCount.incrementAndGet());
            tuple.accept(t -> callCount.incrementAndGet());

            assertEquals(3, callCount.get());
        }

        @Test
        void accept_withNull() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            assertThrows(IllegalArgumentException.class, () -> {
                tuple.accept((Throwables.IntBiConsumer<RuntimeException>) null);
            });
        }

        @Test
        void accept_withSideEffect() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            List<String> log = new ArrayList<>();

            tuple.accept(t -> {
                log.add("Processing tuple: " + t._1 + ", " + t._2);
            });

            assertEquals(1, log.size());
            assertTrue(log.get(0).contains("5"));
            assertTrue(log.get(0).contains("10"));
        }

        @Test
        void accept_withSideEffect_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            StringBuilder sb = new StringBuilder();

            tuple.accept(t -> sb.append(t._1).append("-").append(t._2).append("-").append(t._3));

            assertEquals("1-2-3", sb.toString());
        }

        @Test
        void acceptDoubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            final List<Double> values = new ArrayList<>();

            tuple.accept(t -> {
                values.add(t._1);
                values.add(t._2);
            });

            assertEquals(2, values.size());
            assertEquals(10.0, values.get(0), 0.001);
            assertEquals(20.0, values.get(1), 0.001);
        }

        @Test
        void acceptFloatTuple() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            final List<Float> values = new ArrayList<>();

            tuple.accept(t -> {
                values.add(t._1);
                values.add(t._2);
            });

            assertEquals(2, values.size());
            assertEquals(3.0f, values.get(0), 0.001f);
            assertEquals(4.0f, values.get(1), 0.001f);
        }

        @Test
        void acceptThenMap() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> values = new ArrayList<>();

            tuple.accept(t -> {
                values.add(t._1);
                values.add(t._2);
                values.add(t._3);
            });

            Integer sum = tuple.map(t -> t._1 + t._2 + t._3);

            assertEquals(3, values.size());
            assertEquals(6, sum.intValue());
        }

        @Test
        void acceptThenMap_values_5_0F_10_0F() {
            FloatTuple2 tuple = FloatTuple.of(5.0f, 10.0f);
            final List<Float> collected = new ArrayList<>();

            tuple.accept(t -> {
                collected.add(t._1);
                collected.add(t._2);
            });

            Float sum = tuple.map(t -> t._1 + t._2);

            assertEquals(2, collected.size());
            assertEquals(15.0f, sum, 0.001f);
        }

        @Test
        void acceptWithDoubleTuple() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(2.5, 4.0);
            AtomicBoolean wasExecuted = new AtomicBoolean(false);

            tuple.accept(t -> {
                assertTrue(t._1 > 2.0);
                assertTrue(t._2 > 3.0);
                wasExecuted.set(true);
            });

            assertTrue(wasExecuted.get());
        }

        @Test
        void acceptWithException() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);

            assertThrows(IllegalArgumentException.class, () -> {
                tuple.accept(t -> {
                    if (t._1 > 0) {
                        throw new IllegalArgumentException("Test exception");
                    }
                });
            });
        }

        @Test
        void acceptWithIntTuple() {
            IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
            AtomicInteger sum = new AtomicInteger(0);

            tuple.accept(t -> sum.set(t._1 + t._2));

            assertEquals(15, sum.get());
        }

        @Test
        void acceptWithLongTuple() {
            LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);
            AtomicInteger product = new AtomicInteger(0);

            tuple.accept(t -> product.set((int) (t._1 * t._2)));

            assertEquals(20000, product.get());
        }

        @Test
        void acceptWithSideEffects() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final float[] sum = { 0.0f };

            tuple.accept(t -> {
                sum[0] = t._1 + t._2 + t._3;
            });

            assertEquals(6.0f, sum[0], 0.001f);
        }

        @Test
        void baseAcceptMapFilterRejectNullCallbackForEmptyAndNonEmptyTuples() {
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> IntTuple.of(1).accept(null)).getMessage());
            assertEquals("'mapper' cannot be null", assertThrows(IllegalArgumentException.class, () -> LongTuple.of(1L).map(null)).getMessage());
            assertEquals("'predicate' cannot be null", assertThrows(IllegalArgumentException.class, () -> ShortTuple.of((short) 1).filter(null)).getMessage());
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.of((byte) 1).accept(null));
            assertThrows(IllegalArgumentException.class, () -> IntTuple.from(new int[0]).map(null));
            assertThrows(IllegalArgumentException.class, () -> LongTuple.from(new long[0]).filter(null));
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.from(new short[0]).accept(null));
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.from(new byte[0]).map(null));
            assertThrows(IllegalArgumentException.class, () -> IntTuple.of(1, 2).accept((Throwables.Consumer<IntTuple.IntTuple2, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class,
                    () -> LongTuple.of(1L, 2L, 3L).filter((Throwables.Predicate<LongTuple.LongTuple3, RuntimeException>) null));
        }

        @Test
        void integration_acceptThenMap() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            StringBuilder log = new StringBuilder();

            // Accept for logging
            tuple.accept(t -> log.append("Processing: ").append(t._1).append(", ").append(t._2));

            // Map for calculation
            Integer sum = tuple.map(t -> t._1 + t._2);

            assertEquals("Processing: 10, 20", log.toString());
            assertEquals(30, sum);
        }

        @Test
        void multipleAcceptCalls() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> values1 = new ArrayList<>();
            List<Integer> values2 = new ArrayList<>();

            tuple.accept(t -> values1.add(t._1 + t._2));
            tuple.accept(t -> values2.add(t._1 * t._2));

            assertEquals(1, values1.size());
            assertEquals(30, values1.get(0).intValue());
            assertEquals(1, values2.size());
            assertEquals(200, values2.get(0).intValue());
        }
    }

    @Nested
    class MapTests {

        @Test
        void integration_multipleMapOperations() {
            IntTuple2 tuple = IntTuple.of(3, 4);

            // Calculate Pythagorean distance
            Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));

            // Map again to format
            String formatted = tuple.map(t -> String.format(java.util.Locale.ROOT, "Distance from (%d,%d) is %.1f", t._1, t._2, distance));

            assertEquals("Distance from (3,4) is 5.0", formatted);
        }

        @Test
        void map_accessesAllFields() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);

            Integer sum = tuple.map(t -> t._1 + t._2 + t._3);

            assertEquals(6, sum);
        }

        @Test
        void map_canReturnNull() {
            IntTuple2 tuple = IntTuple.of(0, 0);

            String result = tuple.map(t -> t._1 == 0 ? null : "Non-zero");

            assertEquals(null, result);
        }

        @Test
        void map_chainedWithAccept() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> values = new ArrayList<>();

            Integer sum = tuple.map(t -> {
                values.add(t._1);
                values.add(t._2);
                return t._1 + t._2;
            });

            assertEquals(30, sum.intValue());
            assertEquals(2, values.size());
        }

        @Test
        void map_complexTransformation() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String result = tuple.map(t -> {
                int sum = t._1 + t._2 + t._3;
                int product = t._1 * t._2 * t._3;
                return "sum=" + sum + ",product=" + product;
            });
            assertEquals("sum=6,product=6", result);
        }

        @Test
        void map_doubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(10.5, 20.3);

            Double area = tuple.map(t -> t._1 * t._2);

            assertEquals(213.15, area, 0.001);
        }

        @Test
        void map_doubleTuple2_toDouble() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(2.5, 3.5);
            double result = tuple.map(t -> t._1 + t._2);
            assertEquals(6.0, result);
        }

        @Test
        void map_doubleTuple2_toInt() {
            DoubleTuple2 tuple = DoubleTuple.of(5.7, 3.2);
            Integer result = tuple.map(t -> (int) (t._1 + t._2));
            assertEquals(8, result.intValue());
        }

        @Test
        void map_doubleTuple3_toBoolean() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            boolean result = tuple.map(t -> t._1 > 0 && t._2 > 0 && t._3 > 0);
            assertTrue(result);
        }

        @Test
        void map_floatTuple2_toFloat() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            float result = tuple.map((t) -> t._1 * t._2);
            assertEquals(12.0f, result);
        }

        @Test
        void map_floatTuple3_toDouble() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            double result = tuple.map(t -> (t._1 + t._2 + t._3) / 3.0);
            assertEquals(2.0, result, 0.001);
        }

        @Test
        void map_intTuple2_toInt() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Integer sum = tuple.map(t -> t._1 + t._2);
            assertEquals(7, sum.intValue());
        }

        @Test
        void map_intTuple2_toString() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            String result = tuple.map(t -> t._1 + "," + t._2);
            assertEquals("10,20", result);
        }

        @Test
        void map_intTuple2_toString_values_5_10() throws Exception {
            IntTuple2 tuple = IntTuple.of(5, 10);
            String result = tuple.map(t -> "Sum: " + (t._1 + t._2));
            assertEquals("Sum: 15", result);
        }

        @Test
        void map_intTuple3_toDouble() {
            IntTuple3 tuple = IntTuple.of(2, 3, 4);
            Double average = tuple.map(t -> (t._1 + t._2 + t._3) / 3.0);
            assertEquals(3.0, average.doubleValue(), 0.0001);
        }

        @Test
        void map_intTuple3_toInt() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6, result);
        }

        @Test
        void map_longTuple() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);

            String result = tuple.map(t -> "Sum: " + (t._1 + t._2));

            assertEquals("Sum: 300", result);
        }

        @Test
        void map_longTuple2_toBoolean() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            Boolean result = tuple.map(t -> t._1 < t._2);
            assertTrue(result);
        }

        @Test
        void map_longTuple2_toLong() throws Exception {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            long result = tuple.map(t -> t._1 + t._2);
            assertEquals(300L, result);
        }

        @Test
        void map_multipleOperations() {
            IntTuple2 tuple = IntTuple.of(5, 10);

            String result1 = tuple.map(t -> "First: " + t._1);
            String result2 = tuple.map(t -> "Second: " + t._2);
            String result3 = tuple.map(t -> "Sum: " + (t._1 + t._2));

            assertEquals("First: 5", result1);
            assertEquals("Second: 10", result2);
            assertEquals("Sum: 15", result3);
        }

        @Test
        void map_returnsMappedValue() {
            // Calculate Euclidean distance from origin
            // IntTuple.IntTuple2 point = IntTuple.of(3, 4);
            // Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
            // distance = 5.0
            IntTuple.IntTuple2 point = IntTuple.of(3, 4);
            Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
            assertEquals(5.0, distance, 0.001);

            // Convert tuple to a formatted string
            // IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 0);
            // String hexColor = rgb.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));
            // hexColor = "#FF8000"
            IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 0);
            String hexColor = rgb.map(t -> String.format(java.util.Locale.ROOT, "#%02X%02X%02X", t._1, t._2, t._3));
            assertEquals("#FF8000", hexColor);

            // Extract a single computed value
            // DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
            // Double area = dimensions.map(t -> t._1 * t._2);
            // area = 213.15
            DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
            Double area = dimensions.map(t -> t._1 * t._2);
            assertEquals(213.15, area, 0.001);
        }

        @Test
        void map_transformsToBoolean() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Boolean result = tuple.map(t -> t._1 + t._2 > 25);

            assertTrue(result);
        }

        @Test
        void map_transformsToNumber() {
            IntTuple2 tuple = IntTuple.of(3, 4);

            Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));

            assertEquals(5.0, distance, 0.001);
        }

        @Test
        void map_transformsToObject() {
            IntTuple3 tuple = IntTuple.of(255, 128, 0);

            String hexColor = tuple.map(t -> String.format(java.util.Locale.ROOT, "#%02X%02X%02X", t._1, t._2, t._3));

            assertEquals("#FF8000", hexColor);
        }

        @Test
        void map_transformsToString() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            String result = tuple.map(t -> "Values: " + t._1 + ", " + t._2);

            assertEquals("Values: 10, 20", result);
        }

        @Test
        void map_withException() {
            IntTuple2 tuple = IntTuple.of(10, 0);

            assertThrows(ArithmeticException.class, () -> {
                tuple.map(t -> t._1 / t._2); // Division by zero
            });
        }

        @Test
        void map_withMathOperations() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
            assertEquals(5.0, distance.doubleValue(), 0.0001);
        }

        @Test
        void map_withNull() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            assertThrows(IllegalArgumentException.class, () -> {
                tuple.map((Throwables.IntBiFunction<String, RuntimeException>) null);
            });
        }

        @Test
        void mapReturnsNull() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            Object result = tuple.map(t -> null);

            assertEquals(null, result);
        }

        @Test
        void mapToNumber() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Double sum = tuple.map(t -> t._1 + t._2 + t._3);

            assertEquals(6.0, sum, 0.001);
        }

        @Test
        void mapToObject() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            List<Float> result = tuple.map(t -> {
                List<Float> list = new ArrayList<>();
                list.add(t._1);
                list.add(t._2);
                return list;
            });

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(10.0f, result.get(0), 0.001f);
            assertEquals(20.0f, result.get(1), 0.001f);
        }

        @Test
        void mapToString() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            String result = tuple.map(t -> "Values: " + t._1 + ", " + t._2);

            assertEquals("Values: 3.0, 4.0", result);
        }

        @Test
        void mapTransformations() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);

            // Test various map transformations
            Double product = tuple.map(t -> t._1 * t._2);
            assertEquals(12.0, product, 0.001);

            Double division = tuple.map(t -> t._2 / t._1);
            assertEquals(4.0 / 3.0, division, 0.001);

            Boolean comparison = tuple.map(t -> t._1 < t._2);
            assertTrue(comparison);
        }

        @Test
        void mapWithComplexCalculation() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));

            assertEquals(5.0, distance, 0.001);
        }

        @Test
        void mapWithDoubleTuple() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);

            Double average = tuple.map(t -> (t._1 + t._2) / 2.0);
            assertEquals(2.0, average, 0.001);

            Boolean allPositive = tuple.map(t -> t._1 > 0 && t._2 > 0);
            assertTrue(allPositive);
        }

        @Test
        void mapWithException() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);

            assertThrows(ArithmeticException.class, () -> {
                tuple.map(t -> {
                    if (t._1 > 0) {
                        throw new ArithmeticException("Test exception");
                    }
                    return 0;
                });
            });
        }

        @Test
        void mapWithIntTuple() {
            IntTuple.IntTuple2 tuple = IntTuple.of(3, 4);

            String result = tuple.map(t -> "Sum: " + (t._1 + t._2));
            assertEquals("Sum: 7", result);

            Integer product = tuple.map(t -> t._1 * t._2);
            assertEquals(12, product);

            Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
            assertEquals(5.0, distance, 0.001);
        }

        @Test
        void mapWithLongTuple() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);

            String result = tuple.map(t -> String.format(java.util.Locale.ROOT, "Values: %d, %d, %d", t._1, t._2, t._3));
            assertEquals("Values: 100, 200, 300", result);

            Long sum = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(600L, sum);
        }

        @Test
        void multipleMapCalls() {
            IntTuple2 tuple = IntTuple.of(3, 4);

            Integer sum = tuple.map(t -> t._1 + t._2);
            Integer product = tuple.map(t -> t._1 * t._2);
            Double average = tuple.map(t -> (t._1 + t._2) / 2.0);

            assertEquals(7, sum.intValue());
            assertEquals(12, product.intValue());
            assertEquals(3.5, average.doubleValue(), 0.0001);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void chainedOperationsWithFilterFailure() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);

            Optional<String> result = tuple.filter(t -> t._1 > 10) // This will fail
                    .map(t -> "Large values: " + t._1 + ", " + t._2);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_alwaysFalse() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.filter(t -> false);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_alwaysTrue() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.filter(t -> true);

            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filter_appliesPredicate() {
            // Filter for positive values
            // IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
            // positive.isPresent() = true
            IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple.IntTuple2> positive = tuple.filter(t -> t._1 > 0 && t._2 > 0);
            assertTrue(positive.isPresent());

            // Filter for negative values
            // negative.isPresent() = false
            Optional<IntTuple.IntTuple2> negative = tuple.filter(t -> t._1 < 0 || t._2 < 0);
            assertFalse(negative.isPresent());

            // Chain with other operations
            // IntTuple.IntTuple3 values = IntTuple.of(10, 20, 30);
            // result = "Sum is: 60"
            IntTuple.IntTuple3 values = IntTuple.of(10, 20, 30);
            String result = values.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");
            assertEquals("Sum is: 60", result);
        }

        @Test
        void filter_chainWithMap() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);

            String result = tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");

            assertEquals("Sum is: 60", result);
        }

        @Test
        void filter_chainWithMapNoMatch() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);

            String result = tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");

            assertEquals("Sum too small", result);
        }

        @Test
        void filter_complexCondition() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);

            Optional<IntTuple3> result = tuple.filter(t -> t._1 < t._2 && t._2 < t._3 && (t._1 + t._2 + t._3) == 60);

            assertTrue(result.isPresent());
        }

        @Test
        void filter_doubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(45.5, -122.6);

            Optional<DoubleTuple2> result = tuple.filter(t -> t._1 >= -90 && t._1 <= 90 && t._2 >= -180 && t._2 <= 180);

            assertTrue(result.isPresent());
        }

        @Test
        void filter_doubleTuple2_false() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.5, 4.5);
            Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 < 5.0);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_doubleTuple2_match() {
            DoubleTuple2 tuple = DoubleTuple.of(3.5, 7.5);
            Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 > 10.0);

            assertTrue(result.isPresent());
            assertEquals(3.5, result.get()._1, 0.0001);
            assertEquals(7.5, result.get()._2, 0.0001);
        }

        @Test
        void filter_doubleTuple2_noMatch() {
            DoubleTuple2 tuple = DoubleTuple.of(3.5, 4.5);
            Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 > 10.0);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_doubleTuple2_true() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.5, 4.5);
            Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 == 8.0);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_doubleTuple3_false() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter(t -> t._1 < 0);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_doubleTuple3_true() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter(t -> t._1 > 0 && t._2 > 0 && t._3 > 0);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_floatTuple2_false() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            Optional<FloatTuple2> result = tuple.filter(t -> t._1 < 0.0f);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_floatTuple2_true() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            Optional<FloatTuple2> result = tuple.filter(t -> t._1 > 1.0f);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_floatTuple3_false() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 10.0f);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_floatTuple3_true() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 > 5.0f);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_intTuple2_false() throws Exception {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple2> result = tuple.filter(t -> t._1 < 0);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_intTuple2_match() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);

            assertTrue(result.isPresent());
            assertEquals(5, result.get()._1);
            assertEquals(10, result.get()._2);
        }

        @Test
        void filter_intTuple2_noMatch() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple2> result = tuple.filter(t -> t._1 > 10);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_intTuple2_true() throws Exception {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filter_intTuple3_false() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 < 0);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_intTuple3_match() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 == 6);

            assertTrue(result.isPresent());
            assertEquals(1, result.get()._1);
            assertEquals(2, result.get()._2);
            assertEquals(3, result.get()._3);
        }

        @Test
        void filter_intTuple3_noMatch() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 == 10);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_intTuple3_true() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 > 5);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_longTuple() {
            LongTuple2 tuple = LongTuple.of(1000L, 2000L);

            Optional<LongTuple2> result = tuple.filter(t -> t._1 + t._2 > 2500L);

            assertTrue(result.isPresent());
        }

        @Test
        void filter_longTuple2_false() throws Exception {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            Optional<LongTuple2> result = tuple.filter(t -> t._1 > t._2);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_longTuple2_match() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            Optional<LongTuple2> result = tuple.filter(t -> t._1 < t._2);

            assertTrue(result.isPresent());
            assertEquals(100L, result.get()._1);
            assertEquals(200L, result.get()._2);
        }

        @Test
        void filter_longTuple2_true() throws Exception {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            Optional<LongTuple2> result = tuple.filter(t -> t._1 < t._2);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_matchingPredicate() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.filter(t -> t._1 + t._2 > 25);

            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filter_mixedValues() {
            IntTuple2 tuple = IntTuple.of(-5, 10);
            Optional<IntTuple2> result = tuple.filter(t -> t._1 < 0 && t._2 > 0);

            assertTrue(result.isPresent());
        }

        @Test
        void filter_negativeValues() {
            IntTuple2 tuple = IntTuple.of(-5, -10);
            Optional<IntTuple2> result = tuple.filter(t -> t._1 < 0 && t._2 < 0);

            assertTrue(result.isPresent());
            assertEquals(-5, result.get()._1);
            assertEquals(-10, result.get()._2);
        }

        @Test
        void filter_nocountMatchBetweeningPredicate() {
            IntTuple2 tuple = IntTuple.of(5, 10);

            Optional<IntTuple2> result = tuple.filter(t -> t._1 + t._2 > 100);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_withComplexPredicate() {
            IntTuple3 tuple = IntTuple.of(2, 4, 6);
            Optional<IntTuple3> result = tuple.filter(t -> t._1 % 2 == 0 && t._2 % 2 == 0 && t._3 % 2 == 0);

            assertTrue(result.isPresent());
        }

        @Test
        void filter_withException() {
            IntTuple2 tuple = IntTuple.of(10, 0);

            assertThrows(ArithmeticException.class, () -> {
                tuple.filter(t -> t._1 / t._2 > 5); // Division by zero
            });
        }

        @Test
        void filter_withNegativeValues() {
            IntTuple2 tuple = IntTuple.of(-5, -10);

            Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0);

            assertFalse(result.isPresent());
        }

        @Test
        void filter_withNull() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            assertThrows(IllegalArgumentException.class, () -> {
                tuple.filter((Throwables.IntBiPredicate<RuntimeException>) null);
            });
        }

        @Test
        void filter_withPositiveValues() {
            IntTuple2 tuple = IntTuple.of(5, 10);

            Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);

            assertTrue(result.isPresent());
        }

        @Test
        void filterChaining() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

            Optional<Float> result = tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> t._1 + t._2 + t._3);

            assertTrue(result.isPresent());
            assertEquals(60.0f, result.get(), 0.001f);
        }

        @Test
        void filterChainingWithEmpty() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

            Optional<Float> result = tuple.filter(t -> t._1 + t._2 + t._3 > 100).map(t -> t._1 + t._2 + t._3);

            assertFalse(result.isPresent());
        }

        @Test
        void filterEmptyWhenFalse() {
            FloatTuple2 tuple = FloatTuple.of(5.0f, 10.0f);
            Optional<FloatTuple2> result = tuple.filter(t -> t._1 < 0);

            assertFalse(result.isPresent());
        }

        @Test
        void filterPresentWhenTrue() {
            FloatTuple2 tuple = FloatTuple.of(5.0f, 10.0f);
            Optional<FloatTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);

            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterRangeValidation() {
            DoubleTuple2 coords = DoubleTuple.of(45.5, -122.6);

            Optional<DoubleTuple2> validCoords = coords.filter(t -> t._1 >= -90 && t._1 <= 90 && // valid latitude
                    t._2 >= -180 && t._2 <= 180 // valid longitude
            );

            assertTrue(validCoords.isPresent());

            Optional<DoubleTuple2> invalidCoords = coords.filter(t -> t._1 >= 0 && t._1 <= 45 // restricted latitude
            );

            assertFalse(invalidCoords.isPresent());
        }

        @Test
        void filterThenMap() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<Integer> result = tuple.filter(t -> t._1 > 0).map(t -> t._1 + t._2);

            assertTrue(result.isPresent());
            assertEquals(15, result.get().intValue());
        }

        @Test
        void filterThenMap_noMatch() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<Integer> result = tuple.filter(t -> t._1 > 10).map(t -> t._1 + t._2);

            assertFalse(result.isPresent());
        }

        @Test
        void filterThenMapThenAccept() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);

            Optional<String> result = tuple.filter(t -> t._1 + t._2 + t._3 > 5).map(t -> "Sum is: " + (t._1 + t._2 + t._3));

            result.ifPresent(s -> assertEquals("Sum is: 6.0", s));
            assertTrue(result.isPresent());
        }

        @Test
        void filterWithComplexPredicate() {
            DoubleTuple3 tuple = DoubleTuple.of(45.5, -122.6, 100.0);
            Optional<DoubleTuple3> result = tuple.filter(t -> t._1 >= -90 && t._1 <= 90 && // valid latitude
                    t._2 >= -180 && t._2 <= 180 // valid longitude
            );

            assertTrue(result.isPresent());
        }

        @Test
        void filterWithDoubleTuple() {
            DoubleTuple.DoubleTuple2 positivesTuple = DoubleTuple.of(1.5, 2.7);
            DoubleTuple.DoubleTuple2 negativesTuple = DoubleTuple.of(-1.5, -2.7);

            Optional<DoubleTuple.DoubleTuple2> positiveResult = positivesTuple.filter(t -> t._1 > 0 && t._2 > 0);
            Optional<DoubleTuple.DoubleTuple2> negativeResult = negativesTuple.filter(t -> t._1 > 0 && t._2 > 0);

            assertTrue(positiveResult.isPresent());
            assertFalse(negativeResult.isPresent());
        }

        @Test
        void filterWithException() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);

            assertThrows(IllegalStateException.class, () -> {
                tuple.filter(t -> {
                    if (t._1 > 0) {
                        throw new IllegalStateException("Test exception");
                    }
                    return true;
                });
            });
        }

        @Test
        void filterWithIntTuple() {
            IntTuple.IntTuple2 positivesTuple = IntTuple.of(5, 10);
            IntTuple.IntTuple2 mixedTuple = IntTuple.of(-5, 10);

            Optional<IntTuple.IntTuple2> positiveResult = positivesTuple.filter(t -> t._1 > 0 && t._2 > 0);
            Optional<IntTuple.IntTuple2> mixedResult = mixedTuple.filter(t -> t._1 > 0 && t._2 > 0);

            assertTrue(positiveResult.isPresent());
            assertFalse(mixedResult.isPresent());

            assertSame(positivesTuple, positiveResult.get());
        }

        @Test
        void filterWithLongTuple() {
            LongTuple.LongTuple3 largeTuple = LongTuple.of(1000L, 2000L, 3000L);
            LongTuple.LongTuple3 smallTuple = LongTuple.of(1L, 2L, 3L);

            Optional<LongTuple.LongTuple3> largeResult = largeTuple.filter(t -> t._1 > 100 && t._2 > 100 && t._3 > 100);
            Optional<LongTuple.LongTuple3> smallResult = smallTuple.filter(t -> t._1 > 100 && t._2 > 100 && t._3 > 100);

            assertTrue(largeResult.isPresent());
            assertFalse(smallResult.isPresent());
        }

        @Test
        void functionalChain_intTuple2_filtered() throws Exception {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<String> result = tuple.filter(t -> t._1 < 0).map(t -> "Sum: " + (t._1 + t._2));
            assertFalse(result.isPresent());
        }

        @Test
        void integration_filterMapAccept() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            AtomicInteger result = new AtomicInteger(0);

            tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> t._1 + t._2 + t._3).ifPresent(sum -> result.set(sum));

            assertEquals(60, result.get());
        }

        @Test
        void multipleFilterCalls() {
            IntTuple2 tuple = IntTuple.of(5, 10);

            Optional<IntTuple2> filter1 = tuple.filter(t -> t._1 > 0);
            Optional<IntTuple2> filter2 = tuple.filter(t -> t._1 < 10);
            Optional<IntTuple2> filter3 = tuple.filter(t -> t._1 == 5 && t._2 == 10);

            assertTrue(filter1.isPresent());
            assertTrue(filter2.isPresent());
            assertTrue(filter3.isPresent());
        }

        @Test
        void multipleFiltersInSequence() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

            Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 0).filter(t -> t._2 > 15).filter(t -> t._3 > 25);

            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void integration_toOptionalFilterMap() {
            IntTuple2 tuple = IntTuple.of(5, 10);

            String result = tuple.toOptional().filter(t -> t._1 > 0).map(t -> "Positive: " + t._1).orElse("Not positive");

            assertEquals("Positive: 5", result);
        }

        @Test
        void toOptional_chainWithFilter() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.toOptional().filter(t -> t._1 > 5);

            assertTrue(result.isPresent());
        }

        @Test
        void toOptional_chainWithMap() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);

            String result = tuple.toOptional().map(t -> "Coordinates: " + t._1 + ", " + t._2 + ", " + t._3).orElse("No coordinates");

            assertEquals("Coordinates: 10, 20, 30", result);
        }

        @Test
        void toOptional_containsSameTuple() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.toOptional();

            assertEquals(tuple, result.get());
        }

        @Test
        void toOptional_doubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);

            Optional<DoubleTuple2> result = tuple.toOptional();

            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void toOptional_doubleTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            Optional<DoubleTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(1.5, optional.get()._1, 0.0001);
            assertEquals(2.5, optional.get()._2, 0.0001);
        }

        @Test
        void toOptional_doubleTuple2_values_1_0_2_0() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            Optional<DoubleTuple2> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_doubleTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_floatTuple2() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            Optional<FloatTuple2> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_floatTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_ifPresent() {
            DoubleTuple2 tuple = DoubleTuple.of(3.14, 2.71);
            AtomicBoolean executed = new AtomicBoolean(false);

            tuple.toOptional().ifPresent(t -> executed.set(true));

            assertTrue(executed.get());
        }

        @Test
        void toOptional_intTuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            Optional<IntTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(1, optional.get()._1);
            assertEquals(2, optional.get()._2);
        }

        @Test
        void toOptional_intTuple2_values_1_2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            Optional<IntTuple2> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_intTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(1, optional.get()._1);
            assertEquals(2, optional.get()._2);
            assertEquals(3, optional.get()._3);
        }

        @Test
        void toOptional_intTuple3_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_isPresent() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.toOptional();

            assertTrue(result.isPresent());
        }

        @Test
        void toOptional_largeValues() {
            LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MIN_VALUE);
            Optional<LongTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(Long.MAX_VALUE, optional.get()._1);
            assertEquals(Long.MIN_VALUE, optional.get()._2);
        }

        @Test
        void toOptional_longTuple() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);

            Optional<LongTuple2> result = tuple.toOptional();

            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void toOptional_longTuple2() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            Optional<LongTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(100L, optional.get()._1);
            assertEquals(200L, optional.get()._2);
        }

        @Test
        void toOptional_longTuple2_values_1L_2L() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            Optional<LongTuple2> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_longTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        @Test
        void toOptional_multipleCalls() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result1 = tuple.toOptional();
            Optional<IntTuple2> result2 = tuple.toOptional();

            assertEquals(result1.get(), result2.get());
        }

        @Test
        void toOptional_negativeValues() {
            IntTuple2 tuple = IntTuple.of(-1, -2);
            Optional<IntTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(-1, optional.get()._1);
            assertEquals(-2, optional.get()._2);
        }

        @Test
        void toOptional_neverEmpty() {
            IntTuple1 tuple1 = IntTuple.of(1);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            IntTuple3 tuple3 = IntTuple.of(1, 2, 3);

            assertTrue(tuple1.toOptional().isPresent());
            assertTrue(tuple2.toOptional().isPresent());
            assertTrue(tuple3.toOptional().isPresent());
        }

        @Test
        void toOptional_notNull() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            Optional<IntTuple2> result = tuple.toOptional();

            assertNotNull(result);
        }

        @Test
        void toOptional_orElseNotCalled() {
            IntTuple2 tuple = IntTuple.of(10, 20);

            IntTuple2 result = tuple.toOptional().orElse(IntTuple.of(0, 0));

            assertEquals(tuple, result);
        }

        @Test
        void toOptional_wrapsValue() {
            // IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            // optional.isPresent() = true
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            Optional<IntTuple.IntTuple2> optional = tuple.toOptional();
            assertTrue(optional.isPresent());
        }

        @Test
        void toOptional_zeroValues() {
            IntTuple2 tuple = IntTuple.of(0, 0);
            Optional<IntTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(0, optional.get()._1);
            assertEquals(0, optional.get()._2);
        }

        @Test
        void toOptionalAlwaysPresent() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            Optional<FloatTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertEquals(tuple, optional.get());
        }

        @Test
        void toOptionalThenFilter() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple2> optional = tuple.toOptional();
            Optional<IntTuple2> filtered = optional.filter(t -> t._1 > 0 && t._2 > 0);

            assertTrue(filtered.isPresent());
            assertEquals(5, filtered.get()._1);
            assertEquals(10, filtered.get()._2);
        }

        @Test
        void toOptionalWithChaining() {
            DoubleTuple3 tuple = DoubleTuple.of(10.0, 20.0, 30.0);

            String result = tuple.toOptional().filter(t -> t._1 > 0).map(t -> "Sum: " + (t._1 + t._2 + t._3)).orElse("Invalid");

            assertEquals("Sum: 60.0", result);
        }

        @Test
        void toOptionalWithDoubleTuple() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.1, 2.2, 3.3);

            Optional<DoubleTuple.DoubleTuple3> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertSame(tuple, optional.get());
            assertEquals(1.1, optional.get()._1, 0.001);
            assertEquals(2.2, optional.get()._2, 0.001);
            assertEquals(3.3, optional.get()._3, 0.001);
        }

        @Test
        void toOptionalWithIfPresent() {
            FloatTuple2 tuple = FloatTuple.of(3.14f, 2.71f);
            final List<Float> values = new ArrayList<>();

            tuple.toOptional().ifPresent(t -> {
                values.add(t._1);
                values.add(t._2);
            });

            assertEquals(2, values.size());
            assertEquals(3.14f, values.get(0), 0.001f);
            assertEquals(2.71f, values.get(1), 0.001f);
        }

        @Test
        void toOptionalWithIntTuple() {
            IntTuple.IntTuple1 tuple = IntTuple.of(42);

            Optional<IntTuple.IntTuple1> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertSame(tuple, optional.get());
            assertEquals(42, optional.get()._1);
        }

        @Test
        void toOptionalWithLongTuple() {
            LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);

            Optional<LongTuple.LongTuple2> optional = tuple.toOptional();

            assertTrue(optional.isPresent());
            assertSame(tuple, optional.get());
            assertEquals(100L, optional.get()._1);
            assertEquals(200L, optional.get()._2);
        }

        @Test
        void toOptionalWithOrElse() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);

            DoubleTuple1 result = tuple.toOptional().filter(t -> t._1 > 100).orElse(DoubleTuple.of(0.0));

            assertEquals(0.0, result._1, 0.001);
        }
    }

    @Nested
    class TypeHierarchyTests {

        @Test
        void baseIsSealedToBuiltInFamilies() {
            assertTrue(PrimitiveTuple.class.isSealed(), "PrimitiveTuple should be sealed");
            assertTrue(Modifier.isAbstract(PrimitiveTuple.class.getModifiers()), "PrimitiveTuple should be abstract");

            final Class<?>[] actualPermittedTypes = PrimitiveTuple.class.getPermittedSubclasses();
            final Class<?>[] expectedPermittedTypes = { BooleanTuple.class, ByteTuple.class, CharTuple.class, DoubleTuple.class, FloatTuple.class,
                    IntTuple.class, LongTuple.class, ShortTuple.class };

            assertEquals(expectedPermittedTypes.length, actualPermittedTypes.length);
            assertEquals(Set.of(expectedPermittedTypes), Set.of(actualPermittedTypes));

            for (final Class<?> permittedType : expectedPermittedTypes) {
                assertTrue(PrimitiveTuple.class.isAssignableFrom(permittedType));
                assertTrue(permittedType.isSealed(), permittedType.getSimpleName() + " should be sealed");
            }
        }

        @Test
        void familiesAreSealedToBuiltInArities() {
            assertSealedTupleFamily(BooleanTuple.class, BooleanTuple.BooleanTuple0.class, BooleanTuple.BooleanTuple1.class, BooleanTuple.BooleanTuple2.class,
                    BooleanTuple.BooleanTuple3.class, BooleanTuple.BooleanTuple4.class, BooleanTuple.BooleanTuple5.class, BooleanTuple.BooleanTuple6.class,
                    BooleanTuple.BooleanTuple7.class, BooleanTuple.BooleanTuple8.class, BooleanTuple.BooleanTuple9.class);
            assertSealedTupleFamily(ByteTuple.class, ByteTuple.ByteTuple0.class, ByteTuple.ByteTuple1.class, ByteTuple.ByteTuple2.class,
                    ByteTuple.ByteTuple3.class, ByteTuple.ByteTuple4.class, ByteTuple.ByteTuple5.class, ByteTuple.ByteTuple6.class, ByteTuple.ByteTuple7.class,
                    ByteTuple.ByteTuple8.class, ByteTuple.ByteTuple9.class);
            assertSealedTupleFamily(CharTuple.class, CharTuple.CharTuple0.class, CharTuple.CharTuple1.class, CharTuple.CharTuple2.class,
                    CharTuple.CharTuple3.class, CharTuple.CharTuple4.class, CharTuple.CharTuple5.class, CharTuple.CharTuple6.class, CharTuple.CharTuple7.class,
                    CharTuple.CharTuple8.class, CharTuple.CharTuple9.class);
            assertSealedTupleFamily(DoubleTuple.class, DoubleTuple.DoubleTuple0.class, DoubleTuple.DoubleTuple1.class, DoubleTuple.DoubleTuple2.class,
                    DoubleTuple.DoubleTuple3.class, DoubleTuple.DoubleTuple4.class, DoubleTuple.DoubleTuple5.class, DoubleTuple.DoubleTuple6.class,
                    DoubleTuple.DoubleTuple7.class, DoubleTuple.DoubleTuple8.class, DoubleTuple.DoubleTuple9.class);
            assertSealedTupleFamily(FloatTuple.class, FloatTuple.FloatTuple0.class, FloatTuple.FloatTuple1.class, FloatTuple.FloatTuple2.class,
                    FloatTuple.FloatTuple3.class, FloatTuple.FloatTuple4.class, FloatTuple.FloatTuple5.class, FloatTuple.FloatTuple6.class,
                    FloatTuple.FloatTuple7.class, FloatTuple.FloatTuple8.class, FloatTuple.FloatTuple9.class);
            assertSealedTupleFamily(IntTuple.class, IntTuple.IntTuple0.class, IntTuple.IntTuple1.class, IntTuple.IntTuple2.class, IntTuple.IntTuple3.class,
                    IntTuple.IntTuple4.class, IntTuple.IntTuple5.class, IntTuple.IntTuple6.class, IntTuple.IntTuple7.class, IntTuple.IntTuple8.class,
                    IntTuple.IntTuple9.class);
            assertSealedTupleFamily(LongTuple.class, LongTuple.LongTuple0.class, LongTuple.LongTuple1.class, LongTuple.LongTuple2.class,
                    LongTuple.LongTuple3.class, LongTuple.LongTuple4.class, LongTuple.LongTuple5.class, LongTuple.LongTuple6.class, LongTuple.LongTuple7.class,
                    LongTuple.LongTuple8.class, LongTuple.LongTuple9.class);
            assertSealedTupleFamily(ShortTuple.class, ShortTuple.ShortTuple0.class, ShortTuple.ShortTuple1.class, ShortTuple.ShortTuple2.class,
                    ShortTuple.ShortTuple3.class, ShortTuple.ShortTuple4.class, ShortTuple.ShortTuple5.class, ShortTuple.ShortTuple6.class,
                    ShortTuple.ShortTuple7.class, ShortTuple.ShortTuple8.class, ShortTuple.ShortTuple9.class);
        }
    }

    @Nested
    class ContractTests {

        @Test
        void complexFunctionalPipeline() {
            FloatTuple3 coords = FloatTuple.of(10.0f, 20.0f, 30.0f);

            String description = coords.toOptional()
                    .filter(t -> t._1 > 0 && t._2 > 0 && t._3 > 0)
                    .map(t -> String.format(java.util.Locale.ROOT, "Coordinates: (%.1f, %.1f, %.1f)", t._1, t._2, t._3))
                    .orElse("Invalid coordinates");

            assertEquals("Coordinates: (10.0, 20.0, 30.0)", description);
        }

        @Test
        void doubleTuple_consistency() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2, tuple.arity());

            tuple.accept(t -> {
                assertEquals(1.5, t._1, 0.0001);
                assertEquals(2.5, t._2, 0.0001);
            });

            Double sum = tuple.map(t -> t._1 + t._2);
            assertEquals(4.0, sum.doubleValue(), 0.0001);

            Optional<DoubleTuple2> opt = tuple.filter(t -> t._1 > 0);
            assertTrue(opt.isPresent());
        }

        @Test
        void edgeCase_maxDoubleValues() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE);

            tuple.accept(t -> {
                assertEquals(Double.MAX_VALUE, t._1);
                assertEquals(Double.MAX_VALUE, t._2);
            });
        }

        @Test
        void edgeCase_maxIntValues() {
            IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE);

            tuple.accept(t -> {
                assertEquals(Integer.MAX_VALUE, t._1);
                assertEquals(Integer.MAX_VALUE, t._2);
            });
        }

        @Test
        void edgeCase_maxLongValues() {
            LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE);

            tuple.accept(t -> {
                assertEquals(Long.MAX_VALUE, t._1);
                assertEquals(Long.MAX_VALUE, t._2);
            });
        }

        @Test
        void edgeCase_minDoubleValues() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE);

            tuple.accept(t -> {
                assertEquals(Double.MIN_VALUE, t._1);
                assertEquals(Double.MIN_VALUE, t._2);
            });
        }

        @Test
        void edgeCase_minIntValues() {
            IntTuple2 tuple = IntTuple.of(Integer.MIN_VALUE, Integer.MIN_VALUE);

            tuple.accept(t -> {
                assertEquals(Integer.MIN_VALUE, t._1);
                assertEquals(Integer.MIN_VALUE, t._2);
            });
        }

        @Test
        void edgeCase_minLongValues() {
            LongTuple2 tuple = LongTuple.of(Long.MIN_VALUE, Long.MIN_VALUE);

            tuple.accept(t -> {
                assertEquals(Long.MIN_VALUE, t._1);
                assertEquals(Long.MIN_VALUE, t._2);
            });
        }

        @Test
        void edgeCase_mixedSignValues() {
            IntTuple3 tuple = IntTuple.of(-10, 0, 10);

            Integer sum = tuple.map(t -> t._1 + t._2 + t._3);

            assertEquals(0, sum);
        }

        @Test
        void edgeCase_negativeValues() {
            IntTuple2 tuple = IntTuple.of(-10, -20);

            Integer sum = tuple.map(t -> t._1 + t._2);

            assertEquals(-30, sum);
        }

        @Test
        void edgeCase_zeroValues() {
            IntTuple2 tuple = IntTuple.of(0, 0);

            tuple.accept(t -> {
                assertEquals(0, t._1);
                assertEquals(0, t._2);
            });

            Integer sum = tuple.map(t -> t._1 + t._2);
            assertEquals(0, sum);
        }

        @Test
        void exceptionHandlingInOperations() {
            IntTuple.IntTuple2 tuple = IntTuple.of(10, 0);

            // Test that exceptions thrown in lambda functions are properly propagated
            assertThrows(ArithmeticException.class, () -> tuple.map(t -> t._1 / t._2)); // Division by zero
        }

        @Test
        void floatAndDoubleTupleOperations() {
            FloatTuple2 floatTuple = FloatTuple.of(1.5f, 2.5f);
            DoubleTuple2 doubleTuple = DoubleTuple.of(1.5, 2.5);

            Float floatSum = floatTuple.map(t -> t._1 + t._2);
            Double doubleSum = doubleTuple.map(t -> t._1 + t._2);

            assertEquals(4.0f, floatSum, 0.001f);
            assertEquals(4.0, doubleSum, 0.001);
        }

        @Test
        void forEachRejectsNullConsumerForEmptyAndNonEmptyTuples() {
            assertThrows(IllegalArgumentException.class, () -> BooleanTuple.from(new boolean[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> BooleanTuple.of(true, false).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.from(new byte[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.of((byte) 1, (byte) 2).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> CharTuple.from(new char[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> CharTuple.of('a', 'b').forEach(null));
            assertThrows(IllegalArgumentException.class, () -> DoubleTuple.from(new double[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> DoubleTuple.of(1.0, 2.0).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.from(new float[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.of(1.0f, 2.0f).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> IntTuple.from(new int[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> IntTuple.of(1, 2).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> LongTuple.from(new long[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> LongTuple.of(1L, 2L).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.from(new short[0]).forEach(null));
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.of((short) 1, (short) 2).forEach(null));
        }

        @Test
        void functionalChain_doubleTuple2() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(2.5, 3.5);
            Optional<Double> result = tuple.filter(t -> t._1 < t._2).map(t -> t._1 + t._2);
            assertTrue(result.isPresent());
            assertEquals(6.0, result.get());
        }

        @Test
        void functionalChain_doubleTuple3() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Double result = tuple.toOptional().filter(t -> t._1 > 0 && t._2 > 0 && t._3 > 0).map(t -> (t._1 + t._2 + t._3) / 3.0).orElse(0.0);
            assertEquals(2.0, result);
        }

        @Test
        void functionalChain_floatTuple2() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Optional<Float> result = tuple.filter(t -> t._1 > 0 && t._2 > 0).map(t -> (float) Math.sqrt(t._1 * t._1 + t._2 * t._2));
            assertTrue(result.isPresent());
            assertEquals(5.0f, result.get(), 0.001f);
        }

        @Test
        void functionalChain_floatTuple3() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String result = tuple.toOptional().filter(t -> t._1 + t._2 + t._3 > 5).map(t -> "Average: " + ((t._1 + t._2 + t._3) / 3.0f)).orElse("Not valid");
            assertEquals("Average: 2.0", result);
        }

        @Test
        void functionalChain_intTuple2() throws Exception {
            IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<String> result = tuple.filter(t -> t._1 > 0 && t._2 > 0).map(t -> "Sum: " + (t._1 + t._2));
            assertTrue(result.isPresent());
            assertEquals("Sum: 15", result.get());
        }

        @Test
        void functionalChain_intTuple3() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String result = tuple.toOptional().filter(t -> t._1 + t._2 + t._3 > 5).map(t -> "Product: " + (t._1 * t._2 * t._3)).orElse("Not valid");
            assertEquals("Product: 6", result);
        }

        @Test
        void immutability_doubleTuple() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);

            tuple.map(t -> t._1 * 2);
            tuple.filter(t -> t._1 > 1.0);
            tuple.toOptional();

            assertEquals(1.5, tuple._1, 0.0001);
            assertEquals(2.5, tuple._2, 0.0001);
        }

        @Test
        void immutability_intTuple() {
            IntTuple2 tuple = IntTuple.of(1, 2);

            // Operations should not modify original
            tuple.map(t -> t._1 + 100);
            tuple.filter(t -> t._1 > 0);
            tuple.toOptional();

            // Original values should remain unchanged
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
        }

        @Test
        void immutability_longTuple() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);

            tuple.map(t -> t._1 * 2);
            tuple.filter(t -> t._1 > 50L);
            tuple.toOptional();

            assertEquals(100L, tuple._1);
            assertEquals(200L, tuple._2);
        }

        @Test
        void integration_complexChain() {
            IntTuple3 tuple = IntTuple.of(100, 200, 300);

            Optional<IntTuple3> filtered = tuple.filter(t -> t._1 < t._2 && t._2 < t._3);
            String result = filtered.isPresent() ? "Total: " + (filtered.get()._1 + filtered.get()._2 + filtered.get()._3) : "Invalid sequence";

            assertEquals("Total: 600", result);
        }

        @Test
        void intTuple_consistency() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(2, tuple.arity());

            tuple.accept(t -> {
                assertEquals(1, t._1);
                assertEquals(2, t._2);
            });

            Integer sum = tuple.map(t -> t._1 + t._2);
            assertEquals(3, sum.intValue());

            Optional<IntTuple2> opt = tuple.filter(t -> t._1 > 0);
            assertTrue(opt.isPresent());
        }

        @Test
        void largeDoubleValues() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE);
            Double sum = tuple.map(t -> t._1 + t._2);
            assertEquals(Double.POSITIVE_INFINITY, sum.doubleValue(), 0.0001);
        }

        @Test
        void longMaxValues() {
            LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, 1L);
            // Test without overflow
            Long max = tuple.map(t -> t._1);
            assertEquals(Long.MAX_VALUE, max.longValue());
        }

        @Test
        void longTuple_consistency() {
            LongTuple2 tuple = LongTuple.of(100L, 200L);
            assertEquals(2, tuple.arity());

            tuple.accept(t -> {
                assertEquals(100L, t._1);
                assertEquals(200L, t._2);
            });

            Long sum = tuple.map(t -> t._1 + t._2);
            assertEquals(300L, sum.longValue());

            Optional<LongTuple2> opt = tuple.filter(t -> t._1 < t._2);
            assertTrue(opt.isPresent());
        }

        @Test
        void maxValues() {
            IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
            Long sum = tuple.map(t -> (long) t._1 + (long) t._2);
            assertEquals((long) Integer.MAX_VALUE * 2, sum.longValue());
        }

        @Test
        void minValues() {
            IntTuple2 tuple = IntTuple.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
            Long sum = tuple.map(t -> (long) t._1 + (long) t._2);
            assertEquals((long) Integer.MIN_VALUE * 2, sum.longValue());
        }

        @Test
        void mixedTypes_floatAndDouble() throws Exception {
            FloatTuple2 floatTuple = FloatTuple.of(1.5f, 2.5f);
            DoubleTuple2 doubleTuple = DoubleTuple.of(1.5, 2.5);

            float floatResult = floatTuple.map(t -> t._1 + t._2);
            double doubleResult = doubleTuple.map(t -> t._1 + t._2);

            assertEquals(4.0f, floatResult, 0.001f);
            assertEquals(4.0, doubleResult);
        }

        @Test
        void mixedTypes_intAndLongTuple() throws Exception {
            IntTuple2 intTuple = IntTuple.of(10, 20);
            LongTuple2 longTuple = LongTuple.of(100L, 200L);

            int intSum = intTuple.map(t -> t._1 + t._2);
            long longSum = longTuple.map(t -> t._1 + t._2);

            assertEquals(30, intSum);
            assertEquals(300L, longSum);
        }

        @Test
        void multipleTupleTypesInteraction() {
            // Test that different tuple types work correctly with common interface
            IntTuple.IntTuple2 intTuple = IntTuple.of(10, 20);
            LongTuple.LongTuple2 longTuple = LongTuple.of(100L, 200L);
            DoubleTuple.DoubleTuple2 doubleTuple = DoubleTuple.of(1.5, 2.5);

            assertEquals(2, intTuple.arity());
            assertEquals(2, longTuple.arity());
            assertEquals(2, doubleTuple.arity());

            String intResult = intTuple.map(t -> "int: " + (t._1 + t._2));
            String longResult = longTuple.map(t -> "long: " + (t._1 + t._2));
            String doubleResult = doubleTuple.map(t -> "double: " + (t._1 + t._2));

            assertEquals("int: 30", intResult);
            assertEquals("long: 300", longResult);
            assertEquals("double: 4.0", doubleResult);
        }

        @Test
        void negativeValues() {
            IntTuple2 tuple = IntTuple.of(-5, -10);
            Integer sum = tuple.map(t -> t._1 + t._2);
            assertEquals(-15, sum.intValue());

            Optional<IntTuple2> filtered = tuple.filter(t -> t._1 < 0 && t._2 < 0);
            assertTrue(filtered.isPresent());
        }

        @Test
        void smallDoubleValues() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE);
            Double sum = tuple.map(t -> t._1 + t._2);
            assertNotNull(sum);
        }

        @Test
        void validationMessagesIdentifyTheOperationAndArgument() {
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> BooleanTuple.of(true).forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> ByteTuple.of((byte) 1).forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> CharTuple.of('a').forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> ShortTuple.of((short) 1).forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> IntTuple.of(1).forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> LongTuple.of(1L).forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> FloatTuple.of(1F).forEach(null)).getMessage());
            assertEquals("'action' cannot be null", assertThrows(IllegalArgumentException.class, () -> DoubleTuple.of(1D).forEach(null)).getMessage());

            assertEquals("Cannot compute min() for an empty tuple",
                    assertThrows(NoSuchElementException.class, () -> ByteTuple.from((byte[]) null).min()).getMessage());
            assertEquals("Cannot compute max() for an empty tuple",
                    assertThrows(NoSuchElementException.class, () -> IntTuple.from((int[]) null).max()).getMessage());
            assertEquals("Cannot compute lowerMedian() for an empty tuple",
                    assertThrows(NoSuchElementException.class, () -> DoubleTuple.from((double[]) null).lowerMedian()).getMessage());
        }

        @Test
        void withMixedSignValues() {
            DoubleTuple3 tuple = DoubleTuple.of(-5.0, 0.0, 5.0);

            assertTrue(tuple.filter(t -> t._2 == 0.0).isPresent());
            assertEquals(0.0, tuple.map(t -> t._1 + t._2 + t._3), 0.001);
        }

        @Test
        void withNegativeValues() {
            FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);

            assertTrue(tuple.filter(t -> t._1 < 0 && t._2 < 0 && t._3 < 0).isPresent());
            assertEquals(-6.0f, tuple.map(t -> t._1 + t._2 + t._3), 0.001f);
        }

        @Test
        void withZeroValues() {
            DoubleTuple2 tuple = DoubleTuple.of(0.0, 0.0);

            assertTrue(tuple.filter(t -> t._1 == 0.0).isPresent());
            assertEquals(0.0, tuple.map(t -> t._1 + t._2), 0.001);
        }

        @Test
        void zeroValues() {
            IntTuple2 tuple = IntTuple.of(0, 0);
            Integer sum = tuple.map(t -> t._1 + t._2);
            assertEquals(0, sum.intValue());

            Optional<IntTuple2> filtered = tuple.filter(t -> t._1 == 0);
            assertTrue(filtered.isPresent());
        }
    }

    private static void assertSealedTupleFamily(final Class<?> tupleType, final Class<?>... permittedTypes) {
        assertTrue(tupleType.isSealed(), tupleType.getSimpleName() + " should be sealed");
        assertTrue(Modifier.isAbstract(tupleType.getModifiers()), tupleType.getSimpleName() + " should be abstract");

        final Class<?>[] actualPermittedTypes = tupleType.getPermittedSubclasses();

        assertEquals(permittedTypes.length, actualPermittedTypes.length);
        assertEquals(Set.of(permittedTypes), Set.of(actualPermittedTypes));

        for (final Class<?> permittedType : permittedTypes) {
            assertTrue(tupleType.isAssignableFrom(permittedType));
            assertTrue(Modifier.isFinal(permittedType.getModifiers()), permittedType.getSimpleName() + " should be final");
        }
    }
}
