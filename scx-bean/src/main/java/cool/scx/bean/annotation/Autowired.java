package cool.scx.bean.annotation;

import java.lang.annotation.*;

import static cool.scx.common.constant.AnnotationValue.NULL;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    /// 名字
    String value() default NULL;

    /// 是否必填
    boolean required() default true;

}
