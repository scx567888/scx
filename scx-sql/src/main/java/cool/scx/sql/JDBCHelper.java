package cool.scx.sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 用于处理不同数据库语言间的区别 以及驱动的区别
 */
public interface JDBCHelper {

    /**
     * 根据连接 获取数据库中所有的字段
     *
     * @param con          连接
     * @param databaseName 数据库名称
     * @param tableName    表名称
     * @return 如果表存在返回所有字段的名称 否则返回 null
     * @throws java.sql.SQLException s
     */
    List<String> getTableAllColumnNames(Connection con, String databaseName, String tableName) throws SQLException;

    /**
     * 检查是否需要修复表
     *
     * @param tableInfo a
     * @return true 需要 false 不需要
     * @throws java.sql.SQLException e
     */
    boolean checkNeedFixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException;

    /**
     * 获取建表语句
     *
     * @return s
     */
    String getCreateTableDDL(TableInfo<?> tableInfo);

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    String initNormalDDL(ColumnInfo column);

    /**
     * 当前列对象特殊的 DDL 如设置是否为主键 是否创建索引 是否是唯一值 (建表语句片段 , 需和 normalDDL 一起使用才完整)
     */
    String[] initSpecialDDL(ColumnInfo column);

    /**
     * a
     *
     * @param tableInfo a
     * @throws java.sql.SQLException a
     */
    void fixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException;

    /**
     * 获取修复表的语句
     *
     * @param nonExistentColumnName java 字段的名称 (注意 : fieldNames 中存在但 allFields 中不存在的则会忽略)
     * @return a
     */
    String getAlertTableDDL(List<? extends ColumnInfo> nonExistentColumnName, String tableName);

    boolean canHandler(Statement preparedStatement);

    boolean canHandler(DataSource dataSource);

    default String getSQL(Statement preparedStatement){
        return preparedStatement.toString();
    }

}
