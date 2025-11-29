/*
 * Copyright (C) 2020 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.IntTuple.IntTuple1;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for PrimitiveTuple abstract base class.
 * Tests are performed through concrete implementations (IntTuple, DoubleTuple, LongTuple)
 * to verify all public methods defined in PrimitiveTuple.
 */
@Tag("2512")
public class PrimitiveTuple2512Test extends TestBase {

    // ============================================
    // Tests for arity() method
    // ============================================

    @Test
    public void test_arity_tuple1() {
        IntTuple1 tuple = IntTuple.of(10);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_arity_tuple2() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_arity_tuple3() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_arity_doubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_arity_longTuple() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_arity_consistentAcrossCalls() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int arity1 = tuple.arity();
        int arity2 = tuple.arity();
        int arity3 = tuple.arity();

        assertEquals(arity1, arity2);
        assertEquals(arity2, arity3);
    }

    @Test
    public void test_arity_alwaysPositive() {
        IntTuple1 tuple1 = IntTuple.of(5);
        IntTuple2 tuple2 = IntTuple.of(5, 10);
        IntTuple3 tuple3 = IntTuple.of(5, 10, 15);

        assertTrue(tuple1.arity() > 0);
        assertTrue(tuple2.arity() > 0);
        assertTrue(tuple3.arity() > 0);
    }

    // ============================================
    // Tests for accept() method
    // ============================================

    @Test
    public void test_accept_executesAction() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        AtomicInteger sum = new AtomicInteger(0);

        tuple.accept(t -> sum.set(t._1 + t._2));

