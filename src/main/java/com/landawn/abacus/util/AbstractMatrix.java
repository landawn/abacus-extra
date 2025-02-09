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

import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * <li>
 * {@code R} = Row, {@code C} = Column, {@code H} = Horizontal, {@code V} = Vertical.
 * </li>
 *
 * @param <A>
 * @param <PL>
 * @param <ES> element stream
 * @param <RS> row/column stream.
 * @param <X>
 */
public abstract sealed class AbstractMatrix<A, PL, ES, RS, X extends AbstractMatrix<A, PL, ES, RS, X>>
        permits BooleanMatrix, CharMatrix, ByteMatrix, ShortMatrix, DoubleMatrix, FloatMatrix, IntMatrix, LongMatrix, Matrix {

    static final char CHAR_0 = (char) 0;

    static final byte BYTE_0 = (byte) 0;

    static final byte BYTE_1 = (byte) 1;

    static final short SHORT_0 = (short) 0;

    /**
     * Row length.
     */
    public final int rows;

    /**
     * Column length.
     */
    public final int cols;

    public final long count;

    final A[] a;

    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    protected AbstractMatrix(final A[] a) {
        this.a = a;
        rows = a.length;
        cols = a.length == 0 ? 0 : length(a[0]);

        if (a.length > 1) {
            for (int i = 1, len = a.length; i < len; i++) {
                if (length(a[i]) != cols) {
                    throw new IllegalArgumentException("The length of sub arrays must be same");
                }
            }
        }

        count = (long) cols * rows;
    }

    @SuppressWarnings("rawtypes")
    public abstract Class componentType();

    /**
     * @return
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public A[] array() {
        return a;
    }

    /**
     * Println.
     */
    public abstract void println();

    /**
     * Checks if is empty.
     *
     * @return {@code true}, if is empty
     */
    public boolean isEmpty() {
        return count == 0;
    }

    // Replaced by stream and stream2.
    //    public abstract PL row(int i);
    //
    //    public abstract PL column(int j);

    /**
     * @return a new Matrix
     */
    public abstract X copy();

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return a new Matrix
     */
    public abstract X copy(int fromRowIndex, int toRowIndex);

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return a new Matrix
     */
    public abstract X copy(int fromRowIndex, int toRowIndex, int fromColumnIndex, int toColumnIndex);

    /**
     * Rotate this matrix clockwise 90.
     *
     * @return a new Matrix
     */
    public abstract X rotate90();

    /**
     * Rotate this matrix clockwise 180.
     *
     * @return a new Matrix
     */
    public abstract X rotate180();

    /**
     * Rotate this matrix clockwise 270.
     *
     * @return a new Matrix
     */
    public abstract X rotate270();

    /**
     * @return
     */
    public abstract X transpose();

    /**
     * @param newCols
     * @return
     */
    public X reshape(final int newCols) {
        return reshape((int) (count % newCols == 0 ? count / newCols : count / newCols + 1), newCols);
    }

    /**
     * @param newRows
     * @param newCols
     * @return
     */
    public abstract X reshape(int newRows, int newCols);

    /**
     * Checks if is same shape.
     *
     * @param x
     * @return {@code true}, if is same shape
     */
    public boolean isSameShape(final X x) {
        return rows == x.rows && cols == x.cols;
    }

    /**
     * Repeat elements {@code rowRepeats} times in row direction and {@code colRepeats} times in column direction.
     *
     * @param rowRepeats
     * @param colRepeats
     * @return a new matrix
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repelem.html">https://www.mathworks.com/help/matlab/ref/repelem.html</a>
     */
    public abstract X repelem(int rowRepeats, int colRepeats);

    /**
     * Repeat this matrix {@code rowRepeats} times in row direction and {@code colRepeats} times in column direction.
     *
     * @param rowRepeats
     * @param colRepeats
     * @return a new matrix
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repmat.html">https://www.mathworks.com/help/matlab/ref/repmat.html</a>
     */
    public abstract X repmat(int rowRepeats, int colRepeats);

    /**
     * @return
     */
    public abstract PL flatten();

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * matrix.flatOp(a -> N.sort(a));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param op
     * @throws E the e
     */
    public abstract <E extends Exception> void flatOp(Throwables.Consumer<? super A, E> op) throws E;

    /**
     * <pre>
     * <code>
     * for (int i = 0; i < rows; i++) {
     *      for (int j = 0; j < cols; j++) {
     *          action.accept(i, j);
     *      }
     *  }
     * </code>
     * </pre>
     *
     * @param <E>
     * @param action
     * @throws E the e
     */
    public <E extends Exception> void forEach(final Throwables.IntBiConsumer<E> action) throws E {
        if (Matrixes.isParallelable(this)) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> cmd = action::accept;
            Matrixes.run(rows, cols, cmd, true);
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param action
     * @throws IndexOutOfBoundsException
     * @throws E
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> cmd = action::accept;
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.BiIntObjConsumer<X, E> action) throws E {
        final X x = (X) this;
        if (Matrixes.isParallelable(this)) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(i, j, x);
            Matrixes.run(rows, cols, cmd, true);
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    action.accept(i, j, x);
                }
            }
        }
    }

    /**
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param action
     * @throws IndexOutOfBoundsException
     * @throws E
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.BiIntObjConsumer<X, E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final X x = (X) this;

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(i, j, x);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j, x);
                }
            }
        }
    }

    /**
     * Points LU 2 RD.
     *
     * @return
     */
    public Stream<Point> pointsLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rows).mapToObj(i -> Point.of(i, i));
    }

    /**
     * Points RU 2 LD.
     *
     * @return
     */
    public Stream<Point> pointsRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rows).mapToObj(i -> Point.of(i, cols - i - 1));
    }

    /**
     * @return
     */
    public Stream<Point> pointsH() {
        return pointsH(0, rows);
    }

    /**
     * @param rowIndex
     * @return
     */
    public Stream<Point> pointsH(final int rowIndex) {
        return pointsH(rowIndex, rowIndex + 1);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        return IntStream.range(fromRowIndex, toRowIndex)
                .flatMapToObj(rowIndex -> IntStream.range(0, cols).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * @return
     */
    public Stream<Point> pointsV() {
        return pointsV(0, cols);
    }

    /**
     * @param columnIndex
     * @return
     */
    public Stream<Point> pointsV(final int columnIndex) {
        return pointsV(columnIndex, columnIndex + 1);
    }

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .flatMapToObj(columnIndex -> IntStream.range(0, rows).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * @return
     */
    public Stream<Stream<Point>> pointsR() {
        return pointsR(0, rows);
    }

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        return IntStream.range(fromRowIndex, toRowIndex)
                .mapToObj(rowIndex -> IntStream.range(0, cols).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * @return
     */
    public Stream<Stream<Point>> pointsC() {
        return pointsR(0, cols);
    }

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .mapToObj(columnIndex -> IntStream.range(0, rows).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Stream LU 2 RD.
     *
     * @return
     */
    public abstract ES streamLU2RD();

    /**
     * Stream RU 2 LD.
     *
     * @return
     */
    public abstract ES streamRU2LD();

    /**
     * @return
     */
    public abstract ES streamH();

    /**
     * @param rowIndex
     * @return
     */
    public abstract ES streamH(final int rowIndex);

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     */
    public abstract ES streamH(final int fromRowIndex, final int toRowIndex);

    /**
     * @return
     */
    public abstract ES streamV();

    /**
     * @param columnIndex
     * @return
     */
    public abstract ES streamV(final int columnIndex);

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return
     */
    public abstract ES streamV(final int fromColumnIndex, final int toColumnIndex);

    /**
     * @return
     */
    public abstract RS streamR();

    /**
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     */
    public abstract RS streamR(final int fromRowIndex, final int toRowIndex);

    /**
     * @return
     */
    public abstract RS streamC();

    /**
     * @param fromColumnIndex
     * @param toColumnIndex
     * @return
     */
    public abstract RS streamC(final int fromColumnIndex, final int toColumnIndex);

    /**
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super X, E> action) throws E {
        action.accept((X) this);
    }

    /**
     * @param <R>
     * @param <E>
     * @param action
     * @return
     * @throws E
     */
    public <R, E extends Exception> R apply(final Throwables.Function<? super X, R, E> action) throws E {
        return action.apply((X) this);
    }

    protected abstract int length(@SuppressWarnings("hiding") A a);

    protected void checkSameShape(final X x) {
        N.checkArgument(this.isSameShape(x), "Must be same shape");
    }

    protected void checkIfRowAndColumnSizeAreSame() {
        N.checkState(rows == cols, "'rows' and 'cols' must be same to get diagonals: rows=%s, cols=%s", rows, cols);
    }

}
