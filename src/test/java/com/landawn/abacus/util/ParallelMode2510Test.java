package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the ParallelMode enum.
 * This enum controls parallel processing behavior for matrix operations.
 * Tests verify enum values and their basic properties.
 */
@Tag("2510")
public class ParallelMode2510Test extends TestBase {

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

    // ============================================
    // Tests for valueOf() method
    // ============================================

    @Test
    public void testValueOf_Yes() {
        ParallelMode result = ParallelMode.valueOf("FORCE_ON");

        assertEquals(ParallelMode.FORCE_ON, result);
    }

    @Test
    public void testValueOf_No() {
        ParallelMode result = ParallelMode.valueOf("FORCE_OFF");

        assertEquals(ParallelMode.FORCE_OFF, result);
    }

    @Test
    public void testValueOf_Default() {
        ParallelMode result = ParallelMode.valueOf("AUTO");

        assertEquals(ParallelMode.AUTO, result);
    }

    @Test
    public void testValueOf_InvalidValue() {
        try {
            ParallelMode.valueOf("INVALID");
            assertTrue(false, "Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue(true);
        }
    }

    @Test
    public void testValueOf_CaseSensitive() {
        try {
            ParallelMode.valueOf("yes");
            assertTrue(false, "Should throw IllegalArgumentException for lowercase");
        } catch (IllegalArgumentException e) {
            // Expected - enum valueOf is case-sensitive
            assertTrue(true);
        }
    }

    @Test
    public void testValueOf_Null() {
        try {
            ParallelMode.valueOf(null);
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
        String name = ParallelMode.FORCE_ON.name();

        assertEquals("FORCE_ON", name);
    }

    @Test
    public void testName_No() {
        String name = ParallelMode.FORCE_OFF.name();

        assertEquals("FORCE_OFF", name);
    }

    @Test
    public void testName_Default() {
        String name = ParallelMode.AUTO.name();

        assertEquals("AUTO", name);
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
        assertTrue(yesOrdinal != noOrdinal);
        assertTrue(yesOrdinal != defaultOrdinal);
        assertTrue(noOrdinal != defaultOrdinal);
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

    // ============================================
    // Tests for equals() and comparisons
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
        assertTrue(!ParallelMode.FORCE_ON.equals(ParallelMode.FORCE_OFF));
        assertTrue(!ParallelMode.FORCE_ON.equals(ParallelMode.AUTO));
        assertTrue(!ParallelMode.FORCE_OFF.equals(ParallelMode.AUTO));
    }

    @Test
    public void testEquals_WithNull() {
        assertTrue(!ParallelMode.FORCE_ON.equals(null));
        assertTrue(!ParallelMode.FORCE_OFF.equals(null));
        assertTrue(!ParallelMode.AUTO.equals(null));
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
    public void testEnumHashCode() {
        // Enum instances should have consistent hash codes
        int hash1 = ParallelMode.FORCE_ON.hashCode();
        int hash2 = ParallelMode.FORCE_ON.hashCode();

        assertEquals(hash1, hash2);
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
}
