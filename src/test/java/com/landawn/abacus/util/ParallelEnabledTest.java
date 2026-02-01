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

public class ParallelEnabledTest extends TestBase {

    @Test
    public void testEnumValues() {
        ParallelEnabled[] values = ParallelEnabled.values();

        assertEquals(3, values.length);
        assertEquals(ParallelEnabled.YES, values[0]);
        assertEquals(ParallelEnabled.NO, values[1]);
        assertEquals(ParallelEnabled.DEFAULT, values[2]);
    }

    @Test
    public void testEnumConstants() {
        assertNotNull(ParallelEnabled.YES);
        assertNotNull(ParallelEnabled.NO);
        assertNotNull(ParallelEnabled.DEFAULT);
    }

    @Test
    public void testValueOf() {
        assertSame(ParallelEnabled.YES, ParallelEnabled.valueOf("YES"));
        assertSame(ParallelEnabled.NO, ParallelEnabled.valueOf("NO"));
        assertSame(ParallelEnabled.DEFAULT, ParallelEnabled.valueOf("DEFAULT"));
    }

    @Test
    public void testValueOfWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf("yes"));
        assertThrows(IllegalArgumentException.class, () -> ParallelEnabled.valueOf(""));
    }

    @Test
    public void testValueOfWithNull() {
        assertThrows(NullPointerException.class, () -> ParallelEnabled.valueOf(null));
    }

    @Test
    public void testEnumOrdinals() {
        assertEquals(0, ParallelEnabled.YES.ordinal());
        assertEquals(1, ParallelEnabled.NO.ordinal());
        assertEquals(2, ParallelEnabled.DEFAULT.ordinal());
    }

    @Test
    public void testEnumNames() {
        assertEquals("YES", ParallelEnabled.YES.name());
        assertEquals("NO", ParallelEnabled.NO.name());
        assertEquals("DEFAULT", ParallelEnabled.DEFAULT.name());
    }

    @Test
    public void testEnumToString() {
        assertEquals("YES", ParallelEnabled.YES.toString());
        assertEquals("NO", ParallelEnabled.NO.toString());
        assertEquals("DEFAULT", ParallelEnabled.DEFAULT.toString());
    }

    @Test
    public void testEnumCompareTo() {
        // Test that ordinal order is maintained
        assertEquals(-1, ParallelEnabled.YES.compareTo(ParallelEnabled.NO));
        assertEquals(-2, ParallelEnabled.YES.compareTo(ParallelEnabled.DEFAULT));
        assertEquals(-1, ParallelEnabled.NO.compareTo(ParallelEnabled.DEFAULT));

        assertEquals(1, ParallelEnabled.NO.compareTo(ParallelEnabled.YES));
        assertEquals(2, ParallelEnabled.DEFAULT.compareTo(ParallelEnabled.YES));
        assertEquals(1, ParallelEnabled.DEFAULT.compareTo(ParallelEnabled.NO));

        assertEquals(0, ParallelEnabled.YES.compareTo(ParallelEnabled.YES));
        assertEquals(0, ParallelEnabled.NO.compareTo(ParallelEnabled.NO));
        assertEquals(0, ParallelEnabled.DEFAULT.compareTo(ParallelEnabled.DEFAULT));
    }

    @Test
    public void testEnumEquals() {
        assertEquals(ParallelEnabled.YES, ParallelEnabled.YES);
        assertEquals(ParallelEnabled.NO, ParallelEnabled.NO);
        assertEquals(ParallelEnabled.DEFAULT, ParallelEnabled.DEFAULT);
    }

    @Test
    public void testEnumHashCode() {
        // Hash codes should be consistent
        assertEquals(ParallelEnabled.YES.hashCode(), ParallelEnabled.YES.hashCode());
        assertEquals(ParallelEnabled.NO.hashCode(), ParallelEnabled.NO.hashCode());
        assertEquals(ParallelEnabled.DEFAULT.hashCode(), ParallelEnabled.DEFAULT.hashCode());
    }

    @Test
    public void testEnumInSwitchStatement() {
        // Test that enum can be used in switch statements
        for (ParallelEnabled value : ParallelEnabled.values()) {
            String result = switch (value) {
                case YES -> "parallel";
                case NO -> "sequential";
                case DEFAULT -> "automatic";
            };

            assertNotNull(result);
            switch (value) {
                case YES -> assertEquals("parallel", result);
                case NO -> assertEquals("sequential", result);
                case DEFAULT -> assertEquals("automatic", result);
            }
        }
    }

    @Test
    public void testEnumSerializability() {
        // Test that enum constants maintain identity after serialization
        // (This is guaranteed by the JVM for enums)
        ParallelEnabled yes1 = ParallelEnabled.YES;
        ParallelEnabled yes2 = ParallelEnabled.valueOf("YES");

        assertSame(yes1, yes2);
    }
}
