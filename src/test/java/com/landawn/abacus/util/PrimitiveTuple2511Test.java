package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.u.Optional;

@Tag("2511")
public class PrimitiveTuple2511Test extends TestBase {

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
    public void testArity_floatTuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_floatTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
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

    // ============ Accept Method Tests - Using Consumer Pattern ============

    @Test
    public void testAccept_intTuple2() throws Exception {
        IntTuple2 tuple = IntTuple.of(10, 20);
        final List<IntTuple2> results = new ArrayList<>();
        ((PrimitiveTuple<IntTuple2>) tuple).accept(t -> results.add(t));
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testAccept_intTuple3() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        final List<IntTuple3> results = new ArrayList<>();
        ((PrimitiveTuple<IntTuple3>) tuple).accept(t -> results.add(t));
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testAccept_floatTuple2() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        final List<FloatTuple2> results = new ArrayList<>();
        ((PrimitiveTuple<FloatTuple2>) tuple).accept(t -> results.add(t));
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testAccept_floatTuple3() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        final List<FloatTuple3> results = new ArrayList<>();
        ((PrimitiveTuple<FloatTuple3>) tuple).accept(t -> results.add(t));
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testAccept_doubleTuple2() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        final List<DoubleTuple2> results = new ArrayList<>();
        ((PrimitiveTuple<DoubleTuple2>) tuple).accept(t -> results.add(t));
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    @Test
    public void testAccept_doubleTuple3() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        final List<DoubleTuple3> results = new ArrayList<>();
        ((PrimitiveTuple<DoubleTuple3>) tuple).accept(t -> results.add(t));
        assertEquals(1, results.size());
        assertEquals(tuple, results.get(0));
    }

    // ============ Map Method Tests ============

    @Test
    public void testMap_intTuple2_toString() throws Exception {
        IntTuple2 tuple = IntTuple.of(5, 10);
        String result = tuple.map(t -> "Sum: " + (t._1 + t._2));
        assertEquals("Sum: 15", result);
    }

