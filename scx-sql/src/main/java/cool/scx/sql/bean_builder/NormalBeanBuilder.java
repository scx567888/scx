package cool.scx.sql.bean_builder;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;
import cool.scx.util.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static cool.scx.util.reflect.ConstructorUtils.findNoArgsConstructor;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public final class NormalBeanBuilder<T> implements BeanBuilder<T> {

    private final Constructor<T> constructor;

    private final FieldSetter[] fieldSetters;

    public NormalBeanBuilder(Class<T> type, Function<Field, String> columnNameMapping) {
        this.constructor = findNoArgsConstructor(type);
        this.constructor.setAccessible(true);
        this.fieldSetters = ofArray(type, columnNameMapping);
    }

    public NormalBeanBuilder(Class<T> type) {
        this(type, Field::getName);
    }

    static FieldSetter[] ofArray(Class<?> type, Function<Field, String> columnNameMapping) {
        var fields = FieldUtils.findFields(type);
        var fieldSetters = new FieldSetter[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            var field = fields[i];
            var columnName = columnNameMapping.apply(field);
            //若 columnNameMapping 提供空值, 则回退到 field.getName()
            fieldSetters[i] = new FieldSetter(field, columnName != null ? columnName : field.getName());
        }
        return fieldSetters;
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
