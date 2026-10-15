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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

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

    static Stream<Class<?>> printableTypes() {
        return Stream.concat(primitiveTypes(), Stream.of(Object.class));
    }

    static Stream<Arguments> primitiveArrayCases() {
        return primitiveTypes().flatMap(type -> IntStream.rangeClosed(1, 3).mapToObj(dimensions -> Arguments.of(type, dimensions)));
    }

    static Stream<Arguments> nestedPrimitiveArrayCases() {
        return primitiveTypes().flatMap(type -> IntStream.rangeClosed(2, 3).mapToObj(dimensions -> Arguments.of(type, dimensions)));
    }

    static Class<?> arrayType(Class<?> elementType, int dimensions) {
        return Array.newInstance(elementType, new int[dimensions]).getClass();
    }

    static Object primitiveValue(Class<?> type, int value) {
        return switch (type.getName()) {
            case "boolean" -> value % 2 != 0;
            case "char" -> (char) ('a' + value);
            case "byte" -> (byte) value;
            case "short" -> (short) value;
            case "long" -> (long) value;
            case "float" -> (float) value;
            case "double" -> (double) value;
            default -> value;
        };
    }

    static Object primitiveArray(Class<?> type, int... values) {
        Object result = Array.newInstance(type, values.length);
        for (int i = 0; i < values.length; i++) {
            Array.set(result, i, primitiveValue(type, values[i]));
        }
        return result;
    }

    static Object nestedArray(Class<?> elementType, int dimensions, Object... children) {
        Object result = Array.newInstance(arrayType(elementType, dimensions - 1), children.length);
        for (int i = 0; i < children.length; i++) {
            Array.set(result, i, children[i]);
        }
        return result;
    }

    static Method arrayMethod(String name, Class<?> elementType, int dimensions, int parameterCount) {
        Class<?> inputType = arrayType(elementType, dimensions);
        return Stream.of(Arrays.class.getMethods())
                .filter(method -> method.getName().equals(name) && method.getParameterCount() == parameterCount && method.getParameterTypes()[0] == inputType)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing overload: " + name + "(" + inputType.getTypeName() + ")"));
    }

    static Object invokeArrayMethod(Method method, Object... arguments) throws Throwable {
        try {
            return method.invoke(null, arguments);
        } catch (InvocationTargetException failure) {
            throw failure.getCause();
        }
    }

    static Object callback(Class<?> functionalType, Function<Object[], Object> action) {
        return Proxy.newProxyInstance(functionalType.getClassLoader(), new Class<?>[] { functionalType }, (proxy, method, arguments) -> {
            if (method.getDeclaringClass() == Object.class) {
                return switch (method.getName()) {
                    case "equals" -> proxy == arguments[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    default -> "test callback for " + functionalType.getSimpleName();
                };
            }
            return action.apply(arguments);
        });
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
