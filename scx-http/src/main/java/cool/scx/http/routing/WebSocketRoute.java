package cool.scx.http.routing;

import cool.scx.http.PathMatcher;

import java.util.function.Consumer;

/**
 * 路由只保存状态不做行为处理
 */
public class WebSocketRoute {

    private String path;
    private PathMatcher pathMatcher;
    private int order;
    private Consumer<WebSocketRoutingContext> handler;

    public WebSocketRoute() {
        this.path = null;
        this.pathMatcher = PathMatcher.any();
        this.order = Integer.MAX_VALUE;
        this.handler = WebSocketRoutingContext::next;
    }

    public WebSocketRoute path(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.of(path);
        return this;
    }

    public WebSocketRoute pathRegex(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.ofRegex(path);
        return this;
    }

    public WebSocketRoute order(int order) {
        this.order = order;
        return this;
    }

    public WebSocketRoute handler(Consumer<WebSocketRoutingContext> handler) {
        this.handler = handler;
        return this;
    }

    public String path() {
        return path;
    }

    public PathMatcher pathMatcher() {
        return pathMatcher;
    }

    public int order() {
        return order;
    }

    public Consumer<WebSocketRoutingContext> handler() {
        return handler;
    }

}
