/*
 * Copyright (C) 2026 HaiYang Li
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

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.TestBase;

class ArraysDeepReviewRegressionTest extends TestBase {

    static Stream<Arguments> arrayValues() {
        return Stream.of(Arguments.of(new boolean[] { true, false }, "[true, false]"),
                Arguments.of(new char[] { 'a', 'b' }, "[a, b]"),
                Arguments.of(new byte[] { 1, -2 }, "[1, -2]"),
                Arguments.of(new short[] { 3, -4 }, "[3, -4]"),
                Arguments.of(new int[] { 5, -6 }, "[5, -6]"),
                Arguments.of(new long[] { 7, -8 }, "[7, -8]"),
                Arguments.of(new float[] { 1.5f, -2.5f }, "[1.5, -2.5]"),
                Arguments.of(new double[] { 3.5d, -4.5d }, "[3.5, -4.5]"),
                Arguments.of(new Object[] { "nested", new int[] { 9, 10 }, null }, "[nested, [9, 10], null]"),
                Arguments.of(new Object[0], "[]"));
    }

    @ParameterizedTest
    @MethodSource("arrayValues")
    void twoDimensionalPrintingRendersArrayValuedElementsByContent(final Object value, final String renderedValue) {
        final Object[] row = { value, "tail", null };
        final Object[][] grid = { row, null, {} };

        assertEquals("[" + renderedValue + ", tail, null]", Arrays.println(row));
        assertEquals("[[" + renderedValue + ", tail, null],\n null,\n []]", Arrays.println(grid));
    }

    @ParameterizedTest
    @MethodSource("arrayValues")
    void threeDimensionalPrintingRendersArrayValuedElementsByContent(final Object value, final String renderedValue) {
        final Object[][][] cube = { { { value }, null, {} }, null, {} };

        assertEquals("[[[" + renderedValue + "],\n  null,\n  []],\n null,\n []]", Arrays.println(cube));
    }

    @Test
    void objectPrintingExpandsArrayPayloadsWithTheSameElementFormattingAtEveryDepth() {
        final Object value = new Object[] { new Object[] { "leaf", new boolean[] { true } }, new String[0] };
        final String rendered = "[[leaf, [true]], []]";

        assertEquals("[" + rendered + "]", Arrays.println(new Object[] { value }));
        assertEquals("[[" + rendered + "]]", Arrays.println(new Object[][] { { value } }));
        assertEquals("[[[" + rendered + "]]]", Arrays.println(new Object[][][] { { { value } } }));
    }

    @Test
    void nestedObjectPrintingHandlesCyclicArrayElementsAndRepeatedReferences() {
        final Object[] cyclic = new Object[1];
        cyclic[0] = cyclic;
        final String rendered = "[[...]]";

        assertEquals("[[" + rendered + ", " + rendered + "]]", Arrays.println(new Object[][] { { cyclic, cyclic } }));
        assertEquals("[[[" + rendered + ", " + rendered + "]]]", Arrays.println(new Object[][][] { { { cyclic, cyclic } } }));
    }

    @Test
    void nestedObjectPrintingPreservesNonArrayToStringRepresentations() {
        final Object value = new AbstractCollection<Object>() {
            @Override
            public Iterator<Object> iterator() {
                throw new AssertionError("Printing a non-array element must use its toString method");
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public String toString() {
                return "custom value";
            }
        };

        assertEquals("[[custom value]]", Arrays.println(new Object[][] { { value } }));
        assertEquals("[[[custom value]]]", Arrays.println(new Object[][][] { { { value } } }));
    }
}
