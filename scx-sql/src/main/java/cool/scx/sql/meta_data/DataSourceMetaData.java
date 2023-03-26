package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.DataSourceMapping;

import java.sql.DatabaseMetaData;

import static cool.scx.sql.MetaDataHelper.initCatalogs;

public final class DataSourceMetaData implements DataSourceMapping {

    private CatalogMetaData[] catalogs;

    @Override
    public CatalogMetaData[] catalogs() {
        return catalogs;
    }

    public DataSourceMetaData refreshCatalogs(DatabaseMetaData dbMetaData) {
        return refreshCatalogs(dbMetaData, false);
    }

    public DataSourceMetaData refreshCatalogs(DatabaseMetaData dbMetaData, boolean deep) {
        catalogs = initCatalogs(dbMetaData);
        if (deep) {
            for (var catalog : catalogs) {
                catalog.refreshSchemas(dbMetaData, deep);
            }
        }
        return this;
    }

}
