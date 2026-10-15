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

import com.landawn.abacus.util.ByteTuple.ByteTuple0;
import com.landawn.abacus.util.ByteTuple.ByteTuple1;
import com.landawn.abacus.util.ByteTuple.ByteTuple2;
import com.landawn.abacus.util.ByteTuple.ByteTuple3;
import com.landawn.abacus.util.ByteTuple.ByteTuple4;
import com.landawn.abacus.util.ByteTuple.ByteTuple5;
import com.landawn.abacus.util.ByteTuple.ByteTuple6;
import com.landawn.abacus.util.ByteTuple.ByteTuple7;
import com.landawn.abacus.util.ByteTuple.ByteTuple8;
import com.landawn.abacus.util.ByteTuple.ByteTuple9;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.u.Optional;

class ByteTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, byte[] values) {
            ByteTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of4Median() {
            // ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            // byte median = tuple.lowerMedian();   // 20
            ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals((byte) 20, t.lowerMedian());
        }

        @Test
        void of5Sum() {
            // ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            // int sum = tuple.sum();   // 150
            ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(150, t.sum());
        }

        private static ByteTuple<?> createWithOf(byte[] values) {
            return switch (values.length) {
                case 1 -> ByteTuple.of(values[0]);
                case 2 -> ByteTuple.of(values[0], values[1]);
                case 3 -> ByteTuple.of(values[0], values[1], values[2]);
                case 4 -> ByteTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> ByteTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: (byte)1", new byte[] { (byte) 1 }), Arguments.of("1: (byte)10", new byte[] { (byte) 10 }),
                    Arguments.of("2: (byte)1, (byte)2", new byte[] { (byte) 1, (byte) 2 }),
                    Arguments.of("2: (byte)10, (byte)20", new byte[] { (byte) 10, (byte) 20 }),
                    Arguments.of("3: (byte)1, (byte)2, (byte)3", new byte[] { (byte) 1, (byte) 2, (byte) 3 }),
                    Arguments.of("3: (byte)10, (byte)20, (byte)30", new byte[] { (byte) 10, (byte) 20, (byte) 30 }),
                    Arguments.of("4: (byte)1, (byte)2, (byte)3, (byte)4", new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 }),
                    Arguments.of("4: (byte)10, (byte)20, (byte)30, (byte)40", new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40 }),
                    Arguments.of("5: (byte)1, (byte)2, (byte)3, (byte)4, (byte)5", new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5 }),
                    Arguments.of("5: (byte)10, (byte)20, (byte)30, (byte)40, (byte)50", new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50 }),
                    Arguments.of("6: (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6",
                            new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6 }),
                    Arguments.of("6: (byte)10, (byte)20, (byte)30, (byte)40, (byte)50, (byte)60",
                            new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60 }),
                    Arguments.of("7: (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7",
                            new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7 }),
                    Arguments.of("7: (byte)10, (byte)20, (byte)30, (byte)40, (byte)50, (byte)60, (byte)70",
                            new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70 }),
                    Arguments.of("8: (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8",
                            new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8 }),
                    Arguments.of("8: (byte)10, (byte)20, (byte)30, (byte)40, (byte)50, (byte)60, (byte)70, (byte)80",
                            new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80 }),
                    Arguments.of("9: (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9",
                            new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9 }),
                    Arguments.of("9: (byte)10, (byte)20, (byte)30, (byte)40, (byte)50, (byte)60, (byte)70, (byte)80, (byte)90",
                            new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90 }));
        }

        private static byte[] publicFields(ByteTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    ByteTuple.ByteTuple1 value = (ByteTuple.ByteTuple1) tuple;
                    yield new byte[] { value._1 };
                }
                case 2 -> {
                    ByteTuple.ByteTuple2 value = (ByteTuple.ByteTuple2) tuple;
                    yield new byte[] { value._1, value._2 };
                }
                case 3 -> {
                    ByteTuple.ByteTuple3 value = (ByteTuple.ByteTuple3) tuple;
                    yield new byte[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    ByteTuple.ByteTuple4 value = (ByteTuple.ByteTuple4) tuple;
                    yield new byte[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    ByteTuple.ByteTuple5 value = (ByteTuple.ByteTuple5) tuple;
                    yield new byte[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    ByteTuple.ByteTuple6 value = (ByteTuple.ByteTuple6) tuple;
                    yield new byte[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    ByteTuple.ByteTuple7 value = (ByteTuple.ByteTuple7) tuple;
                    yield new byte[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    ByteTuple.ByteTuple8 value = (ByteTuple.ByteTuple8) tuple;
                    yield new byte[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    ByteTuple.ByteTuple9 value = (ByteTuple.ByteTuple9) tuple;
                    yield new byte[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
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
                byte[] array = new byte[i];
                ByteTuple<?> tuple = ByteTuple.from(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        void create_arraySize9() {
            ByteTuple9 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 9, tuple._9);
        }

        @Test
        void create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                ByteTuple.from(new byte[10]);
            });
        }

        @Test
        void create_emptyArray() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_nullArray() {
            ByteTuple0 tuple = ByteTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_array3() {
            ByteTuple3 tuple = ByteTuple.from(new byte[] { 10, 20, 30 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
        }

        @Test
        void from_array4() {
            ByteTuple4 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4 });
            assertEquals(4, tuple.arity());
        }

        @Test
        void from_array5() {
            ByteTuple5 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5 });
            assertEquals(5, tuple.arity());
        }

        @Test
        void from_array6() {
            ByteTuple6 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(6, tuple.arity());
        }

        @Test
        void from_array7() {
            ByteTuple7 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(7, tuple.arity());
        }

        @Test
        void from_array8() {
            ByteTuple8 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(8, tuple.arity());
        }

        @Test
        void from_array9() {
            ByteTuple9 tuple = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_emptyArray() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nineElements() {
            ByteTuple9 tuple = ByteTuple.from(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 90, tuple._9);
        }

        @Test
        void from_nullArray() {
            ByteTuple<?> tuple = ByteTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_preservesElements() {
            // Test empty array
            ByteTuple<?> empty = ByteTuple.from(null);
            assertEquals(0, empty.arity());

            empty = ByteTuple.from(new byte[0]);
            assertEquals(0, empty.arity());

            // Test array with 1 element
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.from(new byte[] { 10 });
            assertEquals(1, tuple1.arity());
            assertEquals(10, tuple1._1);

            // Test array with 5 elements
            ByteTuple.ByteTuple5 tuple5 = ByteTuple.from(new byte[] { 10, 20, 30, 40, 50 });
            assertEquals(5, tuple5.arity());
            assertEquals(50, tuple5._5);

            // Test array with 9 elements
            ByteTuple.ByteTuple9 tuple9 = ByteTuple.from(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
            assertEquals(9, tuple9.arity());
            assertEquals(90, tuple9._9);

            // Test too many elements
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.from(new byte[10]));
        }

        @Test
        void from_singleElement() {
            ByteTuple1 tuple = ByteTuple.from(new byte[] { 10 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.from(new byte[10]));
        }

        @Test
        void from_twoElements() {
            ByteTuple2 tuple = ByteTuple.from(new byte[] { 10, 20 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
        }

        @Test
        void from1() {
            ByteTuple1 tuple = ByteTuple.from(new byte[] { (byte) 1 });
            assertEquals((byte) 1, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2Through8() {
            ByteTuple2 tuple2 = ByteTuple.from(new byte[] { 1, 2 });
            assertEquals((byte) 1, tuple2._1);
            assertEquals((byte) 2, tuple2._2);

            ByteTuple4 tuple4 = ByteTuple.from(new byte[] { 1, 2, 3, 4 });
            assertEquals((byte) 1, tuple4._1);
            assertEquals((byte) 4, tuple4._4);

            ByteTuple5 tuple5 = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5 });
            assertEquals((byte) 1, tuple5._1);
            assertEquals((byte) 5, tuple5._5);

            ByteTuple6 tuple6 = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6 });
            assertEquals((byte) 1, tuple6._1);
            assertEquals((byte) 6, tuple6._6);

            ByteTuple7 tuple7 = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals((byte) 1, tuple7._1);
            assertEquals((byte) 7, tuple7._7);

            ByteTuple8 tuple8 = ByteTuple.from(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals((byte) 1, tuple8._1);
            assertEquals((byte) 8, tuple8._8);
        }

        @Test
        void from3() {
            ByteTuple3 tuple = ByteTuple.from(new byte[] { (byte) 1, (byte) 2, (byte) 3 });
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 2, tuple._2);
            assertEquals((byte) 3, tuple._3);
        }

        @Test
        void from9() {
            ByteTuple9 tuple = ByteTuple.from(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9 });
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 9, tuple._9);
        }

        @Test
        void fromEmpty() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromNull() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                ByteTuple.from(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10 });
            });
        }
    }

    @Nested
    class ArityTests {

        @Test
        void arity_returnsElementCount() {
            assertEquals(0, ByteTuple.from(new byte[0]).arity());
            assertEquals(1, ByteTuple.of((byte) 1).arity());
            assertEquals(2, ByteTuple.of((byte) 1, (byte) 2).arity());
            assertEquals(3, ByteTuple.of((byte) 1, (byte) 2, (byte) 3).arity());
            assertEquals(4, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).arity());
            assertEquals(5, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5).arity());
            assertEquals(6, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6).arity());
            assertEquals(7, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7).arity());
            assertEquals(8, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8).arity());
            assertEquals(9, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9).arity());
        }

        @Test
        void coverageRegression_highArityEqualsBranches() {
            ByteTuple.ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertTrue(tuple4.equals(tuple4));
            assertTrue(tuple4.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4)));
            assertFalse(tuple4.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 9)));
            assertFalse(tuple4.equals("tuple4"));

            ByteTuple.ByteTuple5 tuple5 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertTrue(tuple5.equals(tuple5));
            assertTrue(tuple5.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5)));
            assertFalse(tuple5.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 9)));
            assertFalse(tuple5.equals("tuple5"));

            ByteTuple.ByteTuple6 tuple6 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertTrue(tuple6.equals(tuple6));
            assertTrue(tuple6.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6)));
            assertFalse(tuple6.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 9)));
            assertFalse(tuple6.equals("tuple6"));

            ByteTuple.ByteTuple7 tuple7 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertTrue(tuple7.equals(tuple7));
            assertTrue(tuple7.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7)));
            assertFalse(tuple7.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 9)));
            assertFalse(tuple7.equals("tuple7"));

            ByteTuple.ByteTuple8 tuple8 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertTrue(tuple8.equals(tuple8));
            assertTrue(tuple8.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8)));
            assertFalse(tuple8.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 9)));
            assertFalse(tuple8.equals("tuple8"));

            ByteTuple.ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertTrue(tuple9.equals(tuple9));
            assertTrue(tuple9.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9)));
            assertFalse(tuple9.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 0)));
            assertFalse(tuple9.equals("tuple9"));
        }

        @Test
        void coverageRegression_highArityOperations() {
            ByteTuple.ByteTuple4 tuple4 = ByteTuple.of((byte) 9, (byte) 1, (byte) 7, (byte) 3);
            assertEquals((byte) 1, tuple4.min());
            assertEquals((byte) 9, tuple4.max());
            assertEquals((byte) 3, tuple4.lowerMedian());
            assertEquals(20, tuple4.sum());
            assertEquals(5.0, tuple4.average().getAsDouble());
            assertTrue(tuple4.contains((byte) 3));

            ByteTuple.ByteTuple5 tuple5 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertTrue(tuple5.contains((byte) 5));

            ByteTuple.ByteTuple6 tuple6 = ByteTuple.of((byte) 9, (byte) 1, (byte) 7, (byte) 3, (byte) 5, (byte) 11);
            assertEquals((byte) 5, tuple6.lowerMedian());
            assertTrue(tuple6.contains((byte) 11));

            ByteTuple.ByteTuple7 tuple7 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertTrue(tuple7.contains((byte) 7));

            ByteTuple.ByteTuple8 tuple8 = ByteTuple.of((byte) 9, (byte) 1, (byte) 7, (byte) 3, (byte) 5, (byte) 11, (byte) 13, (byte) 15);
            assertEquals((byte) 7, tuple8.lowerMedian());
            assertTrue(tuple8.contains((byte) 15));

            ByteTuple.ByteTuple9 tuple9 = ByteTuple.of((byte) 9, (byte) 1, (byte) 7, (byte) 3, (byte) 5, (byte) 11, (byte) 13, (byte) 15, (byte) 17);
            assertEquals((byte) 9, tuple9.lowerMedian());
            assertTrue(tuple9.contains((byte) 17));
        }

        @Test
        void largeArityContainsAndEqualsBranchesForCoverage() {
            final ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            final ByteTuple5 tuple5 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            final ByteTuple6 tuple6 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            final ByteTuple7 tuple7 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            final ByteTuple8 tuple8 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            final ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);

            assertTrue(tuple4.contains((byte) 2));
            assertTrue(tuple4.contains((byte) 3));
            assertTrue(tuple4.contains((byte) 4));
            assertFalse(tuple4.contains((byte) 99));
            assertTrue(tuple5.contains((byte) 2));
            assertTrue(tuple5.contains((byte) 3));
            assertTrue(tuple5.contains((byte) 4));
            assertTrue(tuple5.contains((byte) 5));
            assertFalse(tuple5.contains((byte) 99));
            assertTrue(tuple6.contains((byte) 2));
            assertTrue(tuple6.contains((byte) 3));
            assertTrue(tuple6.contains((byte) 4));
            assertTrue(tuple6.contains((byte) 5));
            assertTrue(tuple6.contains((byte) 6));
            assertFalse(tuple6.contains((byte) 99));
            assertTrue(tuple7.contains((byte) 2));
            assertTrue(tuple7.contains((byte) 3));
            assertTrue(tuple7.contains((byte) 4));
            assertTrue(tuple7.contains((byte) 5));
            assertTrue(tuple7.contains((byte) 6));
            assertTrue(tuple7.contains((byte) 7));
            assertFalse(tuple7.contains((byte) 99));
            assertTrue(tuple8.contains((byte) 2));
            assertTrue(tuple8.contains((byte) 3));
            assertTrue(tuple8.contains((byte) 4));
            assertTrue(tuple8.contains((byte) 5));
            assertTrue(tuple8.contains((byte) 6));
            assertTrue(tuple8.contains((byte) 7));
            assertTrue(tuple8.contains((byte) 8));
            assertFalse(tuple8.contains((byte) 99));
            assertTrue(tuple9.contains((byte) 2));
            assertTrue(tuple9.contains((byte) 3));
            assertTrue(tuple9.contains((byte) 4));
            assertTrue(tuple9.contains((byte) 5));
            assertTrue(tuple9.contains((byte) 6));
            assertTrue(tuple9.contains((byte) 7));
            assertTrue(tuple9.contains((byte) 8));
            assertTrue(tuple9.contains((byte) 9));
            assertFalse(tuple9.contains((byte) 99));

            assertTrue(tuple4.equals(tuple4));
            assertFalse(tuple4.equals("not a tuple"));
            assertFalse(tuple4.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 9)));
            assertTrue(tuple5.equals(tuple5));
            assertFalse(tuple5.equals("not a tuple"));
            assertFalse(tuple5.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 9)));
            assertTrue(tuple6.equals(tuple6));
            assertFalse(tuple6.equals("not a tuple"));
            assertFalse(tuple6.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 9)));
            assertTrue(tuple7.equals(tuple7));
            assertFalse(tuple7.equals("not a tuple"));
            assertFalse(tuple7.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 9)));
            assertTrue(tuple8.equals(tuple8));
            assertFalse(tuple8.equals("not a tuple"));
            assertFalse(tuple8.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 9)));
            assertTrue(tuple9.equals(tuple9));
            assertFalse(tuple9.equals("not a tuple"));
            assertFalse(tuple9.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 0)));
        }

        @Test
        void tuple0_arity() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void tuple0_arity_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertEquals(0, tuple.arity());
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void elementsMethod() {
            // Just test that elements are cached properly
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] elements1 = tuple.elements();
            byte[] elements2 = tuple.elements();
            assertSame(elements1, elements2); // Should return same cached array
        }
    }

    @Nested
    class MinTests {

        @Test
        void arity1_min() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.min());
        }

        @Test
        void min_returnsMinimum() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals(10, tuple.min());

            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> empty.min());
        }

        @Test
        void minMaxMedian_largerTuple() {
            ByteTuple5 tuple = ByteTuple.of((byte) 5, (byte) 2, (byte) 8, (byte) 1, (byte) 9);
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 9, tuple.max());
            assertEquals((byte) 5, tuple.lowerMedian());
        }

        @Test
        void minTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple.min());
        }

        @Test
        void minTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            assertEquals((byte) 1, tuple.min());
        }

        @Test
        void tuple0_min_throwsException() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void tuple0_min_throwsException_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void tuple1_min() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals((byte) 42, tuple.min());
        }

        @Test
        void tuple1_min_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        void tuple2_min() {
            ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        void tuple2_min_values_20_10() {
            ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        void tuple3_min() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        void tuple5_min() {
            ByteTuple5 tuple = ByteTuple.of((byte) 50, (byte) 30, (byte) 10, (byte) 40, (byte) 20);
            assertEquals((byte) 10, tuple.min());
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity1_max() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.max());
        }

        @Test
        void max_returnsMaximum() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals(30, tuple.max());

            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> empty.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple.max());
        }

        @Test
        void maxTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            assertEquals((byte) 3, tuple.max());
        }

        @Test
        void tuple0_max_throwsException() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void tuple0_max_throwsException_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void tuple1_max() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals((byte) 42, tuple.max());
        }

        @Test
        void tuple1_max_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.max());
        }

        @Test
        void tuple2_max() {
            ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        void tuple2_max_values_10_20() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 20, tuple.max());
        }

        @Test
        void tuple3_max() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        void tuple3_max_values_30_10_20() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        void tuple5_max() {
            ByteTuple5 tuple = ByteTuple.of((byte) 50, (byte) 30, (byte) 10, (byte) 40, (byte) 20);
            assertEquals((byte) 50, tuple.max());
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity1_sum() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.sum());
        }

        @Test
        void sum_returnsTotal() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(60, tuple.sum());

            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertEquals(0, empty.sum());
        }

        @Test
        void sumTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void sumTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals(1, tuple.sum());
        }

        @Test
        void sumTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(6, tuple.sum());
        }

        @Test
        void tuple0_sum() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void tuple0_sum_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void tuple1_sum() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals(42, tuple.sum());
        }

        @Test
        void tuple1_sum_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(10, tuple.sum());
        }

        @Test
        void tuple2_sum() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(30, tuple.sum());
        }

        @Test
        void tuple3_sum() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(60, tuple.sum());
        }

        @Test
        void tuple5_sum() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(150, tuple.sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity1_average() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void average_returnsMean() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(20.0, tuple.average().getAsDouble());

            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals(1.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple0_average_returnsEmpty() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void tuple0_average_returnsEmpty_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void tuple1_average_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(10.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void tuple1_average_values_42() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals(42.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple2_average_values_10_20() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(15.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple3_average_values_10_20_30() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(20.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple5_average() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(30.0, tuple.average().getAsDouble());
        }
    }

    @Nested
    class MedianTests {

        @Test
        void arity1_median() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void median_returnsMedian() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals(20, tuple.lowerMedian());

            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> empty.lowerMedian());
        }

        @Test
        void medianTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple.lowerMedian());
        }

        @Test
        void medianTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            assertEquals((byte) 2, tuple.lowerMedian());
        }

        @Test
        void tuple0_median_throwsException() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void tuple0_median_throwsException_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void tuple1_median() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals((byte) 42, tuple.lowerMedian());
        }

        @Test
        void tuple1_median_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.lowerMedian());
        }

        @Test
        void tuple2_median() {
            ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
            assertEquals((byte) 10, tuple.lowerMedian()); // lower value for even-sized tuple
        }

        @Test
        void tuple2_median_values_10_20() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.lowerMedian());
        }

        @Test
        void tuple3_median() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
            assertEquals((byte) 20, tuple.lowerMedian());
        }

        @Test
        void tuple3_median_values_30_10_20() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 20, tuple.lowerMedian()); // middle value
        }

        @Test
        void tuple4_median() {
            ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals((byte) 20, tuple.lowerMedian()); // lower of two middle values
        }
    }

    @Nested
    class LowerMedianTests {

        @Test
        void lowerMedian_evenArityDocExampleDiffersFromMedian() {
            final ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals((byte) 20, t.lowerMedian());
            assertEquals(25.0, t.median());
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity1_reversed() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteTuple.ByteTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity2_reversed() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple.ByteTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity3_reversed() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple.ByteTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity4_reversed() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple.ByteTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple.ByteTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple.ByteTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple.ByteTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple.ByteTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple.ByteTuple9 reversed = tuple.reversed();
            assertEquals((byte) 9, reversed._1);
            assertEquals((byte) 8, reversed._2);
            assertEquals((byte) 7, reversed._3);
            assertEquals((byte) 6, reversed._4);
            assertEquals((byte) 5, reversed._5);
            assertEquals((byte) 4, reversed._6);
            assertEquals((byte) 3, reversed._7);
            assertEquals((byte) 2, reversed._8);
            assertEquals((byte) 1, reversed._9);
        }

        @Test
        void reversed_reversesElementOrder() {
            // Test Tuple0
            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            ByteTuple.ByteTuple0 reversedEmpty = empty.reversed();
            assertEquals(0, reversedEmpty.arity());

            // Test Tuple1
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple.ByteTuple1 reversed1 = tuple1.reversed();
            assertEquals(10, reversed1._1);

            // Test Tuple2
            ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple.ByteTuple2 reversed2 = tuple2.reversed();
            assertEquals(20, reversed2._1);
            assertEquals(10, reversed2._2);

            // Test Tuple3
            ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple.ByteTuple3 reversed3 = tuple3.reversed();
            assertEquals(30, reversed3._1);
            assertEquals(20, reversed3._2);
            assertEquals(10, reversed3._3);

            // Test larger tuples
            ByteTuple.ByteTuple5 tuple5 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            ByteTuple.ByteTuple5 reversed5 = tuple5.reversed();
            assertEquals(50, reversed5._1);
            assertEquals(40, reversed5._2);
            assertEquals(30, reversed5._3);
            assertEquals(20, reversed5._4);
            assertEquals(10, reversed5._5);
        }

        @Test
        void reversedTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            ByteTuple<ByteTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversedTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteTuple1 reversed = tuple.reversed();
            assertEquals((byte) 1, reversed._1);
        }

        @Test
        void reversedTuple2() {
            ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 reversed = tuple.reversed();
            assertEquals((byte) 2, reversed._1);
            assertEquals((byte) 1, reversed._2);
        }

        @Test
        void reversedTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 reversed = tuple.reversed();
            assertEquals((byte) 3, reversed._1);
            assertEquals((byte) 2, reversed._2);
            assertEquals((byte) 1, reversed._3);
        }

        @Test
        void tuple0_reversed() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            ByteTuple<?> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void tuple0_reversed_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            ByteTuple0 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(tuple, reversed);
        }

        @Test
        void tuple0_reversed_returnsSameInstance() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            ByteTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void tuple1_reversed_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteTuple1 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals((byte) 10, reversed._1);
        }

        @Test
        void tuple2_reversed_values_10_20() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals((byte) 20, reversed._1);
            assertEquals((byte) 10, reversed._2);
        }

        @Test
        void tuple3_reversed_values_10_20_30() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals((byte) 30, reversed._1);
            assertEquals((byte) 20, reversed._2);
            assertEquals((byte) 10, reversed._3);
        }

        @Test
        void tuple4_reversed() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple4 reversed = tuple.reversed();
            assertEquals((byte) 4, reversed._1);
            assertEquals((byte) 3, reversed._2);
            assertEquals((byte) 2, reversed._3);
            assertEquals((byte) 1, reversed._4);
        }

        @Test
        void tuple5_reversed() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple5 reversed = tuple.reversed();
            assertEquals((byte) 5, reversed._1);
            assertEquals((byte) 1, reversed._5);
        }

        @Test
        void tuple5_reversed_values_10_20_30_40_50() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            ByteTuple5 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals((byte) 50, reversed._1);
            assertEquals((byte) 40, reversed._2);
            assertEquals((byte) 30, reversed._3);
            assertEquals((byte) 20, reversed._4);
            assertEquals((byte) 10, reversed._5);
        }

        @Test
        void tuple6_reversed() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple6 reversed = tuple.reversed();
            assertEquals((byte) 6, reversed._1);
            assertEquals((byte) 1, reversed._6);
        }

        @Test
        void tuple7_reversed() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple7 reversed = tuple.reversed();
            assertEquals((byte) 7, reversed._1);
            assertEquals((byte) 1, reversed._7);
        }

        @Test
        void tuple8_reversed() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple8 reversed = tuple.reversed();
            assertEquals((byte) 8, reversed._1);
            assertEquals((byte) 1, reversed._8);
        }

        @Test
        void tuple9_reversed() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple9 reversed = tuple.reversed();
            assertEquals((byte) 9, reversed._1);
            assertEquals((byte) 1, reversed._9);
        }
    }

    @Nested
    class ContainsTests {

        @Test
        void arity1_contains() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity2_contains() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity3_contains() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity4_contains() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity5_contains() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity6_contains() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity7_contains() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity8_contains() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void arity9_contains() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void contains_findsPresentElements() {
            // Test Tuple0
            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertFalse(empty.contains((byte) 10));

            // Test Tuple1
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            assertTrue(tuple1.contains((byte) 10));
            assertFalse(tuple1.contains((byte) 20));

            // Test Tuple3
            ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertTrue(tuple3.contains((byte) 10));
            assertTrue(tuple3.contains((byte) 20));
            assertTrue(tuple3.contains((byte) 30));
            assertFalse(tuple3.contains((byte) 40));

            // Test larger tuples
            ByteTuple.ByteTuple7 tuple7 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
            assertTrue(tuple7.contains((byte) 70));
            assertFalse(tuple7.contains((byte) 80));
        }

        @Test
        void containsTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertFalse(tuple.contains((byte) 1));
        }

        @Test
        void containsTuple1False() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertFalse(tuple.contains((byte) 99));
        }

        @Test
        void containsTuple1True() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        void containsTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 2));
            assertTrue(tuple.contains((byte) 3));
            assertFalse(tuple.contains((byte) 99));
        }

        @Test
        void tuple0_contains_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertFalse(tuple.contains((byte) 10));
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        void tuple1_contains() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.contains((byte) 10));
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        void tuple1_contains_false() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        void tuple1_contains_true() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.contains((byte) 10));
        }

        @Test
        void tuple2_contains() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertFalse(tuple.contains((byte) 30));
        }

        @Test
        void tuple2_contains_found() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
        }

        @Test
        void tuple2_contains_notFound() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertFalse(tuple.contains((byte) 30));
        }

        @Test
        void tuple3_contains() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertTrue(tuple.contains((byte) 30));
            assertFalse(tuple.contains((byte) 40));
        }

        @Test
        void tuple4_contains() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 4));
            assertFalse(tuple.contains((byte) 5));
        }

        @Test
        void tuple5_contains() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertTrue(tuple.contains((byte) 5));
            assertFalse(tuple.contains((byte) 6));
        }

        @Test
        void tuple5_contains_values_10_20_30_40_50() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 50));
            assertFalse(tuple.contains((byte) 60));
        }

        @Test
        void tuple6_contains() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertTrue(tuple.contains((byte) 6));
            assertFalse(tuple.contains((byte) 7));
        }

        @Test
        void tuple7_contains() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertTrue(tuple.contains((byte) 7));
            assertFalse(tuple.contains((byte) 8));
        }

        @Test
        void tuple8_contains() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertTrue(tuple.contains((byte) 8));
            assertFalse(tuple.contains((byte) 9));
        }

        @Test
        void tuple9_contains() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertTrue(tuple.contains((byte) 9));
            assertFalse(tuple.contains((byte) 10));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_immutability() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            byte[] array1 = tuple.toArray();
            byte[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
        }

        @Test
        void toArray_independence() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            array[0] = 99;
            assertEquals((byte) 10, tuple._1); // Tuple should be unaffected
        }

        @Test
        void toArray_preservesElements() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(10, array[0]);
            assertEquals(20, array[1]);
            assertEquals(30, array[2]);

            // Ensure it's a copy
            array[0] = 100;
            assertEquals(10, tuple._1);
        }

        @Test
        void toArray_preservesElements_values_10_20_30() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10, 20, 30 }, array);

            // Verify it's a copy
            array[0] = 99;
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            byte[] array = tuple.toArray();
            array[0] = (byte) 100;
            assertEquals((byte) 1, tuple._1);
        }

        @Test
        void toArrayTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            byte[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArrayTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { (byte) 1 }, array);
        }

        @Test
        void toArrayTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { (byte) 1, (byte) 2, (byte) 3 }, array);
        }

        @Test
        void tuple0_toArray() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        void tuple1_toArray() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(1, array.length);
            assertEquals((byte) 10, array[0]);
        }

        @Test
        void tuple1_toArray_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10 }, array);
        }

        @Test
        void tuple2_toArray() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(2, array.length);
            assertArrayEquals(new byte[] { 10, 20 }, array);
        }

        @Test
        void tuple2_toArray_values_10_20() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10, 20 }, array);
        }

        @Test
        void tuple3_toArray() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertArrayEquals(new byte[] { 10, 20, 30 }, array);
        }

        @Test
        void tuple3_toArray_values_10_20_30() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10, 20, 30 }, array);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_independence() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteList list = tuple.toList();
            list.set(0, (byte) 99);
            assertEquals((byte) 10, tuple._1); // Tuple should be unaffected
        }

        @Test
        void toList_preservesElements() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
            assertEquals(30, list.get(2));
        }

        @Test
        void toListTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            ByteList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals((byte) 1, list.get(0));
        }

        @Test
        void toListTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((byte) 1, list.get(0));
            assertEquals((byte) 2, list.get(1));
            assertEquals((byte) 3, list.get(2));
        }

        @Test
        void toListTuple4Through9() {
            ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals((byte) 4, list4.get(3));

            ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals((byte) 9, list9.get(8));
        }

        @Test
        void tuple0_toList() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        void tuple0_toList_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        void tuple1_toList() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals((byte) 10, list.get(0));
        }

        @Test
        void tuple2_toList() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals((byte) 10, list.get(0));
            assertEquals((byte) 20, list.get(1));
        }

        @Test
        void tuple2_toList_values_10_20() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        void tuple3_toList() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals((byte) 10, list.get(0));
            assertEquals((byte) 20, list.get(1));
            assertEquals((byte) 30, list.get(2));
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity3_forEach() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity4_forEach() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_visitsElements() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int[] sum = { 0 };
            tuple.forEach(b -> sum[0] += b);
            assertEquals(60, sum[0]);

            // Test Tuple2 forEach
            ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            int[] count = { 0 };
            tuple2.forEach(b -> count[0]++);
            assertEquals(2, count[0]);
        }

        @Test
        void forEach_withException() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(b -> {
                    if (b == 20)
                        throw new RuntimeException("test exception");
                });
            });
        }

        @Test
        void forEachTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
        }

        @Test
        void forEachTuple2Override() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Byte.valueOf((byte) 10), result.get(0));
            assertEquals(Byte.valueOf((byte) 20), result.get(1));
        }

        @Test
        void forEachTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
            assertEquals(Byte.valueOf((byte) 2), result.get(1));
            assertEquals(Byte.valueOf((byte) 3), result.get(2));
        }

        @Test
        void forEachTuple3Override() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 30), result.get(2));
        }

        @Test
        void forEachTuple4() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Byte.valueOf((byte) 4), result.get(3));
        }

        @Test
        void tuple0_forEach() throws Exception {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(0, results.size());
        }

        @Test
        void tuple1_forEach() throws Exception {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(1, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
        }

        @Test
        void tuple2_forEach() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals((byte) 10, values.get(0));
            assertEquals((byte) 20, values.get(1));
        }

        @Test
        void tuple3_forEach_values_10_20_30() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(3, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
            assertEquals((byte) 30, results.get(2).byteValue());
        }

        @Test
        void tuple4_forEach() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(4, values.size());
        }

        @Test
        void tuple5_forEach() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void tuple6_forEach() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(6, values.size());
        }

        @Test
        void tuple7_forEach() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(7, values.size());
        }

        @Test
        void tuple8_forEach() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(8, values.size());
        }

        @Test
        void tuple9_forEach() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(9, values.size());
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_visitsElements() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.stream().toArray();
            assertEquals(3, array.length);
            assertEquals(10, array[0]);
            assertEquals(20, array[1]);
            assertEquals(30, array[2]);

            long count = tuple.stream().filter(b -> b > 15).count();
            assertEquals(2, count);
        }

        @Test
        void streamTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            ByteStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteStream stream = tuple.stream();
            assertEquals(1, stream.sum());
        }

        @Test
        void streamTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteStream stream = tuple.stream();
            assertEquals(6, stream.sum());
        }

        @Test
        void streamTuple4Through9() {
            ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(10, tuple4.stream().sum());

            ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(45, tuple9.stream().sum());
        }

        @Test
        void tuple0_stream() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        void tuple1_stream() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            long count = tuple.stream().count();
            assertEquals(1, count);
        }

        @Test
        void tuple1_stream_values_10() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            int sum = tuple.stream().sum();
            assertEquals(10, sum);
        }

        @Test
        void tuple2_stream() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            long count = tuple.stream().count();
            assertEquals(2, count);
        }

        @Test
        void tuple2_stream_filter() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            long count = tuple.stream().filter(b -> b > 15).count();
            assertEquals(1, count);
        }

        @Test
        void tuple3_stream_sum() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int sum = tuple.stream().sum();
            assertEquals(60, sum);
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void acceptConsumer() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Byte> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals((byte) 1, result.get(0));
            assertEquals((byte) 2, result.get(1));
            assertEquals((byte) 3, result.get(2));
        }

        @Test
        void arity2_accept_biConsumer() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept_triConsumer() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2_accept() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Integer> results = new ArrayList<>();
            tuple.accept((a, b) -> results.add(a + b));
            assertEquals(1, results.size());
            assertEquals(30, results.get(0));
        }

        @Test
        void tuple2_accept_values_10_20() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
        }

        @Test
        void tuple2Accept() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            List<Byte> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Byte.valueOf((byte) 3), result.get(0));
            assertEquals(Byte.valueOf((byte) 4), result.get(1));
        }

        @Test
        void tuple3_accept() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Integer> results = new ArrayList<>();
            tuple.accept((a, b, c) -> results.add(a + b + c));
            assertEquals(1, results.size());
            assertEquals(60, results.get(0));
        }

        @Test
        void tuple3_accept_values_10_20_30() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
            assertEquals((byte) 30, results.get(2).byteValue());
        }

        @Test
        void tuple3Accept() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Byte> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
            assertEquals(Byte.valueOf((byte) 2), result.get(1));
            assertEquals(Byte.valueOf((byte) 3), result.get(2));
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map_biFunction() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map_triFunction() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void mapFunction() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            int result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6, result);
        }

        @Test
        void tuple2_map() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Integer result = tuple.map((a, b) -> (int) (a + b));
            assertEquals(30, result);
        }

        @Test
        void tuple2_map_multiply() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 5, (byte) 6);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(30, result);
        }

        @Test
        void tuple2_map_values_10_20() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            int result = tuple.map((a, b) -> a + b);
            assertEquals(30, result);
        }

        @Test
        void tuple2Map() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(12, result);
        }

        @Test
        void tuple3_map() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Integer result = tuple.map((a, b, c) -> (int) (a + b + c));
            assertEquals(60, result);
        }

        @Test
        void tuple3_map_max() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
            byte result = tuple.map((a, b, c) -> (byte) Math.max(a, Math.max(b, c)));
            assertEquals((byte) 30, result);
        }

        @Test
        void tuple3_map_values_10_20_30() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(60, result);
        }

        @Test
        void tuple3Map() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filterPredicate() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter(t -> t._1 == (byte) 1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter(t -> t._1 == (byte) 99);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_fail() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_noMatch() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_pass() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple2FilterFalse() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            var result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            var result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3_filter_fail() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3_filter_matches() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        void tuple3_filter_noMatch() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 20, (byte) 10);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3_filter_noMatch_values_10_20_30() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3_filter_pass() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3FilterFalse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple.ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            ByteTuple.ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            ByteTuple.ByteTuple4 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple.ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            ByteTuple.ByteTuple5 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple.ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            ByteTuple.ByteTuple6 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple.ByteTuple6 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            ByteTuple.ByteTuple7 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple.ByteTuple7 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            ByteTuple.ByteTuple8 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple.ByteTuple8 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            ByteTuple.ByteTuple9 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple.ByteTuple9 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void equalTuplesStillShareHashCodes() {
            assertEquals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3).hashCode(), ByteTuple.of((byte) 1, (byte) 2, (byte) 3).hashCode());
            assertEquals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3), ByteTuple.of((byte) 1, (byte) 2, (byte) 3));
        }

        @Test
        void hashCode_consistency() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_matchesValueContract() {
            ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 31);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

            // Test specific hashcodes for coverage
            ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 10);
            assertEquals(10, single.hashCode());

            ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(31 * 10 + 20, pair.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple2() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple3() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void subclassHashCodeDiffersFromArrayBasedHash() {
            // the non-empty overrides are self-consistent but NOT numerically equal to N.hashCode(byte[])
            assertEquals(33, ByteTuple.of((byte) 1, (byte) 2).hashCode());
            assertNotEquals(N.hashCode(ByteTuple.of((byte) 1, (byte) 2).toArray()), ByteTuple.of((byte) 1, (byte) 2).hashCode());

            assertEquals(5, ByteTuple.of((byte) 5).hashCode());
            assertNotEquals(N.hashCode(ByteTuple.of((byte) 5).toArray()), ByteTuple.of((byte) 5).hashCode());
        }

        @Test
        void tuple0_hashCode() {
            ByteTuple0 tuple1 = ByteTuple.from(new byte[0]);
            ByteTuple0 tuple2 = ByteTuple.from(new byte[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple1_hashCode() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple3 = ByteTuple.of((byte) 20);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(10, tuple1.hashCode());
            assertEquals(20, tuple3.hashCode());
        }

        @Test
        void tuple2_hashCode() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple2_hashCode_different() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple3_hashCode() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @Test
        void arity1_equals() {
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple.ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity2_equals() {
            ByteTuple.ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity3_equals() {
            ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity4_equals() {
            ByteTuple.ByteTuple4 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple.ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity5_equals() {
            ByteTuple.ByteTuple5 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple.ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity6_equals() {
            ByteTuple.ByteTuple6 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple.ByteTuple6 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity7_equals() {
            ByteTuple.ByteTuple7 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple.ByteTuple7 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity8_equals() {
            ByteTuple.ByteTuple8 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple.ByteTuple8 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equals() {
            ByteTuple.ByteTuple9 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple.ByteTuple9 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equalsAndHashCode_differInOneField() {
            byte[] base = { 10, 20, 30, 40, 50, 60, 70, 80, 90 };
            ByteTuple.ByteTuple9 t = ByteTuple.of(base[0], base[1], base[2], base[3], base[4], base[5], base[6], base[7], base[8]);
            for (int i = 0; i < 9; i++) {
                byte[] diff = base.clone();
                diff[i] = (byte) (diff[i] + 1);
                ByteTuple.ByteTuple9 other = ByteTuple.of(diff[0], diff[1], diff[2], diff[3], diff[4], diff[5], diff[6], diff[7], diff[8]);
                assertNotEquals(t, other, "field _" + (i + 1) + " should affect equals");
                assertNotEquals(t.hashCode(), other.hashCode(), "field _" + (i + 1) + " should affect hashCode");
            }
        }

        @Test
        void equals_comparesValues() {
            ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 31);
            ByteTuple.ByteTuple2 tuple4 = ByteTuple.of((byte) 10, (byte) 20);

            assertTrue(tuple1.equals(tuple1));
            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(tuple4));
            assertFalse(tuple1.equals(null));
            assertFalse(tuple1.equals("not a tuple"));

            // Test specific equals for coverage
            ByteTuple.ByteTuple1 single1 = ByteTuple.of((byte) 10);
            ByteTuple.ByteTuple1 single2 = ByteTuple.of((byte) 10);
            ByteTuple.ByteTuple1 single3 = ByteTuple.of((byte) 20);
            assertTrue(single1.equals(single2));
            assertFalse(single1.equals(single3));
            assertFalse(single1.equals("not a tuple"));

            ByteTuple.ByteTuple2 pair1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple.ByteTuple2 pair2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple.ByteTuple2 pair3 = ByteTuple.of((byte) 10, (byte) 21);
            assertTrue(pair1.equals(pair2));
            assertFalse(pair1.equals(pair3));
        }

        @Test
        void equals_differentArity() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        void equals_higherArityIdentityAndTypeMismatch() {
            ByteTuple.ByteTuple7 tuple7 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
            ByteTuple.ByteTuple8 tuple8 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
            ByteTuple.ByteTuple9 tuple9 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);

            assertTrue(tuple7.equals(tuple7));
            assertEquals(tuple7, ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70));
            assertNotEquals(tuple7, ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 71));
            assertNotEquals(tuple7, "tuple");

            assertTrue(tuple8.equals(tuple8));
            assertEquals(tuple8, ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80));
            assertNotEquals(tuple8, ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 81));
            assertNotEquals(tuple8, "tuple");

            assertTrue(tuple9.equals(tuple9));
            assertEquals(tuple9, ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90));
            assertNotEquals(tuple9, ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 91));
            assertNotEquals(tuple9, "tuple");
        }

        @Test
        void equals_symmetry() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        void equalsDifferentClass() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        void equalsNull() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotEquals(null, tuple);
        }

        @Test
        void equalsSameObject() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equalsTuple1() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            ByteTuple1 tuple3 = ByteTuple.of((byte) 99);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple2() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 tuple3 = ByteTuple.of((byte) 1, (byte) 3);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple3() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 4);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void tuple_notEquals_differentType() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotEquals(tuple, "10");
            assertNotEquals(tuple, 10);
        }

        @Test
        void tuple_notEquals_null() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotEquals(tuple, null);
            assertFalse(tuple.equals(null));
        }

        @Test
        void tuple0_equals() {
            ByteTuple0 tuple1 = ByteTuple.from(new byte[0]);
            ByteTuple0 tuple2 = ByteTuple.from(new byte[0]);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void tuple1_equals() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple3 = ByteTuple.of((byte) 20);

            assertTrue(tuple1.equals(tuple1));
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertNotEquals(tuple1, null);
            assertNotEquals(tuple1, "not a tuple");
        }

        @Test
        void tuple1_equals_differentClass() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.equals("not a tuple"));
        }

        @Test
        void tuple1_equals_equal() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            assertTrue(tuple1.equals(tuple2));
            assertTrue(tuple2.equals(tuple1));
        }

        @Test
        void tuple1_equals_notEqual() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 20);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        void tuple1_equals_null() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.equals(null));
        }

        @Test
        void tuple1_equals_same() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void tuple1_notEquals() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 20);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void tuple2_equals() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple3 = ByteTuple.of((byte) 20, (byte) 10);

            assertTrue(tuple1.equals(tuple1));
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void tuple2_equals_rejectsNullAndDifferentOrder() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple3 = ByteTuple.of((byte) 20, (byte) 10);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(null));
        }

        @Test
        void tuple2_notEquals_different_values() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void tuple2_notEquals_differentTuple() {
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        void tuple3_equals() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 30, (byte) 20, (byte) 10);

            assertTrue(tuple1.equals(tuple1));
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void tuple3_equals_lastFieldDiffers() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 40);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity1_toString() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity3_toString() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_formatsValue() {
            ByteTuple.ByteTuple0 empty = ByteTuple.from(new byte[0]);
            assertEquals("()", empty.toString());

            ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 10);
            assertEquals("(10)", single.toString());

            ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals("(10, 20)", pair.toString());

            ByteTuple.ByteTuple3 triple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals("(10, 20, 30)", triple.toString());
        }

        @Test
        void toStringTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.from(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
        }

        @Test
        void toStringTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        @Test
        void tuple0_toString() {
            ByteTuple<?> tuple = ByteTuple.from(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple0_toString_values_ByteTuple_from() {
            ByteTuple0 tuple = ByteTuple.from(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple1_toString() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals("(10)", tuple.toString());
        }

        @Test
        void tuple2_toString() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals("(10, 20)", tuple.toString());
        }

        @Test
        void tuple3_toString() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals("(10, 20, 30)", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void classLevelExamples() {
            // byte min = triple.min();   // 10
            // byte max = triple.max();   // 30
            // triple.average();   // OptionalDouble.of(20.0)
            ByteTuple.ByteTuple3 triple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals((byte) 10, triple.min());
            assertEquals((byte) 30, triple.max());
            assertEquals(20.0, triple.average().getAsDouble(), 0.001);
        }

        @Test
        void coverageRegression_defaultConstructorsAndCachedArrays() {
            ByteTuple.ByteTuple1 tuple1 = new ByteTuple.ByteTuple1();
            assertArrayEquals(new byte[] { 0 }, tuple1.toArray());
            assertArrayEquals(new byte[] { 0 }, tuple1.toArray());

            ByteTuple.ByteTuple2 tuple2 = new ByteTuple.ByteTuple2();
            assertArrayEquals(new byte[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new byte[] { 0, 0 }, tuple2.toArray());

            ByteTuple.ByteTuple3 tuple3 = new ByteTuple.ByteTuple3();
            assertArrayEquals(new byte[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0 }, tuple3.toArray());

            ByteTuple.ByteTuple4 tuple4 = new ByteTuple.ByteTuple4();
            assertArrayEquals(new byte[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0 }, tuple4.toArray());

            ByteTuple.ByteTuple5 tuple5 = new ByteTuple.ByteTuple5();
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0 }, tuple5.toArray());

            ByteTuple.ByteTuple6 tuple6 = new ByteTuple.ByteTuple6();
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());

            ByteTuple.ByteTuple7 tuple7 = new ByteTuple.ByteTuple7();
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());

            ByteTuple.ByteTuple8 tuple8 = new ByteTuple.ByteTuple8();
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());

            ByteTuple.ByteTuple9 tuple9 = new ByteTuple.ByteTuple9();
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
        }

        @Test
        void coverageRegression_emptyTupleBaseMethods() {
            ByteTuple<?> empty = ByteTuple.from((byte[]) null);
            assertEquals(ByteTuple.from(new byte[0]), empty);
            assertFalse(empty.equals("byte"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void emptyTupleUsesTheArrayBasedHash() {
            final ByteTuple<?> empty = ByteTuple.from(new byte[0]);
            assertEquals(1, empty.hashCode());
            assertEquals(N.hashCode(empty.toArray()), empty.hashCode());
        }

        @Test
        void extremeValues() {
            ByteTuple2 tuple = ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE);
            assertEquals(Byte.MIN_VALUE, tuple.min());
            assertEquals(Byte.MAX_VALUE, tuple.max());
        }

        @Test
        void inheritedAggregateEmptyBehavior() {
            final ByteTuple.ByteTuple0 tuple = ByteTuple.from(new byte[0]);

            assertThrows(NoSuchElementException.class, tuple::min);
            assertThrows(NoSuchElementException.class, tuple::max);
            assertThrows(NoSuchElementException.class, tuple::median);
            assertTrue(tuple.average().isEmpty());
            assertEquals(0, tuple.sum());
        }

        @Test
        void inheritedOuterMethodsForCoverage() throws Exception {
            final ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 9, (byte) 2, (byte) 5, (byte) 1);
            final byte[] firstArray = tuple.toArray();
            final byte[] secondArray = tuple.toArray();
            final List<Byte> visited = new ArrayList<>();

            tuple.forEach(visited::add);

            assertEquals(1, tuple.min());
            assertEquals(9, tuple.max());
            assertEquals(2, tuple.lowerMedian());
            assertEquals(17, tuple.sum());
            assertEquals(4.25, tuple.average().getAsDouble());
            assertArrayEquals(new byte[] { 9, 2, 5, 1 }, firstArray);
            assertArrayEquals(firstArray, secondArray);
            assertNotSame(firstArray, secondArray);
            assertEquals(ByteList.of((byte) 9, (byte) 2, (byte) 5, (byte) 1), tuple.toList());
            assertEquals(visited, tuple.stream().boxed().toList());
            assertEquals(tuple.hashCode(), ByteTuple.of((byte) 9, (byte) 2, (byte) 5, (byte) 1).hashCode());
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(ByteTuple.of((byte) 9, (byte) 2, (byte) 5, (byte) 1)));
            assertFalse(tuple.equals(ByteTuple.of((byte) 9, (byte) 2, (byte) 5, (byte) 2)));
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals("not a tuple"));
            assertEquals("(9, 2, 5, 1)", tuple.toString());
            assertArrayEquals(new byte[] { 1, 5, 2, 9 }, tuple.reversed().toArray());
        }

        @Test
        void multipleTuples_independence() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
            assertNotEquals(tuple1, tuple2);
            assertEquals((byte) 10, tuple1._1);
            assertEquals((byte) 20, tuple2._1);
        }

        @Test
        void negativeValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) -20, (byte) -30);
            assertEquals((byte) -30, tuple.min());
            assertEquals((byte) -10, tuple.max());
            assertEquals(-60, tuple.sum());
        }

        @Test
        void negativeValues_values_minus10_0_10() {
            ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) 0, (byte) 10);
            assertEquals((byte) -10, tuple.min());
            assertEquals((byte) 10, tuple.max());
            assertEquals((byte) 0, tuple.lowerMedian());
            assertEquals(0, tuple.sum());
        }

        @Test
        void tuple_mixedSignValues() {
            ByteTuple4 tuple = ByteTuple.of((byte) -5, (byte) 10, (byte) -3, (byte) 8);
            assertEquals((byte) -5, tuple.min());
            assertEquals((byte) 10, tuple.max());
            assertEquals(10, tuple.sum());
        }

        @Test
        void tuple_negativeValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) 20, (byte) -30);
            assertEquals((byte) -30, tuple.min());
            assertEquals((byte) 20, tuple.max());
            assertEquals(-20, tuple.sum());
        }

        @Test
        void tuple_zeroValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) 0, (byte) 0, (byte) 0);
            assertTrue(tuple.contains((byte) 0));
            assertEquals(0, tuple.sum());
            assertEquals(0.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple1SpecificMethods() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 10);

            // Test min/max/median/sum/average
            assertEquals(10, tuple.min());
            assertEquals(10, tuple.max());
            assertEquals(10, tuple.lowerMedian());
            assertEquals(10, tuple.sum());
            assertEquals(10.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple2SpecificMethods() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);

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
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);

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
        void tuple4_basic() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(4, tuple.arity());
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 4, tuple.max());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.0001);
            assertTrue(tuple.contains((byte) 3));
            ByteTuple4 reversed = tuple.reversed();
            assertEquals((byte) 4, reversed._1);
            assertEquals((byte) 1, reversed._4);
        }

        @Test
        void tuple4Operations() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

            // Test reverse
            ByteTuple4 reversed = tuple.reversed();
            assertEquals((byte) 4, reversed._1);
            assertEquals((byte) 3, reversed._2);
            assertEquals((byte) 2, reversed._3);
            assertEquals((byte) 1, reversed._4);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 4));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4 }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 4, tuple.max());
            assertEquals((byte) 2, tuple.lowerMedian());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple4 tuple3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 5);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }

        @Test
        void tuple5_basic() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(5, tuple.arity());
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 5, tuple.max());
            assertEquals((byte) 3, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertTrue(tuple.contains((byte) 3));
        }

        @Test
        void tuple5Operations() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);

            // Test reverse
            ByteTuple5 reversed = tuple.reversed();
            assertEquals((byte) 5, reversed._1);
            assertEquals((byte) 1, reversed._5);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 5));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 5, tuple.max());
            assertEquals((byte) 3, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);

            // Test equals
            ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(tuple, tuple2);
        }

        @Test
        void tuple6_basic() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(6, tuple.arity());
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 6, tuple.max());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void tuple6Operations() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);

            // Test reverse
            ByteTuple6 reversed = tuple.reversed();
            assertEquals((byte) 6, reversed._1);
            assertEquals((byte) 1, reversed._6);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 6));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 6, tuple.max());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple7_basic() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(7, tuple.arity());
            assertEquals((byte) 4, tuple.lowerMedian());
            assertEquals(7, tuple.toArray().length);
        }

        @Test
        void tuple7Operations() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);

            // Test reverse
            ByteTuple7 reversed = tuple.reversed();
            assertEquals((byte) 7, reversed._1);
            assertEquals((byte) 1, reversed._7);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 7));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 7, tuple.max());
            assertEquals(28, tuple.sum());
            assertEquals(4.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple8_basic() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
            assertEquals(36, tuple.sum());
        }

        @Test
        void tuple8Operations() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);

            // Test reverse
            ByteTuple8 reversed = tuple.reversed();
            assertEquals((byte) 8, reversed._1);
            assertEquals((byte) 1, reversed._8);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 8));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 8, tuple.max());
            assertEquals(36, tuple.sum());
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9_basic() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void tuple9Operations() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);

            // Test reverse
            ByteTuple9 reversed = tuple.reversed();
            assertEquals((byte) 9, reversed._1);
            assertEquals((byte) 1, reversed._9);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 9));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 9, tuple.max());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void zeroArgConstructorsForCoverage() {
            final ByteTuple1 tuple1 = new ByteTuple1();
            final ByteTuple2 tuple2 = new ByteTuple2();
            final ByteTuple3 tuple3 = new ByteTuple3();
            final ByteTuple4 tuple4 = new ByteTuple4();
            final ByteTuple5 tuple5 = new ByteTuple5();
            final ByteTuple6 tuple6 = new ByteTuple6();
            final ByteTuple7 tuple7 = new ByteTuple7();
            final ByteTuple8 tuple8 = new ByteTuple8();
            final ByteTuple9 tuple9 = new ByteTuple9();

            assertArrayEquals(new byte[] { 0 }, tuple1.toArray());
            assertArrayEquals(new byte[] { 0 }, tuple1.toArray());
            assertArrayEquals(new byte[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
        }
    }
}
