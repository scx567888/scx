package cool.scx.web.vo;

import cool.scx.http.routing.RoutingContext;
import cool.scx.http.status.ScxHttpStatus;

import static cool.scx.http.headers.HttpFieldName.LOCATION;
import static cool.scx.http.status.HttpStatus.FOUND;
import static cool.scx.http.status.HttpStatus.MOVED_PERMANENTLY;

/// 重定向
///
/// @author scx567888
/// @version 0.0.1
public final class Redirection implements BaseVo {

    private final String location;
    private final ScxHttpStatus status;

    private Redirection(String location, ScxHttpStatus status) {
        this.location = location;
        this.status = status;
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
        routingContext.response().setHeader(LOCATION, location).status(status).send();
    }

}
