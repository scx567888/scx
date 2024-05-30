package cool.scx.data.jdbc.annotation;

import cool.scx.common.standard.JDBCType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {

    JDBCType value();

    int length() default -1;

}
