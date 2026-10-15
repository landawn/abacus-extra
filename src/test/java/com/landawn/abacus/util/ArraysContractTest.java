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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.landawn.abacus.util.Arrays.fff;
import com.landawn.abacus.util.stream.Stream;

class ArraysContractTest {

    @Nested
    class CrossOperationContractTests {

        @Test
        void composedMapping_formatsNestedResults() throws IOException {

            File file = new File("src/main/java/com/landawn/abacus/util/Arrays.java");
            List<String> lines = IOUtil.readAllLines(file);

            Stream.of(lines).filter(line -> line.trim().startsWith("*") && line.contains("N.")).forEach(System.out::println);

            List<String> newLines = Stream.of(lines)
                    .map(line -> line.trim().startsWith("*") && line.contains("N.") ? line.replace("N.", "Arrays.") : line)
                    .toList();

            IOUtil.writeLines(newLines, file);

            assertEquals(lines.size(), newLines.size());
            assertTrue(newLines.stream().noneMatch(line -> line.trim().startsWith("*") && line.contains("N.")));
        }

        @Test
        void edgeCasesWithNullElements() throws Exception {
            // Test updateAll with null elements in array
            Integer[][][] arr = { { { 1, null, 2 } }, { { null, 3 } } };
            fff.updateAll(arr, n -> n == null ? 0 : n * 2);
            assertEquals(2, arr[0][0][0]);
            assertEquals(0, arr[0][0][1]);
            assertEquals(4, arr[0][0][2]);
            assertEquals(0, arr[1][0][0]);
            assertEquals(6, arr[1][0][1]);

            // Test map with function that can handle nulls
            String[][][] strArr = { { { null, "hello" } }, { { null, null } } };
            String[][][] mapped = fff.map(strArr, s -> s == null ? "NULL" : s.toUpperCase());
            assertEquals("NULL", mapped[0][0][0]);
            assertEquals("HELLO", mapped[0][0][1]);
            assertEquals("NULL", mapped[1][0][0]);
            assertEquals("NULL", mapped[1][0][1]);
        }

