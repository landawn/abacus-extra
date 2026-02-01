/*
 * Copyright (C) 2024 HaiYang Li
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.u.Optional;

/**
 * Test class for PrimitiveTuple abstract base class.
 * Tests the common functionality through concrete implementations (IntTuple, LongTuple, etc.)
 */
public class PrimitiveTupleTest extends TestBase {

    @Test
    public void testArityThroughIntTuple() {
        IntTuple.IntTuple1 tuple1 = IntTuple.of(10);
        IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
        IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 30);
        IntTuple.IntTuple4 tuple4 = IntTuple.of(10, 20, 30, 40);

        assertEquals(1, tuple1.arity());
        assertEquals(2, tuple2.arity());
        assertEquals(3, tuple3.arity());
        assertEquals(4, tuple4.arity());
    }

    @Test
    public void testArityThroughLongTuple() {
        LongTuple.LongTuple1 tuple1 = LongTuple.of(10L);
        LongTuple.LongTuple2 tuple2 = LongTuple.of(10L, 20L);

        assertEquals(1, tuple1.arity());
        assertEquals(2, tuple2.arity());
    }

    @Test
    public void testArityThroughDoubleTuple() {
        DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(10.5);
        DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(10.5, 20.7);

        assertEquals(1, tuple1.arity());
        assertEquals(2, tuple2.arity());
    }

    @Test
    public void testAcceptWithIntTuple() {
        IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
        AtomicInteger sum = new AtomicInteger(0);

        tuple.accept(t -> sum.set(t._1 + t._2));

        assertEquals(15, sum.get());
    }

    @Test
    public void testAcceptWithLongTuple() {
        LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);
        AtomicInteger product = new AtomicInteger(0);

        tuple.accept(t -> product.set((int) (t._1 * t._2)));

        assertEquals(20000, product.get());
    }

    @Test
    public void testAcceptWithDoubleTuple() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(2.5, 4.0);
        AtomicBoolean wasExecuted = new AtomicBoolean(false);

        tuple.accept(t -> {
            assertTrue(t._1 > 2.0);
            assertTrue(t._2 > 3.0);
            wasExecuted.set(true);
        });

        assertTrue(wasExecuted.get());
    }

    @Test
    public void testMapWithIntTuple() {
        IntTuple.IntTuple2 tuple = IntTuple.of(3, 4);

        String result = tuple.map(t -> "Sum: " + (t._1 + t._2));
        assertEquals("Sum: 7", result);

        Integer product = tuple.map(t -> t._1 * t._2);
        assertEquals(12, product);

        Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
        assertEquals(5.0, distance, 0.001);
    }

    @Test
    public void testMapWithLongTuple() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);

        String result = tuple.map(t -> String.format("Values: %d, %d, %d", t._1, t._2, t._3));
        assertEquals("Values: 100, 200, 300", result);

        Long sum = tuple.map(t -> t._1 + t._2 + t._3);
        assertEquals(600L, sum);
    }

    @Test
    public void testMapWithDoubleTuple() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);

        Double average = tuple.map(t -> (t._1 + t._2) / 2.0);
        assertEquals(2.0, average, 0.001);

        Boolean allPositive = tuple.map(t -> t._1 > 0 && t._2 > 0);
        assertTrue(allPositive);
    }

    @Test
    public void testFilterWithIntTuple() {
        IntTuple.IntTuple2 positivesTuple = IntTuple.of(5, 10);
        IntTuple.IntTuple2 mixedTuple = IntTuple.of(-5, 10);

        Optional<IntTuple.IntTuple2> positiveResult = positivesTuple.filter(t -> t._1 > 0 && t._2 > 0);
        Optional<IntTuple.IntTuple2> mixedResult = mixedTuple.filter(t -> t._1 > 0 && t._2 > 0);

        assertTrue(positiveResult.isPresent());
        assertFalse(mixedResult.isPresent());

        assertSame(positivesTuple, positiveResult.get());
    }

    @Test
    public void testFilterWithLongTuple() {
        LongTuple.LongTuple3 largeTuple = LongTuple.of(1000L, 2000L, 3000L);
        LongTuple.LongTuple3 smallTuple = LongTuple.of(1L, 2L, 3L);

        Optional<LongTuple.LongTuple3> largeResult = largeTuple.filter(t -> t._1 > 100 && t._2 > 100 && t._3 > 100);
        Optional<LongTuple.LongTuple3> smallResult = smallTuple.filter(t -> t._1 > 100 && t._2 > 100 && t._3 > 100);

        assertTrue(largeResult.isPresent());
        assertFalse(smallResult.isPresent());
    }

    @Test
    public void testFilterWithDoubleTuple() {
        DoubleTuple.DoubleTuple2 positivesTuple = DoubleTuple.of(1.5, 2.7);
        DoubleTuple.DoubleTuple2 negativesTuple = DoubleTuple.of(-1.5, -2.7);

        Optional<DoubleTuple.DoubleTuple2> positiveResult = positivesTuple.filter(t -> t._1 > 0 && t._2 > 0);
        Optional<DoubleTuple.DoubleTuple2> negativeResult = negativesTuple.filter(t -> t._1 > 0 && t._2 > 0);

        assertTrue(positiveResult.isPresent());
        assertFalse(negativeResult.isPresent());
    }

    @Test
    public void testToOptionalWithIntTuple() {
        IntTuple.IntTuple1 tuple = IntTuple.of(42);

        Optional<IntTuple.IntTuple1> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertSame(tuple, optional.get());
        assertEquals(42, optional.get()._1);
    }

    @Test
    public void testToOptionalWithLongTuple() {
        LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);

        Optional<LongTuple.LongTuple2> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertSame(tuple, optional.get());
        assertEquals(100L, optional.get()._1);
        assertEquals(200L, optional.get()._2);
    }

    @Test
    public void testToOptionalWithDoubleTuple() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.1, 2.2, 3.3);

        Optional<DoubleTuple.DoubleTuple3> optional = tuple.toOptional();

        assertTrue(optional.isPresent());
        assertSame(tuple, optional.get());
        assertEquals(1.1, optional.get()._1, 0.001);
        assertEquals(2.2, optional.get()._2, 0.001);
        assertEquals(3.3, optional.get()._3, 0.001);
    }

    @Test
    public void testChainedOperationsWithFilterFailure() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);

        Optional<String> result = tuple.filter(t -> t._1 > 10) // This will fail
                .map(t -> "Large values: " + t._1 + ", " + t._2);

        assertFalse(result.isPresent());
    }

    @Test
    public void testExceptionHandlingInOperations() {
        IntTuple.IntTuple2 tuple = IntTuple.of(10, 0);

        // Test that exceptions thrown in lambda functions are properly propagated
        try {
            tuple.map(t -> t._1 / t._2); // Division by zero
            assertTrue(false, "Should have thrown ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testMultipleTupleTypesInteraction() {
        // Test that different tuple types work correctly with common interface
        IntTuple.IntTuple2 intTuple = IntTuple.of(10, 20);
        LongTuple.LongTuple2 longTuple = LongTuple.of(100L, 200L);
        DoubleTuple.DoubleTuple2 doubleTuple = DoubleTuple.of(1.5, 2.5);

        assertEquals(2, intTuple.arity());
        assertEquals(2, longTuple.arity());
        assertEquals(2, doubleTuple.arity());

        String intResult = intTuple.map(t -> "int: " + (t._1 + t._2));
        String longResult = longTuple.map(t -> "long: " + (t._1 + t._2));
        String doubleResult = doubleTuple.map(t -> "double: " + (t._1 + t._2));

        assertEquals("int: 30", intResult);
        assertEquals("long: 300", longResult);
        assertEquals("double: 4.0", doubleResult);
    }
}
