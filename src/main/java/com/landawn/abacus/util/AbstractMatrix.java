/*
 * Copyright (C) 2016 HaiYang Li
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

import java.util.ArrayList;
import java.util.List;

import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * A comprehensive abstract base class providing the foundational architecture for all matrix implementations
 * in the Abacus library. This sealed class establishes a type-safe hierarchy for working with two-dimensional
 * arrays of various primitive types and objects, offering a unified API for matrix operations while maintaining
 * optimal performance through type-specific implementations.
 *
 * <p>The {@code AbstractMatrix} serves as the cornerstone of a sophisticated matrix computation framework,
 * providing essential operations for scientific computing, data analysis, and mathematical modeling scenarios.
 * It employs advanced generic typing to ensure compile-time type safety while enabling runtime polymorphism
 * across different primitive and object types, making it suitable for high-performance numerical computations.</p>
 *
 * <p><b>Key Features and Capabilities:</b>
 * <ul>
 *   <li><b>Type-Safe Matrix Operations:</b> Compile-time type checking for all matrix operations</li>
 *   <li><b>Primitive Specialization:</b> Optimized implementations for all Java primitive types</li>
 *   <li><b>Stream Integration:</b> Comprehensive stream-based access patterns for functional programming</li>
 *   <li><b>Memory Efficiency:</b> Optimal memory layout with minimal boxing overhead for primitives</li>
 *   <li><b>Mathematical Operations:</b> Full suite of linear algebra and matrix manipulation operations</li>
 *   <li><b>Dimensional Flexibility:</b> Support for two-dimensional matrices with rich reshape and extension operations</li>
 *   <li><b>Performance Optimized:</b> Specialized algorithms for different matrix types and operations</li>
 *   <li><b>Immutable Operations:</b> Many transformation operations return new matrix instances while mutating operations are also available</li>
 * </ul>
 *
 * <p><b>Design Philosophy:</b>
 * <ul>
 *   <li><b>Type Safety First:</b> Sealed hierarchy prevents incorrect type usage and enables pattern matching</li>
 *   <li><b>Performance Critical:</b> Primitive specialization eliminates boxing overhead in numerical computations</li>
 *   <li><b>Functional Paradigm:</b> Stream-based operations enable elegant functional programming patterns</li>
 *   <li><b>Memory Conscious:</b> Efficient internal representation minimizing memory footprint</li>
 *   <li><b>API Consistency:</b> Uniform interface across all matrix types for predictable behavior</li>
 * </ul>
 *
 * <p><b>Generic Type Parameters:</b>
 * <table border="1" style="border-collapse: collapse;">
 *   <caption><b>Generic Type Parameter Definitions</b></caption>
 *   <tr style="background-color: #f2f2f2;">
 *     <th>Parameter</th>
 *     <th>Description</th>
 *     <th>Example Types</th>
 *     <th>Usage</th>
 *   </tr>
 *   <tr>
 *     <td>A</td>
 *     <td>Array type for internal storage</td>
 *     <td>int[], double[], String[]</td>
 *     <td>Row storage and data access</td>
 *   </tr>
 *   <tr>
 *     <td>PL</td>
 *     <td>Primitive list type for collections</td>
 *     <td>IntList, DoubleList, List&lt;T&gt;</td>
 *     <td>Flattened matrix operations</td>
 *   </tr>
 *   <tr>
 *     <td>ES</td>
 *     <td>Element stream type</td>
 *     <td>IntStream, DoubleStream, Stream&lt;T&gt;</td>
 *     <td>Element-wise streaming operations</td>
 *   </tr>
 *   <tr>
 *     <td>RS</td>
 *     <td>Row/column stream type</td>
 *     <td>Stream&lt;int[]&gt;, Stream&lt;double[]&gt;</td>
 *     <td>Row and column streaming</td>
 *   </tr>
 *   <tr>
 *     <td>X</td>
 *     <td>Self-referencing concrete type</td>
 *     <td>IntMatrix, DoubleMatrix, Matrix&lt;T&gt;</td>
 *     <td>Fluent API and method chaining</td>
 *   </tr>
 * </table>
 *
 * <p><b>Permitted Matrix Implementations:</b>
 * <ul>
 *   <li><b>{@code BooleanMatrix}:</b> Specialized matrix for boolean values with logical operations</li>
 *   <li><b>{@code CharMatrix}:</b> Character matrix for text processing and manipulation</li>
 *   <li><b>{@code ByteMatrix}:</b> 8-bit integer matrix for compact numerical storage</li>
 *   <li><b>{@code ShortMatrix}:</b> 16-bit integer matrix for medium-range numerical operations</li>
 *   <li><b>{@code IntMatrix}:</b> 32-bit integer matrix for general integer computations</li>
 *   <li><b>{@code LongMatrix}:</b> 64-bit integer matrix for large integer calculations</li>
 *   <li><b>{@code FloatMatrix}:</b> 32-bit floating-point matrix for precision-conscious operations</li>
 *   <li><b>{@code DoubleMatrix}:</b> 64-bit floating-point matrix for high-precision computations</li>
 *   <li><b>{@code Matrix&lt;T&gt;}:</b> Generic object matrix for complex data structures</li>
 * </ul>
 *
 * <p><b>Core Matrix Operations Categories:</b>
 * <ul>
 *   <li><b>Dimensional Operations:</b> {@code rowCount}, {@code columnCount}, {@code elementCount}, {@code isEmpty()}</li>
 *   <li><b>Access Patterns:</b> {@code get()}, {@code set()}, {@code row()}, {@code column()}</li>
 *   <li><b>Stream Operations:</b> {@code streamH()}, {@code streamV()}, {@code streamR()}, {@code streamC()}, {@code streamLU2RD()}, {@code streamRU2LD()}</li>
 *   <li><b>Transformation Operations:</b> {@code transpose()}, {@code rotate()}, {@code flip()}, {@code reshape()}</li>
 *   <li><b>Mathematical Operations:</b> Element-wise arithmetic, linear algebra, statistical functions</li>
 *   <li><b>Utility Operations:</b> {@code copy()}, {@code clone()}, {@code toArray()}, {@code toString()}</li>
 * </ul>
 *
 * <p><b>Common Usage Patterns:</b>
 * <pre>{@code
 * // Creating matrices from arrays
 * IntMatrix intMatrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
 * DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] {{1.1, 2.2}, {3.3, 4.4}});
 * Matrix<String> stringMatrix = Matrix.of(new String[][] {{"A", "B"}, {"C", "D"}});
 *
 * // Basic matrix operations
 * IntMatrix transposed = intMatrix.transpose();   // 3x2 matrix from 2x3
 * IntMatrix rotated = intMatrix.rotate90();   // 90-degree rotation
 * IntMatrix flipped = intMatrix.flipH();   // Horizontal flip
 *
 * // Stream-based processing
 * intMatrix.streamH()                            // Stream all elements
 *     .filter(x -> x > 0)
 *     .sum();
 *
 * intMatrix.streamV()                           // Stream by columns
 *     .mapToObj(Arrays::toString)
 *     .forEach(System.out::println);
 *
 * // Mathematical operations (type-specific)
 * IntMatrix result = intMatrix.add(5);   // Add scalar to all elements
 * IntMatrix otherMatrix = IntMatrix.of(new int[][] {{6, 5, 4}, {3, 2, 1}});
 * IntMatrix sum = intMatrix.add(otherMatrix);   // Element-wise addition
 * }</pre>
 *
 * <p><b>Advanced Stream Processing Examples:</b>
 * <pre>{@code
 * // Complex stream operations with multiple access patterns
 * DoubleMatrix analyticsMatrix = DoubleMatrix.of(new double[][] {{1.0, 3.0, 5.0}, {2.0, 4.0, 8.0}});
 * int rowIndex = 0;
 * double[] row = analyticsMatrix.row(rowIndex);
 * double mean = java.util.stream.DoubleStream.of(row).average().orElse(0.0);
 * double variance = java.util.stream.DoubleStream.of(row)
 *     .map(x -> Math.pow(x - mean, 2))
 *     .average()
 *     .orElse(0.0);
 *
 * IntMatrix localMaximaInput = IntMatrix.of(new int[][] {{1, 2, 1}, {2, 9, 2}, {1, 2, 1}});
 * IntMatrix maxima = IntMatrix.of(new int[localMaximaInput.rowCount()][localMaximaInput.columnCount()]);
 * for (int i = 1; i < localMaximaInput.rowCount() - 1; i++) {
 *     for (int j = 1; j < localMaximaInput.columnCount() - 1; j++) {
 *         int current = localMaximaInput.get(i, j);
 *         boolean isMaxima = localMaximaInput.adjacent8Points(i, j)
 *             .allMatch(point -> current >= localMaximaInput.get(point.rowIndex(), point.columnIndex()));
 *         if (isMaxima) {
 *             maxima.set(i, j, 1);
 *         }
 *     }
 * }
 *
 * Matrix<Double> input = Matrix.of(new Double[][] {{2.0, 4.0}, {6.0, 8.0}});
 * Matrix<Double> normalized = Matrix.of(Matrixes.newArray(input.rowCount(), input.columnCount(), Double.class));
 * input.pointsC().forEach(colStream -> {
 *     List<Point> colPoints = colStream.toList();
 *     double max = colPoints.stream()
 *         .mapToDouble(p -> input.get(p.rowIndex(), p.columnIndex()))
 *         .max()
 *         .orElse(1.0);
 *
 *     colPoints.forEach(p -> {
 *         double normalizedValue = input.get(p.rowIndex(), p.columnIndex()) / max;
 *         normalized.set(p.rowIndex(), p.columnIndex(), normalizedValue);
 *     });
 * });
 * }</pre>
 *
 * <p><b>Naming Conventions and Terminology:</b>
 * <ul>
 *   <li><b>R:</b> Row-related operations and dimensions</li>
 *   <li><b>C:</b> Column-related operations and dimensions</li>
 *   <li><b>H:</b> Horizontal operations (row-wise processing)</li>
 *   <li><b>V:</b> Vertical operations (column-wise processing)</li>
 *   <li><b>D:</b> Diagonal operations (diagonal traversal)</li>
 *   <li><b>Stream Methods:</b> {@code stream*()} methods for functional programming patterns</li>
 *   <li><b>Transformation Methods:</b> Return new matrix instances preserving immutability</li>
 * </ul>
 *
 * <p><b>Memory and Performance Characteristics:</b>
 * <ul>
 *   <li><b>Storage Efficiency:</b> Primitive arrays eliminate boxing overhead for numerical types</li>
 *   <li><b>Cache Locality:</b> Row-major storage order optimizes CPU cache usage patterns</li>
 *   <li><b>Parallel Processing:</b> Stream operations support parallel execution for large matrices</li>
 *   <li><b>Memory Layout:</b> Contiguous memory allocation for optimal performance</li>
 *   <li><b>Lazy Evaluation:</b> Stream operations use lazy evaluation where applicable</li>
 *   <li><b>Copy-on-Write:</b> Immutable operations create new instances efficiently</li>
 * </ul>
 *
 * <p><b>Thread Safety and Concurrency:</b>
 * <ul>
 *   <li><b>Immutable Operations:</b> All transformation operations return new instances</li>
 *   <li><b>Thread-Safe Reads:</b> Multiple threads can safely read from the same matrix</li>
 *   <li><b>Parallel Streams:</b> Built-in support for parallel stream processing</li>
 *   <li><b>Concurrent Access:</b> Read operations are inherently thread-safe</li>
 *   <li><b>Modification Safety:</b> Mutating operations should be externally synchronized</li>
 * </ul>
 *
 * <p><b>Integration with Mathematical Libraries:</b>
 * <ul>
 *   <li><b>Linear Algebra:</b> Foundation for BLAS-like operations and matrix decompositions</li>
 *   <li><b>Statistical Computing:</b> Support for statistical analysis and data science operations</li>
 *   <li><b>Machine Learning:</b> Optimized for feature matrices and mathematical modeling</li>
 *   <li><b>Scientific Computing:</b> High-performance numerical computations and simulations</li>
 *   <li><b>Image Processing:</b> Efficient representation for pixel data and convolution operations</li>
 * </ul>
 *
 * <p><b>Type System and Pattern Matching:</b>
 * <ul>
 *   <li><b>Sealed Hierarchy:</b> Enables exhaustive pattern matching in switch expressions</li>
 *   <li><b>Type Inference:</b> Generic parameters enable better type inference in client code</li>
 *   <li><b>Variance:</b> Careful variance design for safe type relationships</li>
 *   <li><b>Self-Types:</b> X parameter enables fluent API patterns and method chaining</li>
 * </ul>
 *
 * <p><b>Common Anti-Patterns to Avoid:</b>
 * <ul>
 *   <li>Attempting to extend AbstractMatrix directly (sealed class prevents this)</li>
 *   <li>Using Object matrices when primitive matrices would be more efficient</li>
 *   <li>Ignoring the immutable nature of transformation operations</li>
 *   <li>Not leveraging stream operations for complex matrix processing</li>
 *   <li>Manual index iteration instead of using provided access methods</li>
 *   <li>Synchronizing on matrix instances for thread safety (use external synchronization)</li>
 * </ul>
 *
 * <p><b>Best Practices and Recommendations:</b>
 * <ul>
 *   <li>Choose the most specific matrix type for your data (e.g., IntMatrix vs Matrix&lt;Integer&gt;)</li>
 *   <li>Leverage stream operations for complex transformations and analysis</li>
 *   <li>Use parallel streams for large matrices and CPU-intensive operations</li>
 *   <li>Prefer immutable operations to maintain data integrity</li>
 *   <li>Cache frequently accessed matrix views and transformations</li>
 *   <li>Consider memory usage when working with very large matrices</li>
 *   <li>Use appropriate numerical precision for your computational requirements</li>
 * </ul>
 *
 * <p><b>Example: Scientific Computing Application</b>
 * <pre>{@code
 * // Matrix multiplication
 * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1, 2, 3}, {4, 5, 6}});
 * DoubleMatrix b = DoubleMatrix.of(new double[][] {{7, 8}, {9, 10}, {11, 12}});
 * if (a.columnCount() != b.rowCount()) {
 *     throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
 * }
 *
 * DoubleMatrix multiplied = DoubleMatrix.of(new double[a.rowCount()][b.columnCount()]);
 * java.util.stream.IntStream.range(0, a.rowCount()).parallel().forEach(i -> {
 *     java.util.stream.IntStream.range(0, b.columnCount()).forEach(j -> {
 *         double sum = java.util.stream.IntStream.range(0, a.columnCount())
 *             .mapToDouble(k -> a.get(i, k) * b.get(k, j))
 *             .sum();
 *         multiplied.set(i, j, sum);
 *     });
 * });
 *
 * // Column statistics
 * double[] stats = new double[multiplied.columnCount()];
 * for (int c = 0; c < multiplied.columnCount(); c++) {
 *     stats[c] = java.util.stream.DoubleStream.of(multiplied.column(c))
 *         .filter(x -> !Double.isNaN(x))
 *         .average()
 *         .orElse(0.0);
 * }
 *
 * // Image-style convolution
 * IntMatrix image = IntMatrix.of(new int[][] {{0, 1, 0}, {1, 2, 1}, {0, 1, 0}});
 * DoubleMatrix filter = DoubleMatrix.of(new double[][] {{0, 1, 0}, {1, 4, 1}, {0, 1, 0}});
 * IntMatrix filtered = IntMatrix.of(new int[image.rowCount()][image.columnCount()]);
 * int filterSize = filter.rowCount();
 * int offset = filterSize / 2;
 * java.util.stream.IntStream.range(offset, image.rowCount() - offset).parallel().forEach(i -> {
 *     java.util.stream.IntStream.range(offset, image.columnCount() - offset).forEach(j -> {
 *         double sum = 0.0;
 *         for (int fi = 0; fi < filterSize; fi++) {
 *             for (int fj = 0; fj < filterSize; fj++) {
 *                 sum += image.get(i - offset + fi, j - offset + fj) * filter.get(fi, fj);
 *             }
 *         }
 *         filtered.set(i, j, (int) Math.round(sum));
 *     });
 * });
 * }</pre>
 *
 * <p><b>Debugging and Profiling Considerations:</b>
 * <ul>
 *   <li><b>Matrix Visualization:</b> Built-in toString() methods for debugging matrix content</li>
 *   <li><b>Performance Monitoring:</b> Stream operations can be profiled for optimization</li>
 *   <li><b>Memory Analysis:</b> Monitor heap usage for large matrix operations</li>
 *   <li><b>Parallel Debugging:</b> Be aware of parallel stream debugging challenges</li>
 * </ul>
 *
 * <p><b>Comparison with Alternative Matrix Libraries:</b>
 * <ul>
 *   <li><b>vs. Apache Commons Math:</b> More specialized type system vs. general mathematical library</li>
 *   <li><b>vs. EJML:</b> Functional programming support vs. pure performance focus</li>
 *   <li><b>vs. ND4J:</b> Java-native vs. native library integration with GPU support</li>
 *   <li><b>vs. JAMA:</b> Modern Java features vs. legacy compatibility</li>
 * </ul>
 *
 * @param <A> the array type used for internal row storage (e.g., {@code int[]}, {@code double[]}, {@code Object[]})
 * @param <PL> the primitive list type for flattened matrix operations (e.g., {@code IntList}, {@code DoubleList}, {@code List<T>})
 * @param <ES> the element stream type for streaming individual matrix elements (e.g., {@code IntStream}, {@code DoubleStream}, {@code Stream<T>})
 * @param <RS> the row/column stream type for streaming matrix rows or columns (e.g., {@code Stream<int[]>}, {@code Stream<double[]>})
 * @param <X> the concrete matrix implementation type for fluent API support and method chaining
 *
 * @see BooleanMatrix
 * @see CharMatrix
 * @see ByteMatrix
 * @see ShortMatrix
 * @see IntMatrix
 * @see LongMatrix
 * @see FloatMatrix
 * @see DoubleMatrix
 * @see Matrix
 * @see com.landawn.abacus.util.stream.Stream
 * @see com.landawn.abacus.util.stream.IntStream
 * @see com.landawn.abacus.util.Arrays
 * @see <a href="https://en.wikipedia.org/wiki/Matrix_(mathematics)">Mathematical Matrix Theory</a>
 */
