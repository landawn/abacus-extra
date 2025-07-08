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
 * <p>Example usage:</p>
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
     */
    YES,

    /**
     * Forces sequential execution of matrix operations.
     * Use this for small matrices or when thread safety is a concern.
     */
    NO,

    /**
     * Uses default heuristics to determine whether to parallelize.
     * The decision is typically based on the matrix size and operation complexity.
     * Operations on matrices with more than 8192 elements are typically parallelized.
     */
    DEFAULT
}