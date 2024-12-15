package cool.scx.common.test;

import cool.scx.common.count_map.CountMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountMapTest {

    public static void main(String[] args) {
        testAdd();
        testSet();
        testGet();
        testRemove();
        testSizeAndIsEmpty();
        testClear();
        testKeys();
        testToMap();
        testToMapWithSupplier();
    }

    @Test
    public static void testAdd() {
        var countMap = new CountMap<>();
        long count = countMap.add("key1", 5);
        Assert.assertEquals(count, 5);
        count = countMap.add("key1", 3);
        Assert.assertEquals(count, 8);
    }

    @Test
    public static void testSet() {
        var countMap = new CountMap<>();
        Long previousCount = countMap.set("key1", 10);
        Assert.assertNull(previousCount);
        previousCount = countMap.set("key1", 15);
        Assert.assertEquals(previousCount, Long.valueOf(10));
    }

    @Test
    public static void testGet() {
        var countMap = new CountMap<>();
        countMap.add("key1", 5);
        Long count = countMap.get("key1");
        Assert.assertEquals(count, Long.valueOf(5));
    }

    @Test
    public static void testRemove() {
        var countMap = new CountMap<>();
        countMap.add("key1", 5);
        Long removedCount = countMap.remove("key1");
        Assert.assertEquals(removedCount, Long.valueOf(5));
        Long count = countMap.get("key1");
        Assert.assertNull(count);
    }

    @Test
    public static void testSizeAndIsEmpty() {
        var countMap = new CountMap<>();
        Assert.assertTrue(countMap.isEmpty());
        countMap.add("key1", 5);
        Assert.assertEquals(countMap.size(), 1);
        Assert.assertFalse(countMap.isEmpty());
    }

    @Test
    public static void testClear() {
        var countMap = new CountMap<>();
        countMap.add("key1", 5);
        countMap.add("key2", 10);
        countMap.clear();
        Assert.assertTrue(countMap.isEmpty());
        Assert.assertEquals(countMap.size(), 0);
    }

    @Test
    public static void testKeys() {
        var countMap = new CountMap<String>();
        countMap.add("key1", 5);
        countMap.add("key2", 10);
        Set<String> keys = countMap.keys();
        Assert.assertTrue(keys.contains("key1"));
        Assert.assertTrue(keys.contains("key2"));
    }

    @Test
    public static void testToMap() {
        var countMap = new CountMap<String>();
        countMap.add("key1", 5);
        countMap.add("key2", 10);
        Map<String, Long> map = countMap.toMap();
        Assert.assertEquals(map.get("key1"), Long.valueOf(5));
        Assert.assertEquals(map.get("key2"), Long.valueOf(10));
    }

    @Test
    public static void testToMapWithSupplier() {
        var countMap = new CountMap<String>();
        countMap.add("key1", 5);
        countMap.add("key2", 10);
        Map<String, Long> map = countMap.toMap(HashMap::new);
        Assert.assertEquals(map.get("key1"), Long.valueOf(5));
        Assert.assertEquals(map.get("key2"), Long.valueOf(10));
    }
}
