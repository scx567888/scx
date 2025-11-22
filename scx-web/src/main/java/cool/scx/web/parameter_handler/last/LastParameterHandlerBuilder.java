package cool.scx.web.parameter_handler.last;

import dev.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/// LastParameterHandler
///
/// @author scx567888
/// @version 0.0.1
public final class LastParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        return new LastParameterHandler(parameter);
    }

}
