package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static cool.scx.http.routing.TypeMatcher.Type.WEB_SOCKET_HANDSHAKE;

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

    Router errorHandler(BiConsumer<Throwable, RoutingContext> handler);

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

    default RouteWritable webSocketRoute() {
        var route = Route.of().type(WEB_SOCKET_HANDSHAKE);
        addRoute(route);
        return route;
    }

    default RouteWritable webSocketRoute(int order) {
        var route = Route.of().type(WEB_SOCKET_HANDSHAKE).order(order);
        addRoute(route);
        return route;
    }

}
