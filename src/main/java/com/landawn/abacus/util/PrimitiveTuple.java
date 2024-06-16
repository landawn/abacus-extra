/*
 * Copyright (C) 2020 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

import com.landawn.abacus.util.u.Optional;

@com.landawn.abacus.annotation.Immutable
abstract class PrimitiveTuple<TP extends PrimitiveTuple<TP>> implements Immutable {

    /**
     * 
     *
     * @return 
     */
    public abstract int arity();

    /**
     * 
     *
     * @param <E> 
     * @param action 
     * @throws E 
     */
    public <E extends Exception> void accept(Throwables.Consumer<? super TP, E> action) throws E {
        action.accept((TP) this);
    }

    /**
     * 
     *
     * @param <U> 
     * @param <E> 
     * @param mapper 
     * @return 
     * @throws E 
     */
    public <U, E extends Exception> U map(Throwables.Function<? super TP, U, E> mapper) throws E {
        return mapper.apply((TP) this);
    }

    /**
     * 
     *
     * @param <E> 
     * @param predicate 
     * @return 
     * @throws E 
     */
    public <E extends Exception> Optional<TP> filter(final Throwables.Predicate<? super TP, E> predicate) throws E {
        return predicate.test((TP) this) ? Optional.of((TP) this) : Optional.<TP> empty();
    }

    /**
     * 
     *
     * @return 
     */
    public Optional<TP> toOptional() {
        return Optional.of((TP) this);
    }
}
