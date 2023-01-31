package cool.scx.mvc.return_value_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.mvc.ScxMappingMethodReturnValueHandler;
import cool.scx.mvc.vo.BaseVo;
import cool.scx.util.ObjectUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastMethodReturnValueHandler implements ScxMappingMethodReturnValueHandler {

    /**
     * a
     */
    public static final LastMethodReturnValueHandler DEFAULT_INSTANCE = new LastMethodReturnValueHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Object result) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Object result, RoutingContext context) throws JsonProcessingException {
        var accept = context.request().getHeader(HttpHeaderNames.ACCEPT);
        if (accept != null && accept.toLowerCase().startsWith(HttpHeaderValues.APPLICATION_XML.toString())) {
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            BaseVo.fillXmlContentType(context.request().response()).end(ObjectUtils.toXml(result));
        } else { // 其余全部返回 json
            BaseVo.fillJsonContentType(context.request().response()).end(ObjectUtils.toJson(result));
        }
    }

}
