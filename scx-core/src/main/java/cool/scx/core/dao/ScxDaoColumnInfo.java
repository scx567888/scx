package cool.scx.core.dao;

import cool.scx.core.annotation.Column;
import cool.scx.sql.ColumnInfo;

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
public final class ScxDaoColumnInfo implements ColumnInfo {

    /**
     * 对应 java 字段
     */
    private final Field field;

    /**
     * 列名称 (数据库中的列名称)
     */
    private final String columnName;

    /**
     * 类型  (数据库中的类型 , 目前仅在建表时使用)
     */
    private final String type;

    /**
     * 更新时的 sql 片段 提前生成好,以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ user_name = :userName ]
     * <br>
     * SQL : [ update user set user_name = :userName where id = 1 ]
     */
    private final String updateSetSQL;

    /**
     * 插入时 的 sql 片段 提前生成好,以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ :userName ]
     * <br>
     * SQL : [ insert into (user_name) values(:userName) ]
     */
    private final String insertValuesSQL;

    /**
     * 查询时(select) 的 sql 片段 提前生成好, 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ user_name as userName ] ,
     * <br>
     * 如 查询列名和 java 字段名相同则直接返回列名 , 如 : [ password as password ] 简化为 [ password ]
     * <br>
     * SQL : [ select user_name as userName from user where id = 1 ]
     */
    private final String selectSQL;

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
     * @param field a
     */
    public ScxDaoColumnInfo(Field field) {
        this.field = field;
        var column = field.getAnnotation(Column.class);
        if (column != null) {
            this.type = notBlank(column.type()) ? column.type() : getMySQLTypeCreateName(field.getType());
            this.columnName = notBlank(column.columnName()) ? column.columnName() : toSnake(field.getName());
            this.needIndex = column.needIndex();
            this.unique = column.unique();
            this.onUpdateValue = column.onUpdateValue();
            this.defaultValue = column.defaultValue();
            this.primaryKey = column.primaryKey();
            this.autoIncrement = column.autoIncrement();
            this.notNull = column.notNull();
        } else {
            this.type = getMySQLTypeCreateName(field.getType());
            this.columnName = toSnake(field.getName());
            this.needIndex = false;
            this.unique = false;
            this.onUpdateValue = null;
            this.defaultValue = null;
            this.primaryKey = false;
            this.autoIncrement = false;
            this.notNull = false;
        }
        this.updateSetSQL = this.columnName + " = ?";
        this.insertValuesSQL = "?";
        this.selectSQL = this.fieldName().equals(this.columnName) ? this.columnName : this.columnName + " AS " + this.fieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field javaField() {
        return field;
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

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public String selectSQL() {
        return selectSQL;
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public String columnName() {
        return columnName;
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public String updateSetSQL() {
        return updateSetSQL;
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public String insertValuesSQL() {
        return insertValuesSQL;
    }

    /**
     * a
     *
     * @return a
     */
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
