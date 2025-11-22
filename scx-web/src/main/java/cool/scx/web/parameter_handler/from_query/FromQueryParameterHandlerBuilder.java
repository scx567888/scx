package cool.scx.web.parameter_handler.from_query;

import dev.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromQuery;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/// 参数处理器
///
/// @author scx567888
/// @version 0.0.1
public class FromQueryParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        var fromQuery = parameter.findAnnotation(FromQuery.class);
        if (fromQuery == null) {
            return null;
        }
        return new FromQueryParameterHandler(fromQuery, parameter);
    }

}
