package cool.scx.dao.spy.wrapper;

import cool.scx.dao.spy.SpyEventListener;
import cool.scx.dao.spy.event.LoggingEventListener;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

import static cool.scx.dao.dialect.DialectSelector.findDialect;

public class DataSourceWrapper extends AbstractWrapper implements DataSource {

    private final DataSource dataSource;
    private final SpyEventListener eventListener;

    public DataSourceWrapper(DataSource dataSource, SpyEventListener eventListener) {
        super(dataSource);
        this.dataSource = dataSource;
        this.eventListener = eventListener;
    }

    public DataSourceWrapper(DataSource dataSource) {
        this(dataSource, new LoggingEventListener(findDialect(dataSource)));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new ConnectionWrapper(dataSource.getConnection(), eventListener);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new ConnectionWrapper(dataSource.getConnection(username, password), eventListener);
    }

    // ***********
    // 以下为委托方法
    // ***********

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return dataSource.createConnectionBuilder();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return dataSource.createShardingKeyBuilder();
    }

}
