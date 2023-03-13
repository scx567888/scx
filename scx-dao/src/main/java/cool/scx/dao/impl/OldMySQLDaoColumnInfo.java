package cool.scx.dao.impl;

import java.lang.reflect.Field;

/**
 * BaseDaoColumnInfo
 */
@Deprecated
public class OldMySQLDaoColumnInfo extends AnnotationConfigColumnInfo {

    private String updateSetSQL;
    private String selectSQL;

    /**
     * a
     *
     * @param javaField a
     */
    public OldMySQLDaoColumnInfo(Field javaField) {
        super(javaField);
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
    public final String updateSetSQL() {
        if (updateSetSQL == null) {
            updateSetSQL = this.columnName() + " = ?";
        }
        return updateSetSQL;
    }

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

    /**
     * 插入时 的 sql 片段 强烈建议提前生成好!!! 以提高性能
     * <br>
     * 举例 :
     * <br>
     * 片段 : [ :userName ]
     * <br>
     * SQL : [ insert into (user_name) values(:userName) ]
     */
    public final String insertValuesSQL() {
        return "?";
    }

}
