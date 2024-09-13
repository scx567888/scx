package cool.scx.web.return_value_handler;

import cool.scx.http.routing.RoutingContext;

/**
 * 空值处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class NullReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return returnValue == null;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) {
        routingContext.request().response().send();
    }

}
