package cool.scx.jdbc.result_handler.bean_builder;

import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.ScxReflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Function;

/// RecordBeanBuilder
///
/// @author scx567888
/// @version 0.0.1
final class RecordBeanBuilder<T> extends BeanBuilder<T> {

    private final ConstructorInfo constructor;

    private final FieldSetter[] fieldSetters;

    public RecordBeanBuilder(Class<T> type, Function<Field, String> columnNameMapping) {
        //todo 这里强转可能有问题
        this.constructor = checkRecordConstructor((ClassInfo) ScxReflect.typeOf(type));
        this.constructor.setAccessible(true);
        this.fieldSetters = sortFieldSetters(this.constructor.parameters(), FieldSetter.ofArray(type, columnNameMapping));
    }

    public RecordBeanBuilder(Class<T> type) {
        this(type, Field::getName);
    }

    /// 根据构造函数的参数顺序重新排序 fieldSetter
    ///
    /// @param parameters   a
    /// @param fieldSetters a
    /// @return a
    private static FieldSetter[] sortFieldSetters(ParameterInfo[] parameters, FieldSetter[] fieldSetters) {
        //使用 map 加速查找
        var map = new HashMap<String, FieldSetter>();
        for (var fieldSetter : fieldSetters) {
            map.put(fieldSetter.fieldInfo().name(), fieldSetter);
        }
        //根据 parameters 的参数顺序重排 fieldSetters
        var temp = new FieldSetter[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            //这里直接使用 getName 是安全的, 因为 Record 中规范构造函数的参数名称是固定的
            temp[i] = map.get(parameters[i].name());
        }
        return temp;
    }

    /// 寻找 Record 规范构造参数
    public static ConstructorInfo checkRecordConstructor(ClassInfo type) {
        var canonicalConstructor = type.recordConstructor();
        if (canonicalConstructor == null) {
            throw new IllegalArgumentException("寻找 Record 规范构造函数失败, type " + type.name());
        }
        return canonicalConstructor;
    }

    @Override
    public T createBean(ResultSet rs, int[] indexInfo) throws SQLException {
        var objs = new Object[fieldSetters.length];
        for (int i = 0; i < fieldSetters.length; i = i + 1) {
            if (indexInfo[i] != -1) {// -1 需要跳过
                objs[i] = fieldSetters[i].typeHandler().getObject(rs, indexInfo[i]);
            } else {// 这里我们使用 默认值
                objs[i] = fieldSetters[i].typeHandler().getDefaultValue();
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
