package cool.scx.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cool.scx.constant.ScxDateTimeFormatter.yyyy_MM_dd;
import static cool.scx.constant.ScxDateTimeFormatter.yyyy_MM_dd_HH_mm_ss;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class JacksonHelper {

    /**
     * 默认的 NullKey 序列化器
     */
    private static final NullKeySerializer DEFAULT_NULL_KEY_SERIALIZER = new NullKeySerializer();

    /**
     * 获取针对日期处理的 jackson module;
     * <br>
     * 仅仅是在 jackson-datatype-jsr310 包的基础上 添加了一些自定义的日期序列化格式
     *
     * @return a {@link com.fasterxml.jackson.datatype.jsr310.JavaTimeModule} object
     */
    private static JavaTimeModule initJavaTimeModule() {
        var javaTimeModule = new JavaTimeModule();
        //因为其内部默认使用 ISO-8601 标准 作为日期处理格式
        //如 DateTimeFormatter.ISO_LOCAL_DATE_TIME , DateTimeFormatter.ISO_LOCAL_TIME
        //但是这里我们需要针对一些 常见的日期格式 如 [LocalDateTime] 进行更友好的序列化格式处理 所以这里使用 自定义的 DateTimeFormatter

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(yyyy_MM_dd_HH_mm_ss));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(yyyy_MM_dd_HH_mm_ss));

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(yyyy_MM_dd));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(yyyy_MM_dd));
        return javaTimeModule;
    }

    /**
     * 获取 jsonMapper
     *
     * @param formatter a
     * @return jsonMapper
     */
    public static JsonMapper initJsonMapper(DateTimeFormatter formatter) {
        return initObjectMapper(JsonMapper.builder(), formatter);
    }

    /**
     * a
     *
     * @return a
     */
    public static JsonMapper initJsonMapper() {
        return initJsonMapper(yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 获取 xmlMapper
     *
     * @param formatter a
     * @return xmlMapper
     */
    public static XmlMapper initXmlMapper(DateTimeFormatter formatter) {
        return initObjectMapper(XmlMapper.builder(), formatter);
    }

    /**
     * a
     *
     * @return a
     */
    public static XmlMapper initXmlMapper() {
        return initXmlMapper(yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 根据 MapperBuilder 获取 ObjectMapper 对象 并对默认属性进行一些设置,具体如下
     * 如需获得原始的 ObjectMapper 对象请使用 {@link com.fasterxml.jackson.databind.cfg.MapperBuilder}; 自行创建
     * 1, 针对 LocalDateTime 类型设置默认的日期格式化格式 默认为 {@link cool.scx.constant.ScxDateTimeFormatter#yyyy_MM_dd_HH_mm_ss} 决定
     * 2, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES 设置为  false
     * 3, SerializationFeature.FAIL_ON_EMPTY_BEANS          设置为  false
     * 4, NullKeySerializer                                 设置为  JacksonHelper.NULL_KEY_SERIALIZER
     *
     * @param <M>           a M class
     * @param <B>           a B class
     * @param mapperBuilder a {@link com.fasterxml.jackson.databind.cfg.MapperBuilder} object
     * @param formatter     a {@link java.time.format.DateTimeFormatter} object
     * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} object
     */
    static <M extends ObjectMapper, B extends MapperBuilder<M, B>> M initObjectMapper(MapperBuilder<M, B> mapperBuilder, DateTimeFormatter formatter) {
        // 初始化一个 JsonMapper 构建器
        var objectMapper = mapperBuilder
                // 注册 module 用来识别一些特定的类型
                .addModule(initJavaTimeModule())
                // 遇到未知属性是否抛出异常 (默认为 : true) 例如 json 中包含的属性 bean 中没有 这时会抛出异常
                // 在此设置 false 表示遇到上述情况时不抛出异常
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 当 待序列化的对象没有任何可以序列化的属性(字段)时是否抛出异常
                // 如 public class EmptyClass { }
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).build();
        // 获取序列化空值 处理器 一般用于处理例如 map.put(null,"abc"); 之类 key 为空的
        objectMapper.getSerializerProvider().setNullKeySerializer(JacksonHelper.DEFAULT_NULL_KEY_SERIALIZER);
        return objectMapper;
    }

    /**
     * 设置 IgnoreJsonIgnore
     *
     * @param mapper m
     * @param <M>    a M class
     * @return m
     */
    static <M extends ObjectMapper> M setIgnoreJsonIgnore(M mapper) {
        mapper.setAnnotationIntrospector(new IgnoreJsonIgnore());
        return mapper;
    }

    /**
     * <p>setIgnoreNullValue.</p>
     *
     * @param mapper a M object
     * @param <M>    a M class
     * @return a M object
     */
    static <M extends ObjectMapper> M setIgnoreNullValue(M mapper) {
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    /**
     * 针对 HashMap 中可能出现的 null key 这里做特殊处理
     */
    private static class NullKeySerializer extends JsonSerializer<Object> {
        public static final String NULL_KEY = "";

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                throw JsonMappingException.from(gen, "NullKeySerializer is only for serializing null values.");
            }
            gen.writeFieldName(NULL_KEY);
        }

    }

    /**
     * 忽略 JsonIgnore 注解
     * <p>
     * 这里可能有点难以理解 我们假设一下这种情况 一个实体类的某个字段不想再向前台发送时序列化某个字段
     * 如 password 但是在前台向后台发送 json 时则需要进行序列化
     * 我们可以使用此类 通过 objectMapper.setAnnotationIntrospector(new IgnoreJsonIgnore());
     * 来设置一个 objectMapper 使其忽略 @JsonIgnore 注解
     * 同时还可以创建另一个 普通的 objectMapper 使其正常识别 @JsonIgnore 注解
     * 换句话说就是使 @JsonIgnore 注解只在 像前台发送数据时使用
     * 项目内部使用时则不会理会 @JsonIgnore 注解
     */
    private static class IgnoreJsonIgnore extends JacksonAnnotationIntrospector {

        /**
         * 此方法默认会调用 _isIgnorable 来查找 是否具有 需要忽略的注解
         * 而 _isIgnorable 则会依次查找是否存在以下两个注解
         * JsonIgnore(Jackson 提供) Transient(JDK 7 提供)
         * 如果有则使用 注解的 value 否则返回 false
         * 这里因为需要屏蔽掉所有的 JsonIgnore 或 Transient 注解故直接返回 false
         *
         * @param m m
         * @return r
         */
        @Override
        public boolean hasIgnoreMarker(AnnotatedMember m) {
            return false;
        }

    }

}
