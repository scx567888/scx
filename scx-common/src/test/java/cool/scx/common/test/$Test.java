package cool.scx.common.test;

import cool.scx.common.count_map.CountMap;
import cool.scx.common.multi_map.MultiMap;
import cool.scx.common.util.$;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class $Test {

    public static void main(String[] args) {
        test1();
        test2();
    }

    @Test
    public static void test1() {
        List<String> list = List.of("apple", "banana", "cherry", "date", "apple", "banana");

        // 按首字母分组
        MultiMap<Character, String> groupedByFirstLetter = $.groupingBy(list, s -> s.charAt(0));
        MultiMap<Character, String> expected = new MultiMap<>();
        expected.add('a', "apple");
        expected.add('a', "apple");
        expected.add('b', "banana");
        expected.add('b', "banana");
        expected.add('c', "cherry");
        expected.add('d', "date");
        Assert.assertEquals(groupedByFirstLetter, expected);
    }

    @Test
    public static void test2() {
        List<String> list = List.of("apple", "banana", "cherry", "date", "apple", "banana");

        // 计数每个字符串出现的次数
        CountMap<String> countMap = $.countingBy(list);
        CountMap<String> expected = new CountMap<>();
        expected.add("apple", 2L);
        expected.add("banana", 2L);
        expected.add("cherry", 1L);
        expected.add("date", 1L);
        Assert.assertEquals(countMap, expected);
    }
}
