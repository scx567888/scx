package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SchemaMetaData
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class SchemaMetaData implements Schema {

    private final String catalog;
    private final String name;
    private TableMetaData[] tables;
    private Map<String, TableMetaData> tableMap = new HashMap<>();

    public SchemaMetaData(String catalog, String name) {
        this.catalog = catalog;
        this.name = name;
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
    public TableMetaData[] tables() {
        return tables;
    }

    @Override
    public TableMetaData getTable(String name) {
        return tableMap.get(name);
    }

    public SchemaMetaData refreshTables(Connection connection) throws SQLException {
        return refreshTables(connection, new String[]{"TABLE"}, false);
    }

    public SchemaMetaData refreshTables(Connection connection, boolean deep) throws SQLException {
        return refreshTables(connection, new String[]{"TABLE"}, deep);
    }

    public SchemaMetaData refreshTables(Connection connection, String[] types) throws SQLException {
        return refreshTables(connection, types, false);
    }

    public SchemaMetaData refreshTables(Connection connection, String[] types, boolean deep) throws SQLException {
        this.tables = MetaDataHelper.getTables(connection, this.catalog, this.name, null, types);
        this.tableMap = Arrays.stream(this.tables).collect(Collectors.toMap(TableMetaData::name, c -> c));
        if (deep) {
            for (var table : tables) {
                table.refreshColumns(connection);
            }
        }
        return this;
    }

}
