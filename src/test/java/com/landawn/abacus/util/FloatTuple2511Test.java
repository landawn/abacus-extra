package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
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
import com.landawn.abacus.util.u.Optional;

@Tag("2511")
public class FloatTuple2511Test extends TestBase {

    // ============ Factory Method Tests - FloatTuple.of() ============

    @Test
    public void testOf_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(3.14f);
        assertNotNull(tuple);
        assertEquals(3.14f, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        assertNotNull(tuple);
        assertEquals(1.5f, tuple._1);
        assertEquals(2.5f, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(2.0f, tuple._2);
        assertEquals(3.0f, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(2.0f, tuple._2);
        assertEquals(3.0f, tuple._3);
        assertEquals(4.0f, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(2.0f, tuple._2);
        assertEquals(3.0f, tuple._3);
        assertEquals(4.0f, tuple._4);
        assertEquals(5.0f, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(2.0f, tuple._2);
        assertEquals(3.0f, tuple._3);
        assertEquals(4.0f, tuple._4);
        assertEquals(5.0f, tuple._5);
        assertEquals(6.0f, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(7.0f, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(8.0f, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1);
        assertEquals(9.0f, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - FloatTuple.create() ============

    @Test
    public void testCreate_nullArray() {
        FloatTuple<?> tuple = FloatTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_array1() {
        FloatTuple1 tuple = FloatTuple.create(new float[] { 5.5f });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(5.5f, tuple._1);
    }

    @Test
    public void testCreate_array2() {
        FloatTuple2 tuple = FloatTuple.create(new float[] { 1.1f, 2.2f });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals(1.1f, tuple._1);
        assertEquals(2.2f, tuple._2);
    }

    @Test
    public void testCreate_array3() {
        FloatTuple3 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(1.0f, tuple._1);
        assertEquals(3.0f, tuple._3);
    }

    @Test
    public void testCreate_array9() {
        FloatTuple9 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> FloatTuple.create(new float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    // ============ Min/Max/Median Tests ============

    @Test
    public void testMin_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        assertEquals(5.5f, tuple.min());
    }

    @Test
    public void testMin_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(1.0f, tuple.min());
    }

    @Test
    public void testMin_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(10.5f, -3.2f, 0.0f, 7.1f, -10.0f);
        assertEquals(-10.0f, tuple.min());
    }

    @Test
    public void testMin_emptyTuple() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, tuple::min);
    }

    @Test
    public void testMax_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        assertEquals(5.5f, tuple.max());
    }

    @Test
    public void testMax_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(3.0f, tuple.max());
    }

    @Test
    public void testMax_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(10.5f, -3.2f, 0.0f, 7.1f, -10.0f);
        assertEquals(10.5f, tuple.max());
    }

    @Test
    public void testMax_emptyTuple() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, tuple::max);
    }

    @Test
    public void testMedian_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        assertEquals(5.5f, tuple.median());
    }

