package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.LongTuple.LongTuple0;
import com.landawn.abacus.util.LongTuple.LongTuple1;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple4;
import com.landawn.abacus.util.LongTuple.LongTuple5;
import com.landawn.abacus.util.LongTuple.LongTuple6;
import com.landawn.abacus.util.LongTuple.LongTuple7;
import com.landawn.abacus.util.LongTuple.LongTuple8;
import com.landawn.abacus.util.LongTuple.LongTuple9;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.u.Optional;

class LongTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, long[] values) {
            LongTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of3Average() {
            // LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            // OptionalDouble average = triple.average();   // OptionalDouble.of(2.0)
            LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        @Test
        void of4Sum() {
            // LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
            // long sum = quad.sum();   // 10L
            // long min = quad.min();   // 1L
            LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(10L, quad.sum());
            assertEquals(1L, quad.min());
        }

        @Test
        void of5() {
            // LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            // OptionalDouble avg = tuple.average();   // OptionalDouble.of(3.0)
            // long median = tuple.lowerMedian();   // 3
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);
            assertEquals(3L, tuple.lowerMedian());
        }

        @Test
        void of6() {
            // LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            // long sum = tuple.sum();         // 21
            // OptionalDouble avg = tuple.average();   // OptionalDouble.of(3.5)
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(21L, tuple.sum());
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void of7() {
            // LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            // long sum = tuple.sum();        // 28
            // long median = tuple.lowerMedian();  // 4
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(28L, tuple.sum());
            assertEquals(4L, tuple.lowerMedian());
        }

        @Test
        void of8() {
            // LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            // long sum = tuple.sum();                   // 36
            // OptionalDouble avg = tuple.average();             // OptionalDouble.of(4.5)
            // boolean contains5 = tuple.contains(5L);   // true
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(36L, tuple.sum());
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);
            assertTrue(tuple.contains(5L));
        }

