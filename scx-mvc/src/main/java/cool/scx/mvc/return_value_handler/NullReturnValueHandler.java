package cool.scx.mvc.return_value_handler;

import cool.scx.mvc.ScxMvcReturnValueHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class NullReturnValueHandler implements ScxMvcReturnValueHandler {

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
