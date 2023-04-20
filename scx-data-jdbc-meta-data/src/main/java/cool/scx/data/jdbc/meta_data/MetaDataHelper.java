package cool.scx.data.jdbc.meta_data;

import cool.scx.data.jdbc.result_handler.ResultHandler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cool.scx.data.jdbc.dialect.StandardDialect.STANDARD_DIALECT;

public final class MetaDataHelper {

    private static final ResultHandler<List<_Catalog>> CATALOG_LIST_HANDLER = ResultHandler.ofBeanList(_Catalog.class);
    private static final ResultHandler<List<_Schema>> SCHEMA_LIST_HANDLER = ResultHandler.ofBeanList(_Schema.class);
    private static final ResultHandler<List<_Table>> TABLE_LIST_HANDLER = ResultHandler.ofBeanList(_Table.class);
    private static final ResultHandler<List<_Column>> COLUMN_LIST_HANDLER = ResultHandler.ofBeanList(_Column.class);
    private static final ResultHandler<List<_PrimaryKey>> PRIMARY_KEY_LIST_HANDLER = ResultHandler.ofBeanList(_PrimaryKey.class);
    private static final ResultHandler<List<_IndexInfo>> INDEX_INFO_LIST_HANDLER = ResultHandler.ofBeanList(_IndexInfo.class);

    public static List<_Catalog> getCatalogs(Connection connection) throws SQLException {
        var catalogs = CATALOG_LIST_HANDLER.apply(connection.getMetaData().getCatalogs(), STANDARD_DIALECT);
        // 因为存在一些数据库没有 Catalog 所以这里默认返回虚拟的一个 Catalog
        if (catalogs.size() == 0) {
            catalogs = List.of(new _Catalog(null));
        }
        return catalogs;
    }

    public static List<_Schema> getSchemas(Connection connection, String catalog, String schemaPattern) throws SQLException {
        var resultSet = connection.getMetaData().getSchemas(catalog, schemaPattern);
        var schemas = SCHEMA_LIST_HANDLER.apply(resultSet, STANDARD_DIALECT);
        // 因为存在一些数据库没有 Schema 所以这里默认返回虚拟的一个 Schema
        if (schemas.size() == 0) {
            schemas = List.of(new _Schema(null, catalog));
        }
        return schemas;
    }

