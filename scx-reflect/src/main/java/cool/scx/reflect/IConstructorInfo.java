package cool.scx.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/// 仅用做规范
public interface IConstructorInfo extends IExecutableInfo {

    Constructor<?> constructor();

    AccessModifier accessModifier();

    void setAccessible(boolean flag);

    <T> T newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException;

}
