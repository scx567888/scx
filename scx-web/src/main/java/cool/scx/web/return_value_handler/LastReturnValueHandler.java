package cool.scx.web.return_value_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.ext.web.RoutingContext;

import static cool.scx.common.standard.HttpFieldName.ACCEPT;
import static cool.scx.common.standard.MediaType.APPLICATION_XML;
import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.common.util.ObjectUtils.toXml;
import static cool.scx.common.util.StringUtils.startsWithIgnoreCase;
import static cool.scx.web.ScxWebHelper.fillJsonContentType;
import static cool.scx.web.ScxWebHelper.fillXmlContentType;

/**
 * 最后的 返回值处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return true;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) throws JsonProcessingException {
        var accept = routingContext.request().getHeader(ACCEPT.toString());
        if (accept != null && startsWithIgnoreCase(accept, APPLICATION_XML.toString())) {
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            fillXmlContentType(routingContext.request().response()).end(toXml(returnValue));
        } else { // 其余全部返回 json
            fillJsonContentType(routingContext.request().response()).end(toJson(returnValue));
        }
    }

}
