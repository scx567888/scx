package cool.scx.core.mvc.parameter_handler;

import cool.scx.core.annotation.FromQuery;
import cool.scx.core.mvc.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.ScxMappingRoutingContextInfo;
import cool.scx.core.mvc.exception.ParamConvertException;
import cool.scx.core.mvc.exception.RequiredParamEmptyException;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromQueryMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * a
     */
    public static final FromQueryMethodParameterHandler DEFAULT_INSTANCE = new FromQueryMethodParameterHandler();

    /**
     * a
     *
     * @param name           a
     * @param merge          a
     * @param required       a
     * @param javaType       a
     * @param routingContext a
     * @return a
     * @throws cool.scx.core.mvc.exception.RequiredParamEmptyException a
     * @throws cool.scx.core.mvc.exception.ParamConvertException       a
     */
    public static Object getValueFromQuery(String name, boolean merge, boolean required, Type javaType, ScxMappingRoutingContextInfo routingContext) throws RequiredParamEmptyException, ParamConvertException {
        var v = merge ? routingContext.queryParams() : routingContext.queryParams().get(name);
        // 为了提高性能这里提前做一次校验
        if (v == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = ObjectUtils.convertValue(v, javaType, ObjectUtils.Option.IGNORE_JSON_IGNORE);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromQuery.class) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo context) throws Exception {
        var javaType = parameter.getParameterizedType();
        var required = false;
        var name = parameter.getName();
        var merge = false;

        var fromQuery = parameter.getAnnotation(FromQuery.class);
        if (fromQuery != null) {
            required = fromQuery.required();
            if (StringUtils.notBlank(fromQuery.value())) {
                name = fromQuery.value();
            }
            merge = fromQuery.merge();
        }
        return getValueFromQuery(name, merge, required, javaType, context);
    }

}
