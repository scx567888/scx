package cool.scx.mvc.return_value_handler;

import cool.scx.mvc.ScxMvcReturnValueHandler;
import cool.scx.mvc.vo.BaseVo;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class BaseVoReturnValueHandler implements ScxMvcReturnValueHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Object result) {
        return result instanceof BaseVo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Object result, RoutingContext context) throws Exception {
        ((BaseVo) result).accept(context);
    }

}
