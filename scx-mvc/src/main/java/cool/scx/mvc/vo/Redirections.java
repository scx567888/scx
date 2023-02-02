package cool.scx.mvc.vo;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;

/**
 * 重定向
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class Redirections implements BaseVo {

    /**
     * 待重定向的地址
     */
    private final String location;

    /**
     * 状态码
     */
    private final int statusCode;


    /**
     * <p>Constructor for Redirections.</p>
     *
     * @param location   a {@link java.lang.String} object
     * @param statusCode a int
     */
    private Redirections(String location, int statusCode) {
        this.location = location;
        this.statusCode = statusCode;
    }

    /**
     * 永久重定向
     *
     * @param location 重定向地址
     * @return r
     */
    public static Redirections ofPermanent(String location) {
        return new Redirections(location, 301);
    }

    /**
     * 临时重定向
     *
     * @param location 重定向地址
     * @return r
     */
    public static Redirections ofTemporary(String location) {
        return new Redirections(location, 302);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(RoutingContext routingContext) {
        routingContext.request().response().putHeader(HttpHeaderNames.LOCATION, location).setStatusCode(statusCode).end();
    }

}
