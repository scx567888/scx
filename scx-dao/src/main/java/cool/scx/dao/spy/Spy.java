package cool.scx.dao.spy;

import cool.scx.dao.spy.wrapper.DataSourceWrapper;

import javax.sql.DataSource;

public class Spy {

    public static DataSource wrap(DataSource dataSource) {
        //防止多次包装
        return dataSource instanceof DataSourceWrapper ? dataSource : new DataSourceWrapper(dataSource);
    }

}
