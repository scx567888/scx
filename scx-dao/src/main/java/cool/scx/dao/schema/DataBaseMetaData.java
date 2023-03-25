package cool.scx.dao.schema;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.ResultHandler;
import cool.scx.sql.result_handler.BeanListHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public record DataBaseMetaData(CatalogMetaData[] catalogs) {

    private static final ResultHandler<List<CatalogMetaData._Catalog>> CATALOG_LIST_HANDLER = new BeanListHandler<>(BeanBuilder.of(CatalogMetaData._Catalog.class));

    public static DataBaseMetaData of(DatabaseMetaData dbMetaData) {
        var catalogs = initCatalogs(dbMetaData);
        return new DataBaseMetaData(catalogs);
    }

    private static CatalogMetaData[] initCatalogs(DatabaseMetaData dbMetaData) {
        try {
            var catalogs = CATALOG_LIST_HANDLER.apply(dbMetaData.getCatalogs());
            if (catalogs.size() > 0) {
                return catalogs.stream()
                        .map(catalog -> CatalogMetaData.of(dbMetaData, catalog))
                        .toArray(CatalogMetaData[]::new);
            }
        } catch (SQLException ignored) {

        }
        return new CatalogMetaData[]{CatalogMetaData.of(dbMetaData, (String) null)};
    }

}
