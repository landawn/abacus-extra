# Similar Public Methods Report

This report lists similar public methods across Matrix and Tuple classes in `./src/main/java/com/landawn/abacus/util/`

## Part 1: Matrix Classes - Similar Methods

### 1. Factory Methods - empty()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix empty()` |
| ByteMatrix | `public static ByteMatrix empty()` |
| CharMatrix | `public static CharMatrix empty()` |
| DoubleMatrix | `public static DoubleMatrix empty()` |
| FloatMatrix | `public static FloatMatrix empty()` |
| IntMatrix | `public static IntMatrix empty()` |
| LongMatrix | `public static LongMatrix empty()` |
| ShortMatrix | `public static ShortMatrix empty()` |

### 2. Factory Methods - of()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix of(final boolean[]... a)` |
| ByteMatrix | `public static ByteMatrix of(final byte[]... a)` |
| CharMatrix | `public static CharMatrix of(final char[]... a)` |
| DoubleMatrix | `public static DoubleMatrix of(final double[]... a)` |
| FloatMatrix | `public static FloatMatrix of(final float[]... a)` |
| IntMatrix | `public static IntMatrix of(final int[]... a)` |
| LongMatrix | `public static LongMatrix of(final long[]... a)` |
| ShortMatrix | `public static ShortMatrix of(final short[]... a)` |

### 3. Factory Methods - random()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix random(final int len)` |
| ByteMatrix | `public static ByteMatrix random(final int len)` |
| CharMatrix | `public static CharMatrix random(final int len)` |
| DoubleMatrix | `public static DoubleMatrix random(final int len)` |
| FloatMatrix | `public static FloatMatrix random(final int len)` |
| IntMatrix | `public static IntMatrix random(final int len)` |
| LongMatrix | `public static LongMatrix random(final int len)` |
| ShortMatrix | `public static ShortMatrix random(final int len)` |

### 4. Factory Methods - repeat()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix repeat(final boolean val, final int len)` |
| ByteMatrix | `public static ByteMatrix repeat(final byte val, final int len)` |
| CharMatrix | `public static CharMatrix repeat(final char val, final int len)` |
| DoubleMatrix | `public static DoubleMatrix repeat(final double val, final int len)` |
| FloatMatrix | `public static FloatMatrix repeat(final float val, final int len)` |
| IntMatrix | `public static IntMatrix repeat(final int val, final int len)` |
| LongMatrix | `public static LongMatrix repeat(final long val, final int len)` |
| ShortMatrix | `public static ShortMatrix repeat(final short val, final int len)` |

### 5. Diagonal Factory Methods - diagonalLU2RD()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix diagonalLU2RD(final boolean[] leftUp2RightDownDiagonal)` |
| ByteMatrix | `public static ByteMatrix diagonalLU2RD(final byte[] leftUp2RightDownDiagonal)` |
| CharMatrix | `public static CharMatrix diagonalLU2RD(final char[] leftUp2RightDownDiagonal)` |
| DoubleMatrix | `public static DoubleMatrix diagonalLU2RD(final double[] leftUp2RightDownDiagonal)` |
| FloatMatrix | `public static FloatMatrix diagonalLU2RD(final float[] leftUp2RightDownDiagonal)` |
| IntMatrix | `public static IntMatrix diagonalLU2RD(final int[] leftUp2RightDownDiagonal)` |
| LongMatrix | `public static LongMatrix diagonalLU2RD(final long[] leftUp2RightDownDiagonal)` |
| ShortMatrix | `public static ShortMatrix diagonalLU2RD(final short[] leftUp2RightDownDiagonal)` |

