package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.util.ObjectUtils.constructType;

public final class ReflectHelper {

    private static final Map<JavaType, ClassInfo> CLASS_INFO_CACHE = new HashMap<>();

    public static ClassInfo getClassInfo(Class<?> javaType) {
        return getClassInfo(constructType(javaType));
    }

    public static ClassInfo getClassInfo(JavaType javaType) {
        var classInfo = CLASS_INFO_CACHE.get(javaType);
        if (classInfo == null) {
            classInfo = new ClassInfo(javaType);
            CLASS_INFO_CACHE.put(javaType, classInfo);
        }
        return classInfo;
    }

}
