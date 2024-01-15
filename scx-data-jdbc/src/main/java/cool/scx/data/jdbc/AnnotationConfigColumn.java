package cool.scx.data.jdbc;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.jdbc.ColumnMapping;

import java.lang.reflect.Field;

import static cool.scx.util.CaseUtils.toSnake;
import static cool.scx.util.StringUtils.notBlank;

public class AnnotationConfigColumn implements ColumnMapping {

    private final Field javaField;
    private final String type;
    private final String columnName;
    private final boolean needIndex;
    private final boolean unique;
    private final String onUpdateValue;
    private final String defaultValue;
    private final boolean primaryKey;
    private final boolean autoIncrement;
    private final boolean notNull;

    public AnnotationConfigColumn(Field javaField) {
        this.javaField = javaField;
        this.javaField.setAccessible(true);
        var column = javaField.getAnnotation(Column.class);
        if (column != null) {
            this.type = notBlank(column.type()) ? column.type() : null;
            this.columnName = notBlank(column.columnName()) ? column.columnName() : toSnake(javaField.getName());
            this.needIndex = column.needIndex();
            this.unique = column.unique();
            this.onUpdateValue = notBlank(column.onUpdateValue()) ? column.onUpdateValue() : null;
            this.defaultValue = notBlank(column.defaultValue()) ? column.defaultValue() : null;
            this.primaryKey = column.primaryKey();
            this.autoIncrement = column.autoIncrement();
            this.notNull = column.notNull();
        } else {
            this.type = null;
            this.columnName = toSnake(javaField.getName());
            this.needIndex = false;
            this.unique = false;
            this.onUpdateValue = null;
            this.defaultValue = null;
            this.primaryKey = false;
            this.autoIncrement = false;
            this.notNull = false;
        }
    }

    @Override
    public Field javaField() {
        return this.javaField;
    }

    @Override
    public String name() {
        return this.columnName;
    }

    @Override
    public String typeName() {
        return this.type;
    }

    @Override
    public Integer columnSize() {
        return null;
    }

    @Override
    public boolean notNull() {
        return this.notNull;
    }

    @Override
    public boolean autoIncrement() {
        return this.autoIncrement;
    }

    @Override
    public String defaultValue() {
        return this.defaultValue;
    }

    @Override
    public String onUpdateValue() {
        return this.onUpdateValue;
    }

    @Override
    public boolean unique() {
        return this.unique;
    }

    @Override
    public boolean primaryKey() {
        return this.primaryKey;
    }

    @Override
    public boolean index() {
        return this.needIndex;
    }

}
