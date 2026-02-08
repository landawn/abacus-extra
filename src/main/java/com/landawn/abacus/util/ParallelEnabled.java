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
 * multiple CPU cores for better performance. This setting applies to matrix operations executed on the current thread and affects all
 * subsequent matrix operations until changed.</p>
 *
 * <p><b>Performance Considerations:</b></p>
 * <ul>
 *   <li>{@link #YES} - Forces parallelization regardless of matrix size; use when
 *       you know operations will benefit from parallel processing.</li>
 *   <li>{@link #NO} - Forces sequential execution; use for deterministic behavior
 *       or when integrating with non-thread-safe code.</li>
 *   <li>{@link #DEFAULT} - Automatically decides based on matrix size and operation
 *       complexity; recommended for most use cases.</li>
 * </ul>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Force parallel execution
 * Matrixes.setParallelEnabled(ParallelEnabled.YES);
 *
 * // Force sequential execution
 * Matrixes.setParallelEnabled(ParallelEnabled.NO);
 *
 * // Use default heuristics (recommended)
 * Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
 * }</pre>
 *
 * @see Matrixes#setParallelEnabled(ParallelEnabled)
 * @see Matrixes#getParallelEnabled()
 */
public enum ParallelEnabled {
    /**
     * Forces parallel execution of matrix operations regardless of matrix size.
     *
     * <p>This option always enables parallelization, even for small matrices where
     * the overhead of thread management may outweigh performance benefits. Use this
     * when you know the operation will benefit from parallelization or when you want
     * to guarantee parallel processing behavior.</p>
     *
     * <p><b>Use Cases:</b></p>
     * <ul>
     *   <li>Computationally intensive operations on large matrices</li>
     *   <li>Batch processing where parallel execution is required</li>
     *   <li>Systems with abundant CPU cores that benefit from parallelization</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Force parallel execution for a computationally intensive operation
     * Matrixes.setParallelEnabled(ParallelEnabled.YES);
     * IntMatrix matrix1 = IntMatrix.random(10, 10);
     * IntMatrix matrix2 = IntMatrix.random(10, 10);
     * IntMatrix result = matrix1.multiply(matrix2);   // Will use parallel processing
     *
     * // Even small matrices will be processed in parallel when YES is set
     * IntMatrix smallMatrix = IntMatrix.random(10, 10);
     * IntMatrix transposed = smallMatrix.transpose();   // Will still parallelize
     * }</pre>
     */
    YES,

    /**
     * Forces sequential execution of matrix operations.
     *
     * <p>This option disables parallelization entirely, ensuring all operations
     * execute in a single thread. Use this when you need deterministic behavior,
     * when integrating with non-thread-safe code, or when you want to minimize
     * thread overhead for small datasets.</p>
     *
     * <p><b>Use Cases:</b></p>
     * <ul>
     *   <li>Single-threaded or real-time contexts where thread spawning is undesirable</li>
     *   <li>Ensuring deterministic behavior for debugging or testing</li>
     *   <li>Integrating with non-thread-safe external libraries</li>
     *   <li>Small matrices where parallelization overhead would reduce performance</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Ensure sequential execution for deterministic behavior
     * Matrixes.setParallelEnabled(ParallelEnabled.NO);
     * IntMatrix matrix = IntMatrix.random(20, 20);
     * IntMatrix result = matrix.transpose();   // Will execute sequentially
     *
     * // Use sequential execution in single-threaded contexts
     * // or when integrating with non-thread-safe code
     * DoubleMatrix data = DoubleMatrix.of(new double[][]{{1, 2}, {3, 4}});
     * DoubleMatrix transposed = data.transpose();   // Sequential execution
     * }</pre>
     */
    NO,

    /**
     * Uses default heuristics to determine whether to parallelize.
     *
     * <p>This option automatically decides whether to parallelize based on the
     * matrix size and operation complexity. The framework evaluates factors such as
     * the number of elements in the matrix and the computational cost of the operation
     * to make an optimal decision. Operations on matrices with more than 8192 elements
     * are typically parallelized, but this threshold may vary based on the operation.</p>
     *
     * <p><b>Advantages:</b></p>
     * <ul>
     *   <li>Optimal performance for most use cases without manual tuning</li>
     *   <li>Automatically adapts to matrix size and operation complexity</li>
     *   <li>Reduces thread overhead for small datasets</li>
     *   <li>Leverages parallel processing for computationally intensive operations</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Let the framework decide based on matrix size (recommended)
     * Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
     *
     * // Small matrix - will execute sequentially
     * IntMatrix small = IntMatrix.random(10, 10);
     * IntMatrix result1 = small.transpose();   // Sequential execution
     *
     * // Large matrix - will automatically parallelize
     * IntMatrix large = IntMatrix.random(20, 20);   // still demonstrates API usage
     * IntMatrix result2 = large.transpose();          // Parallel execution
     *
     * // This is the recommended setting for most use cases
     * // as it balances performance and overhead automatically
     * }</pre>
     */
    DEFAULT
}
