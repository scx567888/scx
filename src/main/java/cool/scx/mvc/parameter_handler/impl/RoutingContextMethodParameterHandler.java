package cool.scx.mvc.parameter_handler.impl;

import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;

/**
 * 类型为 RoutingContext 的参数处理器
 */
public final class RoutingContextMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * 默认实例
     */
    public static final RoutingContextMethodParameterHandler DEFAULT_INSTANCE = new RoutingContextMethodParameterHandler();

    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getParameterizedType() == RoutingContext.class;
    }

    @Override
    public Object handle(Parameter paramInfo, RoutingContext routingContext) {
        return routingContext;
    }

}
