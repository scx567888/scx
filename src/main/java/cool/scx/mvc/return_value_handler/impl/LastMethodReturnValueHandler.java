package cool.scx.mvc.return_value_handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class LastMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    public static final LastMethodReturnValueHandler DEFAULT_INSTANCE = new LastMethodReturnValueHandler();

    @Override
    public boolean canHandle(Object result) {
        return true;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws JsonProcessingException {
        VoHelper.fillJsonContentType(context.request().response()).end(VoHelper.toJson(result));
    }

}
