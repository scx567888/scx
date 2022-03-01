package cool.scx.mvc.parameter_handler.impl;

import cool.scx.annotation.FromUpload;
import cool.scx.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.mvc.parameter_handler.ScxMappingRoutingContextInfo;
import cool.scx.type.UploadedEntity;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class UploadedEntityMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * a
     */
    public static final UploadedEntityMethodParameterHandler DEFAULT_INSTANCE = new UploadedEntityMethodParameterHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getParameterizedType() == UploadedEntity.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo context) throws RequiredParamEmptyException {
        var name = parameter.getName();
        var required = false;
        var fromUpload = parameter.getAnnotation(FromUpload.class);
        if (fromUpload != null) {
            if (StringUtils.isNotBlank(fromUpload.value())) {
                name = fromUpload.value();
            }
            required = fromUpload.required();
        }

        var v = context.uploadedEntityMap().get(name);
        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (required && v == null) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromUpload] , 参数类型 [" + parameter.getParameterizedType().getTypeName() + "]");
        }
        return v;
    }

}
