package cool.scx.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface MethodInfo extends ExecutableInfo {

    Method rawMethod();

    String name();

    MethodType methodType();

    boolean isFinal();

    boolean isStatic();

    TypeInfo returnType();

    /// 获取当前方法重写的父类的方法 (可能为空)
    MethodInfo superMethod();

    //************ 简化操作 **************

    @Override
    default void setAccessible(boolean flag) {
        rawMethod().setAccessible(flag);
    }

    default Object invoke(Object obj, Object... args) throws InvocationTargetException, IllegalAccessException {
        return rawMethod().invoke(obj, args);
    }

}
