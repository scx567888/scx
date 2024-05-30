package cool.scx.web.return_value_handler;

import io.vertx.ext.web.RoutingContext;

import static cool.scx.web.ScxWebHelper.fillTextPlainContentType;

/**
 * String 类型处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class StringReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return returnValue instanceof String;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) {
        if (returnValue instanceof String str) {
            fillTextPlainContentType(routingContext.request().response()).end(str);
        } else {
            throw new IllegalArgumentException("参数不是 String 类型 !!! " + returnValue.getClass());
        }
    }

}