    @Test
    public void testMap_intTuple3_toInt() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int result = tuple.map(t -> t._1 + t._2 + t._3);
        assertEquals(6, result);
    }

    @Test
    public void testMap_floatTuple2_toFloat() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        float result = tuple.map((t) -> t._1 * t._2);
        assertEquals(12.0f, result);
    }

    @Test
    public void testMap_floatTuple3_toDouble() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        double result = tuple.map(t -> (t._1 + t._2 + t._3) / 3.0);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testMap_doubleTuple2_toDouble() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(2.5, 3.5);
        double result = tuple.map(t -> t._1 + t._2);
        assertEquals(6.0, result);
    }

    @Test
    public void testMap_doubleTuple3_toBoolean() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        boolean result = tuple.map(t -> t._1 > 0 && t._2 > 0 && t._3 > 0);
        assertTrue(result);
    }

    @Test
    public void testMap_longTuple2_toLong() throws Exception {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        long result = tuple.map(t -> t._1 + t._2);
        assertEquals(300L, result);
    }

    // ============ Filter Method Tests ============

    @Test
    public void testFilter_intTuple2_true() throws Exception {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_intTuple2_false() throws Exception {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<IntTuple2> result = tuple.filter(t -> t._1 < 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_intTuple3_true() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 > 5);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_intTuple3_false() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 < 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_floatTuple2_true() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        Optional<FloatTuple2> result = tuple.filter(t -> t._1 > 1.0f);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_floatTuple2_false() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        Optional<FloatTuple2> result = tuple.filter(t -> t._1 < 0.0f);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_floatTuple3_true() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> result = tuple.filter(t -> t._1 + t._2 + t._3 > 5.0f);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_floatTuple3_false() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 10.0f);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_doubleTuple2_true() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(3.5, 4.5);
        Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 == 8.0);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_doubleTuple2_false() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(3.5, 4.5);
        Optional<DoubleTuple2> result = tuple.filter(t -> t._1 + t._2 < 5.0);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_doubleTuple3_true() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> result = tuple.filter(t -> t._1 > 0 && t._2 > 0 && t._3 > 0);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_doubleTuple3_false() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> result = tuple.filter(t -> t._1 < 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFilter_longTuple2_true() throws Exception {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        Optional<LongTuple2> result = tuple.filter(t -> t._1 < t._2);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_longTuple2_false() throws Exception {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        Optional<LongTuple2> result = tuple.filter(t -> t._1 > t._2);
        assertFalse(result.isPresent());
    }

    // ============ ToOptional Method Tests ============

    @Test
    public void testToOptional_intTuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        Optional<IntTuple2> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_intTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_longTuple2() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        Optional<LongTuple2> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_longTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        Optional<LongTuple3> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_floatTuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        Optional<FloatTuple2> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_floatTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_doubleTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        Optional<DoubleTuple2> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    @Test
    public void testToOptional_doubleTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> opt = tuple.toOptional();
        assertTrue(opt.isPresent());
        assertEquals(tuple, opt.get());
    }

    // ============ Functional Chain Tests ============

    @Test
    public void testFunctionalChain_intTuple2() throws Exception {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<String> result = tuple.filter(t -> t._1 > 0 && t._2 > 0).map(t -> "Sum: " + (t._1 + t._2));
        assertTrue(result.isPresent());
        assertEquals("Sum: 15", result.get());
    }

    @Test
    public void testFunctionalChain_intTuple2_filtered() throws Exception {
        IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<String> result = tuple.filter(t -> t._1 < 0).map(t -> "Sum: " + (t._1 + t._2));
        assertFalse(result.isPresent());
    }

    @Test
    public void testFunctionalChain_intTuple3() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        String result = tuple.toOptional().filter(t -> t._1 + t._2 + t._3 > 5).map(t -> "Product: " + (t._1 * t._2 * t._3)).orElse("Not valid");
        assertEquals("Product: 6", result);
    }

    @Test
    public void testFunctionalChain_floatTuple2() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        Optional<Float> result = tuple.filter(t -> t._1 > 0 && t._2 > 0).map(t -> (float) Math.sqrt(t._1 * t._1 + t._2 * t._2));
        assertTrue(result.isPresent());
        assertEquals(5.0f, result.get(), 0.001f);
    }

    @Test
    public void testFunctionalChain_floatTuple3() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        String result = tuple.toOptional().filter(t -> t._1 + t._2 + t._3 > 5).map(t -> "Average: " + ((t._1 + t._2 + t._3) / 3.0f)).orElse("Not valid");
        assertEquals("Average: 2.0", result);
    }

    @Test
    public void testFunctionalChain_doubleTuple2() throws Exception {
        DoubleTuple2 tuple = DoubleTuple.of(2.5, 3.5);
        Optional<Double> result = tuple.filter(t -> t._1 < t._2).map(t -> t._1 + t._2);
        assertTrue(result.isPresent());
        assertEquals(6.0, result.get());
    }

    @Test
    public void testFunctionalChain_doubleTuple3() throws Exception {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Double result = tuple.toOptional().filter(t -> t._1 > 0 && t._2 > 0 && t._3 > 0).map(t -> (t._1 + t._2 + t._3) / 3.0).orElse(0.0);
        assertEquals(2.0, result);
    }

    // ============ Mixed Type Tests ============

    @Test
    public void testMixedTypes_intAndLongTuple() throws Exception {
        IntTuple2 intTuple = IntTuple.of(10, 20);
        LongTuple2 longTuple = LongTuple.of(100L, 200L);

        int intSum = intTuple.map(t -> t._1 + t._2);
        long longSum = longTuple.map(t -> t._1 + t._2);

        assertEquals(30, intSum);
        assertEquals(300L, longSum);
    }

    @Test
    public void testMixedTypes_floatAndDouble() throws Exception {
        FloatTuple2 floatTuple = FloatTuple.of(1.5f, 2.5f);
        DoubleTuple2 doubleTuple = DoubleTuple.of(1.5, 2.5);

        float floatResult = floatTuple.map(t -> t._1 + t._2);
        double doubleResult = doubleTuple.map(t -> t._1 + t._2);

        assertEquals(4.0f, floatResult, 0.001f);
        assertEquals(4.0, doubleResult);
    }

}
