package cool.scx.web.vo;

import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.routing.RoutingContext;
import cool.scx.http.status_code.ScxHttpStatusCode;

import static cool.scx.http.headers.HttpHeaderName.LOCATION;
import static cool.scx.http.status_code.HttpStatusCode.FOUND;
import static cool.scx.http.status_code.HttpStatusCode.MOVED_PERMANENTLY;

/// 重定向
///
/// @author scx567888
/// @version 0.0.1
public final class Redirection implements BaseVo {

    private final String location;
    private final ScxHttpStatusCode status;

    private Redirection(String location, ScxHttpStatusCode status) {
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

    public void handle(ScxHttpServerResponse response) {
        response.setHeader(LOCATION, location).statusCode(status).send();
    }

    @Override
    public void apply(RoutingContext routingContext) {
        handle(routingContext.response());
    }

}
