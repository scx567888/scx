package cool.scx.dao;

import cool.scx.dao.spy.wrapper.DataSourceWrapper;

import javax.sql.DataSource;

public class Spy {

    public static DataSource wrap(DataSource dataSource) {
        return new DataSourceWrapper(dataSource);
    }

}
