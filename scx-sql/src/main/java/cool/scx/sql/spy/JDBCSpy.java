package cool.scx.sql.spy;

import cool.scx.sql.spy.wrapper.DataSourceWrapper;

import javax.sql.DataSource;

public class JDBCSpy {

    public static DataSource wrap(DataSource dataSource) {
        return new DataSourceWrapper(dataSource);
    }

}
