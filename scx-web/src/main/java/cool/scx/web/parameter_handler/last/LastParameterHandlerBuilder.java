package cool.scx.web.parameter_handler.last;

import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * LastParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        return new LastParameterHandler(parameter);
    }

}
