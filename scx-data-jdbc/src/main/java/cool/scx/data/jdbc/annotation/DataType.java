package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 同时支持枚举和字面量 枚举用来实现多数据源匹配
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {

    String name();

    int length();

}
