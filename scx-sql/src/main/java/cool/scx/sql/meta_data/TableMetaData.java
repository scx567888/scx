package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.Table;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cool.scx.sql.meta_data.MetaDataHelper.*;

public final class TableMetaData implements Table {

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

    public TableMetaData refreshColumns(DatabaseMetaData dbMetaData) {
        this.keys = initPrimaryKeys(dbMetaData, this.catalog, this.schema, this.name);
        this.indexes = initIndexInfo(dbMetaData, this.catalog, this.schema, this.name, false, false);
        this.columns = initColumns(dbMetaData, this.catalog, this.schema, this.name, null, this);
        this.columnsMap = toColumnsMap(this.columns);
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

    public boolean checkPrimaryKey(String columnName) {
        for (var primaryKey : keys) {
            if (Objects.equals(primaryKey.columnName(), columnName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUnique(String columnName) {
        for (var indexInfoMetaData : indexes) {
            if (Objects.equals(indexInfoMetaData.columnName(), columnName)) {
                return indexInfoMetaData.unique();
            }
        }
        return false;
    }

}
