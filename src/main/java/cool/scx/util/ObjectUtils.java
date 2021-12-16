package cool.scx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 处理对象的工具类<br>
 * 本质上就是对 {@link ObjectMapper} 进行了一些简单的封装
 * 注意其中所有方法使用的 ObjectMapper 均采用 {@link ObjectMapperHelper#setIgnoreJsonIgnore} 进行了处理
 * 故此方法中所有方法均忽略 @JsonIgnore 注解
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ObjectUtils {

    /**
     * 因为 java 无法方便的存储泛型 使用 TypeReference 创建一些常用的类型
     * 此类为 Map 类型
     */
    public static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    /**
     * 忽略 @JsonIgnore 注解的 objectMapper 一般用于内部使用
     */
    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperHelper.setIgnoreJsonIgnore(ObjectMapperHelper.initObjectMapper());

    /**
     * a
     *
     * @param jsonNode a
     * @param type     a
     * @param <T>      a
     * @return a
     * @throws IOException a
     */
    public static <T> T readValue(JsonNode jsonNode, Type type) throws IOException {
        return mapper().readerFor(constructType(type)).readValue(jsonNode);
    }

    /**
     * a
     *
     * @param fromValue a
     * @param tClass    a
     * @param <T>       a
     * @return a
     */
    public static <T> T convertValue(Object fromValue, Class<T> tClass) {
        return mapper().convertValue(fromValue, constructType(tClass));
    }

    /**
     * a
     *
     * @param fromValue   a
     * @param toValueType a
     * @param <T>         a
     * @return a
     */
    public static <T> T convertValue(Object fromValue, Type toValueType) {
        return mapper().convertValue(fromValue, constructType(toValueType));
    }

    /**
     * a
     *
     * @param fromValue   a
     * @param toValueType a
     * @param <T>         a
     * @return a
     * @throws JsonProcessingException a
     */
    public static <T> T readValue(String fromValue, Type toValueType) throws JsonProcessingException {
        return mapper().readValue(fromValue, constructType(toValueType));
    }

    /**
     * 将对象转 json 底层调用 JacksonHelper.getObjectMapperIgnoreJsonIgnore().writeValueAsString()
     * 所以会忽略 JsonIgnore 注解 同时如果转换失败则在其内部消化异常 (打印) 并返回 ""
     *
     * @param value        a {@link Object} object.
     * @param defaultValue a {@link Object} object.
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

    /**
     * a
     *
     * @param value a
     * @return a
     * @throws JsonProcessingException a
     */
    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return mapper().writeValueAsString(value);
    }

    /**
     * 获取 ObjectMapper
     *
     * @return a
     */
    public static ObjectMapper mapper() {
        return OBJECT_MAPPER;
    }

    /**
     * a
     *
     * @param type a
     * @return a
     */
    public static JavaType constructType(Type type) {
        return mapper().getTypeFactory().constructType(type);
    }

    /**
     * a
     *
     * @param typeRef a
     * @return a
     */
    public static JavaType constructType(TypeReference<?> typeRef) {
        return mapper().getTypeFactory().constructType(typeRef);
    }

}
