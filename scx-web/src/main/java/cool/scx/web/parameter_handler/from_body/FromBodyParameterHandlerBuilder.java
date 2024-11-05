package cool.scx.web.parameter_handler.from_body;

import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromBody;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * FromBodyParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromBodyParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuildParameterHandler(ParameterInfo parameter) {
        var fromBody = parameter.parameter().getAnnotation(FromBody.class);
        if (fromBody == null) {
            return null;
        }
        return new FromBodyParameterHandler(fromBody, parameter);
    }

}
