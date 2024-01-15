package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Schema;

import java.sql.Connection;
import java.sql.SQLException;

public final class SchemaMetaData implements Schema {

    private final String catalog;
    private final String name;
    private TableMetaData[] tables;

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

    public SchemaMetaData refreshTables(Connection connection) throws SQLException {
        return refreshTables(connection, false);
    }

    public SchemaMetaData refreshTables(Connection connection, boolean deep) throws SQLException {
        this.tables = MetaDataHelper.initTables(connection, this.catalog, this.name, null, null);
        if (deep) {
            for (var table : tables) {
                table.refreshColumns(connection);
            }
        }
        return this;
    }

}
