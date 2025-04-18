package cool.scx.data.jdbc.mapping;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.jdbc.JDBCType;

import java.lang.reflect.Type;

import static cool.scx.common.util.ClassUtils.isEnum;
import static cool.scx.jdbc.JDBCType.JSON;
import static cool.scx.jdbc.JDBCType.VARCHAR;

class AnnotationConfigHelper {

    public static JDBCType getDataTypeByJavaType(Type type) {
        if (type instanceof Class<?> clazz) {
            //普通 class 直接创建 失败后回退到 ObjectTypeHandler
            var c = getDataTypeByJavaType0(clazz);
            if (c != null) {
                return c;
            }
        } else if (type instanceof JavaType javaType) {
            //JavaType 先尝试使用 getRawClass 进行创建 失败后回退到 ObjectTypeHandler
            var c = getDataTypeByJavaType0(javaType.getRawClass());
            if (c != null) {
                return c;
            }
        }
        return JSON;
    }

    private static JDBCType getDataTypeByJavaType0(Class<?> clazz) {
        if (isEnum(clazz)) {
            return VARCHAR;
        } else {
            return JDBCType.getByJavaType(clazz);
        }
    }

}
