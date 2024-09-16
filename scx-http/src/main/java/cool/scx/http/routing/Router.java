package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Router implements Consumer<ScxHttpServerRequest> {

    private static final Comparator<Route> ROUTE_COMPARATOR = (Route o1, Route o2) -> {
        final int compare = Integer.compare(o1.order(), o2.order());
        if (compare == 0) {
            if (o1.equals(o2)) {
                return 0;
            }
            return 1;
        }
        return compare;
    };

    final TreeSet<Route> routes;
    BiConsumer<Throwable, RoutingContext> errorHandler;

    public Router() {
        this.routes = new TreeSet<>(ROUTE_COMPARATOR);
    }

    public Router addRoute(Route route) {
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

    public Router errorHandler(BiConsumer<Throwable, RoutingContext> handler) {
        this.errorHandler = handler;
        return this;
    }

}
