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
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

// TODO: Auto-generated Javadoc

/**
 *
 */
public final class BooleanMatrix extends AbstractMatrix<boolean[], BooleanList, Stream<Boolean>, Stream<Stream<Boolean>>, BooleanMatrix> {

    static final BooleanMatrix EMPTY_BOOLEAN_MATRIX = new BooleanMatrix(new boolean[0][0]);

    /**
     * @param a
     */
    public BooleanMatrix(final boolean[][] a) {
        super(a == null ? new boolean[0][0] : a);
    }

    /**
     * @return
     */
    public static BooleanMatrix empty() {
        return EMPTY_BOOLEAN_MATRIX;
    }

    /**
     * @param a
     * @return
     */
    public static BooleanMatrix of(final boolean[]... a) {
        return N.isEmpty(a) ? EMPTY_BOOLEAN_MATRIX : new BooleanMatrix(a);
    }

    /**
     * @param len
     * @return
     */
    public static BooleanMatrix random(final int len) {
        return new BooleanMatrix(new boolean[][] { BooleanList.random(len).array() });
    }

    /**
     * @param val
     * @param len
     * @return
     */
    public static BooleanMatrix repeat(final boolean val, final int len) {
        return new BooleanMatrix(new boolean[][] { Array.repeat(val, len) });
    }

