/*
 * Copyright (C) 2016 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;
import com.landawn.abacus.util.u.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

// TODO: Auto-generated Javadoc

/**
 * @param <T>
 */
public final class Matrix<T> extends AbstractMatrix<T[], List<T>, Stream<T>, Stream<Stream<T>>, Matrix<T>> {

    final Class<T[]> arrayType;

    final Class<T> elementType;

    /**
     * @param a
     */
    public Matrix(final T[][] a) {
        super(a);
        arrayType = (Class<T[]>) this.a.getClass().getComponentType();
        elementType = (Class<T>) arrayType.getComponentType();
    }

    /**
     * @param <T>
     * @param a
     * @return
     */
    @SafeVarargs
    public static <T> Matrix<T> of(final T[]... a) {
        return new Matrix<>(a);
    }

    /**
     * @param <T>
     * @param element
     * @param len
     * @return
     * @throws IllegalArgumentException if {@code element} is {@code null}.
     * @see #repeat(Object, int, Class)
     * @see #repeatNonNull(Object, int)
     * @deprecated Prefer to {@code Matrix.repeat(Object, int, Class)}
     */
    @Deprecated
    public static <T> Matrix<T> repeat(final T element, final int len) throws IllegalArgumentException {
        N.checkArgNotNull(element, "element");

        return repeat(element, len, (Class<T>) element.getClass());
    }

    /**
     * @param <T>
     * @param element
     * @param len
     * @param elementClass
     * @return
     * @throws IllegalArgumentException
     */
    public static <T> Matrix<T> repeat(final T element, final int len, final Class<T> elementClass) throws IllegalArgumentException {
        N.checkArgNotNull(elementClass, "elementClass");

        final T[][] c = N.newArray(N.newArray(elementClass, 0).getClass(), 1);
        c[0] = N.newArray(elementClass, len);
        N.fill(c[0], element);
        return new Matrix<>(c);
    }

    /**
     * @param <T>
     * @param element
     * @param len
     * @return
     * @throws IllegalArgumentException if the specified {@code element} is {@code null}.
     */
    public static <T> Matrix<T> repeatNonNull(final T element, final int len) throws IllegalArgumentException {
        N.checkArgNotNull(element, "element");

        return repeat(element, len, (Class<T>) element.getClass());
    }

