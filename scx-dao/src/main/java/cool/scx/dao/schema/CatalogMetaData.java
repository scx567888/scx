package cool.scx.dao.schema;

import cool.scx.sql.ResultHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static cool.scx.sql.ResultHandler.ofBeanList;

public class CatalogMetaData {

    private static final ResultHandler<List<_Schema>> handler = ofBeanList(_Schema.class);
    private final SchemaMetaData[] schemas;
    private final String catalogName;

    public CatalogMetaData(DatabaseMetaData dbMetaData, String catalogName) {
        this.catalogName = catalogName;
        this.schemas = initSchemas(dbMetaData);
    }

    public CatalogMetaData(DatabaseMetaData dbMetaData) {
        this(dbMetaData, null);
    }

    private SchemaMetaData[] initSchemas(DatabaseMetaData dbMetaData) {
        try {
            var schemas = handler.apply(dbMetaData.getSchemas(this.catalogName, null));
            if (schemas.size() > 0) {
                return schemas.stream()
                        .map(schema -> new SchemaMetaData(this, dbMetaData, schema.TABLE_SCHEM))
                        .toArray(SchemaMetaData[]::new);
            }
            return new SchemaMetaData[]{new SchemaMetaData(this, dbMetaData)};
        } catch (SQLException e) {
            return new SchemaMetaData[]{new SchemaMetaData(this, dbMetaData)};
        }
    }

    public SchemaMetaData[] schemas() {
        return schemas;
    }

    public String catalogName() {
        return catalogName;
    }

    record _Schema(String TABLE_SCHEM, String TABLE_CATALOG) {

    }

}
