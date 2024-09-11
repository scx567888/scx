package cool.scx.web.routing;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;

import java.util.Iterator;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

public class RoutingContext {

    private static final System.Logger logger = System.getLogger(RoutingContext.class.getName());
    protected final Iterator<Route> iter;
    private final ScxHttpServerRequest request;

    public RoutingContext(ScxHttpServerRequest request, List<Route> routes) {
        this.request = request;
        this.iter = routes.iterator();
    }

    public ScxHttpServerRequest request() {
        return request;
    }

    public ScxHttpServerResponse response() {
        return request.response();
    }

    public final void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(request)) {
                handle(next);
                return;
            }
        }
    }

    public void handle(Route route) {
        try {
            route.handler().accept(this);
        } catch (Exception e) {
            logger.log(ERROR, "ScxWebSocketRoute : onOpen 发生异常 !!!", e);
        }
    }

}
