package cool.scx.sql.bean_builder;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;
import cool.scx.sql.TableInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public final class RecordBeanBuilder<T> implements BeanBuilder<T> {

    private final Constructor<T> constructor;

    private final FieldSetter[] fieldSetters;

    public RecordBeanBuilder(Class<T> type, TableInfo<?> tableInfo) {
        this.constructor = findConstructor(type);
        this.fieldSetters = initFieldSetters(this.constructor.getParameters(), ofMap(type.getDeclaredFields(), tableInfo));
    }

    public RecordBeanBuilder(Class<T> type) {
        this.constructor = findConstructor(type);
        this.fieldSetters = initFieldSetters(this.constructor.getParameters(), ofMap(type.getDeclaredFields()));
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
     * @param map        a
     * @return a
     */
    private static FieldSetter[] initFieldSetters(Parameter[] parameters, Map<String, FieldSetter> map) {
        var temp = new FieldSetter[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            temp[i] = map.get(parameters[i].getName());
        }
        return temp;
    }

    private static Map<String, FieldSetter> ofMap(Field[] fields) {
        var map = new HashMap<String, FieldSetter>();
        for (var field : fields) {
            map.put(field.getName(), FieldSetter.of(field));
        }
        return map;
    }

    private static Map<String, FieldSetter> ofMap(Field[] fields, TableInfo<?> tableInfo) {
        var map = new HashMap<String, FieldSetter>();
        for (var field : fields) {
            map.put(field.getName(), FieldSetter.of(field, tableInfo.getColumnInfo(field.getName())));
        }
        return map;
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
