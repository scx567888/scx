package cool.scx.reflect.i;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/// 构造函数 信息
public interface ConstructorInfo extends ExecutableInfo {

    /// 获取原始的 构造函数
    Constructor<?> rawConstructor();

    //************* 简化方法 **************

    @Override
    default void setAccessible(boolean flag) {
        rawConstructor().setAccessible(flag);
    }

    @SuppressWarnings("unchecked")
    default <T> T newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) rawConstructor().newInstance(args);
    }

}
