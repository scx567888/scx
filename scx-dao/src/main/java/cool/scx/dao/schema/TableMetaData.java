package cool.scx.dao.schema;

import cool.scx.sql.ResultHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static cool.scx.sql.ResultHandler.ofBeanList;

public class TableMetaData {

    private static final ResultHandler<List<_Column>> handler = ofBeanList(_Column.class);
    private static final ResultHandler<List<_PrimaryKey>> handler2 = ofBeanList(_PrimaryKey.class);

    private final String tableName;
    private final ColumnMetaData[] columns;
    private final SchemaMetaData schema;
    private final SchemaMetaData._Table _table;
    private final String remarks;
    private final PrimaryKeyMetaData[] primaryKeys;

    public TableMetaData(SchemaMetaData schema, DatabaseMetaData dbMetaData, SchemaMetaData._Table table) {
        this.schema = schema;
        this._table = table;
        this.tableName = table.TABLE_NAME();
        this.remarks = table.REMARKS();
        this.columns = initColumns(dbMetaData);
        this.primaryKeys = initPrimaryKeys(dbMetaData);
    }

    private PrimaryKeyMetaData[] initPrimaryKeys(DatabaseMetaData dbMetaData) {
        try {
            var primaryKeys = handler2.apply(dbMetaData.getPrimaryKeys(this.schema.catalog().catalogName(), this.schema.schemaName(), this.tableName));
            return primaryKeys.stream()
                    .map(primaryKey -> new PrimaryKeyMetaData(this, dbMetaData, primaryKey))
                    .toArray(PrimaryKeyMetaData[]::new);
        } catch (SQLException e) {
            return new PrimaryKeyMetaData[]{};
        }
    }

    private ColumnMetaData[] initColumns(DatabaseMetaData dbMetaData) {
        try {
            var columns = handler.apply(dbMetaData.getColumns(this.schema.catalog().catalogName(), this.schema.schemaName(), this.tableName, null));
            return columns.stream()
                    .map(column -> new ColumnMetaData(this, dbMetaData, column))
                    .toArray(ColumnMetaData[]::new);
        } catch (SQLException e) {
            return new ColumnMetaData[]{};
        }
    }

    public ColumnMetaData[] columns() {
        return columns;
    }

    public PrimaryKeyMetaData[] primaryKeys() {
        return primaryKeys;
    }

    public String remarks() {
        return remarks;
    }

    public String tableName() {
        return tableName;
    }

    public record _PrimaryKey(String TABLE_CAT, String TABLE_SCHEM, String TABLE_NAME, String COLUMN_NAME,
                              Short KEY_SEQ,
                              String PK_NAME) {

    }

    public record _Column(String SCOPE_TABLE, String TABLE_CAT, Integer BUFFER_LENGTH, String IS_NULLABLE,
                          String TABLE_NAME,
                          String COLUMN_DEF, String SCOPE_CATALOG, String TABLE_SCHEM, String COLUMN_NAME,
                          Integer NULLABLE,
                          String REMARKS, Integer DECIMAL_DIGITS, Integer NUM_PREC_RADIX, Integer SQL_DATETIME_SUB,
                          String IS_GENERATEDCOLUMN, String IS_AUTOINCREMENT, Integer SQL_DATA_TYPE,
                          Integer CHAR_OCTET_LENGTH,
                          Integer ORDINAL_POSITION, String SCOPE_SCHEMA, String SOURCE_DATA_TYPE, Integer DATA_TYPE,
                          String TYPE_NAME, Integer COLUMN_SIZE) {

    }

}
