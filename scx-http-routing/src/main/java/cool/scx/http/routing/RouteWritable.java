package cool.scx.http.routing;

import cool.scx.function.Function1Void;
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

    RouteWritable handler(Function1Void<RoutingContext, ?> handler);

}
