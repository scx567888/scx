package cool.scx.web.return_value_handler;

import cool.scx.http.routing.RoutingContext;

/// String 类型处理器
///
/// @author scx567888
/// @version 0.0.1
public final class StringReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return returnValue instanceof String;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) {
        if (returnValue instanceof String str) {
            routingContext.response().send(str);
        } else {
            throw new IllegalArgumentException("参数不是 String 类型 !!! " + returnValue.getClass());
        }
    }

}
