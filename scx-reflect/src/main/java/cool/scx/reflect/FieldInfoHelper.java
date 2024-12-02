package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;

/**
 * FieldInfoHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
class FieldInfoHelper {


    //*********************** FieldInfo START ********************

    static String _findName(FieldInfo fieldInfo) {
        return fieldInfo.field().getName();
    }

    static AccessModifier _findAccessModifier(FieldInfo fieldInfo) {
        return ReflectHelper._findAccessModifier(fieldInfo.field().getModifiers());
    }

    static JavaType _findType(FieldInfo fieldInfo) {
        return ReflectHelper._findType(fieldInfo.field().getGenericType(), fieldInfo.classInfo());
    }

    static Annotation[] _findAnnotations(FieldInfo fieldInfo) {
        return fieldInfo.field().getDeclaredAnnotations();
    }

    //*********************** FieldInfo END *******************

}