### 6. Diagonal Factory Methods - diagonalRU2LD()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix diagonalRU2LD(final boolean[] rightUp2LeftDownDiagonal)` |
| ByteMatrix | `public static ByteMatrix diagonalRU2LD(final byte[] rightUp2LeftDownDiagonal)` |
| CharMatrix | `public static CharMatrix diagonalRU2LD(final char[] rightUp2LeftDownDiagonal)` |
| DoubleMatrix | `public static DoubleMatrix diagonalRU2LD(final double[] rightUp2LeftDownDiagonal)` |
| FloatMatrix | `public static FloatMatrix diagonalRU2LD(final float[] rightUp2LeftDownDiagonal)` |
| IntMatrix | `public static IntMatrix diagonalRU2LD(final int[] rightUp2LeftDownDiagonal)` |
| LongMatrix | `public static LongMatrix diagonalRU2LD(final long[] rightUp2LeftDownDiagonal)` |
| ShortMatrix | `public static ShortMatrix diagonalRU2LD(final short[] rightUp2LeftDownDiagonal)` |

### 7. Diagonal Factory Methods - diagonal()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public static BooleanMatrix diagonal(final boolean[] leftUp2RightDownDiagonal, final boolean[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| ByteMatrix | `public static ByteMatrix diagonal(final byte[] leftUp2RightDownDiagonal, final byte[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| CharMatrix | `public static CharMatrix diagonal(final char[] leftUp2RightDownDiagonal, final char[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| DoubleMatrix | `public static DoubleMatrix diagonal(final double[] leftUp2RightDownDiagonal, final double[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| FloatMatrix | `public static FloatMatrix diagonal(final float[] leftUp2RightDownDiagonal, final float[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| IntMatrix | `public static IntMatrix diagonal(final int[] leftUp2RightDownDiagonal, final int[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| LongMatrix | `public static LongMatrix diagonal(final long[] leftUp2RightDownDiagonal, final long[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |
| ShortMatrix | `public static ShortMatrix diagonal(final short[] leftUp2RightDownDiagonal, final short[] rightUp2LeftDownDiagonal) throws IllegalArgumentException` |

### 8. Element Access - get(int, int)

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public boolean get(final int i, final int j)` |
| ByteMatrix | `public byte get(final int i, final int j)` |
| CharMatrix | `public char get(final int i, final int j)` |
| DoubleMatrix | `public double get(final int i, final int j)` |
| FloatMatrix | `public float get(final int i, final int j)` |
| IntMatrix | `public int get(final int i, final int j)` |
| LongMatrix | `public long get(final int i, final int j)` |
| ShortMatrix | `public short get(final int i, final int j)` |

### 9. Element Access - get(Point)

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public boolean get(final Point point)` |
| ByteMatrix | `public byte get(final Point point)` |
| CharMatrix | `public char get(final Point point)` |
| DoubleMatrix | `public double get(final Point point)` |
| FloatMatrix | `public float get(final Point point)` |
| IntMatrix | `public int get(final Point point)` |
| LongMatrix | `public long get(final Point point)` |
| ShortMatrix | `public short get(final Point point)` |

### 10. Element Modification - set(int, int, value)

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public void set(final int i, final int j, final boolean val)` |
| ByteMatrix | `public void set(final int i, final int j, final byte val)` |
| CharMatrix | `public void set(final int i, final int j, final char val)` |
| DoubleMatrix | `public void set(final int i, final int j, final double val)` |
| FloatMatrix | `public void set(final int i, final int j, final float val)` |
| IntMatrix | `public void set(final int i, final int j, final int val)` |
| LongMatrix | `public void set(final int i, final int j, final long val)` |
| ShortMatrix | `public void set(final int i, final int j, final short val)` |

### 11. Element Modification - set(Point, value)

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public void set(final Point point, final boolean val)` |
| ByteMatrix | `public void set(final Point point, final byte val)` |
| CharMatrix | `public void set(final Point point, final char val)` |
| DoubleMatrix | `public void set(final Point point, final double val)` |
| FloatMatrix | `public void set(final Point point, final float val)` |
| IntMatrix | `public void set(final Point point, final int val)` |
| LongMatrix | `public void set(final Point point, final long val)` |
| ShortMatrix | `public void set(final Point point, final short val)` |

