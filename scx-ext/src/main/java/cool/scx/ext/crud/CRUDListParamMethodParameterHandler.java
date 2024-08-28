package cool.scx.ext.crud;

import cool.scx.common.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;

import static cool.scx.web.parameter_handler.FromBodyParameterHandler.getValueFromBody;

/**
 * a
 *
 * @author scx567888
 * @version 1.10.8
 */
public final class CRUDListParamMethodParameterHandler implements ParameterHandler {

    /**
     * a
     */
    public static final CRUDListParamMethodParameterHandler DEFAULT_INSTANCE = new CRUDListParamMethodParameterHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.type().getRawClass() == CRUDListParam.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception {
        var javaType = parameter.type();
        var name = parameter.name();
        var required = false;
        var useAllBody = true;
        var crudListParam = getValueFromBody(name, useAllBody, required, javaType, requestInfo);
        //这里保证 方法上的 CRUDListParam 类型参数永远不为空
        return crudListParam != null ? crudListParam : new CRUDListParam();
    }

}
