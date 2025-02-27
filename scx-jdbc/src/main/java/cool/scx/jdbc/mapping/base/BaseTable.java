package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Index;
import cool.scx.jdbc.mapping.Key;
import cool.scx.jdbc.mapping.Table;

import java.util.HashMap;
import java.util.Map;

/// 用于手动编写 Table
///
/// @author scx567888
/// @version 0.0.1
public class BaseTable implements Table {

    private final Map<String, BaseColumn> columnMap = new HashMap<>();
    private final Map<String, BaseKey> keyMap = new HashMap<>();
    private final Map<String, BaseIndex> indexMap = new HashMap<>();

    private String catalog;
    private String schema;
    private String name;

    public BaseTable() {
    }

    public BaseTable(Table oldTable) {
        setCatalog(oldTable.catalog());
        setSchema(oldTable.schema());
        setName(oldTable.name());
        for (var column : oldTable.columns()) {
            addColumn(column);
        }
        for (var key : oldTable.keys()) {
            addKey(key);
        }
        for (var index : oldTable.indexes()) {
            addIndex(index);
        }
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

    @Override
    public BaseColumn[] columns() {
        return columnMap.values().toArray(BaseColumn[]::new);
    }

    @Override
    public BaseKey[] keys() {
        return keyMap.values().toArray(BaseKey[]::new);
    }

    @Override
    public BaseIndex[] indexes() {
        return indexMap.values().toArray(BaseIndex[]::new);
    }

    @Override
    public BaseColumn getColumn(String name) {
        return columnMap.get(name);
    }

    @Override
    public BaseKey getKey(String name) {
        return keyMap.get(name);
    }

    @Override
    public BaseIndex getIndex(String name) {
        return indexMap.get(name);
    }

    public BaseTable setCatalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    public BaseTable setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public BaseTable setName(String name) {
        this.name = name;
        return this;
    }

    public BaseTable addColumn(Column oldColumn) {
        var column = new BaseColumn(oldColumn);
        columnMap.put(column.name(), column);
        return this;
    }

    public BaseTable removeColumn(String name) {
        columnMap.remove(name);
        return this;
    }

    public BaseTable clearColumns() {
        columnMap.clear();
        return this;
    }

    public BaseTable addKey(Key oldKey) {
        var key = new BaseKey(oldKey);
        keyMap.put(key.name(), key);
        return this;
    }

    public BaseTable removeKey(String name) {
        keyMap.remove(name);
        return this;
    }

    public BaseTable clearKeys() {
        keyMap.clear();
        return this;
    }

    public BaseTable addIndex(Index oldIndex) {
        var index = new BaseIndex(oldIndex);
        indexMap.put(index.name(), index);
        return this;
    }

    public BaseTable removeIndex(String name) {
        indexMap.remove(name);
        return this;
    }

    public BaseTable clearIndexes() {
        indexMap.clear();
        return this;
    }

}
