package cool.scx.sql.schema.impl;

import cool.scx.sql.SchemaHelper;
import cool.scx.sql.schema.Table;

import java.sql.DatabaseMetaData;

public final class TableMetaData implements Table {

    private final String catalog;
    private final String schema;
    private final String tableName;
    private final String remarks;
    private final SchemaHelper._Table _table;
    private ColumnMetaData[] columns;
    private PrimaryKeyMetaData[] primaryKeys;

    public TableMetaData(String catalog, String schema, String tableName, String remarks, SchemaHelper._Table _table) {
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
        columns = SchemaHelper.initColumns(dbMetaData, this.catalog, this.schema, this.tableName, null);
        return this;
    }

    public TableMetaData refreshPrimaryKeys(DatabaseMetaData dbMetaData) {
        primaryKeys = SchemaHelper.initPrimaryKeys(dbMetaData, this.catalog, this.schema, this.tableName);
        return this;
    }

    @Override
    public PrimaryKeyMetaData[] primaryKeys() {
        return primaryKeys;
    }

    public SchemaHelper._Table _table() {
        return _table;
    }

}
