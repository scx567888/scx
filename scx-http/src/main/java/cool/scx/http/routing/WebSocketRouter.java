package cool.scx.http.routing;

import cool.scx.http.web_socket.ScxServerWebSocket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * WebSocketRouter
 */
public class WebSocketRouter implements Consumer<ScxServerWebSocket> {

    private static final Comparator<WebSocketRoute> ROUTE_COMPARATOR = (o1, o2) -> {
        var compare = Integer.compare(o1.order(), o2.order());
        if (compare == 0) {
            if (o1.equals(o2)) {
                return 0;
            }
            return 1;
        }
        return compare;
    };

    final TreeSet<WebSocketRoute> routes;
    BiConsumer<Throwable, WebSocketRoutingContext> errorHandler;

    public WebSocketRouter() {
        this.routes = new TreeSet<>(ROUTE_COMPARATOR);
    }

    public WebSocketRouter addRoute(WebSocketRoute route) {
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

    public WebSocketRouter errorHandler(BiConsumer<Throwable, WebSocketRoutingContext> handler) {
        this.errorHandler = handler;
        return this;
    }

}
