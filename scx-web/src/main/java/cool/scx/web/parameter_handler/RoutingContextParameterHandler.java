package cool.scx.web.parameter_handler;

import cool.scx.reflect.ParameterInfo;
import cool.scx.http.routing.RoutingContext;

/**
 * 类型为 RoutingContext 的参数处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class RoutingContextParameterHandler implements ParameterHandler {

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.type().getRawClass() == RoutingContext.class;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) {
        return requestInfo.routingContext();
    }

}