    /**
     * Diagonal LU 2 RD.
     *
     * @param <T>
     * @param leftUp2RightDownDiagonal
     * @return
     */
    public static <T> Matrix<T> diagonalLU2RD(final T[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Diagonal RU 2 LD.
     *
     * @param <T>
     * @param rightUp2LeftDownDiagonal
     * @return
     */
    public static <T> Matrix<T> diagonalRU2LD(final T[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * @param <T>
     * @param leftUp2RightDownDiagonal
     * @param rightUp2LeftDownDiagonal
     * @return
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("null")
    public static <T> Matrix<T> diagonal(final T[] leftUp2RightDownDiagonal, final T[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final Class<?> arrayClass = leftUp2RightDownDiagonal != null ? leftUp2RightDownDiagonal.getClass() : rightUp2LeftDownDiagonal.getClass();
        final Class<?> componentClass = arrayClass.getComponentType();

        final T[][] c = N.newArray(arrayClass, len);

        for (int i = 0; i < len; i++) {
            c[i] = N.newArray(componentClass, len);
        }

        if (N.isEmpty(leftUp2RightDownDiagonal)) {
            if (N.notEmpty(rightUp2LeftDownDiagonal)) {
                for (int i = 0, j = len - 1; i < len; i++, j--) {
                    c[i][j] = rightUp2LeftDownDiagonal[i];
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                c[i][i] = leftUp2RightDownDiagonal[i]; // NOSONAR
            }

            if (N.notEmpty(rightUp2LeftDownDiagonal)) {
                for (int i = 0, j = len - 1; i < len; i++, j--) {
                    c[i][j] = rightUp2LeftDownDiagonal[i];
                }
            }
        }

        return new Matrix<>(c);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return elementType;
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public T get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * @param point
     * @return
     */
    public T get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * @param i
     * @param j
     * @param val
     */
    public void set(final int i, final int j, final T val) {
        a[i][j] = val;
    }

    /**
     * @param point
     * @param val
     */
    public void set(final Point point, final T val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public Nullable<T> upOf(final int i, final int j) {
        return i == 0 ? Nullable.<T> empty() : Nullable.of(a[i - 1][j]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public Nullable<T> downOf(final int i, final int j) {
        return i == rows - 1 ? Nullable.<T> empty() : Nullable.of(a[i + 1][j]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public Nullable<T> leftOf(final int i, final int j) {
        return j == 0 ? Nullable.<T> empty() : Nullable.of(a[i][j - 1]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public Nullable<T> rightOf(final int i, final int j) {
        return j == cols - 1 ? Nullable.<T> empty() : Nullable.of(a[i][j + 1]);
    }

    /**
     * Returns the four adjacencies with order: up, right, down, left. {@code null} is set if the adjacency doesn't exist.
     *
     * @param i
     * @param j
     * @return
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns the eight adjacencies with order: left-up, up, right-up, right, right-down, down, left-down, left. {@code null} is set if the adjacency doesn't exist.
     *
     * @param i
     * @param j
     * @return
     */
    public Stream<Point> adjacent8Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        final Point leftUp = i > 0 && j > 0 ? Point.of(i - 1, j - 1) : null;
        final Point rightUp = i > 0 && j < cols - 1 ? Point.of(i - 1, j + 1) : null;
        final Point rightDown = i < rows - 1 && j < cols - 1 ? Point.of(j + 1, j + 1) : null;
        final Point leftDown = i < rows - 1 && j > 0 ? Point.of(i + 1, j - 1) : null;

        return Stream.of(leftUp, up, rightUp, right, rightDown, down, leftDown, left);
    }

    /**
     * @param rowIndex
     * @return
     * @throws IllegalArgumentException
     */
    public T[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * @param columnIndex
     * @return
     * @throws IllegalArgumentException
     */
    public T[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final T[] c = N.newArray(elementType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the row.
     *
     * @param rowIndex
     * @param row
     * @throws IllegalArgumentException
     */
    public void setRow(final int rowIndex, final T[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the column.
     *
     * @param columnIndex
     * @param column
     * @throws IllegalArgumentException
     */
    public void setColumn(final int columnIndex, final T[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * @param <E>
     * @param rowIndex
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.UnaryOperator<T, E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.apply(a[rowIndex][i]);
        }
    }

    /**
     * @param <E>
     * @param columnIndex
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.UnaryOperator<T, E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.apply(a[i][columnIndex]);
        }
    }

    /**
     * Gets the lu2rd.
     *
     * @return
     */
    public T[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final T[] res = N.newArray(elementType, rows);

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the lu2rd.
     *
     * @param diagonal the new lu2rd
     * @throws IllegalArgumentException
     */
    public void setLU2RD(final T[] diagonal) throws IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Update LU 2 RD.
     *
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateLU2RD(final Throwables.UnaryOperator<T, E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.apply(a[i][i]);
        }
    }

    /**
     * Gets the ru2ld.
     *
     * @return
     */
    public T[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final T[] res = N.newArray(elementType, rows);

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the ru2ld.
     *
     * @param diagonal the new ru2ld
     * @throws IllegalArgumentException
     */
    public void setRU2LD(final T[] diagonal) throws IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Update RU 2 LD.
     *
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateRU2LD(final Throwables.UnaryOperator<T, E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.apply(a[i][cols - i - 1]);
        }
    }

    // TODO should the method name be "replaceAll"? If change the method name to replaceAll, what about updateLU2RD/updateRU2LD?

    /**
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateAll(final Throwables.UnaryOperator<T, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Update all elements based on points.
     *
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<? extends T, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * @param <E>
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public <E extends Exception> void replaceIf(final Throwables.Predicate<? super T, E> predicate, final T newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replace elements by <code>Predicate.test(i, j)</code> based on points
     *
     * @param <E>
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final T newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> Matrix<T> map(final Throwables.UnaryOperator<T, E> func) throws E {
        return map(func, elementType);
    }

    /**
     * @param <R>
     * @param <E>
     * @param func
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public <R, E extends Exception> Matrix<R> map(final Throwables.Function<? super T, R, E> func, final Class<R> targetElementType) throws E {
        final R[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Map to boolean.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> BooleanMatrix mapToBoolean(final Throwables.ToBooleanFunction<? super T, E> func) throws E {
        final boolean[][] result = new boolean[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsBoolean(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Map to byte.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> ByteMatrix mapToByte(final Throwables.ToByteFunction<? super T, E> func) throws E {
        final byte[][] result = new byte[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsByte(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Map to char.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> CharMatrix mapToChar(final Throwables.ToCharFunction<? super T, E> func) throws E {
        final char[][] result = new char[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsChar(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Map to short.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> ShortMatrix mapToShort(final Throwables.ToShortFunction<? super T, E> func) throws E {
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsShort(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Map to int.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> IntMatrix mapToInt(final Throwables.ToIntFunction<? super T, E> func) throws E {
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsInt(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Map to long.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> LongMatrix mapToLong(final Throwables.ToLongFunction<? super T, E> func) throws E {
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsLong(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Map to float.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> FloatMatrix mapToFloat(final Throwables.ToFloatFunction<? super T, E> func) throws E {
        final float[][] result = new float[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsFloat(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Map to double.
     *
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> DoubleMatrix mapToDouble(final Throwables.ToDoubleFunction<? super T, E> func) throws E {
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsDouble(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * @param val
     */
    public void fill(final T val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * @param b
     */
    public void fill(final T[][] b) {
        fill(0, 0, b);
    }

    /**
     * @param fromRowIndex
     * @param fromColumnIndex
     * @param b
     * @throws IndexOutOfBoundsException
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final T[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * @return
     */
    @Override
    public Matrix<T> copy() {
        final T[][] c = N.newArray(arrayType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new Matrix<>(c);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Matrix<T> copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final T[][] c = N.newArray(arrayType, toRowIndex - fromRowIndex);

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new Matrix<>(c);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Matrix<T> copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final T[][] c = N.newArray(arrayType, toRowIndex - fromRowIndex);

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new Matrix<>(c);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    public Matrix<T> extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, null);
    }

    /**
     * @param newRows
     * @param newCols
     * @param defaultValueForNewCell
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix<T> extend(final int newRows, final int newCols, final T defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != null;
            final T[][] b = N.newArray(arrayType, newRows);

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : (T[]) N.newArray(elementType, newCols);

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new Matrix<>(b);
        }
    }

    /**
     * @param toUp
     * @param toDown
     * @param toLeft
     * @param toRight
     * @return
     */
    public Matrix<T> extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, null);
    }

    /**
     * @param toUp
     * @param toDown
     * @param toLeft
     * @param toRight
     * @param defaultValueForNewCell
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix<T> extend(final int toUp, final int toDown, final int toLeft, final int toRight, final T defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, "The 'toUp' can't be negative %s", toUp);
        N.checkArgument(toDown >= 0, "The 'toDown' can't be negative %s", toDown);
        N.checkArgument(toLeft >= 0, "The 'toLeft' can't be negative %s", toLeft);
        N.checkArgument(toRight >= 0, "The 'toRight' can't be negative %s", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            final int newRows = toUp + rows + toDown;
            final int newCols = toLeft + cols + toRight;
            final boolean fillDefaultValue = defaultValueForNewCell != null;
            final T[][] b = N.newArray(arrayType, newRows);

            for (int i = 0; i < newRows; i++) {
                b[i] = N.newArray(elementType, newCols);

                if (i >= toUp && i < toUp + rows) {
                    N.copy(a[i - toUp], 0, b[i], toLeft, cols);
                }

                if (fillDefaultValue) {
                    if (i < toUp || i >= toUp + rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        if (toLeft > 0) {
                            N.fill(b[i], 0, toLeft, defaultValueForNewCell);
                        }

                        if (toRight > 0) {
                            N.fill(b[i], cols + toLeft, newCols, defaultValueForNewCell);
                        }
                    }
                }
            }

            return new Matrix<>(b);
        }
    }

    /**
     * Reverse H.
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverse V.
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            T tmp = null;
            for (int l = 0, h = rows - 1; l < h; ) {
                tmp = a[l][j];
                a[l++][j] = a[h][j];
                a[h--][j] = tmp;
            }
        }
    }

    /**
     * @return
     * @see IntMatrix#flipH()
     */
    public Matrix<T> flipH() {
        final Matrix<T> res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * @return
     * @see IntMatrix#flipV()
     */
    public Matrix<T> flipV() {
        final Matrix<T> res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * @return
     */
    @Override
    public Matrix<T> rotate90() {
        final T[][] c = N.newArray(arrayType, cols);

        for (int i = 0; i < cols; i++) {
            c[i] = N.newArray(elementType, rows);
        }

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    c[i][j] = a[rows - j - 1][i];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    c[i][j] = a[rows - j - 1][i];
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * @return
     */
    @Override
    public Matrix<T> rotate180() {
        final T[][] c = N.newArray(arrayType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new Matrix<>(c);
    }

    /**
     * @return
     */
    @Override
    public Matrix<T> rotate270() {
        final T[][] c = N.newArray(arrayType, cols);

        for (int i = 0; i < cols; i++) {
            c[i] = N.newArray(elementType, rows);
        }

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    c[i][j] = a[j][cols - i - 1];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    c[i][j] = a[j][cols - i - 1];
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * @return
     */
    @Override
    public Matrix<T> transpose() {
        final T[][] c = N.newArray(arrayType, cols);

        for (int i = 0; i < cols; i++) {
            c[i] = N.newArray(elementType, rows);
        }

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    c[i][j] = a[j][i];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    c[i][j] = a[j][i];
                }
            }
        }
        return new Matrix<>(c);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public Matrix<T> reshape(final int newRows, final int newCols) {
        final T[][] c = N.newArray(arrayType, newRows);

        for (int i = 0; i < newRows; i++) {
            c[i] = N.newArray(elementType, newCols);
        }

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new Matrix<>(c);
        }

        if (a.length == 1) {
            final T[] a0 = a[0];

            for (int i = 0, len = (int) N.min(newRows, count % newCols == 0 ? count / newCols : count / newCols + 1); i < len; i++) {
                N.copy(a0, i * newCols, c[i], 0, (int) N.min(newCols, count - i * newCols));
            }
        } else {
            long cnt = 0;

            for (int i = 0, len = (int) N.min(newRows, count % newCols == 0 ? count / newCols : count / newCols + 1); i < len; i++) {
                for (int j = 0, col = (int) N.min(newCols, count - i * newCols); j < col; j++, cnt++) {
                    c[i][j] = a[(int) (cnt / cols)][(int) (cnt % cols)];
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * Repeat elements {@code rowRepeats} times in row direction and {@code colRepeats} times in column direction.
     *
     * @param rowRepeats
     * @param colRepeats
     * @return a new matrix
     * @throws IllegalArgumentException
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public Matrix<T> repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final T[][] c = N.newArray(arrayType, rows * rowRepeats);

        for (int i = 0, len = c.length; i < len; i++) {
            c[i] = N.newArray(elementType, cols * colRepeats);
        }

        for (int i = 0; i < rows; i++) {
            final T[] aa = a[i];
            final T[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                // N.copy(Array.repeat(a[i][j], colRepeats), 0, fr, j * colRepeats, colRepeats);
                N.fill(fr, j * colRepeats, j * colRepeats + colRepeats, aa);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new Matrix<>(c);
    }

    /**
     * Repeat this matrix {@code rowRepeats} times in row direction and {@code colRepeats} times in column direction.
     *
     * @param rowRepeats
     * @param colRepeats
     * @return a new matrix
     * @throws IllegalArgumentException
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public Matrix<T> repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final T[][] c = N.newArray(arrayType, rows * rowRepeats);

        for (int i = 0, len = c.length; i < len; i++) {
            c[i] = N.newArray(elementType, cols * colRepeats);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colRepeats; j++) {
                N.copy(a[i], 0, c[i], j * cols, cols);
            }
        }

        for (int i = 1; i < rowRepeats; i++) {
            for (int j = 0; j < rows; j++) {
                N.copy(c[j], 0, c[i * rows + j], 0, c[j].length);
            }
        }

        return new Matrix<>(c);
    }

    /**
     * @return
     */
    @Override
    public List<T> flatten() {
        final T[] c = N.newArray(elementType, rows * cols);

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return N.asList(c);
    }

    /**
     * @param <E>
     * @param op
     * @throws E the e
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super T[], E> op) throws E {
        ff.flatOp(a, op);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @see IntMatrix#vstack(IntMatrix)
     */
    public Matrix<T> vstack(final Matrix<? extends T> b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final T[][] c = N.newArray(arrayType, rows + b.rows);
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return Matrix.of(c);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @see IntMatrix#hstack(IntMatrix)
     */
    public Matrix<T> hstack(final Matrix<? extends T> b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final T[][] c = N.newArray(arrayType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = N.copyOf(a[i], cols + b.cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return Matrix.of(c);
    }

    /**
     * @param <B>
     * @param <E>
     * @param matrixB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public <B, E extends Exception> Matrix<T> zipWith(final Matrix<B> matrixB, final Throwables.BiFunction<? super T, ? super B, T, E> zipFunction) throws E {
        return zipWith(matrixB, zipFunction, elementType);
    }

    /**
     * @param <B>
     * @param <R>
     * @param <E>
     * @param matrixB
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E                        the e
     */
    public <B, R, E extends Exception> Matrix<R> zipWith(final Matrix<B> matrixB, final Throwables.BiFunction<? super T, ? super B, R, E> zipFunction,
            final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgument(Matrixes.isSameShape(this, matrixB), "Can't zip two or more matrices which don't have same shape");

        final B[][] b = matrixB.a;
        final R[][] result = Matrixes.newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * @param <B>
     * @param <C>
     * @param <E>
     * @param matrixB
     * @param matrixC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public <B, C, E extends Exception> Matrix<T> zipWith(final Matrix<B> matrixB, final Matrix<C> matrixC,
            final Throwables.TriFunction<? super T, ? super B, ? super C, T, E> zipFunction) throws E {
        return zipWith(matrixB, matrixC, zipFunction, elementType);
    }

    /**
     * @param <B>
     * @param <C>
     * @param <R>
     * @param <E>
     * @param matrixB
     * @param matrixC
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E                        the e
     */
    public <B, C, R, E extends Exception> Matrix<R> zipWith(final Matrix<B> matrixB, final Matrix<C> matrixC,
            final Throwables.TriFunction<? super T, ? super B, ? super C, R, E> zipFunction, final Class<R> targetElementType)
            throws IllegalArgumentException, E {
        N.checkArgument(Matrixes.isSameShape(this, matrixB, matrixC), "Can't zip two or more matrices which don't have same shape");

        final B[][] b = matrixB.a;
        final C[][] c = matrixC.a;
        final R[][] result = Matrixes.newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Stream LU 2 RD.
     *
     * @return a stream composed by elements on the diagonal line from left up to right down.
     */
    @Override
    public Stream<T> streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<T>() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public T next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return a[cursor][cursor++];
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Stream RU 2 LD.
     *
     * @return a stream composed by elements on the diagonal line from right up to left down.
     */
    @Override
    public Stream<T> streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<T>() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public T next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return a[cursor][rows - ++cursor];
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * @return a stream based on the order of row.
     */
    @Override
    public Stream<T> streamH() {
        return streamH(0, rows);
    }

    /**
     * @param rowIndex
     * @return
     */
    @Override
    public Stream<T> streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a stream based on the order of row.
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Stream<T> streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<T>() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public T next() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final T result = a[i][j++];

                if (j >= cols) {
                    i++;
                    j = 0;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toRowIndex - i) * cols - j) {
                    i = toRowIndex;
                    j = 0;
                } else {
                    i += (n + j) / cols;
                    j += (n + j) % cols;
                }
            }

            @Override
            public long count() {
                return (long) (toRowIndex - i) * cols - j;
            }

            @Override
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) a[i][j++];

                    if (j >= cols) {
                        i++;
                        j = 0;
                    }
                }

                return c;
            }
        });
    }

    /**
     * @return a stream based on the order of column.
     */
    @Override
    @Beta
    public Stream<T> streamV() {
        return streamV(0, cols);
    }

    /**
     * @param columnIndex
     * @return
     */
    @Override
    public Stream<T> streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return a stream based on the order of column.
     * @throws IndexOutOfBoundsException
     */
    @Beta
    @Override
    public Stream<T> streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<T>() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public T next() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final T result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * Matrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (n + i) % Matrix.this.rows;
                    j += (n + i) / Matrix.this.rows;
                }
            }

            @Override
            public long count() {
                return (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) a[i++][j];

                    if (i >= rows) {
                        i = 0;
                        j++;
                    }
                }

                return c;
            }
        });
    }

    /**
     * @return a row stream based on the order of row.
     */
    @Override
    public Stream<Stream<T>> streamR() {
        return streamR(0, rows);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a row stream based on the order of row.
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Stream<Stream<T>> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<Stream<T>>() {
            private final int toIndex = toRowIndex;
            private int cursor = fromRowIndex;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public Stream<T> next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return Stream.of(a[cursor++]);
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * @return a column stream based on the order of column.
     */
    @Override
    @Beta
    public Stream<Stream<T>> streamC() {
        return streamC(0, cols);
    }

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return a column stream based on the order of column.
     * @throws IndexOutOfBoundsException
     */
    @Override
    @Beta
    public Stream<Stream<T>> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<Stream<T>>() {
            private final int toIndex = toColumnIndex;
            private volatile int cursor = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public Stream<T> next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return Stream.of(new ObjIteratorEx<T>() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public T next() {
                        if (cursor2 >= toIndex2) {
                            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                        }

                        return a[cursor2++][columnIndex];
                    }

                    @Override
                    public void advance(final long n) throws IllegalArgumentException {
                        N.checkArgNotNegative(n, "n");

                        cursor2 = n < toIndex2 - cursor2 ? cursor2 + (int) n : toIndex2;
                    }

                    @Override
                    public long count() {
                        return toIndex2 - cursor2; // NOSONAR
                    }
                });
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * @param a
     * @return
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final T[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * @param <E>
     * @param action
     * @throws E the e
     */
    public <E extends Exception> void forEach(final Throwables.Consumer<? super T, E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param action
     * @throws IndexOutOfBoundsException
     * @throws E                         the e
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.Consumer<? super T, E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final T[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
    }

    /**
     * @param columnNames
     * @return
     * @throws IllegalArgumentException
     */
    @Beta
    public DataSet toDataSetH(final Collection<String> columnNames) throws IllegalArgumentException {
        N.checkArgument(columnNames.size() == cols, "The size({}) of specified columnNames and column count({}) of this Matrix are not equals",
                columnNames.size(), cols);

        final List<String> newColumnNameList = new ArrayList<>(columnNames);
        final List<List<Object>> newColumnList = new ArrayList<>(newColumnNameList.size());

        for (int j = 0; j < cols; j++) {
            final List<Object> column = new ArrayList<>(rows);

            for (int i = 0; i < rows; i++) {
                column.add(a[i][j]);
            }

            newColumnList.add(column);
        }

        return new RowDataSet(newColumnNameList, newColumnList);
    }

    /**
     * @param columnNames
     * @return
     * @throws IllegalArgumentException
     */
    @Beta
    public DataSet toDataSetV(final Collection<String> columnNames) throws IllegalArgumentException {
        N.checkArgument(columnNames.size() == rows, "The size({}) of specified columnNames and row count({}) of this Matrix are not equals", columnNames.size(),
                rows);

        final List<String> newColumnNameList = new ArrayList<>(columnNames);
        final List<List<Object>> newColumnList = new ArrayList<>(newColumnNameList.size());

        for (int i = 0; i < rows; i++) {
            newColumnList.add(new ArrayList<>(Array.asList(a[i])));
        }

        return new RowDataSet(newColumnNameList, newColumnList);
    }

    /**
     * Println.
     */
    @Override
    public void println() {
        Arrays.println(a);
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * @param obj
     * @return {@code true}, if successful
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Matrix) {
            final Matrix<T> another = (Matrix<T>) obj;

            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
