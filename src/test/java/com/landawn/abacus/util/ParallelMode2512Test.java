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
 * Comprehensive unit tests for ParallelMode enum.
 * Tests all enum constants (FORCE_ON, FORCE_OFF, AUTO) and all inherited methods from Enum class.
 * Covers: values(), valueOf(), name(), ordinal(), toString(), equals(), hashCode(), compareTo().
 */
@Tag("2512")
public class ParallelMode2512Test extends TestBase {

    // ============================================
    // Tests for enum values() method
    // ============================================

    @Test
    public void test_values_returnsAllEnumConstants() {
        ParallelMode[] values = ParallelMode.values();

        assertNotNull(values);
        assertEquals(3, values.length);
    }

    @Test
    public void test_values_containsYes() {
        ParallelMode[] values = ParallelMode.values();
        boolean containsYes = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.FORCE_ON) {
                containsYes = true;
                break;
            }
        }

        assertTrue(containsYes);
    }

    @Test
    public void test_values_containsNo() {
        ParallelMode[] values = ParallelMode.values();
        boolean containsNo = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.FORCE_OFF) {
                containsNo = true;
                break;
            }
        }

        assertTrue(containsNo);
    }

    @Test
    public void test_values_containsDefault() {
        ParallelMode[] values = ParallelMode.values();
        boolean containsDefault = false;

        for (ParallelMode value : values) {
            if (value == ParallelMode.AUTO) {
                containsDefault = true;
                break;
            }
        }

        assertTrue(containsDefault);
    }

    @Test
    public void test_values_allThreePresent() {
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

        assertTrue(hasYes && hasNo && hasDefault);
    }

    @Test
    public void test_values_orderConsistent() {
        ParallelMode[] values1 = ParallelMode.values();
        ParallelMode[] values2 = ParallelMode.values();

        assertEquals(values1.length, values2.length);
        for (int i = 0; i < values1.length; i++) {
            assertEquals(values1[i], values2[i]);
        }
    }

    @Test
    public void test_values_notNull() {
        ParallelMode[] values = ParallelMode.values();

        for (ParallelMode value : values) {
            assertNotNull(value);
        }
    }

    // ============================================
    // Tests for valueOf() method
    // ============================================

    @Test
    public void test_valueOf_yes() {
        ParallelMode result = ParallelMode.valueOf("FORCE_ON");

        assertNotNull(result);
        assertEquals(ParallelMode.FORCE_ON, result);
    }

    @Test
    public void test_valueOf_no() {
        ParallelMode result = ParallelMode.valueOf("FORCE_OFF");

        assertNotNull(result);
        assertEquals(ParallelMode.FORCE_OFF, result);
    }

    @Test
    public void test_valueOf_default() {
        ParallelMode result = ParallelMode.valueOf("AUTO");

        assertNotNull(result);
        assertEquals(ParallelMode.AUTO, result);
    }

    @Test
    public void test_valueOf_invalidValue() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("INVALID"));
    }

    @Test
    public void test_valueOf_caseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("no"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("default"));
    }

    @Test
    public void test_valueOf_null() {
        assertThrows(NullPointerException.class, () -> ParallelMode.valueOf(null));
    }

    @Test
    public void test_valueOf_emptyString() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf(""));
    }

    @Test
    public void test_valueOf_whitespace() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf(" FORCE_ON "));
    }

    @Test
    public void test_valueOf_mixedCase() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("Yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("No"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("Default"));
    }

    @Test
    public void test_valueOf_returnsSameInstance() {
        ParallelMode yes1 = ParallelMode.valueOf("FORCE_ON");
        ParallelMode yes2 = ParallelMode.valueOf("FORCE_ON");

        assertTrue(yes1 == yes2);
    }

    // ============================================
    // Tests for name() method
    // ============================================

    @Test
    public void test_name_yes() {
        String name = ParallelMode.FORCE_ON.name();

        assertNotNull(name);
        assertEquals("FORCE_ON", name);
    }

    @Test
    public void test_name_no() {
        String name = ParallelMode.FORCE_OFF.name();

        assertNotNull(name);
        assertEquals("FORCE_OFF", name);
    }

    @Test
    public void test_name_default() {
        String name = ParallelMode.AUTO.name();

        assertNotNull(name);
        assertEquals("AUTO", name);
    }

    @Test
    public void test_name_consistentWithValueOf() {
        for (ParallelMode value : ParallelMode.values()) {
            assertEquals(value, ParallelMode.valueOf(value.name()));
        }
    }

    @Test
    public void test_name_notEmpty() {
        for (ParallelMode value : ParallelMode.values()) {
            assertFalse(value.name().isEmpty());
        }
    }

    @Test
    public void test_name_notNull() {
        for (ParallelMode value : ParallelMode.values()) {
            assertNotNull(value.name());
        }
    }

    // ============================================
    // Tests for ordinal() method
    // ============================================

    @Test
    public void test_ordinal_sequential() {
        ParallelMode[] values = ParallelMode.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    public void test_ordinal_yes() {
        int ordinal = ParallelMode.FORCE_ON.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3);
    }

    @Test
    public void test_ordinal_no() {
        int ordinal = ParallelMode.FORCE_OFF.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3);
    }

    @Test
    public void test_ordinal_default() {
        int ordinal = ParallelMode.AUTO.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3);
    }

    @Test
    public void test_ordinal_uniqueValues() {
        int yesOrdinal = ParallelMode.FORCE_ON.ordinal();
        int noOrdinal = ParallelMode.FORCE_OFF.ordinal();
        int defaultOrdinal = ParallelMode.AUTO.ordinal();

        assertNotEquals(yesOrdinal, noOrdinal);
        assertNotEquals(yesOrdinal, defaultOrdinal);
        assertNotEquals(noOrdinal, defaultOrdinal);
    }

    @Test
    public void test_ordinal_nonNegative() {
        for (ParallelMode value : ParallelMode.values()) {
            assertTrue(value.ordinal() >= 0);
        }
    }

    @Test
    public void test_ordinal_matchesArrayIndex() {
        ParallelMode[] values = ParallelMode.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    // ============================================
    // Tests for toString() method
    // ============================================

    @Test
    public void test_toString_yes() {
        String str = ParallelMode.FORCE_ON.toString();

        assertNotNull(str);
        assertEquals("FORCE_ON", str);
    }

    @Test
    public void test_toString_no() {
        String str = ParallelMode.FORCE_OFF.toString();

        assertNotNull(str);
        assertEquals("FORCE_OFF", str);
    }

    @Test
    public void test_toString_default() {
        String str = ParallelMode.AUTO.toString();

        assertNotNull(str);
        assertEquals("AUTO", str);
    }

    @Test
    public void test_toString_matchesName() {
        for (ParallelMode value : ParallelMode.values()) {
            assertEquals(value.name(), value.toString());
        }
    }

    @Test
    public void test_toString_notNull() {
        for (ParallelMode value : ParallelMode.values()) {
            assertNotNull(value.toString());
        }
    }

    @Test
    public void test_toString_notEmpty() {
        for (ParallelMode value : ParallelMode.values()) {
            assertFalse(value.toString().isEmpty());
        }
    }

    // ============================================
    // Tests for equals() method
    // ============================================

    @Test
    public void test_equals_sameInstance() {
        assertTrue(ParallelMode.FORCE_ON == ParallelMode.FORCE_ON);
        assertTrue(ParallelMode.FORCE_OFF == ParallelMode.FORCE_OFF);
        assertTrue(ParallelMode.AUTO == ParallelMode.AUTO);
    }

    @Test
    public void test_equals_differentInstances() {
        assertTrue(ParallelMode.FORCE_ON != ParallelMode.FORCE_OFF);
        assertTrue(ParallelMode.FORCE_ON != ParallelMode.AUTO);
        assertTrue(ParallelMode.FORCE_OFF != ParallelMode.AUTO);
    }

    @Test
    public void test_equals_usingEqualsMethod() {
        assertTrue(ParallelMode.FORCE_ON.equals(ParallelMode.FORCE_ON));
        assertTrue(ParallelMode.FORCE_OFF.equals(ParallelMode.FORCE_OFF));
        assertTrue(ParallelMode.AUTO.equals(ParallelMode.AUTO));
    }

    @Test
    public void test_equals_usingEqualsMethod_differentValues() {
        assertFalse(ParallelMode.FORCE_ON.equals(ParallelMode.FORCE_OFF));
        assertFalse(ParallelMode.FORCE_ON.equals(ParallelMode.AUTO));
        assertFalse(ParallelMode.FORCE_OFF.equals(ParallelMode.AUTO));
    }

    @Test
    public void test_equals_withNull() {
        assertFalse(ParallelMode.FORCE_ON.equals(null));
        assertFalse(ParallelMode.FORCE_OFF.equals(null));
        assertFalse(ParallelMode.AUTO.equals(null));
    }

    @Test
    public void test_equals_withDifferentType() {
        assertFalse(ParallelMode.FORCE_ON.equals("FORCE_ON"));
        assertFalse(ParallelMode.FORCE_OFF.equals("FORCE_OFF"));
        assertFalse(ParallelMode.AUTO.equals("AUTO"));
    }

    @Test
    public void test_equals_reflexive() {
        for (ParallelMode value : ParallelMode.values()) {
            assertTrue(value.equals(value));
        }
    }

    @Test
    public void test_equals_symmetric() {
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_ON;

        assertEquals(a.equals(b), b.equals(a));
    }

    @Test
    public void test_equals_transitive() {
        ParallelMode a = ParallelMode.AUTO;
        ParallelMode b = ParallelMode.valueOf("AUTO");
        ParallelMode c = ParallelMode.AUTO;

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void test_equals_consistent() {
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_OFF;

        boolean result1 = a.equals(b);
        boolean result2 = a.equals(b);
        boolean result3 = a.equals(b);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    // ============================================
    // Tests for hashCode() method
    // ============================================

    @Test
    public void test_hashCode_consistent() {
        int hash1 = ParallelMode.FORCE_ON.hashCode();
        int hash2 = ParallelMode.FORCE_ON.hashCode();
        int hash3 = ParallelMode.FORCE_ON.hashCode();

        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
    }

    @Test
    public void test_hashCode_sameValuesSameHash() {
        ParallelMode yes1 = ParallelMode.FORCE_ON;
        ParallelMode yes2 = ParallelMode.valueOf("FORCE_ON");

        assertEquals(yes1.hashCode(), yes2.hashCode());
    }

    @Test
    public void test_hashCode_differentValuesDifferentHash() {
        int yesHash = ParallelMode.FORCE_ON.hashCode();
        int noHash = ParallelMode.FORCE_OFF.hashCode();
        int defaultHash = ParallelMode.AUTO.hashCode();

        assertTrue(yesHash != noHash || yesHash != defaultHash);
    }

    @Test
    public void test_hashCode_equalsContract() {
        ParallelMode a = ParallelMode.AUTO;
        ParallelMode b = ParallelMode.AUTO;

        if (a.equals(b)) {
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    @Test
    public void test_hashCode_allValuesHaveHashCode() {
        for (ParallelMode value : ParallelMode.values()) {
            int hash = value.hashCode();
            assertNotNull(Integer.valueOf(hash));
        }
    }

    // ============================================
    // Tests for compareTo() method
    // ============================================

    @Test
    public void test_compareTo_sameValue() {
        ParallelMode yes1 = ParallelMode.FORCE_ON;
        ParallelMode yes2 = ParallelMode.FORCE_ON;

        assertEquals(0, yes1.compareTo(yes2));
    }

    @Test
    public void test_compareTo_differentValues() {
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
    public void test_compareTo_orderingByOrdinal() {
        ParallelMode first = ParallelMode.values()[0];
        ParallelMode last = ParallelMode.values()[ParallelMode.values().length - 1];

        assertTrue(first.compareTo(last) < 0);
        assertTrue(last.compareTo(first) > 0);
    }

    @Test
    public void test_compareTo_reflexive() {
        for (ParallelMode value : ParallelMode.values()) {
            assertEquals(0, value.compareTo(value));
        }
    }

    @Test
    public void test_compareTo_antisymmetric() {
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_OFF;

        int ab = a.compareTo(b);
        int ba = b.compareTo(a);

        assertEquals(Integer.signum(ab), -Integer.signum(ba));
    }

    @Test
    public void test_compareTo_transitive() {
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

    @Test
    public void test_compareTo_consistentWithEquals() {
        ParallelMode a = ParallelMode.FORCE_ON;
        ParallelMode b = ParallelMode.FORCE_ON;

        if (a.compareTo(b) == 0) {
            assertTrue(a.equals(b));
        }
    }

    // ============================================
    // Switch statement tests
    // ============================================

    @Test
    public void test_switchStatement_yes() {
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
    public void test_switchStatement_no() {
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
    public void test_switchStatement_default() {
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
    public void test_switchStatement_allValues() {
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
    public void test_switchStatement_returnValue() {
        ParallelMode value = ParallelMode.FORCE_ON;

        boolean shouldParallelize = switch (value) {
            case FORCE_ON -> true;
            case FORCE_OFF -> false;
            case AUTO -> true;
        };

        assertTrue(shouldParallelize);
    }

    // ============================================
    // Integration tests
    // ============================================

    @Test
    public void test_integration_enumUsagePattern() {
        ParallelMode setting = ParallelMode.AUTO;

        assertNotNull(setting);
        assertEquals("AUTO", setting.name());
        assertEquals(ParallelMode.AUTO, setting);
    }

    @Test
    public void test_integration_enumIteration() {
        int count = 0;
        for (ParallelMode value : ParallelMode.values()) {
            assertNotNull(value);
            assertNotNull(value.name());
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void test_integration_enumComparison() {
        assertTrue(ParallelMode.FORCE_ON.ordinal() < ParallelMode.values().length);
        assertTrue(ParallelMode.FORCE_OFF.ordinal() < ParallelMode.values().length);
        assertTrue(ParallelMode.AUTO.ordinal() < ParallelMode.values().length);
    }

    @Test
    public void test_integration_enumInCollection() {
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
    public void test_integration_enumInSet() {
        java.util.Set<ParallelMode> set = new java.util.HashSet<>();
        set.add(ParallelMode.FORCE_ON);
        set.add(ParallelMode.FORCE_OFF);
        set.add(ParallelMode.AUTO);

        assertEquals(3, set.size());
    }

    @Test
    public void test_integration_enumInMap() {
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
    public void test_integration_enumInEnumSet() {
        java.util.EnumSet<ParallelMode> enumSet = java.util.EnumSet.allOf(ParallelMode.class);

        assertEquals(3, enumSet.size());
        assertTrue(enumSet.contains(ParallelMode.FORCE_ON));
        assertTrue(enumSet.contains(ParallelMode.FORCE_OFF));
        assertTrue(enumSet.contains(ParallelMode.AUTO));
    }

    @Test
    public void test_integration_enumInEnumMap() {
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
    // Edge cases and semantic tests
    // ============================================

    @Test
    public void test_edgeCase_valueOfWithExactName() {
        assertEquals(ParallelMode.FORCE_ON, ParallelMode.valueOf("FORCE_ON"));
        assertEquals(ParallelMode.FORCE_OFF, ParallelMode.valueOf("FORCE_OFF"));
        assertEquals(ParallelMode.AUTO, ParallelMode.valueOf("AUTO"));
    }

    @Test
    public void test_edgeCase_allValuesAreDistinct() {
        ParallelMode[] values = ParallelMode.values();

        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    public void test_edgeCase_enumConstantsArePublic() {
        assertNotNull(ParallelMode.FORCE_ON);
        assertNotNull(ParallelMode.FORCE_OFF);
        assertNotNull(ParallelMode.AUTO);
    }

    @Test
    public void test_edgeCase_enumIsNotInstantiable() {
        assertEquals(3, ParallelMode.values().length);
    }

    @Test
    public void test_semantics_yesRepresentsParallelMode() {
        ParallelMode yes = ParallelMode.FORCE_ON;

        assertNotNull(yes);
        assertEquals("FORCE_ON", yes.name());
    }

    @Test
    public void test_semantics_noRepresentsParallelDisabled() {
        ParallelMode no = ParallelMode.FORCE_OFF;

        assertNotNull(no);
        assertEquals("FORCE_OFF", no.name());
    }

    @Test
    public void test_semantics_defaultRepresentsDefaultBehavior() {
        ParallelMode defaultValue = ParallelMode.AUTO;

        assertNotNull(defaultValue);
        assertEquals("AUTO", defaultValue.name());
    }

    @Test
    public void test_semantics_allValuesHaveDistinctMeaning() {
        ParallelMode yes = ParallelMode.FORCE_ON;
        ParallelMode no = ParallelMode.FORCE_OFF;
        ParallelMode defaultValue = ParallelMode.AUTO;

        assertNotEquals(yes, no);
        assertNotEquals(yes, defaultValue);
        assertNotEquals(no, defaultValue);
    }
}
