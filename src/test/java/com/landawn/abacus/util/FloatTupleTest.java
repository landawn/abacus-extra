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

import com.landawn.abacus.util.FloatTuple.FloatTuple0;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple4;
import com.landawn.abacus.util.FloatTuple.FloatTuple5;
import com.landawn.abacus.util.FloatTuple.FloatTuple6;
import com.landawn.abacus.util.FloatTuple.FloatTuple7;
import com.landawn.abacus.util.FloatTuple.FloatTuple8;
import com.landawn.abacus.util.FloatTuple.FloatTuple9;
import com.landawn.abacus.util.stream.FloatStream;
import com.landawn.abacus.util.u.Optional;

class FloatTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, float[] values) {
            FloatTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of5Median() {
            // FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            // float median = tuple.lowerMedian();   // 3.0f
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(3.0f, tuple.lowerMedian(), 0.001f);
        }

        @Test
        void of6Sum() {
            // FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            // float sum = tuple.sum();   // 21.0f
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(21.0f, tuple.sum(), 0.001f);
        }

        private static FloatTuple<?> createWithOf(float[] values) {
            return switch (values.length) {
                case 1 -> FloatTuple.of(values[0]);
                case 2 -> FloatTuple.of(values[0], values[1]);
                case 3 -> FloatTuple.of(values[0], values[1], values[2]);
                case 4 -> FloatTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> FloatTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: 1.0F", new float[] { 1.0F }), Arguments.of("1: 1.5F", new float[] { 1.5F }),
                    Arguments.of("1: 3.14F", new float[] { 3.14F }), Arguments.of("1: 5.0F", new float[] { 5.0F }),
                    Arguments.of("2: 1.0F, 2.0F", new float[] { 1.0F, 2.0F }), Arguments.of("2: 1.5F, 2.5F", new float[] { 1.5F, 2.5F }),
                    Arguments.of("2: Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY", new float[] { Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY }),
                    Arguments.of("3: 1.0F, 2.0F, 3.0F", new float[] { 1.0F, 2.0F, 3.0F }),
                    Arguments.of("3: 1.5F, 2.5F, 3.5F", new float[] { 1.5F, 2.5F, 3.5F }),
                    Arguments.of("4: 1.0F, 2.0F, 3.0F, 4.0F", new float[] { 1.0F, 2.0F, 3.0F, 4.0F }),
                    Arguments.of("4: 1.5F, 2.5F, 3.5F, 4.5F", new float[] { 1.5F, 2.5F, 3.5F, 4.5F }),
                    Arguments.of("5: 1.0F, 2.0F, 3.0F, 4.0F, 5.0F", new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F }),
                    Arguments.of("5: 1.5F, 2.5F, 3.5F, 4.5F, 5.5F", new float[] { 1.5F, 2.5F, 3.5F, 4.5F, 5.5F }),
                    Arguments.of("6: 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F", new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F }),
                    Arguments.of("6: 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F", new float[] { 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F }),
                    Arguments.of("7: 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F", new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F }),
                    Arguments.of("7: 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F", new float[] { 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F }),
                    Arguments.of("8: 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F", new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F }),
                    Arguments.of("8: 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F, 8.5F", new float[] { 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F, 8.5F }),
                    Arguments.of("9: 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F",
                            new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F }),
                    Arguments.of("9: 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F, 8.5F, 9.5F",
                            new float[] { 1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F, 8.5F, 9.5F }));
        }

        private static float[] publicFields(FloatTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    FloatTuple.FloatTuple1 value = (FloatTuple.FloatTuple1) tuple;
                    yield new float[] { value._1 };
                }
                case 2 -> {
                    FloatTuple.FloatTuple2 value = (FloatTuple.FloatTuple2) tuple;
                    yield new float[] { value._1, value._2 };
                }
                case 3 -> {
                    FloatTuple.FloatTuple3 value = (FloatTuple.FloatTuple3) tuple;
                    yield new float[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    FloatTuple.FloatTuple4 value = (FloatTuple.FloatTuple4) tuple;
                    yield new float[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    FloatTuple.FloatTuple5 value = (FloatTuple.FloatTuple5) tuple;
                    yield new float[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    FloatTuple.FloatTuple6 value = (FloatTuple.FloatTuple6) tuple;
                    yield new float[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    FloatTuple.FloatTuple7 value = (FloatTuple.FloatTuple7) tuple;
                    yield new float[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    FloatTuple.FloatTuple8 value = (FloatTuple.FloatTuple8) tuple;
                    yield new float[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    FloatTuple.FloatTuple9 value = (FloatTuple.FloatTuple9) tuple;
                    yield new float[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
                }
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + tuple.arity());
            };
        }
    }

    @Nested
    class FromTests {

        @Test
        void create_emptyArray() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_multipleElementsArray() {
            FloatTuple3 tuple = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1.0f, tuple._1, 0.0001f);
            assertEquals(2.0f, tuple._2, 0.0001f);
            assertEquals(3.0f, tuple._3, 0.0001f);
        }

        @Test
        void create_nullArray() {
            FloatTuple0 tuple = FloatTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_singleElementArray() {
            FloatTuple1 tuple = FloatTuple.from(new float[] { 42.0f });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(42.0f, tuple._1, 0.0001f);
        }

        @Test
        void create_tooManyElements() {
            float[] array = new float[10];
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.from(array));
        }

        @Test
        void from_array1() {
            FloatTuple1 tuple = FloatTuple.from(new float[] { 5.5f });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(5.5f, tuple._1);
        }

        @Test
        void from_array2() {
            FloatTuple2 tuple = FloatTuple.from(new float[] { 1.1f, 2.2f });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(1.1f, tuple._1);
            assertEquals(2.2f, tuple._2);
        }

        @Test
        void from_array3() {
            FloatTuple3 tuple = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1.0f, tuple._1);
            assertEquals(3.0f, tuple._3);
        }

        @Test
        void from_array9() {
            FloatTuple9 tuple = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_emptyArray() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nullArray() {
            FloatTuple<?> tuple = FloatTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_preservesElements() {
            // Test empty array
            FloatTuple<?> empty = FloatTuple.from(null);
            assertEquals(0, empty.arity());

            empty = FloatTuple.from(new float[0]);
            assertEquals(0, empty.arity());

            // Test array with 1 element
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.from(new float[] { 1.5f });
            assertEquals(1, tuple1.arity());
            assertEquals(1.5f, tuple1._1, DELTA);

            // Test array with 5 elements
            FloatTuple.FloatTuple5 tuple5 = FloatTuple.from(new float[] { 1.5f, 2.5f, 3.5f, 4.5f, 5.5f });
            assertEquals(5, tuple5.arity());
            assertEquals(5.5f, tuple5._5, DELTA);

            // Test array with 9 elements
            FloatTuple.FloatTuple9 tuple9 = FloatTuple.from(new float[] { 1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f });
            assertEquals(9, tuple9.arity());
            assertEquals(9.5f, tuple9._9, DELTA);

            // Test too many elements
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.from(new float[10]));
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.from(new float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        void from1() {
            FloatTuple1 tuple = FloatTuple.from(new float[] { 1.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from1_values_FloatTuple_from() {
            FloatTuple1 tuple = FloatTuple.from(new float[] { 42.0f });
            assertEquals(42.0f, tuple._1, 0.001f);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2() {
            FloatTuple2 tuple = FloatTuple.from(new float[] { 1.0f, 2.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
        }

        @Test
        void from2Through8() {
            FloatTuple2 tuple2 = FloatTuple.from(new float[] { 1.0f, 2.0f });
            assertEquals(1.0f, tuple2._1, 0.001f);
            assertEquals(2.0f, tuple2._2, 0.001f);

            FloatTuple4 tuple4 = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f });
            assertEquals(1.0f, tuple4._1, 0.001f);
            assertEquals(4.0f, tuple4._4, 0.001f);

            FloatTuple5 tuple5 = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f });
            assertEquals(1.0f, tuple5._1, 0.001f);
            assertEquals(5.0f, tuple5._5, 0.001f);

            FloatTuple6 tuple6 = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f });
            assertEquals(1.0f, tuple6._1, 0.001f);
            assertEquals(6.0f, tuple6._6, 0.001f);

            FloatTuple7 tuple7 = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f });
            assertEquals(1.0f, tuple7._1, 0.001f);
            assertEquals(7.0f, tuple7._7, 0.001f);

            FloatTuple8 tuple8 = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f });
            assertEquals(1.0f, tuple8._1, 0.001f);
            assertEquals(8.0f, tuple8._8, 0.001f);
        }

        @Test
        void from3() {
            FloatTuple3 tuple = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
            assertEquals(3.0f, tuple._3, 0.001f);
        }

        @Test
        void from9() {
            FloatTuple9 tuple = FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(9.0f, tuple._9, 0.001f);
        }

        @Test
        void fromEmpty() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromNull() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                FloatTuple.from(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f });
            });
        }
    }

    @Nested
    class ArityTests {

        @Test
        void arity_returnsElementCount() {
            assertEquals(0, FloatTuple.from(new float[0]).arity());
            assertEquals(1, FloatTuple.of(1.0f).arity());
            assertEquals(2, FloatTuple.of(1.0f, 2.0f).arity());
            assertEquals(3, FloatTuple.of(1.0f, 2.0f, 3.0f).arity());
            assertEquals(4, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).arity());
            assertEquals(5, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f).arity());
            assertEquals(6, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).arity());
            assertEquals(7, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f).arity());
            assertEquals(8, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f).arity());
            assertEquals(9, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f).arity());
        }

        @Test
        void arity_tuple0() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void arity0_arity() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void coverageRegression_highArityEqualsBranches() {
            FloatTuple.FloatTuple4 tuple4 = FloatTuple.of(1f, 2f, 3f, 4f);
            assertTrue(tuple4.equals(tuple4));
            assertTrue(tuple4.equals(FloatTuple.of(1f, 2f, 3f, 4f)));
            assertFalse(tuple4.equals(FloatTuple.of(1f, 2f, 3f, 9f)));
            assertFalse(tuple4.equals("tuple4"));

            FloatTuple.FloatTuple5 tuple5 = FloatTuple.of(1f, 2f, 3f, 4f, 5f);
            assertTrue(tuple5.equals(tuple5));
            assertTrue(tuple5.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f)));
            assertFalse(tuple5.equals(FloatTuple.of(1f, 2f, 3f, 4f, 9f)));
            assertFalse(tuple5.equals("tuple5"));

            FloatTuple.FloatTuple6 tuple6 = FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f);
            assertTrue(tuple6.equals(tuple6));
            assertTrue(tuple6.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f)));
            assertFalse(tuple6.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 9f)));
            assertFalse(tuple6.equals("tuple6"));

            FloatTuple.FloatTuple7 tuple7 = FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f);
            assertTrue(tuple7.equals(tuple7));
            assertTrue(tuple7.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f)));
            assertFalse(tuple7.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 9f)));
            assertFalse(tuple7.equals("tuple7"));

            FloatTuple.FloatTuple8 tuple8 = FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);
            assertTrue(tuple8.equals(tuple8));
            assertTrue(tuple8.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f)));
            assertFalse(tuple8.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 9f)));
            assertFalse(tuple8.equals("tuple8"));

            FloatTuple.FloatTuple9 tuple9 = FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f);
            assertTrue(tuple9.equals(tuple9));
            assertTrue(tuple9.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f)));
            assertFalse(tuple9.equals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 0f)));
            assertFalse(tuple9.equals("tuple9"));
        }

        @Test
        void coverageRegression_highArityOperations() {
            FloatTuple.FloatTuple4 tuple4 = FloatTuple.of(9f, 1f, 7f, 3f);
            assertEquals(1f, tuple4.min(), 0.0f);
            assertEquals(9f, tuple4.max(), 0.0f);
            assertEquals(3f, tuple4.lowerMedian(), 0.0f);
            assertEquals(20f, tuple4.sum(), 0.0f);
            assertEquals(5.0, tuple4.average().getAsDouble());
            assertTrue(tuple4.contains(3f));

            FloatTuple.FloatTuple5 tuple5 = FloatTuple.of(1f, 2f, 3f, 4f, 5f);
            assertTrue(tuple5.contains(5f));

            FloatTuple.FloatTuple6 tuple6 = FloatTuple.of(9f, 1f, 7f, 3f, 5f, 11f);
            assertEquals(5f, tuple6.lowerMedian(), 0.0f);
            assertTrue(tuple6.contains(11f));

            FloatTuple.FloatTuple7 tuple7 = FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f);
            assertTrue(tuple7.contains(7f));

            FloatTuple.FloatTuple8 tuple8 = FloatTuple.of(9f, 1f, 7f, 3f, 5f, 11f, 13f, 15f);
            assertEquals(7f, tuple8.lowerMedian(), 0.0f);
            assertTrue(tuple8.contains(15f));

            FloatTuple.FloatTuple9 tuple9 = FloatTuple.of(9f, 1f, 7f, 3f, 5f, 11f, 13f, 15f, 17f);
            assertEquals(9f, tuple9.lowerMedian(), 0.0f);
            assertTrue(tuple9.contains(17f));
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void elementsMethod() {
            // Just test that elements are cached properly
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
            float[] elements1 = tuple.elements();
            float[] elements2 = tuple.elements();
            assertSame(elements1, elements2); // Should return same cached array
        }
    }

    @Nested
    class MinTests {

        @Test
        void arity0_min_throwsException() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void arity1_min() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.min());
        }

        @Test
        void arity1_min_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.min(), 0.0001f);
        }

        @Test
        void arity2_min() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min_values_3_0F_1_0F() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 1.0f);
            assertEquals(1.0f, tuple.min(), 0.0001f);
        }

        @Test
        void arity3_min() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min_values_3_0F_1_0F_2_0F() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min(), 0.0001f);
        }

        @Test
        void baseMinMax_naNPropagationAndEmpty() {
            // Tuple containing a NaN element should produce NaN min/max (was previously
            // incorrectly skipping NaN via N.min/N.max).
            final FloatTuple.FloatTuple4 tupleWithNaN = FloatTuple.of(1.0f, Float.NaN, 2.0f, 3.0f);
            assertTrue(Float.isNaN(tupleWithNaN.min()), "min() should propagate NaN");
            assertTrue(Float.isNaN(tupleWithNaN.max()), "max() should propagate NaN");

            // NaN in trailing position also propagates (covers loop tail).
            final FloatTuple.FloatTuple4 tupleNaNLast = FloatTuple.of(1.0f, 2.0f, 3.0f, Float.NaN);
            assertTrue(Float.isNaN(tupleNaNLast.min()));
            assertTrue(Float.isNaN(tupleNaNLast.max()));

            // Without NaN, results are still correct.
            final FloatTuple.FloatTuple4 tuple = FloatTuple.of(4.5f, 1.5f, 3.5f, 2.5f);
            assertEquals(1.5f, tuple.min(), 0.0f);
            assertEquals(4.5f, tuple.max(), 0.0f);

            // Single element including NaN still propagates.
            final FloatTuple.FloatTuple1 onlyNaN = FloatTuple.of(Float.NaN);
            assertTrue(Float.isNaN(onlyNaN.min()));
            assertTrue(Float.isNaN(onlyNaN.max()));

            // Median follows Float.compare ordering, where NaN sorts after finite values.
            assertEquals(1.0f, FloatTuple.of(1.0f, Float.NaN).lowerMedian(), 0.0f);
            assertEquals(1.0f, FloatTuple.of(Float.NaN, 1.0f).lowerMedian(), 0.0f);

            // Empty min/max/median must throw NoSuchElementException; average() returns an empty OptionalDouble.
            final FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, empty::min);
            assertThrows(NoSuchElementException.class, empty::max);
            assertThrows(NoSuchElementException.class, empty::median);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void min_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::min);
        }

        @Test
        void min_returnsMinimum() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
            assertEquals(1.5f, tuple.min(), DELTA);

            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> empty.min());
        }

        @Test
        void min_returnsMinimum_values_3_0F_1_0F_2_0F() {
            // FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            // float min = tuple.min();   // 1.0f
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min(), 0.001f);

            // FloatTuple.FloatTuple2 pair = FloatTuple.of(2.5f, 1.5f);
            // float minPair = pair.min();   // 1.5f
            FloatTuple.FloatTuple2 pair = FloatTuple.of(2.5f, 1.5f);
            assertEquals(1.5f, pair.min(), 0.001f);
        }

        @Test
        void min_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertEquals(5.5f, tuple.min());
        }

        @Test
        void min_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min());
        }

        @Test
        void min_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(10.5f, -3.2f, 0.0f, 7.1f, -10.0f);
            assertEquals(-10.0f, tuple.min());
        }

        @Test
        void minMax_nanPropagation_arity4to9() {
            final float n = Float.NaN;

            assertTrue(Float.isNaN(FloatTuple.of(1f, n, 2f, 3f).min()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, n, 2f, 3f).max()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, 4f, n).min()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, 4f, n).max()));
            assertTrue(Float.isNaN(FloatTuple.of(n, 2f, 3f, 4f, 5f, 6f).min()));
            assertTrue(Float.isNaN(FloatTuple.of(n, 2f, 3f, 4f, 5f, 6f).max()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, n, 5f, 6f, 7f).min()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, n, 5f, 6f, 7f).max()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, n).min()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, n).max()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, n).min()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, n).max()));

            // Without NaN, results are still correct.
            assertEquals(1f, FloatTuple.of(4f, 1f, 3f, 2f).min(), 0f);
            assertEquals(4f, FloatTuple.of(4f, 1f, 3f, 2f).max(), 0f);
            assertEquals(1f, FloatTuple.of(4f, 1f, 3f, 2f, 5f).min(), 0f);
            assertEquals(5f, FloatTuple.of(4f, 1f, 3f, 2f, 5f).max(), 0f);
            assertEquals(1f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f).min(), 0f);
            assertEquals(6f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f).max(), 0f);
            assertEquals(1f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f, 7f).min(), 0f);
            assertEquals(7f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f, 7f).max(), 0f);
            assertEquals(1f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f, 7f, 8f).min(), 0f);
            assertEquals(8f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f, 7f, 8f).max(), 0f);
            assertEquals(1f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f, 7f, 8f, 9f).min(), 0f);
            assertEquals(9f, FloatTuple.of(4f, 1f, 3f, 2f, 5f, 6f, 7f, 8f, 9f).max(), 0f);
        }

        @Test
        void minTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.min(), 0.001f);
        }

        @Test
        void minTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.min(), 0.001f);
        }

        @Test
        void minTuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min(), 0.001f);
        }

        @Test
        void minWithNegativeValues() {
            FloatTuple3 tuple = FloatTuple.of(-5.0f, -1.0f, -10.0f);
            assertEquals(-10.0f, tuple.min(), 0.001f);
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity0_max_throwsException() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void arity1_max() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.max());
        }

        @Test
        void arity1_max_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.max(), 0.0001f);
        }

        @Test
        void arity2_max() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max_values_3_0F_1_0F() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 1.0f);
            assertEquals(3.0f, tuple.max(), 0.0001f);
        }

        @Test
        void arity3_max() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max_values_3_0F_1_0F_2_0F() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(3.0f, tuple.max(), 0.0001f);
        }

        @Test
        void max_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::max);
        }

        @Test
        void max_returnsMaximum() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
            assertEquals(3.5f, tuple.max(), DELTA);

            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> empty.max());
        }

        @Test
        void max_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertEquals(5.5f, tuple.max());
        }

        @Test
        void max_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(3.0f, tuple.max());
        }

        @Test
        void max_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(10.5f, -3.2f, 0.0f, 7.1f, -10.0f);
            assertEquals(10.5f, tuple.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.max(), 0.001f);
        }

        @Test
        void maxTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.max(), 0.001f);
        }

        @Test
        void maxTuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(3.0f, tuple.max(), 0.001f);
        }

        @Test
        void maxTuple7() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(7.0f, tuple.max(), 0.001f);
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity0_sum() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertEquals(0.0f, tuple.sum(), 0.0001f);
        }

        @Test
        void arity1_sum() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity1_sum_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.sum(), 0.0001f);
        }

        @Test
        void arity2_sum() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum_values_1_5F_2_5F() {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            assertEquals(4.0f, tuple.sum(), 0.0001f);
        }

        @Test
        void arity3_sum() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(6.0f, tuple.sum(), 0.0001f);
        }

        @Test
        void sum_emptyAndNaN() {
            assertEquals(0.0f, FloatTuple.from(new float[0]).sum());
            assertTrue(Float.isNaN(FloatTuple.of(1.0f, Float.NaN).sum()));
            assertTrue(Float.isNaN(FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY).sum()));
        }

        @Test
        void sum_isCompensatedNotLeftToRight() {
            // accumulated in double, so the two trailing units survive
            final float big = Float.parseFloat("16777216"), one = 1f;
            assertEquals(1.6777218E7f, FloatTuple.of(big, one, one).sum());
            assertEquals(1.6777216E7f, big + one + one, "plain left-to-right addition loses both units");

            // The double accumulator also avoids intermediate float overflow before cancellation.
            assertEquals(Float.MAX_VALUE, FloatTuple.of(Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE).sum());
            assertEquals(-Float.MAX_VALUE, FloatTuple.of(-Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE).sum());
        }

        @Test
        void sum_losesSignOfZeroBeyondArityOne() {
            assertEquals(Float.floatToRawIntBits(-0.0f), Float.floatToRawIntBits(FloatTuple.of(-0.0f).sum()));
            assertEquals(Float.floatToRawIntBits(0.0f), Float.floatToRawIntBits(FloatTuple.of(-0.0f, -0.0f).sum()));
            final float negZero = Float.parseFloat("-0.0");
            assertEquals(Float.floatToRawIntBits(-0.0f), Float.floatToRawIntBits(negZero + negZero));
        }

        @Test
        void sum_returnsTotal() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.0f);
            assertEquals(7.0f, tuple.sum(), DELTA);

            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertEquals(0.0f, empty.sum(), DELTA);
        }

        @Test
        void sum_tuple0() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertEquals(0.0f, tuple.sum());
        }

        @Test
        void sum_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(3.14f);
            assertEquals(3.14f, tuple.sum(), 0.001f);
        }

        @Test
        void sum_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(6.0f, tuple.sum());
        }

        @Test
        void sum_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(15.0f, tuple.sum());
        }

        @Test
        void sumTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertEquals(0.0f, tuple.sum(), 0.001f);
        }

        @Test
        void sumTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.sum(), 0.001f);
        }

        @Test
        void sumTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.sum(), 0.001f);
        }

        @Test
        void sumTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(6.0f, tuple.sum(), 0.001f);
        }

        @Test
        void sumTuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(10.0f, tuple.sum(), 0.001f);
        }

        @Test
        void sumTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(45.0f, tuple.sum(), 0.001f);
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity0_average_returnsEmpty() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void arity1_average() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity1_average_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity2_average() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average_values_1_0F_3_0F() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 3.0f);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity3_average() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void average_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void average_isOverflowSafeUnlikeSum() {
            assertEquals(Float.POSITIVE_INFINITY, FloatTuple.of(Float.MAX_VALUE, Float.MAX_VALUE).sum());
            assertEquals(3.4028234663852886E38, FloatTuple.of(Float.MAX_VALUE, Float.MAX_VALUE).average().getAsDouble());
        }

        @Test
        void average_losesSignOfZeroBeyondArityOne() {
            assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(FloatTuple.of(-0.0f).average().getAsDouble()));
            assertEquals(Double.doubleToRawLongBits(0.0), Double.doubleToRawLongBits(FloatTuple.of(-0.0f, -0.0f).average().getAsDouble()));
        }

        @Test
        void average_returnsMean() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0f, tuple.average().getAsDouble(), DELTA);

            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void average_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0, tuple.average().getAsDouble());
        }

        @Test
        void average_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0, tuple.average().getAsDouble());
        }

        @Test
        void average_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(2.0f, 4.0f, 6.0f, 8.0f, 10.0f);
            assertEquals(6.0, tuple.average().getAsDouble());
        }

        @Test
        void averageAndMedianPreserveFiniteFloatBoundaries() {
            for (int len = 2; len <= 9; len++) {
                final float[] values = new float[len];

                for (int i = 0; i < len; i++) {
                    values[i] = Float.MAX_VALUE;
                }

                assertEquals((double) Float.MAX_VALUE, FloatTuple.from(values).average().getAsDouble(), "average for arity " + len);
                assertEquals((double) Float.MAX_VALUE, FloatTuple.from(values).median(), "median for arity " + len);
            }

            assertEquals(-(double) Float.MAX_VALUE, FloatTuple.of(-Float.MAX_VALUE, -Float.MAX_VALUE).median());
            assertEquals((double) Float.MIN_VALUE, FloatTuple.of(Float.MIN_VALUE, Float.MIN_VALUE).median());
            assertEquals((double) Float.MAX_VALUE, FloatTuple.of(0f, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE).median());
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple2() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            assertEquals(15.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple6() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }
    }

    @Nested
    class MedianTests {

        @Test
        void arity0_median_throwsException() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void arity1_median() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity1_median_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.lowerMedian(), 0.0001f);
        }

        @Test
        void arity2_median() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median_values_1_0F_2_0F() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            float median = tuple.lowerMedian();
            assertEquals(1.0f, median, 0f);
        }

        @Test
        void arity3_median() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median_values_30_0F_10_0F_20_0F() {
            FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
            assertEquals(20.0f, tuple.lowerMedian(), 0.0001f);
        }

        @Test
        void median_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::median);
        }

        @Test
        void median_ofOppositeInfinitiesIsNaN() {
            final FloatTuple.FloatTuple2 t = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
            assertEquals(Float.NEGATIVE_INFINITY, t.min());
            assertEquals(Float.POSITIVE_INFINITY, t.max());
            assertTrue(Double.isNaN(t.median()));
            assertEquals(Float.NEGATIVE_INFINITY, t.lowerMedian());
        }

        @Test
        void median_returnsMedian() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
            assertEquals(2.5f, tuple.lowerMedian(), DELTA);

            FloatTuple.FloatTuple4 evenTuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f);
            assertEquals(2.5f, evenTuple.lowerMedian(), DELTA);

            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> empty.lowerMedian());
        }

        @Test
        void median_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertEquals(5.5f, tuple.lowerMedian());
        }

        @Test
        void median_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
            assertEquals(20.0f, tuple.lowerMedian());
        }

        @Test
        void median_tuple4_even() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(2.0f, tuple.lowerMedian());
        }

        @Test
        void median_usesTotalOrderingSoNaNSortsLast() {
            assertEquals(3.0, FloatTuple.of(1.0f, Float.NaN, 3.0f).median());
        }

        @Test
        void medianTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.lowerMedian(), 0.001f);
        }

        @Test
        void medianTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.lowerMedian(), 0.001f);
        }

        @Test
        void medianTuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(2.0f, tuple.lowerMedian(), 0.001f);
        }

        @Test
        void medianTuple3_values_30_0F_10_0F_20_0F() {
            FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
            assertEquals(20.0f, tuple.lowerMedian(), 0.001f);
        }

        @Test
        void medianTuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(2.0f, tuple.lowerMedian(), 0.001f);
        }

        @Test
        void medianTuple5() {
            FloatTuple5 tuple = FloatTuple.of(5.0f, 1.0f, 3.0f, 2.0f, 4.0f);
            assertEquals(3.0f, tuple.lowerMedian(), 0.001f);
        }
    }

    @Nested
    class LowerMedianTests {

        @Test
        void lowerMedian_evenArityDocExamplesDifferFromMedian() {
            assertEquals(2.0f, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).lowerMedian());
            assertEquals(2.5, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).median());
            assertEquals(3.0f, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).lowerMedian());
            assertEquals(3.5, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).median());
            assertEquals(4.0f, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f).lowerMedian());
            assertEquals(4.5, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f).median());
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity0_reversed() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            FloatTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void arity1_reversed() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatTuple.FloatTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity1_reversed_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            FloatTuple1 reversed = tuple.reversed();
            assertEquals(5.0f, reversed._1, 0.0001f);
        }

        @Test
        void arity2_reversed() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            FloatTuple.FloatTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity2_reversed_values_1_0F_2_0F() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 reversed = tuple.reversed();
            assertEquals(2.0f, reversed._1, 0.0001f);
            assertEquals(1.0f, reversed._2, 0.0001f);
        }

        @Test
        void arity3_reversed() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple.FloatTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity3_reversed_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 reversed = tuple.reversed();
            assertEquals(3.0f, reversed._1, 0.0001f);
            assertEquals(2.0f, reversed._2, 0.0001f);
            assertEquals(1.0f, reversed._3, 0.0001f);
        }

        @Test
        void arity4_reversed() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple.FloatTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple.FloatTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            FloatTuple.FloatTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            FloatTuple.FloatTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            FloatTuple.FloatTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple.FloatTuple9 reversed = tuple.reversed();
            assertEquals(9.0f, reversed._1, 0.0f);
            assertEquals(8.0f, reversed._2, 0.0f);
            assertEquals(7.0f, reversed._3, 0.0f);
            assertEquals(6.0f, reversed._4, 0.0f);
            assertEquals(5.0f, reversed._5, 0.0f);
            assertEquals(4.0f, reversed._6, 0.0f);
            assertEquals(3.0f, reversed._7, 0.0f);
            assertEquals(2.0f, reversed._8, 0.0f);
            assertEquals(1.0f, reversed._9, 0.0f);
        }

        @Test
        void reversed_reversesElementOrder() {
            // Test Tuple0
            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            FloatTuple.FloatTuple0 reversedEmpty = empty.reversed();
            assertEquals(0, reversedEmpty.arity());

            // Test Tuple1
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.5f);
            FloatTuple.FloatTuple1 reversed1 = tuple1.reversed();
            assertEquals(1.5f, reversed1._1, DELTA);

            // Test Tuple2
            FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.5f, 2.5f);
            FloatTuple.FloatTuple2 reversed2 = tuple2.reversed();
            assertEquals(2.5f, reversed2._1, DELTA);
            assertEquals(1.5f, reversed2._2, DELTA);

            // Test Tuple3
            FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 3.5f);
            FloatTuple.FloatTuple3 reversed3 = tuple3.reversed();
            assertEquals(3.5f, reversed3._1, DELTA);
            assertEquals(2.5f, reversed3._2, DELTA);
            assertEquals(1.5f, reversed3._3, DELTA);

            // Test larger tuples
            FloatTuple.FloatTuple5 tuple5 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f);
            FloatTuple.FloatTuple5 reversed5 = tuple5.reversed();
            assertEquals(5.5f, reversed5._1, DELTA);
            assertEquals(4.5f, reversed5._2, DELTA);
            assertEquals(3.5f, reversed5._3, DELTA);
            assertEquals(2.5f, reversed5._4, DELTA);
            assertEquals(1.5f, reversed5._5, DELTA);
        }

        @Test
        void reversed_tuple0() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            FloatTuple<?> reversed = tuple.reversed();
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversed_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            FloatTuple1 reversed = tuple.reversed();
            assertEquals(5.5f, reversed._1);
        }

        @Test
        void reversed_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 reversed = tuple.reversed();
            assertEquals(3.0f, reversed._1);
            assertEquals(2.0f, reversed._2);
            assertEquals(1.0f, reversed._3);
        }

        @Test
        void reversed_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple5 reversed = tuple.reversed();
            assertEquals(5.0f, reversed._1);
            assertEquals(1.0f, reversed._5);
        }

        @Test
        void reversedTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            FloatTuple<FloatTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversedTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatTuple1 reversed = tuple.reversed();
            assertEquals(1.0f, reversed._1, 0.001f);
        }

        @Test
        void reversedTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            FloatTuple1 reversed = tuple.reversed();
            assertEquals(42.0f, reversed._1, 0.001f);
        }

        @Test
        void reversedTuple2() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 reversed = tuple.reversed();
            assertEquals(2.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._2, 0.001f);
        }

        @Test
        void reversedTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 reversed = tuple.reversed();
            assertEquals(3.0f, reversed._1, 0.001f);
            assertEquals(2.0f, reversed._2, 0.001f);
            assertEquals(1.0f, reversed._3, 0.001f);
        }

        @Test
        void reversedTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple9 reversed = tuple.reversed();
            assertEquals(9.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._9, 0.001f);
        }
    }

    @Nested
    class ContainsTests {

        @ParameterizedTest(name = "arity {0}: contains checks every component")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void contains_checksEveryComponentAndRejectsMissingValue(int arity) {
            float[] values = new float[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = (float) (i + 1);
            }
            FloatTuple<?> tuple = FloatTuple.from(values);
            for (float value : values) {
                assertTrue(tuple.contains(value), "missing component " + value);
            }
            assertFalse(tuple.contains((float) 100));
        }

        @Test
        void arity0_contains() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertFalse(tuple.contains(1.0f));
        }

        @Test
        void arity1_contains() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity1_contains_found() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertTrue(tuple.contains(5.0f));
        }

        @Test
        void arity1_contains_notFound() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertFalse(tuple.contains(10.0f));
        }

        @Test
        void arity2_contains() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity2_contains_found() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(2.0f));
        }

        @Test
        void arity2_contains_notFound() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertFalse(tuple.contains(3.0f));
        }

        @Test
        void arity3_contains() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity3_contains_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(2.0f));
            assertFalse(tuple.contains(5.0f));
        }

        @Test
        void arity4_contains() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity5_contains() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity6_contains() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity7_contains() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity8_contains() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void arity9_contains() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void contains_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            assertFalse(tuple.contains(1.0f));
        }

        @Test
        void contains_findsPresentElements() {
            // Test Tuple0
            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertFalse(empty.contains(1.5f));

            // Test Tuple1
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.5f);
            assertTrue(tuple1.contains(1.5f));
            assertFalse(tuple1.contains(2.5f));

            // Test Tuple3
            FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 3.5f);
            assertTrue(tuple3.contains(1.5f));
            assertTrue(tuple3.contains(2.5f));
            assertTrue(tuple3.contains(3.5f));
            assertFalse(tuple3.contains(4.5f));

            // Test larger tuples
            FloatTuple.FloatTuple7 tuple7 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f);
            assertTrue(tuple7.contains(7.5f));
            assertFalse(tuple7.contains(8.5f));
        }

        @Test
        void contains_higherArityTrailingElements() {
            FloatTuple.FloatTuple6 tuple6 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f);
            FloatTuple.FloatTuple8 tuple8 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f);
            FloatTuple.FloatTuple9 tuple9 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f);

            assertTrue(tuple6.contains(6.5f));
            assertFalse(tuple6.contains(10.5f));
            assertTrue(tuple8.contains(8.5f));
            assertFalse(tuple8.contains(10.5f));
            assertTrue(tuple9.contains(9.5f));
            assertFalse(tuple9.contains(10.5f));
        }

        @Test
        void contains_tuple1_found() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertTrue(tuple.contains(5.5f));
        }

        @Test
        void contains_tuple1_notFound() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertFalse(tuple.contains(3.3f));
        }

        @Test
        void contains_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(2.0f));
            assertTrue(tuple.contains(3.0f));
            assertFalse(tuple.contains(4.0f));
        }

        @Test
        void contains_withNaN() {
            FloatTuple2 tuple = FloatTuple.of(Float.NaN, 1.0f);
            assertTrue(tuple.contains(Float.NaN));
        }

        @Test
        void containsTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertFalse(tuple.contains(1.0f));
        }

        @Test
        void containsTuple1False() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertFalse(tuple.contains(99.0f));
        }

        @Test
        void containsTuple1False_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertFalse(tuple.contains(41.0f));
        }

        @Test
        void containsTuple1True() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        void containsTuple1True_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertTrue(tuple.contains(42.0f));
        }

        @Test
        void containsTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(2.0f));
            assertTrue(tuple.contains(3.0f));
            assertFalse(tuple.contains(99.0f));
        }

        @Test
        void containsTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertTrue(tuple.contains(5.0f));
            assertFalse(tuple.contains(10.0f));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_independence() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array1 = tuple.toArray();
            float[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
            assertArrayEquals(array1, array2);
        }

        @Test
        void toArray_modification() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            array[0] = 99.0f;
            assertEquals(1.0f, tuple._1, 0.0001f);
        }

        @Test
        void toArray_preservesElements() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
            float[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1.5f, array[0], DELTA);
            assertEquals(2.5f, array[1], DELTA);
            assertEquals(3.5f, array[2], DELTA);

            // Ensure it's a copy
            array[0] = 10.5f;
            assertEquals(1.5f, tuple._1, DELTA);
        }

        @Test
        void toArray_preservesElements_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1.0f, array[0], 0.0001f);
            assertEquals(2.0f, array[1], 0.0001f);
            assertEquals(3.0f, array[2], 0.0001f);
        }

        @Test
        void toArray_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array);
        }

        @Test
        void toArrayImmutable() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array1 = tuple.toArray();
            float[] array2 = tuple.toArray();
            array1[0] = 999.0f;
            assertEquals(1.0f, array2[0], 0.001f);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            array[0] = 999.0f;
            assertEquals(1.0f, tuple._1, 0.001f);
        }

        @Test
        void toArrayTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            float[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArrayTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 1.0f }, array, 0.001f);
        }

        @Test
        void toArrayTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 42.0f }, array, 0.001f);
        }

        @Test
        void toArrayTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array, 0.001f);
        }

        @Test
        void toArrayTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            float[] array = tuple.toArray();
            assertEquals(9, array.length);
            assertEquals(1.0f, array[0], 0.001f);
            assertEquals(9.0f, array[8], 0.001f);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_preservesElements() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.5f, list.get(0), DELTA);
            assertEquals(2.5f, list.get(1), DELTA);
            assertEquals(3.5f, list.get(2), DELTA);
        }

        @Test
        void toList_preservesElements_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0), 0.0001f);
        }

        @Test
        void toList_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            FloatList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(5.5f, list.get(0));
        }

        @Test
        void toList_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0));
            assertEquals(3.0f, list.get(2));
        }

        @Test
        void toListTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            FloatList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(1.0f, list.get(0), 0.001f);
        }

        @Test
        void toListTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            FloatList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42.0f, list.get(0), 0.001f);
        }

        @Test
        void toListTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0), 0.001f);
            assertEquals(2.0f, list.get(1), 0.001f);
            assertEquals(3.0f, list.get(2), 0.001f);
        }

        @Test
        void toListTuple3_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0), 0.001f);
            assertEquals(3.0f, list.get(2), 0.001f);
        }

        @Test
        void toListTuple4Through9() {
            FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(4.0f, list4.get(3), 0.001f);

            FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9.0f, list9.get(8), 0.001f);
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity2_forEach_values_1_0F_2_0F() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            List<Float> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(1.0f, values.get(0), 0.0001f);
            assertEquals(2.0f, values.get(1), 0.0001f);
        }

        @Test
        void arity3_forEach() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity3_forEach_values_1_0F_2_0F_3_0F() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        void arity4_forEach() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_multipleElements() throws Exception {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            List<Float> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void forEach_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(5.5f, collected.get(0));
        }

        @Test
        void forEach_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0f, collected.get(0));
            assertEquals(3.0f, collected.get(2));
        }

        @Test
        void forEach_visitsElements() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.0f);
            float[] sum = { 0.0f };
            tuple.forEach(f -> sum[0] += f);
            assertEquals(7.0f, sum[0], DELTA);

            // Test Tuple2 forEach
            FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.5f, 2.5f);
            int[] count = { 0 };
            tuple2.forEach(f -> count[0]++);
            assertEquals(2, count[0]);
        }

        @Test
        void forEachTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple0_values_FloatTuple_from() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(0, collected.size());
        }

        @Test
        void forEachTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Float.valueOf(1.0f), result.get(0));
        }

        @Test
        void forEachTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(42.0f, collected.get(0), 0.001f);
        }

        @Test
        void forEachTuple2() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(10.0f, collected.get(0), 0.001f);
            assertEquals(20.0f, collected.get(1), 0.001f);
        }

        @Test
        void forEachTuple2Override() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Float.valueOf(10.0f), result.get(0));
            assertEquals(Float.valueOf(20.0f), result.get(1));
        }

        @Test
        void forEachTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Float.valueOf(1.0f), result.get(0));
            assertEquals(Float.valueOf(2.0f), result.get(1));
            assertEquals(Float.valueOf(3.0f), result.get(2));
        }

        @Test
        void forEachTuple3_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0f, collected.get(0), 0.001f);
            assertEquals(2.0f, collected.get(1), 0.001f);
            assertEquals(3.0f, collected.get(2), 0.001f);
        }

        @Test
        void forEachTuple3Override() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Float.valueOf(30.0f), result.get(2));
        }

        @Test
        void forEachTuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Float.valueOf(4.0f), result.get(3));
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum);
        }

        @Test
        void stream_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            long count = tuple.stream().count();
            assertEquals(5, count);
        }

        @Test
        void stream_visitsElements() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
            float[] array = tuple.stream().toArray();
            assertEquals(3, array.length);
            assertEquals(1.5f, array[0], DELTA);
            assertEquals(2.5f, array[1], DELTA);
            assertEquals(3.5f, array[2], DELTA);

            long count = tuple.stream().filter(f -> f > 2.0f).count();
            assertEquals(2, count);
        }

        @Test
        void stream_visitsElements_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum, 0.0001);
        }

        @Test
        void streamTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            FloatStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatStream stream = tuple.stream();
            assertEquals(1.0f, stream.sum(), 0.001f);
        }

        @Test
        void streamTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            FloatStream stream = tuple.stream();
            assertEquals(1, stream.count());
        }

        @Test
        void streamTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatStream stream = tuple.stream();
            assertEquals(6.0f, stream.sum(), 0.001f);
        }

        @Test
        void streamTuple3_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float sum = (float) tuple.stream().sum();
            assertEquals(6.0f, sum, 0.001f);
        }

        @Test
        void streamTuple4Through9() {
            FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(10.0f, tuple4.stream().sum(), 0.001f);

            FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(45.0f, tuple9.stream().sum(), 0.001f);
        }

        @Test
        void streamTuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            float max = tuple.stream().max().getAsFloat();
            assertEquals(5.0f, max, 0.001f);
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_invokesAction() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<FloatTuple3> results = new ArrayList<>();
            tuple.accept(results::add);
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        void acceptConsumer() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(1.0f, result.get(0), 0.001f);
            assertEquals(2.0f, result.get(1), 0.001f);
            assertEquals(3.0f, result.get(2), 0.001f);
        }

        @Test
        void arity2_accept() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            final float[] result = new float[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7.0f, result[0], 0.0001f);
        }

        @Test
        void arity2_accept_biConsumer() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final float[] result = new float[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6.0f, result[0], 0.0001f);
        }

        @Test
        void arity3_accept_triConsumer() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2_accept() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            List<Float> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals(3.0f, results.get(0));
            assertEquals(4.0f, results.get(1));
        }

        @Test
        void tuple2Accept() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            List<Float> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Float.valueOf(3.0f), result.get(0));
            assertEquals(Float.valueOf(4.0f), result.get(1));
        }

        @Test
        void tuple2Accept_values_3_0F_4_0F() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            final List<Float> values = new ArrayList<>();
            tuple.accept((a, b) -> {
                values.add(a);
                values.add(b);
            });
            assertEquals(2, values.size());
            assertEquals(3.0f, values.get(0), 0.001f);
            assertEquals(4.0f, values.get(1), 0.001f);
        }

        @Test
        void tuple3_accept() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals(1.0f, results.get(0));
            assertEquals(3.0f, results.get(2));
        }

        @Test
        void tuple3Accept() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Float.valueOf(1.0f), result.get(0));
            assertEquals(Float.valueOf(2.0f), result.get(1));
            assertEquals(Float.valueOf(3.0f), result.get(2));
        }

        @Test
        void tuple3Accept_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final List<Float> values = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                values.add(a);
                values.add(b);
                values.add(c);
            });
            assertEquals(3, values.size());
            assertEquals(1.0f, values.get(0), 0.001f);
            assertEquals(2.0f, values.get(1), 0.001f);
            assertEquals(3.0f, values.get(2), 0.001f);
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Float result = tuple.map((a, b) -> a * b);
            assertEquals(12.0f, result, 0.0001f);
        }

        @Test
        void arity2_map_biFunction() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Float result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0f, result, 0.0001f);
        }

        @Test
        void arity3_map_triFunction() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void map_returnsMappedValue() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
            assertEquals("Sum: 6.0", result);
        }

        @Test
        void mapFunction() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6.0f, result, 0.001f);
        }

        @Test
        void tuple2_map() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            float product = tuple.map((a, b) -> a * b);
            assertEquals(12.0f, product);
        }

        @Test
        void tuple2Map() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            float result = tuple.map((a, b) -> a * b);
            assertEquals(12.0f, result, 0.001f);
        }

        @Test
        void tuple3_map() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float product = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0f, product);
        }

        @Test
        void tuple3Map() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0f, result, 0.001f);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity2_filter_notSatisfied() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 1.0f);
            Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        void arity2_filter_satisfied() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void arity3_filter() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filter_appliesPredicate() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 0);
            assertTrue(result.isPresent());

            Optional<FloatTuple3> result2 = tuple.filter(t -> t._1 < 0);
            assertFalse(result2.isPresent());
        }

        @Test
        void filterPredicate() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter(t -> t._1 == 1.0f);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter(t -> t._1 == 99.0f);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());

            Optional<FloatTuple2> result2 = tuple.filter((a, b) -> a + b < 5);
            assertFalse(result2.isPresent());
        }

        @Test
        void tuple2Filter() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            assertTrue(tuple.filter((a, b) -> a + b > 5).isPresent());
            assertFalse(tuple.filter((a, b) -> a + b > 10).isPresent());
        }

        @Test
        void tuple2FilterFalse() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            var result = tuple.filter((a, b) -> a + b > 10.0f);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            var result = tuple.filter((a, b) -> a + b > 5.0f);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3_filter() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());

            Optional<FloatTuple3> result2 = tuple.filter((a, b, c) -> a + b + c < 5);
            assertFalse(result2.isPresent());
        }

        @Test
        void tuple3Filter() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.filter((a, b, c) -> a + b + c > 5).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a + b + c > 10).isPresent());
        }

        @Test
        void tuple3FilterFalse() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter((a, b, c) -> a + b + c > 10.0f);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter((a, b, c) -> a + b + c > 5.0f);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple.FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity1_hashCode_values_5_0F() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple1 tuple2 = FloatTuple.of(5.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            FloatTuple.FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode_values_1_0F_2_0F() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            FloatTuple.FloatTuple4 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple.FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            FloatTuple.FloatTuple5 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple.FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            FloatTuple.FloatTuple6 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            FloatTuple.FloatTuple6 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            FloatTuple.FloatTuple7 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            FloatTuple.FloatTuple7 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            FloatTuple.FloatTuple8 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            FloatTuple.FloatTuple8 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            FloatTuple.FloatTuple9 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple.FloatTuple9 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void floatSemantics_hashCode_naN_and_signedZero() {
            // NaN: equal -> equal hash.
            assertEquals(FloatTuple.of(Float.NaN).hashCode(), FloatTuple.of(Float.NaN).hashCode());
            assertEquals(FloatTuple.of(1f, Float.NaN).hashCode(), FloatTuple.of(1f, Float.NaN).hashCode());
            assertEquals(FloatTuple.of(Float.NaN, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f).hashCode(),
                    FloatTuple.of(Float.NaN, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f).hashCode());

            // +0.0f vs -0.0f: not equal; bit-pattern hash differs.
            assertNotEquals(FloatTuple.of(0.0f).hashCode(), FloatTuple.of(-0.0f).hashCode());
            assertNotEquals(FloatTuple.of(1f, 0.0f).hashCode(), FloatTuple.of(1f, -0.0f).hashCode());
            assertNotEquals(FloatTuple.of(0.0f, 1f, 2f).hashCode(), FloatTuple.of(-0.0f, 1f, 2f).hashCode());
        }

        @Test
        void hashCode_consistency() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_different() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 4.0f);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_matchesValueContract() {
            FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.5f, 2.5f, 3.5f);
            FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.5f, 2.5f, 3.5f);
            FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 4.6f);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

            // Test specific hashcodes for coverage
            FloatTuple.FloatTuple1 single = FloatTuple.of(1.5f);
            assertEquals(Float.floatToIntBits(1.5f), single.hashCode());

            FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
            assertEquals(31 * Float.floatToIntBits(1.5f) + Float.floatToIntBits(2.5f), pair.hashCode());
        }

        @Test
        void hashCode_signChangesCanCollideForUnequalTuples() {
            final FloatTuple.FloatTuple4 positive4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            final FloatTuple.FloatTuple4 negative4 = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f);
            assertNotEquals(positive4, negative4);
            assertEquals(positive4.hashCode(), negative4.hashCode());

            final FloatTuple.FloatTuple6 positive6 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            final FloatTuple.FloatTuple6 negative6 = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f);
            assertNotEquals(positive6, negative6);
            assertEquals(positive6.hashCode(), negative6.hashCode());
        }

        @Test
        void hashCodeTuple0() {
            FloatTuple<FloatTuple0> tuple1 = FloatTuple.from(new float[0]);
            FloatTuple<FloatTuple0> tuple2 = FloatTuple.from(new float[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1_values_42_0F() {
            FloatTuple1 tuple1 = FloatTuple.of(42.0f);
            FloatTuple1 tuple2 = FloatTuple.of(42.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @ParameterizedTest(name = "arity {0}: equality checks every component")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void equals_rejectsEverySingleComponentDifference(int arity) {
            float[] values = new float[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = (float) (i + 1);
            }
            FloatTuple<?> tuple = FloatTuple.from(values);
            // A separately constructed empty value verifies equality independently of the factory singleton.
            FloatTuple<?> equal = arity == 0 ? new FloatTuple.FloatTuple0() : FloatTuple.from(values.clone());
            assertNotSame(tuple, equal);
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(equal));
            assertTrue(equal.equals(tuple));
            assertEquals(tuple.hashCode(), equal.hashCode());
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals(new Object()));

            for (int i = 0; i < arity; i++) {
                float[] changed = values.clone();
                changed[i] = (float) 100;
                FloatTuple<?> different = FloatTuple.from(changed);
                assertFalse(tuple.equals(different), "component " + i);
                assertFalse(different.equals(tuple), "component " + i);
            }
        }

        @Test
        void arity1_equals() {
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple.FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_differentType() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertNotEquals("string", tuple);
        }

        @Test
        void arity1_equals_equal() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple1 tuple2 = FloatTuple.of(5.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_notEqual() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple1 tuple2 = FloatTuple.of(10.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_null() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertNotEquals(null, tuple);
        }

        @Test
        void arity1_equals_same() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void arity2_equals() {
            FloatTuple.FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity2_equals_values_1_0F_2_0F() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity3_equals() {
            FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity4_equals() {
            FloatTuple.FloatTuple4 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple.FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity5_equals() {
            FloatTuple.FloatTuple5 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple.FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity6_equals() {
            FloatTuple.FloatTuple6 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            FloatTuple.FloatTuple6 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity7_equals() {
            FloatTuple.FloatTuple7 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            FloatTuple.FloatTuple7 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity8_equals() {
            FloatTuple.FloatTuple8 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            FloatTuple.FloatTuple8 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equals() {
            FloatTuple.FloatTuple9 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple.FloatTuple9 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equalsAndHashCode_differInOneField() {
            float[] base = { 10f, 20f, 30f, 40f, 50f, 60f, 70f, 80f, 90f };
            FloatTuple.FloatTuple9 t = FloatTuple.of(base[0], base[1], base[2], base[3], base[4], base[5], base[6], base[7], base[8]);
            for (int i = 0; i < 9; i++) {
                float[] diff = base.clone();
                diff[i] = diff[i] + 1.0f;
                FloatTuple.FloatTuple9 other = FloatTuple.of(diff[0], diff[1], diff[2], diff[3], diff[4], diff[5], diff[6], diff[7], diff[8]);
                assertNotEquals(t, other, "field _" + (i + 1) + " should affect equals");
                assertNotEquals(t.hashCode(), other.hashCode(), "field _" + (i + 1) + " should affect hashCode");
            }
        }

        @Test
        void equals_comparesValues() {
            FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.5f, 2.5f, 3.5f);
            FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.5f, 2.5f, 3.5f);
            FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 3.6f);
            FloatTuple.FloatTuple2 tuple4 = FloatTuple.of(1.5f, 2.5f);

            assertTrue(tuple1.equals(tuple1));
            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(tuple4));
            assertFalse(tuple1.equals(null));
            assertFalse(tuple1.equals("not a tuple"));

            // Test specific equals for coverage
            FloatTuple.FloatTuple1 single1 = FloatTuple.of(1.5f);
            FloatTuple.FloatTuple1 single2 = FloatTuple.of(1.5f);
            FloatTuple.FloatTuple1 single3 = FloatTuple.of(2.5f);
            assertTrue(single1.equals(single2));
            assertFalse(single1.equals(single3));
            assertFalse(single1.equals("not a tuple"));

            FloatTuple.FloatTuple2 pair1 = FloatTuple.of(1.5f, 2.5f);
            FloatTuple.FloatTuple2 pair2 = FloatTuple.of(1.5f, 2.5f);
            FloatTuple.FloatTuple2 pair3 = FloatTuple.of(1.5f, 2.6f);
            assertTrue(pair1.equals(pair2));
            assertFalse(pair1.equals(pair3));
        }

        @Test
        void equals_differentTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 4.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_differentTypes() {
            FloatTuple1 tuple1 = FloatTuple.of(5.5f);
            FloatTuple2 tuple2 = FloatTuple.of(5.5f, 1.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_followsFloatCompareSemanticsAtArities4And9() {
            // identical to DoubleTuple's documented rule: NaN == NaN, but +0.0f != -0.0f
            assertEquals(FloatTuple.of(Float.NaN, 2f, 3f, 4f), FloatTuple.of(Float.NaN, 2f, 3f, 4f));
            assertNotEquals(FloatTuple.of(0.0f, 1f, 2f, 3f), FloatTuple.of(-0.0f, 1f, 2f, 3f));
            assertNotEquals(FloatTuple.of(0.0f, 1f, 2f, 3f).hashCode(), FloatTuple.of(-0.0f, 1f, 2f, 3f).hashCode());

            assertEquals(FloatTuple.of(Float.NaN, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f), FloatTuple.of(Float.NaN, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f));
            assertNotEquals(FloatTuple.of(0.0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f), FloatTuple.of(-0.0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f));
        }

        @Test
        void equals_higherArityIdentityAndTypeMismatch() {
            FloatTuple.FloatTuple6 tuple6 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f);
            FloatTuple.FloatTuple8 tuple8 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f);
            FloatTuple.FloatTuple9 tuple9 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f);

            assertTrue(tuple6.equals(tuple6));
            assertEquals(tuple6, FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f));
            assertNotEquals(tuple6, FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 7.5f));
            assertNotEquals(tuple6, "tuple");

            assertTrue(tuple8.equals(tuple8));
            assertEquals(tuple8, FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f));
            assertNotEquals(tuple8, FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 9.5f));
            assertNotEquals(tuple8, "tuple");

            assertTrue(tuple9.equals(tuple9));
            assertEquals(tuple9, FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f));
            assertNotEquals(tuple9, FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 10.5f));
            assertNotEquals(tuple9, "tuple");
        }

        @Test
        void equals_null() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertNotEquals(tuple, null);
        }

        @Test
        void equals_sameTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(5.5f);
            FloatTuple1 tuple2 = FloatTuple.of(5.5f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_sameTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_symmetric() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        void equalsDifferentClass() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        void equalsNull() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotEquals(null, tuple);
        }

        @Test
        void equalsNull_values_1_0F_2_0F() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotEquals(tuple, null);
        }

        @Test
        void equalsSameObject() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equalsSelf() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equalsTuple0() {
            FloatTuple<FloatTuple0> tuple1 = FloatTuple.from(new float[0]);
            FloatTuple<FloatTuple0> tuple2 = FloatTuple.from(new float[0]);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equalsTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            FloatTuple1 tuple3 = FloatTuple.of(99.0f);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple1_values_42_0F() {
            FloatTuple1 tuple1 = FloatTuple.of(42.0f);
            FloatTuple1 tuple2 = FloatTuple.of(42.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equalsTuple2() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple3 = FloatTuple.of(1.0f, 3.0f);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 4.0f);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void notEqualsDifferentTypes() {
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        void notEqualsDifferentValues() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(2.0f, 3.0f);
            assertNotEquals(tuple1, tuple2);
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity0_toString() {
            FloatTuple0 tuple = FloatTuple.from(new float[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void arity1_toString() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity1_toString_values_5_0F() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals("(5.0)", tuple.toString());
        }

        @Test
        void arity2_toString() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString_values_1_0F_2_0F() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertEquals("(1.0, 2.0)", tuple.toString());
        }

        @Test
        void arity3_toString() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.from(new float[0]);
            String str = tuple.toString();
            assertEquals("()", str);
        }

        @Test
        void toString_formatsValue() {
            FloatTuple.FloatTuple0 empty = FloatTuple.from(new float[0]);
            assertEquals("()", empty.toString());

            FloatTuple.FloatTuple1 single = FloatTuple.of(1.5f);
            assertEquals("(1.5)", single.toString());

            FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
            assertEquals("(1.5, 2.5)", pair.toString());

            FloatTuple.FloatTuple3 triple = FloatTuple.of(1.5f, 2.5f, 3.5f);
            assertEquals("(1.5, 2.5, 3.5)", triple.toString());
        }

        @Test
        void toString_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("5.5"));
        }

        @Test
        void toString_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        @Test
        void toStringTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.from(new float[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
        }

        @Test
        void toStringTuple1_values_42_0F() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals("(42.0)", tuple.toString());
        }

        @Test
        void toStringTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        @Test
        void toStringTuple3_values_1_0F_2_0F_3_0F() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void arity4_statisticalOperations() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(1.0f, tuple.min(), 0.0001f);
            assertEquals(4.0f, tuple.max(), 0.0001f);
            assertEquals(10.0f, tuple.sum(), 0.0001f);
            assertEquals(2.5, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void baseMethods_inheritedImplementation() {
            final FloatTuple.FloatTuple4 tuple = FloatTuple.of(4.5f, 1.5f, 3.5f, 2.5f);
            final float[] copy = tuple.toArray();
            copy[0] = 99.5f;

            assertEquals(1.5f, tuple.min(), DELTA);
            assertEquals(4.5f, tuple.max(), DELTA);
            assertEquals(2.5f, tuple.lowerMedian(), DELTA);
            assertEquals(12.0f, tuple.sum(), DELTA);
            assertEquals(3.0, tuple.average().getAsDouble(), DELTA);
            assertTrue(tuple.contains(3.5f));
            assertFalse(tuple.contains(8.5f));
            assertEquals(4.5f, tuple.toArray()[0], DELTA);
            assertTrue(tuple.equals(FloatTuple.of(4.5f, 1.5f, 3.5f, 2.5f)));
            assertFalse(tuple.equals(FloatTuple.of(4.5f, 1.5f, 3.5f, 9.5f)));
            assertFalse(tuple.equals(null));
            assertEquals("(4.5, 1.5, 3.5, 2.5)", tuple.toString());
        }

        @Test
        void classLevelExamples() {
            // float min = triple.min();   // 1.0f
            // float max = triple.max();   // 3.0f
            // OptionalDouble avg = triple.average();   // OptionalDouble.of(2.0)
            FloatTuple.FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(1.0f, triple.min(), 0.001f);
            assertEquals(3.0f, triple.max(), 0.001f);
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        @Test
        void coverageRegression_defaultConstructorsAndCachedArrays() {
            FloatTuple.FloatTuple1 tuple1 = new FloatTuple.FloatTuple1();
            assertArrayEquals(new float[] { 0f }, tuple1.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f }, tuple1.toArray(), 0.0f);

            FloatTuple.FloatTuple2 tuple2 = new FloatTuple.FloatTuple2();
            assertArrayEquals(new float[] { 0f, 0f }, tuple2.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f }, tuple2.toArray(), 0.0f);

            FloatTuple.FloatTuple3 tuple3 = new FloatTuple.FloatTuple3();
            assertArrayEquals(new float[] { 0f, 0f, 0f }, tuple3.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f }, tuple3.toArray(), 0.0f);

            FloatTuple.FloatTuple4 tuple4 = new FloatTuple.FloatTuple4();
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f }, tuple4.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f }, tuple4.toArray(), 0.0f);

            FloatTuple.FloatTuple5 tuple5 = new FloatTuple.FloatTuple5();
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f }, tuple5.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f }, tuple5.toArray(), 0.0f);

            FloatTuple.FloatTuple6 tuple6 = new FloatTuple.FloatTuple6();
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f }, tuple6.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f }, tuple6.toArray(), 0.0f);

            FloatTuple.FloatTuple7 tuple7 = new FloatTuple.FloatTuple7();
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f, 0f }, tuple7.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f, 0f }, tuple7.toArray(), 0.0f);

            FloatTuple.FloatTuple8 tuple8 = new FloatTuple.FloatTuple8();
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f }, tuple8.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f }, tuple8.toArray(), 0.0f);

            FloatTuple.FloatTuple9 tuple9 = new FloatTuple.FloatTuple9();
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f }, tuple9.toArray(), 0.0f);
            assertArrayEquals(new float[] { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f }, tuple9.toArray(), 0.0f);
        }

        @Test
        void coverageRegression_emptyTupleBaseMethods() {
            FloatTuple<?> empty = FloatTuple.from((float[]) null);
            assertEquals(FloatTuple.from(new float[0]), empty);
            assertFalse(empty.equals("float"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void defaultConstructors_zeroInitialization() {
            final FloatTuple1 tuple1 = new FloatTuple1();
            final FloatTuple2 tuple2 = new FloatTuple2();
            final FloatTuple3 tuple3 = new FloatTuple3();
            final FloatTuple4 tuple4 = new FloatTuple4();
            final FloatTuple5 tuple5 = new FloatTuple5();
            final FloatTuple6 tuple6 = new FloatTuple6();
            final FloatTuple7 tuple7 = new FloatTuple7();
            final FloatTuple8 tuple8 = new FloatTuple8();
            final FloatTuple9 tuple9 = new FloatTuple9();

            assertArrayEquals(new float[] { 0.0f }, tuple1.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f }, tuple1.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f }, tuple2.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f }, tuple2.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f }, tuple3.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f }, tuple3.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, tuple4.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, tuple4.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple5.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple5.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple6.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple6.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple7.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple7.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple8.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple8.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple9.toArray(), DELTA);
            assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, tuple9.toArray(), DELTA);
        }

        @Test
        void floatSemantics_aggregatesWithSpecialValues() {
            // Sum: +INF + (-INF) = NaN (IEEE-754); NaN propagates through sum/average.
            assertTrue(Float.isNaN(FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY).sum()));
            assertTrue(Float.isNaN(FloatTuple.of(1f, 2f, Float.NaN).sum()));
            assertTrue(Double.isNaN(FloatTuple.of(1f, 2f, Float.NaN).average().getAsDouble()));

            // Min/max with -0.0f vs +0.0f: Math.min/max treats -0.0f as strictly less than +0.0f.
            assertEquals(Float.floatToRawIntBits(-0.0f), Float.floatToRawIntBits(FloatTuple.of(0.0f, -0.0f).min()));
            assertEquals(Float.floatToRawIntBits(+0.0f), Float.floatToRawIntBits(FloatTuple.of(0.0f, -0.0f).max()));
            // Same for higher arities (Tuple4-9 use Math.min/max chains).
            assertEquals(Float.floatToRawIntBits(-0.0f), Float.floatToRawIntBits(FloatTuple.of(1f, 0.0f, 2f, -0.0f, 3f, 4f, 5f, 6f, 7f).min()));

            // Median for 2 elements uses Float.compare ordering, where NaN sorts last.
            assertFalse(Float.isNaN(FloatTuple.of(1f, Float.NaN).lowerMedian()));
        }

        @Test
        void floatSemantics_naN_and_signedZero() {
            // ---- contains() must treat NaN as equal to NaN across all arities. ----
            assertTrue(FloatTuple.of(Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, 3f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, 3f, 4f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, 3f, 4f, 5f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, Float.NaN).contains(Float.NaN));
            assertTrue(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, Float.NaN).contains(Float.NaN));

            // ---- contains() must NOT treat +0.0f and -0.0f as equal. ----
            assertFalse(FloatTuple.of(0.0f).contains(-0.0f));
            assertFalse(FloatTuple.of(-0.0f).contains(0.0f));
            assertFalse(FloatTuple.of(1f, 0.0f).contains(-0.0f));
            assertFalse(FloatTuple.of(1f, 2f, 0.0f).contains(-0.0f));
            assertFalse(FloatTuple.of(1f, 2f, 3f, 0.0f).contains(-0.0f));
            assertFalse(FloatTuple.of(1f, 2f, 3f, 4f, 0.0f).contains(-0.0f));
            // Same value matches with sign preserved.
            assertTrue(FloatTuple.of(0.0f).contains(0.0f));
            assertTrue(FloatTuple.of(-0.0f).contains(-0.0f));

            // ---- equals() must treat NaN as equal to NaN. ----
            assertEquals(FloatTuple.of(Float.NaN), FloatTuple.of(Float.NaN));
            assertEquals(FloatTuple.of(1f, Float.NaN), FloatTuple.of(1f, Float.NaN));
            assertEquals(FloatTuple.of(1f, 2f, Float.NaN), FloatTuple.of(1f, 2f, Float.NaN));
            assertEquals(FloatTuple.of(1f, 2f, 3f, Float.NaN), FloatTuple.of(1f, 2f, 3f, Float.NaN));
            assertEquals(FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, Float.NaN), FloatTuple.of(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, Float.NaN));

            // ---- equals() must NOT treat +0.0f and -0.0f as equal. ----
            assertNotEquals(FloatTuple.of(0.0f), FloatTuple.of(-0.0f));
            assertNotEquals(FloatTuple.of(0.0f, 1f), FloatTuple.of(-0.0f, 1f));
            assertNotEquals(FloatTuple.of(1f, 0.0f, 2f), FloatTuple.of(1f, -0.0f, 2f));
            assertNotEquals(FloatTuple.of(1f, 2f, 0.0f, 3f), FloatTuple.of(1f, 2f, -0.0f, 3f));
            assertNotEquals(FloatTuple.of(1f, 2f, 3f, 0.0f, 4f), FloatTuple.of(1f, 2f, 3f, -0.0f, 4f));
        }

        @Test
        void largerArities_branchCoverage() {
            final FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            final FloatTuple5 tuple5 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            final FloatTuple6 tuple6 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            final FloatTuple7 tuple7 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            final FloatTuple8 tuple8 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            final FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);

            assertTrue(tuple4.contains(2.0f));
            assertTrue(tuple4.contains(3.0f));
            assertTrue(tuple5.contains(4.0f));
            assertTrue(tuple6.contains(5.0f));
            assertTrue(tuple7.contains(6.0f));
            assertTrue(tuple8.contains(7.0f));
            assertTrue(tuple9.contains(8.0f));
            assertFalse(tuple9.contains(10.0f));

            assertTrue(tuple4.equals(tuple4));
            assertFalse(tuple4.equals("not a tuple"));
            assertFalse(tuple4.equals(FloatTuple.of(1.0f, 2.0f, 3.0f, 9.0f)));
            assertTrue(tuple5.equals(tuple5));
            assertFalse(tuple5.equals("not a tuple"));
            assertFalse(tuple5.equals(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 9.0f)));
            assertTrue(tuple6.equals(tuple6));
            assertFalse(tuple6.equals("not a tuple"));
            assertFalse(tuple6.equals(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 9.0f)));
            assertTrue(tuple7.equals(tuple7));
            assertFalse(tuple7.equals("not a tuple"));
            assertFalse(tuple7.equals(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 9.0f)));
            assertTrue(tuple8.equals(tuple8));
            assertFalse(tuple8.equals("not a tuple"));
            assertFalse(tuple8.equals(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 9.0f)));
            assertTrue(tuple9.equals(tuple9));
            assertFalse(tuple9.equals("not a tuple"));
            assertFalse(tuple9.equals(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 10.0f)));
        }

        @Test
        void largeValues() {
            FloatTuple2 tuple = FloatTuple.of(Float.MAX_VALUE, Float.MAX_VALUE / 2);
            assertEquals(Float.MAX_VALUE / 2, tuple.min(), 0.0001f);
            assertEquals(Float.MAX_VALUE, tuple.max(), 0.0001f);
        }

        @Test
        void mixedValues() {
            FloatTuple5 tuple = FloatTuple.of(-2.0f, 0.0f, 5.0f, -1.0f, 3.0f);
            assertEquals(-2.0f, tuple.min(), 0.001f);
            assertEquals(5.0f, tuple.max(), 0.001f);
            assertEquals(5.0f, tuple.sum(), 0.001f);
        }

        @Test
        void naN_handling() {
            FloatTuple2 tuple = FloatTuple.of(Float.NaN, 1.0f);
            assertTrue(Float.isNaN(tuple._1));
            assertEquals(1.0f, tuple._2, 0.0001f);
        }

        @Test
        void negativeValues() {
            FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);
            assertEquals(-3.0f, tuple.min(), 0.001f);
            assertEquals(-1.0f, tuple.max(), 0.001f);
            assertEquals(-6.0f, tuple.sum(), 0.001f);
        }

        @Test
        void negativeValues_values_minus1_0F_minus2_0F_minus3_0F() {
            FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);
            assertEquals(-3.0f, tuple.min(), 0.0001f);
            assertEquals(-1.0f, tuple.max(), 0.0001f);
            assertEquals(-6.0f, tuple.sum(), 0.0001f);
        }

        @Test
        void reverse_largerTuples() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple4 reversed = tuple.reversed();
            assertEquals(4.0f, reversed._1, 0.0001f);
            assertEquals(3.0f, reversed._2, 0.0001f);
            assertEquals(2.0f, reversed._3, 0.0001f);
            assertEquals(1.0f, reversed._4, 0.0001f);
        }

        @Test
        void tuple1SpecificMethods() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.5f);

            // Test min/max/median/sum/average
            assertEquals(1.5f, tuple.min(), DELTA);
            assertEquals(1.5f, tuple.max(), DELTA);
            assertEquals(1.5f, tuple.lowerMedian(), DELTA);
            assertEquals(1.5f, tuple.sum(), DELTA);
            assertEquals(1.5f, tuple.average().getAsDouble(), DELTA);
        }

        @Test
        void tuple2SpecificMethods() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);

            // Test accept
            float[] sum = { 0.0f };
            tuple.accept((a, b) -> sum[0] = a + b);
            assertEquals(4.0f, sum[0], DELTA);

            // Test map
            float result = tuple.map((a, b) -> a + b);
            assertEquals(4.0f, result, DELTA);

            // Test filter
            assertTrue(tuple.filter((a, b) -> a < b).isPresent());
            assertFalse(tuple.filter((a, b) -> a > b).isPresent());
        }

        @Test
        void tuple3SpecificMethods() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);

            // Test accept
            float[] sum = { 0.0f };
            tuple.accept((a, b, c) -> sum[0] = a + b + c);
            assertEquals(7.5f, sum[0], DELTA);

            // Test map
            float result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(7.5f, result, DELTA);

            // Test filter
            assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
        }

        @Test
        void tuple4Operations() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);

            // Test reverse
            FloatTuple4 reversed = tuple.reversed();
            assertEquals(4.0f, reversed._1, 0.001f);
            assertEquals(3.0f, reversed._2, 0.001f);
            assertEquals(2.0f, reversed._3, 0.001f);
            assertEquals(1.0f, reversed._4, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(4.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, tuple.toArray(), 0.001f);

            // Test min/max/median/sum/average via base class
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(4.0f, tuple.max(), 0.001f);
            assertEquals(2.0f, tuple.lowerMedian(), 0.001f);
            assertEquals(10.0f, tuple.sum(), 0.001f);
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple4 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f, 5.0f);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("4.0"));
        }

        @Test
        void tuple5Operations() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);

            // Test reverse
            FloatTuple5 reversed = tuple.reversed();
            assertEquals(5.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._5, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(5.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(5.0f, tuple.max(), 0.001f);
            assertEquals(3.0f, tuple.lowerMedian(), 0.001f);
            assertEquals(15.0f, tuple.sum(), 0.001f);
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);

            // Test equals
            FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(tuple, tuple2);
        }

        @Test
        void tuple6Operations() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);

            // Test reverse
            FloatTuple6 reversed = tuple.reversed();
            assertEquals(6.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._6, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(6.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(6.0f, tuple.max(), 0.001f);
            assertEquals(21.0f, tuple.sum(), 0.001f);
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple7Operations() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);

            // Test reverse
            FloatTuple7 reversed = tuple.reversed();
            assertEquals(7.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._7, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(7.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(7.0f, tuple.max(), 0.001f);
            assertEquals(28.0f, tuple.sum(), 0.001f);
            assertEquals(4.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple8Operations() {
            FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);

            // Test reverse
            FloatTuple8 reversed = tuple.reversed();
            assertEquals(8.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._8, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(8.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(8.0f, tuple.max(), 0.001f);
            assertEquals(36.0f, tuple.sum(), 0.001f);
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9Operations() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);

            // Test reverse
            FloatTuple9 reversed = tuple.reversed();
            assertEquals(9.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._9, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(9.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(9.0f, tuple.max(), 0.001f);
            assertEquals(45.0f, tuple.sum(), 0.001f);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void zeroValues() {
            FloatTuple3 tuple = FloatTuple.of(0.0f, 0.0f, 0.0f);
            assertEquals(0.0f, tuple.min(), 0.001f);
            assertEquals(0.0f, tuple.max(), 0.001f);
            assertEquals(0.0f, tuple.sum(), 0.001f);
        }

        @Test
        void zeroValues_values_0_0F_0_0F_0_0F() {
            FloatTuple3 tuple = FloatTuple.of(0.0f, 0.0f, 0.0f);
            assertEquals(0.0f, tuple.min(), 0.0001f);
            assertEquals(0.0f, tuple.max(), 0.0001f);
            assertEquals(0.0f, tuple.sum(), 0.0001f);
        }
    }

    private static final float DELTA = 0.0001f;
}