    public static List<_Table> getTables(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return TABLE_LIST_HANDLER.apply(connection.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types), STANDARD_DIALECT);
    }

    public static List<_Column> getColumns(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return COLUMN_LIST_HANDLER.apply(connection.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern), STANDARD_DIALECT);
    }

    public static List<_PrimaryKey> getPrimaryKeys(Connection connection, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return PRIMARY_KEY_LIST_HANDLER.apply(connection.getMetaData().getPrimaryKeys(catalog, schemaPattern, tableNamePattern), STANDARD_DIALECT);
    }

    public static List<_IndexInfo> getIndexInfo(Connection connection, String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        return INDEX_INFO_LIST_HANDLER.apply(connection.getMetaData().getIndexInfo(catalog, schema, table, unique, approximate), STANDARD_DIALECT);
    }

    public static CatalogMetaData[] initCatalogs(Connection connection) {
        try {
            var catalogs = getCatalogs(connection);
            if (catalogs.size() > 0) {
                return catalogs.stream().map(_Catalog::toCatalogMetaData).toArray(CatalogMetaData[]::new);
            }
        } catch (SQLException ignored) {

        }
        return new CatalogMetaData[]{new CatalogMetaData(null)};
    }

    public static SchemaMetaData[] initSchemas(Connection connection, String catalog, String schemaPattern) {
        try {
            var schemas = getSchemas(connection, catalog, schemaPattern);
            return schemas.stream().map(_Schema::toSchemaMetaData).toArray(SchemaMetaData[]::new);
        } catch (SQLException ignored) {

        }
        return new SchemaMetaData[]{new SchemaMetaData(catalog, null)};
    }

    public static TableMetaData[] initTables(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        try {
            var tables = getTables(connection, catalog, schemaPattern, tableNamePattern, types);
            return tables.stream().map(_Table::toTableMetaData).toArray(TableMetaData[]::new);
        } catch (SQLException ignored) {

        }
        return new TableMetaData[]{};
    }

    public static ColumnMetaData[] initColumns(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern, TableMetaData tableMetaData) {
        try {
            var columns = getColumns(connection, catalog, schemaPattern, tableNamePattern, columnNamePattern);
            return columns.stream().map(column -> column.toColumnMetaData(tableMetaData)).toArray(ColumnMetaData[]::new);
        } catch (SQLException e) {
            return new ColumnMetaData[]{};
        }
    }

    public static KeyMetaData[] initPrimaryKeys(Connection connection, String catalog, String schemaPattern, String tableNamePattern) {
        try {
            var primaryKeys = getPrimaryKeys(connection, catalog, schemaPattern, tableNamePattern);
            return primaryKeys.stream().map(_PrimaryKey::toPrimaryKeyMetaData).toArray(KeyMetaData[]::new);
        } catch (SQLException e) {
            return new KeyMetaData[]{};
        }
    }

    public static IndexMetaData[] initIndexInfo(Connection connection, String catalog, String schema, String table, boolean unique, boolean approximate) {
        try {
            var indexInfo = getIndexInfo(connection, catalog, schema, table, unique, approximate);
            return indexInfo.stream().map(_IndexInfo::toIndexInfoMetaData).toArray(IndexMetaData[]::new);
        } catch (SQLException e) {
            return new IndexMetaData[]{};
        }
    }

    public static Map<String, ColumnMetaData> toColumnsMap(ColumnMetaData[] columns) {
        var map = new HashMap<String, ColumnMetaData>();
        for (var column : columns) {
            map.put(column.name(), column);
        }
        return map;
    }

    public static Map<String, TableMetaData> toTablesMap(TableMetaData[] columns) {
        var map = new HashMap<String, TableMetaData>();
        for (var column : columns) {
            map.put(column.name(), column);
        }
        return map;
    }

    public static boolean checkPrimaryKey(TableMetaData table, String columnName) {
        for (var primaryKey : table.keys()) {
            if (Objects.equals(primaryKey.columnName(), columnName)) {
                return true;
            }
        }
        return false;
    }

    public static IndexMetaData checkIndex(TableMetaData table, String columnName) {
        for (var indexInfoMetaData : table.indexes()) {
            if (Objects.equals(indexInfoMetaData.columnName(), columnName)) {
                return indexInfoMetaData;
            }
        }
        return null;
    }

    /**
     * @see DatabaseMetaData#getCatalogs()
     */
    public record _Catalog(String TABLE_CAT) {

        public CatalogMetaData toCatalogMetaData() {
            return new CatalogMetaData(TABLE_CAT);
        }

    }

    /**
     * @see DatabaseMetaData#getSchemas(String, String)
     */
    public record _Schema(String TABLE_SCHEM,
                          String TABLE_CATALOG) {

        public SchemaMetaData toSchemaMetaData() {
            return new SchemaMetaData(TABLE_CATALOG, TABLE_SCHEM);
        }

    }

    /**
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    public record _Table(String TABLE_CAT,
                         String TABLE_SCHEM,
                         String TABLE_NAME,
                         String TABLE_TYPE,
                         String REMARKS,
                         String TYPE_CAT,
                         String TYPE_SCHEM,
                         String TYPE_NAME,
                         String SELF_REFERENCING_COL_NAME,
                         String REF_GENERATION) {

        public TableMetaData toTableMetaData() {
            return new TableMetaData(TABLE_CAT, TABLE_SCHEM, TABLE_NAME, REMARKS);
        }

    }

    /**
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public record _Column(String TABLE_CAT,
                          String TABLE_SCHEM,
                          String TABLE_NAME,
                          String COLUMN_NAME,
                          int DATA_TYPE,
                          String TYPE_NAME,
                          int COLUMN_SIZE,
                          String BUFFER_LENGTH,//is not used.
                          int DECIMAL_DIGITS,
                          int NUM_PREC_RADIX,
                          int NULLABLE,
                          String REMARKS,
                          String COLUMN_DEF,
                          int SQL_DATA_TYPE,//unused
                          int SQL_DATETIME_SUB,//unused
                          int CHAR_OCTET_LENGTH,
                          int ORDINAL_POSITION,
                          String IS_NULLABLE,
                          String SCOPE_CATALOG,
                          String SCOPE_SCHEMA,
                          String SCOPE_TABLE,
                          short SOURCE_DATA_TYPE,
                          String IS_AUTOINCREMENT,
                          String IS_GENERATEDCOLUMN) {

        public ColumnMetaData toColumnMetaData(TableMetaData tableMetaData) {
            var notNull = Objects.equals("NO", IS_NULLABLE);
            var isAutoincrement = Objects.equals("YES", IS_AUTOINCREMENT);
            var primaryKey = checkPrimaryKey(tableMetaData, COLUMN_NAME);
            boolean index = false;
            boolean unique = false;
            var indexMetaData = checkIndex(tableMetaData, COLUMN_NAME);
            if (indexMetaData != null) {
                index = true;
                if (indexMetaData.unique()) {
                    unique = true;
                }
            }
            return new ColumnMetaData(
                    TABLE_NAME,
                    COLUMN_NAME,
                    TYPE_NAME,
                    COLUMN_SIZE,
                    notNull,
                    isAutoincrement,
                    unique,
                    primaryKey,
                    index,
                    COLUMN_DEF,
                    null,
                    REMARKS);
        }

    }

    /**
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    public record _PrimaryKey(String TABLE_CAT,
                              String TABLE_SCHEM,
                              String TABLE_NAME,
                              String COLUMN_NAME,
                              short KEY_SEQ,
                              String PK_NAME) {

        public KeyMetaData toPrimaryKeyMetaData() {
            return new KeyMetaData(PK_NAME, COLUMN_NAME, true);
        }

    }

    /**
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    public record _IndexInfo(String TABLE_CAT,
                             String TABLE_SCHEM,
                             String TABLE_NAME,
                             boolean NON_UNIQUE,
                             String INDEX_QUALIFIER,
                             String INDEX_NAME,
                             short TYPE,
                             short ORDINAL_POSITION,
                             String COLUMN_NAME,
                             String ASC_OR_DESC,
                             long CARDINALITY,
                             long PAGES,
                             String FILTER_CONDITION) {

        public IndexMetaData toIndexInfoMetaData() {
            return new IndexMetaData(INDEX_NAME, COLUMN_NAME, !NON_UNIQUE);
        }

    }

}
