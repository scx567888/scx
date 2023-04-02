package cool.scx.orm.jdbc.spy;

import cool.scx.orm.jdbc.spy.wrapper.DataSourceWrapper;

import javax.sql.DataSource;

public class Spy {

    public static DataSource wrap(DataSource dataSource) {
        //防止多次包装
        return dataSource instanceof DataSourceWrapper ? dataSource : new DataSourceWrapper(dataSource);
    }

}
