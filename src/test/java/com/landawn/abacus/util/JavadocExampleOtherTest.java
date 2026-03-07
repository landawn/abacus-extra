package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests verifying Javadoc examples from Points.java, ImmutableIntArray.java, and ParallelMode.java.
 */
public class JavadocExampleOtherTest {

    // ===== Points.xy 2D examples =====

    @Test
    public void testXyIntIntPoint() {
        // From class-level Javadoc and IntIntPoint.of Javadoc
        Points.xy.IntIntPoint point2D = Points.xy.IntIntPoint.of(10, 20, 100);
        assertEquals(10, point2D.x());
        assertEquals(20, point2D.y());
        assertEquals(100, point2D.v());
    }

    @Test
    public void testXyIntIntPointPathfinding() {
        // From IntIntPoint.of Javadoc: "Use in pathfinding with cost values"
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(300, point.v());

        Points.xy.IntIntPoint pathNode = Points.xy.IntIntPoint.of(5, 8, 15);
        assertEquals(5, pathNode.x());
        assertEquals(8, pathNode.y());
        assertEquals(15, pathNode.v());
    }

    @Test
    public void testXyByteBytePoint() {
        // From ByteBytePoint.of Javadoc
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 5);
        assertEquals((byte) 10, point.x());
        assertEquals((byte) 20, point.y());
        assertEquals((byte) 5, point.v());
    }

    @Test
    public void testXyByteIntPoint() {
        // From ByteIntPoint.of Javadoc
        Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 5, (byte) 10, 1000);
        assertEquals((byte) 5, point.x());
        assertEquals((byte) 10, point.y());
        assertEquals(1000, point.v());
    }

    @Test
    public void testXyByteLongPoint() {
        // From ByteLongPoint.of Javadoc
        Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 3, (byte) 7, 1000000000L);
        assertEquals((byte) 3, point.x());
        assertEquals((byte) 7, point.y());
        assertEquals(1000000000L, point.v());
    }

    @Test
    public void testXyByteDoublePoint() {
        // From ByteDoublePoint.of Javadoc
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 2, (byte) 4, 3.14159);
        assertEquals((byte) 2, point.x());
        assertEquals((byte) 4, point.y());
        assertEquals(3.14159, point.v(), 0.00001);
    }

    @Test
    public void testXyByteObjPoint() {
        // From ByteObjPoint.of Javadoc
        Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "label");
        assertEquals((byte) 1, point.x());
        assertEquals((byte) 2, point.y());
        assertEquals("label", point.v());
    }

    @Test
    public void testXyIntBytePoint() {
        // From IntBytePoint.of Javadoc
        Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(100, 200, (byte) 10);
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals((byte) 10, point.v());
    }

    @Test
    public void testXyIntLongPoint() {
        // From IntLongPoint.of Javadoc
        Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(50, 75, 10000000000L);
        assertEquals(50, point.x());
        assertEquals(75, point.y());
        assertEquals(10000000000L, point.v());
    }

    @Test
    public void testXyIntDoublePoint() {
        // From IntDoublePoint.of Javadoc
        Points.xy.IntDoublePoint point = Points.xy.IntDoublePoint.of(10, 20, 3.14159);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(3.14159, point.v(), 0.00001);
    }

    @Test
    public void testXyIntObjPoint() {
        // From IntObjPoint.of Javadoc
        Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, "label");
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals("label", point.v());
    }

    @Test
    public void testXyLongBytePoint() {
        // From LongBytePoint.of Javadoc
        Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000000L, 2000000L, (byte) 5);
        assertEquals(1000000L, point.x());
        assertEquals(2000000L, point.y());
        assertEquals((byte) 5, point.v());
    }

    @Test
    public void testXyLongIntPoint() {
        // From LongIntPoint.of Javadoc
        Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(1000000L, 2000000L, 500);
        assertEquals(1000000L, point.x());
        assertEquals(2000000L, point.y());
        assertEquals(500, point.v());
    }

    @Test
    public void testXyLongLongPoint() {
        // From LongLongPoint.of Javadoc
        Points.xy.LongLongPoint point = Points.xy.LongLongPoint.of(1000000L, 2000000L, 3000000000L);
        assertEquals(1000000L, point.x());
        assertEquals(2000000L, point.y());
        assertEquals(3000000000L, point.v());
    }

    @Test
    public void testXyLongDoublePoint() {
        // From LongDoublePoint.of Javadoc
        Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(1000000L, 2000000L, 3.14159);
        assertEquals(1000000L, point.x());
        assertEquals(2000000L, point.y());
        assertEquals(3.14159, point.v(), 0.00001);
    }

    @Test
    public void testXyLongObjPoint() {
        // From LongObjPoint.of Javadoc
        Points.xy.LongObjPoint<String> point = Points.xy.LongObjPoint.of(1000000L, 2000000L, "marker");
        assertEquals(1000000L, point.x());
        assertEquals(2000000L, point.y());
        assertEquals("marker", point.v());
    }

    @Test
    public void testXyDoubleBytePoint() {
        // From DoubleBytePoint.of Javadoc
        Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 3);
        assertEquals(10.5, point.x(), 0.00001);
        assertEquals(20.7, point.y(), 0.00001);
        assertEquals((byte) 3, point.v());
    }

    @Test
    public void testXyDoubleIntPoint() {
        // From DoubleIntPoint.of Javadoc
        Points.xy.DoubleIntPoint point = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
        assertEquals(10.5, point.x(), 0.00001);
        assertEquals(20.7, point.y(), 0.00001);
        assertEquals(100, point.v());
    }

    @Test
    public void testXyDoubleLongPoint() {
        // From DoubleLongPoint.of Javadoc
        Points.xy.DoubleLongPoint point = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000000L);
        assertEquals(10.5, point.x(), 0.00001);
        assertEquals(20.7, point.y(), 0.00001);
        assertEquals(1000000000L, point.v());
    }

    @Test
    public void testXyDoubleDoublePoint() {
        // From DoubleDoublePoint.of Javadoc
        Points.xy.DoubleDoublePoint point = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 3.14159);
        assertEquals(10.5, point.x(), 0.00001);
        assertEquals(20.7, point.y(), 0.00001);
        assertEquals(3.14159, point.v(), 0.00001);
    }

    @Test
    public void testXyDoubleObjPoint() {
        // From DoubleObjPoint.of Javadoc
        Points.xy.DoubleObjPoint<String> point = Points.xy.DoubleObjPoint.of(10.5, 20.7, "location");
        assertEquals(10.5, point.x(), 0.00001);
        assertEquals(20.7, point.y(), 0.00001);
        assertEquals("location", point.v());
    }

    // ===== Points.xyz 3D examples =====

    @Test
    public void testXyzIntIntPoint() {
        // From class-level Javadoc
        Points.xyz.IntIntPoint point3D = Points.xyz.IntIntPoint.of(10, 20, 30, 100);
        assertEquals(10, point3D.x());
        assertEquals(20, point3D.y());
        assertEquals(30, point3D.z());
        assertEquals(100, point3D.v());
    }

    @Test
    public void testXyzByteBytePoint() {
        // From xyz.ByteBytePoint.of Javadoc
        Points.xyz.ByteBytePoint point = Points.xyz.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30, (byte) 5);
        assertEquals((byte) 10, point.x());
        assertEquals((byte) 20, point.y());
        assertEquals((byte) 30, point.z());
        assertEquals((byte) 5, point.v());
    }

    @Test
    public void testXyzByteIntPoint() {
        // From xyz.ByteIntPoint.of Javadoc
        Points.xyz.ByteIntPoint point = Points.xyz.ByteIntPoint.of((byte) 5, (byte) 10, (byte) 15, 1000);
        assertEquals((byte) 5, point.x());
        assertEquals((byte) 10, point.y());
        assertEquals((byte) 15, point.z());
        assertEquals(1000, point.v());
    }

    @Test
    public void testXyzByteLongPoint() {
        // From xyz.ByteLongPoint.of Javadoc
        Points.xyz.ByteLongPoint point = Points.xyz.ByteLongPoint.of((byte) 3, (byte) 7, (byte) 11, 1000000000L);
        assertEquals((byte) 3, point.x());
        assertEquals((byte) 7, point.y());
        assertEquals((byte) 11, point.z());
        assertEquals(1000000000L, point.v());
    }

    @Test
    public void testXyzByteDoublePoint() {
        // From xyz.ByteDoublePoint.of Javadoc
        Points.xyz.ByteDoublePoint point = Points.xyz.ByteDoublePoint.of((byte) 2, (byte) 4, (byte) 6, 3.14159);
        assertEquals((byte) 2, point.x());
        assertEquals((byte) 4, point.y());
        assertEquals((byte) 6, point.z());
        assertEquals(3.14159, point.v(), 0.00001);
    }

    @Test
    public void testXyzByteObjPoint() {
        // From xyz.ByteObjPoint.of Javadoc
        Points.xyz.ByteObjPoint<String> point = Points.xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "voxel");
        assertEquals((byte) 1, point.x());
        assertEquals((byte) 2, point.y());
        assertEquals((byte) 3, point.z());
        assertEquals("voxel", point.v());
    }

    // ===== ImmutableIntArray examples =====

    @Test
    public void testImmutableIntArrayCopyOfGet() {
        // From class-level Javadoc: "int value = immutable.get(2);   // returns 3"
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);
        assertEquals(3, immutable.get(2));
    }

    @Test
    public void testImmutableIntArrayWrapGet() {
        // From unsafeWrap() Javadoc: "System.out.println(array.get(1));   // prints: 20"
        int[] data = new int[] { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
        assertEquals(20, array.get(1));
    }

    @Test
    public void testImmutableIntArrayWrapMutability() {
        // From unsafeWrap() Javadoc: modifying original array affects the wrapper
        int[] data = new int[] { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
        assertEquals(20, array.get(1));

        data[1] = 99;
        assertEquals(99, array.get(1)); // "prints: 99 (not recommended!)"
    }

    @Test
    public void testImmutableIntArrayCopyOfImmutability() {
        // From copyOf() Javadoc: modifying original array does NOT affect the copy
        int[] data = { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);
        assertEquals(10, array.get(0));

        data[0] = 99;
        assertEquals(10, array.get(0)); // "still prints: 10"
    }

    @Test
    public void testImmutableIntArrayIsEmpty() {
        // From isEmpty() Javadoc
        ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
        assertTrue(empty.isEmpty()); // returns true

        ImmutableIntArray nonEmpty = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        assertFalse(nonEmpty.isEmpty()); // returns false

        ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
        assertTrue(fromNull.isEmpty()); // returns true (null becomes empty array)
    }

    @Test
    public void testImmutableIntArrayContains() {
        // From contains() Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
        assertTrue(array.contains(30)); // returns true
        assertFalse(array.contains(99)); // returns false

        ImmutableIntArray emptyArr = ImmutableIntArray.unsafeWrap(new int[0]);
        assertFalse(emptyArr.contains(1)); // returns false
    }

    @Test
    public void testImmutableIntArrayGet() {
        // From get() Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 5, 10, 15, 20 });
        assertEquals(10, array.get(1)); // returns 10
        assertEquals(5, array.get(0)); // returns 5
        assertEquals(20, array.get(array.length() - 1)); // returns 20
    }

    @Test
    public void testImmutableIntArrayForEachSum() {
        // From forEach() Javadoc: accumulate sum
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
        int[] sum = { 0 };
        array.forEach(value -> sum[0] += value);
        assertEquals(15, sum[0]); // prints: 15
    }

    @Test
    public void testImmutableIntArrayStreamSum() {
        // From stream() Javadoc: "int sum = array.stream().sum();   // returns 15"
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
        int sum = array.stream().sum();
        assertEquals(15, sum);
    }

    @Test
    public void testImmutableIntArrayStreamMax() {
        // From stream() Javadoc: "int max = array.stream().max().orElse(0);   // returns 5"
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
        int max = array.stream().max().orElse(0);
        assertEquals(5, max);
    }

    @Test
    public void testImmutableIntArrayStreamSumOfSquares() {
        // From stream() Javadoc: "int sumOfSquares = ... // returns 55 (1 + 4 + 9 + 16 + 25)"
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
        int sumOfSquares = array.stream().map(x -> x * x).sum();
        assertEquals(55, sumOfSquares);
    }

    @Test
    public void testImmutableIntArraySubArray() {
        // From subArray() Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

        int[] subArray = array.subArray(1, 4);
        assertArrayEquals(new int[] { 20, 30, 40 }, subArray); // {20, 30, 40} with length 3
        assertEquals(3, subArray.length);

        int[] first3 = array.subArray(0, 3);
        assertArrayEquals(new int[] { 10, 20, 30 }, first3);

        int[] empty = array.subArray(2, 2);
        assertEquals(0, empty.length); // empty array with length 0
    }

    @Test
    public void testImmutableIntArrayHashCodeEquals() {
        // From hashCode() Javadoc: equal arrays have same hash
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });

        assertEquals(array1.hashCode(), array2.hashCode()); // "prints: true"
    }

    @Test
    public void testImmutableIntArrayEquals() {
        // From equals() Javadoc
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });
        ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

        assertTrue(array1.equals(array2)); // prints: true
        assertFalse(array1.equals(array3)); // prints: false
        assertFalse(array1.equals(new int[] { 1, 2, 3 })); // prints: false (different type)
    }

    @Test
    public void testImmutableIntArrayToString() {
        // From toString() Javadoc
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        assertEquals("[1, 2, 3]", array.toString());

        ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
        assertEquals("[]", empty.toString());

        ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] { 42 });
        assertEquals("[42]", single.toString());
    }

    // ===== ParallelMode examples =====

    @Test
    public void testParallelModeEnumValues() {
        // Verify the enum constants exist as documented
        assertEquals(ParallelMode.FORCE_ON, ParallelMode.valueOf("FORCE_ON"));
        assertEquals(ParallelMode.FORCE_OFF, ParallelMode.valueOf("FORCE_OFF"));
        assertEquals(ParallelMode.AUTO, ParallelMode.valueOf("AUTO"));

        // Verify all three values are present
        ParallelMode[] values = ParallelMode.values();
        assertEquals(3, values.length);
    }
}
