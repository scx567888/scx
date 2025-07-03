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
    ClassInfo classInfo();

}
