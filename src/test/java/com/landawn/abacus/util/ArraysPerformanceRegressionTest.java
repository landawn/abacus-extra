/*
 * Copyright (C) 2026 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("base-test")
class ArraysPerformanceRegressionTest {

    private static final class Value {
    }

    static Stream<Class<?>> primitiveTypes() {
        return Stream.of(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class);
    }

    @ParameterizedTest
    @MethodSource("primitiveTypes")
    void minimumRowLengthPreservesResultsForEveryPrimitiveType(final Class<?> type) throws Exception {
        final Object[] rows = { newArray(type, 3), newArray(type, 2), newArray(type, 4) };
        final Object matrix = newArray(rows[0].getClass(), rows.length);
        final Method minimum = Arrays.class.getMethod("minRowLength", matrix.getClass());

        for (int i = 0; i < rows.length; i++) {
            java.lang.reflect.Array.set(matrix, i, rows[i]);
        }

        assertEquals(2, minimum.invoke(null, matrix));
        assertEquals(0, minimum.invoke(null, new Object[] { null }));
        assertEquals(0, minimum.invoke(null, newArray(rows[0].getClass(), 0)));

        for (int i = 0; i < rows.length; i++) {
            java.lang.reflect.Array.set(matrix, i, null);
            assertEquals(0, minimum.invoke(null, matrix));
            java.lang.reflect.Array.set(matrix, i, newArray(type, 0));
            assertEquals(0, minimum.invoke(null, matrix));
            java.lang.reflect.Array.set(matrix, i, rows[i]);
            assertEquals(2, minimum.invoke(null, matrix));
        }
    }

    @ParameterizedTest
    @MethodSource("primitiveTypes")
    void primitiveFlattenPreservesEmptyResultIdentityAndSparseShapes(final Class<?> type) throws Exception {
        final Object emptyRow = newArray(type, 0);
        final Object emptyMatrix = newArray(emptyRow.getClass(), 3);
        java.lang.reflect.Array.set(emptyMatrix, 1, emptyRow);
        final Object emptyCube = newArray(emptyMatrix.getClass(), 4);
        java.lang.reflect.Array.set(emptyCube, 1, newArray(emptyRow.getClass(), 0));
        java.lang.reflect.Array.set(emptyCube, 2, emptyMatrix);

        for (final Object input : new Object[] { emptyMatrix, emptyCube }) {
            final Method flatten = Arrays.class.getMethod("flatten", input.getClass());
            final Object result = flatten.invoke(null, input);
            assertEquals(emptyRow.getClass(), result.getClass());
            assertEquals(0, java.lang.reflect.Array.getLength(result));
            assertNotSame(emptyRow, result);
            assertNotSame(result, flatten.invoke(null, input));
        }

        final Object row = newArray(type, 1);
        java.lang.reflect.Array.set(emptyMatrix, 2, row);

        for (final Object input : new Object[] { emptyMatrix, emptyCube }) {
            final Object result = Arrays.class.getMethod("flatten", input.getClass()).invoke(null, input);
            assertEquals(row.getClass(), result.getClass());
            assertEquals(1, java.lang.reflect.Array.getLength(result));
            assertEquals(java.lang.reflect.Array.get(row, 0), java.lang.reflect.Array.get(result, 0));
            assertNotSame(row, result);
        }
    }

    @Test
    void genericMinimumRowLengthHandlesNullAndEmptyRowsAtEveryPosition() {
        final Object[][] rows = { new String[3], new Number[2], new Value[4] };
        assertEquals(2, Arrays.ff.minRowLength(rows));
        assertEquals(0, Arrays.ff.minRowLength(null));
        assertEquals(0, Arrays.ff.minRowLength(new Object[0][]));

        for (int i = 0; i < rows.length; i++) {
            final Object[] original = rows[i];
            rows[i] = null;
            assertEquals(0, Arrays.ff.minRowLength(rows));
            rows[i] = new Object[0];
            assertEquals(0, Arrays.ff.minRowLength(rows));
            rows[i] = original;
            assertEquals(2, Arrays.ff.minRowLength(rows));
        }
    }

    @Test
    void genericEmptyFlattenPreservesRuntimeTypesAndFreshCustomArrays() {
        final Value[][] matrix = { null, new Value[0] };
        final Value[][][] cube = { null, new Value[0][], matrix };
        final Value[] flatMatrix = Arrays.ff.flatten(matrix);
        final Value[] flatCube = Arrays.fff.flatten(cube);
        assertEquals(Value[].class, flatMatrix.getClass());
        assertEquals(Value[].class, flatCube.getClass());
        assertEquals(0, flatMatrix.length);
        assertEquals(0, flatCube.length);
        assertNotSame(matrix[1], flatMatrix);
        assertNotSame(flatMatrix, Arrays.ff.flatten(matrix));
        assertNotSame(flatCube, Arrays.fff.flatten(cube));
        assertEquals(String[].class, Arrays.ff.flatten(new String[][] { null, {} }).getClass());
        assertEquals(String[].class, Arrays.fff.flatten(new String[][][] { null, { null, {} } }).getClass());
        assertThrows(IllegalArgumentException.class, () -> Arrays.ff.flatten((Object[][]) null));
        assertThrows(IllegalArgumentException.class, () -> Arrays.fff.flatten((Object[][][]) null));
    }

    @Test
    void genericFlattenPreservesArrayValuedElementTypes() {
        final int[][][] matrix = { null, new int[0][] };
        final int[][][][] cube = { null, matrix };
        final int[][] flatMatrix = Arrays.ff.flatten(matrix);
        final int[][] flatCube = Arrays.fff.flatten(cube);
        assertEquals(int[][].class, flatMatrix.getClass());
        assertEquals(int[][].class, flatCube.getClass());
        assertEquals(0, flatMatrix.length);
        assertEquals(0, flatCube.length);
        assertNotSame(flatMatrix, Arrays.ff.flatten(matrix));
        assertNotSame(flatCube, Arrays.fff.flatten(cube));

        final int[] element = { 7 };
        matrix[1] = new int[][] { element };
        assertSame(element, Arrays.ff.flatten(matrix)[0]);
        assertSame(element, Arrays.fff.flatten(cube)[0]);
    }

    private static Object newArray(final Class<?> type, final int length) {
        return java.lang.reflect.Array.newInstance(type, length);
    }
}
