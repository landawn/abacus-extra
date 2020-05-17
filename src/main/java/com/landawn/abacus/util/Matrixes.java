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

import com.landawn.abacus.logging.Logger;
import com.landawn.abacus.logging.LoggerFactory;

public final class Matrixes {

    /** The Constant logger. */
    static final Logger logger = LoggerFactory.getLogger(Matrixes.class);

    static final ThreadLocal<ParallelEnabled> isParallelEnabled_TL = ThreadLocal.withInitial(() -> ParallelEnabled.DEFAULT);

    private Matrixes() {
        // singleton: utility class.
    }

    public static void setParallelEnabled(final ParallelEnabled flag) {
        N.checkArgNotNull(flag);

        isParallelEnabled_TL.set(flag);
    }

    public static ParallelEnabled getParallelEnabled() {
        return isParallelEnabled_TL.get();
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

}
