package cool.scx.reflect;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public interface AnnotatedElementInfo {

    /// 元素上的注解
    Annotation[] annotations();

    /// 获取所有的注解 包括继承自父级的注解 (假设存在继承的话, 如类或方法)
    default Annotation[] allAnnotations() {
        return annotations();
    }

    /// 查找指定类型的注解
    default <T extends Annotation> T findAnnotation(Class<T> annotationClass) {
        var annotations = annotations();
        for (var annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    /// 查找所有指定类型的注解
    default <T extends Annotation> List<T> findAnnotations(Class<T> annotationClass) {
        var annotations = annotations();
        var result = new ArrayList<T>();
        for (var annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                result.add((T) annotation);
            }
        }
        return result;
    }

    /// 从整个继承层次查找 指定的注解
    default <T extends Annotation> T findAnnotationFromAll(Class<T> annotationClass) {
        var annotations = allAnnotations();
        for (var annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    /// 从整个继承层次查找 所有注解
    default <T extends Annotation> List<T> findAnnotationsFromAll(Class<T> annotationClass) {
        var annotations = allAnnotations();
        var result = new ArrayList<T>();
        for (var annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                result.add((T) annotation);
            }
        }
        return result;
    }

}
