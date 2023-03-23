package cool.scx.spy;

import cool.scx.spy.wrapper.DataSourceWrapper;

import javax.sql.DataSource;

public class JDBCSpy {

    public static DataSource wrap(DataSource dataSource) {
        return new DataSourceWrapper(dataSource);
    }

}
