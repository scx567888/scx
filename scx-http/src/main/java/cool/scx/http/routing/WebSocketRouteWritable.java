package cool.scx.http.routing;

import java.util.function.Consumer;

/**
 * WebSocketRouteWritable
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface WebSocketRouteWritable extends WebSocketRoute {

    WebSocketRouteWritable path(String path);

    WebSocketRouteWritable pathRegex(String path);

    WebSocketRouteWritable order(int order);

    WebSocketRouteWritable handler(Consumer<WebSocketRoutingContext> handler);

}
