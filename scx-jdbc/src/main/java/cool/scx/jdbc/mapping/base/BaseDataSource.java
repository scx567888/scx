package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Catalog;
import cool.scx.jdbc.mapping.DataSource;

import java.util.HashMap;
import java.util.Map;

/// 用于手动编写 DataSource
///
/// @author scx567888
/// @version 0.0.1
public class BaseDataSource implements DataSource {

    private final Map<String, BaseCatalog> catalogMap = new HashMap<>();

    public BaseDataSource() {
    }

    @Override
    public BaseCatalog[] catalogs() {
        return catalogMap.values().toArray(BaseCatalog[]::new);
    }

    @Override
    public BaseCatalog getCatalog(String name) {
        return catalogMap.get(name);
    }

    public BaseDataSource addCatalog(Catalog oldCatalog) {
        var catalog = new BaseCatalog(oldCatalog);
        catalogMap.put(catalog.name(), catalog);
        return this;
    }

    public BaseDataSource removeCatalog(String name) {
        catalogMap.remove(name);
        return this;
    }

    public BaseDataSource clearCatalogs() {
        catalogMap.clear();
        return this;
    }

}
