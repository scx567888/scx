package cool.scx.dao;

import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;
import cool.scx.sql.MetaDataHelper;
import cool.scx.sql.SQL;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.mapping.ColumnMapping;
import cool.scx.sql.mapping.TableMapping;
import cool.scx.sql.meta_data.ColumnMetaData;
import cool.scx.sql.meta_data.SchemaMetaData;
import cool.scx.sql.meta_data.TableMetaData;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 架构管理工具
 */
public final class SchemaHelper {

    private static final List<Dialect> DIALECT_LIST;

    static {
        DIALECT_LIST = new ArrayList<>();
        var loader = ServiceLoader.load(Dialect.class);
        for (var dialect : loader) {
            DIALECT_LIST.add(dialect);
        }
    }

    public static Dialect findDialect(Driver realDriver) {
        for (Dialect dialect : DIALECT_LIST) {
            if (dialect.canHandle(realDriver)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + realDriver.getClass().getName());
    }

    public static Dialect findDialect(DataSource dataSource) {
        for (Dialect dialect : DIALECT_LIST) {
            if (dialect.canHandle(dataSource)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + dataSource.getClass().getName());
    }

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
                tableMetaData.refreshColumns(con.getMetaData()).refreshPrimaryKeys(con.getMetaData());
                SchemaVerifyResult verify = verify(tableMetaData, tableInfo);
                //获取不存在的字段
                var needAdd = verify.getNeedAdd();
                if (needAdd.length > 0) {
                    var alertTableDDL = dialect.getAlertTableDDL(needAdd, tableInfo.tableName());
                    SQLRunner.execute(con, SQL.ofNormal(alertTableDDL));
                }
            } else {// 没有这个表
                String createTableDDL = dialect.getCreateTableDDL(tableInfo);
                System.out.println(createTableDDL);
                SQLRunner.execute(con, SQL.ofNormal(createTableDDL));
            }
        }
    }

    /**
     * 根据连接 获取数据库中所有的字段
     *
     * @param con          连接
     * @param databaseName 数据库名称
     * @param tableName    表名称
     * @return 如果表存在返回所有字段的名称 否则返回 null
     * @throws java.sql.SQLException s
     */
    public static ColumnMetaData[] getTableAllColumnNames(Connection con, String databaseName, String tableName) throws SQLException {
        var schemaMetaData = new SchemaMetaData(databaseName, databaseName).refreshTables(con.getMetaData());
        for (TableMetaData table : schemaMetaData.tables()) {

        }
        var tableMetaData = new TableMetaData(databaseName, databaseName, tableName, null, null).refreshColumns(con.getMetaData()).refreshPrimaryKeys(con.getMetaData());
        return tableMetaData.columns();
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
            TableMetaData[] tableMetaDataList = MetaDataHelper.initTables(con.getMetaData(), databaseName, databaseName, tableInfo.tableName(), null);
            var tableMetaData = new TableMetaData(databaseName, databaseName, tableInfo.tableName(), null, null).refreshColumns(con.getMetaData()).refreshPrimaryKeys(con.getMetaData());
            var existingColumn = getTableAllColumnNames(con, databaseName, tableInfo.tableName());
            //这个表不存在
            if (existingColumn != null) {
                //获取不存在的字段
//                var nonExistentColumnNames = Stream.of(tableInfo.columnInfos()).filter(c -> !existingColumn.contains(c.columnName())).toList();
//                return nonExistentColumnNames.size() != 0;
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * todo
     */
    public class SchemaVerifyResult {

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
