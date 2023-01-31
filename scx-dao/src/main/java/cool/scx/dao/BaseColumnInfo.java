package cool.scx.dao;

import cool.scx.sql.ColumnInfo;

import java.lang.reflect.Field;

/**
 * BaseColumnInfo
 */
public class BaseColumnInfo implements ColumnInfo {

    protected final Field javaField;
    protected String updateSetSQL;
    protected String selectSQL;

    public BaseColumnInfo(Field javaField) {
        this.javaField = javaField;
    }

    @Override
    public final String updateSetSQL() {
        if (updateSetSQL == null) {
            updateSetSQL = this.columnName() + " = ?";
        }
        return updateSetSQL;
    }

    @Override
    public final String selectSQL() {
        if (selectSQL == null) {
            if (this.javaFieldName().equals(this.columnName())) {
                selectSQL = this.columnName();
            } else {
                selectSQL = this.columnName() + " AS " + this.javaFieldName();
            }
        }
        return selectSQL;
    }

    @Override
    public final String insertValuesSQL() {
        return "?";
    }

    @Override
    public Object javaFieldValue(Object target) {
        try {
            return this.javaField.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String columnName() {
        return javaFieldName();
    }

    @Override
    public String javaFieldName() {
        return this.javaField.getName();
    }

}
