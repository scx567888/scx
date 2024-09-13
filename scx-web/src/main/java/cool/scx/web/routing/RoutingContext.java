package cool.scx.web.routing;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RoutingContext {

    private static final System.Logger logger = System.getLogger(RoutingContext.class.getName());
    protected final Iterator<Route> iter;
    private final ScxHttpServerRequest request;

    public RoutingContext(ScxHttpServerRequest request, List<Route> routes) {
        this.request = request;
        this.iter = routes.iterator();
    }

    public ScxHttpServerRequest request() {
        return request;
    }

    public ScxHttpServerResponse response() {
        return request.response();
    }

    public final void next() {
        iterateNext();
    }

    boolean iterateNext() {

        var matchResult = 0;
        
        while (iter.hasNext()) {
            var routeState = iter.next();
            
            //匹配路径
            var pathMatchResult = routeState.pathMatcher().matches(request.path().path());

            //匹配不到就下一次
            if (!pathMatchResult.accepted()) {
                matchResult = 404;
                continue;
            }

            //匹配方法
            var methodMatchResult = routeState.methodMatcher().matches(request.method());

            //匹配方法失败
            if (!methodMatchResult) {
                matchResult = 405;
                continue;
            }

            routeState.handler().accept(this);

            return true;

        }
        return false;
    }

    public Map<String, String> pathParams() {
        return null;
    }

}
