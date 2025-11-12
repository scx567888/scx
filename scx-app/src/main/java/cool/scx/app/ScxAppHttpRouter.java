package cool.scx.app;


import cool.scx.http.headers.HttpHeaderName;
import cool.scx.http.method.HttpMethod;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.RouterImpl;
import cool.scx.http.routing.handler.CorsHandler;


import static cool.scx.http.headers.HttpHeaderName.*;
import static cool.scx.http.method.HttpMethod.*;

/// ScxHttp 路由 内部使用 Router 进行具体路由的处理
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppHttpRouter extends RouterImpl {

    private static final HttpMethod[] DEFAULT_ALLOWED_METHODS = new HttpMethod[]{GET, POST, OPTIONS, DELETE, PATCH, PUT};
    private static final HttpHeaderName[] DEFAULT_ALLOWED_HEADERS = new HttpHeaderName[]{ACCEPT, CONTENT_TYPE};
    private static final HttpHeaderName[] DEFAULT_EXPOSED_HEADERS = new HttpHeaderName[]{CONTENT_DISPOSITION};

    //基本 handler
    private final CorsHandler corsHandler;
    //基本 handler 对应的 路由
    private final Route corsHandlerRoute;

    public ScxAppHttpRouter(ScxApp scx) {
        //设置基本的 handler
        this.corsHandler = initCorsHandler(scx.scxOptions().allowedOrigin());
        //注册路由
        this.corsHandlerRoute = this.route(-10000).handler(corsHandler);
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
