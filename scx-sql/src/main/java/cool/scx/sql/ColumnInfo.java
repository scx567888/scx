package cool.scx.sql;

import java.lang.reflect.Field;

/**
 * <p>ColumnInfo.</p>
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface ColumnInfo {

    /**
     * 查询时(select) 的 sql 片段 强烈建议提前生成好 !!! 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ user_name as userName ] ,
     * <br>
     * 如 查询列名和 java 字段名相同则直接返回列名 , 如 : [ password as password ] 简化为 [ password ]
     * <br>
     * SQL : [ select user_name as userName from user where id = 1 ]
     *
     * @return a {@link java.lang.String} object
     */
    default String selectSQL() {
        return this.fieldName().equals(this.columnName()) ? this.columnName() : this.columnName() + " AS " + this.fieldName();
    }

    /**
     * 更新时的 sql 片段 强烈建议提前生成好!!! 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ user_name = :userName ]
     * <br>
     * SQL : [ update user set user_name = :userName where id = 1 ]
     */
    default String updateSetSQL() {
        return this.columnName() + " = ?";
    }

    /**
     * 插入时 的 sql 片段 强烈建议提前生成好!!! 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ :userName ]
     * <br>
     * SQL : [ insert into (user_name) values(:userName) ]
     */
    default String insertValuesSQL() {
        return "?";
    }

    /**
     * 列名称 (数据库中的列名称)
     */
    default String columnName() {
        return fieldName();
    }

    /**
     * 对应 java 的 字段 (Field)
     *
     * @return a
     */
    Field javaField();

    /**
     * 对应 java 的字段的名称 用于过滤
     *
     * @return a
     */
    default String fieldName() {
        return javaField().getName();
    }

    /**
     * 获取字段值
     *
     * @param target 字段所属实例对象
     * @return a
     */
    default Object getFieldValue(Object target) {
        try {
            return javaField().get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default boolean notNull() {
        return false;
    }

    default boolean primaryKey() {
        return false;
    }

    default boolean autoIncrement() {
        return false;
    }

    default String defaultValue() {
        return null;
    }

    default String onUpdateValue() {
        return null;
    }

    /**
     * 类型  (数据库中的类型 , 目前仅在建表时使用)
     */
    default String type() {
        return "VARCHAR(128)";
    }

    default boolean unique() {
        return false;
    }

    default boolean needIndex() {
        return false;
    }

}
