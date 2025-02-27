package cool.scx.jdbc.mapping;

/// 数据源 注意此接口表示的是结构上的 数据源 不要和 [javax.sql.DataSource] 混淆
///
/// @author scx567888
/// @version 0.0.1
public interface DataSource {

    Catalog[] catalogs();

    default Catalog getCatalog(String name) {
        for (var catalog : catalogs()) {
            if (name.equals(catalog.name())) {
                return catalog;
            }
        }
        return null;
    }

}
