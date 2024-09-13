package cool.scx.http.routing;

import cool.scx.http.ScxServerWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WebSocketRouter implements Consumer<ScxServerWebSocket> {

    final List<WebSocketRoute> routes;
    BiConsumer<Throwable, WebSocketRoutingContext> exceptionHandler;

    public WebSocketRouter() {
        this.routes = new ArrayList<>();
    }

    public WebSocketRouter addRoute(WebSocketRoute route) {
        //todo order 需要处理
        routes.add(route);
        return this;
    }

    public List<WebSocketRoute> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public void accept(ScxServerWebSocket scxServerWebSocket) {
        new WebSocketRoutingContext(this, scxServerWebSocket).next();
    }

    public WebSocketRouter exceptionHandler(BiConsumer<Throwable, WebSocketRoutingContext> handler) {
        this.exceptionHandler = handler;
        return this;
    }

}
