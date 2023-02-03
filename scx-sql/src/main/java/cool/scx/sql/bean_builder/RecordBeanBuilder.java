package cool.scx.sql.bean_builder;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;
import cool.scx.sql.TableInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static cool.scx.sql.FieldSetter.ofArray;
import static cool.scx.util.reflect.ConstructorUtils.findRecordConstructor;

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
        var tempFieldSetters = ofArray(type, tableInfo);
        this.constructor = findRecordConstructor(type);
        this.fieldSetters = sortFieldSetters(this.constructor.getParameters(), tempFieldSetters);
        this.constructor.setAccessible(true);
    }

    public RecordBeanBuilder(Class<T> type) {
        this(type, null);
    }

    /**
     * 根据构造函数的参数顺序重新排序 fieldSetter
     *
     * @param parameters   a
     * @param fieldSetters a
     * @return a
     */
    private static FieldSetter[] sortFieldSetters(Parameter[] parameters, FieldSetter[] fieldSetters) {
        //使用 map 加速查找
        var map = new HashMap<String, FieldSetter>();
        for (var fieldSetter : fieldSetters) {
            map.put(fieldSetter.javaField().getName(), fieldSetter);
        }
        //根据 parameters 的参数顺序重排 fieldSetters
        var temp = new FieldSetter[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            //这里直接使用 getName 是安全的, 因为 Record 中规范构造函数的参数名称是固定的
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
