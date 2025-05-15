package cool.scx.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/// Autowired
///
/// @author scx567888
/// @version 0.0.1
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

    /// 名字
    String value() default NULL;

}
