package cool.scx.http.routing;

import cool.scx.functional.ScxConsumer;
import cool.scx.http.method.HttpMethod;

/// RouteWritable
///
/// @author scx567888
/// @version 0.0.1
public interface RouteWritable extends Route {

    RouteWritable type(TypeMatcher typeMatcher);

    RouteWritable path(String path);

    RouteWritable pathRegex(String path);

    RouteWritable method(HttpMethod... httpMethods);

    RouteWritable order(int order);

    RouteWritable handler(ScxConsumer<RoutingContext, ?> handler);

}
