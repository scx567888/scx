package cool.scx.web.vo;

import cool.scx.common.standard.HttpStatusCode;
import io.vertx.ext.web.RoutingContext;

import static cool.scx.common.standard.HttpFieldName.LOCATION;
import static cool.scx.common.standard.HttpStatusCode.FOUND;
import static cool.scx.common.standard.HttpStatusCode.MOVED_PERMANENTLY;

/**
 * 重定向
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class Redirection implements BaseVo {

    private final String location;
    private final HttpStatusCode statusCode;

    private Redirection(String location, HttpStatusCode statusCode) {
        this.location = location;
        this.statusCode = statusCode;
    }

    /**
     * 永久重定向
     *
     * @param location 重定向地址
     * @return r
     */
    public static Redirection ofPermanent(String location) {
        return new Redirection(location, MOVED_PERMANENTLY);
    }

    /**
     * 临时重定向
     *
     * @param location 重定向地址
     * @return r
     */
    public static Redirection ofTemporary(String location) {
        return new Redirection(location, FOUND);
    }

    @Override
    public void accept(RoutingContext routingContext) {
        routingContext.request().response().putHeader(LOCATION.toString(), location).setStatusCode(statusCode.code()).end();
    }

}
