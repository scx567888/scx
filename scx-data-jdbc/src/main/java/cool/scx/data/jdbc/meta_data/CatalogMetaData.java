package cool.scx.data.jdbc.meta_data;

import cool.scx.data.jdbc.mapping.Catalog;

import java.sql.DatabaseMetaData;

public final class CatalogMetaData implements Catalog {

    private final String name;

    private SchemaMetaData[] schemas;

    public CatalogMetaData(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SchemaMetaData[] schemas() {
        return schemas;
    }

    public CatalogMetaData refreshSchemas(DatabaseMetaData dbMetaData) {
        return refreshSchemas(dbMetaData, false);
    }

    public CatalogMetaData refreshSchemas(DatabaseMetaData dbMetaData, boolean deep) {
        schemas = MetaDataHelper.initSchemas(dbMetaData, this.name, null);
        if (deep) {
            for (var schema : schemas) {
                schema.refreshTables(dbMetaData, deep);
            }
        }
        return this;
    }

}
