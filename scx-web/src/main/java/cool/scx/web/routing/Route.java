package cool.scx.web.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.function.Consumer;

public class Route {

    private Consumer<RoutingContext> handler;

    boolean matches(ScxHttpServerRequest scxHttpRequest) {
        return true;
    }

    public String path() {
        return null;
    }

    public Route handler(Consumer<RoutingContext> handler) {
        this.handler = handler;
        return this;
    }

    public Consumer<RoutingContext> handler() {
        return handler;
    }

}
