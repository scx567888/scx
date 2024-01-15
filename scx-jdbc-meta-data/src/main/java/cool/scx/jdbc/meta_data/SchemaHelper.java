package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.sql.SQL;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.jdbc.dialect.DialectSelector.findDialect;
import static cool.scx.jdbc.meta_data.MetaDataHelper.initTables;
import static cool.scx.jdbc.meta_data.MetaDataHelper.toTablesMap;

/**
 * 架构管理工具
 */
public final class SchemaHelper {

    // todo
    public static String getMigrateSQL(Table<?> oldTable, Table<?> newTable, Dialect dialect) {
        return "";
    }

    public static SchemaVerifyResult verify(Table<?> oldTable, Table<?> newTable) {
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
        return new SchemaVerifyResult(needAdd.toArray(Column[]::new), needRemove, needChange);
    }

    /**
     * a
     *
     * @param tableInfo a
     * @throws java.sql.SQLException a
     */
    public static void fixTable(Table<?> tableInfo, JDBCContext jdbcContext) throws SQLException {
        var dataSource = jdbcContext.dataSource();
        var sqlRunner = jdbcContext.sqlRunner();
        try (var con = dataSource.getConnection()) {
            var catalog = con.getCatalog();
            var schema = con.getSchema();
            // 查找同名表
            var map = toTablesMap(initTables(con, catalog, schema, tableInfo.name(), null));
            var tableMetaData = map.get(tableInfo.name());
            if (tableMetaData == null) {// 没有这个表
                var createTableDDL = findDialect(dataSource).getCreateTableDDL(tableInfo);
                sqlRunner.execute(con, SQL.ofNormal(createTableDDL));
            } else {// 有表, 接下来校验字段
                var verify = verify(tableMetaData.refreshColumns(con), tableInfo);
                // 获取不存在的字段
                var needAdd = verify.needAdd();
                if (needAdd.length > 0) {
                    var alertTableDDL = findDialect(dataSource).getAlertTableDDL(needAdd, tableInfo);
                    sqlRunner.execute(con, SQL.ofNormal(alertTableDDL));
                }
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
    public static boolean checkNeedFixTable(Table<?> tableInfo, DataSource dataSource) throws SQLException {
        try (var con = dataSource.getConnection()) {
            var catalog = con.getCatalog();
            var schema = con.getSchema();
            // 查找同名表
            var map = toTablesMap(initTables(con, catalog, schema, tableInfo.name(), null));
            var tableMetaData = map.get(tableInfo.name());
            if (tableMetaData == null) {// 没有这个表
                return true;
            } else {// 有表, 接下来校验字段
                var verify = verify(tableMetaData.refreshColumns(con), tableInfo);
                //获取不存在的字段
                var needAdd = verify.needAdd();
                if (needAdd.length > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public record SchemaVerifyResult(Column[] needAdd, List<Column> needRemove, List<Column> needChange) {

    }

}
