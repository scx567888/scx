package cool.scx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 处理对象的工具类<br>
 * 本质上就是对 {@link JacksonHelper} 进行了一些简单的封装
 * 注意其中所有方法使用的 ObjectMapper 均采用 {@link JacksonHelper#getObjectMapper}
 * 故此方法中所有方法均忽略 @JsonIgnore 注解
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ObjectUtils {

    /**
     * <p>convertValueToMap.</p>
     *
     * @param obj a {@link java.lang.Object} object
     * @return a {@link java.util.Map} object
     */
    public static Map<String, Object> convertValueToMap(Object obj) {
        return _o().convertValue(obj, JacksonHelper.MAP_TYPE);
    }

    /**
     * 获取字段值
     *
     * @param field  字段
     * @param target 字段所属实例对象
     * @return a
     */
    public static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValue(JsonNode jsonNode, Type type) throws IOException {
        return _o().readerFor(JacksonHelper.getTypeFactory().constructType(type)).readValue(jsonNode);
    }

    public static <T> T convertValue(Object fromValue, Class<T> tClass) {
        return _o().convertValue(fromValue, JacksonHelper.getTypeFactory().constructType(tClass));
    }

    public static <T> T convertValue(Object fromValue, Type toValueType) {
        return _o().convertValue(fromValue, JacksonHelper.getTypeFactory().constructType(toValueType));
    }

    public static <T> T convertValue(Object fromValue, JavaType javaType) {
        return _o().convertValue(fromValue, javaType);
    }

    public static <T> T readValue(String fromValue, Type toValueType) throws JsonProcessingException {
        return _o().readValue(fromValue, JacksonHelper.getTypeFactory().constructType(toValueType));
    }

    /**
     * 将对象转 json 底层调用 JacksonHelper.getObjectMapperIgnoreJsonIgnore().writeValueAsString()
     * 所以会忽略 JsonIgnore 注解 同时如果转换失败则在其内部消化异常 (打印) 并返回 ""
     *
     * @param value a {@link Object} object.
     * @return a {@link java.lang.String} object.
     */
    public static String writeValueAsString(Object value, String defaultValue) {
        try {
            return writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return _o().writeValueAsString(value);
    }

    public static JavaType constructType(Type type) {
        return JacksonHelper.getTypeFactory().constructType(type);
    }

    public static JavaType constructType(TypeReference<?> typeRef) {
        return JacksonHelper.getTypeFactory().constructType(typeRef);
    }

    public static Map<String, Object> readValueToMap(File file) throws IOException {
        return _o().readValue(file, JacksonHelper.MAP_TYPE);
    }

    /**
     * 获取 ObjectMapper
     *
     * @return
     */
    private static ObjectMapper _o() {
        return JacksonHelper.getObjectMapper();
    }

}
