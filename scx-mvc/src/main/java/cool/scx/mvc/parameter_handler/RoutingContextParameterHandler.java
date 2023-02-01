package cool.scx.mvc.parameter_handler;

import cool.scx.mvc.ScxMvcRequestInfo;
import cool.scx.mvc.ScxMvcParameterHandler;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;

/**
 * 类型为 RoutingContext 的参数处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class RoutingContextParameterHandler implements ScxMvcParameterHandler {

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
    public Object handle(Parameter paramInfo, ScxMvcRequestInfo routingContext) {
        return routingContext.routingContext();
    }

}
