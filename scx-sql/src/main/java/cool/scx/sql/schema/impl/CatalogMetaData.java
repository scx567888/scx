package cool.scx.sql.schema.impl;

import cool.scx.sql.SchemaHelper;
import cool.scx.sql.schema.Catalog;

import java.sql.DatabaseMetaData;

public final class CatalogMetaData implements Catalog {

    private final String catalogName;

    private SchemaMetaData[] schemas;

    public CatalogMetaData(String catalogName) {
        this.catalogName = catalogName;
    }

    @Override
    public String catalogName() {
        return catalogName;
    }

    @Override
    public SchemaMetaData[] schemas() {
        return schemas;
    }

    public CatalogMetaData refreshSchemas(DatabaseMetaData dbMetaData) {
        return refreshSchemas(dbMetaData, false);
    }

    public CatalogMetaData refreshSchemas(DatabaseMetaData dbMetaData, boolean deep) {
        schemas = SchemaHelper.initSchemas(dbMetaData, this.catalogName, null);
        if (deep) {
            for (var schema : schemas) {
                schema.refreshTables(dbMetaData, deep);
            }
        }
        return this;
    }

}