### 12. Navigation - upOf()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public OptionalBoolean upOf(final int i, final int j)` |
| ByteMatrix | `public OptionalByte upOf(final int i, final int j)` |
| CharMatrix | `public OptionalChar upOf(final int i, final int j)` |
| DoubleMatrix | `public OptionalDouble upOf(final int i, final int j)` |
| FloatMatrix | `public OptionalFloat upOf(final int i, final int j)` |
| IntMatrix | `public OptionalInt upOf(final int i, final int j)` |
| LongMatrix | `public OptionalLong upOf(final int i, final int j)` |
| ShortMatrix | `public OptionalShort upOf(final int i, final int j)` |

### 13. Navigation - downOf()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public OptionalBoolean downOf(final int i, final int j)` |
| ByteMatrix | `public OptionalByte downOf(final int i, final int j)` |
| CharMatrix | `public OptionalChar downOf(final int i, final int j)` |
| DoubleMatrix | `public OptionalDouble downOf(final int i, final int j)` |
| FloatMatrix | `public OptionalFloat downOf(final int i, final int j)` |
| IntMatrix | `public OptionalInt downOf(final int i, final int j)` |
| LongMatrix | `public OptionalLong downOf(final int i, final int j)` |
| ShortMatrix | `public OptionalShort downOf(final int i, final int j)` |

### 14. Navigation - leftOf()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public OptionalBoolean leftOf(final int i, final int j)` |
| ByteMatrix | `public OptionalByte leftOf(final int i, final int j)` |
| CharMatrix | `public OptionalChar leftOf(final int i, final int j)` |
| DoubleMatrix | `public OptionalDouble leftOf(final int i, final int j)` |
| FloatMatrix | `public OptionalFloat leftOf(final int i, final int j)` |
| IntMatrix | `public OptionalInt leftOf(final int i, final int j)` |
| LongMatrix | `public OptionalLong leftOf(final int i, final int j)` |
| ShortMatrix | `public OptionalShort leftOf(final int i, final int j)` |

### 15. Navigation - rightOf()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public OptionalBoolean rightOf(final int i, final int j)` |
| ByteMatrix | `public OptionalByte rightOf(final int i, final int j)` |
| CharMatrix | `public OptionalChar rightOf(final int i, final int j)` |
| DoubleMatrix | `public OptionalDouble rightOf(final int i, final int j)` |
| FloatMatrix | `public OptionalFloat rightOf(final int i, final int j)` |
| IntMatrix | `public OptionalInt rightOf(final int i, final int j)` |
| LongMatrix | `public OptionalLong rightOf(final int i, final int j)` |
| ShortMatrix | `public OptionalShort rightOf(final int i, final int j)` |

### 16. Navigation - adjacent4Points()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| ByteMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| CharMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| DoubleMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| FloatMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| IntMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| LongMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |
| ShortMatrix | `public Stream<Point> adjacent4Points(final int i, final int j)` |

### 17. Navigation - adjacent8Points()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| ByteMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| CharMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| DoubleMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| FloatMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| IntMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| LongMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |
| ShortMatrix | `public Stream<Point> adjacent8Points(final int i, final int j)` |

### 18. Row/Column Operations - row()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public boolean[] row(final int rowIndex) throws IllegalArgumentException` |
| ByteMatrix | `public byte[] row(final int rowIndex) throws IllegalArgumentException` |
| CharMatrix | `public char[] row(final int rowIndex) throws IllegalArgumentException` |
| DoubleMatrix | `public double[] row(final int rowIndex) throws IllegalArgumentException` |
| FloatMatrix | `public float[] row(final int rowIndex) throws IllegalArgumentException` |
| IntMatrix | `public int[] row(final int rowIndex) throws IllegalArgumentException` |
| LongMatrix | `public long[] row(final int rowIndex) throws IllegalArgumentException` |
| ShortMatrix | `public short[] row(final int rowIndex) throws IllegalArgumentException` |

