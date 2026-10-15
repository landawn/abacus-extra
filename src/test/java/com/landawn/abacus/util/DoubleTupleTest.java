package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.landawn.abacus.util.DoubleTuple.DoubleTuple0;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple4;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple5;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple6;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple7;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple8;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.DoubleStream;

class DoubleTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, double[] values) {
            DoubleTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of5Median() {
            // DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            // double median = tuple.lowerMedian();   // 3.0
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(3.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void of6Sum() {
            // DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            // double sum = tuple.sum();   // 21.0
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(21.0, tuple.sum(), 0.001);
        }

        private static DoubleTuple<?> createWithOf(double[] values) {
            return switch (values.length) {
                case 1 -> DoubleTuple.of(values[0]);
                case 2 -> DoubleTuple.of(values[0], values[1]);
                case 3 -> DoubleTuple.of(values[0], values[1], values[2]);
                case 4 -> DoubleTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> DoubleTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: 1.0", new double[] { 1.0 }), Arguments.of("1: 1.5", new double[] { 1.5 }),
                    Arguments.of("1: 3.14", new double[] { 3.14 }), Arguments.of("2: 1.0, 2.0", new double[] { 1.0, 2.0 }),
                    Arguments.of("2: 1.5, 2.5", new double[] { 1.5, 2.5 }), Arguments.of("3: 1.0, 2.0, 3.0", new double[] { 1.0, 2.0, 3.0 }),
                    Arguments.of("3: 1.5, 2.5, 3.5", new double[] { 1.5, 2.5, 3.5 }),
                    Arguments.of("4: 1.0, 2.0, 3.0, 4.0", new double[] { 1.0, 2.0, 3.0, 4.0 }),
                    Arguments.of("4: 1.5, 2.5, 3.5, 4.5", new double[] { 1.5, 2.5, 3.5, 4.5 }),
                    Arguments.of("5: 1.0, 2.0, 3.0, 4.0, 5.0", new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }),
                    Arguments.of("5: 1.5, 2.5, 3.5, 4.5, 5.5", new double[] { 1.5, 2.5, 3.5, 4.5, 5.5 }),
                    Arguments.of("6: 1.0, 2.0, 3.0, 4.0, 5.0, 6.0", new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }),
                    Arguments.of("6: 1.5, 2.5, 3.5, 4.5, 5.5, 6.5", new double[] { 1.5, 2.5, 3.5, 4.5, 5.5, 6.5 }),
                    Arguments.of("7: 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0", new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 }),
                    Arguments.of("7: 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5", new double[] { 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5 }),
                    Arguments.of("8: 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0", new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 }),
                    Arguments.of("8: 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5", new double[] { 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5 }),
                    Arguments.of("9: 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0", new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }),
                    Arguments.of("9: 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5", new double[] { 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5 }));
        }

        private static double[] publicFields(DoubleTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    DoubleTuple.DoubleTuple1 value = (DoubleTuple.DoubleTuple1) tuple;
                    yield new double[] { value._1 };
                }
                case 2 -> {
                    DoubleTuple.DoubleTuple2 value = (DoubleTuple.DoubleTuple2) tuple;
                    yield new double[] { value._1, value._2 };
                }
                case 3 -> {
                    DoubleTuple.DoubleTuple3 value = (DoubleTuple.DoubleTuple3) tuple;
                    yield new double[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    DoubleTuple.DoubleTuple4 value = (DoubleTuple.DoubleTuple4) tuple;
                    yield new double[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    DoubleTuple.DoubleTuple5 value = (DoubleTuple.DoubleTuple5) tuple;
                    yield new double[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    DoubleTuple.DoubleTuple6 value = (DoubleTuple.DoubleTuple6) tuple;
                    yield new double[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    DoubleTuple.DoubleTuple7 value = (DoubleTuple.DoubleTuple7) tuple;
                    yield new double[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    DoubleTuple.DoubleTuple8 value = (DoubleTuple.DoubleTuple8) tuple;
                    yield new double[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    DoubleTuple.DoubleTuple9 value = (DoubleTuple.DoubleTuple9) tuple;
                    yield new double[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
                }
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + tuple.arity());
            };
        }
    }

    @Nested
    class FromTests {

        @Test
        void create_allSizes() {
            for (int i = 0; i <= 9; i++) {
                double[] array = new double[i];
                DoubleTuple<?> tuple = DoubleTuple.from(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        void create_arraySize1() {
            DoubleTuple1 tuple = DoubleTuple.from(new double[] { 1.5 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(1.5, tuple._1, DELTA);
        }

        @Test
        void create_arraySize2() {
            DoubleTuple2 tuple = DoubleTuple.from(new double[] { 1.5, 2.5 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(1.5, tuple._1, DELTA);
            assertEquals(2.5, tuple._2, DELTA);
        }

        @Test
        void create_arraySize9() {
            DoubleTuple9 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(9.0, tuple._9, DELTA);
        }

        @Test
        void create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                DoubleTuple.from(new double[10]);
            });
        }

        @Test
        void create_emptyArray() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_nullArray() {
            DoubleTuple0 tuple = DoubleTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_array1() {
            DoubleTuple1 tuple = DoubleTuple.from(new double[] { 5.5 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(5.5, tuple._1);
        }

        @Test
        void from_array2() {
            DoubleTuple2 tuple = DoubleTuple.from(new double[] { 1.1, 2.2 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(1.1, tuple._1);
            assertEquals(2.2, tuple._2);
        }

        @Test
        void from_array3() {
            DoubleTuple3 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1.0, tuple._1);
            assertEquals(3.0, tuple._3);
        }

        @Test
        void from_array9() {
            DoubleTuple9 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_dispatchesToCorrectArity() {
            for (int len = 0; len <= 9; len++) {
                final double[] src = new double[len];
                for (int i = 0; i < len; i++) {
                    src[i] = i + 1.5;
                }
                final DoubleTuple<?> tuple = DoubleTuple.from(src);
                assertEquals(len, tuple.arity(), "arity mismatch for length " + len);
                assertArrayEquals(src, tuple.toArray(), 0.0, "elements mismatch for length " + len);
            }

            assertThrows(IllegalArgumentException.class, () -> DoubleTuple.from(new double[10]));
        }

        @Test
        void from_emptyArray() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nullArray() {
            DoubleTuple<?> tuple = DoubleTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> DoubleTuple.from(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        void from1() {
            DoubleTuple1 tuple = DoubleTuple.from(new double[] { 1.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from1_values_DoubleTuple_from() {
            DoubleTuple1 tuple = DoubleTuple.from(new double[] { 42.0 });
            assertEquals(42.0, tuple._1, 0.001);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2() {
            DoubleTuple2 tuple = DoubleTuple.from(new double[] { 1.0, 2.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
        }

        @Test
        void from2Through8() {
            DoubleTuple2 tuple2 = DoubleTuple.from(new double[] { 1.0, 2.0 });
            assertEquals(1.0, tuple2._1, 0.001);
            assertEquals(2.0, tuple2._2, 0.001);

            DoubleTuple4 tuple4 = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0 });
            assertEquals(1.0, tuple4._1, 0.001);
            assertEquals(4.0, tuple4._4, 0.001);

            DoubleTuple5 tuple5 = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
            assertEquals(1.0, tuple5._1, 0.001);
            assertEquals(5.0, tuple5._5, 0.001);

            DoubleTuple6 tuple6 = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
            assertEquals(1.0, tuple6._1, 0.001);
            assertEquals(6.0, tuple6._6, 0.001);

            DoubleTuple7 tuple7 = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 });
            assertEquals(1.0, tuple7._1, 0.001);
            assertEquals(7.0, tuple7._7, 0.001);

            DoubleTuple8 tuple8 = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 });
            assertEquals(1.0, tuple8._1, 0.001);
            assertEquals(8.0, tuple8._8, 0.001);
        }

        @Test
        void from3() {
            DoubleTuple3 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(3.0, tuple._3, 0.001);
        }

        @Test
        void from4() {
            DoubleTuple4 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(4.0, tuple._4, 0.001);
        }

        @Test
        void from5() {
            DoubleTuple5 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(5.0, tuple._5, 0.001);
        }

        @Test
        void from6() {
            DoubleTuple6 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(6.0, tuple._6, 0.001);
        }

        @Test
        void from7() {
            DoubleTuple7 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(7.0, tuple._7, 0.001);
        }

        @Test
        void from8() {
            DoubleTuple8 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(8.0, tuple._8, 0.001);
        }

        @Test
        void from9() {
            DoubleTuple9 tuple = DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(9.0, tuple._9, 0.001);
        }

        @Test
        void fromEmpty() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromNull() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                DoubleTuple.from(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
            });
        }
    }

    @Nested
    class ArityTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("arityCases")
        void arity_inputCases_returnsElementCount(String scenario, DoubleTuple<?> tuple, int expected) {
            assertEquals(expected, tuple.arity(), scenario);
        }

        private static java.util.stream.Stream<Arguments> arityCases() {
            return java.util.stream.Stream.of(Arguments.of("from(new double[0])", DoubleTuple.from(new double[0]), 0),
                    Arguments.of("of(1.0)", DoubleTuple.of(1.0), 1), Arguments.of("of(1.0, 2.0)", DoubleTuple.of(1.0, 2.0), 2),
                    Arguments.of("of(1.0, 2.0, 3.0)", DoubleTuple.of(1.0, 2.0, 3.0), 3),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0), 4),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0), 5),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), 6),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), 7),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), 8),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0), 9));
        }

    }

    @Nested
    class MinTests {

        @Test
        void arity1_min() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.min());
        }

        @Test
        void baseMinMaxMedianAverage_emptyAndNaN() {
            // NaN should propagate through the base min/max scan.
            final DoubleTuple.DoubleTuple4 tupleWithNaN = DoubleTuple.of(1.0, Double.NaN, 2.0, 3.0);
            assertTrue(Double.isNaN(tupleWithNaN.min()));
            assertTrue(Double.isNaN(tupleWithNaN.max()));

            // Without NaN, results are still correct.
            final DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(4.5, 1.5, 3.5, 2.5);
            assertEquals(1.5, tuple.min(), 0.0);
            assertEquals(4.5, tuple.max(), 0.0);

            // Empty min/max/median must throw NoSuchElementException; average() returns an empty OptionalDouble.
            final DoubleTuple.DoubleTuple0 empty = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, empty::min);
            assertThrows(NoSuchElementException.class, empty::max);
            assertThrows(NoSuchElementException.class, empty::median);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void min_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::min);
        }

        @Test
        void min_returnsMinimum() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.5, 1.5, 2.5);
            assertEquals(1.5, tuple.min(), DELTA);
        }

        @Test
        void min_returnsMinimum_values_3_0_1_0_2_0() {
            // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            // double min = tuple.min();   // 1.0
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min(), 0.001);

            // DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(2.5, 1.5);
            // double minPair = pair.min();   // 1.5
            DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(2.5, 1.5);
            assertEquals(1.5, pair.min(), 0.001);
        }

        @Test
        void min_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertEquals(5.5, tuple.min());
        }

        @Test
        void min_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min());
        }

        @Test
        void min_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(10.5, -3.2, 0.0, 7.1, -10.0);
            assertEquals(-10.0, tuple.min());
        }

        @Test
        void minMax_nanPropagation_arity4to9() {
            final double n = Double.NaN;

            assertTrue(Double.isNaN(DoubleTuple.of(1d, n, 2d, 3d).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, n, 2d, 3d).max()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, 4d, n).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, 4d, n).max()));
            assertTrue(Double.isNaN(DoubleTuple.of(n, 2d, 3d, 4d, 5d, 6d).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(n, 2d, 3d, 4d, 5d, 6d).max()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, n, 5d, 6d, 7d).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, n, 5d, 6d, 7d).max()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, n).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, n).max()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, n).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, n).max()));

            // Without NaN, results are still correct.
            assertEquals(1d, DoubleTuple.of(4d, 1d, 3d, 2d).min(), 0d);
            assertEquals(4d, DoubleTuple.of(4d, 1d, 3d, 2d).max(), 0d);
            assertEquals(1d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d).min(), 0d);
            assertEquals(5d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d).max(), 0d);
            assertEquals(1d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d).min(), 0d);
            assertEquals(6d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d).max(), 0d);
            assertEquals(1d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d, 7d).min(), 0d);
            assertEquals(7d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d, 7d).max(), 0d);
            assertEquals(1d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d, 7d, 8d).min(), 0d);
            assertEquals(8d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d, 7d, 8d).max(), 0d);
            assertEquals(1d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d, 7d, 8d, 9d).min(), 0d);
            assertEquals(9d, DoubleTuple.of(4d, 1d, 3d, 2d, 5d, 6d, 7d, 8d, 9d).max(), 0d);
        }

        @Test
        void minTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.min(), 0.001);
        }

        @Test
        void minTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.min(), 0.001);
        }

        @Test
        void minTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min(), 0.001);
        }

        @Test
        void minWithNegativeValues() {
            DoubleTuple3 tuple = DoubleTuple.of(-5.0, -1.0, -10.0);
            assertEquals(-10.0, tuple.min(), 0.001);
        }

        @Test
        void nanTupleAlsoBreaksTheMinMedianMaxRelation() {
            // min()/max() propagate NaN, so the min <= median <= max relation fails for ANY tuple
            // containing NaN -- the (+Inf,-Inf) case is not unique in that respect
            final DoubleTuple.DoubleTuple3 t = DoubleTuple.of(1.0, Double.NaN, 3.0);
            assertTrue(Double.isNaN(t.min()));
            assertTrue(Double.isNaN(t.max()));
            assertEquals(3.0, t.median(), "median orders NaN last, so it is not selected here");
            assertFalse(t.min() <= t.median());
        }

        @Test
        void tuple0_min_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void tuple1_min() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.min(), DELTA);
        }

        @Test
        void tuple2_min() {
            DoubleTuple2 tuple = DoubleTuple.of(2.5, 1.5);
            assertEquals(1.5, tuple.min(), DELTA);
        }

        @Test
        void tuple3_min() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min(), DELTA);
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity1_max() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.max());
        }

        @Test
        void max_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::max);
        }

        @Test
        void max_returnsMaximum() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.5, 1.5, 2.5);
            assertEquals(3.5, tuple.max(), DELTA);
        }

        @Test
        void max_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertEquals(5.5, tuple.max());
        }

        @Test
        void max_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max());
        }

        @Test
        void max_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(10.5, -3.2, 0.0, 7.1, -10.0);
            assertEquals(10.5, tuple.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.max(), 0.001);
        }

        @Test
        void maxTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.max(), 0.001);
        }

        @Test
        void maxTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max(), 0.001);
        }

        @Test
        void maxTuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(7.0, tuple.max(), 0.001);
        }

        @Test
        void tuple0_max_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void tuple1_max() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.max(), DELTA);
        }

        @Test
        void tuple2_max() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2.5, tuple.max(), DELTA);
        }

        @Test
        void tuple3_max() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max(), DELTA);
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity1_sum() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.sum());
        }

        @Test
        void sum_compensationOverflowDoesNotCorruptAFiniteTotal() {
            // Kahan's compensation term (t - sum) - y overflows here, which used to turn a finite total
            // into -Infinity / +Infinity / NaN depending on element order
            final double expected = -3.0E307 + Double.MAX_VALUE;
            assertTrue(Double.isFinite(expected));
            assertEquals(expected, DoubleTuple.of(-3.0E307, Double.MAX_VALUE).sum());
            assertEquals(-expected, DoubleTuple.of(3.0E307, -Double.MAX_VALUE).sum());
            assertEquals(DoubleTuple.of(Double.MAX_VALUE, -3.0E307).sum(), DoubleTuple.of(-3.0E307, Double.MAX_VALUE).sum());
            assertEquals(expected, DoubleTuple.of(-3.0E307, Double.MAX_VALUE, 1.0).sum());

            for (int n = 2; n <= 9; n++) {
                final double[] a = new double[n];
                a[0] = -3.0E307;
                a[1] = Double.MAX_VALUE;
                assertEquals(expected, DoubleTuple.from(a).sum(), "arity " + n);
            }
        }

        @Test
        void sum_emptyAndNaN() {
            assertEquals(0.0, DoubleTuple.from(new double[0]).sum());
            assertTrue(Double.isNaN(DoubleTuple.of(1.0, Double.NaN).sum()));
            assertTrue(Double.isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).sum()));
        }

        @Test
        void sum_finiteExactTotalIsNeverReportedAsNonFinite() {
            final java.util.Random rnd = new java.util.Random(20260922L);

            for (int iter = 0; iter < 20000; iter++) {
                final int n = 2 + rnd.nextInt(8);
                final double[] a = new double[n];
                java.math.BigDecimal exact = java.math.BigDecimal.ZERO;

                for (int i = 0; i < n; i++) {
                    a[i] = (rnd.nextBoolean() ? 1 : -1) * Double.MAX_VALUE * rnd.nextDouble();
                    exact = exact.add(new java.math.BigDecimal(a[i]));
                }

                final double sum = DoubleTuple.from(a).sum();

                if (Double.isFinite(exact.doubleValue())) {
                    assertTrue(Double.isFinite(sum), () -> java.util.Arrays.toString(a) + " -> " + sum);
                } else {
                    assertEquals(exact.doubleValue(), sum, () -> java.util.Arrays.toString(a));
                }
            }
        }

        @Test
        void sum_isCompensatedNotLeftToRight() {
            // the compensated sum is the more accurate of the two
            assertEquals(3.0, DoubleTuple.of(1.0, 1.0, 1.0).sum());
            final double a = Double.parseDouble("1e16"), b = 1.0, c = 1.0;
            assertEquals(1.0E16 + 2.0, DoubleTuple.of(a, b, c).sum());
            assertEquals(1.0E16, a + b + c, "plain left-to-right addition loses both units");
        }

        @Test
        void sum_losesSignOfZeroBeyondArityOne() {
            // arity 1 returns the field directly and keeps -0.0
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(DoubleTuple.of(-0.0).sum()));
            // arity >= 2 goes through the compensated accumulator, which starts at +0.0
            assertEquals(Double.doubleToRawLongBits(0.0), Double.doubleToRawLongBits(DoubleTuple.of(-0.0, -0.0).sum()));
            // ... whereas plain IEEE-754 addition would have kept it
            final double negZero = Double.parseDouble("-0.0");
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(negZero + negZero));
        }

        @Test
        void sum_overflowAndNonFiniteElementsAreUnchangedByTheFallback() {
            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).sum());
            assertEquals(Double.NEGATIVE_INFINITY, DoubleTuple.of(-Double.MAX_VALUE, -Double.MAX_VALUE).sum());
            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0).sum());
            assertTrue(Double.isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).sum()));
            assertTrue(Double.isNaN(DoubleTuple.of(-3.0E307, Double.MAX_VALUE, Double.NaN).sum()));
            assertEquals(0.0, DoubleTuple.of(-0.0, -0.0).sum());
            assertEquals(-0.0, DoubleTuple.of(-0.0).sum());
        }

        @Test
        void sum_returnsTotal() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.0);
            assertEquals(7.0, tuple.sum(), DELTA);
        }

        @Test
        void sum_tuple0() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            assertEquals(0.0, tuple.sum());
        }

        @Test
        void sum_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.sum(), 0.001);
        }

        @Test
        void sum_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum());
        }

        @Test
        void sum_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(15.0, tuple.sum());
        }

        @Test
        void sumTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertEquals(0.0, tuple.sum(), 0.001);
        }

        @Test
        void sumTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.sum(), 0.001);
        }

        @Test
        void sumTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.sum(), 0.001);
        }

        @Test
        void sumTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum(), 0.001);
        }

        @Test
        void sumTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(10.0, tuple.sum(), 0.001);
        }

        @Test
        void sumTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(45.0, tuple.sum(), 0.001);
        }

        @Test
        void tuple0_sum() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertEquals(0.0, tuple.sum(), DELTA);
        }

        @Test
        void tuple1_sum() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.sum(), DELTA);
        }

        @Test
        void tuple2_sum() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(4.0, tuple.sum(), DELTA);
        }

        @Test
        void tuple3_sum() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum(), DELTA);
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity1_average() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void average_distinguishesNegativeZeroInputsFromNegativeUnderflow() {
            for (int len = 2; len <= 9; len++) {
                final double[] negativeZeros = new double[len];
                java.util.Arrays.fill(negativeZeros, -0.0d);
                assertEquals(Double.doubleToRawLongBits(0.0d), Double.doubleToRawLongBits(DoubleTuple.from(negativeZeros).average().getAsDouble()),
                        "zero inputs for arity " + len);

                final double[] negativeSubnormal = new double[len];
                negativeSubnormal[0] = -Double.MIN_VALUE;
                assertEquals(Double.doubleToRawLongBits(-0.0d), Double.doubleToRawLongBits(DoubleTuple.from(negativeSubnormal).average().getAsDouble()),
                        "underflow for arity " + len);
            }
        }

        @Test
        void average_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void average_isOverflowSafeUnlikeSum() {
            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).sum());
            assertEquals(Double.MAX_VALUE, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).average().getAsDouble());
        }

        @Test
        void average_losesSignOfZeroBeyondArityOne() {
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(DoubleTuple.of(-0.0).average().getAsDouble()));
            assertEquals(Double.doubleToRawLongBits(0.0), Double.doubleToRawLongBits(DoubleTuple.of(-0.0, -0.0).average().getAsDouble()));
        }

        @Test
        void average_negativeUnderflowSignFollowsTheComputedTotal() {
            assertEquals(-0.0, DoubleTuple.of(-Double.MIN_VALUE, 0.0).average().getAsDouble());
            assertEquals(-0.0, DoubleTuple.of(1.0, -1.0, -Double.MIN_VALUE).average().getAsDouble());
            // the true mean is negative and underflows, but the compensated total itself rounds to +0.0
            assertEquals(0.0, DoubleTuple.of(-1.0, -Double.MIN_VALUE, 1.0).sum());
            assertEquals(0.0, DoubleTuple.of(-1.0, -Double.MIN_VALUE, 1.0).average().getAsDouble());
        }

        @Test
        void average_returnsMean() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void average_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.0);
            assertEquals(5.0, tuple.average().getAsDouble());
        }

        @Test
        void average_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average().getAsDouble());
        }

        @Test
        void average_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(2.0, 4.0, 6.0, 8.0, 10.0);
            assertEquals(6.0, tuple.average().getAsDouble());
        }

        @Test
        void averageDoesNotOverflowForFiniteValues() {
            for (int len = 2; len <= 9; len++) {
                final double[] positive = new double[len];
                final double[] negative = new double[len];

                for (int i = 0; i < len; i++) {
                    positive[i] = Double.MAX_VALUE;
                    negative[i] = -Double.MAX_VALUE;
                }

                assertEquals(Double.MAX_VALUE, DoubleTuple.from(positive).average().getAsDouble(), "positive average for arity " + len);
                assertEquals(-Double.MAX_VALUE, DoubleTuple.from(negative).average().getAsDouble(), "negative average for arity " + len);
            }

            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(Double.POSITIVE_INFINITY, 1d).average().getAsDouble());
            assertEquals(Double.NEGATIVE_INFINITY, DoubleTuple.of(Double.NEGATIVE_INFINITY, -1d).average().getAsDouble());
            assertTrue(Double.isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).average().getAsDouble()));

            final double midpoint = DoubleTuple
                    .of(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, 2d * Double.MIN_VALUE, Double.MIN_VALUE)
                    .average()
                    .getAsDouble();
            assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(midpoint));
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            assertEquals(15.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple0_average_returnsEmpty() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void tuple1_average() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void tuple2_average() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2.0, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void tuple3_average() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average().getAsDouble(), DELTA);
        }
    }

    @Nested
    class MedianTests {

        @Test
        void arity1_median() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void median_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::median);
        }

        @Test
        void median_evenCount() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, tuple.lowerMedian(), DELTA); // Lower middle value
        }

        @Test
        void median_keepsSignOfZeroWhereAverageDoesNot() {
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(DoubleTuple.of(-0.0, -0.0).median()));
        }

        @Test
        void median_ofOppositeInfinitiesIsNaN() {
            // notable because min() and max() are themselves NOT NaN here -- unlike a tuple that
            // actually contains NaN, where min()/max() propagate it (see the test below)
            final DoubleTuple.DoubleTuple2 t = DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
            assertEquals(Double.NEGATIVE_INFINITY, t.min());
            assertEquals(Double.POSITIVE_INFINITY, t.max());
            assertTrue(Double.isNaN(t.median()));
            assertEquals(Double.NEGATIVE_INFINITY, t.lowerMedian());

            assertTrue(Double
                    .isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).median()));
        }

        @Test
        void median_ofSameSignInfinitiesIsThatInfinity() {
            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).median());
            assertEquals(Double.NEGATIVE_INFINITY, DoubleTuple.of(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).median());
        }

        @Test
        void median_returnsMedian() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(2.0, tuple.lowerMedian(), DELTA);

            DoubleTuple.DoubleTuple4 evenTuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, evenTuple.lowerMedian(), DELTA);
        }

        @Test
        void median_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertEquals(5.5, tuple.lowerMedian());
        }

        @Test
        void median_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
            assertEquals(20.0, tuple.lowerMedian());
        }

        @Test
        void median_tuple4_even() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, tuple.lowerMedian());
        }

        @Test
        void median_usesTotalOrderingSoNaNSortsLast() {
            // min()/max() propagate NaN, but median() orders by Double.compare, where NaN is largest
            assertEquals(3.0, DoubleTuple.of(1.0, Double.NaN, 3.0).median());
            assertTrue(Double.isNaN(DoubleTuple.of(1.0, Double.NaN, 3.0).min()));
            assertTrue(Double.isNaN(DoubleTuple.of(1.0, Double.NaN, 3.0).max()));
        }

        @Test
        void medianDoesNotOverflowOrUnderflow() {
            assertEquals(Double.MIN_VALUE, N.median(Double.MIN_VALUE, Double.MIN_VALUE));
            assertEquals(Double.MIN_VALUE, DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE).median());
            assertEquals(Double.MAX_VALUE, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).median());
            assertEquals(Double.MAX_VALUE, DoubleTuple.of(0d, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE).median());

            assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(DoubleTuple.of(Double.MIN_VALUE, 0d).average().getAsDouble()));
            assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(-Double.MIN_VALUE, 0d).average().getAsDouble()));
            assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(DoubleTuple.of(Double.MIN_VALUE, 0d).median()));
            assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(-Double.MIN_VALUE, 0d).median()));
            assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(-0d, -0d).median()));
            assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(DoubleTuple.of(-0d, 0d).median()));
        }

        @Test
        void medianDoesNotReorderTuple() {
            final DoubleTuple4 tuple = DoubleTuple.of(4d, 1d, 3d, 2d);
            final int hashCode = tuple.hashCode();

            assertEquals(2.5d, tuple.median());
            assertArrayEquals(new double[] { 4d, 1d, 3d, 2d }, tuple.toArray());
            assertEquals(hashCode, tuple.hashCode());
            assertEquals(DoubleTuple.of(4d, 1d, 3d, 2d), tuple);
        }

        @Test
        void medianTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void medianTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void medianTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(2.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void medianTuple3_values_30_0_10_0_20_0() {
            DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
            assertEquals(20.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void medianTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void medianTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(5.0, 1.0, 3.0, 2.0, 4.0);
            assertEquals(3.0, tuple.lowerMedian(), 0.001);
        }

        @Test
        void pairMedian_matchesDocumentedFormulaIncludingSubnormals() {
            // the @return documents (_1 + _2) / 2d, which (unlike _1/2d + _2/2d) survives subnormals
            assertEquals(Double.MIN_VALUE, DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE).median());
            assertEquals(Double.doubleToRawLongBits(-Double.MIN_VALUE),
                    Double.doubleToRawLongBits(DoubleTuple.of(-Double.MIN_VALUE, -Double.MIN_VALUE).median()));
            // ... and the documented overflow fallback still applies
            assertEquals(Double.MAX_VALUE, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).median());
        }

        @Test
        void pairMedian_nanPropagates() {
            // at arity 2 the median IS the mean, so NaN propagates (unlike lowerMedian)
            assertTrue(Double.isNaN(DoubleTuple.of(3.0, Double.NaN).median()));
            assertEquals(3.0, DoubleTuple.of(3.0, Double.NaN).lowerMedian());
        }

        @Test
        void tuple0_median_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void tuple1_median() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.lowerMedian(), DELTA);
        }

        @Test
        void tuple2_median() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(1.5, tuple.lowerMedian(), DELTA);
        }

        @Test
        void tuple3_median() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(2.0, tuple.lowerMedian(), DELTA);
        }
    }

    @Nested
    class LowerMedianTests {

        @Test
        void lowerMedian_evenArityDocExamplesDifferFromMedian() {
            assertEquals(2.0, DoubleTuple.of(1.0, 2.0, 3.0, 4.0).lowerMedian());
            assertEquals(2.5, DoubleTuple.of(1.0, 2.0, 3.0, 4.0).median());
            assertEquals(3.0, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).lowerMedian());
            assertEquals(3.5, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).median());
            assertEquals(4.0, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0).lowerMedian());
            assertEquals(4.5, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0).median());
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity1_reversed() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleTuple.DoubleTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity2_reversed() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            DoubleTuple.DoubleTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity3_reversed() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity4_reversed() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple.DoubleTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple.DoubleTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple.DoubleTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple.DoubleTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple.DoubleTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple.DoubleTuple9 reversed = tuple.reversed();
            assertEquals(9.0, reversed._1, 0.0);
            assertEquals(8.0, reversed._2, 0.0);
            assertEquals(7.0, reversed._3, 0.0);
            assertEquals(6.0, reversed._4, 0.0);
            assertEquals(5.0, reversed._5, 0.0);
            assertEquals(4.0, reversed._6, 0.0);
            assertEquals(3.0, reversed._7, 0.0);
            assertEquals(2.0, reversed._8, 0.0);
            assertEquals(1.0, reversed._9, 0.0);
        }

        @Test
        void reversed_reversesElementOrder() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 reversed = tuple.reversed();

            assertEquals(3.0, reversed._1, DELTA);
            assertEquals(2.0, reversed._2, DELTA);
            assertEquals(1.0, reversed._3, DELTA);
        }

        @Test
        void reversed_tuple0() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            DoubleTuple<?> reversed = tuple.reversed();
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversed_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            DoubleTuple1 reversed = tuple.reversed();
            assertEquals(5.5, reversed._1);
        }

        @Test
        void reversed_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reversed();
            assertEquals(3.0, reversed._1);
            assertEquals(2.0, reversed._2);
            assertEquals(1.0, reversed._3);
        }

        @Test
        void reversed_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple5 reversed = tuple.reversed();
            assertEquals(5.0, reversed._1);
            assertEquals(1.0, reversed._5);
        }

        @Test
        void reversedTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            DoubleTuple<DoubleTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversedTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleTuple1 reversed = tuple.reversed();
            assertEquals(1.0, reversed._1, 0.001);
        }

        @Test
        void reversedTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            DoubleTuple1 reversed = tuple.reversed();
            assertEquals(42.0, reversed._1, 0.001);
        }

        @Test
        void reversedTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 reversed = tuple.reversed();
            assertEquals(2.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._2, 0.001);
        }

        @Test
        void reversedTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reversed();
            assertEquals(3.0, reversed._1, 0.001);
            assertEquals(2.0, reversed._2, 0.001);
            assertEquals(1.0, reversed._3, 0.001);
        }

        @Test
        void reversedTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple4 reversed = tuple.reversed();
            assertEquals(4.0, reversed._1, 0.001);
            assertEquals(3.0, reversed._2, 0.001);
            assertEquals(2.0, reversed._3, 0.001);
            assertEquals(1.0, reversed._4, 0.001);
        }

        @Test
        void reversedTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple5 reversed = tuple.reversed();
            assertEquals(5.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._5, 0.001);
        }

        @Test
        void reversedTuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple6 reversed = tuple.reversed();
            assertEquals(6.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._6, 0.001);
        }

        @Test
        void reversedTuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple7 reversed = tuple.reversed();
            assertEquals(7.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._7, 0.001);
        }

        @Test
        void reversedTuple8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple8 reversed = tuple.reversed();
            assertEquals(8.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._8, 0.001);
        }

        @Test
        void reversedTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple9 reversed = tuple.reversed();
            assertEquals(9.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._9, 0.001);
        }

        @Test
        void tuple0_reversed() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            DoubleTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void tuple1_reversed() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            DoubleTuple1 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(3.14, reversed._1, DELTA);
            assertNotSame(tuple, reversed);
        }

        @Test
        void tuple2_reversed() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            DoubleTuple2 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(2.5, reversed._1, DELTA);
            assertEquals(1.5, reversed._2, DELTA);
        }

        @Test
        void tuple3_reversed() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(3.0, reversed._1, DELTA);
            assertEquals(2.0, reversed._2, DELTA);
            assertEquals(1.0, reversed._3, DELTA);
        }
    }

    @Nested
    class ContainsTests {

        @ParameterizedTest(name = "arity {0}: contains checks every component")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void contains_checksEveryComponentAndRejectsMissingValue(int arity) {
            double[] values = new double[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = i + 1;
            }
            DoubleTuple<?> tuple = DoubleTuple.from(values);
            for (double value : values) {
                assertTrue(tuple.contains(value), "missing component " + value);
            }
            assertFalse(tuple.contains(100));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("containsCases")
        void contains_inputCases_matchExpectedMembership(String scenario, DoubleTuple<?> tuple, double[] present, double[] absent) {
            for (double value : present) {
                assertTrue(tuple.contains(value), scenario + ": expected to contain " + value);
            }
            for (double value : absent) {
                assertFalse(tuple.contains(value), scenario + ": expected to exclude " + value);
            }
        }

        @ParameterizedTest(name = "arity {0}: special values at every position")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void contains_specialValues_matchesNaNAndDistinguishesSignedZero(int arity) {
            double[] specialValues = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, -0.0 };
            for (double special : specialValues) {
                for (int position = 0; position < arity; position++) {
                    double[] values = new double[arity];
                    java.util.Arrays.fill(values, 42.0);
                    values[position] = special;
                    DoubleTuple<?> tuple = DoubleTuple.from(values);
                    String scenario = "component " + position + ": " + special;
                    assertTrue(tuple.contains(special), scenario);
                    if (Double.isNaN(special)) {
                        assertTrue(tuple.contains(Double.longBitsToDouble(0x7ff8000000000001L)), scenario + ": alternate NaN payload");
                    } else if (special == 0.0) {
                        assertFalse(tuple.contains(-special), scenario + ": opposite signed zero");
                    }
                }
            }
        }

        private static java.util.stream.Stream<Arguments> containsCases() {
            return java.util.stream.Stream.of(Arguments.of("from(new double[0])", DoubleTuple.from(new double[0]), new double[] {}, new double[] { 1.0 }),
                    Arguments.of("of(1.0)", DoubleTuple.of(1.0), new double[] { 1.0 }, new double[] { 99.0 }),
                    Arguments.of("of(3.14)", DoubleTuple.of(3.14), new double[] { 3.14 }, new double[] { 2.71 }),
                    Arguments.of("of(42.0)", DoubleTuple.of(42.0), new double[] { 42.0 }, new double[] { 41.0 }),
                    Arguments.of("of(5.5)", DoubleTuple.of(5.5), new double[] { 5.5 }, new double[] { 3.3 }),
                    Arguments.of("of(1.0, 2.0)", DoubleTuple.of(1.0, 2.0), new double[] { 1.0 }, new double[] {}),
                    Arguments.of("of(1.5, 2.5)", DoubleTuple.of(1.5, 2.5), new double[] { 1.5, 2.5 }, new double[] { 3.5 }),
                    Arguments.of("of(1.0, 2.0, 3.0)", DoubleTuple.of(1.0, 2.0, 3.0), new double[] { 1.0, 2.0, 3.0 }, new double[] { 99.0, 4.0 }),
                    Arguments.of("of(1.5, 2.5, 3.5)", DoubleTuple.of(1.5, 2.5, 3.5), new double[] { 1.5, 2.5, 3.5 }, new double[] { 4.5 }),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0), new double[] { 1.0, 4.0 }, new double[] { 99.0 }),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0), new double[] { 1.0, 5.0 }, new double[] { 99.0 }),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), new double[] { 1.0, 6.0 },
                            new double[] { 99.0 }),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), new double[] { 1.0, 7.0 },
                            new double[] { 99.0 }),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5)", DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5), new double[] { 7.5 },
                            new double[] { 9.5 }),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0),
                            new double[] { 1.0, 8.0 }, new double[] { 99.0 }),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5)", DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5), new double[] { 8.5 },
                            new double[] { 9.5 }),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0),
                            new double[] { 1.0, 9.0, 5.0 }, new double[] { 99.0, 10.0 }));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_independence() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array1 = tuple.toArray();
            double[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
            assertArrayEquals(array1, array2);
        }

        @Test
        void toArray_independence_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            array[0] = 99.9;
            assertEquals(1.0, tuple._1, DELTA); // Tuple should be unaffected
        }

        @Test
        void toArray_preservesElements() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);
            double[] array = tuple.toArray();

            assertEquals(3, array.length);
            assertEquals(1.5, array[0], DELTA);
            assertEquals(2.5, array[1], DELTA);
            assertEquals(3.5, array[2], DELTA);
        }

        @Test
        void toArray_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array);
        }

        @Test
        void toArrayImmutable() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array1 = tuple.toArray();
            double[] array2 = tuple.toArray();
            array1[0] = 999.0;
            assertEquals(1.0, array2[0], 0.001);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            array[0] = 999.0;
            assertEquals(1.0, tuple._1, 0.001);
        }

        @Test
        void toArrayTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            double[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArrayTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0 }, array, 0.001);
        }

        @Test
        void toArrayTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 42.0 }, array, 0.001);
        }

        @Test
        void toArrayTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, 0.001);
        }

        @Test
        void toArrayTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            double[] array = tuple.toArray();
            assertEquals(9, array.length);
            assertEquals(1.0, array[0], 0.001);
            assertEquals(9.0, array[8], 0.001);
        }

        @Test
        void tuple0_toArray() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            double[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        void tuple1_toArray() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 3.14 }, array, DELTA);
        }

        @Test
        void tuple2_toArray() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.5, 2.5 }, array, DELTA);
        }

        @Test
        void tuple3_toArray() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, DELTA);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_independence() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            list.set(0, 99.9);
            assertEquals(1.0, tuple._1, DELTA); // Tuple should be unaffected
        }

        @Test
        void toList_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(5.5, list.get(0));
        }

        @Test
        void toList_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(1.0, list.get(0));
            assertEquals(3.0, list.get(2));
        }

        @Test
        void toListTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            DoubleList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(1.0, list.get(0), 0.001);
        }

        @Test
        void toListTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            DoubleList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42.0, list.get(0), 0.001);
        }

        @Test
        void toListTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0, list.get(0), 0.001);
            assertEquals(2.0, list.get(1), 0.001);
            assertEquals(3.0, list.get(2), 0.001);
        }

        @Test
        void toListTuple3_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0, list.get(0), 0.001);
            assertEquals(3.0, list.get(2), 0.001);
        }

        @Test
        void toListTuple4Through9() {
            DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(4.0, list4.get(3), 0.001);

            DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9.0, list9.get(8), 0.001);
        }

        @Test
        void tuple0_toList() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        void tuple1_toList() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(3.14, list.get(0), DELTA);
        }

        @Test
        void tuple2_toList() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity3_forEach() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity4_forEach() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(5.5, collected.get(0));
        }

        @Test
        void forEach_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0, collected.get(0));
            assertEquals(3.0, collected.get(2));
        }

        @Test
        void forEach_withException() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(d -> {
                    if (d == 2.0) {
                        throw new RuntimeException("test exception");
                    }
                });
            });
        }

        @Test
        void forEachTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple0_values_DoubleTuple_from() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(0, collected.size());
        }

        @Test
        void forEachTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
        }

        @Test
        void forEachTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(42.0, collected.get(0), 0.001);
        }

        @Test
        void forEachTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(10.0, collected.get(0), 0.001);
            assertEquals(20.0, collected.get(1), 0.001);
        }

        @Test
        void forEachTuple2Override() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Double.valueOf(10.0), result.get(0));
            assertEquals(Double.valueOf(20.0), result.get(1));
        }

        @Test
        void forEachTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
            assertEquals(Double.valueOf(2.0), result.get(1));
            assertEquals(Double.valueOf(3.0), result.get(2));
        }

        @Test
        void forEachTuple3_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0, collected.get(0), 0.001);
            assertEquals(2.0, collected.get(1), 0.001);
            assertEquals(3.0, collected.get(2), 0.001);
        }

        @Test
        void forEachTuple3Override() {
            DoubleTuple3 tuple = DoubleTuple.of(10.0, 20.0, 30.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(30.0), result.get(2));
        }

        @Test
        void forEachTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Double.valueOf(4.0), result.get(3));
        }

        @Test
        void tuple0_forEach() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        void tuple1_forEach() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(3.14, collected.get(0), DELTA);
        }

        @Test
        void tuple2_forEach() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(1.5, collected.get(0), DELTA);
            assertEquals(2.5, collected.get(1), DELTA);
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_operations() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum, DELTA);
        }

        @Test
        void stream_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum);
        }

        @Test
        void stream_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            long count = tuple.stream().count();
            assertEquals(5, count);
        }

        @Test
        void stream_visitsElements() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();

            assertEquals(6.0, sum, DELTA);
        }

        @Test
        void streamTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            DoubleStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleStream stream = tuple.stream();
            assertEquals(1.0, stream.sum(), 0.001);
        }

        @Test
        void streamTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            DoubleStream stream = tuple.stream();
            assertEquals(1, stream.count());
        }

        @Test
        void streamTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleStream stream = tuple.stream();
            assertEquals(6.0, stream.sum(), 0.001);
        }

        @Test
        void streamTuple3_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum, 0.001);
        }

        @Test
        void streamTuple4Through9() {
            DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(10.0, tuple4.stream().sum(), 0.001);

            DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(45.0, tuple9.stream().sum(), 0.001);
        }

        @Test
        void streamTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            double max = tuple.stream().max().getAsDouble();
            assertEquals(5.0, max, 0.001);
        }

        @Test
        void tuple0_stream() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        void tuple1_stream() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            double sum = tuple.stream().sum();
            assertEquals(3.14, sum, DELTA);
        }

        @Test
        void tuple2_stream() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            long count = tuple.stream().count();
            assertEquals(2, count);
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_invokesAction() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<DoubleTuple3> results = new ArrayList<>();
            tuple.accept(results::add);
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void acceptConsumer() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
            assertEquals(Double.valueOf(2.0), result.get(1));
            assertEquals(Double.valueOf(3.0), result.get(2));
        }

        @Test
        void arity2_accept_biConsumer() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept_triConsumer() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2_accept() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            List<Double> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals(3.0, results.get(0));
            assertEquals(4.0, results.get(1));
        }

        @Test
        void tuple2_accept_values_1_5_2_5() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            List<Double> collected = new ArrayList<>();
            tuple.accept((a, b) -> {
                collected.add(a);
                collected.add(b);
            });
            assertEquals(2, collected.size());
            assertEquals(1.5, collected.get(0), DELTA);
            assertEquals(2.5, collected.get(1), DELTA);
        }

        @Test
        void tuple2Accept() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            List<Double> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Double.valueOf(3.0), result.get(0));
            assertEquals(Double.valueOf(4.0), result.get(1));
        }

        @Test
        void tuple2Accept_values_3_0_4_0() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            final List<Double> values = new ArrayList<>();
            tuple.accept((a, b) -> {
                values.add(a);
                values.add(b);
            });
            assertEquals(2, values.size());
            assertEquals(3.0, values.get(0), 0.001);
            assertEquals(4.0, values.get(1), 0.001);
        }

        @Test
        void tuple3_accept() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals(1.0, results.get(0));
            assertEquals(3.0, results.get(2));
        }

        @Test
        void tuple3_accept_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> collected = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                collected.add(a);
                collected.add(b);
                collected.add(c);
            });
            assertEquals(3, collected.size());
        }

        @Test
        void tuple3Accept() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
            assertEquals(Double.valueOf(2.0), result.get(1));
            assertEquals(Double.valueOf(3.0), result.get(2));
        }

        @Test
        void tuple3Accept_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            final List<Double> values = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                values.add(a);
                values.add(b);
                values.add(c);
            });
            assertEquals(3, values.size());
            assertEquals(1.0, values.get(0), 0.001);
            assertEquals(2.0, values.get(1), 0.001);
            assertEquals(3.0, values.get(2), 0.001);
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map_biFunction() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map_triFunction() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void map_returnsMappedValue() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
            assertEquals("Sum: 6.0", result);
        }

        @Test
        void map2() {
            // DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            // double product = tuple.map((a, b) -> a * b);   // 12.0
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            double product = tuple.map((a, b) -> a * b);
            assertEquals(12.0, product, 0.001);

            // DoubleTuple.DoubleTuple2 point = DoubleTuple.of(3.0, 4.0);
            // Double distance = point.map((x, y) -> Math.sqrt(x * x + y * y));   // 5.0
            DoubleTuple.DoubleTuple2 point = DoubleTuple.of(3.0, 4.0);
            Double distance = point.map((x, y) -> Math.sqrt(x * x + y * y));
            assertEquals(5.0, distance, 0.001);
        }

        @Test
        void map3() {
            // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            // double product = tuple.map((a, b, c) -> a * b * c);   // 6.0
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double productVal = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0, productVal, 0.001);

            // DoubleTuple.DoubleTuple3 point = DoubleTuple.of(1.0, 2.0, 2.0);
            // Double distance = point.map((x, y, z) -> Math.sqrt(x*x + y*y + z*z));   // 3.0
            DoubleTuple.DoubleTuple3 point = DoubleTuple.of(1.0, 2.0, 2.0);
            Double dist = point.map((x, y, z) -> Math.sqrt(x * x + y * y + z * z));
            assertEquals(3.0, dist, 0.001);
        }

        @Test
        void tuple2_map() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            double product = tuple.map((a, b) -> a * b);
            assertEquals(12.0, product);
        }

        @Test
        void tuple2_map_values_1_5_2_5() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            double result = tuple.map((a, b) -> a + b);
            assertEquals(4.0, result, DELTA);
        }

        @Test
        void tuple2Map() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            double result = tuple.map((a, b) -> a * b);
            assertEquals(12.0, result, 0.001);
        }

        @Test
        void tuple3_map() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double product = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0, product);
        }

        @Test
        void tuple3_map_values_1_0_2_0_3_0() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(6.0, result, DELTA);
        }

        @Test
        void tuple3Map() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0, result, 0.001);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filter_appliesPredicate() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter(t -> t._1 > 0);
            assertTrue(result.isPresent());

            Optional<DoubleTuple3> result2 = tuple.filter(t -> t._1 < 0);
            assertFalse(result2.isPresent());
        }

        @Test
        void filter2() {
            // DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            // result = tuple.filter((a, b) -> a + b > 5);
            // Returns: Optional containing tuple (since 3.0 + 4.0 = 7.0 > 5)
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            Optional<DoubleTuple.DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());

            // DoubleTuple.DoubleTuple2 small = DoubleTuple.of(1.0, 2.0);
            // empty = small.filter((a, b) -> a + b > 10);
            // Returns: Optional.empty()
            DoubleTuple.DoubleTuple2 small = DoubleTuple.of(1.0, 2.0);
            Optional<DoubleTuple.DoubleTuple2> empty = small.filter((a, b) -> a + b > 10);
            assertFalse(empty.isPresent());
        }

        @Test
        void filter3() {
            // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            // result = tuple.filter((a, b, c) -> a + b + c > 5);
            // Returns: Optional containing tuple
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple.DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());

            // DoubleTuple.DoubleTuple3 small = DoubleTuple.of(1.0, 1.0, 1.0);
            // empty = small.filter((a, b, c) -> a + b + c > 10);
            // Returns: Optional.empty()
            DoubleTuple.DoubleTuple3 small = DoubleTuple.of(1.0, 1.0, 1.0);
            Optional<DoubleTuple.DoubleTuple3> empty = small.filter((a, b, c) -> a + b + c > 10);
            assertFalse(empty.isPresent());
        }

        @Test
        void filterPredicate() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter(t -> t._1 == 1.0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter(t -> t._1 == 99.0);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            Optional<DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());

            Optional<DoubleTuple2> result2 = tuple.filter((a, b) -> a + b < 5);
            assertFalse(result2.isPresent());
        }

        @Test
        void tuple2_filter_match() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            Optional<DoubleTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple2_filter_noMatch() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            Optional<DoubleTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2Filter() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            assertTrue(tuple.filter((a, b) -> a + b > 5).isPresent());
            assertFalse(tuple.filter((a, b) -> a + b > 10).isPresent());
        }

        @Test
        void tuple2FilterFalse() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            var result = tuple.filter((a, b) -> a + b > 10.0);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            var result = tuple.filter((a, b) -> a + b > 5.0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3_filter() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());

            Optional<DoubleTuple3> result2 = tuple.filter((a, b, c) -> a + b + c < 5);
            assertFalse(result2.isPresent());
        }

        @Test
        void tuple3_filter_match() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        void tuple3_filter_noMatch() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3Filter() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.filter((a, b, c) -> a + b + c > 5).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a + b + c > 10).isPresent());
        }

        @Test
        void tuple3FilterFalse() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter((a, b, c) -> a + b + c > 10.0);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter((a, b, c) -> a + b + c > 5.0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple.DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            DoubleTuple.DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            DoubleTuple.DoubleTuple4 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple.DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            DoubleTuple.DoubleTuple5 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple.DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            DoubleTuple.DoubleTuple6 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple.DoubleTuple6 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            DoubleTuple.DoubleTuple7 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple.DoubleTuple7 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            DoubleTuple.DoubleTuple8 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple.DoubleTuple8 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            DoubleTuple.DoubleTuple9 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple.DoubleTuple9 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void doubleSemantics_hashCode_naN_and_signedZero() {
            // NaN: equal -> equal hash.
            assertEquals(DoubleTuple.of(Double.NaN).hashCode(), DoubleTuple.of(Double.NaN).hashCode());
            assertEquals(DoubleTuple.of(1d, Double.NaN).hashCode(), DoubleTuple.of(1d, Double.NaN).hashCode());
            assertEquals(DoubleTuple.of(Double.NaN, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d).hashCode(),
                    DoubleTuple.of(Double.NaN, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d).hashCode());

            // +0.0 vs -0.0: not equal; bit-pattern hash differs.
            assertNotEquals(DoubleTuple.of(0.0).hashCode(), DoubleTuple.of(-0.0).hashCode());
            assertNotEquals(DoubleTuple.of(1d, 0.0).hashCode(), DoubleTuple.of(1d, -0.0).hashCode());
            assertNotEquals(DoubleTuple.of(0.0, 1d, 2d).hashCode(), DoubleTuple.of(-0.0, 1d, 2d).hashCode());
        }

        @Test
        void hashCode_consistency() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_different() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 4.0);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_matchesValueContract() {
            DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
        }

        @Test
        void hashCodeTuple0() {
            DoubleTuple<DoubleTuple0> tuple1 = DoubleTuple.from(new double[0]);
            DoubleTuple<DoubleTuple0> tuple2 = DoubleTuple.from(new double[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1_values_42_0() {
            DoubleTuple1 tuple1 = DoubleTuple.of(42.0);
            DoubleTuple1 tuple2 = DoubleTuple.of(42.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple2() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple1_hashCode() {
            DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
            DoubleTuple1 tuple2 = DoubleTuple.of(3.14);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple2_hashCode() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.5, 2.5);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.5, 2.5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple3_hashCode() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @ParameterizedTest(name = "arity {0}: equality value contract")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void equals_allArities_obeysValueContract(int arity) {
            double[] values = new double[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = i + 1;
            }
            DoubleTuple<?> tuple = DoubleTuple.from(values);
            // A separately constructed empty value verifies equality independently of the factory singleton.
            DoubleTuple<?> equal = arity == 0 ? new DoubleTuple.DoubleTuple0() : DoubleTuple.from(values.clone());
            assertNotSame(tuple, equal);
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(equal));
            assertTrue(equal.equals(tuple));
            DoubleTuple<?> third = arity == 0 ? new DoubleTuple.DoubleTuple0() : DoubleTuple.from(values.clone());
            assertNotSame(equal, third);
            assertTrue(equal.equals(third));
            assertTrue(tuple.equals(third), "equality must be transitive");
            assertEquals(tuple.hashCode(), equal.hashCode());
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals(new Object()));

            for (int i = 0; i < arity; i++) {
                double[] changed = values.clone();
                changed[i] = 100;
                DoubleTuple<?> different = DoubleTuple.from(changed);
                assertFalse(tuple.equals(different), "component " + i);
                assertFalse(different.equals(tuple), "component " + i);
            }

            for (int otherArity = 0; otherArity <= 9; otherArity++) {
                if (otherArity != arity) {
                    DoubleTuple<?> differentArity = DoubleTuple.from(java.util.Arrays.copyOf(values, otherArity));
                    assertFalse(tuple.equals(differentArity), "different arity " + otherArity);
                    assertFalse(differentArity.equals(tuple), "reverse comparison with arity " + otherArity);
                }
            }
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("equalityCases")
        void equals_inputCases_comparesValuesAndTypes(String scenario, DoubleTuple<?> tuple, Object other, boolean expected) {
            assertTrue(tuple.equals(tuple), scenario + ": reflexivity");
            assertEquals(expected, tuple.equals(other), scenario + ": forward comparison");
            if (other != null) {
                assertEquals(expected, other.equals(tuple), scenario + ": reverse comparison");
            }
            if (expected) {
                assertEquals(tuple.hashCode(), other.hashCode(), scenario + ": equal values must have equal hashes");
            }
        }

        @ParameterizedTest(name = "arity {0}: special values at every position")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void equals_specialValues_preservesFloatingPointValueSemantics(int arity) {
            double[] specialValues = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, -0.0 };
            for (double special : specialValues) {
                for (int position = 0; position < arity; position++) {
                    double[] values = new double[arity];
                    java.util.Arrays.fill(values, 42.0);
                    values[position] = special;
                    DoubleTuple<?> tuple = DoubleTuple.from(values);
                    String scenario = "component " + position + ": " + special;
                    double[] matchingValues = values.clone();
                    if (Double.isNaN(special)) {
                        matchingValues[position] = Double.longBitsToDouble(0x7ff8000000000001L);
                    }
                    DoubleTuple<?> equal = DoubleTuple.from(matchingValues);
                    assertTrue(tuple.equals(equal), scenario);
                    assertTrue(equal.equals(tuple), scenario + ": reverse comparison");
                    assertEquals(tuple.hashCode(), equal.hashCode(), scenario);
                    matchingValues[position] = special == 0.0 ? -special : 0.0;
                    DoubleTuple<?> different = DoubleTuple.from(matchingValues);
                    assertFalse(tuple.equals(different), scenario);
                    assertFalse(different.equals(tuple), scenario + ": reverse comparison");
                }
            }
        }

        @Test
        void arity9_equalsAndHashCode_differInOneField() {
            double[] base = { 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0 };
            DoubleTuple.DoubleTuple9 t = DoubleTuple.of(base[0], base[1], base[2], base[3], base[4], base[5], base[6], base[7], base[8]);
            for (int i = 0; i < 9; i++) {
                double[] diff = base.clone();
                diff[i] = diff[i] + 1.0;
                DoubleTuple.DoubleTuple9 other = DoubleTuple.of(diff[0], diff[1], diff[2], diff[3], diff[4], diff[5], diff[6], diff[7], diff[8]);
                assertNotEquals(t, other, "field _" + (i + 1) + " should affect equals");
                assertNotEquals(t.hashCode(), other.hashCode(), "field _" + (i + 1) + " should affect hashCode");
            }
        }

        private static java.util.stream.Stream<Arguments> equalityCases() {
            return java.util.stream.Stream.of(
                    Arguments.of("from(new double[0]) versus from(new double[0])", DoubleTuple.from(new double[0]), DoubleTuple.from(new double[0]), true),
                    Arguments.of("of(1.0) versus of(1.0, 2.0)", DoubleTuple.of(1.0), DoubleTuple.of(1.0, 2.0), false),
                    Arguments.of("of(1.0) versus of(1.0)", DoubleTuple.of(1.0), DoubleTuple.of(1.0), true),
                    Arguments.of("of(1.0) versus of(99.0)", DoubleTuple.of(1.0), DoubleTuple.of(99.0), false),
                    Arguments.of("of(3.14) versus \"not a tuple\"", DoubleTuple.of(3.14), "not a tuple", false),
                    Arguments.of("of(3.14) versus of(2.71)", DoubleTuple.of(3.14), DoubleTuple.of(2.71), false),
                    Arguments.of("of(3.14) versus of(3.14)", DoubleTuple.of(3.14), DoubleTuple.of(3.14), true),
                    Arguments.of("of(3.14) versus null", DoubleTuple.of(3.14), null, false),
                    Arguments.of("of(42.0) versus of(42.0)", DoubleTuple.of(42.0), DoubleTuple.of(42.0), true),
                    Arguments.of("of(5.5) versus of(5.5, 1.0)", DoubleTuple.of(5.5), DoubleTuple.of(5.5, 1.0), false),
                    Arguments.of("of(5.5) versus of(5.5)", DoubleTuple.of(5.5), DoubleTuple.of(5.5), true),
                    Arguments.of("of(5.5) versus null", DoubleTuple.of(5.5), null, false),
                    Arguments.of("of(1.0, 2.0) versus of(1.0, 2.0, 3.0)", DoubleTuple.of(1.0, 2.0), DoubleTuple.of(1.0, 2.0, 3.0), false),
                    Arguments.of("of(1.0, 2.0) versus of(1.0, 2.0)", DoubleTuple.of(1.0, 2.0), DoubleTuple.of(1.0, 2.0), true),
                    Arguments.of("of(1.0, 2.0) versus of(1.0, 3.0)", DoubleTuple.of(1.0, 2.0), DoubleTuple.of(1.0, 3.0), false),
                    Arguments.of("of(1.0, 2.0) versus of(2.0, 3.0)", DoubleTuple.of(1.0, 2.0), DoubleTuple.of(2.0, 3.0), false),
                    Arguments.of("of(1.0, 2.0) versus null", DoubleTuple.of(1.0, 2.0), null, false),
                    Arguments.of("of(1.5, 2.5) versus of(1.5, 2.5)", DoubleTuple.of(1.5, 2.5), DoubleTuple.of(1.5, 2.5), true),
                    Arguments.of("of(1.5, 2.5) versus of(2.5, 1.5)", DoubleTuple.of(1.5, 2.5), DoubleTuple.of(2.5, 1.5), false),
                    Arguments.of("of(1.5, 2.5) versus null", DoubleTuple.of(1.5, 2.5), null, false),
                    Arguments.of("of(1.0, 2.0, 3.0) versus \"not a tuple\"", DoubleTuple.of(1.0, 2.0, 3.0), "not a tuple", false),
                    Arguments.of("of(1.0, 2.0, 3.0) versus of(1.0, 2.0, 3.0)", DoubleTuple.of(1.0, 2.0, 3.0), DoubleTuple.of(1.0, 2.0, 3.0), true),
                    Arguments.of("of(1.0, 2.0, 3.0) versus of(1.0, 2.0, 4.0)", DoubleTuple.of(1.0, 2.0, 3.0), DoubleTuple.of(1.0, 2.0, 4.0), false),
                    Arguments.of("of(1.0, 2.0, 3.0) versus null", DoubleTuple.of(1.0, 2.0, 3.0), null, false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0) versus \"tuple4\"", DoubleTuple.of(1.0, 2.0, 3.0, 4.0), "tuple4", false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0) versus of(1.0, 2.0, 3.0, 4.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0),
                            true),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0) versus of(1.0, 2.0, 3.0, 9.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0), DoubleTuple.of(1.0, 2.0, 3.0, 9.0),
                            false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0) versus \"tuple5\"", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0), "tuple5", false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0),
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0), true),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0) versus of(1.0, 2.0, 3.0, 4.0, 9.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0),
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 9.0), false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0) versus \"tuple6\"", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), "tuple6", false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), true),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 9.0)", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 9.0), false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0) versus \"tuple7\"", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), "tuple7", false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), true),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 9.0)",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 9.0), false),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5) versus \"tuple\"", DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5), "tuple", false),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5) versus of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5)",
                            DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5), DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5), true),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5) versus of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 8.5)",
                            DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5), DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 8.5), false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0) versus \"tuple8\"", DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0),
                            "tuple8", false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), true),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 9.0)",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 9.0), false),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5) versus \"tuple\"", DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5), "tuple",
                            false),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5) versus of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5)",
                            DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5), DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5), true),
                    Arguments.of("of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5) versus of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 9.5)",
                            DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5), DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 9.5), false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0) versus \"tuple9\"",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0), "tuple9", false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 0.0)",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 0.0), false),
                    Arguments.of("of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0) versus of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)",
                            DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0), DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0), true));
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity1_toString() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity3_toString() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.from(new double[0]);
            String str = tuple.toString();
            assertEquals("()", str);
        }

        @Test
        void toString_formatsValue() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            String result = tuple.toString();
            assertTrue(result.contains("1.5"));
            assertTrue(result.contains("2.5"));
        }

        @Test
        void toString_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("5.5"));
        }

        @Test
        void toString_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        @Test
        void toStringTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.from(new double[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
        }

        @Test
        void toStringTuple1_values_42_0() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals("(42.0)", tuple.toString());
        }

        @Test
        void toStringTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals("(1.0, 2.0)", tuple.toString());
        }

        @Test
        void toStringTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        @Test
        void tuple0_toString() {
            DoubleTuple0 tuple = DoubleTuple.from(new double[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple1_toString() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals("(3.14)", tuple.toString());
        }

        @Test
        void tuple2_toString() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals("(1.5, 2.5)", tuple.toString());
        }

        @Test
        void tuple3_toString() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void baseMethods_inheritedImplementation() {
            final DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(4.5, 1.5, 3.5, 2.5);
            final double[] copy = tuple.toArray();
            copy[0] = 99.5;

            assertEquals(1.5, tuple.min(), DELTA);
            assertEquals(4.5, tuple.max(), DELTA);
            assertEquals(2.5, tuple.lowerMedian(), DELTA);
            assertEquals(12.0, tuple.sum(), DELTA);
            assertEquals(3.0, tuple.average().getAsDouble(), DELTA);
            assertTrue(tuple.contains(3.5));
            assertFalse(tuple.contains(8.5));
            assertEquals(4.5, tuple.toArray()[0], DELTA);
            assertTrue(tuple.equals(DoubleTuple.of(4.5, 1.5, 3.5, 2.5)));
            assertFalse(tuple.equals(DoubleTuple.of(4.5, 1.5, 3.5, 9.5)));
            assertFalse(tuple.equals(null));
            assertEquals("(4.5, 1.5, 3.5, 2.5)", tuple.toString());
        }

        @Test
        void classLevelExamples() {
            // double min = triple.min();   // 1.0
            // double max = triple.max();   // 3.0
            // OptionalDouble avg = triple.average();   // OptionalDouble.of(2.0)
            DoubleTuple.DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(1.0, triple.min(), 0.001);
            assertEquals(3.0, triple.max(), 0.001);
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        @ParameterizedTest(name = "arity {0}: default exports are detached")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void defaultConstructors_exportDefaultValuesInIndependentArrays(int arity) {
            DoubleTuple<?> tuple = switch (arity) {
                case 1 -> new DoubleTuple.DoubleTuple1();
                case 2 -> new DoubleTuple.DoubleTuple2();
                case 3 -> new DoubleTuple.DoubleTuple3();
                case 4 -> new DoubleTuple.DoubleTuple4();
                case 5 -> new DoubleTuple.DoubleTuple5();
                case 6 -> new DoubleTuple.DoubleTuple6();
                case 7 -> new DoubleTuple.DoubleTuple7();
                case 8 -> new DoubleTuple.DoubleTuple8();
                case 9 -> new DoubleTuple.DoubleTuple9();
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + arity);
            };
            double[] expected = new double[arity];
            double[] first = tuple.toArray();
            double[] second = tuple.toArray();

            assertEquals(arity, tuple.arity());
            assertArrayEquals(expected, first);
            assertArrayEquals(expected, second);
            assertNotSame(first, second);
            first[0] = 1;
            assertArrayEquals(expected, second, "each export must have independent storage");
            assertArrayEquals(expected, tuple.toArray(), "mutating an export must not affect the tuple");
        }

        @Test
        void emptyTuple_operations_obeyEmptyValueContract() {
            DoubleTuple<?> empty = DoubleTuple.from((double[]) null);
            assertEquals(DoubleTuple.from(new double[0]), empty);
            assertFalse(empty.equals("double"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void defaultConstructors_zeroInitialization() {
            final DoubleTuple1 tuple1 = new DoubleTuple1();
            final DoubleTuple2 tuple2 = new DoubleTuple2();
            final DoubleTuple3 tuple3 = new DoubleTuple3();
            final DoubleTuple4 tuple4 = new DoubleTuple4();
            final DoubleTuple5 tuple5 = new DoubleTuple5();
            final DoubleTuple6 tuple6 = new DoubleTuple6();
            final DoubleTuple7 tuple7 = new DoubleTuple7();
            final DoubleTuple8 tuple8 = new DoubleTuple8();
            final DoubleTuple9 tuple9 = new DoubleTuple9();

            assertArrayEquals(new double[] { 0.0 }, tuple1.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0 }, tuple1.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0 }, tuple2.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0 }, tuple2.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0 }, tuple3.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0 }, tuple3.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0 }, tuple4.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0 }, tuple4.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple5.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple5.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple6.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple6.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple7.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple7.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple8.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple8.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple9.toArray(), DELTA);
            assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tuple9.toArray(), DELTA);
        }

        @Test
        void doubleSemantics_aggregatesWithSpecialValues() {
            // Sum: +INF + (-INF) = NaN (IEEE-754).
            assertTrue(Double.isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).sum()));
            // Sum with NaN propagates.
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, Double.NaN).sum()));
            // Average with NaN propagates.
            assertTrue(Double.isNaN(DoubleTuple.of(1d, 2d, Double.NaN).average().getAsDouble()));

            // Min/max with -0.0 vs +0.0: Math.min/max treats -0.0 as strictly less than +0.0.
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(DoubleTuple.of(0.0, -0.0).min()));
            assertEquals(Double.doubleToRawLongBits(+0.0), Double.doubleToRawLongBits(DoubleTuple.of(0.0, -0.0).max()));
            // Same for higher arities (Tuple4-9 use Math.min/max chains).
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(DoubleTuple.of(1d, 0.0, 2d, -0.0, 3d, 4d, 5d, 6d, 7d).min()));
            assertEquals(Double.doubleToRawLongBits(+0.0), Double.doubleToRawLongBits(DoubleTuple.of(0.0, -0.0).max()));

            // Median for 2 elements uses Double.compare ordering, where NaN sorts last.
            assertFalse(Double.isNaN(DoubleTuple.of(1d, Double.NaN).lowerMedian()));
        }

        @Test
        void doubleSemantics_naN_and_signedZero() {
            // ---- contains() must treat NaN as equal to NaN across all arities. ----
            assertTrue(DoubleTuple.of(Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, 3d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, 3d, 4d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, Double.NaN).contains(Double.NaN));
            assertTrue(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, Double.NaN).contains(Double.NaN));

            // ---- contains() must NOT treat +0.0 and -0.0 as equal. ----
            assertFalse(DoubleTuple.of(0.0).contains(-0.0));
            assertFalse(DoubleTuple.of(-0.0).contains(0.0));
            assertFalse(DoubleTuple.of(1d, 0.0).contains(-0.0));
            assertFalse(DoubleTuple.of(1d, 2d, 0.0).contains(-0.0));
            assertFalse(DoubleTuple.of(1d, 2d, 3d, 0.0).contains(-0.0));
            assertFalse(DoubleTuple.of(1d, 2d, 3d, 4d, 0.0).contains(-0.0));
            // Same value matches with sign preserved.
            assertTrue(DoubleTuple.of(0.0).contains(0.0));
            assertTrue(DoubleTuple.of(-0.0).contains(-0.0));

            // ---- equals() must treat NaN as equal to NaN. ----
            assertEquals(DoubleTuple.of(Double.NaN), DoubleTuple.of(Double.NaN));
            assertEquals(DoubleTuple.of(1d, Double.NaN), DoubleTuple.of(1d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, Double.NaN), DoubleTuple.of(1d, 2d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, 3d, Double.NaN), DoubleTuple.of(1d, 2d, 3d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, 3d, 4d, Double.NaN), DoubleTuple.of(1d, 2d, 3d, 4d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, Double.NaN), DoubleTuple.of(1d, 2d, 3d, 4d, 5d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, Double.NaN), DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, Double.NaN), DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, Double.NaN));
            assertEquals(DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, Double.NaN), DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, Double.NaN));

            // ---- equals() must NOT treat +0.0 and -0.0 as equal. ----
            assertNotEquals(DoubleTuple.of(0.0), DoubleTuple.of(-0.0));
            assertNotEquals(DoubleTuple.of(0.0, 1d), DoubleTuple.of(-0.0, 1d));
            assertNotEquals(DoubleTuple.of(1d, 0.0, 2d), DoubleTuple.of(1d, -0.0, 2d));
            assertNotEquals(DoubleTuple.of(1d, 2d, 0.0, 3d), DoubleTuple.of(1d, 2d, -0.0, 3d));
            assertNotEquals(DoubleTuple.of(1d, 2d, 3d, 0.0, 4d), DoubleTuple.of(1d, 2d, 3d, -0.0, 4d));
        }

        @Test
        void extremeValues() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MAX_VALUE);
            assertEquals(Double.MIN_VALUE, tuple.min(), DELTA);
            assertEquals(Double.MAX_VALUE, tuple.max(), DELTA);
        }

        @Test
        void fractionalValues() {
            DoubleTuple3 tuple = DoubleTuple.of(0.1, 0.2, 0.3);
            assertEquals(0.6, tuple.sum(), DELTA);
            assertEquals(0.2, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void highArity_aggregatesAndExports_preserveValues() {
            final DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            final DoubleTuple5 tuple5 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            final DoubleTuple6 tuple6 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            final DoubleTuple7 tuple7 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            final DoubleTuple8 tuple8 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            final DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

            assertTrue(tuple4.contains(2.0));
            assertTrue(tuple4.contains(3.0));
            assertTrue(tuple5.contains(4.0));
            assertTrue(tuple6.contains(5.0));
            assertTrue(tuple7.contains(6.0));
            assertTrue(tuple8.contains(7.0));
            assertTrue(tuple9.contains(8.0));
            assertFalse(tuple9.contains(10.0));

            assertTrue(tuple4.equals(tuple4));
            assertFalse(tuple4.equals("not a tuple"));
            assertFalse(tuple4.equals(DoubleTuple.of(1.0, 2.0, 3.0, 9.0)));
            assertTrue(tuple5.equals(tuple5));
            assertFalse(tuple5.equals("not a tuple"));
            assertFalse(tuple5.equals(DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 9.0)));
            assertTrue(tuple6.equals(tuple6));
            assertFalse(tuple6.equals("not a tuple"));
            assertFalse(tuple6.equals(DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 9.0)));
            assertTrue(tuple7.equals(tuple7));
            assertFalse(tuple7.equals("not a tuple"));
            assertFalse(tuple7.equals(DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 9.0)));
            assertTrue(tuple8.equals(tuple8));
            assertFalse(tuple8.equals("not a tuple"));
            assertFalse(tuple8.equals(DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 9.0)));
            assertTrue(tuple9.equals(tuple9));
            assertFalse(tuple9.equals("not a tuple"));
            assertFalse(tuple9.equals(DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 10.0)));
        }

        @Test
        void mixedValues() {
            DoubleTuple5 tuple = DoubleTuple.of(-2.0, 0.0, 5.0, -1.0, 3.0);
            assertEquals(-2.0, tuple.min(), 0.001);
            assertEquals(5.0, tuple.max(), 0.001);
            assertEquals(5.0, tuple.sum(), 0.001);
        }

        @Test
        void negativeValues() {
            DoubleTuple3 tuple = DoubleTuple.of(-1.0, -2.0, -3.0);
            assertEquals(-3.0, tuple.min(), 0.001);
            assertEquals(-1.0, tuple.max(), 0.001);
            assertEquals(-6.0, tuple.sum(), 0.001);
        }

        @Test
        void negativeValues_values_minus1_0_0_0_1_0() {
            DoubleTuple3 tuple = DoubleTuple.of(-1.0, 0.0, 1.0);
            assertEquals(-1.0, tuple.min(), DELTA);
            assertEquals(1.0, tuple.max(), DELTA);
            assertEquals(0.0, tuple.lowerMedian(), DELTA);
            assertEquals(0.0, tuple.sum(), DELTA);
        }

        @Test
        void reverse() {
            // DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            // DoubleTuple.DoubleTuple2 reversed = tuple.reversed();   // (4.0, 3.0)
            DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(3.0, 4.0);
            DoubleTuple.DoubleTuple2 reversed2 = tuple2.reversed();
            assertEquals(4.0, reversed2._1, 0.001);
            assertEquals(3.0, reversed2._2, 0.001);

            // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            // DoubleTuple.DoubleTuple3 reversed = tuple.reversed();   // (3.0, 2.0, 1.0)
            DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 reversed3 = tuple3.reversed();
            assertEquals(3.0, reversed3._1, 0.001);
            assertEquals(2.0, reversed3._2, 0.001);
            assertEquals(1.0, reversed3._3, 0.001);
        }

        @Test
        void reverse_identityContract() {
            // reversed() returns a new object for non-empty tuples; the empty tuple returns itself
            DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
            assertNotSame(pair, pair.reversed());

            DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotSame(triple, triple.reversed());

            DoubleTuple0 empty = DoubleTuple.from(new double[0]);
            assertSame(empty, empty.reversed());
        }

        @Test
        void specialValues_infinity() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            assertEquals(Double.NEGATIVE_INFINITY, tuple.min(), DELTA);
            assertEquals(Double.POSITIVE_INFINITY, tuple.max(), DELTA);
        }

        @Test
        void specialValues_naN() {
            DoubleTuple1 tuple = DoubleTuple.of(Double.NaN);
            assertTrue(Double.isNaN(tuple._1));
        }

        @Test
        void tuple4_basic() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(4, tuple.arity());
            assertEquals(1.0, tuple.min(), DELTA);
            assertEquals(4.0, tuple.max(), DELTA);
            assertEquals(10.0, tuple.sum(), DELTA);
            assertEquals(2.5, tuple.average().getAsDouble(), DELTA);
            assertTrue(tuple.contains(3.0));
            DoubleTuple4 reversed = tuple.reversed();
            assertEquals(4.0, reversed._1, DELTA);
            assertEquals(1.0, reversed._4, DELTA);
        }

        @Test
        void tuple4Operations() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);

            // Test reverse
            DoubleTuple4 reversed = tuple.reversed();
            assertEquals(4.0, reversed._1, 0.001);
            assertEquals(3.0, reversed._2, 0.001);
            assertEquals(2.0, reversed._3, 0.001);
            assertEquals(1.0, reversed._4, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(4.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, tuple.toArray(), 0.001);

            // Test min/max/median/sum/average via base class
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(4.0, tuple.max(), 0.001);
            assertEquals(2.0, tuple.lowerMedian(), 0.001); // For even-sized tuples, returns lower middle value
            assertEquals(10.0, tuple.sum(), 0.001);
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple4 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0, 5.0);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("4.0"));
        }

        @Test
        void tuple5_basic() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(5, tuple.arity());
            assertEquals(1.0, tuple.min(), DELTA);
            assertEquals(5.0, tuple.max(), DELTA);
            assertEquals(3.0, tuple.lowerMedian(), DELTA);
            assertEquals(15.0, tuple.sum(), DELTA);
            assertTrue(tuple.contains(3.0));
        }

        @Test
        void tuple5Operations() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);

            // Test reverse
            DoubleTuple5 reversed = tuple.reversed();
            assertEquals(5.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._5, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(5.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(5.0, tuple.max(), 0.001);
            assertEquals(3.0, tuple.lowerMedian(), 0.001);
            assertEquals(15.0, tuple.sum(), 0.001);
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);

            // Test equals
            DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(tuple, tuple2);
        }

        @Test
        void tuple6_basic() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(6, tuple.arity());
            assertEquals(1.0, tuple.min(), DELTA);
            assertEquals(6.0, tuple.max(), DELTA);
            assertEquals(21.0, tuple.sum(), DELTA);
            assertEquals(3.5, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void tuple6Operations() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

            // Test reverse
            DoubleTuple6 reversed = tuple.reversed();
            assertEquals(6.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._6, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(6.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(6.0, tuple.max(), 0.001);
            assertEquals(3.0, tuple.lowerMedian(), 0.001); // For even-sized tuples, returns lower middle value
            assertEquals(21.0, tuple.sum(), 0.001);
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple7_basic() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(7, tuple.arity());
            assertEquals(4.0, tuple.lowerMedian(), DELTA);
            assertEquals(7, tuple.toArray().length);
        }

        @Test
        void tuple7Operations() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

            // Test reverse
            DoubleTuple7 reversed = tuple.reversed();
            assertEquals(7.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._7, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(7.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(7.0, tuple.max(), 0.001);
            assertEquals(28.0, tuple.sum(), 0.001);
            assertEquals(4.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple8_basic() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
            assertEquals(36.0, tuple.sum(), DELTA);
        }

        @Test
        void tuple8Operations() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);

            // Test reverse
            DoubleTuple8 reversed = tuple.reversed();
            assertEquals(8.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._8, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(8.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(8.0, tuple.max(), 0.001);
            assertEquals(4.0, tuple.lowerMedian(), 0.001); // For even-sized tuples, returns lower middle value
            assertEquals(36.0, tuple.sum(), 0.001);
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9_basic() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
            assertEquals(45.0, tuple.sum(), DELTA);
            assertEquals(5.0, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void tuple9Operations() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

            // Test reverse
            DoubleTuple9 reversed = tuple.reversed();
            assertEquals(9.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._9, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(9.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(9.0, tuple.max(), 0.001);
            assertEquals(45.0, tuple.sum(), 0.001);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void verySmallDifferences() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0000001, 1.0000002);
            assertEquals(1.0000001, tuple.min(), 0.00000001);
            assertEquals(1.0000002, tuple.max(), 0.00000001);
        }

        @Test
        void zero() {
            DoubleTuple3 tuple = DoubleTuple.of(0.0, 0.0, 0.0);
            assertEquals(0.0, tuple.min(), DELTA);
            assertEquals(0.0, tuple.max(), DELTA);
            assertEquals(0.0, tuple.lowerMedian(), DELTA);
            assertEquals(0.0, tuple.sum(), DELTA);
            assertEquals(0.0, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void zeroValues() {
            DoubleTuple3 tuple = DoubleTuple.of(0.0, 0.0, 0.0);
            assertEquals(0.0, tuple.min(), 0.001);
            assertEquals(0.0, tuple.max(), 0.001);
            assertEquals(0.0, tuple.sum(), 0.001);
        }

        @Test
        void highArity_aggregatesAndMembership_preserveValues() {
            DoubleTuple.DoubleTuple4 tuple4 = DoubleTuple.of(9d, 1d, 7d, 3d);
            assertEquals(1d, tuple4.min(), 0.0);
            assertEquals(9d, tuple4.max(), 0.0);
            assertEquals(3d, tuple4.lowerMedian(), 0.0);
            assertEquals(20d, tuple4.sum(), 0.0);
            assertEquals(5.0, tuple4.average().getAsDouble(), 0.0);
            assertTrue(tuple4.contains(3d));

            DoubleTuple.DoubleTuple5 tuple5 = DoubleTuple.of(1d, 2d, 3d, 4d, 5d);
            assertTrue(tuple5.contains(5d));

            DoubleTuple.DoubleTuple6 tuple6 = DoubleTuple.of(9d, 1d, 7d, 3d, 5d, 11d);
            assertEquals(5d, tuple6.lowerMedian(), 0.0);
            assertTrue(tuple6.contains(11d));

            DoubleTuple.DoubleTuple7 tuple7 = DoubleTuple.of(1d, 2d, 3d, 4d, 5d, 6d, 7d);
            assertTrue(tuple7.contains(7d));

            DoubleTuple.DoubleTuple8 tuple8 = DoubleTuple.of(9d, 1d, 7d, 3d, 5d, 11d, 13d, 15d);
            assertEquals(7d, tuple8.lowerMedian(), 0.0);
            assertTrue(tuple8.contains(15d));

            DoubleTuple.DoubleTuple9 tuple9 = DoubleTuple.of(9d, 1d, 7d, 3d, 5d, 11d, 13d, 15d, 17d);
            assertEquals(9d, tuple9.lowerMedian(), 0.0);
            assertTrue(tuple9.contains(17d));
        }
    }

    private static final double DELTA = 0.0001;
}
