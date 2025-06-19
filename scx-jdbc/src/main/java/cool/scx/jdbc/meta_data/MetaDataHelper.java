package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.ResultHandler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Objects;

import static cool.scx.jdbc.result_handler.ResultHandler.ofBeanList;

/// MetaDataHelper
///
/// @author scx567888
/// @version 0.0.1
public final class MetaDataHelper {

    private static final ResultHandler<List<_Catalog>, RuntimeException> CATALOG_LIST_HANDLER = ofBeanList(_Catalog.class);
    private static final ResultHandler<List<_Schema>, RuntimeException> SCHEMA_LIST_HANDLER = ofBeanList(_Schema.class);
    private static final ResultHandler<List<_Table>, RuntimeException> TABLE_LIST_HANDLER = ofBeanList(_Table.class);
    private static final ResultHandler<List<_Column>, RuntimeException> COLUMN_LIST_HANDLER = ofBeanList(_Column.class);
    private static final ResultHandler<List<_Key>, RuntimeException> KEY_LIST_HANDLER = ofBeanList(_Key.class);
    private static final ResultHandler<List<_Index>, RuntimeException> INDEX_INFO_LIST_HANDLER = ofBeanList(_Index.class);

    public static CatalogMetaData[] getCatalogs(Connection con) throws SQLException {
        try {
            var catalogs = CATALOG_LIST_HANDLER.apply(con.getMetaData().getCatalogs()).stream().map(_Catalog::toCatalogMetaData).toArray(CatalogMetaData[]::new);
            if (catalogs.length > 0) {
                return catalogs;
            }
        } catch (SQLFeatureNotSupportedException ignored) {

        }
        // 因为存在一些数据库没有 Catalog 所以这里默认返回虚拟的一个 Catalog
        return new CatalogMetaData[]{new CatalogMetaData(null)};
    }

    public static SchemaMetaData[] getSchemas(Connection con, String catalog, String schemaPattern) throws SQLException {
        try {
            var schemas = SCHEMA_LIST_HANDLER.apply(con.getMetaData().getSchemas(catalog, schemaPattern)).stream().map(_Schema::toSchemaMetaData).toArray(SchemaMetaData[]::new);
            if (schemas.length > 0) {
                return schemas;
            }
        } catch (SQLFeatureNotSupportedException ignored) {

        }
        // 因为存在一些数据库没有 Schema 所以这里默认返回虚拟的一个 Schema
        return new SchemaMetaData[]{new SchemaMetaData(catalog, null)};
    }

    public static TableMetaData[] getTables(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return TABLE_LIST_HANDLER.apply(connection.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types)).stream().map(_Table::toTableMetaData).toArray(TableMetaData[]::new);
    }

    public static ColumnMetaData[] getColumns(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern, TableMetaData tableMetaData, Dialect dialect) throws SQLException {
        return COLUMN_LIST_HANDLER.apply(connection.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)).stream().map(column -> column.toColumnMetaData(tableMetaData, dialect)).toArray(ColumnMetaData[]::new);
    }

    public static KeyMetaData[] getKeys(Connection connection, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return KEY_LIST_HANDLER.apply(connection.getMetaData().getPrimaryKeys(catalog, schemaPattern, tableNamePattern)).stream().map(_Key::toKeyMetaData).toArray(KeyMetaData[]::new);
    }

    public static IndexMetaData[] getIndexes(Connection connection, String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        return INDEX_INFO_LIST_HANDLER.apply(connection.getMetaData().getIndexInfo(catalog, schema, table, unique, approximate)).stream().map(_Index::toIndexMetaData).toArray(IndexMetaData[]::new);
    }

    public static SchemaMetaData getCurrentSchema(Connection con) throws SQLException {
        return getSchemas(con, con.getCatalog(), con.getSchema())[0];
    }

    /// @see DatabaseMetaData#getCatalogs()
    private record _Catalog(String TABLE_CAT) {

        public CatalogMetaData toCatalogMetaData() {
            return new CatalogMetaData(TABLE_CAT);
        }

    }

    /// @see DatabaseMetaData#getSchemas(String, String)
    private record _Schema(String TABLE_SCHEM,
                           String TABLE_CATALOG) {

        public SchemaMetaData toSchemaMetaData() {
            return new SchemaMetaData(TABLE_CATALOG, TABLE_SCHEM);
        }

    }

    /// @see DatabaseMetaData#getTables(String, String, String, String[])
    private record _Table(String TABLE_CAT,
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


    /// @see DatabaseMetaData#getColumns(String, String, String, String)
    private record _Column(String TABLE_CAT,
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

        private static boolean checkPrimaryKey(TableMetaData table, String columnName) {
            for (var primaryKey : table.keys()) {
                if (Objects.equals(primaryKey.columnName(), columnName)) {
                    return true;
                }
            }
            return false;
        }

        private static IndexMetaData checkIndex(TableMetaData table, String columnName) {
            for (var indexInfoMetaData : table.indexes()) {
                if (Objects.equals(indexInfoMetaData.columnName(), columnName)) {
                    return indexInfoMetaData;
                }
            }
            return null;
        }

        public ColumnMetaData toColumnMetaData(TableMetaData tableMetaData, Dialect dialect) {
            var dataType = new DataTypeMetaData(dialect.dialectDataTypeToJDBCType(TYPE_NAME), TYPE_NAME, COLUMN_SIZE);
            var notNull = Objects.equals("NO", IS_NULLABLE);
            var autoincrement = Objects.equals("YES", IS_AUTOINCREMENT);
            var primary = checkPrimaryKey(tableMetaData, COLUMN_NAME);
            var index = false;
            var unique = false;
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
                    dataType,
                    COLUMN_DEF,
                    null,
                    notNull,
                    autoincrement,
                    primary,
                    unique,
                    index,
                    REMARKS);
        }

    }

    /// @see DatabaseMetaData#getPrimaryKeys(String, String, String)
    private record _Key(String TABLE_CAT,
                        String TABLE_SCHEM,
                        String TABLE_NAME,
                        String COLUMN_NAME,
                        short KEY_SEQ,
                        String PK_NAME) {

        public KeyMetaData toKeyMetaData() {
            return new KeyMetaData(PK_NAME, COLUMN_NAME, true);
        }

    }

    /// @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
    private record _Index(String TABLE_CAT,
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

        public IndexMetaData toIndexMetaData() {
            return new IndexMetaData(INDEX_NAME, COLUMN_NAME, !NON_UNIQUE);
        }

    }

}
