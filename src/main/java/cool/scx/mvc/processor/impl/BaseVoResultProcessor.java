package cool.scx.mvc.processor.impl;

import cool.scx.mvc.processor.ScxMappingResultProcessor;
import cool.scx.vo.BaseVo;
import io.vertx.ext.web.RoutingContext;

public final class BaseVoResultProcessor implements ScxMappingResultProcessor {

    @Override
    public boolean canHandle(Object result) {
        return result instanceof BaseVo;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws Exception {
        ((BaseVo) result).handle(context);
    }

}
