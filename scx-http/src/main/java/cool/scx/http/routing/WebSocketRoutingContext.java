package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.ScxServerWebSocket;
import cool.scx.http.exception.NotFoundException;

import java.util.Iterator;

public class WebSocketRoutingContext {

    private final WebSocketRouter router;
    private final ScxServerWebSocket webSocket;
    private final Iterator<WebSocketRoute> iter;
    private Parameters nowPathParams;

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
            router.exceptionHandler.accept(this, e);
        }
    }

    void tryNext() throws Throwable {

        Throwable e = new NotFoundException();

        while (iter.hasNext()) {
            var routeState = iter.next();

            //匹配路径
            var pathMatchResult = routeState.pathMatcher().matches(webSocket.path().path());

            this.nowPathParams = pathMatchResult.pathParams();

            //匹配不到就下一次
            if (!pathMatchResult.accepted()) {
                e = new NotFoundException();
                continue;
            }

            routeState.handler().accept(this);

            return;

        }

        throw e;

    }

    public Parameters pathParams() {
        return this.nowPathParams;
    }

}
