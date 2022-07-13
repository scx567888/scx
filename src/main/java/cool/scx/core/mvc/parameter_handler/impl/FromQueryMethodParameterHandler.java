package cool.scx.core.mvc.parameter_handler.impl;

import cool.scx.core.annotation.FromQuery;
import cool.scx.core.mvc.parameter_handler.ParamConvertException;
import cool.scx.core.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.core.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.parameter_handler.ScxMappingRoutingContextInfo;
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
     * @throws cool.scx.core.mvc.parameter_handler.RequiredParamEmptyException a
     * @throws cool.scx.core.mvc.parameter_handler.ParamConvertException       a
     */
    public static Object getValueFromQuery(String name, boolean merge, boolean required, Type javaType, ScxMappingRoutingContextInfo routingContext) throws RequiredParamEmptyException, ParamConvertException {
        var v = merge ? routingContext.queryParams() : routingContext.queryParams().get(name);
        if (v == null) {
            //为空的时候做两个处理 即必填则报错 非必填则返回 null
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }

        try {
            return ObjectUtils.convertValue(v, javaType);
        } catch (Exception e) {
            //和上方类似 针对是否是必填项进行不同的处理
            if (required) {
                throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
            }
        }
        return null;
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
