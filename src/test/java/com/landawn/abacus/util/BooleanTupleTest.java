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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
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

class BooleanTupleTest extends TestBase {

    @Test
    public void testOf1() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(1, tuple.arity());
        assertTrue(tuple._1);
    }

    @Test
    public void testOf2() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(2, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
    }

    @Test
    public void testOf3() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(3, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
    }

    @Test
    public void testOf4() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
    }

    @Test
    public void testOf5() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
        assertTrue(tuple._5);
    }

    @Test
    public void testOf6() {
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
    public void testOf7() {
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
    public void testOf8() {
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
    public void testOf9() {
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
    public void testEquals() {
        BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testHashCode() {
        BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testToString() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        String result = tuple.toString();
        assertTrue(result.contains("true"));
        assertTrue(result.contains("false"));
    }

    @Test
    public void testReverse() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
        BooleanTuple.BooleanTuple3 reversed = tuple.reverse();

        assertFalse(reversed._1);
        assertFalse(reversed._2);
        assertTrue(reversed._3);
    }

    @Test
    public void testContains() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);

        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testToArray() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();

        assertEquals(3, array.length);
        assertTrue(array[0]);
        assertFalse(array[1]);
        assertTrue(array[2]);
    }

    @Test
    public void testStream() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        long trueCount = tuple.stream().filter(b -> b).count();

        assertEquals(2, trueCount);
    }

    // Additional comprehensive tests for missing functionality

    @Test
    public void testHighArityTupleEqualsAndHashCode() {
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

    @Test
    public void testHighArityTupleToString() {
        BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        String str4 = tuple4.toString();
        assertTrue(str4.contains("true"));
        assertTrue(str4.contains("false"));

        BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);
        String str6 = tuple6.toString();
        assertTrue(str6.contains("true"));
        assertTrue(str6.contains("false"));
    }

    @Test
    public void testHighArityTupleReverse() {
        BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 reversed4 = tuple4.reverse();

        assertEquals(false, reversed4._1);
        assertEquals(true, reversed4._2);
        assertEquals(false, reversed4._3);
        assertEquals(true, reversed4._4);

        BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 reversed5 = tuple5.reverse();

        assertEquals(true, reversed5._1);
        assertEquals(false, reversed5._2);
        assertEquals(true, reversed5._3);
        assertEquals(false, reversed5._4);
        assertEquals(true, reversed5._5);
    }

    @Test
    public void testHighArityTupleContains() {
        BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);

        assertTrue(tuple6.contains(true));
        assertTrue(tuple6.contains(false));

        BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
        assertTrue(allTrue.contains(true));
        assertFalse(allTrue.contains(false));
    }

    @Test
    public void testCompleteHighArityTuples() {
        // Test all tuple arities 4-9 for basic functionality
        BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple4.arity());
        assertEquals(4, tuple4.toArray().length);

        BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple5.arity());
        assertEquals(5, tuple5.toArray().length);

        BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple6.arity());
        assertEquals(6, tuple6.toArray().length);

        BooleanTuple.BooleanTuple7 tuple7 = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple7.arity());
        assertEquals(7, tuple7.toArray().length);

        BooleanTuple.BooleanTuple8 tuple8 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple8.arity());
        assertEquals(8, tuple8.toArray().length);

        BooleanTuple.BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple9.arity());
        assertEquals(9, tuple9.toArray().length);
    }

    @Test
    public void testEqualsDifferentTupleTypes() {
        BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);

        assertNotEquals(tuple2, tuple3);
        assertNotEquals(tuple2, null);
        assertNotEquals(tuple2, "not a tuple");
    }

    @Test
    public void testSelfEquality() {
        // Test that all tuple types equal themselves
        BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple2, tuple2);
        assertEquals(tuple3, tuple3);
    }

    @Test
    public void testStreamOperationsOnHighArityTuples() {
        BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, true, false);

        long trueCount = tuple5.stream().filter(b -> b).count();
        assertEquals(3, trueCount);

        long falseCount = tuple5.stream().filter(b -> !b).count();
        assertEquals(2, falseCount);
    }

    @Nested
    /**
     * Comprehensive test suite for BooleanTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class BooleanTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(true, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            BooleanTuple1 tuple = BooleanTuple.copyOf(new boolean[] { true });
            assertEquals(true, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            BooleanTuple3 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true });
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
        }

        @Test
        public void testCreate9() {
            BooleanTuple9 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._9);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false, true, false });
            });
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            BooleanTuple<BooleanTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple1 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
        }

        @Test
        public void testReverseTuple2() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple2 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        public void testReverseTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple3 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            assertFalse(tuple.contains(true));
        }

        @Test
        public void testContainsTuple1True() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testContainsTuple1False() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertFalse(tuple.contains(false));
        }

        @Test
        public void testContainsTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertTrue(tuple._1);
            assertFalse(tuple._2);
            assertTrue(tuple._3);
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            boolean[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true }, array);
        }

        @Test
        public void testToArrayTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false, true }, array);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            array[0] = true;
            assertEquals(true, tuple._1);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            BooleanList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(true, list.get(0));
        }

        @Test
        public void testToListTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
            assertEquals(true, list.get(2));
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
            assertEquals(Boolean.valueOf(false), result.get(1));
            assertEquals(Boolean.valueOf(true), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            Stream<Boolean> stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            Stream<Boolean> stream = tuple.stream();
            assertEquals(1, stream.count());
        }

        @Test
        public void testStreamTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Stream<Boolean> stream = tuple.stream();
            assertEquals(3, stream.count());
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple2() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple3() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            BooleanTuple1 tuple3 = BooleanTuple.of(false);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple3 = BooleanTuple.of(true, true);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple3 = BooleanTuple.of(true, false, false);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            BooleanTuple<BooleanTuple0> tuple = BooleanTuple.copyOf(new boolean[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            String str = tuple.toString();
            assertTrue(str.contains("true"));
        }

        @Test
        public void testToStringTuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            String str = tuple.toString();
            assertTrue(str.contains("true"));
            assertTrue(str.contains("false"));
            assertTrue(str.contains("true"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
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

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean result = tuple.map((a, b) -> a && b);
            assertEquals(false, result);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            var result = tuple.filter((a, b) -> a || b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            var result = tuple.filter((a, b) -> a && b);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
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

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean result = tuple.map((a, b, c) -> a || b || c);
            assertEquals(true, result);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.filter((a, b, c) -> a || b || c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            var result = tuple.filter((a, b, c) -> a && b && c);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, BooleanTuple.copyOf(new boolean[0]).arity());
            assertEquals(1, BooleanTuple.of(true).arity());
            assertEquals(2, BooleanTuple.of(true, false).arity());
            assertEquals(3, BooleanTuple.of(true, false, true).arity());
            assertEquals(4, BooleanTuple.of(true, false, true, false).arity());
            assertEquals(5, BooleanTuple.of(true, false, true, false, true).arity());
            assertEquals(6, BooleanTuple.of(true, false, true, false, true, false).arity());
            assertEquals(7, BooleanTuple.of(true, false, true, false, true, false, true).arity());
            assertEquals(8, BooleanTuple.of(true, false, true, false, true, false, true, false).arity());
            assertEquals(9, BooleanTuple.of(true, false, true, false, true, false, true, false, true).arity());
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);

            // Test reverse
            BooleanTuple4 reversed = tuple.reverse();
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
        public void testTuple5Operations() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);

            // Test reverse
            BooleanTuple5 reversed = tuple.reverse();
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
        public void testTuple6Operations() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);

            // Test reverse
            BooleanTuple6 reversed = tuple.reverse();
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
        public void testTuple7Operations() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);

            // Test reverse
            BooleanTuple7 reversed = tuple.reverse();
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
        public void testTuple8Operations() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);

            // Test reverse
            BooleanTuple8 reversed = tuple.reverse();
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
        public void testTuple9Operations() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);

            // Test reverse
            BooleanTuple9 reversed = tuple.reverse();
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

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            BooleanTuple2 tuple2 = BooleanTuple.copyOf(new boolean[] { true, false });
            assertEquals(true, tuple2._1);
            assertEquals(false, tuple2._2);

            BooleanTuple4 tuple4 = BooleanTuple.copyOf(new boolean[] { true, false, true, false });
            assertEquals(true, tuple4._1);
            assertEquals(false, tuple4._4);

            BooleanTuple5 tuple5 = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true });
            assertEquals(true, tuple5._1);
            assertEquals(true, tuple5._5);

            BooleanTuple6 tuple6 = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false });
            assertEquals(true, tuple6._1);
            assertEquals(false, tuple6._6);

            BooleanTuple7 tuple7 = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true });
            assertEquals(true, tuple7._1);
            assertEquals(true, tuple7._7);

            BooleanTuple8 tuple8 = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false });
            assertEquals(true, tuple8._1);
            assertEquals(false, tuple8._8);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            BooleanList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(false, list4.get(3));

            BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(true, list9.get(8));
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Boolean.valueOf(false), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Boolean.valueOf(true), result.get(0));
            assertEquals(Boolean.valueOf(false), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Boolean.valueOf(true), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple4.stream().count());

            BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple9.stream().count());
        }

        // ==================== BooleanTuple Nested Class Tests ====================

        // ============ BooleanTuple1 Nested Class Tests ============

        @Test
        public void testBooleanTuple1_arity() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testBooleanTuple1_reverse() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple.BooleanTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testBooleanTuple1_contains() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple1_hashCode() {
            BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple.BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple1_equals() {
            BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple.BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple1_toString() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple1_forEach() {
            BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        // ============ BooleanTuple2 Nested Class Tests ============

        @Test
        public void testBooleanTuple2_arity() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testBooleanTuple2_reverse() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple.BooleanTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testBooleanTuple2_contains() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple2_hashCode() {
            BooleanTuple.BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple2_equals() {
            BooleanTuple.BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple2_toString() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple2_forEach() {
            BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testBooleanTuple2_accept_biConsumer() {
            {
                BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
                List<Integer> count = new ArrayList<>();
                tuple.accept((a, b) -> count.add(1));
                assertEquals(1, count.size());
            }
        }

        @Test
        public void testBooleanTuple2_map_biFunction() {
            {
                BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
                String result = tuple.map((a, b) -> "test");
                assertNotNull(result);
            }
        }

        @Test
        public void testBooleanTuple2_filter_biPredicate() {
            {
                BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
                assertTrue(tuple.filter((a, b) -> true).isPresent());
                assertFalse(tuple.filter((a, b) -> false).isPresent());
            }
        }

        // ============ BooleanTuple3 Nested Class Tests ============

        @Test
        public void testBooleanTuple3_arity() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testBooleanTuple3_reverse() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testBooleanTuple3_contains() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple3_hashCode() {
            BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple3_equals() {
            BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple3_toString() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple3_forEach() {
            BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testBooleanTuple3_accept_triConsumer() {
            {
                BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
                List<Integer> count = new ArrayList<>();
                tuple.accept((a, b, c) -> count.add(1));
                assertEquals(1, count.size());
            }
        }

        @Test
        public void testBooleanTuple3_map_triFunction() {
            {
                BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
                String result = tuple.map((a, b, c) -> "test");
                assertNotNull(result);
            }
        }

        @Test
        public void testBooleanTuple3_filter_triPredicate() {
            {
                BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
                assertTrue(tuple.filter((a, b, c) -> true).isPresent());
                assertFalse(tuple.filter((a, b, c) -> false).isPresent());
            }
        }

        // ============ BooleanTuple4 Nested Class Tests ============

        @Test
        public void testBooleanTuple4_arity() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testBooleanTuple4_reverse() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testBooleanTuple4_contains() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple4_hashCode() {
            BooleanTuple.BooleanTuple4 tuple1 = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple4_equals() {
            BooleanTuple.BooleanTuple4 tuple1 = BooleanTuple.of(true, false, true, false);
            BooleanTuple.BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple4_toString() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple4_forEach() {
            BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ BooleanTuple5 Nested Class Tests ============

        @Test
        public void testBooleanTuple5_arity() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testBooleanTuple5_reverse() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testBooleanTuple5_contains() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple5_hashCode() {
            BooleanTuple.BooleanTuple5 tuple1 = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple5_equals() {
            BooleanTuple.BooleanTuple5 tuple1 = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple.BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple5_toString() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple5_forEach() {
            BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ BooleanTuple6 Nested Class Tests ============

        @Test
        public void testBooleanTuple6_arity() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testBooleanTuple6_reverse() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple.BooleanTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testBooleanTuple6_contains() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple6_hashCode() {
            BooleanTuple.BooleanTuple6 tuple1 = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple.BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple6_equals() {
            BooleanTuple.BooleanTuple6 tuple1 = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple.BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple6_toString() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple6_forEach() {
            BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ BooleanTuple7 Nested Class Tests ============

        @Test
        public void testBooleanTuple7_arity() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testBooleanTuple7_reverse() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testBooleanTuple7_contains() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple7_hashCode() {
            BooleanTuple.BooleanTuple7 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple7_equals() {
            BooleanTuple.BooleanTuple7 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple7_toString() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple7_forEach() {
            BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ BooleanTuple8 Nested Class Tests ============

        @Test
        public void testBooleanTuple8_arity() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testBooleanTuple8_reverse() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple.BooleanTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testBooleanTuple8_contains() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple8_hashCode() {
            BooleanTuple.BooleanTuple8 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple.BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple8_equals() {
            BooleanTuple.BooleanTuple8 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple.BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple8_toString() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple8_forEach() {
            BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ BooleanTuple9 Nested Class Tests ============

        @Test
        public void testBooleanTuple9_arity() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testBooleanTuple9_reverse() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testBooleanTuple9_contains() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testBooleanTuple9_hashCode() {
            BooleanTuple.BooleanTuple9 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testBooleanTuple9_equals() {
            BooleanTuple.BooleanTuple9 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple.BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testBooleanTuple9_toString() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testBooleanTuple9_forEach() {
            BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    @Tag("2510")
    class BooleanTuple2510Test extends TestBase {

        // ============ Factory Method Tests - BooleanTuple.of() ============

        @Test
        public void testOf_tuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(false, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(false, tuple._8);
            assertEquals(true, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - BooleanTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            BooleanTuple<?> tuple = BooleanTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            BooleanTuple<?> tuple = BooleanTuple.copyOf(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_array1() {
            BooleanTuple1 tuple = BooleanTuple.copyOf(new boolean[] { true });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(true, tuple._1);
        }

        @Test
        public void testCreate_array2() {
            BooleanTuple2 tuple = BooleanTuple.copyOf(new boolean[] { true, false });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
        }

        @Test
        public void testCreate_array3() {
            BooleanTuple3 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
        }

        @Test
        public void testCreate_array4() {
            BooleanTuple4 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false });
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testCreate_array5() {
            BooleanTuple5 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true });
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testCreate_array6() {
            BooleanTuple6 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false });
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testCreate_array7() {
            BooleanTuple7 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true });
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testCreate_array8() {
            BooleanTuple8 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false });
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testCreate_array9() {
            BooleanTuple9 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCreate_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                BooleanTuple.copyOf(new boolean[10]);
            });
        }

        // ============ BooleanTuple1 Tests ============

        @Test
        public void testTuple1_arity() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testTuple1_reverse() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple1 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void testTuple1_contains_true() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testTuple1_contains_false() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertFalse(tuple.contains(false));
        }

        @Test
        public void testTuple1_hashCode() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            BooleanTuple1 tuple3 = BooleanTuple.of(false);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(1231, tuple1.hashCode());
            assertEquals(1237, tuple3.hashCode());
        }

        @Test
        public void testTuple1_equals() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            BooleanTuple1 tuple3 = BooleanTuple.of(false);

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertNotEquals(tuple1, null);
            assertNotEquals(tuple1, "not a tuple");
        }

        @Test
        public void testTuple1_toString() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals("(true)", tuple.toString());
        }

        // ============ BooleanTuple2 Tests ============

        @Test
        public void testTuple2_arity() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testTuple2_reverse() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple2 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        public void testTuple2_contains_firstElement() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void testTuple2_contains_secondElement() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple2_contains_notFound() {
            BooleanTuple2 tuple = BooleanTuple.of(true, true);
            assertTrue(tuple.contains(true));
            assertFalse(tuple.contains(false));
        }

        @Test
        public void testTuple2_forEach() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(true, values.get(0));
            assertEquals(false, values.get(1));
        }

        @Test
        public void testTuple2_accept() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<String> results = new ArrayList<>();
            tuple.accept((a, b) -> results.add(a + "," + b));
            assertEquals(1, results.size());
            assertEquals("true,false", results.get(0));
        }

        @Test
        public void testTuple2_map() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Boolean result = tuple.map((a, b) -> a && b);
            assertFalse(result);
        }

        @Test
        public void testTuple2_filter_matches() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2_filter_noMatch() {
            BooleanTuple2 tuple = BooleanTuple.of(false, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a && b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple2_hashCode() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
        }

        @Test
        public void testTuple2_equals() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertNotEquals(tuple1, null);
        }

        @Test
        public void testTuple2_toString() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals("(true, false)", tuple.toString());
        }

        // ============ BooleanTuple3 Tests ============

        @Test
        public void testTuple3_arity() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testTuple3_reverse() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
            BooleanTuple3 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        @Test
        public void testTuple3_contains() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple3_forEach() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void testTuple3_accept() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<String> results = new ArrayList<>();
            tuple.accept((a, b, c) -> results.add(a + "," + b + "," + c));
            assertEquals(1, results.size());
            assertEquals("true,false,true", results.get(0));
        }

        @Test
        public void testTuple3_map() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Boolean result = tuple.map((a, b, c) -> a || b || c);
            assertTrue(result);
        }

        @Test
        public void testTuple3_filter_matches() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a && c);
            assertTrue(result.isPresent());
        }

        @Test
        public void testTuple3_filter_noMatch() {
            BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || b || c);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple3_hashCode() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple3_equals() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testTuple3_toString() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals("(true, false, true)", tuple.toString());
        }

        // ============ BooleanTuple4 Tests ============

        @Test
        public void testTuple4_arity() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testTuple4_reverse() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            BooleanTuple4 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);
        }

        @Test
        public void testTuple4_contains() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple4_forEach() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(4, values.size());
        }

        // ============ BooleanTuple5 Tests ============

        @Test
        public void testTuple5_arity() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testTuple5_reverse() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple5 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);
        }

        @Test
        public void testTuple5_contains() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple5_forEach() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        // ============ BooleanTuple6 Tests ============

        @Test
        public void testTuple6_arity() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testTuple6_reverse() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            BooleanTuple6 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        public void testTuple6_contains() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple6_forEach() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(6, values.size());
        }

        // ============ BooleanTuple7 Tests ============

        @Test
        public void testTuple7_arity() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testTuple7_reverse() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            BooleanTuple7 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
        }

        @Test
        public void testTuple7_contains() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple7_forEach() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(7, values.size());
        }

        // ============ BooleanTuple8 Tests ============

        @Test
        public void testTuple8_arity() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testTuple8_reverse() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            BooleanTuple8 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        public void testTuple8_contains() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple8_forEach() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(8, values.size());
        }

        // ============ BooleanTuple9 Tests ============

        @Test
        public void testTuple9_arity() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testTuple9_reverse() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            BooleanTuple9 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
        }

        @Test
        public void testTuple9_contains() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple9_forEach() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            List<Boolean> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(9, values.size());
        }

        // ============ Common Method Tests (inherited from BooleanTuple) ============

        @Test
        public void testToArray() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false, true }, array);

            // Verify it's a copy
            array[0] = false;
            assertEquals(true, tuple._1);
        }

        @Test
        public void testToList() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
            assertEquals(true, list.get(2));
        }

        @Test
        public void testStream() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            long count = tuple.stream().filter(b -> b).count();
            assertEquals(2, count);
        }

        @Test
        public void testHashCode_consistency() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testEquals_symmetry() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        public void testEquals_differentTypes() {
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);
            assertNotEquals(tuple2, tuple3);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for BooleanTuple and its inner classes (BooleanTuple0-9).
     * Tests cover factory methods, accessor methods, utility methods, functional methods,
     * equality/hashCode, and stream operations.
     */
    @Tag("2511")
    class BooleanTuple2511Test extends TestBase {

        // ============ Factory Method Tests - BooleanTuple.of() ============

        @Test
        public void testOf_tuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(false, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(false, tuple._8);
            assertEquals(true, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - BooleanTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            BooleanTuple<?> tuple = BooleanTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            BooleanTuple<?> tuple = BooleanTuple.copyOf(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_singleElement() {
            BooleanTuple1 tuple = BooleanTuple.copyOf(new boolean[] { true });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(true, tuple._1);
        }

        @Test
        public void testCreate_twoElements() {
            BooleanTuple2 tuple = BooleanTuple.copyOf(new boolean[] { true, false });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
        }

        @Test
        public void testCreate_threeElements() {
            BooleanTuple3 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
        }

        @Test
        public void testCreate_nineElements() {
            BooleanTuple9 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._9);
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> BooleanTuple.copyOf(new boolean[10]));
        }

        // ============ Accessor Tests - Direct Field Access ============

        @Test
        public void testTuple1_directFieldAccess() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(true, tuple._1);
        }

        @Test
        public void testTuple2_directFieldAccess() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
        }

        @Test
        public void testTuple3_directFieldAccess() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
        }

        // ============ Reverse Tests ============

        @Test
        public void testTuple0_reverse() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            BooleanTuple0 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(tuple, reversed);
        }

        @Test
        public void testTuple1_reverse() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals(true, reversed._1);
        }

        @Test
        public void testTuple2_reverse() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple2 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        public void testTuple3_reverse() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        @Test
        public void testTuple5_reverse() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            BooleanTuple5 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
            assertEquals(false, reversed._4);
            assertEquals(true, reversed._5);
        }

        // ============ Contains Tests ============

        @Test
        public void testTuple0_contains() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertFalse(tuple.contains(true));
            assertFalse(tuple.contains(false));
        }

        @Test
        public void testTuple1_contains_true() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertTrue(tuple.contains(true));
            assertFalse(tuple.contains(false));
        }

        @Test
        public void testTuple1_contains_false() {
            BooleanTuple1 tuple = BooleanTuple.of(false);
            assertFalse(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple2_contains_true() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple3_contains_mixed() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void testTuple5_contains_all_true() {
            BooleanTuple5 tuple = BooleanTuple.of(true, true, true, true, true);
            assertTrue(tuple.contains(true));
            assertFalse(tuple.contains(false));
        }

        // ============ toArray Tests ============

        @Test
        public void testTuple0_toArray() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void testTuple1_toArray() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(1, array.length);
            assertEquals(true, array[0]);
        }

        @Test
        public void testTuple2_toArray() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(2, array.length);
            assertArrayEquals(new boolean[] { true, false }, array);
        }

        @Test
        public void testTuple3_toArray() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertArrayEquals(new boolean[] { true, false, true }, array);
        }

        @Test
        public void testToArray_immutability() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean[] array1 = tuple.toArray();
            boolean[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
        }

        // ============ toList Tests ============

        @Test
        public void testTuple0_toList() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        public void testTuple1_toList() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(true, list.get(0));
        }

        @Test
        public void testTuple2_toList() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
        }

        @Test
        public void testTuple3_toList() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(true, list.get(0));
            assertEquals(false, list.get(1));
            assertEquals(true, list.get(2));
        }

        // ============ forEach Tests ============

        @Test
        public void testTuple0_forEach() throws Exception {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(0, results.size());
        }

        @Test
        public void testTuple1_forEach() throws Exception {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(1, results.size());
            assertEquals(true, results.get(0));
        }

        @Test
        public void testTuple2_forEach() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(2, results.size());
            assertEquals(true, results.get(0));
            assertEquals(false, results.get(1));
        }

        @Test
        public void testTuple3_forEach() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(3, results.size());
            assertEquals(true, results.get(0));
            assertEquals(false, results.get(1));
            assertEquals(true, results.get(2));
        }

        // ============ Stream Tests ============

        @Test
        public void testTuple1_stream() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            long count = tuple.stream().count();
            assertEquals(1, count);
        }

        @Test
        public void testTuple2_stream_filter() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            long trueCount = tuple.stream().filter(b -> b).count();
            assertEquals(1, trueCount);
        }

        @Test
        public void testTuple3_stream_allMatch() {
            BooleanTuple3 tuple = BooleanTuple.of(true, true, true);
            boolean allTrue = tuple.stream().allMatch(b -> b);
            assertTrue(allTrue);
        }

        // ============ BooleanTuple2.accept Tests ============

        @Test
        public void testTuple2_accept() throws Exception {
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

        // ============ BooleanTuple2.map Tests ============

        @Test
        public void testTuple2_map() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean result = tuple.map((a, b) -> a && b);
            assertEquals(false, result);
        }

        @Test
        public void testTuple2_map_or() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean result = tuple.map((a, b) -> a || b);
            assertEquals(true, result);
        }

        // ============ BooleanTuple2.filter Tests ============

        @Test
        public void testTuple2_filter_pass() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2_filter_fail() throws Exception {
            BooleanTuple2 tuple = BooleanTuple.of(false, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
            assertFalse(result.isPresent());
        }

        // ============ BooleanTuple3.accept Tests ============

        @Test
        public void testTuple3_accept() throws Exception {
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

        // ============ BooleanTuple3.map Tests ============

        @Test
        public void testTuple3_map() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean result = tuple.map((a, b, c) -> a && b && c);
            assertEquals(false, result);
        }

        @Test
        public void testTuple3_map_or() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean result = tuple.map((a, b, c) -> a || b || c);
            assertEquals(true, result);
        }

        // ============ BooleanTuple3.filter Tests ============

        @Test
        public void testTuple3_filter_pass() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3_filter_fail() throws Exception {
            BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || b || c);
            assertFalse(result.isPresent());
        }

        // ============ hashCode Tests ============

        @Test
        public void testTuple0_hashCode() {
            BooleanTuple0 tuple1 = BooleanTuple.copyOf(new boolean[0]);
            BooleanTuple0 tuple2 = BooleanTuple.copyOf(new boolean[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple1_hashCode_true() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(1231, tuple1.hashCode());
        }

        @Test
        public void testTuple1_hashCode_false() {
            BooleanTuple1 tuple = BooleanTuple.of(false);
            assertEquals(1237, tuple.hashCode());
        }

        @Test
        public void testTuple2_hashCode() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple3_hashCode() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple2_hashCode_different() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // ============ equals Tests ============

        @Test
        public void testTuple0_equals() {
            BooleanTuple0 tuple1 = BooleanTuple.copyOf(new boolean[0]);
            BooleanTuple0 tuple2 = BooleanTuple.copyOf(new boolean[0]);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple1_equals_same() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testTuple1_equals_true() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple1_equals_false() {
            BooleanTuple1 tuple1 = BooleanTuple.of(false);
            BooleanTuple1 tuple2 = BooleanTuple.of(false);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple1_notEquals() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(false);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple2_equals() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple2_notEquals_different_values() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple3_equals() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple_notEquals_null() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotEquals(tuple, null);
            assertFalse(tuple.equals(null));
        }

        @Test
        public void testTuple_notEquals_differentType() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotEquals(tuple, "true");
            assertNotEquals(tuple, 1);
        }

        @Test
        public void testTuple2_notEquals_differentTuple() {
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);
            assertNotEquals(tuple2, tuple3);
        }

        // ============ toString Tests ============

        @Test
        public void testTuple0_toString() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testTuple1_toString() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals("(true)", tuple.toString());
        }

        @Test
        public void testTuple1_toString_false() {
            BooleanTuple1 tuple = BooleanTuple.of(false);
            assertEquals("(false)", tuple.toString());
        }

        @Test
        public void testTuple2_toString() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals("(true, false)", tuple.toString());
        }

        @Test
        public void testTuple3_toString() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals("(true, false, true)", tuple.toString());
        }

        // ============ Arity Tests ============

        @Test
        public void testTuple0_arity() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testTuple1_arity() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testTuple2_arity() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testTuple3_arity() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testTuple4_arity() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testTuple5_arity() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testTuple6_arity() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testTuple7_arity() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testTuple8_arity() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testTuple9_arity() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple.arity());
        }

        // ============ Edge Cases and Special Tests ============

        @Test
        public void testTuple_immutability() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            // The tuple is immutable, so we can't change _1, but we verify it stays the same
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._1);
        }

        @Test
        public void testMultipleTuples_independence() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
            assertNotEquals(tuple1, tuple2);
            assertEquals(true, tuple1._1);
            assertEquals(false, tuple2._1);
        }

        @Test
        public void testTuple_allTrueValues() {
            BooleanTuple3 tuple = BooleanTuple.of(true, true, true);
            assertTrue(tuple.contains(true));
            assertFalse(tuple.contains(false));
            assertTrue(tuple.stream().allMatch(b -> b));
        }

        @Test
        public void testTuple_allFalseValues() {
            BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
            assertFalse(tuple.contains(true));
            assertTrue(tuple.contains(false));
            assertFalse(tuple.stream().anyMatch(b -> b));
        }

    }

    @Nested
    /**
     * Comprehensive unit tests for BooleanTuple and its inner classes (BooleanTuple0-9).
     * Tests cover all public methods including factory methods, accessor methods, utility methods,
     * functional methods, equality/hashCode, and stream operations.
     */
    @Tag("2512")
    class BooleanTuple2512Test extends TestBase {

        // ============ Factory Method Tests - BooleanTuple.of() ============

        @Test
        public void test_of_tuple1() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_of_tuple2() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_of_tuple3() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_of_tuple4() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_of_tuple5() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_of_tuple6() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_of_tuple7() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void test_of_tuple8() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(false, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void test_of_tuple9() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertNotNull(tuple);
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
            assertEquals(true, tuple._3);
            assertEquals(false, tuple._4);
            assertEquals(true, tuple._5);
            assertEquals(false, tuple._6);
            assertEquals(true, tuple._7);
            assertEquals(false, tuple._8);
            assertEquals(true, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - BooleanTuple.copyOf() ============

        @Test
        public void test_create_nullArray() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_emptyArray() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_arraySize1() {
            BooleanTuple1 tuple = BooleanTuple.copyOf(new boolean[] { true });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(true, tuple._1);
        }

        @Test
        public void test_create_arraySize2() {
            BooleanTuple2 tuple = BooleanTuple.copyOf(new boolean[] { true, false });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(false, tuple._2);
        }

        @Test
        public void test_create_arraySize9() {
            BooleanTuple9 tuple = BooleanTuple.copyOf(new boolean[] { true, false, true, false, true, false, true, false, true });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals(true, tuple._1);
            assertEquals(true, tuple._9);
        }

        @Test
        public void test_create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                BooleanTuple.copyOf(new boolean[10]);
            });
        }

        // ============ Tuple0 Tests ============

        @Test
        public void test_tuple0_arity() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_tuple0_reverse() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            BooleanTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_tuple0_contains() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertFalse(tuple.contains(true));
            assertFalse(tuple.contains(false));
        }

        @Test
        public void test_tuple0_toArray() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            boolean[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void test_tuple0_toList() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        public void test_tuple0_forEach() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            List<Boolean> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        public void test_tuple0_stream() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        public void test_tuple0_toString() {
            BooleanTuple0 tuple = BooleanTuple.copyOf(new boolean[0]);
            assertEquals("()", tuple.toString());
        }

        // ============ Tuple1 Tests ============

        @Test
        public void test_tuple1_arity() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_tuple1_reverse() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(true, reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void test_tuple1_contains_found() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertTrue(tuple.contains(true));
        }

        @Test
        public void test_tuple1_contains_notFound() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertFalse(tuple.contains(false));
        }

        @Test
        public void test_tuple1_toArray() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true }, array);
        }

        @Test
        public void test_tuple1_toList() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(true, list.get(0));
        }

        @Test
        public void test_tuple1_forEach() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            List<Boolean> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(true, collected.get(0));
        }

        @Test
        public void test_tuple1_stream() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            long trueCount = tuple.stream().filter(b -> b).count();
            assertEquals(1, trueCount);
        }

        @Test
        public void test_tuple1_hashCode() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple1_equals_same() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        public void test_tuple1_equals_equal() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(true);
            assertTrue(tuple1.equals(tuple2));
            assertTrue(tuple2.equals(tuple1));
        }

        @Test
        public void test_tuple1_equals_notEqual() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple1 tuple2 = BooleanTuple.of(false);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_tuple1_equals_null() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertFalse(tuple.equals(null));
        }

        @Test
        public void test_tuple1_equals_differentClass() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertFalse(tuple.equals("not a tuple"));
        }

        @Test
        public void test_tuple1_toString() {
            BooleanTuple1 tuple = BooleanTuple.of(true);
            assertEquals("(true)", tuple.toString());
        }

        // ============ Tuple2 Tests ============

        @Test
        public void test_tuple2_arity() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_tuple2_reverse() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanTuple2 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
        }

        @Test
        public void test_tuple2_contains_found() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void test_tuple2_contains_notFound() {
            BooleanTuple2 tuple = BooleanTuple.of(true, true);
            assertFalse(tuple.contains(false));
        }

        @Test
        public void test_tuple2_toArray() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false }, array);
        }

        @Test
        public void test_tuple2_toList() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            BooleanList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        public void test_tuple2_forEach() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(true, collected.get(0));
            assertEquals(false, collected.get(1));
        }

        @Test
        public void test_tuple2_stream() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            long count = tuple.stream().count();
            assertEquals(2, count);
        }

        @Test
        public void test_tuple2_accept() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            List<Boolean> collected = new ArrayList<>();
            tuple.accept((a, b) -> {
                collected.add(a);
                collected.add(b);
            });
            assertEquals(2, collected.size());
            assertEquals(true, collected.get(0));
            assertEquals(false, collected.get(1));
        }

        @Test
        public void test_tuple2_map() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            String result = tuple.map((a, b) -> a + "," + b);
            assertEquals("true,false", result);
        }

        @Test
        public void test_tuple2_filter_match() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a != b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_tuple2_filter_noMatch() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            Optional<BooleanTuple2> result = tuple.filter((a, b) -> a == b);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple2_hashCode() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple2_equals() {
            BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(null));
        }

        @Test
        public void test_tuple2_toString() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertEquals("(true, false)", tuple.toString());
        }

        // ============ Tuple3 Tests ============

        @Test
        public void test_tuple3_arity() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_tuple3_reverse() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
            assertEquals(true, reversed._3);
        }

        @Test
        public void test_tuple3_contains() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
        }

        @Test
        public void test_tuple3_toArray() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            assertArrayEquals(new boolean[] { true, false, true }, array);
        }

        @Test
        public void test_tuple3_accept() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            List<Boolean> collected = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                collected.add(a);
                collected.add(b);
                collected.add(c);
            });
            assertEquals(3, collected.size());
        }

        @Test
        public void test_tuple3_map() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            String result = tuple.map((a, b, c) -> a + "," + b + "," + c);
            assertEquals("true,false,true", result);
        }

        @Test
        public void test_tuple3_filter_match() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a == c);
            assertTrue(result.isPresent());
        }

        @Test
        public void test_tuple3_filter_noMatch() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a == b);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple3_hashCode() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple3_equals() {
            BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
            BooleanTuple3 tuple3 = BooleanTuple.of(false, false, true);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
        }

        @Test
        public void test_tuple3_toString() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            assertEquals("(true, false, true)", tuple.toString());
        }

        // ============ Tuple4-9 Basic Tests ============

        @Test
        public void test_tuple4_basic() {
            BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            assertEquals(4, tuple.arity());
            assertTrue(tuple.contains(true));
            assertTrue(tuple.contains(false));
            BooleanTuple4 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
            assertEquals(true, reversed._2);
            assertEquals(false, reversed._3);
            assertEquals(true, reversed._4);
        }

        @Test
        public void test_tuple5_basic() {
            BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
            assertEquals(5, tuple.arity());
            assertTrue(tuple.contains(true));
            BooleanTuple5 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertEquals(false, reversed._2);
        }

        @Test
        public void test_tuple6_basic() {
            BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
            assertEquals(6, tuple.arity());
            assertTrue(tuple.contains(false));
            BooleanTuple6 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
        }

        @Test
        public void test_tuple7_basic() {
            BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
            assertEquals(7, tuple.arity());
            assertEquals(7, tuple.toArray().length);
            BooleanTuple7 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
        }

        @Test
        public void test_tuple8_basic() {
            BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
            BooleanTuple8 reversed = tuple.reverse();
            assertEquals(false, reversed._1);
        }

        @Test
        public void test_tuple9_basic() {
            BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
            BooleanTuple9 reversed = tuple.reverse();
            assertEquals(true, reversed._1);
            assertEquals(true, reversed._9);
        }

        // ============ Edge Cases and Additional Coverage ============

        @Test
        public void test_toArray_independence() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            boolean[] array = tuple.toArray();
            array[0] = false;
            assertEquals(true, tuple._1); // Tuple should be unaffected
        }

        @Test
        public void test_toList_independence() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            BooleanList list = tuple.toList();
            list.set(0, false);
            assertEquals(true, tuple._1); // Tuple should be unaffected
        }

        @Test
        public void test_stream_operations() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            long trueCount = tuple.stream().filter(b -> b).count();
            assertEquals(2, trueCount);
        }

        @Test
        public void test_forEach_withException() {
            BooleanTuple2 tuple = BooleanTuple.of(true, false);
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(b -> {
                    if (!b)
                        throw new RuntimeException("test exception");
                });
            });
        }

        @Test
        public void test_equals_differentArity() {
            BooleanTuple1 tuple1 = BooleanTuple.of(true);
            BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_hashCode_consistency() {
            BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_create_allSizes() {
            for (int i = 0; i <= 9; i++) {
                boolean[] array = new boolean[i];
                BooleanTuple<?> tuple = BooleanTuple.copyOf(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_BooleanTuple extends TestBase {
        // ===== BooleanTuple Javadoc examples =====

        @Test
        public void testBooleanTupleOf1() {
            // BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
            // boolean value = tuple._1;  // true
            BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
            assertTrue(t._1);
        }

        @Test
        public void testBooleanTupleOf2() {
            // BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
            // boolean first = tuple._1;  // true
            // boolean second = tuple._2;  // false
            BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
            assertTrue(t._1);
            assertFalse(t._2);
        }

        @Test
        public void testBooleanTupleOf3() {
            // BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
            // boolean third = tuple._3;  // true
            BooleanTuple.BooleanTuple3 t = BooleanTuple.of(true, false, true);
            assertTrue(t._3);
        }

        @Test
        public void testBooleanTupleOf4Reverse() {
            // BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
            // boolean first = tuple._1;  // true
            // boolean fourth = tuple._4;  // false
            // BooleanTuple.BooleanTuple4 reversed = tuple.reverse();   // (false, true, false, true)
            BooleanTuple.BooleanTuple4 t = BooleanTuple.of(true, false, true, false);
            assertTrue(t._1);
            assertFalse(t._4);
            BooleanTuple.BooleanTuple4 reversed = t.reverse();
            assertFalse(reversed._1);
            assertTrue(reversed._2);
            assertFalse(reversed._3);
            assertTrue(reversed._4);
        }

        @Test
        public void testBooleanTupleOf5Contains() {
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
        public void testBooleanTupleOf9Arity() {
            // BooleanTuple.BooleanTuple9 tuple = ...
            // boolean first = tuple._1;  // true
            // boolean ninth = tuple._9;  // true
            // int arity = tuple.arity();   // 9
            BooleanTuple.BooleanTuple9 t = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
            assertTrue(t._1);
            assertTrue(t._9);
            assertEquals(9, t.arity());
        }

        @Test
        public void testBooleanTupleClassLevelMap() {
            // boolean result = triple.map((a, b, c) -> a || b || c);
            BooleanTuple.BooleanTuple3 triple = BooleanTuple.of(true, false, true);
            boolean result = triple.map((a, b, c) -> a || b || c);
            assertTrue(result);
        }

        @Test
        public void testBooleanTupleClassLevelFilter() {
            // u.Optional<BooleanTuple.BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
            BooleanTuple.BooleanTuple2 pair = BooleanTuple.of(true, false);
            Optional<BooleanTuple.BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
            assertTrue(filtered.isPresent());
        }
    }

}
