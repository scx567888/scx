package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.exception.NotFoundException;
import cool.scx.http.web_socket.ScxServerWebSocket;

import java.util.Iterator;

/**
 * WebSocketRoutingContext
 * *
 *
 * @author scx567888
 * @version 0.0.1
 */
public class WebSocketRoutingContext {

    private final WebSocketRouter router;
    private final ScxServerWebSocket webSocket;
    private final Iterator<WebSocketRoute> iter;
    private Parameters<String, String> nowPathParams;

    public WebSocketRoutingContext(WebSocketRouter router, ScxServerWebSocket webSocket) {
        this.router = router;
        this.webSocket = webSocket;
        this.iter = router.routes.iterator();
    }

    public ScxServerWebSocket webSocket() {
        return webSocket;
    }

    public final void next() {
        try {
            tryNext();
        } catch (Throwable e) {
            router.errorHandler.accept(e, this);
        }
    }

    void tryNext() throws Throwable {

        Throwable e = new NotFoundException();

        while (iter.hasNext()) {
            var routeState = iter.next();

            //匹配路径
            var pathMatchResult = routeState.pathMatcher().matches(webSocket.path());

            this.nowPathParams = pathMatchResult.pathParams();

            //匹配不到就下一次
            if (!pathMatchResult.accepted()) {
                continue;
            }

            routeState.handler().accept(this);

            return;

        }

        throw e;

    }

    public Parameters<String, String> pathParams() {
        return this.nowPathParams;
    }

}
