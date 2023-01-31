package cool.scx.mvc.return_value_handler;

import cool.scx.mvc.ScxMappingMethodReturnValueHandler;
import cool.scx.mvc.vo.BaseVo;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class BaseVoMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    /**
     * a
     */
    public static final BaseVoMethodReturnValueHandler DEFAULT_INSTANCE = new BaseVoMethodReturnValueHandler();

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
