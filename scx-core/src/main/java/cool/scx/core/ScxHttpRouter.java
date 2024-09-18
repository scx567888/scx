package cool.scx.core;

import cool.scx.common.util.FileUtils;
import cool.scx.http.HttpFieldName;
import cool.scx.http.HttpMethod;
import cool.scx.http.handler.CorsHandler;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.RouterImpl;

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

    /**
     * 默认 http 请求 body 限制大小
     */
    private static final long DEFAULT_BODY_LIMIT = FileUtils.displaySizeToLong("16384KB");

    //基本 handler
    private final CorsHandler corsHandler;
    //    private final BodyHandler bodyHandler;
    //基本 handler 对应的 路由
    private final Route corsHandlerRoute;
//    private final Route bodyHandlerRoute;

    public ScxHttpRouter(Scx scx) {
//        super(scx.vertx());
        //设置基本的 handler
        this.corsHandler = initCorsHandler(scx.scxOptions().allowedOrigin());
//        this.bodyHandler = initBodyHandler(scx.scxEnvironment().getTempPath(BodyHandler.DEFAULT_UPLOADS_DIRECTORY));
        //注册路由
        this.corsHandlerRoute = this.route().handler(corsHandler);
//        this.bodyHandlerRoute = this.route().handler(bodyHandler);
    }

    private static CorsHandler initCorsHandler(String allowedOriginPattern) {
        return new CorsHandler().addOrigin(allowedOriginPattern)
                .allowedHeader(DEFAULT_ALLOWED_HEADERS)
                .allowedMethod(DEFAULT_ALLOWED_METHODS)
                .exposedHeader(DEFAULT_EXPOSED_HEADERS)
                .allowCredentials(true);
    }

    /**
     * <p>initBodyHandler.</p>
     *
     * @param uploadDirectory a {@link java.nio.file.Path} object
     * @return a {@link io.vertx.ext.web.handler.BodyHandler} object
     */
//    static BodyHandler initBodyHandler(Path uploadDirectory) {
//        return new BodyHandlerImpl(uploadDirectory.toString())
//                .setBodyLimit(DEFAULT_BODY_LIMIT)
//                .setMergeFormAttributes(false)
//                .setDeleteUploadedFilesOnEnd(true);
//    }
//

    public CorsHandler corsHandler() {
        return corsHandler;
    }

//    /**
//     * a
//     *
//     * @return a
//     */
//    public BodyHandler bodyHandler() {
//        return bodyHandler;
//    }

    public Route corsHandlerRoute() {
        return corsHandlerRoute;
    }

//    /**
//     * a
//     *
//     * @return a
//     */
//    public Route bodyHandlerRoute() {
//        return bodyHandlerRoute;
//    }

}
