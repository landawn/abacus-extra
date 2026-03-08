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
 * Common base class for the immutable primitive tuple families in this package.
 *
 * <p>It provides the functional helpers shared by the concrete tuple types:
 * {@link #accept(Throwables.Consumer)}, {@link #map(Throwables.Function)},
 * {@link #filter(Throwables.Predicate)}, and {@link #toOptional()}.</p>
 *
 * @param <TP> the specific tuple type extending this class
 */
@com.landawn.abacus.annotation.Immutable
abstract class PrimitiveTuple<TP extends PrimitiveTuple<TP>> implements Immutable {

    /**
     * Returns the fixed number of elements stored by this tuple type.
     *
     * @return the tuple arity
     */
    public abstract int arity();

    /**
     * Performs the given action with this tuple instance.
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to perform
     * @throws NullPointerException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super TP, E> action) throws E {
        action.accept((TP) this);
    }

    /**
     * Applies the supplied mapper to this tuple and returns the result.
     *
     * @param <U> the type of the result produced by the mapping function
     * @param <E> the type of exception that may be thrown by the mapper
     * @param mapper the mapping function
     * @return the mapped value, which may be {@code null}
     * @throws NullPointerException if {@code mapper} is {@code null}
     * @throws E if the mapper throws an exception
     */
    @MayReturnNull
    public <U, E extends Exception> U map(final Throwables.Function<? super TP, U, E> mapper) throws E {
        return mapper.apply((TP) this);
    }

    /**
     * Returns this tuple wrapped in an {@link Optional} when the predicate matches.
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param predicate the predicate to test
     * @return an {@link Optional} containing this tuple if the predicate returns {@code true}; otherwise {@link Optional#empty()}
     * @throws NullPointerException if {@code predicate} is {@code null}
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> Optional<TP> filter(final Throwables.Predicate<? super TP, E> predicate) throws E {
        return predicate.test((TP) this) ? Optional.of((TP) this) : Optional.empty();
    }

    /**
     * Returns this tuple wrapped in a non-empty {@link Optional}.
     *
     * @return an {@link Optional} containing this tuple
     */
    public Optional<TP> toOptional() {
        return Optional.of((TP) this);
    }
}

