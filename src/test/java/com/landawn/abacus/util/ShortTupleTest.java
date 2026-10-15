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
import org.junit.jupiter.params.provider.ValueSource;

import com.landawn.abacus.util.ShortTuple.ShortTuple0;
import com.landawn.abacus.util.ShortTuple.ShortTuple1;
import com.landawn.abacus.util.ShortTuple.ShortTuple2;
import com.landawn.abacus.util.ShortTuple.ShortTuple3;
import com.landawn.abacus.util.ShortTuple.ShortTuple4;
import com.landawn.abacus.util.ShortTuple.ShortTuple5;
import com.landawn.abacus.util.ShortTuple.ShortTuple6;
import com.landawn.abacus.util.ShortTuple.ShortTuple7;
import com.landawn.abacus.util.ShortTuple.ShortTuple8;
import com.landawn.abacus.util.ShortTuple.ShortTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.ShortStream;

class ShortTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, short[] values) {
            ShortTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of3Average() {
            // ShortTuple.ShortTuple3 triple = ShortTuple.of((short)1, (short)2, (short)3);
            // triple.average();   // OptionalDouble.of(2.0)
            ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        private static ShortTuple<?> createWithOf(short[] values) {
            return switch (values.length) {
                case 1 -> ShortTuple.of(values[0]);
                case 2 -> ShortTuple.of(values[0], values[1]);
                case 3 -> ShortTuple.of(values[0], values[1], values[2]);
                case 4 -> ShortTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> ShortTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: (short)1", new short[] { (short) 1 }), Arguments.of("1: (short)42", new short[] { (short) 42 }),
                    Arguments.of("1: (short)5", new short[] { (short) 5 }), Arguments.of("2: (short)1, (short)2", new short[] { (short) 1, (short) 2 }),
                    Arguments.of("2: (short)10, (short)20", new short[] { (short) 10, (short) 20 }),
                    Arguments.of("2: (short)5, (short)10", new short[] { (short) 5, (short) 10 }),
                    Arguments.of("3: (short)1, (short)2, (short)3", new short[] { (short) 1, (short) 2, (short) 3 }),
                    Arguments.of("3: (short)5, (short)10, (short)15", new short[] { (short) 5, (short) 10, (short) 15 }),
                    Arguments.of("4: (short)1, (short)2, (short)3, (short)4", new short[] { (short) 1, (short) 2, (short) 3, (short) 4 }),
                    Arguments.of("4: (short)5, (short)10, (short)15, (short)20", new short[] { (short) 5, (short) 10, (short) 15, (short) 20 }),
                    Arguments.of("5: (short)1, (short)2, (short)3, (short)4, (short)5", new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5 }),
                    Arguments.of("5: (short)5, (short)10, (short)15, (short)20, (short)25",
                            new short[] { (short) 5, (short) 10, (short) 15, (short) 20, (short) 25 }),
                    Arguments.of("6: (short)1, (short)2, (short)3, (short)4, (short)5, (short)6",
                            new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6 }),
                    Arguments.of("6: (short)5, (short)10, (short)15, (short)20, (short)25, (short)30",
                            new short[] { (short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30 }),
                    Arguments.of("7: (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7",
                            new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7 }),
                    Arguments.of("7: (short)5, (short)10, (short)15, (short)20, (short)25, (short)30, (short)35",
                            new short[] { (short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35 }),
                    Arguments.of("8: (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8",
                            new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8 }),
                    Arguments.of("8: (short)5, (short)10, (short)15, (short)20, (short)25, (short)30, (short)35, (short)40",
                            new short[] { (short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40 }),
                    Arguments.of("9: (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9",
                            new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9 }),
                    Arguments.of("9: (short)5, (short)10, (short)15, (short)20, (short)25, (short)30, (short)35, (short)40, (short)45",
                            new short[] { (short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40, (short) 45 }));
        }

        private static short[] publicFields(ShortTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    ShortTuple.ShortTuple1 value = (ShortTuple.ShortTuple1) tuple;
                    yield new short[] { value._1 };
                }
                case 2 -> {
                    ShortTuple.ShortTuple2 value = (ShortTuple.ShortTuple2) tuple;
                    yield new short[] { value._1, value._2 };
                }
                case 3 -> {
                    ShortTuple.ShortTuple3 value = (ShortTuple.ShortTuple3) tuple;
                    yield new short[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    ShortTuple.ShortTuple4 value = (ShortTuple.ShortTuple4) tuple;
                    yield new short[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    ShortTuple.ShortTuple5 value = (ShortTuple.ShortTuple5) tuple;
                    yield new short[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    ShortTuple.ShortTuple6 value = (ShortTuple.ShortTuple6) tuple;
                    yield new short[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    ShortTuple.ShortTuple7 value = (ShortTuple.ShortTuple7) tuple;
                    yield new short[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    ShortTuple.ShortTuple8 value = (ShortTuple.ShortTuple8) tuple;
                    yield new short[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    ShortTuple.ShortTuple9 value = (ShortTuple.ShortTuple9) tuple;
                    yield new short[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
                }
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + tuple.arity());
            };
        }
    }

    @Nested
    class FromTests {

        @Test
        void create_emptyArray() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_multipleElementsArray() {
            ShortTuple3 tuple = ShortTuple.from(new short[] { (short) 1, (short) 2, (short) 3 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        void create_nullArray() {
            ShortTuple0 tuple = ShortTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void create_singleElementArray() {
            ShortTuple1 tuple = ShortTuple.from(new short[] { (short) 42 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals((short) 42, tuple._1);
        }

        @Test
        void create_tooManyElements() {
            short[] array = new short[10];
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.from(array));
        }

        @Test
        void from_emptyArray() {
            ShortTuple<?> tuple = ShortTuple.from(new short[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nullArray() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_preservesElements() {
            // Test empty array
            ShortTuple<?> empty = ShortTuple.from(null);
            assertEquals(0, empty.arity());

            empty = ShortTuple.from(new short[0]);
            assertEquals(0, empty.arity());

            // Test array with 1 element
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.from(new short[] { 5 });
            assertEquals(1, tuple1.arity());
            assertEquals(5, tuple1._1);

            // Test array with 5 elements
            ShortTuple.ShortTuple5 tuple5 = ShortTuple.from(new short[] { 5, 10, 15, 20, 25 });
            assertEquals(5, tuple5.arity());
            assertEquals(25, tuple5._5);

            // Test array with 9 elements
            ShortTuple.ShortTuple9 tuple9 = ShortTuple.from(new short[] { 5, 10, 15, 20, 25, 30, 35, 40, 45 });
            assertEquals(9, tuple9.arity());
            assertEquals(45, tuple9._9);

            // Test too many elements
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.from(new short[10]));
        }

        @Test
        void from_sizeEight() {
            ShortTuple8 tuple = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(8, tuple.arity());
            assertEquals((short) 8, tuple._8);
        }

        @Test
        void from_sizeFive() {
            ShortTuple5 tuple = ShortTuple.from(new short[] { 1, 2, 3, 4, 5 });
            assertEquals(5, tuple.arity());
            assertEquals((short) 5, tuple._5);
        }

        @Test
        void from_sizeFour() {
            ShortTuple4 tuple = ShortTuple.from(new short[] { 1, 2, 3, 4 });
            assertEquals(4, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 4, tuple._4);
        }

        @Test
        void from_sizeNine() {
            ShortTuple9 tuple = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple.arity());
            assertEquals((short) 9, tuple._9);
        }

        @Test
        void from_sizeOne() {
            ShortTuple1 tuple = ShortTuple.from(new short[] { 1 });
            assertEquals(1, tuple.arity());
            assertEquals((short) 1, tuple._1);
        }

        @Test
        void from_sizeSeven() {
            ShortTuple7 tuple = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(7, tuple.arity());
            assertEquals((short) 7, tuple._7);
        }

        @Test
        void from_sizeSix() {
            ShortTuple6 tuple = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(6, tuple.arity());
            assertEquals((short) 6, tuple._6);
        }

        @Test
        void from_sizeThree() {
            ShortTuple3 tuple = ShortTuple.from(new short[] { 1, 2, 3 });
            assertEquals(3, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        void from_sizeTwo() {
            ShortTuple2 tuple = ShortTuple.from(new short[] { 1, 2 });
            assertEquals(2, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
        }

        @Test
        void from_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        void from1() {
            ShortTuple1 tuple = ShortTuple.from(new short[] { (short) 1 });
            assertEquals((short) 1, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2Through8() {
            ShortTuple2 tuple2 = ShortTuple.from(new short[] { 1, 2 });
            assertEquals((short) 1, tuple2._1);
            assertEquals((short) 2, tuple2._2);

            ShortTuple4 tuple4 = ShortTuple.from(new short[] { 1, 2, 3, 4 });
            assertEquals((short) 1, tuple4._1);
            assertEquals((short) 4, tuple4._4);

            ShortTuple5 tuple5 = ShortTuple.from(new short[] { 1, 2, 3, 4, 5 });
            assertEquals((short) 1, tuple5._1);
            assertEquals((short) 5, tuple5._5);

            ShortTuple6 tuple6 = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6 });
            assertEquals((short) 1, tuple6._1);
            assertEquals((short) 6, tuple6._6);

            ShortTuple7 tuple7 = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals((short) 1, tuple7._1);
            assertEquals((short) 7, tuple7._7);

            ShortTuple8 tuple8 = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals((short) 1, tuple8._1);
            assertEquals((short) 8, tuple8._8);
        }

        @Test
        void from3() {
            ShortTuple3 tuple = ShortTuple.from(new short[] { (short) 1, (short) 2, (short) 3 });
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        void from9() {
            ShortTuple9 tuple = ShortTuple
                    .from(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9 });
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 9, tuple._9);
        }

        @Test
        void fromEmpty() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromFromArray() {
            // Empty array
            ShortTuple<?> empty = ShortTuple.from(null);
            assertEquals(0, empty.arity());

            empty = ShortTuple.from(new short[0]);
            assertEquals(0, empty.arity());

            // Single element
            ShortTuple1 tuple1 = ShortTuple.of((short) 42);
            assertEquals(1, tuple1.arity());
            assertEquals((short) 42, tuple1._1);

            // Multiple elements
            ShortTuple<?> tuple3 = ShortTuple.from(new short[] { 10, 20, 30 });
            assertEquals(3, tuple3.arity());

            // Max size
            ShortTuple<?> tuple9 = ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple9.arity());

            // Too many elements
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.from(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        void fromNull() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                ShortTuple.from(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10 });
            });
        }
    }

    @Nested
    class ArityTests {

        @Test
        void arity_returnsElementCount() {
            assertEquals(0, ShortTuple.from(new short[0]).arity());
            assertEquals(1, ShortTuple.of((short) 1).arity());
            assertEquals(2, ShortTuple.of((short) 1, (short) 2).arity());
            assertEquals(3, ShortTuple.of((short) 1, (short) 2, (short) 3).arity());
            assertEquals(4, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).arity());
            assertEquals(5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).arity());
            assertEquals(6, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6).arity());
            assertEquals(7, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7).arity());
            assertEquals(8, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8).arity());
            assertEquals(9, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9).arity());
        }

        @Test
        void arity0() {
            ShortTuple<?> empty = ShortTuple.from(null);
            assertEquals(0, empty.arity());
        }

        @Test
        void arity0_arity() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        void coverageRegression_highArityEqualsBranches() {
            ShortTuple.ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertTrue(tuple4.equals(tuple4));
            assertTrue(tuple4.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4)));
            assertFalse(tuple4.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 9)));
            assertFalse(tuple4.equals("tuple4"));

