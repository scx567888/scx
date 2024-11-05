package cool.scx.web.parameter_handler.from_path;

import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromPath;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * FromPathParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromPathParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuildParameterHandler(ParameterInfo parameter) {
        var fromPath = parameter.parameter().getAnnotation(FromPath.class);
        if (fromPath == null) {
            return null;
        }
        return new FromPathParameterHandler(fromPath, parameter);
    }

}