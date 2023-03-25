package cool.scx.dao.schema;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.ResultHandler;
import cool.scx.sql.result_handler.BeanListHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public class DataBaseMetaData {

    private static final ResultHandler<List<_Catalog>> handler = new BeanListHandler<>(BeanBuilder.of(_Catalog.class));

    private final CatalogMetaData[] catalogs;

    public DataBaseMetaData(DatabaseMetaData dbMetaData) {
        this.catalogs = initCatalogs(dbMetaData);
    }

    private static CatalogMetaData[] initCatalogs(DatabaseMetaData dbMetaData) {
        try {
            var catalogs = handler.apply(dbMetaData.getCatalogs());
            if (catalogs.size() > 0) {
                return catalogs.stream()
                        .map(catalog -> new CatalogMetaData(dbMetaData, catalog.TABLE_CAT))
                        .toArray(CatalogMetaData[]::new);
            } else {
                return new CatalogMetaData[]{new CatalogMetaData(dbMetaData)};
            }
        } catch (SQLException e) {
            return new CatalogMetaData[]{new CatalogMetaData(dbMetaData)};
        }
    }

    public CatalogMetaData[] catalogs() {
        return catalogs;
    }

    record _Catalog(String TABLE_CAT) {

    }

}
