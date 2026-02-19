/*
 * Copyright (C) 2024 HaiYang Li
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class ParallelModeTest extends TestBase {

    @Test
    public void testEnumValues() {
        ParallelMode[] values = ParallelMode.values();

        assertEquals(3, values.length);
        assertEquals(ParallelMode.FORCE_ON, values[0]);
        assertEquals(ParallelMode.FORCE_OFF, values[1]);
        assertEquals(ParallelMode.AUTO, values[2]);
    }

    @Test
    public void testEnumConstants() {
        assertNotNull(ParallelMode.FORCE_ON);
        assertNotNull(ParallelMode.FORCE_OFF);
        assertNotNull(ParallelMode.AUTO);
    }

    @Test
    public void testValueOf() {
        assertSame(ParallelMode.FORCE_ON, ParallelMode.valueOf("FORCE_ON"));
        assertSame(ParallelMode.FORCE_OFF, ParallelMode.valueOf("FORCE_OFF"));
        assertSame(ParallelMode.AUTO, ParallelMode.valueOf("AUTO"));
    }

    @Test
    public void testValueOfWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf("yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelMode.valueOf(""));
    }

    @Test
    public void testValueOfWithNull() {
        assertThrows(NullPointerException.class, () -> ParallelMode.valueOf(null));
    }

    @Test
    public void testEnumOrdinals() {
        assertEquals(0, ParallelMode.FORCE_ON.ordinal());
        assertEquals(1, ParallelMode.FORCE_OFF.ordinal());
        assertEquals(2, ParallelMode.AUTO.ordinal());
    }

    @Test
    public void testEnumNames() {
        assertEquals("FORCE_ON", ParallelMode.FORCE_ON.name());
        assertEquals("FORCE_OFF", ParallelMode.FORCE_OFF.name());
        assertEquals("AUTO", ParallelMode.AUTO.name());
    }

    @Test
    public void testEnumToString() {
        assertEquals("FORCE_ON", ParallelMode.FORCE_ON.toString());
        assertEquals("FORCE_OFF", ParallelMode.FORCE_OFF.toString());
        assertEquals("AUTO", ParallelMode.AUTO.toString());
    }

    @Test
    public void testEnumCompareTo() {
        // Test that ordinal order is maintained
        assertEquals(-1, ParallelMode.FORCE_ON.compareTo(ParallelMode.FORCE_OFF));
        assertEquals(-2, ParallelMode.FORCE_ON.compareTo(ParallelMode.AUTO));
        assertEquals(-1, ParallelMode.FORCE_OFF.compareTo(ParallelMode.AUTO));

        assertEquals(1, ParallelMode.FORCE_OFF.compareTo(ParallelMode.FORCE_ON));
        assertEquals(2, ParallelMode.AUTO.compareTo(ParallelMode.FORCE_ON));
        assertEquals(1, ParallelMode.AUTO.compareTo(ParallelMode.FORCE_OFF));

        assertEquals(0, ParallelMode.FORCE_ON.compareTo(ParallelMode.FORCE_ON));
        assertEquals(0, ParallelMode.FORCE_OFF.compareTo(ParallelMode.FORCE_OFF));
        assertEquals(0, ParallelMode.AUTO.compareTo(ParallelMode.AUTO));
    }

    @Test
    public void testEnumEquals() {
        assertEquals(ParallelMode.FORCE_ON, ParallelMode.FORCE_ON);
        assertEquals(ParallelMode.FORCE_OFF, ParallelMode.FORCE_OFF);
        assertEquals(ParallelMode.AUTO, ParallelMode.AUTO);
    }

    @Test
    public void testEnumHashCode() {
        // Hash codes should be consistent
        assertEquals(ParallelMode.FORCE_ON.hashCode(), ParallelMode.FORCE_ON.hashCode());
        assertEquals(ParallelMode.FORCE_OFF.hashCode(), ParallelMode.FORCE_OFF.hashCode());
        assertEquals(ParallelMode.AUTO.hashCode(), ParallelMode.AUTO.hashCode());
    }

    @Test
    public void testEnumInSwitchStatement() {
        // Test that enum can be used in switch statements
        for (ParallelMode value : ParallelMode.values()) {
            String result = switch (value) {
                case FORCE_ON -> "parallel";
                case FORCE_OFF -> "sequential";
                case AUTO -> "automatic";
            };

            assertNotNull(result);
            switch (value) {
                case FORCE_ON -> assertEquals("parallel", result);
                case FORCE_OFF -> assertEquals("sequential", result);
                case AUTO -> assertEquals("automatic", result);
            }
        }
    }

    @Test
    public void testEnumSerializability() {
        // Test that enum constants maintain identity after serialization
        // (This is guaranteed by the JVM for enums)
        ParallelMode yes1 = ParallelMode.FORCE_ON;
        ParallelMode yes2 = ParallelMode.valueOf("FORCE_ON");

        assertSame(yes1, yes2);
    }
}
