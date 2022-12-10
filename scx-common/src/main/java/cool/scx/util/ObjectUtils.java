package cool.scx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static cool.scx.util.JacksonHelper.*;

/**
 * 处理对象的工具类<br>
 * 本质上就是对 {@link com.fasterxml.jackson.databind.ObjectMapper} 进行了一些简单的封装
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ObjectUtils {

    /**
     * 因为 java 无法方便的存储泛型 使用 TypeReference 创建一些常用的类型
     * 此类为 Map 类型
     */
    public static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    /**
     * a
     */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * Constant <code>JSON_MAPPER</code>
     */
    private static final JsonMapper JSON_MAPPER = initJsonMapper();

    /**
     * Constant <code>XML_MAPPER</code>
     */
    private static final XmlMapper XML_MAPPER = initXmlMapper();

    /**
     * Constant <code>JSON_MAPPER_IGNORE_JSON_IGNORE</code>
     */
    private static final JsonMapper JSON_MAPPER_IGNORE_JSON_IGNORE = setIgnoreJsonIgnore(initJsonMapper());

    /**
     * Constant <code>XML_MAPPER_IGNORE_JSON_IGNORE</code>
     */
    private static final XmlMapper XML_MAPPER_IGNORE_JSON_IGNORE = setIgnoreJsonIgnore(initXmlMapper());

    /**
     * Constant <code>JSON_MAPPER_IGNORE_NULL_VALUE</code>
     */
    private static final JsonMapper JSON_MAPPER_IGNORE_NULL_VALUE = setIgnoreNullValue(initJsonMapper());

    /**
     * Constant <code>XML_MAPPER_IGNORE_NULL_VALUE</code>
     */
    private static final XmlMapper XML_MAPPER_IGNORE_NULL_VALUE = setIgnoreNullValue(initXmlMapper());

    /**
     * Constant <code>JSON_MAPPER_IGNORE_NULL_VALUE_AND_IGNORE_JSON_IGNORE</code>
     */
    private static final JsonMapper JSON_MAPPER_IGNORE_NULL_VALUE_AND_IGNORE_JSON_IGNORE = setIgnoreJsonIgnore(setIgnoreNullValue(initJsonMapper()));

    /**
     * Constant <code>XML_MAPPER_IGNORE_NULL_VALUE_AND_IGNORE_JSON_IGNORE</code>
     */
    private static final XmlMapper XML_MAPPER_IGNORE_NULL_VALUE_AND_IGNORE_JSON_IGNORE = setIgnoreJsonIgnore(setIgnoreNullValue(initXmlMapper()));

    /**
     * 获取 jsonMapper
     *
     * @param options a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     */
    public static JsonMapper jsonMapper(Option... options) {
        var info = new Option.Info(options);
        if (info.ignoreJsonIgnore && !info.ignoreNullValue) {
            return JSON_MAPPER_IGNORE_JSON_IGNORE;
        } else if (info.ignoreJsonIgnore) {
            return JSON_MAPPER_IGNORE_NULL_VALUE_AND_IGNORE_JSON_IGNORE;
        } else if (info.ignoreNullValue) {
            return JSON_MAPPER_IGNORE_NULL_VALUE;
        } else {
            return JSON_MAPPER;
        }
    }

    /**
     * 获取 xmlMapper
     *
     * @param options a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     */
    public static XmlMapper xmlMapper(Option... options) {
        var info = new Option.Info(options);
        if (info.ignoreJsonIgnore && !info.ignoreNullValue) {
            return XML_MAPPER_IGNORE_JSON_IGNORE;
        } else if (info.ignoreJsonIgnore) {
            return XML_MAPPER_IGNORE_NULL_VALUE_AND_IGNORE_JSON_IGNORE;
        } else if (info.ignoreNullValue) {
            return XML_MAPPER_IGNORE_NULL_VALUE;
        } else {
            return XML_MAPPER;
        }
    }

    /**
     * a
     *
     * @param type a
     * @return a
     */
    public static JavaType constructType(Type type) {
        return JSON_MAPPER.getTypeFactory().constructType(type);
    }

    /**
     * a
     *
     * @param typeRef a
     * @return a
     */
    public static JavaType constructType(TypeReference<?> typeRef) {
        return constructType(typeRef.getType());
    }

    /**
     * a
     *
     * @param fromValue a
     * @param javaType  a
     * @param <T>       a
     * @param options   a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     */
    public static <T> T convertValue(Object fromValue, JavaType javaType, Option... options) {
        return jsonMapper(options).convertValue(fromValue, javaType);
    }

    /**
     * a
     *
     * @param fromValue a
     * @param tClass    a
     * @param <T>       a
     * @param options   a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     */
    public static <T> T convertValue(Object fromValue, Class<T> tClass, Option... options) {
        return jsonMapper(options).convertValue(fromValue, constructType(tClass));
    }

    /**
     * a
     *
     * @param fromValue   a
     * @param toValueType a
     * @param <T>         a
     * @param options     a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     */
    public static <T> T convertValue(Object fromValue, Type toValueType, Option... options) {
        return jsonMapper(options).convertValue(fromValue, constructType(toValueType));
    }

    /**
     * a
     *
     * @param fromValue      a
     * @param toValueTypeRef a
     * @param <T>            a
     * @param options        a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef, Option... options) {
        return jsonMapper(options).convertValue(fromValue, constructType(toValueTypeRef));
    }

    /**
     * 将对象转 json 底层调用 JSON_MAPPER.writeValueAsString()
     * 所以会忽略 JsonIgnore 注解 同时如果转换失败则在其内部消化异常 (打印) 并返回 ""
     *
     * @param value        a {@link java.lang.Object} object.
     * @param defaultValue a {@link java.lang.Object} object.
     * @param options      a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a {@link java.lang.String} object.
     */
    public static String toJson(Object value, String defaultValue, Option... options) {
        try {
            return toJson(value, options);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 将对象转 xml 底层调用 XML_MAPPER.writeValueAsString()
     * 所以会忽略 JsonIgnore 注解 同时如果转换失败则在其内部消化异常 (打印) 并返回 ""
     *
     * @param value        a {@link java.lang.Object} object.
     * @param defaultValue a {@link java.lang.Object} object.
     * @param options      a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a {@link java.lang.String} object.
     */
    public static String toXml(Object value, String defaultValue, Option... options) {
        try {
            return toXml(value, options);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * a
     *
     * @param value   a
     * @param options a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     * @throws com.fasterxml.jackson.core.JsonProcessingException a
     */
    public static String toJson(Object value, Option... options) throws JsonProcessingException {
        return jsonMapper(options).writeValueAsString(value);
    }

    /**
     * a
     *
     * @param value   a
     * @param options a {@link cool.scx.util.ObjectUtils.Option} object
     * @return a
     * @throws com.fasterxml.jackson.core.JsonProcessingException a
     */
    public static String toXml(Object value, Option... options) throws JsonProcessingException {
        return xmlMapper(options).writeValueAsString(value);
    }

    /**
     * a
     *
     * @param source a
     * @return a
     */
    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[]) source;
        }
        if (source == null) {
            return EMPTY_OBJECT_ARRAY;
        }
        if (source instanceof Collection) {
            return ((Collection<?>) source).toArray();
        }
        if (source.getClass().isArray()) {
            var length = Array.getLength(source);
            var arr = new Object[length];
            if (source instanceof byte[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof short[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof int[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof long[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof float[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof double[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof boolean[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            } else if (source instanceof char[] arrSource) {
                for (int i = 0; i < length; i = i + 1) {
                    arr[i] = arrSource[i];
                }
            }
            return arr;
        }
        throw new IllegalArgumentException("源数据无法转换为数组对象 !!!");
    }

    /**
     * 将嵌套的 map 扁平化
     *
     * @param sourceMap 源 map
     * @param parentKey a {@link java.lang.String} object.
     * @return 扁平化后的 map
     */
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

    /**
     * 将嵌套的 map 扁平化
     *
     * @param sourceMap 源 map
     * @return 扁平化后的 map
     */
    public static Map<String, Object> flatMap(Map<?, ?> sourceMap) {
        return flatMap0(sourceMap, null);
    }

    public enum Option {

        /**
         * 忽略 空值 如 原数据 user { name = "123" age = null}
         * 默认输出为  { "name" : "123" , "age" : null}
         * 启用此参数后则变为 { "name" : "123" }
         */
        IGNORE_NULL_VALUE,

        /**
         * 忽略 {@link com.fasterxml.jackson.annotation.JsonIgnore} 注解
         */
        IGNORE_JSON_IGNORE;

        private static class Info {

            boolean ignoreNullValue = false;
            boolean ignoreJsonIgnore = false;

            Info(Option... options) {
                for (var option : options) {
                    switch (option) {
                        case IGNORE_NULL_VALUE -> this.ignoreNullValue = true;
                        case IGNORE_JSON_IGNORE -> this.ignoreJsonIgnore = true;
                    }
                }
            }

        }

    }

}
