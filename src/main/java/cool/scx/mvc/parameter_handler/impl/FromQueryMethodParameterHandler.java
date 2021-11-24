package cool.scx.mvc.parameter_handler.impl;

import cool.scx.annotation.FromQuery;
import cool.scx.mvc.parameter_handler.ParamConvertException;
import cool.scx.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.util.MapUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public final class FromQueryMethodParameterHandler implements ScxMappingMethodParameterHandler {

    public static final FromQueryMethodParameterHandler DEFAULT_INSTANCE = new FromQueryMethodParameterHandler();

    public static Object getValueFromQuery(String name, boolean merge, boolean required, Type javaType, RoutingContext routingContext) throws RequiredParamEmptyException, ParamConvertException {
        var queryParams = MapUtils.multiMapToMap(routingContext.queryParams());
        var v = merge ? queryParams : queryParams.get(name);
        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (required && v == null) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
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

    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromQuery.class) != null;
    }

    @Override
    public Object handle(Parameter parameter, RoutingContext context) throws Exception {
        var javaType = parameter.getParameterizedType();
        var required = false;
        var name = parameter.getName();
        var merge = false;

        var fromQuery = parameter.getAnnotation(FromQuery.class);
        if (fromQuery != null) {
            required = fromQuery.required();
            if (StringUtils.isNotBlank(fromQuery.value())) {
                name = fromQuery.value();
            }
            merge = fromQuery.merge();
        }
        return getValueFromQuery(name, merge, required, javaType, context);
    }

}
