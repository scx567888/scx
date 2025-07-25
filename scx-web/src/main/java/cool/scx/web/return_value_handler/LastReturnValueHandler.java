package cool.scx.web.return_value_handler;

import cool.scx.http.routing.RoutingContext;

/// 兜底 返回值处理器
///
/// @author scx567888
/// @version 0.0.1
public final class LastReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return true;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) {
        routingContext.response().send(returnValue);
    }

}
