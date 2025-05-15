package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.util.ObjectUtils.constructType;

/// ClassInfo 工厂
/// 因为 Class 基本是固定的 所以此处全局缓存
///
/// @author scx567888
/// @version 0.0.1
public final class ClassInfoFactory {

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
