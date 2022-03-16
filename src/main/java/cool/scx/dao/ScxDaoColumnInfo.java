package cool.scx.dao;

import cool.scx.annotation.Column;
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
public final class ScxDaoColumnInfo {

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
        this.columnName = CaseUtils.toSnake(this.field.getName());
        var column = field.getAnnotation(Column.class);
        if (column != null) {
            this.type = StringUtils.isNotBlank(column.type()) ? column.type() : SQLHelper.getMySQLTypeCreateName(field.getType());
        } else {
            this.type = SQLHelper.getMySQLTypeCreateName(field.getType());
        }
        this.normalDDL = initNormalDDL(this.columnName, this.type, column);
        this.specialDDL = initSpecialDDL(this.columnName, column);
        this.updateSetSQL = this.columnName + " = ?";
        this.insertValuesSQL = "?";
        this.selectSQL = this.fieldName().equals(this.columnName) ? this.columnName : this.columnName + " AS " + this.fieldName();
    }

    /**
     * 获取通常的 ddl
     *
     * @param name   a {@link java.lang.String} object
     * @param type   a {@link java.lang.String} object
     * @param column a {@link cool.scx.annotation.Column} object
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
            if (StringUtils.isNotBlank(column.defaultValue())) {
                tempList.add("DEFAULT " + column.defaultValue());
            }
            if (StringUtils.isNotBlank(column.onUpdateValue())) {
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
     * @param column a {@link cool.scx.annotation.Column} object
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
            list.add("UNIQUE KEY `unique_" + name + "`(`" + name + "`)");
        }
        if (column.needIndex()) {
            list.add("KEY `index_" + name + "`(`" + name + "`)");
        }
        return list.toArray(String[]::new);
    }

    /**
     * a
     *
     * @return a
     */
    public String fieldName() {
        return field.getName();
    }

    /**
     * a
     *
     * @return a
     */
    public String selectSQL() {
        return selectSQL;
    }

    /**
     * a
     *
     * @return a
     */
    public String columnName() {
        return columnName;
    }

    /**
     * a
     *
     * @return a
     */
    public String updateSetSQL() {
        return updateSetSQL;
    }

    /**
     * a
     *
     * @return a
     */
    public String insertValuesSQL() {
        return insertValuesSQL;
    }

    /**
     * 获取字段值
     *
     * @param target 字段所属实例对象
     * @return a
     */
    public Object getFieldValue(Object target) {
        try {
            return this.field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
