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
import com.landawn.abacus.TestBase;
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
import com.landawn.abacus.util.stream.CharStream;
import com.landawn.abacus.util.u.Optional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class CharTupleTest extends TestBase {

    @Test
    public void testOf1() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertEquals(1, tuple.arity());
        assertEquals('a', tuple._1);
    }

    @Test
    public void testOf2() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertEquals(2, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
    }

    @Test
    public void testOf3() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals(3, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
    }

    @Test
    public void testOf4() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals(4, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
    }

    @Test
    public void testOf5() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals(5, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
    }

    @Test
    public void testOf6() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertEquals(6, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
    }

    @Test
    public void testOf7() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertEquals(7, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
        assertEquals('g', tuple._7);
    }

    @Test
    public void testOf8() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertEquals(8, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
        assertEquals('g', tuple._7);
        assertEquals('h', tuple._8);
    }

    @Test
    public void testOf9() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals(9, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
        assertEquals('g', tuple._7);
        assertEquals('h', tuple._8);
        assertEquals('i', tuple._9);
    }

    @Test
    public void testEquals() {
        CharTuple.CharTuple3 tuple1 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple2 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple3 = CharTuple.of('a', 'b', 'c');

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testHashCode() {
        CharTuple.CharTuple3 tuple1 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple2 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple3 = CharTuple.of('a', 'b', 'c');

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testToString() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        String result = tuple.toString();
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
    }

    @Test
    public void testToStringForHigherArityUsesTupleFormat() {
        assertEquals("(a, b, c, d)", CharTuple.of('a', 'b', 'c', 'd').toString());
        assertEquals("(a, b, c, d, e, f, g, h, i)", CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i').toString());
    }

    @Test
    public void testReverse() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        CharTuple.CharTuple3 reversed = tuple.reverse();

        assertEquals('c', reversed._1);
        assertEquals('b', reversed._2);
        assertEquals('a', reversed._3);
    }

    @Test
    public void testContains() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');

        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('b'));
        assertTrue(tuple.contains('c'));
        assertFalse(tuple.contains('x'));
    }

    @Test
    public void testToArray() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        char[] array = tuple.toArray();

        assertEquals(3, array.length);
        assertEquals('a', array[0]);
        assertEquals('b', array[1]);
        assertEquals('c', array[2]);
    }

    @Test
    public void testStream() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        long count = tuple.stream().count();

        assertEquals(3, count);
    }

    @Test
    public void testMin() {
        CharTuple.CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('a', tuple.min());
    }

    @Test
    public void testMax() {
        CharTuple.CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('c', tuple.max());
    }

    @Test
    public void testSum() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        int sum = tuple.sum();
        assertEquals('a' + 'b' + 'c', sum);
    }

    @Test
    public void testAverage() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'c');
        double average = tuple.average();
        assertEquals(('a' + 'c') / 2.0, average, 0.001);
    }

    @Nested
    /**
     * Comprehensive test suite for CharTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class CharTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            CharTuple2 tuple = CharTuple.of('a', 'b');
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
            assertEquals('c', tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
            assertEquals('c', tuple._3);
            assertEquals('d', tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals('a', tuple._1);
            assertEquals('e', tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertEquals('a', tuple._1);
            assertEquals('f', tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertEquals('a', tuple._1);
            assertEquals('g', tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertEquals('a', tuple._1);
            assertEquals('h', tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals('a', tuple._1);
            assertEquals('i', tuple._9);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            CharTuple1 tuple = CharTuple.copyOf(new char[] { 'a' });
            assertEquals('a', tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            CharTuple3 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c' });
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
            assertEquals('c', tuple._3);
        }

        @Test
        public void testCreate9() {
            CharTuple9 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' });
            assertEquals('a', tuple._1);
            assertEquals('i', tuple._9);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' });
            });
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple.min());
        }

        @Test
        public void testMinTuple3() {
            CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('a', tuple.min());
        }

        @Test
        public void testMinTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple.max());
        }

        @Test
        public void testMaxTuple3() {
            CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('c', tuple.max());
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals('a', tuple.median());
        }

        @Test
        public void testMedianTuple3() {
            CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
            assertEquals('b', tuple.median());
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testSumTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals(97, tuple.sum());
        }

        @Test
        public void testSumTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals(294, tuple.sum());
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            assertEquals(97.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals(98.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            CharTuple<CharTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            CharTuple1 reversed = tuple.reverse();
            assertEquals('a', reversed._1);
        }

        @Test
        public void testReverseTuple2() {
            CharTuple2 tuple = CharTuple.of('a', 'b');
            CharTuple2 reversed = tuple.reverse();
            assertEquals('b', reversed._1);
            assertEquals('a', reversed._2);
        }

        @Test
        public void testReverseTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharTuple3 reversed = tuple.reverse();
            assertEquals('c', reversed._1);
            assertEquals('b', reversed._2);
            assertEquals('a', reversed._3);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertFalse(tuple.contains('a'));
        }

        @Test
        public void testContainsTuple1True() {
            CharTuple1 tuple = CharTuple.of('a');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testContainsTuple1False() {
            CharTuple1 tuple = CharTuple.of('a');
            assertFalse(tuple.contains('z'));
        }

        @Test
        public void testContainsTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('b'));
            assertTrue(tuple.contains('c'));
            assertFalse(tuple.contains('z'));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            char[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'a' }, array);
        }

        @Test
        public void testToArrayTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, array);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            char[] array = tuple.toArray();
            array[0] = 'x';
            assertEquals('a', tuple._1);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            CharList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            CharList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals('a', list.get(0));
        }

        @Test
        public void testToListTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals('a', list.get(0));
            assertEquals('b', list.get(1));
            assertEquals('c', list.get(2));
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Character.valueOf('a'), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Character.valueOf('a'), result.get(0));
            assertEquals(Character.valueOf('b'), result.get(1));
            assertEquals(Character.valueOf('c'), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            CharStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            CharStream stream = tuple.stream();
            assertEquals(97, stream.sum());
        }

        @Test
        public void testStreamTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharStream stream = tuple.stream();
            assertEquals(294, stream.sum());
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            CharTuple1 tuple1 = CharTuple.of('a');
            CharTuple1 tuple2 = CharTuple.of('a');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple2() {
            CharTuple2 tuple1 = CharTuple.of('a', 'b');
            CharTuple2 tuple2 = CharTuple.of('a', 'b');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple3() {
            CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
            CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            CharTuple1 tuple1 = CharTuple.of('a');
            CharTuple1 tuple2 = CharTuple.of('a');
            CharTuple1 tuple3 = CharTuple.of('z');
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            CharTuple2 tuple1 = CharTuple.of('a', 'b');
            CharTuple2 tuple2 = CharTuple.of('a', 'b');
            CharTuple2 tuple3 = CharTuple.of('a', 'c');
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
            CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
            CharTuple3 tuple3 = CharTuple.of('a', 'b', 'd');
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            CharTuple<CharTuple0> tuple = CharTuple.copyOf(new char[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            CharTuple1 tuple = CharTuple.of('a');
            String str = tuple.toString();
            assertTrue(str.contains("a"));
        }

        @Test
        public void testToStringTuple3() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String str = tuple.toString();
            assertTrue(str.contains("a"));
            assertTrue(str.contains("b"));
            assertTrue(str.contains("c"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
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

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            String result = tuple.map((a, b) -> String.valueOf(a) + String.valueOf(b));
            assertEquals("cd", result);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            var result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            CharTuple2 tuple = CharTuple.of('c', 'd');
            var result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
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

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String result = tuple.map((a, b, c) -> String.valueOf(a) + String.valueOf(b) + String.valueOf(c));
            assertEquals("abc", result);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, CharTuple.copyOf(new char[0]).arity());
            assertEquals(1, CharTuple.of('a').arity());
            assertEquals(2, CharTuple.of('a', 'b').arity());
            assertEquals(3, CharTuple.of('a', 'b', 'c').arity());
            assertEquals(4, CharTuple.of('a', 'b', 'c', 'd').arity());
            assertEquals(5, CharTuple.of('a', 'b', 'c', 'd', 'e').arity());
            assertEquals(6, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f').arity());
            assertEquals(7, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g').arity());
            assertEquals(8, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h').arity());
            assertEquals(9, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i').arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
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
        public void testMapFunction() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String result = tuple.map(t -> "" + t._1 + t._2 + t._3);
            assertEquals("abc", result);
        }

        @Test
        public void testFilterPredicate() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter(t -> t._1 == 'a');
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.filter(t -> t._1 == 'z');
            assertFalse(result.isPresent());
        }

        @Test
        public void testToOptional() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        // Additional tests for larger tuple sizes - reverse
        @Test
        public void testReverseTuple4() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple4 reversed = tuple.reverse();
            assertEquals('d', reversed._1);
            assertEquals('c', reversed._2);
            assertEquals('b', reversed._3);
            assertEquals('a', reversed._4);
        }

        @Test
        public void testReverseTuple5() {
            CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple5 reversed = tuple.reverse();
            assertEquals('e', reversed._1);
            assertEquals('a', reversed._5);
        }

        @Test
        public void testReverseTuple6() {
            CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple6 reversed = tuple.reverse();
            assertEquals('f', reversed._1);
            assertEquals('a', reversed._6);
        }

        @Test
        public void testReverseTuple7() {
            CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple7 reversed = tuple.reverse();
            assertEquals('g', reversed._1);
            assertEquals('a', reversed._7);
        }

        @Test
        public void testReverseTuple8() {
            CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple8 reversed = tuple.reverse();
            assertEquals('h', reversed._1);
            assertEquals('a', reversed._8);
        }

        @Test
        public void testReverseTuple9() {
            CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple9 reversed = tuple.reverse();
            assertEquals('i', reversed._1);
            assertEquals('a', reversed._9);
        }

        // Additional tests for larger tuple sizes - contains
        @Test
        public void testContainsTuple4() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            assertTrue(tuple.contains('a'));
            assertTrue(tuple.contains('d'));
            assertFalse(tuple.contains('z'));
        }

        @Test
        public void testContainsTuple5() {
            CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertTrue(tuple.contains('e'));
            assertFalse(tuple.contains('z'));
        }

        @Test
        public void testContainsTuple6() {
            CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertTrue(tuple.contains('f'));
            assertFalse(tuple.contains('z'));
        }

        @Test
        public void testContainsTuple7() {
            CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertTrue(tuple.contains('g'));
            assertFalse(tuple.contains('z'));
        }

        @Test
        public void testContainsTuple8() {
            CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertTrue(tuple.contains('h'));
            assertFalse(tuple.contains('z'));
        }

        @Test
        public void testContainsTuple9() {
            CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertTrue(tuple.contains('i'));
            assertFalse(tuple.contains('z'));
        }

        // Test for create() with all sizes (2, 4-9)
        @Test
        public void testCreate2() {
            CharTuple2 tuple = CharTuple.copyOf(new char[] { 'a', 'b' });
            assertEquals('a', tuple._1);
            assertEquals('b', tuple._2);
        }

        @Test
        public void testCreate4() {
            CharTuple4 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd' });
            assertEquals('a', tuple._1);
            assertEquals('d', tuple._4);
        }

        @Test
        public void testCreate5() {
            CharTuple5 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e' });
            assertEquals('a', tuple._1);
            assertEquals('e', tuple._5);
        }

        @Test
        public void testCreate6() {
            CharTuple6 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' });
            assertEquals('a', tuple._1);
            assertEquals('f', tuple._6);
        }

        @Test
        public void testCreate7() {
            CharTuple7 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' });
            assertEquals('a', tuple._1);
            assertEquals('g', tuple._7);
        }

        @Test
        public void testCreate8() {
            CharTuple8 tuple = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
            assertEquals('a', tuple._1);
            assertEquals('h', tuple._8);
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');

            // Test reverse
            CharTuple4 reversed = tuple.reverse();
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
            assertEquals('b', tuple.median());
            assertEquals(394, tuple.sum()); // 97+98+99+100
            assertEquals(98.5, tuple.average(), 0.001);

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
        public void testTuple5Operations() {
            CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');

            // Test reverse
            CharTuple5 reversed = tuple.reverse();
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
            assertEquals('c', tuple.median());
            assertEquals(495, tuple.sum()); // 97+98+99+100+101
            assertEquals(99.0, tuple.average(), 0.001);

            // Test equals
            CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals(tuple, tuple2);
        }

        @Test
        public void testTuple6Operations() {
            CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');

            // Test reverse
            CharTuple6 reversed = tuple.reverse();
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
            assertEquals(99.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7Operations() {
            CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');

            // Test reverse
            CharTuple7 reversed = tuple.reverse();
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
            assertEquals(100.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple8Operations() {
            CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');

            // Test reverse
            CharTuple8 reversed = tuple.reverse();
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
            assertEquals(100.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9Operations() {
            CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');

            // Test reverse
            CharTuple9 reversed = tuple.reverse();
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
            assertEquals(101.0, tuple.average(), 0.001);
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            CharTuple2 tuple2 = CharTuple.copyOf(new char[] { 'a', 'b' });
            assertEquals('a', tuple2._1);
            assertEquals('b', tuple2._2);

            CharTuple4 tuple4 = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd' });
            assertEquals('a', tuple4._1);
            assertEquals('d', tuple4._4);

            CharTuple5 tuple5 = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e' });
            assertEquals('a', tuple5._1);
            assertEquals('e', tuple5._5);

            CharTuple6 tuple6 = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' });
            assertEquals('a', tuple6._1);
            assertEquals('f', tuple6._6);

            CharTuple7 tuple7 = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' });
            assertEquals('a', tuple7._1);
            assertEquals('g', tuple7._7);

            CharTuple8 tuple8 = CharTuple.copyOf(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
            assertEquals('a', tuple8._1);
            assertEquals('h', tuple8._8);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            CharTuple4 tuple4 = CharTuple.of('a', 'b', 'c', 'd');
            CharList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals('d', list4.get(3));

            CharTuple9 tuple9 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals('i', list9.get(8));
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Character.valueOf('d'), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            CharTuple2 tuple = CharTuple.of('x', 'y');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Character.valueOf('x'), result.get(0));
            assertEquals(Character.valueOf('y'), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            CharTuple3 tuple = CharTuple.of('x', 'y', 'z');
            List<Character> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Character.valueOf('z'), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            CharTuple4 tuple4 = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals(394, tuple4.stream().sum());

            CharTuple9 tuple9 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals(909, tuple9.stream().sum());
        }

        // ==================== CharTuple Nested Class Tests ====================

        // ============ CharTuple1 Nested Class Tests ============

        @Test
        public void testCharTuple1_arity() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCharTuple1_reverse() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            CharTuple.CharTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testCharTuple1_contains() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple1_hashCode() {
            CharTuple.CharTuple1 tuple1 = CharTuple.of('a');
            CharTuple.CharTuple1 tuple2 = CharTuple.of('a');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple1_equals() {
            CharTuple.CharTuple1 tuple1 = CharTuple.of('a');
            CharTuple.CharTuple1 tuple2 = CharTuple.of('a');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple1_toString() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple1_forEach() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testCharTuple1_min() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.min());
        }

        @Test
        public void testCharTuple1_max() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.max());
        }

        @Test
        public void testCharTuple1_median() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.median());
        }

        @Test
        public void testCharTuple1_sum() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertNotNull(tuple.sum());
        }

        @Test
        public void testCharTuple1_average() {
            CharTuple.CharTuple1 tuple = CharTuple.of('a');
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ CharTuple2 Nested Class Tests ============

        @Test
        public void testCharTuple2_arity() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testCharTuple2_reverse() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            CharTuple.CharTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testCharTuple2_contains() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple2_hashCode() {
            CharTuple.CharTuple2 tuple1 = CharTuple.of('a', 'b');
            CharTuple.CharTuple2 tuple2 = CharTuple.of('a', 'b');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple2_equals() {
            CharTuple.CharTuple2 tuple1 = CharTuple.of('a', 'b');
            CharTuple.CharTuple2 tuple2 = CharTuple.of('a', 'b');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple2_toString() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple2_forEach() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testCharTuple2_min() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.min());
        }

        @Test
        public void testCharTuple2_max() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.max());
        }

        @Test
        public void testCharTuple2_median() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.median());
        }

        @Test
        public void testCharTuple2_sum() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertNotNull(tuple.sum());
        }

        @Test
        public void testCharTuple2_average() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testCharTuple2_accept_biConsumer() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testCharTuple2_map_biFunction() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testCharTuple2_filter_biPredicate() {
            CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ CharTuple3 Nested Class Tests ============

        @Test
        public void testCharTuple3_arity() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testCharTuple3_reverse() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            CharTuple.CharTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testCharTuple3_contains() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple3_hashCode() {
            CharTuple.CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
            CharTuple.CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple3_equals() {
            CharTuple.CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
            CharTuple.CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple3_toString() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple3_forEach() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testCharTuple3_min() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.min());
        }

        @Test
        public void testCharTuple3_max() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.max());
        }

        @Test
        public void testCharTuple3_median() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.median());
        }

        @Test
        public void testCharTuple3_sum() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertNotNull(tuple.sum());
        }

        @Test
        public void testCharTuple3_average() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testCharTuple3_accept_triConsumer() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testCharTuple3_map_triFunction() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testCharTuple3_filter_triPredicate() {
            CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ CharTuple4 Nested Class Tests ============

        @Test
        public void testCharTuple4_arity() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testCharTuple4_reverse() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple.CharTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testCharTuple4_contains() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple4_hashCode() {
            CharTuple.CharTuple4 tuple1 = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple.CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple4_equals() {
            CharTuple.CharTuple4 tuple1 = CharTuple.of('a', 'b', 'c', 'd');
            CharTuple.CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple4_toString() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple4_forEach() {
            CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ CharTuple5 Nested Class Tests ============

        @Test
        public void testCharTuple5_arity() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testCharTuple5_reverse() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple.CharTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testCharTuple5_contains() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple5_hashCode() {
            CharTuple.CharTuple5 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple.CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple5_equals() {
            CharTuple.CharTuple5 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            CharTuple.CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple5_toString() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple5_forEach() {
            CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ CharTuple6 Nested Class Tests ============

        @Test
        public void testCharTuple6_arity() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testCharTuple6_reverse() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple.CharTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testCharTuple6_contains() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple6_hashCode() {
            CharTuple.CharTuple6 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple.CharTuple6 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple6_equals() {
            CharTuple.CharTuple6 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            CharTuple.CharTuple6 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple6_toString() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple6_forEach() {
            CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ CharTuple7 Nested Class Tests ============

        @Test
        public void testCharTuple7_arity() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testCharTuple7_reverse() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple.CharTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testCharTuple7_contains() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple7_hashCode() {
            CharTuple.CharTuple7 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple.CharTuple7 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple7_equals() {
            CharTuple.CharTuple7 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            CharTuple.CharTuple7 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple7_toString() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple7_forEach() {
            CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ CharTuple8 Nested Class Tests ============

        @Test
        public void testCharTuple8_arity() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testCharTuple8_reverse() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple.CharTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testCharTuple8_contains() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple8_hashCode() {
            CharTuple.CharTuple8 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple.CharTuple8 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple8_equals() {
            CharTuple.CharTuple8 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            CharTuple.CharTuple8 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple8_toString() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple8_forEach() {
            CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ CharTuple9 Nested Class Tests ============

        @Test
        public void testCharTuple9_arity() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCharTuple9_reverse() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple.CharTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testCharTuple9_contains() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertTrue(tuple.contains('a'));
        }

        @Test
        public void testCharTuple9_hashCode() {
            CharTuple.CharTuple9 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple.CharTuple9 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testCharTuple9_equals() {
            CharTuple.CharTuple9 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            CharTuple.CharTuple9 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testCharTuple9_toString() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            assertNotNull(tuple.toString());
        }

        @Test
        public void testCharTuple9_forEach() {
            CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    @Tag("2510")
    class CharTuple2510Test extends TestBase {
        @Test
        public void testOf_tuple4() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('B', tuple._2);
            assertEquals('C', tuple._3);
            assertEquals('D', tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertNotNull(tuple);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            assertNotNull(tuple);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            assertNotNull(tuple);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertNotNull(tuple);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - CharTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            CharTuple<?> tuple = CharTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_array3() {
            CharTuple3 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C' });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testCreate_array4() {
            CharTuple4 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D' });
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testCreate_array5() {
            CharTuple5 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E' });
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testCreate_array6() {
            CharTuple6 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F' });
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testCreate_array7() {
            CharTuple7 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' });
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testCreate_array8() {
            CharTuple8 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' });
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testCreate_array9() {
            CharTuple9 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
            assertEquals(9, tuple.arity());
        }
        // ============ CharTuple0 Tests ============

        @Test
        public void testTuple0_arity() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testTuple0_min_throwsException() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testTuple0_max_throwsException() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void testTuple0_median_throwsException() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void testTuple0_sum() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testTuple0_average_throwsException() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void testTuple0_reverse() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            CharTuple<?> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testTuple0_contains() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertFalse(tuple.contains('A'));
        }

        @Test
        public void testTuple0_toString() {
            CharTuple<?> tuple = CharTuple.copyOf(new char[0]);
            assertEquals("()", tuple.toString());
        }

        // ============ CharTuple1 Tests ============

        @Test
        public void testTuple1_arity() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testTuple1_sum() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals(65, tuple.sum()); // ASCII value of 'A' is 65
        }

        @Test
        public void testTuple1_average() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals(65.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple1_reverse() {
            CharTuple1 tuple = CharTuple.of('A');
            CharTuple1 reversed = tuple.reverse();
            assertEquals('A', reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void testTuple1_contains_true() {
            CharTuple1 tuple = CharTuple.of('A');
            assertTrue(tuple.contains('A'));
        }

        @Test
        public void testTuple1_contains_false() {
            CharTuple1 tuple = CharTuple.of('A');
            assertFalse(tuple.contains('B'));
        }

        @Test
        public void testTuple1_hashCode() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('A');
            CharTuple1 tuple3 = CharTuple.of('B');

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(65, tuple1.hashCode()); // ASCII value of 'A'
            assertEquals(66, tuple3.hashCode()); // ASCII value of 'B'
        }

        @Test
        public void testTuple1_equals() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('A');
            CharTuple1 tuple3 = CharTuple.of('B');

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertNotEquals(tuple1, null);
            assertNotEquals(tuple1, "not a tuple");
        }
        // ============ CharTuple2 Tests ============

        @Test
        public void testTuple2_arity() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testTuple2_sum() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(131, tuple.sum()); // 65 + 66
        }

        @Test
        public void testTuple2_average() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(65.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple2_reverse() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            CharTuple2 reversed = tuple.reverse();
            assertEquals('B', reversed._1);
            assertEquals('A', reversed._2);
        }

        @Test
        public void testTuple2_contains() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertTrue(tuple.contains('A'));
            assertTrue(tuple.contains('B'));
            assertFalse(tuple.contains('C'));
        }

        @Test
        public void testTuple2_forEach() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals('A', values.get(0));
            assertEquals('B', values.get(1));
        }

        @Test
        public void testTuple2_accept() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<String> results = new ArrayList<>();
            tuple.accept((a, b) -> results.add("" + a + b));
            assertEquals(1, results.size());
            assertEquals("AB", results.get(0));
        }

        @Test
        public void testTuple2_filter_matches() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2_filter_noMatch() {
            CharTuple2 tuple = CharTuple.of('B', 'A');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple2_equals() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            CharTuple2 tuple3 = CharTuple.of('B', 'A');

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }
        // ============ CharTuple3 Tests ============

        @Test
        public void testTuple3_arity() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testTuple3_max() {
            CharTuple3 tuple = CharTuple.of('A', 'C', 'B');
            assertEquals('C', tuple.max());
        }

        @Test
        public void testTuple3_median() {
            CharTuple3 tuple = CharTuple.of('A', 'C', 'B');
            assertEquals('B', tuple.median());
        }

        @Test
        public void testTuple3_sum() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(198, tuple.sum()); // 65 + 66 + 67
        }

        @Test
        public void testTuple3_average() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(66.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple3_reverse() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharTuple3 reversed = tuple.reverse();
            assertEquals('C', reversed._1);
            assertEquals('B', reversed._2);
            assertEquals('A', reversed._3);
        }

        @Test
        public void testTuple3_forEach() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void testTuple3_accept() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            List<String> results = new ArrayList<>();
            tuple.accept((a, b, c) -> results.add("" + a + b + c));
            assertEquals(1, results.size());
            assertEquals("ABC", results.get(0));
        }

        @Test
        public void testTuple3_filter_matches() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        public void testTuple3_filter_noMatch() {
            CharTuple3 tuple = CharTuple.of('C', 'B', 'A');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple3_equals() {
            CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
            CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
            CharTuple3 tuple3 = CharTuple.of('C', 'B', 'A');

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }
        // ============ CharTuple4-9 Basic Tests ============

        @Test
        public void testTuple4_reverse() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            CharTuple4 reversed = tuple.reverse();
            assertEquals('D', reversed._1);
            assertEquals('C', reversed._2);
            assertEquals('B', reversed._3);
            assertEquals('A', reversed._4);
        }

        @Test
        public void testTuple5_reverse() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            CharTuple5 reversed = tuple.reverse();
            assertEquals('E', reversed._1);
            assertEquals('A', reversed._5);
        }

        @Test
        public void testTuple6_reverse() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            CharTuple6 reversed = tuple.reverse();
            assertEquals('F', reversed._1);
            assertEquals('A', reversed._6);
        }

        @Test
        public void testTuple7_reverse() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            CharTuple7 reversed = tuple.reverse();
            assertEquals('G', reversed._1);
            assertEquals('A', reversed._7);
        }

        @Test
        public void testTuple8_reverse() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            CharTuple8 reversed = tuple.reverse();
            assertEquals('H', reversed._1);
            assertEquals('A', reversed._8);
        }

        @Test
        public void testTuple9_reverse() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            CharTuple9 reversed = tuple.reverse();
            assertEquals('I', reversed._1);
            assertEquals('A', reversed._9);
        }

        @Test
        public void testTuple4_contains() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertTrue(tuple.contains('A'));
            assertTrue(tuple.contains('D'));
            assertFalse(tuple.contains('E'));
        }

        @Test
        public void testTuple5_contains() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertTrue(tuple.contains('E'));
            assertFalse(tuple.contains('F'));
        }

        @Test
        public void testTuple6_contains() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            assertTrue(tuple.contains('F'));
            assertFalse(tuple.contains('G'));
        }

        @Test
        public void testTuple7_contains() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            assertTrue(tuple.contains('G'));
            assertFalse(tuple.contains('H'));
        }

        @Test
        public void testTuple8_contains() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertTrue(tuple.contains('H'));
            assertFalse(tuple.contains('I'));
        }

        @Test
        public void testTuple9_contains() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertTrue(tuple.contains('I'));
            assertFalse(tuple.contains('J'));
        }

        @Test
        public void testTuple4_forEach() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(4, values.size());
        }

        @Test
        public void testTuple5_forEach() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        public void testTuple6_forEach() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(6, values.size());
        }

        @Test
        public void testTuple7_forEach() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(7, values.size());
        }

        @Test
        public void testTuple8_forEach() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(8, values.size());
        }

        @Test
        public void testTuple9_forEach() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            List<Character> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(9, values.size());
        }

        // ============ Common Method Tests (inherited from CharTuple) ============

        @Test
        public void testToArray() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);

            // Verify it's a copy
            array[0] = 'Z';
            assertEquals('A', tuple._1);
        }

        @Test
        public void testToList() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals('A', list.get(0));
            assertEquals('B', list.get(1));
            assertEquals('C', list.get(2));
        }

        @Test
        public void testEquals_symmetry() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        public void testEquals_differentTypes() {
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            CharTuple3 tuple3 = CharTuple.of('A', 'B', 'C');
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        public void testDigitCharacters() {
            CharTuple3 tuple = CharTuple.of('1', '2', '3');
            assertEquals('1', tuple.min());
            assertEquals('3', tuple.max());
            assertEquals('2', tuple.median());
        }

        @Test
        public void testSpecialCharacters() {
            CharTuple3 tuple = CharTuple.of('!', '@', '#');
            assertTrue(tuple.contains('!'));
            assertTrue(tuple.contains('@'));
            assertTrue(tuple.contains('#'));
        }

        @Test
        public void testMinMaxMedian_largerTuple() {
            CharTuple5 tuple = CharTuple.of('E', 'B', 'D', 'A', 'C');
            assertEquals('A', tuple.min());
            assertEquals('E', tuple.max());
            assertEquals('C', tuple.median());
        }

        @Test
        public void testLowercaseCharacters() {
            CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
            assertEquals('a', tuple.min());
            assertEquals('c', tuple.max());
            assertEquals(294, tuple.sum()); // 97 + 98 + 99
        }

        @Test
        public void testMixedCaseCharacters() {
            CharTuple2 tuple = CharTuple.of('A', 'a');
            assertEquals('A', tuple.min()); // 'A' = 65, 'a' = 97
            assertEquals('a', tuple.max());
        }
    }

    @Nested
    @Tag("2511")
    class CharTuple2511Test extends TestBase {
        // ============ Create from Array Tests ============

        @Test
        public void testCreate_empty() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_single() {
            CharTuple1 tuple = CharTuple.copyOf(new char[] { 'X' });
            assertNotNull(tuple);
            assertEquals('X', tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate_two() {
            CharTuple2 tuple = CharTuple.copyOf(new char[] { 'X', 'Y' });
            assertNotNull(tuple);
            assertEquals('X', tuple._1);
            assertEquals('Y', tuple._2);
        }

        @Test
        public void testCreate_nine() {
            CharTuple9 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('I', tuple._9);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCreate_tooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' });
            });
        }

        @Test
        public void testMin_single() {
            CharTuple1 tuple = CharTuple.of('M');
            assertEquals('M', tuple.min());
        }

        @Test
        public void testMin_multiple() {
            CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
            assertEquals('A', tuple.min());
        }

        @Test
        public void testMax_single() {
            CharTuple1 tuple = CharTuple.of('M');
            assertEquals('M', tuple.max());
        }

        @Test
        public void testMax_multiple() {
            CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
            assertEquals('Z', tuple.max());
        }

        @Test
        public void testMedian_single() {
            CharTuple1 tuple = CharTuple.of('M');
            assertEquals('M', tuple.median());
        }

        @Test
        public void testMedian_odd() {
            CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
            assertEquals('M', tuple.median());
        }

        @Test
        public void testMedian_even() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertEquals('B', tuple.median()); // Lower middle for even length
        }

        @Test
        public void testAverage_single() {
            CharTuple1 tuple = CharTuple.of('A'); // 65
            assertEquals(65.0, tuple.average());
        }

        @Test
        public void testAverage_multiple() {
            CharTuple2 tuple = CharTuple.of('A', 'C'); // (65 + 67) / 2 = 66.0
            assertEquals(66.0, tuple.average());
        }

        // ============ Reverse Tests ============

        @Test
        public void testReverse_empty() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            CharTuple0 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverse_twoElements() {
            CharTuple2 tuple = CharTuple.of('X', 'Y');
            CharTuple2 reversed = tuple.reverse();
            assertEquals('Y', reversed._1);
            assertEquals('X', reversed._2);
        }

        @Test
        public void testContains_multiple_found() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertTrue(tuple.contains('A'));
            assertTrue(tuple.contains('B'));
            assertTrue(tuple.contains('C'));
        }

        @Test
        public void testContains_multiple_notfound() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertFalse(tuple.contains('Z'));
        }

        // ============ Functional Methods - CharTuple2 ============

        @Test
        public void testAccept_tuple2() {
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
        public void testFilter_tuple2_fails() {
            CharTuple2 tuple = CharTuple.of('Z', 'A');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        // ============ Functional Methods - CharTuple3 ============

        @Test
        public void testAccept_tuple3() {
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
        public void testFilter_tuple3_passes() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }
        // ============ ForEach Tests ============

        @Test
        public void testForEach_empty() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(c -> count.incrementAndGet());
            assertEquals(0, count.get());
        }

        @Test
        public void testForEach_tuple2() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(2, visited.size());
            assertEquals('A', visited.get(0));
            assertEquals('B', visited.get(1));
        }

        @Test
        public void testForEach_tuple3() {
            CharTuple3 tuple = CharTuple.of('X', 'Y', 'Z');
            List<Character> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(3, visited.size());
            assertEquals('X', visited.get(0));
            assertEquals('Y', visited.get(1));
            assertEquals('Z', visited.get(2));
        }

        @Test
        public void testToArray_independence() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array1 = tuple.toArray();
            char[] array2 = tuple.toArray();
            assertNotSame(array1, array2); // Should be independent copies
            assertArrayEquals(array1, array2);
        }

        // ============ ToList Tests ============

        @Test
        public void testToList_empty() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        public void testToList_single() {
            CharTuple1 tuple = CharTuple.of('A');
            CharList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals('A', list.get(0));
        }

        @Test
        public void testToList_multiple() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals('A', list.get(0));
            assertEquals('B', list.get(1));
            assertEquals('C', list.get(2));
        }

        // ============ Stream Tests ============

        @Test
        public void testStream_empty() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            int sum = tuple.stream().sum();
            assertEquals(0, sum);
        }

        @Test
        public void testStream_single() {
            CharTuple1 tuple = CharTuple.of('A'); // 65
            int sum = tuple.stream().sum();
            assertEquals(65, sum);
        }

        @Test
        public void testStream_multiple() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C'); // 65 + 66 + 67 = 198
            int sum = tuple.stream().sum();
            assertEquals(198, sum);
        }

        // ============ Equality and HashCode Tests ============

        @Test
        public void testEquals_sameInstance() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEquals_sameValues() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentValues() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('X', 'Y');
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentTypes() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_null() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertNotEquals(tuple, null);
        }

        @Test
        public void testEquals_otherObject() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertNotEquals(tuple, "AB");
        }

        @Test
        public void testHashCode_differentValues() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('X', 'Y');
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_empty() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertNotNull(tuple.hashCode());
        }

        @Test
        public void testArity_4() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testArity_5() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testArity_6() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testArity_7() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testArity_8() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testArity_9() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertEquals(9, tuple.arity());
        }

        // ============ Field Accessors Tests ============

        @Test
        public void testFieldAccessors_tuple5() {
            CharTuple5 tuple = CharTuple.of('1', '2', '3', '4', '5');
            assertEquals('1', tuple._1);
            assertEquals('2', tuple._2);
            assertEquals('3', tuple._3);
            assertEquals('4', tuple._4);
            assertEquals('5', tuple._5);
        }

        @Test
        public void testFieldAccessors_tuple9() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertEquals('A', tuple._1);
            assertEquals('B', tuple._2);
            assertEquals('C', tuple._3);
            assertEquals('D', tuple._4);
            assertEquals('E', tuple._5);
            assertEquals('F', tuple._6);
            assertEquals('G', tuple._7);
            assertEquals('H', tuple._8);
            assertEquals('I', tuple._9);
        }

        // ============ Complex Statistics Tests ============

        @Test
        public void testStatistics_allOperations() {
            CharTuple5 tuple = CharTuple.of('A', 'E', 'C', 'B', 'D');
            assertEquals('A', tuple.min());
            assertEquals('E', tuple.max());
            assertEquals('C', tuple.median());
            assertEquals(65 + 69 + 67 + 66 + 68, tuple.sum()); // A(65)+E(69)+C(67)+B(66)+D(68)
            assertTrue(tuple.average() > 0);
        }

        @Test
        public void testContains_allElements() {
            CharTuple5 tuple = CharTuple.of('X', 'Y', 'Z', 'W', 'V');
            assertTrue(tuple.contains('X'));
            assertTrue(tuple.contains('Y'));
            assertTrue(tuple.contains('Z'));
            assertTrue(tuple.contains('W'));
            assertTrue(tuple.contains('V'));
            assertFalse(tuple.contains('A'));
        }

        // ============ Edge Cases Tests ============

        @Test
        public void testSingleElement_allOperations() {
            CharTuple1 tuple = CharTuple.of('X');
            assertEquals('X', tuple.min());
            assertEquals('X', tuple.max());
            assertEquals('X', tuple.median());
            assertEquals('X', tuple.average()); // 88.0
            assertEquals(88, tuple.sum());
            assertTrue(tuple.contains('X'));
            assertFalse(tuple.contains('Y'));
            CharTuple1 reversed = tuple.reverse();
            assertEquals('X', reversed._1);
        }

        @Test
        public void testLargeCharValues() {
            CharTuple3 tuple = CharTuple.of('\u00FF', '\u0080', '\u0100');
            assertNotNull(tuple);
            assertTrue(tuple.contains('\u0080'));
        }

        @Test
        public void testSpecialChars() {
            CharTuple3 tuple = CharTuple.of('\n', '\t', ' ');
            assertEquals(3, tuple.arity());
            assertTrue(tuple.contains('\n'));
            assertTrue(tuple.contains('\t'));
            assertTrue(tuple.contains(' '));
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for CharTuple and its inner classes (CharTuple0-9).
     * Tests cover all public methods including factory methods, statistical methods,
     * utility methods, functional methods, equality/hashCode, and stream operations.
     */
    @Tag("2512")
    class CharTuple2512Test extends TestBase {

        // ============ Factory Method Tests - CharTuple.of() ============

        @Test
        public void test_of_tuple1() {
            CharTuple1 tuple = CharTuple.of('A');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_of_tuple2() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('B', tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_of_tuple3() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('B', tuple._2);
            assertEquals('C', tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_of_tuple4() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('D', tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_of_tuple5() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('E', tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_of_tuple6() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('F', tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_of_tuple7() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('G', tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void test_of_tuple8() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('H', tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void test_of_tuple9() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertNotNull(tuple);
            assertEquals('A', tuple._1);
            assertEquals('I', tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - CharTuple.copyOf() ============

        @Test
        public void test_create_nullArray() {
            CharTuple0 tuple = CharTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_arraySize1() {
            CharTuple1 tuple = CharTuple.copyOf(new char[] { 'A' });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals('A', tuple._1);
        }

        @Test
        public void test_create_arraySize2() {
            CharTuple2 tuple = CharTuple.copyOf(new char[] { 'A', 'B' });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals('A', tuple._1);
            assertEquals('B', tuple._2);
        }

        @Test
        public void test_create_arraySize9() {
            CharTuple9 tuple = CharTuple.copyOf(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals('A', tuple._1);
            assertEquals('I', tuple._9);
        }

        @Test
        public void test_create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                CharTuple.copyOf(new char[10]);
            });
        }

        // ============ Tuple0 Tests ============

        @Test
        public void test_tuple0_arity() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_tuple0_min_throwsException() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_tuple0_max_throwsException() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_tuple0_median_throwsException() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_tuple0_sum() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_tuple0_average_throwsException() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_tuple0_reverse() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            CharTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_tuple0_contains() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertFalse(tuple.contains('A'));
        }

        @Test
        public void test_tuple0_toArray() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            char[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void test_tuple0_toList() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            CharList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        public void test_tuple0_forEach() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            List<Character> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        public void test_tuple0_stream() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        public void test_tuple0_toString() {
            CharTuple0 tuple = CharTuple.copyOf(new char[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void test_tuple1_min() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.min());
        }

        @Test
        public void test_tuple1_max() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.max());
        }

        @Test
        public void test_tuple1_median() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals('A', tuple.median());
        }

        @Test
        public void test_tuple1_sum() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals((int) 'A', tuple.sum());
        }

        @Test
        public void test_tuple1_average() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals((double) 'A', tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple1_reverse() {
            CharTuple1 tuple = CharTuple.of('A');
            CharTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals('A', reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void test_tuple1_toArray() {
            CharTuple1 tuple = CharTuple.of('A');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A' }, array);
        }

        @Test
        public void test_tuple1_toList() {
            CharTuple1 tuple = CharTuple.of('A');
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals('A', list.get(0));
        }

        @Test
        public void test_tuple1_forEach() {
            CharTuple1 tuple = CharTuple.of('A');
            List<Character> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals('A', collected.get(0));
        }

        @Test
        public void test_tuple1_stream() {
            CharTuple1 tuple = CharTuple.of('A');
            int sum = tuple.stream().sum();
            assertEquals((int) 'A', sum);
        }

        @Test
        public void test_tuple1_hashCode() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('A');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple1_equals_same() {
            CharTuple1 tuple = CharTuple.of('A');
            assertTrue(tuple.equals(tuple));
        }

        @Test
        public void test_tuple1_equals_equal() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('A');
            assertTrue(tuple1.equals(tuple2));
            assertTrue(tuple2.equals(tuple1));
        }

        @Test
        public void test_tuple1_equals_notEqual() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple1 tuple2 = CharTuple.of('B');
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_tuple1_equals_null() {
            CharTuple1 tuple = CharTuple.of('A');
            assertFalse(tuple.equals(null));
        }

        @Test
        public void test_tuple1_equals_differentClass() {
            CharTuple1 tuple = CharTuple.of('A');
            assertFalse(tuple.equals("not a tuple"));
        }

        @Test
        public void test_tuple1_toString() {
            CharTuple1 tuple = CharTuple.of('A');
            assertEquals("(A)", tuple.toString());
        }

        @Test
        public void test_tuple2_min() {
            CharTuple2 tuple = CharTuple.of('B', 'A');
            assertEquals('A', tuple.min());
        }

        @Test
        public void test_tuple2_max() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals('B', tuple.max());
        }

        @Test
        public void test_tuple2_median() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals('A', tuple.median());
        }

        @Test
        public void test_tuple2_sum() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals((int) 'A' + (int) 'B', tuple.sum());
        }

        @Test
        public void test_tuple2_average() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals(((int) 'A' + (int) 'B') / 2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple2_reverse() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            CharTuple2 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals('B', reversed._1);
            assertEquals('A', reversed._2);
        }

        @Test
        public void test_tuple2_contains_found() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertTrue(tuple.contains('A'));
            assertTrue(tuple.contains('B'));
        }

        @Test
        public void test_tuple2_contains_notFound() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertFalse(tuple.contains('C'));
        }

        @Test
        public void test_tuple2_toArray() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A', 'B' }, array);
        }

        @Test
        public void test_tuple2_toList() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            CharList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        public void test_tuple2_forEach() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            List<Character> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals('A', collected.get(0));
            assertEquals('B', collected.get(1));
        }

        @Test
        public void test_tuple2_stream() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            long count = tuple.stream().count();
            assertEquals(2, count);
        }

        @Test
        public void test_tuple2_accept() {
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
        public void test_tuple2_map() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            String result = tuple.map((a, b) -> "" + a + b);
            assertEquals("AB", result);
        }

        @Test
        public void test_tuple2_filter_noMatch() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            Optional<CharTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple2_hashCode() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple2_equals() {
            CharTuple2 tuple1 = CharTuple.of('A', 'B');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            CharTuple2 tuple3 = CharTuple.of('B', 'A');

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(null));
        }

        @Test
        public void test_tuple2_toString() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertEquals("(A, B)", tuple.toString());
        }

        @Test
        public void test_tuple3_min() {
            CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
            assertEquals('A', tuple.min());
        }

        @Test
        public void test_tuple3_max() {
            CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
            assertEquals('C', tuple.max());
        }

        @Test
        public void test_tuple3_median() {
            CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
            assertEquals('B', tuple.median());
        }

        @Test
        public void test_tuple3_sum() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals((int) 'A' + (int) 'B' + (int) 'C', tuple.sum());
        }

        @Test
        public void test_tuple3_average() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals(((int) 'A' + (int) 'B' + (int) 'C') / 3.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple3_reverse() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals('C', reversed._1);
            assertEquals('B', reversed._2);
            assertEquals('A', reversed._3);
        }

        @Test
        public void test_tuple3_contains() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertTrue(tuple.contains('A'));
            assertTrue(tuple.contains('B'));
            assertTrue(tuple.contains('C'));
            assertFalse(tuple.contains('D'));
        }

        @Test
        public void test_tuple3_toArray() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array = tuple.toArray();
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);
        }

        @Test
        public void test_tuple3_accept() {
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
        public void test_tuple3_map() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            String result = tuple.map((a, b, c) -> "" + a + b + c);
            assertEquals("ABC", result);
        }

        @Test
        public void test_tuple3_filter_noMatch() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            Optional<CharTuple3> result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple3_hashCode() {
            CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
            CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple3_equals() {
            CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
            CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
            CharTuple3 tuple3 = CharTuple.of('A', 'B', 'D');

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
        }

        @Test
        public void test_tuple3_toString() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            assertEquals("(A, B, C)", tuple.toString());
        }

        // ============ Tuple4-9 Basic Tests ============

        @Test
        public void test_tuple4_basic() {
            CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            assertEquals(4, tuple.arity());
            assertEquals('A', tuple.min());
            assertEquals('D', tuple.max());
            assertTrue(tuple.contains('C'));
            CharTuple4 reversed = tuple.reverse();
            assertEquals('D', reversed._1);
            assertEquals('A', reversed._4);
        }

        @Test
        public void test_tuple5_basic() {
            CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertEquals(5, tuple.arity());
            assertEquals('A', tuple.min());
            assertEquals('E', tuple.max());
            assertEquals('C', tuple.median());
            assertTrue(tuple.contains('C'));
        }

        @Test
        public void test_tuple6_basic() {
            CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
            assertEquals(6, tuple.arity());
            assertEquals('A', tuple.min());
            assertEquals('F', tuple.max());
        }

        @Test
        public void test_tuple7_basic() {
            CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
            assertEquals(7, tuple.arity());
            assertEquals('D', tuple.median());
            assertEquals(7, tuple.toArray().length);
        }

        @Test
        public void test_tuple8_basic() {
            CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
        }

        @Test
        public void test_tuple9_basic() {
            CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
        }

        // ============ Edge Cases and Additional Coverage ============

        @Test
        public void test_numericChars() {
            CharTuple3 tuple = CharTuple.of('0', '1', '2');
            assertEquals('0', tuple.min());
            assertEquals('2', tuple.max());
            assertEquals('1', tuple.median());
        }

        @Test
        public void test_specialChars() {
            CharTuple3 tuple = CharTuple.of('!', '@', '#');
            assertEquals('!', tuple.min());
            assertEquals('@', tuple.max());
        }

        @Test
        public void test_mixedCase() {
            CharTuple2 tuple = CharTuple.of('a', 'A');
            assertEquals('A', tuple.min()); // uppercase has lower ASCII value
            assertEquals('a', tuple.max());
        }

        @Test
        public void test_toArray_independence() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            char[] array = tuple.toArray();
            array[0] = 'Z';
            assertEquals('A', tuple._1); // Tuple should be unaffected
        }

        @Test
        public void test_toList_independence() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            CharList list = tuple.toList();
            list.set(0, 'Z');
            assertEquals('A', tuple._1); // Tuple should be unaffected
        }

        @Test
        public void test_stream_operations() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            int sum = tuple.stream().sum();
            assertEquals((int) 'A' + (int) 'B' + (int) 'C', sum);
        }

        @Test
        public void test_forEach_withException() {
            CharTuple2 tuple = CharTuple.of('A', 'B');
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(c -> {
                    if (c == 'B')
                        throw new RuntimeException("test exception");
                });
            });
        }

        @Test
        public void test_equals_differentArity() {
            CharTuple1 tuple1 = CharTuple.of('A');
            CharTuple2 tuple2 = CharTuple.of('A', 'B');
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_hashCode_consistency() {
            CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_create_allSizes() {
            for (int i = 0; i <= 9; i++) {
                char[] array = new char[i];
                CharTuple<?> tuple = CharTuple.copyOf(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        public void test_whitespaceChars() {
            CharTuple2 tuple = CharTuple.of(' ', '\t');
            assertTrue(tuple.contains(' '));
            assertTrue(tuple.contains('\t'));
            assertNotNull(tuple.toString());
        }
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_CharTuple extends TestBase {
        // ===== CharTuple Javadoc examples =====

        @Test
        public void testCharTupleOf1() {
            // CharTuple.CharTuple1 tuple = CharTuple.of('A');
            // char value = tuple._1;  // 'A'
            CharTuple.CharTuple1 t = CharTuple.of('A');
            assertEquals('A', t._1);
        }

        @Test
        public void testCharTupleOf2() {
            // CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
            // char first = tuple._1;  // 'A'
            // char second = tuple._2;  // 'B'
            CharTuple.CharTuple2 t = CharTuple.of('A', 'B');
            assertEquals('A', t._1);
            assertEquals('B', t._2);
        }

        @Test
        public void testCharTupleOf3() {
            // CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
            // char third = tuple._3;  // 'C'
            CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
            assertEquals('C', t._3);
        }

        @Test
        public void testCharTupleOf4() {
            // CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
            // char fourth = tuple._4;  // 'D'
            CharTuple.CharTuple4 t = CharTuple.of('A', 'B', 'C', 'D');
            assertEquals('D', t._4);
        }

        @Test
        public void testCharTupleClassLevelExamples() {
            // char min = triple.min();   // 'A'
            // char max = triple.max();   // 'C'
            // double avg = triple.average();   // 66.0 (average of ASCII values)
            CharTuple.CharTuple3 triple = CharTuple.of('A', 'B', 'C');
            assertEquals('A', triple.min());
            assertEquals('C', triple.max());
            assertEquals(66.0, triple.average(), 0.001);
        }

        @Test
        public void testCharTupleOf5Median() {
            // CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
            // char median = tuple.median();   // 'C'
            CharTuple.CharTuple5 t = CharTuple.of('A', 'B', 'C', 'D', 'E');
            assertEquals('C', t.median());
        }

        @Test
        public void testCharTupleOf8Contains() {
            // CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            // boolean hasX = tuple.contains('X');   // false
            CharTuple.CharTuple8 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
            assertFalse(t.contains('X'));
        }

        @Test
        public void testCharTupleOf9ToArray() {
            // CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            // char[] array = tuple.toArray();   // ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
            CharTuple.CharTuple9 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
            assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' }, t.toArray());
        }

        @Test
        public void testCharTupleReverse() {
            // CharTuple.CharTuple7 tuple = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
            // CharTuple.CharTuple7 reversed = tuple.reverse();   // ('S', 'Y', 'A', 'D', 'N', 'O', 'M')
            CharTuple.CharTuple7 t = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
            CharTuple.CharTuple7 reversed = t.reverse();
            assertEquals('S', reversed._1);
            assertEquals('Y', reversed._2);
            assertEquals('A', reversed._3);
            assertEquals('D', reversed._4);
            assertEquals('N', reversed._5);
            assertEquals('O', reversed._6);
            assertEquals('M', reversed._7);
        }
    }

}
