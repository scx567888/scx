package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cool.scx.common.util.ObjectUtils.constructType;
import static cool.scx.common.util.ObjectUtils.resolveMemberType;
import static cool.scx.reflect.AccessModifier.*;

public class ReflectHelper {

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

    public static AccessModifier _findAccessModifier(Set<AccessFlag> accessFlags) {
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

    static AccessModifier _findAccessModifier(int m) {
        if (Modifier.isPublic(m)) {
            return PUBLIC;
        } else if (Modifier.isProtected(m)) {
            return PROTECTED;
        } else if (Modifier.isPrivate(m)) {
            return PRIVATE;
        } else {
            return PACKAGE_PRIVATE;
        }
    }

    static JavaType _findType(Type type, IClassInfo classInfo) {
        return resolveMemberType(type, classInfo.type().getBindings());
    }

}
