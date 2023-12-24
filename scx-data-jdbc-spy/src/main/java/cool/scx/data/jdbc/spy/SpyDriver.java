package cool.scx.data.jdbc.spy;

import cool.scx.data.jdbc.spy.event.LoggingEventListener;
import cool.scx.data.jdbc.spy.wrapper.ConnectionWrapper;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

import static cool.scx.data.jdbc.dialect.DialectSelector.findDialect;
import static cool.scx.util.StringUtils.startsWithIgnoreCase;

public class SpyDriver implements Driver {

    public static final String PREFIX = "ScxSpy:";

    static {
        try {
            DriverManager.registerDriver(new SpyDriver());
        } catch (SQLException e) {
            throw new IllegalStateException("Could not register ScxSpyDriver with DriverManager", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        var realUrl = extractRealUrl(url);
        var realDriver = DriverManager.getDriver(realUrl);
        return new ConnectionWrapper(realDriver.connect(realUrl, info), new LoggingEventListener(findDialect(realDriver)));
    }

    @Override
    public boolean acceptsURL(String url) {
        return url != null && startsWithIgnoreCase(url, PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return DriverManager.getDriver(extractRealUrl(url)).getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    private String extractRealUrl(String url) {
        return acceptsURL(url) ? url.substring(0, PREFIX.length()) : url;
    }

}