            ShortTuple.ShortTuple5 tuple5 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertTrue(tuple5.equals(tuple5));
            assertTrue(tuple5.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
            assertFalse(tuple5.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 9)));
            assertFalse(tuple5.equals("tuple5"));

            ShortTuple.ShortTuple6 tuple6 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertTrue(tuple6.equals(tuple6));
            assertTrue(tuple6.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6)));
            assertFalse(tuple6.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 9)));
            assertFalse(tuple6.equals("tuple6"));

            ShortTuple.ShortTuple7 tuple7 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertTrue(tuple7.equals(tuple7));
            assertTrue(tuple7.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7)));
            assertFalse(tuple7.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 9)));
            assertFalse(tuple7.equals("tuple7"));

            ShortTuple.ShortTuple8 tuple8 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertTrue(tuple8.equals(tuple8));
            assertTrue(tuple8.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8)));
            assertFalse(tuple8.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 9)));
            assertFalse(tuple8.equals("tuple8"));

            ShortTuple.ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertTrue(tuple9.equals(tuple9));
            assertTrue(tuple9.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9)));
            assertFalse(tuple9.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 0)));
            assertFalse(tuple9.equals("tuple9"));
        }

        @Test
        void coverageRegression_highArityOperations() {
            ShortTuple.ShortTuple4 tuple4 = ShortTuple.of((short) 9, (short) 1, (short) 7, (short) 3);
            assertEquals((short) 1, tuple4.min());
            assertEquals((short) 9, tuple4.max());
            assertEquals((short) 3, tuple4.lowerMedian());
            assertEquals(20, tuple4.sum());
            assertEquals(5.0, tuple4.average().getAsDouble());
            assertTrue(tuple4.contains((short) 3));

            ShortTuple.ShortTuple5 tuple5 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertTrue(tuple5.contains((short) 5));

            ShortTuple.ShortTuple6 tuple6 = ShortTuple.of((short) 9, (short) 1, (short) 7, (short) 3, (short) 5, (short) 11);
            assertEquals((short) 5, tuple6.lowerMedian());
            assertTrue(tuple6.contains((short) 11));

            ShortTuple.ShortTuple7 tuple7 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertTrue(tuple7.contains((short) 7));

            ShortTuple.ShortTuple8 tuple8 = ShortTuple.of((short) 9, (short) 1, (short) 7, (short) 3, (short) 5, (short) 11, (short) 13, (short) 15);
            assertEquals((short) 7, tuple8.lowerMedian());
            assertTrue(tuple8.contains((short) 15));

            ShortTuple.ShortTuple9 tuple9 = ShortTuple.of((short) 9, (short) 1, (short) 7, (short) 3, (short) 5, (short) 11, (short) 13, (short) 15,
                    (short) 17);
            assertEquals((short) 9, tuple9.lowerMedian());
            assertTrue(tuple9.contains((short) 17));
        }

        @Test
        void largeArityContainsAndEqualsBranchesForCoverage() {
            final ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            final ShortTuple5 tuple5 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            final ShortTuple6 tuple6 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            final ShortTuple7 tuple7 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            final ShortTuple8 tuple8 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            final ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

            assertTrue(tuple4.contains((short) 2));
            assertTrue(tuple4.contains((short) 3));
            assertTrue(tuple4.contains((short) 4));
            assertFalse(tuple4.contains((short) 99));
            assertTrue(tuple5.contains((short) 2));
            assertTrue(tuple5.contains((short) 3));
            assertTrue(tuple5.contains((short) 4));
            assertTrue(tuple5.contains((short) 5));
            assertFalse(tuple5.contains((short) 99));
            assertTrue(tuple6.contains((short) 2));
            assertTrue(tuple6.contains((short) 3));
            assertTrue(tuple6.contains((short) 4));
            assertTrue(tuple6.contains((short) 5));
            assertTrue(tuple6.contains((short) 6));
            assertFalse(tuple6.contains((short) 99));
            assertTrue(tuple7.contains((short) 2));
            assertTrue(tuple7.contains((short) 3));
            assertTrue(tuple7.contains((short) 4));
            assertTrue(tuple7.contains((short) 5));
            assertTrue(tuple7.contains((short) 6));
            assertTrue(tuple7.contains((short) 7));
            assertFalse(tuple7.contains((short) 99));
            assertTrue(tuple8.contains((short) 2));
            assertTrue(tuple8.contains((short) 3));
            assertTrue(tuple8.contains((short) 4));
            assertTrue(tuple8.contains((short) 5));
            assertTrue(tuple8.contains((short) 6));
            assertTrue(tuple8.contains((short) 7));
            assertTrue(tuple8.contains((short) 8));
            assertFalse(tuple8.contains((short) 99));
            assertTrue(tuple9.contains((short) 2));
            assertTrue(tuple9.contains((short) 3));
            assertTrue(tuple9.contains((short) 4));
            assertTrue(tuple9.contains((short) 5));
            assertTrue(tuple9.contains((short) 6));
            assertTrue(tuple9.contains((short) 7));
            assertTrue(tuple9.contains((short) 8));
            assertTrue(tuple9.contains((short) 9));
            assertFalse(tuple9.contains((short) 99));

            assertTrue(tuple4.equals(tuple4));
            assertFalse(tuple4.equals("not a tuple"));
            assertFalse(tuple4.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 9)));
            assertTrue(tuple5.equals(tuple5));
            assertFalse(tuple5.equals("not a tuple"));
            assertFalse(tuple5.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 9)));
            assertTrue(tuple6.equals(tuple6));
            assertFalse(tuple6.equals("not a tuple"));
            assertFalse(tuple6.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 9)));
            assertTrue(tuple7.equals(tuple7));
            assertFalse(tuple7.equals("not a tuple"));
            assertFalse(tuple7.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 9)));
            assertTrue(tuple8.equals(tuple8));
            assertFalse(tuple8.equals("not a tuple"));
            assertFalse(tuple8.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 9)));
            assertTrue(tuple9.equals(tuple9));
            assertFalse(tuple9.equals("not a tuple"));
            assertFalse(tuple9.equals(ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 0)));
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void elementsMethod() {
            // Just test that elements are cached properly
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            short[] elements1 = tuple.elements();
            short[] elements2 = tuple.elements();
            assertSame(elements1, elements2); // Should return same cached array
        }
    }

    @Nested
    class MinTests {

        @Test
        void arity0_min_throwsException() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void arity1_min() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.min());
        }

        @Test
        void arity1_min_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals((short) 5, tuple.min());
        }

        @Test
        void arity2_min() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min_values_3_1() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 1);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        void arity3_min() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min_values_3_1_2() {
            ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        void min_empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.from(null).min());
        }

        @Test
        void min_returnsMinimum() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
            assertEquals(5, tuple.min());

            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> empty.min());
        }

        @Test
        void min_returnsMinimum_values_5_2_8_min() {
            assertEquals((short) 2, ShortTuple.of((short) 5, (short) 2, (short) 8).min());
            assertEquals((short) 1, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).min());
            assertEquals((short) -10, ShortTuple.of((short) -5, (short) -10, (short) 0).min());
        }

        @Test
        void min_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals((short) 2, tuple.min());
        }

        @Test
        void min_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8);
            assertEquals((short) 2, tuple.min());
        }

        @Test
        void min_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8, (short) 1);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        void minTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        void tuple0_min() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity0_max_throwsException() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void arity1_max() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.max());
        }

        @Test
        void arity1_max_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals((short) 5, tuple.max());
        }

        @Test
        void arity2_max() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max_values_3_1() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 1);
            assertEquals((short) 3, tuple.max());
        }

        @Test
        void arity3_max() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max_values_3_1_2() {
            ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
            assertEquals((short) 3, tuple.max());
        }

        @Test
        void max_empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.from(null).max());
        }

        @Test
        void max_returnsMaximum() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
            assertEquals(15, tuple.max());

            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> empty.max());
        }

        @Test
        void max_returnsMaximum_values_5_2_8_max() {
            assertEquals((short) 8, ShortTuple.of((short) 5, (short) 2, (short) 8).max());
            assertEquals((short) 5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).max());
            assertEquals((short) 10, ShortTuple.of((short) -5, (short) 0, (short) 10).max());
        }

        @Test
        void max_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals((short) 5, tuple.max());
        }

        @Test
        void max_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8);
            assertEquals((short) 8, tuple.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple.max());
        }

        @Test
        void tuple0_max() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void withMaxValues() {
            short max = Short.MAX_VALUE / 2;
            ShortTuple2 tuple = ShortTuple.of(max, max);
            assertEquals(max, tuple.min());
            assertEquals(max, tuple.max());
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity0_sum() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void arity1_sum() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity1_sum_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(5, tuple.sum());
        }

        @Test
        void arity2_sum() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum_values_1_2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals(3, tuple.sum());
        }

        @Test
        void arity3_sum() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(6, tuple.sum());
        }

        @Test
        void sum_returnsInt() {
            ShortTuple3 tuple = ShortTuple.of((short) 100, (short) 200, (short) 300);
            int sum = tuple.sum();
            assertEquals(600, sum);
            // Note: sum() returns int (not short) for ShortTuple
        }

        @Test
        void sum_returnsTotal() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            assertEquals(30, tuple.sum());

            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertEquals(0, empty.sum());
        }

        @Test
        void sum_returnsTotal_values_10_20_30_sum() {
            assertEquals(60, ShortTuple.of((short) 10, (short) 20, (short) 30).sum());
            assertEquals(15, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).sum());
            assertEquals(0, ShortTuple.from(null).sum());
        }

        @Test
        void sum_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals(7, tuple.sum());
        }

        @Test
        void sumTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void sumTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1, tuple.sum());
        }

        @Test
        void tuple0_sum() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertEquals(0, tuple.sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity0_average_returnsEmpty() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void arity1_average() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity1_average_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity2_average() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average_values_1_3() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void arity3_average() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void average_empty() {
            assertTrue(ShortTuple.from(null).average().isEmpty());
        }

        @Test
        void average_returnsMean() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            assertEquals(10.0, tuple.average().getAsDouble());

            final ShortTuple.ShortTuple2 balanced = ShortTuple.of((short) -1, (short) 1);
            assertTrue(balanced.average().isPresent());
            assertEquals(0.0, balanced.average().getAsDouble());
            assertEquals(-0.5, ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE).average().getAsDouble());

            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void average_returnsMean_values_10_20_30_average_getAsDouble_1_0Eminus4() {
            assertEquals(20.0, ShortTuple.of((short) 10, (short) 20, (short) 30).average().getAsDouble(), 0.0001);
            assertEquals(3.0, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).average().getAsDouble(), 0.0001);
        }

        @Test
        void average_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 4, (short) 6);
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(2.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void average_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple0_average() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertTrue(tuple.average().isEmpty());
        }
    }

    @Nested
    class MedianTests {

        @Test
        void arity0_median_throwsException() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void arity1_median() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity1_median_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals((short) 5, tuple.lowerMedian());
        }

        @Test
        void arity2_median() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median_values_1_2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            short median = tuple.lowerMedian();
            assertEquals((short) 1, median);
        }

        @Test
        void arity3_median() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median_values_30_10_20() {
            ShortTuple3 tuple = ShortTuple.of((short) 30, (short) 10, (short) 20);
            assertEquals((short) 20, tuple.lowerMedian());
        }

        @Test
        void median_empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.from(null).lowerMedian());
        }

        @Test
        void median_evenSize() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            short median = tuple.lowerMedian();
            assertEquals((short) 2, median);
        }

        @Test
        void median_oddSize() {
            ShortTuple5 tuple = ShortTuple.of((short) 50, (short) 10, (short) 30, (short) 20, (short) 40);
            short median = tuple.lowerMedian();
            assertEquals((short) 30, median);
        }

        @Test
        void median_returnsMedian() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
            assertEquals(10, tuple.lowerMedian());

            ShortTuple.ShortTuple4 evenTuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20);
            assertEquals((short) 10, evenTuple.lowerMedian()); // even arity returns the lower of the two middle values: 10

            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> empty.lowerMedian());
        }

        @Test
        void median_returnsMedian_values_3_1_2_lowerMedian() {
            // Odd number of elements - returns middle value
            assertEquals((short) 2, ShortTuple.of((short) 3, (short) 1, (short) 2).lowerMedian());
            assertEquals((short) 3, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).lowerMedian());

            // Even number of elements - returns lower middle value
            assertEquals((short) 2, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).lowerMedian());
        }

        @Test
        void median_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals((short) 2, tuple.lowerMedian());
        }

        @Test
        void median_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 3, (short) 2);
            assertEquals((short) 2, tuple.lowerMedian());
        }

        @Test
        void median_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals((short) 2, tuple.lowerMedian());
        }

        @Test
        void medianTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple.lowerMedian());
        }

        @Test
        void medianTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
            assertEquals((short) 2, tuple.lowerMedian());
        }

        @Test
        void tuple0_median() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }
    }

    @Nested
    class LowerMedianTests {

        @Test
        void lowerMedian_evenArityDocExamplesDifferFromMedian() {
            assertEquals((short) 2, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).lowerMedian());
            assertEquals(2.5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).median());
            assertEquals((short) 3, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6).lowerMedian());
            assertEquals(3.5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6).median());
            assertEquals((short) 4, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8).lowerMedian());
            assertEquals(4.5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8).median());
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity0_reversed() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            ShortTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void arity1_reversed() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortTuple.ShortTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity1_reversed_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            ShortTuple1 reversed = tuple.reversed();
            assertEquals((short) 5, reversed._1);
        }

        @Test
        void arity2_reversed() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            ShortTuple.ShortTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity2_reversed_values_1_2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 reversed = tuple.reversed();
            assertEquals((short) 2, reversed._1);
            assertEquals((short) 1, reversed._2);
        }

        @Test
        void arity3_reversed() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple.ShortTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity3_reversed_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 reversed = tuple.reversed();
            assertEquals((short) 3, reversed._1);
            assertEquals((short) 2, reversed._2);
            assertEquals((short) 1, reversed._3);
        }

        @Test
        void arity4_reversed() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple.ShortTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple.ShortTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple.ShortTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple.ShortTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple.ShortTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple.ShortTuple9 reversed = tuple.reversed();
            assertEquals((short) 9, reversed._1);
            assertEquals((short) 8, reversed._2);
            assertEquals((short) 7, reversed._3);
            assertEquals((short) 6, reversed._4);
            assertEquals((short) 5, reversed._5);
            assertEquals((short) 4, reversed._6);
            assertEquals((short) 3, reversed._7);
            assertEquals((short) 2, reversed._8);
            assertEquals((short) 1, reversed._9);
        }

        @Test
        void reversed_reversesElementOrder() {
            // Test Tuple0
            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            ShortTuple.ShortTuple0 reversedEmpty = empty.reversed();
            assertEquals(0, reversedEmpty.arity());

            // Test Tuple1
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple.ShortTuple1 reversed1 = tuple1.reversed();
            assertEquals(5, reversed1._1);

            // Test Tuple2
            ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 5, (short) 10);
            ShortTuple.ShortTuple2 reversed2 = tuple2.reversed();
            assertEquals(10, reversed2._1);
            assertEquals(5, reversed2._2);

            // Test Tuple3
            ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 15);
            ShortTuple.ShortTuple3 reversed3 = tuple3.reversed();
            assertEquals(15, reversed3._1);
            assertEquals(10, reversed3._2);
            assertEquals(5, reversed3._3);

            // Test larger tuples
            ShortTuple.ShortTuple5 tuple5 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25);
            ShortTuple.ShortTuple5 reversed5 = tuple5.reversed();
            assertEquals(25, reversed5._1);
            assertEquals(20, reversed5._2);
            assertEquals(15, reversed5._3);
            assertEquals(10, reversed5._4);
            assertEquals(5, reversed5._5);
        }

        @Test
        void reversed_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortTuple1 reversed = tuple.reversed();
            assertEquals((short) 1, reversed._1);
        }

        @Test
        void reversed_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple4 reversed = tuple.reversed();
            assertEquals((short) 4, reversed._1);
            assertEquals((short) 1, reversed._4);
        }

        @Test
        void reversed0() {
            ShortTuple<?> reversed = ShortTuple.from(null).reversed();
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversed1() {
            ShortTuple1 reversed = ShortTuple.of((short) 42).reversed();
            assertEquals((short) 42, reversed._1);
        }

        @Test
        void reversed2() {
            ShortTuple2 reversed = ShortTuple.of((short) 10, (short) 20).reversed();
            assertEquals((short) 20, reversed._1);
            assertEquals((short) 10, reversed._2);
        }

        @Test
        void reversed3() {
            ShortTuple3 reversed = ShortTuple.of((short) 1, (short) 2, (short) 3).reversed();
            assertEquals((short) 3, reversed._1);
            assertEquals((short) 2, reversed._2);
            assertEquals((short) 1, reversed._3);
        }

        @Test
        void reversedTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            ShortTuple<ShortTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void tuple0_reversed() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertNotNull(tuple.reversed());
        }

        @Test
        void tuple5_reversed() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple5 reversed = tuple.reversed();
            assertEquals((short) 5, reversed._1);
            assertEquals((short) 4, reversed._2);
            assertEquals((short) 3, reversed._3);
            assertEquals((short) 2, reversed._4);
            assertEquals((short) 1, reversed._5);
        }

        @Test
        void tuple9_reversed() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple9 reversed = tuple.reversed();
            assertEquals((short) 9, reversed._1);
            assertEquals((short) 1, reversed._9);
        }
    }

    @Nested
    class ContainsTests {

        @ParameterizedTest(name = "arity {0}: contains checks every component")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void contains_checksEveryComponentAndRejectsMissingValue(int arity) {
            short[] values = new short[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = (short) (i + 1);
            }
            ShortTuple<?> tuple = ShortTuple.from(values);
            for (short value : values) {
                assertTrue(tuple.contains(value), "missing component " + value);
            }
            assertFalse(tuple.contains((short) 100));
        }

        @Test
        void arity0_contains() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertFalse(tuple.contains((short) 1));
        }

        @Test
        void arity1_contains() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity1_contains_found() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertTrue(tuple.contains((short) 5));
        }

        @Test
        void arity1_contains_notFound() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        void arity2_contains() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity2_contains_found() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 2));
        }

        @Test
        void arity2_contains_notFound() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertFalse(tuple.contains((short) 3));
        }

        @Test
        void arity3_contains() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity3_contains_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 2));
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        void arity4_contains() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity5_contains() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity6_contains() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity7_contains() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity8_contains() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void arity9_contains() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void contains_empty() {
            assertFalse(ShortTuple.from(null).contains((short) 10));
        }

        @Test
        void contains_findsPresentElements() {
            // Test Tuple0
            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertFalse(empty.contains((short) 5));

            // Test Tuple1
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            assertTrue(tuple1.contains((short) 5));
            assertFalse(tuple1.contains((short) 10));

            // Test Tuple3
            ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 15);
            assertTrue(tuple3.contains((short) 5));
            assertTrue(tuple3.contains((short) 10));
            assertTrue(tuple3.contains((short) 15));
            assertFalse(tuple3.contains((short) 20));

            // Test larger tuples
            ShortTuple.ShortTuple7 tuple7 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35);
            assertTrue(tuple7.contains((short) 35));
            assertFalse(tuple7.contains((short) 40));
        }

        @Test
        void contains_findsPresentElements_values_10_20_30() {
            ShortTuple3 tuple = ShortTuple.of((short) 10, (short) 20, (short) 30);
            assertTrue(tuple.contains((short) 20));
            assertFalse(tuple.contains((short) 40));
            assertTrue(tuple.contains((short) 10));
            assertTrue(tuple.contains((short) 30));
        }

        @Test
        void contains_single() {
            ShortTuple1 tuple = ShortTuple.of((short) 42);
            assertTrue(tuple.contains((short) 42));
            assertFalse(tuple.contains((short) 43));
        }

        @Test
        void contains_tuple1_notFound() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertFalse(tuple.contains((short) 2));
        }

        @Test
        void contains_tuple2_found() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.contains((short) 2));
        }

        @Test
        void contains_tuple2_notFound() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        void contains_tuple3_found() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 2));
        }

        @Test
        void contains_tuple3_notFound() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        void contains_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 4));
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        void contains_tuple5() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertTrue(tuple.contains((short) 5));
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        void contains_tuple6() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertTrue(tuple.contains((short) 6));
            assertFalse(tuple.contains((short) 7));
        }

        @Test
        void contains_tuple7() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertTrue(tuple.contains((short) 7));
            assertFalse(tuple.contains((short) 8));
        }

        @Test
        void contains_tuple8() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertTrue(tuple.contains((short) 8));
            assertFalse(tuple.contains((short) 9));
        }

        @Test
        void contains_tuple9() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertTrue(tuple.contains((short) 9));
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        void containsTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertFalse(tuple.contains((short) 1));
        }

        @Test
        void containsTuple1False() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertFalse(tuple.contains((short) 99));
        }

        @Test
        void containsTuple1True() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        void containsTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 2));
            assertTrue(tuple.contains((short) 3));
            assertFalse(tuple.contains((short) 99));
        }

        @Test
        void tuple0_contains() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertFalse(tuple.contains((short) 1));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_empty() {
            short[] array = ShortTuple.from(null).toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArray_independence() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] arr1 = tuple.toArray();
            short[] arr2 = tuple.toArray();
            arr1[0] = 999;
            assertEquals((short) 1, arr2[0]);
        }

        @Test
        void toArray_modification() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            array[0] = (short) 99;
            assertEquals((short) 1, tuple._1);
        }

        @Test
        void toArray_preservesElements() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            short[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(5, array[0]);
            assertEquals(10, array[1]);
            assertEquals(15, array[2]);

            // Ensure it's a copy
            array[0] = 50;
            assertEquals(5, tuple._1);
        }

        @Test
        void toArray_preservesElements_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals((short) 1, array[0]);
            assertEquals((short) 2, array[1]);
            assertEquals((short) 3, array[2]);
        }

        @Test
        void toArray_preservesElements_values_1_2_3_toArray() {
            short[] array = ShortTuple.of((short) 1, (short) 2, (short) 3).toArray();
            assertArrayEquals(new short[] { 1, 2, 3 }, array);

            // Test that modifications to returned array don't affect tuple
            array[0] = 999;
            assertEquals((short) 1, ShortTuple.of((short) 1, (short) 2, (short) 3).toArray()[0]);
        }

        @Test
        void toArray_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { 1 }, array);
        }

        @Test
        void toArray_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { 1, 2 }, array);
        }

        @Test
        void toArray_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { 1, 2, 3 }, array);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            array[0] = (short) 100;
            assertEquals((short) 1, tuple._1);
        }

        @Test
        void toArrayTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            short[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArrayTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { (short) 1 }, array);
        }

        @Test
        void toArrayTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { (short) 1, (short) 2, (short) 3 }, array);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_empty() {
            ShortList list = ShortTuple.from(null).toList();
            assertEquals(0, list.size());
        }

        @Test
        void toList_preservesElements() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(5, list.get(0));
            assertEquals(10, list.get(1));
            assertEquals(15, list.get(2));
        }

        @Test
        void toList_preservesElements_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((short) 1, list.get(0));
        }

        @Test
        void toList_preservesElements_values_1_2_3_toList() {
            ShortList list = ShortTuple.of((short) 1, (short) 2, (short) 3).toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals((short) 1, list.get(0));
            assertEquals((short) 2, list.get(1));
            assertEquals((short) 3, list.get(2));
        }

        @Test
        void toList_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals((short) 1, list.get(0));
        }

        @Test
        void toList_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            ShortList list = tuple.toList();
            assertEquals(2, list.size());
            assertEquals((short) 1, list.get(0));
            assertEquals((short) 2, list.get(1));
        }

        @Test
        void toList_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((short) 3, list.get(2));
        }

        @Test
        void toListTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            ShortList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((short) 1, list.get(0));
            assertEquals((short) 2, list.get(1));
            assertEquals((short) 3, list.get(2));
        }

        @Test
        void toListTuple4Through9() {
            ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals((short) 4, list4.get(3));

            ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals((short) 9, list9.get(8));
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity2_forEach_values_1_2() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            List<Short> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals((short) 1, values.get(0).shortValue());
            assertEquals((short) 2, values.get(1).shortValue());
        }

        @Test
        void arity3_forEach() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity3_forEach_values_1_2_3() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        void arity4_forEach() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_empty() {
            AtomicInteger sum = new AtomicInteger(0);
            ShortTuple.from(null).forEach(v -> sum.addAndGet(v));
            assertEquals(0, sum.get());
        }

        @Test
        void forEach_multipleElements() throws Exception {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            List<Short> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void forEach_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(1, sum.get());
        }

        @Test
        void forEach_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(3, sum.get());
        }

        @Test
        void forEach_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(6, sum.get());
        }

        @Test
        void forEach_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(10, sum.get());
        }

        @Test
        void forEach_tuple5() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(5, count.get());
        }

        @Test
        void forEach_tuple6() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(6, count.get());
        }

        @Test
        void forEach_tuple7() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(7, count.get());
        }

        @Test
        void forEach_tuple8() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(8, count.get());
        }

        @Test
        void forEach_tuple9() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(9, count.get());
        }

        @Test
        void forEach_visitsElements() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            int[] sum = { 0 };
            tuple.forEach(s -> sum[0] += s);
            assertEquals(30, sum[0]);

            // Test Tuple2 forEach
            ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 5, (short) 10);
            int[] count = { 0 };
            tuple2.forEach(s -> count[0]++);
            assertEquals(2, count[0]);
        }

        @Test
        void forEach_visitsElements_values_1_2_3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(v -> sum.addAndGet(v));
            assertEquals(6, sum.get());
        }

        @Test
        void forEachTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Short.valueOf((short) 1), result.get(0));
        }

        @Test
        void forEachTuple2Override() {
            ShortTuple2 tuple = ShortTuple.of((short) 10, (short) 20);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Short.valueOf((short) 10), result.get(0));
            assertEquals(Short.valueOf((short) 20), result.get(1));
        }

        @Test
        void forEachTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 1), result.get(0));
            assertEquals(Short.valueOf((short) 2), result.get(1));
            assertEquals(Short.valueOf((short) 3), result.get(2));
        }

        @Test
        void forEachTuple3Override() {
            ShortTuple3 tuple = ShortTuple.of((short) 10, (short) 20, (short) 30);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 30), result.get(2));
        }

        @Test
        void forEachTuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Short.valueOf((short) 4), result.get(3));
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_empty() {
            int sum = ShortTuple.from(null).stream().sum();
            assertEquals(0, sum);
        }

        @Test
        void stream_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            int sum = tuple.stream().sum();
            assertEquals(1, sum);
        }

        @Test
        void stream_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            int sum = tuple.stream().sum();
            assertEquals(3, sum);
        }

        @Test
        void stream_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int sum = tuple.stream().sum();
            assertEquals(6, sum);
        }

        @Test
        void stream_visitsElements() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            short[] array = tuple.stream().toArray();
            assertEquals(3, array.length);
            assertEquals(5, array[0]);
            assertEquals(10, array[1]);
            assertEquals(15, array[2]);

            long count = tuple.stream().filter(s -> s > 7).count();
            assertEquals(2, count);
        }

        @Test
        void stream_visitsElements_values_1_2_3_stream_sum() {
            int sum = ShortTuple.of((short) 1, (short) 2, (short) 3).stream().sum();
            assertEquals(6, sum);
        }

        @Test
        void streamTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            ShortStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortStream stream = tuple.stream();
            assertEquals(1, stream.sum());
        }

        @Test
        void streamTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortStream stream = tuple.stream();
            assertEquals(6, stream.sum());
        }

        @Test
        void streamTuple4Through9() {
            ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(10, tuple4.stream().sum());

            ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(45, tuple9.stream().sum());
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b) -> result.set(a * b));
            assertEquals(15, result.get());
        }

        @Test
        void accept_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b, c) -> result.set(a + b + c));
            assertEquals(10, result.get());
        }

        @Test
        void accept2() {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            AtomicInteger sum = new AtomicInteger(0);
            pair.accept((a, b) -> sum.set(a + b));
            assertEquals(7, sum.get());
        }

        @Test
        void accept2_exception() throws Exception {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            assertThrows(IllegalArgumentException.class, () -> pair.accept((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        void accept3() {
            ShortTuple3 triple = ShortTuple.of((short) 3, (short) 4, (short) 5);
            AtomicInteger sum = new AtomicInteger(0);
            triple.accept((a, b, c) -> sum.set(a + b + c));
            assertEquals(12, sum.get());
        }

        @Test
        void acceptConsumer() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals((short) 1, result.get(0));
            assertEquals((short) 2, result.get(1));
            assertEquals((short) 3, result.get(2));
        }

        @Test
        void arity2_accept() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            final int[] result = new int[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7, result[0]);
        }

        @Test
        void arity2_accept_biConsumer() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            final int[] result = new int[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6, result[0]);
        }

        @Test
        void arity3_accept_triConsumer() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2Accept() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            List<Short> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Short.valueOf((short) 3), result.get(0));
            assertEquals(Short.valueOf((short) 4), result.get(1));
        }

        @Test
        void tuple3Accept() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 1), result.get(0));
            assertEquals(Short.valueOf((short) 2), result.get(1));
            assertEquals(Short.valueOf((short) 3), result.get(2));
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            Integer result = tuple.map((a, b) -> a * b);
            assertEquals(12, result.intValue());
        }

        @Test
        void arity2_map_biFunction() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            Integer result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result.intValue());
        }

        @Test
        void arity3_map_triFunction() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void map_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(15, result);
        }

        @Test
        void map_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(30, result);
        }

        @Test
        void map2() {
            ShortTuple2 pair = ShortTuple.of((short) 10, (short) 3);
            int remainder = pair.map((a, b) -> a % b);
            assertEquals(1, remainder);
        }

        @Test
        void map2_exception() {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            assertThrows(IllegalArgumentException.class, () -> pair.map((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        void map3() {
            ShortTuple3 triple = ShortTuple.of((short) 2, (short) 3, (short) 4);
            int volume = triple.map((l, w, h) -> l * w * h);
            assertEquals(24, volume);
        }

        @Test
        void mapFunction() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6, result);
        }

        @Test
        void tuple2Map() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(12, result);
        }

        @Test
        void tuple3Map() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity2_filter_notSatisfied() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 1);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        void arity2_filter_satisfied() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void arity3_filter() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filter_tuple2_match() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filter_tuple2_noMatch() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple3_match() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        void filter_tuple3_noMatch() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter2_exception() {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            assertThrows(IllegalArgumentException.class, () -> pair.filter((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        void filter2_false() {
            ShortTuple2 pair = ShortTuple.of((short) 20, (short) 10);
            Optional<ShortTuple2> result = pair.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter2_true() {
            ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
            Optional<ShortTuple2> result = pair.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(pair, result.get());
        }

        @Test
        void filter3_false() {
            ShortTuple3 triple = ShortTuple.of((short) 3, (short) 2, (short) 1);
            Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        @Test
        void filter3_true() {
            ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(triple, result.get());
        }

        @Test
        void filterPredicate() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter(t -> t._1 == (short) 1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter(t -> t._1 == (short) 99);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterFalse() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            var result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            var result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3FilterFalse() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple.ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity1_hashCode_values_5() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple1 tuple2 = ShortTuple.of((short) 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            ShortTuple.ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode_values_1_2() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            ShortTuple.ShortTuple4 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple.ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            ShortTuple.ShortTuple5 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple.ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            ShortTuple.ShortTuple6 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple.ShortTuple6 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            ShortTuple.ShortTuple7 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple.ShortTuple7 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            ShortTuple.ShortTuple8 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple.ShortTuple8 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            ShortTuple.ShortTuple9 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple.ShortTuple9 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_consistency() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_matchesValueContract() {
            ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 5, (short) 10, (short) 15);
            ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 5, (short) 10, (short) 15);
            ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 16);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

            // Test specific hashcodes for coverage
            ShortTuple.ShortTuple1 single = ShortTuple.of((short) 5);
            assertEquals(5, single.hashCode());

            ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 5, (short) 10);
            assertEquals(31 * 5 + 10, pair.hashCode());
        }

        @Test
        void hashCode_tuple1_different() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 2);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_tuple3() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @ParameterizedTest(name = "arity {0}: equality checks every component")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void equals_rejectsEverySingleComponentDifference(int arity) {
            short[] values = new short[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = (short) (i + 1);
            }
            ShortTuple<?> tuple = ShortTuple.from(values);
            // A separately constructed empty value verifies equality independently of the factory singleton.
            ShortTuple<?> equal = arity == 0 ? new ShortTuple.ShortTuple0() : ShortTuple.from(values.clone());
            assertNotSame(tuple, equal);
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(equal));
            assertTrue(equal.equals(tuple));
            assertEquals(tuple.hashCode(), equal.hashCode());
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals(new Object()));

            for (int i = 0; i < arity; i++) {
                short[] changed = values.clone();
                changed[i] = (short) 100;
                ShortTuple<?> different = ShortTuple.from(changed);
                assertFalse(tuple.equals(different), "component " + i);
                assertFalse(different.equals(tuple), "component " + i);
            }
        }

        @Test
        void arity1_equals() {
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple.ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_differentType() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertNotEquals("string", tuple);
        }

        @Test
        void arity1_equals_equal() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple1 tuple2 = ShortTuple.of((short) 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_notEqual() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple1 tuple2 = ShortTuple.of((short) 10);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void arity1_equals_null() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertNotEquals(null, tuple);
        }

        @Test
        void arity1_equals_same() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void arity2_equals() {
            ShortTuple.ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity2_equals_values_1_2() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity3_equals() {
            ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity4_equals() {
            ShortTuple.ShortTuple4 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple.ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity5_equals() {
            ShortTuple.ShortTuple5 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple.ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity6_equals() {
            ShortTuple.ShortTuple6 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple.ShortTuple6 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity7_equals() {
            ShortTuple.ShortTuple7 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple.ShortTuple7 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity8_equals() {
            ShortTuple.ShortTuple8 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple.ShortTuple8 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equals() {
            ShortTuple.ShortTuple9 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple.ShortTuple9 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void arity9_equalsAndHashCode_differInOneField() {
            short[] base = { 10, 20, 30, 40, 50, 60, 70, 80, 90 };
            ShortTuple.ShortTuple9 t = ShortTuple.of(base[0], base[1], base[2], base[3], base[4], base[5], base[6], base[7], base[8]);
            for (int i = 0; i < 9; i++) {
                short[] diff = base.clone();
                diff[i] = (short) (diff[i] + 1);
                ShortTuple.ShortTuple9 other = ShortTuple.of(diff[0], diff[1], diff[2], diff[3], diff[4], diff[5], diff[6], diff[7], diff[8]);
                assertNotEquals(t, other, "field _" + (i + 1) + " should affect equals");
                assertNotEquals(t.hashCode(), other.hashCode(), "field _" + (i + 1) + " should affect hashCode");
            }
        }

        @Test
        void equals_comparesValues() {
            ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 5, (short) 10, (short) 15);
            ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 5, (short) 10, (short) 15);
            ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 16);
            ShortTuple.ShortTuple2 tuple4 = ShortTuple.of((short) 5, (short) 10);

            assertTrue(tuple1.equals(tuple1));
            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(tuple4));
            assertFalse(tuple1.equals(null));
            assertFalse(tuple1.equals("not a tuple"));

            // Test specific equals for coverage
            ShortTuple.ShortTuple1 single1 = ShortTuple.of((short) 5);
            ShortTuple.ShortTuple1 single2 = ShortTuple.of((short) 5);
            ShortTuple.ShortTuple1 single3 = ShortTuple.of((short) 10);
            assertTrue(single1.equals(single2));
            assertFalse(single1.equals(single3));
            assertFalse(single1.equals("not a tuple"));

            ShortTuple.ShortTuple2 pair1 = ShortTuple.of((short) 5, (short) 10);
            ShortTuple.ShortTuple2 pair2 = ShortTuple.of((short) 5, (short) 10);
            ShortTuple.ShortTuple2 pair3 = ShortTuple.of((short) 5, (short) 11);
            assertTrue(pair1.equals(pair2));
            assertFalse(pair1.equals(pair3));
        }

        @Test
        void equals_comparesValues_values_1_2_3() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 4);

            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertTrue(tuple1.equals(tuple1));
            assertNotEquals(tuple1, null);
        }

        @Test
        void equals_differentClass() {
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        void equals_empty() {
            ShortTuple<?> empty1 = ShortTuple.from(null);
            ShortTuple<?> empty2 = ShortTuple.from(new short[0]);
            assertEquals(empty1, empty2);
        }

        @Test
        void equals_higherArityIdentityAndTypeMismatch() {
            ShortTuple.ShortTuple7 tuple7 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35);
            ShortTuple.ShortTuple8 tuple8 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40);
            ShortTuple.ShortTuple9 tuple9 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40,
                    (short) 45);

            assertTrue(tuple7.equals(tuple7));
            assertEquals(tuple7, ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35));
            assertNotEquals(tuple7, ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 36));
            assertNotEquals(tuple7, "tuple");

            assertTrue(tuple8.equals(tuple8));
            assertEquals(tuple8, ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40));
            assertNotEquals(tuple8, ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 41));
            assertNotEquals(tuple8, "tuple");

            assertTrue(tuple9.equals(tuple9));
            assertEquals(tuple9, ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40, (short) 45));
            assertNotEquals(tuple9, ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40, (short) 46));
            assertNotEquals(tuple9, "tuple");
        }

        @Test
        void equals_symmetric() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        void equals_tuple1_different() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 2);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple1_null() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotEquals(null, tuple);
        }

        @Test
        void equals_tuple1_same() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple1_sameObject() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equals_tuple2_different() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 3);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple3_different() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 4);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        void equals_tuple3_same() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        void equalsDifferentClass() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        void equalsNull() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotEquals(null, tuple);
        }

        @Test
        void equalsSameObject() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        void equalsTuple1() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            ShortTuple1 tuple3 = ShortTuple.of((short) 99);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple2() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple3 = ShortTuple.of((short) 1, (short) 3);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        void equalsTuple3() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 4);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity0_toString() {
            ShortTuple0 tuple = ShortTuple.from(new short[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void arity1_toString() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity1_toString_values_5() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals("(5)", tuple.toString());
        }

        @Test
        void arity2_toString() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString_values_1_2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals("(1, 2)", tuple.toString());
        }

        @Test
        void arity3_toString() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_formatsValue() {
            ShortTuple.ShortTuple0 empty = ShortTuple.from(new short[0]);
            assertEquals("()", empty.toString());

            ShortTuple.ShortTuple1 single = ShortTuple.of((short) 5);
            assertEquals("(5)", single.toString());

            ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 5, (short) 10);
            assertEquals("(5, 10)", pair.toString());

            ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 5, (short) 10, (short) 15);
            assertEquals("(5, 10, 15)", triple.toString());
        }

        @Test
        void toString_formatsValue_values_42_toString() {
            assertEquals("(42)", ShortTuple.of((short) 42).toString());
            assertEquals("(10, 20)", ShortTuple.of((short) 10, (short) 20).toString());
            assertEquals("(1, 2, 3)", ShortTuple.of((short) 1, (short) 2, (short) 3).toString());
            assertEquals("()", ShortTuple.from(null).toString());
        }

        @Test
        void toString_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals("(1)", tuple.toString());
        }

        @Test
        void toString_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals("(1, 2, 3)", tuple.toString());
        }

        @Test
        void toStringTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.from(new short[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
        }

        @Test
        void toStringTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        @Test
        void tuple0_toString() {
            ShortTuple<?> tuple = ShortTuple.from(null);
            assertEquals("()", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void allTupleSizes() {
            ShortTuple0 t0 = ShortTuple.from(null);
            ShortTuple1 t1 = ShortTuple.of((short) 1);
            ShortTuple2 t2 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple3 t3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple4 t4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple5 t5 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple6 t6 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple7 t7 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple8 t8 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple9 t9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

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
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 4, tuple.max());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void classLevelExamples() {
            // short min = triple.min();         // 1
            // short max = triple.max();         // 3
            // triple.average();    // OptionalDouble.of(2.0)
            ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals((short) 1, triple.min());
            assertEquals((short) 3, triple.max());
            assertEquals(2.0, triple.average().getAsDouble(), 0.001);
        }

        @Test
        void coverageRegression_defaultConstructorsAndCachedArrays() {
            ShortTuple.ShortTuple1 tuple1 = new ShortTuple.ShortTuple1();
            assertArrayEquals(new short[] { 0 }, tuple1.toArray());
            assertArrayEquals(new short[] { 0 }, tuple1.toArray());

            ShortTuple.ShortTuple2 tuple2 = new ShortTuple.ShortTuple2();
            assertArrayEquals(new short[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new short[] { 0, 0 }, tuple2.toArray());

            ShortTuple.ShortTuple3 tuple3 = new ShortTuple.ShortTuple3();
            assertArrayEquals(new short[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new short[] { 0, 0, 0 }, tuple3.toArray());

            ShortTuple.ShortTuple4 tuple4 = new ShortTuple.ShortTuple4();
            assertArrayEquals(new short[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0 }, tuple4.toArray());

            ShortTuple.ShortTuple5 tuple5 = new ShortTuple.ShortTuple5();
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0 }, tuple5.toArray());

            ShortTuple.ShortTuple6 tuple6 = new ShortTuple.ShortTuple6();
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());

            ShortTuple.ShortTuple7 tuple7 = new ShortTuple.ShortTuple7();
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());

            ShortTuple.ShortTuple8 tuple8 = new ShortTuple.ShortTuple8();
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());

            ShortTuple.ShortTuple9 tuple9 = new ShortTuple.ShortTuple9();
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
        }

        @Test
        void coverageRegression_emptyTupleBaseMethods() {
            ShortTuple<?> empty = ShortTuple.from((short[]) null);
            assertEquals(ShortTuple.from(new short[0]), empty);
            assertFalse(empty.equals("short"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void immutability() {
            short[] arr = new short[] { 1, 2, 3 };
            ShortTuple3 tuple = ShortTuple.from(arr);
            arr[0] = 999;
            assertEquals((short) 1, tuple._1);
        }

        @Test
        void inheritedAggregateEmptyBehavior() {
            final ShortTuple.ShortTuple0 tuple = ShortTuple.from(new short[0]);

            assertThrows(NoSuchElementException.class, tuple::min);
            assertThrows(NoSuchElementException.class, tuple::max);
            assertThrows(NoSuchElementException.class, tuple::median);
            assertTrue(tuple.average().isEmpty());
            assertEquals(0, tuple.sum());
        }

        @Test
        void inheritedOuterMethodsForCoverage() throws Exception {
            final ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 9, (short) 2, (short) 5, (short) 1);
            final short[] firstArray = tuple.toArray();
            final short[] secondArray = tuple.toArray();
            final List<Short> visited = new ArrayList<>();

            tuple.forEach(visited::add);

            assertEquals(1, tuple.min());
            assertEquals(9, tuple.max());
            assertEquals(2, tuple.lowerMedian());
            assertEquals(17, tuple.sum());
            assertEquals(4.25, tuple.average().getAsDouble());
            assertArrayEquals(new short[] { 9, 2, 5, 1 }, firstArray);
            assertArrayEquals(firstArray, secondArray);
            assertNotSame(firstArray, secondArray);
            assertEquals(ShortList.of((short) 9, (short) 2, (short) 5, (short) 1), tuple.toList());
            assertEquals(visited, tuple.stream().boxed().toList());
            assertEquals(tuple.hashCode(), ShortTuple.of((short) 9, (short) 2, (short) 5, (short) 1).hashCode());
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(ShortTuple.of((short) 9, (short) 2, (short) 5, (short) 1)));
            assertFalse(tuple.equals(ShortTuple.of((short) 9, (short) 2, (short) 5, (short) 2)));
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals("not a tuple"));
            assertEquals("(9, 2, 5, 1)", tuple.toString());
            assertArrayEquals(new short[] { 1, 5, 2, 9 }, tuple.reversed().toArray());
        }

        @Test
        void largeValues() {
            ShortTuple2 tuple = ShortTuple.of(Short.MAX_VALUE, (short) (Short.MAX_VALUE / 2));
            assertEquals((short) (Short.MAX_VALUE / 2), tuple.min());
            assertEquals(Short.MAX_VALUE, tuple.max());
        }

        @Test
        void mixedValues() {
            ShortTuple3 tuple = ShortTuple.of((short) -100, (short) 0, (short) 100);
            assertEquals((short) -100, tuple.min());
            assertEquals((short) 100, tuple.max());
            assertEquals(0, tuple.sum());
        }

        @Test
        void negativeValues() {
            ShortTuple3 tuple = ShortTuple.of((short) -1, (short) -2, (short) -3);
            assertEquals((short) -3, tuple.min());
            assertEquals((short) -1, tuple.max());
            assertEquals(-6, tuple.sum());
        }

        @Test
        void reverse_largerTuples() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple4 reversed = tuple.reversed();
            assertEquals((short) 4, reversed._1);
            assertEquals((short) 3, reversed._2);
            assertEquals((short) 2, reversed._3);
            assertEquals((short) 1, reversed._4);
        }

        @Test
        void tuple1SpecificMethods() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 5);

            // Test min/max/median/sum/average
            assertEquals(5, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(5, tuple.lowerMedian());
            assertEquals(5, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble());
        }

        @Test
        void tuple2SpecificMethods() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 10);

            // Test accept
            int[] sum = { 0 };
            tuple.accept((a, b) -> sum[0] = a + b);
            assertEquals(15, sum[0]);

            // Test map
            int result = tuple.map((a, b) -> a + b);
            assertEquals(15, result);

            // Test filter
            assertTrue(tuple.filter((a, b) -> a < b).isPresent());
            assertFalse(tuple.filter((a, b) -> a > b).isPresent());
        }

        @Test
        void tuple3SpecificMethods() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);

            // Test accept
            int[] sum = { 0 };
            tuple.accept((a, b, c) -> sum[0] = a + b + c);
            assertEquals(30, sum[0]);

            // Test map
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(30, result);

            // Test filter
            assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
        }

        @Test
        void tuple4Operations() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);

            // Test reverse
            ShortTuple4 reversed = tuple.reversed();
            assertEquals((short) 4, reversed._1);
            assertEquals((short) 3, reversed._2);
            assertEquals((short) 2, reversed._3);
            assertEquals((short) 1, reversed._4);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 4));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4 }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 4, tuple.max());
            assertEquals((short) 2, tuple.lowerMedian());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple4 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 5);
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
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(5, tuple.arity());
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 5, tuple.max());
            assertEquals((short) 3, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);
            assertTrue(tuple.contains((short) 3));
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        void tuple5Operations() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);

            // Test reverse
            ShortTuple5 reversed = tuple.reversed();
            assertEquals((short) 5, reversed._1);
            assertEquals((short) 1, reversed._5);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 5));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 5, tuple.max());
            assertEquals((short) 3, tuple.lowerMedian());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average().getAsDouble(), 0.001);

            // Test equals
            ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(tuple, tuple2);
        }

        @Test
        void tuple6_operations() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(6, tuple.arity());
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 6, tuple.max());
            assertEquals(21, tuple.sum());
        }

        @Test
        void tuple6Operations() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);

            // Test reverse
            ShortTuple6 reversed = tuple.reversed();
            assertEquals((short) 6, reversed._1);
            assertEquals((short) 1, reversed._6);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 6));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 6, tuple.max());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple7_operations() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(7, tuple.arity());
            assertEquals((short) 4, tuple.lowerMedian());
            assertEquals(28, tuple.sum());
        }

        @Test
        void tuple7Operations() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);

            // Test reverse
            ShortTuple7 reversed = tuple.reversed();
            assertEquals((short) 7, reversed._1);
            assertEquals((short) 1, reversed._7);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 7));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 7, tuple.max());
            assertEquals(28, tuple.sum());
            assertEquals(4.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple8_operations() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(8, tuple.arity());
            assertEquals((short) 8, tuple.max());
            assertEquals(36, tuple.sum());
        }

        @Test
        void tuple8Operations() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);

            // Test reverse
            ShortTuple8 reversed = tuple.reversed();
            assertEquals((short) 8, reversed._1);
            assertEquals((short) 1, reversed._8);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 8));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 8, tuple.max());
            assertEquals(36, tuple.sum());
            assertEquals(4.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9_operations() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(9, tuple.arity());
            assertEquals((short) 5, tuple.lowerMedian());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9Operations() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

            // Test reverse
            ShortTuple9 reversed = tuple.reversed();
            assertEquals((short) 9, reversed._1);
            assertEquals((short) 1, reversed._9);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 9));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 9, tuple.max());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void withNegatives() {
            ShortTuple3 tuple = ShortTuple.of((short) -5, (short) -10, (short) -1);
            assertEquals((short) -10, tuple.min());
            assertEquals((short) -1, tuple.max());
            assertEquals(-16, tuple.sum());
        }

        @Test
        void withSingleElement() {
            ShortTuple1 tuple = ShortTuple.of((short) 42);
            assertEquals((short) 42, tuple.min());
            assertEquals((short) 42, tuple.max());
            assertEquals((short) 42, tuple.lowerMedian());
            assertEquals(42, tuple.sum());
            assertEquals(42.0, tuple.average().getAsDouble(), 0.0001);
            assertTrue(tuple.contains((short) 42));
        }

        @Test
        void withZero() {
            ShortTuple3 tuple = ShortTuple.of((short) 0, (short) 1, (short) 2);
            assertEquals((short) 0, tuple.min());
            assertEquals((short) 2, tuple.max());
            assertEquals(3, tuple.sum());
        }

        @Test
        void zeroArgConstructorsForCoverage() {
            final ShortTuple1 tuple1 = new ShortTuple1();
            final ShortTuple2 tuple2 = new ShortTuple2();
            final ShortTuple3 tuple3 = new ShortTuple3();
            final ShortTuple4 tuple4 = new ShortTuple4();
            final ShortTuple5 tuple5 = new ShortTuple5();
            final ShortTuple6 tuple6 = new ShortTuple6();
            final ShortTuple7 tuple7 = new ShortTuple7();
            final ShortTuple8 tuple8 = new ShortTuple8();
            final ShortTuple9 tuple9 = new ShortTuple9();

            assertArrayEquals(new short[] { 0 }, tuple1.toArray());
            assertArrayEquals(new short[] { 0 }, tuple1.toArray());
            assertArrayEquals(new short[] { 0, 0 }, tuple2.toArray());
            assertArrayEquals(new short[] { 0, 0, 0 }, tuple3.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0 }, tuple4.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0 }, tuple5.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0 }, tuple6.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0 }, tuple7.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0, 0 }, tuple8.toArray());
            assertArrayEquals(new short[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, tuple9.toArray());
        }

        @Test
        void zeroValues() {
            ShortTuple3 tuple = ShortTuple.of((short) 0, (short) 0, (short) 0);
            assertEquals((short) 0, tuple.min());
            assertEquals((short) 0, tuple.max());
            assertEquals(0, tuple.sum());
        }
    }
}
