package cool.scx.web.parameter_handler.from_query;

import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromQuery;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

public class FromQueryParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        var fromQuery = parameter.parameter().getAnnotation(FromQuery.class);
        if (fromQuery == null) {
            return null;
        }
        return new FromQueryParameterHandler(fromQuery, parameter);
    }

}
