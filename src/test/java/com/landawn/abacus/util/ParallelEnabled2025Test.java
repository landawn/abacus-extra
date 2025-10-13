package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the ParallelEnabled enum.
 * This enum controls parallel processing behavior for matrix operations.
 * Tests verify enum values and their basic properties.
 */
@Tag("2025")
public class ParallelEnabled2025Test extends TestBase {

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

    // ============================================
    // Tests for valueOf() method
    // ============================================

    @Test
    public void testValueOf_Yes() {
        ParallelEnabled result = ParallelEnabled.valueOf("YES");

        assertEquals(ParallelEnabled.YES, result);
    }

    @Test
    public void testValueOf_No() {
        ParallelEnabled result = ParallelEnabled.valueOf("NO");

        assertEquals(ParallelEnabled.NO, result);
    }

    @Test
    public void testValueOf_Default() {
        ParallelEnabled result = ParallelEnabled.valueOf("DEFAULT");

        assertEquals(ParallelEnabled.DEFAULT, result);
    }

    @Test
    public void testValueOf_InvalidValue() {
        try {
            ParallelEnabled.valueOf("INVALID");
            assertTrue(false, "Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue(true);
        }
    }

    @Test
    public void testValueOf_CaseSensitive() {
        try {
            ParallelEnabled.valueOf("yes");
            assertTrue(false, "Should throw IllegalArgumentException for lowercase");
        } catch (IllegalArgumentException e) {
            // Expected - enum valueOf is case-sensitive
            assertTrue(true);
        }
    }

    @Test
    public void testValueOf_Null() {
        try {
            ParallelEnabled.valueOf(null);
            assertTrue(false, "Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
            assertTrue(true);
        }
    }

    // ============================================
    // Tests for name() method
    // ============================================

    @Test
    public void testName_Yes() {
        String name = ParallelEnabled.YES.name();

        assertEquals("YES", name);
    }

    @Test
    public void testName_No() {
        String name = ParallelEnabled.NO.name();

        assertEquals("NO", name);
    }

    @Test
    public void testName_Default() {
        String name = ParallelEnabled.DEFAULT.name();

        assertEquals("DEFAULT", name);
    }

    // ============================================
    // Tests for ordinal() method
    // ============================================

    @Test
    public void testOrdinal_Values() {
        ParallelEnabled[] values = ParallelEnabled.values();

        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal(),
                "Ordinal of " + values[i] + " should be " + i);
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
        assertTrue(yesOrdinal != noOrdinal);
        assertTrue(yesOrdinal != defaultOrdinal);
        assertTrue(noOrdinal != defaultOrdinal);
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

    // ============================================
    // Tests for equals() and comparisons
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
        assertTrue(!ParallelEnabled.YES.equals(ParallelEnabled.NO));
        assertTrue(!ParallelEnabled.YES.equals(ParallelEnabled.DEFAULT));
        assertTrue(!ParallelEnabled.NO.equals(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEquals_WithNull() {
        assertTrue(!ParallelEnabled.YES.equals(null));
        assertTrue(!ParallelEnabled.NO.equals(null));
        assertTrue(!ParallelEnabled.DEFAULT.equals(null));
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
    public void testEnumHashCode() {
        // Enum instances should have consistent hash codes
        int hash1 = ParallelEnabled.YES.hashCode();
        int hash2 = ParallelEnabled.YES.hashCode();

        assertEquals(hash1, hash2);
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
}
