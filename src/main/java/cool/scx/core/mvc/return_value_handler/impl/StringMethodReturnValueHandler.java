package cool.scx.core.mvc.return_value_handler.impl;

import cool.scx.core.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import cool.scx.core.vo.BaseVo;
import cool.scx.util.ObjectUtils;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class StringMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    /**
     * a
     */
    public static final StringMethodReturnValueHandler DEFAULT_INSTANCE = new StringMethodReturnValueHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Object o) {
        return o instanceof String;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Object result, RoutingContext context) {
        BaseVo.fillTextPlainContentType(context.request().response())
                .end(ObjectUtils.convertValue(result, String.class));
    }

}
