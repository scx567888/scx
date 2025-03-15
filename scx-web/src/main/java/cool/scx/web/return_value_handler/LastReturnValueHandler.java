package cool.scx.web.return_value_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.http.header.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.common.util.ObjectUtils.toXml;
import static cool.scx.common.util.StringUtils.startsWithIgnoreCase;
import static cool.scx.http.HttpFieldName.ACCEPT;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static cool.scx.http.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

/// 兜底 返回值处理器
///
/// @author scx567888
/// @version 0.0.1
public final class LastReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return true;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) throws JsonProcessingException {
        var accept = routingContext.request().getHeader(ACCEPT);
        if (accept != null && startsWithIgnoreCase(accept, APPLICATION_XML.value())) {
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            routingContext.response()
                    .contentType(ContentType.of(APPLICATION_XML).charset(UTF_8))
                    .send(toXml(returnValue));
        } else { // 其余全部返回 json
            routingContext.request().response()
                    .contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8))
                    .send(toJson(returnValue));
        }
    }

}
