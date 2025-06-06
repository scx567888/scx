package cool.scx.http.routing;

import cool.scx.common.functional.ScxConsumer;
import cool.scx.http.ScxHttpServerRequest;

import java.util.List;

/// Router
///
/// @author scx567888
/// @version 0.0.1
public interface Router extends ScxConsumer<ScxHttpServerRequest, Throwable> {

    static Router of() {
        return new RouterImpl();
    }

    Router addRoute(Route route);

    List<Route> getRoutes();

    default RouteWritable route() {
        var route = Route.of();
        addRoute(route);
        return route;
    }

    default RouteWritable route(int order) {
        var route = Route.of().order(order);
        addRoute(route);
        return route;
    }

}
