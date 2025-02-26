package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cool.scx.common.util.ObjectUtils.constructType;
import static cool.scx.common.util.ObjectUtils.resolveMemberType;

public class ReflectHelper {

    private static final Map<JavaType, IClassInfo> CLASS_INFO_CACHE = new HashMap<>();

    public static IClassInfo getClassInfo(Class<?> javaType) {
        return getClassInfo(constructType(javaType));
    }

    public static IClassInfo getClassInfo(JavaType javaType) {
        var classInfo = CLASS_INFO_CACHE.get(javaType);
        if (classInfo == null) {
            classInfo = new ClassInfo(javaType);
            CLASS_INFO_CACHE.put(javaType, classInfo);
        }
        return classInfo;
    }

    static AccessModifier _findAccessModifier(Set<AccessFlag> accessFlags) {
        if (accessFlags.contains(AccessFlag.PUBLIC)) {
            return AccessModifier.PUBLIC;
        }
        if (accessFlags.contains(AccessFlag.PROTECTED)) {
            return AccessModifier.PROTECTED;
        }
        if (accessFlags.contains(AccessFlag.PRIVATE)) {
            return AccessModifier.PRIVATE;
        }
        return AccessModifier.PACKAGE_PRIVATE;
    }

    static JavaType _findType(Type type, IClassInfo classInfo) {
        return resolveMemberType(type, classInfo.type().getBindings());
    }

}
