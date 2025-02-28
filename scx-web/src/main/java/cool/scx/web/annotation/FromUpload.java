package cool.scx.web.annotation;

import cool.scx.http.media.multi_part.MultiPartPart;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/// 从上传中获取参数 请标识在 [MultiPartPart] 类型的参数上
///
/// @author scx567888
/// @version 0.0.1
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromUpload {

    /// 路径参数名称 默认为空
    /// 为空的情况下会将参数名称作为 路径参数名称
    String value() default NULL;

    /// 是否必填
    boolean required() default true;

}
