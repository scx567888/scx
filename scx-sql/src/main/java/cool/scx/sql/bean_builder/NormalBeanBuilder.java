package cool.scx.sql.bean_builder;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;
import cool.scx.sql.mapping.TableInfo;
import cool.scx.util.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public NormalBeanBuilder(Class<T> type, TableInfo<?> tableInfo) {
        this.constructor = findNoArgsConstructor(type);
        this.fieldSetters = ofArray(type, tableInfo);
        this.constructor.setAccessible(true);
    }

    public NormalBeanBuilder(Class<T> type) {
        this(type, null);
    }

    static FieldSetter[] ofArray(Class<?> type, TableInfo<?> tableInfo) {
        var fields = FieldUtils.findFields(type);
        var fieldSetters = new FieldSetter[fields.length];
        if (tableInfo == null) {
            for (int i = 0; i < fields.length; i = i + 1) {
                fieldSetters[i] = new FieldSetter(fields[i]);
            }
        } else {
            for (int i = 0; i < fields.length; i = i + 1) {
                var columnInfo = tableInfo.getColumnInfo(fields[i].getName());
                if (columnInfo == null) {
                    fieldSetters[i] = new FieldSetter(fields[i]);
                } else {
                    fieldSetters[i] = new FieldSetter(fields[i], columnInfo.columnName());
                }

            }
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
