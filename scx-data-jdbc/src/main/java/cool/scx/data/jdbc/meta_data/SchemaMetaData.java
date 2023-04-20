package cool.scx.data.jdbc.meta_data;

import cool.scx.data.jdbc.mapping.Schema;

import java.sql.DatabaseMetaData;

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

    public SchemaMetaData refreshTables(DatabaseMetaData dbMetaData) {
        return refreshTables(dbMetaData, false);
    }

    public SchemaMetaData refreshTables(DatabaseMetaData dbMetaData, boolean deep) {
        this.tables = MetaDataHelper.initTables(dbMetaData, this.catalog, this.name, null, null);
        if (deep) {
            for (var table : tables) {
                table.refreshColumns(dbMetaData);
            }
        }
        return this;
    }

}
