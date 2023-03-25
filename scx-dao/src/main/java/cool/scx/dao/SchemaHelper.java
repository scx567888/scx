package cool.scx.dao;

import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;
import cool.scx.dao.schema.CatalogMetaData;
import cool.scx.dao.schema.DataBaseMetaData;
import cool.scx.dao.schema.SchemaVerifyResult;
import cool.scx.sql.SQL;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.result_handler.MapListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

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

    public static String getMigrateSQL(TableInfo oldTable, TableInfo newTable) {
        return "";
    }

    public static SchemaVerifyResult verify(TableInfo oldTable, TableInfo newTable) {
        return new SchemaVerifyResult();
    }

    /**
     * a
     *
     * @param tableInfo a
     * @throws java.sql.SQLException a
     */
    public static void fixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException {
        getTableInfoFromDataSource(databaseName, dataSource);
        Dialect dialect = findDialect(dataSource);
        try (var con = dataSource.getConnection()) {
            var existingColumn = getTableAllColumnNames(con, databaseName, tableInfo.tableName());
            if (existingColumn != null) {
                //获取不存在的字段
                var nonExistentColumnNames = Stream.of(tableInfo.columnInfos()).filter(c -> !existingColumn.contains(c.columnName())).toArray(ColumnInfo[]::new);
                if (nonExistentColumnNames.length > 0) {
                    var alertTableDDL = dialect.getAlertTableDDL(nonExistentColumnNames, tableInfo.tableName());
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
    public static List<String> getTableAllColumnNames(Connection con, String databaseName, String tableName) throws SQLException {
        var dbMetaData = con.getMetaData();
        var nowTable = dbMetaData.getTables(databaseName, databaseName, tableName, new String[]{"TABLE"});
        if (nowTable.next()) { //有这个表
            var nowColumns = dbMetaData.getColumns(databaseName, databaseName, nowTable.getString("TABLE_NAME"), null);
            var existingColumn = new ArrayList<String>();
            while (nowColumns.next()) {
                existingColumn.add(nowColumns.getString("COLUMN_NAME"));
            }
            return existingColumn;
        } else {//没有这个表
            return null;
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
        getTableInfoFromDataSource(databaseName, dataSource);
        try (var con = dataSource.getConnection()) {
            var existingColumn = getTableAllColumnNames(con, databaseName, tableInfo.tableName());
            //这个表不存在
            if (existingColumn != null) {
                //获取不存在的字段
                var nonExistentColumnNames = Stream.of(tableInfo.columnInfos()).filter(c -> !existingColumn.contains(c.columnName())).toList();
                return nonExistentColumnNames.size() != 0;
            } else {
                return true;
            }
        }
    }

    public static TableInfo<?>[] getTableInfoFromDataSource(String databaseName, DataSource dataSource) throws SQLException {
        var mapListHandler = new MapListHandler();
        try (var con = dataSource.getConnection()) {
            var n = new CatalogMetaData(con.getMetaData(), databaseName);
            System.out.println();
        }

//        var nowTable = dbMetaData.getTables(databaseName, databaseName, tableName, new String[]{"TABLE"});
        return null;
    }

    public static DataBaseMetaData getDataBaseMetaData(DataSource dataSource) throws SQLException {
        try (var con = dataSource.getConnection()) {
            return new DataBaseMetaData(con.getMetaData());
        }
    }

}
