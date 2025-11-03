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
 * Comprehensive unit tests for ParallelEnabled enum.
 * Tests all enum constants (YES, NO, DEFAULT) and all inherited methods from Enum class.
 * Covers: values(), valueOf(), name(), ordinal(), toString(), equals(), hashCode(), compareTo().
 */
@Tag("2512")
public class ParallelEnabled2512Test extends TestBase {

    // ============================================
    // Tests for enum values() method
    // ============================================

    @Test
    public void test_values_returnsAllEnumConstants() {
        ParallelEnabled[] values = ParallelEnabled.values();

        assertNotNull(values);
        assertEquals(3, values.length);
    }

    @Test
    public void test_values_containsYes() {
        ParallelEnabled[] values = ParallelEnabled.values();
        boolean containsYes = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.YES) {
                containsYes = true;
                break;
            }
        }

        assertTrue(containsYes);
    }

    @Test
    public void test_values_containsNo() {
        ParallelEnabled[] values = ParallelEnabled.values();
        boolean containsNo = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.NO) {
                containsNo = true;
                break;
            }
        }

        assertTrue(containsNo);
    }

    @Test
    public void test_values_containsDefault() {
        ParallelEnabled[] values = ParallelEnabled.values();
        boolean containsDefault = false;

        for (ParallelEnabled value : values) {
            if (value == ParallelEnabled.DEFAULT) {
                containsDefault = true;
                break;
            }
        }

        assertTrue(containsDefault);
    }

    @Test
    public void test_values_allThreePresent() {
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

        assertTrue(hasYes && hasNo && hasDefault);
    }

    @Test
    public void test_values_orderConsistent() {
        ParallelEnabled[] values1 = ParallelEnabled.values();
        ParallelEnabled[] values2 = ParallelEnabled.values();

        assertEquals(values1.length, values2.length);
        for (int i = 0; i < values1.length; i++) {
            assertEquals(values1[i], values2[i]);
        }
    }

    @Test
    public void test_values_notNull() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (ParallelEnabled value : values) {
            assertNotNull(value);
        }
    }

    // ============================================
    // Tests for valueOf() method
    // ============================================

    @Test
    public void test_valueOf_yes() {
        ParallelEnabled result = ParallelEnabled.valueOf("YES");

        assertNotNull(result);
        assertEquals(ParallelEnabled.YES, result);
    }

    @Test
    public void test_valueOf_no() {
        ParallelEnabled result = ParallelEnabled.valueOf("NO");

        assertNotNull(result);
        assertEquals(ParallelEnabled.NO, result);
    }

    @Test
    public void test_valueOf_default() {
        ParallelEnabled result = ParallelEnabled.valueOf("DEFAULT");

        assertNotNull(result);
        assertEquals(ParallelEnabled.DEFAULT, result);
    }

    @Test
    public void test_valueOf_invalidValue() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("INVALID"));
    }

    @Test
    public void test_valueOf_caseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("no"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("default"));
    }

    @Test
    public void test_valueOf_null() {
        assertThrows(NullPointerException.class, () -> ParallelEnabled.valueOf(null));
    }

    @Test
    public void test_valueOf_emptyString() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf(""));
    }

    @Test
    public void test_valueOf_whitespace() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf(" YES "));
    }

    @Test
    public void test_valueOf_mixedCase() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("Yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("No"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("Default"));
    }

    @Test
    public void test_valueOf_returnsSameInstance() {
        ParallelEnabled yes1 = ParallelEnabled.valueOf("YES");
        ParallelEnabled yes2 = ParallelEnabled.valueOf("YES");

        assertTrue(yes1 == yes2);
    }

    // ============================================
    // Tests for name() method
    // ============================================

    @Test
    public void test_name_yes() {
        String name = ParallelEnabled.YES.name();

        assertNotNull(name);
        assertEquals("YES", name);
    }

    @Test
    public void test_name_no() {
        String name = ParallelEnabled.NO.name();

        assertNotNull(name);
        assertEquals("NO", name);
    }

    @Test
    public void test_name_default() {
        String name = ParallelEnabled.DEFAULT.name();

        assertNotNull(name);
        assertEquals("DEFAULT", name);
    }

    @Test
    public void test_name_consistentWithValueOf() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertEquals(value, ParallelEnabled.valueOf(value.name()));
        }
    }

    @Test
    public void test_name_notEmpty() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertFalse(value.name().isEmpty());
        }
    }

    @Test
    public void test_name_notNull() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertNotNull(value.name());
        }
    }

    // ============================================
    // Tests for ordinal() method
    // ============================================

    @Test
    public void test_ordinal_sequential() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    public void test_ordinal_yes() {
        int ordinal = ParallelEnabled.YES.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3);
    }

    @Test
    public void test_ordinal_no() {
        int ordinal = ParallelEnabled.NO.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3);
    }

    @Test
    public void test_ordinal_default() {
        int ordinal = ParallelEnabled.DEFAULT.ordinal();

        assertTrue(ordinal >= 0 && ordinal < 3);
    }

    @Test
    public void test_ordinal_uniqueValues() {
        int yesOrdinal = ParallelEnabled.YES.ordinal();
        int noOrdinal = ParallelEnabled.NO.ordinal();
        int defaultOrdinal = ParallelEnabled.DEFAULT.ordinal();

        assertNotEquals(yesOrdinal, noOrdinal);
        assertNotEquals(yesOrdinal, defaultOrdinal);
        assertNotEquals(noOrdinal, defaultOrdinal);
    }

    @Test
    public void test_ordinal_nonNegative() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertTrue(value.ordinal() >= 0);
        }
    }

    @Test
    public void test_ordinal_matchesArrayIndex() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    // ============================================
    // Tests for toString() method
    // ============================================

    @Test
    public void test_toString_yes() {
        String str = ParallelEnabled.YES.toString();

        assertNotNull(str);
        assertEquals("YES", str);
    }

    @Test
    public void test_toString_no() {
        String str = ParallelEnabled.NO.toString();

        assertNotNull(str);
        assertEquals("NO", str);
    }

    @Test
    public void test_toString_default() {
        String str = ParallelEnabled.DEFAULT.toString();

        assertNotNull(str);
        assertEquals("DEFAULT", str);
    }

    @Test
    public void test_toString_matchesName() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertEquals(value.name(), value.toString());
        }
    }

    @Test
    public void test_toString_notNull() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertNotNull(value.toString());
        }
    }

    @Test
    public void test_toString_notEmpty() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertFalse(value.toString().isEmpty());
        }
    }

    // ============================================
    // Tests for equals() method
    // ============================================

    @Test
    public void test_equals_sameInstance() {
        assertTrue(ParallelEnabled.YES == ParallelEnabled.YES);
        assertTrue(ParallelEnabled.NO == ParallelEnabled.NO);
        assertTrue(ParallelEnabled.DEFAULT == ParallelEnabled.DEFAULT);
    }

    @Test
    public void test_equals_differentInstances() {
        assertTrue(ParallelEnabled.YES != ParallelEnabled.NO);
        assertTrue(ParallelEnabled.YES != ParallelEnabled.DEFAULT);
        assertTrue(ParallelEnabled.NO != ParallelEnabled.DEFAULT);
    }

    @Test
    public void test_equals_usingEqualsMethod() {
        assertTrue(ParallelEnabled.YES.equals(ParallelEnabled.YES));
        assertTrue(ParallelEnabled.NO.equals(ParallelEnabled.NO));
        assertTrue(ParallelEnabled.DEFAULT.equals(ParallelEnabled.DEFAULT));
    }

    @Test
    public void test_equals_usingEqualsMethod_differentValues() {
        assertFalse(ParallelEnabled.YES.equals(ParallelEnabled.NO));
        assertFalse(ParallelEnabled.YES.equals(ParallelEnabled.DEFAULT));
        assertFalse(ParallelEnabled.NO.equals(ParallelEnabled.DEFAULT));
    }

    @Test
    public void test_equals_withNull() {
        assertFalse(ParallelEnabled.YES.equals(null));
        assertFalse(ParallelEnabled.NO.equals(null));
        assertFalse(ParallelEnabled.DEFAULT.equals(null));
    }

    @Test
    public void test_equals_withDifferentType() {
        assertFalse(ParallelEnabled.YES.equals("YES"));
        assertFalse(ParallelEnabled.NO.equals("NO"));
        assertFalse(ParallelEnabled.DEFAULT.equals("DEFAULT"));
    }

    @Test
    public void test_equals_reflexive() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertTrue(value.equals(value));
        }
    }

    @Test
    public void test_equals_symmetric() {
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.YES;

        assertEquals(a.equals(b), b.equals(a));
    }

    @Test
    public void test_equals_transitive() {
        ParallelEnabled a = ParallelEnabled.DEFAULT;
        ParallelEnabled b = ParallelEnabled.valueOf("DEFAULT");
        ParallelEnabled c = ParallelEnabled.DEFAULT;

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void test_equals_consistent() {
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.NO;

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
        int hash1 = ParallelEnabled.YES.hashCode();
        int hash2 = ParallelEnabled.YES.hashCode();
        int hash3 = ParallelEnabled.YES.hashCode();

        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
    }

    @Test
    public void test_hashCode_sameValuesSameHash() {
        ParallelEnabled yes1 = ParallelEnabled.YES;
        ParallelEnabled yes2 = ParallelEnabled.valueOf("YES");

        assertEquals(yes1.hashCode(), yes2.hashCode());
    }

    @Test
    public void test_hashCode_differentValuesDifferentHash() {
        int yesHash = ParallelEnabled.YES.hashCode();
        int noHash = ParallelEnabled.NO.hashCode();
        int defaultHash = ParallelEnabled.DEFAULT.hashCode();

        assertTrue(yesHash != noHash || yesHash != defaultHash);
    }

    @Test
    public void test_hashCode_equalsContract() {
        ParallelEnabled a = ParallelEnabled.DEFAULT;
        ParallelEnabled b = ParallelEnabled.DEFAULT;

        if (a.equals(b)) {
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    @Test
    public void test_hashCode_allValuesHaveHashCode() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            int hash = value.hashCode();
            assertNotNull(Integer.valueOf(hash));
        }
    }

    // ============================================
    // Tests for compareTo() method
    // ============================================

    @Test
    public void test_compareTo_sameValue() {
        ParallelEnabled yes1 = ParallelEnabled.YES;
        ParallelEnabled yes2 = ParallelEnabled.YES;

        assertEquals(0, yes1.compareTo(yes2));
    }

    @Test
    public void test_compareTo_differentValues() {
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
    public void test_compareTo_orderingByOrdinal() {
        ParallelEnabled first = ParallelEnabled.values()[0];
        ParallelEnabled last = ParallelEnabled.values()[ParallelEnabled.values().length - 1];

        assertTrue(first.compareTo(last) < 0);
        assertTrue(last.compareTo(first) > 0);
    }

    @Test
    public void test_compareTo_reflexive() {
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertEquals(0, value.compareTo(value));
        }
    }

    @Test
    public void test_compareTo_antisymmetric() {
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.NO;

        int ab = a.compareTo(b);
        int ba = b.compareTo(a);

        assertEquals(Integer.signum(ab), -Integer.signum(ba));
    }

    @Test
    public void test_compareTo_transitive() {
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

    @Test
    public void test_compareTo_consistentWithEquals() {
        ParallelEnabled a = ParallelEnabled.YES;
        ParallelEnabled b = ParallelEnabled.YES;

        if (a.compareTo(b) == 0) {
            assertTrue(a.equals(b));
        }
    }

    // ============================================
    // Switch statement tests
    // ============================================

    @Test
    public void test_switchStatement_yes() {
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
    public void test_switchStatement_no() {
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
    public void test_switchStatement_default() {
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
    public void test_switchStatement_allValues() {
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
    public void test_switchStatement_returnValue() {
        ParallelEnabled value = ParallelEnabled.YES;

        boolean shouldParallelize = switch (value) {
            case YES -> true;
            case NO -> false;
            case DEFAULT -> true;
        };

        assertTrue(shouldParallelize);
    }

    // ============================================
    // Integration tests
    // ============================================

    @Test
    public void test_integration_enumUsagePattern() {
        ParallelEnabled setting = ParallelEnabled.DEFAULT;

        assertNotNull(setting);
        assertEquals("DEFAULT", setting.name());
        assertEquals(ParallelEnabled.DEFAULT, setting);
    }

    @Test
    public void test_integration_enumIteration() {
        int count = 0;
        for (ParallelEnabled value : ParallelEnabled.values()) {
            assertNotNull(value);
            assertNotNull(value.name());
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void test_integration_enumComparison() {
        assertTrue(ParallelEnabled.YES.ordinal() < ParallelEnabled.values().length);
        assertTrue(ParallelEnabled.NO.ordinal() < ParallelEnabled.values().length);
        assertTrue(ParallelEnabled.DEFAULT.ordinal() < ParallelEnabled.values().length);
    }

    @Test
    public void test_integration_enumInCollection() {
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
    public void test_integration_enumInSet() {
        java.util.Set<ParallelEnabled> set = new java.util.HashSet<>();
        set.add(ParallelEnabled.YES);
        set.add(ParallelEnabled.NO);
        set.add(ParallelEnabled.DEFAULT);

        assertEquals(3, set.size());
    }

    @Test
    public void test_integration_enumInMap() {
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
    public void test_integration_enumInEnumSet() {
        java.util.EnumSet<ParallelEnabled> enumSet = java.util.EnumSet.allOf(ParallelEnabled.class);

        assertEquals(3, enumSet.size());
        assertTrue(enumSet.contains(ParallelEnabled.YES));
        assertTrue(enumSet.contains(ParallelEnabled.NO));
        assertTrue(enumSet.contains(ParallelEnabled.DEFAULT));
    }

    @Test
    public void test_integration_enumInEnumMap() {
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
    // Edge cases and semantic tests
    // ============================================

    @Test
    public void test_edgeCase_valueOfWithExactName() {
        assertEquals(ParallelEnabled.YES, ParallelEnabled.valueOf("YES"));
        assertEquals(ParallelEnabled.NO, ParallelEnabled.valueOf("NO"));
        assertEquals(ParallelEnabled.DEFAULT, ParallelEnabled.valueOf("DEFAULT"));
    }

    @Test
    public void test_edgeCase_allValuesAreDistinct() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    public void test_edgeCase_enumConstantsArePublic() {
        assertNotNull(ParallelEnabled.YES);
        assertNotNull(ParallelEnabled.NO);
        assertNotNull(ParallelEnabled.DEFAULT);
    }

    @Test
    public void test_edgeCase_enumIsNotInstantiable() {
        assertEquals(3, ParallelEnabled.values().length);
    }

    @Test
    public void test_semantics_yesRepresentsParallelEnabled() {
        ParallelEnabled yes = ParallelEnabled.YES;

        assertNotNull(yes);
        assertEquals("YES", yes.name());
    }

    @Test
    public void test_semantics_noRepresentsParallelDisabled() {
        ParallelEnabled no = ParallelEnabled.NO;

        assertNotNull(no);
        assertEquals("NO", no.name());
    }

    @Test
    public void test_semantics_defaultRepresentsDefaultBehavior() {
        ParallelEnabled defaultValue = ParallelEnabled.DEFAULT;

        assertNotNull(defaultValue);
        assertEquals("DEFAULT", defaultValue.name());
    }

    @Test
    public void test_semantics_allValuesHaveDistinctMeaning() {
        ParallelEnabled yes = ParallelEnabled.YES;
        ParallelEnabled no = ParallelEnabled.NO;
        ParallelEnabled defaultValue = ParallelEnabled.DEFAULT;

        assertNotEquals(yes, no);
        assertNotEquals(yes, defaultValue);
        assertNotEquals(no, defaultValue);
    }
}
