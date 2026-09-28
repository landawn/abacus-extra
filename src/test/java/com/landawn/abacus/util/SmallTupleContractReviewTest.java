package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class SmallTupleContractReviewTest extends TestBase {

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
    void charReversalReversesSurrogateCodeUnitsIndividually() {
        final char[] supplementaryCharacter = Character.toChars(0x1F600);
        final CharTuple.CharTuple3 tuple = CharTuple.of(supplementaryCharacter[0], supplementaryCharacter[1], 'x');

        assertArrayEquals(new char[] { 'x', supplementaryCharacter[1], supplementaryCharacter[0] }, tuple.reversed().toArray());
        assertEquals(tuple, tuple.reversed().reversed());
        assertArrayEquals(new char[] { supplementaryCharacter[0], supplementaryCharacter[1], 'x' }, tuple.toArray());
    }
}
