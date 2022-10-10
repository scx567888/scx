package cool.scx.core.mvc.parameter_handler;

import cool.scx.core.mvc.ScxMappingHandler;
import cool.scx.core.mvc.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.ScxMappingRoutingContextInfo;

import java.lang.reflect.Parameter;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxMappingHandlerParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * a
     */
    public static final ScxMappingHandlerParameterHandler DEFAULT_INSTANCE = new ScxMappingHandlerParameterHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getParameterizedType() == ScxMappingHandler.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo scxMappingRoutingContextInfo, ScxMappingHandler scxMappingHandler) throws Exception {
        return scxMappingHandler;
    }

}
