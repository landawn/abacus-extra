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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple0;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple4;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple5;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple6;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple7;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple8;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple9;
import com.landawn.abacus.util.u.Optional;

@Tag("2511")
public class DoubleTuple2511Test extends TestBase {

    // ============ Factory Method Tests - DoubleTuple.of() ============

    @Test
    public void testOf_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertNotNull(tuple);
        assertEquals(3.14, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertNotNull(tuple);
        assertEquals(1.5, tuple._1);
        assertEquals(2.5, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(2.0, tuple._2);
        assertEquals(3.0, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(2.0, tuple._2);
        assertEquals(3.0, tuple._3);
        assertEquals(4.0, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(2.0, tuple._2);
        assertEquals(3.0, tuple._3);
        assertEquals(4.0, tuple._4);
        assertEquals(5.0, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(6.0, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(7.0, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(8.0, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1);
        assertEquals(9.0, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - DoubleTuple.create() ============

    @Test
    public void testCreate_nullArray() {
        DoubleTuple<?> tuple = DoubleTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_array1() {
        DoubleTuple1 tuple = DoubleTuple.create(new double[] { 5.5 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(5.5, tuple._1);
    }

    @Test
    public void testCreate_array2() {
        DoubleTuple2 tuple = DoubleTuple.create(new double[] { 1.1, 2.2 });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals(1.1, tuple._1);
        assertEquals(2.2, tuple._2);
    }

    @Test
    public void testCreate_array3() {
        DoubleTuple3 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0 });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(1.0, tuple._1);
        assertEquals(3.0, tuple._3);
    }

    @Test
    public void testCreate_array9() {
        DoubleTuple9 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> DoubleTuple.create(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    // ============ Min/Max/Median Tests ============

    @Test
    public void testMin_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        assertEquals(5.5, tuple.min());
    }

    @Test
    public void testMin_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(1.0, tuple.min());
    }

    @Test
    public void testMin_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(10.5, -3.2, 0.0, 7.1, -10.0);
        assertEquals(-10.0, tuple.min());
    }

    @Test
    public void testMin_emptyTuple() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, tuple::min);
    }

    @Test
    public void testMax_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        assertEquals(5.5, tuple.max());
    }

    @Test
    public void testMax_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(3.0, tuple.max());
    }

    @Test
    public void testMax_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(10.5, -3.2, 0.0, 7.1, -10.0);
        assertEquals(10.5, tuple.max());
    }

    @Test
    public void testMax_emptyTuple() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, tuple::max);
    }

    @Test
    public void testMedian_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        assertEquals(5.5, tuple.median());
    }

