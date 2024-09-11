package cool.scx.web.return_value_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.http.ScxRoutingContext;

import static cool.scx.http.HttpFieldName.ACCEPT;
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
    public void handle(Object returnValue, ScxRoutingContext routingContext) throws JsonProcessingException {
        var accept = routingContext.request().headers().get(ACCEPT);
        if (accept != null && startsWithIgnoreCase(accept, APPLICATION_XML.toString())) {
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            fillXmlContentType(routingContext.request().response()).send(toXml(returnValue));
        } else { // 其余全部返回 json
            fillJsonContentType(routingContext.request().response()).send(toJson(returnValue));
        }
    }

}
