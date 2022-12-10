package cool.scx.core.mvc.parameter_handler;

import cool.scx.core.mvc.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.ScxMappingRoutingContextInfo;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;

/**
 * 类型为 RoutingContext 的参数处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class RoutingContextMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * 默认实例
     */
    public static final RoutingContextMethodParameterHandler DEFAULT_INSTANCE = new RoutingContextMethodParameterHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getParameterizedType() == RoutingContext.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter paramInfo, ScxMappingRoutingContextInfo routingContext) {
        return routingContext.routingContext();
    }

}
