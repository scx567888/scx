package cool.scx.data.jdbc.meta_data;

import cool.scx.data.jdbc.mapping.DataSource;

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
        catalogs = MetaDataHelper.initCatalogs(dbMetaData);
        if (deep) {
            for (var catalog : catalogs) {
                catalog.refreshSchemas(dbMetaData, deep);
            }
        }
        return this;
    }

}
