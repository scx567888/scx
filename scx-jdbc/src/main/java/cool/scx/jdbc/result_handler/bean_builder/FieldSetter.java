package cool.scx.jdbc.result_handler.bean_builder;

import cool.scx.jdbc.type_handler.TypeHandler;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.ScxReflect;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;

import static cool.scx.reflect.AccessModifier.PUBLIC;
import static cool.scx.reflect.ClassKind.RECORD;

/// FieldSetter
///
/// @author scx567888
/// @version 0.0.1
final class FieldSetter {

    private final FieldInfo fieldInfo;
    private final String columnName;
    private TypeHandler<?> typeHandler;

    ///
    FieldSetter(FieldInfo fieldInfo, String columnName) {
        this.fieldInfo = fieldInfo;
        this.columnName = columnName;
        this.typeHandler = null;
    }

    static FieldSetter of(FieldInfo field, Function<Field, String> columnNameMapping) {
        field.setAccessible(true);
        var columnName = columnNameMapping.apply(field.rawField());
        //若 columnNameMapping 提供空值, 则回退到 field.getName()
        if (columnName == null) {
            columnName = field.name();
        }
        return new FieldSetter(field, columnName);
    }

    static FieldSetter[] ofArray(Class<?> type, Function<Field, String> columnNameMapping) {
        //todo 这里可能有问题
        var classInfo = (ClassInfo) ScxReflect.typeOf(type);
        var fields = classInfo.classKind() == RECORD ? classInfo.allFields() : Stream.of(classInfo.allFields()).filter(c -> c.accessModifier() == PUBLIC).toArray(FieldInfo[]::new);
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
