package cool.scx.data.jdbc.meta_data;

import cool.scx.data.jdbc.mapping.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class DataSourceMetaData implements DataSource {

    private CatalogMetaData[] catalogs;

    @Override
    public CatalogMetaData[] catalogs() {
        return catalogs;
    }

    public DataSourceMetaData refreshCatalogs(Connection connection) throws SQLException {
        return refreshCatalogs(connection, false);
    }

    public DataSourceMetaData refreshCatalogs(Connection connection, boolean deep) throws SQLException {
        catalogs = MetaDataHelper.initCatalogs(connection);
        if (deep) {
            for (var catalog : catalogs) {
                catalog.refreshSchemas(connection, deep);
            }
        }
        return this;
    }

}
