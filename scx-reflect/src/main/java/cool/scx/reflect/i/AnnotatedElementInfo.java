package cool.scx.reflect.i;

import java.lang.annotation.Annotation;

/// 可被注解的元素
public interface AnnotatedElementInfo {

    /// 元素上的注解
    Annotation[] annotations();

}
