package cool.scx.web.parameter_handler.from_context;

import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.routing.RoutingContext;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;
import cool.scx.web.parameter_handler.RequestInfo;

/// 类型为 基本 的参数处理器
///
/// @author scx567888
/// @version 0.0.1
public final class FromContextParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        var rawClass = parameter.type().getRawClass();
        if (rawClass == RoutingContext.class) {
            return RequestInfo::routingContext;
        }
        if (rawClass == ScxHttpServerRequest.class) {
            return (requestInfo) -> requestInfo.routingContext().request();
        }
        if (rawClass == ScxHttpServerResponse.class) {
            return (requestInfo) -> requestInfo.routingContext().response();
        }
        if (rawClass == ScxHttpHeaders.class) {
            return (requestInfo) -> requestInfo.routingContext().request().headers();
        }
        if (rawClass == ScxHttpBody.class) {
            return (requestInfo) -> requestInfo.routingContext().request().body();
        }
        if (rawClass == Cookies.class) {
            return (requestInfo) -> requestInfo.routingContext().request().cookies();
        }
        return null;
    }

}