    /**
     * Diagonal LU 2 RD.
     *
     * @param leftUp2RightDownDiagonal
     * @return
     */
    public static BooleanMatrix diagonalLU2RD(final boolean[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Diagonal RU 2 LD.
     *
     * @param rightUp2LeftDownDiagonal
     * @return
     */
    public static BooleanMatrix diagonalRU2LD(final boolean[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * @param leftUp2RightDownDiagonal
     * @param rightUp2LeftDownDiagonal
     * @return
     * @throws IllegalArgumentException
     */
    public static BooleanMatrix diagonal(final boolean[] leftUp2RightDownDiagonal, final boolean[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_BOOLEAN_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final boolean[][] c = new boolean[len][len];

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

        return new BooleanMatrix(c);
    }

    /**
     * @param x
     * @return
     */
    public static BooleanMatrix unbox(final Matrix<Boolean> x) {
        return BooleanMatrix.of(Array.unbox(x.a));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return boolean.class;
    }

    /**
     * @param i
     * @param j
     * @return {@code true}, if successful
     */
    public boolean get(final int i, final int j) { // NOSONAR
        return a[i][j];
    }

    /**
     * @param point
     * @return {@code true}, if successful
     */
    public boolean get(final Point point) { // NOSONAR
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * @param i
     * @param j
     * @param val
     */
    public void set(final int i, final int j, final boolean val) {
        a[i][j] = val;
    }

    /**
     * @param point
     * @param val
     */
    public void set(final Point point, final boolean val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalBoolean upOf(final int i, final int j) {
        return i == 0 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i - 1][j]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalBoolean downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i + 1][j]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalBoolean leftOf(final int i, final int j) {
        return j == 0 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i][j - 1]);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public OptionalBoolean rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i][j + 1]);
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
    public boolean[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * @param columnIndex
     * @return
     * @throws IllegalArgumentException
     */
    public boolean[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final boolean[] c = new boolean[rows];

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
    public void setRow(final int rowIndex, final boolean[] row) throws IllegalArgumentException {
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
    public void setColumn(final int columnIndex, final boolean[] column) throws IllegalArgumentException {
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
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.BooleanUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsBoolean(a[rowIndex][i]);
        }
    }

    /**
     * @param <E>
     * @param columnIndex
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.BooleanUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsBoolean(a[i][columnIndex]);
        }
    }

    /**
     * Gets the lu2rd.
     *
     * @return
     */
    public boolean[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final boolean[] res = new boolean[rows];

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
    public void setLU2RD(final boolean[] diagonal) throws IllegalArgumentException {
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
    public <E extends Exception> void updateLU2RD(final Throwables.BooleanUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsBoolean(a[i][i]);
        }
    }

    /**
     * Gets the ru2ld.
     *
     * @return
     */
    public boolean[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final boolean[] res = new boolean[rows];

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
    public void setRU2LD(final boolean[] diagonal) throws IllegalArgumentException {
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
    public <E extends Exception> void updateRU2LD(final Throwables.BooleanUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsBoolean(a[i][cols - i - 1]);
        }
    }

    /**
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateAll(final Throwables.BooleanUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsBoolean(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Update all elements based on points.
     *
     * @param <E>
     * @param func
     * @throws E the e
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Boolean, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * @param <E>
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public <E extends Exception> void replaceIf(final Throwables.BooleanPredicate<E> predicate, final boolean newValue) throws E {
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
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final boolean newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * @param <E>
     * @param func
     * @return
     * @throws E the e
     */
    public <E extends Exception> BooleanMatrix map(final Throwables.BooleanUnaryOperator<E> func) throws E {
        final boolean[][] result = new boolean[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsBoolean(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
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
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.BooleanFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * @param val
     */
    public void fill(final boolean val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * @param b
     */
    public void fill(final boolean[][] b) {
        fill(0, 0, b);
    }

    /**
     * @param fromRowIndex
     * @param fromColumnIndex
     * @param b
     * @throws IndexOutOfBoundsException
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final boolean[][] b) throws IndexOutOfBoundsException {
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
    public BooleanMatrix copy() {
        final boolean[][] c = new boolean[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new BooleanMatrix(c);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public BooleanMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final boolean[][] c = new boolean[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new BooleanMatrix(c);
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
    public BooleanMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex)
            throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final boolean[][] c = new boolean[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new BooleanMatrix(c);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    public BooleanMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, false);
    }

    /**
     * @param newRows
     * @param newCols
     * @param defaultValueForNewCell
     * @return
     * @throws IllegalArgumentException
     */
    public BooleanMatrix extend(final int newRows, final int newCols, final boolean defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            // NOSONAR
            final boolean[][] b = new boolean[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new boolean[newCols];

                if (defaultValueForNewCell) {
                    if (i >= rows) {
                        N.fill(b[i], true);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, true);
                    }
                }
            }

            return new BooleanMatrix(b);
        }
    }

    /**
     * @param toUp
     * @param toDown
     * @param toLeft
     * @param toRight
     * @return
     */
    public BooleanMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, false);
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
    public BooleanMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final boolean defaultValueForNewCell)
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
            // NOSONAR
            final boolean[][] b = new boolean[newRows][newCols];

            for (int i = 0; i < newRows; i++) {
                if (i >= toUp && i < toUp + rows) {
                    N.copy(a[i - toUp], 0, b[i], toLeft, cols);
                }

                if (defaultValueForNewCell) {
                    if (i < toUp || i >= toUp + rows) {
                        N.fill(b[i], true);
                    } else if (cols < newCols) {
                        if (toLeft > 0) {
                            N.fill(b[i], 0, toLeft, true);
                        }

                        if (toRight > 0) {
                            N.fill(b[i], cols + toLeft, newCols, true);
                        }
                    }
                }
            }

            return new BooleanMatrix(b);
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
            boolean tmp = false;
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
    public BooleanMatrix flipH() {
        final BooleanMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * @return
     * @see IntMatrix#flipV()
     */
    public BooleanMatrix flipV() {
        final BooleanMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * @return
     */
    @Override
    public BooleanMatrix rotate90() {
        final boolean[][] c = new boolean[cols][rows];

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

        return new BooleanMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public BooleanMatrix rotate180() {
        final boolean[][] c = new boolean[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new BooleanMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public BooleanMatrix rotate270() {
        final boolean[][] c = new boolean[cols][rows];

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

        return new BooleanMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public BooleanMatrix transpose() {
        final boolean[][] c = new boolean[cols][rows];

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

        return new BooleanMatrix(c);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public BooleanMatrix reshape(final int newRows, final int newCols) {
        final boolean[][] c = new boolean[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new BooleanMatrix(c);
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

        return new BooleanMatrix(c);
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
    public BooleanMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final boolean[][] c = new boolean[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final boolean[] aa = a[i];
            final boolean[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new BooleanMatrix(c);
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
    public BooleanMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final boolean[][] c = new boolean[rows * rowRepeats][cols * colRepeats];

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

        return new BooleanMatrix(c);
    }

    /**
     * @return
     */
    @Override
    public BooleanList flatten() {
        final boolean[] c = new boolean[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return BooleanList.of(c);
    }

    /**
     * @param <E>
     * @param op
     * @throws E the e
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super boolean[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @see IntMatrix#vstack(IntMatrix)
     */
    public BooleanMatrix vstack(final BooleanMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final boolean[][] c = new boolean[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return BooleanMatrix.of(c);
    }

    /**
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @see IntMatrix#hstack(IntMatrix)
     */
    public BooleanMatrix hstack(final BooleanMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final boolean[][] c = new boolean[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return BooleanMatrix.of(c);
    }

    /**
     * @return
     */
    public Matrix<Boolean> boxed() {
        final Boolean[][] c = new Boolean[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final boolean[] aa = a[i];
                final Boolean[] cc = c[i];

                for (int j = 0; j < cols; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows; i++) {
                    c[i][j] = a[i][j]; // NOSONAR
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * @param <E>
     * @param matrixB
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E                        the e
     */
    public <E extends Exception> BooleanMatrix zipWith(final BooleanMatrix matrixB, final Throwables.BooleanBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final boolean[][] b = matrixB.a;
        final boolean[][] result = new boolean[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsBoolean(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * @param <E>
     * @param matrixB
     * @param matrixC
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E                        the e
     */
    public <E extends Exception> BooleanMatrix zipWith(final BooleanMatrix matrixB, final BooleanMatrix matrixC,
            final Throwables.BooleanTernaryOperator<E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final boolean[][] b = matrixB.a;
        final boolean[][] c = matrixC.a;
        final boolean[][] result = new boolean[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsBoolean(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Stream LU 2 RD.
     *
     * @return a stream composed by elements on the diagonal line from left up to right down.
     */
    @Override
    public Stream<Boolean> streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public Boolean next() {
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
    public Stream<Boolean> streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public Boolean next() {
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
    public Stream<Boolean> streamH() {
        return streamH(0, rows);
    }

    /**
     * @param rowIndex
     * @return
     */
    @Override
    public Stream<Boolean> streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a stream based on the order of row.
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Stream<Boolean> streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public Boolean next() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final boolean result = a[i][j++];

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
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) (Boolean) a[i][j++];

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
    public Stream<Boolean> streamV() {
        return streamV(0, cols);
    }

    /**
     * @param columnIndex
     * @return
     */
    @Override
    public Stream<Boolean> streamV(final int columnIndex) {
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
    public Stream<Boolean> streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public Boolean next() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final boolean result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * BooleanMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % BooleanMatrix.this.rows);
                    j += (int) ((n + i) / BooleanMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) (Boolean) a[i++][j];

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
    public Stream<Stream<Boolean>> streamR() {
        return streamR(0, rows);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a row stream based on the order of row.
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Stream<Stream<Boolean>> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public Stream<Boolean> next() {
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
    public Stream<Stream<Boolean>> streamC() {
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
    public Stream<Stream<Boolean>> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public Stream<Boolean> next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return Stream.of(new ObjIteratorEx<>() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public Boolean next() {
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
    protected int length(@SuppressWarnings("hiding") final boolean[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * @param <E>
     * @param action
     * @throws E the e
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
            final Throwables.BooleanConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final boolean[] aa = a[i];

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

        if (obj instanceof final BooleanMatrix another) {
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
