package cool.scx.sql.schema.impl;

import cool.scx.sql.SchemaHelper;
import cool.scx.sql.schema.Schema;

import java.sql.DatabaseMetaData;

public final class SchemaMetaData implements Schema {

    private final String catalog;
    private final String schemaName;
    private TableMetaData[] tables;

    public SchemaMetaData(String catalog, String schemaName) {
        this.catalog = catalog;
        this.schemaName = schemaName;
    }

    @Override
    public String catalog() {
        return catalog;
    }

    @Override
    public String schemaName() {
        return schemaName;
    }

    @Override
    public TableMetaData[] tables() {
        return tables;
    }

    public SchemaMetaData refreshTables(DatabaseMetaData dbMetaData) {
        return refreshTables(dbMetaData, false);
    }

    public SchemaMetaData refreshTables(DatabaseMetaData dbMetaData, boolean deep) {
        tables = SchemaHelper.initTables(dbMetaData, this.catalog, this.schemaName, null, null);
        if (deep) {
            for (var table : tables) {
                table.refreshColumns(dbMetaData);
                table.refreshPrimaryKeys(dbMetaData);
            }
        }
        return this;
    }

}
