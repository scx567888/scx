package cool.scx.mvc.http;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.AsciiString;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.impl.BodyHandlerImpl;
import io.vertx.ext.web.handler.impl.CorsHandlerImpl;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>ScxHttpHelper class.</p>
 *
 * @author scx567888
 * @version 1.18.2
 */
public class ScxHttpHelper {

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
            HttpHeaderNames.ACCEPT,
            HttpHeaderNames.CONTENT_TYPE
    );

    /**
     * Constant <code>defaultExposedHeaders</code>
     */
    private static final Set<String> DEFAULT_EXPOSED_HEADERS = toSet(
            HttpHeaderNames.CONTENT_DISPOSITION
    );


    /**
     * <p>toSet.</p>
     *
     * @param values a {@link io.netty.util.AsciiString} object
     * @return a {@link java.util.Set} object
     */
    private static Set<String> toSet(AsciiString... values) {
        return Stream.of(values).map(AsciiString::toString).collect(Collectors.toSet());
    }

    /**
     * <p>initCorsHandler.</p>
     *
     * @param allowedOriginPattern a {@link java.lang.String} object
     * @return a {@link io.vertx.ext.web.handler.CorsHandler} object
     */
    static CorsHandler initCorsHandler(String allowedOriginPattern) {
        return new CorsHandlerImpl().addOrigin(allowedOriginPattern)
                .allowedHeaders(DEFAULT_ALLOWED_HEADERS)
                .allowedMethods(DEFAULT_ALLOWED_METHODS)
                .exposedHeaders(DEFAULT_EXPOSED_HEADERS)
                .allowCredentials(true);
    }

    /**
     * <p>initBodyHandler.</p>
     *
     * @param uploadDirectory a {@link java.nio.file.Path} object
     * @return a {@link io.vertx.ext.web.handler.BodyHandler} object
     */
    static BodyHandler initBodyHandler(Path uploadDirectory, long bodyLimit) {
        return new BodyHandlerImpl(uploadDirectory != null ? uploadDirectory.toString() : null)
                .setBodyLimit(bodyLimit)
                .setMergeFormAttributes(false)
                .setDeleteUploadedFilesOnEnd(true);
    }

    /**
     * request 是否还可用 (可以写入数据)
     *
     * @param context ctx
     * @return 是否可用
     */
    public static boolean responseCanUse(RoutingContext context) {
        return !context.request().response().ended() && !context.request().response().closed();
    }

}
