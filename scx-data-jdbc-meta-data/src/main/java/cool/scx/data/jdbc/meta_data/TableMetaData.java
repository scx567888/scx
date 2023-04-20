package cool.scx.data.jdbc.meta_data;

import cool.scx.data.jdbc.mapping.Table;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public final class TableMetaData implements Table<ColumnMetaData> {

    private final String catalog;
    private final String schema;
    private final String name;
    private final String remarks;
    private ColumnMetaData[] columns;
    private Map<String, ColumnMetaData> columnsMap = new HashMap<>();
    private KeyMetaData[] keys;
    private IndexMetaData[] indexes;

    public TableMetaData(String catalog, String schema, String name, String remarks) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
        this.remarks = remarks;
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
    public String name() {
        return name;
    }

    public String remarks() {
        return remarks;
    }

    @Override
    public ColumnMetaData[] columns() {
        return columns;
    }

    public TableMetaData refreshColumns(Connection connection) {
        this.keys = MetaDataHelper.initPrimaryKeys(connection, this.catalog, this.schema, this.name);
        this.indexes = MetaDataHelper.initIndexInfo(connection, this.catalog, this.schema, this.name, false, false);
        this.columns = MetaDataHelper.initColumns(connection, this.catalog, this.schema, this.name, null, this);
        this.columnsMap = MetaDataHelper.toColumnsMap(this.columns);
        return this;
    }

    @Override
    public KeyMetaData[] keys() {
        return keys;
    }

    @Override
    public IndexMetaData[] indexes() {
        return indexes;
    }

    @Override
    public ColumnMetaData getColumn(String column) {
        return columnsMap.get(column);
    }

}
