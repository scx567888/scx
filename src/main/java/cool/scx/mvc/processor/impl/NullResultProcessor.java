package cool.scx.mvc.processor.impl;

import cool.scx.mvc.processor.ScxMappingResultProcessor;
import io.vertx.ext.web.RoutingContext;

public final class NullResultProcessor implements ScxMappingResultProcessor {

    @Override
    public boolean canHandle(Object result) {
        return result == null;
    }

    @Override
    public void handle(Object result, RoutingContext context) {
        context.request().response().end();
    }

}
