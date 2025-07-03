package cool.scx.reflect.i;

import java.lang.reflect.Field;

public interface FieldInfo extends MemberInfo, AnnotatedElementInfo {

    /// 原始 Field
    Field rawField();

    /// 字段名称
    String name();

    /// 是否为 final 字段
    boolean isFinal();

    /// 字段本身的类型
    ClassInfo fieldType();

    //************* 简化操作 *****************

    default void setAccessible(boolean flag) {
        rawField().setAccessible(flag);
    }

    default void set(Object obj, Object value) throws IllegalAccessException {
        rawField().set(obj, value);
    }

    default Object get(Object obj) throws IllegalAccessException {
        return rawField().get(obj);
    }

}
