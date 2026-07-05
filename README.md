# abacus-extra

[![Maven Central](https://img.shields.io/maven-central/v/com.landawn.abacus/abacus-extra.svg)](https://central.sonatype.com/artifact/com.landawn.abacus/abacus-extra/3.8.0)
[![Javadocs](https://img.shields.io/badge/javadoc-3.8.0-brightgreen.svg)](https://www.javadoc.io/doc/com.landawn.abacus/abacus-extra/3.8.0/index.html)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE.txt)

Additional utility types for the [Abacus](https://github.com/landawn/abacus-common) ecosystem: immutable
**primitive tuples**, record-based **point/value carriers**, an **immutable `int[]` wrapper**, and a rich set of
**multi-dimensional array helpers** for primitive and object arrays.

The library builds on [abacus-common](https://github.com/landawn/abacus-common) and targets **Java 17+**.

## What's inside

| Type | Purpose |
| --- | --- |
| `BooleanTuple`, `ByteTuple`, `CharTuple`, `ShortTuple`, `IntTuple`, `LongTuple`, `FloatTuple`, `DoubleTuple` | Immutable, fixed-arity (0–9) primitive tuples with aggregate, reversal, containment, and functional helpers. |
| `Points.D2` / `Points.D3` | Record-based 2D/3D point carriers that pair `x`/`y`(`/z`) coordinates with a `value` payload, in primitive or object flavors. |
| `Arrays` | Utility methods for one-, two-, and three-dimensional arrays: bulk update, conditional replace, reshape, flatten, zip, map, element counting, primitive conversion, and formatted printing. |
| `ImmutableIntArray` | Immutable-style wrapper around an `int[]`, with copy-on-create or (unsafe) zero-copy wrapping. |

Browse the rendered class views:
[PrimitiveTuple](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/PrimitiveTuple_view.html) ·
[BooleanTuple](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/BooleanTuple_view.html) ·
[ByteTuple](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/ByteTuple_view.html) ·
[IntTuple](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/IntTuple_view.html) ·
[LongTuple](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/LongTuple_view.html) ·
[DoubleTuple](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/DoubleTuple_view.html) ·
[Points](https://htmlpreview.github.io/?https://github.com/landawn/abacus-extra/blob/master/docs/Points_view.html)

## Requirements

* Java 17 or above
* [abacus-common](https://github.com/landawn/abacus-common) on the classpath — abacus-extra declares it as a
  `provided` dependency, so add it to your own build (see below).

## Installation

**Maven**

```xml
<dependency>
    <groupId>com.landawn.abacus</groupId>
    <artifactId>abacus-extra</artifactId>
    <version>3.8.0</version>
</dependency>

<!-- Required at runtime; abacus-extra does not pull it in transitively -->
<dependency>
    <groupId>com.landawn.abacus</groupId>
    <artifactId>abacus-common</artifactId>
    <version>7.8.5</version>
</dependency>
```

**Gradle**

```gradle
implementation 'com.landawn.abacus:abacus-extra:3.8.0'
implementation 'com.landawn.abacus:abacus-common:7.8.5'
```

## Quick start

**Primitive tuples** — immutable, fixed-arity, with element access via `_1`, `_2`, … fields:

```java
IntTuple.IntTuple3 t = IntTuple.of(1, 2, 3);
t.sum();        // 6
t.average();    // 2.0
t.max();        // 3
t.contains(2);  // true
t.reverse();    // (3, 2, 1)
int first = t._1;  // 1

// Functional helpers shared by every tuple family (accept / map / filter / toOptional):
String s = t.map(x -> "sum=" + x.sum());   // "sum=6"
t.accept(System.out::println);             // prints the tuple
```

**Points** — coordinate + value records; the name encodes the coordinate and value types
(e.g. `IntDoublePoint` = `int` coordinates, `double` value):

```java
Points.D2.IntDoublePoint p = Points.D2.IntDoublePoint.of(3, 4, 12.5);
p.x();      // 3
p.y();      // 4
p.value();  // 12.5

Points.D3.DoubleObjPoint<String> tagged =
        Points.D3.DoubleObjPoint.of(1.0, 2.0, 3.0, "corner");
```

**Arrays** — bulk operations on 1D/2D/3D arrays:

```java
int[] a = {1, 2, 3, 4, 5, 6};
int[][] grid = Arrays.reshape(a, 3);   // [[1, 2, 3], [4, 5, 6]]
int[]   flat = Arrays.flatten(grid);   // [1, 2, 3, 4, 5, 6]

int[] xs = {1, 2, 3};
int[] ys = {10, 20, 30};
int[] sums = Arrays.zip(xs, ys, (x, y) -> x + y);   // [11, 22, 33]

Arrays.updateAll(a, x -> x * 2);        // a -> [2, 4, 6, 8, 10, 12]
Arrays.replaceIf(a, x -> x < 5, 0);     // values < 5 -> 0

// Object arrays live under Arrays.f (1D), Arrays.ff (2D), Arrays.fff (3D):
String[] names = {"ann", "bob"};
String[] upper = Arrays.f.map(names, String::toUpperCase, String.class);  // ["ANN", "BOB"]
```

**ImmutableIntArray** — a read-only view over an `int[]`:

```java
ImmutableIntArray snapshot = ImmutableIntArray.copyOf(new int[] {10, 20, 30});
snapshot.get(1);      // 20
snapshot.length();    // 3
snapshot.stream().sum();  // 60
```

## Documentation

* [API Javadoc](https://www.javadoc.io/doc/com.landawn.abacus/abacus-extra/3.8.0/index.html)
* [User Guide (Wiki)](https://github.com/landawn/abacus-extra/wiki)
* [Changelog](CHANGES.md)

## Related projects

* [abacus-common](https://github.com/landawn/abacus-common) — the core utility library this project extends.
* [abacus-matrix](https://github.com/landawn/abacus-matrix) — primitive/object matrix types (moved out of abacus-extra in 3.6.6).

## License

Licensed under the [Apache License, Version 2.0](LICENSE.txt).
