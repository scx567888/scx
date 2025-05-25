package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Table
///
/// @author scx567888
/// @version 0.0.1
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /// 用于标注实体类对应的数据库表名.
    ///
    /// @return 表的名称（全限定)
    String value();

}
