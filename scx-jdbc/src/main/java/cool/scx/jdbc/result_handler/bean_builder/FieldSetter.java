package cool.scx.jdbc.result_handler.bean_builder;

import cool.scx.common.reflect.FieldInfo;
import cool.scx.common.reflect.ReflectFactory;
import cool.scx.jdbc.type_handler.TypeHandler;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;

import static cool.scx.common.reflect.AccessModifier.PUBLIC;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
final class FieldSetter {

    private final FieldInfo fieldInfo;
    private final String columnName;
    private TypeHandler<?> typeHandler;

    /**
     *
     */
    FieldSetter(FieldInfo fieldInfo, String columnName) {
        this.fieldInfo = fieldInfo;
        this.columnName = columnName;
        this.typeHandler = null;
    }

    static FieldSetter of(FieldInfo field, Function<Field, String> columnNameMapping) {
        field.setAccessible(true);
        var columnName = columnNameMapping.apply(field.field());
        //若 columnNameMapping 提供空值, 则回退到 field.getName()
        if (columnName == null) {
            columnName = field.name();
        }
        return new FieldSetter(field, columnName);
    }

    static FieldSetter[] ofArray(Class<?> type, Function<Field, String> columnNameMapping) {
        var classInfo = ReflectFactory.getClassInfo(type);
        var fields = classInfo.isRecord() ? classInfo.allFields() : Stream.of(classInfo.allFields()).filter(c -> c.accessModifier() == PUBLIC).toArray(FieldInfo[]::new);
        var fieldSetters = new FieldSetter[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            fieldSetters[i] = of(fields[i], columnNameMapping);
        }
        return fieldSetters;
    }

    public void bindTypeHandler(TypeHandler<Object> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public FieldInfo fieldInfo() {
        return fieldInfo;
    }

    public String columnName() {
        return columnName;
    }

    public TypeHandler<?> typeHandler() {
        return typeHandler;
    }

}
