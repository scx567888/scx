package cool.scx.sql.bean_builder;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public final class RecordBeanBuilder<T> implements BeanBuilder<T> {

    private final Constructor<T> constructor;

    private final FieldSetter[] fieldSetters;

    public RecordBeanBuilder(Class<T> type) {
        this.constructor = findConstructor(type);
        this.fieldSetters = initFieldSetters(this.constructor.getParameters(), type.getDeclaredFields());
    }

    /**
     * 寻找参数长度最长的构造函数
     */
    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findConstructor(Class<T> type) {
        var constructors = type.getDeclaredConstructors();
        var defaultConstructor = constructors[0];
        for (var constructor : constructors) {
            if (defaultConstructor.getParameterCount() < constructor.getParameterCount()) {
                defaultConstructor = constructor;
            }
        }
        defaultConstructor.setAccessible(true);
        return (Constructor<T>) defaultConstructor;
    }

    /**
     * 根据构造函数的参数顺序重新排序 fieldSetter
     *
     * @param parameters a
     * @param fields     a
     * @return a
     */
    private static FieldSetter[] initFieldSetters(Parameter[] parameters, Field[] fields) {
        var map = FieldSetter.ofMap(fields);
        var temp = new FieldSetter[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            temp[i] = map.get(parameters[i].getName());
        }
        return temp;
    }

    @Override
    public T createBean(ResultSet rs, int[] indexInfo) throws SQLException {
        var objs = new Object[fieldSetters.length];
        for (int i = 0; i < fieldSetters.length; i = i + 1) {
            if (indexInfo[i] != -1) {// -1 需要跳过
                objs[i] = fieldSetters[i].getObject(rs, indexInfo[i]);
            }
        }
        return newInstance(objs);
    }

    @Override
    public FieldSetter[] fieldSetters() {
        return fieldSetters;
    }

    private T newInstance(Object... objs) {
        try {
            return this.constructor.newInstance(objs);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
