package cool.scx.common.test;

import cool.scx.common.util.MultiMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MultiMapTest {

    public static void main(String[] args) {
        MultiMap<String, String> multiMap = new MultiMap<>();

        // 测试添加方法
        System.out.println("---- 添加方法测试 ----");
        multiMap.add("key1", "value1");
        multiMap.add("key1", "value2", "value3");
        multiMap.add("key2", Arrays.asList("value4", "value5"));
        printMultiMap(multiMap);
        // 预期: MultiMap: {key1=[value1, value2, value3], key2=[value4, value5]}

        // 测试覆盖方法
        System.out.println("---- 覆盖方法测试 ----");
        multiMap.set("key1", "newValue1");
        multiMap.set("key2", "newValue2", "newValue3");
        multiMap.set("key3", Arrays.asList("newValue4", "newValue5"));
        printMultiMap(multiMap);
        // 预期: MultiMap: {key1=[newValue1], key2=[newValue2, newValue3], key3=[newValue4, newValue5]}

        // 测试获取方法
        System.out.println("---- 获取方法测试 ----");
        System.out.println("get(key1): " + multiMap.get("key1"));
        System.out.println("getAll(key2): " + multiMap.getAll("key2"));
        // 预期: get(key1): newValue1
        // 预期: getAll(key2): [newValue2, newValue3]

        // 测试包含方法
        System.out.println("---- 包含方法测试 ----");
        System.out.println("containsKey(key1): " + multiMap.containsKey("key1"));
        System.out.println("containsValue(newValue1): " + multiMap.containsValue("newValue1"));
        // 预期: containsKey(key1): true
        // 预期: containsValue(newValue1): true

        // 测试移除方法
        System.out.println("---- 移除方法测试 ----");
        multiMap.remove("key1", "newValue1");
        printMultiMap(multiMap);  // 验证移除单个值后的状态
        multiMap.remove("key2", "newValue2", "newValue3");
        printMultiMap(multiMap);  // 验证移除多个值后的状态
        multiMap.remove("key3", Arrays.asList("newValue4", "newValue5"));
        printMultiMap(multiMap);  // 验证移除集合值后的状态
        multiMap.removeAll("key1");
        printMultiMap(multiMap);
        // 预期: MultiMap: {key2=[newValue2, newValue3], key3=[newValue4, newValue5]}
        // 预期: MultiMap: {key3=[newValue4, newValue5]}
        // 预期: MultiMap: {}
        // 预期: MultiMap: {}

        // 测试基本值和功能方法
        System.out.println("---- 基本值和功能方法测试 ----");
        System.out.println("keys: " + multiMap.keys());
        System.out.println("values: " + multiMap.values());
        System.out.println("size: " + multiMap.size());
        System.out.println("isEmpty: " + multiMap.isEmpty());
        // 预期: keys: []
        // 预期: values: []
        // 预期: size: 0
        // 预期: isEmpty: true

        // 测试转换方法
        System.out.println("---- 转换方法测试 ----");
        Map<String, List<String>> multiValueMap = multiMap.toMultiValueMap();
        System.out.println("toMultiValueMap: " + multiValueMap);
        Map<String, String> singleValueMap = multiMap.toSingleValueMap();
        System.out.println("toSingleValueMap: " + singleValueMap);
        // 预期: toMultiValueMap: {}
        // 预期: toSingleValueMap: {}
    }

    private static void printMultiMap(MultiMap<String, String> multiMap) {
        System.out.println("MultiMap: " + multiMap);
    }

}
