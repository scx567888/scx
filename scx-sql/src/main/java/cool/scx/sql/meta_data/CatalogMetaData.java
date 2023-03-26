package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.CatalogMapping;

import java.sql.DatabaseMetaData;

import static cool.scx.sql.MetaDataHelper.initSchemas;

public final class CatalogMetaData implements CatalogMapping {

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
        schemas = initSchemas(dbMetaData, this.catalogName, null);
        if (deep) {
            for (var schema : schemas) {
                schema.refreshTables(dbMetaData, deep);
            }
        }
        return this;
    }

}
