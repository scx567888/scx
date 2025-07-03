package cool.scx.reflect.i;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ScxReflect {

    static final Map<Type, ClassInfo> CLASS_INFO_CACHE = new ConcurrentHashMap<>();

    //todo 多线程问题 ? 
    public static ClassInfo getClassInfo(Type type) {
        var classInfo = CLASS_INFO_CACHE.get(type);
        if (classInfo == null) {
            return new ClassInfoImpl(type);
        }
        return classInfo;
    }

}
