package cool.scx.mvc.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.mvc.processor.ScxMappingResultProcessor;
import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class CanConvertJsonResultProcessor implements ScxMappingResultProcessor {

    @Override
    public boolean canHandle(Object result) {
        return true;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws JsonProcessingException {
        VoHelper.fillJsonContentType(context.request().response()).end(VoHelper.toJson(result));
    }

}
