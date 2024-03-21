package cool.scx.data.jdbc.annotation;

import cool.scx.jdbc.standard.StandardDataType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {

    StandardDataType value();

    int length() default -1;

}
