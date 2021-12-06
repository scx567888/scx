package cool.scx.mvc.return_value_handler.impl;

import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import cool.scx.vo.BaseVo;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class BaseVoMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    /**
     * a
     */
    public static final BaseVoMethodReturnValueHandler DEFAULT_INSTANCE = new BaseVoMethodReturnValueHandler();

    @Override
    public boolean canHandle(Object result) {
        return result instanceof BaseVo;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws Exception {
        ((BaseVo) result).handle(context);
    }

}
