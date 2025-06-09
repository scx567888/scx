package cool.scx.jdbc.spy;

import cool.scx.jdbc.dialect.DialectSelector;
import cool.scx.jdbc.spy.event_listener.LoggingEventListener;
import cool.scx.jdbc.spy.wrapper.DataSourceWrapper;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.function.Function;

/// Spy
///
/// @author scx567888
/// @version 0.0.1
public class Spy {

    private static Function<DataSource, SpyEventListener> eventListenerBuilderByDataSource = (dataSource) -> new LoggingEventListener(DialectSelector.findDialect(dataSource));

    private static Function<Driver, SpyEventListener> eventListenerBuilderByDriver = (driver) -> new LoggingEventListener(DialectSelector.findDialect(driver));

    public static DataSource wrap(DataSource dataSource) {
        return wrap(dataSource, buildEventListener(dataSource));
    }

    public static DataSource wrap(DataSource dataSource, SpyEventListener eventListener) {
        //防止多次包装
        return dataSource instanceof DataSourceWrapper w ? new DataSourceWrapper(w.delegate, eventListener) : new DataSourceWrapper(dataSource, eventListener);
    }

    static SpyEventListener buildEventListener(DataSource dataSource) {
        return eventListenerBuilderByDataSource.apply(dataSource);
    }

    static SpyEventListener buildEventListener(Driver driver) {
        return eventListenerBuilderByDriver.apply(driver);
    }

    public static Function<DataSource, SpyEventListener> getEventListenerBuilderByDataSource() {
        return eventListenerBuilderByDataSource;
    }

    public static void setEventListenerBuilderByDataSource(Function<DataSource, SpyEventListener> eventListenerBuilderByDataSource) {
        Spy.eventListenerBuilderByDataSource = eventListenerBuilderByDataSource;
    }

    public static Function<Driver, SpyEventListener> getEventListenerBuilderByDriver() {
        return eventListenerBuilderByDriver;
    }

    public static void setEventListenerBuilderByDriver(Function<Driver, SpyEventListener> eventListenerBuilderByDriver) {
        Spy.eventListenerBuilderByDriver = eventListenerBuilderByDriver;
    }

}
