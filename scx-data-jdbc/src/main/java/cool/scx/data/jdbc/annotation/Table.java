package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/// Table
///
/// @author scx567888
/// @version 0.0.1
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /// 用于标注实体类对应的数据库表名.
    ///
    /// 如果未显式指定表名 (即 `value` 为空), 将默认使用类名作为表名.
    ///
    /// 本注解不会进行任何隐式的命名转换, 例如 `UserInfo` 不会自动转换为 `user_info`.
    ///
    /// 这种设计经过明确考量, 旨在保持命名一致性, 减少认知负担, 并避免因自动转换导致的歧义或错误.
    ///
    /// @return 表的名称（全限定），默认为类名
    String value() default NULL;

}
