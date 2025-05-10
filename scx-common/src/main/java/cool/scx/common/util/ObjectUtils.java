package cool.scx.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cool.scx.common.jackson.BuildOptions;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

import static cool.scx.common.jackson.JacksonHelper.createObjectMapper;

/// 处理对象的工具类
/// 本质上就是对 [com.fasterxml.jackson.databind.ObjectMapper] 进行了一些简单的封装
/// todo 我们是否真的需要如此多的 ObjectMapper 来完成忽略注解这件事
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectUtils {

    /// 因为 java 无法方便的存储泛型 使用 TypeReference 创建一些常用的类型
    /// 此类为 Map 类型
    public static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private static final JsonMapper JSON_MAPPER;
    private static final XmlMapper XML_MAPPER;
    private static final Map<BuildOptions, JsonMapper> JSON_MAPPER_CACHE = new HashMap<>();
    private static final Map<BuildOptions, XmlMapper> XML_MAPPER_CACHE = new HashMap<>();

    static {
        JSON_MAPPER = jsonMapper(new Options());
        XML_MAPPER = xmlMapper(new Options());
    }

    public static JsonMapper jsonMapper(Options options) {
        return JSON_MAPPER_CACHE.computeIfAbsent(options.toBuildOptions(), (k) -> createObjectMapper(JsonMapper.builder(), k));
    }

    public static XmlMapper xmlMapper(Options options) {
        return XML_MAPPER_CACHE.computeIfAbsent(options.toBuildOptions(), (k) -> createObjectMapper(XmlMapper.builder(), k));
    }

    public static JsonMapper jsonMapper() {
        return JSON_MAPPER;
    }

    public static XmlMapper xmlMapper() {
        return XML_MAPPER;
    }

    public static JavaType constructType(Type type) {
        return getTypeFactory().constructType(type);
    }

    public static JavaType constructType(TypeReference<?> typeRef) {
        return getTypeFactory().constructType(typeRef);
    }

    public static JavaType resolveMemberType(Type type, TypeBindings contextBindings) {
        return getTypeFactory().resolveMemberType(type, contextBindings);
    }

    public static TypeFactory getTypeFactory() {
        return jsonMapper().getTypeFactory();
    }

    public static <T> T convertValue(Object fromValue, JavaType javaType, Options options) {
        return jsonMapper(options).convertValue(fromValue, javaType);
    }

    public static <T> T convertValue(Object fromValue, Class<T> tClass, Options options) {
        return jsonMapper(options).convertValue(fromValue, constructType(tClass));
    }

    public static <T> T convertValue(Object fromValue, Type toValueType, Options options) {
        return jsonMapper(options).convertValue(fromValue, constructType(toValueType));
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef, Options options) {
        return jsonMapper(options).convertValue(fromValue, constructType(toValueTypeRef));
    }

    public static <T> T convertValue(Object fromValue, JavaType javaType) {
        return jsonMapper().convertValue(fromValue, javaType);
    }

    public static <T> T convertValue(Object fromValue, Class<T> tClass) {
        return jsonMapper().convertValue(fromValue, constructType(tClass));
    }

    public static <T> T convertValue(Object fromValue, Type toValueType) {
        return jsonMapper().convertValue(fromValue, constructType(toValueType));
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return jsonMapper().convertValue(fromValue, constructType(toValueTypeRef));
    }


    public static String toJson(Object value, String defaultValue, Options options) {
        try {
            return toJson(value, options);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String toXml(Object value, String defaultValue, Options options) {
        try {
            return toXml(value, options);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String toJson(Object value, String defaultValue) {
        try {
            return toJson(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String toXml(Object value, String defaultValue) {
        try {
            return toXml(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String toJson(Object value, Options options) throws JsonProcessingException {
        return jsonMapper(options).writeValueAsString(value);
    }

    public static String toXml(Object value, Options options) throws JsonProcessingException {
        return xmlMapper(options).writeValueAsString(value);
    }

    public static String toJson(Object value) throws JsonProcessingException {
        return jsonMapper().writeValueAsString(value);
    }

    public static String toXml(Object value) throws JsonProcessingException {
        return xmlMapper().writeValueAsString(value);
    }

    /// 将嵌套的 map 扁平化
    ///
    /// @param sourceMap 源 map
    /// @param parentKey a [java.lang.String] object.
    /// @return 扁平化后的 map
    private static Map<String, Object> flatMap0(Map<?, ?> sourceMap, String parentKey) {
        var result = new LinkedHashMap<String, Object>();
        var prefix = StringUtils.isBlank(parentKey) ? "" : parentKey + ".";
        sourceMap.forEach((key, value) -> {
            var newKey = prefix + key;
            if (value instanceof Map<?, ?> m) {
                result.putAll(flatMap0(m, newKey));
            } else {
                result.put(newKey, value);
            }
        });
        return result;
    }

    /// 将嵌套的 map 扁平化
    ///
    /// @param sourceMap 源 map
    /// @return 扁平化后的 map
    public static Map<String, Object> flatMap(Map<?, ?> sourceMap) {
        return flatMap0(sourceMap, null);
    }

    //todo 支持字段过滤器 (如何表现一个路径 是否应该拓展出一个 JsonPath)
    public static class Options {

        /// 忽略 空值 如 原数据 user { name = "123" age = null}
        /// 默认输出为  { "name" : "123" , "age" : null}
        /// 启用此参数后则变为 { "name" : "123" }
        boolean ignoreNullValue;

        /// 忽略 [com.fasterxml.jackson.annotation.JsonIgnore] 注解
        boolean ignoreJsonIgnore;
        boolean failOnUnknownProperties;
        boolean failOnEmptyBeans;
        Map<PropertyAccessor, JsonAutoDetect.Visibility> visibilityConfig;

        public Options() {
            this.ignoreNullValue = false;
            this.ignoreJsonIgnore = false;
            this.failOnUnknownProperties = false;
            this.failOnEmptyBeans = false;
            this.visibilityConfig = null;
        }

        public Options setIgnoreNullValue(boolean ignoreNullValue) {
            this.ignoreNullValue = ignoreNullValue;
            return this;
        }

        public Options setIgnoreJsonIgnore(boolean ignoreJsonIgnore) {
            this.ignoreJsonIgnore = ignoreJsonIgnore;
            return this;
        }

        public Options setFailOnUnknownProperties(boolean failOnUnknownProperties) {
            this.failOnUnknownProperties = failOnUnknownProperties;
            return this;
        }

        public Options setFailOnEmptyBeans(boolean failOnEmptyBeans) {
            this.failOnEmptyBeans = failOnEmptyBeans;
            return this;
        }

        public Options visibility(PropertyAccessor a, JsonAutoDetect.Visibility b) {
            if (this.visibilityConfig == null) {
                this.visibilityConfig = new HashMap<>();
            }
            this.visibilityConfig.put(a, b);
            return this;
        }

        BuildOptions toBuildOptions() {
            return new BuildOptions(this.ignoreNullValue, this.ignoreJsonIgnore, false, false, visibilityConfig);
        }

    }

    /// null -> true
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional<?> optional) {
            return optional.isEmpty();
        } else if (obj instanceof CharSequence charSequence) {
            return charSequence.isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection<?> collection) {
            return collection.isEmpty();
        } else if (obj instanceof Map<?, ?> map) {
            return map.isEmpty();
        } else {
            return false;
        }
    }

    /// null -> true
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

}
