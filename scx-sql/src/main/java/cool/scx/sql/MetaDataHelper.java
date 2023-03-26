package cool.scx.sql;

import cool.scx.sql.meta_data.*;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cool.scx.sql.ResultHandler.ofBeanList;

public final class MetaDataHelper {

    private static final ResultHandler<List<_Catalog>> CATALOG_LIST_HANDLER = ofBeanList(_Catalog.class);
    private static final ResultHandler<List<_Schema>> SCHEMA_LIST_HANDLER = ofBeanList(_Schema.class);
    private static final ResultHandler<List<_Table>> TABLE_LIST_HANDLER = ofBeanList(_Table.class);
    private static final ResultHandler<List<_Column>> COLUMN_LIST_HANDLER = ofBeanList(_Column.class);
    private static final ResultHandler<List<_PrimaryKey>> PRIMARY_KEY_LIST_HANDLER = ofBeanList(_PrimaryKey.class);

    public static List<_Catalog> getCatalogs(DatabaseMetaData dbMetaData) throws SQLException {
        var catalogs = CATALOG_LIST_HANDLER.apply(dbMetaData.getCatalogs());
        // 因为存在一些数据库没有 Catalog 所以这里默认返回虚拟的一个 Catalog
        if (catalogs.size() == 0) {
            catalogs = List.of(new _Catalog(null));
        }
        return catalogs;
    }


    public static List<_Schema> getSchemas(DatabaseMetaData dbMetaData, String catalog, String schemaPattern) throws SQLException {
        var schemas = SCHEMA_LIST_HANDLER.apply(dbMetaData.getSchemas(catalog, schemaPattern));
        // 因为存在一些数据库没有 Schema 所以这里默认返回虚拟的一个 Schema
        if (schemas.size() == 0) {
            schemas = List.of(new _Schema(null, catalog));
        }
        return schemas;
    }

    public static List<_Table> getTables(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return TABLE_LIST_HANDLER.apply(dbMetaData.getTables(catalog, schemaPattern, tableNamePattern, types));
    }


    public static List<_Column> getColumns(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return COLUMN_LIST_HANDLER.apply(dbMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern));
    }

    public static List<_PrimaryKey> getPrimaryKeys(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return PRIMARY_KEY_LIST_HANDLER.apply(dbMetaData.getPrimaryKeys(catalog, schemaPattern, tableNamePattern));
    }

    public static CatalogMetaData[] initCatalogs(DatabaseMetaData dbMetaData) {
        try {
            var catalogs = getCatalogs(dbMetaData);
            if (catalogs.size() > 0) {
                return catalogs.stream().map(_Catalog::toCatalogMetaData).toArray(CatalogMetaData[]::new);
            }
        } catch (SQLException ignored) {

        }
        return new CatalogMetaData[]{new CatalogMetaData(null)};
    }

    public static SchemaMetaData[] initSchemas(DatabaseMetaData dbMetaData, String catalog, String schemaPattern) {
        try {
            var schemas = getSchemas(dbMetaData, catalog, schemaPattern);
            return schemas.stream().map(_Schema::toSchemaMetaData).toArray(SchemaMetaData[]::new);
        } catch (SQLException ignored) {

        }
        return new SchemaMetaData[]{new SchemaMetaData(catalog, null)};
    }

    public static TableMetaData[] initTables(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        try {
            var tables = getTables(dbMetaData, catalog, schemaPattern, tableNamePattern, types);
            return tables.stream().map(_Table::toTableMetaData).toArray(TableMetaData[]::new);
        } catch (SQLException ignored) {

        }
        return new TableMetaData[]{};
    }

    public static ColumnMetaData[] initColumns(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        try {
            var columns = getColumns(dbMetaData, catalog, schemaPattern, tableNamePattern, columnNamePattern);
            return columns.stream().map(_Column::toColumnMetaData).toArray(ColumnMetaData[]::new);
        } catch (SQLException e) {
            return new ColumnMetaData[]{};
        }
    }

    public static PrimaryKeyMetaData[] initPrimaryKeys(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern) {
        try {
            var primaryKeys = getPrimaryKeys(dbMetaData, catalog, schemaPattern, tableNamePattern);
            return primaryKeys.stream().map(_PrimaryKey::toPrimaryKeyMetaData).toArray(PrimaryKeyMetaData[]::new);
        } catch (SQLException e) {
            return new PrimaryKeyMetaData[]{};
        }
    }

    public static Map<String, ColumnMetaData> toColumnsMap(ColumnMetaData[] columns) {
        var map = new HashMap<String, ColumnMetaData>();
        for (var column : columns) {
            map.put(column.columnName(), column);
        }
        return map;
    }

    public static Map<String, TableMetaData> toTablesMap(TableMetaData[] columns) {
        var map = new HashMap<String, TableMetaData>();
        for (var column : columns) {
            map.put(column.tableName(), column);
        }
        return map;
    }

    public record _Catalog(String TABLE_CAT) {

        public CatalogMetaData toCatalogMetaData() {
            return new CatalogMetaData(TABLE_CAT());
        }

    }

    public record _Schema(String TABLE_SCHEM, String TABLE_CATALOG) {

        public SchemaMetaData toSchemaMetaData() {
            return new SchemaMetaData(TABLE_CATALOG, TABLE_SCHEM);
        }

    }

    public record _Table(String TABLE_CAT, String TABLE_NAME, String SELF_REFERENCING_COL_NAME, String TABLE_SCHEM,
                         String TYPE_SCHEM, String TYPE_CAT, String TABLE_TYPE, String REMARKS, String REF_GENERATION,
                         String TYPE_NAME) {

        public TableMetaData toTableMetaData() {
            return new TableMetaData(TABLE_CAT, TABLE_SCHEM, TABLE_NAME, REMARKS, this);
        }

    }

    public record _Column(String SCOPE_TABLE, String TABLE_CAT, Integer BUFFER_LENGTH, String IS_NULLABLE,
                          String TABLE_NAME, String COLUMN_DEF, String SCOPE_CATALOG, String TABLE_SCHEM,
                          String COLUMN_NAME, Integer NULLABLE, String REMARKS, Integer DECIMAL_DIGITS,
                          Integer NUM_PREC_RADIX, Integer SQL_DATETIME_SUB, String IS_GENERATEDCOLUMN,
                          String IS_AUTOINCREMENT, Integer SQL_DATA_TYPE, Integer CHAR_OCTET_LENGTH,
                          Integer ORDINAL_POSITION, String SCOPE_SCHEMA, String SOURCE_DATA_TYPE, Integer DATA_TYPE,
                          String TYPE_NAME, Integer COLUMN_SIZE) {

        public ColumnMetaData toColumnMetaData() {
            var isNullable = Objects.equals("NO", IS_NULLABLE());
            var isAutoincrement = Objects.equals("YES", IS_AUTOINCREMENT());
            return new ColumnMetaData(TABLE_NAME, COLUMN_NAME, TYPE_NAME, COLUMN_SIZE, isNullable, isAutoincrement, false, COLUMN_DEF, null, REMARKS, this);
        }

    }

    public record _PrimaryKey(String TABLE_CAT, String TABLE_SCHEM, String TABLE_NAME, String COLUMN_NAME,
                              Short KEY_SEQ, String PK_NAME) {

        public PrimaryKeyMetaData toPrimaryKeyMetaData() {
            return new PrimaryKeyMetaData(COLUMN_NAME, this);
        }

    }

}
