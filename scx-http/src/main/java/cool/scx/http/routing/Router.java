package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.List;
import java.util.function.Consumer;

/// Router
///
/// @author scx567888
/// @version 0.0.1
public interface Router extends Consumer<ScxHttpServerRequest> {

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
