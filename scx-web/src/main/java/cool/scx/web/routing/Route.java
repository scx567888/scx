package cool.scx.web.routing;

import cool.scx.http.HttpMethod;
import cool.scx.http.MethodMatcher;
import cool.scx.http.PathMatcher;

import java.util.function.Consumer;

/**
 * 路由只保存状态不做行为处理
 */
public class Route {

    private String path;
    private PathMatcher pathMatcher;
    private MethodMatcher methodMatcher;
    private int order;
    private Consumer<RoutingContext> handler;

    public Route() {
        this.pathMatcher = PathMatcher.any();
        this.methodMatcher = MethodMatcher.any();
        this.order = Integer.MAX_VALUE;
        this.handler = RoutingContext::next;
    }

    public Route path(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.of(path);
        return this;
    }

    public Route pathRegex(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.ofRegex(path);
        return this;
    }

    public Route method(HttpMethod... httpMethods) {
        this.methodMatcher = MethodMatcher.of(httpMethods);
        return this;
    }

    public Route order(int order) {
        this.order = order;
        return this;
    }

    public Route handler(Consumer<RoutingContext> handler) {
        this.handler = handler;
        return this;
    }

    public String path() {
        return path;
    }

    public PathMatcher pathMatcher() {
        return pathMatcher;
    }

    public MethodMatcher methodMatcher() {
        return methodMatcher;
    }

    public int order() {
        return order;
    }

    public Consumer<RoutingContext> handler() {
        return handler;
    }

}
