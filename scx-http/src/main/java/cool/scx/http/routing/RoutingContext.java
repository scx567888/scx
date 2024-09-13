package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.exception.MethodNotAllowedException;
import cool.scx.http.exception.NotFoundException;

import java.util.Iterator;

public class RoutingContext {

    private final Router router;
    private final ScxHttpServerRequest request;
    private final Iterator<Route> iter;
    private Parameters nowPathParams;

    public RoutingContext(Router router, ScxHttpServerRequest request) {
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
            router.exceptionHandler.accept(e, this);
        }
    }

    void tryNext() throws Throwable {

        Throwable e = new NotFoundException();

        while (iter.hasNext()) {
            var routeState = iter.next();

            //匹配路径
            var pathMatchResult = routeState.pathMatcher().matches(request.path().path());

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

    public Parameters pathParams() {
        return this.nowPathParams;
    }

}
