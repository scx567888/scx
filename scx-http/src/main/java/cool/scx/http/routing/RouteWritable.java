package cool.scx.http.routing;

import cool.scx.http.HttpMethod;
import cool.scx.http.routing.TypeMatcher.Type;

import java.util.function.Consumer;

/**
 * RouteWritable
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface RouteWritable extends Route {

    RouteWritable type(Type type);

    RouteWritable path(String path);

    RouteWritable pathRegex(String path);

    RouteWritable method(HttpMethod... httpMethods);

    RouteWritable order(int order);

    RouteWritable handler(Consumer<RoutingContext> handler);

}
