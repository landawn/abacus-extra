package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for PrimitiveTuple.
 * Tests common tuple functionality through concrete primitive tuple implementations.
 */
@Tag("2025")
public class PrimitiveTuple2025Test extends TestBase {

    // ============ Arity Tests ============

    @Test
    public void testArity_intTuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_intTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testArity_longTuple2() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_longTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testArity_doubleTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_doubleTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(3, tuple.arity());
    }

    // ============ Accept Tests ============

    @Test
    public void testAccept_intTuple2() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        List<Integer> values = new ArrayList<>();

        tuple.accept(t -> {
            values.add(t._1);
            values.add(t._2);
        });

        assertEquals(2, values.size());
        assertEquals(10, values.get(0).intValue());
        assertEquals(20, values.get(1).intValue());
    }

    @Test
    public void testAccept_intTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> sums = new ArrayList<>();

        tuple.accept(t -> sums.add(t._1 + t._2 + t._3));

        assertEquals(1, sums.size());
        assertEquals(6, sums.get(0).intValue());
    }

    @Test
    public void testAccept_longTuple2() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        List<Long> values = new ArrayList<>();

        tuple.accept(t -> {
            values.add(t._1);
            values.add(t._2);
        });

        assertEquals(2, values.size());
        assertEquals(100L, values.get(0).longValue());
        assertEquals(200L, values.get(1).longValue());
    }

    @Test
    public void testAccept_doubleTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        List<Double> values = new ArrayList<>();

        tuple.accept(t -> {
            values.add(t._1);
            values.add(t._2);
        });

        assertEquals(2, values.size());
        assertEquals(1.5, values.get(0).doubleValue(), 0.0001);
        assertEquals(2.5, values.get(1).doubleValue(), 0.0001);
    }

    @Test
    public void testAccept_withSideEffect() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        List<String> log = new ArrayList<>();

        tuple.accept(t -> {
            log.add("Processing tuple: " + t._1 + ", " + t._2);
        });

        assertEquals(1, log.size());
        assertTrue(log.get(0).contains("5"));
        assertTrue(log.get(0).contains("10"));
    }

    @Test
    public void testAccept_multipleOperations() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> results = new ArrayList<>();

        tuple.accept(t -> {
            results.add(t._1 * 2);
            results.add(t._2 * 2);
            results.add(t._3 * 2);
        });

        assertEquals(3, results.size());
        assertEquals(2, results.get(0).intValue());
        assertEquals(4, results.get(1).intValue());
        assertEquals(6, results.get(2).intValue());
    }

    // ============ Map Tests ============

    @Test
    public void testMap_intTuple2_toInt() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Integer sum = tuple.map(t -> t._1 + t._2);
        assertEquals(7, sum.intValue());
    }

    @Test
    public void testMap_intTuple2_toString() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        String result = tuple.map(t -> t._1 + "," + t._2);
        assertEquals("10,20", result);
    }

    @Test
    public void testMap_intTuple3_toDouble() {
        IntTuple3 tuple = IntTuple.of(2, 3, 4);
        Double average = tuple.map(t -> (t._1 + t._2 + t._3) / 3.0);
        assertEquals(3.0, average.doubleValue(), 0.0001);
    }

    @Test
    public void testMap_longTuple2_toBoolean() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        Boolean result = tuple.map(t -> t._1 < t._2);
        assertTrue(result);
    }

    @Test
    public void testMap_doubleTuple2_toInt() {
        DoubleTuple2 tuple = DoubleTuple.of(5.7, 3.2);
        Integer result = tuple.map(t -> (int) (t._1 + t._2));
        assertEquals(8, result.intValue());
    }

    @Test
    public void testMap_complexTransformation() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        String result = tuple.map(t -> {
            int sum = t._1 + t._2 + t._3;
            int product = t._1 * t._2 * t._3;
            return "sum=" + sum + ",product=" + product;
        });
        assertEquals("sum=6,product=6", result);
    }

    @Test
    public void testMap_withMathOperations() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
        assertEquals(5.0, distance.doubleValue(), 0.0001);
    }

    @Test
    public void testMap_chainedWithAccept() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        List<Integer> values = new ArrayList<>();

        Integer sum = tuple.map(t -> {
            values.add(t._1);
            values.add(t._2);
            return t._1 + t._2;
        });

        assertEquals(30, sum.intValue());
        assertEquals(2, values.size());
    }

    // ============ Filter Tests ============

    @Test
    public void testFilter_intTuple2_match() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);

        assertTrue(result.isPresent());
        assertEquals(5, result.get()._1);
        assertEquals(10, result.get()._2);
    }

    @Test
    public void testFilter_intTuple2_noMatch() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<IntTuple2> result = tuple.filter(t -> t._1 > 10);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_intTuple3_match() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 == 6);

        assertTrue(result.isPresent());
        assertEquals(1, result.get()._1);
        assertEquals(2, result.get()._2);
        assertEquals(3, result.get()._3);
    }

    @Test
    public void testFilter_intTuple3_noMatch() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 == 10);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_longTuple2_match() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        Optional<LongTuple2> result = tuple.filter(t -> t._1 < t._2);

        assertTrue(result.isPresent());
        assertEquals(100L, result.get()._1);
        assertEquals(200L, result.get()._2);
    }

    @Test
    public void testFilter_longTuple2_noMatch() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        Optional<LongTuple2> result = tuple.filter(t -> t._1 > t._2);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_doubleTuple2_match() {
        DoubleTuple2 tuple = DoubleTuple.of(3.5, 7.5);
        Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 > 10.0);

        assertTrue(result.isPresent());
        assertEquals(3.5, result.get()._1, 0.0001);
        assertEquals(7.5, result.get()._2, 0.0001);
    }

    @Test
    public void testFilter_doubleTuple2_noMatch() {
        DoubleTuple2 tuple = DoubleTuple.of(3.5, 4.5);
        Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 > 10.0);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_withComplexPredicate() {
        IntTuple3 tuple = IntTuple.of(2, 4, 6);
        Optional<IntTuple3> result = tuple.filter(t -> t._1 % 2 == 0 && t._2 % 2 == 0 && t._3 % 2 == 0);

        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_negativeValues() {
        IntTuple2 tuple = IntTuple.of(-5, -10);
        Optional<IntTuple2> result = tuple.filter(t -> t._1 < 0 && t._2 < 0);

        assertTrue(result.isPresent());
        assertEquals(-5, result.get()._1);
        assertEquals(-10, result.get()._2);
    }

    @Test
    public void testFilter_mixedValues() {
        IntTuple2 tuple = IntTuple.of(-5, 10);
        Optional<IntTuple2> result = tuple.filter(t -> t._1 < 0 && t._2 > 0);

        assertTrue(result.isPresent());
    }

    // ============ ToOptional Tests ============

    @Test
    public void testToOptional_intTuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        Optional<IntTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(1, optional.get()._1);
        assertEquals(2, optional.get()._2);
    }

    @Test
    public void testToOptional_intTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(1, optional.get()._1);
        assertEquals(2, optional.get()._2);
        assertEquals(3, optional.get()._3);
    }

    @Test
    public void testToOptional_longTuple2() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        Optional<LongTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(100L, optional.get()._1);
        assertEquals(200L, optional.get()._2);
    }

    @Test
    public void testToOptional_doubleTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        Optional<DoubleTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(1.5, optional.get()._1, 0.0001);
        assertEquals(2.5, optional.get()._2, 0.0001);
    }

    @Test
    public void testToOptional_zeroValues() {
        IntTuple2 tuple = IntTuple.of(0, 0);
        Optional<IntTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(0, optional.get()._1);
        assertEquals(0, optional.get()._2);
    }

    @Test
    public void testToOptional_negativeValues() {
        IntTuple2 tuple = IntTuple.of(-1, -2);
        Optional<IntTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(-1, optional.get()._1);
        assertEquals(-2, optional.get()._2);
    }

    @Test
    public void testToOptional_largeValues() {
        LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MIN_VALUE);
        Optional<LongTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(Long.MAX_VALUE, optional.get()._1);
        assertEquals(Long.MIN_VALUE, optional.get()._2);
    }

    // ============ Combined Operations Tests ============

    @Test
    public void testFilterThenMap() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<Integer> result = tuple.filter(t -> t._1 > 0).map(t -> t._1 + t._2);

        assertTrue(result.isPresent());
        assertEquals(15, result.get().intValue());
    }

    @Test
    public void testFilterThenMap_noMatch() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<Integer> result = tuple.filter(t -> t._1 > 10).map(t -> t._1 + t._2);

        assertFalse(result.isPresent());
    }

    @Test
    public void testMapThenFilter() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Integer sum = tuple.map(t -> t._1 + t._2);
        // Note: filter operates on tuple, not mapped result
        // This test shows map works independently
        assertEquals(7, sum.intValue());
    }

    @Test
    public void testAcceptThenMap() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> values = new ArrayList<>();

        tuple.accept(t -> {
            values.add(t._1);
            values.add(t._2);
            values.add(t._3);
        });

        Integer sum = tuple.map(t -> t._1 + t._2 + t._3);

        assertEquals(3, values.size());
        assertEquals(6, sum.intValue());
    }

    @Test
    public void testToOptionalThenFilter() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<IntTuple2> optional = tuple.toOptional();
        Optional<IntTuple2> filtered = optional.filter(t -> t._1 > 0 && t._2 > 0);

        assertTrue(filtered.isPresent());
        assertEquals(5, filtered.get()._1);
        assertEquals(10, filtered.get()._2);
    }

    // ============ Edge Cases Tests ============

    @Test
    public void testZeroValues() {
        IntTuple2 tuple = IntTuple.of(0, 0);
        Integer sum = tuple.map(t -> t._1 + t._2);
        assertEquals(0, sum.intValue());

        Optional<IntTuple2> filtered = tuple.filter(t -> t._1 == 0);
        assertTrue(filtered.isPresent());
    }

    @Test
    public void testNegativeValues() {
        IntTuple2 tuple = IntTuple.of(-5, -10);
        Integer sum = tuple.map(t -> t._1 + t._2);
        assertEquals(-15, sum.intValue());

        Optional<IntTuple2> filtered = tuple.filter(t -> t._1 < 0 && t._2 < 0);
        assertTrue(filtered.isPresent());
    }

    @Test
    public void testMaxValues() {
        IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Long sum = tuple.map(t -> (long) t._1 + (long) t._2);
        assertEquals((long) Integer.MAX_VALUE * 2, sum.longValue());
    }

    @Test
    public void testMinValues() {
        IntTuple2 tuple = IntTuple.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Long sum = tuple.map(t -> (long) t._1 + (long) t._2);
        assertEquals((long) Integer.MIN_VALUE * 2, sum.longValue());
    }

    @Test
    public void testLargeDoubleValues() {
        DoubleTuple2 tuple = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE);
        Double sum = tuple.map(t -> t._1 + t._2);
        assertEquals(Double.POSITIVE_INFINITY, sum.doubleValue(), 0.0001);
    }

    @Test
    public void testSmallDoubleValues() {
        DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE);
        Double sum = tuple.map(t -> t._1 + t._2);
        assertNotNull(sum);
    }

    @Test
    public void testLongMaxValues() {
        LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, 1L);
        // Test without overflow
        Long max = tuple.map(t -> t._1);
        assertEquals(Long.MAX_VALUE, max.longValue());
    }

    // ============ Multiple Operations Tests ============

    @Test
    public void testMultipleAcceptCalls() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        List<Integer> values1 = new ArrayList<>();
        List<Integer> values2 = new ArrayList<>();

        tuple.accept(t -> values1.add(t._1 + t._2));
        tuple.accept(t -> values2.add(t._1 * t._2));

        assertEquals(1, values1.size());
        assertEquals(30, values1.get(0).intValue());
        assertEquals(1, values2.size());
        assertEquals(200, values2.get(0).intValue());
    }

    @Test
    public void testMultipleMapCalls() {
        IntTuple2 tuple = IntTuple.of(3, 4);

        Integer sum = tuple.map(t -> t._1 + t._2);
        Integer product = tuple.map(t -> t._1 * t._2);
        Double average = tuple.map(t -> (t._1 + t._2) / 2.0);

        assertEquals(7, sum.intValue());
        assertEquals(12, product.intValue());
        assertEquals(3.5, average.doubleValue(), 0.0001);
    }

    @Test
    public void testMultipleFilterCalls() {
        IntTuple2 tuple = IntTuple.of(5, 10);

        Optional<IntTuple2> filter1 = tuple.filter(t -> t._1 > 0);
        Optional<IntTuple2> filter2 = tuple.filter(t -> t._1 < 10);
        Optional<IntTuple2> filter3 = tuple.filter(t -> t._1 == 5 && t._2 == 10);

        assertTrue(filter1.isPresent());
        assertTrue(filter2.isPresent());
        assertTrue(filter3.isPresent());
    }

    // ============ Type Consistency Tests ============

    @Test
    public void testIntTuple_consistency() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(2, tuple.arity());

        tuple.accept(t -> {
            assertEquals(1, t._1);
            assertEquals(2, t._2);
        });

        Integer sum = tuple.map(t -> t._1 + t._2);
        assertEquals(3, sum.intValue());

        Optional<IntTuple2> opt = tuple.filter(t -> t._1 > 0);
        assertTrue(opt.isPresent());
    }

    @Test
    public void testLongTuple_consistency() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        assertEquals(2, tuple.arity());

        tuple.accept(t -> {
            assertEquals(100L, t._1);
            assertEquals(200L, t._2);
        });

        Long sum = tuple.map(t -> t._1 + t._2);
        assertEquals(300L, sum.longValue());

        Optional<LongTuple2> opt = tuple.filter(t -> t._1 < t._2);
        assertTrue(opt.isPresent());
    }

    @Test
    public void testDoubleTuple_consistency() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2, tuple.arity());

        tuple.accept(t -> {
            assertEquals(1.5, t._1, 0.0001);
            assertEquals(2.5, t._2, 0.0001);
        });

        Double sum = tuple.map(t -> t._1 + t._2);
        assertEquals(4.0, sum.doubleValue(), 0.0001);

        Optional<DoubleTuple2> opt = tuple.filter(t -> t._1 > 0);
        assertTrue(opt.isPresent());
    }

    // ============ Immutability Tests ============

    @Test
    public void testImmutability_intTuple() {
        IntTuple2 tuple = IntTuple.of(1, 2);

        // Operations should not modify original
        tuple.map(t -> t._1 + 100);
        tuple.filter(t -> t._1 > 0);
        tuple.toOptional();

        // Original values should remain unchanged
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
    }

    @Test
    public void testImmutability_longTuple() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);

        tuple.map(t -> t._1 * 2);
        tuple.filter(t -> t._1 > 50L);
        tuple.toOptional();

        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
    }

    @Test
    public void testImmutability_doubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);

        tuple.map(t -> t._1 * 2);
        tuple.filter(t -> t._1 > 1.0);
        tuple.toOptional();

        assertEquals(1.5, tuple._1, 0.0001);
        assertEquals(2.5, tuple._2, 0.0001);
    }
}
