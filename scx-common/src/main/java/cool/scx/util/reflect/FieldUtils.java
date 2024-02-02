package cool.scx.util.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static java.util.Collections.addAll;

public final class FieldUtils {

    public static Field[] findFields(Class<?> clazz) {
        var list = new ArrayList<Field>();
        while (clazz != null && !clazz.isInterface()) {
            var fields = clazz.getDeclaredFields();
            addAll(list, fields);
            clazz = clazz.getSuperclass();
        }
        return list.toArray(Field[]::new);
    }

}
