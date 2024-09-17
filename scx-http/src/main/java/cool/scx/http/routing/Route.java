package cool.scx.http.routing;

import cool.scx.http.HttpMethod;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Route 路由只保存状态不做行为处理
 */
public interface Route {

    static RouteWritable of() {
        return new RouteImpl();
    }

    String path();

    Set<HttpMethod> methods();

    PathMatcher pathMatcher();

    MethodMatcher methodMatcher();

    int order();

    Consumer<RoutingContext> handler();

}
