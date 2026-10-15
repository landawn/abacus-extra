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

import com.landawn.abacus.util.CharTuple.CharTuple0;
import com.landawn.abacus.util.CharTuple.CharTuple1;
import com.landawn.abacus.util.CharTuple.CharTuple2;
import com.landawn.abacus.util.CharTuple.CharTuple3;
import com.landawn.abacus.util.CharTuple.CharTuple4;
import com.landawn.abacus.util.CharTuple.CharTuple5;
import com.landawn.abacus.util.CharTuple.CharTuple6;
import com.landawn.abacus.util.CharTuple.CharTuple7;
import com.landawn.abacus.util.CharTuple.CharTuple8;
import com.landawn.abacus.util.CharTuple.CharTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.CharStream;

class CharTupleTest {

    @Nested
    class OfTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("factoryInputs")
        void of_allArities_preservesFieldsAndOrder(String scenario, char[] values) {
            CharTuple<?> tuple = createWithOf(values);

            assertEquals(values.length, tuple.arity(), scenario);
            assertArrayEquals(values, publicFields(tuple), scenario);
            assertArrayEquals(values, tuple.toArray(), scenario);
        }

        @Test
        void of5Median() {
            // CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            // char median = tuple.lowerMedian();   // 'C'
            CharTuple.CharTuple5 t = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertEquals('C', t.lowerMedian());
        }

        @Test
        void of8Contains() {
            // CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            // boolean hasX = tuple.contains('X');   // false
            CharTuple.CharTuple8 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertFalse(t.contains('X'));
        }

        @Test
        void of9ToArray() {
            // CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            // char[] array = tuple.toArray();   // ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
            CharTuple.CharTuple9 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' }, t.toArray());
        }

        private static CharTuple<?> createWithOf(char[] values) {
            return switch (values.length) {
                case 1 -> CharTuple.of(values[0]);
                case 2 -> CharTuple.of(values[0], values[1]);
                case 3 -> CharTuple.of(values[0], values[1], values[2]);
                case 4 -> CharTuple.of(values[0], values[1], values[2], values[3]);
                case 5 -> CharTuple.of(values[0], values[1], values[2], values[3], values[4]);
                case 6 -> CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
                case 7 -> CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                case 8 -> CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                case 9 -> CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
            };
        }

        private static java.util.stream.Stream<Arguments> factoryInputs() {
            return java.util.stream.Stream.of(Arguments.of("1: 'a'", new char[] { 'a' }), Arguments.of("1: 'A'", new char[] { 'A' }),
                    Arguments.of("2: 'a', 'b'", new char[] { 'a', 'b' }), Arguments.of("2: 'A', 'B'", new char[] { 'A', 'B' }),
                    Arguments.of("3: 'a', 'b', 'c'", new char[] { 'a', 'b', 'c' }), Arguments.of("3: 'A', 'B', 'C'", new char[] { 'A', 'B', 'C' }),
                    Arguments.of("4: 'a', 'b', 'c', 'd'", new char[] { 'a', 'b', 'c', 'd' }),
                    Arguments.of("4: 'A', 'B', 'C', 'D'", new char[] { 'A', 'B', 'C', 'D' }),
                    Arguments.of("5: '1', '2', '3', '4', '5'", new char[] { '1', '2', '3', '4', '5' }),
                    Arguments.of("5: 'a', 'b', 'c', 'd', 'e'", new char[] { 'a', 'b', 'c', 'd', 'e' }),
                    Arguments.of("5: 'A', 'B', 'C', 'D', 'E'", new char[] { 'A', 'B', 'C', 'D', 'E' }),
                    Arguments.of("6: 'a', 'b', 'c', 'd', 'e', 'f'", new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }),
                    Arguments.of("6: 'A', 'B', 'C', 'D', 'E', 'F'", new char[] { 'A', 'B', 'C', 'D', 'E', 'F' }),
                    Arguments.of("7: 'a', 'b', 'c', 'd', 'e', 'f', 'g'", new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' }),
                    Arguments.of("7: 'A', 'B', 'C', 'D', 'E', 'F', 'G'", new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' }),
                    Arguments.of("8: 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'", new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' }),
                    Arguments.of("8: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'", new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' }),
                    Arguments.of("9: 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'", new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' }),
                    Arguments.of("9: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'", new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' }));
        }

        private static char[] publicFields(CharTuple<?> tuple) {
            return switch (tuple.arity()) {
                case 1 -> {
                    CharTuple.CharTuple1 value = (CharTuple.CharTuple1) tuple;
                    yield new char[] { value._1 };
                }
                case 2 -> {
                    CharTuple.CharTuple2 value = (CharTuple.CharTuple2) tuple;
                    yield new char[] { value._1, value._2 };
                }
                case 3 -> {
                    CharTuple.CharTuple3 value = (CharTuple.CharTuple3) tuple;
                    yield new char[] { value._1, value._2, value._3 };
                }
                case 4 -> {
                    CharTuple.CharTuple4 value = (CharTuple.CharTuple4) tuple;
                    yield new char[] { value._1, value._2, value._3, value._4 };
                }
                case 5 -> {
                    CharTuple.CharTuple5 value = (CharTuple.CharTuple5) tuple;
                    yield new char[] { value._1, value._2, value._3, value._4, value._5 };
                }
                case 6 -> {
                    CharTuple.CharTuple6 value = (CharTuple.CharTuple6) tuple;
                    yield new char[] { value._1, value._2, value._3, value._4, value._5, value._6 };
                }
                case 7 -> {
                    CharTuple.CharTuple7 value = (CharTuple.CharTuple7) tuple;
                    yield new char[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7 };
                }
                case 8 -> {
                    CharTuple.CharTuple8 value = (CharTuple.CharTuple8) tuple;
                    yield new char[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8 };
                }
                case 9 -> {
                    CharTuple.CharTuple9 value = (CharTuple.CharTuple9) tuple;
                    yield new char[] { value._1, value._2, value._3, value._4, value._5, value._6, value._7, value._8, value._9 };
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
                char[] array = new char[i];
                CharTuple<?> tuple = CharTuple.from(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        void create_arraySize1() {
            CharTuple1 tuple = CharTuple.from(new char[] { 'A' });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals('A', tuple._1);
        }

        @Test
        void create_arraySize2() {
            CharTuple2 tuple = CharTuple.from(new char[] { 'A', 'B' });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals('A', tuple._1);
            assertEquals('B', tuple._2);
        }

        @Test
        void create_arraySize9() {
            CharTuple9 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals('A', tuple._1);
            assertEquals('I', tuple._9);
        }

        @Test
        void create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                CharTuple.from(new char[10]);
            });
        }

        @Test
        void create_nullArray() {
            CharTuple0 tuple = CharTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_array3() {
            CharTuple3 tuple = CharTuple.from(new char[] { 'A', 'B', 'C' });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
        }

        @Test
        void from_array4() {
            CharTuple4 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D' });
            assertEquals(4, tuple.arity());
        }

        @Test
        void from_array5() {
            CharTuple5 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E' });
            assertEquals(5, tuple.arity());
        }

        @Test
        void from_array6() {
            CharTuple6 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F' });
            assertEquals(6, tuple.arity());
        }

        @Test
        void from_array7() {
            CharTuple7 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' });
            assertEquals(7, tuple.arity());
        }

        @Test
        void from_array8() {
            CharTuple8 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' });
            assertEquals(8, tuple.arity());
        }

        @Test
        void from_array9() {
            CharTuple9 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_empty() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_emptyArray() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_nine() {
            CharTuple9 tuple = CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('I', tuple._9);
            assertEquals(9, tuple.arity());
        }

        @Test
        void from_nullArray() {
            CharTuple<?> tuple = CharTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void from_single() {
            CharTuple1 tuple = CharTuple.from(new char[] { 'X' });
            assertNotNull(tuple);
            assertEquals('X', tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from_tooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                CharTuple.from(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' });
            });
        }

        @Test
        void from_two() {
            CharTuple2 tuple = CharTuple.from(new char[] { 'X', 'Y' });
            assertNotNull(tuple);
            assertEquals('X', tuple._1);
            assertEquals('Y', tuple._2);
        }

        @Test
        void from1() {
            CharTuple1 tuple = CharTuple.from(new char[] { 'a' });
            assertEquals('a', tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        void from2() {
            CharTuple2 tuple = CharTuple.from(new char[] { 'a', 'b' });
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
        }

        @Test
        void from2Through8() {
            CharTuple2 tuple2 = CharTuple.from(new char[] { 'a', 'b' });
            assertEquals('a', tuple2._1);
            assertEquals('b', tuple2._2);

            CharTuple4 tuple4 = CharTuple.from(new char[] { 'a', 'b', 'c', 'd' });
            assertEquals('a', tuple4._1);
            assertEquals('d', tuple4._4);

            CharTuple5 tuple5 = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e' });
            assertEquals('a', tuple5._1);
            assertEquals('e', tuple5._5);

            CharTuple6 tuple6 = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' });
            assertEquals('a', tuple6._1);
            assertEquals('f', tuple6._6);

            CharTuple7 tuple7 = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' });
            assertEquals('a', tuple7._1);
            assertEquals('g', tuple7._7);

            CharTuple8 tuple8 = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
            assertEquals('a', tuple8._1);
            assertEquals('h', tuple8._8);
        }

        @Test
        void from3() {
            CharTuple3 tuple = CharTuple.from(new char[] { 'a', 'b', 'c' });
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
            assertEquals('c', tuple._3);
        }

        @Test
        void from4() {
            CharTuple4 tuple = CharTuple.from(new char[] { 'a', 'b', 'c', 'd' });
            assertEquals('a', tuple._1);
            assertEquals('d', tuple._4);
        }

        @Test
        void from5() {
            CharTuple5 tuple = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e' });
            assertEquals('a', tuple._1);
            assertEquals('e', tuple._5);
        }

        @Test
        void from6() {
            CharTuple6 tuple = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' });
            assertEquals('a', tuple._1);
            assertEquals('f', tuple._6);
        }

        @Test
        void from7() {
            CharTuple7 tuple = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' });
            assertEquals('a', tuple._1);
            assertEquals('g', tuple._7);
        }

        @Test
        void from8() {
            CharTuple8 tuple = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
            assertEquals('a', tuple._1);
            assertEquals('h', tuple._8);
        }

        @Test
        void from9() {
            CharTuple9 tuple = CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' });
            assertEquals('a', tuple._1);
            assertEquals('i', tuple._9);
        }

        @Test
        void fromEmpty() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromNull() {
            CharTuple<CharTuple0> tuple = CharTuple.from(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        void fromTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                CharTuple.from(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' });
            });
        }
    }

    @Nested
    class ArityTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("arityCases")
        void arity_inputCases_returnsElementCount(String scenario, CharTuple<?> tuple, int expected) {
            assertEquals(expected, tuple.arity(), scenario);
        }

        private static java.util.stream.Stream<Arguments> arityCases() {
            return java.util.stream.Stream.of(Arguments.of("from(new char[0])", CharTuple.from(new char[0]), 0), Arguments.of("of('a')", CharTuple.of('a'), 1),
                    Arguments.of("of('a', 'b')", CharTuple.of('a', 'b'), 2), Arguments.of("of('a', 'b', 'c')", CharTuple.of('a', 'b', 'c'), 3),
                    Arguments.of("of('a', 'b', 'c', 'd')", CharTuple.of('a', 'b', 'c', 'd'), 4),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e')", CharTuple.of('a', 'b', 'c', 'd', 'e'), 5),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'), 6),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), 7),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), 8),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'), 9));
        }

    }

    @Nested
    class MinTests {

        @Test
        void arity1_min() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.min());
        }

        @Test
        void arity2_min() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.min());
        }

        @Test
        void arity3_min() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.min());
        }

