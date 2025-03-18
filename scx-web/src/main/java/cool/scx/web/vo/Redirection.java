package cool.scx.web.vo;

import cool.scx.http.routing.RoutingContext;
import cool.scx.http.status.HttpStatusCode;

import static cool.scx.http.headers.HttpFieldName.LOCATION;
import static cool.scx.http.status.HttpStatusCode.FOUND;
import static cool.scx.http.status.HttpStatusCode.MOVED_PERMANENTLY;

/// 重定向
///
/// @author scx567888
/// @version 0.0.1
public final class Redirection implements BaseVo {

    private final String location;
    private final HttpStatusCode statusCode;

    private Redirection(String location, HttpStatusCode statusCode) {
        this.location = location;
        this.statusCode = statusCode;
    }

    /// 永久重定向
    ///
    /// @param location 重定向地址
    /// @return r
    public static Redirection ofPermanent(String location) {
        return new Redirection(location, MOVED_PERMANENTLY);
    }

    /// 临时重定向
    ///
    /// @param location 重定向地址
    /// @return r
    public static Redirection ofTemporary(String location) {
        return new Redirection(location, FOUND);
    }

    @Override
    public void accept(RoutingContext routingContext) {
        routingContext.response().setHeader(LOCATION, location).status(statusCode).send();
    }

}
