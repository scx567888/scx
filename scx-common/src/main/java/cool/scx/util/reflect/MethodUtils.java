package cool.scx.util.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.util.Collections.addAll;

public class MethodUtils {

    public static Method[] findMethods(Class<?> clazz) {
        var list = new ArrayList<Method>();
        while (clazz != null) {
            var methods = clazz.getDeclaredMethods();
            addAll(list, methods);
            clazz = clazz.getSuperclass();
        }
        return list.toArray(Method[]::new);
    }

}
