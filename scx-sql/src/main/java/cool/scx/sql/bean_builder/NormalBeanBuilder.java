package cool.scx.sql.bean_builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static cool.scx.sql.bean_builder.FieldSetter.ofArray;
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

    @Override
    public T createBean(ResultSet rs, int[] indexInfo) throws SQLException {
        T t = newInstance();
        for (int i = 0; i < fieldSetters.length; i = i + 1) {
            if (indexInfo[i] != -1) {// -1 需要跳过
                var o = fieldSetters[i].typeHandler().getObject(rs, indexInfo[i]);
                if (o != null) {// 为空我们就跳过了
                    try {
                        fieldSetters[i].javaField().set(t, o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
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
