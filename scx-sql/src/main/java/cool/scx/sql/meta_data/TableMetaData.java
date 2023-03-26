package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.TableMapping;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.sql.MetaDataHelper.*;

public final class TableMetaData implements TableMapping<ColumnMetaData, PrimaryKeyMetaData> {

    private final String catalog;
    private final String schema;
    private final String tableName;
    private final String remarks;
    private final _Table _table;
    private ColumnMetaData[] columns;
    private Map<String, ColumnMetaData> columnsMap = new HashMap<>();
    private PrimaryKeyMetaData[] primaryKeys;

    public TableMetaData(String catalog, String schema, String tableName, String remarks, _Table _table) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
        this.remarks = remarks;
        this._table = _table;
    }

    @Override
    public String catalog() {
        return catalog;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public String tableName() {
        return tableName;
    }

    public String remarks() {
        return remarks;
    }

    @Override
    public ColumnMetaData[] columns() {
        return columns;
    }

    public TableMetaData refreshColumns(DatabaseMetaData dbMetaData) {
        columns = initColumns(dbMetaData, this.catalog, this.schema, this.tableName, null);
        columnsMap = toColumnsMap(columns);
        return this;
    }

    public TableMetaData refreshPrimaryKeys(DatabaseMetaData dbMetaData) {
        primaryKeys = initPrimaryKeys(dbMetaData, this.catalog, this.schema, this.tableName);
        return this;
    }

    @Override
    public PrimaryKeyMetaData[] primaryKeys() {
        return primaryKeys;
    }

    @Override
    public ColumnMetaData getColumn(String column) {
        return columnsMap.get(column);
    }

    public _Table _table() {
        return _table;
    }

}
