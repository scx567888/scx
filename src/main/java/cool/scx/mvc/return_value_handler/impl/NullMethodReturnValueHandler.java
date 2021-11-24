package cool.scx.mvc.return_value_handler.impl;

import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class NullMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    public static final NullMethodReturnValueHandler DEFAULT_INSTANCE = new NullMethodReturnValueHandler();

    @Override
    public boolean canHandle(Object result) {
        return result == null;
    }

    @Override
    public void handle(Object result, RoutingContext context) {
        context.request().response().end();
    }

}
