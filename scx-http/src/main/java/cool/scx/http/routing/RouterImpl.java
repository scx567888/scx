package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.BiConsumer;

/// Router
///
/// @author scx567888
/// @version 0.0.1
public class RouterImpl implements Router {

    private static final Comparator<Route> ROUTE_COMPARATOR = (o1, o2) -> {
        var compare = Integer.compare(o1.order(), o2.order());
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

    public RouterImpl() {
        this.routes = new TreeSet<>(ROUTE_COMPARATOR);
        this.errorHandler = null;
    }

    @Override
    public RouterImpl addRoute(Route route) {
        routes.add(route);
        return this;
    }

    @Override
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public void accept(ScxHttpServerRequest scxHttpRequest) {
        new RoutingContextImpl(this, scxHttpRequest).next();
    }

    @Override
    public RouterImpl errorHandler(BiConsumer<Throwable, RoutingContext> handler) {
        this.errorHandler = handler;
        return this;
    }

}
