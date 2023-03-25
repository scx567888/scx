package cool.scx.dao.schema;


import cool.scx.sql.ResultHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static cool.scx.sql.ResultHandler.ofBeanList;

public record SchemaMetaData(String catalog, String schemaName, TableMetaData[] tables) {

    private static final ResultHandler<List<TableMetaData._Table>> TABLE_LIST_HANDLER = ofBeanList(TableMetaData._Table.class);

    public static SchemaMetaData of(DatabaseMetaData dbMetaData, String catalog, String schemaName) {
        var tables = getTables(dbMetaData, catalog, schemaName, null, null);
        return new SchemaMetaData(catalog, schemaName, tables);
    }

    public static SchemaMetaData of(DatabaseMetaData dbMetaData, _Schema _schema) {
        return of(dbMetaData, _schema.TABLE_CATALOG(), _schema.TABLE_SCHEM());
    }

    public static TableMetaData[] getTables(DatabaseMetaData dbMetaData, String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        try {
            var tables = TABLE_LIST_HANDLER.apply(dbMetaData.getTables(catalog, schemaPattern, tableNamePattern, types));
            return tables.stream()
                    .map(table -> TableMetaData.of(dbMetaData, table))
                    .toArray(TableMetaData[]::new);
        } catch (SQLException ignored) {

        }
        return new TableMetaData[]{};
    }

    public record _Schema(String TABLE_SCHEM, String TABLE_CATALOG) {

    }

}