        @Test
        void explicitTargetElementTypeIsValidated() {
            final Class<String> targetElementType = null;
            final List<Executable> calls = List.of(//
                    () -> Arrays.mapToObj((boolean[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((boolean[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((boolean[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((char[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((char[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((char[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((byte[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((byte[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((byte[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((short[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((short[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((short[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((int[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((int[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((int[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((long[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((long[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((long[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((float[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((float[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((float[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((double[]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((double[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.mapToObj((double[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.f.map((String[]) null, ignored -> "", targetElementType),
                    () -> Arrays.ff.map((String[][]) null, ignored -> "", targetElementType),
                    () -> Arrays.ff.zip((String[][]) null, (String[][]) null, (left, right) -> "", targetElementType),
                    () -> Arrays.ff.zip((String[][]) null, (String[][]) null, "", "", (left, right) -> "", targetElementType),
                    () -> Arrays.ff.zip((String[][]) null, (String[][]) null, (String[][]) null, (first, second, third) -> "", targetElementType),
                    () -> Arrays.ff.zip((String[][]) null, (String[][]) null, (String[][]) null, "", "", "", (first, second, third) -> "", targetElementType),
                    () -> Arrays.fff.map((String[][][]) null, ignored -> "", targetElementType),
                    () -> Arrays.fff.zip((String[][][]) null, (String[][][]) null, (left, right) -> "", targetElementType),
                    () -> Arrays.fff.zip((String[][][]) null, (String[][][]) null, "", "", (left, right) -> "", targetElementType),
                    () -> Arrays.fff.zip((String[][][]) null, (String[][][]) null, (String[][][]) null, (first, second, third) -> "", targetElementType),
                    () -> Arrays.fff.zip((String[][][]) null, (String[][][]) null, (String[][][]) null, "", "", "", (first, second, third) -> "",
                            targetElementType));

            for (final Executable call : calls) {
                final IllegalArgumentException error = assertThrows(IllegalArgumentException.class, call);
                assertEquals("'targetElementType' cannot be null", error.getMessage());
            }
        }

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
                    () -> Arrays.mapToObj(new double[][][] { { { 1 } } }, value -> 1.5d, integerType), () -> Arrays.f.map(grid[0], value -> 1.5d, integerType),
                    () -> Arrays.ff.map(grid, value -> 1.5d, integerType), () -> Arrays.fff.map(cube, value -> 1.5d, integerType),
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
        void functionalArgumentsAreValidatedForEmptyInputs() {
            final Throwables.BooleanFunction<String, RuntimeException> booleanMapper = null;
            final Throwables.IntUnaryOperator<RuntimeException> intOperator = null;
            final Throwables.IntBinaryOperator<RuntimeException> intZipFunction = null;
            final Throwables.Function<String, Integer, RuntimeException> objectMapper = null;

            assertThrows(IllegalArgumentException.class, () -> Arrays.mapToObj((boolean[]) null, booleanMapper, String.class));
            assertThrows(IllegalArgumentException.class, () -> Arrays.updateAll(new int[0][], intOperator));
            assertThrows(IllegalArgumentException.class, () -> Arrays.zip((int[]) null, new int[0], intZipFunction));
            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.map((String[][]) null, objectMapper, Integer.class));
        }

        @Test
        @SuppressWarnings("unchecked")
        void inferredGenericOperationsPreserveCallbackArrayStoreExceptions() {
            final Integer[][] a2 = { { 1 } };
            final Integer[][] b2 = { { 2 } };
            final Integer[][] c2 = { { 3 } };
            final Integer[][][] a3 = { a2 };
            final Integer[][][] b3 = { b2 };
            final Integer[][][] c3 = { c2 };
            final ArrayStoreException failure = new ArrayStoreException("from callback");

            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.ff.map(a2, value -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.ff.zip(a2, b2, (left, right) -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.ff.zip(a2, b2, 0, 0, (left, right) -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.ff.zip(a2, b2, c2, (first, second, third) -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.ff.zip(a2, b2, c2, 0, 0, 0, (first, second, third) -> {
                throw failure;
            })) == failure);

            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.fff.map(a3, value -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.fff.zip(a3, b3, (left, right) -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.fff.zip(a3, b3, 0, 0, (left, right) -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.fff.zip(a3, b3, c3, (first, second, third) -> {
                throw failure;
            })) == failure);
            assertTrue(assertThrows(ArrayStoreException.class, () -> Arrays.fff.zip(a3, b3, c3, 0, 0, 0, (first, second, third) -> {
                throw failure;
            })) == failure);

            final Number[][] narrowRuntimeType = new Integer[][] { { 1 } };
            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.map(narrowRuntimeType, value -> 1.5d));

            final Class<Number> integerRuntimeType = (Class<Number>) (Class<?>) Integer.class;
            final ArrayStoreException nestedStorageFailure = assertThrows(ArrayStoreException.class, () -> Arrays.ff.map(a2, value -> {
                Arrays.f.map(new Integer[] { 1 }, ignored -> 1.5d, integerRuntimeType);
                return value;
            }));
            assertEquals(ArrayStoreException.class, nestedStorageFailure.getClass());
        }

        @Test
        void largeArrayOperations() throws Exception {
            // Test with larger arrays
            int size = 10;
            Integer[][][] largeArr = new Integer[size][][];
            for (int i = 0; i < size; i++) {
                largeArr[i] = new Integer[size][];
                for (int j = 0; j < size; j++) {
                    largeArr[i][j] = new Integer[size];
                    for (int k = 0; k < size; k++) {
                        largeArr[i][j][k] = i * 100 + j * 10 + k;
                    }
                }
            }

            // Test totalElementCount
            long count = fff.totalElementCount(largeArr);
            assertEquals(1000, count);

            // Test flatten
            Integer[] flattened = fff.flatten(largeArr);
            assertEquals(1000, flattened.length);
            assertEquals(0, flattened[0]);
            assertEquals(999, flattened[999]);

            // Test map
            Integer[][][] doubled = fff.map(largeArr, n -> n * 2);
            assertEquals(0, doubled[0][0][0]);
            assertEquals(1998, doubled[9][9][9]);
        }

        @Test
        @SuppressWarnings("unchecked")
        void resultTypesCannotExceedTheJvmArrayDimensionLimit() {
            final Class<Object> type253 = (Class<Object>) (Class<?>) arrayWithDimensions(253).getClass();
            final Class<Object> type254 = (Class<Object>) (Class<?>) arrayWithDimensions(254).getClass();
            final Class<Object> type255 = (Class<Object>) (Class<?>) arrayWithDimensions(255).getClass();
            final List<Executable> operations = List.of(//
                    () -> Arrays.mapToObj((int[]) null, value -> null, type255), () -> Arrays.mapToObj((int[][]) null, value -> null, type254),
                    () -> Arrays.mapToObj((int[][][]) null, value -> null, type253), () -> Arrays.f.map((Object[]) null, value -> null, type255),
                    () -> Arrays.ff.map((Object[][]) null, value -> null, type254), () -> Arrays.fff.map((Object[][][]) null, value -> null, type253),
                    () -> Arrays.ff.zip(null, null, (left, right) -> null, type254),
                    () -> Arrays.fff.zip(null, null, null, (first, second, third) -> null, type253),
                    () -> Arrays.ff.zip(null, null, arrayWithDimensions(254), null, (left, right) -> left),
                    () -> Arrays.fff.zip(null, null, null, arrayWithDimensions(253), null, null, (first, second, third) -> first),
                    () -> Arrays.ff.reshape(arrayWithDimensions(255), 1), () -> Arrays.fff.reshape(arrayWithDimensions(254), 1, 1));

            for (final Executable operation : operations) {
                assertThrows(IllegalArgumentException.class, operation);
            }

            // The boundary itself is legal, including on an empty input.
            assertEquals(type255, Arrays.mapToObj((int[]) null, value -> null, type254).getClass());
            assertEquals(type255, Arrays.ff.map((Object[][]) null, value -> null, type253).getClass());
        }
    }

    private static Object[] arrayWithDimensions(final int dimensions) {
        return (Object[]) java.lang.reflect.Array.newInstance(Object.class, new int[dimensions]);
    }
}
