package cool.scx.core;

import cool.scx.http.HttpFieldName;
import cool.scx.http.HttpMethod;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.RouterImpl;
import cool.scx.http.routing.handler.CorsHandler;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpMethod.*;

/**
 * ScxHttp 路由 内部使用 vertxRouter 进行具体路由的处理
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxHttpRouter extends RouterImpl {

    private static final HttpMethod[] DEFAULT_ALLOWED_METHODS = new HttpMethod[]{GET, POST, OPTIONS, DELETE, PATCH, PUT};
    private static final HttpFieldName[] DEFAULT_ALLOWED_HEADERS = new HttpFieldName[]{ACCEPT, CONTENT_TYPE};
    private static final HttpFieldName[] DEFAULT_EXPOSED_HEADERS = new HttpFieldName[]{CONTENT_DISPOSITION};

    //基本 handler
    private final CorsHandler corsHandler;
    //基本 handler 对应的 路由
    private final Route corsHandlerRoute;

    public ScxHttpRouter(Scx scx) {
        //设置基本的 handler
        this.corsHandler = initCorsHandler(scx.scxOptions().allowedOrigin());
        //注册路由
        this.corsHandlerRoute = this.route().handler(corsHandler);
    }

    private static CorsHandler initCorsHandler(String allowedOriginPattern) {
        return new CorsHandler().addOrigin(allowedOriginPattern)
                .allowedHeader(DEFAULT_ALLOWED_HEADERS)
                .allowedMethod(DEFAULT_ALLOWED_METHODS)
                .exposedHeader(DEFAULT_EXPOSED_HEADERS)
                .allowCredentials(true);
    }

    public CorsHandler corsHandler() {
        return corsHandler;
    }

    public Route corsHandlerRoute() {
        return corsHandlerRoute;
    }

}
