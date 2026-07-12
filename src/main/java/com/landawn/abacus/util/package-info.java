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
 * Provides supplemental array, point, and primitive tuple utilities for Abacus.
 *
 * <p>The principal APIs are:</p>
 * <ul>
 *   <li>{@link com.landawn.abacus.util.Arrays}, for operations on one-, two-, and
 *       three-dimensional object and primitive arrays, including mapping, reshaping, flattening,
 *       zipping, and primitive conversions;</li>
 *   <li>{@link com.landawn.abacus.util.ImmutableIntArray}, an immutable value-oriented wrapper for
 *       an {@code int} array;</li>
 *   <li>{@link com.landawn.abacus.util.Points}, which groups two- and three-dimensional point/value
 *       records for several coordinate and payload types; and</li>
 *   <li>the {@link com.landawn.abacus.util.BooleanTuple},
 *       {@link com.landawn.abacus.util.ByteTuple}, {@link com.landawn.abacus.util.CharTuple},
 *       {@link com.landawn.abacus.util.ShortTuple}, {@link com.landawn.abacus.util.IntTuple},
 *       {@link com.landawn.abacus.util.LongTuple}, {@link com.landawn.abacus.util.FloatTuple}, and
 *       {@link com.landawn.abacus.util.DoubleTuple} families, which provide immutable,
 *       primitive-specialized tuples with fixed arities.</li>
 * </ul>
 *
 * <p>These types extend the core utilities supplied by the {@code abacus-common} artifact and use
 * its collection, functional, optional, and stream abstractions.</p>
 */
package com.landawn.abacus.util;
