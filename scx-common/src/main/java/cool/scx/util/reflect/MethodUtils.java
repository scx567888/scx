package cool.scx.util.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodUtils {

    public static Method[] findMethods(Class<?> clazz) {
        var list = new ArrayList<Method>();
        while (clazz != null) {
            var fields = clazz.getDeclaredMethods();
            list.addAll(List.of(fields));
            clazz = clazz.getSuperclass();
        }
        return list.toArray(Method[]::new);
    }

}