        @Test
        void min_multiple() {
            CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
            assertEquals('A', tuple.min());
        }

        @Test
        void min_returnsMinimum() {
            CharTuple.CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('a', tuple.min());
        }

        @Test
        void min_single() {
            CharTuple1 tuple = CharTuple.of('M');
            assertEquals('M', tuple.min());
        }

        @Test
        void minMaxMedian_largerTuple() {
            CharTuple5 tuple = CharTuple.of('E', 'B', 'D', 'A', 'C');
            assertEquals('A', tuple.min());
            assertEquals('E', tuple.max());
            assertEquals('C', tuple.lowerMedian());
        }

        @Test
        void minTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void minTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple.min());
        }

        @Test
        void minTuple3() {
            CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('a', tuple.min());
        }

        @Test
        void tuple0_min_throwsException() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void tuple0_min_throwsException_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        void tuple1_min() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.min());
        }

        @Test
        void tuple2_min() {
            CharTuple2 tuple = CharTuple.of('B', 'A');
            assertEquals('A', tuple.min());
        }

        @Test
        void tuple3_min() {
            CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
            assertEquals('A', tuple.min());
        }
    }

    @Nested
    class MaxTests {

        @Test
        void arity1_max() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.max());
        }

        @Test
        void arity2_max() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.max());
        }

        @Test
        void arity3_max() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.max());
        }

        @Test
        void max_multiple() {
            CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
            assertEquals('Z', tuple.max());
        }

        @Test
        void max_returnsMaximum() {
            CharTuple.CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('c', tuple.max());
        }

        @Test
        void max_single() {
            CharTuple1 tuple = CharTuple.of('M');
            assertEquals('M', tuple.max());
        }

        @Test
        void maxTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void maxTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple.max());
        }

        @Test
        void maxTuple3() {
            CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('c', tuple.max());
        }

        @Test
        void tuple0_max_throwsException() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void tuple0_max_throwsException_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        void tuple1_max() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.max());
        }

        @Test
        void tuple2_max() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals('B', tuple.max());
        }

        @Test
        void tuple3_max() {
            CharTuple3 tuple = CharTuple.of('A', 'C', 'B');
            assertEquals('C', tuple.max());
        }

        @Test
        void tuple3_max_values_C_A_B() {
            CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
            assertEquals('C', tuple.max());
        }
    }

    @Nested
    class SumTests {

        @Test
        void arity1_sum() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.sum());
        }

        @Test
        void arity2_sum() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.sum());
        }

        @Test
        void arity3_sum() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.sum());
        }

        @Test
        void sum_returnsTotal() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            int sum = tuple.sum();
            assertEquals('a' + 'b' + 'c', sum);
        }

        @Test
        void sumTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void sumTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals(97, tuple.sum());
        }

        @Test
        void sumTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals(294, tuple.sum());
        }

        @Test
        void tuple0_sum() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void tuple0_sum_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        void tuple1_sum() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals(65, tuple.sum()); // ASCII value of 'A' is 65
        }

        @Test
        void tuple1_sum_values_A() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.sum());
        }

        @Test
        void tuple2_sum() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(131, tuple.sum()); // 65 + 66
        }

        @Test
        void tuple2_sum_values_A_B() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals('A' + 'B', tuple.sum());
        }

        @Test
        void tuple3_sum() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(198, tuple.sum()); // 65 + 66 + 67
        }

        @Test
        void tuple3_sum_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals('A' + 'B' + 'C', tuple.sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void arity1_average() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity2_average() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void arity3_average() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertTrue(tuple.average().getAsDouble() >= 0 || tuple.average().getAsDouble() < 0);
        }

        @Test
        void average_multiple() {
            CharTuple2 tuple = CharTuple.of('A', 'C'); // (65 + 67) / 2 = 66.0
            assertEquals(66.0, tuple.average().getAsDouble());
        }

        @Test
        void average_returnsMean() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'c');
            double average = tuple.average().getAsDouble();
            assertEquals(('a' + 'c') / 2.0, average, 0.001);
        }

        @Test
        void average_single() {
            CharTuple1 tuple = CharTuple.of('A'); // 65
            assertEquals(65.0, tuple.average().getAsDouble());
        }

        @Test
        void averageTuple0ReturnsEmpty() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void averageTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals(97.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void averageTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals(98.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple0_average_returnsEmpty() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void tuple0_average_returnsEmpty_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertTrue(tuple.average().isEmpty());
        }

        @Test
        void tuple1_average() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals(65.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple1_average_values_A() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void tuple2_average() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(65.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple2_average_values_A_B() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(('A' + 'B') / 2.0, tuple.average().getAsDouble(), 0.0001);
        }

        @Test
        void tuple3_average() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(66.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple3_average_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(('A' + 'B' + 'C') / 3.0, tuple.average().getAsDouble(), 0.0001);
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void arity1_reversed() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            CharTuple.CharTuple1 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        void arity2_reversed() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            CharTuple.CharTuple2 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        void arity3_reversed() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharTuple.CharTuple3 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        void arity4_reversed() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple.CharTuple4 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        void arity5_reversed() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple.CharTuple5 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        void arity6_reversed() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple.CharTuple6 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        void arity7_reversed() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple.CharTuple7 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        void arity8_reversed() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple.CharTuple8 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        void arity9_reversed() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple.CharTuple9 reversed = tuple.reversed();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        void reversed_empty() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            CharTuple0 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversed_reversesElementOrder() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharTuple.CharTuple3 reversed = tuple.reversed();

            assertEquals('c', reversed._1);
            assertEquals('b', reversed._2);
            assertEquals('a', reversed._3);
        }

        @Test
        void reversed_twoElements() {
            CharTuple2 tuple = CharTuple.of('X', 'Y');
            CharTuple2 reversed = tuple.reversed();
            assertEquals('Y', reversed._1);
            assertEquals('X', reversed._2);
        }

        @Test
        void reversedTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            CharTuple<CharTuple0> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void reversedTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            CharTuple1 reversed = tuple.reversed();
            assertEquals('a', reversed._1);
        }

        @Test
        void reversedTuple2() {
            CharTuple2 tuple = CharTuple.of('a', 'b');
            CharTuple2 reversed = tuple.reversed();
            assertEquals('b', reversed._1);
            assertEquals('a', reversed._2);
        }

        @Test
        void reversedTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharTuple3 reversed = tuple.reversed();
            assertEquals('c', reversed._1);
            assertEquals('b', reversed._2);
            assertEquals('a', reversed._3);
        }

        @Test
        void reversedTuple4() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple4 reversed = tuple.reversed();
            assertEquals('d', reversed._1);
            assertEquals('c', reversed._2);
            assertEquals('b', reversed._3);
            assertEquals('a', reversed._4);
        }

        @Test
        void reversedTuple5() {
            CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple5 reversed = tuple.reversed();
            assertEquals('e', reversed._1);
            assertEquals('a', reversed._5);
        }

        @Test
        void reversedTuple6() {
            CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple6 reversed = tuple.reversed();
            assertEquals('f', reversed._1);
            assertEquals('a', reversed._6);
        }

        @Test
        void reversedTuple7() {
            CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple7 reversed = tuple.reversed();
            assertEquals('g', reversed._1);
            assertEquals('a', reversed._7);
        }

        @Test
        void reversedTuple8() {
            CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple8 reversed = tuple.reversed();
            assertEquals('h', reversed._1);
            assertEquals('a', reversed._8);
        }

        @Test
        void reversedTuple9() {
            CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple9 reversed = tuple.reversed();
            assertEquals('i', reversed._1);
            assertEquals('h', reversed._2);
            assertEquals('g', reversed._3);
            assertEquals('f', reversed._4);
            assertEquals('e', reversed._5);
            assertEquals('d', reversed._6);
            assertEquals('c', reversed._7);
            assertEquals('b', reversed._8);
            assertEquals('a', reversed._9);
        }

        @Test
        void tuple0_reversed() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            CharTuple<?> reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        void tuple0_reversed_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            CharTuple0 reversed = tuple.reversed();
            assertSame(tuple, reversed);
        }

        @Test
        void tuple1_reversed() {
            CharTuple1 tuple = CharTuple.of('A');
            CharTuple1 reversed = tuple.reversed();
            assertEquals('A', reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        void tuple1_reversed_values_A() {
            CharTuple1 tuple = CharTuple.of('A');
            CharTuple1 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals('A', reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        void tuple2_reversed() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            CharTuple2 reversed = tuple.reversed();
            assertEquals('B', reversed._1);
            assertEquals('A', reversed._2);
        }

        @Test
        void tuple2_reversed_values_A_B() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            CharTuple2 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals('B', reversed._1);
            assertEquals('A', reversed._2);
        }

        @Test
        void tuple3_reversed() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharTuple3 reversed = tuple.reversed();
            assertEquals('C', reversed._1);
            assertEquals('B', reversed._2);
            assertEquals('A', reversed._3);
        }

        @Test
        void tuple3_reversed_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharTuple3 reversed = tuple.reversed();
            assertNotNull(reversed);
            assertEquals('C', reversed._1);
            assertEquals('B', reversed._2);
            assertEquals('A', reversed._3);
        }

        @Test
        void tuple4_reversed() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            CharTuple4 reversed = tuple.reversed();
            assertEquals('D', reversed._1);
            assertEquals('C', reversed._2);
            assertEquals('B', reversed._3);
            assertEquals('A', reversed._4);
        }

        @Test
        void tuple5_reversed() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            CharTuple5 reversed = tuple.reversed();
            assertEquals('E', reversed._1);
            assertEquals('A', reversed._5);
        }

        @Test
        void tuple6_reversed() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            CharTuple6 reversed = tuple.reversed();
            assertEquals('F', reversed._1);
            assertEquals('A', reversed._6);
        }

        @Test
        void tuple7_reversed() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            CharTuple7 reversed = tuple.reversed();
            assertEquals('G', reversed._1);
            assertEquals('A', reversed._7);
        }

        @Test
        void tuple8_reversed() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            CharTuple8 reversed = tuple.reversed();
            assertEquals('H', reversed._1);
            assertEquals('A', reversed._8);
        }

        @Test
        void tuple9_reversed() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            CharTuple9 reversed = tuple.reversed();
            assertEquals('I', reversed._1);
            assertEquals('A', reversed._9);
        }
    }

    @Nested
    class ContainsTests {

        @ParameterizedTest(name = "arity {0}: contains checks every component")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void contains_checksEveryComponentAndRejectsMissingValue(int arity) {
            char[] values = new char[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = (char) (i + 1);
            }
            CharTuple<?> tuple = CharTuple.from(values);
            for (char value : values) {
                assertTrue(tuple.contains(value), "missing component " + value);
            }
            assertFalse(tuple.contains((char) 100));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("containsCases")
        void contains_inputCases_matchExpectedMembership(String scenario, CharTuple<?> tuple, char[] present, char[] absent) {
            for (char value : present) {
                assertTrue(tuple.contains(value), scenario + ": expected to contain " + value);
            }
            for (char value : absent) {
                assertFalse(tuple.contains(value), scenario + ": expected to exclude " + value);
            }
        }

        private static java.util.stream.Stream<Arguments> containsCases() {
            return java.util.stream.Stream.of(Arguments.of("from(new char[0])", CharTuple.from(new char[0]), new char[] {}, new char[] { 'a', 'A' }),
                    Arguments.of("of('a')", CharTuple.of('a'), new char[] { 'a' }, new char[] { 'z' }),
                    Arguments.of("of('A')", CharTuple.of('A'), new char[] { 'A' }, new char[] { 'B' }),
                    Arguments.of("of('a', 'b')", CharTuple.of('a', 'b'), new char[] { 'a' }, new char[] {}),
                    Arguments.of("of('A', 'B')", CharTuple.of('A', 'B'), new char[] { 'A', 'B' }, new char[] { 'C' }),
                    Arguments.of("of('a', 'b', 'c')", CharTuple.of('a', 'b', 'c'), new char[] { 'a', 'b', 'c' }, new char[] { 'x', 'z' }),
                    Arguments.of("of('A', 'B', 'C')", CharTuple.of('A', 'B', 'C'), new char[] { 'A', 'B', 'C' }, new char[] { 'Z', 'D' }),
                    Arguments.of("of('a', 'b', 'c', 'd')", CharTuple.of('a', 'b', 'c', 'd'), new char[] { 'b', 'c', 'd', 'a' }, new char[] { 'z' }),
                    Arguments.of("of('A', 'B', 'C', 'D')", CharTuple.of('A', 'B', 'C', 'D'), new char[] { 'A', 'D' }, new char[] { 'E' }),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e')", CharTuple.of('a', 'b', 'c', 'd', 'e'), new char[] { 'b', 'c', 'd', 'e', 'a' },
                            new char[] { 'z' }),
                    Arguments.of("of('A', 'B', 'C', 'D', 'E')", CharTuple.of('A', 'B', 'C', 'D', 'E'), new char[] { 'E' }, new char[] { 'F' }),
                    Arguments.of("of('X', 'Y', 'Z', 'W', 'V')", CharTuple.of('X', 'Y', 'Z', 'W', 'V'), new char[] { 'X', 'Y', 'Z', 'W', 'V' },
                            new char[] { 'A' }),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'), new char[] { 'b', 'c', 'd', 'e', 'f', 'a' },
                            new char[] { 'z' }),
                    Arguments.of("of('A', 'B', 'C', 'D', 'E', 'F')", CharTuple.of('A', 'B', 'C', 'D', 'E', 'F'), new char[] { 'F' }, new char[] { 'G' }),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'),
                            new char[] { 'b', 'c', 'd', 'e', 'f', 'g', 'a' }, new char[] { 'z' }),
                    Arguments.of("of('A', 'B', 'C', 'D', 'E', 'F', 'G')", CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G'), new char[] { 'G' },
                            new char[] { 'H' }),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'),
                            new char[] { 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a' }, new char[] { 'z' }),
                    Arguments.of("of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')", CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'), new char[] { 'H' },
                            new char[] { 'I' }),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'),
                            new char[] { 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'a' }, new char[] { 'z' }),
                    Arguments.of("of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')", CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'),
                            new char[] { 'I' }, new char[] { 'J' }));
        }
    }

    @Nested
    class ToArrayTests {

        @Test
        void toArray_independence() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array1 = tuple.toArray();
            char[] array2 = tuple.toArray();
            assertNotSame(array1, array2); // Should be independent copies
            assertArrayEquals(array1, array2);
        }

        @Test
        void toArray_independence_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array = tuple.toArray();
            array[0] = 'Z';
            assertEquals('A', tuple._1); // Tuple should be unaffected
        }

        @Test
        void toArray_preservesElements() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            char[] array = tuple.toArray();

            assertEquals(3, array.length);
            assertEquals('a', array[0]);
            assertEquals('b', array[1]);
            assertEquals('c', array[2]);
        }

        @Test
        void toArray_preservesElements_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);

            // Verify it's a copy
            array[0] = 'Z';
            assertEquals('A', tuple._1);
        }

        @Test
        void toArrayModificationDoesNotAffectTuple() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            char[] array = tuple.toArray();
            array[0] = 'x';
            assertEquals('a', tuple._1);
        }

        @Test
        void toArrayTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            char[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        void toArrayTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'a' }, array);
        }

        @Test
        void toArrayTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, array);
        }

        @Test
        void tuple0_toArray() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            char[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        void tuple1_toArray() {
            CharTuple1 tuple = CharTuple.of('A');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A' }, array);
        }

        @Test
        void tuple2_toArray() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A', 'B' }, array);
        }

        @Test
        void tuple3_toArray() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);
        }
    }

    @Nested
    class ToListTests {

        @Test
        void toList_empty() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        void toList_independence() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharList list = tuple.toList();
            list.set(0, 'Z');
            assertEquals('A', tuple._1); // Tuple should be unaffected
        }

        @Test
        void toList_multiple() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals('A', list.get(0));
            assertEquals('B', list.get(1));
            assertEquals('C', list.get(2));
        }

        @Test
        void toList_preservesElements() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals('A', list.get(0));
            assertEquals('B', list.get(1));
            assertEquals('C', list.get(2));
        }

        @Test
        void toList_single() {
            CharTuple1 tuple = CharTuple.of('A');
            CharList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals('A', list.get(0));
        }

        @Test
        void toListTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            CharList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        void toListTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            CharList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals('a', list.get(0));
        }

        @Test
        void toListTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals('a', list.get(0));
            assertEquals('b', list.get(1));
            assertEquals('c', list.get(2));
        }

        @Test
        void toListTuple4Through9() {
            CharTuple4 tuple4 = CharTuple.of('a', 'b', 'c', 'd');
            CharList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals('d', list4.get(3));

            CharTuple9 tuple9 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals('i', list9.get(8));
        }

        @Test
        void tuple0_toList() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            CharList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        void tuple1_toList() {
            CharTuple1 tuple = CharTuple.of('A');
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals('A', list.get(0));
        }

        @Test
        void tuple2_toList() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void arity1_forEach() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity2_forEach() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        void arity3_forEach() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        void arity4_forEach() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        @Test
        void arity5_forEach() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        @Test
        void arity6_forEach() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        @Test
        void arity7_forEach() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        @Test
        void arity8_forEach() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        @Test
        void arity9_forEach() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

        @Test
        void forEach_empty() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(c -> count.incrementAndGet());
            assertEquals(0, count.get());
        }

        @Test
        void forEach_tuple2() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(2, visited.size());
            assertEquals('A', visited.get(0));
            assertEquals('B', visited.get(1));
        }

        @Test
        void forEach_tuple3() {
            CharTuple3 tuple = CharTuple.of('X', 'Y', 'Z');
            List<Character> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(3, visited.size());
            assertEquals('X', visited.get(0));
            assertEquals('Y', visited.get(1));
            assertEquals('Z', visited.get(2));
        }

        @Test
        void forEach_withException() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(c -> {
                    if (c == 'B') {
                        throw new RuntimeException("test exception");
                    }
                });
            });
        }

        @Test
        void forEachTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        void forEachTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Character.valueOf('a'), result.get(0));
        }

        @Test
        void forEachTuple2Override() {
            CharTuple2 tuple = CharTuple.of('x', 'y');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Character.valueOf('x'), result.get(0));
            assertEquals(Character.valueOf('y'), result.get(1));
        }

        @Test
        void forEachTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Character.valueOf('a'), result.get(0));
            assertEquals(Character.valueOf('b'), result.get(1));
            assertEquals(Character.valueOf('c'), result.get(2));
        }

        @Test
        void forEachTuple3Override() {
            CharTuple3 tuple = CharTuple.of('x', 'y', 'z');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Character.valueOf('z'), result.get(2));
        }

        @Test
        void forEachTuple4() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Character.valueOf('d'), result.get(3));
        }

        @Test
        void tuple0_forEach() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            List<Character> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        void tuple1_forEach() {
            CharTuple1 tuple = CharTuple.of('A');
            List<Character> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals('A', collected.get(0));
        }

        @Test
        void tuple2_forEach() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals('A', values.get(0));
            assertEquals('B', values.get(1));
        }

        @Test
        void tuple2_forEach_values_A_B() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals('A', collected.get(0));
            assertEquals('B', collected.get(1));
        }

        @Test
        void tuple3_forEach() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        void tuple4_forEach() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(4, values.size());
        }

        @Test
        void tuple5_forEach() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        void tuple6_forEach() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(6, values.size());
        }

        @Test
        void tuple7_forEach() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(7, values.size());
        }

        @Test
        void tuple8_forEach() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(8, values.size());
        }

        @Test
        void tuple9_forEach() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(9, values.size());
        }
    }

    @Nested
    class StreamTests {

        @Test
        void stream_empty() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            int sum = tuple.stream().sum();
            assertEquals(0, sum);
        }

        @Test
        void stream_multiple() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C'); // 65 + 66 + 67 = 198
            int sum = tuple.stream().sum();
            assertEquals(198, sum);
        }

        @Test
        void stream_operations() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            int sum = tuple.stream().sum();
            assertEquals('A' + 'B' + 'C', sum);
        }

        @Test
        void stream_visitsElements() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            long count = tuple.stream().count();

            assertEquals(3, count);
        }

        @Test
        void streamTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            CharStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        void streamTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            CharStream stream = tuple.stream();
            assertEquals(97, stream.sum());
        }

        @Test
        void streamTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharStream stream = tuple.stream();
            assertEquals(294, stream.sum());
        }

        @Test
        void streamTuple4Through9() {
            CharTuple4 tuple4 = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals(394, tuple4.stream().sum());

            CharTuple9 tuple9 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals(909, tuple9.stream().sum());
        }

        @Test
        void tuple0_stream() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        void tuple1_stream() {
            CharTuple1 tuple = CharTuple.of('A');
            int sum = tuple.stream().sum();
            assertEquals('A', sum);
        }

        @Test
        void tuple2_stream() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            long count = tuple.stream().count();
            assertEquals(2, count);
        }
    }

    @Nested
    class AcceptTests {

        @Test
        void accept_tuple2() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> captured = new ArrayList<>();
            tuple.accept((a, b) -> {
                captured.add(a);
                captured.add(b);
            });
            assertEquals(2, captured.size());
            assertEquals('A', captured.get(0));
            assertEquals('B', captured.get(1));
        }

        @Test
        void accept_tuple3() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            List<Character> captured = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                captured.add(a);
                captured.add(b);
                captured.add(c);
            });
            assertEquals(3, captured.size());
            assertEquals('A', captured.get(0));
            assertEquals('B', captured.get(1));
            assertEquals('C', captured.get(2));
        }

        @Test
        void acceptConsumer() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Character> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(Character.valueOf('a'), result.get(0));
            assertEquals(Character.valueOf('b'), result.get(1));
            assertEquals(Character.valueOf('c'), result.get(2));
        }

        @Test
        void arity2_accept_biConsumer() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void arity3_accept_triConsumer() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        void tuple2_accept() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<String> results = new ArrayList<>();
            tuple.accept((a, b) -> results.add("" + a + b));
            assertEquals(1, results.size());
            assertEquals("AB", results.get(0));
        }

        @Test
        void tuple2_accept_values_A_B() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> collected = new ArrayList<>();
            tuple.accept((a, b) -> {
                collected.add(a);
                collected.add(b);
            });
            assertEquals(2, collected.size());
            assertEquals('A', collected.get(0));
            assertEquals('B', collected.get(1));
        }

        @Test
        void tuple2Accept() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            List<Character> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Character.valueOf('c'), result.get(0));
            assertEquals(Character.valueOf('d'), result.get(1));
        }

        @Test
        void tuple3_accept() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            List<String> results = new ArrayList<>();
            tuple.accept((a, b, c) -> results.add("" + a + b + c));
            assertEquals(1, results.size());
            assertEquals("ABC", results.get(0));
        }

        @Test
        void tuple3_accept_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            List<Character> collected = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                collected.add(a);
                collected.add(b);
                collected.add(c);
            });
            assertEquals(3, collected.size());
        }

        @Test
        void tuple3Accept() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Character> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Character.valueOf('a'), result.get(0));
            assertEquals(Character.valueOf('b'), result.get(1));
            assertEquals(Character.valueOf('c'), result.get(2));
        }
    }

    @Nested
    class MapTests {

        @Test
        void arity2_map_biFunction() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        void arity3_map_triFunction() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        void callbackNullGuards_andNullMapResult() {
            CharTuple.CharTuple2 tuple2 = CharTuple.of('a', 'b');

            assertThrows(IllegalArgumentException.class, () -> tuple2.forEach(null));
            assertThrows(IllegalArgumentException.class, () -> tuple2.accept((Throwables.CharBiConsumer<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> tuple2.map((Throwables.CharBiFunction<String, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> tuple2.filter((Throwables.CharBiPredicate<RuntimeException>) null));

            Object mapped = tuple2.map((a, b) -> null);
            assertEquals(null, mapped);
        }

        @Test
        void mapFunction() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String result = tuple.map(t -> "" + t._1 + t._2 + t._3);
            assertEquals("abc", result);
        }

        @Test
        void tuple2_map() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            String result = tuple.map((a, b) -> "" + a + b);
            assertEquals("AB", result);
        }

        @Test
        void tuple2Map() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            String result = tuple.map((a, b) -> String.valueOf(a) + String.valueOf(b));
            assertEquals("cd", result);
        }

        @Test
        void tuple3_map() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            String result = tuple.map((a, b, c) -> "" + a + b + c);
            assertEquals("ABC", result);
        }

        @Test
        void tuple3Map() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String result = tuple.map((a, b, c) -> String.valueOf(a) + String.valueOf(b) + String.valueOf(c));
            assertEquals("abc", result);
        }
    }

    @Nested
    class FilterTests {

        @Test
        void arity2_filter_biPredicate() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        @Test
        void arity3_filter_triPredicate() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        @Test
        void filter_tuple2_fails() {
            CharTuple2 tuple = CharTuple.of('Z', 'A');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        void filter_tuple3_passes() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicate() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter(t -> t._1 == 'a');
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void filterPredicateFalse() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter(t -> t._1 == 'z');
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_matches() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple2_filter_noMatch() {
            CharTuple2 tuple = CharTuple.of('B', 'A');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2_filter_noMatch_values_A_B() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterFalse() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            var result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple2FilterTrue() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            var result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        void tuple3_filter_matches() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        void tuple3_filter_noMatch() {
            CharTuple3 tuple = CharTuple.of('C', 'B', 'A');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3_filter_noMatch_values_A_B_C() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterFalse() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        void tuple3FilterTrue() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class ToOptionalTests {

        @Test
        void toOptional_wrapsValue() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void arity1_hashCode() {
            CharTuple.CharTuple1 tuple1 = CharTuple.of('a');
            CharTuple.CharTuple1 tuple2 = CharTuple.of('a');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity2_hashCode() {
            CharTuple.CharTuple2 tuple1 = CharTuple.of('a', 'b');
            CharTuple.CharTuple2 tuple2 = CharTuple.of('a', 'b');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity3_hashCode() {
            CharTuple.CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
            CharTuple.CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity4_hashCode() {
            CharTuple.CharTuple4 tuple1 = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple.CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity5_hashCode() {
            CharTuple.CharTuple5 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple.CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity6_hashCode() {
            CharTuple.CharTuple6 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple.CharTuple6 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity7_hashCode() {
            CharTuple.CharTuple7 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple.CharTuple7 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity8_hashCode() {
            CharTuple.CharTuple8 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple.CharTuple8 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void arity9_hashCode() {
            CharTuple.CharTuple9 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple.CharTuple9 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_consistency() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_differentValues() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('X', 'Y');
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCode_empty() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertNotNull(tuple.hashCode());
        }

        @Test
        void hashCode_matchesValueContract() {
            CharTuple.CharTuple3 tuple1 = CharTuple.of('x', 'y', 'z');
            CharTuple.CharTuple3 tuple2 = CharTuple.of('x', 'y', 'z');
            CharTuple.CharTuple3 tuple3 = CharTuple.of('a', 'b', 'c');

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
        }

        @Test
        void hashCodeTuple1() {
            CharTuple1 tuple1 = CharTuple.of('a');
            CharTuple1 tuple2 = CharTuple.of('a');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple2() {
            CharTuple2 tuple1 = CharTuple.of('a', 'b');
            CharTuple2 tuple2 = CharTuple.of('a', 'b');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void hashCodeTuple3() {
            CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
            CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple1_hashCode() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('A');
            CharTuple1 tuple3 = CharTuple.of('B');

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(65, tuple1.hashCode()); // ASCII value of 'A'
            assertEquals(66, tuple3.hashCode()); // ASCII value of 'B'
        }

        @Test
        void tuple1_hashCode_values_A() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('A');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple2_hashCode() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        void tuple3_hashCode() {
            CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
            CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    class EqualsTests {

        @ParameterizedTest(name = "arity {0}: equality value contract")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void equals_allArities_obeysValueContract(int arity) {
            char[] values = new char[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = (char) (i + 1);
            }
            CharTuple<?> tuple = CharTuple.from(values);
            // A separately constructed empty value verifies equality independently of the factory singleton.
            CharTuple<?> equal = arity == 0 ? new CharTuple.CharTuple0() : CharTuple.from(values.clone());
            assertNotSame(tuple, equal);
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(equal));
            assertTrue(equal.equals(tuple));
            CharTuple<?> third = arity == 0 ? new CharTuple.CharTuple0() : CharTuple.from(values.clone());
            assertNotSame(equal, third);
            assertTrue(equal.equals(third));
            assertTrue(tuple.equals(third), "equality must be transitive");
            assertEquals(tuple.hashCode(), equal.hashCode());
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals(new Object()));

            for (int i = 0; i < arity; i++) {
                char[] changed = values.clone();
                changed[i] = (char) 100;
                CharTuple<?> different = CharTuple.from(changed);
                assertFalse(tuple.equals(different), "component " + i);
                assertFalse(different.equals(tuple), "component " + i);
            }

            for (int otherArity = 0; otherArity <= 9; otherArity++) {
                if (otherArity != arity) {
                    CharTuple<?> differentArity = CharTuple.from(java.util.Arrays.copyOf(values, otherArity));
                    assertFalse(tuple.equals(differentArity), "different arity " + otherArity);
                    assertFalse(differentArity.equals(tuple), "reverse comparison with arity " + otherArity);
                }
            }
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("equalityCases")
        void equals_inputCases_comparesValuesAndTypes(String scenario, CharTuple<?> tuple, Object other, boolean expected) {
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
        void arity9_equalsAndHashCode_differInOneField() {
            char[] base = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' };
            CharTuple9 t = CharTuple.of(base[0], base[1], base[2], base[3], base[4], base[5], base[6], base[7], base[8]);
            for (int i = 0; i < 9; i++) {
                char[] diff = base.clone();
                diff[i] = (char) (diff[i] + 1);
                CharTuple9 other = CharTuple.of(diff[0], diff[1], diff[2], diff[3], diff[4], diff[5], diff[6], diff[7], diff[8]);
                assertNotEquals(t, other, "field _" + (i + 1) + " should affect equals");
                assertNotEquals(t.hashCode(), other.hashCode(), "field _" + (i + 1) + " should affect hashCode");
            }
        }

        private static java.util.stream.Stream<Arguments> equalityCases() {
            return java.util.stream.Stream.of(Arguments.of("of('a') versus of('a')", CharTuple.of('a'), CharTuple.of('a'), true),
                    Arguments.of("of('a') versus of('z')", CharTuple.of('a'), CharTuple.of('z'), false),
                    Arguments.of("of('A') versus \"not a tuple\"", CharTuple.of('A'), "not a tuple", false),
                    Arguments.of("of('A') versus of('A', 'B')", CharTuple.of('A'), CharTuple.of('A', 'B'), false),
                    Arguments.of("of('A') versus of('A')", CharTuple.of('A'), CharTuple.of('A'), true),
                    Arguments.of("of('A') versus of('B')", CharTuple.of('A'), CharTuple.of('B'), false),
                    Arguments.of("of('A') versus null", CharTuple.of('A'), null, false),
                    Arguments.of("of('a', 'b') versus of('a', 'b')", CharTuple.of('a', 'b'), CharTuple.of('a', 'b'), true),
                    Arguments.of("of('a', 'b') versus of('a', 'c')", CharTuple.of('a', 'b'), CharTuple.of('a', 'c'), false),
                    Arguments.of("of('A', 'B') versus \"AB\"", CharTuple.of('A', 'B'), "AB", false),
                    Arguments.of("of('A', 'B') versus of('A', 'B', 'C')", CharTuple.of('A', 'B'), CharTuple.of('A', 'B', 'C'), false),
                    Arguments.of("of('A', 'B') versus of('A', 'B')", CharTuple.of('A', 'B'), CharTuple.of('A', 'B'), true),
                    Arguments.of("of('A', 'B') versus of('B', 'A')", CharTuple.of('A', 'B'), CharTuple.of('B', 'A'), false),
                    Arguments.of("of('A', 'B') versus of('X', 'Y')", CharTuple.of('A', 'B'), CharTuple.of('X', 'Y'), false),
                    Arguments.of("of('A', 'B') versus null", CharTuple.of('A', 'B'), null, false),
                    Arguments.of("of('a', 'b', 'c') versus \"not a tuple\"", CharTuple.of('a', 'b', 'c'), "not a tuple", false),
                    Arguments.of("of('a', 'b', 'c') versus of('a', 'b', 'c')", CharTuple.of('a', 'b', 'c'), CharTuple.of('a', 'b', 'c'), true),
                    Arguments.of("of('a', 'b', 'c') versus of('a', 'b', 'd')", CharTuple.of('a', 'b', 'c'), CharTuple.of('a', 'b', 'd'), false),
                    Arguments.of("of('a', 'b', 'c') versus null", CharTuple.of('a', 'b', 'c'), null, false),
                    Arguments.of("of('A', 'B', 'C') versus of('A', 'B', 'C')", CharTuple.of('A', 'B', 'C'), CharTuple.of('A', 'B', 'C'), true),
                    Arguments.of("of('A', 'B', 'C') versus of('A', 'B', 'D')", CharTuple.of('A', 'B', 'C'), CharTuple.of('A', 'B', 'D'), false),
                    Arguments.of("of('A', 'B', 'C') versus of('C', 'B', 'A')", CharTuple.of('A', 'B', 'C'), CharTuple.of('C', 'B', 'A'), false),
                    Arguments.of("of('x', 'y', 'z') versus of('a', 'b', 'c')", CharTuple.of('x', 'y', 'z'), CharTuple.of('a', 'b', 'c'), false),
                    Arguments.of("of('x', 'y', 'z') versus of('x', 'y', 'z')", CharTuple.of('x', 'y', 'z'), CharTuple.of('x', 'y', 'z'), true),
                    Arguments.of("of('x', 'y', 'z') versus null", CharTuple.of('x', 'y', 'z'), null, false),
                    Arguments.of("of('a', 'b', 'c', 'd') versus \"not a tuple\"", CharTuple.of('a', 'b', 'c', 'd'), "not a tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd') versus \"tuple4\"", CharTuple.of('a', 'b', 'c', 'd'), "tuple4", false),
                    Arguments.of("of('a', 'b', 'c', 'd') versus of('a', 'b', 'c', 'd')", CharTuple.of('a', 'b', 'c', 'd'), CharTuple.of('a', 'b', 'c', 'd'),
                            true),
                    Arguments.of("of('a', 'b', 'c', 'd') versus of('a', 'b', 'c', 'z')", CharTuple.of('a', 'b', 'c', 'd'), CharTuple.of('a', 'b', 'c', 'z'),
                            false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e') versus \"not a tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e'), "not a tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e') versus \"tuple5\"", CharTuple.of('a', 'b', 'c', 'd', 'e'), "tuple5", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e') versus of('a', 'b', 'c', 'd', 'e')", CharTuple.of('a', 'b', 'c', 'd', 'e'),
                            CharTuple.of('a', 'b', 'c', 'd', 'e'), true),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e') versus of('a', 'b', 'c', 'd', 'z')", CharTuple.of('a', 'b', 'c', 'd', 'e'),
                            CharTuple.of('a', 'b', 'c', 'd', 'z'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f') versus \"not a tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'), "not a tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f') versus \"tuple6\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'), "tuple6", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f') versus of('a', 'b', 'c', 'd', 'e', 'f')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'),
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'), true),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f') versus of('a', 'b', 'c', 'd', 'e', 'z')", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f'),
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'z'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g') versus \"not a tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), "not a tuple",
                            false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g') versus \"tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), "tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g') versus \"tuple7\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), "tuple7", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), true),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g') versus of('a', 'b', 'c', 'd', 'e', 'f', 'x')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'x'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g') versus of('a', 'b', 'c', 'd', 'e', 'f', 'z')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'z'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h') versus \"not a tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'),
                            "not a tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h') versus \"tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), "tuple",
                            false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h') versus \"tuple8\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), "tuple8",
                            false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), true),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'x')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'x'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'z')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'z'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i') versus \"not a tuple\"",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'), "not a tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i') versus \"tuple\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'),
                            "tuple", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i') versus \"tuple9\"", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'),
                            "tuple9", false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'), true),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'x')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'x'), false),
                    Arguments.of("of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i') versus of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'z')",
                            CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'), CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'z'), false));
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void arity1_toString() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity2_toString() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity3_toString() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity4_toString() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity5_toString() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity6_toString() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity7_toString() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity8_toString() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertNotNull(tuple.toString());
        }

        @Test
        void arity9_toString() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertNotNull(tuple.toString());
        }

        @Test
        void toString_formatsValue() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            String result = tuple.toString();
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
        }

        @Test
        void toStringForHigherArityUsesTupleFormat() {
            assertEquals("(a, b, c, d)", CharTuple.of('a', 'b', 'c', 'd').toString());
            assertEquals("(a, b, c, d, e, f, g, h, i)", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i').toString());
        }

        @Test
        void toStringTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void toStringTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            String str = tuple.toString();
            assertTrue(str.contains("a"));
        }

        @Test
        void toStringTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String str = tuple.toString();
            assertTrue(str.contains("a"));
            assertTrue(str.contains("b"));
            assertTrue(str.contains("c"));
        }

        @Test
        void tuple0_toString() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple0_toString_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        void tuple1_toString() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals("(A)", tuple.toString());
        }

        @Test
        void tuple2_toString() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals("(A, B)", tuple.toString());
        }

        @Test
        void tuple3_toString() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals("(A, B, C)", tuple.toString());
        }
    }

    @Nested
    class ContractTests {

        @Test
        void arity1_median() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity2_median() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void arity3_median() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.lowerMedian());
        }

        @Test
        void classDocExamples_renderAsDocumented() {
            final int mapped = CharTuple.of('A', 'B').map((a, b) -> a + b);
            assertEquals(131, mapped);

            final CharTuple.CharTuple3 t3 = CharTuple.of('A', 'B', 'C');
            assertEquals('A', t3.min());
            assertEquals('C', t3.max());
            assertEquals(66.0, t3.average().getAsDouble());

            assertEquals('B', CharTuple.of('A', 'B', 'C', 'D').lowerMedian());
            assertEquals(335, CharTuple.of('A', 'B', 'C', 'D', 'E').sum());
            assertEquals("(F, E, D, C, B, A)", CharTuple.of('A', 'B', 'C', 'D', 'E', 'F').reversed().toString());
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' }, CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G').toArray());
            assertEquals(9, CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I').arity());
        }

        @Test
        void classLevelExamples() {
            // char min = triple.min();   // 'A'
            // char max = triple.max();   // 'C'
            // OptionalDouble avg = triple.average();   // OptionalDouble.of(66.0) (average of ASCII values)
            CharTuple.CharTuple3 triple = CharTuple.of('A', 'B', 'C');
            assertEquals('A', triple.min());
            assertEquals('C', triple.max());
            assertEquals(66.0, triple.average().getAsDouble(), 0.001);
        }

        @ParameterizedTest(name = "arity {0}: default exports are detached")
        @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
        void defaultConstructors_exportDefaultValuesInIndependentArrays(int arity) {
            CharTuple<?> tuple = switch (arity) {
                case 1 -> new CharTuple.CharTuple1();
                case 2 -> new CharTuple.CharTuple2();
                case 3 -> new CharTuple.CharTuple3();
                case 4 -> new CharTuple.CharTuple4();
                case 5 -> new CharTuple.CharTuple5();
                case 6 -> new CharTuple.CharTuple6();
                case 7 -> new CharTuple.CharTuple7();
                case 8 -> new CharTuple.CharTuple8();
                case 9 -> new CharTuple.CharTuple9();
                default -> throw new IllegalArgumentException("Unsupported tuple arity: " + arity);
            };
            char[] expected = new char[arity];
            char[] first = tuple.toArray();
            char[] second = tuple.toArray();

            assertEquals(arity, tuple.arity());
            assertArrayEquals(expected, first);
            assertArrayEquals(expected, second);
            assertNotSame(first, second);
            first[0] = 'A';
            assertArrayEquals(expected, second, "each export must have independent storage");
            assertArrayEquals(expected, tuple.toArray(), "mutating an export must not affect the tuple");
        }

        @Test
        void emptyTuple_operations_obeyEmptyValueContract() {
            CharTuple<?> empty = CharTuple.from((char[]) null);
            assertEquals(CharTuple.from(new char[0]), empty);
            assertFalse(empty.equals("char"));
            assertEquals("()", empty.toString());
            assertEquals(1, empty.hashCode());
        }

        @Test
        void digitCharacters() {
            CharTuple3 tuple = CharTuple.of('1', '2', '3');
            assertEquals('1', tuple.min());
            assertEquals('3', tuple.max());
            assertEquals('2', tuple.lowerMedian());
        }

        @Test
        void inheritedAggregateEmptyBehavior() {
            final CharTuple.CharTuple0 tuple = CharTuple.from(new char[0]);

            assertThrows(NoSuchElementException.class, tuple::min);
            assertThrows(NoSuchElementException.class, tuple::max);
            assertTrue(tuple.average().isEmpty());
            assertEquals(0, tuple.sum());
        }

        @Test
        void inheritedOperations_preserveTupleValues() throws Exception {
            final CharTuple.CharTuple4 tuple = CharTuple.of('d', 'a', 'c', 'b');
            final char[] firstArray = tuple.toArray();
            final char[] secondArray = tuple.toArray();
            final List<Character> visited = new ArrayList<>();

            tuple.forEach(visited::add);

            assertEquals('a', tuple.min());
            assertEquals('d', tuple.max());
            assertEquals('b', tuple.lowerMedian());
            assertEquals('d' + 'a' + 'c' + 'b', tuple.sum());
            assertEquals(('d' + 'a' + 'c' + 'b') / 4.0, tuple.average().getAsDouble(), 0.001);
            assertArrayEquals(new char[] { 'd', 'a', 'c', 'b' }, firstArray);
            assertArrayEquals(firstArray, secondArray);
            assertNotSame(firstArray, secondArray);
            assertEquals(CharList.of('d', 'a', 'c', 'b'), tuple.toList());
            assertEquals(visited, tuple.stream().boxed().toList());
            assertEquals(tuple.hashCode(), CharTuple.of('d', 'a', 'c', 'b').hashCode());
            assertTrue(tuple.equals(tuple));
            assertTrue(tuple.equals(CharTuple.of('d', 'a', 'c', 'b')));
            assertFalse(tuple.equals(CharTuple.of('d', 'a', 'c', 'z')));
            assertFalse(tuple.equals(null));
            assertFalse(tuple.equals("not a tuple"));
            assertEquals("(d, a, c, b)", tuple.toString());
            assertArrayEquals(new char[] { 'b', 'c', 'a', 'd' }, tuple.reversed().toArray());
        }

        @Test
        void largeCharValues() {
            CharTuple3 tuple = CharTuple.of('\u00FF', '\u0080', '\u0100');
            assertNotNull(tuple);
            assertTrue(tuple.contains('\u0080'));
        }

        @Test
        void lowercaseCharacters() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals('a', tuple.min());
            assertEquals('c', tuple.max());
            assertEquals(294, tuple.sum()); // 97 + 98 + 99
        }

        @Test
        void median_even() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertEquals('B', tuple.lowerMedian()); // Lower middle for even length
        }

        @Test
        void median_odd() {
            CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
            assertEquals('M', tuple.lowerMedian());
        }

        @Test
        void median_single() {
            CharTuple1 tuple = CharTuple.of('M');
            assertEquals('M', tuple.lowerMedian());
        }

        @Test
        void medianTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void medianTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple.lowerMedian());
        }

        @Test
        void medianTuple3() {
            CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('b', tuple.lowerMedian());
        }

        @Test
        void mixedCase() {
            CharTuple2 tuple = CharTuple.of('a', 'A');
            assertEquals('A', tuple.min()); // uppercase has lower ASCII value
            assertEquals('a', tuple.max());
        }

        @Test
        void mixedCaseCharacters() {
            CharTuple2 tuple = CharTuple.of('A', 'a');
            assertEquals('A', tuple.min()); // 'A' = 65, 'a' = 97
            assertEquals('a', tuple.max());
        }

        @Test
        void numericChars() {
            CharTuple3 tuple = CharTuple.of('0', '1', '2');
            assertEquals('0', tuple.min());
            assertEquals('2', tuple.max());
            assertEquals('1', tuple.lowerMedian());
        }

        @Test
        void reverse() {
            // CharTuple.CharTuple7 tuple = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
            // CharTuple.CharTuple7 reversed = tuple.reversed();   // ('S', 'Y', 'A', 'D', 'N', 'O', 'M')
            CharTuple.CharTuple7 t = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
            CharTuple.CharTuple7 reversed = t.reversed();
            assertEquals('S', reversed._1);
            assertEquals('Y', reversed._2);
            assertEquals('A', reversed._3);
            assertEquals('D', reversed._4);
            assertEquals('N', reversed._5);
            assertEquals('O', reversed._6);
            assertEquals('M', reversed._7);
        }

        @Test
        void singleElement_allOperations() {
            CharTuple1 tuple = CharTuple.of('X');
            assertEquals('X', tuple.min());
            assertEquals('X', tuple.max());
            assertEquals('X', tuple.lowerMedian());
            assertEquals('X', tuple.average().getAsDouble()); // 88.0
            assertEquals(88, tuple.sum());
            assertTrue(tuple.contains('X'));
            assertFalse(tuple.contains('Y'));
            CharTuple1 reversed = tuple.reversed();
            assertEquals('X', reversed._1);
        }

        @Test
        void specialCharacters() {
            CharTuple3 tuple = CharTuple.of('!', '@', '#');
            assertTrue(tuple.contains('!'));
            assertTrue(tuple.contains('@'));
            assertTrue(tuple.contains('#'));
        }

        @Test
        void specialChars() {
            CharTuple3 tuple = CharTuple.of('\n', '\t', ' ');
            assertEquals(3, tuple.arity());
            assertTrue(tuple.contains('\n'));
            assertTrue(tuple.contains('\t'));
            assertTrue(tuple.contains(' '));
        }

        @Test
        void specialChars_additionalAssertions() {
            CharTuple3 tuple = CharTuple.of('!', '@', '#');
            assertEquals('!', tuple.min());
            assertEquals('@', tuple.max());
        }

        @Test
        void statistics_allOperations() {
            CharTuple5 tuple = CharTuple.of('A', 'E', 'C', 'B', 'D');
            assertEquals('A', tuple.min());
            assertEquals('E', tuple.max());
            assertEquals('C', tuple.lowerMedian());
            assertEquals(65 + 69 + 67 + 66 + 68, tuple.sum()); // A(65)+E(69)+C(67)+B(66)+D(68)
            assertTrue(tuple.average().getAsDouble() > 0);
        }

        @Test
        void tuple0_median_throwsException() {
            CharTuple<?> tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void tuple0_median_throwsException_values_CharTuple_from() {
            CharTuple0 tuple = CharTuple.from(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.lowerMedian());
        }

        @Test
        void tuple1_median() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.lowerMedian());
        }

        @Test
        void tuple2_median() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals('A', tuple.lowerMedian());
        }

        @Test
        void tuple3_median() {
            CharTuple3 tuple = CharTuple.of('A', 'C', 'B');
            assertEquals('B', tuple.lowerMedian());
        }

        @Test
        void tuple3_median_values_C_A_B() {
            CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
            assertEquals('B', tuple.lowerMedian());
        }

        @Test
        void tuple4_basic() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertEquals(4, tuple.arity());
            assertEquals('A', tuple.min());
            assertEquals('D', tuple.max());
            assertTrue(tuple.contains('C'));
            CharTuple4 reversed = tuple.reversed();
            assertEquals('D', reversed._1);
            assertEquals('A', reversed._4);
        }

        @Test
        void tuple4Operations() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');

            // Test reverse
            CharTuple4 reversed = tuple.reversed();
            assertEquals('d', reversed._1);
            assertEquals('c', reversed._2);
            assertEquals('b', reversed._3);
            assertEquals('a', reversed._4);

            // Test contains
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('d'));
            assertFalse(tuple.contains('z'));

            // Test toArray
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals('a', tuple.min());
            assertEquals('d', tuple.max());
            assertEquals('b', tuple.lowerMedian());
            assertEquals(394, tuple.sum()); // 97+98+99+100
            assertEquals(98.5, tuple.average().getAsDouble(), 0.001);

            // Test hashCode and equals
            CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple4 tuple3 = CharTuple.of('a', 'b', 'c', 'e');
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("a"));
            assertTrue(str.contains("d"));
        }

        @Test
        void tuple5_basic() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertEquals(5, tuple.arity());
            assertEquals('A', tuple.min());
            assertEquals('E', tuple.max());
            assertEquals('C', tuple.lowerMedian());
            assertTrue(tuple.contains('C'));
        }

        @Test
        void tuple5Operations() {
            CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');

            // Test reverse
            CharTuple5 reversed = tuple.reversed();
            assertEquals('e', reversed._1);
            assertEquals('a', reversed._5);

            // Test contains
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('e'));
            assertFalse(tuple.contains('z'));

            // Test toArray
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e' }, tuple.toArray());

            // Test statistical operations
            assertEquals('a', tuple.min());
            assertEquals('e', tuple.max());
            assertEquals('c', tuple.lowerMedian());
            assertEquals(495, tuple.sum()); // 97+98+99+100+101
            assertEquals(99.0, tuple.average().getAsDouble(), 0.001);

            // Test equals
            CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals(tuple, tuple2);
        }

        @Test
        void tuple6_basic() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            assertEquals(6, tuple.arity());
            assertEquals('A', tuple.min());
            assertEquals('F', tuple.max());
        }

        @Test
        void tuple6Operations() {
            CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');

            // Test reverse
            CharTuple6 reversed = tuple.reversed();
            assertEquals('f', reversed._1);
            assertEquals('a', reversed._6);

            // Test contains
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('f'));
            assertFalse(tuple.contains('z'));

            // Test toArray
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, tuple.toArray());

            // Test statistical operations
            assertEquals('a', tuple.min());
            assertEquals('f', tuple.max());
            assertEquals(597, tuple.sum()); // 97+98+99+100+101+102
            assertEquals(99.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple7_basic() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            assertEquals(7, tuple.arity());
            assertEquals('D', tuple.lowerMedian());
            assertEquals(7, tuple.toArray().length);
        }

        @Test
        void tuple7Operations() {
            CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');

            // Test reverse
            CharTuple7 reversed = tuple.reversed();
            assertEquals('g', reversed._1);
            assertEquals('a', reversed._7);

            // Test contains
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('g'));
            assertFalse(tuple.contains('z'));

            // Test toArray
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' }, tuple.toArray());

            // Test statistical operations
            assertEquals('a', tuple.min());
            assertEquals('g', tuple.max());
            assertEquals(700, tuple.sum()); // 97+98+99+100+101+102+103
            assertEquals(100.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple8_basic() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
        }

        @Test
        void tuple8Operations() {
            CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');

            // Test reverse
            CharTuple8 reversed = tuple.reversed();
            assertEquals('h', reversed._1);
            assertEquals('a', reversed._8);

            // Test contains
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('h'));
            assertFalse(tuple.contains('z'));

            // Test toArray
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' }, tuple.toArray());

            // Test statistical operations
            assertEquals('a', tuple.min());
            assertEquals('h', tuple.max());
            assertEquals(804, tuple.sum()); // 97+98+99+100+101+102+103+104
            assertEquals(100.5, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void tuple9_basic() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
        }

        @Test
        void tuple9Operations() {
            CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');

            // Test reverse
            CharTuple9 reversed = tuple.reversed();
            assertEquals('i', reversed._1);
            assertEquals('a', reversed._9);

            // Test contains
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('i'));
            assertFalse(tuple.contains('z'));

            // Test toArray
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' }, tuple.toArray());

            // Test statistical operations
            assertEquals('a', tuple.min());
            assertEquals('i', tuple.max());
            assertEquals(909, tuple.sum()); // 97+98+99+100+101+102+103+104+105
            assertEquals(101.0, tuple.average().getAsDouble(), 0.001);
        }

        @Test
        void whitespaceChars() {
            CharTuple2 tuple = CharTuple.of(' ', '\t');
            assertTrue(tuple.contains(' '));
            assertTrue(tuple.contains('\t'));
            assertNotNull(tuple.toString());
        }

        @Test
        void highArity_aggregatesAndMembership_preserveValues() {
            CharTuple.CharTuple4 tuple4 = CharTuple.of('i', 'a', 'g', 'c');
            assertEquals('a', tuple4.min());
            assertEquals('i', tuple4.max());
            assertEquals('c', tuple4.lowerMedian());
            assertEquals('i' + 'a' + 'g' + 'c', tuple4.sum());
            assertEquals(((double) 'i' + 'a' + 'g' + 'c') / 4, tuple4.average().getAsDouble());
            assertTrue(tuple4.contains('c'));

            CharTuple.CharTuple5 tuple5 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertTrue(tuple5.contains('e'));

            CharTuple.CharTuple6 tuple6 = CharTuple.of('i', 'a', 'g', 'c', 'e', 'k');
            assertEquals('e', tuple6.lowerMedian());
            assertTrue(tuple6.contains('k'));

            CharTuple.CharTuple7 tuple7 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertTrue(tuple7.contains('g'));

            CharTuple.CharTuple8 tuple8 = CharTuple.of('i', 'a', 'g', 'c', 'e', 'k', 'm', 'o');
            assertEquals('g', tuple8.lowerMedian());
            assertTrue(tuple8.contains('o'));

            CharTuple.CharTuple9 tuple9 = CharTuple.of('i', 'a', 'g', 'c', 'e', 'k', 'm', 'o', 'q');
            assertEquals('i', tuple9.lowerMedian());
            assertTrue(tuple9.contains('q'));
        }
    }
}
