package cool.scx.web.handler;

import io.vertx.core.http.HttpMethod;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>ScxCorsHandlerConfiguration class.</p>
 *
 * @author scx567888
 * @version 1.3.14
 */
public final class ScxCorsHandlerConfiguration {

    private static final Set<HttpMethod> allowedMethods = new LinkedHashSet<>();
    private static final Set<String> allowedHeaders = new LinkedHashSet<>();
    private static final boolean allowCredentials = true;

    static {
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("X-PINGARUNER");

        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);
    }

    /**
     * <p>allowedHeaders.</p>
     *
     * @param headers a {@link java.util.Set} object
     */
    public static void allowedHeaders(Set<String> headers) {
        allowedHeaders.addAll(headers);
    }

    /**
     * <p>allowedHeader.</p>
     *
     * @param header a {@link java.lang.String} object
     */
    public static void allowedHeader(String header) {
        allowedHeaders.add(header);
    }

    /**
     * <p>allowedMethods.</p>
     *
     * @param methods a {@link java.util.Set} object
     */
    public static void allowedMethods(Set<HttpMethod> methods) {
        allowedMethods.addAll(methods);
    }

    /**
     * <p>allowedMethod.</p>
     *
     * @param method a {@link io.vertx.core.http.HttpMethod} object
     */
    public static void allowedMethod(HttpMethod method) {
        allowedMethods.add(method);
    }

    /**
     * <p>allowedHeaders.</p>
     *
     * @return a {@link java.util.Set} object
     */
    public static Set<String> allowedHeaders() {
        return allowedHeaders;
    }

    /**
     * <p>isAllowCredentials.</p>
     *
     * @return a boolean
     */
    public static boolean isAllowCredentials() {
        return allowCredentials;
    }

    /**
     * <p>allowedMethods.</p>
     *
     * @return a {@link java.util.Set} object
     */
    public static Set<HttpMethod> allowedMethods() {
        return allowedMethods;
    }

}
