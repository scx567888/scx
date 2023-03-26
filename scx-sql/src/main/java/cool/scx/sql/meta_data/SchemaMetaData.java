package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.SchemaMapping;

import java.sql.DatabaseMetaData;

import static cool.scx.sql.MetaDataHelper.initTables;

public final class SchemaMetaData implements SchemaMapping {

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
        tables = initTables(dbMetaData, this.catalog, this.schemaName, null, null);
        if (deep) {
            for (var table : tables) {
                table.refreshColumns(dbMetaData);
                table.refreshPrimaryKeys(dbMetaData);
            }
        }
        return this;
    }

}
