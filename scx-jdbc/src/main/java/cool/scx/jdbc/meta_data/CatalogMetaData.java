package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Catalog;

import java.sql.Connection;
import java.sql.SQLException;

/// CatalogMetaData
///
/// @author scx567888
/// @version 0.0.1
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

    public CatalogMetaData refreshSchemas(Connection connection) throws SQLException {
        return refreshSchemas(connection, false);
    }

    public CatalogMetaData refreshSchemas(Connection connection, boolean deep) throws SQLException {
        schemas = MetaDataHelper.getSchemas(connection, this.name, null);
        if (deep) {
            for (var schema : schemas) {
                schema.refreshTables(connection, deep);
            }
        }
        return this;
    }

}
