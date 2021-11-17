package cool.scx.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 处理对象的工具类<br>
 * 本质上就是对 ObjectMapper 进行了一些简单的封装
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ObjectUtils {

    /**
     * 默认 LocalDateTime 格式化类
     */
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 忽略 Jackson 注解的 objectMapper  用于前台向后台发送数据和 后台数据序列化
     */
    private static final ObjectMapper OBJECT_MAPPER = getObjectMapper(false);

    /**
     * 类型工厂
     */
    private static final TypeFactory TYPE_FACTORY = OBJECT_MAPPER.getTypeFactory();

    /**
     * 使用 Jackson 注解的 objectMapper 用于向前台发送数据
     */
    private static final ObjectMapper OBJECT_MAPPER_USE_ANNOTATIONS = getObjectMapper(true);

    /**
     * map 类 Type ,转换用
     */
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {

    };

    /**
     * 处理字符串，基础类型以及对应的包装类型
     *
     * @param value       需要处理的值
     * @param targetClass 需要返回的类型
     * @param <T>         T
     * @return 处理后的值
     */
    public static <T> T convertValue(Object value, Class<T> targetClass) {
        return OBJECT_MAPPER.convertValue(value, targetClass);
    }

    /**
     * <p>convertValue.</p>
     *
     * @param value a {@link java.lang.Object} object
     * @param type  a {@link java.lang.reflect.Type} object
     * @param <T>   a T class
     * @return a T object
     */
    public static <T> T convertValue(Object value, Type type) {
        return OBJECT_MAPPER.convertValue(value, TYPE_FACTORY.constructType(type));
    }

    /**
     * <p>beanToMap.</p>
     *
     * @param fromValue      a {@link java.lang.Object} object.
     * @param toValueTypeRef a {@link com.fasterxml.jackson.core.type.TypeReference} object
     * @param <T>            a T class
     * @return a {@link java.util.Map} object.
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    /**
     * <p>convertValueToMap.</p>
     *
     * @param obj a {@link java.lang.Object} object
     * @return a {@link java.util.Map} object
     */
    public static Map<String, Object> convertValueToMap(Object obj) {
        return OBJECT_MAPPER.convertValue(obj, MAP_TYPE);
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

    /**
     * <p>readValue.</p>
     *
     * @param src          a {@link java.io.File} object
     * @param valueTypeRef a {@link com.fasterxml.jackson.core.type.TypeReference} object
     * @param <T>          a T class
     * @return a T object
     * @throws java.io.IOException if any.
     */
    public static <T> T readValue(File src, TypeReference<T> valueTypeRef) throws IOException {
        return OBJECT_MAPPER.readValue(src, valueTypeRef);
    }

    /**
     * 对象转 json 不使用 注解如 @JsonIgnore
     *
     * @param o a {@link java.lang.Object} object.
     * @return a {@link java.lang.String} object.
     * @throws com.fasterxml.jackson.core.JsonProcessingException if any.
     */
    public static String writeValueAsString(Object o) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(o);
    }

    /**
     * 对象转 json 不使用 注解如 @JsonIgnore
     *
     * @param o          a {@link java.lang.Object} object.
     * @param defaultStr a {@link java.lang.String} object
     * @return a {@link java.lang.String} object.
     */
    public static String writeValueAsString(Object o, String defaultStr) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultStr;
        }
    }

    /**
     * <p>JsonToTree.</p>
     *
     * @param json a {@link java.lang.String} object.
     * @return a {@link com.fasterxml.jackson.databind.JsonNode} object.
     * @throws com.fasterxml.jackson.core.JsonProcessingException if any.
     */
    public static JsonNode readTree(String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(json);
    }

    /**
     * <p>JsonToTree.</p>
     *
     * @param json a {@link java.lang.String} object.
     * @param type a {@link java.lang.Class} object.
     * @param <T>  a T object.
     * @return a {@link com.fasterxml.jackson.databind.JsonNode} object.
     * @throws com.fasterxml.jackson.core.JsonProcessingException if any.
     */
    public static <T> T readValue(String json, Class<T> type) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, type);
    }

    /**
     * <p>JsonToBean.</p>
     *
     * @param json a {@link java.lang.String} object
     * @param type a {@link java.lang.reflect.Type} object
     * @param <T>  a T class
     * @return a T object
     * @throws com.fasterxml.jackson.core.JsonProcessingException if any.
     */
    public static <T> T readValue(String json, Type type) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, TYPE_FACTORY.constructType(type));
    }

    /**
     * <p>jsonToMap.</p>
     *
     * @param json a {@link java.lang.String} object.
     * @return a {@link java.util.Map} object.
     * @throws com.fasterxml.jackson.core.JsonProcessingException if any.
     */
    public static Map<String, Object> readValueToMap(String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, MAP_TYPE);
    }


    /**
     * <p>jsonToMap.</p>
     *
     * @param jsonFile a {@link java.lang.String} object.
     * @return a {@link java.util.Map} object.
     * @throws java.io.IOException if any.
     */
    public static Map<String, Object> readValueToMap(File jsonFile) throws IOException {
        return OBJECT_MAPPER.readValue(jsonFile, MAP_TYPE);
    }

    /**
     * <p>jsonNodeToBean.</p>
     *
     * @param jsonNode a {@link com.fasterxml.jackson.databind.JsonNode} object.
     * @param type     a {@link java.lang.Class} object.
     * @param <T>      a T object.
     * @return a T object.
     * @throws java.io.IOException if any.
     */
    public static <T> T readValue(JsonNode jsonNode, Type type) throws IOException {
        var reader = OBJECT_MAPPER.readerFor(TYPE_FACTORY.constructType(type));
        return reader.readValue(jsonNode);
    }

    public static TypeFactory getTypeFactory() {
        return TYPE_FACTORY;
    }

    /**
     * 对象转 json 使用 注解如 @JsonIgnore
     *
     * @param o a {@link java.lang.Object} object.
     * @return a {@link java.lang.String} object.
     * @throws com.fasterxml.jackson.core.JsonProcessingException if any.
     */
    public static String writeValueAsStringUseAnnotations(Object o) throws JsonProcessingException {
        return OBJECT_MAPPER_USE_ANNOTATIONS.writeValueAsString(o);
    }

    /**
     * 对象转 json 使用 注解如 @JsonIgnore
     *
     * @param o          a {@link java.lang.Object} object.
     * @param defaultStr a {@link java.lang.String} object
     * @return a {@link java.lang.String} object.
     */
    public static String writeValueAsStringUseAnnotations(Object o, String defaultStr) {
        try {
            return OBJECT_MAPPER_USE_ANNOTATIONS.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultStr;
        }
    }

    /**
     * <p>valueToTree.</p>
     *
     * @param object a {@link java.lang.Object} object
     * @return a {@link com.fasterxml.jackson.databind.JsonNode} object
     */
    public static JsonNode valueToTreeUseAnnotations(Object object) {
        return OBJECT_MAPPER_USE_ANNOTATIONS.valueToTree(object);
    }


    /**
     * 获取 objectMapper 对象 (注意!!! 这里会对默认属性进行一些设置,具体如下)
     * <br>
     * 如需修改请使用 objectMapper.configure(xxx, true|false);
     * <p>
     * 过滤注解 (MapperFeature.USE_ANNOTATIONS, false)
     * <p>
     * 未知字段抛出错误 (FAIL_ON_UNKNOWN_PROPERTIES, false)
     * <p>
     * 空bean抛出错误  (FAIL_ON_EMPTY_BEANS, false)
     *
     * @param useAnnotations a boolean
     * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} object
     */
    public static ObjectMapper getObjectMapper(boolean useAnnotations) {
        //初始化正常的 JsonMapper 构建器
        var jsonMapper = JsonMapper.builder()
                //注册 module 用来识别一些特定的类型
                .addModule(getJavaTimeModule())
                //不使用注解
                .configure(MapperFeature.USE_ANNOTATIONS, useAnnotations)
                //遇到未知属性是否抛出异常
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //遇到空 bean 是否抛出异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .build();
        //获取序列化空值 处理器 一般用于处理例如 map.put(null,"abc"); 之类 key 为空的
        jsonMapper.getSerializerProvider().setNullKeySerializer(getNullKeySerializer());
        return jsonMapper;
    }

    /**
     * <p>getJavaTimeModule.</p>
     *
     * @return a {@link com.fasterxml.jackson.databind.module.SimpleModule} object
     */
    public static SimpleModule getJavaTimeModule() {
        var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DEFAULT_DATETIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DEFAULT_DATETIME_FORMATTER));
        return javaTimeModule;
    }

    /**
     * <p>getNullKeySerializer.</p>
     *
     * @return a {@link com.fasterxml.jackson.databind.JsonSerializer} object
     */
    public static JsonSerializer<Object> getNullKeySerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeFieldName("");
            }
        };
    }

}
