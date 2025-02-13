package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.exception.MethodNotAllowedException;
import cool.scx.http.exception.NotFoundException;
import cool.scx.http.exception.ScxHttpException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static cool.scx.http.HttpStatusCode.INTERNAL_SERVER_ERROR;

/**
 * RoutingContext
 *
 * @author scx567888
 * @version 0.0.1
 */
public class RoutingContext {

    private final RouterImpl router;
    private final ScxHttpServerRequest request;
    private final Iterator<Route> iter;
    private final Map<String, Object> contentParams;
    private Parameters<String, String> nowPathParams;

    RoutingContext(RouterImpl router, ScxHttpServerRequest request) {
        this.router = router;
        this.request = request;
        this.iter = router.routes.iterator();
        this.contentParams = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends ScxHttpServerRequest> T request() {
        return (T) request;
    }

    @SuppressWarnings("unchecked")
    public <T extends ScxHttpServerResponse> T response() {
        return (T) request.response();
    }

    public final void next() {
        try {
            tryNext();
        } catch (Throwable e) {
            //如果有自定义的处理器则使用
            if (router.errorHandler != null) {
                router.errorHandler.accept(e, this);
            } else {
                if (e instanceof ScxHttpException httpException) {
                    var code = httpException.statusCode();
                    response().status(code).send(code.toString());
                } else {
                    response().status(INTERNAL_SERVER_ERROR).send("Internal Server Error");
                }
            }
        }
    }

    void tryNext() throws Throwable {

        Throwable e = new NotFoundException();

        while (iter.hasNext()) {
            var route = iter.next();

            //匹配类型
            var typeMatcherResult = route.typeMatcher().matches(request);

            if (!typeMatcherResult) {
                continue;
            }

            //匹配路径
            var pathMatchResult = route.pathMatcher().matches(request.path());

            this.nowPathParams = pathMatchResult.pathParams();

            //匹配不到就下一次
            if (!pathMatchResult.accepted()) {
                continue;
            }

            //匹配方法
            var methodMatchResult = route.methodMatcher().matches(request.method());

            //匹配方法失败
            if (!methodMatchResult) {
                e = new MethodNotAllowedException();
                continue;
            }

            route.handler().accept(this);

            return;

        }

        throw e;

    }

    public Parameters<String, String> pathParams() {
        return this.nowPathParams;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) contentParams.get(name);
    }

    public RoutingContext put(String name, Object value) {
        contentParams.put(name, value);
        return this;
    }

}
