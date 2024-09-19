package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

}
