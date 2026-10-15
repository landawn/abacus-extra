package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SmallTupleContractTest {

    @Nested
    class AggregateTests {

        @Test
        void byteAggregatesMatchAnIndependentOracleAtEveryArityWithoutCaching() {
            for (int arity = 1; arity <= 9; arity++) {
                for (final int[] input : aggregateInputs(arity, Byte.MIN_VALUE, Byte.MAX_VALUE)) {
                    final byte[] values = new byte[arity];

                    for (int i = 0; i < arity; i++) {
                        values[i] = (byte) input[i];
                    }

                    final ByteTuple<?> tuple = ByteTuple.from(values);
                    final int[] sorted = input.clone();
                    java.util.Arrays.sort(sorted);
                    final int sum = java.util.Arrays.stream(input).sum();

                    assertEquals(sorted[0], tuple.min());
                    assertEquals(sorted[arity - 1], tuple.max());
                    assertEquals(sum, tuple.sum());
                    assertEquals((double) sum / arity, tuple.average().getAsDouble());
                    assertEquals((sorted[(arity - 1) / 2] + sorted[arity / 2]) / 2d, tuple.median());
                    assertEquals(sorted[(arity - 1) / 2], tuple.lowerMedian());
                    assertNull(tuple.elements, "Scalar aggregates must not retain an array cache");
                    assertArrayEquals(values, tuple.toArray());
                    assertArrayEquals(values, tuple.stream().toArray());
                }
            }
        }

        @Test
        void charAggregatesMatchAnIndependentUnsignedOracleAtEveryArityWithoutCaching() {
            for (int arity = 1; arity <= 9; arity++) {
                for (final int[] input : aggregateInputs(arity, Character.MIN_VALUE, Character.MAX_VALUE)) {
                    final char[] values = new char[arity];

                    for (int i = 0; i < arity; i++) {
                        values[i] = (char) input[i];
                    }

                    final CharTuple<?> tuple = CharTuple.from(values);
                    final int[] sorted = input.clone();
                    java.util.Arrays.sort(sorted);
                    final int sum = java.util.Arrays.stream(input).sum();

                    assertEquals(sorted[0], tuple.min());
                    assertEquals(sorted[arity - 1], tuple.max());
                    assertEquals(sum, tuple.sum());
                    assertEquals((double) sum / arity, tuple.average().getAsDouble());
                    assertEquals(sorted[(arity - 1) / 2], tuple.lowerMedian());
                    assertNull(tuple.elements, "Scalar aggregates must not retain an array cache");
                    assertArrayEquals(values, tuple.toArray());
                    assertArrayEquals(values, tuple.stream().toArray());
                }
            }
        }

        @Test
        void shortAggregatesMatchAnIndependentOracleAtEveryArityWithoutCaching() {
            for (int arity = 1; arity <= 9; arity++) {
                for (final int[] input : aggregateInputs(arity, Short.MIN_VALUE, Short.MAX_VALUE)) {
                    final short[] values = new short[arity];

                    for (int i = 0; i < arity; i++) {
                        values[i] = (short) input[i];
                    }

                    final ShortTuple<?> tuple = ShortTuple.from(values);
                    final int[] sorted = input.clone();
                    java.util.Arrays.sort(sorted);
                    final int sum = java.util.Arrays.stream(input).sum();

                    assertEquals(sorted[0], tuple.min());
                    assertEquals(sorted[arity - 1], tuple.max());
                    assertEquals(sum, tuple.sum());
                    assertEquals((double) sum / arity, tuple.average().getAsDouble());
                    assertEquals((sorted[(arity - 1) / 2] + sorted[arity / 2]) / 2d, tuple.median());
                    assertEquals(sorted[(arity - 1) / 2], tuple.lowerMedian());
                    assertNull(tuple.elements, "Scalar aggregates must not retain an array cache");
                    assertArrayEquals(values, tuple.toArray());
                    assertArrayEquals(values, tuple.stream().toArray());
                }
            }
        }
    }

    @Nested
    class MedianTests {

        @Test
        void medianLeavesPreviouslyCachedElementsInTheirOriginalOrder() {
            final byte[] byteValues = { Byte.MAX_VALUE, Byte.MIN_VALUE, 2, -2, 0, 3, -3, 1, -1 };
            final short[] shortValues = { Short.MAX_VALUE, Short.MIN_VALUE, 2000, -2000, 0, 3000, -3000, 1000, -1000 };

            for (int arity = 1; arity <= 9; arity++) {
                final byte[] expectedBytes = java.util.Arrays.copyOf(byteValues, arity);
                final short[] expectedShorts = java.util.Arrays.copyOf(shortValues, arity);
                final ByteTuple<?> byteTuple = ByteTuple.from(expectedBytes);
                final ShortTuple<?> shortTuple = ShortTuple.from(expectedShorts);
                final byte[] cachedBytes = byteTuple.elements();
                final short[] cachedShorts = shortTuple.elements();
                final byte[] sortedBytes = expectedBytes.clone();
                final short[] sortedShorts = expectedShorts.clone();
                java.util.Arrays.sort(sortedBytes);
                java.util.Arrays.sort(sortedShorts);

                assertEquals((sortedBytes[(arity - 1) / 2] + sortedBytes[arity / 2]) / 2d, byteTuple.median());
                assertEquals((sortedShorts[(arity - 1) / 2] + sortedShorts[arity / 2]) / 2d, shortTuple.median());
                assertSame(cachedBytes, byteTuple.elements());
                assertSame(cachedShorts, shortTuple.elements());
                assertArrayEquals(expectedBytes, cachedBytes);
                assertArrayEquals(expectedShorts, cachedShorts);
                assertArrayEquals(expectedBytes, byteTuple.stream().toArray());
                assertArrayEquals(expectedShorts, shortTuple.stream().toArray());
            }
        }
    }

    @Nested
    class ReversedTests {

        @Test
        void charReversalReversesSurrogateCodeUnitsIndividually() {
            final char[] supplementaryCharacter = Character.toChars(0x1F600);
            final CharTuple.CharTuple3 tuple = CharTuple.of(supplementaryCharacter[0], supplementaryCharacter[1], 'x');

            assertArrayEquals(new char[] { 'x', supplementaryCharacter[1], supplementaryCharacter[0] }, tuple.reversed().toArray());
            assertEquals(tuple, tuple.reversed().reversed());
            assertArrayEquals(new char[] { supplementaryCharacter[0], supplementaryCharacter[1], 'x' }, tuple.toArray());
        }
    }

    @Nested
    class ExportTests {

        @Test
        void booleanExportsStayDetachedWithoutInitializingCache() {
            final boolean[] values = { true, false, false, true, false, true, true, false, true };

            for (int arity = 0; arity <= values.length; arity++) {
                final boolean[] expected = java.util.Arrays.copyOf(values, arity);
                final BooleanTuple<?> tuple = BooleanTuple.from(expected);
                final boolean[] exported = tuple.toArray();
                final BooleanList list = tuple.toList();

                assertArrayEquals(expected, exported);
                assertArrayEquals(expected, list.toArray());
                assertNotSame(exported, tuple.toArray());
                assertNotSame(list, tuple.toList());

                if (arity > 0) {
                    assertNull(tuple.elements, "Exports must not initialize the tuple cache");
                    exported[0] = !exported[0];
                    list.set(0, !expected[0]);
                    assertArrayEquals(expected, tuple.toArray());
                    assertArrayEquals(expected, tuple.toList().toArray());
                }

                final boolean[] cached = tuple.elements();
                assertSame(cached, tuple.elements());
                assertNotSame(cached, tuple.toArray());
                assertArrayEquals(expected, cached);
                assertEquals(BooleanList.of(expected).boxed(), tuple.stream().toList());
            }
        }

        @Test
        void byteExportsAndLowerMedianPreserveSignedOrderWithoutInitializingCache() {
            final byte[] values = { Byte.MAX_VALUE, Byte.MIN_VALUE, 0, -1, 1, Byte.MIN_VALUE, Byte.MAX_VALUE, 42, -42 };
            final byte[] medians = { Byte.MAX_VALUE, Byte.MIN_VALUE, 0, -1, 0, -1, 0, 0, 0 };

            for (int arity = 0; arity <= values.length; arity++) {
                final byte[] expected = java.util.Arrays.copyOf(values, arity);
                final ByteTuple<?> tuple = ByteTuple.from(expected);
                final byte[] exported = tuple.toArray();
                final ByteList list = tuple.toList();

                assertArrayEquals(expected, exported);
                assertArrayEquals(expected, list.toArray());
                assertNotSame(exported, tuple.toArray());
                assertNotSame(list, tuple.toList());

                if (arity == 0) {
                    assertThrows(NoSuchElementException.class, tuple::lowerMedian);
                } else {
                    assertNull(tuple.elements, "Exports must not initialize the tuple cache");
                    exported[0] = 11;
                    list.set(0, (byte) 12);
                    assertArrayEquals(expected, tuple.toArray());
                    assertArrayEquals(expected, tuple.toList().toArray());
                    assertEquals(medians[arity - 1], tuple.lowerMedian());
                    assertNull(tuple.elements, "lowerMedian must not initialize the tuple cache");
                }

                final byte[] cached = tuple.elements();

                if (arity > 0) {
                    assertEquals(medians[arity - 1], tuple.lowerMedian());
                }

                assertSame(cached, tuple.elements());
                assertNotSame(cached, tuple.toArray());
                assertArrayEquals(expected, cached);
                assertArrayEquals(expected, tuple.toArray());
                assertArrayEquals(expected, tuple.stream().toArray());
            }
        }

        @Test
        void charExportsAndLowerMedianPreserveUnsignedCodeUnitOrderWithoutInitializingCache() {
            final char[] values = { Character.MAX_VALUE, 0, 0x8000, 127, 128, 0xD800, 0xDFFF, 1, Character.MAX_VALUE };
            final char[] medians = { Character.MAX_VALUE, 0, 0x8000, 127, 128, 128, 0x8000, 128, 0x8000 };

            for (int arity = 0; arity <= values.length; arity++) {
                final char[] expected = java.util.Arrays.copyOf(values, arity);
                final CharTuple<?> tuple = CharTuple.from(expected);
                final char[] exported = tuple.toArray();
                final CharList list = tuple.toList();

                assertArrayEquals(expected, exported);
                assertArrayEquals(expected, list.toArray());
                assertNotSame(exported, tuple.toArray());
                assertNotSame(list, tuple.toList());

                if (arity == 0) {
                    assertThrows(NoSuchElementException.class, tuple::lowerMedian);
                } else {
                    assertNull(tuple.elements, "Exports must not initialize the tuple cache");
                    exported[0] = 11;
                    list.set(0, (char) 12);
                    assertArrayEquals(expected, tuple.toArray());
                    assertArrayEquals(expected, tuple.toList().toArray());
                    assertEquals(medians[arity - 1], tuple.lowerMedian());
                    assertNull(tuple.elements, "lowerMedian must not initialize the tuple cache");
                }

                final char[] cached = tuple.elements();

                if (arity > 0) {
                    assertEquals(medians[arity - 1], tuple.lowerMedian());
                }

                assertSame(cached, tuple.elements());
                assertNotSame(cached, tuple.toArray());
                assertArrayEquals(expected, cached);
                assertArrayEquals(expected, tuple.toArray());
                assertArrayEquals(expected, tuple.stream().toArray());
            }
        }

        @Test
        void shortExportsAndLowerMedianPreserveSignedOrderWithoutInitializingCache() {
            final short[] values = { Short.MAX_VALUE, Short.MIN_VALUE, 0, -1, 1, Short.MIN_VALUE, Short.MAX_VALUE, 1024, -1024 };
            final short[] medians = { Short.MAX_VALUE, Short.MIN_VALUE, 0, -1, 0, -1, 0, 0, 0 };

            for (int arity = 0; arity <= values.length; arity++) {
                final short[] expected = java.util.Arrays.copyOf(values, arity);
                final ShortTuple<?> tuple = ShortTuple.from(expected);
                final short[] exported = tuple.toArray();
                final ShortList list = tuple.toList();

                assertArrayEquals(expected, exported);
                assertArrayEquals(expected, list.toArray());
                assertNotSame(exported, tuple.toArray());
                assertNotSame(list, tuple.toList());

                if (arity == 0) {
                    assertThrows(NoSuchElementException.class, tuple::lowerMedian);
                } else {
                    assertNull(tuple.elements, "Exports must not initialize the tuple cache");
                    exported[0] = 11;
                    list.set(0, (short) 12);
                    assertArrayEquals(expected, tuple.toArray());
                    assertArrayEquals(expected, tuple.toList().toArray());
                    assertEquals(medians[arity - 1], tuple.lowerMedian());
                    assertNull(tuple.elements, "lowerMedian must not initialize the tuple cache");
                }

                final short[] cached = tuple.elements();

                if (arity > 0) {
                    assertEquals(medians[arity - 1], tuple.lowerMedian());
                }

                assertSame(cached, tuple.elements());
                assertNotSame(cached, tuple.toArray());
                assertArrayEquals(expected, cached);
                assertArrayEquals(expected, tuple.toArray());
                assertArrayEquals(expected, tuple.stream().toArray());
            }
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void forEachStopsImmediatelyAndPropagatesTheOriginalCheckedExceptionAtEveryArity() {
            final boolean[] booleans = { true, false, true, true, false, false, true, false, true };
            final byte[] bytes = { Byte.MIN_VALUE, 1, -2, 3, -4, 5, -6, 7, Byte.MAX_VALUE };
            final char[] chars = { 0, 'a', 'b', 'c', 'd', 'e', 'f', 'g', Character.MAX_VALUE };
            final short[] shorts = { Short.MIN_VALUE, 1, -2, 3, -4, 5, -6, 7, Short.MAX_VALUE };

            for (int arity = 1; arity <= 9; arity++) {
                final int stopAfter = Math.min(2, arity);
                final int[] calls = new int[4];
                final IOException failure = new IOException("callback failure");
                final BooleanTuple<?> booleanTuple = BooleanTuple.from(java.util.Arrays.copyOf(booleans, arity));
                final ByteTuple<?> byteTuple = ByteTuple.from(java.util.Arrays.copyOf(bytes, arity));
                final CharTuple<?> charTuple = CharTuple.from(java.util.Arrays.copyOf(chars, arity));
                final ShortTuple<?> shortTuple = ShortTuple.from(java.util.Arrays.copyOf(shorts, arity));

                assertSame(failure, assertThrows(IOException.class, () -> booleanTuple.forEach(value -> {
                    assertEquals(booleans[calls[0]++], value);
                    if (calls[0] == stopAfter) {
                        throw failure;
                    }
                })));
                assertSame(failure, assertThrows(IOException.class, () -> byteTuple.forEach(value -> {
                    assertEquals(bytes[calls[1]++], value);
                    if (calls[1] == stopAfter) {
                        throw failure;
                    }
                })));
                assertSame(failure, assertThrows(IOException.class, () -> charTuple.forEach(value -> {
                    assertEquals(chars[calls[2]++], value);
                    if (calls[2] == stopAfter) {
                        throw failure;
                    }
                })));
                assertSame(failure, assertThrows(IOException.class, () -> shortTuple.forEach(value -> {
                    assertEquals(shorts[calls[3]++], value);
                    if (calls[3] == stopAfter) {
                        throw failure;
                    }
                })));
                assertArrayEquals(new int[] { stopAfter, stopAfter, stopAfter, stopAfter }, calls);
            }
        }

        @Test
        void singletonForEachUsesTheFieldAndPreservesCallbackFailures() throws IOException {
            final BooleanTuple.BooleanTuple1 booleanTuple = BooleanTuple.of(true);
            final ByteTuple.ByteTuple1 byteTuple = ByteTuple.of(Byte.MIN_VALUE);
            final CharTuple.CharTuple1 charTuple = CharTuple.of(Character.MAX_VALUE);
            final ShortTuple.ShortTuple1 shortTuple = ShortTuple.of(Short.MIN_VALUE);
            final int[] calls = new int[4];

            booleanTuple.forEach(value -> {
                assertEquals(true, value);
                calls[0]++;
            });
            byteTuple.forEach(value -> {
                assertEquals(Byte.MIN_VALUE, value);
                calls[1]++;
            });
            charTuple.forEach(value -> {
                assertEquals(Character.MAX_VALUE, value);
                calls[2]++;
            });
            shortTuple.forEach(value -> {
                assertEquals(Short.MIN_VALUE, value);
                calls[3]++;
            });
            assertArrayEquals(new int[] { 1, 1, 1, 1 }, calls);
            assertThrows(IllegalArgumentException.class, () -> booleanTuple.forEach(null));
            assertThrows(IllegalArgumentException.class, () -> byteTuple.forEach(null));
            assertThrows(IllegalArgumentException.class, () -> charTuple.forEach(null));
            assertThrows(IllegalArgumentException.class, () -> shortTuple.forEach(null));

            final IOException failure = new IOException("callback failure");
            assertSame(failure, assertThrows(IOException.class, () -> booleanTuple.forEach(value -> {
                throw failure;
            })));
            assertSame(failure, assertThrows(IOException.class, () -> byteTuple.forEach(value -> {
                throw failure;
            })));
            assertSame(failure, assertThrows(IOException.class, () -> charTuple.forEach(value -> {
                throw failure;
            })));
            assertSame(failure, assertThrows(IOException.class, () -> shortTuple.forEach(value -> {
                throw failure;
            })));
            assertNull(booleanTuple.elements);
            assertNull(byteTuple.elements);
            assertNull(charTuple.elements);
            assertNull(shortTuple.elements);
        }
    }

    @Nested
    class CallbackTests {

        @Test
        void emptyTuplesRemainValuesForWholeTupleCallbacks() {
            final PrimitiveTuple<?>[] tuples = { BooleanTuple.from(null), ByteTuple.from(null), CharTuple.from(null), ShortTuple.from(null),
                    IntTuple.from(null), LongTuple.from(null), FloatTuple.from(null), DoubleTuple.from(null) };

            for (final PrimitiveTuple<?> tuple : tuples) {
                final int[] calls = new int[3];

                assertEquals(0, tuple.arity());
                tuple.accept(value -> {
                    assertSame(tuple, value);
                    calls[0]++;
                });
                assertSame(tuple, tuple.map(value -> {
                    calls[1]++;
                    return value;
                }));
                assertSame(tuple, tuple.filter(value -> {
                    assertSame(tuple, value);
                    calls[2]++;
                    return true;
                }).get());
                assertArrayEquals(new int[] { 1, 1, 1 }, calls);
                assertTrue(tuple.filter(value -> false).isEmpty());
                assertSame(tuple, tuple.toOptional().get());
            }
        }
    }

    @Nested
    class ValueContractTests {

        @Test
        void everyBooleanPatternPreservesValueAndHashContractsAcrossAllArities() {
            for (int arity = 0; arity <= 9; arity++) {
                for (int pattern = 0; pattern < 1 << arity; pattern++) {
                    final boolean[] values = new boolean[arity];
                    final boolean[] reversed = new boolean[arity];
                    int expectedHash = 0;

                    for (int i = 0; i < arity; i++) {
                        values[i] = (pattern & 1 << i) != 0;
                        reversed[arity - i - 1] = values[i];
                        expectedHash = 31 * expectedHash + Boolean.hashCode(values[i]);
                    }

                    final BooleanTuple<?> tuple = BooleanTuple.from(values);
                    final BooleanTuple<?> copy = BooleanTuple.from(values.clone());

                    assertEquals(arity, tuple.arity());
                    assertEquals(pattern != 0, tuple.contains(true));
                    assertEquals(arity > 0 && pattern != (1 << arity) - 1, tuple.contains(false));
                    assertEquals(tuple, copy);
                    assertEquals(tuple.hashCode(), copy.hashCode());
                    if (arity > 0) {
                        assertEquals(expectedHash, tuple.hashCode());
                    }
                    assertArrayEquals(values, tuple.toArray());
                    assertArrayEquals(reversed, tuple.reversed().toArray());
                    assertEquals(tuple, tuple.reversed().reversed());
                }
            }
        }
    }

    private static List<int[]> aggregateInputs(final int arity, final int minValue, final int maxValue) {
        final List<int[]> inputs = new ArrayList<>();

        for (final int repeated : new int[] { minValue, maxValue, 0 }) {
            final int[] values = new int[arity];
            java.util.Arrays.fill(values, repeated);
            inputs.add(values);
        }

        final int[] alternating = new int[arity];
        for (int i = 0; i < arity; i++) {
            alternating[i] = i % 2 == 0 ? minValue : maxValue;
        }
        inputs.add(alternating);

        for (int position = 0; position < arity; position++) {
            final int[] minimumAtPosition = new int[arity];
            final int[] maximumAtPosition = new int[arity];
            java.util.Arrays.fill(minimumAtPosition, maxValue);
            java.util.Arrays.fill(maximumAtPosition, minValue);
            minimumAtPosition[position] = minValue;
            maximumAtPosition[position] = maxValue;
            inputs.add(minimumAtPosition);
            inputs.add(maximumAtPosition);
        }

        final Random random = new Random(0x5EEDL + arity + minValue);
        for (int sample = 0; sample < 32; sample++) {
            final int[] values = new int[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = minValue + random.nextInt(maxValue - minValue + 1);
            }
            inputs.add(values);
        }

        return inputs;
    }
}
