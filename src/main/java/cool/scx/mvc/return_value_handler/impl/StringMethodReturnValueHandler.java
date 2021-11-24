package cool.scx.mvc.return_value_handler.impl;

import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import cool.scx.util.ObjectUtils;
import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class StringMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    public static final StringMethodReturnValueHandler DEFAULT_INSTANCE = new StringMethodReturnValueHandler();

    @Override
    public boolean canHandle(Object o) {
        return o instanceof String;
    }

    @Override
    public void handle(Object result, RoutingContext context) {
        VoHelper.fillTextPlainContentType(context.request().response())
                .end(ObjectUtils.convertValue(result, String.class));
    }

}
