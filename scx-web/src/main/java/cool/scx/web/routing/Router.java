package cool.scx.web.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Router implements Consumer<ScxHttpServerRequest> {

    List<Route> routes;

    public Router() {
        this.routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    @Override
    public void accept(ScxHttpServerRequest scxHttpRequest) {
        var routingContext = new RoutingContext(scxHttpRequest, routes);
        routingContext.next();
    }

}