### 19. Row/Column Operations - column()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public boolean[] column(final int columnIndex) throws IllegalArgumentException` |
| ByteMatrix | `public byte[] column(final int columnIndex) throws IllegalArgumentException` |
| CharMatrix | `public char[] column(final int columnIndex) throws IllegalArgumentException` |
| DoubleMatrix | `public double[] column(final int columnIndex) throws IllegalArgumentException` |
| FloatMatrix | `public float[] column(final int columnIndex) throws IllegalArgumentException` |
| IntMatrix | `public int[] column(final int columnIndex) throws IllegalArgumentException` |
| LongMatrix | `public long[] column(final int columnIndex) throws IllegalArgumentException` |
| ShortMatrix | `public short[] column(final int columnIndex) throws IllegalArgumentException` |

### 20. Row/Column Operations - setRow()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public void setRow(final int rowIndex, final boolean[] row) throws IllegalArgumentException` |
| ByteMatrix | `public void setRow(final int rowIndex, final byte[] row) throws IllegalArgumentException` |
| CharMatrix | `public void setRow(final int rowIndex, final char[] row) throws IllegalArgumentException` |
| DoubleMatrix | `public void setRow(final int rowIndex, final double[] row) throws IllegalArgumentException` |
| FloatMatrix | `public void setRow(final int rowIndex, final float[] row) throws IllegalArgumentException` |
| IntMatrix | `public void setRow(final int rowIndex, final int[] row) throws IllegalArgumentException` |
| LongMatrix | `public void setRow(final int rowIndex, final long[] row) throws IllegalArgumentException` |
| ShortMatrix | `public void setRow(final int rowIndex, final short[] row) throws IllegalArgumentException` |

### 21. Row/Column Operations - setColumn()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public void setColumn(final int columnIndex, final boolean[] column) throws IllegalArgumentException` |
| ByteMatrix | `public void setColumn(final int columnIndex, final byte[] column) throws IllegalArgumentException` |
| CharMatrix | `public void setColumn(final int columnIndex, final char[] column) throws IllegalArgumentException` |
| DoubleMatrix | `public void setColumn(final int columnIndex, final double[] column) throws IllegalArgumentException` |
| FloatMatrix | `public void setColumn(final int columnIndex, final float[] column) throws IllegalArgumentException` |
| IntMatrix | `public void setColumn(final int columnIndex, final int[] column) throws IllegalArgumentException` |
| LongMatrix | `public void setColumn(final int columnIndex, final long[] column) throws IllegalArgumentException` |
| ShortMatrix | `public void setColumn(final int columnIndex, final short[] column) throws IllegalArgumentException` |

### 22. Diagonal Operations - getLU2RD()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public boolean[] getLU2RD()` |
| ByteMatrix | `public byte[] getLU2RD()` |
| CharMatrix | `public char[] getLU2RD()` |
| DoubleMatrix | `public double[] getLU2RD()` |
| FloatMatrix | `public float[] getLU2RD()` |
| IntMatrix | `public int[] getLU2RD()` |
| LongMatrix | `public long[] getLU2RD()` |
| ShortMatrix | `public short[] getLU2RD()` |

### 23. Diagonal Operations - setLU2RD()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public void setLU2RD(final boolean[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| ByteMatrix | `public void setLU2RD(final byte[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| CharMatrix | `public void setLU2RD(final char[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| DoubleMatrix | `public void setLU2RD(final double[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| FloatMatrix | `public void setLU2RD(final float[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| IntMatrix | `public void setLU2RD(final int[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| LongMatrix | `public void setLU2RD(final long[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| ShortMatrix | `public void setLU2RD(final short[] diagonal) throws IllegalStateException, IllegalArgumentException` |

### 24. Diagonal Operations - getRU2LD()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public boolean[] getRU2LD()` |
| ByteMatrix | `public byte[] getRU2LD()` |
| CharMatrix | `public char[] getRU2LD()` |
| DoubleMatrix | `public double[] getRU2LD()` |
| FloatMatrix | `public float[] getRU2LD()` |
| IntMatrix | `public int[] getRU2LD()` |
| LongMatrix | `public long[] getRU2LD()` |
| ShortMatrix | `public short[] getRU2LD()` |

