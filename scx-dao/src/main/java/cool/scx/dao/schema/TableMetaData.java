package cool.scx.dao.schema;

import cool.scx.sql.ResultHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static cool.scx.sql.ResultHandler.ofBeanList;

public record TableMetaData(String tableName, String remarks, ColumnMetaData[] columns,
                            PrimaryKeyMetaData[] primaryKeys, _Table _table) {

    private static final ResultHandler<List<ColumnMetaData._Column>> COLUMN_LIST_HANDLER = ofBeanList(ColumnMetaData._Column.class);

    private static final ResultHandler<List<PrimaryKeyMetaData._PrimaryKey>> PRIMARY_KEY_LIST_HANDLER = ofBeanList(PrimaryKeyMetaData._PrimaryKey.class);

    public static ColumnMetaData[] getColumns(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        try {
            var columns = COLUMN_LIST_HANDLER.apply(dbMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern));
            return columns.stream().map(ColumnMetaData::of).toArray(ColumnMetaData[]::new);
        } catch (SQLException e) {
            return new ColumnMetaData[]{};
        }
    }

    public static PrimaryKeyMetaData[] getPrimaryKeys(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern) {
        try {
            var primaryKeys = PRIMARY_KEY_LIST_HANDLER.apply(dbMetaData.getPrimaryKeys(catalog, schemaPattern, tableNamePattern));
            return primaryKeys.stream().map(PrimaryKeyMetaData::of).toArray(PrimaryKeyMetaData[]::new);
        } catch (SQLException e) {
            return new PrimaryKeyMetaData[]{};
        }
    }

    public static TableMetaData of(DatabaseMetaData dbMetaData, _Table _table) {
        var catalog = _table.TABLE_CAT();
        var schema = _table.TABLE_SCHEM();
        var tableName = _table.TABLE_NAME();
        var remarks = _table.REMARKS();
        var columns = getColumns(dbMetaData, catalog, schema, tableName, null);
        var primaryKeys = getPrimaryKeys(dbMetaData, catalog, schema, tableName);
        return new TableMetaData(tableName, remarks, columns, primaryKeys, _table);
    }

    public record _Table(String TABLE_CAT, String TABLE_NAME, String SELF_REFERENCING_COL_NAME, String TABLE_SCHEM,
                         String TYPE_SCHEM, String TYPE_CAT, String TABLE_TYPE, String REMARKS, String REF_GENERATION,
                         String TYPE_NAME) {

    }

}
