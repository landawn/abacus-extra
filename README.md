# abacus-extra

[![Maven Central](https://img.shields.io/maven-central/v/com.landawn.abacus/abacus-extra.svg)](https://central.sonatype.com/artifact/com.landawn.abacus/abacus-extra)
[![Javadocs](https://img.shields.io/badge/javadoc-online-brightgreen.svg)](https://www.javadoc.io/doc/com.landawn.abacus/abacus-extra)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

Additional utilities for the [Abacus](https://github.com/landawn/abacus-common) ecosystem: immutable primitive tuples, point/value records, a read-only `int[]` wrapper, and operations on one-, two-, and three-dimensional arrays.

This guide describes the source in this checkout: **abacus-extra 3.9.0**, built for **Java 21**, with **abacus-common 8.3.0**. These versions come from [pom.xml](pom.xml); they do not identify the latest published release. Select hosted Javadoc matching your dependency, or generate documentation locally.

## Contents

- [Requirements and installation](#requirements-and-installation)
- [Quick start](#quick-start)
- [Choosing an API](#choosing-an-api)
- [Primitive tuples](#primitive-tuples)
  - [Creation and element access](#creation-and-element-access)
  - [Tuple operation reference](#tuple-operation-reference)
  - [Numeric behavior](#numeric-behavior)
  - [Functional operations](#functional-operations)
  - [Empty tuples and deprecated factories](#empty-tuples-and-deprecated-factories)
- [Point and value records](#point-and-value-records)
- [ImmutableIntArray](#immutableintarray)
- [Array operations](#array-operations)
  - [Namespaces and operation reference](#namespaces-and-operation-reference)
  - [Reshape and flatten](#reshape-and-flatten)
  - [Mapping and type conversion](#mapping-and-type-conversion)
  - [Zipping arrays](#zipping-arrays)
  - [In-place updates](#in-place-updates)
  - [Flatten, operate, and copy back](#flatten-operate-and-copy-back)
  - [Inspecting shape and printing](#inspecting-shape-and-printing)
  - [Nulls, runtime types, and exceptions](#nulls-runtime-types-and-exceptions)
- [Ownership and concurrency](#ownership-and-concurrency)
- [Troubleshooting](#troubleshooting)
- [Building and testing](#building-and-testing)
- [Further reference](#further-reference)
- [License](#license)

## Requirements and installation

Use JDK 21 or later. `abacus-common` supplies primitive collections, streams, optional types, functional interfaces, and supporting utilities. It is declared with Maven scope `provided`, so consumers must add it explicitly to their compile and runtime classpaths.

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.landawn.abacus</groupId>
        <artifactId>abacus-extra</artifactId>
        <version>3.9.0</version>
    </dependency>
    <dependency>
        <groupId>com.landawn.abacus</groupId>
        <artifactId>abacus-common</artifactId>
        <version>8.3.0</version>
    </dependency>
</dependencies>
```

### Gradle

Groovy DSL:

```groovy
implementation 'com.landawn.abacus:abacus-extra:3.9.0'
implementation 'com.landawn.abacus:abacus-common:8.3.0'
```

Kotlin DSL:

```kotlin
implementation("com.landawn.abacus:abacus-extra:3.9.0")
implementation("com.landawn.abacus:abacus-common:8.3.0")
```

These examples use the checkout's versions. If that version is unavailable in your configured repository, use a published version with its corresponding requirements, or install this checkout locally using the [build instructions](#building-and-testing).

Public utility types are in `com.landawn.abacus.util`. Import `com.landawn.abacus.util.Arrays` explicitly: it shares its simple name with `java.util.Arrays`. When using both, qualify JDK calls such as `java.util.Arrays.sort(values)`.

## Quick start

This complete example combines the main APIs:

```java
import com.landawn.abacus.util.Arrays;
import com.landawn.abacus.util.ImmutableIntArray;
import com.landawn.abacus.util.IntTuple;
import com.landawn.abacus.util.Points;

public class QuickStart {
    public static void main(String[] args) {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        System.out.println(tuple._2);                     // 20
        System.out.println(tuple.sum());                  // 60
        System.out.println(tuple.average().orElse(0.0));   // 20.0

        int[][] grid = Arrays.reshape(new int[] {1, 2, 3, 4, 5}, 2);
        Arrays.println(grid); // [[1, 2],\n [3, 4],\n [5]]

        int[] sums = Arrays.zip(new int[] {1, 2}, new int[] {10, 20},
                (left, right) -> left + right);
        Arrays.println(sums);                             // [11, 22]

        ImmutableIntArray snapshot = ImmutableIntArray.copyOf(sums);
        sums[0] = 999;
        System.out.println(snapshot.get(0));              // 11

        Points.D2.IntDoublePoint sample = Points.D2.IntDoublePoint.of(3, 4, 12.5);
        System.out.println(sample.value());               // 12.5
    }
}
```

The shorter Java examples below are method-body fragments using these imports. Array comments describe contents; use `Arrays.println` to display them.

## Choosing an API

| Need | API | Characteristics |
| --- | --- | --- |
| A few values of one primitive type | `BooleanTuple`, `ByteTuple`, `CharTuple`, `ShortTuple`, `IntTuple`, `LongTuple`, `FloatTuple`, `DoubleTuple` | Fixed arity, final fields, value equality, primitive storage. |
| Coordinates associated with a measurement or label | `Points.D2`, `Points.D3` | Records with named coordinate accessors and a `value()` payload. |
| A read-only integer sequence of arbitrary length | `ImmutableIntArray` | Indexed access, aggregates, ranges, traversal; copying or shared backing. |
| Transform or combine arrays | `Arrays` | Primitive overloads and object helpers grouped by dimension. |

`Arrays` and `ImmutableIntArray` are annotated `@Beta`; their APIs may evolve. Primitive tuples use sealed hierarchies. Their common `PrimitiveTuple` base is package-private: application code should use the public families or their nested arity-specific types.

## Primitive tuples

### Creation and element access

Each family stores one primitive type. Factory overloads return a specific nested type whose suffix is its element count: `IntTuple.of(4, 8)` returns `IntTuple.IntTuple2`.

```java
IntTuple.IntTuple3 coordinates = IntTuple.of(3, 4, 5);
int first = coordinates._1;                  // 3
int size = coordinates.arity();              // 3
IntTuple.IntTuple3 reversed = coordinates.reversed();
// reversed contains [5, 4, 3]; coordinates remains [3, 4, 5]

int[] exported = coordinates.toArray();
exported[0] = 99;
assert coordinates._1 == 3;
```

Components are public final fields `_1` through `_9`, starting at one. `toArray()` returns an independent primitive array. `toList()` returns an independent, mutable Abacus primitive list. Value equality includes family, arity, order, and values.

For narrow types, supply the correct primitive type, for example `ByteTuple.of((byte) 1, (byte) 2)` or `ShortTuple.of((short) 10)`. Use `L` for long literals and `f` for float literals.

### Tuple operation reference

| Operation | Meaning |
| --- | --- |
| `arity()` | Element count, from 0 through 9. |
| `_1`, `_2`, ... | Direct access on the corresponding nonempty nested type. |
| `contains(value)` | Whether an equal primitive value occurs. |
| `reversed()` | Tuple of the same arity with reversed element order. |
| `toArray()` / `toList()` | Independent primitive array / Abacus primitive list. |
| `forEach(action)` | Visit primitive elements in order. |
| `stream()` | Abacus stream; `BooleanTuple` uses `Stream<Boolean>`. |
| `accept(action)` | Pass the entire tuple to an action. |
| `map(mapper)` | Map the entire tuple to a result, possibly null. |
| `filter(predicate)` | Abacus `Optional` containing this tuple if the predicate passes. |
| `toOptional()` | Present Abacus `Optional`, even for a zero-element tuple. |
| `equals`, `hashCode`, `toString` | Value equality, matching hash code, readable contents. |

Streams and optionals belong to `com.landawn.abacus.util.stream` and `com.landawn.abacus.util.u`. They are not the similarly named JDK types.

### Numeric behavior

`BooleanTuple` has no numeric aggregates. The other families provide:

| Family | `min()` / `max()` / `lowerMedian()` | `sum()` | `average()` | `median()` |
| --- | --- | --- | --- | --- |
| `ByteTuple` | `byte` | `int` | Abacus `OptionalDouble` | `double` |
| `CharTuple` | `char` | `int` | Abacus `OptionalDouble` | Not available |
| `ShortTuple` | `short` | `int` | Abacus `OptionalDouble` | `double` |
| `IntTuple` | `int` | `int`, checked overflow | Abacus `OptionalDouble` | `double` |
| `LongTuple` | `long` | `long`, wrapping overflow | Abacus `OptionalDouble` | `double` |
| `FloatTuple` | `float` | `float` | Abacus `OptionalDouble` | `double` |
| `DoubleTuple` | `double` | `double` | Abacus `OptionalDouble` | `double` |

`lowerMedian()` selects the lower middle sorted value for even sizes. `median()` averages the two middle values for even sizes. Neither changes tuple order. Character aggregates operate on UTF-16 code-unit values.

```java
IntTuple.IntTuple4 values = IntTuple.of(8, 2, 6, 4);
assert values.min() == 2;
assert values.max() == 8;
assert values.sum() == 20;
assert values.average().orElse(0.0) == 5.0;
assert values.lowerMedian() == 4;
assert values.median() == 5.0;
```

`IntTuple.sum()` throws `ArithmeticException` if the total cannot fit in an `int`. `LongTuple.sum()` uses wrapping `long` arithmetic. Floating-point operations retain precision limits and special-value behavior; do not assume exact decimal sums or identical results after reordering. Float/double tuple equality and containment use `Float.compare` / `Double.compare`: NaN matches NaN, and positive zero differs from negative zero.

### Functional operations

A one-argument `map` receives the tuple itself, rather than each element. Pairs and triples also provide component-wise `accept`, `map`, and `filter` overloads with two- and three-argument callbacks.

```java
IntTuple.IntTuple2 pair = IntTuple.of(3, 4);
String label = pair.map(t -> "x=" + t._1 + ", y=" + t._2);
int squaredLength = pair.map((x, y) -> x * x + y * y); // 25
boolean positive = pair.filter((x, y) -> x > 0 && y > 0).isPresent();

pair.forEach(value -> System.out.println(value)); // prints 3, then 4
pair.accept(t -> System.out.println(t.sum()));    // prints 7 once
```

Callbacks use checked-exception-capable `Throwables` interfaces. Callback exceptions propagate; handle or declare checked exceptions as usual. Null callbacks throw `IllegalArgumentException`, even on empty tuples. Whole-tuple callbacks run once for arity zero; element-wise `forEach` runs zero times.

### Empty tuples and deprecated factories

Arity 0 through 9 is supported, but the `of(...)` factories take 1 through 9 arguments; there is no zero-argument `of()`. Eight- and nine-element `of(...)` overloads are deprecated in favor of a custom type with meaningful property names.

Deprecated `from(primitiveArray)` chooses arity at runtime. Null or empty input produces a shared empty tuple; more than nine elements throws `IllegalArgumentException`. Input values are copied. Prefer `of(...)` for known arities.

In legacy code, assign `from` results to a family wildcard such as `IntTuple<?>` unless the exact length is guaranteed. The generic return type does not verify arity: assigning a two-element result to `IntTuple.IntTuple3` causes `ClassCastException`.

For empty numeric tuples, `sum()` is zero and `average()` is empty. `min()`, `max()`, `lowerMedian()`, and `median()` (where available) throw `NoSuchElementException`. An empty tuple's `toOptional()` remains present because the tuple itself is a value.

## Point and value records

Every point contains a payload as well as coordinates:

- `Points.D2`: `x()`, `y()`, `value()`.
- `Points.D3`: `x()`, `y()`, `z()`, `value()`.

Names follow `<CoordinateType><PayloadType>Point`. Both dimensions offer all combinations below (20 record types per dimension):

| Coordinates | Available record names |
| --- | --- |
| `byte` | `ByteBytePoint`, `ByteIntPoint`, `ByteLongPoint`, `ByteDoublePoint`, `ByteObjPoint<T>` |
| `int` | `IntBytePoint`, `IntIntPoint`, `IntLongPoint`, `IntDoublePoint`, `IntObjPoint<T>` |
| `long` | `LongBytePoint`, `LongIntPoint`, `LongLongPoint`, `LongDoublePoint`, `LongObjPoint<T>` |
| `double` | `DoubleBytePoint`, `DoubleIntPoint`, `DoubleLongPoint`, `DoubleDoublePoint`, `DoubleObjPoint<T>` |

Use `of(...)` or the canonical record constructor, with coordinates followed by the payload:

```java
Points.D2.IntDoublePoint reading = Points.D2.IntDoublePoint.of(3, 4, 12.5);
assert reading.x() == 3;
assert reading.y() == 4;
assert reading.value() == 12.5;

Points.D3.DoubleObjPoint<String> tagged =
        Points.D3.DoubleObjPoint.of(1.0, 2.0, 3.0, "corner");
assert tagged.z() == 3.0;
assert tagged.value().equals("corner");

Points.D2.IntDoublePoint same = new Points.D2.IntDoublePoint(3, 4, 12.5);
assert same.equals(reading);
```

These are data carriers, without distance, vector arithmetic, or coordinate normalization. Factories and constructors preserve inputs without validation: double components accept NaN/infinities, and object payloads may be null.

Equality requires the same record type and compares every coordinate and payload. Double components distinguish signed zero and treat NaN as equal to NaN. Object payloads use their own equality; array payloads compare by reference. Payloads are not copied, so mutation can change a point's effective value and hash code. Use immutable payloads for stable map keys or set members.

## ImmutableIntArray

The wrapper exposes no mutator methods. Its immutability depends on how backing storage is acquired:

```java
int[] source = {10, 20, 30, 40};
ImmutableIntArray snapshot = ImmutableIntArray.copyOf(source);
ImmutableIntArray shared = ImmutableIntArray.unsafeWrap(source);

source[0] = 99;
assert snapshot.get(0) == 10; // defensive copy
assert shared.get(0) == 99;   // shared backing

ImmutableIntArray middle = snapshot.subArray(1, 3); // [20, 30]
int[] exported = snapshot.copyOfRange(0, snapshot.length());
exported[0] = -1;
assert snapshot.get(0) == 10;
```

Prefer `copyOf` if the source may change. Use `unsafeWrap` only when you control source-array access throughout the wrapper's lifetime. Both accept null and produce an empty wrapper.

| Method | Contract |
| --- | --- |
| `copyOf(int[])` | Defensive copy; null becomes empty. |
| `unsafeWrap(int[])` | Retains input storage; external changes remain visible. |
| `length()`, `isEmpty()` | Size and emptiness. |
| `get(index)` | Zero-based access; invalid index throws `ArrayIndexOutOfBoundsException`. |
| `contains(value)` | Linear search by value. |
| `min()`, `max()` | Extremum; empty input throws `NoSuchElementException`. |
| `sum()` | `int` total; zero for empty input; overflow throws `ArithmeticException`. |
| `average()` | Abacus `OptionalDouble`, empty when there are no values. |
| `forEach(action)` | Visit elements in index order. |
| `forEachIndexed(action)` | Callback receives `(index, value)`. |
| `stream()` | Fresh Abacus `IntStream` over backing storage. |
| `subArray(from, to)` | Independent wrapper containing a copied range. |
| `copyOfRange(from, to)` | Independent plain `int[]` containing a copied range. |
| `equals`, `hashCode`, `toString` | Ordered content comparison and readable contents. |

Ranges are half-open, `[from, to)`, with `0 <= from <= to <= length()`. Equal endpoints produce an empty result. Invalid ranges throw `IndexOutOfBoundsException`; results are never padded. Export all values with `copyOfRange(0, length())`.

```java
ImmutableIntArray numbers = ImmutableIntArray.copyOf(new int[] {10, 20, 30});
assert numbers.sum() == 60;
assert numbers.average().orElse(0.0) == 20.0;
assert numbers.stream().filter(value -> value >= 20).sum() == 50;
numbers.forEachIndexed((index, value) -> System.out.println(index + ": " + value));
```

Every `stream()` call returns a fresh, single-use stream. Nonempty streams read backing storage directly, so unsafe wrappers can expose external mutations during traversal. After a terminal operation or close, obtain another stream. External writes also change equality/hash codes; do not use shared wrappers as hash keys while mutation remains possible.

## Array operations

### Namespaces and operation reference

Primitive overloads live directly on `Arrays`. Object transformations use nested namespaces:

| Namespace | Role |
| --- | --- |
| `Arrays` | Primitive operations; printing for primitive and object arrays. |
| `Arrays.f` | 1D object `map` and `mapToXxx` operations. |
| `Arrays.ff` | 2D object operations, including reshaping a 1D array into 2D. |
| `Arrays.fff` | 3D object operations, including reshaping a 1D array into 3D. |

Core primitive operations cover all eight primitive types, except mapping/conversion families whose supported type pairs are listed below.

| Operation | Dimensions | Effect |
| --- | --- | --- |
| `reshape(a, columns)` | 1D to 2D | Copies sequential chunks into rows. |
| `reshape(a, rows, columns)` | 1D to 3D | Copies sequential chunks into blocks of rows. |
| `flatten(a)` | 2D/3D to 1D | Concatenates values in row-major order. |
| `mapToObj(a, mapper, type)` | Primitive 1D/2D/3D | Maps primitives to reference values. |
| `mapToInt`, `mapToLong`, `mapToDouble` | Selected primitive pairs, 1D/2D/3D | Applies a supplied conversion function. |
| `toXxx(a)` | Selected primitive pairs, 1D/2D/3D | Built-in element conversion. |
| `zip(...)` | Primitive 1D/2D/3D; object 2D/3D | Combines two or three inputs at matching positions. |
| `updateAll(a, operator)` | Primitive 1D/2D/3D; object 2D/3D | Replaces each element in place. |
| `replaceIf(a, predicate, value)` | Primitive 1D/2D/3D; object 2D/3D | Replaces matching elements in place. |
| `mutateViaFlatArray(a, action)` | Primitive/object 2D/3D | Temporary flat copy, callback, copy-back. |
| `totalElementCount(a)` | Primitive/object 2D/3D | `long` count of innermost elements. |
| `minRowLength(a)`, `maxRowLength(a)` | Primitive/object 2D | Shortest/longest row length. |
| `println(a)` | Primitive/object 1D/2D/3D | Prints and returns formatted text. |

Call object `map`, `mapToXxx`, shape, mutation, and zip operations through the appropriate nested namespace. There is no general primitive `Arrays.map(a, unaryOperator)` in this API; use `updateAll` to mutate or a supported mapping/conversion method to obtain a result.

### Reshape and flatten

Reshape fills rows in source order. It **does not pad** the final row or block. Dimensions describe maximum row/block capacities, so results may be ragged.

```java
int[] linear = {1, 2, 3, 4, 5, 6, 7};
int[][] rows = Arrays.reshape(linear, 3);
// [[1, 2, 3], [4, 5, 6], [7]]

int[][][] blocks = Arrays.reshape(linear, 2, 2);
// [[[1, 2], [3, 4]], [[5, 6], [7]]]

int[][] ragged = {{1, 2}, null, {}, {3}};
int[] flat = Arrays.flatten(ragged); // [1, 2, 3]

String[][] words = Arrays.ff.reshape(new String[] {"a", "b", "c"}, 2);
String[] restored = Arrays.ff.flatten(words); // ["a", "b", "c"]
```

Row/column counts must be positive, even for empty inputs. Primitive null input becomes an empty result. Object reshape/flatten require a non-null outer array to determine runtime component types. Flatten skips null/empty rows and blocks, but preserves null object elements within non-null rows.

Result storage is independent of the source; object elements are copied as references. Flattening and reshaping preserves element order but loses original row boundaries, empty rows, and null rows. Flatten rejects a logical element count exceeding `Integer.MAX_VALUE` with `ArithmeticException` before allocating the flat result.

### Mapping and type conversion

Mapping applies your function; built-in conversion applies primitive conversion rules. Neither writes to the source array itself.

```java
String[] labels = Arrays.mapToObj(new int[] {1, 2}, n -> "item-" + n, String.class);
// ["item-1", "item-2"]
long[] scaled = Arrays.mapToLong(new int[] {1, 2}, n -> n * 1_000L);
// [1000, 2000]
double[][] widened = Arrays.toDouble(new int[][] {{1, 2}, {3}});
// [[1.0, 2.0], [3.0]]

String[] names = {"ann", "bob"};
String[] upper = Arrays.f.map(names, s -> s.toUpperCase(java.util.Locale.ROOT), String.class);
int[] lengths = Arrays.f.mapToInt(names, String::length); // [3, 3]

String[][] text = {{"1", "2"}, null, {"3"}};
int[][] parsed = Arrays.ff.mapToInt(text, Integer::parseInt);
// [[1, 2], [], [3]]
Integer[][] boxed = Arrays.ff.map(text, Integer::valueOf, Integer.class);
// [[1, 2], [], [3]]
```

Explicit classes identify the **element type**, such as `String.class`, not `String[].class` for scalar string results. Primitive classes such as `int.class` are invalid for object targets; use a primitive mapper.

Primitive callback mappings support `int -> long/double`, `long -> int/double`, and `double -> int/long`. `mapToObj` accepts all primitive families. Object `mapToXxx` supports all eight primitive targets in 1D, 2D, and 3D.

Built-in conversions have 1D, 2D, and 3D overloads for these pairs:

| Target method | Source primitive types |
| --- | --- |
| `toBoolean` | `byte`, `int` |
| `toChar` | `int` |
| `toByte` | `boolean` |
| `toShort` | `byte` |
| `toInt` | `boolean`, `char`, `byte`, `short`, `float`, `double` |
| `toLong` | `byte`, `short`, `int`, `float`, `double` |
| `toFloat` | `byte`, `short`, `int`, `long` |
| `toDouble` | `byte`, `short`, `int`, `long`, `float` |

Boolean-to-numeric conversions produce 1 or 0; numeric-to-boolean tests for strictly positive values (`> 0`), so zero and negative values become false. Floating-point-to-integral conversions use Java casts: fractions truncate toward zero, NaN becomes zero, and out-of-range values saturate to the target bounds. `toChar(int[])` uses Java's narrowing character conversion. Integral-to-floating-point conversion may lose precision.

Mapping/conversion preserves existing row lengths, while null nested arrays become empty arrays at the corresponding level. Object callbacks receive actual null elements; handle them when your data can contain them.

### Zipping arrays

Zip combines corresponding positions. Overloads without defaults stop at the shortest input. Overloads with one default per input extend to the longest input and substitute defaults for missing values.

```java
int[] left = {1, 2, 3};
int[] right = {10, 20};
int[] shortest = Arrays.zip(left, right, (a, b) -> a + b);
// [11, 22]
int[] longest = Arrays.zip(left, right, 0, 100, (a, b) -> a + b);
// [11, 22, 103]

int[] three = Arrays.zip(new int[] {1, 2}, new int[] {10, 20},
        new int[] {100, 200}, (a, b, c) -> a + b + c);
// [111, 222]

int[][] a = {{1, 2}, {3}};
int[][] b = {{10}, {20, 30}, {40}};
int[][] padded = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);
// [[11, 2], [23, 30], [40]]

String[][] descriptions = Arrays.ff.zip(
        new String[][] {{"a", "b"}}, new Integer[][] {{1, 2}},
        (name, count) -> name + "=" + count, String.class);
// [["a=1", "b=2"]]
```

For 2D/3D inputs, shortest/longest rules apply independently at each corresponding level. Zip does not flatten inputs or broadcast rows. Null primitive arrays/rows act as empty inputs. Defaults fill missing positions; existing null object elements still reach the callback as null.

Object zip can infer its result type from the first input or accept an explicit target class. Prefer an explicit class when changing result types or handling null inputs. See runtime-type rules below.

### In-place updates

`updateAll` applies an operator to each element; `replaceIf` writes a fixed value when a predicate passes. Both return `void`.

```java
int[][] values = {{1, 2}, null, {3, 4}};
Arrays.updateAll(values, n -> n * 2);
// [[2, 4], null, [6, 8]]
Arrays.replaceIf(values, n -> n < 5, 0);
// [[0, 0], null, [6, 8]]

String[][] names = {{" ann ", "bob"}, null};
Arrays.ff.updateAll(names, String::trim);
Arrays.ff.replaceIf(names, "bob"::equals, "Robert");
// [["ann", "Robert"], null]
```

Null/empty outer and nested arrays contain no elements to update. Null object elements are passed to callbacks. Operators/predicates must be non-null even for empty inputs. Writes happen during traversal: if a callback throws, earlier writes remain. Aliased rows can be visited and updated more than once.

### Flatten, operate, and copy back

`mutateViaFlatArray` creates a temporary flat array, passes it to your action, and copies values back into existing rows:

```java
int[][] values = {{9, 1}, null, {7, 3, 5}};
Arrays.mutateViaFlatArray(values, flat -> java.util.Arrays.sort(flat));
// [[1, 3], null, [5, 7, 9]]
```

The action is skipped for a null/empty outer array. For a nonempty outer array containing only null/empty rows, it runs once with an empty flat array. Copy-back happens only after the action returns normally.

This is not a general transaction: direct writes to the original array or shared object elements are not rolled back. Do not change original array structure inside the callback. When multiple positions reference the same row, copy-back proceeds in index order and the last copy wins. Temporary storage is proportional to total element count.

### Inspecting shape and printing

```java
int[][] ragged = {{1, 2, 3}, null, {}, {4}};
long count = Arrays.totalElementCount(ragged); // 4
int shortest = Arrays.minRowLength(ragged);   // 0
int longest = Arrays.maxRowLength(ragged);    // 3
String printed = Arrays.println(ragged);
// Prints and returns "[[1, 2, 3],\n null,\n [],\n [4]]"
```

`totalElementCount` counts innermost elements, not rows. Null/empty arrays contribute zero. `minRowLength` treats null rows as length zero; both row-length methods return zero for null/empty outer arrays. Object equivalents are `Arrays.ff.totalElementCount`, `Arrays.fff.totalElementCount`, and 2D `Arrays.ff.minRowLength` / `maxRowLength`.

`println` prints to the console and returns the formatted text. Multidimensional output uses `\n` and indentation. It is not a formatting-only operation; avoid using it merely to obtain a string when console output is unwanted.

### Nulls, runtime types, and exceptions

Null behavior is method-specific. This table summarizes common cases; consult the selected overload's Javadoc for the full contract.

| Method family | Null outer input | Additional rules |
| --- | --- | --- |
| Primitive reshape / flatten | Empty result | Reshape dimensions must still be positive. |
| Object reshape / flatten | `IllegalArgumentException` | Runtime type information comes from input. |
| Mapping with explicit target class | Empty result | Mapper/class validated even on empty input. |
| Object 2D/3D `map` without target class | `IllegalArgumentException` | Result type inferred from input runtime type. |
| Primitive conversion / primitive-target mapping | Empty result | Required mapper must be non-null. |
| Primitive zip | Treated as empty | Defaults used only by padded overloads. |
| Object zip with target class | Treated as empty | Class must be a reference type. |
| Object zip without target class | Requires type information | Unpadded forms need the first input; padded forms can infer from a non-null first default when that input is null. |
| Update / replace / flat mutation | No-op | Callback must still be non-null. |
| Count / row-length inspection | Zero | Null nested arrays contribute no elements. |
| `println` | Prints/returns `"null"` | Cast null to select an overload, e.g. `(int[][]) null`. |

Java arrays retain runtime component types. A `Number[][]` variable can reference an `Integer[][]`. Inferred-result mapping uses that actual type, so a produced `Double` cannot fit. Supply `Number.class` to allocate a result capable of holding both. Inferred object map/zip overloads report incompatible produced values as `IllegalArgumentException`, with guidance to use an explicit target. Explicit-target methods can throw `ArrayStoreException` for incompatible results.

Callbacks may throw checked exceptions, which propagate. Object transformations copy references and can expose callback side effects on shared objects. Array-size overflow, invalid ranges/dimensions, and incompatible runtime types are separate errors; accepting null does not make every argument combination valid.

## Ownership and concurrency

| API | Ownership |
| --- | --- |
| Primitive tuples | Immutable values; exported arrays/lists are independent. |
| Primitive-valued points | Immutable coordinate and payload values. |
| Object-valued points | Fixed reference to a potentially mutable payload; no defensive copy. |
| `ImmutableIntArray.copyOf` | Isolated snapshot. |
| `ImmutableIntArray.unsafeWrap` | Shared storage; prevent external writes when immutability is required. |
| Array reshape / flatten / mapping / conversion / zip | Result storage; the operation does not write input arrays. Object values may remain shared. |
| Array update / replace / flat mutation | Writes supplied array storage. |

Array transformations provide no synchronization or atomicity. Share immutable values after safe publication; use synchronization or separate copies for mutable arrays/payloads. Empty result arrays may be shared constants: rely on contents rather than reference identity.

## Troubleshooting

| Symptom | What to check |
| --- | --- |
| Missing Abacus classes at runtime | Add `abacus-common` explicitly; `provided` is not transitive to consumers. |
| Unsupported class-file version or release error | Use JDK 21+; check the JVM in `mvn -version`. |
| `Arrays` ambiguity or missing method | Import Abacus `Arrays`, qualify JDK calls, and use `f` / `ff` / `fff` for object methods. |
| Cannot assign `average()` to `double` | It returns Abacus `OptionalDouble`; handle emptiness or supply a fallback. |
| Tuple `map` receives an unexpected type | One-argument callbacks receive the tuple; use element traversal or pair/triple component overloads. |
| `ClassCastException` from tuple creation | Deprecated `from` was assigned to the wrong arity. Prefer `of` or a family wildcard. |
| Object map/zip rejects a result | Supply a target class broad enough for all result values. |
| Reshape creates a short final row | Intentional: reshape does not pad. |
| Zip drops trailing elements | Use the overload with defaults to extend to the longest input. |
| An immutable value appears to change | Check `unsafeWrap` backing storage or mutable point payloads. |
| Aggregate overflow or precision loss | Check the family-specific sum type and numeric semantics. |

## Building and testing

Run commands from the repository root with JDK 21+ and Maven. Dependency/plugin resolution needs access to configured repositories unless artifacts are cached.

```shell
# Confirm Maven uses the intended JDK.
mvn -version

# Compile production code.
mvn compile

# Run the default unit suite.
mvn test

# Run one focused test class.
mvn -Dtest=IntTupleTest test

# Build the JAR, source JAR, and Javadoc JAR.
mvn package

# Install locally without requiring GPG signing.
mvn -Dgpg.skip=true install

# Generate fresh test coverage.
mvn clean test jacoco:report

# Generate browsable API documentation.
mvn javadoc:javadoc
```

Maven discovers `*Test.java` classes and excludes the `slow-test` tag by default. Test failures fail the build. In PowerShell, quote comma-separated selections: `mvn '-Dtest=IntTupleTest,ArraysShapeTest' test`.

The main JAR is `target/abacus-extra-3.9.0.jar`. Coverage is at `target/site/jacoco/index.html`; consult Javadoc command output for the generated `apidocs/index.html` location. Packaging attaches sources and Javadoc. GPG signing is bound to `verify`; use `-Dgpg.skip=true` for local `verify` or `install` runs when signing is not intended.

See [the test guide](src/test/README.md) for test organization/coverage, and [tools/README.md](tools/README.md) and [scripts/README.md](scripts/README.md) for optional maintenance tooling.

## Further reference

Source Javadocs describe exact overloads and contracts for this checkout:

- [Package overview](src/main/java/com/landawn/abacus/util/package-info.java)
- Tuples: [BooleanTuple](src/main/java/com/landawn/abacus/util/BooleanTuple.java), [ByteTuple](src/main/java/com/landawn/abacus/util/ByteTuple.java), [CharTuple](src/main/java/com/landawn/abacus/util/CharTuple.java), [ShortTuple](src/main/java/com/landawn/abacus/util/ShortTuple.java), [IntTuple](src/main/java/com/landawn/abacus/util/IntTuple.java), [LongTuple](src/main/java/com/landawn/abacus/util/LongTuple.java), [FloatTuple](src/main/java/com/landawn/abacus/util/FloatTuple.java), [DoubleTuple](src/main/java/com/landawn/abacus/util/DoubleTuple.java)
- [Points](src/main/java/com/landawn/abacus/util/Points.java)
- [ImmutableIntArray](src/main/java/com/landawn/abacus/util/ImmutableIntArray.java)
- [Arrays](src/main/java/com/landawn/abacus/util/Arrays.java)
- [Published Javadoc](https://www.javadoc.io/doc/com.landawn.abacus/abacus-extra), [Wiki](https://github.com/landawn/abacus-extra/wiki), and [changelog](CHANGES.md)

Related projects: [abacus-common](https://github.com/landawn/abacus-common) supplies core utilities. [abacus-matrix](https://github.com/landawn/abacus-matrix) contains matrix types moved out of abacus-extra in 3.6.6.

## License

Licensed under the [Apache License, Version 2.0](LICENSE).