### 25. Diagonal Operations - setRU2LD()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public void setRU2LD(final boolean[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| ByteMatrix | `public void setRU2LD(final byte[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| CharMatrix | `public void setRU2LD(final char[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| DoubleMatrix | `public void setRU2LD(final double[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| FloatMatrix | `public void setRU2LD(final float[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| IntMatrix | `public void setRU2LD(final int[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| LongMatrix | `public void setRU2LD(final long[] diagonal) throws IllegalStateException, IllegalArgumentException` |
| ShortMatrix | `public void setRU2LD(final short[] diagonal) throws IllegalStateException, IllegalArgumentException` |

### 26. Matrix Transformations - copy()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public BooleanMatrix copy()` |
| ByteMatrix | `public ByteMatrix copy()` |
| CharMatrix | `public CharMatrix copy()` |
| DoubleMatrix | `public DoubleMatrix copy()` |
| FloatMatrix | `public FloatMatrix copy()` |
| IntMatrix | `public IntMatrix copy()` |
| LongMatrix | `public LongMatrix copy()` |
| ShortMatrix | `public ShortMatrix copy()` |

### 27. Matrix Transformations - transpose()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public BooleanMatrix transpose()` |
| ByteMatrix | `public ByteMatrix transpose()` |
| CharMatrix | `public CharMatrix transpose()` |
| DoubleMatrix | `public DoubleMatrix transpose()` |
| FloatMatrix | `public FloatMatrix transpose()` |
| IntMatrix | `public IntMatrix transpose()` |
| LongMatrix | `public LongMatrix transpose()` |
| ShortMatrix | `public ShortMatrix transpose()` |

### 28. Matrix Transformations - rotate90()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public BooleanMatrix rotate90()` |
| ByteMatrix | `public ByteMatrix rotate90()` |
| CharMatrix | `public CharMatrix rotate90()` |
| DoubleMatrix | `public DoubleMatrix rotate90()` |
| FloatMatrix | `public FloatMatrix rotate90()` |
| IntMatrix | `public IntMatrix rotate90()` |
| LongMatrix | `public LongMatrix rotate90()` |
| ShortMatrix | `public ShortMatrix rotate90()` |

### 29. Matrix Transformations - rotate180()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public BooleanMatrix rotate180()` |
| ByteMatrix | `public ByteMatrix rotate180()` |
| CharMatrix | `public CharMatrix rotate180()` |
| DoubleMatrix | `public DoubleMatrix rotate180()` |
| FloatMatrix | `public FloatMatrix rotate180()` |
| IntMatrix | `public IntMatrix rotate180()` |
| LongMatrix | `public LongMatrix rotate180()` |
| ShortMatrix | `public ShortMatrix rotate180()` |

### 30. Matrix Transformations - rotate270()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public BooleanMatrix rotate270()` |
| ByteMatrix | `public ByteMatrix rotate270()` |
| CharMatrix | `public CharMatrix rotate270()` |
| DoubleMatrix | `public DoubleMatrix rotate270()` |
| FloatMatrix | `public FloatMatrix rotate270()` |
| IntMatrix | `public IntMatrix rotate270()` |
| LongMatrix | `public LongMatrix rotate270()` |
| ShortMatrix | `public ShortMatrix rotate270()` |

### 31. Matrix Transformations - flatten()

| Class Name | Method Signature |
|------------|------------------|
| BooleanMatrix | `public BooleanList flatten()` |
| ByteMatrix | `public ByteList flatten()` |
| CharMatrix | `public CharList flatten()` |
| DoubleMatrix | `public DoubleList flatten()` |
| FloatMatrix | `public FloatList flatten()` |
| IntMatrix | `public IntList flatten()` |
| LongMatrix | `public LongList flatten()` |
| ShortMatrix | `public ShortList flatten()` |

---

## Part 2: Tuple Classes - Similar Methods

