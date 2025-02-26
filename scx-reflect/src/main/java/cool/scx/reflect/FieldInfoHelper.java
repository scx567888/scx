package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/// FieldInfoHelper
///
/// @author scx567888
/// @version 0.0.1
final class FieldInfoHelper {

    public static String _findName(Field field) {
        return field.getName();
    }

    public static JavaType _findType(FieldInfo fieldInfo) {
        return ReflectHelper._findType(fieldInfo.field().getGenericType(), fieldInfo.classInfo());
    }

    public static Annotation[] _findAnnotations(Field field) {
        return field.getDeclaredAnnotations();
    }

}
