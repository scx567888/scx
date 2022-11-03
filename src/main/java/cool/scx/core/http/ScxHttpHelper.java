package cool.scx.core.http;

import cool.scx.core.ScxConstant;
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
    private static final Set<HttpMethod> defaultAllowedMethods = Set.of(
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
    private static final Set<String> defaultAllowedHeaders = toSet(
            HttpHeaderNames.ACCEPT,
            HttpHeaderNames.CONTENT_TYPE
    );

    /**
     * Constant <code>defaultExposedHeaders</code>
     */
    private static final Set<String> defaultExposedHeaders = toSet(
            HttpHeaderNames.CONTENT_DISPOSITION
    );

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
                .allowedHeaders(defaultAllowedHeaders)
                .allowedMethods(defaultAllowedMethods)
                .exposedHeaders(defaultExposedHeaders)
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
                .setBodyLimit(ScxConstant.DEFAULT_BODY_LIMIT)
                .setMergeFormAttributes(false)
                .setDeleteUploadedFilesOnEnd(true);
    }

}
