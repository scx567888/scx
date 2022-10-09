package cool.scx.core.dao;

import cool.scx.core.ScxContext;
import cool.scx.core.annotation.Column;
import cool.scx.sql.ColumnInfo;
import cool.scx.sql.SQLHelper;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    private final String normalDDL;

    /**
     * 当前列对象特殊的 DDL 如设置是否为主键 是否创建索引 是否是唯一值 (建表语句片段 , 需和 normalDDL 一起使用才完整)
     */
    private final String[] specialDDL;

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

    /**
     * a
     *
     * @param field a
     */
    public ScxDaoColumnInfo(Field field) {
        this.field = field;
        var column = field.getAnnotation(Column.class);
        this.type = initType(field, column);
        this.columnName = initColumnName(field, column);
        this.normalDDL = initNormalDDL(this.columnName, this.type, column);
        this.specialDDL = initSpecialDDL(this.columnName, column);
        this.updateSetSQL = this.columnName + " = ?";
        this.insertValuesSQL = "?";
        this.selectSQL = this.fieldName().equals(this.columnName) ? this.columnName : this.columnName + " AS " + this.fieldName();
    }

    private static String initType(Field field, Column column) {
        if (column != null && StringUtils.notBlank(column.type())) {
            return column.type();
        } else {
            return SQLHelper.getMySQLTypeCreateName(field.getType());
        }
    }

    private static String initColumnName(Field field, Column column) {
        if (column != null && StringUtils.notBlank(column.columnName())) {
            return column.columnName();
        } else {
            return CaseUtils.toSnake(field.getName());
        }
    }

    /**
     * 获取通常的 ddl
     *
     * @param name   a {@link java.lang.String} object
     * @param type   a {@link java.lang.String} object
     * @param column a {@link cool.scx.core.annotation.Column} object
     * @return a
     */
    private static String initNormalDDL(String name, String type, Column column) {
        var tempList = new ArrayList<String>();
        tempList.add("`" + name + "`");
        tempList.add(type);
        if (column != null) {
            tempList.add(column.notNull() || column.primaryKey() ? "NOT NULL" : "NULL");
            if (column.autoIncrement()) {
                tempList.add("AUTO_INCREMENT");
            }
            if (StringUtils.notBlank(column.defaultValue())) {
                tempList.add("DEFAULT " + column.defaultValue());
            }
            if (StringUtils.notBlank(column.onUpdateValue())) {
                tempList.add("ON UPDATE " + column.onUpdateValue());
            }
        } else {
            tempList.add("NULL");
        }
        return String.join(" ", tempList);
    }

    /**
     * 获取特殊的 ddl 如是否为主键 是否是唯一键 是否添加 索引 等
     *
     * @param name   a {@link java.lang.String} object
     * @param column a {@link cool.scx.core.annotation.Column} object
     * @return a
     */
    private static String[] initSpecialDDL(String name, Column column) {
        if (column == null) {
            return new String[0];
        }
        var list = new ArrayList<String>();
        if (column.primaryKey()) {
            list.add("PRIMARY KEY (`" + name + "`)");
        }
        if (column.unique()) {
            if (ScxContext.coreConfig().tombstone()) {
                list.add("UNIQUE KEY `unique_" + name + "_tombstone`(`" + name + "`, `tombstone`)");
            } else {
                list.add("UNIQUE KEY `unique_" + name + "`(`" + name + "`)");
            }
        }
        if (column.needIndex()) {
            list.add("KEY `index_" + name + "`(`" + name + "`)");
        }
        return list.toArray(String[]::new);
    }

    @Override
    public Field javaField() {
        return field;
    }

    /**
     * a
     *
     * @return a
     */
    @Override
    public String selectSQL() {
        return selectSQL;
    }

    /**
     * a
     *
     * @return a
     */
    @Override
    public String columnName() {
        return columnName;
    }

    /**
     * a
     *
     * @return a
     */
    @Override
    public String updateSetSQL() {
        return updateSetSQL;
    }

    /**
     * a
     *
     * @return a
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
    public String normalDDL() {
        return normalDDL;
    }

    /**
     * a
     *
     * @return a
     */
    public String[] specialDDL() {
        return specialDDL;
    }

    /**
     * a
     *
     * @return a
     */
    public String type() {
        return type;
    }

}
