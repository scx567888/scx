package cool.scx.data.jdbc.bean_builder;

import cool.scx.data.jdbc.type_handler.TypeHandler;
import cool.scx.util.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
final class FieldSetter {

    private final Field javaField;
    private final String columnName;
    private TypeHandler<?> typeHandler;

    /**
     *
     */
    FieldSetter(Field javaField, String columnName) {
        this.javaField = javaField;
        this.columnName = columnName;
        this.typeHandler = null;
    }

    static FieldSetter of(Field field, Function<Field, String> columnNameMapping) {
        field.setAccessible(true);
        var columnName = columnNameMapping.apply(field);
        //若 columnNameMapping 提供空值, 则回退到 field.getName()
        if (columnName == null) {
            columnName = field.getName();
        }
        return new FieldSetter(field, columnName);
    }

    static FieldSetter[] ofArray(Class<?> type, Function<Field, String> columnNameMapping) {
        var fields = FieldUtils.findFields(type);
        var fieldSetters = new FieldSetter[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            fieldSetters[i] = of(fields[i], columnNameMapping);
        }
        return fieldSetters;
    }

    public void setTypeHandler(TypeHandler<Object> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Field javaField() {
        return javaField;
    }

    public String columnName() {
        return columnName;
    }

    public TypeHandler<?> typeHandler() {
        return typeHandler;
    }

}
