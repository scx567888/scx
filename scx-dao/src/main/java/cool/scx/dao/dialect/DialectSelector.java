package cool.scx.dao.dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class DialectSelector {

    private static final List<Dialect> DIALECT_LIST;

    static {
        DIALECT_LIST = new ArrayList<>();
        var loader = ServiceLoader.load(Dialect.class);
        for (var dialect : loader) {
            DIALECT_LIST.add(dialect);
        }
    }

    public static Dialect findDialect(Driver realDriver) {
        for (Dialect dialect : DIALECT_LIST) {
            if (dialect.canHandle(realDriver)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + realDriver.getClass().getName());
    }

    public static Dialect findDialect(DataSource dataSource) {
        for (Dialect dialect : DIALECT_LIST) {
            if (dialect.canHandle(dataSource)) {
                return dialect;
            }
        }
        throw new IllegalArgumentException("未找到对应的方言 !!! " + dataSource.getClass().getName());
    }

}
