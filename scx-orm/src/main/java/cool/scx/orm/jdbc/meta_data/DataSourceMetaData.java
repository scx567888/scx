package cool.scx.orm.jdbc.meta_data;

import cool.scx.orm.jdbc.mapping.DataSource;

import java.sql.DatabaseMetaData;

import static cool.scx.orm.jdbc.meta_data.MetaDataHelper.initCatalogs;

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
        catalogs = initCatalogs(dbMetaData);
        if (deep) {
            for (var catalog : catalogs) {
                catalog.refreshSchemas(dbMetaData, deep);
            }
        }
        return this;
    }

}
