package cool.scx.vo;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;

/**
 * 重定向
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

    @Override
    public void handle(RoutingContext routingContext) throws Exception {
        routingContext.request().response().putHeader(HttpHeaderNames.LOCATION, location).setStatusCode(statusCode).end();
    }

}