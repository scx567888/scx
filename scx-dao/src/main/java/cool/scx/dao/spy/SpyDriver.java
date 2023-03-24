package cool.scx.dao.spy;

import cool.scx.dao.SchemaHelper;
import cool.scx.dao.spy.event.LoggingEventListener;
import cool.scx.dao.spy.wrapper.ConnectionWrapper;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class SpyDriver implements Driver {

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
        return new ConnectionWrapper(realDriver.connect(realUrl, info), new LoggingEventListener(SchemaHelper.findDialect(realDriver)));
    }

    @Override
    public boolean acceptsURL(String url) {
        return url != null && url.startsWith("ScxSpy:");
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
        return acceptsURL(url) ? url.replace("ScxSpy:", "") : url;
    }

}
