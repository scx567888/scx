package cool.scx.mvc.return_value_handler;

import cool.scx.common.util.ObjectUtils;
import cool.scx.mvc.ScxMvcReturnValueHandler;
import cool.scx.mvc.vo.BaseVo;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class StringReturnValueHandler implements ScxMvcReturnValueHandler {

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
