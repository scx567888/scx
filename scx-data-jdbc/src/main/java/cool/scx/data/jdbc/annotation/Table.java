package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValues.NULL;

/// Table
///
/// @author scx567888
/// @version 0.0.1
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /// 表名称
    /// 不指定时为类名转下划线如 UserInfo -> user_info
    ///
    /// @return 表全限定名称
    String value() default NULL;

}
