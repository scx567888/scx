package cool.scx.http.routing;

import cool.scx.http.HttpMethod;

import java.util.function.Consumer;

/**
 * RouteWritable
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface RouteWritable extends Route {

    RouteWritable path(String path);

    RouteWritable pathRegex(String path);

    RouteWritable method(HttpMethod... httpMethods);

    RouteWritable order(int order);

    RouteWritable handler(Consumer<RoutingContext> handler);

}
