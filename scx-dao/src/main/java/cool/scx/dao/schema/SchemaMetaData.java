package cool.scx.dao.schema;


import cool.scx.sql.ResultHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static cool.scx.sql.ResultHandler.ofBeanList;

public class SchemaMetaData {

    private static final ResultHandler<List<_Table>> handler = ofBeanList(_Table.class);
    private final TableMetaData[] tables;
    private final String schemaName;
    private final CatalogMetaData catalog;

    public SchemaMetaData(CatalogMetaData catalog, DatabaseMetaData dbMetaData, String schemaName) {
        this.catalog = catalog;
        this.schemaName = schemaName;
        this.tables = initTables(dbMetaData);
    }

    public SchemaMetaData(CatalogMetaData catalog, DatabaseMetaData dbMetaData) {
        this(catalog, dbMetaData, null);
    }

    private TableMetaData[] initTables(DatabaseMetaData dbMetaData) {
        try {
            var tables = handler.apply(dbMetaData.getTables(this.catalog.catalogName(), this.schemaName, null, new String[]{"TABLE"}));
            return tables.stream()
                    .map(table -> new TableMetaData(this, dbMetaData, table))
                    .toArray(TableMetaData[]::new);
        } catch (SQLException e) {
            return new TableMetaData[]{};
        }
    }

    public TableMetaData[] tables() {
        return tables;
    }

    public CatalogMetaData catalog() {
        return catalog;
    }

    public String schemaName() {
        return schemaName;
    }

    record _Table(String TABLE_CAT, String TABLE_NAME, String SELF_REFERENCING_COL_NAME, String TABLE_SCHEM,
                  String TYPE_SCHEM, String TYPE_CAT, String TABLE_TYPE, String REMARKS, String REF_GENERATION,
                  String TYPE_NAME) {

    }

}
