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

import com.landawn.abacus.util.u.Optional;

/**
 * Abstract base class for primitive tuple implementations.
 * <p>
 * This class provides common functionality for tuples containing primitive values,
 * including functional operations like map, filter, and accept. All primitive tuple
 * implementations extend this class and are immutable by design.
 * </p>
 *
 * <p>Example usage:</p>
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
     * Returns the number of elements in this tuple.
     * <p>
     * The arity represents the size of the tuple. For example, a Tuple3 has an arity of 3.
     * </p>
     *
     * @return the number of elements in this tuple
     */
    public abstract int arity();

    /**
     * Performs the given action on this tuple.
     * <p>
     * This method allows performing side effects on the tuple without returning a value.
     * </p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntTuple2 tuple = IntTuple.of(10, 20);
     * tuple.accept(t -> System.out.println("Sum: " + (t._1 + t._2)));
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param action the action to be performed on this tuple
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super TP, E> action) throws E {
        action.accept((TP) this);
    }

    /**
     * Applies the given mapping function to this tuple and returns the result.
     * <p>
     * This method transforms the tuple into a value of a different type using the provided mapper function.
     * </p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntTuple2 tuple = IntTuple.of(3, 4);
     * Double distance = tuple.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
     * }</pre>
     *
     * @param <U> the type of the result
     * @param <E> the type of exception that may be thrown
     * @param mapper the mapping function to apply to this tuple
     * @return the result of applying the mapper function
     * @throws E if the mapper function throws an exception
     */
    public <U, E extends Exception> U map(final Throwables.Function<? super TP, U, E> mapper) throws E {
        return mapper.apply((TP) this);
    }

    /**
     * Returns an Optional containing this tuple if it matches the given predicate, otherwise returns an empty Optional.
     * <p>
     * This method is useful for conditional processing of tuples based on their content.
     * </p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntTuple2 tuple = IntTuple.of(5, 10);
     * Optional<IntTuple2> positive = tuple.filter(t -> t._1 > 0 && t._2 > 0);
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param predicate the predicate to test this tuple against
     * @return an Optional containing this tuple if the predicate is satisfied, otherwise empty Optional
     * @throws E if the predicate test throws an exception
     */
    public <E extends Exception> Optional<TP> filter(final Throwables.Predicate<? super TP, E> predicate) throws E {
        return predicate.test((TP) this) ? Optional.of((TP) this) : Optional.empty();
    }

    /**
     * Wraps this tuple in an Optional.
     * <p>
     * This is a convenience method that always returns a non-empty Optional containing this tuple.
     * </p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntTuple2 tuple = IntTuple.of(1, 2);
     * Optional<IntTuple2> optional = tuple.toOptional();
     * }</pre>
     *
     * @return an Optional containing this tuple
     */
    public Optional<TP> toOptional() {
        return Optional.of((TP) this);
    }
}