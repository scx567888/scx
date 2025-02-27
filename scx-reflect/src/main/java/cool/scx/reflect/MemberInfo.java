package cool.scx.reflect;

import java.lang.annotation.Annotation;

///  类成员接口
public interface MemberInfo {

    ClassInfo classInfo();

    AccessModifier accessModifier();

    void setAccessible(boolean flag);

    Annotation[] annotations();

    default <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        var annotations = annotations();
        for (var annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }

}
