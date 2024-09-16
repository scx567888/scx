package cool.scx.core;

import cool.scx.common.util.FileUtils;
import cool.scx.http.HttpFieldName;
import cool.scx.http.HttpMethod;
import cool.scx.http.routing.Router;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ScxHttp 路由 内部使用 vertxRouter 进行具体路由的处理
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxHttpRouter extends Router {

    /**
     * Constant <code>defaultAllowedMethods</code>
     */
    private static final Set<HttpMethod> DEFAULT_ALLOWED_METHODS = Set.of(
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.OPTIONS,
            HttpMethod.DELETE,
            HttpMethod.PATCH,
            HttpMethod.PUT
    );
    /**
     * Constant <code>defaultAllowedHeaders</code>
     */
    private static final Set<String> DEFAULT_ALLOWED_HEADERS = toSet(
            HttpFieldName.ACCEPT,
            HttpFieldName.CONTENT_TYPE
    );
    /**
     * Constant <code>defaultExposedHeaders</code>
     */
    private static final Set<String> DEFAULT_EXPOSED_HEADERS = toSet(
            HttpFieldName.CONTENT_DISPOSITION
    );

    /**
     * 默认 http 请求 body 限制大小
     */
    private static final long DEFAULT_BODY_LIMIT = FileUtils.displaySizeToLong("16384KB");

    //基本 handler
//    private final CorsHandler corsHandler;
//    private final BodyHandler bodyHandler;
    //基本 handler 对应的 路由
//    private final Route corsHandlerRoute;
//    private final Route bodyHandlerRoute;

    public ScxHttpRouter(Scx scx) {
//        super(scx.vertx());
        //设置基本的 handler
//        this.corsHandler = initCorsHandler(scx.scxOptions().allowedOrigin());
//        this.bodyHandler = initBodyHandler(scx.scxEnvironment().getTempPath(BodyHandler.DEFAULT_UPLOADS_DIRECTORY));
        //注册路由
//        this.corsHandlerRoute = this.route().handler(corsHandler);
//        this.bodyHandlerRoute = this.route().handler(bodyHandler);
    }

    /**
     * <p>toSet.</p>
     *
     * @param accept
     * @param contentType
     * @param values      a {@link io.netty.util.AsciiString} object
     * @return a {@link java.util.Set} object
     */
    private static Set<String> toSet(HttpFieldName...  values) {
        return Stream.of(values).map(HttpFieldName::value).collect(Collectors.toSet());
    }

    /**
     * <p>initCorsHandler.</p>
     *
     * @param allowedOriginPattern a {@link java.lang.String} object
     * @return a {@link io.vertx.ext.web.handler.CorsHandler} object
     */
//    static CorsHandler initCorsHandler(String allowedOriginPattern) {
//        return new CorsHandlerImpl().addOrigin(allowedOriginPattern)
//                .allowedHeaders(DEFAULT_ALLOWED_HEADERS)
//                .allowedMethods(DEFAULT_ALLOWED_METHODS)
//                .exposedHeaders(DEFAULT_EXPOSED_HEADERS)
//                .allowCredentials(true);
//    }

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
//    /**
//     * a
//     *
//     * @return a
//     */
//    public CorsHandler corsHandler() {
//        return corsHandler;
//    }
//
//    /**
//     * a
//     *
//     * @return a
//     */
//    public BodyHandler bodyHandler() {
//        return bodyHandler;
//    }
//
//    /**
//     * a
//     *
//     * @return a
//     */
//    public Route corsHandlerRoute() {
//        return corsHandlerRoute;
//    }

//    /**
//     * a
//     *
//     * @return a
//     */
//    public Route bodyHandlerRoute() {
//        return bodyHandlerRoute;
//    }

}
