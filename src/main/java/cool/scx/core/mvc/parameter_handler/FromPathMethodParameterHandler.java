package cool.scx.core.mvc.parameter_handler;

import cool.scx.core.annotation.FromPath;
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
public final class FromPathMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * a
     */
    public static final FromPathMethodParameterHandler DEFAULT_INSTANCE = new FromPathMethodParameterHandler();

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
    public static Object getValueFromPath(String name, boolean merge, boolean required, Type javaType, ScxMappingRoutingContextInfo routingContext) throws RequiredParamEmptyException, ParamConvertException {
        var v = merge ? routingContext.routingContext().pathParams() : routingContext.routingContext().pathParams().get(name);
        // 为了提高性能这里提前做一次校验
        if (v == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = ObjectUtils.convertValue(v, javaType, ObjectUtils.Option.IGNORE_JSON_IGNORE);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromPath.class) != null;
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

        var fromPath = parameter.getAnnotation(FromPath.class);
        if (fromPath != null) {
            required = fromPath.required();
            if (StringUtils.notBlank(fromPath.value())) {
                name = fromPath.value();
            }
            merge = fromPath.merge();
        }
        return getValueFromPath(name, merge, required, javaType, context);
    }

}
