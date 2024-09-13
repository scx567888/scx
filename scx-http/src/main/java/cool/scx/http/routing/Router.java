package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Router implements Consumer<ScxHttpServerRequest> {

    final List<Route> routes;
    BiConsumer<Throwable, RoutingContext> exceptionHandler;

    public Router() {
        this.routes = new ArrayList<>();
    }

    public Router addRoute(Route route) {
        //todo order 需要处理
        routes.add(route);
        return this;
    }

    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public void accept(ScxHttpServerRequest scxHttpRequest) {
        new RoutingContext(this, scxHttpRequest).next();
    }

    public Router exceptionHandler(BiConsumer<Throwable, RoutingContext> handler) {
        this.exceptionHandler = handler;
        return this;
    }

}