    @Test
    public void testMedian_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
        assertEquals(20.0, tuple.median());
    }

    @Test
    public void testMedian_tuple4_even() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, tuple.median());
    }

    @Test
    public void testMedian_emptyTuple() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, tuple::median);
    }

    // ============ Sum/Average Tests ============

    @Test
    public void testSum_tuple0() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertEquals(0.0, tuple.sum());
    }

    @Test
    public void testSum_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple.sum(), 0.001);
    }

    @Test
    public void testSum_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(6.0, tuple.sum());
    }

    @Test
    public void testSum_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(15.0, tuple.sum());
    }

    @Test
    public void testAverage_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.0);
        assertEquals(5.0, tuple.average());
    }

    @Test
    public void testAverage_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(2.0, tuple.average());
    }

    @Test
    public void testAverage_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(2.0, 4.0, 6.0, 8.0, 10.0);
        assertEquals(6.0, tuple.average());
    }

    @Test
    public void testAverage_emptyTuple() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, tuple::average);
    }

    // ============ Reverse Tests ============

    @Test
    public void testReverse_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        DoubleTuple1 reversed = tuple.reverse();
        assertEquals(5.5, reversed._1);
    }

    @Test
    public void testReverse_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 reversed = tuple.reverse();
        assertEquals(3.0, reversed._1);
        assertEquals(2.0, reversed._2);
        assertEquals(1.0, reversed._3);
    }

    @Test
    public void testReverse_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        DoubleTuple5 reversed = tuple.reverse();
        assertEquals(5.0, reversed._1);
        assertEquals(1.0, reversed._5);
    }

    @Test
    public void testReverse_tuple0() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        DoubleTuple<?> reversed = tuple.reverse();
        assertEquals(0, reversed.arity());
    }

    // ============ Contains Tests ============

    @Test
    public void testContains_tuple1_found() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        assertTrue(tuple.contains(5.5));
    }

    @Test
    public void testContains_tuple1_notFound() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        assertFalse(tuple.contains(3.3));
    }

    @Test
    public void testContains_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(2.0));
        assertTrue(tuple.contains(3.0));
        assertFalse(tuple.contains(4.0));
    }

    @Test
    public void testContains_emptyTuple() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertFalse(tuple.contains(1.0));
    }

    // ============ Array/List Conversion Tests ============

    @Test
    public void testToArray_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array);
    }

    @Test
    public void testToArray_independence() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array1 = tuple.toArray();
        double[] array2 = tuple.toArray();
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
    }

    @Test
    public void testToList_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(1.0, list.get(0));
        assertEquals(3.0, list.get(2));
    }

    @Test
    public void testToList_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        DoubleList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(5.5, list.get(0));
    }

    // ============ Functional Methods Tests (ForEach, Stream) ============

    @Test
    public void testForEach_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(3, collected.size());
        assertEquals(1.0, collected.get(0));
        assertEquals(3.0, collected.get(2));
    }

    @Test
    public void testForEach_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals(5.5, collected.get(0));
    }

    @Test
    public void testStream_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double sum = tuple.stream().sum();
        assertEquals(6.0, sum);
    }

    @Test
    public void testStream_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        long count = tuple.stream().count();
        assertEquals(5, count);
    }

    // ============ Tuple2 Specific Functional Methods ============

    @Test
    public void testTuple2_accept() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        List<Double> results = new ArrayList<>();
        tuple.accept((a, b) -> {
            results.add(a);
            results.add(b);
        });
        assertEquals(2, results.size());
        assertEquals(3.0, results.get(0));
        assertEquals(4.0, results.get(1));
    }

    @Test
    public void testTuple2_map() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        double product = tuple.map((a, b) -> a * b);
        assertEquals(12.0, product);
    }

    @Test
    public void testTuple2_filter() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        Optional<DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());

        Optional<DoubleTuple2> result2 = tuple.filter((a, b) -> a + b < 5);
        assertFalse(result2.isPresent());
    }

    // ============ Tuple3 Specific Functional Methods ============

    @Test
    public void testTuple3_accept() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Double> results = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            results.add(a);
            results.add(b);
            results.add(c);
        });
        assertEquals(3, results.size());
        assertEquals(1.0, results.get(0));
        assertEquals(3.0, results.get(2));
    }

    @Test
    public void testTuple3_map() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double product = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0, product);
    }

    @Test
    public void testTuple3_filter() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());

        Optional<DoubleTuple3> result2 = tuple.filter((a, b, c) -> a + b + c < 5);
        assertFalse(result2.isPresent());
    }

    // ============ Equality Tests ============

    @Test
    public void testEquals_sameTuple1() {
        DoubleTuple1 tuple1 = DoubleTuple.of(5.5);
        DoubleTuple1 tuple2 = DoubleTuple.of(5.5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_sameTuple3() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentTuple3() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 4.0);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentTypes() {
        DoubleTuple1 tuple1 = DoubleTuple.of(5.5);
        DoubleTuple2 tuple2 = DoubleTuple.of(5.5, 1.0);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_null() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        assertNotEquals(tuple, null);
    }

    @Test
    public void testEquals_self() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple, tuple);
    }

    // ============ HashCode Tests ============

    @Test
    public void testHashCode_consistency() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testHashCode_equal() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_different() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 4.0);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(5.5);
        String str = tuple.toString();
        assertNotNull(str);
        assertTrue(str.contains("5.5"));
    }

    @Test
    public void testToString_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        String str = tuple.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
    }

    @Test
    public void testToString_emptyTuple() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        String str = tuple.toString();
        assertEquals("[]", str);
    }

    // ============ PrimitiveTuple Base Class Methods Tests ============

    @Test
    public void testAccept() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<DoubleTuple3> results = new ArrayList<>();
        tuple.accept(results::add);
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testMap() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
        assertEquals("Sum: 6.0", result);
    }

    @Test
    public void testFilter() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> result = tuple.filter(t -> t._1 > 0);
        assertTrue(result.isPresent());

        Optional<DoubleTuple3> result2 = tuple.filter(t -> t._1 < 0);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testToOptional() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    // ============ Arity Tests ============

    @Test
    public void testArity_tuple0() {
        DoubleTuple<?> tuple = DoubleTuple.create(new double[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testArity_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testArity_tuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testArity_tuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(9, tuple.arity());
    }

}
