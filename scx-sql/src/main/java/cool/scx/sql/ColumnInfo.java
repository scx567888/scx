package cool.scx.sql;

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
    String selectSQL();

    /**
     * 更新时的 sql 片段 强烈建议提前生成好!!! 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ user_name = :userName ]
     * <br>
     * SQL : [ update user set user_name = :userName where id = 1 ]
     */
    String updateSetSQL();

    /**
     * 插入时 的 sql 片段 强烈建议提前生成好!!! 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ :userName ]
     * <br>
     * SQL : [ insert into (user_name) values(:userName) ]
     */
    String insertValuesSQL();

    /**
     * 列名称 (数据库中的列名称)
     */
    String columnName();

    /**
     * 对应 java 的字段的名称 用于过滤
     *
     * @return a
     */
    String javaFieldName();

    /**
     * 获取字段值
     *
     * @param target 字段所属实例对象
     * @return a
     */
    Object javaFieldValue(Object target);

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
