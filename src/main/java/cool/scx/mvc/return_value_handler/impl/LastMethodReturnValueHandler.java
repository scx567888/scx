package cool.scx.mvc.return_value_handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import cool.scx.vo.VoHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class LastMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    /**
     * a
     */
    public static final LastMethodReturnValueHandler DEFAULT_INSTANCE = new LastMethodReturnValueHandler();

    @Override
    public boolean canHandle(Object result) {
        return true;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws JsonProcessingException {
        var accept = context.request().getHeader(HttpHeaderNames.ACCEPT);
        if (accept != null && accept.toLowerCase().startsWith(HttpHeaderValues.APPLICATION_XML.toString())) {
            VoHelper.fillXmlContentType(context.request().response()).end(VoHelper.toXml(result));
        } else {
            VoHelper.fillJsonContentType(context.request().response()).end(VoHelper.toJson(result));
        }
    }

}
