package cool.scx.web.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Router implements Consumer<ScxHttpServerRequest> {

    private final List<Route> routes;

    public Router() {
        this.routes = new ArrayList<>();
    }

    public Router addRoute(Route route) {
        routes.add(route.order(), route);
        return this;
    }

    public List<Route> routes() {
        return routes;
    }

    @Override
    public void accept(ScxHttpServerRequest scxHttpRequest) {
        var routingContext = new RoutingContext(scxHttpRequest, this.routes);
        routingContext.next();
    }

}
