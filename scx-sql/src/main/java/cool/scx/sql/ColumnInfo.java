package cool.scx.sql;

import java.lang.reflect.Field;

/**
 * <p>ColumnInfo interface.</p>
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface ColumnInfo {

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
     *
     * @return a {@link java.lang.String} object
     */
    String selectSQL();

    /**
     * 更新时的 sql 片段 提前生成好,以提高性能
     * <p>
     * 片段 : [ user_name = :userName ]
     * <br>
     * SQL : [ update user set user_name = :userName where id = 1 ]
     *
     * @return a {@link java.lang.String} object
     */
    String updateSetSQL();

    /**
     * 插入时 的 sql 片段 提前生成好,以提高性能
     *
     * @return a {@link java.lang.String} object
     * <p>
     * 片段 : [ :userName ]
     * <br>
     * SQL : [ insert into (user_name) values(:userName) ]
     */
    String insertValuesSQL();

    /**
     * 列名称
     *
     * @return a
     */
    String columnName();

    /**
     * 对应 java 的 字段(Field)
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

}
