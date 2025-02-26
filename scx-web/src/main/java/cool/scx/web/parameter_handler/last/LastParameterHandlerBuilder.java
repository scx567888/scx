package cool.scx.web.parameter_handler.last;

import cool.scx.reflect.IParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * LastParameterHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class LastParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(IParameterInfo parameter) {
        return new LastParameterHandler(parameter);
    }

}
