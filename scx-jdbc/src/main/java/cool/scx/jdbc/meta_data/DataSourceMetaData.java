package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

/// DataSourceMetaData
///
/// @author scx567888
/// @version 0.0.1
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
        catalogs = MetaDataHelper.getCatalogs(connection);
        if (deep) {
            for (var catalog : catalogs) {
                catalog.refreshSchemas(connection, deep);
            }
        }
        return this;
    }

}
