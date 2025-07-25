package cool.scx.data.jdbc.mapping;

import cool.scx.jdbc.JDBCType;
import cool.scx.reflect.TypeInfo;

import static cool.scx.common.util.ClassUtils.isEnum;
import static cool.scx.jdbc.JDBCType.JSON;
import static cool.scx.jdbc.JDBCType.VARCHAR;

class AnnotationConfigHelper {

    public static JDBCType getDataTypeByJavaType(TypeInfo type) {
        //JavaType 先尝试使用 getRawClass 进行创建 失败后回退到 ObjectTypeHandler
        var c = getDataTypeByJavaType0(type.rawClass());
        if (c != null) {
            return c;
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