### 1. Factory Methods - of() with 1 parameter

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public static BooleanTuple1 of(final boolean _1)` |
| ByteTuple | `public static ByteTuple1 of(final byte _1)` |
| CharTuple | `public static CharTuple1 of(final char _1)` |
| DoubleTuple | `public static DoubleTuple1 of(final double _1)` |
| FloatTuple | `public static FloatTuple1 of(final float _1)` |
| IntTuple | `public static IntTuple1 of(final int _1)` |
| LongTuple | `public static LongTuple1 of(final long _1)` |
| ShortTuple | `public static ShortTuple1 of(final short _1)` |

### 2. Factory Methods - of() with 2 parameters

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public static BooleanTuple2 of(final boolean _1, final boolean _2)` |
| ByteTuple | `public static ByteTuple2 of(final byte _1, final byte _2)` |
| CharTuple | `public static CharTuple2 of(final char _1, final char _2)` |
| DoubleTuple | `public static DoubleTuple2 of(final double _1, final double _2)` |
| FloatTuple | `public static FloatTuple2 of(final float _1, final float _2)` |
| IntTuple | `public static IntTuple2 of(final int _1, final int _2)` |
| LongTuple | `public static LongTuple2 of(final long _1, final long _2)` |
| ShortTuple | `public static ShortTuple2 of(final short _1, final short _2)` |

### 3. Factory Methods - create()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public static <TP extends BooleanTuple<TP>> TP create(final boolean[] a)` |
| ByteTuple | `public static <TP extends ByteTuple<TP>> TP create(final byte[] a)` |
| CharTuple | `public static <TP extends CharTuple<TP>> TP create(final char[] a)` |
| DoubleTuple | `public static <TP extends DoubleTuple<TP>> TP create(final double[] a)` |
| FloatTuple | `public static <TP extends FloatTuple<TP>> TP create(final float[] a)` |
| IntTuple | `public static <TP extends IntTuple<TP>> TP create(final int[] a)` |
| LongTuple | `public static <TP extends LongTuple<TP>> TP create(final long[] a)` |
| ShortTuple | `public static <TP extends ShortTuple<TP>> TP create(final short[] a)` |

### 4. Aggregate Operations - min()

| Class Name | Method Signature |
|------------|------------------|
| ByteTuple | `public byte min()` |
| CharTuple | `public char min()` |
| DoubleTuple | `public double min()` |
| FloatTuple | `public float min()` |
| IntTuple | `public int min()` |
| LongTuple | `public long min()` |
| ShortTuple | `public short min()` |

### 5. Aggregate Operations - max()

| Class Name | Method Signature |
|------------|------------------|
| ByteTuple | `public byte max()` |
| CharTuple | `public char max()` |
| DoubleTuple | `public double max()` |
| FloatTuple | `public float max()` |
| IntTuple | `public int max()` |
| LongTuple | `public long max()` |
| ShortTuple | `public short max()` |

### 6. Aggregate Operations - median()

| Class Name | Method Signature |
|------------|------------------|
| ByteTuple | `public byte median()` |
| CharTuple | `public char median()` |
| DoubleTuple | `public double median()` |
| FloatTuple | `public float median()` |
| IntTuple | `public int median()` |
| LongTuple | `public long median()` |
| ShortTuple | `public short median()` |

### 7. Aggregate Operations - sum()

| Class Name | Method Signature |
|------------|------------------|
| ByteTuple | `public int sum()` |
| CharTuple | `public int sum()` |
| DoubleTuple | `public double sum()` |
| FloatTuple | `public float sum()` |
| IntTuple | `public long sum()` |
| LongTuple | `public long sum()` |
| ShortTuple | `public int sum()` |

### 8. Aggregate Operations - average()

| Class Name | Method Signature |
|------------|------------------|
| ByteTuple | `public double average()` |
| CharTuple | `public double average()` |
| DoubleTuple | `public double average()` |
| FloatTuple | `public double average()` |
| IntTuple | `public double average()` |
| LongTuple | `public double average()` |
| ShortTuple | `public double average()` |

