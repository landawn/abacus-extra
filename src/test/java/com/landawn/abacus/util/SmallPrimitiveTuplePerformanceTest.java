package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class SmallPrimitiveTuplePerformanceTest extends TestBase {

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