        assertEquals(30, sum.get());
    }

    @Test
    public void test_accept_withSideEffect() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        StringBuilder sb = new StringBuilder();

        tuple.accept(t -> sb.append(t._1).append("-").append(t._2).append("-").append(t._3));

        assertEquals("1-2-3", sb.toString());
    }

    @Test
    public void test_accept_withMultipleCalls() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        AtomicInteger callCount = new AtomicInteger(0);

        tuple.accept(t -> callCount.incrementAndGet());
        tuple.accept(t -> callCount.incrementAndGet());
        tuple.accept(t -> callCount.incrementAndGet());

        assertEquals(3, callCount.get());
    }

    @Test
    public void test_accept_withException() {
        IntTuple2 tuple = IntTuple.of(10, 0);

        assertThrows(ArithmeticException.class, () -> {
            tuple.accept(t -> {
                @SuppressWarnings("unused")
                int result = t._1 / t._2; // Division by zero
            });
        });
    }

    @Test
    public void test_accept_withNull() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        assertThrows(NullPointerException.class, () -> {
            tuple.accept((Throwables.IntBiConsumer<RuntimeException>) null);
        });
    }

    @Test
    public void test_accept_accessesAllFields() {
        IntTuple3 tuple = IntTuple.of(100, 200, 300);
        int[] values = new int[3];

        tuple.accept(t -> {
            values[0] = t._1;
            values[1] = t._2;
            values[2] = t._3;
        });

        assertEquals(100, values[0]);
        assertEquals(200, values[1]);
        assertEquals(300, values[2]);
    }

    @Test
    public void test_accept_returnsVoid() {
        IntTuple2 tuple = IntTuple.of(1, 2);

        // Should compile and not return anything
        tuple.accept(t -> System.out.println(t._1 + t._2));
    }

    @Test
    public void test_accept_doubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(3.14, 2.71);
        double[] result = new double[1];

        tuple.accept(t -> result[0] = t._1 + t._2);

        assertEquals(5.85, result[0], 0.001);
    }

    @Test
    public void test_accept_longTuple() {
        LongTuple2 tuple = LongTuple.of(1000L, 2000L);
        long[] result = new long[1];

        tuple.accept(t -> result[0] = t._1 * t._2);

        assertEquals(2000000L, result[0]);
    }

    // ============================================
    // Tests for map() method
    // ============================================

    @Test
    public void test_map_transformsToString() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        String result = tuple.map(t -> "Values: " + t._1 + ", " + t._2);

        assertEquals("Values: 10, 20", result);
    }

    @Test
    public void test_map_transformsToNumber() {
        IntTuple2 tuple = IntTuple.of(3, 4);

        Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));

        assertEquals(5.0, distance, 0.001);
    }

    @Test
    public void test_map_transformsToBoolean() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Boolean result = tuple.map(t -> t._1 + t._2 > 25);

        assertTrue(result);
    }

    @Test
    public void test_map_transformsToObject() {
        IntTuple3 tuple = IntTuple.of(255, 128, 0);

        String hexColor = tuple.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));

        assertEquals("#FF8000", hexColor);
    }

    @Test
    public void test_map_canReturnNull() {
        IntTuple2 tuple = IntTuple.of(0, 0);

        String result = tuple.map(t -> t._1 == 0 ? null : "Non-zero");

        assertEquals(null, result);
    }

    @Test
    public void test_map_withException() {
        IntTuple2 tuple = IntTuple.of(10, 0);

        assertThrows(ArithmeticException.class, () -> {
            tuple.map(t -> t._1 / t._2);   // Division by zero
        });
    }

    @Test
    public void test_map_withNull() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        assertThrows(NullPointerException.class, () -> {
            tuple.map((Throwables.IntBiFunction<String, RuntimeException>) null);
        });
    }

    @Test
    public void test_map_accessesAllFields() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);

        Integer sum = tuple.map(t -> t._1 + t._2 + t._3);

        assertEquals(6, sum);
    }

    @Test
    public void test_map_doubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(10.5, 20.3);

        Double area = tuple.map(t -> t._1 * t._2);

        assertEquals(213.15, area, 0.001);
    }

    @Test
    public void test_map_longTuple() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);

        String result = tuple.map(t -> "Sum: " + (t._1 + t._2));

        assertEquals("Sum: 300", result);
    }

    @Test
    public void test_map_multipleOperations() {
        IntTuple2 tuple = IntTuple.of(5, 10);

        String result1 = tuple.map(t -> "First: " + t._1);
        String result2 = tuple.map(t -> "Second: " + t._2);
        String result3 = tuple.map(t -> "Sum: " + (t._1 + t._2));

        assertEquals("First: 5", result1);
        assertEquals("Second: 10", result2);
        assertEquals("Sum: 15", result3);
    }

    // ============================================
    // Tests for filter() method
    // ============================================

    @Test
    public void test_filter_matchingPredicate() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.filter(t -> t._1 + t._2 > 25);

        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_filter_nonMatchingPredicate() {
        IntTuple2 tuple = IntTuple.of(5, 10);

        Optional<IntTuple2> result = tuple.filter(t -> t._1 + t._2 > 100);

        assertFalse(result.isPresent());
    }

    @Test
    public void test_filter_withPositiveValues() {
        IntTuple2 tuple = IntTuple.of(5, 10);

        Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);

        assertTrue(result.isPresent());
    }

    @Test
    public void test_filter_withNegativeValues() {
        IntTuple2 tuple = IntTuple.of(-5, -10);

        Optional<IntTuple2> result = tuple.filter(t -> t._1 > 0);

        assertFalse(result.isPresent());
    }

    @Test
    public void test_filter_withException() {
        IntTuple2 tuple = IntTuple.of(10, 0);

        assertThrows(ArithmeticException.class, () -> {
            tuple.filter(t -> t._1 / t._2 > 5);   // Division by zero
        });
    }

    @Test
    public void test_filter_withNull() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        assertThrows(NullPointerException.class, () -> {
            tuple.filter((Throwables.IntBiPredicate<RuntimeException>) null);
        });
    }

    @Test
    public void test_filter_complexCondition() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);

        Optional<IntTuple3> result = tuple.filter(t -> t._1 < t._2 && t._2 < t._3 && (t._1 + t._2 + t._3) == 60);

        assertTrue(result.isPresent());
    }

    @Test
    public void test_filter_doubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(45.5, -122.6);

        Optional<DoubleTuple2> result = tuple.filter(t -> t._1 >= -90 && t._1 <= 90 && t._2 >= -180 && t._2 <= 180);

        assertTrue(result.isPresent());
    }

    @Test
    public void test_filter_longTuple() {
        LongTuple2 tuple = LongTuple.of(1000L, 2000L);

        Optional<LongTuple2> result = tuple.filter(t -> t._1 + t._2 > 2500L);

        assertTrue(result.isPresent());
    }

    @Test
    public void test_filter_chainWithMap() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);

        String result = tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");

        assertEquals("Sum is: 60", result);
    }

    @Test
    public void test_filter_chainWithMapNoMatch() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);

        String result = tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");

        assertEquals("Sum too small", result);
    }

    @Test
    public void test_filter_alwaysTrue() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.filter(t -> true);

        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_filter_alwaysFalse() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.filter(t -> false);

        assertFalse(result.isPresent());
    }

    // ============================================
    // Tests for toOptional() method
    // ============================================

    @Test
    public void test_toOptional_isPresent() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.toOptional();

        assertTrue(result.isPresent());
    }

    @Test
    public void test_toOptional_containsSameTuple() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.toOptional();

        assertEquals(tuple, result.get());
    }

    @Test
    public void test_toOptional_neverEmpty() {
        IntTuple1 tuple1 = IntTuple.of(1);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        IntTuple3 tuple3 = IntTuple.of(1, 2, 3);

        assertTrue(tuple1.toOptional().isPresent());
        assertTrue(tuple2.toOptional().isPresent());
        assertTrue(tuple3.toOptional().isPresent());
    }

    @Test
    public void test_toOptional_chainWithFilter() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.toOptional().filter(t -> t._1 > 5);

        assertTrue(result.isPresent());
    }

    @Test
    public void test_toOptional_chainWithMap() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);

        String result = tuple.toOptional().map(t -> "Coordinates: " + t._1 + ", " + t._2 + ", " + t._3).orElse("No coordinates");

        assertEquals("Coordinates: 10, 20, 30", result);
    }

    @Test
    public void test_toOptional_ifPresent() {
        DoubleTuple2 tuple = DoubleTuple.of(3.14, 2.71);
        AtomicBoolean executed = new AtomicBoolean(false);

        tuple.toOptional().ifPresent(t -> executed.set(true));

        assertTrue(executed.get());
    }

    @Test
    public void test_toOptional_orElseNotCalled() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        IntTuple2 result = tuple.toOptional().orElse(IntTuple.of(0, 0));

        assertEquals(tuple, result);
    }

    @Test
    public void test_toOptional_doubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);

        Optional<DoubleTuple2> result = tuple.toOptional();

        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_toOptional_longTuple() {
        LongTuple2 tuple = LongTuple.of(100L, 200L);

        Optional<LongTuple2> result = tuple.toOptional();

        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_toOptional_multipleCalls() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result1 = tuple.toOptional();
        Optional<IntTuple2> result2 = tuple.toOptional();

        assertEquals(result1.get(), result2.get());
    }

    @Test
    public void test_toOptional_notNull() {
        IntTuple2 tuple = IntTuple.of(10, 20);

        Optional<IntTuple2> result = tuple.toOptional();

        assertNotNull(result);
    }

    // ============================================
    // Integration tests - testing combinations
    // ============================================

    @Test
    public void test_integration_filterMapAccept() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        AtomicInteger result = new AtomicInteger(0);

        tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> t._1 + t._2 + t._3).ifPresent(sum -> result.set(sum));

        assertEquals(60, result.get());
    }

    @Test
    public void test_integration_toOptionalFilterMap() {
        IntTuple2 tuple = IntTuple.of(5, 10);

        String result = tuple.toOptional().filter(t -> t._1 > 0).map(t -> "Positive: " + t._1).orElse("Not positive");

        assertEquals("Positive: 5", result);
    }

    @Test
    public void test_integration_multipleMapOperations() {
        IntTuple2 tuple = IntTuple.of(3, 4);

        // Calculate Pythagorean distance
        Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));

        // Map again to format
        String formatted = tuple.map(t -> String.format("Distance from (%d,%d) is %.1f", t._1, t._2, distance));

        assertEquals("Distance from (3,4) is 5.0", formatted);
    }

    @Test
    public void test_integration_acceptThenMap() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        StringBuilder log = new StringBuilder();

        // Accept for logging
        tuple.accept(t -> log.append("Processing: ").append(t._1).append(", ").append(t._2));

        // Map for calculation
        Integer sum = tuple.map(t -> t._1 + t._2);

        assertEquals("Processing: 10, 20", log.toString());
        assertEquals(30, sum);
    }

    @Test
    public void test_integration_complexChain() {
        IntTuple3 tuple = IntTuple.of(100, 200, 300);

        Optional<IntTuple3> filtered = tuple.filter(t -> t._1 < t._2 && t._2 < t._3);
        String result = filtered.isPresent() ? "Total: " + (filtered.get()._1 + filtered.get()._2 + filtered.get()._3) : "Invalid sequence";

        assertEquals("Total: 600", result);
    }

    @Test
    public void test_integration_arityConsistency() {
        IntTuple1 tuple1 = IntTuple.of(1);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        IntTuple3 tuple3 = IntTuple.of(1, 2, 3);

        assertEquals(1, tuple1.arity());
        assertEquals(2, tuple2.arity());
        assertEquals(3, tuple3.arity());

        // Arity should be consistent even after operations
        tuple1.filter(t -> true);
        tuple2.map(t -> t._1 + t._2);
        tuple3.toOptional();

        assertEquals(1, tuple1.arity());
        assertEquals(2, tuple2.arity());
        assertEquals(3, tuple3.arity());
    }

    // ============================================
    // Edge case tests
    // ============================================

    @Test
    public void test_edgeCase_zeroValues() {
        IntTuple2 tuple = IntTuple.of(0, 0);

        tuple.accept(t -> {
            assertEquals(0, t._1);
            assertEquals(0, t._2);
        });

        Integer sum = tuple.map(t -> t._1 + t._2);
        assertEquals(0, sum);
    }

    @Test
    public void test_edgeCase_negativeValues() {
        IntTuple2 tuple = IntTuple.of(-10, -20);

        Integer sum = tuple.map(t -> t._1 + t._2);

        assertEquals(-30, sum);
    }

    @Test
    public void test_edgeCase_mixedSignValues() {
        IntTuple3 tuple = IntTuple.of(-10, 0, 10);

        Integer sum = tuple.map(t -> t._1 + t._2 + t._3);

        assertEquals(0, sum);
    }

    @Test
    public void test_edgeCase_maxIntValues() {
        IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE);

        tuple.accept(t -> {
            assertEquals(Integer.MAX_VALUE, t._1);
            assertEquals(Integer.MAX_VALUE, t._2);
        });
    }

    @Test
    public void test_edgeCase_minIntValues() {
        IntTuple2 tuple = IntTuple.of(Integer.MIN_VALUE, Integer.MIN_VALUE);

        tuple.accept(t -> {
            assertEquals(Integer.MIN_VALUE, t._1);
            assertEquals(Integer.MIN_VALUE, t._2);
        });
    }

    @Test
    public void test_edgeCase_maxDoubleValues() {
        DoubleTuple2 tuple = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE);

        tuple.accept(t -> {
            assertEquals(Double.MAX_VALUE, t._1);
            assertEquals(Double.MAX_VALUE, t._2);
        });
    }

    @Test
    public void test_edgeCase_minDoubleValues() {
        DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE);

        tuple.accept(t -> {
            assertEquals(Double.MIN_VALUE, t._1);
            assertEquals(Double.MIN_VALUE, t._2);
        });
    }

    @Test
    public void test_edgeCase_maxLongValues() {
        LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE);

        tuple.accept(t -> {
            assertEquals(Long.MAX_VALUE, t._1);
            assertEquals(Long.MAX_VALUE, t._2);
        });
    }

    @Test
    public void test_edgeCase_minLongValues() {
        LongTuple2 tuple = LongTuple.of(Long.MIN_VALUE, Long.MIN_VALUE);

        tuple.accept(t -> {
            assertEquals(Long.MIN_VALUE, t._1);
            assertEquals(Long.MIN_VALUE, t._2);
        });
    }
}
