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

import java.util.NoSuchElementException;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalLong;
import com.landawn.abacus.util.stream.LongIteratorEx;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

// TODO: Auto-generated Javadoc

/**
 *
 */
public final class LongMatrix extends AbstractMatrix<long[], LongList, LongStream, Stream<LongStream>, LongMatrix> {

    static final LongMatrix EMPTY_LONG_MATRIX = new LongMatrix(new long[0][0]);

    /**
     * @param a
     */
    public LongMatrix(final long[][] a) {
        super(a == null ? new long[0][0] : a);
    }

    /**
     * @return
     */
    public static LongMatrix empty() {
        return EMPTY_LONG_MATRIX;
    }

    /**
     * @param a
     * @return
     */
    public static LongMatrix of(final long[]... a) {
        return N.isEmpty(a) ? EMPTY_LONG_MATRIX : new LongMatrix(a);
    }

    /**
     * @param a
     * @return
     */
    public static LongMatrix create(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_LONG_MATRIX;
        }

        final long[][] c = new long[a.length][a[0].length];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final long[] cc = c[i];

            for (int j = 0, col = a[0].length; j < col; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new LongMatrix(c);
    }

    /**
     * @param len
     * @return
     */
    public static LongMatrix random(final int len) {
        return new LongMatrix(new long[][] { LongList.random(len).array() });
    }

    /**
     * @param val
     * @param len
     * @return
     */
    public static LongMatrix repeat(final long val, final int len) {
        return new LongMatrix(new long[][] { Array.repeat(val, len) });
    }

