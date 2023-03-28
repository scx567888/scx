package cool.scx.dao;

import cool.scx.dao.dialect.Dialect;
import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.mapping.ColumnMapping;
import cool.scx.sql.mapping.TableMapping;
import cool.scx.sql.meta_data.MetaDataHelper;
import cool.scx.sql.meta_data.TableMetaData;
import cool.scx.sql.sql.SQL;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;

import static cool.scx.dao.dialect.DialectSelector.findDialect;

/**
 * 架构管理工具 todo 待重构
 */
public final class SchemaHelper {

    public static String getMigrateSQL(TableInfo<?> oldTable, TableInfo<?> newTable) {
        return "";
    }

    public static SchemaVerifyResult verify(TableMapping<?, ?> oldTable, TableInfo<?> newTable) {
        var needAdd = new ArrayList<ColumnInfo>();
        var needRemove = new ArrayList<ColumnMapping>();
        var needChange = new ArrayList<ColumnMapping>();
        for (var oldColumn : oldTable.columns()) {
            var column = newTable.getColumn(oldColumn.columnName());
            if (column == null) {
                needRemove.add(oldColumn);
            }
        }
        for (ColumnInfo newColumn : newTable.columns()) {
            var column = oldTable.getColumn(newColumn.columnName());
            if (column == null) {
                needAdd.add(newColumn);
            }
        }
        return new SchemaVerifyResult(needAdd, needRemove, needChange);
    }

    /**
     * a
     *
     * @param tableInfo a
     * @throws java.sql.SQLException a
     */
    public static void fixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException {
        Dialect dialect = findDialect(dataSource);
        try (var con = dataSource.getConnection()) {
            var map = MetaDataHelper.toTablesMap(MetaDataHelper.initTables(con.getMetaData(), databaseName, databaseName, tableInfo.tableName(), null));
            TableMetaData tableMetaData = map.get(tableInfo.tableName());
            if (tableMetaData != null) {
                tableMetaData.refreshColumns(con.getMetaData());
                SchemaVerifyResult verify = verify(tableMetaData, tableInfo);
                //获取不存在的字段
                var needAdd = verify.getNeedAdd();
                if (needAdd.length > 0) {
                    var alertTableDDL = dialect.getAlertTableDDL(needAdd, tableInfo.tableName());
                    SQLRunner.execute(con, SQL.ofNormal(alertTableDDL));
                }
            } else {// 没有这个表
                var createTableDDL = dialect.getCreateTableDDL(tableInfo);
                SQLRunner.execute(con, SQL.ofNormal(createTableDDL));
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
    public static boolean checkNeedFixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException {
        try (var con = dataSource.getConnection()) {
            var map = MetaDataHelper.toTablesMap(MetaDataHelper.initTables(con.getMetaData(), databaseName, databaseName, tableInfo.tableName(), null));
            var tableMetaData = map.get(tableInfo.tableName());
            if (tableMetaData != null) {
                tableMetaData.refreshColumns(con.getMetaData());
                var verify = verify(tableMetaData, tableInfo);
                //获取不存在的字段
                var needAdd = verify.getNeedAdd();
                if (needAdd.length > 0) {
                    return true;
                }
            } else {// 没有这个表
                return true;
            }
        }
        return false;
    }


    /**
     * todo
     */
    public static class SchemaVerifyResult {

        private final ColumnInfo[] needAdd;
        private final ArrayList<ColumnMapping> needRemove;
        private final ArrayList<ColumnMapping> needChange;

        public SchemaVerifyResult(ArrayList<ColumnInfo> needAdd, ArrayList<ColumnMapping> needRemove, ArrayList<ColumnMapping> needChange) {
            this.needAdd = needAdd.toArray(ColumnInfo[]::new);
            this.needRemove = needRemove;
            this.needChange = needChange;
        }

        public ColumnInfo[] getNeedAdd() {
            return needAdd;
        }

        public ArrayList<ColumnMapping> getNeedRemove() {
            return needRemove;
        }

        public ArrayList<ColumnMapping> getNeedChange() {
            return needChange;
        }

    }

}
