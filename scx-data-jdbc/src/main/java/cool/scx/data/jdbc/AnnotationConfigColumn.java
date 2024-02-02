package cool.scx.data.jdbc;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.jdbc.mapping.DataType;
import cool.scx.jdbc.mapping.base.BaseDataType;
import cool.scx.jdbc.mapping.type.TypeColumn;

import java.lang.reflect.Field;

import static cool.scx.util.CaseUtils.toSnake;
import static cool.scx.util.StringUtils.notBlank;

public class AnnotationConfigColumn implements TypeColumn {

    private final Field javaField;
    private final String columnName;
    private final DataType dataType;
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
        if (column != null) {
            this.columnName = notBlank(column.columnName()) ? column.columnName() : toSnake(javaField.getName());
            this.dataType = notBlank(column.dataType()) ? new BaseDataType(column.dataType()) : null;
            this.defaultValue = notBlank(column.defaultValue()) ? column.defaultValue() : null;
            this.onUpdate = notBlank(column.onUpdate()) ? column.onUpdate() : null;
            this.notNull = column.notNull();
            this.autoIncrement = column.autoIncrement();
            this.primary = column.primary();
            this.unique = column.unique();
            this.index = column.index();
        } else {
            this.columnName = toSnake(javaField.getName());
            this.dataType = null;
            this.defaultValue = null;
            this.onUpdate = null;
            this.notNull = false;
            this.autoIncrement = false;
            this.primary = false;
            this.unique = false;
            this.index = false;
        }
    }

    @Override
    public Field javaField() {
        return javaField;
    }

    @Override
    public String name() {
        return columnName;
    }

    @Override
    public DataType dataType() {
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
