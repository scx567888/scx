package cool.scx.core.http;

import cool.scx.util.FileUtils;
import io.netty5.handler.codec.http.HttpHeaderNames;
import io.netty5.util.AsciiString;
import io.vertx.core.http.HttpMethod;
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
class ScxHttpHelper {

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
     * 默认 http 请求 body 限制大小
     */
    private static final long DEFAULT_BODY_LIMIT = FileUtils.displaySizeToLong("16384KB");

    /**
     * <p>toSet.</p>
     *
     * @param values a {@link io.netty5.util.AsciiString} object
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
        return new CorsHandlerImpl(allowedOriginPattern)
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
    static BodyHandler initBodyHandler(Path uploadDirectory) {
        return new BodyHandlerImpl(uploadDirectory.toString())
                .setBodyLimit(DEFAULT_BODY_LIMIT)
                .setMergeFormAttributes(false)
                .setDeleteUploadedFilesOnEnd(true);
    }

}
