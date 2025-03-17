package cool.scx.web.return_value_handler;

import cool.scx.http.headers.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.http.media_type.MediaType.TEXT_PLAIN;
import static java.nio.charset.StandardCharsets.UTF_8;

/// String 类型处理器
///
/// @author scx567888
/// @version 0.0.1
public final class StringReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean canHandle(Object returnValue) {
        return returnValue instanceof String;
    }

    @Override
    public void handle(Object returnValue, RoutingContext routingContext) {
        if (returnValue instanceof String str) {
            routingContext.response()
                    .contentType(ContentType.of(TEXT_PLAIN).charset(UTF_8))
                    .send(str);
        } else {
            throw new IllegalArgumentException("参数不是 String 类型 !!! " + returnValue.getClass());
        }
    }

}