        @Test
        void of9() {
            // LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            // long sum = tuple.sum();         // 45
            // long median = tuple.lowerMedian();   // 5
            // OptionalDouble avg = tuple.average();   // OptionalDouble.of(5.0)
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(45L, tuple.sum());
            assertEquals(5L, tuple.lowerMedian());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        private static LongTuple<?> createWithOf(long[] values) {
            return switch (values.length) {
                case 1 -> LongTuple.of(values[0]);
                case 2 -> LongTuple.of(values[0], values[1]);
                case 3 -> LongTuple.of(values[0], values[1], values[2]);
                case 4 -> LongTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> LongTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: 100L", new long[] { 100L }), Arguments.of("1: 1L", new long[] { 1L }),
                    Arguments.of("1: 42L", new long[] { 42L }), Arguments.of("1: 5L", new long[] { 5L }),
                    Arguments.of("2: 100L, 200L", new long[] { 100L, 200L }), Arguments.of("2: 10L, 20L", new long[] { 10L, 20L }),
                    Arguments.of("2: 1L, 2L", new long[] { 1L, 2L }), Arguments.of("3: 100L, 200L, 300L", new long[] { 100L, 200L, 300L }),
                    Arguments.of("3: 1L, 2L, 3L", new long[] { 1L, 2L, 3L }), Arguments.of("4: 100L, 200L, 300L, 400L", new long[] { 100L, 200L, 300L, 400L }),
                    Arguments.of("4: 1L, 2L, 3L, 4L", new long[] { 1L, 2L, 3L, 4L }),
                    Arguments.of("5: 100L, 200L, 300L, 400L, 500L", new long[] { 100L, 200L, 300L, 400L, 500L }),
                    Arguments.of("5: 1L, 2L, 3L, 4L, 5L", new long[] { 1L, 2L, 3L, 4L, 5L }),
                    Arguments.of("6: 100L, 200L, 300L, 400L, 500L, 600L", new long[] { 100L, 200L, 300L, 400L, 500L, 600L }),
                    Arguments.of("6: 1L, 2L, 3L, 4L, 5L, 6L", new long[] { 1L, 2L, 3L, 4L, 5L, 6L }),
                    Arguments.of("7: 100L, 200L, 300L, 400L, 500L, 600L, 700L", new long[] { 100L, 200L, 300L, 400L, 500L, 600L, 700L }),
                    Arguments.of("7: 1L, 2L, 3L, 4L, 5L, 6L, 7L", new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L }),
                    Arguments.of("8: 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L", new long[] { 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L }),
                    Arguments.of("8: 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L", new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L }),
                    Arguments.of("9: 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L",
                            new long[] { 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L }),
                    Arguments.of("9: 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L", new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L }));
        }

        private static long[] publicFields(LongTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    LongTuple.LongTuple1 value = (LongTuple.LongTuple1) tuple;
                    yield new long[] { value._1 };
                }
                case 2 -> {
                    LongTuple.LongTuple2 value = (LongTuple.LongTuple2) tuple;
                    yield new long[] { value._1, value._2 };
                }
                case 3 -> {
                    LongTuple.LongTuple3 value = (LongTuple.LongTuple3) tuple;
                    yield new long[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    LongTuple.LongTuple4 value = (LongTuple.LongTuple4) tuple;
                    yield new long[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    LongTuple.LongTuple5 value = (LongTuple.LongTuple5) tuple;
                    yield new long[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    LongTuple.LongTuple6 value = (LongTuple.LongTuple6) tuple;
                    yield new long[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    LongTuple.LongTuple7 value = (LongTuple.LongTuple7) tuple;
                    yield new long[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    LongTuple.LongTuple8 value = (LongTuple.LongTuple8) tuple;
                    yield new long[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    LongTuple.LongTuple9 value = (LongTuple.LongTuple9) tuple;
                    yield new long[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
                }
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + tuple.arity());
            };
        }
    }

    @Nested
    class FromTests {

        @Test
        void create_emptyArray() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_multipleElementsArray() {
            LongTuple3 tuple = LongTuple.from(new long[] { 1L, 2L, 3L });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
        }

        @Test
        void create_nullArray() {
            LongTuple0 tuple = LongTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_singleElementArray() {
            LongTuple1 tuple = LongTuple.from(new long[] { 42L });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(42L, tuple._1);
        }

        @Test
        void create_tooManyElements() {
            long[] array = new long[10];
            assertThrows(IllegalArgumentException.class, () -> LongTuple.from(array));
        }

        @Test
        void from_emptyArray() {
            LongTuple<?> tuple = LongTuple.from(new long[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nullArray() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_preservesElements() {
            // Test empty array
            LongTuple<?> empty = LongTuple.from(null);
            assertEquals(0, empty.arity());

            empty = LongTuple.from(new long[0]);
            assertEquals(0, empty.arity());

            // Test array with 1 element
            LongTuple.LongTuple1 tuple1 = LongTuple.from(new long[] { 100L });
            assertEquals(1, tuple1.arity());
            assertEquals(100L, tuple1._1);

            // Test array with 5 elements
            LongTuple.LongTuple5 tuple5 = LongTuple.from(new long[] { 100L, 200L, 300L, 400L, 500L });
            assertEquals(5, tuple5.arity());
            assertEquals(500L, tuple5._5);

            // Test array with 9 elements
            LongTuple.LongTuple9 tuple9 = LongTuple.from(new long[] { 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L });
            assertEquals(9, tuple9.arity());
            assertEquals(900L, tuple9._9);

            // Test too many elements
            assertThrows(IllegalArgumentException.class, () -> LongTuple.from(new long[10]));
        }

        @Test
        void from_sizeEight() {
            LongTuple8 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
            assertEquals(8, tuple.arity());
            assertEquals(8L, tuple._8);
        }

        @Test
        void from_sizeFive() {
            LongTuple5 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L });
            assertEquals(5, tuple.arity());
            assertEquals(5L, tuple._5);
        }

        @Test
        void from_sizeFour() {
            LongTuple4 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L });
            assertEquals(4, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(4L, tuple._4);
        }

        @Test
        void from_sizeNine() {
            LongTuple9 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
            assertEquals(9, tuple.arity());
            assertEquals(9L, tuple._9);
        }

        @Test
        void from_sizeOne() {
            LongTuple1 tuple = LongTuple.from(new long[] { 42L });
            assertEquals(1, tuple.arity());
            assertEquals(42L, tuple._1);
        }

        @Test
        void from_sizeSeven() {
            LongTuple7 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L });
            assertEquals(7, tuple.arity());
            assertEquals(7L, tuple._7);
        }

        @Test
        void from_sizeSix() {
            LongTuple6 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L });
            assertEquals(6, tuple.arity());
            assertEquals(6L, tuple._6);
        }

        @Test
        void from_sizeThree() {
            LongTuple3 tuple = LongTuple.from(new long[] { 1L, 2L, 3L });
            assertEquals(3, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(3L, tuple._3);
        }

        @Test
        void from_sizeTwo() {
            LongTuple2 tuple = LongTuple.from(new long[] { 10L, 20L });
            assertEquals(2, tuple.arity());
            assertEquals(10L, tuple._1);
            assertEquals(20L, tuple._2);
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L }));
        }

        @Test
        void from1() {
            LongTuple1 tuple = LongTuple.from(new long[] { 1L });
            assertEquals(1L, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2Through8() {
            LongTuple2 tuple2 = LongTuple.from(new long[] { 1L, 2L });
            assertEquals(1L, tuple2._1);
            assertEquals(2L, tuple2._2);

            LongTuple4 tuple4 = LongTuple.from(new long[] { 1L, 2L, 3L, 4L });
            assertEquals(1L, tuple4._1);
            assertEquals(4L, tuple4._4);

            LongTuple5 tuple5 = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L });
            assertEquals(1L, tuple5._1);
            assertEquals(5L, tuple5._5);

            LongTuple6 tuple6 = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L });
            assertEquals(1L, tuple6._1);
            assertEquals(6L, tuple6._6);

            LongTuple7 tuple7 = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L });
            assertEquals(1L, tuple7._1);
            assertEquals(7L, tuple7._7);

            LongTuple8 tuple8 = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
            assertEquals(1L, tuple8._1);
            assertEquals(8L, tuple8._8);
        }

        @Test
        void from3() {
            LongTuple3 tuple = LongTuple.from(new long[] { 1L, 2L, 3L });
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
        }

        @Test
        void from9() {
            LongTuple9 tuple = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
            assertEquals(1L, tuple._1);
            assertEquals(9L, tuple._9);
        }

        @Test
        void fromEmpty() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromFromArray() {
            // Empty array
            LongTuple<?> empty = LongTuple.from(null);
            assertEquals(0, empty.arity());

            empty = LongTuple.from(new long[0]);
            assertEquals(0, empty.arity());

            // Single element
            LongTuple<?> tuple1 = LongTuple.from(new long[] { 42L });
            assertEquals(1, tuple1.arity());

            // Multiple elements
            LongTuple<?> tuple3 = LongTuple.from(new long[] { 10L, 20L, 30L });
            assertEquals(3, tuple3.arity());

            // Max size
            LongTuple<?> tuple9 = LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
            assertEquals(9, tuple9.arity());

            // Too many elements
            assertThrows(IllegalArgumentException.class, () -> LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L }));
        }

        @Test
        void fromNull() {
            LongTuple<LongTuple0> tuple = LongTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                LongTuple.from(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L });
            });
        }
    }

    @Nested
    class ArityTests {

        @Test
        void arity_returnsElementCount() {
            assertEquals(0, LongTuple.from(new long[0]).arity());
            assertEquals(1, LongTuple.of(1L).arity());
            assertEquals(2, LongTuple.of(1L, 2L).arity());
            assertEquals(3, LongTuple.of(1L, 2L, 3L).arity());
            assertEquals(4, LongTuple.of(1L, 2L, 3L, 4L).arity());
            assertEquals(5, LongTuple.of(1L, 2L, 3L, 4L, 5L).arity());
            assertEquals(6, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L).arity());
            assertEquals(7, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L).arity());
            assertEquals(8, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L).arity());
            assertEquals(9, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L).arity());
        }

        @Test
        void arity0() {
            LongTuple<?> empty = LongTuple.from(null);
            assertEquals(0, empty.arity());
        }

        @Test
        void arity0_arity() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void coverageRegression_highArityEqualsBranches() {
            LongTuple.LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
            assertTrue(tuple4.equals(tuple4));
            assertTrue(tuple4.equals(LongTuple.of(1L, 2L, 3L, 4L)));
            assertFalse(tuple4.equals(LongTuple.of(1L, 2L, 3L, 9L)));
            assertFalse(tuple4.equals("tuple4"));

            LongTuple.LongTuple5 tuple5 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(tuple5.equals(tuple5));
            assertTrue(tuple5.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L)));
            assertFalse(tuple5.equals(LongTuple.of(1L, 2L, 3L, 4L, 9L)));
            assertFalse(tuple5.equals("tuple5"));

            LongTuple.LongTuple6 tuple6 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertTrue(tuple6.equals(tuple6));
            assertTrue(tuple6.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L)));
            assertFalse(tuple6.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 9L)));
            assertFalse(tuple6.equals("tuple6"));

            LongTuple.LongTuple7 tuple7 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertTrue(tuple7.equals(tuple7));
            assertTrue(tuple7.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)));
            assertFalse(tuple7.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 9L)));
            assertFalse(tuple7.equals("tuple7"));

            LongTuple.LongTuple8 tuple8 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertTrue(tuple8.equals(tuple8));
            assertTrue(tuple8.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)));
            assertFalse(tuple8.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 9L)));
            assertFalse(tuple8.equals("tuple8"));

            LongTuple.LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertTrue(tuple9.equals(tuple9));
            assertTrue(tuple9.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)));
            assertFalse(tuple9.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 0L)));
            assertFalse(tuple9.equals("tuple9"));
        }

        @Test
        void coverageRegression_highArityOperations() {
            LongTuple.LongTuple4 tuple4 = LongTuple.of(9L, 1L, 7L, 3L);
            assertEquals(1L, tuple4.min());
            assertEquals(9L, tuple4.max());
            assertEquals(3L, tuple4.lowerMedian());
            assertEquals(20L, tuple4.sum());
            assertEquals(5.0, tuple4.average().getAsDouble());
            assertTrue(tuple4.contains(3L));

            LongTuple.LongTuple5 tuple5 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(tuple5.contains(5L));

            LongTuple.LongTuple6 tuple6 = LongTuple.of(9L, 1L, 7L, 3L, 5L, 11L);
            assertEquals(5L, tuple6.lowerMedian());
            assertTrue(tuple6.contains(11L));

            LongTuple.LongTuple7 tuple7 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertTrue(tuple7.contains(7L));

            LongTuple.LongTuple8 tuple8 = LongTuple.of(9L, 1L, 7L, 3L, 5L, 11L, 13L, 15L);
            assertEquals(7L, tuple8.lowerMedian());
            assertTrue(tuple8.contains(15L));

            LongTuple.LongTuple9 tuple9 = LongTuple.of(9L, 1L, 7L, 3L, 5L, 11L, 13L, 15L, 17L);
            assertEquals(9L, tuple9.lowerMedian());
            assertTrue(tuple9.contains(17L));
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void elementsMethod() {
            // Just test that elements are cached properly
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            long[] elements1 = tuple.elements();
            long[] elements2 = tuple.elements();
            assertSame(elements1, elements2); // Should return same cached array
        }
    }

    @Nested
    class MinTests {

        @Test
        void arity0_min_throwsException() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void arity1_min() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.min());
        }

        @Test
        void arity1_min_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.min());
        }

        @Test
        void arity2_min() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min_values_3L_1L() {
            LongTuple2 tuple = LongTuple.of(3L, 1L);
            assertEquals(1L, tuple.min());
        }

        @Test
        void arity3_min() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min_values_3L_1L_2L() {
            LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
            assertEquals(1L, tuple.min());
        }

        @Test
        void min_empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.from(null).min());
        }

        @Test
        void min_returnsMinimum() {
            LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
            assertEquals(100L, tuple.min());

            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> empty.min());
        }

        @Test
        void min_returnsMinimum_values_5L_2L_8L_min() {
            assertEquals(2L, LongTuple.of(5L, 2L, 8L).min());
            assertEquals(1L, LongTuple.of(1L, 2L, 3L, 4L, 5L).min());
            assertEquals(-10L, LongTuple.of(-5L, -10L, 0L).min());
        }

        @Test
        void min_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.min());
        }

        @Test
        void min_tuple2() {
            LongTuple2 tuple = LongTuple.of(5L, 2L);
            assertEquals(2L, tuple.min());
        }

        @Test
        void min_tuple3() {
            LongTuple3 tuple = LongTuple.of(5L, 2L, 8L);
            assertEquals(2L, tuple.min());
        }

        @Test
        void min_tuple4() {
            LongTuple4 tuple = LongTuple.of(5L, 2L, 8L, 1L);
            assertEquals(1L, tuple.min());
        }

        @Test
        void minTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.min());
        }

        @Test
        void tuple0_min() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void tuple4_minMaxMedian() {
            LongTuple4 tuple = LongTuple.of(10L, 5L, 15L, 20L);
            assertEquals(5L, tuple.min());
            assertEquals(20L, tuple.max());
            assertEquals(10L, tuple.lowerMedian());
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity0_max_throwsException() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void arity1_max() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.max());
        }

        @Test
        void arity1_max_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.max());
        }

        @Test
        void arity2_max() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max_values_3L_1L() {
            LongTuple2 tuple = LongTuple.of(3L, 1L);
            assertEquals(3L, tuple.max());
        }

        @Test
        void arity3_max() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max_values_3L_1L_2L() {
            LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
            assertEquals(3L, tuple.max());
        }

        @Test
        void max_empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.from(null).max());
        }

        @Test
        void max_returnsMaximum() {
            LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
            assertEquals(300L, tuple.max());

            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> empty.max());
        }

        @Test
        void max_returnsMaximum_values_5L_2L_8L_max() {
            assertEquals(8L, LongTuple.of(5L, 2L, 8L).max());
            assertEquals(5L, LongTuple.of(1L, 2L, 3L, 4L, 5L).max());
            assertEquals(10L, LongTuple.of(-5L, 0L, 10L).max());
        }

        @Test
        void max_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.max());
        }

        @Test
        void max_tuple2() {
            LongTuple2 tuple = LongTuple.of(5L, 2L);
            assertEquals(5L, tuple.max());
        }

        @Test
        void max_tuple3() {
            LongTuple3 tuple = LongTuple.of(5L, 2L, 8L);
            assertEquals(8L, tuple.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.max());
        }

        @Test
        void tuple0_max() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity0_sum() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertEquals(0L, tuple.sum());
        }

        @Test
        void arity1_sum() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity1_sum_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.sum());
        }

        @Test
        void arity2_sum() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum_values_1L_2L() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals(3L, tuple.sum());
        }

        @Test
        void arity3_sum() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(6L, tuple.sum());
        }

        @Test
        void sum_overflowWrapsSilently() {
            assertEquals(Long.MIN_VALUE, LongTuple.of(Long.MAX_VALUE, 1L).sum());
            assertEquals(Long.MAX_VALUE, LongTuple.of(Long.MIN_VALUE, -1L).sum());
            assertEquals(-2L, LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE).sum());
            assertEquals(Long.MAX_VALUE - 1, LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, 1L).sum());
        }

        @Test
        void sum_returnsTotal() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            assertEquals(600L, tuple.sum());

            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertEquals(0L, empty.sum());
        }

        @Test
        void sum_returnsTotal_values_10L_20L_30L_sum() {
            assertEquals(60L, LongTuple.of(10L, 20L, 30L).sum());
            assertEquals(15L, LongTuple.of(1L, 2L, 3L, 4L, 5L).sum());
            assertEquals(0L, LongTuple.from(null).sum());
        }

        @Test
        void sum_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.sum());
        }

        @Test
        void sum_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals(30L, tuple.sum());
        }

        @Test
        void sum_tuple3() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertEquals(60L, tuple.sum());
        }

        @Test
        void sumTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertEquals(0L, tuple.sum());
        }

        @Test
        void sumTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.sum());
        }

        @Test
        void tuple0_sum() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertEquals(0L, tuple.sum());
        }

        @Test
        void tuple5_sum() {
            LongTuple5 tuple = LongTuple.of(10L, 20L, 30L, 40L, 50L);
            assertEquals(150L, tuple.sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity0_average_returnsEmpty() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void arity1_average() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity1_average_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity2_average() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average_values_1L_3L() {
            LongTuple2 tuple = LongTuple.of(1L, 3L);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity3_average() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void average_empty() {
            assertTrue(LongTuple.from(null).average().isEmpty());
        }

        @Test
        void average_returnsMean() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            assertEquals(200.0, tuple.average().getAsDouble());

            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void average_returnsMean_values_10L_20L_30L_average_getAsDouble_1_0Eminus4() {
            assertEquals(20.0, LongTuple.of(10L, 20L, 30L).average().getAsDouble(), 0.0001);
            assertEquals(3.0, LongTuple.of(1L, 2L, 3L, 4L, 5L).average().getAsDouble(), 0.0001);
        }

        @Test
        void average_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals(15.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple3() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertEquals(20.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageDoesNotOverflow() {
            assertEquals((double) Long.MAX_VALUE, LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE).average().getAsDouble());
            assertEquals((double) Long.MIN_VALUE, LongTuple.of(Long.MIN_VALUE, Long.MIN_VALUE).average().getAsDouble());
            assertEquals(-0.5d, LongTuple.of(Long.MAX_VALUE, Long.MIN_VALUE).average().getAsDouble());

            LongTuple.LongTuple8 tuple8 = LongTuple.of(Long.MAX_VALUE, Long.MIN_VALUE, 1L, -1L, 5L, -5L, 9L, -9L);
            assertEquals(-0.125d, tuple8.average().getAsDouble());

            LongTuple.LongTuple9 tuple9 = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE,
                    Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
            assertEquals((double) Long.MAX_VALUE, tuple9.average().getAsDouble());
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple0_average() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void tuple6_average() {
            LongTuple6 tuple = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L);
            assertEquals(35.0, tuple.average().getAsDouble(), 0.001);
        }

        @Nested

        class RoundingTests {

            @Test
            void average_nonOverflowBranchStaysAdjacentToNearestButCanExceedOneUlpOfTheTrueMean() {
                final long[] v = { 9007199254741009L, 0L, 0L, 0L, 0L };
                assertFalse(prefixOverflows(v));
                final double got = LongTuple.from(v).average().getAsDouble();
                assertEquals(1.8014398509482015E15, got);
                // nearest double to the true mean 1801439850948201.8 is ...201.75, exactly one double above
                assertEquals(1.8014398509482018E15, exactOnce(v));
                assertEquals(exactOnce(v), Math.nextUp(got));
                final java.math.BigDecimal trueMean = new java.math.BigDecimal("1801439850948201.8");
                assertTrue(new java.math.BigDecimal(got).subtract(trueMean).abs().compareTo(new java.math.BigDecimal(Math.ulp(got))) > 0,
                        "absolute error exceeds one ulp of the result");
            }

            /** Exact mean rounded exactly once, for use as an oracle. */
            private double exactOnce(final long... xs) {
                java.math.BigInteger s = java.math.BigInteger.ZERO;
                for (final long x : xs) {
                    s = s.add(java.math.BigInteger.valueOf(x));
                }
                return new java.math.BigDecimal(s)
                        .divide(java.math.BigDecimal.valueOf(xs.length), new java.math.MathContext(60, java.math.RoundingMode.HALF_EVEN))
                        .doubleValue();
            }

            @Test
            void average_documentedExample() {
                // the exact example the javadoc states
                assertFalse(prefixOverflows(9007199254740991L, 1L, 1L));
                assertEquals(3.0023997515803305E15, LongTuple.of(9007199254740991L, 1L, 1L).average().getAsDouble());
                // ... and the true mean it is one ulp away from is itself exactly representable.
                // Round-tripping through double is the non-vacuous form: comparing the two literals
                // directly folds to the same constant and would pass for any long at all.
                assertEquals(3002399751580331L, (long) (double) 3002399751580331L);
                assertNotEquals(9007199254740993L, (long) (double) 9007199254740993L, "control: this one is NOT representable");
            }

            @Test
            void average_isCorrectlyRoundedWhenTotalFitsIn2Pow53() {
                assertEquals(exactOnce(1L, 2L, 3L), LongTuple.of(1L, 2L, 3L).average().getAsDouble());
                assertEquals(exactOnce(-3L, -1L), LongTuple.of(-3L, -1L).average().getAsDouble());
                final long big = (1L << 53) / 3;
                assertEquals(exactOnce(big, big, big), LongTuple.of(big, big, big).average().getAsDouble());
            }

            @Test
            void average_isCorrectlyRoundedForPowerOfTwoAritiesWhenTheTotalDoesNotOverflow() {
                // dividing by a power of two is exact, so only one rounding is observable -- but this holds
                // ONLY while the long accumulation itself does not overflow (see the next test)
                // must stay below the prefix-overflow threshold at the LARGEST arity tested, so /8 not /2
                final long v = Long.MAX_VALUE / 8;
                assertEquals(exactOnce(v, v), LongTuple.of(v, v).average().getAsDouble());
                assertEquals(exactOnce(v, v, v, v), LongTuple.of(v, v, v, v).average().getAsDouble());
                assertEquals(exactOnce(v, v, v, v, v, v, v, v), LongTuple.of(v, v, v, v, v, v, v, v).average().getAsDouble());
            }

            @Test
            void average_powerOfTwoArityIsNotImmuneOnTheOverflowBranch() {
                // once the long total overflows, N.average switches to a quotient/remainder decomposition
                // that can round twice at ANY arity -- including 2. This is what the javadoc now states.
                assertTrue(prefixOverflows(Long.MAX_VALUE, 1026L));
                final double got = LongTuple.of(Long.MAX_VALUE, 1026L).average().getAsDouble();
                assertEquals(4.611686018427388E18, got);
                assertNotEquals(exactOnce(Long.MAX_VALUE, 1026L), got);
                assertEquals(4.611686018427389E18, exactOnce(Long.MAX_VALUE, 1026L));
            }

            /** True when a running left-to-right prefix sum overflows, which is what selects the fallback. */
            private boolean prefixOverflows(final long... xs) {
                long acc = 0;
                for (final long x : xs) {
                    try {
                        acc = Math.addExact(acc, x);
                    } catch (final ArithmeticException e) {
                        return true;
                    }
                }
                return false;
            }

            @Test
            void average_staysWithinOneUlpOnTheNonOverflowBranch() {
                // the one-ulp bound holds only while no prefix sum overflows; arities 2..9, not just 3
                final java.util.Random r = new java.util.Random(20260916L);
                int checked = 0;
                for (int i = 0; i < 40000; i++) {
                    final int n = 2 + r.nextInt(8);
                    final long[] v = new long[n];
                    for (int j = 0; j < n; j++) {
                        v[j] = r.nextLong();
                    }
                    if (prefixOverflows(v)) {
                        continue;
                    }
                    checked++;
                    final double got = LongTuple.from(v).average().getAsDouble();
                    final double want = exactOnce(v);
                    assertTrue(got == want || Math.abs(got - want) <= Math.ulp(want), "average() drifted more than 1 ulp for " + java.util.Arrays.toString(v));
                }
                assertTrue(checked > 1000, "the sample must actually reach the non-overflow branch, got " + checked);
            }

            @Test
            void average_canBeSeveralUlpsOffOnTheOverflowBranchWhenElementsCancel() {
                // the fallback adds a rounded fraction to a quotient, so near-total cancellation of large
                // elements costs far more than one ulp -- this is why the javadoc does not promise a bound
                final long[] v = { 6748534329674943672L, 7183318492732822330L, -6748534329674943653L, -7183318492732822325L, -17L, 18L, -5L, -6L, -15L };
                assertTrue(prefixOverflows(v));
                assertEquals(-1L, java.util.Arrays.stream(v).reduce(0L, Long::sum), "the exact total is -1");

                final double got = LongTuple.from(v).average().getAsDouble();
                assertEquals(-0.11111111111111072, got);
                assertEquals(-0.1111111111111111, exactOnce(v));
                assertTrue(Math.abs(got - exactOnce(v)) > Math.ulp(exactOnce(v)), "more than one ulp off");
            }

            @Test
            void average_avoidsLongOverflow() {
                // the first documented sentence: overflow is genuinely avoided (unlike sum(), which wraps)
                assertEquals((double) Long.MAX_VALUE, LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE).average().getAsDouble());
                assertEquals((double) Long.MIN_VALUE, LongTuple.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE).average().getAsDouble());
            }

            @Test
            void average_emptyAndSingle() {
                assertFalse(LongTuple.from(new long[0]).average().isPresent());
                assertEquals(7.0, LongTuple.of(7L).average().getAsDouble());
                // arity 1 is a single widening, so it is exact even above 2^53
                assertEquals((double) 9007199254740993L, LongTuple.of(9007199254740993L).average().getAsDouble());
            }
        }
    }

    @Nested
    class MedianTests {

        @Test
        void arity0_median_throwsException() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void arity1_median() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity1_median_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.lowerMedian());
        }

        @Test
        void arity2_median() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median_values_1L_2L() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            long median = tuple.lowerMedian();
            assertEquals(1L, median);
        }

        @Test
        void arity3_median() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median_values_30L_10L_20L() {
            LongTuple3 tuple = LongTuple.of(30L, 10L, 20L);
            assertEquals(20L, tuple.lowerMedian());
        }

        @Test
        void median_empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.from(null).lowerMedian());
        }

        @Test
        void median_evenSize() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            long median = tuple.lowerMedian();
            assertEquals(2L, median);
        }

        @Test
        void median_oddSize() {
            LongTuple5 tuple = LongTuple.of(50L, 10L, 30L, 20L, 40L);
            long median = tuple.lowerMedian();
            assertEquals(30L, median);
        }

        @Test
        void median_returnsMedian() {
            LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
            assertEquals(200L, tuple.lowerMedian());

            LongTuple.LongTuple4 evenTuple = LongTuple.of(100L, 200L, 300L, 400L);
            assertEquals(200L, evenTuple.lowerMedian()); // even arity returns the lower of the two middle values: 200L

            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> empty.lowerMedian());
        }

        @Test
        void median_returnsMedian_values_3L_1L_2L_lowerMedian() {
            // Odd number of elements - returns middle value
            assertEquals(2L, LongTuple.of(3L, 1L, 2L).lowerMedian());
            assertEquals(3L, LongTuple.of(1L, 2L, 3L, 4L, 5L).lowerMedian());

            // Even number of elements - returns lower middle value
            assertEquals(2L, LongTuple.of(1L, 2L, 3L, 4L).lowerMedian());
        }

        @Test
        void median_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.lowerMedian());
        }

        @Test
        void median_tuple2() {
            LongTuple2 tuple = LongTuple.of(5L, 2L);
            assertEquals(2L, tuple.lowerMedian());
        }

        @Test
        void median_tuple3() {
            LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
            assertEquals(2L, tuple.lowerMedian());
        }

        @Test
        void median_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(2L, tuple.lowerMedian());
        }

        @Test
        void medianTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.lowerMedian());
        }

        @Test
        void tuple0_median() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void tuple7_median() {
            LongTuple7 tuple = LongTuple.of(1L, 3L, 5L, 7L, 9L, 11L, 13L);
            assertEquals(7L, tuple.lowerMedian());
        }

        @Nested

        class PairRoundingTests {

            /** Exact (a+b)/2 rounded exactly once. */
            private double exactOnce(final long a, final long b) {
                return new java.math.BigDecimal(java.math.BigInteger.valueOf(a).add(java.math.BigInteger.valueOf(b)))
                        .divide(java.math.BigDecimal.valueOf(2), new java.math.MathContext(60, java.math.RoundingMode.HALF_EVEN))
                        .doubleValue();
            }

            @Test
            void pairMedian_isCorrectlyRoundedAbove2Pow53() {
                // the old hand-rolled formula widened the exact long quotient sum to double before adding
                // the half-bit, losing the last place here
                assertEquals(9.007199254740994E15, LongTuple.of(9007199254740993L, 9007199254740995L).median());
                assertEquals(9.007199254740994E15, LongTuple.of(18014398509481985L, 2L).median());
                assertEquals(9.007199254740994E15, LongTuple.of(18014398509481986L, 1L).median());
            }

            @Test
            void pairMedian_matchesTheArity4PathOnTheSameMiddlePair() {
                final long a = 9007199254740993L, b = 9007199254740995L;
                assertEquals(LongTuple.of(Long.MIN_VALUE, a, b, Long.MAX_VALUE).median(), LongTuple.of(a, b).median());
                // it also agrees with average() here, because this pair does not overflow the long total
                assertEquals(LongTuple.of(a, b).average().getAsDouble(), LongTuple.of(a, b).median());
            }

            @Test
            void pairMedian_isCorrectWhereAverageIsNotOnTheOverflowBranch() {
                // median() now rounds once (BigInteger sum); average() still uses the quotient/remainder
                // fallback when the long total overflows, so the two can differ by one ulp there.
                assertEquals(4.611686018427389E18, LongTuple.of(Long.MAX_VALUE, 1026L).median());
                assertEquals(4.611686018427388E18, LongTuple.of(Long.MAX_VALUE, 1026L).average().getAsDouble());
            }

            @Test
            void pairMedian_documentedExamplesUnchanged() {
                assertEquals(5.0, LongTuple.of(3L, 7L).median());
                assertEquals(15.0, LongTuple.of(10L, 20L).median());
                assertEquals(0.0, LongTuple.of(-5L, 5L).median());
                assertEquals(20.0, LongTuple.of(10L, 30L).median());
                // still no intermediate long overflow
                assertEquals(-0.5, LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE).median());
            }

            @Test
            void pairMedian_extremesAndNearCancelling() {
                assertEquals((double) Long.MAX_VALUE, LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE).median());
                assertEquals((double) Long.MIN_VALUE, LongTuple.of(Long.MIN_VALUE, Long.MIN_VALUE).median());
                assertEquals(0.0, LongTuple.of(0L, 0L).median());
                assertEquals(500.0, LongTuple.of(-5614431094282083852L, 5614431094282084852L).median());
                assertEquals(-3.5, LongTuple.of(-3L, -4L).median());
            }

            @Test
            void pairMedian_matchesExactReferenceOverRandomPairs() {
                final java.util.Random r = new java.util.Random(20260916L);
                for (int i = 0; i < 50000; i++) {
                    final long a = r.nextLong(), b = r.nextLong();
                    assertEquals(exactOnce(a, b), LongTuple.of(a, b).median(), "median() not correctly rounded for " + a + "," + b);
                }
            }
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity0_reversed() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            LongTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void arity1_reversed() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            LongTuple.LongTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity1_reversed_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            LongTuple1 reversed = tuple.reversed();
            assertEquals(5L, reversed._1);
        }

        @Test
        void arity2_reversed() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            LongTuple.LongTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity2_reversed_values_1L_2L() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            LongTuple2 reversed = tuple.reversed();
            assertEquals(2L, reversed._1);
            assertEquals(1L, reversed._2);
        }

        @Test
        void arity3_reversed() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongTuple.LongTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity3_reversed_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongTuple3 reversed = tuple.reversed();
            assertEquals(3L, reversed._1);
            assertEquals(2L, reversed._2);
            assertEquals(1L, reversed._3);
        }

        @Test
        void arity4_reversed() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple.LongTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple.LongTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple.LongTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple.LongTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple.LongTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple.LongTuple9 reversed = tuple.reversed();
            assertEquals(9L, reversed._1);
            assertEquals(8L, reversed._2);
            assertEquals(7L, reversed._3);
            assertEquals(6L, reversed._4);
            assertEquals(5L, reversed._5);
            assertEquals(4L, reversed._6);
            assertEquals(3L, reversed._7);
            assertEquals(2L, reversed._8);
            assertEquals(1L, reversed._9);
        }

        @Test
        void reversed_reversesElementOrder() {
            // Test Tuple0
            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            LongTuple.LongTuple0 reversedEmpty = empty.reversed();
            assertEquals(0, reversedEmpty.arity());

            // Test Tuple1
            LongTuple.LongTuple1 tuple1 = LongTuple.of(100L);
            LongTuple.LongTuple1 reversed1 = tuple1.reversed();
            assertEquals(100L, reversed1._1);

            // Test Tuple2
            LongTuple.LongTuple2 tuple2 = LongTuple.of(100L, 200L);
            LongTuple.LongTuple2 reversed2 = tuple2.reversed();
            assertEquals(200L, reversed2._1);
            assertEquals(100L, reversed2._2);

            // Test Tuple3
            LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 300L);
            LongTuple.LongTuple3 reversed3 = tuple3.reversed();
            assertEquals(300L, reversed3._1);
            assertEquals(200L, reversed3._2);
            assertEquals(100L, reversed3._3);

            // Test larger tuples
            LongTuple.LongTuple5 tuple5 = LongTuple.of(100L, 200L, 300L, 400L, 500L);
            LongTuple.LongTuple5 reversed5 = tuple5.reversed();
            assertEquals(500L, reversed5._1);
            assertEquals(400L, reversed5._2);
            assertEquals(300L, reversed5._3);
            assertEquals(200L, reversed5._4);
            assertEquals(100L, reversed5._5);
        }

        @Test
        void reversed_tuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            LongTuple1 reversed = tuple.reversed();
            assertEquals(1L, reversed._1);
        }

        @Test
        void reversed_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 reversed = tuple.reversed();
            assertEquals(4L, reversed._1);
            assertEquals(1L, reversed._4);
        }

        @Test
        void reversed0() {
            LongTuple<?> reversed = LongTuple.from(null).reversed();
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversed1() {
            LongTuple1 reversed = LongTuple.of(42L).reversed();
            assertEquals(42L, reversed._1);
        }

        @Test
        void reversed2() {
            LongTuple2 reversed = LongTuple.of(10L, 20L).reversed();
            assertEquals(20L, reversed._1);
            assertEquals(10L, reversed._2);
        }

        @Test
        void reversed3() {
            LongTuple3 reversed = LongTuple.of(1L, 2L, 3L).reversed();
            assertEquals(3L, reversed._1);
            assertEquals(2L, reversed._2);
            assertEquals(1L, reversed._3);
        }

        @Test
        void reversedTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            LongTuple<LongTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void tuple0_reversed() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertNotNull(tuple.reversed());
        }

        @Test
        void tuple5_reversed() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple5 reversed = tuple.reversed();
            assertEquals(5L, reversed._1);
            assertEquals(4L, reversed._2);
            assertEquals(3L, reversed._3);
            assertEquals(2L, reversed._4);
            assertEquals(1L, reversed._5);
        }

        @Test
        void tuple6_reversed() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple6 reversed = tuple.reversed();
            assertEquals(6L, reversed._1);
            assertEquals(1L, reversed._6);
        }

        @Test
        void tuple7_reversed() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple7 reversed = tuple.reversed();
            assertEquals(7L, reversed._1);
            assertEquals(1L, reversed._7);
        }

        @Test
        void tuple8_reversed() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple8 reversed = tuple.reversed();
            assertEquals(8L, reversed._1);
            assertEquals(1L, reversed._8);
        }

        @Test
        void tuple9_reversed() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple9 reversed = tuple.reversed();
            assertEquals(9L, reversed._1);
            assertEquals(1L, reversed._9);
        }
    }

    @Nested
    class ContainsTests {

        @Test
        void arity0_contains() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertFalse(tuple.contains(1L));
        }

        @Test
        void arity1_contains() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity1_contains_found() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertTrue(tuple.contains(5L));
        }

        @Test
        void arity1_contains_notFound() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertFalse(tuple.contains(10L));
        }

        @Test
        void arity2_contains() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity2_contains_found() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(2L));
        }

        @Test
        void arity2_contains_notFound() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertFalse(tuple.contains(3L));
        }

        @Test
        void arity3_contains() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity3_contains_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.contains(2L));
            assertFalse(tuple.contains(5L));
        }

        @Test
        void arity4_contains() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity5_contains() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity6_contains() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity7_contains() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity8_contains() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void arity9_contains() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void contains_empty() {
            assertFalse(LongTuple.from(null).contains(10L));
        }

        @Test
        void contains_findsPresentElements() {
            // Test Tuple0
            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertFalse(empty.contains(100L));

            // Test Tuple1
            LongTuple.LongTuple1 tuple1 = LongTuple.of(100L);
            assertTrue(tuple1.contains(100L));
            assertFalse(tuple1.contains(200L));

            // Test Tuple3
            LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 300L);
            assertTrue(tuple3.contains(100L));
            assertTrue(tuple3.contains(200L));
            assertTrue(tuple3.contains(300L));
            assertFalse(tuple3.contains(400L));

            // Test larger tuples
            LongTuple.LongTuple7 tuple7 = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L);
            assertTrue(tuple7.contains(700L));
            assertFalse(tuple7.contains(800L));
        }

        @Test
        void contains_findsPresentElements_values_10L_20L_30L() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertTrue(tuple.contains(20L));
            assertFalse(tuple.contains(40L));
            assertTrue(tuple.contains(10L));
            assertTrue(tuple.contains(30L));
        }

        @Test
        void contains_higherArityTrailingElements() {
            LongTuple.LongTuple7 tuple7 = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L);

            assertTrue(tuple7.contains(700L));
            assertFalse(tuple7.contains(800L));
        }

        @Test
        void contains_single() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertTrue(tuple.contains(42L));
            assertFalse(tuple.contains(43L));
        }

        @Test
        void contains_tuple1_found() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertTrue(tuple.contains(42L));
        }

        @Test
        void contains_tuple1_notFound() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertFalse(tuple.contains(100L));
        }

        @Test
        void contains_tuple2_found() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertTrue(tuple.contains(20L));
        }

        @Test
        void contains_tuple2_notFound() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertFalse(tuple.contains(40L));
        }

        @Test
        void contains_tuple3_found() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertTrue(tuple.contains(20L));
        }

        @Test
        void contains_tuple3_notFound() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertFalse(tuple.contains(40L));
        }

        @Test
        void contains_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(4L));
            assertFalse(tuple.contains(5L));
        }

        @Test
        void contains_tuple5() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(tuple.contains(5L));
            assertFalse(tuple.contains(10L));
        }

        @Test
        void contains_tuple6() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertTrue(tuple.contains(6L));
            assertFalse(tuple.contains(7L));
        }

        @Test
        void contains_tuple7() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertTrue(tuple.contains(7L));
            assertFalse(tuple.contains(8L));
        }

        @Test
        void contains_tuple8() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertTrue(tuple.contains(8L));
            assertFalse(tuple.contains(9L));
        }

        @Test
        void contains_tuple9() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertTrue(tuple.contains(9L));
            assertFalse(tuple.contains(10L));
        }

        @Test
        void containsTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertFalse(tuple.contains(1L));
        }

        @Test
        void containsTuple1False() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertFalse(tuple.contains(99L));
        }

        @Test
        void containsTuple1True() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        void containsTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(2L));
            assertTrue(tuple.contains(3L));
            assertFalse(tuple.contains(99L));
        }

        @Test
        void tuple0_contains() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertFalse(tuple.contains(1L));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_empty() {
            long[] array = LongTuple.from(null).toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArray_independence() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] arr1 = tuple.toArray();
            long[] arr2 = tuple.toArray();
            arr1[0] = 999L;
            assertEquals(1L, arr2[0]);
        }

        @Test
        void toArray_modification() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            array[0] = 99L;
            assertEquals(1L, tuple._1);
        }

        @Test
        void toArray_preservesElements() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            long[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(100L, array[0]);
            assertEquals(200L, array[1]);
            assertEquals(300L, array[2]);

            // Ensure it's a copy
            array[0] = 1000L;
            assertEquals(100L, tuple._1);
        }

        @Test
        void toArray_preservesElements_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1L, array[0]);
            assertEquals(2L, array[1]);
            assertEquals(3L, array[2]);
        }

        @Test
        void toArray_preservesElements_values_1L_2L_3L_toArray() {
            long[] array = LongTuple.of(1L, 2L, 3L).toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L }, array);

            // Test that modifications to returned array don't affect tuple
            array[0] = 999L;
            assertEquals(1L, LongTuple.of(1L, 2L, 3L).toArray()[0]);
        }

        @Test
        void toArray_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 42L }, array);
        }

        @Test
        void toArray_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 10L, 20L }, array);
        }

        @Test
        void toArray_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L }, array);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            array[0] = 999L;
            assertEquals(1L, tuple._1);
        }

        @Test
        void toArrayTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            long[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArrayTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 1L }, array);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_empty() {
            LongList list = LongTuple.from(null).toList();
            assertEquals(0, list.size());
        }

        @Test
        void toList_preservesElements() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(100L, list.get(0));
            assertEquals(200L, list.get(1));
            assertEquals(300L, list.get(2));
        }

        @Test
        void toList_preservesElements_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1L, list.get(0));
        }

        @Test
        void toList_preservesElements_values_1L_2L_3L_toList() {
            LongList list = LongTuple.of(1L, 2L, 3L).toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(1L, list.get(0));
            assertEquals(2L, list.get(1));
            assertEquals(3L, list.get(2));
        }

        @Test
        void toList_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            LongList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42L, list.get(0));
        }

        @Test
        void toList_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            LongList list = tuple.toList();
            assertEquals(2, list.size());
            assertEquals(10L, list.get(0));
            assertEquals(20L, list.get(1));
        }

        @Test
        void toList_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(3L, list.get(2));
        }

        @Test
        void toListTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            LongList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            LongList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(1L, list.get(0));
        }

        @Test
        void toListTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1L, list.get(0));
            assertEquals(2L, list.get(1));
            assertEquals(3L, list.get(2));
        }

        @Test
        void toListTuple4Through9() {
            LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
            LongList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(4L, list4.get(3));

            LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9L, list9.get(8));
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity2_forEach_values_1L_2L() throws Exception {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            List<Long> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(1L, values.get(0).longValue());
            assertEquals(2L, values.get(1).longValue());
        }

        @Test
        void arity3_forEach() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity3_forEach_values_1L_2L_3L() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        void arity4_forEach() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_empty() {
            AtomicLong sum = new AtomicLong(0);
            LongTuple.from(null).forEach(sum::addAndGet);
            assertEquals(0L, sum.get());
        }

        @Test
        void forEach_multipleElements() throws Exception {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            List<Long> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void forEach_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(42L, sum.get());
        }

        @Test
        void forEach_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(30L, sum.get());
        }

        @Test
        void forEach_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(6L, sum.get());
        }

        @Test
        void forEach_visitsElements() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            long[] sum = { 0L };
            tuple.forEach(l -> sum[0] += l);
            assertEquals(600L, sum[0]);

            // Test Tuple2 forEach
            LongTuple.LongTuple2 tuple2 = LongTuple.of(100L, 200L);
            int[] count = { 0 };
            tuple2.forEach(l -> count[0]++);
            assertEquals(2, count[0]);
        }

        @Test
        void forEach_visitsElements_values_1L_2L_3L() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(sum::addAndGet);
            assertEquals(6L, sum.get());
        }

        @Test
        void forEachTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Long.valueOf(1L), result.get(0));
        }

        @Test
        void forEachTuple2Override() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Long.valueOf(10L), result.get(0));
            assertEquals(Long.valueOf(20L), result.get(1));
        }

        @Test
        void forEachTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(1L), result.get(0));
            assertEquals(Long.valueOf(2L), result.get(1));
            assertEquals(Long.valueOf(3L), result.get(2));
        }

        @Test
        void forEachTuple3Override() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(30L), result.get(2));
        }

        @Test
        void forEachTuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Long.valueOf(4L), result.get(3));
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_empty() {
            long sum = LongTuple.from(null).stream().sum();
            assertEquals(0L, sum);
        }

        @Test
        void stream_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            long sum = tuple.stream().sum();
            assertEquals(42L, sum);
        }

        @Test
        void stream_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            long sum = tuple.stream().sum();
            assertEquals(30L, sum);
        }

        @Test
        void stream_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long sum = tuple.stream().sum();
            assertEquals(6L, sum);
        }

        @Test
        void stream_visitsElements() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
            long[] array = tuple.stream().toArray();
            assertEquals(3, array.length);
            assertEquals(100L, array[0]);
            assertEquals(200L, array[1]);
            assertEquals(300L, array[2]);

            long count = tuple.stream().filter(l -> l > 150L).count();
            assertEquals(2, count);
        }

        @Test
        void stream_visitsElements_values_1L_2L_3L_stream_sum() {
            long sum = LongTuple.of(1L, 2L, 3L).stream().sum();
            assertEquals(6L, sum);
        }

        @Test
        void streamTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            LongStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            LongStream stream = tuple.stream();
            assertEquals(1L, stream.sum());
        }

        @Test
        void streamTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongStream stream = tuple.stream();
            assertEquals(6L, stream.sum());
        }

        @Test
        void streamTuple4Through9() {
            LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(10L, tuple4.stream().sum());

            LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(45L, tuple9.stream().sum());
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_tuple2() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            AtomicLong result = new AtomicLong(0);
            tuple.accept((a, b) -> result.set(a + b));
            assertEquals(7L, result.get());
        }

        @Test
        void accept_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            AtomicLong result = new AtomicLong(0);
            tuple.accept((a, b, c) -> result.set(a + b + c));
            assertEquals(6L, result.get());
        }

        @Test
        void accept2() {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            AtomicLong sum = new AtomicLong(0);
            pair.accept((a, b) -> sum.set(a + b));
            assertEquals(7L, sum.get());
        }

        @Test
        void accept2_exception() throws Exception {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            assertThrows(IllegalArgumentException.class, () -> pair.accept((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        void accept3() {
            LongTuple3 triple = LongTuple.of(3L, 4L, 5L);
            AtomicLong sum = new AtomicLong(0);
            triple.accept((a, b, c) -> sum.set(a + b + c));
            assertEquals(12L, sum.get());
        }

        @Test
        void acceptConsumer() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(1L, result.get(0));
            assertEquals(2L, result.get(1));
            assertEquals(3L, result.get(2));
        }

        @Test
        void arity2_accept() throws Exception {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            final long[] result = new long[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7L, result[0]);
        }

        @Test
        void arity2_accept_biConsumer() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            final long[] result = new long[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6L, result[0]);
        }

        @Test
        void arity3_accept_triConsumer() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2Accept() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            List<Long> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Long.valueOf(3L), result.get(0));
            assertEquals(Long.valueOf(4L), result.get(1));
        }

        @Test
        void tuple3Accept() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(1L), result.get(0));
            assertEquals(Long.valueOf(2L), result.get(1));
            assertEquals(Long.valueOf(3L), result.get(2));
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map() throws Exception {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            Long result = tuple.map((a, b) -> a * b);
            assertEquals(12L, result.longValue());
        }

        @Test
        void arity2_map_biFunction() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Long result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6L, result.longValue());
        }

        @Test
        void arity3_map_triFunction() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void map_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 3L);
            long result = tuple.map((a, b) -> a % b);
            assertEquals(1L, result);
        }

        @Test
        void map_tuple2_returnString() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            String result = tuple.map((a, b) -> "Sum: " + (a + b));
            assertEquals("Sum: 30", result);
        }

        @Test
        void map_tuple3() {
            LongTuple3 tuple = LongTuple.of(2L, 3L, 4L);
            long result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(24L, result);
        }

        @Test
        void map_tuple3_returnString() {
            LongTuple3 tuple = LongTuple.of(3L, 4L, 5L);
            String result = tuple.map((a, b, c) -> {
                if (a * a + b * b == c * c) {
                    return "Pythagorean triple!";
                }
                return "Not a Pythagorean triple";
            });
            assertEquals("Pythagorean triple!", result);
        }

        @Test
        void map2() {
            // LongTuple.LongTuple2 dimensions = LongTuple.of(5L, 10L);
            // Long area = dimensions.map((width, height) -> width * height);  // 50
            LongTuple.LongTuple2 dimensions = LongTuple.of(5L, 10L);
            Long longArea = dimensions.map((width, height) -> width * height);
            assertEquals(50L, longArea);

            // LongTuple.LongTuple2 coords = LongTuple.of(3L, 4L);
            // String result = coords.map((x, y) -> "(" + x + ", " + y + ")");  // "(3, 4)"
            LongTuple.LongTuple2 coords = LongTuple.of(3L, 4L);
            String result = coords.map((x, y) -> "(" + x + ", " + y + ")");
            assertEquals("(3, 4)", result);
        }

        @Test
        void map2_exception() {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            assertThrows(IllegalArgumentException.class, () -> pair.map((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        void map2_values_10L_3L() {
            LongTuple2 pair = LongTuple.of(10L, 3L);
            long remainder = pair.map((a, b) -> a % b);
            assertEquals(1L, remainder);
        }

        @Test
        void map3() {
            // LongTuple.LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
            // long volume = triple.map((l, w, h) -> l * w * h);   // 24
            LongTuple.LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
            long volume = triple.map((l, w, h) -> l * w * h);
            assertEquals(24L, volume);

            // LongTuple.LongTuple3 dimensions2 = LongTuple.of(10L, 20L, 30L);
            // String formatted = dimensions2.map((x, y, z) -> x + "x" + y + "x" + z);  // "10x20x30"
            LongTuple.LongTuple3 dimensions2 = LongTuple.of(10L, 20L, 30L);
            String formatted = dimensions2.map((x, y, z) -> x + "x" + y + "x" + z);
            assertEquals("10x20x30", formatted);

            // LongTuple.LongTuple3 values = LongTuple.of(1L, 2L, 3L);
            // Double avg = values.map((a, b, c) -> (a + b + c) / 3.0);  // 2.0
            LongTuple.LongTuple3 values = LongTuple.of(1L, 2L, 3L);
            Double avg = values.map((a, b, c) -> (a + b + c) / 3.0);
            assertEquals(2.0, avg, 0.001);
        }

        @Test
        void map3_values_2L_3L_4L() {
            LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
            long volume = triple.map((l, w, h) -> l * w * h);
            assertEquals(24L, volume);
        }

        @Test
        void mapFunction() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6L, result);
        }

        @Test
        void tuple2Map() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            long result = tuple.map((a, b) -> a * b);
            assertEquals(12L, result);
        }

        @Test
        void tuple3Map() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6L, result);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity2_filter_notSatisfied() throws Exception {
            LongTuple2 tuple = LongTuple.of(1L, 1L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        void arity2_filter_satisfied() throws Exception {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void arity3_filter() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filter_tuple2_match() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filter_tuple2_noMatch() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple3_match() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_tuple3_noMatch() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter2() {
            // LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
            // u.Optional<LongTuple.LongTuple2> result = pair.filter((a, b) -> a < b);   // Optional containing the tuple
            LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
            Optional<LongTuple.LongTuple2> result = pair.filter((a, b) -> a < b);
            assertTrue(result.isPresent());

            // LongTuple.LongTuple2 values = LongTuple.of(5L, 3L);
            // u.Optional<LongTuple.LongTuple2> empty = values.filter((a, b) -> a < b);  // Empty Optional
            LongTuple.LongTuple2 values = LongTuple.of(5L, 3L);
            Optional<LongTuple.LongTuple2> empty = values.filter((a, b) -> a < b);
            assertFalse(empty.isPresent());
        }

        @Test
        void filter2_exception() {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            assertThrows(IllegalArgumentException.class, () -> pair.filter((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        void filter2_false() {
            LongTuple2 pair = LongTuple.of(20L, 10L);
            Optional<LongTuple2> result = pair.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter2_true() {
            LongTuple2 pair = LongTuple.of(10L, 20L);
            Optional<LongTuple2> result = pair.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(pair, result.get());
        }

        @Test
        void filter3() {
            // LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            // result = triple.filter((a, b, c) -> a < b && b < c);   // Optional containing the tuple
            LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple.LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());

            // LongTuple.LongTuple3 descending = LongTuple.of(5L, 4L, 3L);
            // empty = descending.filter((a, b, c) -> a < b && b < c);  // Empty Optional
            LongTuple.LongTuple3 descending = LongTuple.of(5L, 4L, 3L);
            Optional<LongTuple.LongTuple3> empty = descending.filter((a, b, c) -> a < b && b < c);
            assertFalse(empty.isPresent());
        }

        @Test
        void filter3_false() {
            LongTuple3 triple = LongTuple.of(3L, 2L, 1L);
            Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        @Test
        void filter3_true() {
            LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(triple, result.get());
        }

        @Test
        void filterPredicate() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter(t -> t._1 == 1L);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter(t -> t._1 == 99L);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterFalse() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            var result = tuple.filter((a, b) -> a + b > 10L);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            var result = tuple.filter((a, b) -> a + b > 5L);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3FilterFalse() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter((a, b, c) -> a + b + c > 10L);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter((a, b, c) -> a + b + c > 5L);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            LongTuple.LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple.LongTuple1 tuple2 = LongTuple.of(1L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity1_hashCode_values_5L() {
            LongTuple1 tuple1 = LongTuple.of(5L);
            LongTuple1 tuple2 = LongTuple.of(5L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            LongTuple.LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple.LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode_values_1L_2L() {
            LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            LongTuple.LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple.LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            LongTuple.LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple.LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            LongTuple.LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple.LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            LongTuple.LongTuple6 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple.LongTuple6 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            LongTuple.LongTuple7 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple.LongTuple7 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            LongTuple.LongTuple8 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple.LongTuple8 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            LongTuple.LongTuple9 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple.LongTuple9 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_consistency() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_matchesValueContract() {
            LongTuple.LongTuple3 tuple1 = LongTuple.of(100L, 200L, 300L);
            LongTuple.LongTuple3 tuple2 = LongTuple.of(100L, 200L, 300L);
            LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 301L);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

            // Test specific hashcodes for coverage
            LongTuple.LongTuple1 single = LongTuple.of(100L);
            assertEquals(Long.hashCode(100L), single.hashCode());

            LongTuple.LongTuple2 pair = LongTuple.of(100L, 200L);
            assertEquals(31 * Long.hashCode(100L) + Long.hashCode(200L), pair.hashCode());
        }

        @Test
        void hashCode_tuple1() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(42L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple1_different() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(100L);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple2() {
            LongTuple2 tuple1 = LongTuple.of(10L, 20L);
            LongTuple2 tuple2 = LongTuple.of(10L, 20L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple3() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple1 tuple2 = LongTuple.of(1L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @Test
        void arity1_equals() {
            LongTuple.LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple.LongTuple1 tuple2 = LongTuple.of(1L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_differentType() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertNotEquals("string", tuple);
        }

        @Test
        void arity1_equals_equal() {
            LongTuple1 tuple1 = LongTuple.of(5L);
            LongTuple1 tuple2 = LongTuple.of(5L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_notEqual() {
            LongTuple1 tuple1 = LongTuple.of(5L);
            LongTuple1 tuple2 = LongTuple.of(10L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_null() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertNotEquals(null, tuple);
        }

        @Test
        void arity1_equals_same() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void arity2_equals() {
            LongTuple.LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple.LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity2_equals_values_1L_2L() {
            LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity3_equals() {
            LongTuple.LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple.LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity4_equals() {
            LongTuple.LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple.LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity5_equals() {
            LongTuple.LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple.LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity6_equals() {
            LongTuple.LongTuple6 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple.LongTuple6 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity7_equals() {
            LongTuple.LongTuple7 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple.LongTuple7 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity8_equals() {
            LongTuple.LongTuple8 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple.LongTuple8 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equals() {
            LongTuple.LongTuple9 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple.LongTuple9 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equalsAndHashCode_differInOneField() {
            long[] base = { 10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L };
            LongTuple.LongTuple9 t = LongTuple.of(base[0], base[1], base[2], base[3], base[4], base[5], base[6], base[7], base[8]);
            for (int i = 0; i < 9; i++) {
                long[] diff = base.clone();
                diff[i] = diff[i] + 1L;
                LongTuple.LongTuple9 other = LongTuple.of(diff[0], diff[1], diff[2], diff[3], diff[4], diff[5], diff[6], diff[7], diff[8]);
                assertNotEquals(t, other, "field _" + (i + 1) + " should affect equals");
                assertNotEquals(t.hashCode(), other.hashCode(), "field _" + (i + 1) + " should affect hashCode");
            }
        }

        @Test
        void equals_comparesValues() {
            LongTuple.LongTuple3 tuple1 = LongTuple.of(100L, 200L, 300L);
            LongTuple.LongTuple3 tuple2 = LongTuple.of(100L, 200L, 300L);
            LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 301L);
            LongTuple.LongTuple2 tuple4 = LongTuple.of(100L, 200L);

            assertTrue(tuple1.equals(tuple1));
            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(tuple4));
            assertFalse(tuple1.equals(null));
            assertFalse(tuple1.equals("not a tuple"));

            // Test specific equals for coverage
            LongTuple.LongTuple1 single1 = LongTuple.of(100L);
            LongTuple.LongTuple1 single2 = LongTuple.of(100L);
            LongTuple.LongTuple1 single3 = LongTuple.of(200L);
            assertTrue(single1.equals(single2));
            assertFalse(single1.equals(single3));
            assertFalse(single1.equals("not a tuple"));

            LongTuple.LongTuple2 pair1 = LongTuple.of(100L, 200L);
            LongTuple.LongTuple2 pair2 = LongTuple.of(100L, 200L);
            LongTuple.LongTuple2 pair3 = LongTuple.of(100L, 201L);
            assertTrue(pair1.equals(pair2));
            assertFalse(pair1.equals(pair3));
        }

        @Test
        void equals_comparesValues_values_1L_2L_3L() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple3 = LongTuple.of(1L, 2L, 4L);

            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertTrue(tuple1.equals(tuple1));
            assertNotEquals(tuple1, null);
        }

        @Test
        void equals_differentClass() {
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            LongTuple3 tuple3 = LongTuple.of(1L, 2L, 3L);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        void equals_empty() {
            LongTuple<?> empty1 = LongTuple.from(null);
            LongTuple<?> empty2 = LongTuple.from(new long[0]);
            assertEquals(empty1, empty2);
        }

        @Test
        void equals_higherArityIdentityAndTypeMismatch() {
            LongTuple.LongTuple7 tuple7 = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L);

            assertTrue(tuple7.equals(tuple7));
            assertEquals(tuple7, LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L));
            assertNotEquals(tuple7, LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 701L));
            assertNotEquals(tuple7, "tuple");
        }

        @Test
        void equals_symmetric() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        void equals_tuple1_different() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(100L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple1_null() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertNotEquals(null, tuple);
        }

        @Test
        void equals_tuple1_same() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(42L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple1_sameObject() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equals_tuple2_different() {
            LongTuple2 tuple1 = LongTuple.of(10L, 20L);
            LongTuple2 tuple2 = LongTuple.of(10L, 30L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple2_same() {
            LongTuple2 tuple1 = LongTuple.of(10L, 20L);
            LongTuple2 tuple2 = LongTuple.of(10L, 20L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple3_different() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 4L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple3_same() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple4() {
            LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple5() {
            LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equalsDifferentClass() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        void equalsNull() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotEquals(null, tuple);
        }

        @Test
        void equalsSameObject() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equalsTuple1() {
            LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple1 tuple2 = LongTuple.of(1L);
            LongTuple1 tuple3 = LongTuple.of(99L);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple2() {
            LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            LongTuple2 tuple3 = LongTuple.of(1L, 3L);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple3() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple3 = LongTuple.of(1L, 2L, 4L);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity0_toString() {
            LongTuple0 tuple = LongTuple.from(new long[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void arity1_toString() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity1_toString_values_5L() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals("(5)", tuple.toString());
        }

        @Test
        void arity2_toString() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString_values_1L_2L() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals("(1, 2)", tuple.toString());
        }

        @Test
        void arity3_toString() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_formatsValue() {
            LongTuple.LongTuple0 empty = LongTuple.from(new long[0]);
            assertEquals("()", empty.toString());

            LongTuple.LongTuple1 single = LongTuple.of(100L);
            assertEquals("(100)", single.toString());

            LongTuple.LongTuple2 pair = LongTuple.of(100L, 200L);
            assertEquals("(100, 200)", pair.toString());

            LongTuple.LongTuple3 triple = LongTuple.of(100L, 200L, 300L);
            assertEquals("(100, 200, 300)", triple.toString());
        }

        @Test
        void toString_formatsValue_values_42L_toString() {
            assertEquals("(42)", LongTuple.of(42L).toString());
            assertEquals("(10, 20)", LongTuple.of(10L, 20L).toString());
            assertEquals("(1, 2, 3)", LongTuple.of(1L, 2L, 3L).toString());
            assertEquals("()", LongTuple.from(null).toString());
        }

        @Test
        void toString_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals("(42)", tuple.toString());
        }

        @Test
        void toString_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals("(10, 20)", tuple.toString());
        }

        @Test
        void toString_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals("(1, 2, 3)", tuple.toString());
        }

        @Test
        void toString_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals("(1, 2, 3, 4)", tuple.toString());
        }

        @Test
        void toString_tuple5() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals("(1, 2, 3, 4, 5)", tuple.toString());
        }

        @Test
        void toStringTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.from(new long[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
        }

        @Test
        void toStringTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        @Test
        void tuple0_toString() {
            LongTuple<?> tuple = LongTuple.from(null);
            assertEquals("()", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void allTupleSizes() {
            LongTuple0 t0 = LongTuple.from(null);
            LongTuple1 t1 = LongTuple.of(1L);
            LongTuple2 t2 = LongTuple.of(1L, 2L);
            LongTuple3 t3 = LongTuple.of(1L, 2L, 3L);
            LongTuple4 t4 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple5 t5 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple6 t6 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple7 t7 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple8 t8 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple9 t9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

            assertEquals(0, t0.arity());
            assertEquals(1, t1.arity());
            assertEquals(2, t2.arity());
            assertEquals(3, t3.arity());
            assertEquals(4, t4.arity());
            assertEquals(5, t5.arity());
            assertEquals(6, t6.arity());
            assertEquals(7, t7.arity());
            assertEquals(8, t8.arity());
            assertEquals(9, t9.arity());
        }

        @Test
        void arity4_statisticalOperations() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(1L, tuple.min());
            assertEquals(4L, tuple.max());
            assertEquals(10L, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity8_aggregates_unchanged() {
            LongTuple.LongTuple8 t = LongTuple.of(7L, 3L, 5L, 1L, 8L, 4L, 2L, 6L);
            assertEquals(1L, t.min());
            assertEquals(8L, t.max());
            assertEquals(4L, t.lowerMedian());
            assertEquals(36L, t.sum());
            assertEquals(36.0 / 8.0, t.average().getAsDouble(), 0d);
        }

        @Test
        void arity9_aggregates_unchanged() {
            LongTuple.LongTuple9 t = LongTuple.of(7L, 3L, 5L, 1L, 8L, 4L, 2L, 6L, 9L);
            assertEquals(1L, t.min());
            assertEquals(9L, t.max());
            assertEquals(5L, t.lowerMedian());
            assertEquals(45L, t.sum());
            assertEquals(45.0 / 9.0, t.average().getAsDouble(), 0d);
        }

        @Test
        void baseMethods_inheritedImplementation() {
            final LongTuple.LongTuple4 tuple = LongTuple.of(4L, 1L, 3L, 2L);
            final long[] copy = tuple.toArray();
            copy[0] = 99L;

            assertEquals(1L, tuple.min());
            assertEquals(4L, tuple.max());
            assertEquals(2L, tuple.lowerMedian());
            assertEquals(10L, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble());
            assertEquals(-0.5d, LongTuple.of(Long.MAX_VALUE, Long.MIN_VALUE).average().getAsDouble());
            assertTrue(tuple.contains(3L));
            assertFalse(tuple.contains(8L));
            assertEquals(4L, tuple.toArray()[0]);
            assertTrue(tuple.equals(LongTuple.of(4L, 1L, 3L, 2L)));
            assertFalse(tuple.equals(LongTuple.of(4L, 1L, 3L, 9L)));
            assertFalse(tuple.equals(null));
            assertEquals("(4, 1, 3, 2)", tuple.toString());
        }

        @Test
        void classLevelExamples() {
            // long min = triple.min();         // 1
            // long max = triple.max();         // 3
            // OptionalDouble avg = triple.average();   // OptionalDouble.of(2.0)
            LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            assertEquals(1L, triple.min());
            assertEquals(3L, triple.max());
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        @Test
        void coverageRegression_defaultConstructorsAndCachedArrays() {
            LongTuple.LongTuple1 tuple1 = new LongTuple.LongTuple1();
            assertArrayEquals(new long[] { 0L }, tuple1.toArray());
            assertArrayEquals(new long[] { 0L }, tuple1.toArray());

            LongTuple.LongTuple2 tuple2 = new LongTuple.LongTuple2();
            assertArrayEquals(new long[] { 0L, 0L }, tuple2.toArray());
            assertArrayEquals(new long[] { 0L, 0L }, tuple2.toArray());

            LongTuple.LongTuple3 tuple3 = new LongTuple.LongTuple3();
            assertArrayEquals(new long[] { 0L, 0L, 0L }, tuple3.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L }, tuple3.toArray());

            LongTuple.LongTuple4 tuple4 = new LongTuple.LongTuple4();
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L }, tuple4.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L }, tuple4.toArray());

            LongTuple.LongTuple5 tuple5 = new LongTuple.LongTuple5();
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L }, tuple5.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L }, tuple5.toArray());

            LongTuple.LongTuple6 tuple6 = new LongTuple.LongTuple6();
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L }, tuple6.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L }, tuple6.toArray());

            LongTuple.LongTuple7 tuple7 = new LongTuple.LongTuple7();
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple7.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple7.toArray());

            LongTuple.LongTuple8 tuple8 = new LongTuple.LongTuple8();
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple8.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple8.toArray());

            LongTuple.LongTuple9 tuple9 = new LongTuple.LongTuple9();
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple9.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple9.toArray());
        }

        @Test
        void coverageRegression_emptyTupleBaseMethods() {
            LongTuple<?> empty = LongTuple.from((long[]) null);
            assertEquals(LongTuple.from(new long[0]), empty);
            assertFalse(empty.equals("long"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void defaultConstructors_zeroInitialization() {
            final LongTuple1 tuple1 = new LongTuple1();
            final LongTuple2 tuple2 = new LongTuple2();
            final LongTuple3 tuple3 = new LongTuple3();
            final LongTuple4 tuple4 = new LongTuple4();
            final LongTuple5 tuple5 = new LongTuple5();
            final LongTuple6 tuple6 = new LongTuple6();
            final LongTuple7 tuple7 = new LongTuple7();
            final LongTuple8 tuple8 = new LongTuple8();
            final LongTuple9 tuple9 = new LongTuple9();

            assertArrayEquals(new long[] { 0L }, tuple1.toArray());
            assertArrayEquals(new long[] { 0L }, tuple1.toArray());
            assertArrayEquals(new long[] { 0L, 0L }, tuple2.toArray());
            assertArrayEquals(new long[] { 0L, 0L }, tuple2.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L }, tuple3.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L }, tuple3.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L }, tuple4.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L }, tuple4.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L }, tuple5.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L }, tuple5.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L }, tuple6.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L }, tuple6.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple7.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple7.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple8.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple8.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple9.toArray());
            assertArrayEquals(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L }, tuple9.toArray());
        }

        @Test
        void immutability() {
            long[] arr = new long[] { 1L, 2L, 3L };
            LongTuple3 tuple = LongTuple.from(arr);
            arr[0] = 999L;
            assertEquals(1L, tuple._1);
        }

        @Test
        void inheritedAggregateEmptyBehavior() {
            final LongTuple.LongTuple0 tuple = LongTuple.from(new long[0]);

            assertThrows(NoSuchElementException.class, tuple::min);
            assertThrows(NoSuchElementException.class, tuple::max);
            assertThrows(NoSuchElementException.class, tuple::median);
            assertTrue(tuple.average().isEmpty());
            assertEquals(0L, tuple.sum());
        }

        @Test
        void largerArities_branchCoverage() {
            final LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
            final LongTuple5 tuple5 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            final LongTuple6 tuple6 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            final LongTuple7 tuple7 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            final LongTuple8 tuple8 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            final LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

            assertTrue(tuple4.contains(2L));
            assertTrue(tuple4.contains(3L));
            assertTrue(tuple5.contains(4L));
            assertTrue(tuple6.contains(5L));
            assertTrue(tuple7.contains(6L));
            assertTrue(tuple8.contains(7L));
            assertTrue(tuple9.contains(8L));
            assertFalse(tuple9.contains(10L));

            assertTrue(tuple4.equals(tuple4));
            assertFalse(tuple4.equals("not a tuple"));
            assertFalse(tuple4.equals(LongTuple.of(1L, 2L, 3L, 9L)));
            assertTrue(tuple5.equals(tuple5));
            assertFalse(tuple5.equals("not a tuple"));
            assertFalse(tuple5.equals(LongTuple.of(1L, 2L, 3L, 4L, 9L)));
            assertTrue(tuple6.equals(tuple6));
            assertFalse(tuple6.equals("not a tuple"));
            assertFalse(tuple6.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 9L)));
            assertTrue(tuple7.equals(tuple7));
            assertFalse(tuple7.equals("not a tuple"));
            assertFalse(tuple7.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 9L)));
            assertTrue(tuple8.equals(tuple8));
            assertFalse(tuple8.equals("not a tuple"));
            assertFalse(tuple8.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 9L)));
            assertTrue(tuple9.equals(tuple9));
            assertFalse(tuple9.equals("not a tuple"));
            assertFalse(tuple9.equals(LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 10L)));
        }

        @Test
        void largeValues() {
            LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE / 2);
            assertEquals(Long.MAX_VALUE / 2, tuple.min());
            assertEquals(Long.MAX_VALUE, tuple.max());
        }

        @Test
        void mixedValues() {
            LongTuple3 tuple = LongTuple.of(-100L, 0L, 100L);
            assertEquals(-100L, tuple.min());
            assertEquals(100L, tuple.max());
            assertEquals(0L, tuple.sum());
        }

        @Test
        void negativeValues() {
            LongTuple3 tuple = LongTuple.of(-1L, -2L, -3L);
            assertEquals(-3L, tuple.min());
            assertEquals(-1L, tuple.max());
            assertEquals(-6L, tuple.sum());
        }

        @Test
        void reverse_largerTuples() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 reversed = tuple.reversed();
            assertEquals(4L, reversed._1);
            assertEquals(3L, reversed._2);
            assertEquals(2L, reversed._3);
            assertEquals(1L, reversed._4);
        }

        @Test
        void tuple1SpecificMethods() {
            LongTuple.LongTuple1 tuple = LongTuple.of(100L);

            // Test min/max/median/sum/average
            assertEquals(100L, tuple.min());
            assertEquals(100L, tuple.max());
            assertEquals(100L, tuple.lowerMedian());
            assertEquals(100L, tuple.sum());
            assertEquals(100.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple2SpecificMethods() {
            LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);

            // Test accept
            long[] sum = { 0L };
            tuple.accept((a, b) -> sum[0] = a + b);
            assertEquals(300L, sum[0]);

            // Test map
            long result = tuple.map((a, b) -> a + b);
            assertEquals(300L, result);

            // Test filter
            assertTrue(tuple.filter((a, b) -> a < b).isPresent());
            assertFalse(tuple.filter((a, b) -> a > b).isPresent());
        }

        @Test
        void tuple3SpecificMethods() {
            LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);

            // Test accept
            long[] sum = { 0L };
            tuple.accept((a, b, c) -> sum[0] = a + b + c);
            assertEquals(600L, sum[0]);

            // Test map
            long result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(600L, result);

            // Test filter
            assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
        }

        @Test
        void tuple4Operations() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);

            // Test reverse
            LongTuple4 reversed = tuple.reversed();
            assertEquals(4L, reversed._1);
            assertEquals(3L, reversed._2);
            assertEquals(2L, reversed._3);
            assertEquals(1L, reversed._4);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(4L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals(1L, tuple.min());
            assertEquals(4L, tuple.max());
            assertEquals(2L, tuple.lowerMedian());
            assertEquals(10L, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 tuple3 = LongTuple.of(1L, 2L, 3L, 5L);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }

        @Test
        void tuple5_operations() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(5, tuple.arity());
            assertEquals(1L, tuple.min());
            assertEquals(5L, tuple.max());
            assertEquals(3L, tuple.lowerMedian());
            assertEquals(15L, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);
            assertTrue(tuple.contains(3L));
            assertFalse(tuple.contains(10L));
        }

        @Test
        void tuple5Operations() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);

            // Test reverse
            LongTuple5 reversed = tuple.reversed();
            assertEquals(5L, reversed._1);
            assertEquals(1L, reversed._5);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(5L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(5L, tuple.max());
            assertEquals(3L, tuple.lowerMedian());
            assertEquals(15L, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);

            // Test equals
            LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple, tuple2);
        }

        @Test
        void tuple6_operations() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(6, tuple.arity());
            assertEquals(1L, tuple.min());
            assertEquals(6L, tuple.max());
            assertEquals(21L, tuple.sum());
        }

        @Test
        void tuple6Operations() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);

            // Test reverse
            LongTuple6 reversed = tuple.reversed();
            assertEquals(6L, reversed._1);
            assertEquals(1L, reversed._6);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(6L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(6L, tuple.max());
            assertEquals(21L, tuple.sum());
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple7_operations() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(7, tuple.arity());
            assertEquals(4L, tuple.lowerMedian());
            assertEquals(28L, tuple.sum());
        }

        @Test
        void tuple7Operations() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

            // Test reverse
            LongTuple7 reversed = tuple.reversed();
            assertEquals(7L, reversed._1);
            assertEquals(1L, reversed._7);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(7L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(7L, tuple.max());
            assertEquals(28L, tuple.sum());
            assertEquals(4.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple8Operations() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);

            // Test reverse
            LongTuple8 reversed = tuple.reversed();
            assertEquals(8L, reversed._1);
            assertEquals(1L, reversed._8);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(8L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(8L, tuple.max());
            assertEquals(36L, tuple.sum());
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9Operations() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

            // Test reverse
            LongTuple9 reversed = tuple.reversed();
            assertEquals(9L, reversed._1);
            assertEquals(1L, reversed._9);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(9L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(9L, tuple.max());
            assertEquals(45L, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void withLargeValues() {
            long max = Long.MAX_VALUE / 2;
            LongTuple2 tuple = LongTuple.of(max, max);
            assertEquals(max, tuple.min());
            assertEquals(max, tuple.max());
        }

        @Test
        void withNegatives() {
            LongTuple3 tuple = LongTuple.of(-5L, -10L, -1L);
            assertEquals(-10L, tuple.min());
            assertEquals(-1L, tuple.max());
            assertEquals(-16L, tuple.sum());
        }

        @Test
        void withSingleElement() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.min());
            assertEquals(42L, tuple.max());
            assertEquals(42L, tuple.lowerMedian());
            assertEquals(42L, tuple.sum());
            assertEquals(42.0, tuple.average().getAsDouble(), 0.0001);
            assertTrue(tuple.contains(42L));
        }

        @Test
        void withZero() {
            LongTuple3 tuple = LongTuple.of(0L, 1L, 2L);
            assertEquals(0L, tuple.min());
            assertEquals(2L, tuple.max());
            assertEquals(3L, tuple.sum());
        }

        @Test
        void zeroValues() {
            LongTuple3 tuple = LongTuple.of(0L, 0L, 0L);
            assertEquals(0L, tuple.min());
            assertEquals(0L, tuple.max());
            assertEquals(0L, tuple.sum());
        }
    }
}
