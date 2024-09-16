package cool.scx.http.routing;

import java.util.function.Consumer;

/**
 * WebSocketRouteWritable
 */
public interface WebSocketRouteWritable extends WebSocketRoute {

    WebSocketRouteWritable path(String path);

    WebSocketRouteWritable pathRegex(String path);

    WebSocketRouteWritable order(int order);

    WebSocketRouteWritable handler(Consumer<WebSocketRoutingContext> handler);

}