    /**
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static LongMatrix range(final long startInclusive, final long endExclusive) {
        return new LongMatrix(new long[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * @param startInclusive
     * @param endExclusive
     * @param by
     * @return
     */
    public static LongMatrix range(final long startInclusive, final long endExclusive, final long by) {
        return new LongMatrix(new long[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * @param startInclusive
     * @param endInclusive
     * @return
     */
    public static LongMatrix rangeClosed(final long startInclusive, final long endInclusive) {
        return new LongMatrix(new long[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * @param startInclusive
     * @param endInclusive
     * @param by
     * @return
     */
    public static LongMatrix rangeClosed(final long startInclusive, final long endInclusive, final long by) {
        return new LongMatrix(new long[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Diagonal LU 2 RD.
     *
     * @param leftUp2RightDownDiagonal
     * @return
     */
    public static LongMatrix diagonalLU2RD(final long[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Diagonal RU 2 LD.
     *
     * @param rightUp2LeftDownDiagonal
     * @return
     */
    public static LongMatrix diagonalRU2LD(final long[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * @param leftUp2RightDownDiagonal
     * @param rightUp2LeftDownDiagonal
     * @return
     * @throws IllegalArgumentException
     */
    public static LongMatrix diagonal(final long[] leftUp2RightDownDiagonal, final long[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_LONG_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final long[][] c = new long[len][len];

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

        return new LongMatrix(c);
    }

    /**
     * @param x
     * @return
     */
    public static LongMatrix unbox(final Matrix<Long> x) {
        return LongMatrix.of(Array.unbox(x.a));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return long.class;
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public long get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * @param point
     * @return
     */
    public long get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * @param i
     * @param j
     * @param val
     */
    public void set(final int i, final int j, final long val) {
        a[i][j] = val;
    }

    /**
     * @param point
     * @param val
     */
    public void set(final Point point, final long val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalLong upOf(final int i, final int j) {
        return i == 0 ? OptionalLong.empty() : OptionalLong.of(a[i - 1][j]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalLong downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalLong.empty() : OptionalLong.of(a[i + 1][j]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalLong leftOf(final int i, final int j) {
        return j == 0 ? OptionalLong.empty() : OptionalLong.of(a[i][j - 1]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalLong rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalLong.empty() : OptionalLong.of(a[i][j + 1]);
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
    public long[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * @param columnIndex
     * @return
     * @throws IllegalArgumentException
     */
    public long[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final long[] c = new long[rows];

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
    public void setRow(final int rowIndex, final long[] row) throws IllegalArgumentException {
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
    public void setColumn(final int columnIndex, final long[] column) throws IllegalArgumentException {
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
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.LongUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsLong(a[rowIndex][i]);
        }
    }

    /**
     * @param <E>
     * @param columnIndex
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.LongUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsLong(a[i][columnIndex]);
        }
    }

    /**
     * Gets the lu2rd.
     *
     * @return
     */
    public long[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final long[] res = new long[rows];

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
    public void setLU2RD(final long[] diagonal) throws IllegalArgumentException {
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
    public <E extends Exception> void updateLU2RD(final Throwables.LongUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsLong(a[i][i]);
        }
    }

    /**
     * Gets the ru2ld.
     *
     * @return
     */
    public long[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final long[] res = new long[rows];

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
    public void setRU2LD(final long[] diagonal) throws IllegalArgumentException {
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
    public <E extends Exception> void updateRU2LD(final Throwables.LongUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsLong(a[i][cols - i - 1]);
        }
    }

    /**
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateAll(final Throwables.LongUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsLong(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Update all elements based on points.
     *
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Long, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * @param <E>
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public <E extends Exception> void replaceIf(final Throwables.LongPredicate<E> predicate, final long newValue) throws E {
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
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final long newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> LongMatrix map(final Throwables.LongUnaryOperator<E> func) throws E {
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsLong(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> IntMatrix mapToInt(final Throwables.LongToIntFunction<E> func) throws E {
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsInt(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> DoubleMatrix mapToDouble(final Throwables.LongToDoubleFunction<E> func) throws E {
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsDouble(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param func
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.LongFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * @param val
     */
    public void fill(final long val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * @param b
     */
    public void fill(final long[][] b) {
        fill(0, 0, b);
    }

    /**
     * @param fromRowIndex
     * @param fromColumnIndex
     * @param b
     * @throws IndexOutOfBoundsException
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final long[][] b) throws IndexOutOfBoundsException {
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
    public LongMatrix copy() {
        final long[][] c = new long[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new LongMatrix(c);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public LongMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final long[][] c = new long[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new LongMatrix(c);
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
    public LongMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final long[][] c = new long[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new LongMatrix(c);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    public LongMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
    }

    /**
     * @param newRows
     * @param newCols
     * @param defaultValueForNewCell
     * @return
     * @throws IllegalArgumentException
     */
    public LongMatrix extend(final int newRows, final int newCols, final long defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final long[][] b = new long[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new long[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new LongMatrix(b);
        }
    }

    /**
     * @param toUp
     * @param toDown
     * @param toLeft
     * @param toRight
     * @return
     */
    public LongMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
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
    public LongMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final long defaultValueForNewCell)
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
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final long[][] b = new long[newRows][newCols];

            for (int i = 0; i < newRows; i++) {
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

            return new LongMatrix(b);
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
            long tmp = 0;
            for (int l = 0, h = rows - 1; l < h;) {
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
    public LongMatrix flipH() {
        final LongMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * @return
     * @see IntMatrix#flipV()
     */
    public LongMatrix flipV() {
        final LongMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * @return
     */
    @Override
    public LongMatrix rotate90() {
        final long[][] c = new long[cols][rows];

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

        return new LongMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public LongMatrix rotate180() {
        final long[][] c = new long[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new LongMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public LongMatrix rotate270() {
        final long[][] c = new long[cols][rows];

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

        return new LongMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public LongMatrix transpose() {
        final long[][] c = new long[cols][rows];

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

        return new LongMatrix(c);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public LongMatrix reshape(final int newRows, final int newCols) {
        final long[][] c = new long[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new LongMatrix(c);
        }

        final int rowLen = (int) N.min(newRows, count % newCols == 0 ? count / newCols : count / newCols + 1);

        if (a.length == 1) {
            for (int i = 0; i < rowLen; i++) {
                N.copy(a[0], i * newCols, c[i], 0, (int) N.min(newCols, count - (long) i * newCols));
            }
        } else {
            long cnt = 0;

            for (int i = 0; i < rowLen; i++) {
                for (int j = 0, col = (int) N.min(newCols, count - (long) i * newCols); j < col; j++, cnt++) {
                    c[i][j] = a[(int) (cnt / cols)][(int) (cnt % cols)];
                }
            }
        }

        return new LongMatrix(c);
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
    public LongMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final long[][] c = new long[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final long[] aa = a[i];
            final long[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new LongMatrix(c);
    }

    /**
     * Repeat this matrix {@code rowRepeats} times in a row direction and {@code colRepeats} times in column direction.
     *
     * @param rowRepeats
     * @param colRepeats
     * @return a new matrix
     * @throws IllegalArgumentException
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public LongMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final long[][] c = new long[rows * rowRepeats][cols * colRepeats];

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

        return new LongMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public LongList flatten() {
        final long[] c = new long[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return LongList.of(c);
    }

    /**
     * @param <E>
     * @param op
     * @throws E the e
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super long[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @see IntMatrix#vstack(IntMatrix)
     */
    public LongMatrix vstack(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final long[][] c = new long[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return LongMatrix.of(c);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @see IntMatrix#hstack(IntMatrix)
     */
    public LongMatrix hstack(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final long[][] c = new long[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return LongMatrix.of(c);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     */
    public LongMatrix add(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final long[][] ba = b.a;
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     */
    public LongMatrix subtract(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final long[][] ba = b.a;
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     */
    public LongMatrix multiply(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final long[][] ba = b.a;
        final long[][] result = new long[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += a[i][k] * ba[k][j];

        Matrixes.multiply(this, b, cmd);

        return new LongMatrix(result);
    }

    /**
     * @return
     */
    public Matrix<Long> boxed() {
        final Long[][] c = new Long[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final long[] aa = a[i];
                final Long[] cc = c[i];

                for (int j = 0; j < cols; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows; i++) {
                    c[i][j] = a[i][j];
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * To float matrix.
     *
     * @return
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final long[] aa = a[i];
                final float[] cc = c[i];

                for (int j = 0; j < cols; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows; i++) {
                    c[i][j] = a[i][j];
                }
            }
        }

        return new FloatMatrix(c);
    }

    /**
     * To double matrix.
     *
     * @return
     */
    public DoubleMatrix toDoubleMatrix() {
        return DoubleMatrix.create(a);
    }

    /**
     * @param <E>
     * @param matrixB
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E                        the e
     */
    public <E extends Exception> LongMatrix zipWith(final LongMatrix matrixB, final Throwables.LongBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final long[][] b = matrixB.a;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsLong(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * @param <E>
     * @param matrixB
     * @param matrixC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public <E extends Exception> LongMatrix zipWith(final LongMatrix matrixB, final LongMatrix matrixC, final Throwables.LongTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final long[][] b = matrixB.a;
        final long[][] c = matrixC.a;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsLong(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Stream LU 2 RD.
     *
     * @return a stream composed by elements on the diagonal line from left up to right down.
     */
    @Override
    public LongStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public long nextLong() {
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
    public LongStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public long nextLong() {
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
    public LongStream streamH() {
        return streamH(0, rows);
    }

    /**
     * @param rowIndex
     * @return
     */
    @Override
    public LongStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a stream based on the order of row.
     * @throws IndexOutOfBoundsException
     */
    @Override
    public LongStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public long nextLong() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final long result = a[i][j++];

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
                    i += (int) ((n + j) / cols);
                    j += (int) ((n + j) % cols);
                }
            }

            @Override
            public long count() {
                return (long) (toRowIndex - i) * cols - j;
            }

            @Override
            public long[] toArray() {
                final int len = (int) count();
                final long[] c = new long[len];

                for (int k = 0; k < len; k++) {
                    c[k] = a[i][j++];

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
    public LongStream streamV() {
        return streamV(0, cols);
    }

    /**
     * @param columnIndex
     * @return
     */
    @Override
    public LongStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return a stream based on the order of column.
     * @throws IndexOutOfBoundsException
     */
    @Override
    @Beta
    public LongStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public long nextLong() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final long result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * LongMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % LongMatrix.this.rows);
                    j += (int) ((n + i) / LongMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public long[] toArray() {
                final int len = (int) count();
                final long[] c = new long[len];

                for (int k = 0; k < len; k++) {
                    c[k] = a[i++][j];

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
    public Stream<LongStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a row stream based on the order of row.
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Stream<LongStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = toRowIndex;
            private int cursor = fromRowIndex;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public LongStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return LongStream.of(a[cursor++]);
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
    public Stream<LongStream> streamC() {
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
    public Stream<LongStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = toColumnIndex;
            private int cursor = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public LongStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return LongStream.of(new LongIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public long nextLong() {
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
    protected int length(@SuppressWarnings("hiding") final long[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * @param <E>
     * @param action
     * @throws E the e
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
            final Throwables.LongConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final long[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
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

        if (obj instanceof final LongMatrix another) {
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