### 9. Tuple Operations - reverse()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public abstract TP reverse()` |
| ByteTuple | `public abstract TP reverse()` |
| CharTuple | `public abstract TP reverse()` |
| DoubleTuple | `public abstract TP reverse()` |
| FloatTuple | `public abstract TP reverse()` |
| IntTuple | `public abstract TP reverse()` |
| LongTuple | `public abstract TP reverse()` |
| ShortTuple | `public abstract TP reverse()` |

### 10. Tuple Operations - contains()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public abstract boolean contains(boolean valueToFind)` |
| ByteTuple | `public abstract boolean contains(byte valueToFind)` |
| CharTuple | `public abstract boolean contains(char valueToFind)` |
| DoubleTuple | `public abstract boolean contains(double valueToFind)` |
| FloatTuple | `public abstract boolean contains(float valueToFind)` |
| IntTuple | `public abstract boolean contains(int valueToFind)` |
| LongTuple | `public abstract boolean contains(long valueToFind)` |
| ShortTuple | `public abstract boolean contains(short valueToFind)` |

### 11. Conversion - toArray()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public boolean[] toArray()` |
| ByteTuple | `public byte[] toArray()` |
| CharTuple | `public char[] toArray()` |
| DoubleTuple | `public double[] toArray()` |
| FloatTuple | `public float[] toArray()` |
| IntTuple | `public int[] toArray()` |
| LongTuple | `public long[] toArray()` |
| ShortTuple | `public short[] toArray()` |

### 12. Conversion - toList()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public BooleanList toList()` |
| ByteTuple | `public ByteList toList()` |
| CharTuple | `public CharList toList()` |
| DoubleTuple | `public DoubleList toList()` |
| FloatTuple | `public FloatList toList()` |
| IntTuple | `public IntList toList()` |
| LongTuple | `public LongList toList()` |
| ShortTuple | `public ShortList toList()` |

### 13. Stream Operations - stream()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public Stream<Boolean> stream()` |
| ByteTuple | `public ByteStream stream()` |
| CharTuple | `public CharStream stream()` |
| DoubleTuple | `public DoubleStream stream()` |
| FloatTuple | `public FloatStream stream()` |
| IntTuple | `public IntStream stream()` |
| LongTuple | `public LongStream stream()` |
| ShortTuple | `public ShortStream stream()` |

### 14. Object Methods - hashCode()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public int hashCode()` |
| ByteTuple | `public int hashCode()` |
| CharTuple | `public int hashCode()` |
| DoubleTuple | `public int hashCode()` |
| FloatTuple | `public int hashCode()` |
| IntTuple | `public int hashCode()` |
| LongTuple | `public int hashCode()` |
| ShortTuple | `public int hashCode()` |

### 15. Object Methods - equals()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public boolean equals(final Object obj)` |
| ByteTuple | `public boolean equals(final Object obj)` |
| CharTuple | `public boolean equals(final Object obj)` |
| DoubleTuple | `public boolean equals(final Object obj)` |
| FloatTuple | `public boolean equals(final Object obj)` |
| IntTuple | `public boolean equals(final Object obj)` |
| LongTuple | `public boolean equals(final Object obj)` |
| ShortTuple | `public boolean equals(final Object obj)` |

### 16. Object Methods - toString()

| Class Name | Method Signature |
|------------|------------------|
| BooleanTuple | `public String toString()` |
| ByteTuple | `public String toString()` |
| CharTuple | `public String toString()` |
| DoubleTuple | `public String toString()` |
| FloatTuple | `public String toString()` |
| IntTuple | `public String toString()` |
| LongTuple | `public String toString()` |
| ShortTuple | `public String toString()` |

---

## Summary

Total similar method groups identified:
- **Matrix classes**: 31 method groups with consistent signatures
- **Tuple classes**: 16 method groups with consistent signatures

All similar methods follow the same naming convention and parameter structure, with only primitive types varying. This makes them ideal candidates for javadoc harmonization to ensure consistent documentation across all implementations.
