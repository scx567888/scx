package cool.scx.common.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static cool.scx.common.jackson.IgnoreJsonIgnore.IGNORE_JSON_IGNORE;
import static cool.scx.common.jackson.NullKeySerializer.NULL_KEY_SERIALIZER;

/**
 * JacksonHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class JacksonHelper {

    /**
     * 根据 MapperBuilder 获取 ObjectMapper 对象 并对默认属性进行一些设置,具体如下
     * 如需获得原始的 ObjectMapper 对象请使用 {@link com.fasterxml.jackson.databind.cfg.MapperBuilder}; 自行创建
     * 1, 针对 日期 类型设置 自定义的格式  {@link MyJavaTimeModule}
     * 2, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES 设置为  false
     * 3, SerializationFeature.FAIL_ON_EMPTY_BEANS          设置为  false
     * 4, NullKeySerializer                                 设置为  JacksonHelper.NULL_KEY_SERIALIZER
     *
     * @param <M>           a M class
     * @param <B>           a B class
     * @param mapperBuilder a {@link com.fasterxml.jackson.databind.cfg.MapperBuilder} object
     * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} object
     */
    public static <M extends ObjectMapper, B extends MapperBuilder<M, B>> M createObjectMapper(MapperBuilder<M, B> mapperBuilder, BuildOptions o) {
        // 初始化一个 JsonMapper 构建器
        mapperBuilder
                // 注册 module 用来识别一些特定的类型
                .addModule(new JavaTimeModule())
                .addModule(new MyJavaTimeModule())
                // 遇到未知属性是否抛出异常 (默认为 : true) 例如 json 中包含的属性 bean 中没有 这时会抛出异常
                // 在此设置 false 表示遇到上述情况时不抛出异常
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, o.failOnUnknownProperties())
                // 当 待序列化的对象没有任何可以序列化的属性(字段)时是否抛出异常
                // 如 public class EmptyClass { }
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, o.failOnEmptyBeans())
                .filterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));

        if (o.visibilityConfig() != null) {
            // 设置可见性
            o.visibilityConfig().forEach(mapperBuilder::visibility);
        }

        var objectMapper = mapperBuilder.build();

        if (o.ignoreJsonIgnore()) {
            // 设置 IgnoreJsonIgnore
            objectMapper.setAnnotationIntrospector(IGNORE_JSON_IGNORE);
        }

        if (o.ignoreNullValue()) {
            // setIgnoreNullValue
            objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        }

        // 获取序列化空值 处理器 一般用于处理例如 map.put(null,"abc"); 之类 key 为空的
        objectMapper.getSerializerProvider().setNullKeySerializer(NULL_KEY_SERIALIZER);
        return objectMapper;
    }

}
