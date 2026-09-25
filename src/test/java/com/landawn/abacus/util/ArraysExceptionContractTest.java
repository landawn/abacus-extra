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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.landawn.abacus.TestBase;

class ArraysExceptionContractTest extends TestBase {

    @Test
    @SuppressWarnings("unchecked")
    void explicitTargetOperationsRejectValuesIncompatibleWithTheRuntimeClass() {
        // An unchecked class token can disagree with its generic type, so the array store remains a runtime check.
        final Class<Number> integerType = (Class<Number>) (Class<?>) Integer.class;
        final Number[][] grid = { { 1 } };
        final Number[][][] cube = { grid };
        final List<Executable> operations = List.of(//
                () -> Arrays.mapToObj(new boolean[] { true }, value -> 1.5d, integerType),
                () -> Arrays.mapToObj(new int[][] { { 1 } }, value -> 1.5d, integerType),
                () -> Arrays.mapToObj(new double[][][] { { { 1 } } }, value -> 1.5d, integerType),
                () -> Arrays.f.map(grid[0], value -> 1.5d, integerType),
                () -> Arrays.ff.map(grid, value -> 1.5d, integerType),
                () -> Arrays.fff.map(cube, value -> 1.5d, integerType),
                () -> Arrays.ff.zip(grid, grid, (left, right) -> 1.5d, integerType),
                () -> Arrays.ff.zip(grid, null, 0, 0, (left, right) -> 1.5d, integerType),
                () -> Arrays.ff.zip(grid, grid, grid, (first, second, third) -> 1.5d, integerType),
                () -> Arrays.ff.zip(grid, null, null, 0, 0, 0, (first, second, third) -> 1.5d, integerType),
                () -> Arrays.fff.zip(cube, cube, (left, right) -> 1.5d, integerType),
                () -> Arrays.fff.zip(cube, null, 0, 0, (left, right) -> 1.5d, integerType),
                () -> Arrays.fff.zip(cube, cube, cube, (first, second, third) -> 1.5d, integerType),
                () -> Arrays.fff.zip(cube, null, null, 0, 0, 0, (first, second, third) -> 1.5d, integerType));

        for (final Executable operation : operations) {
            assertThrows(ArrayStoreException.class, operation);
        }
    }

    @Test
    void defaultedInferredZipTranslatesIncompatibleResultsAndRetainsTheCause() {
        final Number[][] grid = new Integer[][] { { 1 } };
        final Number[][][] cube = new Integer[][][] { (Integer[][]) grid };
        final List<Executable> operations = List.of(//
                () -> Arrays.ff.zip(grid, null, 0, 0, (left, right) -> 1.5d),
                () -> Arrays.ff.zip(grid, null, null, 0, 0, 0, (first, second, third) -> 1.5d),
                () -> Arrays.fff.zip(cube, null, 0, 0, (left, right) -> 1.5d),
                () -> Arrays.fff.zip(cube, null, null, 0, 0, 0, (first, second, third) -> 1.5d));

        for (final Executable operation : operations) {
            final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
            assertInstanceOf(ArrayStoreException.class, failure.getCause());
        }
    }

    @Test
    void defaultedZipValidatesTypeInferenceInputsBeforeTheCallback() {
        final List<Executable> operations = List.of(//
                () -> Arrays.ff.<Object, Object, RuntimeException> zip(null, null, null, null, null),
                () -> Arrays.ff.<Object, Object, Object, RuntimeException> zip(null, null, null, null, null, null, null),
                () -> Arrays.fff.<Object, Object, RuntimeException> zip(null, null, null, null, null),
                () -> Arrays.fff.<Object, Object, Object, RuntimeException> zip(null, null, null, null, null, null, null));

        for (final Executable operation : operations) {
            final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
            assertEquals("Unable to infer target element type: both 'a' and 'defaultValueA' are null. Use the overload with targetElementType.",
                    failure.getMessage());
        }
    }

