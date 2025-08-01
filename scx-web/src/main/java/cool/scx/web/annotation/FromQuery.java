package cool.scx.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValues.NULL;

/// 从 query 获取参数
///
/// @author scx567888
/// @version 0.0.1
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromQuery {

    /// 查询参数名称 默认为空
    /// 为空的情况下会将方法参数名称作为 查询参数名称
    String value() default NULL;

    /// 将 查询参数聚合
    boolean merge() default false;

    /// 是否必填
    boolean required() default true;

}
