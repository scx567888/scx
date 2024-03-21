package cool.scx.common.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;

public final class ConstructorUtils {

    /**
     * 寻找 无参构造函数 (不支持成员类)
     *
     * @param type c
     * @param <T>  a
     * @return a
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findNoArgsConstructor(Class<T> type) {
        Constructor<?> noArgsConstructor = null;
        for (var constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                noArgsConstructor = constructor;
                break;
            }
        }
        if (noArgsConstructor == null) {
            throw new IllegalArgumentException("寻找 无参 构造函数失败, type " + type.getName());
        }
        return (Constructor<T>) noArgsConstructor;
    }

    /**
     * 寻找 Record 规范构造参数
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findRecordConstructor(Class<T> type) {
        Constructor<?> canonicalConstructor = null;
        var recordComponents = type.getRecordComponents();
        var recordComponentTypes = Arrays.stream(recordComponents).map(RecordComponent::getType).toArray();
        for (var constructor : type.getDeclaredConstructors()) {
            // 判断参数类型是否匹配
            var matched = Arrays.equals(recordComponentTypes, constructor.getParameterTypes());
            if (matched) {
                canonicalConstructor = constructor;
                break;
            }
        }
        if (canonicalConstructor == null) {
            throw new IllegalArgumentException("寻找 Record 规范构造函数失败, type " + type.getName());
        }
        return (Constructor<T>) canonicalConstructor;
    }

}
