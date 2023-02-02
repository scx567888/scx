package cool.scx.enumeration;

/**
 * <p>HttpMethod class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public enum HttpMethod {

    OPTION,

    GET,

    HEAD,

    POST,

    PUT,

    PATCH,

    DELETE,

    TRANCE,

    CONNECT;

    private final io.vertx.core.http.HttpMethod vertxMethod;

    HttpMethod() {
        this.vertxMethod = io.vertx.core.http.HttpMethod.valueOf(this.name());
    }

    /**
     * <p>vertxMethod.</p>
     *
     * @return a {@link io.vertx.core.http.HttpMethod} object
     */
    public io.vertx.core.http.HttpMethod vertxMethod() {
        return this.vertxMethod;
    }

}
