package cool.scx.mvc.return_value_handler.impl;

import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class NullMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    /**
     * a
     */
    public static final NullMethodReturnValueHandler DEFAULT_INSTANCE = new NullMethodReturnValueHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Object result) {
        return result == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Object result, RoutingContext context) {
        context.request().response().end();
    }

}