    @Test
    void defaultedZipValidatesTheCallbackBeforeAllocatingItsResult() {
        final Object defaultValue = arrayWithDimensions(255);
        final List<Executable> operations = List.of(//
                () -> Arrays.ff.<Object, Object, RuntimeException> zip(null, null, defaultValue, null, null),
                () -> Arrays.ff.<Object, Object, Object, RuntimeException> zip(null, null, null, defaultValue, null, null, null),
                () -> Arrays.fff.<Object, Object, RuntimeException> zip(null, null, defaultValue, null, null),
                () -> Arrays.fff.<Object, Object, Object, RuntimeException> zip(null, null, null, defaultValue, null, null, null));

        for (final Executable operation : operations) {
            final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
            assertEquals("'zipFunction' cannot be null", failure.getMessage());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void resultTypesCannotExceedTheJvmArrayDimensionLimit() {
        final Class<Object> type253 = (Class<Object>) (Class<?>) arrayWithDimensions(253).getClass();
        final Class<Object> type254 = (Class<Object>) (Class<?>) arrayWithDimensions(254).getClass();
        final Class<Object> type255 = (Class<Object>) (Class<?>) arrayWithDimensions(255).getClass();
        final List<Executable> operations = List.of(//
                () -> Arrays.mapToObj((int[]) null, value -> null, type255),
                () -> Arrays.mapToObj((int[][]) null, value -> null, type254),
                () -> Arrays.mapToObj((int[][][]) null, value -> null, type253),
                () -> Arrays.f.map((Object[]) null, value -> null, type255),
                () -> Arrays.ff.map((Object[][]) null, value -> null, type254),
                () -> Arrays.fff.map((Object[][][]) null, value -> null, type253),
                () -> Arrays.ff.zip(null, null, (left, right) -> null, type254),
                () -> Arrays.fff.zip(null, null, null, (first, second, third) -> null, type253),
                () -> Arrays.ff.zip(null, null, arrayWithDimensions(254), null, (left, right) -> left),
                () -> Arrays.fff.zip(null, null, null, arrayWithDimensions(253), null, null, (first, second, third) -> first),
                () -> Arrays.ff.reshape(arrayWithDimensions(255), 1),
                () -> Arrays.fff.reshape(arrayWithDimensions(254), 1, 1));

        for (final Executable operation : operations) {
            assertThrows(IllegalArgumentException.class, operation);
        }

        // The boundary itself is legal, including on an empty input.
        assertEquals(type255, Arrays.mapToObj((int[]) null, value -> null, type254).getClass());
        assertEquals(type255, Arrays.ff.map((Object[][]) null, value -> null, type253).getClass());
    }

    @Test
    void objectPrintingPropagatesElementConversionFailures() {
        final IllegalStateException failure = new IllegalStateException("element conversion failed");
        final Object value = new Object() {
            @Override
            public String toString() {
                throw failure;
            }
        };

        assertSame(failure, assertThrows(IllegalStateException.class, () -> Arrays.println(new Object[] { value })));
        assertSame(failure, assertThrows(IllegalStateException.class, () -> Arrays.println(new Object[][] { { value } })));
        assertSame(failure, assertThrows(IllegalStateException.class, () -> Arrays.println(new Object[][][] { { { value } } })));
    }

    @Test
    void primitiveFlatMutationReportsRowsGrownByTheCallback() {
        final int[][] grid = { { 1 }, { 2 } };

        assertThrows(IndexOutOfBoundsException.class, () -> Arrays.mutateViaFlatArray(grid, flat -> {
            flat[0] = 7;
            grid[1] = new int[2];
        }));
        assertArrayEquals(new int[] { 7 }, grid[0]);
        assertArrayEquals(new int[2], grid[1]);
    }

    @Test
    void objectFlatMutationReportsRowsGrownByTheCallback() {
        final String[][][] cube = { { { "a" }, { "b" } } };

        assertThrows(IndexOutOfBoundsException.class, () -> Arrays.fff.mutateViaFlatArray(cube, flat -> {
            flat[0] = "updated";
            cube[0][1] = new String[2];
        }));
        assertArrayEquals(new String[] { "updated" }, cube[0][0]);
        assertArrayEquals(new String[2], cube[0][1]);
    }

    private static Object[] arrayWithDimensions(final int dimensions) {
        return (Object[]) java.lang.reflect.Array.newInstance(Object.class, new int[dimensions]);
    }
}
