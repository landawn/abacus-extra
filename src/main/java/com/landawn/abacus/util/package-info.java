/*
 * Copyright (C) 2026 HaiYang Li
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

/**
 * Provides supplemental, primitive-specialized value types and array operations for Abacus.
 *
 * <h2>API overview</h2>
 * <ul>
 *   <li>{@link com.landawn.abacus.util.Arrays} supplies bulk operations for one-, two-, and
 *       three-dimensional arrays. Primitive-array overloads are declared directly on
 *       {@code Arrays}; object-array transformations are grouped by dimensionality in
 *       {@link com.landawn.abacus.util.Arrays.f Arrays.f},
 *       {@link com.landawn.abacus.util.Arrays.ff Arrays.ff}, and
 *       {@link com.landawn.abacus.util.Arrays.fff Arrays.fff}.</li>
 *   <li>{@link com.landawn.abacus.util.ArrayConversions} supplies policy-explicit exact numeric
 *       conversions and non-zero boolean mappings with strict outer-array null handling.</li>
 *   <li>{@link com.landawn.abacus.util.ImmutableIntArray} is a read-only, value-oriented wrapper
 *       around an {@code int} array, while {@link com.landawn.abacus.util.IntArrayView} is an
 *       explicitly shared zero-copy view.</li>
 *   <li>{@link com.landawn.abacus.util.Points} groups point/value records under
 *       {@link com.landawn.abacus.util.Points.D2 Points.D2} and
 *       {@link com.landawn.abacus.util.Points.D3 Points.D3}. Coordinate types include
 *       {@code byte}, {@code int}, {@code long}, and {@code double}; payloads may be primitive or
 *       object values.</li>
 *   <li>{@link com.landawn.abacus.util.BooleanTuple},
 *       {@link com.landawn.abacus.util.ByteTuple}, {@link com.landawn.abacus.util.CharTuple},
 *       {@link com.landawn.abacus.util.ShortTuple}, {@link com.landawn.abacus.util.IntTuple},
 *       {@link com.landawn.abacus.util.LongTuple}, {@link com.landawn.abacus.util.FloatTuple}, and
 *       {@link com.landawn.abacus.util.DoubleTuple} provide immutable, primitive-specialized tuples
 *       with fixed arities from zero through nine elements.</li>
 * </ul>
 *
 * <h2>Mutation and ownership</h2>
 * <ul>
 *   <li>Array operations named {@code map}, {@code mapToXxx}, {@code reshape}, {@code flatten},
 *       {@code zip}, or {@code toXxx} return a result array without modifying the supplied array.
 *       Empty results may use a shared empty-array constant. Operations named {@code updateAll},
 *       {@code replaceIf}, or {@code mutateViaFlatArray} modify the supplied array.</li>
 *   <li>{@link com.landawn.abacus.util.ImmutableIntArray#copyOf(int[]) ImmutableIntArray.copyOf}
 *       makes a defensive snapshot with stable value semantics. {@link com.landawn.abacus.util.IntArrayView}
 *       explicitly retains caller-owned storage when later external changes should remain observable.</li>
 *   <li>Primitive tuples are immutable; non-empty tuples return fresh storage from {@code toArray()},
 *       while an empty tuple may return a shared zero-length array. Point records have immutable
 *       components, but an object payload may itself be mutable and is not defensively copied.</li>
 *   <li>Null, empty-input, range, overflow, and ragged-array behavior in the legacy array API is
 *       method-specific. {@code ArrayConversions} instead requires a non-null outer array and
 *       preserves null inner arrays; consult the selected method's contract for other APIs.</li>
 * </ul>
 *
 * <h2>Naming guide</h2>
 * <p>In the array API, {@code mapToXxx} applies a caller-supplied mapping function and returns the
 * named target type, while {@code toXxx} performs a built-in primitive conversion. Tuple
 * {@code arity()} and array {@code length()} both report element counts in their respective
 * domains. Functional parameters use the checked-exception-capable interfaces from
 * {@link com.landawn.abacus.util.Throwables}.</p>
 *
 * <h2>Stability and dependencies</h2>
 * <p>Types annotated with {@link com.landawn.abacus.annotation.Beta Beta} may evolve between
 * releases. This package builds on the collections, functional interfaces, optionals, and streams
 * supplied by the {@code abacus-common} artifact.</p>
 */
package com.landawn.abacus.util;
