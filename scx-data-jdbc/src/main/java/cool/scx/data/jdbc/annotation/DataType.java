package cool.scx.data.jdbc.annotation;

import cool.scx.jdbc.JDBCType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataType
 *
 * @author scx567888
 * @version 0.0.1
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {

    JDBCType value();

    int length() default -1;

}
