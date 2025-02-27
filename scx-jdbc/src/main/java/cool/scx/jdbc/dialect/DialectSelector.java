package cool.scx.jdbc.dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/// DialectSelector
///
/// @author scx567888
/// @version 0.0.1
public final class DialectSelector {

    private static final List<Dialect> DIALECT_LIST = initDialectList();

    private static List<Dialect> initDialectList() {
        var list = new ArrayList<Dialect>();
        var loader = ServiceLoader.load(Dialect.class);
        for (var dialect : loader) {
            list.add(dialect);
        }
        return list;
    }

    /// 根据驱动查找
    ///
    /// @param realDriver realDriver
    /// @return a
    public static Dialect findDialect(Driver realDriver) {
        for (var dialect : DIALECT_LIST) {
            if (dialect.canHandle(realDriver)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + realDriver.getClass().getName());
    }

    /// 根据数据源去查找
    ///
    /// @param dataSource da
    /// @return a
    public static Dialect findDialect(DataSource dataSource) {
        for (var dialect : DIALECT_LIST) {
            if (dialect.canHandle(dataSource)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + dataSource.getClass().getName());
    }

    /// 根据数据库连接 URL 去找
    ///
    /// @param url a
    /// @return a
    public static Dialect findDialect(String url) {
        for (var dialect : DIALECT_LIST) {
            if (dialect.canHandle(url)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + url);
    }

}
