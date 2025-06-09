package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Schema;

import java.sql.Connection;
import java.sql.SQLException;

/// SchemaMetaData
///
/// @author scx567888
/// @version 0.0.1
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
        this.tables = MetaDataHelper.getTables(connection, this.catalog, this.name, null, new String[]{"TABLE"});
        return this;
    }

    public SchemaMetaData refreshTablesDeep(Connection connection, Dialect dialect) throws SQLException {
        refreshTables(connection);
        for (var table : tables) {
            table.refreshColumns(connection, dialect);
        }
        return this;
    }

    public SchemaMetaData refreshTables(Connection connection, String[] types) throws SQLException {
        this.tables = MetaDataHelper.getTables(connection, this.catalog, this.name, null, types);
        return this;
    }

    public SchemaMetaData refreshTablesDeep(Connection connection, String[] types, Dialect dialect) throws SQLException {
        this.tables = MetaDataHelper.getTables(connection, this.catalog, this.name, null, types);
        for (var table : tables) {
            table.refreshColumns(connection, dialect);
        }
        return this;
    }

    public TableMetaData getTable(Connection con, String name) throws SQLException {
        var tables = MetaDataHelper.getTables(con, this.catalog, this.name, name, new String[]{"TABLE"});
        if (tables.length == 1) {
            return tables[0];
        }
        return null;
    }

}
