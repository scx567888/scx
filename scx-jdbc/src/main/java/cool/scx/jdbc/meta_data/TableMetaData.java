package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TableMetaData
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class TableMetaData implements Table {

    private final String catalog;
    private final String schema;
    private final String name;
    private final String remarks;
    private ColumnMetaData[] columns;
    private KeyMetaData[] keys;
    private IndexMetaData[] indexes;
    private Map<String, ColumnMetaData> columnMap = new HashMap<>();

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
        return columnMap.get(column);
    }

    public TableMetaData refreshColumns(Connection connection) throws SQLException {
        this.keys = MetaDataHelper.getKeys(connection, this.catalog, this.schema, this.name);
        this.indexes = MetaDataHelper.getIndexes(connection, this.catalog, this.schema, this.name, false, false);
        this.columns = MetaDataHelper.getColumns(connection, this.catalog, this.schema, this.name, null, this);
        this.columnMap = Arrays.stream(this.columns).collect(Collectors.toMap(ColumnMetaData::name, c -> c));
        return this;
    }

}
