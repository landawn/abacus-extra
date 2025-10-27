/*
 * Copyright (C) 2020 HaiYang Li
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

import com.landawn.abacus.annotation.MayReturnNull;
import com.landawn.abacus.util.u.Optional;

/**
 * Abstract base class for primitive tuple implementations.
 * <p>
 * This class provides common functionality for tuples containing primitive values,
 * including functional operations like map, filter, and accept. All primitive tuple
 * implementations extend this class and are immutable by design.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * IntTuple3 tuple = IntTuple.of(1, 2, 3);
 * Optional<IntTuple3> filtered = tuple.filter(t -> t._1 > 0);
 * String mapped = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
 * }</pre>
 *
 * @param <TP> the specific tuple type extending this class
 * @see IntTuple
 * @see LongTuple
 * @see DoubleTuple
 * @see ShortTuple
 */
@com.landawn.abacus.annotation.Immutable
abstract class PrimitiveTuple<TP extends PrimitiveTuple<TP>> implements Immutable {

    /**
     * Returns the number of elements (arity) contained in this tuple.
     * <p>
     * The arity represents the fixed size of the tuple at compile-time. For example,
     * an {@code IntTuple3} has an arity of 3, containing three int values. This value
     * is constant for each tuple type and never changes during the tuple's lifetime.
     * </p>
     *
     * <p>
     * Implementors of this abstract class must override this method to return the
     * specific arity of their tuple type.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple2 tuple2 = IntTuple.of(10, 20);
     * int size = tuple2.arity(); // returns 2
     *
     * IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
     * int size3 = tuple3.arity(); // returns 3
     * }</pre>
     *
     * @return the number of elements in this tuple, which is a positive integer greater than zero
     */
    public abstract int arity();

    /**
     * Performs the given action on this tuple, allowing side effects to be executed.
     * <p>
     * This method executes the provided consumer action with this tuple as the argument.
     * It is primarily used for operations that produce side effects (such as logging,
     * printing, or updating external state) rather than transforming the tuple itself.
     * Since tuples are immutable, this method does not modify the tuple and returns void.
     * </p>
     *
     * <p>
     * The action parameter accepts the specific tuple type (TP), allowing type-safe
     * access to all tuple fields within the consumer lambda.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Printing tuple values
     * IntTuple2 tuple = IntTuple.of(10, 20);
     * tuple.accept(t -> System.out.println("Values: " + t._1 + ", " + t._2));
     *
     * // Performing calculations for side effects
     * IntTuple3 coords = IntTuple.of(1, 2, 3);
     * coords.accept(t -> {
     *     int sum = t._1 + t._2 + t._3;
     *     logger.info("Sum of coordinates: " + sum);
     * });
     *
     * // Exception handling
     * DoubleTuple2 values = DoubleTuple.of(5.0, 10.0);
     * values.accept(t -> {
     *     if (t._1 <= 0) throw new IllegalArgumentException("Value must be positive");
     *     processValues(t._1, t._2);
     * });
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the consumer action to be performed on this tuple, must not be {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super TP, E> action) throws E {
        action.accept((TP) this);
    }

    /**
     * Applies the given mapping function to this tuple and returns the transformed result.
     * <p>
     * This method provides a functional way to transform the tuple into a value of a different
     * type. The mapper function receives this tuple as input and produces a value of type U.
     * This is useful for extracting computed values, converting to different types, or
     * performing any transformation based on the tuple's contents.
     * </p>
     *
     * <p>
     * The mapper function has type-safe access to all fields of the specific tuple type (TP),
     * enabling direct access to tuple elements like {@code t._1}, {@code t._2}, etc.
     * </p>
     *
     * <p>
     * This method does not modify the tuple itself (tuples are immutable), but creates a
     * new value based on the tuple's contents.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Calculate Euclidean distance from origin
     * IntTuple2 point = IntTuple.of(3, 4);
     * Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
     * // distance = 5.0
     *
     * // Convert tuple to a formatted string
     * IntTuple3 rgb = IntTuple.of(255, 128, 0);
     * String hexColor = rgb.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));
     * // hexColor = "#FF8000"
     *
     * // Extract a single computed value
     * DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
     * Double area = dimensions.map(t -> t._1 * t._2);
     * // area = 213.15
     *
     * // Transform to a complex object
     * IntTuple2 coords = IntTuple.of(100, 200);
     * Point2D point2D = coords.map(t -> new Point2D(t._1, t._2));
     * }</pre>
     *
     * @param <U> the type of the result produced by the mapping function
     * @param <E> the type of exception that may be thrown by the mapper
     * @param mapper the mapping function to apply to this tuple, must not be {@code null}
     * @return the result of applying the mapper function to this tuple
     * @throws E if the mapper function throws an exception during execution
     */
    @MayReturnNull
    public <U, E extends Exception> U map(final Throwables.Function<? super TP, U, E> mapper) throws E {
        return mapper.apply((TP) this);
    }

