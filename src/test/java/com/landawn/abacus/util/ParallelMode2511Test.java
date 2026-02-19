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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the ParallelMode enum.
 * This enum controls parallel processing behavior for matrix operations.
 * Tests verify enum values, methods, and their properties.
 */
@Tag("2511")
public class ParallelMode2511Test extends TestBase {

    // ============================================
    // Tests for enum values
    // ============================================

    @Test
    public void testEnumValues_ContainsYes() {
        ParallelMode[] values = ParallelMode.values();
        boolean containsYes = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.FORCE_ON) {
                containsYes = true;
                break;
            }
        }

        assertTrue(containsYes, "ParallelMode should contain FORCE_ON value");
    }

    @Test
    public void testEnumValues_ContainsNo() {
        ParallelMode[] values = ParallelMode.values();
        boolean containsNo = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.FORCE_OFF) {
                containsNo = true;
                break;
            }
        }

        assertTrue(containsNo, "ParallelMode should contain FORCE_OFF value");
    }

    @Test
    public void testEnumValues_ContainsDefault() {
        ParallelMode[] values = ParallelMode.values();
        boolean containsDefault = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.AUTO) {
                containsDefault = true;
                break;
            }
        }

        assertTrue(containsDefault, "ParallelMode should contain AUTO value");
    }

    @Test
    public void testEnumValues_HasExactlyThreeValues() {
        ParallelMode[] values = ParallelMode.values();

        assertEquals(3, values.length, "ParallelMode should have exactly 3 values");
    }

    @Test
    public void testEnumValues_NotNull() {
        ParallelMode[] values = ParallelMode.values();

        assertNotNull(values);
        for (ParallelMode value : values) {
            assertNotNull(value);
        }
    }

    @Test
    public void testEnumValues_OrderIsConsistent() {
        ParallelMode[] values1 = ParallelMode.values();
        ParallelMode[] values2 = ParallelMode.values();

        assertEquals(values1.length, values2.length);
        for (int i = 0; i < values1.length; i++) {
            assertEquals(values1[i], values2[i]);
        }
    }

    @Test
    public void testEnumValues_AllThreePresent() {
        ParallelMode[] values = ParallelMode.values();

        boolean hasYes = false;
        boolean hasNo = false;
        boolean hasDefault = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.FORCE_ON)
                hasYes = true;
            if (value == ParallelMode.FORCE_OFF)
                hasNo = true;
            if (value == ParallelMode.AUTO)
                hasDefault = true;
        }

        assertTrue(hasYes && hasNo && hasDefault, "All three enum values should be present");
    }

    // ============================================
    // Tests for valueOf() method
    // ============================================

    @Test
    public void testValueOf_Yes() {
        ParallelMode result = ParallelMode.valueOf("FORCE_ON");

        assertNotNull(result);
        assertEquals(ParallelMode.FORCE_ON, result);
    }

    @Test
    public void testValueOf_No() {
        ParallelMode result = ParallelMode.valueOf("FORCE_OFF");

        assertNotNull(result);
        assertEquals(ParallelMode.FORCE_OFF, result);
    }

    @Test
    public void testValueOf_Default() {
        ParallelMode result = ParallelMode.valueOf("AUTO");

        assertNotNull(result);
        assertEquals(ParallelMode.AUTO, result);
    }

    @Test
    public void testValueOf_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("INVALID"));
    }

    @Test
    public void testValueOf_CaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("no"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("default"));
    }

    @Test
    public void testValueOf_Null() {
        assertThrows(NullPointerException.class, () -> ParallelMode.valueOf(null));
    }

    @Test
    public void testValueOf_EmptyString() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf(""));
    }

    @Test
    public void testValueOf_Whitespace() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf(" FORCE_ON "));
    }

    @Test
    public void testValueOf_LowerCase() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("Yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("No"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("Default"));
    }

    @Test
    public void testValueOf_ReturnsSameInstance() {
        ParallelMode yes1 = ParallelMode.valueOf("FORCE_ON");
        ParallelMode yes2 = ParallelMode.valueOf("FORCE_ON");

        assertTrue(yes1 == yes2);
    }

    // ============================================
    // Tests for name() method
    // ============================================

    @Test
    public void testName_Yes() {
        String name = ParallelMode.FORCE_ON.name();

        assertNotNull(name);
        assertEquals("FORCE_ON", name);
    }

    @Test
    public void testName_No() {
        String name = ParallelMode.FORCE_OFF.name();

        assertNotNull(name);
        assertEquals("FORCE_OFF", name);
    }

    @Test
    public void testName_Default() {
        String name = ParallelMode.AUTO.name();

        assertNotNull(name);
        assertEquals("AUTO", name);
    }

    @Test
    public void testName_ConsistentWithValueOf() {
        for (ParallelMode value : ParallelMode.values()) {
            assertEquals(value, ParallelMode.valueOf(value.name()));
        }
    }

    @Test
    public void testName_NotEmpty() {
        for (ParallelMode value : ParallelMode.values()) {
            assertFalse(value.name().isEmpty());
        }
    }

    // ============================================
    // Tests for ordinal() method
    // ============================================

    @Test
    public void testOrdinal_Values() {
        ParallelMode[] values = ParallelMode.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal(), "Ordinal of " + values[i] + " should be " + i);
        }
    }

    @Test
    public void testOrdinal_Yes() {
        int ordinal = ParallelMode.FORCE_ON.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3, "Ordinal should be between 0 and 2");
    }

    @Test
    public void testOrdinal_No() {
        int ordinal = ParallelMode.FORCE_OFF.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3, "Ordinal should be between 0 and 2");
    }

    @Test
    public void testOrdinal_Default() {
        int ordinal = ParallelMode.AUTO.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3, "Ordinal should be between 0 and 2");
    }

    @Test
    public void testOrdinal_UniqueValues() {
        int yesOrdinal = ParallelMode.FORCE_ON.ordinal();
        int noOrdinal = ParallelMode.FORCE_OFF.ordinal();
        int defaultOrdinal = ParallelMode.AUTO.ordinal();

        // All ordinals should be different
        assertNotEquals(yesOrdinal, noOrdinal);
        assertNotEquals(yesOrdinal, defaultOrdinal);
        assertNotEquals(noOrdinal, defaultOrdinal);
    }

    @Test
    public void testOrdinal_NonNegative() {
        for (ParallelMode value : ParallelMode.values()) {
            assertTrue(value.ordinal() >= 0, "Ordinal should be non-negative");
        }
    }

    @Test
    public void testOrdinal_Sequential() {
        ParallelMode[] values = ParallelMode.values();
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    // ============================================
    // Tests for toString() method
    // ============================================

    @Test
    public void testToString_Yes() {
        String str = ParallelMode.FORCE_ON.toString();

        assertNotNull(str);
        assertEquals("FORCE_ON", str);
    }

    @Test
    public void testToString_No() {
        String str = ParallelMode.FORCE_OFF.toString();

        assertNotNull(str);
        assertEquals("FORCE_OFF", str);
    }

    @Test
    public void testToString_Default() {
        String str = ParallelMode.AUTO.toString();

        assertNotNull(str);
        assertEquals("AUTO", str);
    }

    @Test
    public void testToString_MatchesName() {
        for (ParallelMode value : ParallelMode.values()) {
            assertEquals(value.name(), value.toString());
        }
    }

    @Test
    public void testToString_NotNull() {
        for (ParallelMode value : ParallelMode.values()) {
            assertNotNull(value.toString());
        }
    }

    @Test
    public void testToString_NotEmpty() {
        for (ParallelMode value : ParallelMode.values()) {
            assertFalse(value.toString().isEmpty());
        }
    }

    // ============================================
    // Tests for equals() and identity
    // ============================================

    @Test
    public void testEquals_SameInstance() {
        assertTrue(ParallelMode.FORCE_ON == ParallelMode.FORCE_ON);
        assertTrue(ParallelMode.FORCE_OFF == ParallelMode.FORCE_OFF);
        assertTrue(ParallelMode.AUTO == ParallelMode.AUTO);
    }

    @Test
    public void testEquals_DifferentInstances() {
        assertTrue(ParallelMode.FORCE_ON != ParallelMode.FORCE_OFF);
        assertTrue(ParallelMode.FORCE_ON != ParallelMode.AUTO);
        assertTrue(ParallelMode.FORCE_OFF != ParallelMode.AUTO);
    }

    @Test
    public void testEquals_UsingEqualsMethod() {
        assertTrue(ParallelMode.FORCE_ON.equals(ParallelMode.FORCE_ON));
        assertTrue(ParallelMode.FORCE_OFF.equals(ParallelMode.FORCE_OFF));
        assertTrue(ParallelMode.AUTO.equals(ParallelMode.AUTO));
    }

    @Test
    public void testEquals_UsingEqualsMethod_DifferentValues() {
        assertFalse(ParallelMode.FORCE_ON.equals(ParallelMode.FORCE_OFF));
        assertFalse(ParallelMode.FORCE_ON.equals(ParallelMode.AUTO));
        assertFalse(ParallelMode.FORCE_OFF.equals(ParallelMode.AUTO));
    }

    @Test
    public void testEquals_WithNull() {
        assertFalse(ParallelMode.FORCE_ON.equals(null));
        assertFalse(ParallelMode.FORCE_OFF.equals(null));
        assertFalse(ParallelMode.AUTO.equals(null));
    }

    @Test
    public void testEquals_WithDifferentType() {
        assertFalse(ParallelMode.FORCE_ON.equals("FORCE_ON"));
        assertFalse(ParallelMode.FORCE_OFF.equals("FORCE_OFF"));
        assertFalse(ParallelMode.AUTO.equals("AUTO"));
    }

    @Test
    public void testEquals_Reflexive() {
        // Reflexive: a.equals(a) is always true
        for (ParallelMode value : ParallelMode.values()) {
            assertTrue(value.equals(value));
        }
    }

    @Test
    public void testEquals_Symmetric() {
        // Symmetric: a.equals(b) == b.equals(a)
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_ON;

        assertEquals(a.equals(b), b.equals(a));
    }

    @Test
    public void testEquals_Transitive() {
        // Transitive: if a.equals(b) and b.equals(c), then a.equals(c)
        ParallelMode a = ParallelMode.AUTO;
        ParallelMode b = ParallelMode.valueOf("AUTO");
        ParallelMode c = ParallelMode.AUTO;

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void testEquals_Consistent() {
        // Multiple invocations of equals should return the same result
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_OFF;

        boolean result1 = a.equals(b);
        boolean result2 = a.equals(b);
        boolean result3 = a.equals(b);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    // ============================================
    // Tests for switch statements
    // ============================================

    @Test
    public void testSwitchStatement_Yes() {
        ParallelMode value = ParallelMode.FORCE_ON;
        String result = "";

        switch (value) {
            case FORCE_ON:
                result = "parallel";
                break;
            case FORCE_OFF:
                result = "sequential";
                break;
            case AUTO:
                result = "default";
                break;
        }

        assertEquals("parallel", result);
    }

    @Test
    public void testSwitchStatement_No() {
        ParallelMode value = ParallelMode.FORCE_OFF;
        String result = "";

        switch (value) {
            case FORCE_ON:
                result = "parallel";
                break;
            case FORCE_OFF:
                result = "sequential";
                break;
            case AUTO:
                result = "default";
                break;
        }

        assertEquals("sequential", result);
    }

    @Test
    public void testSwitchStatement_Default() {
        ParallelMode value = ParallelMode.AUTO;
        String result = "";

        switch (value) {
            case FORCE_ON:
                result = "parallel";
                break;
            case FORCE_OFF:
                result = "sequential";
                break;
            case AUTO:
                result = "default";
                break;
        }

        assertEquals("default", result);
    }

    @Test
    public void testSwitchStatement_AllValues() {
        for (ParallelMode value : ParallelMode.values()) {
            String result = switch (value) {
                case FORCE_ON -> "yes";
                case FORCE_OFF -> "no";
                case AUTO -> "default";
            };

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Test
    public void testSwitchStatement_ReturnValue() {
        ParallelMode value = ParallelMode.FORCE_ON;

        boolean shouldParallelize = switch (value) {
            case FORCE_ON -> true;
            case FORCE_OFF -> false;
            case AUTO -> true; // Assuming default enables parallelization
        };

        assertTrue(shouldParallelize);
    }

    // ============================================
    // Tests for hashCode() method
    // ============================================

    @Test
    public void testHashCode_Consistent() {
        // Multiple invocations should return the same hash code
        int hash1 = ParallelMode.FORCE_ON.hashCode();
        int hash2 = ParallelMode.FORCE_ON.hashCode();
        int hash3 = ParallelMode.FORCE_ON.hashCode();

        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
    }

    @Test
    public void testHashCode_SameValuesSameHash() {
        ParallelMode yes1 = ParallelMode.FORCE_ON;
        ParallelMode yes2 = ParallelMode.valueOf("FORCE_ON");

        assertEquals(yes1.hashCode(), yes2.hashCode());
    }

    @Test
    public void testHashCode_DifferentValuesDifferentHash() {
        int yesHash = ParallelMode.FORCE_ON.hashCode();
        int noHash = ParallelMode.FORCE_OFF.hashCode();
        int defaultHash = ParallelMode.AUTO.hashCode();

        // Different enum values should (typically) have different hash codes
        // Note: This is not guaranteed by the contract but highly likely
        assertTrue(yesHash != noHash || yesHash != defaultHash);
    }

    @Test
    public void testHashCode_EqualsContract() {
        // If two objects are equal, their hash codes must be equal
        ParallelMode a = ParallelMode.AUTO;
        ParallelMode b = ParallelMode.AUTO;

        if (a.equals(b)) {
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    @Test
    public void testHashCode_AllValuesHaveHashCode() {
        for (ParallelMode value : ParallelMode.values()) {
            // Just verify hashCode() doesn't throw an exception
            int hash = value.hashCode();
            // Hash code can be any integer value
            assertNotNull(Integer.valueOf(hash));
        }
    }

    // ============================================
    // Integration tests
    // ============================================

    @Test
    public void testEnumUsagePattern() {
        // Simulate typical usage pattern
        ParallelMode setting = ParallelMode.AUTO;

        assertNotNull(setting);
        assertEquals("AUTO", setting.name());
        assertEquals(ParallelMode.AUTO, setting);
    }

    @Test
    public void testEnumIteration() {
        // Verify all enum values can be iterated
        int count = 0;
        for (ParallelMode value : ParallelMode.values()) {
            assertNotNull(value);
            assertNotNull(value.name());
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testEnumComparison() {
        // Test enum comparison using ordinal
        assertTrue(ParallelMode.FORCE_ON.ordinal() < ParallelMode.values().length);
        assertTrue(ParallelMode.FORCE_OFF.ordinal() < ParallelMode.values().length);
        assertTrue(ParallelMode.AUTO.ordinal() < ParallelMode.values().length);
    }

    @Test
    public void testEnumInCollection() {
        // Test that enum can be used in collections
        java.util.List<ParallelMode> list = new java.util.ArrayList<>();
        list.add(ParallelMode.FORCE_ON);
        list.add(ParallelMode.FORCE_OFF);
        list.add(ParallelMode.AUTO);

        assertEquals(3, list.size());
        assertTrue(list.contains(ParallelMode.FORCE_ON));
        assertTrue(list.contains(ParallelMode.FORCE_OFF));
        assertTrue(list.contains(ParallelMode.AUTO));
    }

    @Test
    public void testEnumInSet() {
        // Test that enum can be used in sets
        java.util.Set<ParallelMode> set = new java.util.HashSet<>();
        set.add(ParallelMode.FORCE_ON);
        set.add(ParallelMode.FORCE_OFF);
        set.add(ParallelMode.AUTO);

        assertEquals(3, set.size());
    }

    @Test
    public void testEnumInMap() {
        // Test that enum can be used as map keys
        java.util.Map<ParallelMode, String> map = new java.util.HashMap<>();
        map.put(ParallelMode.FORCE_ON, "Enable parallel processing");
        map.put(ParallelMode.FORCE_OFF, "Disable parallel processing");
        map.put(ParallelMode.AUTO, "Use default settings");

        assertEquals(3, map.size());
        assertEquals("Enable parallel processing", map.get(ParallelMode.FORCE_ON));
        assertEquals("Disable parallel processing", map.get(ParallelMode.FORCE_OFF));
        assertEquals("Use default settings", map.get(ParallelMode.AUTO));
    }

    @Test
    public void testEnumInEnumSet() {
        // Test that enum works with EnumSet
        java.util.EnumSet<ParallelMode> enumSet = java.util.EnumSet.allOf(ParallelMode.class);

        assertEquals(3, enumSet.size());
        assertTrue(enumSet.contains(ParallelMode.FORCE_ON));
        assertTrue(enumSet.contains(ParallelMode.FORCE_OFF));
        assertTrue(enumSet.contains(ParallelMode.AUTO));
    }

    @Test
    public void testEnumInEnumMap() {
        // Test that enum works with EnumMap
        java.util.EnumMap<ParallelMode, Integer> enumMap = new java.util.EnumMap<>(ParallelMode.class);
        enumMap.put(ParallelMode.FORCE_ON, 1);
        enumMap.put(ParallelMode.FORCE_OFF, 0);
        enumMap.put(ParallelMode.AUTO, -1);

        assertEquals(3, enumMap.size());
        assertEquals(1, enumMap.get(ParallelMode.FORCE_ON));
        assertEquals(0, enumMap.get(ParallelMode.FORCE_OFF));
        assertEquals(-1, enumMap.get(ParallelMode.AUTO));
    }

    // ============================================
    // Serialization and compareTo tests
    // ============================================

    @Test
    public void testCompareTo_SameValue() {
        ParallelMode yes1 = ParallelMode.FORCE_ON;
        ParallelMode yes2 = ParallelMode.FORCE_ON;

        assertEquals(0, yes1.compareTo(yes2));
    }

    @Test
    public void testCompareTo_DifferentValues() {
        ParallelMode[] values = ParallelMode.values();

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                int comparison = values[i].compareTo(values[j]);

                if (i < j) {
                    assertTrue(comparison < 0);
                } else if (i > j) {
                    assertTrue(comparison > 0);
                } else {
                    assertEquals(0, comparison);
                }
            }
        }
    }

    @Test
    public void testCompareTo_OrderingByOrdinal() {
        ParallelMode first = ParallelMode.values()[0];
        ParallelMode last = ParallelMode.values()[ParallelMode.values().length - 1];

        assertTrue(first.compareTo(last) < 0);
        assertTrue(last.compareTo(first) > 0);
    }

    @Test
    public void testCompareTo_Reflexive() {
        // a.compareTo(a) == 0
        for (ParallelMode value : ParallelMode.values()) {
            assertEquals(0, value.compareTo(value));
        }
    }

    @Test
    public void testCompareTo_Antisymmetric() {
        // If a.compareTo(b) > 0, then b.compareTo(a) < 0
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_OFF;

        int ab = a.compareTo(b);
        int ba = b.compareTo(a);

        assertEquals(Integer.signum(ab), -Integer.signum(ba));
    }

    @Test
    public void testCompareTo_Transitive() {
        // If a < b and b < c, then a < c
        ParallelMode[] values = ParallelMode.values();

        if (values.length >= 3) {
            ParallelMode a = values[0];
            ParallelMode b = values[1];
            ParallelMode c = values[2];

            if (a.compareTo(b) < 0 && b.compareTo(c) < 0) {
                assertTrue(a.compareTo(c) < 0);
            }
        }
    }

    // ============================================
    // Edge cases and boundary tests
    // ============================================

    @Test
    public void testEdgeCase_ValueOfWithExactName() {
        // Verify valueOf works with exact names
        assertEquals(ParallelMode.FORCE_ON, ParallelMode.valueOf("FORCE_ON"));
        assertEquals(ParallelMode.FORCE_OFF, ParallelMode.valueOf("FORCE_OFF"));
        assertEquals(ParallelMode.AUTO, ParallelMode.valueOf("AUTO"));
    }

    @Test
    public void testEdgeCase_AllValuesAreDistinct() {
        ParallelMode[] values = ParallelMode.values();

        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    public void testEdgeCase_EnumConstantsArePublic() {
        // Verify that enum constants can be accessed directly
        assertNotNull(ParallelMode.FORCE_ON);
        assertNotNull(ParallelMode.FORCE_OFF);
        assertNotNull(ParallelMode.AUTO);
    }

    @Test
    public void testEdgeCase_EnumIsNotInstantiable() {
        // Verify that there are only 3 instances
        assertEquals(3, ParallelMode.values().length);
    }

    // ============================================
    // Documentation and semantic tests
    // ============================================

    @Test
    public void testSemantics_YesRepresentsParallelMode() {
        // FORCE_ON should represent enabling parallel processing
        ParallelMode yes = ParallelMode.FORCE_ON;

        assertNotNull(yes);
        assertEquals("FORCE_ON", yes.name());
    }

    @Test
    public void testSemantics_NoRepresentsParallelDisabled() {
        // FORCE_OFF should represent disabling parallel processing
        ParallelMode no = ParallelMode.FORCE_OFF;

        assertNotNull(no);
        assertEquals("FORCE_OFF", no.name());
    }

    @Test
    public void testSemantics_DefaultRepresentsDefaultBehavior() {
        // AUTO should represent default heuristics
        ParallelMode defaultValue = ParallelMode.AUTO;

        assertNotNull(defaultValue);
        assertEquals("AUTO", defaultValue.name());
    }

    @Test
    public void testSemantics_AllValuesHaveDistinctMeaning() {
        // Each enum value should represent a distinct choice
        ParallelMode yes = ParallelMode.FORCE_ON;
        ParallelMode no = ParallelMode.FORCE_OFF;
        ParallelMode defaultValue = ParallelMode.AUTO;

        assertNotEquals(yes, no);
        assertNotEquals(yes, defaultValue);
        assertNotEquals(no, defaultValue);
    }
}
