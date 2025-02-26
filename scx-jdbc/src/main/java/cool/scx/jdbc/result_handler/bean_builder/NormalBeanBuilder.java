package cool.scx.jdbc.result_handler.bean_builder;

import cool.scx.reflect.IClassInfo;
import cool.scx.reflect.IConstructorInfo;
import cool.scx.reflect.ReflectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * NormalBeanBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
final class NormalBeanBuilder<T> extends BeanBuilder<T> {

    private final IConstructorInfo constructor;
    private final FieldSetter[] fieldSetters;

    public NormalBeanBuilder(Class<T> type, Function<Field, String> columnNameMapping) {
        this.constructor = checkNoArgsConstructor(ReflectHelper.getClassInfo(type));
        this.constructor.setAccessible(true);
        this.fieldSetters = FieldSetter.ofArray(type, columnNameMapping);
    }

    public NormalBeanBuilder(Class<T> type) {
        this(type, Field::getName);
    }

    /**
     * 寻找 无参构造函数 (不支持成员类)
     *
     * @param classInfo c
     * @return a
     */
    private static IConstructorInfo checkNoArgsConstructor(IClassInfo classInfo) {
        var defaultConstructor = classInfo.defaultConstructor();
        if (defaultConstructor == null) {
            throw new IllegalArgumentException("寻找 无参 构造函数失败, type " + classInfo.type().getRawClass().getName());
        }
        return defaultConstructor;
    }

    @Override
    public T createBean(ResultSet rs, int[] indexInfo) throws SQLException {
        T t = newInstance();
        for (int i = 0; i < fieldSetters.length; i = i + 1) {
            if (indexInfo[i] != -1) {// -1 需要跳过
                var o = fieldSetters[i].typeHandler().getObject(rs, indexInfo[i]);
                if (o != null) {// 为空我们就跳过了
                    try {
                        fieldSetters[i].fieldInfo().set(t, o);
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
