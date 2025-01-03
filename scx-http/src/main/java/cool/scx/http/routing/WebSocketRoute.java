package cool.scx.http.routing;

import java.util.function.Consumer;

/**
 * WebSocketRoute 路由只保存状态不做行为处理
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface WebSocketRoute {

    static WebSocketRouteWritable of() {
        return new WebSocketRouteImpl();
    }

    String path();

    PathMatcher pathMatcher();

    int order();

    Consumer<WebSocketRoutingContext> handler();

}
