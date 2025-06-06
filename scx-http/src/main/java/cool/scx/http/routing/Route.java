package cool.scx.http.routing;

import cool.scx.common.functional.ScxConsumer;
import cool.scx.http.method.HttpMethod;

import java.util.Set;

/// Route 路由只保存状态不做行为处理
///
/// @author scx567888
/// @version 0.0.1
public interface Route {

    static RouteWritable of() {
        return new RouteImpl();
    }

    String path();

    Set<HttpMethod> methods();

    TypeMatcher typeMatcher();

    PathMatcher pathMatcher();

    MethodMatcher methodMatcher();

    int order();

    ScxConsumer<RoutingContext, ?> handler();

}
