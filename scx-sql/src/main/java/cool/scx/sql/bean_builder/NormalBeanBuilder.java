package cool.scx.sql.bean_builder;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;
import cool.scx.sql.TableInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public final class NormalBeanBuilder<T> implements BeanBuilder<T> {

    private final Constructor<T> constructor;

    private final FieldSetter[] fieldSetters;

    public NormalBeanBuilder(Class<T> type, TableInfo<?> tableInfo) {
        this.constructor = findConstructor(type);
        this.fieldSetters = FieldSetter.ofArray(type.getFields(), tableInfo);
    }

    public NormalBeanBuilder(Class<T> type) {
        this.constructor = findConstructor(type);
        this.fieldSetters = FieldSetter.ofArray(type.getFields());
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findConstructor(Class<T> clazz) {
        var constructors = clazz.getConstructors();
        Constructor<?> defaultConstructor = null;
        for (var constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
                break;
            }
        }
        if (defaultConstructor == null) {
            throw new RuntimeException(clazz + " : 必须有一个无参构造函数 !!!");
        }
        defaultConstructor.setAccessible(true);
        return (Constructor<T>) defaultConstructor;
    }

    @Override
    public T createBean(ResultSet rs, int[] indexInfo) throws SQLException {
        T t = newInstance();
        for (int i = 0; i < fieldSetters.length; i = i + 1) {
            if (indexInfo[i] != -1) {// -1 需要跳过
                fieldSetters[i].set(t, rs, indexInfo[i]);
            }
        }
        return t;
    }

    @Override
    public FieldSetter[] fieldSetters() {
        return fieldSetters;
    }

    private T newInstance() {
        try {
            return this.constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
