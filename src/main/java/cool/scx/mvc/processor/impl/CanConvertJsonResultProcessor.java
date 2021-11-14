package cool.scx.mvc.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.mvc.processor.ScxMappingResultProcessor;
import cool.scx.util.ObjectUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;

public final class CanConvertJsonResultProcessor implements ScxMappingResultProcessor {

    @Override
    public boolean canHandle(Object result) {
        return true;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws JsonProcessingException {
        context.request().response()
                .putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8")
                .end(ObjectUtils.writeValueAsStringUseAnnotations(result));
    }

}
