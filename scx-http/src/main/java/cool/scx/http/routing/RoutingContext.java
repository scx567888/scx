package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.exception.MethodNotAllowedException;
import cool.scx.http.exception.NotFoundException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * RoutingContext
 */
public class RoutingContext {

    private final RouterImpl router;
    private final ScxHttpServerRequest request;
    private final Iterator<Route> iter;
    private Parameters<String, String> nowPathParams;

    RoutingContext(RouterImpl router, ScxHttpServerRequest request) {
        this.router = router;
        this.request = request;
        this.iter = router.routes.iterator();
    }

    public ScxHttpServerRequest request() {
        return request;
    }

    public ScxHttpServerResponse response() {
        return request.response();
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
            var pathMatchResult = routeState.pathMatcher().matches(request.path());

            this.nowPathParams = pathMatchResult.pathParams();

            //匹配不到就下一次
            if (!pathMatchResult.accepted()) {
                continue;
            }

            //匹配方法
            var methodMatchResult = routeState.methodMatcher().matches(request.method());

            //匹配方法失败
            if (!methodMatchResult) {
                e = new MethodNotAllowedException();
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
    
    private final Map<String,Object> contentParams = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) contentParams.get(name);
    }

    public RoutingContext put(String name, Object value) {
        contentParams.put(name,value);
        return this;
    }

}
