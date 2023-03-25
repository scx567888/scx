package cool.scx.sql.schema.impl;

import cool.scx.sql.SchemaHelper;
import cool.scx.sql.schema.DataSource;

import java.sql.DatabaseMetaData;

public final class DataSourceMetaData implements DataSource {

    private CatalogMetaData[] catalogs;

    @Override
    public CatalogMetaData[] catalogs() {
        return catalogs;
    }

    public DataSourceMetaData refreshCatalogs(DatabaseMetaData dbMetaData) {
        return refreshCatalogs(dbMetaData, false);
    }

    public DataSourceMetaData refreshCatalogs(DatabaseMetaData dbMetaData, boolean deep) {
        catalogs = SchemaHelper.initCatalogs(dbMetaData);
        if (deep) {
            for (var catalog : catalogs) {
                catalog.refreshSchemas(dbMetaData, deep);
            }
        }
        return this;
    }

}
