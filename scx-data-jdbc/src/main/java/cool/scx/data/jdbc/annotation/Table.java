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

    /// 表名称
    /// 不指定时为类名 
    /// 此处我们不进行任何隐式转换 比如 UserInfo -> user_info, 而是直接使用类名本身
    /// 这是经过严格考量推敲的, 最大化的减少开发人员的理解成本
    ///
    /// @return 表全限定名称
    String value() default NULL;

}
