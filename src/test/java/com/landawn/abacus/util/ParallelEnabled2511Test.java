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
 * Comprehensive unit tests for the ParallelEnabled enum.
 * This enum controls parallel processing behavior for matrix operations.
 * Tests verify enum values, methods, and their properties.
 */
@Tag("2511")
public class ParallelEnabled2511Test extends TestBase {

    // ============================================
    // Tests for enum values
    // ============================================

    @Test
    public void testEnumValues_ContainsYes() {
        ParallelEnabled[] values = ParallelEnabled.values();
        boolean containsYes = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.YES) {
                containsYes = true;
                break;
            }
        }

        assertTrue(containsYes, "ParallelEnabled should contain YES value");
    }

    @Test
    public void testEnumValues_ContainsNo() {
        ParallelEnabled[] values = ParallelEnabled.values();
        boolean containsNo = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.NO) {
                containsNo = true;
                break;
            }
        }

        assertTrue(containsNo, "ParallelEnabled should contain NO value");
    }

    @Test
    public void testEnumValues_ContainsDefault() {
        ParallelEnabled[] values = ParallelEnabled.values();
        boolean containsDefault = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.DEFAULT) {
                containsDefault = true;
                break;
            }
        }

        assertTrue(containsDefault, "ParallelEnabled should contain DEFAULT value");
    }

    @Test
    public void testEnumValues_HasExactlyThreeValues() {
        ParallelEnabled[] values = ParallelEnabled.values();

        assertEquals(3, values.length, "ParallelEnabled should have exactly 3 values");
    }

    @Test
    public void testEnumValues_NotNull() {
        ParallelEnabled[] values = ParallelEnabled.values();

        assertNotNull(values);
        for (ParallelEnabled value : values) {
            assertNotNull(value);
        }
    }

    @Test
    public void testEnumValues_OrderIsConsistent() {
        ParallelEnabled[] values1 = ParallelEnabled.values();
        ParallelEnabled[] values2 = ParallelEnabled.values();

        assertEquals(values1.length, values2.length);
        for (int i = 0; i < values1.length; i++) {
            assertEquals(values1[i], values2[i]);
        }
    }

    @Test
    public void testEnumValues_AllThreePresent() {
        ParallelEnabled[] values = ParallelEnabled.values();

        boolean hasYes = false;
        boolean hasNo = false;
        boolean hasDefault = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.YES)
                hasYes = true;
            if (value == ParallelEnabled.NO)
                hasNo = true;
            if (value == ParallelEnabled.DEFAULT)
                hasDefault = true;
        }

        assertTrue(hasYes && hasNo && hasDefault, "All three enum values should be present");
    }

    // ============================================
    // Tests for valueOf() method
    // ============================================

    @Test
    public void testValueOf_Yes() {
        ParallelEnabled result = ParallelEnabled.valueOf("YES");

        assertNotNull(result);
        assertEquals(ParallelEnabled.YES, result);
    }

    @Test
    public void testValueOf_No() {
        ParallelEnabled result = ParallelEnabled.valueOf("NO");

        assertNotNull(result);
        assertEquals(ParallelEnabled.NO, result);
    }

    @Test
    public void testValueOf_Default() {
        ParallelEnabled result = ParallelEnabled.valueOf("DEFAULT");

        assertNotNull(result);
        assertEquals(ParallelEnabled.DEFAULT, result);
    }

    @Test
    public void testValueOf_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("INVALID"));
    }

    @Test
    public void testValueOf_CaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("no"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("default"));
    }

    @Test
    public void testValueOf_Null() {
        assertThrows(NullPointerException.class, () -> ParallelEnabled.valueOf(null));
    }

    @Test
    public void testValueOf_EmptyString() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf(""));
    }

    @Test
    public void testValueOf_Whitespace() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf(" YES "));
    }

    @Test
    public void testValueOf_LowerCase() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("Yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("No"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("Default"));
    }

    @Test
    public void testValueOf_ReturnsSameInstance() {
        ParallelEnabled yes1 = ParallelEnabled.valueOf("YES");
        ParallelEnabled yes2 = ParallelEnabled.valueOf("YES");

        assertTrue(yes1 == yes2);
    }

    // ============================================
    // Tests for name() method
    // ============================================

    @Test
    public void testName_Yes() {
        String name = ParallelEnabled.YES.name();

        assertNotNull(name);
        assertEquals("YES", name);
    }

    @Test
    public void testName_No() {
        String name = ParallelEnabled.NO.name();

        assertNotNull(name);
        assertEquals("NO", name);
    }

    @Test
    public void testName_Default() {
        String name = ParallelEnabled.DEFAULT.name();

        assertNotNull(name);
        assertEquals("DEFAULT", name);
    }

    @Test
    public void testName_ConsistentWithValueOf() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertEquals(value, ParallelEnabled.valueOf(value.name()));
        }
    }

    @Test
    public void testName_NotEmpty() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertFalse(value.name().isEmpty());
        }
    }

    // ============================================
    // Tests for ordinal() method
    // ============================================

    @Test
    public void testOrdinal_Values() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal(), "Ordinal of " + values[i] + " should be " + i);
        }
    }

    @Test
    public void testOrdinal_Yes() {
        int ordinal = ParallelEnabled.YES.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3, "Ordinal should be between 0 and 2");
    }

    @Test
    public void testOrdinal_No() {
        int ordinal = ParallelEnabled.NO.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3, "Ordinal should be between 0 and 2");
    }

    @Test
    public void testOrdinal_Default() {
        int ordinal = ParallelEnabled.DEFAULT.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3, "Ordinal should be between 0 and 2");
    }

    @Test
    public void testOrdinal_UniqueValues() {
        int yesOrdinal = ParallelEnabled.YES.ordinal();
        int noOrdinal = ParallelEnabled.NO.ordinal();
        int defaultOrdinal = ParallelEnabled.DEFAULT.ordinal();

        // All ordinals should be different
        assertNotEquals(yesOrdinal, noOrdinal);
        assertNotEquals(yesOrdinal, defaultOrdinal);
        assertNotEquals(noOrdinal, defaultOrdinal);
    }

    @Test
    public void testOrdinal_NonNegative() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertTrue(value.ordinal() >= 0, "Ordinal should be non-negative");
        }
    }

    @Test
    public void testOrdinal_Sequential() {
        ParallelEnabled[] values = ParallelEnabled.values();
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    // ============================================
    // Tests for toString() method
    // ============================================

    @Test
    public void testToString_Yes() {
        String str = ParallelEnabled.YES.toString();

        assertNotNull(str);
        assertEquals("YES", str);
    }

    @Test
    public void testToString_No() {
        String str = ParallelEnabled.NO.toString();

        assertNotNull(str);
        assertEquals("NO", str);
    }

    @Test
    public void testToString_Default() {
        String str = ParallelEnabled.DEFAULT.toString();

        assertNotNull(str);
        assertEquals("DEFAULT", str);
    }

    @Test
    public void testToString_MatchesName() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertEquals(value.name(), value.toString());
        }
    }

    @Test
    public void testToString_NotNull() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertNotNull(value.toString());
        }
    }

    @Test
    public void testToString_NotEmpty() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertFalse(value.toString().isEmpty());
        }
    }

    // ============================================
    // Tests for equals() and identity
    // ============================================

    @Test
    public void testEquals_SameInstance() {
        assertTrue(ParallelEnabled.YES == ParallelEnabled.YES);
        assertTrue(ParallelEnabled.NO == ParallelEnabled.NO);
        assertTrue(ParallelEnabled.DEFAULT == ParallelEnabled.DEFAULT);
    }

    @Test
    public void testEquals_DifferentInstances() {
        assertTrue(ParallelEnabled.YES != ParallelEnabled.NO);
        assertTrue(ParallelEnabled.YES != ParallelEnabled.DEFAULT);
        assertTrue(ParallelEnabled.NO != ParallelEnabled.DEFAULT);
    }

    @Test
    public void testEquals_UsingEqualsMethod() {
        assertTrue(ParallelEnabled.YES.equals(ParallelEnabled.YES));
        assertTrue(ParallelEnabled.NO.equals(ParallelEnabled.NO));
        assertTrue(ParallelEnabled.DEFAULT.equals(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEquals_UsingEqualsMethod_DifferentValues() {
        assertFalse(ParallelEnabled.YES.equals(ParallelEnabled.NO));
        assertFalse(ParallelEnabled.YES.equals(ParallelEnabled.DEFAULT));
        assertFalse(ParallelEnabled.NO.equals(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEquals_WithNull() {
        assertFalse(ParallelEnabled.YES.equals(null));
        assertFalse(ParallelEnabled.NO.equals(null));
        assertFalse(ParallelEnabled.DEFAULT.equals(null));
    }

    @Test
    public void testEquals_WithDifferentType() {
        assertFalse(ParallelEnabled.YES.equals("YES"));
        assertFalse(ParallelEnabled.NO.equals("NO"));
        assertFalse(ParallelEnabled.DEFAULT.equals("DEFAULT"));
    }

    @Test
    public void testEquals_Reflexive() {
        // Reflexive: a.equals(a) is always true
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertTrue(value.equals(value));
        }
    }

    @Test
    public void testEquals_Symmetric() {
        // Symmetric: a.equals(b) == b.equals(a)
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.YES;

        assertEquals(a.equals(b), b.equals(a));
    }

    @Test
    public void testEquals_Transitive() {
        // Transitive: if a.equals(b) and b.equals(c), then a.equals(c)
        ParallelEnabled a = ParallelEnabled.DEFAULT;
        ParallelEnabled b = ParallelEnabled.valueOf("DEFAULT");
        ParallelEnabled c = ParallelEnabled.DEFAULT;

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void testEquals_Consistent() {
        // Multiple invocations of equals should return the same result
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.NO;

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
        ParallelEnabled value = ParallelEnabled.YES;
        String result = "";

        switch (value) {
            case YES:
                result = "parallel";
                break;
            case NO:
                result = "sequential";
                break;
            case DEFAULT:
                result = "default";
                break;
        }

        assertEquals("parallel", result);
    }

    @Test
    public void testSwitchStatement_No() {
        ParallelEnabled value = ParallelEnabled.NO;
        String result = "";

        switch (value) {
            case YES:
                result = "parallel";
                break;
            case NO:
                result = "sequential";
                break;
            case DEFAULT:
                result = "default";
                break;
        }

        assertEquals("sequential", result);
    }

    @Test
    public void testSwitchStatement_Default() {
        ParallelEnabled value = ParallelEnabled.DEFAULT;
        String result = "";

        switch (value) {
            case YES:
                result = "parallel";
                break;
            case NO:
                result = "sequential";
                break;
            case DEFAULT:
                result = "default";
                break;
        }

        assertEquals("default", result);
    }

    @Test
    public void testSwitchStatement_AllValues() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            String result = switch (value) {
                case YES -> "yes";
                case NO -> "no";
                case DEFAULT -> "default";
            };

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Test
    public void testSwitchStatement_ReturnValue() {
        ParallelEnabled value = ParallelEnabled.YES;

        boolean shouldParallelize = switch (value) {
            case YES -> true;
            case NO -> false;
            case DEFAULT -> true; // Assuming default enables parallelization
        };

        assertTrue(shouldParallelize);
    }

    // ============================================
    // Tests for hashCode() method
    // ============================================

    @Test
    public void testHashCode_Consistent() {
        // Multiple invocations should return the same hash code
        int hash1 = ParallelEnabled.YES.hashCode();
        int hash2 = ParallelEnabled.YES.hashCode();
        int hash3 = ParallelEnabled.YES.hashCode();

        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
    }

    @Test
    public void testHashCode_SameValuesSameHash() {
        ParallelEnabled yes1 = ParallelEnabled.YES;
        ParallelEnabled yes2 = ParallelEnabled.valueOf("YES");

        assertEquals(yes1.hashCode(), yes2.hashCode());
    }

    @Test
    public void testHashCode_DifferentValuesDifferentHash() {
        int yesHash = ParallelEnabled.YES.hashCode();
        int noHash = ParallelEnabled.NO.hashCode();
        int defaultHash = ParallelEnabled.DEFAULT.hashCode();

        // Different enum values should (typically) have different hash codes
        // Note: This is not guaranteed by the contract but highly likely
        assertTrue(yesHash != noHash || yesHash != defaultHash);
    }

    @Test
    public void testHashCode_EqualsContract() {
        // If two objects are equal, their hash codes must be equal
        ParallelEnabled a = ParallelEnabled.DEFAULT;
        ParallelEnabled b = ParallelEnabled.DEFAULT;

        if (a.equals(b)) {
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    @Test
    public void testHashCode_AllValuesHaveHashCode() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
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
        ParallelEnabled setting = ParallelEnabled.DEFAULT;

        assertNotNull(setting);
        assertEquals("DEFAULT", setting.name());
        assertEquals(ParallelEnabled.DEFAULT, setting);
    }

    @Test
    public void testEnumIteration() {
        // Verify all enum values can be iterated
        int count = 0;
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertNotNull(value);
            assertNotNull(value.name());
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testEnumComparison() {
        // Test enum comparison using ordinal
        assertTrue(ParallelEnabled.YES.ordinal() < ParallelEnabled.values().length);
        assertTrue(ParallelEnabled.NO.ordinal() < ParallelEnabled.values().length);
        assertTrue(ParallelEnabled.DEFAULT.ordinal() < ParallelEnabled.values().length);
    }

    @Test
    public void testEnumInCollection() {
        // Test that enum can be used in collections
        java.util.List<ParallelEnabled> list = new java.util.ArrayList<>();
        list.add(ParallelEnabled.YES);
        list.add(ParallelEnabled.NO);
        list.add(ParallelEnabled.DEFAULT);

        assertEquals(3, list.size());
        assertTrue(list.contains(ParallelEnabled.YES));
        assertTrue(list.contains(ParallelEnabled.NO));
        assertTrue(list.contains(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEnumInSet() {
        // Test that enum can be used in sets
        java.util.Set<ParallelEnabled> set = new java.util.HashSet<>();
        set.add(ParallelEnabled.YES);
        set.add(ParallelEnabled.NO);
        set.add(ParallelEnabled.DEFAULT);

        assertEquals(3, set.size());
    }

    @Test
    public void testEnumInMap() {
        // Test that enum can be used as map keys
        java.util.Map<ParallelEnabled, String> map = new java.util.HashMap<>();
        map.put(ParallelEnabled.YES, "Enable parallel processing");
        map.put(ParallelEnabled.NO, "Disable parallel processing");
        map.put(ParallelEnabled.DEFAULT, "Use default settings");

        assertEquals(3, map.size());
        assertEquals("Enable parallel processing", map.get(ParallelEnabled.YES));
        assertEquals("Disable parallel processing", map.get(ParallelEnabled.NO));
        assertEquals("Use default settings", map.get(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEnumInEnumSet() {
        // Test that enum works with EnumSet
        java.util.EnumSet<ParallelEnabled> enumSet = java.util.EnumSet.allOf(ParallelEnabled.class);

        assertEquals(3, enumSet.size());
        assertTrue(enumSet.contains(ParallelEnabled.YES));
        assertTrue(enumSet.contains(ParallelEnabled.NO));
        assertTrue(enumSet.contains(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEnumInEnumMap() {
        // Test that enum works with EnumMap
        java.util.EnumMap<ParallelEnabled, Integer> enumMap = new java.util.EnumMap<>(ParallelEnabled.class);
        enumMap.put(ParallelEnabled.YES, 1);
        enumMap.put(ParallelEnabled.NO, 0);
        enumMap.put(ParallelEnabled.DEFAULT, -1);

        assertEquals(3, enumMap.size());
        assertEquals(1, enumMap.get(ParallelEnabled.YES));
        assertEquals(0, enumMap.get(ParallelEnabled.NO));
        assertEquals(-1, enumMap.get(ParallelEnabled.DEFAULT));
    }

    // ============================================
    // Serialization and compareTo tests
    // ============================================

    @Test
    public void testCompareTo_SameValue() {
        ParallelEnabled yes1 = ParallelEnabled.YES;
        ParallelEnabled yes2 = ParallelEnabled.YES;

        assertEquals(0, yes1.compareTo(yes2));
    }

    @Test
    public void testCompareTo_DifferentValues() {
        ParallelEnabled[] values = ParallelEnabled.values();

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
        ParallelEnabled first = ParallelEnabled.values()[0];
        ParallelEnabled last = ParallelEnabled.values()[ParallelEnabled.values().length - 1];

        assertTrue(first.compareTo(last) < 0);
        assertTrue(last.compareTo(first) > 0);
    }

    @Test
    public void testCompareTo_Reflexive() {
        // a.compareTo(a) == 0
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertEquals(0, value.compareTo(value));
        }
    }

    @Test
    public void testCompareTo_Antisymmetric() {
        // If a.compareTo(b) > 0, then b.compareTo(a) < 0
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.NO;

        int ab = a.compareTo(b);
        int ba = b.compareTo(a);

        assertEquals(Integer.signum(ab), -Integer.signum(ba));
    }

    @Test
    public void testCompareTo_Transitive() {
        // If a < b and b < c, then a < c
        ParallelEnabled[] values = ParallelEnabled.values();

        if (values.length >= 3) {
            ParallelEnabled a = values[0];
            ParallelEnabled b = values[1];
            ParallelEnabled c = values[2];

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
        assertEquals(ParallelEnabled.YES, ParallelEnabled.valueOf("YES"));
        assertEquals(ParallelEnabled.NO, ParallelEnabled.valueOf("NO"));
        assertEquals(ParallelEnabled.DEFAULT, ParallelEnabled.valueOf("DEFAULT"));
    }

    @Test
    public void testEdgeCase_AllValuesAreDistinct() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    public void testEdgeCase_EnumConstantsArePublic() {
        // Verify that enum constants can be accessed directly
        assertNotNull(ParallelEnabled.YES);
        assertNotNull(ParallelEnabled.NO);
        assertNotNull(ParallelEnabled.DEFAULT);
    }

    @Test
    public void testEdgeCase_EnumIsNotInstantiable() {
        // Verify that there are only 3 instances
        assertEquals(3, ParallelEnabled.values().length);
    }

    // ============================================
    // Documentation and semantic tests
    // ============================================

    @Test
    public void testSemantics_YesRepresentsParallelEnabled() {
        // YES should represent enabling parallel processing
        ParallelEnabled yes = ParallelEnabled.YES;

        assertNotNull(yes);
        assertEquals("YES", yes.name());
    }

    @Test
    public void testSemantics_NoRepresentsParallelDisabled() {
        // NO should represent disabling parallel processing
        ParallelEnabled no = ParallelEnabled.NO;

        assertNotNull(no);
        assertEquals("NO", no.name());
    }

    @Test
    public void testSemantics_DefaultRepresentsDefaultBehavior() {
        // DEFAULT should represent default heuristics
        ParallelEnabled defaultValue = ParallelEnabled.DEFAULT;

        assertNotNull(defaultValue);
        assertEquals("DEFAULT", defaultValue.name());
    }

    @Test
    public void testSemantics_AllValuesHaveDistinctMeaning() {
        // Each enum value should represent a distinct choice
        ParallelEnabled yes = ParallelEnabled.YES;
        ParallelEnabled no = ParallelEnabled.NO;
        ParallelEnabled defaultValue = ParallelEnabled.DEFAULT;

        assertNotEquals(yes, no);
        assertNotEquals(yes, defaultValue);
        assertNotEquals(no, defaultValue);
    }
}
