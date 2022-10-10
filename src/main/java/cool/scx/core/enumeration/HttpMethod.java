package cool.scx.core.enumeration;

/**
 * <p>HttpMethod class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public enum HttpMethod {

    /**
     * a
     */
    POST,

    /**
     * a
     */
    GET,

    /**
     * a
     */
    PUT,

    /**
     * a
     */
    DELETE,

    /**
     * a
     */
    HEAD,

    /**
     * a
     */
    TRANCE,

    /**
     * a
     */
    CONNECT,

    /**
     * a
     */
    PATCH,

    /**
     * a
     */
    OPTION;

    /**
     * <p>Constructor for HttpMethod.</p>
     */
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
