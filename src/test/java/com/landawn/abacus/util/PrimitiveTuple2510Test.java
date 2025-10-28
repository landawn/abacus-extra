/*
 * Copyright (C) 2025 HaiYang Li
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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive test suite for PrimitiveTuple abstract base class.
 * Tests all public methods inherited by primitive tuple implementations,
 * including accept, map, filter, and toOptional methods.
 */
@Tag("2510")
public class PrimitiveTuple2510Test extends TestBase {

    // arity() tests - inherited method tested through concrete implementations
    @Test
    public void testArity() {
        FloatTuple1 tuple1 = FloatTuple.of(1.0f);
        assertEquals(1, tuple1.arity());

        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(2, tuple2.arity());

        FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(3, tuple3.arity());
    }

    // accept() tests
    @Test
    public void testAcceptFloatTuple() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        final List<Float> values = new ArrayList<>();

        tuple.accept(t -> {
            values.add(t._1);
            values.add(t._2);
        });

        assertEquals(2, values.size());
        assertEquals(3.0f, values.get(0), 0.001f);
        assertEquals(4.0f, values.get(1), 0.001f);
    }

    @Test
    public void testAcceptDoubleTuple() {
        DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
        final List<Double> values = new ArrayList<>();

        tuple.accept(t -> {
            values.add(t._1);
            values.add(t._2);
        });

        assertEquals(2, values.size());
        assertEquals(10.0, values.get(0), 0.001);
        assertEquals(20.0, values.get(1), 0.001);
    }

    @Test
    public void testAcceptWithSideEffects() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        final float[] sum = { 0.0f };

        tuple.accept(t -> {
            sum[0] = t._1 + t._2 + t._3;
        });

        assertEquals(6.0f, sum[0], 0.001f);
    }

    @Test
    public void testAcceptWithException() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);

        assertThrows(IllegalArgumentException.class, () -> {
            tuple.accept(t -> {
                if (t._1 > 0) {
                    throw new IllegalArgumentException("Test exception");
                }
            });
        });
    }

    // map() tests
    @Test
    public void testMapToString() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        String result = tuple.map(t -> "Values: " + t._1 + ", " + t._2);

        assertEquals("Values: 3.0, 4.0", result);
    }

    @Test
    public void testMapToNumber() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Double sum = tuple.map(t -> t._1 + t._2 + t._3);

        assertEquals(6.0, sum, 0.001);
    }

    @Test
    public void testMapToObject() {
        FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
        List<Float> result = tuple.map(t -> {
            List<Float> list = new ArrayList<>();
            list.add(t._1);
            list.add(t._2);
            return list;
        });

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10.0f, result.get(0), 0.001f);
        assertEquals(20.0f, result.get(1), 0.001f);
    }

    @Test
    public void testMapWithComplexCalculation() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));

        assertEquals(5.0, distance, 0.001);
    }

    @Test
    public void testMapReturnsNull() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        Object result = tuple.map(t -> null);

        assertEquals(null, result);
    }

    @Test
    public void testMapWithException() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);

        assertThrows(ArithmeticException.class, () -> {
            tuple.map(t -> {
                if (t._1 > 0) {
                    throw new ArithmeticException("Test exception");
                }
                return 0;
            });
        });
    }

    // filter() tests
    @Test
    public void testFilterPresentWhenTrue() {
        FloatTuple2 tuple = FloatTuple.of(5.0f, 10.0f);
        Optional<FloatTuple2> result = tuple.filter(t -> t._1 > 0 && t._2 > 0);

        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilterEmptyWhenFalse() {
        FloatTuple2 tuple = FloatTuple.of(5.0f, 10.0f);
        Optional<FloatTuple2> result = tuple.filter(t -> t._1 < 0);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFilterWithComplexPredicate() {
        DoubleTuple3 tuple = DoubleTuple.of(45.5, -122.6, 100.0);
        Optional<DoubleTuple3> result = tuple.filter(t ->
            t._1 >= -90 && t._1 <= 90 &&  // valid latitude
            t._2 >= -180 && t._2 <= 180   // valid longitude
        );

        assertTrue(result.isPresent());
    }

    @Test
    public void testFilterChaining() {
        FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

        Optional<Float> result = tuple
            .filter(t -> t._1 + t._2 + t._3 > 50)
            .map(t -> t._1 + t._2 + t._3);

        assertTrue(result.isPresent());
        assertEquals(60.0f, result.get(), 0.001f);
    }

    @Test
    public void testFilterChainingWithEmpty() {
        FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

        Optional<Float> result = tuple
            .filter(t -> t._1 + t._2 + t._3 > 100)
            .map(t -> t._1 + t._2 + t._3);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFilterWithException() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);

        assertThrows(IllegalStateException.class, () -> {
            tuple.filter(t -> {
                if (t._1 > 0) {
                    throw new IllegalStateException("Test exception");
                }
                return true;
            });
        });
    }

    @Test
    public void testFilterRangeValidation() {
        DoubleTuple2 coords = DoubleTuple.of(45.5, -122.6);

        Optional<DoubleTuple2> validCoords = coords.filter(t ->
            t._1 >= -90 && t._1 <= 90 &&  // valid latitude
            t._2 >= -180 && t._2 <= 180   // valid longitude
        );

        assertTrue(validCoords.isPresent());

        Optional<DoubleTuple2> invalidCoords = coords.filter(t ->
            t._1 >= 0 && t._1 <= 45  // restricted latitude
        );

        assertFalse(invalidCoords.isPresent());
    }

    // toOptional() tests
    @Test
    public void testToOptionalAlwaysPresent() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        Optional<FloatTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertEquals(tuple, optional.get());
    }

    @Test
    public void testToOptionalWithChaining() {
        DoubleTuple3 tuple = DoubleTuple.of(10.0, 20.0, 30.0);

        String result = tuple.toOptional()
            .filter(t -> t._1 > 0)
            .map(t -> "Sum: " + (t._1 + t._2 + t._3))
            .orElse("Invalid");

        assertEquals("Sum: 60.0", result);
    }

    @Test
    public void testToOptionalWithIfPresent() {
        FloatTuple2 tuple = FloatTuple.of(3.14f, 2.71f);
        final List<Float> values = new ArrayList<>();

        tuple.toOptional().ifPresent(t -> {
            values.add(t._1);
            values.add(t._2);
        });

        assertEquals(2, values.size());
        assertEquals(3.14f, values.get(0), 0.001f);
        assertEquals(2.71f, values.get(1), 0.001f);
    }

    @Test
    public void testToOptionalWithOrElse() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);

        DoubleTuple1 result = tuple.toOptional()
            .filter(t -> t._1 > 100)
            .orElse(DoubleTuple.of(0.0));

        assertEquals(0.0, result._1, 0.001);
    }

    // Combined operations tests
    @Test
    public void testAcceptThenMap() {
        FloatTuple2 tuple = FloatTuple.of(5.0f, 10.0f);
        final List<Float> collected = new ArrayList<>();

        tuple.accept(t -> {
            collected.add(t._1);
            collected.add(t._2);
        });

        Float sum = tuple.map(t -> t._1 + t._2);

        assertEquals(2, collected.size());
        assertEquals(15.0f, sum, 0.001f);
    }

    @Test
    public void testFilterThenMapThenAccept() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);

        Optional<String> result = tuple
            .filter(t -> t._1 + t._2 + t._3 > 5)
            .map(t -> "Sum is: " + (t._1 + t._2 + t._3));

        result.ifPresent(s -> assertEquals("Sum is: 6.0", s));
        assertTrue(result.isPresent());
    }

    @Test
    public void testComplexFunctionalPipeline() {
        FloatTuple3 coords = FloatTuple.of(10.0f, 20.0f, 30.0f);

        String description = coords.toOptional()
            .filter(t -> t._1 > 0 && t._2 > 0 && t._3 > 0)
            .map(t -> String.format("Coordinates: (%.1f, %.1f, %.1f)", t._1, t._2, t._3))
            .orElse("Invalid coordinates");

        assertEquals("Coordinates: (10.0, 20.0, 30.0)", description);
    }

    // Multiple tuple type tests
    @Test
    public void testFloatAndDoubleTupleOperations() {
        FloatTuple2 floatTuple = FloatTuple.of(1.5f, 2.5f);
        DoubleTuple2 doubleTuple = DoubleTuple.of(1.5, 2.5);

        Float floatSum = floatTuple.map(t -> t._1 + t._2);
        Double doubleSum = doubleTuple.map(t -> t._1 + t._2);

        assertEquals(4.0f, floatSum, 0.001f);
        assertEquals(4.0, doubleSum, 0.001);
    }

    @Test
    public void testDifferentArityOperations() {
        FloatTuple1 tuple1 = FloatTuple.of(5.0f);
        FloatTuple2 tuple2 = FloatTuple.of(5.0f, 10.0f);
        FloatTuple3 tuple3 = FloatTuple.of(5.0f, 10.0f, 15.0f);

        assertEquals(1, tuple1.arity());
        assertEquals(2, tuple2.arity());
        assertEquals(3, tuple3.arity());

        Float sum1 = tuple1.map(t -> t._1);
        Float sum2 = tuple2.map(t -> t._1 + t._2);
        Float sum3 = tuple3.map(t -> t._1 + t._2 + t._3);

        assertEquals(5.0f, sum1, 0.001f);
        assertEquals(15.0f, sum2, 0.001f);
        assertEquals(30.0f, sum3, 0.001f);
    }

    // Edge cases
    @Test
    public void testWithZeroValues() {
        DoubleTuple2 tuple = DoubleTuple.of(0.0, 0.0);

        assertTrue(tuple.filter(t -> t._1 == 0.0).isPresent());
        assertEquals(0.0, tuple.map(t -> t._1 + t._2), 0.001);
    }

    @Test
    public void testWithNegativeValues() {
        FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);

        assertTrue(tuple.filter(t -> t._1 < 0 && t._2 < 0 && t._3 < 0).isPresent());
        assertEquals(-6.0f, tuple.map(t -> t._1 + t._2 + t._3), 0.001f);
    }

    @Test
    public void testWithMixedSignValues() {
        DoubleTuple3 tuple = DoubleTuple.of(-5.0, 0.0, 5.0);

        assertTrue(tuple.filter(t -> t._2 == 0.0).isPresent());
        assertEquals(0.0, tuple.map(t -> t._1 + t._2 + t._3), 0.001);
    }

    @Test
    public void testMultipleFiltersInSequence() {
        FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

        Optional<FloatTuple3> result = tuple
            .filter(t -> t._1 > 0)
            .filter(t -> t._2 > 15)
            .filter(t -> t._3 > 25);

        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testMapTransformations() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);

        // Test various map transformations
        Double product = tuple.map(t -> t._1 * t._2);
        assertEquals(12.0, product, 0.001);

        Double division = tuple.map(t -> t._2 / t._1);
        assertEquals(4.0 / 3.0, division, 0.001);

        Boolean comparison = tuple.map(t -> t._1 < t._2);
        assertTrue(comparison);
    }
}
