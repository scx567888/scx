package cool.scx.http.routing;

import java.util.function.Consumer;

/**
 * WebSocketRouteImpl
 *
 * @author scx567888
 * @version 0.0.1
 */
class WebSocketRouteImpl implements WebSocketRouteWritable {

    private String path;
    private PathMatcher pathMatcher;
    private int order;
    private Consumer<WebSocketRoutingContext> handler;

    public WebSocketRouteImpl() {
        this.path = null;
        this.pathMatcher = PathMatcher.any();
        this.order = 0;
        this.handler = WebSocketRoutingContext::next;
    }

    @Override
    public WebSocketRouteImpl path(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.of(path);
        return this;
    }

    @Override
    public WebSocketRouteImpl pathRegex(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.ofRegex(path);
        return this;
    }

    @Override
    public WebSocketRouteImpl order(int order) {
        this.order = order;
        return this;
    }

    @Override
    public WebSocketRouteImpl handler(Consumer<WebSocketRoutingContext> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public PathMatcher pathMatcher() {
        return pathMatcher;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public Consumer<WebSocketRoutingContext> handler() {
        return handler;
    }

}
