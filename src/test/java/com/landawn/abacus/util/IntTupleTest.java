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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.IntTuple.IntTuple0;
import com.landawn.abacus.util.IntTuple.IntTuple1;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.IntTuple.IntTuple4;
import com.landawn.abacus.util.IntTuple.IntTuple5;
import com.landawn.abacus.util.IntTuple.IntTuple6;
import com.landawn.abacus.util.IntTuple.IntTuple7;
import com.landawn.abacus.util.IntTuple.IntTuple8;
import com.landawn.abacus.util.IntTuple.IntTuple9;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.u.Optional;

class IntTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, int[] values) {
            IntTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of3Average() {
            // IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
            // triple.average();   // OptionalDouble.of(2.0)
            IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        private static IntTuple<?> createWithOf(int[] values) {
            return switch (values.length) {
                case 1 -> IntTuple.of(values[0]);
                case 2 -> IntTuple.of(values[0], values[1]);
                case 3 -> IntTuple.of(values[0], values[1], values[2]);
                case 4 -> IntTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> IntTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: 1", new int[] { 1 }), Arguments.of("1: 10", new int[] { 10 }),
                    Arguments.of("1: 42", new int[] { 42 }), Arguments.of("1: 5", new int[] { 5 }), Arguments.of("2: 1, 2", new int[] { 1, 2 }),
                    Arguments.of("2: 10, 20", new int[] { 10, 20 }), Arguments.of("3: 1, 2, 3", new int[] { 1, 2, 3 }),
                    Arguments.of("3: 10, 20, 30", new int[] { 10, 20, 30 }), Arguments.of("4: 1, 2, 3, 4", new int[] { 1, 2, 3, 4 }),
                    Arguments.of("4: 10, 20, 30, 40", new int[] { 10, 20, 30, 40 }), Arguments.of("5: 1, 2, 3, 4, 5", new int[] { 1, 2, 3, 4, 5 }),
                    Arguments.of("5: 10, 20, 30, 40, 50", new int[] { 10, 20, 30, 40, 50 }),
                    Arguments.of("6: 1, 2, 3, 4, 5, 6", new int[] { 1, 2, 3, 4, 5, 6 }),
                    Arguments.of("6: 10, 20, 30, 40, 50, 60", new int[] { 10, 20, 30, 40, 50, 60 }),
                    Arguments.of("7: 1, 2, 3, 4, 5, 6, 7", new int[] { 1, 2, 3, 4, 5, 6, 7 }),
                    Arguments.of("7: 10, 20, 30, 40, 50, 60, 70", new int[] { 10, 20, 30, 40, 50, 60, 70 }),
                    Arguments.of("8: 1, 2, 3, 4, 5, 6, 7, 8", new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }),
                    Arguments.of("8: 10, 20, 30, 40, 50, 60, 70, 80", new int[] { 10, 20, 30, 40, 50, 60, 70, 80 }),
                    Arguments.of("9: 1, 2, 3, 4, 5, 6, 7, 8, 9", new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }),
                    Arguments.of("9: 10, 20, 30, 40, 50, 60, 70, 80, 90", new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 }));
        }

        private static int[] publicFields(IntTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    IntTuple.IntTuple1 value = (IntTuple.IntTuple1) tuple;
                    yield new int[] { value._1 };
                }
                case 2 -> {
                    IntTuple.IntTuple2 value = (IntTuple.IntTuple2) tuple;
                    yield new int[] { value._1, value._2 };
                }
                case 3 -> {
                    IntTuple.IntTuple3 value = (IntTuple.IntTuple3) tuple;
                    yield new int[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    IntTuple.IntTuple4 value = (IntTuple.IntTuple4) tuple;
                    yield new int[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    IntTuple.IntTuple5 value = (IntTuple.IntTuple5) tuple;
                    yield new int[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    IntTuple.IntTuple6 value = (IntTuple.IntTuple6) tuple;
                    yield new int[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    IntTuple.IntTuple7 value = (IntTuple.IntTuple7) tuple;
                    yield new int[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    IntTuple.IntTuple8 value = (IntTuple.IntTuple8) tuple;
                    yield new int[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    IntTuple.IntTuple9 value = (IntTuple.IntTuple9) tuple;
                    yield new int[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
                }
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + tuple.arity());
            };
        }
    }

    @Nested
    class FromTests {

        @Test
        void create_multipleElementsArray() {
            IntTuple3 tuple = IntTuple.from(new int[] { 1, 2, 3 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
        }

        @Test
        void create_nullArray() {
            IntTuple0 tuple = IntTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_singleElementArray() {
            IntTuple1 tuple = IntTuple.from(new int[] { 42 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(42, tuple._1);
        }

        @Test
        void create_tooManyElements() {
            int[] array = new int[10];
            assertThrows(IllegalArgumentException.class, () -> IntTuple.from(array));
        }

        @Test
        void from_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_emptyArray() {
            IntTuple<?> tuple = IntTuple.from(new int[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nine() {
            IntTuple9 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_nullArray() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_preservesElements() {
            // Test empty array
            IntTuple<?> empty = IntTuple.from(null);
            assertEquals(0, empty.arity());

            empty = IntTuple.from(new int[0]);
            assertEquals(0, empty.arity());

            // Test array with 1 element
            IntTuple.IntTuple1 tuple1 = IntTuple.from(new int[] { 10 });
            assertEquals(1, tuple1.arity());
            assertEquals(10, tuple1._1);

            // Test array with 5 elements
            IntTuple.IntTuple5 tuple5 = IntTuple.from(new int[] { 10, 20, 30, 40, 50 });
            assertEquals(5, tuple5.arity());
            assertEquals(50, tuple5._5);

            // Test array with 9 elements
            IntTuple.IntTuple9 tuple9 = IntTuple.from(new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
            assertEquals(9, tuple9.arity());
            assertEquals(90, tuple9._9);

            // Test too many elements
            assertThrows(IllegalArgumentException.class, () -> IntTuple.from(new int[10]));
        }

        @Test
        void from_single() {
            IntTuple1 tuple = IntTuple.from(new int[] { 100 });
            assertNotNull(tuple);
            assertEquals(100, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from_sizeEight() {
            IntTuple8 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple._8);
        }

        @Test
        void from_sizeFive() {
            IntTuple5 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5 });
            assertEquals(5, tuple.arity());
            assertEquals(5, tuple._5);
        }

        @Test
        void from_sizeFour() {
            IntTuple4 tuple = IntTuple.from(new int[] { 1, 2, 3, 4 });
            assertEquals(4, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(4, tuple._4);
        }

        @Test
        void from_sizeNine() {
            IntTuple9 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple._9);
        }

        @Test
        void from_sizeOne() {
            IntTuple1 tuple = IntTuple.from(new int[] { 42 });
            assertEquals(1, tuple.arity());
            assertEquals(42, tuple._1);
        }

        @Test
        void from_sizeSeven() {
            IntTuple7 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(7, tuple.arity());
            assertEquals(7, tuple._7);
        }

        @Test
        void from_sizeSix() {
            IntTuple6 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(6, tuple.arity());
            assertEquals(6, tuple._6);
        }

        @Test
        void from_sizeThree() {
            IntTuple3 tuple = IntTuple.from(new int[] { 1, 2, 3 });
            assertEquals(3, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(3, tuple._3);
        }

        @Test
        void from_sizeTwo() {
            IntTuple2 tuple = IntTuple.from(new int[] { 1, 2 });
            assertEquals(2, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
        }

        @Test
        void from_tooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
            });
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        void from_two() {
            IntTuple2 tuple = IntTuple.from(new int[] { 10, 20 });
            assertNotNull(tuple);
            assertEquals(10, tuple._1);
            assertEquals(20, tuple._2);
        }

        @Test
        void from1() {
            IntTuple1 tuple = IntTuple.from(new int[] { 42 });
            assertEquals(42, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2Through8() {
            IntTuple2 tuple2 = IntTuple.from(new int[] { 1, 2 });
            assertEquals(1, tuple2._1);
            assertEquals(2, tuple2._2);

            IntTuple4 tuple4 = IntTuple.from(new int[] { 1, 2, 3, 4 });
            assertEquals(1, tuple4._1);
            assertEquals(4, tuple4._4);

            IntTuple5 tuple5 = IntTuple.from(new int[] { 1, 2, 3, 4, 5 });
            assertEquals(1, tuple5._1);
            assertEquals(5, tuple5._5);

            IntTuple6 tuple6 = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(1, tuple6._1);
            assertEquals(6, tuple6._6);

            IntTuple7 tuple7 = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(1, tuple7._1);
            assertEquals(7, tuple7._7);

            IntTuple8 tuple8 = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(1, tuple8._1);
            assertEquals(8, tuple8._8);
        }

        @Test
        void from3() {
            IntTuple3 tuple = IntTuple.from(new int[] { 1, 2, 3 });
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
        }

        @Test
        void from9() {
            IntTuple9 tuple = IntTuple.from(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
        }

        @Test
        void fromEmpty() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromNull() {
            IntTuple<IntTuple0> tuple = IntTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }
    }

    @Nested
    class ArityTests {

        @Test
        void arity_returnsElementCount() {
            assertEquals(0, IntTuple.from(new int[0]).arity());
            assertEquals(1, IntTuple.of(1).arity());
            assertEquals(2, IntTuple.of(1, 2).arity());
            assertEquals(3, IntTuple.of(1, 2, 3).arity());
            assertEquals(4, IntTuple.of(1, 2, 3, 4).arity());
            assertEquals(5, IntTuple.of(1, 2, 3, 4, 5).arity());
            assertEquals(6, IntTuple.of(1, 2, 3, 4, 5, 6).arity());
            assertEquals(7, IntTuple.of(1, 2, 3, 4, 5, 6, 7).arity());
            assertEquals(8, IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8).arity());
            assertEquals(9, IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9).arity());
        }

        @Test
        void arity0_arity() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void coverageRegression_highArityEqualsBranches() {
            IntTuple.IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
            assertTrue(tuple4.equals(tuple4));
            assertTrue(tuple4.equals(IntTuple.of(1, 2, 3, 4)));
            assertFalse(tuple4.equals(IntTuple.of(1, 2, 3, 9)));
            assertFalse(tuple4.equals("tuple4"));

            IntTuple.IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            assertTrue(tuple5.equals(tuple5));
            assertTrue(tuple5.equals(IntTuple.of(1, 2, 3, 4, 5)));
            assertFalse(tuple5.equals(IntTuple.of(1, 2, 3, 4, 9)));
            assertFalse(tuple5.equals("tuple5"));

            IntTuple.IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertTrue(tuple6.equals(tuple6));
            assertTrue(tuple6.equals(IntTuple.of(1, 2, 3, 4, 5, 6)));
            assertFalse(tuple6.equals(IntTuple.of(1, 2, 3, 4, 5, 9)));
            assertFalse(tuple6.equals("tuple6"));

            IntTuple.IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertTrue(tuple7.equals(tuple7));
            assertTrue(tuple7.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7)));
            assertFalse(tuple7.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 9)));
            assertFalse(tuple7.equals("tuple7"));

            IntTuple.IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertTrue(tuple8.equals(tuple8));
            assertTrue(tuple8.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8)));
            assertFalse(tuple8.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7, 9)));
            assertFalse(tuple8.equals("tuple8"));

            IntTuple.IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple9.equals(tuple9));
            assertTrue(tuple9.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9)));
            assertFalse(tuple9.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 0)));
            assertFalse(tuple9.equals("tuple9"));
        }

        @Test
        void coverageRegression_highArityOperations() {
            IntTuple.IntTuple4 tuple4 = IntTuple.of(9, 1, 7, 3);
            assertEquals(1, tuple4.min());
            assertEquals(9, tuple4.max());
            assertEquals(3, tuple4.lowerMedian());
            assertEquals(20, tuple4.sum());
            assertEquals(5.0, tuple4.average().getAsDouble());
            assertTrue(tuple4.contains(3));

            IntTuple.IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            assertTrue(tuple5.contains(5));

            IntTuple.IntTuple6 tuple6 = IntTuple.of(9, 1, 7, 3, 5, 11);
            assertEquals(5, tuple6.lowerMedian());
            assertTrue(tuple6.contains(11));

            IntTuple.IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertTrue(tuple7.contains(7));

            IntTuple.IntTuple8 tuple8 = IntTuple.of(9, 1, 7, 3, 5, 11, 13, 15);
            assertEquals(7, tuple8.lowerMedian());
            assertTrue(tuple8.contains(15));

            IntTuple.IntTuple9 tuple9 = IntTuple.of(9, 1, 7, 3, 5, 11, 13, 15, 17);
            assertEquals(9, tuple9.lowerMedian());
            assertTrue(tuple9.contains(17));
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void elementsMethod() {
            // Just test that elements are cached properly
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] elements1 = tuple.elements();
            int[] elements2 = tuple.elements();
            assertSame(elements1, elements2); // Should return same cached array
        }
    }

    @Nested
    class MinTests {

        @Test
        void arity0_min_throwsException() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void arity1_min() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.min());
        }

        @Test
        void arity1_min_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.min());
        }

        @Test
        void arity2_min() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min_values_3_1() {
            IntTuple2 tuple = IntTuple.of(3, 1);
            assertEquals(1, tuple.min());
        }

        @Test
        void arity3_min() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min_values_3_1_2() {
            IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(1, tuple.min());
        }

        @Test
        void min_multiple() {
            IntTuple3 tuple = IntTuple.of(100, 5, 50);
            assertEquals(5, tuple.min());
        }

        @Test
        void min_negatives() {
            IntTuple3 tuple = IntTuple.of(-10, -50, -20);
            assertEquals(-50, tuple.min());
        }

        @Test
        void min_returnsMinimum() {
            IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
            assertEquals(10, tuple.min());

            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> empty.min());
        }

        @Test
        void min_returnsMinimum_values_3_1_2() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            // int min = tuple.min();   // 1
            IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(1, tuple.min());

            // IntTuple.IntTuple1 single = IntTuple.of(42);
            // int singleMin = single.min();   // 42
            IntTuple.IntTuple1 single = IntTuple.of(42);
            assertEquals(42, single.min());
        }

        @Test
        void min_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(2, tuple.min());
        }

        @Test
        void min_tuple3() {
            IntTuple3 tuple = IntTuple.of(5, 2, 8);
            assertEquals(2, tuple.min());
        }

        @Test
        void min_tuple4() {
            IntTuple4 tuple = IntTuple.of(5, 2, 8, 1);
            assertEquals(1, tuple.min());
        }

        @Test
        void minTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.min());
        }

        @Test
        void minWithMaxValue() {
            IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
            assertEquals(Integer.MIN_VALUE, tuple.min());
        }

        @Test
        void minWithNegatives() {
            IntTuple3 tuple = IntTuple.of(5, -10, 3);
            assertEquals(-10, tuple.min());
        }

        @Test
        void tuple0_min() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity0_max_throwsException() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void arity1_max() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.max());
        }

        @Test
        void arity1_max_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.max());
        }

        @Test
        void arity2_max() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max_values_3_1() {
            IntTuple2 tuple = IntTuple.of(3, 1);
            assertEquals(3, tuple.max());
        }

        @Test
        void arity3_max() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max_values_3_1_2() {
            IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(3, tuple.max());
        }

        @Test
        void max_multiple() {
            IntTuple3 tuple = IntTuple.of(100, 5, 50);
            assertEquals(100, tuple.max());
        }

        @Test
        void max_negatives() {
            IntTuple3 tuple = IntTuple.of(-10, -50, -20);
            assertEquals(-10, tuple.max());
        }

        @Test
        void max_returnsMaximum() {
            IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
            assertEquals(30, tuple.max());

            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> empty.max());
        }

        @Test
        void max_returnsMaximum_values_3_1_2() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            // int max = tuple.max();   // 3
            IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(3, tuple.max());

            // IntTuple.IntTuple1 single = IntTuple.of(42);
            // int singleMax = single.max();   // 42
            IntTuple.IntTuple1 single = IntTuple.of(42);
            assertEquals(42, single.max());
        }

        @Test
        void max_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(5, tuple.max());
        }

        @Test
        void max_tuple3() {
            IntTuple3 tuple = IntTuple.of(5, 2, 8);
            assertEquals(8, tuple.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.max());
        }

        @Test
        void maxWithMaxValue() {
            IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
            assertEquals(Integer.MAX_VALUE, tuple.max());
        }

        @Test
        void maxWithNegatives() {
            IntTuple3 tuple = IntTuple.of(-5, -10, -3);
            assertEquals(-3, tuple.max());
        }

        @Test
        void tuple0_max() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity0_sum() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void arity1_sum() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity1_sum_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.sum());
        }

        @Test
        void arity2_sum() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum_values_1_2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(3, tuple.sum());
        }

        @Test
        void arity3_sum() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(6, tuple.sum());
        }

        @Test
        void largeSum() {
            IntTuple3 tuple = IntTuple.of(1000000, 2000000, 3000000);
            assertEquals(6000000, tuple.sum());
            assertEquals(2000000.0, tuple.average().getAsDouble());
        }

        @Test
        void sum_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(60, tuple.sum());
        }

        @Test
        void sum_negatives() {
            IntTuple3 tuple = IntTuple.of(10, -5, 15);
            assertEquals(20, tuple.sum());
        }

        @Test
        void sum_overflowThrowsArithmeticException() {
            assertThrows(ArithmeticException.class, () -> IntTuple.of(Integer.MAX_VALUE, 1).sum());
            assertThrows(ArithmeticException.class, () -> IntTuple.of(Integer.MIN_VALUE, -1).sum());
            assertThrows(ArithmeticException.class, () -> IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE).sum());
            assertThrows(ArithmeticException.class, () -> IntTuple.of(Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, 1, 1).sum());

            // The accumulator is a long, so only the final total matters: values that
            // cancel out along the way must not throw.
            assertEquals(Integer.MAX_VALUE, IntTuple.of(Integer.MAX_VALUE, 1, -1).sum());
            assertEquals(Integer.MIN_VALUE, IntTuple.of(Integer.MIN_VALUE, -1, 1).sum());
        }

        @Test
        void sum_returnsTotal() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(60, tuple.sum());

            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertEquals(0, empty.sum());
        }

        @Test
        void sum_returnsTotal_values_1_2_3() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // int sum = tuple.sum();   // 6
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(6, tuple.sum());

            // IntTuple.IntTuple2 pair = IntTuple.of(100, 200);
            // int total = pair.sum();  // 300
            IntTuple.IntTuple2 pair = IntTuple.of(100, 200);
            assertEquals(300, pair.sum());
        }

        @Test
        void sum_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.sum());
        }

        @Test
        void sum_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(7, tuple.sum());
        }

        @Test
        void sumTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void sumWithNegatives() {
            IntTuple3 tuple = IntTuple.of(10, -5, 3);
            assertEquals(8, tuple.sum());
        }

        @Test
        void tuple0_sum() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertEquals(0, tuple.sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity0_average_returnsEmpty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void arity1_average() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity1_average_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity2_average() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average_values_1_3() {
            IntTuple2 tuple = IntTuple.of(1, 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity3_average() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void average_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(20.0, tuple.average().getAsDouble());
        }

        @Test
        void average_odd() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, tuple.average().getAsDouble());
        }

        @Test
        void average_returnsMean() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(20.0, tuple.average().getAsDouble());

            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void average_returnsMean_values_1_2_3_4() {
            // IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            // tuple.average();   // OptionalDouble.of(2.5)
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_single() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42.0, tuple.average().getAsDouble());
        }

        @Test
        void average_tuple2() {
            IntTuple2 tuple = IntTuple.of(4, 6);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple2() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertEquals(15.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple0_average() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertTrue(tuple.average().isEmpty());
        }
    }

    @Nested
    class MedianTests {

        @Test
        void arity0_median_throwsException() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void arity1_median() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity1_median_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.lowerMedian());
        }

        @Test
        void arity2_median() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median_values_1_2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            int median = tuple.lowerMedian();
            assertEquals(1, median);
        }

        @Test
        void arity3_median() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median_values_30_10_20() {
            IntTuple3 tuple = IntTuple.of(30, 10, 20);
            assertEquals(20, tuple.lowerMedian());
        }

        @Test
        void median_evenSize() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            int median = tuple.lowerMedian();
            assertEquals(2, median);
        }

        @Test
        void median_odd() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2, tuple.lowerMedian());
        }

        @Test
        void median_oddSize() {
            IntTuple5 tuple = IntTuple.of(50, 10, 30, 20, 40);
            int median = tuple.lowerMedian();
            assertEquals(30, median);
        }

        @Test
        void median_returnsMedian() {
            IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
            assertEquals(20, tuple.lowerMedian());

            IntTuple.IntTuple4 evenTuple = IntTuple.of(10, 20, 30, 40);
            assertEquals(20, evenTuple.lowerMedian()); // even arity returns the lower of the two middle values: 20

            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> empty.lowerMedian());
        }

        @Test
        void median_returnsMedian_values_1_3_2() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 3, 2);
            // int median = tuple.lowerMedian();   // 2
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 3, 2);
            assertEquals(2, tuple.lowerMedian());

            // IntTuple.IntTuple4 evenTuple = IntTuple.of(1, 2, 3, 4);
            // int evenMedian = evenTuple.lowerMedian();   // 2
            IntTuple.IntTuple4 evenTuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2, evenTuple.lowerMedian());
        }

        @Test
        void median_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(2, tuple.lowerMedian());
        }

        @Test
        void median_tuple3() {
            IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(2, tuple.lowerMedian());
        }

        @Test
        void median_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2, tuple.lowerMedian());
        }

        @Test
        void medianTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.lowerMedian());
        }

        @Test
        void medianTuple2() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            assertEquals(5, tuple.lowerMedian());
        }

        @Test
        void pairMedianWidensOperandsBeforeAddition() {
            assertEquals((double) Integer.MAX_VALUE, IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE).median());
            assertEquals((double) Integer.MIN_VALUE, IntTuple.of(Integer.MIN_VALUE, Integer.MIN_VALUE).median());
            assertEquals(-0.5d, IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE).median());
        }

        @Test
        void tuple0_median() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity0_reversed() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            IntTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void arity1_reversed() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            IntTuple.IntTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity1_reversed_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            IntTuple1 reversed = tuple.reversed();
            assertEquals(5, reversed._1);
        }

        @Test
        void arity2_reversed() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            IntTuple.IntTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity2_reversed_values_1_2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            IntTuple2 reversed = tuple.reversed();
            assertEquals(2, reversed._1);
            assertEquals(1, reversed._2);
        }

        @Test
        void arity3_reversed() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity3_reversed_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntTuple3 reversed = tuple.reversed();
            assertEquals(3, reversed._1);
            assertEquals(2, reversed._2);
            assertEquals(1, reversed._3);
        }

        @Test
        void arity4_reversed() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            IntTuple.IntTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple.IntTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple.IntTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple.IntTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple.IntTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple.IntTuple9 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        void reversed_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            IntTuple0 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversed_largeValues() {
            IntTuple4 tuple = IntTuple.of(Integer.MAX_VALUE, 0, -1, Integer.MIN_VALUE);
            IntTuple4 reversed = tuple.reversed();
            assertEquals(Integer.MIN_VALUE, reversed._1);
            assertEquals(-1, reversed._2);
            assertEquals(0, reversed._3);
            assertEquals(Integer.MAX_VALUE, reversed._4);
        }

        @Test
        void reversed_multiple() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntTuple3 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(3, reversed._1);
            assertEquals(2, reversed._2);
            assertEquals(1, reversed._3);
        }

        @Test
        void reversed_reversesElementOrder() {
            // Test Tuple0
            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            IntTuple.IntTuple0 reversedEmpty = empty.reversed();
            assertEquals(0, reversedEmpty.arity());

            // Test Tuple1
            IntTuple.IntTuple1 tuple1 = IntTuple.of(10);
            IntTuple.IntTuple1 reversed1 = tuple1.reversed();
            assertEquals(10, reversed1._1);

            // Test Tuple2
            IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            IntTuple.IntTuple2 reversed2 = tuple2.reversed();
            assertEquals(20, reversed2._1);
            assertEquals(10, reversed2._2);

            // Test Tuple3
            IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 30);
            IntTuple.IntTuple3 reversed3 = tuple3.reversed();
            assertEquals(30, reversed3._1);
            assertEquals(20, reversed3._2);
            assertEquals(10, reversed3._3);

            // Test larger tuples
            IntTuple.IntTuple5 tuple5 = IntTuple.of(10, 20, 30, 40, 50);
            IntTuple.IntTuple5 reversed5 = tuple5.reversed();
            assertEquals(50, reversed5._1);
            assertEquals(40, reversed5._2);
            assertEquals(30, reversed5._3);
            assertEquals(20, reversed5._4);
            assertEquals(10, reversed5._5);
        }

        @Test
        void reversed_single() {
            IntTuple1 tuple = IntTuple.of(42);
            IntTuple1 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(42, reversed._1);
            assertNotSame(tuple, reversed); // Should be new instance
        }

        @Test
        void reversed_tuple1() {
            IntTuple1 tuple = IntTuple.of(1);
            IntTuple1 reversed = tuple.reversed();
            assertEquals(1, reversed._1);
        }

        @Test
        void reversed_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            IntTuple4 reversed = tuple.reversed();
            assertEquals(4, reversed._1);
            assertEquals(1, reversed._4);
        }

        @Test
        void reversed_twoElements() {
            IntTuple2 tuple = IntTuple.of(100, 200);
            IntTuple2 reversed = tuple.reversed();
            assertEquals(200, reversed._1);
            assertEquals(100, reversed._2);
        }

        @Test
        void reversedTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            IntTuple<IntTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversedTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            IntTuple1 reversed = tuple.reversed();
            assertEquals(42, reversed._1);
        }

        @Test
        void tuple0_reversed() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertNotNull(tuple.reversed());
        }

        @Test
        void tuple5_reversed() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 reversed = tuple.reversed();
            assertEquals(5, reversed._1);
            assertEquals(4, reversed._2);
            assertEquals(3, reversed._3);
            assertEquals(2, reversed._4);
            assertEquals(1, reversed._5);
        }

        @Test
        void tuple9_reversed() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple9 reversed = tuple.reversed();
            assertEquals(9, reversed._1);
            assertEquals(1, reversed._9);
        }
    }

    @Nested
    class ContainsTests {

        @Test
        void arity0_contains() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertFalse(tuple.contains(1));
        }

        @Test
        void arity1_contains() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity1_contains_found() {
            IntTuple1 tuple = IntTuple.of(5);
            assertTrue(tuple.contains(5));
        }

        @Test
        void arity1_contains_notFound() {
            IntTuple1 tuple = IntTuple.of(5);
            assertFalse(tuple.contains(10));
        }

        @Test
        void arity2_contains() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity2_contains_found() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(2));
        }

        @Test
        void arity2_contains_notFound() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertFalse(tuple.contains(3));
        }

        @Test
        void arity3_contains() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity3_contains_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(2));
            assertFalse(tuple.contains(5));
        }

        @Test
        void arity4_contains() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity5_contains() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity6_contains() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity7_contains() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity8_contains() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertTrue(tuple.contains(1));
        }

        @Test
        void arity9_contains() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple.contains(1));
        }

        @Test
        void contains_allElements() {
            IntTuple5 tuple = IntTuple.of(10, 20, 30, 40, 50);
            assertTrue(tuple.contains(10));
            assertTrue(tuple.contains(20));
            assertTrue(tuple.contains(30));
            assertTrue(tuple.contains(40));
            assertTrue(tuple.contains(50));
            assertFalse(tuple.contains(99));
        }

        @Test
        void contains_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertFalse(tuple.contains(42));
        }

        @Test
        void contains_findsPresentElements() {
            // Test Tuple0
            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertFalse(empty.contains(10));

            // Test Tuple1
            IntTuple.IntTuple1 tuple1 = IntTuple.of(10);
            assertTrue(tuple1.contains(10));
            assertFalse(tuple1.contains(20));

            // Test Tuple3
            IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 30);
            assertTrue(tuple3.contains(10));
            assertTrue(tuple3.contains(20));
            assertTrue(tuple3.contains(30));
            assertFalse(tuple3.contains(40));

            // Test larger tuples
            IntTuple.IntTuple7 tuple7 = IntTuple.of(10, 20, 30, 40, 50, 60, 70);
            assertTrue(tuple7.contains(70));
            assertFalse(tuple7.contains(80));
        }

        @Test
        void contains_findsPresentElements_values_1_2_3() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // boolean has2 = tuple.contains(2);   // true
            // boolean has5 = tuple.contains(5);   // false
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(2));
            assertFalse(tuple.contains(5));

            // IntTuple.IntTuple2 pair = IntTuple.of(10, 10);
            // boolean hasTen = pair.contains(10);   // true
            // boolean hasOne = pair.contains(1);    // false
            IntTuple.IntTuple2 pair = IntTuple.of(10, 10);
            assertTrue(pair.contains(10));
            assertFalse(pair.contains(1));
        }

        @Test
        void contains_higherArityTrailingElements() {
            IntTuple.IntTuple7 tuple7 = IntTuple.of(10, 20, 30, 40, 50, 60, 70);
            IntTuple.IntTuple8 tuple8 = IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80);
            IntTuple.IntTuple9 tuple9 = IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80, 90);

            assertTrue(tuple7.contains(70));
            assertFalse(tuple7.contains(100));
            assertTrue(tuple8.contains(80));
            assertFalse(tuple8.contains(100));
            assertTrue(tuple9.contains(90));
            assertFalse(tuple9.contains(100));
        }

        @Test
        void contains_multiple_found() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertTrue(tuple.contains(10));
            assertTrue(tuple.contains(20));
            assertTrue(tuple.contains(30));
        }

        @Test
        void contains_multiple_notfound() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertFalse(tuple.contains(99));
        }

        @Test
        void contains_negatives() {
            IntTuple3 tuple = IntTuple.of(-10, 0, 10);
            assertTrue(tuple.contains(-10));
            assertTrue(tuple.contains(0));
            assertTrue(tuple.contains(10));
            assertFalse(tuple.contains(5));
        }

        @Test
        void contains_single_notfound() {
            IntTuple1 tuple = IntTuple.of(42);
            assertFalse(tuple.contains(99));
        }

        @Test
        void contains_tuple1_notFound() {
            IntTuple1 tuple = IntTuple.of(42);
            assertFalse(tuple.contains(100));
        }

        @Test
        void contains_tuple2_found() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.contains(2));
        }

        @Test
        void contains_tuple2_notFound() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertFalse(tuple.contains(5));
        }

        @Test
        void contains_tuple3_found() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(2));
        }

        @Test
        void contains_tuple3_notFound() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertFalse(tuple.contains(5));
        }

        @Test
        void contains_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(4));
            assertFalse(tuple.contains(5));
        }

        @Test
        void contains_tuple5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertTrue(tuple.contains(5));
            assertFalse(tuple.contains(10));
        }

        @Test
        void contains_tuple6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertTrue(tuple.contains(6));
            assertFalse(tuple.contains(7));
        }

        @Test
        void contains_tuple7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertTrue(tuple.contains(7));
            assertFalse(tuple.contains(8));
        }

        @Test
        void contains_tuple8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertTrue(tuple.contains(8));
            assertFalse(tuple.contains(9));
        }

        @Test
        void contains_tuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple.contains(9));
            assertFalse(tuple.contains(10));
        }

        @Test
        void containsTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertFalse(tuple.contains(1));
        }

        @Test
        void containsTuple1False() {
            IntTuple1 tuple = IntTuple.of(42);
            assertFalse(tuple.contains(41));
        }

        @Test
        void containsTuple1True() {
            IntTuple1 tuple = IntTuple.of(42);
            assertTrue(tuple.contains(42));
        }

        @Test
        void containsTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(2));
            assertTrue(tuple.contains(3));
            assertFalse(tuple.contains(4));
        }

        @Test
        void containsTuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple.contains(5));
            assertFalse(tuple.contains(10));
        }

        @Test
        void tuple0_contains() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertFalse(tuple.contains(1));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            int[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        void toArray_independence() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] array1 = tuple.toArray();
            int[] array2 = tuple.toArray();
            assertNotSame(array1, array2); // Should be independent copies
            assertArrayEquals(array1, array2);
        }

        @Test
        void toArray_modification() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            array[0] = 99;
            assertEquals(1, tuple._1);
        }

        @Test
        void toArray_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 10, 20, 30 }, array);
        }

        @Test
        void toArray_preservesElements() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(10, array[0]);
            assertEquals(20, array[1]);
            assertEquals(30, array[2]);

            // Ensure it's a copy
            array[0] = 100;
            assertEquals(10, tuple._1);
        }

        @Test
        void toArray_preservesElements_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1, array[0]);
            assertEquals(2, array[1]);
            assertEquals(3, array[2]);
        }

        @Test
        void toArray_tuple2AndTuple3_preservesElements() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // int[] array = tuple.toArray();   // [1, 2, 3]
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertArrayEquals(new int[] { 1, 2, 3 }, tuple.toArray());

            // IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
            // int[] pairArray = pair.toArray();   // [10, 20]
            IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
            assertArrayEquals(new int[] { 10, 20 }, pair.toArray());
        }

        @Test
        void toArray_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 42 }, array);
        }

        @Test
        void toArray_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 1, 2 }, array);
        }

        @Test
        void toArray_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 1, 2, 3 }, array);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            array[0] = 999;
            assertEquals(1, tuple._1);
        }

        @Test
        void toArrayTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            int[] array = tuple.toArray();
            assertEquals(0, array.length);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            IntList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        void toList_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
            assertEquals(30, list.get(2));
        }

        @Test
        void toList_preservesElements() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
            assertEquals(30, list.get(2));
        }

        @Test
        void toList_preservesElements_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1, list.get(0));
        }

        @Test
        void toList_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            IntList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42, list.get(0));
        }

        @Test
        void toList_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            IntList list = tuple.toList();
            assertEquals(2, list.size());
            assertEquals(1, list.get(0));
            assertEquals(2, list.get(1));
        }

        @Test
        void toList_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(3, list.get(2));
        }

        @Test
        void toListTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            IntList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1, list.get(0));
            assertEquals(2, list.get(1));
            assertEquals(3, list.get(2));
        }

        @Test
        void toListTuple4Through9() {
            IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
            IntList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(1, list4.get(0));
            assertEquals(4, list4.get(3));

            IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            IntList list5 = tuple5.toList();
            assertEquals(5, list5.size());
            assertEquals(5, list5.get(4));

            IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntList list6 = tuple6.toList();
            assertEquals(6, list6.size());
            assertEquals(6, list6.get(5));

            IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntList list7 = tuple7.toList();
            assertEquals(7, list7.size());
            assertEquals(7, list7.get(6));

            IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntList list8 = tuple8.toList();
            assertEquals(8, list8.size());
            assertEquals(8, list8.get(7));

            IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9, list9.get(8));
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity2_forEach_values_1_2() throws Exception {
            IntTuple2 tuple = IntTuple.of(1, 2);
            List<Integer> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(1, values.get(0).intValue());
            assertEquals(2, values.get(1).intValue());
        }

        @Test
        void arity3_forEach() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity3_forEach_values_1_2_3() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        void arity4_forEach() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(i -> count.incrementAndGet());
            assertEquals(0, count.get());
        }

        @Test
        void forEach_multipleElements() throws Exception {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            List<Integer> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void forEach_sum() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(sum::addAndGet);
            assertEquals(10, sum.get());
        }

        @Test
        void forEach_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(42, sum.get());
        }

        @Test
        void forEach_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(3, sum.get());
        }

        @Test
        void forEach_tuple2_values_10_20() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(2, visited.size());
            assertEquals(10, visited.get(0));
            assertEquals(20, visited.get(1));
        }

        @Test
        void forEach_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(6, sum.get());
        }

        @Test
        void forEach_tuple3_values_100_200_300() {
            IntTuple3 tuple = IntTuple.of(100, 200, 300);
            List<Integer> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(3, visited.size());
            assertEquals(100, visited.get(0));
            assertEquals(200, visited.get(1));
            assertEquals(300, visited.get(2));
        }

        @Test
        void forEach_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(10, sum.get());
        }

        @Test
        void forEach_tuple5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(5, count.get());
        }

        @Test
        void forEach_tuple6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(6, count.get());
        }

        @Test
        void forEach_tuple7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(7, count.get());
        }

        @Test
        void forEach_tuple8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(8, count.get());
        }

        @Test
        void forEach_tuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(9, count.get());
        }

        @Test
        void forEach_visitsElements() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] sum = { 0 };
            tuple.forEach(i -> sum[0] += i);
            assertEquals(60, sum[0]);

            // Test Tuple2 forEach
            IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            int[] count = { 0 };
            tuple2.forEach(i -> count[0]++);
            assertEquals(2, count[0]);
        }

        @Test
        void forEachTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(42, result.get(0).intValue());
        }

        @Test
        void forEachTuple2Override() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(10), result.get(0));
            assertEquals(Integer.valueOf(20), result.get(1));
        }

        @Test
        void forEachTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Integer.valueOf(1), result.get(0));
            assertEquals(Integer.valueOf(2), result.get(1));
            assertEquals(Integer.valueOf(3), result.get(2));
        }

        @Test
        void forEachTuple3Override() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Integer.valueOf(10), result.get(0));
            assertEquals(Integer.valueOf(20), result.get(1));
            assertEquals(Integer.valueOf(30), result.get(2));
        }

        @Test
        void forEachTuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Integer.valueOf(1), result.get(0));
            assertEquals(Integer.valueOf(2), result.get(1));
            assertEquals(Integer.valueOf(3), result.get(2));
            assertEquals(Integer.valueOf(4), result.get(3));
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            int sum = tuple.stream().sum();
            assertEquals(0, sum);
        }

        @Test
        void stream_filter() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            long count = tuple.stream().filter(i -> i > 2).count();
            assertEquals(2, count);
        }

        @Test
        void stream_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int sum = tuple.stream().sum();
            assertEquals(60, sum);
        }

        @Test
        void stream_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            int sum = tuple.stream().sum();
            assertEquals(42, sum);
        }

        @Test
        void stream_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            int sum = tuple.stream().sum();
            assertEquals(3, sum);
        }

        @Test
        void stream_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int sum = tuple.stream().sum();
            assertEquals(6, sum);
        }

        @Test
        void stream_visitsElements() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] array = tuple.stream().toArray();
            assertEquals(3, array.length);
            assertEquals(10, array[0]);
            assertEquals(20, array[1]);
            assertEquals(30, array[2]);

            long count = tuple.stream().filter(i -> i > 15).count();
            assertEquals(2, count);
        }

        @Test
        void stream_visitsElements_values_1_2_3() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // int sum = tuple.stream().sum();   // 6
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(6, tuple.stream().sum());
        }

        @Test
        void streamTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            IntStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            IntStream stream = tuple.stream();
            assertEquals(42, stream.sum());
        }

        @Test
        void streamTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntStream stream = tuple.stream();
            assertEquals(6, stream.sum());
        }

        @Test
        void streamTuple4Through9() {
            IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
            assertEquals(10, tuple4.stream().sum());

            IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(15, tuple5.stream().sum());

            IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(21, tuple6.stream().sum());

            IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(28, tuple7.stream().sum());

            IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(36, tuple8.stream().sum());

            IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(45, tuple9.stream().sum());
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_tuple2() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b) -> result.set(a * b));
            assertEquals(12, result.get());
        }

        @Test
        void accept_tuple2_values_3_4() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            List<Integer> captured = new ArrayList<>();
            tuple.accept((a, b) -> {
                captured.add(a);
                captured.add(b);
            });
            assertEquals(2, captured.size());
            assertEquals(3, captured.get(0));
            assertEquals(4, captured.get(1));
        }

        @Test
        void accept_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b, c) -> result.set(a + b + c));
            assertEquals(6, result.get());
        }

        @Test
        void accept_tuple3_values_1_2_3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> captured = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                captured.add(a);
                captured.add(b);
                captured.add(c);
            });
            assertEquals(3, captured.size());
            assertEquals(1, captured.get(0));
            assertEquals(2, captured.get(1));
            assertEquals(3, captured.get(2));
        }

        @Test
        void acceptConsumer() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(1, result.get(0));
            assertEquals(2, result.get(1));
            assertEquals(3, result.get(2));
        }

        @Test
        void arity2_accept() throws Exception {
            IntTuple2 tuple = IntTuple.of(3, 4);
            final int[] result = new int[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7, result[0]);
        }

        @Test
        void arity2_accept_biConsumer() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            final int[] result = new int[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6, result[0]);
        }

        @Test
        void arity3_accept_triConsumer() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2Accept() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            List<Integer> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(3), result.get(0));
            assertEquals(Integer.valueOf(4), result.get(1));
        }

        @Test
        void tuple3Accept() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Integer.valueOf(1), result.get(0));
            assertEquals(Integer.valueOf(2), result.get(1));
            assertEquals(Integer.valueOf(3), result.get(2));
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map() throws Exception {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Integer result = tuple.map((a, b) -> a * b);
            assertEquals(12, result.intValue());
        }

        @Test
        void arity2_map_biFunction() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Integer result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result.intValue());
        }

        @Test
        void arity3_map_triFunction() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void map_tuple2_addition() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            int result = tuple.map((a, b) -> a + b);
            assertEquals(30, result);
        }

        @Test
        void map_tuple3_sum() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(60, result);
        }

        @Test
        void tuple2Map() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(12, result);
        }

        @Test
        void tuple2MapToString() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            String result = tuple.map((a, b) -> a + "+" + b + "=" + (a + b));
            assertEquals("3+4=7", result);
        }

        @Test
        void tuple3Map() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result);
        }

        @Test
        void tuple3MapToString() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String result = tuple.map((a, b, c) -> a + "+" + b + "+" + c + "=" + (a + b + c));
            assertEquals("1+2+3=6", result);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity2_filter_notSatisfied() throws Exception {
            IntTuple2 tuple = IntTuple.of(1, 1);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        void arity2_filter_satisfied() throws Exception {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void arity3_filter() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filter_tuple2_fails() {
            IntTuple2 tuple = IntTuple.of(1, 1);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple2_noMatch() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple3_fails() {
            IntTuple3 tuple = IntTuple.of(1, 1, 1);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple3_noMatch() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple3_passes() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicate() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter(t -> t._1 == 1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter(t -> t._1 == 99);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterFalse() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            var result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            var result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3FilterFalse() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            IntTuple.IntTuple1 tuple1 = IntTuple.of(1);
            IntTuple.IntTuple1 tuple2 = IntTuple.of(1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity1_hashCode_values_5() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple1 tuple2 = IntTuple.of(5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            IntTuple.IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple.IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode_values_1_2() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            IntTuple.IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            IntTuple.IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple.IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            IntTuple.IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple.IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            IntTuple.IntTuple6 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple.IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            IntTuple.IntTuple7 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple.IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            IntTuple.IntTuple8 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple.IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            IntTuple.IntTuple9 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple.IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_consistency() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_differentValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(30, 40);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_empty() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertNotNull(tuple.hashCode());
        }

        @Test
        void hashCode_matchesValueContract() {
            IntTuple.IntTuple3 tuple1 = IntTuple.of(10, 20, 30);
            IntTuple.IntTuple3 tuple2 = IntTuple.of(10, 20, 30);
            IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 31);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

            // Test specific hashcodes for coverage
            IntTuple.IntTuple1 single = IntTuple.of(10);
            assertEquals(10, single.hashCode());

            IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
            assertEquals(31 * 10 + 20, pair.hashCode());
        }

        @Test
        void hashCode_sameValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple1_different() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(100);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple3() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple4() {
            IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple5() {
            IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(42);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @Test
        void arity1_equals() {
            IntTuple.IntTuple1 tuple1 = IntTuple.of(1);
            IntTuple.IntTuple1 tuple2 = IntTuple.of(1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_differentType() {
            IntTuple1 tuple = IntTuple.of(5);
            assertNotEquals("string", tuple);
        }

        @Test
        void arity1_equals_equal() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple1 tuple2 = IntTuple.of(5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_notEqual() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple1 tuple2 = IntTuple.of(10);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_null() {
            IntTuple1 tuple = IntTuple.of(5);
            assertNotEquals(null, tuple);
        }

        @Test
        void arity1_equals_same() {
            IntTuple1 tuple = IntTuple.of(5);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void arity2_equals() {
            IntTuple.IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple.IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity2_equals_values_1_2() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity3_equals() {
            IntTuple.IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity4_equals() {
            IntTuple.IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple.IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity5_equals() {
            IntTuple.IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple.IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity6_equals() {
            IntTuple.IntTuple6 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple.IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity7_equals() {
            IntTuple.IntTuple7 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple.IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity8_equals() {
            IntTuple.IntTuple8 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple.IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equals() {
            IntTuple.IntTuple9 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple.IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_comparesValues() {
            IntTuple.IntTuple3 tuple1 = IntTuple.of(10, 20, 30);
            IntTuple.IntTuple3 tuple2 = IntTuple.of(10, 20, 30);
            IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 31);
            IntTuple.IntTuple2 tuple4 = IntTuple.of(10, 20);

            assertTrue(tuple1.equals(tuple1));
            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(tuple4));
            assertFalse(tuple1.equals(null));
            assertFalse(tuple1.equals("not a tuple"));

            // Test specific equals for coverage
            IntTuple.IntTuple1 single1 = IntTuple.of(10);
            IntTuple.IntTuple1 single2 = IntTuple.of(10);
            IntTuple.IntTuple1 single3 = IntTuple.of(20);
            assertTrue(single1.equals(single2));
            assertFalse(single1.equals(single3));
            assertFalse(single1.equals("not a tuple"));

            IntTuple.IntTuple2 pair1 = IntTuple.of(10, 20);
            IntTuple.IntTuple2 pair2 = IntTuple.of(10, 20);
            IntTuple.IntTuple2 pair3 = IntTuple.of(10, 21);
            assertTrue(pair1.equals(pair2));
            assertFalse(pair1.equals(pair3));
        }

        @Test
        void equals_differentClass() {
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        void equals_differentTypes() {
            IntTuple1 tuple1 = IntTuple.of(10);
            IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_differentValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(30, 40);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_higherArityIdentityAndTypeMismatch() {
            IntTuple.IntTuple7 tuple7 = IntTuple.of(10, 20, 30, 40, 50, 60, 70);
            IntTuple.IntTuple8 tuple8 = IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80);
            IntTuple.IntTuple9 tuple9 = IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80, 90);

            assertTrue(tuple7.equals(tuple7));
            assertEquals(tuple7, IntTuple.of(10, 20, 30, 40, 50, 60, 70));
            assertNotEquals(tuple7, IntTuple.of(10, 20, 30, 40, 50, 60, 71));
            assertNotEquals(tuple7, "tuple");

            assertTrue(tuple8.equals(tuple8));
            assertEquals(tuple8, IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80));
            assertNotEquals(tuple8, IntTuple.of(10, 20, 30, 40, 50, 60, 70, 81));
            assertNotEquals(tuple8, "tuple");

            assertTrue(tuple9.equals(tuple9));
            assertEquals(tuple9, IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80, 90));
            assertNotEquals(tuple9, IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80, 91));
            assertNotEquals(tuple9, "tuple");
        }

        @Test
        void equals_null() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertNotEquals(tuple, null);
        }

        @Test
        void equals_otherObject() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertNotEquals(tuple, "10,20");
        }

        @Test
        void equals_sameInstance() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equals_sameValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_symmetric() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        void equals_tuple1_different() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(100);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple1_null() {
            IntTuple1 tuple = IntTuple.of(42);
            assertNotEquals(null, tuple);
        }

        @Test
        void equals_tuple1_same() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(42);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple1_sameObject() {
            IntTuple1 tuple = IntTuple.of(42);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equals_tuple2_different() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 3);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple3_different() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 4);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple3_same() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple4() {
            IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple5() {
            IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equalsDifferentClass() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        void equalsNull() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotEquals(null, tuple);
        }

        @Test
        void equalsSameObject() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equalsTuple1() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(42);
            IntTuple1 tuple3 = IntTuple.of(41);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple2() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            IntTuple2 tuple3 = IntTuple.of(1, 3);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple3() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple3 = IntTuple.of(1, 2, 4);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity0_toString() {
            IntTuple0 tuple = IntTuple.from(new int[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void arity1_toString() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity1_toString_values_5() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals("(5)", tuple.toString());
        }

        @Test
        void arity2_toString() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString_values_1_2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals("(1, 2)", tuple.toString());
        }

        @Test
        void arity3_toString() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_format() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        @Test
        void toString_formatsValue() {
            IntTuple.IntTuple0 empty = IntTuple.from(new int[0]);
            assertEquals("()", empty.toString());

            IntTuple.IntTuple1 single = IntTuple.of(10);
            assertEquals("(10)", single.toString());

            IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
            assertEquals("(10, 20)", pair.toString());

            IntTuple.IntTuple3 triple = IntTuple.of(10, 20, 30);
            assertEquals("(10, 20, 30)", triple.toString());
        }

        @Test
        void toString_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals("(10, 20, 30)", tuple.toString());
        }

        @Test
        void toString_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals("(42)", tuple.toString());
        }

        @Test
        void toString_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals("(1, 2, 3)", tuple.toString());
        }

        @Test
        void toStringTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.from(new int[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple0_toString() {
            IntTuple<?> tuple = IntTuple.from(null);
            assertEquals("()", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void arity4_statisticalOperations() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(1, tuple.min());
            assertEquals(4, tuple.max());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void baseMethods_inheritedImplementation() {
            final IntTuple.IntTuple4 tuple = IntTuple.of(4, 1, 3, 2);
            final int[] copy = tuple.toArray();
            copy[0] = 99;

            assertEquals(1, tuple.min());
            assertEquals(4, tuple.max());
            assertEquals(2, tuple.lowerMedian());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble());
            assertTrue(tuple.contains(3));
            assertFalse(tuple.contains(8));
            assertEquals(4, tuple.toArray()[0]);
            assertTrue(tuple.equals(IntTuple.of(4, 1, 3, 2)));
            assertFalse(tuple.equals(IntTuple.of(4, 1, 3, 9)));
            assertFalse(tuple.equals(null));
            assertEquals("(4, 1, 3, 2)", tuple.toString());
        }

        @Test
        void classLevelExamples() {
            // int min = triple.min();         // 1
            // int max = triple.max();         // 3
            // triple.average();  // OptionalDouble.of(2.0)
            IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
            assertEquals(1, triple.min());
            assertEquals(3, triple.max());
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        @Test
        void coverageRegression_defaultConstructorsAndCachedArrays() {
            IntTuple.IntTuple1 tuple1 = new IntTuple.IntTuple1();
            assertArrayEquals(new int[] { 0 }, tuple1.toArray());
            assertArrayEquals(new int[] { 0 }, tuple1.toArray());

            IntTuple.IntTuple2 tuple2 = new IntTuple.IntTuple2();
            assertArrayEquals(new int[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new int[] { 0, 0 }, tuple2.toArray());

            IntTuple.IntTuple3 tuple3 = new IntTuple.IntTuple3();
            assertArrayEquals(new int[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new int[] { 0, 0, 0 }, tuple3.toArray());

            IntTuple.IntTuple4 tuple4 = new IntTuple.IntTuple4();
            assertArrayEquals(new int[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0 }, tuple4.toArray());

            IntTuple.IntTuple5 tuple5 = new IntTuple.IntTuple5();
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0 }, tuple5.toArray());

            IntTuple.IntTuple6 tuple6 = new IntTuple.IntTuple6();
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());

            IntTuple.IntTuple7 tuple7 = new IntTuple.IntTuple7();
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());

            IntTuple.IntTuple8 tuple8 = new IntTuple.IntTuple8();
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());

            IntTuple.IntTuple9 tuple9 = new IntTuple.IntTuple9();
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
        }

        @Test
        void coverageRegression_emptyTupleBaseMethods() {
            IntTuple<?> empty = IntTuple.from((int[]) null);
            assertEquals(IntTuple.from(new int[0]), empty);
            assertFalse(empty.equals("int"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void defaultConstructors_zeroInitialization() {
            final IntTuple1 tuple1 = new IntTuple1();
            final IntTuple2 tuple2 = new IntTuple2();
            final IntTuple3 tuple3 = new IntTuple3();
            final IntTuple4 tuple4 = new IntTuple4();
            final IntTuple5 tuple5 = new IntTuple5();
            final IntTuple6 tuple6 = new IntTuple6();
            final IntTuple7 tuple7 = new IntTuple7();
            final IntTuple8 tuple8 = new IntTuple8();
            final IntTuple9 tuple9 = new IntTuple9();

            assertArrayEquals(new int[] { 0 }, tuple1.toArray());
            assertArrayEquals(new int[] { 0 }, tuple1.toArray());
            assertArrayEquals(new int[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new int[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new int[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new int[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
            assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
        }

        @Test
        void duplicates() {
            IntTuple3 tuple = IntTuple.of(5, 5, 5);
            assertEquals(5, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(5, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void extremeValues() {
            IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
            assertEquals(Integer.MIN_VALUE, tuple.min());
            assertEquals(Integer.MAX_VALUE, tuple.max());
            assertEquals(0, tuple.lowerMedian());
        }

        @Test
        void extremeValues_values_Integer_MIN_VALUE_0_Integer_MAX_VALUE() {
            IntTuple3 tuple = IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE);
            assertEquals(Integer.MIN_VALUE, tuple.min());
            assertEquals(Integer.MAX_VALUE, tuple.max());
            assertTrue(tuple.contains(0));
        }

        @Test
        void inheritedAggregateEmptyBehavior() {
            final IntTuple.IntTuple0 tuple = IntTuple.from(new int[0]);

            assertThrows(NoSuchElementException.class, tuple::min);
            assertThrows(NoSuchElementException.class, tuple::max);
            assertThrows(NoSuchElementException.class, tuple::median);
            assertTrue(tuple.average().isEmpty());
            assertEquals(0, tuple.sum());
        }

        @Test
        void largerArities_branchCoverage() {
            final IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
            final IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            final IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
            final IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            final IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            final IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

            assertTrue(tuple4.contains(2));
            assertTrue(tuple4.contains(3));
            assertTrue(tuple5.contains(4));
            assertTrue(tuple6.contains(5));
            assertTrue(tuple7.contains(6));
            assertTrue(tuple8.contains(7));
            assertTrue(tuple9.contains(8));
            assertFalse(tuple9.contains(10));

            assertTrue(tuple4.equals(tuple4));
            assertFalse(tuple4.equals("not a tuple"));
            assertFalse(tuple4.equals(IntTuple.of(1, 2, 3, 9)));
            assertTrue(tuple5.equals(tuple5));
            assertFalse(tuple5.equals("not a tuple"));
            assertFalse(tuple5.equals(IntTuple.of(1, 2, 3, 4, 9)));
            assertTrue(tuple6.equals(tuple6));
            assertFalse(tuple6.equals("not a tuple"));
            assertFalse(tuple6.equals(IntTuple.of(1, 2, 3, 4, 5, 9)));
            assertTrue(tuple7.equals(tuple7));
            assertFalse(tuple7.equals("not a tuple"));
            assertFalse(tuple7.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 9)));
            assertTrue(tuple8.equals(tuple8));
            assertFalse(tuple8.equals("not a tuple"));
            assertFalse(tuple8.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7, 9)));
            assertTrue(tuple9.equals(tuple9));
            assertFalse(tuple9.equals("not a tuple"));
            assertFalse(tuple9.equals(IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 10)));
        }

        @Test
        void largeValues() {
            IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE / 2);
            assertEquals(Integer.MAX_VALUE / 2, tuple.min());
            assertEquals(Integer.MAX_VALUE, tuple.max());
        }

        @Test
        void mixedSignes() {
            IntTuple5 tuple = IntTuple.of(-100, -50, 0, 50, 100);
            assertEquals(-100, tuple.min());
            assertEquals(100, tuple.max());
            assertEquals(0, tuple.sum());
            assertEquals(0.0, tuple.average().getAsDouble());
            assertTrue(tuple.contains(-100));
            assertTrue(tuple.contains(100));
        }

        @Test
        void mixedValues() {
            IntTuple3 tuple = IntTuple.of(-100, 0, 100);
            assertEquals(-100, tuple.min());
            assertEquals(100, tuple.max());
            assertEquals(0, tuple.sum());
        }

        @Test
        void negativeValues() {
            IntTuple3 tuple = IntTuple.of(-1, -2, -3);
            assertEquals(-3, tuple.min());
            assertEquals(-1, tuple.max());
            assertEquals(-6, tuple.sum());
        }

        @Test
        void reverse() {
            // IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
            // IntTuple.IntTuple2 reversedPair = pair.reversed();   // (2, 1)
            IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
            IntTuple.IntTuple2 reversed2 = pair.reversed();
            assertEquals(2, reversed2._1);
            assertEquals(1, reversed2._2);

            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // IntTuple.IntTuple3 reversed = tuple.reversed();   // (3, 2, 1)
            IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 reversed3 = tuple3.reversed();
            assertEquals(3, reversed3._1);
            assertEquals(2, reversed3._2);
            assertEquals(1, reversed3._3);
        }

        @Test
        void reverse_largerTuples() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            IntTuple4 reversed = tuple.reversed();
            assertEquals(4, reversed._1);
            assertEquals(3, reversed._2);
            assertEquals(2, reversed._3);
            assertEquals(1, reversed._4);
        }

        @Test
        void singleElement_allOperations() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.min());
            assertEquals(42, tuple.max());
            assertEquals(42, tuple.lowerMedian());
            assertEquals(42.0, tuple.average().getAsDouble());
            assertEquals(42, tuple.sum());
            assertTrue(tuple.contains(42));
            assertFalse(tuple.contains(99));
            IntTuple1 reversed = tuple.reversed();
            assertEquals(42, reversed._1);
        }

        @Test
        void statistics_allOperations() {
            IntTuple5 tuple = IntTuple.of(100, 500, 300, 200, 400);
            assertEquals(100, tuple.min());
            assertEquals(500, tuple.max());
            assertEquals(300, tuple.lowerMedian());
            assertEquals(1500, tuple.sum());
            assertEquals(300.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple1SpecificMethods() {
            IntTuple.IntTuple1 tuple = IntTuple.of(10);

            // Test min/max/median/sum/average
            assertEquals(10, tuple.min());
            assertEquals(10, tuple.max());
            assertEquals(10, tuple.lowerMedian());
            assertEquals(10, tuple.sum());
            assertEquals(10.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple2SpecificMethods() {
            IntTuple.IntTuple2 tuple = IntTuple.of(10, 20);

            // Test accept
            int[] sum = { 0 };
            tuple.accept((a, b) -> sum[0] = a + b);
            assertEquals(30, sum[0]);

            // Test map
            int result = tuple.map((a, b) -> a + b);
            assertEquals(30, result);

            // Test filter
            assertTrue(tuple.filter((a, b) -> a < b).isPresent());
            assertFalse(tuple.filter((a, b) -> a > b).isPresent());
        }

        @Test
        void tuple3SpecificMethods() {
            IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);

            // Test accept
            int[] sum = { 0 };
            tuple.accept((a, b, c) -> sum[0] = a + b + c);
            assertEquals(60, sum[0]);

            // Test map
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(60, result);

            // Test filter
            assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
        }

        @Test
        void tuple4Operations() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);

            // Test reverse
            IntTuple4 reversed = tuple.reversed();
            assertEquals(4, reversed._1);
            assertEquals(3, reversed._2);
            assertEquals(2, reversed._3);
            assertEquals(1, reversed._4);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(4));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4 }, tuple.toArray());

            // Test min/max/median/sum/average
            assertEquals(1, tuple.min());
            assertEquals(4, tuple.max());
            assertEquals(2, tuple.lowerMedian());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            IntTuple4 tuple3 = IntTuple.of(1, 2, 3, 5);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4)", tuple.toString());
        }

        @Test
        void tuple5_operations() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(5, tuple.arity());
            assertEquals(1, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(3, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);
            assertTrue(tuple.contains(3));
            assertFalse(tuple.contains(10));
        }

        @Test
        void tuple5Operations() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);

            // Test reverse
            IntTuple5 reversed = tuple.reversed();
            assertEquals(5, reversed._1);
            assertEquals(4, reversed._2);
            assertEquals(3, reversed._3);
            assertEquals(2, reversed._4);
            assertEquals(1, reversed._5);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(5));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(3, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 tuple3 = IntTuple.of(1, 2, 3, 4, 6);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5)", tuple.toString());
        }

        @Test
        void tuple6_operations() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(6, tuple.arity());
            assertEquals(1, tuple.min());
            assertEquals(6, tuple.max());
            assertEquals(21, tuple.sum());
        }

        @Test
        void tuple6Operations() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);

            // Test reverse
            IntTuple6 reversed = tuple.reversed();
            assertEquals(6, reversed._1);
            assertEquals(5, reversed._2);
            assertEquals(4, reversed._3);
            assertEquals(3, reversed._4);
            assertEquals(2, reversed._5);
            assertEquals(1, reversed._6);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(6));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(6, tuple.max());
            assertEquals(3, tuple.lowerMedian());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple6 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 7);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6)", tuple.toString());
        }

        @Test
        void tuple7_operations() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(7, tuple.arity());
            assertEquals(4, tuple.lowerMedian());
            assertEquals(28, tuple.sum());
        }

        @Test
        void tuple7Operations() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);

            // Test reverse
            IntTuple7 reversed = tuple.reversed();
            assertEquals(7, reversed._1);
            assertEquals(6, reversed._2);
            assertEquals(5, reversed._3);
            assertEquals(4, reversed._4);
            assertEquals(3, reversed._5);
            assertEquals(2, reversed._6);
            assertEquals(1, reversed._7);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(7));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(7, tuple.max());
            assertEquals(4, tuple.lowerMedian());
            assertEquals(28, tuple.sum());
            assertEquals(4.0, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple7 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 8);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6, 7)", tuple.toString());
        }

        @Test
        void tuple8_operations() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.max());
            assertEquals(36, tuple.sum());
        }

        @Test
        void tuple8Operations() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);

            // Test reverse
            IntTuple8 reversed = tuple.reversed();
            assertEquals(8, reversed._1);
            assertEquals(7, reversed._2);
            assertEquals(6, reversed._3);
            assertEquals(5, reversed._4);
            assertEquals(4, reversed._5);
            assertEquals(3, reversed._6);
            assertEquals(2, reversed._7);
            assertEquals(1, reversed._8);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(8));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(8, tuple.max());
            assertEquals(4, tuple.lowerMedian());
            assertEquals(36, tuple.sum());
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple8 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 9);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6, 7, 8)", tuple.toString());
        }

        @Test
        void tuple9_operations() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(9, tuple.arity());
            assertEquals(5, tuple.lowerMedian());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9Operations() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

            // Test reverse
            IntTuple9 reversed = tuple.reversed();
            assertEquals(9, reversed._1);
            assertEquals(8, reversed._2);
            assertEquals(7, reversed._3);
            assertEquals(6, reversed._4);
            assertEquals(5, reversed._5);
            assertEquals(4, reversed._6);
            assertEquals(3, reversed._7);
            assertEquals(2, reversed._8);
            assertEquals(1, reversed._9);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(9));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(9, tuple.max());
            assertEquals(5, tuple.lowerMedian());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple9 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 10);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6, 7, 8, 9)", tuple.toString());
        }

        @Test
        void zeroes() {
            IntTuple4 tuple = IntTuple.of(0, 0, 0, 0);
            assertEquals(0, tuple.min());
            assertEquals(0, tuple.max());
            assertEquals(0, tuple.sum());
            assertEquals(0.0, tuple.average().getAsDouble());
            assertTrue(tuple.contains(0));
        }

        @Test
        void zeroValues() {
            IntTuple3 tuple = IntTuple.of(0, 0, 0);
            assertEquals(0, tuple.min());
            assertEquals(0, tuple.max());
            assertEquals(0, tuple.sum());
        }
    }
}
