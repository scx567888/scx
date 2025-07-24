package cool.scx.object.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.scx.object.ScxObject;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.reflect.TypeReference;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScxObjectTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws NodeMappingException, JsonProcessingException {
        test1();
        test2();
        test3();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        // 准备数据
        var smallList = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            smallList.add(i);
        }
        var bigList = new ArrayList<Integer>();
        for (int i = 0; i < 99999; i++) {
            bigList.add(i);
        }
        var smallArray = smallList.toArray(new Integer[0]);
        var bigArray = bigList.toArray(new Integer[0]);

        var smallIntArray1 = smallList.stream().mapToInt(c -> c).toArray();
        var bitIntArray2 = bigList.stream().mapToInt(c -> c).toArray();


        for (int i = 0; i < 9; i++) {
            test1_0(smallList, bigList, smallArray, bigArray, smallIntArray1, bitIntArray2);
        }

        for (int i = 0; i < 9; i++) {
            test1_1(smallList, bigList, smallArray, bigArray, smallIntArray1, bitIntArray2);
        }

    }

    public static void test1_0(ArrayList<Integer> smallList, ArrayList<Integer> bigList, Integer[] smallArray, Integer[] bigArray, int[] smallIntArray1, int[] bitIntArray2) {

        ScxObject.convertValue(smallList, Integer[].class);
        ScxObject.convertValue(smallList, int[].class);
        ScxObject.convertValue(bigList, Integer[].class);
        ScxObject.convertValue(bigList, int[].class);
        ScxObject.convertValue(smallArray, Integer[].class);
        ScxObject.convertValue(bigArray, int[].class);
        ScxObject.convertValue(smallArray, Integer[].class);
        ScxObject.convertValue(bigArray, int[].class);
        ScxObject.convertValue(smallList, String[].class);
        ScxObject.convertValue(smallList, long[].class);
        ScxObject.convertValue(bigList, String[].class);
        ScxObject.convertValue(bigList, long[].class);
        ScxObject.convertValue(smallArray, String[].class);
        ScxObject.convertValue(bigArray, long[].class);
        ScxObject.convertValue(smallArray, String[].class);
        ScxObject.convertValue(bigArray, long[].class);

        ScxObject.convertValue(smallList, new TypeReference<List<Integer>>() {});
        ScxObject.convertValue(smallList, new TypeReference<List<String>>() {});
        ScxObject.convertValue(bigList, new TypeReference<List<Integer>>() {});
        ScxObject.convertValue(bigList, new TypeReference<List<String>>() {});
        ScxObject.convertValue(smallArray, List.class);
        ScxObject.convertValue(bigArray, List.class);
        ScxObject.convertValue(smallArray, Object.class);
        ScxObject.convertValue(bigArray, Object.class);

    }

    public static void test1_1(ArrayList<Integer> smallList, ArrayList<Integer> bigList, Integer[] smallArray, Integer[] bigArray, int[] smallIntArray1, int[] bitIntArray2) {

        OBJECT_MAPPER.convertValue(smallList, Integer[].class);
        OBJECT_MAPPER.convertValue(smallList, int[].class);
        OBJECT_MAPPER.convertValue(bigList, Integer[].class);
        OBJECT_MAPPER.convertValue(bigList, int[].class);
        OBJECT_MAPPER.convertValue(smallArray, Integer[].class);
        OBJECT_MAPPER.convertValue(bigArray, int[].class);
        OBJECT_MAPPER.convertValue(smallArray, Integer[].class);
        OBJECT_MAPPER.convertValue(bigArray, int[].class);
        OBJECT_MAPPER.convertValue(smallList, String[].class);
        OBJECT_MAPPER.convertValue(smallList, long[].class);
        OBJECT_MAPPER.convertValue(bigList, String[].class);
        OBJECT_MAPPER.convertValue(bigList, long[].class);
        OBJECT_MAPPER.convertValue(smallArray, String[].class);
        OBJECT_MAPPER.convertValue(bigArray, long[].class);
        OBJECT_MAPPER.convertValue(smallArray, String[].class);
        OBJECT_MAPPER.convertValue(bigArray, long[].class);

        OBJECT_MAPPER.convertValue(smallList, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});
        OBJECT_MAPPER.convertValue(smallList, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        OBJECT_MAPPER.convertValue(bigList, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});
        OBJECT_MAPPER.convertValue(bigList, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        OBJECT_MAPPER.convertValue(smallArray, List.class);
        OBJECT_MAPPER.convertValue(bigArray, List.class);
        OBJECT_MAPPER.convertValue(smallArray, Object.class);
        OBJECT_MAPPER.convertValue(bigArray, Object.class);

    }

    @Test
    public static void test2() throws JsonProcessingException {
        var user = new User();
        user.name = "小明";
        user.name1 = "小明";
        user.age = 18;
        user.parent = null;
        user.ids = new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, "sss"};

        for (int i = 0; i < 999; i++) {
            String json1 = OBJECT_MAPPER.writeValueAsString(user);
            String json2 = ScxObject.toJson(user);
        }

        var userList = new ArrayList<User>();
        for (int i = 0; i < 999; i++) {
            userList.add(user);
        }

        for (int i = 0; i < 99; i++) {
            String json1 = OBJECT_MAPPER.writeValueAsString(userList);
            String json2 = ScxObject.toJson(userList);
        }

    }

    @Test
    public static void test3() throws JsonProcessingException {
        var map = new HashMap<>();
        for (int i = 0; i < 999; i++) {
            map.put(i, i);
        }
        map.put(map, map);

        for (int i = 0; i < 99; i++) {
            try {
                var json2 = ScxObject.toJson(map);
            } catch (Exception e) {

            }
        }

        for (int i = 0; i < 99; i++) {
            try {
                String json1 = OBJECT_MAPPER.writeValueAsString(map);
            } catch (Exception e) {

            }
        }
    }

    private static class User {
        public String name;
        public String name1;
        public Integer age;
        public User parent;
        public Object[] ids;
        public Map<String, Integer> map;
    }

}
