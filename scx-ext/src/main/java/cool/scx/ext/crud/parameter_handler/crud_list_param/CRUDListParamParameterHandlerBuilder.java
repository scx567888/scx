package cool.scx.ext.crud.parameter_handler.crud_list_param;

import cool.scx.ext.crud.CRUDListParam;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

public class CRUDListParamParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        if (parameter.type().getRawClass() != CRUDListParam.class) {
            return null;
        }
        return new CRUDListParamParameterHandler(parameter);
    }

}
