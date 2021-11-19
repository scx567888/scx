package cool.scx.mvc.processor.impl;

import cool.scx.mvc.processor.ScxMappingResultProcessor;
import cool.scx.util.ObjectUtils;
import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDateTime;

public final class CanConvertStringResultProcessor implements ScxMappingResultProcessor {

    @Override
    public boolean canHandle(Object o) {
        return (o instanceof String
                || o instanceof Integer
                || o instanceof Double
                || o instanceof Boolean
                || o instanceof LocalDateTime
                || o instanceof Long);
    }

    @Override
    public void handle(Object result, RoutingContext context) {
        VoHelper.fillTextPlainContentType(context.request().response())
                .end(ObjectUtils.convertValue(result, String.class));
    }

}
