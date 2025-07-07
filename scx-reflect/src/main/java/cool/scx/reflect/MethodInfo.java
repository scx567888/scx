package cool.scx.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/// MethodInfo
///
/// @author scx567888
/// @version 0.0.1
public interface MethodInfo extends ExecutableInfo {

    /// 原始 Method
    Method rawMethod();

    /// 名称
    String name();

    boolean isAbstract();

    /// 是否为 final 方法
    boolean isFinal();

    /// 是否为 static 方法
    boolean isStatic();

    boolean isNative();

    boolean isDefault();

    /// 返回值
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
