package cool.scx.jdbc.spy.wrapper;

import cool.scx.jdbc.spy.SpyEventListener;
import cool.scx.jdbc.spy.SpyWrapper;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

public class DataSourceWrapper extends SpyWrapper<DataSource> implements DataSource {

    public DataSourceWrapper(DataSource dataSource, SpyEventListener eventListener) {
        super(dataSource, eventListener);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new ConnectionWrapper(delegate.getConnection(), eventListener);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new ConnectionWrapper(delegate.getConnection(username, password), eventListener);
    }

    // ********************************************
    //               以下为委托方法
    // ********************************************

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return delegate.createConnectionBuilder();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return delegate.createShardingKeyBuilder();
    }

}
