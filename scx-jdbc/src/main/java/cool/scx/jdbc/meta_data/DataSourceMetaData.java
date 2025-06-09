package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.dialect.Dialect;
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
        catalogs = MetaDataHelper.getCatalogs(connection);
        return this;
    }

    public DataSourceMetaData refreshCatalogsDeep(Connection connection, Dialect dialect) throws SQLException {
        refreshCatalogs(connection);
        for (var catalog : catalogs) {
            catalog.refreshSchemasDeep(connection, dialect);
        }
        return this;
    }

}