public abstract sealed class AbstractMatrix<A, PL, ES, RS, X extends AbstractMatrix<A, PL, ES, RS, X>>
        permits BooleanMatrix, CharMatrix, ByteMatrix, ShortMatrix, DoubleMatrix, FloatMatrix, IntMatrix, LongMatrix, Matrix {

    protected static final String ARRAY_PRINT_SEPARATOR = Arrays.ARRAY_PRINT_SEPARATOR;

    static final char CHAR_0 = (char) 0;

    static final byte BYTE_0 = (byte) 0;

    static final byte BYTE_1 = (byte) 1;

    static final short SHORT_0 = (short) 0;

    // ==================== Standardized Exception Message Constants ====================
    // These constants ensure consistent exception messages across all matrix classes.

    /** Exception message format for row index out of bounds. Arguments: rowIndex, rowCount */
    protected static final String MSG_ROW_INDEX_OUT_OF_BOUNDS = "Row index %d is out of bounds. Valid range is [0, %d)";

    /** Exception message format for column index out of bounds. Arguments: columnIndex, columnCount */
    protected static final String MSG_COLUMN_INDEX_OUT_OF_BOUNDS = "Column index %d is out of bounds. Valid range is [0, %d)";

    /** Exception message format for row length mismatch. Arguments: expected, actual */
    protected static final String MSG_ROW_LENGTH_MISMATCH = "Row length mismatch: expected %d columns but got %d";

    /** Exception message format for column length mismatch. Arguments: expected, actual */
    protected static final String MSG_COLUMN_LENGTH_MISMATCH = "Column length mismatch: expected %d rows but got %d";

    /** Exception message format for diagonal array length mismatch. Arguments: expected, actual */
    protected static final String MSG_DIAGONAL_LENGTH_MISMATCH = "Diagonal array length must equal matrix size: expected %d but got %d";

    /** Exception message format for non-square matrix error. Arguments: rowCount, columnCount */
    protected static final String MSG_MATRIX_NOT_SQUARE = "Matrix must be square: current dimensions are %d rows x %d columns";

    /** Exception message format for shape mismatch between two matrices. Arguments: rows1, cols1, rows2, cols2 */
    protected static final String MSG_SHAPE_MISMATCH = "Matrix shape mismatch: this matrix is %dx%d but other is %dx%d";

    /** Exception message format for non-rectangular matrix. Arguments: firstRowLength, currentRowIndex, currentRowLength */
    protected static final String MSG_NOT_RECTANGULAR = "Matrix must be rectangular: row 0 has %d columns, but row %d has %d columns";

    /** Exception message format for vstack column count mismatch. Arguments: thisColumnCount, otherColumnCount */
    protected static final String MSG_VSTACK_COLUMN_MISMATCH = "Column count mismatch for vstack: this matrix has %d columns but other has %d";

    /** Exception message format for hstack row count mismatch. Arguments: thisRowCount, otherRowCount */
    protected static final String MSG_HSTACK_ROW_MISMATCH = "Row count mismatch for hstack: this matrix has %d rows but other has %d";

    /** Exception message format for negative dimension. Arguments: paramName, value */
    protected static final String MSG_NEGATIVE_DIMENSION = "%s cannot be negative: %d";

    /** Exception message format for non-positive repeats. Arguments: rowRepeats, colRepeats */
    protected static final String MSG_REPEATS_NOT_POSITIVE = "rowRepeats and colRepeats must be positive: rowRepeats=%d, colRepeats=%d";

    // ==================== End Exception Message Constants ====================

    /**
     * The number of rows in this matrix.
     * This value is immutable after matrix creation.
     */
    final int rowCount;

    /**
     * The number of columns in this matrix.
     * This value is immutable after matrix creation.
     */
    final int columnCount;

    /**
     * The total number of elements in this matrix (rows × columns).
     * This value is cached for performance and is immutable after matrix creation.
     */
    final long elementCount;

    /**
     * The underlying two-dimensional array storing the matrix data.
     * Direct access to this array should be avoided; use the provided methods instead.
     */
    final A[] a;

    /**
     * Constructs a new AbstractMatrix with the specified two-dimensional array.
     * The constructor validates that all rows have the same length.
     * 
     * @param a the two-dimensional array containing matrix data
     * @throws IllegalArgumentException if the array is null or if rows have different lengths (not rectangular)
     */
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    protected AbstractMatrix(final A[] a) {
        N.checkArgNotNull(a, "Matrix array cannot be null");

        this.a = a;
        rowCount = a.length;

        if (rowCount > 0) {
            N.checkArgument(a[0] != null, "Row 0 cannot be null");
        }

        columnCount = rowCount == 0 ? 0 : length(a[0]);

        if (a.length > 1) {
            for (int i = 1, len = a.length; i < len; i++) {
                N.checkArgument(a[i] != null, "Row %s cannot be null", i);
                if (length(a[i]) != columnCount) {
                    throw new IllegalArgumentException(String.format(MSG_NOT_RECTANGULAR, columnCount, i, length(a[i])));
                }
            }
        }

        elementCount = (long) columnCount * rowCount;
    }

    /**
     * Returns the component type of the elements in this matrix.
     * For primitive matrices, this returns the corresponding primitive class (e.g., {@code int.class} for {@link IntMatrix}).
     * For object matrices, this returns the element's class type.
     *
     * <p>This method is useful for reflection-based operations and type checking at runtime.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Class<?> type = intMatrix.componentType();   // Returns int.class
     *
     * Matrix<String> stringMatrix = Matrix.of(new String[][] {{"a", "b"}, {"c", "d"}});
     * Class<?> strType = stringMatrix.componentType();   // Returns String.class
     * }</pre>
     *
     * @return the Class object representing the component type of matrix elements
     */
    @SuppressWarnings("rawtypes")
    public abstract Class componentType();

    /**
     * Returns the underlying two-dimensional array of this matrix.
     * This method exposes the internal array representation for performance reasons and should be used with caution
     * as modifications to the returned array will directly affect the matrix.
     *
     * <p><strong>Warning:</strong> This method returns the actual internal array, not a copy.
     * Any changes made to the returned array will be reflected in the matrix.
     * If you need an independent copy, use {@link #copy()} instead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * int[][] array = matrix.array();
     * array[0][0] = 10;  // This WILL modify the matrix!
     * // matrix now contains {{10, 2}, {3, 4}}
     * }</pre>
     *
     * @return the underlying two-dimensional array (not a copy)
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public A[] array() {
        return a;
    }

    /**
     * Returns the number of rows in this matrix.
     *
     * @return the number of rows
     */
    public int rowCount() {
        return rowCount;
    }

    /**
     * Returns the number of columns in this matrix.
     *
     * @return the number of columns
     */
    public int columnCount() {
        return columnCount;
    }

    /**
     * Returns the total number of elements in this matrix (rows × columns).
     *
     * @return the total number of elements
     */
    public long elementCount() {
        return elementCount;
    }

    /**
     * Checks if the matrix is empty (contains no elements).
     * A matrix is considered empty if either the number of rows or columns is zero,
     * resulting in a total count of zero elements.
     *
     * <p>Note: An empty matrix can have various shapes such as 0×0, 0×n, or m×0.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix empty1 = IntMatrix.of(new int[0][0]);
     * boolean isEmpty1 = empty1.isEmpty();   // Returns true (0×0)
     *
     * IntMatrix empty2 = IntMatrix.of(new int[0][5]);
     * boolean isEmpty2 = empty2.isEmpty();   // Returns true (0×5)
     *
     * IntMatrix notEmpty = IntMatrix.of(new int[][] {{1}});
     * boolean isEmpty3 = notEmpty.isEmpty();   // Returns false (1×1)
     * }</pre>
     *
     * @return {@code true} if the matrix has no elements (count == 0), {@code false} otherwise
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Prints the matrix to standard output in a formatted, human-readable manner and returns the output string.
     * Each concrete implementation provides its own formatting based on the element type.
     * This method is primarily intended for debugging and logging purposes.
     *
     * <p>The exact output format depends on the matrix type:
     * <ul>
     *   <li>Numeric matrices typically display values aligned in rows and columns</li>
     *   <li>Object matrices display using the {@code toString()} method of elements</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * String output = matrix.println();
     * // Prints to console:
     * // [1, 2]
     * // [3, 4]
     * // And returns the same string
     * }</pre>
     *
     * @return the formatted string representation of the matrix that was printed to standard output
     */
    public abstract String println();

    /**
     * Creates a copy of this matrix.
     * The returned matrix is a completely independent copy with its own underlying array;
     * modifications to one matrix do not affect the other.
     *
     * <p>This method creates new array instances and copies all element values.
     * For large matrices, this operation can be memory and time intensive.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix copy = original.copy();
     * copy.set(0, 0, 10);   // Original matrix remains unchanged
     * // original: {{1, 2}, {3, 4}}
     * // copy:     {{10, 2}, {3, 4}}
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix with the same dimensions and values
     */
    public abstract X copy();

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows (with all columns) and is completely
     * independent from the original matrix.
     *
     * <p>This is equivalent to calling {@code copy(fromRowIndex, toRowIndex, 0, columnCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntMatrix subMatrix = matrix.copy(0, 2);   // Contains rows 0 and 1
     * // subMatrix: {{1, 2}, {3, 4}}
     *
     * IntMatrix lastRow = matrix.copy(2, 3);   // Contains only row 2
     * // lastRow: {{5, 6}}
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new matrix containing the specified rows with dimensions (toRowIndex - fromRowIndex) × columnCount
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    public abstract X copy(int fromRowIndex, int toRowIndex);

    /**
     * Creates a copy of a rectangular region from this matrix.
     * The returned matrix contains only the specified rows and columns and is completely
     * independent from the original matrix.
     *
     * <p>This method allows you to extract any rectangular subregion of the matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix subMatrix = matrix.copy(0, 2, 1, 3);
     * // subMatrix: {{2, 3}, {5, 6}} (rows 0-1, columns 1-2)
     *
     * IntMatrix centerElement = matrix.copy(1, 2, 1, 2);
     * // centerElement: {{5}} (just the center element)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new matrix containing the specified region with dimensions
     *         (toRowIndex - fromRowIndex) × (toColumnIndex - fromColumnIndex)
     * @throws IndexOutOfBoundsException if any index is out of bounds, fromRowIndex &gt; toRowIndex, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract X copy(int fromRowIndex, int toRowIndex, int fromColumnIndex, int toColumnIndex);

    /**
     * Rotates this matrix 90 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the last row of the original matrix reading upward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (j, rowCount - 1 - i) in the rotated matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 90° clockwise:
     * // 1 2 3        7 4 1
     * // 4 5 6   =>   8 5 2
     * // 7 8 9        9 6 3
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix rotated = original.rotate90();   // 3×3 remains 3×3
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise with dimensions columnCount × rowCount
     */
    public abstract X rotate90();

    /**
     * Rotates this matrix 180 degrees.
     * This is equivalent to flipping both horizontally and vertically, reversing the
     * order of all elements. The resulting matrix has the same dimensions as the original.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (rowCount - 1 - i, columnCount - 1 - j) in the rotated matrix.</p>
     *
     * <p>This operation is equivalent to calling {@code rotate90().rotate90()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 180°:
     * // 1 2 3        9 8 7
     * // 4 5 6   =>   6 5 4
     * // 7 8 9        3 2 1
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix rotated = original.rotate180();   // Dimensions remain 2×3
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees with the same dimensions (rowCount × columnCount)
     */
    public abstract X rotate180();

    /**
     * Rotates this matrix 270 degrees clockwise (or 90 degrees counter-clockwise).
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the first row of the original matrix reading downward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (columnCount - 1 - j, i) in the rotated matrix.</p>
     *
     * <p>This operation is equivalent to calling {@code rotate90().rotate90().rotate90()}
     * or {@code transpose().rotate180().transpose()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 270° clockwise:
     * // 1 2 3        3 6 9
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        1 4 7
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix rotated = original.rotate270();   // 3×3 becomes 3×3
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise with dimensions columnCount × rowCount
     */
    public abstract X rotate270();

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rowCount × columnCount becomes columnCount × rowCount).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Transpose formula: element at position (i, j) in the original matrix
     * moves to position (j, i) in the transposed matrix.</p>
     *
     * <p>The transpose is a fundamental matrix operation used in linear algebra,
     * statistics, and many matrix algorithms.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Transposed:
     * // 1 2 3        1 4 7
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        3 6 9
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix transposed = original.transpose();   // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions columnCount × rowCount
     */
    public abstract X transpose();

    /**
     * Reshapes this matrix to have the specified number of columns.
     * The number of rows is automatically calculated based on the total element count.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. If the total element count is not evenly divisible by the new column count,
     * the last row will be padded with default values (0 for numeric types, false for boolean, null for objects).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>The new row count is calculated as: {@code ceiling(elementCount / newColumnCount)}</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(2);   // Becomes [[1, 2], [3, 4], [5, 6]]
     *
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped2 = matrix2.reshape(4);   // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newColumnCount the number of columns in the reshaped matrix (must be positive)
     * @return a new matrix with the specified number of columns
     * @throws IllegalArgumentException if newColumnCount &lt;= 0
     */
    public X reshape(final int newColumnCount) {
        N.checkArgument(newColumnCount > 0, "newColumnCount must be positive, but got: %s", newColumnCount);

        return reshape((int) (elementCount % newColumnCount == 0 ? elementCount / newColumnCount : elementCount / newColumnCount + 1), newColumnCount);
    }

    /**
     * Reshapes this matrix to have the specified dimensions.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. If the new shape has fewer elements than the original, excess elements are
     * discarded. If the new shape has more elements, the extra positions are filled with
     * default values (0 for numeric types, false for boolean, null for objects).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>This is a fundamental operation for restructuring matrix data without changing
     * the underlying element values (within the bounds of the new shape).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(3, 2);    // Becomes [[1, 2], [3, 4], [5, 6]]
     * IntMatrix extended = matrix.reshape(2, 4);    // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * IntMatrix truncated = matrix.reshape(1, 3);   // Becomes [[1, 2, 3]] (remaining elements discarded)
     * }</pre>
     *
     * @param newRowCount the number of rows in the reshaped matrix (must be positive)
     * @param newColumnCount the number of columns in the reshaped matrix (must be positive)
     * @return a new matrix with the specified dimensions (newRowCount × newColumnCount)
     * @throws IllegalArgumentException if newRowCount &lt;= 0 or newColumnCount &lt;= 0
     */
    public abstract X reshape(int newRowCount, int newColumnCount);

    /**
     * Checks if this matrix has the same shape (dimensions) as another matrix.
     * Two matrices have the same shape if they have the same number of rows and columns.
     * The element values are not compared, only the dimensions.
     *
     * <p>This method is useful for validating matrix compatibility before operations
     * that require same-shaped matrices (e.g., element-wise addition or subtraction).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});
     * boolean sameShape = m1.isSameShape(m2);   // Returns true (both are 2×2)
     *
     * IntMatrix m3 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * boolean sameShape2 = m1.isSameShape(m3);   // Returns false (2×2 vs 2×3)
     * }</pre>
     *
     * @param x the matrix to compare with
     * @return {@code true} if both matrices have the same dimensions, {@code false} otherwise
     */
    public boolean isSameShape(final X x) {
        return rowCount == x.rowCount && columnCount == x.columnCount;
    }

    /**
     * Repeats each element in the matrix the specified number of times in both dimensions.
     * Each element is expanded into a block of size rowRepeats × colRepeats.
     * The resulting matrix has dimensions (rows × rowRepeats) × (columnCount × colRepeats).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>This operation is similar to MATLAB's repelem function. Each element becomes a block,
     * effectively creating a "zoomed in" version of the matrix where each original element
     * occupies multiple positions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    repelem(2, 2):
     * // 1 2          1 1 2 2
     * // 3 4     =>   1 1 2 2
     * //              3 3 4 4
     * //              3 3 4 4
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix repeated = matrix.repelem(2, 2);   // 2×2 becomes 4×4
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each element in the row direction (must be &gt;= 1)
     * @param colRepeats number of times to repeat each element in the column direction (must be &gt;= 1)
     * @return a new matrix with repeated elements, dimensions (rows × rowRepeats) × (columnCount × colRepeats)
     * @throws IllegalArgumentException if rowRepeats &lt; 1 or colRepeats &lt; 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repelem.html">MATLAB repelem</a>
     */
    public abstract X repelem(int rowRepeats, int colRepeats);

    /**
     * Repeats the entire matrix the specified number of times in both dimensions.
     * The matrix is tiled rowRepeats times vertically and colRepeats times horizontally.
     * The resulting matrix has dimensions (rows × rowRepeats) × (columnCount × colRepeats).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>This operation is similar to MATLAB's repmat function. The entire matrix pattern
     * is replicated, creating a tiled arrangement.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    repmat(2, 2):
     * // 1 2          1 2 1 2
     * // 3 4     =>   3 4 3 4
     * //              1 2 1 2
     * //              3 4 3 4
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix tiled = matrix.repmat(2, 2);   // 2×2 becomes 4×4
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix in the row direction (must be &gt;= 1)
     * @param colRepeats number of times to repeat the matrix in the column direction (must be &gt;= 1)
     * @return a new matrix with the original matrix repeated, dimensions (rows × rowRepeats) × (columnCount × colRepeats)
     * @throws IllegalArgumentException if rowRepeats &lt; 1 or colRepeats &lt; 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repmat.html">MATLAB repmat</a>
     */
    public abstract X repmat(int rowRepeats, int colRepeats);

    /**
     * Flattens the matrix into a one-dimensional list.
     * Elements are taken in row-major order (row by row from left to right).
     * The returned list is a new instance containing all matrix elements.
     *
     * <p>The flattening operation converts a two-dimensional matrix structure into a one-dimensional sequence,
     * which is useful for operations that work on linear data structures.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntList flat = matrix.flatten();   // Returns [1, 2, 3, 4]
     *
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntList flat2 = matrix2.flatten();   // Returns [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a new list containing all elements in row-major order with size equal to {@code elementCount}
     */
    public abstract PL flatten();

    /**
     * Applies an operation to the flattened view of the matrix and updates the matrix with the result.
     * This method provides access to the underlying array representation in row-major order,
     * executes the operation on it, and the changes are reflected back in the matrix structure.
     *
     * <p>This is useful for operations that are easier to perform on a flat array representation,
     * such as sorting all elements, applying statistical transformations, or batch updates.</p>
     *
     * <p><strong>Note:</strong> The operation modifies the array in place, which affects the matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{3, 1, 4}, {1, 5, 9}});
     * matrix.flatOp(a -> Arrays.sort(a));   // Sorts all elements
     * // Matrix becomes [[1, 1, 3], [4, 5, 9]] (elements sorted in row-major order)
     *
     * matrix.flatOp(a -> { for (int i = 0; i < a.length; i++) a[i] *= 2; });
     * // Doubles all elements
     * }</pre>
     *
     * @param <E> the type of exception that the operation might throw
     * @param op the operation to apply to the flattened array (receives array type A, not A[])
     * @throws E if the operation throws an exception
     */
    public abstract <E extends Exception> void flatOp(Throwables.Consumer<? super A, E> op) throws E;

    /**
     * Performs the given action for each position in the matrix.
     * The action receives the row and column indices for each element.
     * Elements are processed in row-major order (row by row from left to right).
     * For large matrices, the operation may be automatically parallelized for better performance.
     *
     * <p>This method is useful when you need to access matrix positions without caring about
     * the actual element values, or when the element access logic is handled inside the action.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.forEach((i, j) -> {
     *     System.out.println("Position: (" + i + "," + j + ")");
     * });
     *
     * // Count elements on the main diagonal
     * AtomicInteger diagonalCount = new AtomicInteger(0);
     * matrix.forEach((i, j) -> {
     *     if (i == j) diagonalCount.incrementAndGet();
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform for each position, receives (rowIndex, columnIndex)
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.IntBiConsumer<E> action) throws E {
        if (Matrixes.isParallelizable(this)) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> elementAction = action::accept;
            Matrixes.run(rowCount, columnCount, elementAction, true);
        } else {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * Performs the given action for each position in the specified rectangular region of the matrix.
     * The action receives the row and column indices for each element in the region.
     * Elements are processed in row-major order within the specified region.
     * For large regions, the operation may be automatically parallelized for better performance.
     *
     * <p>This allows selective processing of matrix subregions without creating a copy.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process only a 2×2 subregion starting at (1,1)
     * matrix.forEach(1, 3, 1, 3, (i, j) -> {
     *     System.out.println("Processing element at (" + i + "," + j + ")");
     * });
     *
     * // Process only the first column
     * matrix.forEach(0, matrix.rowCount, 0, 1, (i, j) -> {
     *     // Process each element in column 0
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform for each position, receives (rowIndex, columnIndex)
     * @throws IndexOutOfBoundsException if any index is out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        if (Matrixes.isParallelizable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> elementAction = action::accept;
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, elementAction, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * Performs the given action for each position in the matrix, providing the matrix itself as a parameter.
     * The action receives the row index, column index, and the matrix instance.
     * Elements are processed in row-major order (row by row from left to right).
     * For large matrices, the operation may be automatically parallelized for better performance.
     *
     * <p>This variant is useful when the action needs access to matrix elements or methods,
     * allowing you to read/write values or use matrix operations within the action.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.forEach((i, j, m) -> {
     *     int value = m.get(i, j);
     *     System.out.println("Value at (" + i + "," + j + ") is " + value);
     * });
     *
     * // Set each element to the sum of its indices
     * matrix.forEach((i, j, m) -> m.set(i, j, i + j));
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform, receiving (rowIndex, columnIndex, matrix)
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.BiIntObjConsumer<X, E> action) throws E {
        final X matrix = (X) this;
        if (Matrixes.isParallelizable(this)) {
            final Throwables.IntBiConsumer<E> elementAction = (i, j) -> action.accept(i, j, matrix);
            Matrixes.run(rowCount, columnCount, elementAction, true);
        } else {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    action.accept(i, j, matrix);
                }
            }
        }
    }

    /**
     * Performs the given action for each position in the specified rectangular region, providing the matrix itself.
     * The action receives the row index, column index, and the matrix instance for each position in the region.
     * Elements are processed in row-major order within the specified region.
     * For large regions, the operation may be automatically parallelized for better performance.
     *
     * <p>This combines region-based iteration with matrix access, allowing you to process
     * a subregion while having access to the entire matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.forEach(1, 3, 1, 3, (i, j, m) -> {
     *     // Process only the 2×2 subregion with access to matrix
     *     int value = m.get(i, j);
     *     System.out.println("Value at (" + i + "," + j + "): " + value);
     * });
     *
     * // Update subregion based on neighboring values
     * matrix.forEach(1, matrix.rowCount - 1, 1, matrix.columnCount - 1, (i, j, m) -> {
     *     int avg = (m.get(i-1, j) + m.get(i+1, j) + m.get(i, j-1) + m.get(i, j+1)) / 4;
     *     m.set(i, j, avg);
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform, receiving (rowIndex, columnIndex, matrix)
     * @throws IndexOutOfBoundsException if any index is out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.BiIntObjConsumer<X, E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        final X matrix = (X) this;

        if (Matrixes.isParallelizable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> elementAction = (i, j) -> action.accept(i, j, matrix);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, elementAction, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j, matrix);
                }
            }
        }
    }

    /**
     * Returns a stream of points adjacent to the specified position in the four cardinal directions
     * (up, down, left, right). Only includes points within matrix bounds, filtering out null values
     * for positions at the matrix edges.
     *
     * <p>This method is useful for grid traversal algorithms, pathfinding, and neighbor analysis
     * where only orthogonal (non-diagonal) adjacency is considered. Points are returned in the
     * order: up, right, down, left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Stream<Point> adjacent = matrix.adjacent4Points(0, 0);
     * // Returns stream of Point.of(0, 1) and Point.of(1, 0) - only right and down exist
     *
     * // Center position has all 4 neighbors
     * BooleanMatrix larger = BooleanMatrix.of(new boolean[3][3]);
     * Stream<Point> centerAdj = larger.adjacent4Points(1, 1);
     * // Returns all 4 adjacent points: (0,1), (1,2), (2,1), (1,0)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return a stream of adjacent points in cardinal directions (0-4 points depending on position)
     * @throws IndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public Stream<Point> adjacent4Points(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        final List<Point> points = new ArrayList<>(4);

        if (rowIndex > 0) {
            points.add(Point.of(rowIndex - 1, columnIndex)); // up
        }
        if (columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex, columnIndex + 1)); // right
        }
        if (rowIndex < rowCount - 1) {
            points.add(Point.of(rowIndex + 1, columnIndex)); // down
        }
        if (columnIndex > 0) {
            points.add(Point.of(rowIndex, columnIndex - 1)); // left
        }

        return Stream.of(points);
    }

    /**
     * Returns a stream of all 8 points adjacent to the specified position, including both
     * cardinal directions (up, down, left, right) and diagonal directions. Only includes
     * points within matrix bounds.
     *
     * <p>This method is useful for algorithms requiring full 8-way adjacency, such as
     * certain pathfinding algorithms, cellular automaton simulations (like Conway's Game of Life),
     * or flood fill operations. Points are returned clockwise starting from the top-left:
     * leftUp, up, rightUp, right, rightDown, down, leftDown, left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, true}, {false, true, false}, {true, false, true}});
     * Stream<Point> adjacent = matrix.adjacent8Points(1, 1);
     * // Returns stream of all 8 surrounding points for the center position
     *
     * // Corner position has only 3 neighbors
     * Stream<Point> corner = matrix.adjacent8Points(0, 0);
     * // Returns 3 points: (0,1), (1,1), (1,0)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return a stream of adjacent points in all 8 directions (0-8 points depending on position)
     * @throws IndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public Stream<Point> adjacent8Points(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        final List<Point> points = new ArrayList<>(8);

        if (rowIndex > 0 && columnIndex > 0) {
            points.add(Point.of(rowIndex - 1, columnIndex - 1)); // leftUp
        }

        if (rowIndex > 0) {
            points.add(Point.of(rowIndex - 1, columnIndex)); // up
        }

        if (rowIndex > 0 && columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex - 1, columnIndex + 1)); // rightUp
        }

        if (columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex, columnIndex + 1)); // right
        }

        if (rowIndex < rowCount - 1 && columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex + 1, columnIndex + 1)); // rightDown
        }

        if (rowIndex < rowCount - 1) {
            points.add(Point.of(rowIndex + 1, columnIndex)); // down
        }

        if (rowIndex < rowCount - 1 && columnIndex > 0) {
            points.add(Point.of(rowIndex + 1, columnIndex - 1)); // leftDown
        }

        if (columnIndex > 0) {
            points.add(Point.of(rowIndex, columnIndex - 1)); // left
        }

        return Stream.of(points);
    }

    /**
     * Returns a stream of points along the main diagonal (left-up to right-down).
     * The main diagonal consists of elements where row index equals column index.
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>LU2RD = Left-Up to Right-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> diagonal = matrix.pointsLU2RD();   // Points: (0,0), (1,1), (2,2)
     * List<Point> points = diagonal.toList();          // [(0,0), (1,1), (2,2)]
     * }</pre>
     *
     * @return a stream of {@link Point} objects representing the main diagonal positions
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public Stream<Point> pointsLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rowCount).mapToObj(i -> Point.of(i, i));
    }

    /**
     * Returns a stream of points along the anti-diagonal (right-up to left-down).
     * The anti-diagonal consists of elements where row index + column index equals (columnCount - 1).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>RU2LD = Right-Up to Left-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> antiDiagonal = matrix.pointsRU2LD();   // Points: (0,2), (1,1), (2,0)
     * List<Point> points = antiDiagonal.toList();          // [(0,2), (1,1), (2,0)]
     * }</pre>
     *
     * @return a stream of {@link Point} objects representing the anti-diagonal positions
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public Stream<Point> pointsRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rowCount).mapToObj(i -> Point.of(i, columnCount - i - 1));
    }

    /**
     * Returns a stream of all points in the matrix in row-major order (horizontal traversal).
     * Points are generated row by row from left to right, top to bottom.
     *
     * <p>H = Horizontal. This is equivalent to calling {@code pointsH(0, rowCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> allPoints = matrix.pointsH();
     * // Points in order: (0,0), (0,1), (1,0), (1,1)
     * allPoints.forEach(p -> System.out.println("Point: " + p));
     * }</pre>
     *
     * @return a stream of all {@link Point} objects in row-major order
     */
    public Stream<Point> pointsH() {
        return pointsH(0, rowCount);
    }

    /**
     * Returns a stream of points for a specific row in horizontal order (left to right).
     *
     * <p>This is equivalent to calling {@code pointsH(rowIndex, rowIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<Point> row1Points = matrix.pointsH(1);   // All points in row 1
     * // For a 3-column matrix: (1,0), (1,1), (1,2)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a stream of {@link Point} objects for all columns in the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    public Stream<Point> pointsH(final int rowIndex) {
        return pointsH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of points for a range of rows in row-major order (horizontal traversal).
     * Points are generated row by row from left to right for the specified row range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Get points from rows 1 and 2 (indices 1 and 2, not including 3)
     * Stream<Point> points = matrix.pointsH(1, 3);
     * // For a 2-column matrix: (1,0), (1,1), (2,0), (2,1)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of {@link Point} objects in the specified row range, in row-major order
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

        return IntStream.range(fromRowIndex, toRowIndex)
                .flatMapToObj(rowIndex -> IntStream.range(0, columnCount).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of all points in the matrix in column-major order (vertical traversal).
     * Points are generated column by column from top to bottom, left to right.
     *
     * <p>V = Vertical. This is equivalent to calling {@code pointsV(0, columnCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> columnOrder = matrix.pointsV();
     * // Points in order: (0,0), (1,0), (0,1), (1,1)
     * // For a 2×3 matrix, order would be: (0,0), (1,0), (0,1), (1,1), (0,2), (1,2)
     * }</pre>
     *
     * @return a stream of all {@link Point} objects in column-major order
     */
    public Stream<Point> pointsV() {
        return pointsV(0, columnCount);
    }

    /**
     * Returns a stream of points for a specific column in vertical order (top to bottom).
     *
     * <p>This is equivalent to calling {@code pointsV(columnIndex, columnIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<Point> col2Points = matrix.pointsV(2);   // All points in column 2
     * // For a 3-row matrix: (0,2), (1,2), (2,2)
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a stream of {@link Point} objects for all rows in the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    public Stream<Point> pointsV(final int columnIndex) {
        return pointsV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of points for a range of columns in column-major order (vertical traversal).
     * Points are generated column by column from top to bottom for the specified column range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Get points from columns 1 through 3 (indices 1, 2, 3, not including 4)
     * Stream<Point> points = matrix.pointsV(1, 4);
     * // For a 2-row matrix: (0,1), (1,1), (0,2), (1,2), (0,3), (1,3)
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of {@link Point} objects in the specified column range, in column-major order
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .flatMapToObj(columnIndex -> IntStream.range(0, rowCount).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of streams where each inner stream represents a row of points.
     * This allows for row-by-row processing of matrix positions.
     *
     * <p>R = Row. This is equivalent to calling {@code pointsR(0, rowCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.pointsR().forEach(rowStream -> {
     *     rowStream.forEach(point -> System.out.println("Point: " + point));
     * });
     *
     * // Collect each row's points separately
     * List<List<Point>> rowsOfPoints = matrix.pointsR()
     *     .map(Stream::toList)
     *     .toList();
     * }</pre>
     *
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one row
     */
    public Stream<Stream<Point>> pointsR() {
        return pointsR(0, rowCount);
    }

    /**
     * Returns a stream of streams for a range of rows, where each inner stream represents a row of points.
     * This allows for selective row-by-row processing of matrix positions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process rows 1 and 2 separately
     * matrix.pointsR(1, 3).forEach(rowStream -> {
     *     List<Point> rowPoints = rowStream.toList();
     *     // Process each row's points
     * });
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one row
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

        return IntStream.range(fromRowIndex, toRowIndex)
                .mapToObj(rowIndex -> IntStream.range(0, columnCount).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of streams where each inner stream represents a column of points.
     * This allows for column-by-column processing of matrix positions.
     *
     * <p>C = Column. This is equivalent to calling {@code pointsC(0, columnCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.pointsC().forEach(colStream -> {
     *     colStream.forEach(point -> System.out.println("Point: " + point));
     * });
     *
     * // Collect each column's points separately
     * List<List<Point>> columnsOfPoints = matrix.pointsC()
     *     .map(Stream::toList)
     *     .toList();
     * }</pre>
     *
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one column
     */
    public Stream<Stream<Point>> pointsC() {
        return pointsC(0, columnCount);
    }

    /**
     * Returns a stream of streams for a range of columns, where each inner stream represents a column of points.
     * This allows for selective column-by-column processing of matrix positions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process columns 2 through 4 separately
     * matrix.pointsC(2, 5).forEach(colStream -> {
     *     List<Point> colPoints = colStream.toList();
     *     // Process each column's points
     * });
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one column
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .mapToObj(columnIndex -> IntStream.range(0, rowCount).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of elements along the main diagonal (left-up to right-down).
     * The main diagonal consists of elements where row index equals column index.
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>LU2RD = Left-Up to Right-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream diagonal = matrix.streamLU2RD();   // Stream of: 1, 5, 9
     * int sum = diagonal.sum();                    // 15
     * }</pre>
     *
     * @return a stream of diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public abstract ES streamLU2RD();

    /**
     * Returns a stream of elements along the anti-diagonal (right-up to left-down).
     * The anti-diagonal consists of elements where row index + column index equals (columnCount - 1).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>RU2LD = Right-Up to Left-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream antiDiagonal = matrix.streamRU2LD();   // Stream of: 3, 5, 7
     * int sum = antiDiagonal.sum();                    // 15
     * }</pre>
     *
     * @return a stream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public abstract ES streamRU2LD();

    /**
     * Returns a stream of all elements in row-major order (horizontal traversal).
     * Elements are streamed row by row from left to right, top to bottom.
     *
     * <p>H = Horizontal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamH();   // Stream of: 1, 2, 3, 4
     * int sum = elements.sum();                // 10
     * }</pre>
     *
     * @return a stream of all elements in row-major order
     */
    public abstract ES streamH();

    /**
     * Returns a stream of elements from a specific row.
     * Elements are streamed from left to right within the row.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream row1 = matrix.streamH(1);   // Stream of: 4, 5, 6
     * int max = row1.max().orElse(0);       // 6
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a stream of elements in the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    public abstract ES streamH(final int rowIndex);

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * Elements are streamed row by row from left to right for the specified row range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntStream rows1and2 = matrix.streamH(1, 3);   // Stream of: 3, 4, 5, 6
     * long count = rows1and2.count();               // 4
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements in the specified row range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    public abstract ES streamH(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of all elements in column-major order (vertical traversal).
     * Elements are streamed column by column from top to bottom, left to right.
     *
     * <p>V = Vertical.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamV();   // Stream of: 1, 3, 2, 4
     * int sum = elements.sum();                // 10
     * }</pre>
     *
     * @return a stream of all elements in column-major order
     */
    public abstract ES streamV();

    /**
     * Returns a stream of elements from a specific column.
     * Elements are streamed from top to bottom within the column.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream col1 = matrix.streamV(1);   // Stream of: 2, 5
     * int min = col1.min().orElse(0);       // 2
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a stream of elements in the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    public abstract ES streamV(final int columnIndex);

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * Elements are streamed column by column from top to bottom for the specified column range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream columnCount1and2 = matrix.streamV(1, 3);   // Stream of: 2, 5, 3, 6
     * double avg = columnCount1and2.average().orElse(0);   // 4.0
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of elements in the specified column range
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract ES streamV(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Returns a stream of row streams.
     * Each element in the outer stream is a stream representing one row of the matrix.
     * This allows for per-row processing using stream operations.
     *
     * <p>R = Row.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.streamR().forEach(rowStream -> {
     *     int sum = rowStream.sum();   // Sum each row
     *     System.out.println("Row sum: " + sum);
     * });
     * // Output: Row sum: 6
     * //         Row sum: 15
     * }</pre>
     *
     * @return a stream of row streams
     */
    public abstract RS streamR();

    /**
     * Returns a stream of row streams for a range of rows.
     * Each element in the outer stream is a stream representing one row of the matrix
     * within the specified range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process only rows 1 and 2
     * matrix.streamR(1, 3).forEach(rowStream -> {
     *     int max = rowStream.max().orElse(0);
     *     System.out.println("Row max: " + max);
     * });
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams for the specified range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    public abstract RS streamR(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of column streams.
     * Each element in the outer stream is a stream representing one column of the matrix.
     * This allows for per-column processing using stream operations.
     *
     * <p>C = Column.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.streamC().forEach(colStream -> {
     *     double avg = colStream.average().orElse(0);
     *     System.out.println("Column average: " + avg);
     * });
     * // Output: Column average: 2.0
     * //         Column average: 3.0
     * }</pre>
     *
     * @return a stream of column streams
     */
    public abstract RS streamC();

    /**
     * Returns a stream of column streams for a range of columns.
     * Each element in the outer stream is a stream representing one column of the matrix
     * within the specified range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process columns 2 through 4
     * matrix.streamC(2, 5).forEach(colStream -> {
     *     int sum = colStream.sum();
     *     System.out.println("Column sum: " + sum);
     * });
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams for the specified range
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract RS streamC(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Executes the given action with this matrix as the parameter.
     * This method enables the functional programming pattern of passing the matrix to a consumer function
     * for side-effect operations such as logging, validation, or modification.
     *
     * <p>This method is useful for performing operations on the matrix without returning a value,
     * such as printing, logging, or passing to utility methods that expect a consumer.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Log matrix details
     * matrix.accept(m -> {
     *     System.out.println("Matrix dimensions: " + m.rowCount + "x" + m.columnCount);
     *     m.println();
     * });
     *
     * // Validate matrix before processing
     * matrix.accept(m -> {
     *     if (m.isEmpty()) {
     *         throw new IllegalStateException("Matrix cannot be empty");
     *     }
     * });
     *
     * // Modify matrix elements in place
     * matrix.accept(m -> {
     *     for (int i = 0; i < m.rowCount; i++) {
     *         m.set(i, 0, 0);   // Set first column to 0
     *     }
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the consumer action to perform on this matrix
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super X, E> action) throws E {
        action.accept((X) this);
    }

    /**
     * Applies the given function to this matrix and returns the result.
     * This method enables fluent-style transformations where the matrix needs to be passed to a function.
     * It follows the functional programming pattern of applying a function and returning its result.
     *
     * <p>This method is useful for extracting values from the matrix or transforming it
     * into a different type while maintaining a fluent interface.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double determinant = matrix.apply(MatrixUtils::calculateDeterminant);
     * String info = matrix.apply(m -> "Matrix " + m.rowCount + "x" + m.columnCount);
     *
     * // Transform matrix into a different representation
     * List<Integer> allValues = matrix.apply(m -> m.flatten().toList());
     * }</pre>
     *
     * @param <R> the result type of the function
     * @param <E> the type of exception that the function might throw
     * @param action the function to apply to this matrix
     * @return the result of applying the function to this matrix
     * @throws E if the function throws an exception
     */
    public <R, E extends Exception> R apply(final Throwables.Function<? super X, R, E> action) throws E {
        return action.apply((X) this);
    }

    /**
     * Returns the length of the given array.
     * This abstract method must be implemented by concrete subclasses to handle
     * their specific array types (primitive arrays like int[], double[], or object arrays).
     *
     * <p>Implementors should return the appropriate length for their array type:
     * <ul>
     *   <li>For primitive arrays: use the array's length field (e.g., {@code ((int[])a).length})</li>
     *   <li>For object arrays: use {@link java.lang.reflect.Array#getLength(Object)}</li>
     * </ul>
     * </p>
     *
     * @param a the array to measure
     * @return the length of the array
     */
    protected abstract int length(@SuppressWarnings("hiding") A a);

    /**
     * Validates that this matrix has the same shape (dimensions) as the given matrix.
     * This is a helper method used internally to enforce shape compatibility before
     * operations that require matrices of the same dimensions.
     *
     * @param x the matrix to compare with
     * @throws IllegalArgumentException if the matrices have different shapes (different rows or columnCount)
     */
    protected void checkSameShape(final X x) {
        N.checkArgument(this.isSameShape(x), MSG_SHAPE_MISMATCH, rowCount, columnCount, x.rowCount, x.columnCount);
    }

    protected void checkRowColumnIndex(final int rowIndex, final int columnIndex) {
        if (rowIndex < 0 || rowIndex >= rowCount) {
            throw new ArrayIndexOutOfBoundsException(String.format(MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount));
        }

        if (columnIndex < 0 || columnIndex >= columnCount) {
            throw new ArrayIndexOutOfBoundsException(String.format(MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount));
        }
    }

    /**
     * Validates that this matrix is square (has the same number of rows and columns).
     * This is a helper method used internally to enforce the square matrix requirement
     * for operations like diagonal access.
     *
     * <p>Square matrices are required for operations such as:
     * <ul>
     *   <li>{@link #streamLU2RD()} - main diagonal stream</li>
     *   <li>{@link #streamRU2LD()} - anti-diagonal stream</li>
     *   <li>{@link #pointsLU2RD()} - main diagonal points</li>
     *   <li>{@link #pointsRU2LD()} - anti-diagonal points</li>
     * </ul>
     * </p>
     *
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    protected void checkIfRowAndColumnSizeAreSame() {
        N.checkState(rowCount == columnCount, MSG_MATRIX_NOT_SQUARE, rowCount, columnCount);
    }

}
