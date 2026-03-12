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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.IntTuple.IntTuple1;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.u.Optional;

class PrimitiveTupleTest extends TestBase {

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
        assertThrows(ArithmeticException.class, () -> tuple.map(t -> t._1 / t._2)); // Division by zero
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

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_PrimitiveTuple extends TestBase {
        // ===== PrimitiveTuple Javadoc examples =====

        @Test
        public void testPrimitiveTupleArity() {
            // IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            // int size = tuple2.arity();   // returns 2
            IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertEquals(2, tuple2.arity());

            // IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            // int size3 = tuple3.arity();   // returns 3
            IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            assertEquals(3, tuple3.arity());
        }

        @Test
        public void testPrimitiveTupleMap() {
            // Calculate Euclidean distance from origin
            // IntTuple.IntTuple2 point = IntTuple.of(3, 4);
            // Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
            // distance = 5.0
            IntTuple.IntTuple2 point = IntTuple.of(3, 4);
            Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
            assertEquals(5.0, distance, 0.001);

            // Convert tuple to a formatted string
            // IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 0);
            // String hexColor = rgb.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));
            // hexColor = "#FF8000"
            IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 0);
            String hexColor = rgb.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));
            assertEquals("#FF8000", hexColor);

            // Extract a single computed value
            // DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
            // Double area = dimensions.map(t -> t._1 * t._2);
            // area = 213.15
            DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
            Double area = dimensions.map(t -> t._1 * t._2);
            assertEquals(213.15, area, 0.001);
        }

        @Test
        public void testPrimitiveTupleFilter() {
            // Filter for positive values
            // IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
            // positive.isPresent() = true
            IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
            Optional<IntTuple.IntTuple2> positive = tuple.filter(t -> t._1 > 0 && t._2 > 0);
            assertTrue(positive.isPresent());

            // Filter for negative values
            // negative.isPresent() = false
            Optional<IntTuple.IntTuple2> negative = tuple.filter(t -> t._1 < 0 || t._2 < 0);
            assertFalse(negative.isPresent());

            // Chain with other operations
            // IntTuple.IntTuple3 values = IntTuple.of(10, 20, 30);
            // result = "Sum is: 60"
            IntTuple.IntTuple3 values = IntTuple.of(10, 20, 30);
            String result = values.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");
            assertEquals("Sum is: 60", result);
        }

        @Test
        public void testPrimitiveTupleToOptional() {
            // IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            // optional.isPresent() = true
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            Optional<IntTuple.IntTuple2> optional = tuple.toOptional();
            assertTrue(optional.isPresent());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for PrimitiveTuple.
     * Tests common tuple functionality through concrete primitive tuple implementations.
     */
    @Tag("2025")
    class PrimitiveTuple2025Test extends TestBase {

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

    @Nested
    /**
     * Comprehensive test suite for PrimitiveTuple abstract base class.
     * Tests all public methods inherited by primitive tuple implementations,
     * including accept, map, filter, and toOptional methods.
     */
    @Tag("2510")
    class PrimitiveTuple2510Test extends TestBase {

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
            Optional<DoubleTuple3> result = tuple.filter(t -> t._1 >= -90 && t._1 <= 90 && // valid latitude
                    t._2 >= -180 && t._2 <= 180 // valid longitude
            );

            assertTrue(result.isPresent());
        }

        @Test
        public void testFilterChaining() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

            Optional<Float> result = tuple.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> t._1 + t._2 + t._3);

            assertTrue(result.isPresent());
            assertEquals(60.0f, result.get(), 0.001f);
        }

        @Test
        public void testFilterChainingWithEmpty() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);

            Optional<Float> result = tuple.filter(t -> t._1 + t._2 + t._3 > 100).map(t -> t._1 + t._2 + t._3);

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

            Optional<DoubleTuple2> validCoords = coords.filter(t -> t._1 >= -90 && t._1 <= 90 && // valid latitude
                    t._2 >= -180 && t._2 <= 180 // valid longitude
            );

            assertTrue(validCoords.isPresent());

            Optional<DoubleTuple2> invalidCoords = coords.filter(t -> t._1 >= 0 && t._1 <= 45 // restricted latitude
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

            String result = tuple.toOptional().filter(t -> t._1 > 0).map(t -> "Sum: " + (t._1 + t._2 + t._3)).orElse("Invalid");

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

            DoubleTuple1 result = tuple.toOptional().filter(t -> t._1 > 100).orElse(DoubleTuple.of(0.0));

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

            Optional<String> result = tuple.filter(t -> t._1 + t._2 + t._3 > 5).map(t -> "Sum is: " + (t._1 + t._2 + t._3));

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

            Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 0).filter(t -> t._2 > 15).filter(t -> t._3 > 25);

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

    @Nested
    @Tag("2511")
    class PrimitiveTuple2511Test extends TestBase {

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

    @Nested
    /**
     * Comprehensive unit tests for PrimitiveTuple abstract base class.
     * Tests are performed through concrete implementations (IntTuple, DoubleTuple, LongTuple)
     * to verify all public methods defined in PrimitiveTuple.
     */
    @Tag("2512")
    class PrimitiveTuple2512Test extends TestBase {

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

            int[] sum = new int[1];
            tuple.accept(t -> sum[0] = t._1 + t._2);

            assertEquals(3, sum[0]);
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
                tuple.map(t -> t._1 / t._2); // Division by zero
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
        public void test_filter_nocountMatchBetweeningPredicate() {
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
                tuple.filter(t -> t._1 / t._2 > 5); // Division by zero
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

}
