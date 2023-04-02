package cool.scx.orm.xdevapi;


import cool.scx.orm.xdevapi.type_handler.TypeHandler;
import cool.scx.orm.xdevapi.type_handler.TypeHandlerSelector;
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
    private final TypeHandler<?> typeHandler;

    public FieldSetter(Field field, String columnName) {
        this.javaField = field;
        this.columnName = columnName;
        this.javaField.setAccessible(true);
        this.typeHandler = TypeHandlerSelector.findTypeHandler(field.getGenericType());
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

    public String columnName() {
        return columnName;
    }

    public Field javaField() {
        return javaField;
    }

    public TypeHandler<?> typeHandler() {
        return typeHandler;
    }

}