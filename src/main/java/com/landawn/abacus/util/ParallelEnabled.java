/*
 * Copyright (C) 2020 HaiYang Li
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

/**
 * Enumeration that controls parallel processing behavior for matrix operations.
 * This enum is used to explicitly enable, disable, or use default parallelization
 * settings when performing computationally intensive matrix operations.
 * 
 * <p>The parallelization decision affects how matrix operations distribute work
 * across multiple threads. When enabled, operations on large matrices can utilize
 * multiple CPU cores for better performance.</p>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Force parallel execution
 * Matrixes.setParallelEnabled(ParallelEnabled.YES);
 * 
 * // Force sequential execution
 * Matrixes.setParallelEnabled(ParallelEnabled.NO);
 * 
 * // Use default heuristics
 * Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
 * }</pre>
 * 
 * @see Matrixes#setParallelEnabled(ParallelEnabled)
 * @see Matrixes#getParallelEnabled()
 */
public enum ParallelEnabled {
    /**
     * Forces parallel execution of matrix operations regardless of matrix size.
     * Use this when you know the operation will benefit from parallelization.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Force parallel execution for a computationally intensive operation
     * Matrixes.setParallelEnabled(ParallelEnabled.YES);
     * IntMatrix matrix1 = IntMatrix.random(1000, 1000);
     * IntMatrix matrix2 = IntMatrix.random(1000, 1000);
     * IntMatrix result = matrix1.multiply(matrix2); // Will use parallel processing
     *
     * // Even small matrices will be processed in parallel when YES is set
     * IntMatrix smallMatrix = IntMatrix.random(100, 100);
     * IntMatrix transposed = smallMatrix.transpose(); // Will still parallelize
     * }</pre>
     */
    YES,

    /**
     * Forces sequential execution of matrix operations.
     * Use this for small matrices or when thread safety is a concern.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Ensure sequential execution for deterministic behavior
     * Matrixes.setParallelEnabled(ParallelEnabled.NO);
     * IntMatrix matrix = IntMatrix.random(10000, 10000);
     * IntMatrix result = matrix.transpose(); // Will execute sequentially
     *
     * // Use sequential execution in single-threaded contexts
     * // or when integrating with non-thread-safe code
     * DoubleMatrix data = DoubleMatrix.of(new double[][]{{1, 2}, {3, 4}});
     * DoubleMatrix scaled = data.multiply(2.0); // Sequential execution
     * }</pre>
     */
    NO,

    /**
     * Uses default heuristics to determine whether to parallelize.
     * The decision is typically based on the matrix size and operation complexity.
     * Operations on matrices with more than 8192 elements are typically parallelized.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Let the framework decide based on matrix size
     * Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
     *
     * // Small matrix - will execute sequentially
     * IntMatrix small = IntMatrix.random(50, 50);
     * IntMatrix result1 = small.transpose(); // Sequential execution
     *
     * // Large matrix - will automatically parallelize
     * IntMatrix large = IntMatrix.random(200, 200); // 40,000 elements > 8192
     * IntMatrix result2 = large.transpose(); // Parallel execution
     *
     * // This is the recommended setting for most use cases
     * // as it balances performance and overhead automatically
     * }</pre>
     */
    DEFAULT
}