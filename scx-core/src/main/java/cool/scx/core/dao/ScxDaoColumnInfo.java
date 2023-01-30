package cool.scx.core.dao;

import cool.scx.core.annotation.Column;
import cool.scx.sql.base.BaseColumnInfo;

import java.lang.reflect.Field;

import static cool.scx.sql.SQLHelper.getMySQLTypeCreateName;
import static cool.scx.util.CaseUtils.toSnake;
import static cool.scx.util.StringUtils.notBlank;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxDaoColumnInfo extends BaseColumnInfo {

    private final String columnName;
    private final String type;
    private final boolean needIndex;
    private final boolean unique;
    private final String onUpdateValue;
    private final String defaultValue;
    private final boolean primaryKey;
    private final boolean autoIncrement;
    private final boolean notNull;

    /**
     * a
     *
     * @param javaField a
     */
    public ScxDaoColumnInfo(Field javaField) {
        super(javaField);
        var column = javaField.getAnnotation(Column.class);
        if (column != null) {
            this.type = notBlank(column.type()) ? column.type() : getMySQLTypeCreateName(javaField.getType());
            this.columnName = notBlank(column.columnName()) ? column.columnName() : toSnake(javaField.getName());
            this.needIndex = column.needIndex();
            this.unique = column.unique();
            this.onUpdateValue = column.onUpdateValue();
            this.defaultValue = column.defaultValue();
            this.primaryKey = column.primaryKey();
            this.autoIncrement = column.autoIncrement();
            this.notNull = column.notNull();
        } else {
            this.type = getMySQLTypeCreateName(javaField.getType());
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
    public boolean notNull() {
        return this.notNull;
    }

    @Override
    public boolean primaryKey() {
        return this.primaryKey;
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
    public String columnName() {
        return this.columnName;
    }

    @Override
    public String type() {
        return this.type;
    }

    @Override
    public boolean unique() {
        return this.unique;
    }

    @Override
    public boolean needIndex() {
        return this.needIndex;
    }

}
