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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.landawn.abacus.util.BooleanTuple.BooleanTuple0;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple1;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple2;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple3;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple4;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple5;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple6;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple7;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple8;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.Stream;

class BooleanTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, boolean[] values) {
            BooleanTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of1() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(1, tuple.arity());
            assertTrue(tuple._1);
        }

        @Test
        void of1_values_true() {
            // BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            // boolean value = tuple._1;  // true
            BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
            assertTrue(t._1);
        }

        @Test
        void of2() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(2, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
        }

        @Test
        void of2_values_true_false() {
            // BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            // boolean first = tuple._1;  // true
            // boolean second = tuple._2;  // false
            BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
            assertTrue(t._1);
            assertFalse(t._2);
        }

        @Test
        void of3() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(3, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
        }

        @Test
        void of3_values_true_false_true() {
            // BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            // boolean third = tuple._3;  // true
            BooleanTuple.BooleanTuple3 t = BooleanTuple.of(true, false, true);
            assertTrue(t._3);
        }

        @Test
        void of4() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
            assertFalse(tuple._4);
        }

        @Test
        void of4Reverse() {
            // BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            // boolean first = tuple._1;  // true
            // boolean fourth = tuple._4;  // false
            // BooleanTuple.BooleanTuple4 reversed = tuple.reversed();   // (false, true, false, true)
            BooleanTuple.BooleanTuple4 t = BooleanTuple.of(true, false, true, false);
            assertTrue(t._1);
            assertFalse(t._4);
            BooleanTuple.BooleanTuple4 reversed = t.reversed();
            assertFalse(reversed._1);
            assertTrue(reversed._2);
            assertFalse(reversed._3);
            assertTrue(reversed._4);
        }

        @Test
        void of5() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
            assertFalse(tuple._4);
            assertTrue(tuple._5);
        }

        @Test
        void of5Contains() {
            // BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            // boolean first = tuple._1;  // true
            // boolean fifth = tuple._5;  // true
            // boolean contains = tuple.contains(false);   // true
            BooleanTuple.BooleanTuple5 t = BooleanTuple.of(true, false, true, false, true);
            assertTrue(t._1);
            assertTrue(t._5);
            assertTrue(t.contains(false));
        }

        @Test
        void of6() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
            assertFalse(tuple._4);
            assertTrue(tuple._5);
            assertFalse(tuple._6);
        }

        @Test
        void of7() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
            assertFalse(tuple._4);
            assertTrue(tuple._5);
            assertFalse(tuple._6);
            assertTrue(tuple._7);
        }

        @Test
        void of8() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
            assertFalse(tuple._4);
            assertTrue(tuple._5);
            assertFalse(tuple._6);
            assertTrue(tuple._7);
            assertFalse(tuple._8);
        }

        @Test
        void of9() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple.arity());
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
            assertFalse(tuple._4);
            assertTrue(tuple._5);
            assertFalse(tuple._6);
            assertTrue(tuple._7);
            assertFalse(tuple._8);
            assertTrue(tuple._9);
        }

        @Test
        void of9Arity() {
            // BooleanTuple.BooleanTuple9 tuple = ...
            // boolean first = tuple._1;  // true
            // boolean ninth = tuple._9;  // true
            // int arity = tuple.arity();   // 9
            BooleanTuple.BooleanTuple9 t = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertTrue(t._1);
            assertTrue(t._9);
            assertEquals(9, t.arity());
        }

        private static BooleanTuple<?> createWithOf(boolean[] values) {
            return switch (values.length) {
                case 1 -> BooleanTuple.of(values[0]);
                case 2 -> BooleanTuple.of(values[0], values[1]);
                case 3 -> BooleanTuple.of(values[0], values[1], values[2]);
                case 4 -> BooleanTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> BooleanTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: true", new boolean[] { true }), Arguments.of("2: true, false", new boolean[] { true, false }),
                    Arguments.of("3: true, false, true", new boolean[] { true, false, true }),
                    Arguments.of("4: true, false, true, false", new boolean[] { true, false, true, false }),
                    Arguments.of("5: true, false, true, false, true", new boolean[] { true, false, true, false, true }),
                    Arguments.of("6: true, false, true, false, true, false", new boolean[] { true, false, true, false, true, false }),
                    Arguments.of("7: true, false, true, false, true, false, true", new boolean[] { true, false, true, false, true, false, true }),
                    Arguments.of("8: true, false, true, false, true, false, true, false", new boolean[] { true, false, true, false, true, false, true, false }),
                    Arguments.of("9: true, false, true, false, true, false, true, false, true",
                            new boolean[] { true, false, true, false, true, false, true, false, true }));
        }

        private static boolean[] publicFields(BooleanTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    BooleanTuple.BooleanTuple1 value = (BooleanTuple.BooleanTuple1) tuple;
                    yield new boolean[] { value._1 };
                }
                case 2 -> {
                    BooleanTuple.BooleanTuple2 value = (BooleanTuple.BooleanTuple2) tuple;
                    yield new boolean[] { value._1, value._2 };
                }
                case 3 -> {
                    BooleanTuple.BooleanTuple3 value = (BooleanTuple.BooleanTuple3) tuple;
                    yield new boolean[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    BooleanTuple.BooleanTuple4 value = (BooleanTuple.BooleanTuple4) tuple;
                    yield new boolean[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    BooleanTuple.BooleanTuple5 value = (BooleanTuple.BooleanTuple5) tuple;
                    yield new boolean[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    BooleanTuple.BooleanTuple6 value = (BooleanTuple.BooleanTuple6) tuple;
                    yield new boolean[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    BooleanTuple.BooleanTuple7 value = (BooleanTuple.BooleanTuple7) tuple;
                    yield new boolean[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    BooleanTuple.BooleanTuple8 value = (BooleanTuple.BooleanTuple8) tuple;
                    yield new boolean[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    BooleanTuple.BooleanTuple9 value = (BooleanTuple.BooleanTuple9) tuple;
                    yield new boolean[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
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
                boolean[] array = new boolean[i];
                BooleanTuple<?> tuple = BooleanTuple.from(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        void create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                BooleanTuple.from(new boolean[10]);
            });
        }

        @Test
        void create_emptyArray() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_nullArray() {
            BooleanTuple0 tuple = BooleanTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_array4() {
            BooleanTuple4 tuple = BooleanTuple.from(new boolean[] { true, false, true, false });
            assertEquals(4, tuple.arity());
        }

        @Test
        void from_array5() {
            BooleanTuple5 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true });
            assertEquals(5, tuple.arity());
        }

        @Test
        void from_array6() {
            BooleanTuple6 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true, false });
            assertEquals(6, tuple.arity());
        }

        @Test
        void from_array7() {
            BooleanTuple7 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true });
            assertEquals(7, tuple.arity());
        }

        @Test
        void from_array8() {
            BooleanTuple8 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true, false });
            assertEquals(8, tuple.arity());
        }

        @Test
        void from_array9() {
            BooleanTuple9 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_emptyArray() {
            BooleanTuple<?> tuple = BooleanTuple.from(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nineElements() {
            BooleanTuple9 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._9);
        }

        @Test
        void from_nullArray() {
            BooleanTuple<?> tuple = BooleanTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_singleElement() {
            BooleanTuple1 tuple = BooleanTuple.from(new boolean[] { true });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(true, tuple._1);
        }

        @Test
        void from_threeElements() {
            BooleanTuple3 tuple = BooleanTuple.from(new boolean[] { true, false, true });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> BooleanTuple.from(new boolean[10]));
        }

        @Test
        void from_twoElements() {
            BooleanTuple2 tuple = BooleanTuple.from(new boolean[] { true, false });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
        }

        @Test
        void from1() {
            BooleanTuple1 tuple = BooleanTuple.from(new boolean[] { true });
            assertEquals(true, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2Through8() {
            BooleanTuple2 tuple2 = BooleanTuple.from(new boolean[] { true, false });
            assertEquals(true, tuple2._1);
            assertEquals(false, tuple2._2);

            BooleanTuple4 tuple4 = BooleanTuple.from(new boolean[] { true, false, true, false });
            assertEquals(true, tuple4._1);
            assertEquals(false, tuple4._4);

            BooleanTuple5 tuple5 = BooleanTuple.from(new boolean[] { true, false, true, false, true });
            assertEquals(true, tuple5._1);
            assertEquals(true, tuple5._5);

            BooleanTuple6 tuple6 = BooleanTuple.from(new boolean[] { true, false, true, false, true, false });
            assertEquals(true, tuple6._1);
            assertEquals(false, tuple6._6);

            BooleanTuple7 tuple7 = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true });
            assertEquals(true, tuple7._1);
            assertEquals(true, tuple7._7);

            BooleanTuple8 tuple8 = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true, false });
            assertEquals(true, tuple8._1);
            assertEquals(false, tuple8._8);
        }

        @Test
        void from3() {
            BooleanTuple3 tuple = BooleanTuple.from(new boolean[] { true, false, true });
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
        }

        @Test
        void from9() {
            BooleanTuple9 tuple = BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._9);
        }

        @Test
        void fromEmpty() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromNull() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                BooleanTuple.from(new boolean[] { true, false, true, false, true, false, true, false, true, false });
            });
        }
    }

    @Nested
    class ArityTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("arityCases")
        void arity_inputCases_returnsElementCount(String scenario, BooleanTuple<?> tuple, int expected) {
            assertEquals(expected, tuple.arity(), scenario);
        }

        private static java.util.stream.Stream<Arguments> arityCases() {
            return java.util.stream.Stream.of(Arguments.of("from(new boolean[0])", BooleanTuple.from(new boolean[0]), 0),
                    Arguments.of("of(true)", BooleanTuple.of(true), 1), Arguments.of("of(true, false)", BooleanTuple.of(true, false), 2),
                    Arguments.of("of(true, false, true)", BooleanTuple.of(true, false, true), 3),
                    Arguments.of("of(true, false, true, false)", BooleanTuple.of(true, false, true, false), 4),
                    Arguments.of("of(true, false, true, false, true)", BooleanTuple.of(true, false, true, false, true), 5),
                    Arguments.of("of(true, false, true, false, true, false)", BooleanTuple.of(true, false, true, false, true, false), 6),
                    Arguments.of("of(true, false, true, false, true, false, true)", BooleanTuple.of(true, false, true, false, true, false, true), 7),
                    Arguments.of("of(true, false, true, false, true, false, true, false)", BooleanTuple.of(true, false, true, false, true, false, true, false),
                            8),
                    Arguments.of("of(true, false, true, false, true, false, true, false, true)",
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true), 9));
        }

    }

    @Nested
    class ReversedTests {

        @Test
        void arity1_reversed() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple.BooleanTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity2_reversed() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple.BooleanTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity3_reversed() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity4_reversed() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple.BooleanTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple.BooleanTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple9 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        void reversed_reversesElementOrder() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
            BooleanTuple.BooleanTuple3 reversed = tuple.reversed();

            assertFalse(reversed._1);
            assertFalse(reversed._2);
            assertTrue(reversed._3);
        }

        @Test
        void reversedTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            BooleanTuple<BooleanTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversedTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple1 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
        }

        @Test
        void reversedTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple3 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        @Test
        void tuple0_reversed() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            BooleanTuple0 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(tuple, reversed);
        }

        @Test
        void tuple0_reversed_values_BooleanTuple_from() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            BooleanTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void tuple1_reversed_values_true() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple1 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals(true, reversed._1);
        }

        @Test
        void tuple2_reversed_values_true_false() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple2 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        void tuple3_reversed() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
            BooleanTuple3 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        @Test
        void tuple3_reversed_values_true_false_true() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple3 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        @Test
        void tuple4_reversed() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            BooleanTuple4 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);
        }

        @Test
        void tuple5_reversed() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple5 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);
        }

        @Test
        void tuple5_reversed_values_true_false_true_false_true() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple5 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);
        }

        @Test
        void tuple6_reversed() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple6 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        void tuple7_reversed() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple7 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
        }

        @Test
        void tuple8_reversed() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple8 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        void tuple9_reversed() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple9 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
        }

        @Test
        void reversed_highArities_preservesReverseOrder() {
            BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 reversed4 = tuple4.reversed();

            assertEquals(false, reversed4._1);
            assertEquals(true, reversed4._2);
            assertEquals(false, reversed4._3);
            assertEquals(true, reversed4._4);

            BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 reversed5 = tuple5.reversed();

            assertEquals(true, reversed5._1);
            assertEquals(false, reversed5._2);
            assertEquals(true, reversed5._3);
            assertEquals(false, reversed5._4);
            assertEquals(true, reversed5._5);
        }
    }

    @Nested
    class ContainsTests {

        @ParameterizedTest(name = "arity {0}: contains checks every component")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void contains_checksEveryComponentAndRejectsMissingValue(int arity) {
            for (int position = 0; position < arity; position++) {
                boolean[] values = new boolean[arity];
                values[position] = true;
                assertTrue(BooleanTuple.from(values).contains(true), "position " + position);
            }
            assertFalse(BooleanTuple.from(new boolean[arity]).contains(true));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("containsCases")
        void contains_inputCases_matchExpectedMembership(String scenario, BooleanTuple<?> tuple, boolean[] present, boolean[] absent) {
            for (boolean value : present) {
                assertTrue(tuple.contains(value), scenario + ": expected to contain " + value);
            }
            for (boolean value : absent) {
                assertFalse(tuple.contains(value), scenario + ": expected to exclude " + value);
            }
        }

        private static java.util.stream.Stream<Arguments> containsCases() {
            return java.util.stream.Stream.of(
                    Arguments.of("from(new boolean[0])", BooleanTuple.from(new boolean[0]), new boolean[] {}, new boolean[] { true, false }),
                    Arguments.of("of(false)", BooleanTuple.of(false), new boolean[] { false }, new boolean[] { true }),
                    Arguments.of("of(true)", BooleanTuple.of(true), new boolean[] { true }, new boolean[] { false }),
                    Arguments.of("of(true, false)", BooleanTuple.of(true, false), new boolean[] { true, false }, new boolean[] {}),
                    Arguments.of("of(true, true)", BooleanTuple.of(true, true), new boolean[] { true }, new boolean[] { false }),
                    Arguments.of("of(true, false, true)", BooleanTuple.of(true, false, true), new boolean[] { true, false }, new boolean[] {}),
                    Arguments.of("of(false, false, false, false)", BooleanTuple.of(false, false, false, false), new boolean[] {}, new boolean[] { true }),
                    Arguments.of("of(false, false, false, true)", BooleanTuple.of(false, false, false, true), new boolean[] { true }, new boolean[] {}),
                    Arguments.of("of(false, false, true, false)", BooleanTuple.of(false, false, true, false), new boolean[] { true }, new boolean[] {}),
                    Arguments.of("of(false, true, false, false)", BooleanTuple.of(false, true, false, false), new boolean[] { true }, new boolean[] {}),
                    Arguments.of("of(true, false, true, false)", BooleanTuple.of(true, false, true, false), new boolean[] { true, false }, new boolean[] {}),
                    Arguments.of("of(false, false, false, false, false)", BooleanTuple.of(false, false, false, false, false), new boolean[] {},
                            new boolean[] { true }),
                    Arguments.of("of(false, false, false, false, true)", BooleanTuple.of(false, false, false, false, true), new boolean[] { true },
                            new boolean[] {}),
                    Arguments.of("of(false, false, false, true, false)", BooleanTuple.of(false, false, false, true, false), new boolean[] { true },
                            new boolean[] {}),
                    Arguments.of("of(false, false, true, false, false)", BooleanTuple.of(false, false, true, false, false), new boolean[] { true },
                            new boolean[] {}),
                    Arguments.of("of(false, true, false, false, false)", BooleanTuple.of(false, true, false, false, false), new boolean[] { true },
                            new boolean[] {}),
                    Arguments.of("of(true, false, true, false, true)", BooleanTuple.of(true, false, true, false, true), new boolean[] { true, false },
                            new boolean[] {}),
                    Arguments.of("of(true, true, true, true, true)", BooleanTuple.of(true, true, true, true, true), new boolean[] { true },
                            new boolean[] { false }),
                    Arguments.of("of(true, false, true, false, true, false)", BooleanTuple.of(true, false, true, false, true, false),
                            new boolean[] { true, false }, new boolean[] {}),
                    Arguments.of("of(true, false, true, false, true, false, true)", BooleanTuple.of(true, false, true, false, true, false, true),
                            new boolean[] { true, false }, new boolean[] {}),
                    Arguments.of("of(true, true, true, true, true, true, true)", BooleanTuple.of(true, true, true, true, true, true, true),
                            new boolean[] { true }, new boolean[] { false }),
                    Arguments.of("of(true, false, true, false, true, false, true, false)", BooleanTuple.of(true, false, true, false, true, false, true, false),
                            new boolean[] { true, false }, new boolean[] {}),
                    Arguments.of("of(true, false, true, false, true, false, true, false, true)",
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true), new boolean[] { true, false }, new boolean[] {}));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_immutability() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean[] array1 = tuple.toArray();
            boolean[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
        }

        @Test
        void toArray_independence() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            array[0] = false;
            assertEquals(true, tuple._1); // Tuple should be unaffected
        }

        @Test
        void toArray_preservesElements() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();

            assertEquals(3, array.length);
            assertTrue(array[0]);
            assertFalse(array[1]);
            assertTrue(array[2]);
        }

        @Test
        void toArray_preservesElements_values_true_false_true() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false, true }, array);

            // Verify it's a copy
            array[0] = false;
            assertEquals(true, tuple._1);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            array[0] = true;
            assertEquals(true, tuple._1);
        }

        @Test
        void toArrayTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            boolean[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void tuple0_toArray() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        void tuple1_toArray() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(1, array.length);
            assertEquals(true, array[0]);
        }

        @Test
        void tuple1_toArray_values_true() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true }, array);
        }

        @Test
        void tuple2_toArray() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(2, array.length);
            assertArrayEquals(new boolean[] { true, false }, array);
        }

        @Test
        void tuple2_toArray_values_true_false() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false }, array);
        }

        @Test
        void tuple3_toArray() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertArrayEquals(new boolean[] { true, false, true }, array);
        }

        @Test
        void tuple3_toArray_values_true_false_true() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false, true }, array);
        }

        @Test
        void toArray_highArities_preservesEveryComponent() {
            // Test all tuple arities 4-9 for basic functionality
            BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple4.arity());
            assertArrayEquals(new boolean[] { true, false, true, false }, tuple4.toArray());

            BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple5.arity());
            assertArrayEquals(new boolean[] { true, false, true, false, true }, tuple5.toArray());

            BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple6.arity());
            assertArrayEquals(new boolean[] { true, false, true, false, true, false }, tuple6.toArray());

            BooleanTuple.BooleanTuple7 tuple7 = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple7.arity());
            assertArrayEquals(new boolean[] { true, false, true, false, true, false, true }, tuple7.toArray());

            BooleanTuple.BooleanTuple8 tuple8 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple8.arity());
            assertArrayEquals(new boolean[] { true, false, true, false, true, false, true, false }, tuple8.toArray());

            BooleanTuple.BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple9.arity());
            assertArrayEquals(new boolean[] { true, false, true, false, true, false, true, false, true }, tuple9.toArray());
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_independence() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            list.set(0, false);
            assertEquals(true, tuple._1); // Tuple should be unaffected
        }

        @Test
        void toListTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            BooleanList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(true, list.get(0));
        }

        @Test
        void toListTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
            assertEquals(true, list.get(2));
        }

        @Test
        void toListTuple4Through9() {
            BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            BooleanList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(false, list4.get(3));

            BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(true, list9.get(8));
        }

        @Test
        void tuple0_toList() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        void tuple0_toList_values_BooleanTuple_from() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        void tuple1_toList() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(true, list.get(0));
        }

        @Test
        void tuple2_toList() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
        }

        @Test
        void tuple2_toList_values_true_false() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        void tuple3_toList() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
            assertEquals(true, list.get(2));
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity3_forEach() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity4_forEach() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_withException() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(b -> {
                    if (!b) {
                        throw new RuntimeException("test exception");
                    }
                });
            });
        }

        @Test
        void forEachTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
        }

        @Test
        void forEachTuple2Override() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
            assertEquals(Boolean.valueOf(false), result.get(1));
        }

        @Test
        void forEachTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
            assertEquals(Boolean.valueOf(false), result.get(1));
            assertEquals(Boolean.valueOf(true), result.get(2));
        }

        @Test
        void forEachTuple3Override() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Boolean.valueOf(true), result.get(2));
        }

        @Test
        void forEachTuple4() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Boolean.valueOf(false), result.get(3));
        }

        @Test
        void tuple0_forEach() throws Exception {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(0, results.size());
        }

        @Test
        void tuple0_forEach_values_BooleanTuple_from() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            List<Boolean> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        void tuple1_forEach() throws Exception {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(1, results.size());
            assertEquals(true, results.get(0));
        }

        @Test
        void tuple2_forEach() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(true, values.get(0));
            assertEquals(false, values.get(1));
        }

        @Test
        void tuple3_forEach_values_true_false_true() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(3, results.size());
            assertEquals(true, results.get(0));
            assertEquals(false, results.get(1));
            assertEquals(true, results.get(2));
        }

        @Test
        void tuple4_forEach() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(4, values.size());
        }

        @Test
        void tuple5_forEach() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void tuple6_forEach() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(6, values.size());
        }

        @Test
        void tuple7_forEach() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(7, values.size());
        }

        @Test
        void tuple8_forEach() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(8, values.size());
        }

        @Test
        void tuple9_forEach() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(9, values.size());
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_operations() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            long trueCount = tuple.stream().filter(b -> b).count();
            assertEquals(2, trueCount);
        }

        @Test
        void stream_visitsElements() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            long trueCount = tuple.stream().filter(b -> b).count();

            assertEquals(2, trueCount);
        }

        @Test
        void stream_visitsElements_values_true_false_true() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            long count = tuple.stream().filter(b -> b).count();
            assertEquals(2, count);
        }

        @Test
        void streamOperationsOnHighArityTuples() {
            BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, true, false);

            long trueCount = tuple5.stream().filter(b -> b).count();
            assertEquals(3, trueCount);

            long falseCount = tuple5.stream().filter(b -> !b).count();
            assertEquals(2, falseCount);
        }

        @Test
        void streamTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            Stream<Boolean> stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            Stream<Boolean> stream = tuple.stream();
            assertEquals(1, stream.count());
        }

        @Test
        void streamTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Stream<Boolean> stream = tuple.stream();
            assertEquals(3, stream.count());
        }

        @Test
        void streamTuple4Through9() {
            BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple4.stream().count());

            BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple9.stream().count());
        }

        @Test
        void tuple0_stream() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        void tuple1_stream() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            long count = tuple.stream().count();
            assertEquals(1, count);
        }

        @Test
        void tuple1_stream_values_true() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            long trueCount = tuple.stream().filter(b -> b).count();
            assertEquals(1, trueCount);
        }

        @Test
        void tuple2_stream() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            long count = tuple.stream().count();
            assertEquals(2, count);
        }

        @Test
        void tuple2_stream_filter() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            long trueCount = tuple.stream().filter(b -> b).count();
            assertEquals(1, trueCount);
        }

        @Test
        void tuple3_stream_allMatch() {
            BooleanTuple3 tuple = BooleanTuple.of(true, true, true);
            boolean allTrue = tuple.stream().allMatch(b -> b);
            assertTrue(allTrue);
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void acceptConsumer() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertTrue(result.get(0));
            assertFalse(result.get(1));
            assertTrue(result.get(2));
        }

        @Test
        void arity2_accept_biConsumer() {
            {
                BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
                List<Integer> count = new ArrayList<>();
                tuple.accept((a, b) -> count.add(1));
                assertEquals(1, count.size());
            }
        }

        @Test
        void arity3_accept_triConsumer() {
            {
                BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
                List<Integer> count = new ArrayList<>();
                tuple.accept((a, b, c) -> count.add(1));
                assertEquals(1, count.size());
            }
        }

        @Test
        void tuple2_accept() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<String> results = new ArrayList<>();
            tuple.accept((a, b) -> results.add(a + "," + b));
            assertEquals(1, results.size());
            assertEquals("true,false", results.get(0));
        }

        @Test
        void tuple2_accept_values_true_false() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals(true, results.get(0));
            assertEquals(false, results.get(1));
        }

        @Test
        void tuple2Accept() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
            assertEquals(Boolean.valueOf(false), result.get(1));
        }

        @Test
        void tuple3_accept() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<String> results = new ArrayList<>();
            tuple.accept((a, b, c) -> results.add(a + "," + b + "," + c));
            assertEquals(1, results.size());
            assertEquals("true,false,true", results.get(0));
        }

        @Test
        void tuple3_accept_values_true_false_true() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals(true, results.get(0));
            assertEquals(false, results.get(1));
            assertEquals(true, results.get(2));
        }

        @Test
        void tuple3Accept() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
            assertEquals(Boolean.valueOf(false), result.get(1));
            assertEquals(Boolean.valueOf(true), result.get(2));
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map_biFunction() {
            {
                BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
                String result = tuple.map((a, b) -> "test");
                assertNotNull(result);
            }
        }

        @Test
        void arity3_map_triFunction() {
            {
                BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
                String result = tuple.map((a, b, c) -> "test");
                assertNotNull(result);
            }
        }

        @Test
        void callbackNullGuards_andNullMapResult() {
            BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);

            assertThrows(IllegalArgumentException.class, () -> tuple2.forEach(null));
            assertThrows(IllegalArgumentException.class, () -> tuple2.accept((Throwables.BooleanBiConsumer<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> tuple2.map((Throwables.BooleanBiFunction<String, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> tuple2.filter((Throwables.BooleanBiPredicate<RuntimeException>) null));

            Object mapped = tuple2.map((a, b) -> null);
            assertEquals(null, mapped);
        }

        @Test
        void classLevelMap() {
            // boolean result = triple.map((a, b, c) -> a || b || c);
            BooleanTuple.BooleanTuple3 triple = BooleanTuple.of(true, false, true);
            boolean result = triple.map((a, b, c) -> a || b || c);
            assertTrue(result);
        }

        @Test
        void mapFunction() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            String result = tuple.map(t -> "" + t._1 + t._2 + t._3);
            assertEquals("truefalsetrue", result);
        }

        @Test
        void tuple2_map() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Boolean result = tuple.map((a, b) -> a && b);
            assertFalse(result);
        }

        @Test
        void tuple2_map_or() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean result = tuple.map((a, b) -> a || b);
            assertEquals(true, result);
        }

        @Test
        void tuple2_map_toString() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            String result = tuple.map((a, b) -> a + "," + b);
            assertEquals("true,false", result);
        }

        @Test
        void tuple3_map() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Boolean result = tuple.map((a, b, c) -> a || b || c);
            assertTrue(result);
        }

        @Test
        void tuple3_map_values_true_false_true() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean result = tuple.map((a, b, c) -> a && b && c);
            assertEquals(false, result);
        }

        @Test
        void tuple3_map_toString() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            String result = tuple.map((a, b, c) -> a + "," + b + "," + c);
            assertEquals("true,false,true", result);
        }

        @Test
        void tuple3Map() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean result = tuple.map((a, b, c) -> a || b || c);
            assertEquals(true, result);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            {
                BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
                assertTrue(tuple.filter((a, b) -> true).isPresent());
                assertFalse(tuple.filter((a, b) -> false).isPresent());
            }
        }

        @Test
        void arity3_filter_triPredicate() {
            {
                BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
                assertTrue(tuple.filter((a, b, c) -> true).isPresent());
                assertFalse(tuple.filter((a, b, c) -> false).isPresent());
            }
        }

        @Test
        void classLevelFilter() {
            // u.Optional<BooleanTuple.BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
            BooleanTuple.BooleanTuple2 pair = BooleanTuple.of(true, false);
            Optional<BooleanTuple.BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
            assertTrue(filtered.isPresent());
        }

        @Test
        void filterPredicate() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.filter(t -> t._1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.filter(t -> !t._1);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_fail() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(false, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_match() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a != b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple2_filter_noMatch() {
            BooleanTuple2 tuple = BooleanTuple.of(false, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a && b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_noMatch_values_true_false() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a == b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_pass() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple2FilterFalse() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            var result = tuple.filter((a, b) -> a && b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            var result = tuple.filter((a, b) -> a || b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3_filter_fail() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || b || c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3_filter_match() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a == c);
            assertTrue(result.isPresent());
        }

        @Test
        void tuple3_filter_matches() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a && c);
            assertTrue(result.isPresent());
        }

        @Test
        void tuple3_filter_noMatch() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a == b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3_filter_pass() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3FilterFalse() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.filter((a, b, c) -> a && b && c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.filter((a, b, c) -> a || b || c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple.BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            BooleanTuple.BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            BooleanTuple.BooleanTuple4 tuple1 = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            BooleanTuple.BooleanTuple5 tuple1 = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            BooleanTuple.BooleanTuple6 tuple1 = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple.BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            BooleanTuple.BooleanTuple7 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            BooleanTuple.BooleanTuple8 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple.BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            BooleanTuple.BooleanTuple9 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_consistency() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_matchesValueContract() {
            BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
        }

        @Test
        void hashCodeSeedConsistency() {
            // Tuple1 uses seed=0: hashCode = elementHash
            assertEquals(1231, BooleanTuple.of(true).hashCode());
            assertEquals(1237, BooleanTuple.of(false).hashCode());

            // Tuple2 uses seed=0: hashCode = 31 * h1 + h2 (consistent with Tuple1 and IntTuple2 pattern)
            assertEquals(31 * 1231 + 1237, BooleanTuple.of(true, false).hashCode());
            assertEquals(31 * 1237 + 1231, BooleanTuple.of(false, true).hashCode());

            // Tuple3 uses seed=0: hashCode = 31 * (31 * h1 + h2) + h3
            assertEquals(31 * (31 * 1231 + 1237) + 1231, BooleanTuple.of(true, false, true).hashCode());

            // Tuple4 uses seed=0
            assertEquals(31 * (31 * (31 * 1231 + 1237) + 1231) + 1237, BooleanTuple.of(true, false, true, false).hashCode());

            // Tuple5 uses seed=0
            assertEquals(31 * (31 * (31 * (31 * 1231 + 1237) + 1231) + 1237) + 1231, BooleanTuple.of(true, false, true, false, true).hashCode());

            // Same-value different-arity should produce different hashes (extra 0-value element changes hash)
            // except when arity differs and both have same elements up to different lengths
        }

        @Test
        void hashCodeTuple1() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple0_hashCode() {
            BooleanTuple0 tuple1 = BooleanTuple.from(new boolean[0]);
            BooleanTuple0 tuple2 = BooleanTuple.from(new boolean[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple1_hashCode() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            BooleanTuple1 tuple3 = BooleanTuple.of(false);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(1231, tuple1.hashCode());
            assertEquals(1237, tuple3.hashCode());
        }

        @Test
        void tuple1_hashCode_false() {
            BooleanTuple1 tuple = BooleanTuple.of(false);
            assertEquals(1237, tuple.hashCode());
        }

        @Test
        void tuple1_hashCode_true() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(1231, tuple1.hashCode());
        }

        @Test
        void tuple2_hashCode() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
        }

        @Test
        void tuple2_hashCode_different() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple2_hashCode_values_true_false() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple3_hashCode() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @ParameterizedTest(name = "arity {0}: equality value contract")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void equals_allArities_obeysValueContract(int arity) {
            boolean[] values = new boolean[arity];
            BooleanTuple<?> tuple = BooleanTuple.from(values);
            // A separately constructed empty value verifies equality independently of the factory singleton.
            BooleanTuple<?> equal = arity == 0 ? new BooleanTuple.BooleanTuple0() : BooleanTuple.from(values.clone());
            assertNotSame(tuple, equal);
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(equal));
            assertTrue(equal.equals(tuple));
            BooleanTuple<?> third = arity == 0 ? new BooleanTuple.BooleanTuple0() : BooleanTuple.from(values.clone());
            assertNotSame(equal, third);
            assertTrue(equal.equals(third));
            assertTrue(tuple.equals(third), "equality must be transitive");
            assertEquals(tuple.hashCode(), equal.hashCode());
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals(new Object()));

            for (int i = 0; i < arity; i++) {
                boolean[] changed = values.clone();
                changed[i] = !changed[i];
                BooleanTuple<?> different = BooleanTuple.from(changed);
                assertFalse(tuple.equals(different), "component " + i);
                assertFalse(different.equals(tuple), "component " + i);
            }

            for (int otherArity = 0; otherArity <= 9; otherArity++) {
                if (otherArity != arity) {
                    BooleanTuple<?> differentArity = BooleanTuple.from(java.util.Arrays.copyOf(values, otherArity));
                    assertFalse(tuple.equals(differentArity), "different arity " + otherArity);
                    assertFalse(differentArity.equals(tuple), "reverse comparison with arity " + otherArity);
                }
            }
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("equalityCases")
        void equals_inputCases_comparesValuesAndTypes(String scenario, BooleanTuple<?> tuple, Object other, boolean expected) {
            assertTrue(tuple.equals(tuple), scenario + ": reflexivity");
            assertEquals(expected, tuple.equals(other), scenario + ": forward comparison");
            if (other != null) {
                assertEquals(expected, other.equals(tuple), scenario + ": reverse comparison");
            }
            if (expected) {
                assertEquals(tuple.hashCode(), other.hashCode(), scenario + ": equal values must have equal hashes");
            }
        }

        @Test
        void equals_alternatingValues_rejectsDifferentComponentsAndTypes() {
            BooleanTuple.BooleanTuple4 alternating = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 same = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 differentLast = BooleanTuple.of(true, false, true, true);
            BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
            BooleanTuple.BooleanTuple4 allFalse = BooleanTuple.of(false, false, false, false);

            assertTrue(alternating.equals(alternating));
            assertEquals(alternating, same);
            assertNotEquals(alternating, differentLast);
            assertNotEquals(alternating, "tuple");
            assertNotEquals(allTrue.hashCode(), allFalse.hashCode());
        }

        @Test
        void equals_highArityMatchingValues_haveEqualHashes() {
            // Test BooleanTuple4
            BooleanTuple.BooleanTuple4 tuple4a = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 tuple4b = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 tuple4c = BooleanTuple.of(true, false, true, true);

            assertEquals(tuple4a, tuple4b);
            assertNotEquals(tuple4a, tuple4c);
            assertEquals(tuple4a.hashCode(), tuple4b.hashCode());

            // Test BooleanTuple5
            BooleanTuple.BooleanTuple5 tuple5a = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 tuple5b = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 tuple5c = BooleanTuple.of(true, false, true, false, false);

            assertEquals(tuple5a, tuple5b);
            assertNotEquals(tuple5a, tuple5c);
            assertEquals(tuple5a.hashCode(), tuple5b.hashCode());
        }

        private static java.util.stream.Stream<Arguments> equalityCases() {
            return java.util.stream.Stream.of(
                    Arguments.of("from(new boolean[0]) versus from(new boolean[0])", BooleanTuple.from(new boolean[0]), BooleanTuple.from(new boolean[0]),
                            true),
                    Arguments.of("of(false) versus of(false)", BooleanTuple.of(false), BooleanTuple.of(false), true),
                    Arguments.of("of(true) versus \"not a tuple\"", BooleanTuple.of(true), "not a tuple", false),
                    Arguments.of("of(true) versus \"true\"", BooleanTuple.of(true), "true", false),
                    Arguments.of("of(true) versus 1", BooleanTuple.of(true), 1, false),
                    Arguments.of("of(true) versus of(false)", BooleanTuple.of(true), BooleanTuple.of(false), false),
                    Arguments.of("of(true) versus of(true, false)", BooleanTuple.of(true), BooleanTuple.of(true, false), false),
                    Arguments.of("of(true) versus of(true)", BooleanTuple.of(true), BooleanTuple.of(true), true),
                    Arguments.of("of(true) versus null", BooleanTuple.of(true), null, false),
                    Arguments.of("of(true, false) versus \"not a tuple\"", BooleanTuple.of(true, false), "not a tuple", false),
                    Arguments.of("of(true, false) versus of(false, true)", BooleanTuple.of(true, false), BooleanTuple.of(false, true), false),
                    Arguments.of("of(true, false) versus of(true, false, true)", BooleanTuple.of(true, false), BooleanTuple.of(true, false, true), false),
                    Arguments.of("of(true, false) versus of(true, false)", BooleanTuple.of(true, false), BooleanTuple.of(true, false), true),
                    Arguments.of("of(true, false) versus of(true, true)", BooleanTuple.of(true, false), BooleanTuple.of(true, true), false),
                    Arguments.of("of(true, false) versus null", BooleanTuple.of(true, false), null, false),
                    Arguments.of("of(true, false, true) versus \"not a tuple\"", BooleanTuple.of(true, false, true), "not a tuple", false),
                    Arguments.of("of(true, false, true) versus of(false, false, true)", BooleanTuple.of(true, false, true), BooleanTuple.of(false, false, true),
                            false),
                    Arguments.of("of(true, false, true) versus of(false, true, false)", BooleanTuple.of(true, false, true), BooleanTuple.of(false, true, false),
                            false),
                    Arguments.of("of(true, false, true) versus of(true, false, false)", BooleanTuple.of(true, false, true), BooleanTuple.of(true, false, false),
                            false),
                    Arguments.of("of(true, false, true) versus of(true, false, true)", BooleanTuple.of(true, false, true), BooleanTuple.of(true, false, true),
                            true),
                    Arguments.of("of(true, false, true) versus null", BooleanTuple.of(true, false, true), null, false),
                    Arguments.of("of(true, false, true, false) versus \"not a tuple\"", BooleanTuple.of(true, false, true, false), "not a tuple", false),
                    Arguments.of("of(true, false, true, false) versus \"tuple4\"", BooleanTuple.of(true, false, true, false), "tuple4", false),
                    Arguments.of("of(true, false, true, false) versus of(true, false, true, false)", BooleanTuple.of(true, false, true, false),
                            BooleanTuple.of(true, false, true, false), true),
                    Arguments.of("of(true, false, true, false) versus of(true, false, true, true)", BooleanTuple.of(true, false, true, false),
                            BooleanTuple.of(true, false, true, true), false),
                    Arguments.of("of(true, false, true, false, true) versus \"not a tuple\"", BooleanTuple.of(true, false, true, false, true), "not a tuple",
                            false),
                    Arguments.of("of(true, false, true, false, true) versus \"tuple5\"", BooleanTuple.of(true, false, true, false, true), "tuple5", false),
                    Arguments.of("of(true, false, true, false, true) versus of(true, false, true, false, false)",
                            BooleanTuple.of(true, false, true, false, true), BooleanTuple.of(true, false, true, false, false), false),
                    Arguments.of("of(true, false, true, false, true) versus of(true, false, true, false, true)",
                            BooleanTuple.of(true, false, true, false, true), BooleanTuple.of(true, false, true, false, true), true),
                    Arguments.of("of(true, false, true, false, true, false) versus \"not a tuple\"", BooleanTuple.of(true, false, true, false, true, false),
                            "not a tuple", false),
                    Arguments.of("of(true, false, true, false, true, false) versus \"tuple6\"", BooleanTuple.of(true, false, true, false, true, false),
                            "tuple6", false),
                    Arguments.of("of(true, false, true, false, true, false) versus of(true, false, true, false, true, false)",
                            BooleanTuple.of(true, false, true, false, true, false), BooleanTuple.of(true, false, true, false, true, false), true),
                    Arguments.of("of(true, false, true, false, true, false) versus of(true, false, true, false, true, true)",
                            BooleanTuple.of(true, false, true, false, true, false), BooleanTuple.of(true, false, true, false, true, true), false),
                    Arguments.of("of(true, false, true, false, true, false, true) versus \"not a tuple\"",
                            BooleanTuple.of(true, false, true, false, true, false, true), "not a tuple", false),
                    Arguments.of("of(true, false, true, false, true, false, true) versus \"tuple7\"",
                            BooleanTuple.of(true, false, true, false, true, false, true), "tuple7", false),
                    Arguments.of("of(true, false, true, false, true, false, true) versus of(true, false, true, false, true, false, false)",
                            BooleanTuple.of(true, false, true, false, true, false, true), BooleanTuple.of(true, false, true, false, true, false, false), false),
                    Arguments.of("of(true, false, true, false, true, false, true) versus of(true, false, true, false, true, false, true)",
                            BooleanTuple.of(true, false, true, false, true, false, true), BooleanTuple.of(true, false, true, false, true, false, true), true),
                    Arguments.of("of(true, false, true, false, true, false, true, false) versus \"not a tuple\"",
                            BooleanTuple.of(true, false, true, false, true, false, true, false), "not a tuple", false),
                    Arguments.of("of(true, false, true, false, true, false, true, false) versus \"tuple8\"",
                            BooleanTuple.of(true, false, true, false, true, false, true, false), "tuple8", false),
                    Arguments.of("of(true, false, true, false, true, false, true, false) versus of(true, false, true, false, true, false, true, false)",
                            BooleanTuple.of(true, false, true, false, true, false, true, false),
                            BooleanTuple.of(true, false, true, false, true, false, true, false), true),
                    Arguments.of("of(true, false, true, false, true, false, true, false) versus of(true, false, true, false, true, false, true, true)",
                            BooleanTuple.of(true, false, true, false, true, false, true, false),
                            BooleanTuple.of(true, false, true, false, true, false, true, true), false),
                    Arguments.of("of(true, false, true, false, true, false, true, false, true) versus \"not a tuple\"",
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true), "not a tuple", false),
                    Arguments.of("of(true, false, true, false, true, false, true, false, true) versus \"tuple9\"",
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true), "tuple9", false),
                    Arguments.of(
                            "of(true, false, true, false, true, false, true, false, true) versus of(true, false, true, false, true, false, true, false, false)",
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true),
                            BooleanTuple.of(true, false, true, false, true, false, true, false, false), false),
                    Arguments.of(
                            "of(true, false, true, false, true, false, true, false, true) versus of(true, false, true, false, true, false, true, false, true)",
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true),
                            BooleanTuple.of(true, false, true, false, true, false, true, false, true), true));
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity1_toString() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity3_toString() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_formatsValue() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            String result = tuple.toString();
            assertTrue(result.contains("true"));
            assertTrue(result.contains("false"));
        }

        @Test
        void toStringTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.from(new boolean[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            String str = tuple.toString();
            assertTrue(str.contains("true"));
        }

        @Test
        void toStringTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            String str = tuple.toString();
            assertTrue(str.contains("true"));
            assertTrue(str.contains("false"));
            assertTrue(str.contains("true"));
        }

        @Test
        void tuple0_toString() {
            BooleanTuple0 tuple = BooleanTuple.from(new boolean[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple1_toString() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals("(true)", tuple.toString());
        }

        @Test
        void tuple1_toString_false() {
            BooleanTuple1 tuple = BooleanTuple.of(false);
            assertEquals("(false)", tuple.toString());
        }

        @Test
        void tuple2_toString() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals("(true, false)", tuple.toString());
        }

        @Test
        void tuple3_toString() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals("(true, false, true)", tuple.toString());
        }

        @Test
        void toString_highArities_preservesEveryValueAndDelimiter() {
            BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            String str4 = tuple4.toString();
            assertEquals("(true, false, true, false)", str4);

            BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);
            String str6 = tuple6.toString();
            assertEquals("(true, false, true, false, true, false)", str6);
        }
    }

    @Nested
    class ContractTests {

        @ParameterizedTest(name = "arity {0}: default exports are detached")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void defaultConstructors_exportDefaultValuesInIndependentArrays(int arity) {
            BooleanTuple<?> tuple = switch (arity) {
                case 1 -> new BooleanTuple.BooleanTuple1();
                case 2 -> new BooleanTuple.BooleanTuple2();
                case 3 -> new BooleanTuple.BooleanTuple3();
                case 4 -> new BooleanTuple.BooleanTuple4();
                case 5 -> new BooleanTuple.BooleanTuple5();
                case 6 -> new BooleanTuple.BooleanTuple6();
                case 7 -> new BooleanTuple.BooleanTuple7();
                case 8 -> new BooleanTuple.BooleanTuple8();
                case 9 -> new BooleanTuple.BooleanTuple9();
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + arity);
            };
            boolean[] expected = new boolean[arity];
            boolean[] first = tuple.toArray();
            boolean[] second = tuple.toArray();

            assertEquals(arity, tuple.arity());
            assertArrayEquals(expected, first);
            assertArrayEquals(expected, second);
            assertNotSame(first, second);
            first[0] = true;
            assertArrayEquals(expected, second, "each export must have independent storage");
            assertArrayEquals(expected, tuple.toArray(), "mutating an export must not affect the tuple");
        }

        @Test
        void emptyTuple_operations_obeyEmptyValueContract() {
            BooleanTuple<?> empty = BooleanTuple.from((boolean[]) null);
            assertEquals(BooleanTuple.from(new boolean[0]), empty);
            assertFalse(empty.equals("boolean"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void multipleTuples_independence() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
            assertNotEquals(tuple1, tuple2);
            assertEquals(true, tuple1._1);
            assertEquals(false, tuple2._1);
        }

        @Test
        void selfEquality() {
            // Test that all tuple types equal themselves
            BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);

            assertTrue(tuple1.equals(tuple1));
            assertTrue(tuple2.equals(tuple2));
            assertTrue(tuple3.equals(tuple3));
        }

        @Test
        void tuple_allFalseValues() {
            BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
            assertFalse(tuple.contains(true));
            assertTrue(tuple.contains(false));
            assertFalse(tuple.stream().anyMatch(b -> b));
        }

        @Test
        void tuple_allTrueValues() {
            BooleanTuple3 tuple = BooleanTuple.of(true, true, true);
            assertTrue(tuple.contains(true));
            assertFalse(tuple.contains(false));
            assertTrue(tuple.stream().allMatch(b -> b));
        }

        @Test
        void tuple4_basic() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple.arity());
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
            BooleanTuple4 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);
        }

        @Test
        void tuple4Operations() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);

            // Test reverse
            BooleanTuple4 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);

            // Test contains
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));

            // Test toArray
            assertArrayEquals(new boolean[] { true, false, true, false }, tuple.toArray());

            // Test hashCode and equals
            BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
            BooleanTuple4 tuple3 = BooleanTuple.of(true, false, true, true);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("true"));
            assertTrue(str.contains("false"));
        }

        @Test
        void tuple5_basic() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple.arity());
            assertTrue(tuple.contains(true));
            BooleanTuple5 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
        }

        @Test
        void tuple5Operations() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);

            // Test reverse
            BooleanTuple5 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);

            // Test contains
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));

            // Test toArray
            assertArrayEquals(new boolean[] { true, false, true, false, true }, tuple.toArray());

            // Test equals
            BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple5 tuple3 = BooleanTuple.of(true, false, true, false, false);
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("true"));
            assertTrue(str.contains("false"));
        }

        @Test
        void tuple6_basic() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple.arity());
            assertTrue(tuple.contains(false));
            BooleanTuple6 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
        }

        @Test
        void tuple6Operations() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);

            // Test reverse
            BooleanTuple6 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);
            assertEquals(false, reversed._5);
            assertEquals(true, reversed._6);

            // Test contains
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));

            // Test toArray
            assertArrayEquals(new boolean[] { true, false, true, false, true, false }, tuple.toArray());

            // Test hashCode and equals
            BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple6 tuple3 = BooleanTuple.of(true, false, true, false, true, true);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);
        }

        @Test
        void tuple7_basic() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple.arity());
            assertEquals(7, tuple.toArray().length);
            BooleanTuple7 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
        }

        @Test
        void tuple7Operations() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);

            // Test reverse
            BooleanTuple7 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);
            assertEquals(false, reversed._6);
            assertEquals(true, reversed._7);

            // Test contains
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));

            // Test toArray
            assertArrayEquals(new boolean[] { true, false, true, false, true, false, true }, tuple.toArray());

            // Test hashCode and equals
            BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple7 tuple3 = BooleanTuple.of(true, false, true, false, true, false, false);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);
        }

        @Test
        void tuple8_basic() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
            BooleanTuple8 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
        }

        @Test
        void tuple8Operations() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);

            // Test reverse
            BooleanTuple8 reversed = tuple.reversed();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);
            assertEquals(false, reversed._5);
            assertEquals(true, reversed._6);
            assertEquals(false, reversed._7);
            assertEquals(true, reversed._8);

            // Test contains
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));

            // Test toArray
            assertArrayEquals(new boolean[] { true, false, true, false, true, false, true, false }, tuple.toArray());

            // Test hashCode and equals
            BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple8 tuple3 = BooleanTuple.of(true, false, true, false, true, false, true, true);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);
        }

        @Test
        void tuple9_basic() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
            BooleanTuple9 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(true, reversed._9);
        }

        @Test
        void tuple9Operations() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);

            // Test reverse
            BooleanTuple9 reversed = tuple.reversed();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);
            assertEquals(false, reversed._6);
            assertEquals(true, reversed._7);
            assertEquals(false, reversed._8);
            assertEquals(true, reversed._9);

            // Test contains
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));

            // Test toArray
            assertArrayEquals(new boolean[] { true, false, true, false, true, false, true, false, true }, tuple.toArray());

            // Test hashCode and equals
            BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple9 tuple3 = BooleanTuple.of(true, false, true, false, true, false, true, false, false);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);
        }

        @Test
        void highArity_membershipAndDefaultHashes_preserveValues() {
            BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(false, false, false, true);
            assertTrue(tuple4.contains(true));
            assertEquals(BooleanTuple.of(false, false, false, false).hashCode(), new BooleanTuple.BooleanTuple4().hashCode());

            BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(false, false, false, false, true);
            assertTrue(tuple5.contains(true));
            assertEquals(BooleanTuple.of(false, false, false, false, false).hashCode(), new BooleanTuple.BooleanTuple5().hashCode());

            BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(false, false, false, false, false, true);
            assertTrue(tuple6.contains(true));
            assertEquals(BooleanTuple.of(false, false, false, false, false, false).hashCode(), new BooleanTuple.BooleanTuple6().hashCode());

            BooleanTuple.BooleanTuple7 tuple7 = BooleanTuple.of(false, false, false, false, false, false, true);
            assertTrue(tuple7.contains(true));
            assertEquals(BooleanTuple.of(false, false, false, false, false, false, false).hashCode(), new BooleanTuple.BooleanTuple7().hashCode());

            BooleanTuple.BooleanTuple8 tuple8 = BooleanTuple.of(false, false, false, false, false, false, false, true);
            assertTrue(tuple8.contains(true));
            assertEquals(BooleanTuple.of(false, false, false, false, false, false, false, false).hashCode(), new BooleanTuple.BooleanTuple8().hashCode());

            BooleanTuple.BooleanTuple9 tuple9 = BooleanTuple.of(false, false, false, false, false, false, false, false, true);
            assertTrue(tuple9.contains(true));
            assertEquals(BooleanTuple.of(false, false, false, false, false, false, false, false, false).hashCode(),
                    new BooleanTuple.BooleanTuple9().hashCode());
        }
    }
}
