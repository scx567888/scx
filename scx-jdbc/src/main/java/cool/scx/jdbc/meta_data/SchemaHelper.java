package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;

import static cool.scx.common.util.StringUtils.notBlank;
import static cool.scx.jdbc.meta_data.MetaDataHelper.getCurrentSchema;
import static cool.scx.jdbc.sql.SQL.sql;

/**
 * 架构管理工具
 */
public final class SchemaHelper {

    /**
     * 获取迁移 SQL  (当前只支持 创建不存在的新列)
     * todo 需要支持删除旧列和修改列
     *
     * @param oldTable 旧表
     * @param newTable 新表
     * @param dialect  方言
     * @return sql 不需要迁移语句则返回 null
     */
    public static String getMigrateSQL(Table oldTable, Table newTable, Dialect dialect) {
        var verifyResult = verifyTable(oldTable, newTable);
        // 获取不存在的字段
        var needAdd = verifyResult.needAdd();
        if (needAdd.length > 0) {
            return dialect.ddlBuilder().getAlertTableDDL(needAdd, newTable);
        }
        return null;
    }

    public static TableVerifyResult verifyTable(Table oldTable, Table newTable) {
        var needAdd = new ArrayList<Column>();
        var needRemove = new ArrayList<Column>();
        var needChange = new ArrayList<Column>();
        for (var oldColumn : oldTable.columns()) {
            var column = newTable.getColumn(oldColumn.name());
            if (column == null) {
                needRemove.add(oldColumn);
            }
        }
        for (var newColumn : newTable.columns()) {
            var column = oldTable.getColumn(newColumn.name());
            if (column == null) {
                needAdd.add(newColumn);
            }
        }
        return new TableVerifyResult(
                needAdd.toArray(Column[]::new),
                needRemove.toArray(Column[]::new),
                needChange.toArray(Column[]::new)
        );
    }

    /**
     * 和当前数据中同名表进行比对 进行修复  (若表不存在则创建,若存在则只添加不存在的列)
     *
     * @param tableInfo a
     * @throws java.sql.SQLException a
     */
    public static void fixTable(Table tableInfo, JDBCContext jdbcContext) throws SQLException {
        try (var con = jdbcContext.dataSource().getConnection()) {
            // 查找同名表
            var tableMetaData = getCurrentSchema(con).refreshTables(con).getTable(tableInfo.name());

            // 没有这个表 创建表 , 有表 获取迁移 SQL
            var fixTableSQL = tableMetaData == null ? jdbcContext.dialect().ddlBuilder().getCreateTableDDL(tableInfo) : getMigrateSQL(tableMetaData.refreshColumns(con), tableInfo, jdbcContext.dialect());

            if (notBlank(fixTableSQL)) {
                jdbcContext.sqlRunner().execute(con, sql(fixTableSQL));
            }
        }
    }

    /**
     * 检查是否需要修复表
     *
     * @param tableInfo a
     * @return true 需要 false 不需要
     * @throws java.sql.SQLException e
     */
    public static boolean checkNeedFixTable(Table tableInfo, DataSource dataSource) throws SQLException {
        try (var con = dataSource.getConnection()) {
            // 查找同名表
            var tableMetaData = getCurrentSchema(con).refreshTables(con).getTable(tableInfo.name());
            // 没有这个表 或者 验证所需字段 大于 0
            return tableMetaData == null || verifyTable(tableMetaData.refreshColumns(con), tableInfo).needAdd().length > 0;
        }
    }

    public record TableVerifyResult(Column[] needAdd, Column[] needRemove, Column[] needChange) {

    }

}
