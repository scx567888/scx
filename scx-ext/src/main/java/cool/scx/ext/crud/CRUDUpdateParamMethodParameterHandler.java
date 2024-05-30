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
public final class CRUDUpdateParamMethodParameterHandler implements ParameterHandler {

    /**
     * a
     */
    public static final CRUDUpdateParamMethodParameterHandler DEFAULT_INSTANCE = new CRUDUpdateParamMethodParameterHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.type().getRawClass() == CRUDUpdateParam.class;
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
        var crudUpdateParam = getValueFromBody(name, useAllBody, required, javaType, requestInfo);
        //这里保证 方法上的 CRUDUpdateParam 类型参数永远不为空
        return crudUpdateParam != null ? crudUpdateParam : new CRUDUpdateParam();
    }

}
