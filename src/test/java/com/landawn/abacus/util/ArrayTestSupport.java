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
package com.landawn.abacus.util;

import java.util.stream.Stream;

/** Small deterministic fixtures shared by array-operation tests. */
final class ArrayTestSupport {
    private ArrayTestSupport() {
    }

    static final int ITERATIONS = 500;

    static final class Value {
    }

    static Stream<Class<?>> primitiveTypes() {
        return Stream.of(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class);
    }

    static Object newArray(final Class<?> type, final int length) {
        return java.lang.reflect.Array.newInstance(type, length);
    }

    static int[] randomIntArray(final java.util.Random r, final int len) {
        final int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            // Keep values small so sums/zips can never overflow int.
            a[i] = r.nextInt(2001) - 1000;
        }
        return a;
    }
}
