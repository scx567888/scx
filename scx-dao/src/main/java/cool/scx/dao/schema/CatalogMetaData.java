package cool.scx.dao.schema;

import cool.scx.sql.ResultHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static cool.scx.sql.ResultHandler.ofBeanList;

public record CatalogMetaData(String catalogName, SchemaMetaData[] schemas) {

    private static final ResultHandler<List<SchemaMetaData._Schema>> SCHEMA_LIST_HANDLER = ofBeanList(SchemaMetaData._Schema.class);

    public static CatalogMetaData of(DatabaseMetaData dbMetaData, _Catalog _catalog) {
        return of(dbMetaData, _catalog.TABLE_CAT());
    }

    public static CatalogMetaData of(DatabaseMetaData dbMetaData, String catalogName) {
        var schemas = getSchemas(dbMetaData, catalogName, null);
        return new CatalogMetaData(catalogName, schemas);
    }

    public static SchemaMetaData[] getSchemas(DatabaseMetaData dbMetaData, String catalog, String schemaPattern) {
        try {
            var schemas = SCHEMA_LIST_HANDLER.apply(dbMetaData.getSchemas(catalog, schemaPattern));
            if (schemas.size() > 0) {
                return schemas.stream()
                        .map(schema -> SchemaMetaData.of(dbMetaData, schema))
                        .toArray(SchemaMetaData[]::new);
            }
        } catch (SQLException ignored) {

        }
        return new SchemaMetaData[]{SchemaMetaData.of(dbMetaData, catalog, null)};
    }

    public record _Catalog(String TABLE_CAT) {

    }

}
