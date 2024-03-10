package cool.scx.data.jdbc;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.jdbc.mapping.type.TypeColumn;

import java.lang.reflect.Field;

import static cool.scx.util.CaseUtils.toSnake;
import static cool.scx.util.reflect.AnnotationUtils.getAnnotationValue;

public class AnnotationConfigColumn implements TypeColumn {

    private final Field javaField;
    private final String columnName;
    private final AnnotationConfigDataType dataType;
    private final String defaultValue;
    private final String onUpdate;
    private final boolean notNull;
    private final boolean autoIncrement;
    private final boolean primary;
    private final boolean unique;
    private final boolean index;

    public AnnotationConfigColumn(Field javaField) {
        this.javaField = javaField;
        this.javaField.setAccessible(true);
        var column = javaField.getAnnotation(Column.class);
        var defaultColumnName = toSnake(javaField.getName());
        var defaultDataType = new AnnotationConfigDataType(this.javaField.getType());
        if (column != null) {
            var _columnName = getAnnotationValue(column.columnName());
            var _defaultValue = getAnnotationValue(column.defaultValue());
            var _onUpdate = getAnnotationValue(column.onUpdate());

            this.columnName = _columnName != null ? _columnName : defaultColumnName;
            this.dataType = column.dataType().length > 0 ? new AnnotationConfigDataType(column.dataType()[0]) : defaultDataType;
            this.defaultValue = _defaultValue;
            this.onUpdate = _onUpdate;
            this.notNull = column.notNull();
            this.autoIncrement = column.autoIncrement();
            this.primary = column.primary();
            this.unique = column.unique();
            this.index = column.index();
        } else {
            this.columnName = defaultColumnName;
            this.dataType = defaultDataType;
            this.defaultValue = null;
            this.onUpdate = null;
            this.notNull = false;
            this.autoIncrement = false;
            this.primary = false;
            this.unique = false;
            this.index = false;
        }
    }

    public Field javaField() {
        return javaField;
    }

    @Override
    public String name() {
        return columnName;
    }

    @Override
    public AnnotationConfigDataType dataType() {
        return dataType;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String onUpdate() {
        return onUpdate;
    }

    @Override
    public boolean notNull() {
        return notNull;
    }

    @Override
    public boolean autoIncrement() {
        return autoIncrement;
    }

    @Override
    public boolean primary() {
        return primary;
    }

    @Override
    public boolean unique() {
        return unique;
    }

    @Override
    public boolean index() {
        return index;
    }

    public Object javaFieldValue(Object target) {
        try {
            return this.javaField.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