    /**
     * Tests this tuple against the given predicate and returns an Optional containing this tuple
     * if the predicate is satisfied, or an empty Optional otherwise.
     * <p>
     * This method provides a functional way to conditionally select tuples based on their contents.
     * The predicate receives this tuple as input and returns a boolean indicating whether the
     * condition is met. If the predicate returns {@code true}, this method returns an Optional
     * containing this tuple; if {@code false}, it returns an empty Optional.
     * </p>
     *
     * <p>
     * This is particularly useful in functional pipelines where you want to process tuples only
     * when certain conditions are met, enabling clean and expressive filtering logic.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Filter for positive values
     * IntTuple2 tuple = IntTuple.of(5, 10);
     * Optional<IntTuple2> positive = tuple.filter(t -> t._1 > 0 && t._2 > 0);
     * // positive.isPresent() = true
     *
     * // Filter for negative values
     * Optional<IntTuple2> negative = tuple.filter(t -> t._1 < 0 || t._2 < 0);
     * // negative.isPresent() = false
     *
     * // Filter with range validation
     * DoubleTuple2 coords = DoubleTuple.of(45.5, -122.6);
     * Optional<DoubleTuple2> validCoords = coords.filter(t ->
     *     t._1 >= -90 && t._1 <= 90 &&  // valid latitude
     *     t._2 >= -180 && t._2 <= 180   // valid longitude
     * );
     *
     * // Chain with other operations
     * IntTuple3 values = IntTuple.of(10, 20, 30);
     * String result = values.filter(t -> t._1 + t._2 + t._3 > 50)
     *                       .map(t -> "Sum is: " + (t._1 + t._2 + t._3))
     *                       .orElse("Sum too small");
     * // result = "Sum is: 60"
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param predicate the predicate to test this tuple against, must not be {@code null}
     * @return an {@link Optional} containing this tuple if the predicate returns {@code true},
     *         otherwise an empty Optional
     * @throws E if the predicate test throws an exception during execution
     */
    public <E extends Exception> Optional<TP> filter(final Throwables.Predicate<? super TP, E> predicate) throws E {
        return predicate.test((TP) this) ? Optional.of((TP) this) : Optional.empty();
    }

    /**
     * Wraps this tuple in an Optional container.
     * <p>
     * This is a convenience method that always returns a non-empty Optional containing this tuple.
     * Since tuples are immutable and non-null by design, the returned Optional is guaranteed to
     * be present (never empty). This method is useful when integrating with APIs or functional
     * pipelines that work with Optional values.
     * </p>
     *
     * <p>
     * This method is equivalent to {@code Optional.of(this)} and provides a fluent way to
     * start an Optional-based processing chain directly from the tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Simple wrapping
     * IntTuple2 tuple = IntTuple.of(1, 2);
     * Optional<IntTuple2> optional = tuple.toOptional();
     * // optional.isPresent() = true
     *
     * // Use in functional chains
     * IntTuple3 coords = IntTuple.of(10, 20, 30);
     * String description = coords.toOptional()
     *                            .filter(t -> t._1 > 0)
     *                            .map(t -> "Coordinates: " + t._1 + ", " + t._2 + ", " + t._3)
     *                            .orElse("Invalid coordinates");
     *
     * // Integrate with Optional-based APIs
     * DoubleTuple2 values = DoubleTuple.of(3.14, 2.71);
     * values.toOptional()
     *       .ifPresent(t -> processValues(t._1, t._2));
     * }</pre>
     *
     * @return an {@link Optional} containing this tuple, never empty
     */
    public Optional<TP> toOptional() {
        return Optional.of((TP) this);
    }
}