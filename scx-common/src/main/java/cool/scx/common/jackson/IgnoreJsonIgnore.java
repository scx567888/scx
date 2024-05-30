package cool.scx.common.jackson;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

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
public class IgnoreJsonIgnore extends JacksonAnnotationIntrospector {

    /**
     * 默认的 NullKey 序列化器
     */
    public static final IgnoreJsonIgnore IGNORE_JSON_IGNORE = new IgnoreJsonIgnore();

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
