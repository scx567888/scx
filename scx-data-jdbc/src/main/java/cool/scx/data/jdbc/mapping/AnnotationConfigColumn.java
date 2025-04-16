package cool.scx.data.jdbc.mapping;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.jdbc.mapping.type.TypeColumn;
import cool.scx.reflect.FieldInfo;

import static cool.scx.common.constant.AnnotationValueHelper.getRealValue;
import static cool.scx.common.util.CaseUtils.toSnake;

/// AnnotationConfigColumn
///
/// @author scx567888
/// @version 0.0.1
public class AnnotationConfigColumn implements TypeColumn {

    private final FieldInfo javaField;
    private final String columnName;
    private final AnnotationConfigDataType dataType;
    private final String defaultValue;
    private final String onUpdate;
    private final boolean notNull;
    private final boolean autoIncrement;
    private final boolean primary;
    private final boolean unique;
    private final boolean index;

    public AnnotationConfigColumn(FieldInfo javaField) {
        this.javaField = javaField;
        this.javaField.setAccessible(true);
        var column = javaField.findAnnotation(Column.class);
        var defaultColumnName = toSnake(javaField.name());
        var defaultDataType = new AnnotationConfigDataType(this.javaField.type());
        if (column != null) {
            var _columnName = getRealValue(column.columnName());
            var _defaultValue = getRealValue(column.defaultValue());
            var _onUpdate = getRealValue(column.onUpdate());

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

    public FieldInfo javaField() {
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
