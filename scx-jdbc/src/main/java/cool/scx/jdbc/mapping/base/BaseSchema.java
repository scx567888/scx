package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Schema;
import cool.scx.jdbc.mapping.Table;

import java.util.HashMap;
import java.util.Map;

/// 用于手动编写 Schema
///
/// @author scx567888
/// @version 0.0.1
public class BaseSchema implements Schema {

    private final Map<String, BaseTable> tableMap = new HashMap<>();

    private String catalog;
    private String name;

    public BaseSchema() {
    }

    public BaseSchema(Schema oldSchema) {
        setCatalog(oldSchema.catalog());
        setName(oldSchema.name());
        for (var table : oldSchema.tables()) {
            addTable(table);
        }
    }

    @Override
    public String catalog() {
        return catalog;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BaseTable[] tables() {
        return tableMap.values().toArray(BaseTable[]::new);
    }

    @Override
    public BaseTable getTable(String name) {
        return tableMap.get(name);
    }

    public BaseSchema setCatalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    public BaseSchema setName(String name) {
        this.name = name;
        return this;
    }

    public BaseSchema addTable(Table oldTable) {
        var table = new BaseTable(oldTable);
        tableMap.put(table.name(), table);
        return this;
    }

    public BaseSchema removeTable(String name) {
        tableMap.remove(name);
        return this;
    }

    public BaseSchema clearTables() {
        tableMap.clear();
        return this;
    }

}
