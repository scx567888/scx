package cool.scx.orm.jdbc.meta_data;

import cool.scx.orm.jdbc.mapping.Catalog;

import java.sql.DatabaseMetaData;

import static cool.scx.orm.jdbc.meta_data.MetaDataHelper.initSchemas;

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
        schemas = initSchemas(dbMetaData, this.name, null);
        if (deep) {
            for (var schema : schemas) {
                schema.refreshTables(dbMetaData, deep);
            }
        }
        return this;
    }

}