    @Test
    public void testMedian_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
        assertEquals(20.0f, tuple.median());
    }

    @Test
    public void testMedian_tuple4_even() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(2.0f, tuple.median());
    }

    @Test
    public void testMedian_emptyTuple() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, tuple::median);
    }

    // ============ Sum/Average Tests ============

    @Test
    public void testSum_tuple0() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertEquals(0.0f, tuple.sum());
    }

    @Test
    public void testSum_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(3.14f);
        assertEquals(3.14f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSum_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(6.0f, tuple.sum());
    }

    @Test
    public void testSum_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(15.0f, tuple.sum());
    }

    @Test
    public void testAverage_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(5.0, tuple.average());
    }

    @Test
    public void testAverage_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0, tuple.average());
    }

    @Test
    public void testAverage_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(2.0f, 4.0f, 6.0f, 8.0f, 10.0f);
        assertEquals(6.0, tuple.average());
    }

    @Test
    public void testAverage_emptyTuple() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, tuple::average);
    }

    // ============ Reverse Tests ============

    @Test
    public void testReverse_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        FloatTuple1 reversed = tuple.reverse();
        assertEquals(5.5f, reversed._1);
    }

    @Test
    public void testReverse_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 reversed = tuple.reverse();
        assertEquals(3.0f, reversed._1);
        assertEquals(2.0f, reversed._2);
        assertEquals(1.0f, reversed._3);
    }

    @Test
    public void testReverse_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        FloatTuple5 reversed = tuple.reverse();
        assertEquals(5.0f, reversed._1);
        assertEquals(1.0f, reversed._5);
    }

    @Test
    public void testReverse_tuple0() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        FloatTuple<?> reversed = tuple.reverse();
        assertEquals(0, reversed.arity());
    }

    // ============ Contains Tests ============

    @Test
    public void testContains_tuple1_found() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        assertTrue(tuple.contains(5.5f));
    }

    @Test
    public void testContains_tuple1_notFound() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        assertFalse(tuple.contains(3.3f));
    }

    @Test
    public void testContains_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(2.0f));
        assertTrue(tuple.contains(3.0f));
        assertFalse(tuple.contains(4.0f));
    }

    @Test
    public void testContains_emptyTuple() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertFalse(tuple.contains(1.0f));
    }

    // ============ Array/List Conversion Tests ============

    @Test
    public void testToArray_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array = tuple.toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array);
    }

    @Test
    public void testToArray_independence() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array1 = tuple.toArray();
        float[] array2 = tuple.toArray();
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
    }

    @Test
    public void testToList_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(1.0f, list.get(0));
        assertEquals(3.0f, list.get(2));
    }

    @Test
    public void testToList_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        FloatList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(5.5f, list.get(0));
    }

    // ============ Functional Methods Tests (ForEach, Stream) ============

    @Test
    public void testForEach_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<Float> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(3, collected.size());
        assertEquals(1.0f, collected.get(0));
        assertEquals(3.0f, collected.get(2));
    }

    @Test
    public void testForEach_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        List<Float> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals(5.5f, collected.get(0));
    }

    @Test
    public void testStream_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        double sum = tuple.stream().sum();
        assertEquals(6.0, sum);
    }

    @Test
    public void testStream_tuple5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        long count = tuple.stream().count();
        assertEquals(5, count);
    }

    // ============ Tuple2 Specific Functional Methods ============

    @Test
    public void testTuple2_accept() throws Exception {
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
    public void testTuple2_map() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        float product = tuple.map((a, b) -> a * b);
        assertEquals(12.0f, product);
    }

    @Test
    public void testTuple2_filter() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());

        Optional<FloatTuple2> result2 = tuple.filter((a, b) -> a + b < 5);
        assertFalse(result2.isPresent());
    }

    // ============ Tuple3 Specific Functional Methods ============

    @Test
    public void testTuple3_accept() throws Exception {
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
    public void testTuple3_map() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float product = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0f, product);
    }

    @Test
    public void testTuple3_filter() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());

        Optional<FloatTuple3> result2 = tuple.filter((a, b, c) -> a + b + c < 5);
        assertFalse(result2.isPresent());
    }

    // ============ Equality Tests ============

    @Test
    public void testEquals_sameTuple1() {
        FloatTuple1 tuple1 = FloatTuple.of(5.5f);
        FloatTuple1 tuple2 = FloatTuple.of(5.5f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_sameTuple3() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentTuple3() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 4.0f);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentTypes() {
        FloatTuple1 tuple1 = FloatTuple.of(5.5f);
        FloatTuple2 tuple2 = FloatTuple.of(5.5f, 1.0f);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_null() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        assertNotEquals(tuple, null);
    }

    @Test
    public void testEquals_self() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple, tuple);
    }

    // ============ HashCode Tests ============

    @Test
    public void testHashCode_consistency() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testHashCode_equal() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_different() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 4.0f);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(5.5f);
        String str = tuple.toString();
        assertNotNull(str);
        assertTrue(str.contains("5.5"));
    }

    @Test
    public void testToString_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        String str = tuple.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
    }

    @Test
    public void testToString_emptyTuple() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        String str = tuple.toString();
        assertEquals("()", str);
    }

    // ============ PrimitiveTuple Base Class Methods Tests ============

    @Test
    public void testAccept() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<FloatTuple3> results = new ArrayList<>();
        tuple.accept(results::add);
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testMap() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
        assertEquals("Sum: 6.0", result);
    }

    @Test
    public void testFilter() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 0);
        assertTrue(result.isPresent());

        Optional<FloatTuple3> result2 = tuple.filter(t -> t._1 < 0);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testToOptional() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    // ============ Arity Tests ============

    @Test
    public void testArity_tuple0() {
        FloatTuple<?> tuple = FloatTuple.create(new float[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testArity_tuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testArity_tuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_tuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testArity_tuple9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(9, tuple.arity());
    }

}
