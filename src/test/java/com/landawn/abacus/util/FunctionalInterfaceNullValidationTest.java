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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

class FunctionalInterfaceNullValidationTest extends TestBase {

    private static final List<Class<?>> SOURCE_TYPES = sourceTypes();

    @Test
    public void testEveryPublicMethodRejectsNullFunctionalInterfaceParameters() throws Exception {
        int validationCount = 0;

        for (final Class<?> sourceType : SOURCE_TYPES) {
            for (final Method method : sourceType.getDeclaredMethods()) {
                if (!Modifier.isPublic(method.getModifiers()) || method.isBridge() || method.isSynthetic()) {
                    continue;
                }

                final Class<?>[] parameterTypes = method.getParameterTypes();
                for (int parameterIndex = 0; parameterIndex < parameterTypes.length; parameterIndex++) {
                    if (!parameterTypes[parameterIndex].isAnnotationPresent(FunctionalInterface.class)) {
                        continue;
                    }

                    final Object[] arguments = argumentsFor(parameterTypes);
                    arguments[parameterIndex] = null;

                    final IllegalArgumentException error = invokeExpectingIllegalArgumentException(method, arguments);
                    final String parameterName = functionalParameterName(method);
                    assertEquals("'" + parameterName + "' cannot be null", error.getMessage(), method.toGenericString());
                    validationCount++;
                }
            }
        }

        assertEquals(378, validationCount, "Every public source method with a functional-interface parameter must be exercised");
    }

    private static IllegalArgumentException invokeExpectingIllegalArgumentException(final Method method, final Object[] arguments) throws Exception {
        final Object receiver = Modifier.isStatic(method.getModifiers()) ? null : newReceiver(method.getDeclaringClass());

        try {
            method.invoke(receiver, arguments);
            return fail("Expected IllegalArgumentException from " + method.toGenericString());
        } catch (final InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException,
                    () -> method.toGenericString() + " threw " + e.getCause().getClass().getName() + " instead of IllegalArgumentException");
            return (IllegalArgumentException) e.getCause();
        }
    }

    private static Object newReceiver(final Class<?> declaringClass) throws Exception {
        for (final Class<?> sourceType : SOURCE_TYPES) {
            if (!Modifier.isAbstract(sourceType.getModifiers()) && declaringClass.isAssignableFrom(sourceType)) {
                final Constructor<?> constructor = java.util.Arrays.stream(sourceType.getDeclaredConstructors())
                        .min(Comparator.comparingInt(Constructor::getParameterCount))
                        .orElseThrow();
                constructor.setAccessible(true);
                return constructor.newInstance(argumentsFor(constructor.getParameterTypes()));
            }
        }

        throw new AssertionError("No concrete receiver found for " + declaringClass.getName());
    }

    private static Object[] argumentsFor(final Class<?>[] parameterTypes) {
        final Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = defaultValue(parameterTypes[i]);
        }

        return arguments;
    }

    private static Object defaultValue(final Class<?> type) {
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        } else if (type == boolean.class) {
            return false;
        } else if (type == char.class) {
            return (char) 0;
        } else if (type == byte.class) {
            return (byte) 0;
        } else if (type == short.class) {
            return (short) 0;
        } else if (type == int.class) {
            return 0;
        } else if (type == long.class) {
            return 0L;
        } else if (type == float.class) {
            return 0F;
        } else if (type == double.class) {
            return 0D;
        } else if (type == Class.class) {
            return Object.class;
        } else if (type == String.class) {
            return "";
        } else if (type.isEnum()) {
            return type.getEnumConstants()[0];
        }

        return null;
    }

    private static String functionalParameterName(final Method method) {
        return switch (method.getName()) {
            case "updateAll" -> "operator";
            case "replaceIf", "filter" -> "predicate";
            case "map", "mapToBoolean", "mapToChar", "mapToByte", "mapToShort", "mapToInt", "mapToLong", "mapToFloat", "mapToDouble", "mapToObj" -> "mapper";
            case "zip" -> "zipFunction";
            default -> "action";
        };
    }

    private static List<Class<?>> sourceTypes() {
        final List<Class<?>> result = new ArrayList<>();
        addSourceType(Arrays.class, result);
        addSourceType(BooleanTuple.class, result);
        addSourceType(ByteTuple.class, result);
        addSourceType(CharTuple.class, result);
        addSourceType(DoubleTuple.class, result);
        addSourceType(FloatTuple.class, result);
        addSourceType(ImmutableIntArray.class, result);
        addSourceType(IntTuple.class, result);
        addSourceType(LongTuple.class, result);
        addSourceType(Points.class, result);
        addSourceType(PrimitiveTuple.class, result);
        addSourceType(ShortTuple.class, result);
        return result;
    }

    private static void addSourceType(final Class<?> sourceType, final List<Class<?>> result) {
        result.add(sourceType);

        for (final Class<?> declaredClass : sourceType.getDeclaredClasses()) {
            addSourceType(declaredClass, result);
        }
    }
}
